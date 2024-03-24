package ru.teterin.rentalapp.repo.sql

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.teterin.rentalapp.model.helpers.asRentalError
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.repo.DbAdFilterRequest
import ru.teterin.rentalapp.model.repo.DbAdIdRequest
import ru.teterin.rentalapp.model.repo.DbAdRequest
import ru.teterin.rentalapp.model.repo.DbAdResponse
import ru.teterin.rentalapp.model.repo.DbAdsResponse
import ru.teterin.rentalapp.model.repo.IAdRepository
import java.util.UUID.*

class RepoAdSQL(
    properties: SqlProperties,
    initObjects: Collection<RentalAd> = emptyList(),
    val randomUuid: () -> String = { randomUUID().toString() },
) : IAdRepository {
    private val adTable = AdTable(properties.table)

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    init {
        transaction(conn) {
            SchemaUtils.create(adTable)
            initObjects.forEach { createAd(it) }
        }
    }

    private fun createAd(ad: RentalAd): RentalAd {
        val res = adTable
            .insert {
                to(it, ad, randomUuid)
            }
            .resultedValues
            ?.map { adTable.from(it) }
        return res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction(conn) {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbAdResponse): DbAdResponse =
        transactionWrapper(block) { DbAdResponse.error(it.asRentalError()) }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse = transactionWrapper {
        DbAdResponse.success(createAd(rq.ad))
    }

    private fun read(id: RentalAdId): DbAdResponse {
        val res = adTable.select {
            adTable.id eq id.asString()
        }.singleOrNull() ?: return DbAdResponse.errorNotFound
        return DbAdResponse.success(adTable.from(res))
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse = transactionWrapper { read(rq.id) }

    private fun update(
        id: RentalAdId,
        lock: RentalAdLock,
        block: (RentalAd) -> DbAdResponse,
    ): DbAdResponse =
        transactionWrapper {
            if(id == RentalAdId.NONE) return@transactionWrapper DbAdResponse.errorEmptyId

            val current = adTable.select { adTable.id eq id.asString() }
                .singleOrNull()
                ?.let { adTable.from(it) }
            when {
                current == null -> DbAdResponse.errorNotFound
                current.lock != lock -> DbAdResponse.errorConcurrent(lock, current)
                else -> block(current)
            }
        }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse = update(rq.ad.id, rq.ad.lock) {
        adTable.update({ adTable.id eq rq.ad.id.asString() }) {
            to(it, rq.ad.copy(lock = RentalAdLock(randomUuid())), randomUuid)
        }
        read(rq.ad.id)
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse = update(rq.id, rq.lock) {
        adTable.deleteWhere { id eq rq.id.asString() }
        DbAdResponse.success(it)
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse =
        transactionWrapper({
            val res = adTable.select {
                buildList {
                    add(Op.TRUE)
                    if(rq.ownerId != RentalUserId.NONE) {
                        add(adTable.owner eq rq.ownerId.asString())
                    }
                    if(rq.titleFilter.isNotBlank()) {
                        add(
                            (adTable.title like "%${rq.titleFilter}%")
                                or (adTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b}
            }
            DbAdsResponse(data = res.map { adTable.from(it) }, isSuccess = true)
        }, {
            DbAdsResponse.error(it.asRentalError())
        })
}

package ru.teterin.rentalapp.repo.inmemory

import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalError
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.repo.DbAdFilterRequest
import ru.teterin.rentalapp.model.repo.DbAdIdRequest
import ru.teterin.rentalapp.model.repo.DbAdRequest
import ru.teterin.rentalapp.model.repo.DbAdResponse
import ru.teterin.rentalapp.model.repo.DbAdsResponse
import ru.teterin.rentalapp.model.repo.IAdRepository
import ru.teterin.rentalapp.repo.inmemory.model.AdEntity
import java.util.UUID.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class AdRepoInMemory(
    initObjects: Collection<RentalAd> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { randomUUID().toString() },
) : IAdRepository {

    private val cache = Cache.Builder<String, AdEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(ad: RentalAd) {
        val entity = AdEntity(ad)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        val key = randomUuid()
        val ad = rq.ad.copy(id = RentalAdId(key), lock = RentalAdLock(randomUuid()))
        val entity = AdEntity(ad)
        cache.put(key, entity)
        return DbAdResponse(
            data = ad,
            isSuccess = true,
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        val key = rq.id.takeIf { it != RentalAdId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbAdResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    private suspend fun doUpdate(
        id: RentalAdId,
        oldLock: RentalAdLock,
        okBlock: (key: String, oldAd: AdEntity) -> DbAdResponse
    ): DbAdResponse {
        val key = id.takeIf { it != RentalAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLockStr = oldLock.takeIf { it != RentalAdLock.NONE }?.asString()
            ?: return resultErrorEmptyLock

        return mutex.withLock {
            val oldAd = cache.get(key)
            when {
                oldAd == null -> resultErrorNotFound
                oldAd.lock != oldLockStr -> DbAdResponse.errorConcurrent(
                    oldLock,
                    oldAd.toInternal()
                )

                else -> okBlock(key, oldAd)
            }
        }
    }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse =
        doUpdate(rq.ad.id, rq.ad.lock) { key, _ ->
            val newAd = rq.ad.copy(lock = RentalAdLock(randomUuid()))
            val entity = AdEntity(newAd)
            cache.put(key, entity)
            DbAdResponse.success(newAd)
        }


    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse =
        doUpdate(rq.id, rq.lock) { key, oldAd ->
            cache.invalidate(key)
            DbAdResponse.success(oldAd.toInternal())
        }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != RentalUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbAdsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbAdResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                RentalError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorEmptyLock = DbAdResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                RentalError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
        val resultErrorNotFound = DbAdResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                RentalError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}

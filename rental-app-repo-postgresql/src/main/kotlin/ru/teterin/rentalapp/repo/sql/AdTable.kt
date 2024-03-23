package ru.teterin.rentalapp.repo.sql

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility

class AdTable(tableName: String = "ad") : Table(tableName) {
    val id = varchar("id", 36)
    val title = varchar("title", 128)
    val description = text("description")
    val rentDates = text("rentDates")
    val issueTimes = text("issueTimes")
    val owner = varchar("owner", 128)
    val visibility = enumeration("visibility", RentalVisibility::class)
    val lock = varchar("lock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = RentalAd(
        id = RentalAdId(res[id].toString()),
        title = res[title],
        description = res[description],
        timeParam = RentalTimeParam(
            if (res[rentDates].toString().isBlank()) {
                emptyList()
            }
            else {
                res[rentDates].toString()
                    .replace("[\\[\\] ]".toRegex(),"")
                    .split(",")
                    .stream()
                    .map { it -> LocalDate.parse(it) }
                    .toList()
            },
            if (res[issueTimes].toString().isBlank()) {
                emptyList()
            } else {
                res[issueTimes].toString()
                    .replace("[\\[\\] ]".toRegex(),"")
                    .split(",")
                    .stream()
                    .map {it -> Instant.fromEpochSeconds(it.toLong()) }
                    .toList()
            },
        ),
        ownerId = RentalUserId(res[owner].toString()),
        visibility = res[visibility],
        lock = RentalAdLock(res[lock]),
    )

    fun to(it: UpdateBuilder<*>, ad: RentalAd, randomUuid: () -> String) {
        it[id] = ad.id.takeIf { it != RentalAdId.NONE }?.asString() ?: randomUuid()
        it[title] = ad.title
        it[description] = ad.description
        val dates = ad.timeParam.rentDates
        it[rentDates] = if (dates.isEmpty()) "" else dates.toString()
        val times = ad.timeParam.issueTimes
        it[issueTimes] = if (times.isEmpty()) "" else times.map {t -> t.epochSeconds}.toString()
        it[owner] = ad.ownerId.asString()
        it[visibility] = ad.visibility
        it[lock] = ad.lock.takeIf { it != RentalAdLock.NONE }?.asString() ?: randomUuid()
    }

}

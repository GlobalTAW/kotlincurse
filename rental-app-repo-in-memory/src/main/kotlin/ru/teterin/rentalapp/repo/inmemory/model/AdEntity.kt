package ru.teterin.rentalapp.repo.inmemory.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility

data class AdEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val rentDates: String = "",
    val issueTimes: String = "",
    val ownerId: String? = null,
    val adType: String? = null,
    val visibility: String? = null,
    val lock: String? = null,
) {
    constructor(model: RentalAd): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        rentDates = if (model.timeParam.rentDates.isEmpty()) "" else model.timeParam.rentDates.toString(),
        issueTimes = if (model.timeParam.issueTimes.isEmpty()) "" else model.timeParam.issueTimes.map {t -> t.epochSeconds}.toString(),
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        visibility = model.visibility.takeIf { it != RentalVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() },
    )

    fun toInternal() = RentalAd(
        id = id?.let { RentalAdId(it) }?: RentalAdId.NONE,
        title = title?: "",
        description = description?: "",
        timeParam = RentalTimeParam(
            if (rentDates.isBlank()) {
                emptyList()
            }
            else {
                rentDates
                    .replace("[\\[\\] ]".toRegex(),"")
                    .split(",")
                    .stream()
                    .map { it -> LocalDate.parse(it) }
                    .toList()
            },
            if (issueTimes.isBlank()) {
                emptyList()
            } else {
                issueTimes
                    .replace("[\\[\\] ]".toRegex(),"")
                    .split(",")
                    .stream()
                    .map {it -> Instant.fromEpochSeconds(it.toLong()) }
                    .toList()
            },
        ),
        ownerId = ownerId?.let { RentalUserId(it) }?: RentalUserId.NONE,
        visibility = visibility?.let { RentalVisibility.valueOf(it) }?: RentalVisibility.NONE,
        lock = lock?.let { RentalAdLock(it) }?: RentalAdLock.NONE,
    )

}

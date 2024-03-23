package ru.teterin.rentalapp.repo.test

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility

abstract class BaseInitAds(val op: String): IInitObjects<RentalAd> {
    open val lockOld: RentalAdLock = RentalAdLock("11111111-1111-1111-1111-111111111111")
    open val lockBad: RentalAdLock = RentalAdLock("22222222-2222-2222-2222-222222222222")

    fun createInitTestModel(
        suf: String,
        ownerId: RentalUserId = RentalUserId("515307e6-fbae-441a-91e4-afd5652064c0"),
        lock: RentalAdLock = lockOld,
    ) = RentalAd(
        id = RentalAdId("ad-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        timeParam = RentalTimeParam(
            rentDates = arrayListOf(LocalDate(2023, 12, 21)),
            issueTimes = arrayListOf(Instant.fromEpochSeconds(1703170800)),
        ),
        ownerId = ownerId,
        visibility = RentalVisibility.VISIBLE_TO_OWNER,
        lock = lock,
    )
}

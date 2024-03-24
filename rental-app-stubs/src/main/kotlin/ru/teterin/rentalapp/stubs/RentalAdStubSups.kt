package ru.teterin.rentalapp.stubs

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import ru.teterin.rentalapp.model.models.*

object RentalAdStubSups {
    val AD_SUP1: RentalAd
        get() = RentalAd(
            id = RentalAdId("8ae15f5e-9073-47b6-bea2-c4e395f156fd"),
            title = "Прокат Сап Борда",
            description = "Прокат Сап Борда HIPER Динозавр, 305х84",
            timeParam = RentalTimeParam(
                rentDates = listOf(LocalDate(2023, 12, 21)),
                issueTimes = listOf(Instant.fromEpochSeconds(1703170800)),
            ),
            ownerId = RentalUserId("6ec2039d-e417-4e6b-812a-bdfa501c81a2"),
            visibility = RentalVisibility.VISIBLE_PUBLIC,
            permissionsClient = mutableSetOf(
                RentalAdPermissionClient.READ,
                RentalAdPermissionClient.UPDATE,
                RentalAdPermissionClient.DELETE,
                RentalAdPermissionClient.MAKE_VISIBLE_PUBLIC,
                RentalAdPermissionClient.MAKE_VISIBLE_GROUP,
                RentalAdPermissionClient.MAKE_VISIBLE_OWNER,
            )
        )
}

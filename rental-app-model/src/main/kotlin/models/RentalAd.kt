package ru.teterin.rentalapp.model.models

data class RentalAd(
    var id: RentalAdId = RentalAdId.NONE,
    var title: String = "",
    var description: String = "",
    var timeParam: RentalTimeParam = RentalTimeParam(),
    var ownerId: RentalUserId = RentalUserId.NONE,
    var lock: RentalAdLock = RentalAdLock.NONE,
    var visibility: RentalVisibility = RentalVisibility.NONE,
    var productId: RentalProductId = RentalProductId.NONE,
    val permissionsClient: MutableSet<RentalAdPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): RentalAd = copy(
        permissionsClient = permissionsClient.toMutableSet(),
        timeParam = RentalTimeParam(
            timeParam.rentDates.toMutableList(),
            timeParam.issueTimes.toMutableList()
        ),
    )

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = RentalAd()
    }

}

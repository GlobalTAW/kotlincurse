package ru.teterin.rentalapp.model.repo

import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock

data class DbAdIdRequest(
    val id: RentalAdId,
    val lock: RentalAdLock = RentalAdLock.NONE
) {
    constructor(ad: RentalAd): this(ad.id, ad.lock)
}

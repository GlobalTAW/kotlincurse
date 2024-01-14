package ru.teterin.rentalapp.stubs

import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.stubs.RentalAdStubSups.AD_SUP1

object RentalAdStub {

    fun get(): RentalAd = AD_SUP1.copy()

}

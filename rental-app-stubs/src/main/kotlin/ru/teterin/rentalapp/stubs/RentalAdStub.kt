package ru.teterin.rentalapp.stubs

import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.stubs.RentalAdStubSups.AD_SUP1

object RentalAdStub {

    fun get(): RentalAd = AD_SUP1.copy()

    fun prepareResult(block: RentalAd.() -> Unit): RentalAd = get().apply(block)

    fun prepareSearchResult(filter: String) = listOf(
        get(),
        get(),
    )

}

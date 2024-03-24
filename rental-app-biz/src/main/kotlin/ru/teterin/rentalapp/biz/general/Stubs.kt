package ru.teterin.rentalapp.biz.general

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalWorkMode

fun ICorChainDsl<RentalContext>.stubs(title: String, block: ICorChainDsl<RentalContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == RentalWorkMode.STUB && state == RentalState.RUNNING}
}

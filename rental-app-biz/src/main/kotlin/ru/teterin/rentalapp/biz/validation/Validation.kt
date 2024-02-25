package ru.teterin.rentalapp.biz.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.validation(block: ICorChainDsl<RentalContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == RentalState.RUNNING }
}

package ru.teterin.rentalapp.biz.workers

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == RentalState.NONE }
    handle { state = RentalState.RUNNING}
}

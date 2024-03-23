package ru.teterin.rentalapp.biz.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == RentalState.RUNNING }
    handle {
        adValidated = adValidating.deepCopy()
    }
}

fun ICorChainDsl<RentalContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == RentalState.RUNNING }
    handle {
        adFilterValidated = adFilterValidating
    }
}

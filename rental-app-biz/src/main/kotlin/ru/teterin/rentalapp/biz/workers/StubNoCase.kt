package ru.teterin.rentalapp.biz.workers

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.helpers.fail
import ru.teterin.rentalapp.model.models.RentalError
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == RentalState.RUNNING }
    handle {
        fail(
            RentalError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}

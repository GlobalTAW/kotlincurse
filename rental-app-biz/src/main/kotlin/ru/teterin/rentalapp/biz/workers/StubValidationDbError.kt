package ru.teterin.rentalapp.biz.workers

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalError
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.stubs.RentalStubs

fun ICorChainDsl<RentalContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == RentalStubs.DB_ERROR && state == RentalState.RUNNING }
    handle {
        state = RentalState.FAILING
        this.errors.add(
            RentalError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}

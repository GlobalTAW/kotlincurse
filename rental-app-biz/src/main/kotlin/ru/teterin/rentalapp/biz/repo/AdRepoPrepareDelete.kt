package ru.teterin.rentalapp.biz.repo

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные к удалению из БД"
    on { state == RentalState.RUNNING }
    handle {
        adRepoPrepare = adValidated.deepCopy()
    }
}

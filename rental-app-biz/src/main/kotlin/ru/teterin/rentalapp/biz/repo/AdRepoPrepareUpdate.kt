package ru.teterin.rentalapp.biz.repo

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к обновлению в БД"
    on { state == RentalState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.deepCopy().apply {
            this.title = adValidated.title
            description = adValidated.description
            visibility = adValidated.visibility
            timeParam.issueTimes = adValidated.timeParam.issueTimes
        }
    }
}

package ru.teterin.rentalapp.biz.repo

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.repoPrepareBook(title: String) = worker {
    this.title = title
    description = "Готовим данные по бронированию для обновления в БД"
    on { state == RentalState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.deepCopy().apply {
            timeParam.rentDates = adRepoRead.timeParam.rentDates.minus(adValidated.timeParam.rentDates)
            timeParam.issueTimes = adRepoRead.timeParam.issueTimes.minus(adValidated.timeParam.issueTimes)
        }
    }
}

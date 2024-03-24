package ru.teterin.rentalapp.biz.repo

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.repo.DbAdIdRequest

fun ICorChainDsl<RentalContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == RentalState.RUNNING }
    handle {
        val request = DbAdIdRequest(adValidated)
        val result = adRepo.readAd(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            adRepoRead = resultAd
        } else {
            state = RentalState.FAILING
            errors.addAll(result.errors)
        }
    }
}

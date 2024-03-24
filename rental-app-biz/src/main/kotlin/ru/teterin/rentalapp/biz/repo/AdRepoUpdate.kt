package ru.teterin.rentalapp.biz.repo

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.repo.DbAdRequest

fun ICorChainDsl<RentalContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == RentalState.RUNNING }
    handle {
        val request = DbAdRequest(adRepoPrepare)
        val result = adRepo.updateAd(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            adRepoDone = resultAd
        } else {
            state = RentalState.FAILING
            errors.addAll(result.errors)
            adRepoDone
        }
    }
}

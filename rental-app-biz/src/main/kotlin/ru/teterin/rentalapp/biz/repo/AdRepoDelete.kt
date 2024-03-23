package ru.teterin.rentalapp.biz.repo

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.repo.DbAdIdRequest

fun ICorChainDsl<RentalContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == RentalState.RUNNING }
    handle {
        val request = DbAdIdRequest(adRepoPrepare)
        val result = adRepo.deleteAd(request)
        if (!result.isSuccess) {
            state = RentalState.FAILING
            errors.addAll(result.errors)
        }
        adRepoDone = adRepoRead
    }
}

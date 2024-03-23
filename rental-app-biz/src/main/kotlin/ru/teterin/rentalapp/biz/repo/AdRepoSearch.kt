package ru.teterin.rentalapp.biz.repo

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.repo.DbAdFilterRequest

fun ICorChainDsl<RentalContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == RentalState.RUNNING }
    handle {
        val request = DbAdFilterRequest(
            titleFilter = adFilterValidated.searchString,
            ownerId = adFilterValidated.ownerId,
            //TODO add timeParam
        )
        val result = adRepo.searchAd(request)
        val resultAds = result.data
        if (result.isSuccess && resultAds != null) {
            //TODO check resultAds.notEmpty
            adsRepoDone = resultAds.toMutableList()
        } else {
            state = RentalState.FAILING
            errors.addAll(result.errors)
        }
    }
}

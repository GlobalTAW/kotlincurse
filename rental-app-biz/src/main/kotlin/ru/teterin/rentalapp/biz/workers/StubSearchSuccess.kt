package ru.teterin.rentalapp.biz.workers

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.stubs.RentalStubs
import ru.teterin.rentalapp.stubs.RentalAdStub

fun ICorChainDsl<RentalContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == RentalStubs.SUCCESS && state == RentalState.RUNNING }
    handle {
        state = RentalState.FINISHING
        adsResponse.addAll(RentalAdStub.prepareSearchResult(adFilterRequest.searchString))
    }
}

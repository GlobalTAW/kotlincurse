package ru.teterin.rentalapp.biz.workers

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.stubs.RentalStubs
import ru.teterin.rentalapp.stubs.RentalAdStub

fun ICorChainDsl<RentalContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == RentalStubs.SUCCESS && state == RentalState.RUNNING }
    handle {
        state = RentalState.FINISHING
        val stub = RentalAdStub.prepareResult {
            adRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            adRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            adRequest.visibility.takeIf { it != RentalVisibility.NONE }?.also { this.visibility = it }
        }
        adResponse = stub
    }
}

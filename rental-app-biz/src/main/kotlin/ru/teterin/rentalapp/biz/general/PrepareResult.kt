package ru.teterin.rentalapp.biz.general

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalWorkMode

fun ICorChainDsl<RentalContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != RentalWorkMode.STUB }
    handle {
        adResponse = adRepoDone
        adsResponse = adsRepoDone
        state = when (val st = state) {
            RentalState.RUNNING -> RentalState.FINISHING
            else -> st
        }
    }
}

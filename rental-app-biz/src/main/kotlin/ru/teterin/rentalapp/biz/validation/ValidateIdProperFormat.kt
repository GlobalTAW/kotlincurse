package ru.teterin.rentalapp.biz.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.helpers.errorValidation
import ru.teterin.rentalapp.model.helpers.fail
import ru.teterin.rentalapp.model.models.RentalAdId

fun ICorChainDsl<RentalContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regEx = Regex("^[0-9a-f-]+$")
    on { adValidating.id != RentalAdId.NONE && !adValidating.id.asString().matches(regEx) }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value must match UUID format"
            )
        )
    }
}

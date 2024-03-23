package ru.teterin.rentalapp.biz.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.helpers.errorValidation
import ru.teterin.rentalapp.model.helpers.fail

fun ICorChainDsl<RentalContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.lock.asString().isBlank()}
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}

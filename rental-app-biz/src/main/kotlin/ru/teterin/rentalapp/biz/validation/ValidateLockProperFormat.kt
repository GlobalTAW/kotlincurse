package ru.teterin.rentalapp.biz.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.helpers.errorValidation
import ru.teterin.rentalapp.model.helpers.fail
import ru.teterin.rentalapp.model.models.RentalAdLock

fun ICorChainDsl<RentalContext>.validateLockProperFormat(title: String) = worker {
    this.title = title
    val regEx = Regex("^[0-9a-f-]+$")
    on { adValidating.lock != RentalAdLock.NONE && !adValidating.lock.asString().matches(regEx) }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value must match UUID format"
            )
        )
    }
}

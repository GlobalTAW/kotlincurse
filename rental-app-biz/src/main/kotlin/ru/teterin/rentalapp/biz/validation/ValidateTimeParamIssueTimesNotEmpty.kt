package ru.teterin.rentalapp.biz.validation

import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.worker
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.helpers.errorValidation
import ru.teterin.rentalapp.model.helpers.fail

fun ICorChainDsl<RentalContext>.validateTimeParamIssueTimesNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.timeParam.issueTimes.isEmpty()}
    handle {
        fail(
            errorValidation(
                field = "timeParam.issueTimes",
                violationCode = "empty",
                description = "Issue times must not be empty"
            )
        )
    }
}

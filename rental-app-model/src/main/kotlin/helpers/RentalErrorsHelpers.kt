package ru.teterin.rentalapp.model.helpers

import ru.teterin.rentalapp.logging.LogLevel
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalError
import ru.teterin.rentalapp.model.models.RentalState

fun Throwable.asRentalError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = RentalError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun RentalContext.addError(vararg error: RentalError) = errors.addAll(error)

fun RentalContext.fail(error: RentalError) {
    addError(error)
    state = RentalState.FAILING
}

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = RentalError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

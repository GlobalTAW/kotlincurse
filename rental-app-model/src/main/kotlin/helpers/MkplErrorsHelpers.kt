package ru.teterin.rentalapp.model.helpers

import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalError

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

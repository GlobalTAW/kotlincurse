package ru.teterin.rentalapp.model.helpers

import ru.teterin.rentalapp.logging.LogLevel
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.exceptions.RepoConcurrencyException
import ru.teterin.rentalapp.model.models.RentalAdLock
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

fun errorAdministration(
    field: String = "",
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
    exception: Exception? = null,
) = RentalError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
    exception = exception,
)

fun errorRepoConcurrency(
    expectedLock: RentalAdLock,
    actualLock: RentalAdLock?,
    exception: Exception? = null,
) = RentalError(
    field = "lock",
    code = "concurrency",
    group = "repo",
    message = "The object has been changed concurrently by another user or process",
    exception = exception ?: RepoConcurrencyException(expectedLock, actualLock),
)

val errorNotFound = RentalError(
    field = "id",
    message = "Not found",
    code = "not-found",
)

val errorEmptyId = RentalError(
    field = "id",
    message = "Id must not be null or blank",
    code = "empty",
)

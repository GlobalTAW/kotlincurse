package ru.teterin.rentalapp.model.models

import ru.teterin.rentalapp.logging.LogLevel

data class RentalError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)

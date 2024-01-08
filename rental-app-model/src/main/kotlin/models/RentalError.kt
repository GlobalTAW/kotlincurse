package ru.teterin.rentalapp.model.models

data class RentalError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)

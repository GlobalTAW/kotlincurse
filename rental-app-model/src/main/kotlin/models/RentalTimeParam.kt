package ru.teterin.rentalapp.model.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class RentalTimeParam(
    val rentDates: List<LocalDate> = arrayListOf(),
    val issueTimes: List<Instant> = arrayListOf()
)
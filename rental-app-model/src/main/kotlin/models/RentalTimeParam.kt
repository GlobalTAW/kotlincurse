package ru.teterin.rentalapp.model.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class RentalTimeParam(
    var rentDates: List<LocalDate> = arrayListOf(),
    var issueTimes: List<Instant> = arrayListOf()
)
package ru.teterin.rentalapp.model

import kotlinx.datetime.Instant
import ru.teterin.rentalapp.model.models.*
import ru.teterin.rentalapp.model.stubs.RentalStubs

data class RentalContext(
    var command: RentalCommand = RentalCommand.NONE,
    var state: RentalState = RentalState.NONE,
    val errors: MutableList<RentalError> = mutableListOf(),
    var corSettings: RentalCorSettings = RentalCorSettings(),

    var workMode: RentalWorkMode = RentalWorkMode.PROD,
    var stubCase: RentalStubs = RentalStubs.NONE,

    var requestId: RentalRequestId = RentalRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: RentalAd = RentalAd(),
    var adFilterRequest: RentalAdFilter = RentalAdFilter(),

    var adValidating: RentalAd = RentalAd(),
    var adFilterValidating: RentalAdFilter = RentalAdFilter(),

    var adValidated: RentalAd = RentalAd(),
    var adFilterValidated: RentalAdFilter = RentalAdFilter(),

    var adResponse: RentalAd = RentalAd(),
    var adsResponse: MutableList<RentalAd> = mutableListOf(),
)

package ru.teterin.rentalapp.common

import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalCorSettings

interface IRentalAppSettings {
    val processor: RentalAdProcessor
    val corSettings: RentalCorSettings
}

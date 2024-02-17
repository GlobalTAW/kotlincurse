package ru.teterin.rentalapp.springapp.models

import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.common.IRentalAppSettings
import ru.teterin.rentalapp.model.RentalCorSettings

data class RentalAppSettings(
    override val processor: RentalAdProcessor,
    override val corSettings: RentalCorSettings,
): IRentalAppSettings

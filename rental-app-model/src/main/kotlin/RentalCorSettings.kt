package ru.teterin.rentalapp.model

import ru.teterin.rentalapp.logging.RentalLoggerProvider

data class RentalCorSettings(
    val loggerProvider: RentalLoggerProvider = RentalLoggerProvider(),
) {
    companion object{
        val NONE = RentalCorSettings()
    }
}

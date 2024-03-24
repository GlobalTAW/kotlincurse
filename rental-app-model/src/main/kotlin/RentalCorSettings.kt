package ru.teterin.rentalapp.model

import ru.teterin.rentalapp.logging.RentalLoggerProvider
import ru.teterin.rentalapp.model.repo.IAdRepository

data class RentalCorSettings(
    val loggerProvider: RentalLoggerProvider = RentalLoggerProvider(),
    val repoProd: IAdRepository = IAdRepository.NONE,
    val repoStub: IAdRepository = IAdRepository.NONE,
    var repoTest: IAdRepository = IAdRepository.NONE,
) {
    companion object{
        val NONE = RentalCorSettings()
    }
}

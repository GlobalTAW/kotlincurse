package ru.teterin.rentalapp.biz.validation

import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.repo.stub.AdRepoStub

class BizValidationDeleteTest {

    private val command = RentalCommand.DELETE
    private val settings by lazy {
        RentalCorSettings(
            repoTest = AdRepoStub(),
        )
    }
    private val processor by lazy { RentalAdProcessor(settings) }

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badId() = validationIdFormat(command, processor)

    @Test
    fun correctLock() = validationLockCorrect(command, processor)
    @Test
    fun emptyLock() = validationLockEmpty(command, processor)
    fun badLock() = validationLockFormat(command, processor)

}

package ru.teterin.rentalapp.biz.validation

import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.models.RentalCommand

class BizValidationReadTest {

    private val command = RentalCommand.READ
    private val processor by lazy { RentalAdProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badId() = validationIdFormat(command, processor)

}

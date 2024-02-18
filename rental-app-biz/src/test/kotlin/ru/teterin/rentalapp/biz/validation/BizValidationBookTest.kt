package ru.teterin.rentalapp.biz.validation

import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.models.RentalCommand

class BizValidationBookTest {

    private val command = RentalCommand.BOOK
    private val processor by lazy { RentalAdProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badId() = validationIdFormat(command, processor)

    @Test
    fun correctTimeParam() = validationTimeParamCorrect(command, processor)
    @Test
    fun emptyTimeParamRentDates() = validationTimeParamRentDatesEmpty(command, processor)
    @Test
    fun emptyTimeParamIssueDates() = validationTimeParamIssueTimesEmpty(command, processor)

}

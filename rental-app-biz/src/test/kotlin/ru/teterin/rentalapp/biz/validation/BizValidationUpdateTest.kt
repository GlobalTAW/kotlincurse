package ru.teterin.rentalapp.biz.validation

import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.models.RentalCommand

class BizValidationUpdateTest {

    private val command = RentalCommand.UPDATE
    private val processor by lazy { RentalAdProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)
    @Test
    fun emptyId() = validationIdEmpty(command, processor)
    @Test
    fun badId() = validationIdFormat(command, processor)

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)
    @Test
    fun trimTitle() = validationTitleTrim(command, processor)
    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test
    fun badSymbolsTitle() = validationTitleWhiteSpace(command, processor)

    @Test
    fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test
    fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test
    fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test
    fun badSymbolsDescription() = validationDescriptionWhiteSpace(command, processor)

    @Test
    fun correctTimeParam() = validationTimeParamCorrect(command, processor)
    @Test
    fun emptyTimeParamRentDates() = validationTimeParamRentDatesEmpty(command, processor)
    @Test
    fun emptyTimeParamIssueDates() = validationTimeParamIssueTimesEmpty(command, processor)

}

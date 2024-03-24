package ru.teterin.rentalapp.biz.validation

import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.repo.stub.AdRepoStub

class BizValidationUpdateTest {

    private val command = RentalCommand.UPDATE
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

    @Test
    fun correctLock() = validationLockCorrect(command, processor)
    @Test
    fun emptyLock() = validationLockEmpty(command, processor)
    fun badLock() = validationLockFormat(command, processor)

}

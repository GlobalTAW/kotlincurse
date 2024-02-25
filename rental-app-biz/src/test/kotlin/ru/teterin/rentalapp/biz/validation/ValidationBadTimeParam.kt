package ru.teterin.rentalapp.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState.FAILING
import ru.teterin.rentalapp.model.models.RentalState.NONE
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalVisibility.VISIBLE_PUBLIC
import ru.teterin.rentalapp.model.models.RentalWorkMode.TEST
import ru.teterin.rentalapp.stubs.RentalAdStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = RentalAdStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTimeParamCorrect(command: RentalCommand, processor: RentalAdProcessor) = runTest {
    val ctx = RentalContext(
        command = command,
        state = NONE,
        workMode = TEST,
        adRequest = RentalAd(
            id = stub.id,
            title = "abc",
            description = "abc",
            timeParam = RentalTimeParam(
                rentDates = listOf(LocalDate(2023, 12, 21)),
                issueTimes = listOf(Instant.fromEpochSeconds(1703170800))
            ),
            visibility = VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(FAILING, ctx.state)
    assertEquals("abc", ctx.adValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTimeParamRentDatesEmpty(command: RentalCommand, processor: RentalAdProcessor) = runTest {
    val ctx = RentalContext(
        command = command,
        state = NONE,
        workMode = TEST,
        adRequest = RentalAd(
            id = stub.id,
            title = "abc",
            description = "abc",
            timeParam = RentalTimeParam(
                rentDates = listOf(),
                issueTimes = listOf(Instant.fromEpochSeconds(1703170800))
            ),
            visibility = VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("timeParam.rentDates", error?.field)
    assertContains(error?.message ?: "", "rentDates")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTimeParamIssueTimesEmpty(command: RentalCommand, processor: RentalAdProcessor) = runTest {
    val ctx = RentalContext(
        command = command,
        state = NONE,
        workMode = TEST,
        adRequest = RentalAd(
            id = stub.id,
            title = "abc",
            description = "abc",
            timeParam = RentalTimeParam(
                rentDates = listOf(LocalDate(2023, 12, 21)),
                issueTimes = listOf()
            ),
            visibility = VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("timeParam.issueTimes", error?.field)
    assertContains(error?.message ?: "", "timeParam")
}

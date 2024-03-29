package ru.teterin.rentalapp.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.models.RentalWorkMode
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdCorrect(command: RentalCommand, processor: RentalAdProcessor) = runTest {
    val ctx = RentalContext(
        command = command,
        state = RentalState.NONE,
        workMode = RentalWorkMode.TEST,
        adRequest = RentalAd(
            id = RentalAdId("8ae15f5e-9073-47b6-bea2-c4e395f156fd"),
            title = "abc",
            description = "abc",
            timeParam = RentalTimeParam(
                rentDates = listOf(LocalDate(2023, 12, 21)),
                issueTimes = listOf(Instant.fromEpochSeconds(1703170800))
            ),
            visibility = RentalVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(RentalState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: RentalCommand, processor: RentalAdProcessor) = runTest {
    val ctx = RentalContext(
        command = command,
        state = RentalState.NONE,
        workMode = RentalWorkMode.TEST,
        adRequest = RentalAd(
            id = RentalAdId(""),
            title = "abc",
            description = "abc",
            timeParam = RentalTimeParam(
                rentDates = listOf(LocalDate(2023, 12, 21)),
                issueTimes = listOf(Instant.fromEpochSeconds(1703170800))
            ),
            visibility = RentalVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RentalState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdFormat(command: RentalCommand, processor: RentalAdProcessor) = runTest {
    val ctx = RentalContext(
        command = command,
        state = RentalState.NONE,
        workMode = RentalWorkMode.TEST,
        adRequest = RentalAd(
            id = RentalAdId("!@#\$%^&*(),.{}ghijklmnopqrstuvwxyzGHIJKLMNOPQRSTUVWXYZ"),
            title = "abc",
            description = "abc",
            timeParam = RentalTimeParam(
                rentDates = listOf(LocalDate(2023, 12, 21)),
                issueTimes = listOf(Instant.fromEpochSeconds(1703170800))
            ),
            visibility = RentalVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(RentalState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

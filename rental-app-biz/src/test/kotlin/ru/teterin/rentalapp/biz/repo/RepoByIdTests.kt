package ru.teterin.rentalapp.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalError
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.repo.DbAdResponse
import ru.teterin.rentalapp.repo.test.AdRepositoryMock
import kotlin.test.assertEquals

private val initAd = RentalAd(
    id = RentalAdId("8ae15f5e-9073-47b6-bea2-c4e395f156fd"),
    title = "title",
    description = "description",
    visibility = RentalVisibility.VISIBLE_PUBLIC,
)
private val repo = AdRepositoryMock(
    invokeReadAd = {
        if (it.id == initAd.id) {
            DbAdResponse(
                isSuccess = true,
                data = initAd,
            )
        } else {
            DbAdResponse(
                isSuccess = false,
                data = null,
                errors = listOf(RentalError(message = "Not found", field = "id"))
            )
        }
    }
)
private val settings by lazy {
    RentalCorSettings(
        repoTest = repo,
    )
}
private val processor by lazy {
    RentalAdProcessor(settings)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(command: RentalCommand) = runTest{
    val ctx = RentalContext(
        command = command,
        state = RentalState.NONE,
        workMode = RentalWorkMode.TEST,
        adRequest = RentalAd(
            id = RentalAdId("9999999-9999-9999-999999999999"),
            title = "other",
            description = "other",
            visibility = RentalVisibility.VISIBLE_TO_GROUP,
            lock = RentalAdLock("11111111-1111-1111-111111111111"),
            timeParam = RentalTimeParam(
                rentDates = arrayListOf(LocalDate(2023, 12, 21)),
                issueTimes = arrayListOf(Instant.fromEpochSeconds(1703170800))
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(RentalState.FAILING, ctx.state)
    assertEquals(RentalAd(), ctx.adResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}

package ru.teterin.rentalapp.biz.repo

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.repo.DbAdResponse
import ru.teterin.rentalapp.repo.test.AdRepositoryMock
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = RentalUserId("6ec2039d-e417-4e6b-812a-bdfa501c81a2")
    private val command = RentalCommand.CREATE
    private val repo = AdRepositoryMock(
        invokeCreateAd = {
            DbAdResponse(
                isSuccess = true,
                data = RentalAd(
                    id = RentalAdId("11111111-1111-1111-111111111111"),
                    title = it.ad.title,
                    description = it.ad.description,
                    ownerId = userId,
                    visibility = it.ad.visibility,
                )
            )
        }
    )

    private val settings = RentalCorSettings(
        repoTest = repo
    )
    private val processor = RentalAdProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = RentalContext(
            command = command,
            state = RentalState.NONE,
            workMode = RentalWorkMode.TEST,
            adRequest = RentalAd(
                title = "title",
                description = "description",
                visibility = RentalVisibility.VISIBLE_PUBLIC,
                timeParam = RentalTimeParam(
                    rentDates = arrayListOf(LocalDate(2023, 12, 21)),
                    issueTimes = arrayListOf(Instant.fromEpochSeconds(1703170800))
                )
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalState.FINISHING, ctx.state)
        assertNotEquals(RentalAdId.NONE, ctx.adResponse.id)
        assertEquals("title", ctx.adResponse.title)
        assertEquals("description", ctx.adResponse.description)
        assertEquals(RentalVisibility.VISIBLE_PUBLIC, ctx.adResponse.visibility)
    }

}

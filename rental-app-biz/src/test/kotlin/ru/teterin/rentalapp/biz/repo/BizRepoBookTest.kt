package ru.teterin.rentalapp.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.repo.DbAdResponse
import ru.teterin.rentalapp.repo.test.AdRepositoryMock
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoBookTest {

    private val userId = RentalUserId("6ec2039d-e417-4e6b-812a-bdfa501c81a2")
    private val adId = "8ae15f5e-9073-47b6-bea2-c4e395f156fd"
    private val command = RentalCommand.BOOK
    private val initAd = RentalAd(
        id = RentalAdId(adId),
        title = "title",
        description = "description",
        ownerId = userId,
        visibility = RentalVisibility.VISIBLE_PUBLIC,
        lock = RentalAdLock("11111111-1111-1111-1111-111111111111"),
        timeParam = RentalTimeParam(
            rentDates = arrayListOf(LocalDate(2023, 12, 21)),
            issueTimes = arrayListOf(Instant.fromEpochSeconds(1703170800))
        )
    )

    private val repo by lazy {
        AdRepositoryMock(
            invokeReadAd = {
                DbAdResponse(
                    isSuccess = true,
                    data = initAd,
                )
            },
            invokeUpdateAd = {
                DbAdResponse(
                    isSuccess = true,
                    data = RentalAd(
                        id = RentalAdId(adId),
                        title = "title",
                        description = "description",
                        ownerId = userId,
                        visibility = RentalVisibility.VISIBLE_PUBLIC,
                        lock = RentalAdLock("11111111-1111-1111-1111-111111111111"),
                        timeParam = RentalTimeParam(
                            rentDates = arrayListOf(),
                            issueTimes = arrayListOf()
                        )
                    ),
                )
            }
        )
    }
    private val settings by lazy {
        RentalCorSettings(
            repoTest = repo,
        )
    }
    private val processor by lazy {
        RentalAdProcessor(settings)
    }

    @Test
    fun repoBookSuccessTest() = runTest {
        val adToBook = RentalAd(
            id = RentalAdId(adId),
            ownerId = userId,
            visibility = RentalVisibility.VISIBLE_TO_GROUP,
            lock = RentalAdLock("11111111-1111-1111-1111-111111111111"),
            timeParam = RentalTimeParam(
                rentDates = arrayListOf(LocalDate(2023, 12, 21)),
                issueTimes = arrayListOf(Instant.fromEpochSeconds(1703170800))
            )
        )
        val ctx = RentalContext(
            command = command,
            state = RentalState.NONE,
            workMode = RentalWorkMode.TEST,
            adRequest = adToBook,
        )
        processor.exec(ctx)
        assertEquals(RentalState.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
        assertTrue(ctx.adResponse.timeParam.rentDates.isEmpty())
        assertTrue(ctx.adResponse.timeParam.issueTimes.isEmpty())
    }

    @Test
    fun repoBookNotFoundTest() = repoNotFoundTest(command)

}

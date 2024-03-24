package ru.teterin.rentalapp.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.repo.DbAdResponse
import ru.teterin.rentalapp.repo.test.AdRepositoryMock
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoReadTest {

    private val userId = RentalUserId("6ec2039d-e417-4e6b-812a-bdfa501c81a2")
    private val command = RentalCommand.READ
    private val adId = "11111111-1111-1111-111111111111"
    private val initAd = RentalAd(
        id = RentalAdId(adId),
        title = "title",
        description = "description",
        ownerId = userId,
        visibility = RentalVisibility.VISIBLE_PUBLIC,
    )

    private val repo by lazy {
        AdRepositoryMock(
            invokeReadAd = {
                DbAdResponse(
                    isSuccess = true,
                    data = initAd,
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
        RentalAdProcessor(
            settings
        )
    }

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = RentalContext(
            command = command,
            state = RentalState.NONE,
            workMode = RentalWorkMode.TEST,
            adRequest = RentalAd(
                id = RentalAdId(adId),
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalState.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)

}

package ru.teterin.rentalapp.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdFilter
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.repo.DbAdsResponse
import ru.teterin.rentalapp.repo.test.AdRepositoryMock
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoSearchTest {

    private val userId = RentalUserId("6ec2039d-e417-4e6b-812a-bdfa501c81a2")
    private val adId = "11111111-1111-1111-111111111111"
    private val command = RentalCommand.SEARCH
    private val initAd = RentalAd(
        id = RentalAdId(adId),
        title = "title",
        description = "description",
        ownerId = userId,
        visibility = RentalVisibility.VISIBLE_PUBLIC,
    )

    private val repo by lazy {
        AdRepositoryMock(
            invokeSearhcAd = {
                DbAdsResponse(
                    isSuccess = true,
                    data = listOf(initAd),
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
    fun repoSearchSuccessTest() = runTest {
        val ctx = RentalContext(
            command = command,
            state = RentalState.NONE,
            workMode = RentalWorkMode.TEST,
            adFilterRequest = RentalAdFilter(
                searchString = "tit"
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalState.FINISHING, ctx.state)
        assertEquals(1, ctx.adsResponse.size)
    }
}

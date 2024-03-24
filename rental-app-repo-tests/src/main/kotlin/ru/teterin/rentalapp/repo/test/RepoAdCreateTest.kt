package ru.teterin.rentalapp.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.repo.DbAdRequest
import ru.teterin.rentalapp.model.repo.IAdRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoAdCreateTest {
    abstract val repo: IAdRepository

    protected open val lockNew: RentalAdLock = RentalAdLock("33333333-3333-3333-3333-333333333333")

    private val createObj = RentalAd(
        title = "create object",
        description = "create object description",
        ownerId = RentalUserId("515307e6-fbae-441a-91e4-afd5652064c0"),
        visibility = RentalVisibility.VISIBLE_TO_GROUP,
        timeParam = RentalTimeParam(
            rentDates = arrayListOf(LocalDate(2023, 12, 21)),
            issueTimes = arrayListOf(Instant.fromEpochSeconds(1703170800)),
        ),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createAd(DbAdRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: RentalAdId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertNotEquals(RentalAdId.NONE, result.data?.id)
        assertEquals(1, result.data?.timeParam?.rentDates?.size)
        assertEquals(1, result.data?.timeParam?.issueTimes?.size)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitAds("create") {
        override val initObjects: List<RentalAd> = emptyList()
    }

}

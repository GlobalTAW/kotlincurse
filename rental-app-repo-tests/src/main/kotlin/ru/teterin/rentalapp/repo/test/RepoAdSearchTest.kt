package ru.teterin.rentalapp.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.repo.DbAdFilterRequest
import ru.teterin.rentalapp.model.repo.IAdRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoAdSearchTest {
    abstract val repo: IAdRepository

    protected open val initializedObjects: List<RentalAd> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchAd(DbAdFilterRequest(ownerId = searchOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitAds("search") {
        val searchOwnerId = RentalUserId("owner-1234")
        val otherOwnerId = RentalUserId("owner-other")
        override val initObjects: List<RentalAd> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3", ownerId = otherOwnerId),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5", ownerId = otherOwnerId),
        )
    }

}

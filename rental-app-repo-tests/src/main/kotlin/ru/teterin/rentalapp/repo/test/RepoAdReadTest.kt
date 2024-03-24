package ru.teterin.rentalapp.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.repo.DbAdIdRequest
import ru.teterin.rentalapp.model.repo.IAdRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoAdReadTest {
    abstract val repo: IAdRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(readSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found"}
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("delete") {
        override val initObjects: List<RentalAd> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = RentalAdId("ad-repo-read-notFound")
    }

}

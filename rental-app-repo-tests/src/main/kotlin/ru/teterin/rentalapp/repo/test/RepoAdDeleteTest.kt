package ru.teterin.rentalapp.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.repo.DbAdIdRequest
import ru.teterin.rentalapp.model.repo.IAdRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoAdDeleteTest {
    abstract val repo: IAdRepository
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteAd(DbAdIdRequest(deleteSucc.id, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(initObjects[0], result.data)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(notFoundId, lock = lockOld))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found"}
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteAd(DbAdIdRequest(deleteConc.id, lock = lockBad))

        assertEquals(false, result.isSuccess)
        var error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitAds("delete") {
        override val initObjects: List<RentalAd> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
        val notFoundId = RentalAdId("ad-repo-delete-notFound")
    }

}

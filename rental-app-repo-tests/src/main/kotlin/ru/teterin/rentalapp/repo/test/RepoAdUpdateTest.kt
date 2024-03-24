package ru.teterin.rentalapp.repo.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalUserId
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.repo.DbAdRequest
import ru.teterin.rentalapp.model.repo.IAdRepository
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoAdUpdateTest {
    abstract val repo: IAdRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = RentalAdId("ad-repo-update-not-found")
    protected val lockBad = RentalAdLock("22222222-2222-2222-2222-222222222222")
    protected val lockNew = RentalAdLock("33333333-3333-3333-3333-333333333333")

    private val reqUpdateSucc by lazy {
        RentalAd(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = RentalUserId("owner-1234"),
            visibility = RentalVisibility.VISIBLE_TO_GROUP,
            lock = initObjects.first().lock,
        )
    }

    private val reqUpdateNotFound = RentalAd(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = RentalUserId("owner-1234"),
        visibility = RentalVisibility.VISIBLE_TO_GROUP,
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        RentalAd(
            id = updateConc.id,
            title = "update object concurrency",
            description = "update object concurrency description",
            ownerId = RentalUserId("owner-1234"),
            visibility = RentalVisibility.VISIBLE_TO_GROUP,
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find {it.code == "not-found"}
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency"}
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<RentalAd> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }

}

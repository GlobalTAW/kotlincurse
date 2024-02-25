package ru.teterin.rentalapp.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdFilter
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.stubs.RentalStubs
import ru.teterin.rentalapp.stubs.RentalAdStub
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class AdSearchStubTest {
    private val processor = RentalAdProcessor()
    val filter = RentalAdFilter(searchString = "Сап")

    @Test
    fun read() = runTest {

        val ctx = RentalContext(
            command = RentalCommand.SEARCH,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.SUCCESS,
            adFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.adsResponse.size > 1)
        val first = ctx.adsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(filter.searchString))
        assertTrue(first.description.contains(filter.searchString))
        with (RentalAdStub.get()) {
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.SEARCH,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.BAD_ID,
            adFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.SEARCH,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.DB_ERROR,
            adFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.SEARCH,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.BAD_TITLE,
            adFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }

}

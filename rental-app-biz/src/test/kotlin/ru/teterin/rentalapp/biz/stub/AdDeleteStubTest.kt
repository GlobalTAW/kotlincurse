package ru.teterin.rentalapp.biz.stub

import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.stubs.RentalStubs
import ru.teterin.rentalapp.stubs.RentalAdStub
import kotlin.test.assertEquals

class AdDeleteStubTest {
    private val processor = RentalAdProcessor()
    val id = RentalAdId("8ae15f5e-9073-47b6-bea2-c4e395f156fd")

    @Test
    fun delete() = runTest {

        val ctx = RentalContext(
            command = RentalCommand.DELETE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.SUCCESS,
            adRequest = RentalAd(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = RentalAdStub.get()
        assertEquals(stub.id, ctx.adResponse.id)
        assertEquals(stub.title, ctx.adResponse.title)
        assertEquals(stub.description, ctx.adResponse.description)
        assertEquals(stub.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun badId() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.DELETE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.BAD_ID,
            adRequest = RentalAd(),
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.DELETE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.DB_ERROR,
            adRequest = RentalAd(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.DELETE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.BAD_TITLE,
            adRequest = RentalAd(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }

}

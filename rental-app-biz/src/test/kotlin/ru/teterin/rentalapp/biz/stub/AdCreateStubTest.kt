package ru.teterin.rentalapp.biz.stub

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdId
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalProductId
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalTimeParam
import ru.teterin.rentalapp.model.models.RentalVisibility
import ru.teterin.rentalapp.model.models.RentalWorkMode
import ru.teterin.rentalapp.model.stubs.RentalStubs
import ru.teterin.rentalapp.stubs.RentalAdStub
import kotlin.test.assertEquals

class AdCreateStubTest {

    private val processor = RentalAdProcessor()
    val id = RentalAdId("8ae15f5e-9073-47b6-bea2-c4e395f156fd")
    val title = "Прокат Сап Борда"
    val description = "Прокат Сап Борда Exegol 3.2x0.8м"
    val timeParam = RentalTimeParam(
        rentDates = arrayListOf(LocalDate(2023, 12, 21)),
        issueTimes = arrayListOf(Instant.fromEpochSeconds(1703170800))
    )
    val productId = RentalProductId("9cc5e324-fc21-4e1e-860a-fe5cf87e17ea")
    val visibility = RentalVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {

        val ctx = RentalContext(
            command = RentalCommand.CREATE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.SUCCESS,
            adRequest = RentalAd(
                id = id,
                title = title,
                description = description,
                timeParam = timeParam,
                productId = productId,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalAdStub.get().id, ctx.adResponse.id)
        assertEquals(title, ctx.adResponse.title)
        assertEquals(description, ctx.adResponse.description)
        assertEquals(visibility, ctx.adResponse.visibility)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.CREATE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.BAD_TITLE,
            adRequest = RentalAd(
                id = id,
                title = "",
                description = description,
                timeParam = timeParam,
                productId = productId,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.CREATE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.BAD_DESCRIPTION,
            adRequest = RentalAd(
                id = id,
                title = title,
                description = "",
                timeParam = timeParam,
                productId = productId,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = RentalContext(
            command = RentalCommand.CREATE,
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
            command = RentalCommand.CREATE,
            state = RentalState.NONE,
            workMode = RentalWorkMode.STUB,
            stubCase = RentalStubs.BAD_ID,
            adRequest = RentalAd(
                id = id,
                title = title,
                description = description,
                visibility = visibility,
            ),
        )
        processor.exec(ctx)
        assertEquals(RentalAd(), ctx.adResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

}

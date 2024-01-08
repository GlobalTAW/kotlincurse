package ru.teterin.rentalapp.mappers.v1

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.Test
import ru.teterin.rentalapp.api.v1.models.*
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.*
import ru.teterin.rentalapp.model.stubs.RentalStubs
import java.util.UUID.*
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromTransport() {
        val id = randomUUID().toString()
        val req = AdCreateRequest(
            requestType = "create",
            requestId = id,
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS,
            ),
            ad = AdCreateObject(
                title = "Прокат Сап Борда",
                description = "Прокат Сап Борда Exegol 3.2x0.8м",
                timeParam = TimeParam(
                    rentDates = arrayListOf("2023-12-21"),
                    issueTimes = arrayListOf("1703170800")
                ),
                visibility = AdVisibility.PUBLIC,
            ),
        )

        val context = RentalContext()
        context.fromTransport(req)

        assertEquals(RentalCommand.CREATE, context.command)
        assertEquals(RentalStubs.SUCCESS, context.stubCase)
        assertEquals(RentalWorkMode.STUB, context.workMode)
        assertEquals("Прокат Сап Борда", context.adRequest.title)
        assertEquals("Прокат Сап Борда Exegol 3.2x0.8м", context.adRequest.description)
        assertEquals(RentalTimeParam(arrayListOf(LocalDate(2023, 12, 21)), arrayListOf(Instant.fromEpochSeconds(1703170800))), context.adRequest.timeParam)
        assertEquals(RentalVisibility.VISIBLE_PUBLIC, context.adRequest.visibility)
    }

    @Test
    fun toTransport() {
        val id = randomUUID().toString()
        val context = RentalContext(
            requestId = RentalRequestId(id),
            command = RentalCommand.CREATE,
            adResponse = RentalAd(
                title = "Прокат Сап Борда",
                description = "Прокат Сап Борда Exegol 3.2x0.8м",
                visibility = RentalVisibility.VISIBLE_PUBLIC,
            ),
            errors = mutableListOf(
                RentalError(
                    code = "error",
                    group = "badRequest",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = RentalState.RUNNING,
        )

        val req = context.toTransportAd() as AdCreateResponse

        assertEquals(id, req.requestId)
        assertEquals("Прокат Сап Борда", req.ad?.title)
        assertEquals("Прокат Сап Борда Exegol 3.2x0.8м", req.ad?.description)
        assertEquals(AdVisibility.PUBLIC, req.ad?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("error", req.errors?.firstOrNull()?.code)
        assertEquals("badRequest", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }

}

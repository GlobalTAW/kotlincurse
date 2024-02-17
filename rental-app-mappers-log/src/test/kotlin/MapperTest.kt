package ru.teterin.rentalapp.mappers.log

import org.junit.Test
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.*
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromContext() {
        val context = RentalContext(
            requestId = RentalRequestId("113b0802-9182-4c1a-8e66-3f89057b630d"),
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

        val log = context.toLog("test-id")

        assertEquals("test-id", log.logId)
        assertEquals("rental-app", log.source)
        assertEquals("113b0802-9182-4c1a-8e66-3f89057b630d", log.ad?.requestId)
        assertEquals("VISIBLE_PUBLIC", log.ad?.responseAd?.visibility)
        val error = log.errors?.firstOrNull()
        assertEquals("wrong title", error?.message)
        assertEquals("ERROR", error?.level)
    }

}

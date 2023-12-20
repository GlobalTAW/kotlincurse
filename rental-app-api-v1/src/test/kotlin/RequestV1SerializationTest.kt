package ru.teterin.rentalapp.api.v1

import ru.teterin.rentalapp.api.v1.models.*
import ru.teterin.rentalapp.api.v1.models.AdRequestDebugMode.STUB
import ru.teterin.rentalapp.api.v1.models.AdRequestDebugStubs.SUCCESS
import java.util.UUID.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = AdBookRequest(
        requestType = "book",
        requestId = randomUUID().toString(),
        debug = AdDebug(
            mode = STUB,
            stub = SUCCESS
        ),
        ad = AdBookObject(
            id = randomUUID().toString(),
            timeParam = TimeParam(
                rentDates = listOf("2023-12-21", "2023-12-22"),
                issueTimes = listOf("1703170800", "1703257200")
            )
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"requestType\":\\s*\"book\""))
        assertContains(json, Regex("\"requestId\":"))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"success\""))
        assertContains(json, Regex("\"id\":"))
        assertContains(json, Regex("\"timeParam\":"))
        assertContains(json, Regex("\"rentDates\":"))
        assertContains(json, Regex("\"issueTimes\":"))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as AdBookRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "515307e6-fbae-441a-91e4-afd5652064c0"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, AdBookRequest::class.java)

        assertEquals("515307e6-fbae-441a-91e4-afd5652064c0", obj.requestId)
    }

}

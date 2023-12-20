package ru.teterin.rentalapp.api.v1

import ru.teterin.rentalapp.api.v1.models.*
import ru.teterin.rentalapp.api.v1.models.ResponseResult.SUCCESS
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = AdCreateResponse(
        responseType = "create",
        requestId = "515307e6-fbae-441a-91e4-afd5652064c0",
        result = SUCCESS,
        ad = AdResponseObject(
            title = "Аренда сап борда",
            description = "Аренда сап борда со всеми нюансами",
            timeParam = TimeParam(
                rentDates = listOf("2023-12-21", "2023-12-22"),
                issueTimes = listOf("1703170800", "1703257200")
            ),
            visibility = AdVisibility.PUBLIC,
            id = "515307e6-fbae-441a-91e4-afd5652064c0",
            ownerId = "515307e6-fbae-441a-91e4-afd5652064c0",
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
        assertContains(json, Regex("\"requestId\":\\s*\"515307e6-fbae-441a-91e4-afd5652064c0\""))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
        assertContains(json, Regex("\"title\":\\s*\"Аренда сап борда\""))
        assertContains(json, Regex("\"description\":\\s*\"Аренда сап борда со всеми нюансами\""))
        assertContains(json, Regex("\"timeParam\":"))
        assertContains(json, Regex("\"rentDates\":"))
        assertContains(json, Regex("\"issueTimes\":"))
        assertContains(json, Regex("\"visibility\":\\s*\"public\""))
        assertContains(json, Regex("\"id\":\\s*\"515307e6-fbae-441a-91e4-afd5652064c0\""))
        assertContains(json, Regex("\"ownerId\":\\s*\"515307e6-fbae-441a-91e4-afd5652064c0\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as AdCreateResponse

        assertEquals(response, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"result": "success"}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, AdCreateResponse::class.java)

        assertEquals(SUCCESS, obj.result)
    }

}

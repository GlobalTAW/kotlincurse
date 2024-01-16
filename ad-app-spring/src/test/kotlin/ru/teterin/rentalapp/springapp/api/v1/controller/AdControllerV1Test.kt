package ru.teterin.rentalapp.springapp.api.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.teterin.rentalapp.api.v1.models.AdBookRequest
import ru.teterin.rentalapp.api.v1.models.AdCreateRequest
import ru.teterin.rentalapp.api.v1.models.AdDeleteRequest
import ru.teterin.rentalapp.api.v1.models.AdReadRequest
import ru.teterin.rentalapp.api.v1.models.AdSearchRequest
import ru.teterin.rentalapp.api.v1.models.AdUpdateRequest
import ru.teterin.rentalapp.api.v1.models.IResponse
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.mappers.v1.toTransportBook
import ru.teterin.rentalapp.mappers.v1.toTransportCreate
import ru.teterin.rentalapp.mappers.v1.toTransportDelete
import ru.teterin.rentalapp.mappers.v1.toTransportRead
import ru.teterin.rentalapp.mappers.v1.toTransportSearch
import ru.teterin.rentalapp.mappers.v1.toTransportUpdate
import ru.teterin.rentalapp.model.RentalContext

@WebFluxTest(AdControllerV1::class)
internal class AdControllerV1Test {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockkBean(relaxUnitFun = true)
    private lateinit var processor: RentalAdProcessor

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun createAd() = testStubAd(
        "/v1/ad/create",
        AdCreateRequest(),
        RentalContext().toTransportCreate()
    )

    @Test
    fun readAd() = testStubAd(
        "/v1/ad/read",
        AdReadRequest(),
        RentalContext().toTransportRead()
    )

    @Test
    fun updateAd() = testStubAd(
        "/v1/ad/update",
        AdUpdateRequest(),
        RentalContext().toTransportUpdate()
    )

    @Test
    fun deleteAd() = testStubAd(
        "/v1/ad/delete",
        AdDeleteRequest(),
        RentalContext().toTransportDelete()
    )

    @Test
    fun searchAd() = testStubAd(
        "/v1/ad/search",
        AdSearchRequest(),
        RentalContext().toTransportSearch()
    )

    @Test
    fun bookAd() = testStubAd(
        "/v1/ad/book",
        AdBookRequest(),
        RentalContext().toTransportBook()
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubAd(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val expectedResponse = mapper.readValue(mapper.writeValueAsString(responseObj as IResponse), IResponse::class.java)
                Assertions.assertThat(it).isEqualTo(expectedResponse)
            }
        coVerify {
            processor.exec(any())
        }
    }

}

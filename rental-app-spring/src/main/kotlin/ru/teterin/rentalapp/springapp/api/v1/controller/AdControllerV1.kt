package ru.teterin.rentalapp.springapp.api.v1.controller

import org.springframework.web.bind.annotation.*
import ru.teterin.rentalapp.api.v1.models.*
import ru.teterin.rentalapp.mappers.v1.*
import ru.teterin.rentalapp.springapp.models.RentalAppSettings

@Suppress("unused")
@RestController
@RequestMapping("v1/ad")
class AdControllerV1(
    private val appSettings: RentalAppSettings
) {

    @PostMapping("create")
    suspend fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse =
        processV1(appSettings, request = request, AdControllerV1::class, "create")


    @PostMapping("read")
    suspend fun readAd(@RequestBody request: AdReadRequest): AdReadResponse =
        processV1(appSettings, request = request, AdControllerV1::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        processV1(appSettings, request = request, AdControllerV1::class, "update")


    @PostMapping("delete")
    suspend fun deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        processV1(appSettings, request = request, AdControllerV1::class, "delete")

    @PostMapping("search")
    suspend fun searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse =
        processV1(appSettings, request = request, AdControllerV1::class, "search")


    @PostMapping("book")
    suspend fun bookAd(@RequestBody request: AdBookRequest): AdBookResponse =
        processV1(appSettings, request = request, AdControllerV1::class, "book")

}

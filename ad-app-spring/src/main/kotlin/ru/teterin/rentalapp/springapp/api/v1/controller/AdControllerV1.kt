package ru.teterin.rentalapp.springapp.api.v1.controller

import org.springframework.web.bind.annotation.*
import ru.teterin.rentalapp.api.v1.models.*
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.mappers.v1.*

@Suppress("unused")
@RestController
@RequestMapping("v1/ad")
class AdControllerV1(private val processor: RentalAdProcessor) {

    @PostMapping("create")
    suspend fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse {
        val context = RentalContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportCreate()
    }

    @PostMapping("read")
    suspend fun readAd(@RequestBody request: AdReadRequest): AdReadResponse {
        val context = RentalContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportRead()
    }

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse {
        val context = RentalContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportUpdate()
    }

    @PostMapping("delete")
    suspend fun deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse {
        val context = RentalContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportDelete()
    }

    @PostMapping("search")
    suspend fun searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse {
        val context = RentalContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportSearch()
    }

    @PostMapping("book")
    suspend fun bookAd(@RequestBody request: AdBookRequest): AdBookResponse {
        val context = RentalContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportBook()
    }

}

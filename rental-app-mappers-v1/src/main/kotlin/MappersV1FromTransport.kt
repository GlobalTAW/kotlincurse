package ru.teterin.rentalapp.mappers.v1

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import ru.teterin.rentalapp.api.v1.models.*
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.*
import ru.teterin.rentalapp.model.stubs.RentalStubs
import ru.teterin.rentalapp.mappers.v1.exceptions.UnknownRequestClass

fun RentalContext.fromTransport(request: IRequest) = when (request) {
    is AdCreateRequest -> fromTransport(request)
    is AdReadRequest -> fromTransport(request)
    is AdUpdateRequest -> fromTransport(request)
    is AdDeleteRequest -> fromTransport(request)
    is AdSearchRequest -> fromTransport(request)
    is AdBookRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toAdId() = this?.let { RentalAdId(it) } ?: RentalAdId.NONE
private fun String?.toAdWithId() = RentalAd(id = this.toAdId())
private fun IRequest?.requestId() = this?.requestId?.let { RentalRequestId(it) } ?: RentalRequestId.NONE

private fun AdDebug?.transportToWorkMode(): RentalWorkMode = when (this?.mode) {
    AdRequestDebugMode.PROD -> RentalWorkMode.PROD
    AdRequestDebugMode.TEST -> RentalWorkMode.TEST
    AdRequestDebugMode.STUB -> RentalWorkMode.STUB
    null -> RentalWorkMode.PROD
}

private fun AdDebug?.transportToStubCase(): RentalStubs = when (this?.stub) {
    AdRequestDebugStubs.SUCCESS -> RentalStubs.SUCCESS
    AdRequestDebugStubs.NOT_FOUND -> RentalStubs.NOT_FOUND
    AdRequestDebugStubs.BAD_ID -> RentalStubs.BAD_ID
    AdRequestDebugStubs.BAD_TITLE -> RentalStubs.BAD_TITLE
    AdRequestDebugStubs.BAD_DESCRIPTION -> RentalStubs.BAD_DESCRIPTION
    AdRequestDebugStubs.BAD_VISIBILITY -> RentalStubs.BAD_VISIBILITY
    AdRequestDebugStubs.CANNOT_DELETE -> RentalStubs.CANNOT_DELETE
    AdRequestDebugStubs.BAD_SEARCH_STRING -> RentalStubs.BAD_SEARCH_STRING
    null -> RentalStubs.NONE
}

fun RentalContext.fromTransport(request: AdCreateRequest) {
    command = RentalCommand.CREATE
    requestId = request.requestId()
    adRequest = request.ad?.toInternal() ?: RentalAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RentalContext.fromTransport(request: AdReadRequest) {
    command = RentalCommand.READ
    requestId = request.requestId()
    adRequest = request.ad?.id.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RentalContext.fromTransport(request: AdUpdateRequest) {
    command = RentalCommand.UPDATE
    requestId = request.requestId()
    adRequest = request.ad?.toInternal() ?: RentalAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RentalContext.fromTransport(request: AdDeleteRequest) {
    command = RentalCommand.DELETE
    requestId = request.requestId()
    adRequest = request.ad?.id.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RentalContext.fromTransport(request: AdSearchRequest) {
    command = RentalCommand.SEARCH
    requestId = request.requestId()
    adFilterRequest = request.adFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun RentalContext.fromTransport(request: AdBookRequest) {
    command = RentalCommand.BOOK
    requestId = request.requestId()
    adRequest = request.ad?.id.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun AdSearchFilter?.toInternal(): RentalAdFilter = RentalAdFilter(
    searchString = this?.searchString ?: ""
)

private fun AdCreateObject.toInternal(): RentalAd = RentalAd(
    title = this.title ?: "",
    description = this.description ?: "",
    timeParam = RentalTimeParam(
        rentDates = this.timeParam.rentDates(),
        issueTimes = this.timeParam.issueTimes()
    ),
    visibility = this.visibility.fromTransport(),
)

private fun AdUpdateObject.toInternal(): RentalAd = RentalAd(
    id = this.id.toAdId(),
    title = this.title ?: "",
    description = this.description ?: "",
    timeParam = RentalTimeParam(
        rentDates = this.timeParam.rentDates(),
        issueTimes = this.timeParam.issueTimes()
    ),
    visibility = this.visibility.fromTransport(),
)

private fun TimeParam?.rentDates() = this?.rentDates?.let { rentDatesToLocalDate(it) } ?: arrayListOf()
private fun TimeParam?.issueTimes() = this?.issueTimes?.let { issueTimesToInstant(it) } ?: arrayListOf()

private fun rentDatesToLocalDate(rentDates: List<String>): List<LocalDate> {
    return rentDates.map { date -> LocalDate.parse(date) }
}

private fun issueTimesToInstant(issueTimes: List<String>): List<Instant> {
    return issueTimes.map { timestamp -> Instant.fromEpochSeconds(timestamp.toLong()) }
}

private fun AdVisibility?.fromTransport(): RentalVisibility = when (this) {
    AdVisibility.PUBLIC -> RentalVisibility.VISIBLE_PUBLIC
    AdVisibility.OWNER_ONLY -> RentalVisibility.VISIBLE_TO_OWNER
    AdVisibility.GROUP_ONLY -> RentalVisibility.VISIBLE_TO_GROUP
    null -> RentalVisibility.NONE
}

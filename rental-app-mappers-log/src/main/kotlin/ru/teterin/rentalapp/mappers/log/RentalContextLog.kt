package ru.teterin.rentalapp.mappers.log

import kotlinx.datetime.Clock
import ru.teterin.rentalapp.api.log.models.*
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.*

fun RentalContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "rental-app",
    ad =toRentalLog(),
    errors = errors.map { it.toLog() },
)

fun RentalContext.toRentalLog():RentalLogModel? {
    val adNone = RentalAd()
    return RentalLogModel(
        requestId = requestId.takeIf { it != RentalRequestId.NONE }?.asString(),
        requestAd = adRequest.takeIf { it != adNone }?.toLog(),
        responseAd = adResponse.takeIf { it != adNone }?.toLog(),
        responseAds = adsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = adFilterRequest.takeIf { it != RentalAdFilter() }?.toLog(),
    ).takeIf { it != RentalLogModel() }
}

private fun RentalAdFilter.toLog() = AdFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != RentalUserId.NONE }?.asString(),
)

fun RentalError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun RentalAd.toLog() = AdLog(
    id = id.takeIf { it != RentalAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    timeParam = AdLogTimeParam(
        timeParam.rentDates.takeIf {it.isNotEmpty() }?.map { it.toString() }?.toList(),
        timeParam.issueTimes.takeIf { it.isNotEmpty() }?.map { it.toString() }?.toList(),
    ),
    visibility = visibility.takeIf { it != RentalVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != RentalUserId.NONE }?.asString(),
    productId = productId.takeIf { it != RentalProductId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)

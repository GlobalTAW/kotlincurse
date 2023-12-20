package ru.teterin.rentalapp.mappers.v1

import ru.teterin.rentalapp.mappers.v1.exceptions.UnknownRentalCommand
import ru.teterin.rentalapp.api.v1.models.*
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.*

fun RentalContext.toTransportAd(): IResponse = when (val cmd = command) {
    RentalCommand.CREATE -> toTransportCreate()
    RentalCommand.READ -> toTransportRead()
    RentalCommand.UPDATE -> toTransportUpdate()
    RentalCommand.DELETE -> toTransportDelete()
    RentalCommand.SEARCH -> toTransportSearch()
    RentalCommand.BOOK -> toTransportBook()
    RentalCommand.NONE -> throw UnknownRentalCommand(cmd)
}

fun RentalContext.toTransportCreate() = AdCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == RentalState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun RentalContext.toTransportRead() = AdReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == RentalState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun RentalContext.toTransportUpdate() = AdUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == RentalState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun RentalContext.toTransportDelete() = AdDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == RentalState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun RentalContext.toTransportSearch() = AdSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == RentalState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ads = adsResponse.toTransportAds()
)

fun RentalContext.toTransportBook() = AdBookResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == RentalState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun List<RentalAd>.toTransportAds(): List<AdResponseObject>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun RentalAd.toTransportAd(): AdResponseObject = AdResponseObject(
    id = id.takeIf { it != RentalAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != RentalUserId.NONE }?.asString(),
    visibility = visibility.toTransportAd(),
    permissions = permissionsClient.toTransportAd(),
)

private fun Set<RentalAdPermissionClient>.toTransportAd(): Set<AdPermissions>? = this
    .map { it.toTransportAd() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun RentalAdPermissionClient.toTransportAd() = when (this) {
    RentalAdPermissionClient.READ -> AdPermissions.READ
    RentalAdPermissionClient.UPDATE -> AdPermissions.UPDATE
    RentalAdPermissionClient.DELETE -> AdPermissions.DELETE
    RentalAdPermissionClient.BOOK -> AdPermissions.BOOK
    RentalAdPermissionClient.MAKE_VISIBLE_PUBLIC -> AdPermissions.MAKE_VISIBLE_PUBLIC
    RentalAdPermissionClient.MAKE_VISIBLE_OWNER -> AdPermissions.MAKE_VISIBLE_OWN
    RentalAdPermissionClient.MAKE_VISIBLE_GROUP -> AdPermissions.MAKE_VISIBLE_GROUP
}

private fun RentalVisibility.toTransportAd(): AdVisibility? = when (this) {
    RentalVisibility.VISIBLE_PUBLIC -> AdVisibility.PUBLIC
    RentalVisibility.VISIBLE_TO_GROUP -> AdVisibility.GROUP_ONLY
    RentalVisibility.VISIBLE_TO_OWNER -> AdVisibility.OWNER_ONLY
    RentalVisibility.NONE -> null
}

private fun List<RentalError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun RentalError.toTransportAd() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

package ru.teterin.rentalapp.springapp.api.v1.controller

import ru.teterin.rentalapp.api.v1.models.IRequest
import ru.teterin.rentalapp.api.v1.models.IResponse
import ru.teterin.rentalapp.common.controllerHelper
import ru.teterin.rentalapp.mappers.v1.fromTransport
import ru.teterin.rentalapp.mappers.v1.toTransportAd
import ru.teterin.rentalapp.springapp.models.RentalAppSettings
import kotlin.reflect.KClass

suspend inline fun <reified Q: IRequest, reified R: IResponse> processV1(
    appSettings: RentalAppSettings,
    request: Q,
    clazz: KClass<*>,
    logId: String,
): R = appSettings.controllerHelper(
    { fromTransport(request) },
    { toTransportAd() as R},
    clazz,
    logId,
)

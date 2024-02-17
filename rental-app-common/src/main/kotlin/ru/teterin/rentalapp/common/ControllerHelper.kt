package ru.teterin.rentalapp.common

import kotlinx.datetime.Clock
import ru.teterin.rentalapp.mappers.log.toLog
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.helpers.asRentalError
import ru.teterin.rentalapp.model.models.RentalState
import kotlin.reflect.KClass

suspend inline fun <T> IRentalAppSettings.controllerHelper(
    crossinline getRequest: suspend RentalContext.() -> Unit,
    crossinline toResponse: suspend RentalContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    var logger = corSettings.loggerProvider.logger(clazz)
    var ctx = RentalContext(
        timeStart = Clock.System.now(),
    )
    return try {
        logger.doWithLogging(logId) {
            ctx.getRequest()
            processor.exec(ctx)
            logger.info(
                msg = "Request $logId processed for ${clazz.simpleName}",
                marker = "BIZ",
                data = ctx.toLog(logId)
            )
            ctx.toResponse()
        }
    } catch(e: Throwable) {
        logger.doWithLogging("$logId-failure") {
            ctx.state = RentalState.FAILING
            ctx.errors.add(e.asRentalError())
            processor.exec(ctx)
            ctx.toResponse()
        }
    }

}

package ru.teterin.rentalapp.logging.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.teterin.rentalapp.logging.IRentalLogWrapper
import kotlin.reflect.KClass

fun rentalLoggerLogback(logger: Logger): IRentalLogWrapper = RentalLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun rentalLoggerLogback(clazz: KClass<*>): IRentalLogWrapper = rentalLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun rentalLoggerLogback(loggerId: String): IRentalLogWrapper = rentalLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)

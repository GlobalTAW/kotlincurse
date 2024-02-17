package ru.teterin.rentalapp.logging

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class RentalLoggerProvider(
    private val provider: (String) -> IRentalLogWrapper = { IRentalLogWrapper.DEFAULT }
) {

    fun logger(loggerId: String) = provider(loggerId)

    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>) = provider(function.name)

}

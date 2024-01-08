package ru.teterin.rentalapp.model.models

import kotlin.jvm.JvmInline

@JvmInline
value class RentalRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RentalRequestId("")
    }
}

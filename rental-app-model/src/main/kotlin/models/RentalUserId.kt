package ru.teterin.rentalapp.model.models

import kotlin.jvm.JvmInline

@JvmInline
value class RentalUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RentalUserId("")
    }
}

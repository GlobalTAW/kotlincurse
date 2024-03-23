package ru.teterin.rentalapp.model.models

@JvmInline
value class RentalAdLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = RentalAdLock("")
    }

}

package ru.teterin.rentalapp.model.models

data class RentalAdFilter(
    var searchString: String = "",
    var ownerId: RentalUserId = RentalUserId.NONE,
)

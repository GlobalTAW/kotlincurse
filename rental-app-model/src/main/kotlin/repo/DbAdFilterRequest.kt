package ru.teterin.rentalapp.model.repo

import ru.teterin.rentalapp.model.models.RentalUserId

data class DbAdFilterRequest(
    val titleFilter: String = "",
    val ownerId: RentalUserId = RentalUserId.NONE,
    //TODO add timeParam
)

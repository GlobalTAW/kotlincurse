package ru.teterin.rentalapp.model.repo

import ru.teterin.rentalapp.model.models.RentalError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<RentalError>
}

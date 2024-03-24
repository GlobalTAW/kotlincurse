package ru.teterin.rentalapp.model.repo

import ru.teterin.rentalapp.model.helpers.errorEmptyId as rentalErrorEmptyId
import ru.teterin.rentalapp.model.helpers.errorNotFound as rentalErrorNotFound
import ru.teterin.rentalapp.model.helpers.errorRepoConcurrency
import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalAdLock
import ru.teterin.rentalapp.model.models.RentalError

data class DbAdResponse(
    override val data: RentalAd?,
    override val isSuccess: Boolean,
    override val errors: List<RentalError> = emptyList(),
): IDbResponse<RentalAd> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbAdResponse(null, true)
        fun success(result: RentalAd) = DbAdResponse(result, true)
        fun error(errors: List<RentalError>, data: RentalAd? = null) = DbAdResponse(data, false, errors)
        fun error(error: RentalError, data: RentalAd? = null) = DbAdResponse(data, false, listOf(error))

        val errorEmptyId = error(rentalErrorEmptyId)

        fun errorConcurrent(lock: RentalAdLock, ad: RentalAd?) = error(
            errorRepoConcurrency(lock, ad?.lock?.let { RentalAdLock(it.asString()) }),
            ad
        )

        val errorNotFound = error(rentalErrorNotFound)
    }
}

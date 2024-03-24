package ru.teterin.rentalapp.model.repo

import ru.teterin.rentalapp.model.models.RentalAd
import ru.teterin.rentalapp.model.models.RentalError

data class DbAdsResponse(
    override val data: List<RentalAd>?,
    override val isSuccess: Boolean,
    override val errors: List<RentalError> = emptyList(),
): IDbResponse<List<RentalAd>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbAdsResponse(emptyList(), true)
        fun success(result: List<RentalAd>) = DbAdsResponse(result, true)
        fun error(errors: List<RentalError>) = DbAdsResponse(null, false, errors)
        fun error(error: RentalError) = DbAdsResponse(null, false, listOf(error))
    }

}

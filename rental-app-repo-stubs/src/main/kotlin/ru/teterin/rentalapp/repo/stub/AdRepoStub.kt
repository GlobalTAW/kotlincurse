package ru.teterin.rentalapp.repo.stub

import ru.teterin.rentalapp.model.repo.DbAdFilterRequest
import ru.teterin.rentalapp.model.repo.DbAdIdRequest
import ru.teterin.rentalapp.model.repo.DbAdRequest
import ru.teterin.rentalapp.model.repo.DbAdResponse
import ru.teterin.rentalapp.model.repo.DbAdsResponse
import ru.teterin.rentalapp.model.repo.IAdRepository
import ru.teterin.rentalapp.stubs.RentalAdStub

class AdRepoStub() : IAdRepository {
    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        return DbAdResponse(
            data = RentalAdStub.prepareResult { },
            isSuccess = true,
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        return DbAdResponse(
            data = RentalAdStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        return DbAdResponse(
            data = RentalAdStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        return DbAdResponse(
            data = RentalAdStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        return DbAdsResponse(
            data = RentalAdStub.prepareSearchResult(filter = ""),
            isSuccess = true,
        )
    }

}

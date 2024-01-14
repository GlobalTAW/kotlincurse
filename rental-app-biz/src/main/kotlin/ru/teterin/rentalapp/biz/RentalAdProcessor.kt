package ru.teterin.rentalapp.biz

import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.stubs.RentalAdStub

class RentalAdProcessor {

    suspend fun exec(ctx: RentalContext) {
        ctx.adResponse = RentalAdStub.get()
    }

}

package ru.teterin.rentalapp.model.exceptions

import ru.teterin.rentalapp.model.models.RentalAdLock

class RepoConcurrencyException(expectedLock: RentalAdLock, actualLock: RentalAdLock?): RuntimeException (
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)

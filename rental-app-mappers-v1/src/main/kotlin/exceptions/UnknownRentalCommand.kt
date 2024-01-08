package ru.teterin.rentalapp.mappers.v1.exceptions

import ru.teterin.rentalapp.model.models.RentalCommand

class UnknownRentalCommand(command: RentalCommand) : Throwable("Wrong command $command at mapping toTransport stage")

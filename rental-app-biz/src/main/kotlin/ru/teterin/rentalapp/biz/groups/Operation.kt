package ru.teterin.rentalapp.biz.groups

import ru.teterin.rentalapp.model.RentalContext
import ru.otus.otuskotlin.marketplace.cor.ICorChainDsl
import ru.otus.otuskotlin.marketplace.cor.chain
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState

fun ICorChainDsl<RentalContext>.operation(title: String, command: RentalCommand, block: ICorChainDsl<RentalContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == RentalState.RUNNING }
}

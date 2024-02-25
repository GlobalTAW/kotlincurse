package ru.teterin.rentalapp.biz.validation

import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.model.RentalContext
import ru.teterin.rentalapp.model.models.RentalAdFilter
import ru.teterin.rentalapp.model.models.RentalCommand
import ru.teterin.rentalapp.model.models.RentalState
import ru.teterin.rentalapp.model.models.RentalWorkMode
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest {

    private val command = RentalCommand.SEARCH
    private val processor by lazy { RentalAdProcessor() }

    @Test
    fun correctEmpty() = runTest {
        val ctx = RentalContext(
            command = command,
            state = RentalState.NONE,
            workMode = RentalWorkMode.TEST,
            adFilterRequest = RentalAdFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(RentalState.FAILING, ctx.state)
    }

}

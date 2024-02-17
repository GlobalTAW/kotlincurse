package ru.teterin.rentalapp.springapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.logging.RentalLoggerProvider
import ru.teterin.rentalapp.logging.logback.rentalLoggerLogback
import ru.teterin.rentalapp.model.RentalCorSettings
import ru.teterin.rentalapp.springapp.models.RentalAppSettings

@Suppress("unused")
@Configuration
class CorConfig {

    @Bean
    fun processor() = RentalAdProcessor()

    @Bean
    fun loggerProvider(): RentalLoggerProvider = RentalLoggerProvider { rentalLoggerLogback(it) }

    @Bean
    fun corSettings() = RentalCorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings() = RentalAppSettings(
        processor = processor(),
        corSettings = corSettings(),
    )

}

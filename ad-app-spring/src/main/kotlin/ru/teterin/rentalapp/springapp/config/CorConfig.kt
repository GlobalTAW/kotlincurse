package ru.teterin.rentalapp.springapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.teterin.rentalapp.biz.RentalAdProcessor

@Suppress("unused")
@Configuration
class CorConfig {

    @Bean
    fun processor() = RentalAdProcessor()

}

package ru.teterin.rentalapp.kafka

import ru.teterin.rentalapp.kafka.config.AppKafkaConfig

fun main() {
    val config = AppKafkaConfig()
    val consumer = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()))
    consumer.run()
}

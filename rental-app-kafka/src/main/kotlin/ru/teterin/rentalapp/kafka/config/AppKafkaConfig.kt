package ru.teterin.rentalapp.kafka.config

import ru.teterin.rentalapp.biz.RentalAdProcessor
import ru.teterin.rentalapp.common.IRentalAppSettings
import ru.teterin.rentalapp.model.RentalCorSettings

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    override val processor: RentalAdProcessor = RentalAdProcessor(),
    override val corSettings: RentalCorSettings = RentalCorSettings(),
) : IRentalAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "localhost:9094").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "rentalapp" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "rentalapp-in-v1" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "rentalapp-out-v1" }
    }

}

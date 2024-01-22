package ru.teterin.rentalapp.kafka

import ru.teterin.rentalapp.api.v1.apiV1RequestDeserialize
import ru.teterin.rentalapp.api.v1.apiV1ResponseSerialize
import ru.teterin.rentalapp.api.v1.models.IRequest
import ru.teterin.rentalapp.api.v1.models.IResponse
import ru.teterin.rentalapp.kafka.config.AppKafkaConfig
import ru.teterin.rentalapp.mappers.v1.fromTransport
import ru.teterin.rentalapp.mappers.v1.toTransportAd
import ru.teterin.rentalapp.model.RentalContext

class ConsumerStrategyV1 : ConsumerStrategy {

    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: RentalContext): String {
        val response: IResponse = source.toTransportAd()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: RentalContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }

}

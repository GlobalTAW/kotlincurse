import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.teterin.rentalapp.api.v1.apiV1RequestSerialize
import ru.teterin.rentalapp.api.v1.apiV1ResponseDeserialize
import ru.teterin.rentalapp.api.v1.models.AdCreateObject
import ru.teterin.rentalapp.api.v1.models.AdCreateRequest
import ru.teterin.rentalapp.api.v1.models.AdCreateResponse
import ru.teterin.rentalapp.api.v1.models.AdDebug
import ru.teterin.rentalapp.api.v1.models.AdRequestDebugMode
import ru.teterin.rentalapp.api.v1.models.AdRequestDebugStubs
import ru.teterin.rentalapp.api.v1.models.AdVisibility
import ru.teterin.rentalapp.kafka.AppKafkaConsumer
import ru.teterin.rentalapp.kafka.ConsumerStrategyV1
import ru.teterin.rentalapp.kafka.config.AppKafkaConfig
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(AdCreateRequest(
                        requestId = "caea9c8b-821a-4bc4-bef8-c4f79507ef8f",
                        ad = AdCreateObject(
                            title = "Прокат Сап Борда",
                            description = "some testing ad to check them all",
                            visibility = AdVisibility.OWNER_ONLY,
                        ),
                        debug = AdDebug(
                            mode = AdRequestDebugMode.STUB,
                            stub = AdRequestDebugStubs.SUCCESS
                        )
                    ))
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<AdCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("caea9c8b-821a-4bc4-bef8-c4f79507ef8f", result.requestId)
        assertEquals("Прокат Сап Борда", result.ad?.title)
    }

    companion object {
        const val PARTITION = 0
    }

}

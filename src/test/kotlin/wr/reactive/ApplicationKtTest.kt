package wr.reactive

import wr.avro.StringValue
import io.confluent.kafka.serializers.KafkaAvroSerializer
import mu.KotlinLogging
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import java.util.*

private val log = KotlinLogging.logger {}

internal class ApplicationKtTest {

    @Disabled
    @Test
    fun manualTest() {
        val stringValue = StringValue(UUID.randomUUID().toString())
        send("topic1", stringValue.getValue(), stringValue)
    }

    private fun <T : SpecificRecord> send(topic: String, key: String, value: T) {
        log.info("Sending to topic: $topic, key: $key, value: $value")
        val message = MessageBuilder.withPayload(value)
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build()
        kafkaTemplate().send(message).get()
    }

    private fun producerFactory(): ProducerFactory<String, SpecificRecord> {
        val configProps = HashMap<String, Any>()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        configProps["schema.registry.url"] = "http://localhost:8081"
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    private fun kafkaTemplate(): KafkaTemplate<String, SpecificRecord> {
        return KafkaTemplate(producerFactory())
    }

}
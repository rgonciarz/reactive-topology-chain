package wr.reactive

import mu.KotlinLogging
import org.apache.kafka.streams.kstream.KStream
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.support.MutableMessage
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import wr.avro.StringValue
import java.util.function.Consumer
import java.util.function.Function

private val log = KotlinLogging.logger {}

@Configuration
class TopologyConfig {

    @Bean
    fun topologyA() = Function<KStream<String, StringValue>, KStream<String, StringValue>> {
        it.peek { k, v -> log.info("topologyA: {}, {}", k, v) }
    }

    @Bean
    fun topologyRconsumer(fluxSink: FluxSink<Message<StringValue>>) = Consumer<Flux<Message<StringValue>>> {
        it
                .doOnNext { message -> log.info("topologyRconsumer: {}", message.payload) }
                .map { message -> MutableMessage(message.payload, mapOf(KafkaHeaders.MESSAGE_KEY to message.headers[KafkaHeaders.RECEIVED_MESSAGE_KEY])) }
                .doOnNext { message -> fluxSink.next(message) }
                .subscribe()
    }

    @Bean
    fun topologyR() = Function<Flux<StringValue>,Flux<StringValue>> {
        it.doOnNext { v -> log.info("topologyR: {}", v) }
    }

    @Bean
    fun topologyB() = Consumer<KStream<String, StringValue>> {
        it.peek { k, v -> log.info("topologyB: {}, {}", k, v) }
    }

}
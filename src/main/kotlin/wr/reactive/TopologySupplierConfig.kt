package wr.reactive

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxProcessor
import wr.avro.StringValue
import java.util.function.Supplier

private val log = KotlinLogging.logger {}

@Configuration
class TopologySupplierConfig {

    @Bean
    fun fluxProcessor(): FluxProcessor<Message<StringValue>, Message<StringValue>> = EmitterProcessor.create()

    @Bean
    fun fluxSink(fluxProcessor: FluxProcessor<Message<StringValue>, Message<StringValue>>) = fluxProcessor.sink()

    @Bean
    fun topologyRsupplier(fluxProcessor: FluxProcessor<Message<StringValue>, Message<StringValue>>) = Supplier<Flux<Message<StringValue>>> {
        fluxProcessor.doOnNext { message -> log.info("topologyRsupplier: {}", message.payload) }
    }

}
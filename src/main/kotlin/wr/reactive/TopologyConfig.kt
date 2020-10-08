package wr.reactive

import mu.KotlinLogging
import org.apache.kafka.streams.kstream.KStream
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Consumer
import java.util.function.Function

private val log = KotlinLogging.logger {}

@Configuration
class TopologyConfig {

    @Bean
    fun topologyA() = Function<KStream<String, String>, KStream<String, String>> {
        it.peek { k, v -> log.info("topologyA: {}, {}", k, v) }
    }

    @Bean
    fun topologyR() = Function<Flux<String>,Flux<String>> {
        it.doOnNext { v -> log.info("topologyR: {}", v) }
    }

    @Bean
    fun topologyB() = Consumer<KStream<String, String>> {
        it.peek { k, v -> log.info("topologyB: {}, {}", k, v) }
    }

}
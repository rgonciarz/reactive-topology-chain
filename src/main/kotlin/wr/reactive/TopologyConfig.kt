package wr.reactive

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Function

private val log = KotlinLogging.logger {}

@Configuration
class TopologyConfig {

    @Bean
    fun topologyR() = Function<Flux<String>,Flux<String>> {
        it.doOnNext { v -> log.info("topologyR: {}", v) }
    }

}
package com.example.orderservice.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration
public class ObservedAspectConfiguration {

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        observationRegistry.observationConfig().observationHandler(new SimpleLoggingHandler());
//        Observation.createNotStarted("order.detail", observationRegistry)
//                .contextualName("getting-order-detail")
//                .observe(() -> log.info("Hello"));
        return new ObservedAspect(observationRegistry);
    }
}
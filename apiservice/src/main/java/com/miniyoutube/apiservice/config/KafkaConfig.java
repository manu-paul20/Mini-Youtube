package com.miniyoutube.apiservice.config;

import com.miniyoutube.apiservice.entity.KafkaData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaTemplate<String, KafkaData> kafkaTemplate(ProducerFactory<String,KafkaData> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }
}

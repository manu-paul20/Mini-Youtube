package com.miniyoutube.apiservice.service;

import com.miniyoutube.apiservice.entity.KafkaData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaTemplate<String, KafkaData> kafkaTemplate;

    KafkaService(KafkaTemplate<String, KafkaData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(KafkaData data) {
        kafkaTemplate.send("miniYtTopic", data);
    }

    @KafkaListener(groupId = "")
    public void consume(){

    }

}

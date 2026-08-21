package com.miniyoutube.apiservice.service;

import com.miniyoutube.apiservice.entity.KafkaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaService {

    private final KafkaTemplate kafkaTemplate;

    @Autowired
    KafkaService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(KafkaData data) {
        kafkaTemplate.send("miniytTopic", data);
    }


}

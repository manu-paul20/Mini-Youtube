package com.miniyoutube.apiservice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaData {
    String videoId;
    String path;
}

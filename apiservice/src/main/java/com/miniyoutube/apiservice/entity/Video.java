package com.miniyoutube.apiservice.entity;

import com.miniyoutube.apiservice.enums.VideoStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "videos")
public class Video {
    @Id
    String id;
    String title;
    String description;
    List<String> qualities;
    VideoStatus status;
    String url;
}

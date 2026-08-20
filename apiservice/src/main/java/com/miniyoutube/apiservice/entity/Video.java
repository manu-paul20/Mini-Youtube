package com.miniyoutube.apiservice.entity;

import com.miniyoutube.apiservice.enums.VideoStatus;
import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "videos")
@Builder
public class Video {
    @Id
    String id;

    @NonNull
    String title;
    String description;
    List<String> qualities;
    VideoStatus status;
    String url;
}

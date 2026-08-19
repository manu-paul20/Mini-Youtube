package com.miniyoutube.apiservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
@Builder
public class User {
    @Id
    String id;

    @Indexed(unique = true)
    @NonNull
    String userName;

    @NonNull
    String password;

    @DBRef
    List<Video> videos;
}

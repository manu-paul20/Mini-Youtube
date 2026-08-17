package com.miniyoutube.apiservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collation = "users")
public class User {
    @Id
    String id;

    @Indexed(unique = true)
    String userName;

    String password;

    @DBRef
    List<Video> videos;
}

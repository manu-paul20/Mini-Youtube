package com.miniyoutube.apiservice.repository;

import com.miniyoutube.apiservice.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {

    UserRepo(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    private MongoTemplate mongoTemplate;

    public void save(User user){
        mongoTemplate.save(user);
    }
}

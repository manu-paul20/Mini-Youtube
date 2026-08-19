package com.miniyoutube.apiservice.repository;

import com.miniyoutube.apiservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepo {

    @Autowired
    UserRepo(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    private MongoTemplate mongoTemplate;

    public void save(User user){
        mongoTemplate.save(user);
    }

    public User findUser(String userName){
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").eq(userName));
        List<User> users = mongoTemplate.find(query,User.class);
        return (users.isEmpty())? null : users.getFirst();
    }
}

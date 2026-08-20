package com.miniyoutube.apiservice.repository;

import com.miniyoutube.apiservice.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoRepo {

    @Autowired
    public VideoRepo(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private MongoTemplate mongoTemplate;
    public Video saveVideo(Video video){
      return  mongoTemplate.save(video);
    }
}

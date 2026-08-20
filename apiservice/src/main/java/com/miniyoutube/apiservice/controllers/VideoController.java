package com.miniyoutube.apiservice.controllers;

import com.miniyoutube.apiservice.entity.User;
import com.miniyoutube.apiservice.entity.Video;
import com.miniyoutube.apiservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/videos")
public class VideoController {

    private VideoService videoService;

    @Autowired
    VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description

    ) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            videoService.saveVideo(
                    file,
                    title,
                    description,
                    userName
            );
            return new ResponseEntity<>("Video uploaded", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
           List<Video> videos = videoService.getAllVideos(userName);
           return new  ResponseEntity<>(videos,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

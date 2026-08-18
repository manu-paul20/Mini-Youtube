package com.miniyoutube.apiservice.controllers;

import com.miniyoutube.apiservice.dto.UserDto;
import com.miniyoutube.apiservice.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/health")
    public String health(){return "<h1>OK</h1>";}

    public ResponseEntity<String> signup(UserDto user){

    };

}

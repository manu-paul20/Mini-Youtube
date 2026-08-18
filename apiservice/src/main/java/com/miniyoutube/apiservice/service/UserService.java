package com.miniyoutube.apiservice.service;

import com.miniyoutube.apiservice.dto.UserDto;
import com.miniyoutube.apiservice.entity.User;
import com.miniyoutube.apiservice.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

   private UserRepo userRepo;

    public void saveUser(UserDto user){
        if(userRepo.findUser(user.getUserName())!=null){
            User newUser = User.builder()
                    .userName(user.getUserName())
                    .password(user.getPassword())
                    .videos(new ArrayList<>())
                    .build();
            userRepo.save(newUser);
        }
    }
}

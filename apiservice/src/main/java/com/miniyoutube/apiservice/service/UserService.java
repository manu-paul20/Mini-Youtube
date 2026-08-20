package com.miniyoutube.apiservice.service;

import com.miniyoutube.apiservice.dto.UserDto;
import com.miniyoutube.apiservice.entity.User;
import com.miniyoutube.apiservice.exceptions.UserAlreadyExistsException;
import com.miniyoutube.apiservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserService(UserRepo userRepo,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewUser(UserDto user) throws UserAlreadyExistsException {
        if (userRepo.findUser(user.getUserName()) == null) {
            User newUser = User.builder()
                    .userName(user.getUserName())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .videos(new ArrayList<>())
                    .build();
            userRepo.save(newUser);
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    public void updateUser(User user){
        userRepo.save(user);
    }

    public User getUser(String userName){
        return userRepo.findUser(userName);
    }
}

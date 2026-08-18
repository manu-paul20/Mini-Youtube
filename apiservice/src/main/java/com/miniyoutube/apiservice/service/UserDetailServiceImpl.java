package com.miniyoutube.apiservice.service;

import com.miniyoutube.apiservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepo userRepo;

    UserDetailServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.miniyoutube.apiservice.entity.User user = userRepo.findUser(username);
        if (user != null) {
            return User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .build();

        }else {
            throw new UsernameNotFoundException(String.format("User with name = %s not exist",username));
        }
    }
}

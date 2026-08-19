package com.miniyoutube.apiservice.controllers;

import com.miniyoutube.apiservice.dto.UserDto;
import com.miniyoutube.apiservice.exceptions.UserAlreadyExistsException;
import com.miniyoutube.apiservice.service.UserService;
import com.miniyoutube.apiservice.utils.JwtUtils;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "PUBLIC API")
public class PublicController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    @Autowired
    PublicController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;

    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto user) {
        try {
            userService.saveUser(user);
            log.info("User created with name = {}", user.getUserName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            log.info(userDto.toString());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword()));
            String jwt = jwtUtils.generateJWT(userDto.getUserName());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

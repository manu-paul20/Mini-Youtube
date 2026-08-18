package com.miniyoutube.apiservice.exceptions;

public class UserAlreadyExistsException extends Exception{
    UserAlreadyExistsException(){super("User already exists");}
}

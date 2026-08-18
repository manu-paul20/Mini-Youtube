package com.miniyoutube.apiservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${JWT.KEY}")
    private String secretKey;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    private Date getExpiration(String jwt) {
        return getAllClaims(jwt).getExpiration();
    }

    public boolean isTokenValid(String jwt) {
        return !jwt.isBlank() && getExpiration(jwt).after(new Date());
    }

    public String getUserName(String jwt) {
        return getAllClaims(jwt).getSubject();
    }

    private Claims getAllClaims(String jwt) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}

package com.miniyoutube.apiservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${JWT.KEY}")
    private String secretKey;

    @Value("${JWT.EXPIRE-TIME-MINUTES}")
    private int expireTimeInMinutes;

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

    public String generateJWT(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return getJwt(
                userName,
                claims
        );
    }

    private String getJwt(String subject, Map<String, Object> claims) {
        return Jwts
                .builder()
                .subject(subject)
                .claims(claims)
                .header().empty().add("typ", "JWT")
                .and()
                .signWith(getSigningKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (1000L * 60 * expireTimeInMinutes)))
                .compact();
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

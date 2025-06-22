package com.prepify.be.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${spring.jwt.private.key}")
    private String privateJwtKey;

    public String genJwtToken(Map<String, Object> payload) {
        Long expiredTime = 1000L*60*60*24*7; // expired token after 1 week
        Long currentMilis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(new Date(currentMilis))
                .setExpiration(new Date(currentMilis + expiredTime))
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(privateJwtKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String genJwtTokenForgotPassword(Map<String, Object> payload) {
        Long expiredTime = 1000L*60*5; // expired token after 5 minute
        Long currentMilis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(new Date(currentMilis))
                .setExpiration(new Date(currentMilis + expiredTime))
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(privateJwtKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

}

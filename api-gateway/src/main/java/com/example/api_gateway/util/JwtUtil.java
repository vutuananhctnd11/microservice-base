package com.example.api_gateway.util;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUtil {

    @Value("${jwt.signKey}")
    String base64SecretKey;

    JwtParser parser;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(base64SecretKey);
        SecretKey key = Keys.hmacShaKeyFor(decodedKey);
        parser = Jwts.parser().verifyWith(key).build();
    }

    public void validateToken(String token) {
        parser.parseSignedClaims(token);
    }

    public String getSubject(String token) {
        return parser.parseSignedClaims(token).getPayload().getSubject();
    }
}

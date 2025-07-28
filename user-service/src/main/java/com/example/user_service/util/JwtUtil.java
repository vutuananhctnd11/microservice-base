package com.example.user_service.util;

import com.example.user_service.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUtil {

    @Value("${jwt.signKey}")
    String signKey;

    @Value("${jwt.accessToken.expirationTime}")
    long accessTokenExpirationTime;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer("TuanAnhDev")
                .claim("role", "ROLE_" + user.getRole().getRoleName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS512, signKey)
                .compact();
    }


}

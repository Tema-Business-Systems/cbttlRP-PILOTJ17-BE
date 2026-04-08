package com.transport.tracking.k.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TokenService {

    @Value("${transport.secret.key}")
    private String secretKey;

    public String generateAccessToken(List<String> permissions, String userName) {
        String defaultSite = "";

        return Jwts
                .builder()
                .setId("TemaJWT")
                .setSubject("tema")
                .claim("authorities",
                        permissions)
                .claim("username", userName)
                .claim("site", defaultSite)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
    }

    public Claims decodeAccessToken(String accessToken) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(accessToken).getBody();
    }
}

package com.claudemir.sistemafinanceiro.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String secret;
    private final long expirationMillis;
    private final long refreshExpirationMillis;

    private final Algorithm algorithm;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis,
            @Value("${jwt.refreshExpiration}") long refreshExpirationMillis
    ) {
        this.secret = secret;
        this.expirationMillis = expirationMillis;
        this.refreshExpirationMillis = refreshExpirationMillis;
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(algorithm);
    }

    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationMillis))
                .sign(algorithm);
    }

    public boolean isTokenValid(String token, String username) {
        String subject = extractUsername(token);
        return subject.equals(username) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = decodeToken(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}

package com.jeffssousa.fluxo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKey jwtSecretKey;

    public String generateToken(Authentication authentication) {

        Instant now = Instant.now();

        Instant expiration = now.plus(Duration.ofHours(24));

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(jwtSecretKey)
                .compact();
    }

    public String extractUsername(String token){

        return extractClaims(token)
                .getSubject();

    }

    public boolean isTokenValid(String token){

        try {
            Claims claims =
                    extractClaims(token);

            return claims.getExpiration().after(new Date());

        } catch (Exception e) {

            return false;

        }

    }

    private Claims extractClaims(String token){

        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

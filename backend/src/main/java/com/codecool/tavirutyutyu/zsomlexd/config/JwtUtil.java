package com.codecool.tavirutyutyu.zsomlexd.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

@Component
public class JwtUtil {

    // Define your secret key (should be at least 256 bits for HMAC SHA-256)
    private static final String SECRET_KEY = "your-secret-key-should-be-at-least-256-bits-long";

    /**
     * Parse the JWT token and extract claims.
     *
     * @param token The JWT token to parse.
     * @return The claims extracted from the token.
     * @throws SignatureException If the token is invalid or the signature is incorrect.
     */
    public Claims parseToken(String token) throws JwtException {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());


        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generate a JWT token from user information (usually used when generating tokens).
     *
     * @param username The username for which the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(String username) {
        // Generate a new JWT token using the signing key
        return Jwts.builder()
                .subject(username)  // Set the subject (user information)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))  // Sign the token with the same key
                .compact();  // Return the JWT token as a string
    }
}

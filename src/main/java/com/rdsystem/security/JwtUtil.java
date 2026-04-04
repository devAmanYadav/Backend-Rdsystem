package com.rdsystem.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // ✅ Minimum 32+ chars secret key for HS256
    private static final String SECRET = "amanfinanceprojectjwtsecretkey1234"; // 32 chars

    private SecretKey getKey() {
        // Convert secret to bytes and generate a strong HMAC key
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate JWT token for a given mobile number
    public String generateToken(String mobile) {
        long expirationMillis = 1000 * 60 * 60; // 1 hour

        return Jwts.builder()
                .setSubject(mobile)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate JWT token and return subject (mobile)
    public String validateTokenAndGetSubject(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            return claims.getBody().getSubject();
        } catch (JwtException e) {
            // token invalid, expired or tampered
            return null;
        }
    }

	public boolean validateToken(String jwt) {
		// TODO Auto-generated method stub
		return false;
	}
}
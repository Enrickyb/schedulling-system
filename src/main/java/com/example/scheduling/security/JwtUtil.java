package com.example.scheduling.security;

import com.example.scheduling.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "o6gccYY5f2mGScfm6Z22+eWfrqCghke/rUW8PKvtPe0="; // 🔹 Removido "\n"
    private static final MacAlgorithm SIGNING_ALGORITHM = Jwts.SIG.HS256; // 🔹 Algoritmo atualizado

    public String generateToken(User user) { // ✅ Agora recebe um "User" corretamente
        return Jwts.builder()
                .subject(user.getEmail()) // ✅ Usa o e-mail como identificador
                .claim("userId", user.getId().toString()) // ✅ Agora "user" existe e tem getId()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10h de validade
                .signWith(getSigningKey(), SIGNING_ALGORITHM)
                .compact();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public UUID extractUserId(String token) {
        String userIdStr = extractClaims(token).get("userId", String.class);
        return UUID.fromString(userIdStr);
    }


    private io.jsonwebtoken.Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }
}

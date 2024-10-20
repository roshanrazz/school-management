package com.roshan.user_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expires}")
    long jwtExpirationInMillis;

    @Value("${jwt.refresh.expires}")
    long refreshTokenExpirationInMillis;

    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("type", "access")
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationInMillis))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public boolean isAccessToken(String token) {
        final Claims claims = extractClaims(token);
        System.out.println(claims);
        return "access".equals(claims.get("type"));
    }

    public boolean isRefreshToken(String token) {
        final Claims claims = extractClaims(token);
        return "refresh".equals(claims.get("type"));
    }


}

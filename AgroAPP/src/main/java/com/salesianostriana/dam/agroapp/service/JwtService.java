package com.salesianostriana.dam.agroapp.service;

import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.security.errorhandling.JwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.duration}")
    private long jwtLifeInMinutes;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.refresh.duration}")
    private long refreshLifeInDays;

    private JwtParser jwtParser;
    private JwtParser refreshTokenParser;

    private SecretKey secretKey;
    private SecretKey refreshSecretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());

        jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        refreshTokenParser = Jwts.parser()
                .verifyWith(refreshSecretKey)
                .build();
    }

    public String generateToken(Trabajador trabajador) {
        Date tokenExpirationDate = Date.from(
                LocalDateTime
                        .now()
                        .plusMinutes(jwtLifeInMinutes)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

        return Jwts.builder()
                .header().type(TOKEN_TYPE)
                .and()
                .subject(trabajador.getId().toString())
                .issuedAt(new Date())
                .expiration(tokenExpirationDate)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Trabajador trabajador) {
        Date tokenExpirationDate = Date.from(
                LocalDateTime
                        .now()
                        .plusMinutes(refreshLifeInDays)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());

        return Jwts.builder()
                .header().type(TOKEN_TYPE)
                .and()
                .subject(trabajador.getId().toString())
                .issuedAt(new Date())
                .expiration(tokenExpirationDate)
                .signWith(refreshSecretKey)
                .compact();
    }

    public Long getUserIdFromAccessToken(String token) {
        String sub = jwtParser.parseSignedClaims(token).getPayload().getSubject();
        return Long.parseLong(sub);
    }

    public Long getUserIdFromRefreshToken(String token) {
        String sub = refreshTokenParser.parseSignedClaims(token).getPayload().getSubject();
        return Long.parseLong(sub);
    }

    public boolean validateAccessToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
                | IllegalArgumentException ex) {
            throw new JwtException(ex.getMessage());
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            refreshTokenParser.parseSignedClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
                | IllegalArgumentException ex) {
            throw new JwtException(ex.getMessage());
        }
    }
}

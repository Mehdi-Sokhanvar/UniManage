package org.unimanage.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {
    public static final String TOKEN_TYPE = "token_type";

    private final KeyProvider keyProvider;

    @Value("${app.security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${app.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public JwtUtil(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public String generateAccessToken(UUID uuid) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "ACCESS_TOKEN");
        return generateToken(uuid, this.accessTokenExpiration);
    }

    public String generateRefreshToken(UUID uuid) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "REFRESH_TOKEN");
        return generateToken(uuid, this.refreshTokenExpiration);
    }

    private String generateToken(UUID uuid, Long expiration) {
        PrivateKey privateKey = keyProvider.getPrivateKey();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .claim("auth-id", uuid.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        PublicKey publicKey = keyProvider.getPublicKey();
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token);
    }

}

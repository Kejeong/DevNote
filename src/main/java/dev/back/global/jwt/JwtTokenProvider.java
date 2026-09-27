package dev.back.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final int MINIMUM_HS256_KEY_BYTES = 32;

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        byte[] keyBytes = decodeSecret(jwtProperties.secret());

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = jwtProperties.expirationSeconds() * 1000;
    }

    private byte[] decodeSecret(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET must be configured.");
        }

        try {
            byte[] keyBytes = Decoders.BASE64URL.decode(secret);

            if (keyBytes.length < MINIMUM_HS256_KEY_BYTES) {
                throw new IllegalStateException(
                        "JWT_SECRET must decode to at least 32 bytes for HS256."
                );
            }

            return keyBytes;
        } catch (DecodingException exception) {
            throw new IllegalStateException(
                    "JWT_SECRET must be a Base64URL-encoded value.",
                    exception
            );
        }
    }

    // accessToken 생성
    public String createAccessToken(Long memberId, String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

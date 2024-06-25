package com.yz.oj.util;

import com.yz.oj.entity.User;
import com.yz.oj.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

public class JwtService {
    private final SecretKey secretKey;
    private final int validSeconds;
    private final JwtParser jwtParser;

    @Autowired
    private UserService userService;

    public JwtService(String secretKeyStr, int validSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyStr.getBytes());
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
        this.validSeconds = validSeconds;
    }
    // ...

    public String createLoginAccessToken(UserDetails userDetails) {
        // 計算過期時間
        long expirationMillis = Instant.now()
                .plusSeconds(validSeconds)
                .getEpochSecond()
                * 1000;

        // 從資料庫取得使用者資訊
        User user = userService.findByUsername(userDetails.getUsername()).get();

        // 準備 payload 內容
        Claims claims = Jwts.claims()
                .issuedAt(new Date())
                .expiration(new Date(expirationMillis))
                .add("username", userDetails.getUsername())
                .add("userId", user.getId())
                .add("authorities", userDetails.getAuthorities())
                .build();

        // 簽名後產生 JWT
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }



    public Claims parseToken(String jwt) throws JwtException {
        return jwtParser.parseSignedClaims(jwt).getPayload();
    }
}
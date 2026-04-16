package com.springfield.gymrat.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret:springfield-gymrat-secret-key-2026}")// 读取密钥&默认密钥
    private String secret;

    @Value("${jwt.expiration.day:86400}")  // 默认1天（秒）
    private long expirationDay;

    @Value("${jwt.expiration.month:2592000}")  // 默认30天（秒）
    private long expirationMonth;

    /**
     * 生成密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成令牌
     */
    public String generateToken(Long userId, String username, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        String role = "user";
        if ("admin".equals(username)) {
            role = "admin";
        }
        claims.put("role", role);

        long expiration = rememberMe ? expirationMonth : expirationDay;
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()   // 创建JWT构建器
                .claims(claims) // 添加自定义声明
                .subject(username)  // 设置用户名
                .issuedAt(now)  // 设置令牌生成时间
                .expiration(expiryDate) // 设置令牌过期时间
                .signWith(getSigningKey())  // 设置签名密钥
                .compact(); // 生成令牌
    }

    /**
     * 从令牌中获取角色
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * 从令牌中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 获取令牌过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 验证令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            log.warn("JWT令牌无效: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 解析令牌
     */
    private Claims getClaimsFromToken(String token) {
            return Jwts.parser()    // 创建JWT解析器
                .verifyWith(getSigningKey())//  验证签名防止篡改
                .build()    //构建解析器实例
                .parseSignedClaims(token)   // 解析令牌
                .getPayload();  // 获取负载
    }

    /**
     * 检查令牌是否即将过期（剩余时间小于1小时）
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            long remaining = expiration.getTime() - System.currentTimeMillis();
            return remaining < 3600000;  // 1小时
        } catch (Exception e) {
            return true;
        }
    }
}

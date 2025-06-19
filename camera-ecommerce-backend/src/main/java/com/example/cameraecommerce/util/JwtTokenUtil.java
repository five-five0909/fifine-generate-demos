package com.example.cameraecommerce.util; // 请替换为你的包名

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // Token有效期5小时

    // 从 application.properties 或 application.yml 读取JWT秘钥
    // 注意：这个秘钥非常重要，需要妥善保管，并且长度足够安全
    // 例如：spring.jwt.secret=your-very-long-and-secure-secret-key-which-is-at-least-256-bits
    @Value("${spring.jwt.secret}")
    private String secretString;

    private SecretKey getSigningKey() {
        // 对于HS256，秘钥长度至少需要256位 (32字节)
        // Keys.hmacShaKeyFor会自动根据提供的字节数组生成合适的SecretKey
        byte[] keyBytes = secretString.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 从token中获取用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 从token中获取过期时间
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 获取token中的所有声明
    private Claims getAllClaimsFromToken(String token) {
        // Handles parsing and validation of the JWT.
        // The setSigningKey method is used to specify the key for verifying the signature.
        // If the token is invalid (e.g., expired, malformed, signature mismatch),
        // this method will throw an exception (e.g., ExpiredJwtException, MalformedJwtException, SignatureException).
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // 检查token是否过期
    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) { // Catching general exception if token is malformed and expiration cannot be parsed
            return true; // If token is invalid for any reason, treat as expired or invalid
        }
    }

    // 生成token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 你可以在这里添加额外的claims，例如用户角色等
        // claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // 创建token的具体逻辑
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 设置主题为用户名
                .setIssuedAt(new Date(System.currentTimeMillis())) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // 设置过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 使用HS256签名算法和秘钥
                .compact();
    }

    // 验证token
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) { // Catch exceptions during parsing or validation
            // Log the exception (optional)
            // logger.error("JWT validation error: {}", e.getMessage());
            return false; // Token is invalid if any error occurs
        }
    }
}

package com.tour.util;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类 —— Token 生成、解析与验证
 *
 * <p>使用 HS512 算法签名，密钥通过 {@code jwt.secret} 配置注入，
 * 过期时间通过 {@code jwt.expiration} 配置（单位：毫秒）。</p>
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /** JWT 签名密钥，生产环境必须设置强随机值 */
    @Value("${jwt.secret}")
    private String secret;

    /** Token 有效期（毫秒），默认 86400000（24 小时） */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 根据用户名生成 JWT Token
     *
     * @param username 用户名（作为 Subject）
     * @return 签名后的 JWT 字符串
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从 Token 中解析用户名
     *
     * @param token JWT Token 字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 验证 Token 是否有效（签名正确且未过期）
     *
     * @param token JWT Token 字符串
     * @return true=有效，false=无效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("JWT 签名无效: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("JWT Token 格式错误: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token 已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("不支持的 JWT Token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims 为空: {}", e.getMessage());
        }
        return false;
    }
}

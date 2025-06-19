package com.example.cameraecommerce.config; // 请替换为你的包名

import com.example.cameraecommerce.service.JwtUserDetailsService; // 你的UserDetailsService实现
import com.example.cameraecommerce.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService; // 用于加载用户信息

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token通常以 "Bearer " 开头
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); // 提取 "Bearer " 后面的部分
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.warn("无法获取JWT Token的用户名 (Illegal Argument): " + e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token已过期: " + e.getMessage());
            } catch (SignatureException e) {
                logger.warn("JWT Token签名无效: " + e.getMessage());
            } catch (MalformedJwtException e) {
                logger.warn("JWT Token格式错误: " + e.getMessage());
            } catch (Exception e) {
                logger.warn("JWT Token解析时发生未知错误: " + e.getMessage());
            }
        } else {
            // logger.warn("JWT Token请求头为空或格式不正确 (未找到 'Bearer ' 字符串)");
            // Allow requests without Authorization header to proceed,
            // Spring Security will handle authorization based on SecurityConfig rules.
        }

        // 一旦获取到Token，就验证它
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                logger.warn("从JWT Token中获取的用户名在系统中未找到: " + username);
            }


            // 如果Token有效，配置Spring Security以手动设置认证
            if (userDetails != null && jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 在设置了认证之后，我们指定当前用户已经通过认证。
                // 所以它会成功通过Spring Security的配置。
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                 if (userDetails == null) {
                    // This case is already logged above by UsernameNotFoundException
                 } else {
                    logger.warn("JWT Token验证失败 (token可能有效但与UserDetails不匹配或已过期)");
                 }
            }
        }
        chain.doFilter(request, response);
    }
}

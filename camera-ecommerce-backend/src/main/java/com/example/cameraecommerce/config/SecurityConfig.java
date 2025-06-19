package com.example.cameraecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService; // 已通过JwtUserDetailsService注入
import com.example.cameraecommerce.service.JwtUserDetailsService; // 明确指定
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 开启方法级别的权限控制
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService; // 使用我们自定义的UserDetailsService

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(csrf -> csrf.disable()) // 关闭CSRF，因为我们使用JWT
            .cors(cors -> {}) // 启用CORS并使用默认配置 (允许来自任何来源的请求)，可进一步定制
            .authorizeHttpRequests(auth -> auth
                .requestMatchers( // 公开访问的路径
                        "/api/auth/register",
                        "/api/auth/login",
                        "/api/products/**", // 商品查询通常允许匿名
                        "/api/categories/**" // 分类查询通常允许匿名
                ).permitAll()
                .requestMatchers( // Swagger UI 路径
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated() // 其他所有请求都需要认证
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 未认证用户的处理
                // .accessDeniedHandler(customAccessDeniedHandler) // 可选：用户权限不足时的处理
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态Session
            );

        // 添加JWT过滤器，在 UsernamePasswordAuthenticationFilter 之前执行
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

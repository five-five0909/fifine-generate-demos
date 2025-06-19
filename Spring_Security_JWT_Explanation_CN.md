# Spring Security 与 JWT (JSON Web Tokens) 深度解析

## 1. 什么是Spring Security?

Spring Security 是一个功能强大且高度可定制的身份验证和访问控制框架。它是保护基于Spring的应用程序的事实标准。

**核心功能:**
- **认证 (Authentication):** 验证用户是谁（你是谁？）。
- **授权 (Authorization):** 决定用户是否有权限执行某个操作（你能做什么？）。
- **攻击防护:** 提供对常见安全漏洞的防护，如CSRF（跨站请求伪造）、Session Fixation等。

**关键组件:**
- `SecurityContextHolder`: 存储当前安全上下文（包括已认证用户信息）的地方。
- `AuthenticationManager`: 处理认证请求。
- `UserDetailsService`: 根据用户名加载用户特定数据的核心接口。
- `PasswordEncoder`: 用于密码加密和验证。
- `AccessDecisionManager`: 决定是否允许访问受保护资源。

## 2. 什么是JWT (JSON Web Tokens)?

JWT 是一种开放标准（RFC 7519），它定义了一种紧凑且自包含的方式，用于在各方之间安全地传输信息作为JSON对象。由于其体积小、可跨域、无需服务端保存会话（Stateless）等优点，特别适用于分布式系统和API认证。

**JWT结构 (三部分组成，由`.`分隔):**
- **Header (头部):** 包含两部分信息：
    - `typ` (Type): Token的类型，通常是 "JWT"。
    - `alg` (Algorithm): 使用的签名算法，例如 HMAC SHA256 (HS256) 或 RSA。
    ```json
    {
      "alg": "HS256",
      "typ": "JWT"
    }
    ```
    Header部分会经过Base64Url编码形成JWT的第一部分。

- **Payload (负载):** 包含声明（Claims）。声明是关于实体（通常是用户）和附加数据的语句。有三种类型的声明：
    - **Registered Claims (注册声明):** 一些预定义的声明，非强制性但建议使用，以提供一组有用的、可互操作的声明。例如：
        - `iss` (Issuer): 签发者
        - `exp` (Expiration Time): 过期时间
        - `sub` (Subject): 主题（通常是用户ID）
        - `aud` (Audience): 受众
        - `iat` (Issued At): 签发时间
    - **Public Claims (公共声明):** 可以由使用JWT的各方随意定义。为避免冲突，应在IANA JSON Web Token Registry中定义它们，或将其定义为包含防冲突命名空间的URI。
    - **Private Claims (私有声明):** 是为在同意使用它们的各方之间共享信息而创建的自定义声明，既非注册声明也非公共声明。
    ```json
    {
      "sub": "1234567890",
      "name": "John Doe",
      "admin": true,
      "iat": 1516239022
    }
    ```
    Payload部分会经过Base64Url编码形成JWT的第二部分。

- **Signature (签名):** 用于验证消息在传递过程中没有被篡改，并且对于使用私钥签名的Token，它还可以验证发送者的身份。
    签名是通过取编码后的Header、编码后的Payload、一个秘钥（Secret）、Header中指定的签名算法进行计算得出的。
    例如，如果使用HMAC SHA256算法，签名的创建方式如下：
    ```
    HMACSHA256(
      base64UrlEncode(header) + "." +
      base64UrlEncode(payload),
      secret)
    ```
    签名用于确保JWT的完整性。

**JWT的优点:**
- **无状态 (Stateless):** 服务器不需要存储会话Token或用户会话信息。Token本身包含了所有验证所需的信息。
- **可扩展性:** 由于服务器无状态，更容易水平扩展。
- **安全性:** 通过签名机制保证了Token的完整性和真实性（如果使用非对称加密，还能保证来源）。
- **跨域友好:** Token可以轻松地在不同服务或应用之间传递。
- **移动端友好:** 对于API驱动的应用，尤其是在移动端，JWT是一种流行的认证方式。

## 3. Spring Security 与 JWT 如何结合?

在传统的基于Session的认证中，Spring Security会在用户成功登录后，在服务端创建一个Session，并将Session ID返回给客户端（通常存储在Cookie中）。后续请求客户端会携带Session ID，服务端根据ID查找Session信息以验证用户。

当引入JWT后，流程变为：
1.  **用户登录:** 用户使用用户名和密码发送登录请求。
2.  **服务器认证:** Spring Security的`AuthenticationManager`验证用户凭据。
3.  **生成JWT:** 如果认证成功，服务器会为该用户生成一个JWT。这个JWT的Payload中通常会包含用户ID、用户名、角色、过期时间等信息。
4.  **返回JWT给客户端:** 服务器将生成的JWT返回给客户端。客户端通常将JWT存储在本地（例如：localStorage、sessionStorage或HTTP Header的Authorization）。
5.  **后续请求:** 客户端在后续的每个请求中，通常会将JWT放在HTTP请求的`Authorization`头部中，并使用`Bearer`方案（例如：`Authorization: Bearer <token>`）。
6.  **服务器验证JWT:** 对于每个受保护的API请求，Spring Security会配置一个过滤器（通常是`OncePerRequestFilter`的子类）。此过滤器会从请求头中提取JWT，然后：
    a.  **验证Token的有效性:** 检查签名是否正确、Token是否过期等。
    b.  **解析用户信息:** 如果Token有效，从中解析出用户信息（如用户名、权限）。
    c.  **构建Authentication对象:** 使用解析出的用户信息创建一个`UsernamePasswordAuthenticationToken`（或其他`Authentication`实现），并设置到`SecurityContextHolder`中。这表示当前用户已通过认证。
7.  **授权决策:** Spring Security的授权机制（如基于注解的`@PreAuthorize`或基于配置的`http.authorizeRequests()`）会根据`SecurityContextHolder`中的用户信息和配置的权限规则，决定是否允许访问该资源。

**核心组件实现思路:**

-   **`JwtTokenUtil` (或类似名称的工具类):**
    *   生成JWT: 输入用户信息（如用户名），生成包含声明（claims）并使用秘钥签名的JWT字符串。
    *   解析JWT: 从JWT字符串中提取声明（claims）。
    *   验证JWT: 检查签名是否正确、是否过期等。
    *   通常会使用一个库如 `io.jsonwebtoken:jjwt`。

-   **`JwtRequestFilter` (自定义过滤器):**
    *   继承 `OncePerRequestFilter`，确保每个请求只执行一次。
    *   在 `doFilterInternal` 方法中：
        1.  从请求头 (`Authorization: Bearer <token>`) 中获取JWT。
        2.  如果JWT存在且格式正确，使用 `JwtTokenUtil` 验证并解析它。
        3.  如果Token有效，根据Token中的用户信息创建一个 `UsernamePasswordAuthenticationToken`。
        4.  将此 `Authentication` 对象设置到 `SecurityContextHolder.getContext().setAuthentication()`。

-   **`UserDetailsService` 实现:**
    *   Spring Security在某些流程中仍可能需要通过用户名加载用户信息（例如，在JWT验证后，你可能想从数据库加载最新的用户权限信息，而不是完全依赖Token中的信息）。
    *   `loadUserByUsername(String username)` 方法根据用户名从数据库或其他数据源查找用户，并返回一个实现了 `UserDetails` 接口的对象。

-   **`SecurityConfig` (或 `WebSecurityConfigurerAdapter` 的子类):**
    *   **配置密码编码器 (`PasswordEncoder`):** 例如 `BCryptPasswordEncoder`。
    *   **配置`AuthenticationManager`:** 通常通过重写 `authenticationManagerBean()` 方法。
    *   **配置HTTP安全性 (`HttpSecurity`):**
        *   禁用CSRF（因为JWT是无状态的，通常不需要CSRF保护，但需谨慎评估）。
        *   配置哪些路径是公开的（如登录 `/api/auth/login`、注册 `/api/auth/register`），哪些路径需要认证。
        *   配置无状态会话管理：`http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);`
        *   将自定义的 `JwtRequestFilter` 添加到过滤器链中，通常在 `UsernamePasswordAuthenticationFilter` 之前。
        *   配置认证入口点 (`AuthenticationEntryPoint`)：当未认证用户尝试访问受保护资源时如何响应（例如返回401 Unauthorized）。
        *   配置访问拒绝处理器 (`AccessDeniedHandler`)：当已认证用户尝试访问其无权限的资源时如何响应（例如返回403 Forbidden）。

## 4. 代码示例 (关键部分)

**a. `pom.xml` 依赖 (部分):**
```xml
<dependencies>
    <!-- Spring Boot Starter Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- JWT Library (jjwt) -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version> <!-- 请使用最新版本 -->
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson -->
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <!-- Spring Boot Starter Web (for Controllers, etc.) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

**b. `JwtTokenUtil.java` (简化版):**
```java
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
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // 检查token是否过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
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
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```
**注意:** 在 `application.properties` 或 `application.yml` 中配置 `spring.jwt.secret`。这个秘钥必须足够强大和保密。

**c. `JwtRequestFilter.java`:**
```java
package com.example.cameraecommerce.config; // 请替换为你的包名

import com.example.cameraecommerce.service.JwtUserDetailsService; // 你的UserDetailsService实现
import com.example.cameraecommerce.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
                logger.warn("无法获取JWT Token的用户名");
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token已过期");
            }
        } else {
            logger.warn("JWT Token请求头为空或格式不正确 (未找到 'Bearer ' 字符串)");
        }

        // 一旦获取到Token，就验证它
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // 如果Token有效，配置Spring Security以手动设置认证
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 在设置了认证之后，我们指定当前用户已经通过认证。
                // 所以它会成功通过Spring Security的配置。
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
```

**d. `JwtUserDetailsService.java` (示例，你需要根据你的用户实体和数据访问逻辑调整):**
```java
package com.example.cameraecommerce.service; // 请替换为你的包名

import com.example.cameraecommerce.model.User; // 你的用户实体类
import com.example.cameraecommerce.repository.UserRepository; // 你的用户仓库
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // 用于创建空的权限列表，实际项目中应加载用户真实权限

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // 假设你有一个UserRepository来获取用户信息

    // 在实际项目中，你可能还需要一个 PasswordEncoder Bean
    // @Autowired
    // private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库或其他地方根据用户名查找用户
        // 这里是一个示例，你需要根据你的实际情况修改
        User user = userRepository.findByUsername(username); // 假设你的User实体有username字段
        if (user == null) {
            throw new UsernameNotFoundException("用户未找到，用户名: " + username);
        }
        // 第三个参数是权限列表，你需要根据你的用户角色和权限来填充
        // Spring Security的User需要用户名、密码和权限列表
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>()); // 示例：使用空权限列表，实际应加载用户真实权限
    }

    // 如果你需要在注册时保存用户并加密密码，可以添加类似这样的方法
    // public User save(UserDTO userDto) {
    //     User newUser = new User();
    //     newUser.setUsername(userDto.getUsername());
    //     newUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));
    //     // 设置其他属性...
    //     return userRepository.save(newUser);
    // }
}
```

**e. `SecurityConfig.java` (或 `WebSecurityConfigurerAdapter` 的子类，Spring Boot 3.x 推荐使用 `SecurityFilterChain` Bean):**
```java
package com.example.cameraecommerce.config; // 请替换为你的包名

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 开启方法级别的权限控制
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 处理未认证访问

    @Autowired
    private UserDetailsService jwtUserDetailsService; // 你的UserDetailsService

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // 你的JWT过滤器

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
            // 关闭CSRF防护，因为我们使用JWT，它是无状态的
            .csrf(csrf -> csrf.disable())
            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll() // 允许匿名访问登录和注册接口
                .requestMatchers("/api/products/**", "/api/categories/**").permitAll() // 允许匿名访问商品和分类查询接口
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // 允许匿名访问Swagger
                .anyRequest().authenticated() // 其他所有请求都需要认证
            )
            // 配置异常处理：未认证访问和权限不足
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 用户未认证时的处理
                // .accessDeniedHandler(customAccessDeniedHandler) // 可选：用户权限不足时的处理
            )
            // 配置Session管理策略为无状态 (STATELESS)，因为我们不使用Session
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        // 添加JWT过滤器，在 UsernamePasswordAuthenticationFilter 之前执行
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
```

**f. `JwtAuthenticationEntryPoint.java` (处理未认证访问):**
```java
package com.example.cameraecommerce.config; // 请替换为你的包名

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 当用户尝试访问安全的REST资源而不提供任何凭据时，此方法将被调用
        // 我们应该只发送一个401 Unauthorized响应，因为没有“登录页面”可以重定向。
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
```

## 5. 整体流程回顾

1.  **客户端:** 发送登录请求 (`/api/auth/login`)，包含用户名和密码。
2.  **后端 (AuthController):**
    *   调用 `AuthenticationManager.authenticate()` 对用户名密码进行认证。
    *   如果认证成功，调用 `JwtUserDetailsService.loadUserByUsername()` 获取 `UserDetails`。
    *   调用 `JwtTokenUtil.generateToken()` 生成JWT。
    *   将JWT返回给客户端。
3.  **客户端:** 存储JWT (例如，在localStorage)。之后每次请求受保护资源时，在 `Authorization` Header中携带 `Bearer <JWT>`。
4.  **后端 (JwtRequestFilter):**
    *   拦截请求，从Header中提取JWT。
    *   使用 `JwtTokenUtil` 验证JWT（签名、过期时间）。
    *   如果JWT有效，从中解析出用户名。
    *   调用 `JwtUserDetailsService.loadUserByUsername()` 获取 `UserDetails`。
    *   创建一个 `UsernamePasswordAuthenticationToken`，并设置到 `SecurityContextHolder`。
5.  **后端 (Spring Security):**
    *   后续的过滤器链和访问控制决策（如 `@PreAuthorize`）会使用 `SecurityContextHolder` 中的 `Authentication` 对象来判断用户是否有权限访问资源。

## 6. 注意事项和最佳实践

-   **密钥安全:** JWT的密钥 (`spring.jwt.secret`) 至关重要。它必须保密，并且足够复杂以防止暴力破解。考虑使用环境变量或专门的密钥管理服务来存储它，而不是硬编码在配置文件中（尤其是在生产环境）。
-   **HTTPS:** 始终在生产环境中使用HTTPS来传输JWT，以防止中间人攻击窃取Token。
-   **Token有效期:** 设置合理的Token过期时间 (`JWT_TOKEN_VALIDITY`)。太短会导致用户频繁重新登录，太长会增加Token泄露后的安全风险。
-   **Token吊销/刷新机制 (Refresh Tokens):** JWT本身是无状态的，一旦签发，在过期前通常无法主动使其失效。如果需要强制用户下线或处理Token泄露，需要额外的机制：
    *   **Refresh Tokens:** 签发一个有效期较短的Access Token和一个有效期较长的Refresh Token。当Access Token过期后，客户端使用Refresh Token获取新的Access Token。服务器可以吊销Refresh Token。
    *   **黑名单机制:** 将需要吊销的Token ID（例如JWT的`jti`声明）存储在缓存（如Redis）中。每次验证Token时，先检查是否在黑名单中。
-   **Payload内容:** 不要在JWT的Payload中存储敏感信息，因为Payload是Base64Url编码的，可以被轻易解码。只存储必要的用户标识和非敏感信息。
-   **算法选择:** HS256 (HMAC with SHA-256) 是对称加密，意味着签发和验证Token使用相同的密钥。如果Token需要在多个服务间验证，且这些服务不应共享同一个密钥，可以考虑使用非对称加密算法如RS256 (RSA with SHA-256)，其中签发服务使用私钥签名，验证服务使用公钥验证。
-   **错误处理:** `JwtAuthenticationEntryPoint` 用于处理认证失败（例如，未提供Token或Token无效）。你可能还需要一个 `AccessDeniedHandler` 来处理已认证用户访问无权限资源的情况（返回403 Forbidden）。

这篇文档提供了Spring Security与JWT结合使用的基础知识和实现思路。在实际项目中，你可能需要根据具体需求进行调整和扩展。

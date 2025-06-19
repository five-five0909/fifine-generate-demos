package com.example.cameraecommerce.controller;

import com.example.cameraecommerce.dto.request.LoginRequest;
import com.example.cameraecommerce.dto.request.RegisterRequest;
import com.example.cameraecommerce.dto.response.JwtResponse;
import com.example.cameraecommerce.entity.User;
import com.example.cameraecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证管理", description = "用户注册与登录接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User registeredUser = authService.register(registerRequest);
            // Avoid returning password or other sensitive info; consider a UserResponseDTO.
            // For simplicity, returning a success message or partial user info.
            return ResponseEntity.status(HttpStatus.CREATED).body("用户注册成功！ 用户名: " + registeredUser.getUsername());
        } catch (RuntimeException e) {
            // Consider logging the exception details for server-side monitoring
            // logger.error("Registration error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            // More specific exception handling for user not found, bad credentials, disabled account etc.
            // logger.warn("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

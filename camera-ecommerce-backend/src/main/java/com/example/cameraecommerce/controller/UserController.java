package com.example.cameraecommerce.controller;

import com.example.cameraecommerce.dto.request.PasswordUpdateRequest;
import com.example.cameraecommerce.dto.request.UserUpdateRequest;
import com.example.cameraecommerce.entity.User;
import com.example.cameraecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "当前用户信息获取与修改")
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth") // Swagger UI: all endpoints here require authentication
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Ensures the user is authenticated
    public ResponseEntity<?> getCurrentUserInfo() {
        User currentUser = userService.getCurrentUser();
        // userService.getCurrentUser() already sets password to null.
        // If not, ensure to map to a DTO that omits sensitive fields.
        if (currentUser == null) {
            // This case should ideally be handled by PreAuthorize or security filters,
            // but as a safeguard:
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未登录或认证已失效");
        }
        return ResponseEntity.ok(currentUser);
    }

    @Operation(summary = "更新当前用户信息")
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateCurrentUserInfo(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            User updatedUser = userService.updateUserInfo(userUpdateRequest);
            // updatedUser from service should also have password set to null.
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            // logger.warn("User update error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "修改当前用户密码")
    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserPassword(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        try {
            userService.updateUserPassword(passwordUpdateRequest);
            return ResponseEntity.ok("密码修改成功");
        } catch (RuntimeException e) {
            // logger.warn("Password update error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.example.cameraecommerce.controller;

import com.example.cameraecommerce.dto.request.CartItemAddRequest;
import com.example.cameraecommerce.dto.request.CartItemUpdateRequest;
import com.example.cameraecommerce.entity.CartItem;
import com.example.cameraecommerce.entity.User;
import com.example.cameraecommerce.service.CartService;
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
// import org.slf4j.Logger; // For logging
// import org.slf4j.LoggerFactory; // For logging

import java.util.List;
// import java.util.Map; // For custom response structure
// import java.util.HashMap; // For custom response structure


@Tag(name = "购物车管理", description = "用户购物车操作")
@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()") // All cart operations require authentication
public class CartController {

    // private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService; // To get the current user

    private User getCurrentAuthenticatedUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            // This should ideally not be reached if PreAuthorize is effective.
            // Throwing an exception that can be caught by a global handler might be cleaner.
            throw new IllegalStateException("用户未登录或认证已失效");
        }
        return currentUser;
    }

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartItemAddRequest request) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            CartItem cartItem = cartService.addItemToCart(currentUser.getId(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.warn("Error adding item to cart: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "查看购物车")
    @GetMapping
    public ResponseEntity<?> viewCart() {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            List<CartItem> cartItems = cartService.getCartItems(currentUser.getId());
            // Optionally, wrap this in a CartViewResponse DTO that includes totals, item counts, etc.
            // Map<String, Object> response = new HashMap<>();
            // response.put("items", cartItems);
            // response.put("totalItems", cartItems.stream().mapToInt(CartItem::getQuantity).sum());
            // response.put("totalPrice", cartItems.stream().map(item -> item.getCurrentPrice().multiply(new java.math.BigDecimal(item.getQuantity()))).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
            return ResponseEntity.ok(cartItems);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.error("Error viewing cart: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "修改购物车中商品数量或选中状态")
    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long itemId, @Valid @RequestBody CartItemUpdateRequest request) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            CartItem updatedItem = cartService.updateCartItemQuantity(currentUser.getId(), itemId, request);
            if (updatedItem == null && request.getQuantity() != null && request.getQuantity() <=0) { // Item was deleted due to quantity <= 0
                return ResponseEntity.ok("购物车商品已移除，因为数量更新为0或更少。");
            }
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.warn("Error updating cart item {}: {}", itemId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "从购物车删除商品")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long itemId) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            cartService.deleteCartItem(currentUser.getId(), itemId);
            return ResponseEntity.ok("购物车商品删除成功");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.warn("Error deleting cart item {}: {}", itemId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "清空当前用户购物车")
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            cartService.clearCart(currentUser.getId());
            return ResponseEntity.ok("购物车已清空");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.error("Error clearing cart: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

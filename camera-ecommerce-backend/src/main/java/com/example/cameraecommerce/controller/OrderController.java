package com.example.cameraecommerce.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cameraecommerce.dto.request.OrderCreateRequest;
import com.example.cameraecommerce.entity.Order;
import com.example.cameraecommerce.entity.OrderItem;
import com.example.cameraecommerce.entity.User;
// import com.example.cameraecommerce.mapper.OrderItemMapper; // Replaced by OrderService method
import com.example.cameraecommerce.service.OrderService;
import com.example.cameraecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "订单管理", description = "用户订单创建与查询")
@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
public class OrderController {

    // private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    private User getCurrentAuthenticatedUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("用户未登录或认证已失效");
        }
        return currentUser;
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            Order order = orderService.createOrder(currentUser.getId(), request);
            // Consider returning a more detailed OrderResponseDTO
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.warn("Error creating order: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "查询用户订单列表 (分页)")
    @GetMapping
    public ResponseEntity<?> getUserOrders(
            @Parameter(description = "当前页码 (从1开始)") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size
    ) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            Page<Order> page = new Page<>(current, size);
            Page<Order> orderPage = orderService.getUserOrders(currentUser.getId(), page);
            return ResponseEntity.ok(orderPage);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "查询单个订单详情")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            Order order = orderService.getOrderDetails(orderId, currentUser.getId());
            if (order == null) {
                return ResponseEntity.notFound().build();
            }
            // Fetch order items using the service method
            List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("order", order);
            response.put("orderItems", items);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) { // Catch other potential errors from service layer
            // logger.warn("Error fetching order details for orderId {}: {}", orderId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "模拟支付接口")
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<?> simulatePayment(@PathVariable Long orderId) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            boolean success = orderService.simulatePayment(orderId, currentUser.getId());
            if (success) {
                return ResponseEntity.ok("订单支付成功");
            } else {
                // This path might not be reached if service throws exception for payment failure
                return ResponseEntity.badRequest().body("订单支付失败或状态不允许支付");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.warn("Payment simulation error for orderId {}: {}", orderId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "用户取消订单")
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            User currentUser = getCurrentAuthenticatedUser();
            orderService.cancelOrder(orderId, currentUser.getId());
            return ResponseEntity.ok("订单已取消");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            // logger.warn("Error cancelling order {}: {}", orderId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Example for Admin functionality (ensure proper security config if uncommented)
    // @Operation(summary = "[ADMIN] 更新订单状态")
    // @PutMapping("/admin/{orderId}/status")
    // @PreAuthorize("hasRole('ADMIN')") // Requires ADMIN role
    // public ResponseEntity<?> updateOrderStatusByAdmin(
    //         @PathVariable Long orderId,
    //         @Parameter(description = "新的订单状态码") @RequestParam Integer status
    // ) {
    //     try {
    //         boolean success = orderService.updateOrderStatusByAdmin(orderId, status);
    //         if (success) {
    //             return ResponseEntity.ok("订单状态已由管理员更新");
    //         } else {
    //             return ResponseEntity.badRequest().body("管理员更新订单状态失败");
    //         }
    //     } catch (RuntimeException e) {
    //         // logger.error("Admin update order status error for orderId {}: {}", orderId, e.getMessage(), e);
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }
}

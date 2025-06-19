package com.example.cameraecommerce.service;

import com.example.cameraecommerce.dto.request.OrderCreateRequest;
import com.example.cameraecommerce.entity.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cameraecommerce.entity.OrderItem; // For returning with order details

import java.util.List; // For returning with order details


public interface OrderService extends IService<Order> {
    Order createOrder(Long userId, OrderCreateRequest request);
    Order getOrderDetails(Long orderId, Long userId); // userId用于权限校验
    List<OrderItem> getOrderItemsByOrderId(Long orderId); // Separate method to get items
    Page<Order> getUserOrders(Long userId, Page<Order> page);
    boolean simulatePayment(Long orderId, Long userId);
    boolean updateOrderStatusByAdmin(Long orderId, Integer newStatus); // 后台用, more descriptive name
    void cancelOrder(Long orderId, Long userId); // 用户取消订单
}

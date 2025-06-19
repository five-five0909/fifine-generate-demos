package com.example.cameraecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cameraecommerce.dto.request.OrderCreateRequest;
import com.example.cameraecommerce.entity.CartItem;
import com.example.cameraecommerce.entity.Order;
import com.example.cameraecommerce.entity.OrderItem;
import com.example.cameraecommerce.entity.Product;
import com.example.cameraecommerce.mapper.OrderItemMapper;
import com.example.cameraecommerce.mapper.OrderMapper;
import com.example.cameraecommerce.service.CartService;
import com.example.cameraecommerce.service.OrderService;
import com.example.cameraecommerce.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// import org.springframework.beans.BeanUtils; // Not used
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // 用于生成订单号

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public Order createOrder(Long userId, OrderCreateRequest request) {
        List<CartItem> checkedCartItems = cartService.getCheckedCartItems(userId);
        if (CollectionUtils.isEmpty(checkedCartItems)) {
            throw new RuntimeException("购物车中没有选中的商品来创建订单");
        }

        Order order = new Order();
        // Generate a unique order serial number
        order.setOrderSn("CAM" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase());
        order.setUserId(userId);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setRemarks(request.getRemarks());

        order.setOrderStatus(0); // 0-待付款
        order.setPaymentStatus(0); // 0-未支付
        order.setShippingStatus(0); // 0-未发货
        order.setShippingFee(BigDecimal.ZERO); // Default shipping fee, can be calculated
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : checkedCartItems) {
            Product product = productService.getById(cartItem.getProductId()); // Product already enriched in CartItem from CartService
            if (product == null || cartItem.getCurrentStock() == null || cartItem.getCurrentStock() < cartItem.getQuantity()) {
                 throw new RuntimeException("商品 " + (cartItem.getProductName() != null ? cartItem.getProductName() : "ID: "+cartItem.getProductId()) + " 库存不足或已下架");
            }

            // Deduct stock
            boolean stockDecreased = productService.decreaseStock(product.getId(), cartItem.getQuantity());
            if (!stockDecreased) {
                // This might happen due to concurrent stock modification.
                throw new RuntimeException("商品 " + product.getName() + " 库存扣减失败，请重试");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductSku(product.getSku());
            orderItem.setProductImageUrl(product.getMainImageUrl());
            orderItem.setQuantity(cartItem.getQuantity());
            // Use current price from enriched CartItem (which should be product's current price)
            BigDecimal unitPrice = cartItem.getCurrentPrice();
            orderItem.setUnitPrice(unitPrice);
            orderItem.setTotalPrice(unitPrice.multiply(new BigDecimal(cartItem.getQuantity())));
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        // Placeholder for potential future logic like coupon application, shipping fee calculation
        order.setPayableAmount(totalAmount.add(order.getShippingFee()));

        // Save the main order record
        this.save(order);

        // Batch insert order items
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setOrderSn(order.getOrderSn());
            orderItemMapper.insert(item);
        }

        // Clear checked items from cart
        // This assumes deleteCartItem is idempotent or handles non-existent items gracefully
        checkedCartItems.forEach(cartItem -> cartService.deleteCartItem(userId, cartItem.getId()));

        return order;
    }

    @Override
    public Order getOrderDetails(Long orderId, Long userId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getId, orderId);
        if (userId != null) {
            queryWrapper.eq(Order::getUserId, userId);
        }
        // Order order = getOne(queryWrapper); // Replaced by baseMapper.selectOne for clarity
        Order order = baseMapper.selectOne(queryWrapper);
        // Note: OrderItems are not typically embedded in Order entity for list views.
        // They are fetched separately when viewing order details.
        return order;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemMapper.selectList(
            new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId)
        );
    }


    @Override
    public Page<Order> getUserOrders(Long userId, Page<Order> page) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId)
                    .orderByDesc(Order::getCreatedAt);
        return page(page, queryWrapper); // page method from IService
    }

    @Override
    @Transactional
    public boolean simulatePayment(Long orderId, Long userId) {
        Order order = getOrderDetails(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在或无权操作, OrderID: " + orderId);
        }
        if (order.getPaymentStatus() != 0 || order.getOrderStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法支付, OrderID: " + orderId + ", PaymentStatus: " + order.getPaymentStatus() + ", OrderStatus: " + order.getOrderStatus());
        }

        order.setPaymentStatus(1); // 1-已支付
        order.setOrderStatus(1);   // 1-待发货
        order.setPaymentTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    @Transactional
    public boolean updateOrderStatusByAdmin(Long orderId, Integer newStatus) {
        Order order = getById(orderId); // Admin can fetch any order
        if (order == null) {
            throw new RuntimeException("订单不存在, OrderID: " + orderId);
        }

        // Basic state transition validation (can be more complex)
        // Example: Cannot revert to an earlier state like 'PENDING_PAYMENT' from 'SHIPPED'
        if (newStatus < order.getOrderStatus() && !(newStatus == 4 && order.getOrderStatus() !=4)) { // 4 is 'CLOSED/CANCELLED'
             // Allow moving to 'CLOSED' from most states, but not backwards for others
            // log.warn("Invalid status transition attempt for orderId: {}, from {} to {}", orderId, order.getOrderStatus(), newStatus);
            // throw new RuntimeException("无效的订单状态变更");
        }

        order.setOrderStatus(newStatus);
        if (newStatus == 2) { // 2 = SHIPPED
             order.setShippingTime(LocalDateTime.now());
             order.setShippingStatus(1); // 1 = SHIPPED
        } else if (newStatus == 3) { // 3 = COMPLETED (Delivered)
            order.setReceiveTime(LocalDateTime.now());
            order.setShippingStatus(2); // 2 = DELIVERED
        } else if (newStatus == 4) { // 4 = CLOSED/CANCELLED
            order.setShippingStatus( (order.getShippingStatus() == 0) ? 0 : 3 ); // If not shipped, remains 0, else 3=Cancelled_Shipment (example)
        }
        order.setUpdatedAt(LocalDateTime.now());
        return updateById(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = getOrderDetails(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在或无权操作, OrderID: " + orderId);
        }

        // Only allow cancellation if order is in 'PENDING_PAYMENT' (0) or 'PENDING_SHIPMENT' (1) for some businesses
        if (order.getOrderStatus() != 0 ) { // 0-待付款
            throw new RuntimeException("订单状态不允许取消. Current status: " + order.getOrderStatus());
        }

        order.setOrderStatus(4); // 4-已关闭(用户取消)
        order.setUpdatedAt(LocalDateTime.now());
        boolean updated = updateById(order);

        if (updated) {
            // Restore stock
            List<OrderItem> orderItems = getOrderItemsByOrderId(orderId);
            if (!CollectionUtils.isEmpty(orderItems)) {
                for (OrderItem item : orderItems) {
                    productService.increaseStock(item.getProductId(), item.getQuantity());
                }
            }
        } else {
            throw new RuntimeException("订单取消失败, OrderID: " + orderId);
        }
    }
}

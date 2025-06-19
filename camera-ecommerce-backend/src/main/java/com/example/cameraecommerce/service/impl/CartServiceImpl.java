package com.example.cameraecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper; // Not strictly needed for current ops
import com.example.cameraecommerce.dto.request.CartItemAddRequest;
import com.example.cameraecommerce.dto.request.CartItemUpdateRequest;
import com.example.cameraecommerce.entity.CartItem;
import com.example.cameraecommerce.entity.Product;
import com.example.cameraecommerce.mapper.CartItemMapper;
import com.example.cameraecommerce.service.CartService;
import com.example.cameraecommerce.service.ProductService;
// import org.springframework.beans.BeanUtils; // Not used
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public CartItem addItemToCart(Long userId, CartItemAddRequest request) {
        Product product = productService.getById(request.getProductId());
        if (product == null || (product.getStatus() != null && product.getStatus() == 0)) { // 假设0为下架, 1为上架
            throw new RuntimeException("商品不存在或已下架, ProductID: " + request.getProductId());
        }
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("商品库存不足, ProductID: " + request.getProductId() + ", 需要: " + request.getQuantity() + ", 现有: " + product.getStock());
        }

        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, userId).eq(CartItem::getProductId, request.getProductId());
        CartItem cartItem = cartItemMapper.selectOne(queryWrapper);

        if (cartItem != null) {
            // 商品已在购物车中，更新数量
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("商品库存不足以增加到新数量, ProductID: " + request.getProductId());
            }
            cartItem.setQuantity(newQuantity);
            cartItem.setUpdatedAt(LocalDateTime.now());
            // price_at_addition typically should not be updated to keep the price snapshot.
            cartItemMapper.updateById(cartItem);
        } else {
            // 商品不在购物车中，新增
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPriceAtAddition(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice()); // 保存添加时的价格
            cartItem.setCheckedStatus(1); // 默认选中
            cartItem.setCreatedAt(LocalDateTime.now());
            cartItem.setUpdatedAt(LocalDateTime.now());
            cartItemMapper.insert(cartItem);
        }
        // 填充商品信息用于返回
        return enrichCartItemWithProductDetails(cartItem, product);
    }

    @Override
    public List<CartItem> getCartItems(Long userId) {
        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, userId).orderByDesc(CartItem::getUpdatedAt);
        List<CartItem> cartItems = cartItemMapper.selectList(queryWrapper);

        return cartItems.stream()
                        .map(item -> {
                            Product product = productService.getById(item.getProductId());
                            return enrichCartItemWithProductDetails(item, product);
                        })
                        .filter(item -> item.getProductName() != null) // 过滤掉商品信息获取失败的项
                        .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemMapper.selectById(cartItemId);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("购物车项不存在或不属于当前用户, CartItemID: " + cartItemId);
        }
        Product product = productService.getById(cartItem.getProductId());
        if (product == null) {
            // This implies data inconsistency or product was deleted.
            // Depending on requirements, might remove cartItem or throw a more specific error.
            cartItemMapper.deleteById(cartItem.getId()); // Clean up inconsistent cart item
            throw new RuntimeException("购物车关联的商品不存在, ProductID: " + cartItem.getProductId() + ". 该购物车项已被移除。");
        }

        if (request.getQuantity() != null) {
             if (request.getQuantity() <= 0) { // Handle case where user wants to remove item by setting quantity to 0 or less
                 deleteCartItem(userId, cartItemId);
                 return null; // Or return a CartItem with quantity 0 if preferred by frontend
             }
             if (product.getStock() < request.getQuantity()) {
                throw new RuntimeException("商品库存不足, ProductID: " + product.getId() + ", 需要: " + request.getQuantity() + ", 现有: " + product.getStock());
            }
            cartItem.setQuantity(request.getQuantity());
        }

        if(request.getCheckedStatus() != null) {
            cartItem.setCheckedStatus(request.getCheckedStatus() ? 1 : 0);
        }

        cartItem.setUpdatedAt(LocalDateTime.now());
        cartItemMapper.updateById(cartItem);
        return enrichCartItemWithProductDetails(cartItem, product);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long userId, Long cartItemId) {
        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getId, cartItemId).eq(CartItem::getUserId, userId);
        int deletedRows = cartItemMapper.delete(queryWrapper);
        if (deletedRows == 0) {
            log.warn("购物车项删除：未找到或不属于当前用户, cartItemId: " + cartItemId + ", userId: " + userId);
            // Depending on strictness, could throw new RuntimeException("购物车项删除失败...");
        }
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, userId);
        cartItemMapper.delete(queryWrapper);
    }

    @Override
    public List<CartItem> getCheckedCartItems(Long userId) {
        LambdaQueryWrapper<CartItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CartItem::getUserId, userId)
                    .eq(CartItem::getCheckedStatus, 1) // 只获取选中的
                    .orderByDesc(CartItem::getUpdatedAt);
        List<CartItem> cartItems = cartItemMapper.selectList(queryWrapper);

        return cartItems.stream()
                        .map(item -> {
                            Product product = productService.getById(item.getProductId());
                            return enrichCartItemWithProductDetails(item, product);
                        })
                        .filter(item -> item.getProductName() != null)
                        .collect(Collectors.toList());
    }

    private CartItem enrichCartItemWithProductDetails(CartItem cartItem, Product product) {
        if (cartItem == null) return null; // Should not happen if called internally with valid cartItem
        if (product != null) {
            cartItem.setProductName(product.getName());
            cartItem.setProductMainImage(product.getMainImageUrl());
            cartItem.setCurrentPrice(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice());
            cartItem.setCurrentStock(product.getStock());
        } else {
            log.warn("购物车项关联的商品信息未找到, ProductID: " + (cartItem.getProductId() != null ? cartItem.getProductId() : "null") + ". CartItemID: " + cartItem.getId());
            // To prevent returning null product details which might break frontend:
            // cartItem.setProductName("商品已失效"); // Or some other placeholder
            // cartItem.setCurrentPrice(BigDecimal.ZERO);
            // cartItem.setCurrentStock(0);
            // Or, the filter in getCartItems/getCheckedCartItems will remove this item.
        }
        return cartItem;
    }
}

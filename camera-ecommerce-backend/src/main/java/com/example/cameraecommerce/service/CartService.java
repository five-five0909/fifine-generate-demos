package com.example.cameraecommerce.service;

import com.example.cameraecommerce.dto.request.CartItemAddRequest;
import com.example.cameraecommerce.dto.request.CartItemUpdateRequest;
import com.example.cameraecommerce.entity.CartItem; // 使用Entity，VO转换在Service层或Controller层完成
import java.util.List;

public interface CartService {
    CartItem addItemToCart(Long userId, CartItemAddRequest request);
    List<CartItem> getCartItems(Long userId); // 返回的CartItem应包含商品详情
    CartItem updateCartItemQuantity(Long userId, Long cartItemId, CartItemUpdateRequest request);
    void deleteCartItem(Long userId, Long cartItemId);
    void clearCart(Long userId); // 清空购物车
    List<CartItem> getCheckedCartItems(Long userId); // 获取选中的购物车项
}

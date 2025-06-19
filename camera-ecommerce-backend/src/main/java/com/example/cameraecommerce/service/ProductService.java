package com.example.cameraecommerce.service;

import com.example.cameraecommerce.entity.Product;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProductService extends IService<Product> {
    Page<Product> listProductsByFilter(Long categoryId, String keyword, Page<Product> page);
    Product getProductDetails(Long id);
    boolean decreaseStock(Long productId, Integer quantity);
    boolean increaseStock(Long productId, Integer quantity); // 用于取消订单等场景
}

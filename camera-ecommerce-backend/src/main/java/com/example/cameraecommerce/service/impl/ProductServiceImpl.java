package com.example.cameraecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cameraecommerce.entity.Product;
import com.example.cameraecommerce.mapper.ProductMapper;
import com.example.cameraecommerce.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Page<Product> listProductsByFilter(Long categoryId, String keyword, Page<Product> page) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getStatus, 1); // 只查询上架商品

        if (categoryId != null && categoryId > 0) {
            queryWrapper.eq(Product::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            // 构建 OR 查询条件: name LIKE %keyword% OR subtitle LIKE %keyword% OR tags LIKE %keyword%
            queryWrapper.and(qw -> qw.like(Product::getName, keyword)
                                     .or().like(Product::getSubtitle, keyword)
                                     .or().like(Product::getTags, keyword));
        }
        queryWrapper.orderByDesc(Product::getCreatedAt); // 默认按创建时间降序
        return page(page, queryWrapper);
    }

    @Override
    public Product getProductDetails(Long id) {
        Product product = getById(id);
        // 可以在这里处理促销逻辑，计算discountPrice等
        // 例如: if (product != null && product.getDiscountPrice() == null) { product.setDiscountPrice(product.getPrice()); }
        // 确保只返回上架商品详情，除非是特定管理接口
        if (product != null && product.getStatus() != null && product.getStatus() == 0) { // 0表示下架
             // Depending on requirements, either return null or throw an exception for public access
             // For admin access, this check might be bypassed.
             // return null;
             // throw new RuntimeException("商品已下架");
        }
        return product;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Long productId, Integer quantity) {
        Product product = getById(productId); // First, check if product exists
        if (product == null) {
            throw new RuntimeException("商品不存在, ID: " + productId);
        }
        if (product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足, ID: " + productId + ", 需要: " + quantity + ", 现有: " + product.getStock());
        }
        // baseMapper是ServiceImpl注入的ProductMapper实例
        // 使用乐观锁思想，在更新时再次检查库存
        int affectedRows = baseMapper.update(null,
            new LambdaQueryWrapper<Product>()
                .eq(Product::getId, productId)
                .ge(Product::getStock, quantity) // WHERE stock >= quantity
                .setSql("stock = stock - " + quantity)
        );
        if (affectedRows == 0) {
            // 这可能意味着在getById和update之间库存发生了变化
            throw new RuntimeException("库存扣减失败，可能库存已不足或商品信息已变更, ID: " + productId);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean increaseStock(Long productId, Integer quantity) {
        Product product = getById(productId);
        if (product == null) {
            log.warn("尝试增加库存失败：商品不存在, productId: " + productId);
            return false;
        }
        int affectedRows = baseMapper.update(null,
            new LambdaQueryWrapper<Product>()
                .eq(Product::getId, productId)
                .setSql("stock = stock + " + quantity)
        );
        return affectedRows > 0;
    }
}

package com.example.cameraecommerce.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cameraecommerce.entity.Product;
import com.example.cameraecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品管理", description = "商品列表查询与详情获取")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "分页查询商品列表")
    @GetMapping
    public ResponseEntity<Page<Product>> listProducts(
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "当前页码 (从1开始)") @RequestParam(defaultValue = "1") int current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size
    ) {
        Page<Product> page = new Page<>(current, size);
        Page<Product> productPage = productService.listProductsByFilter(categoryId, keyword, page);
        return ResponseEntity.ok(productPage);
    }

    @Operation(summary = "获取单个商品详情")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductDetails(id);

        if (product == null || (product.getStatus() != null && product.getStatus() == 0)) { // 0 means unavailable/delisted
            return ResponseEntity.notFound().build();
        }

        // Optional: Increment view count asynchronously or if performance is not critical
        // Consider moving this logic to service layer if it involves more than just a simple increment
        // try {
        //     Product productToUpdate = new Product();
        //     productToUpdate.setId(id);
        //     productToUpdate.setViewCount(product.getViewCount() + 1);
        //     productService.updateById(productToUpdate); // Only updates non-null fields by default with MP
        // } catch (Exception e) {
        //     // logger.error("Error incrementing view count for product {}", id, e);
        // }

        return ResponseEntity.ok(product);
    }
}

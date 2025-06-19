package com.example.cameraecommerce.controller;

import com.example.cameraecommerce.entity.ProductCategory;
import com.example.cameraecommerce.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper; // For direct use if not calling service method

import java.util.List;

@Tag(name = "商品分类管理", description = "获取商品分类信息")
@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Operation(summary = "获取所有商品分类列表")
    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories(
            @Parameter(description = "父分类ID，查询其子分类，不传或传0则查询顶级分类") @RequestParam(required = false, defaultValue = "0") Long parentId
    ) {
        // Using the service method which already implements this logic
        List<ProductCategory> categories = productCategoryService.listByParentId(parentId);
        // If a tree structure is needed, it should ideally be handled in the service layer
        // or by a dedicated DTO/ViewModel transformation.
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "获取单个商品分类详情")
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Long id) {
        ProductCategory category = productCategoryService.getById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    // Backend admin functions - Example: Create Category (requires auth & role check in real project)
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Assuming an ADMIN role exists
    // public ResponseEntity<ProductCategory> createCategory(@Valid @RequestBody ProductCategory category) {
    //     // Additional validation for category fields might be needed
    //     category.setCreatedAt(java.time.LocalDateTime.now());
    //     category.setUpdatedAt(java.time.LocalDateTime.now());
    //     productCategoryService.save(category);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(category);
    // }
}

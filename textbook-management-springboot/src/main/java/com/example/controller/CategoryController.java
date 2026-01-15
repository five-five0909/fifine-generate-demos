package com.example.controller;

import com.example.entity.Category;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类控制器
 */
@Tag(name = "分类管理", description = "分类相关接口")
@Controller
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类（API）
     */
    @Operation(summary = "获取所有分类", description = "获取所有教材分类列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回分类列表")
    })
    @GetMapping("/all")
    @ResponseBody
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 添加分类
     */
    @Operation(summary = "添加分类", description = "添加新的教材分类")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "添加成功/失败")
    })
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestBody Category category) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查分类名称是否已存在
            Category existCategory = categoryService.getCategoryByName(category.getName());
            if (existCategory != null) {
                result.put("success", false);
                result.put("message", "分类名称已存在");
                return result;
            }
            
            boolean success = categoryService.addCategory(category);
            result.put("success", success);
            result.put("message", success ? "添加成功" : "添加失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新分类
     */
    @Operation(summary = "更新分类", description = "更新教材分类信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功/失败")
    })
    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody Category category) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查分类名称是否已被其他分类使用
            Category existCategory = categoryService.getCategoryByName(category.getName());
            if (existCategory != null && !existCategory.getId().equals(category.getId())) {
                result.put("success", false);
                result.put("message", "分类名称已存在");
                return result;
            }
            
            boolean success = categoryService.updateCategory(category);
            result.put("success", success);
            result.put("message", success ? "更新成功" : "更新失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除分类
     */
    @Operation(summary = "删除分类", description = "根据ID删除教材分类")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "删除成功/失败")
    })
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> delete(@Parameter(description = "分类ID", required = true) @PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = categoryService.deleteCategory(id);
            result.put("success", success);
            result.put("message", success ? "删除成功" : "删除失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取分类详情
     */
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详细信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回分类详情")
    })
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Category getDetail(@Parameter(description = "分类ID", required = true) @PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }
} 
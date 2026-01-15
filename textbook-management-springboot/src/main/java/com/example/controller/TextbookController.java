package com.example.controller;

import com.example.entity.Textbook;
import com.example.service.TextbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教材控制器
 */
@Tag(name = "教材管理", description = "教材相关接口")
@Controller
@RequestMapping("/api/textbook")
public class TextbookController {

    @Autowired
    private TextbookService textbookService;

    /**
     * 获取所有教材（API）
     */
    @Operation(summary = "获取所有教材", description = "获取所有教材列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回教材列表")
    })
    @GetMapping("/all")
    @ResponseBody
    public List<Textbook> getAllTextbooks() {
        return textbookService.getAllTextbooks();
    }

    /**
     * 添加教材
     */
    @Operation(summary = "添加教材", description = "添加新的教材")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "添加成功/失败")
    })
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestBody Textbook textbook) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = textbookService.addTextbook(textbook);
            result.put("success", success);
            result.put("message", success ? "添加成功" : "添加失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新教材
     */
    @Operation(summary = "更新教材", description = "更新教材信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功/失败")
    })
    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody Textbook textbook) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = textbookService.updateTextbook(textbook);
            result.put("success", success);
            result.put("message", success ? "更新成功" : "更新失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除教材
     */
    @Operation(summary = "删除教材", description = "根据ID删除教材")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "删除成功/失败")
    })
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> delete(@Parameter(description = "教材ID", required = true) @PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = textbookService.deleteTextbook(id);
            result.put("success", success);
            result.put("message", success ? "删除成功" : "删除失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据条件搜索教材
     */
    @Operation(summary = "搜索教材", description = "根据条件搜索教材列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回教材列表")
    })
    @GetMapping("/search")
    @ResponseBody
    public List<Textbook> search(@Parameter(description = "教材名称", required = false) @RequestParam(required = false) String title,
                                @Parameter(description = "作者", required = false) @RequestParam(required = false) String author,
                                @Parameter(description = "分类ID", required = false) @RequestParam(required = false) Integer categoryId) {
        return textbookService.getTextbooksByCondition(title, author, categoryId);
    }

    /**
     * 获取教材详情
     */
    @Operation(summary = "获取教材详情", description = "根据ID获取教材详细信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回教材详情")
    })
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Textbook getDetail(@Parameter(description = "教材ID", required = true) @PathVariable Integer id) {
        return textbookService.getTextbookById(id);
    }

    /**
     * 更新库存
     */
    @Operation(summary = "更新库存", description = "根据ID更新教材库存")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "库存更新成功/失败")
    })
    @PostMapping("/updateStock")
    @ResponseBody
    public Map<String, Object> updateStock(@Parameter(description = "教材ID", required = true) @RequestParam Integer id, @Parameter(description = "库存", required = true) @RequestParam Integer stock) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = textbookService.updateStock(id, stock);
            result.put("success", success);
            result.put("message", success ? "库存更新成功" : "库存更新失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "库存更新失败：" + e.getMessage());
        }
        return result;
    }
} 
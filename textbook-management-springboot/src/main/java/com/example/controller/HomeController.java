package com.example.controller;

import com.example.service.BorrowRecordService;
import com.example.service.CategoryService;
import com.example.service.TextbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 */
@Tag(name = "首页", description = "首页相关接口")
@Controller
public class HomeController {

    @Autowired
    private TextbookService textbookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BorrowRecordService borrowRecordService;

    /**
     * 首页 - 返回index.html
     */
    @Operation(summary = "首页页面", description = "返回index.html页面")
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    /**
     * 获取统计数据（API）
     */
    @Operation(summary = "获取统计数据", description = "获取首页统计数字，包括教材总数、分类数量、借阅记录、逾期记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回统计数据")
    })
    @GetMapping("/api/statistics")
    @ResponseBody
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            statistics.put("textbookCount", textbookService.getTextbookCount());
            statistics.put("categoryCount", categoryService.getAllCategories().size());
            statistics.put("borrowRecordCount", borrowRecordService.getBorrowRecordCount());
            statistics.put("overdueCount", borrowRecordService.getOverdueRecords().size());
        } catch (Exception e) {
            e.printStackTrace();
            // 如果发生错误，返回0
            statistics.put("textbookCount", 0);
            statistics.put("categoryCount", 0);
            statistics.put("borrowRecordCount", 0);
            statistics.put("overdueCount", 0);
        }
        return statistics;
    }
} 
package com.example.controller;

import com.example.entity.BorrowRecord;
import com.example.service.BorrowRecordService;
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
 * 借阅记录控制器
 */
@Tag(name = "借阅管理", description = "借阅记录相关接口")
@Controller
@RequestMapping("/api/borrow")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    /**
     * 获取所有借阅记录（API）
     */
    @Operation(summary = "获取所有借阅记录", description = "获取所有借阅记录列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回借阅记录列表")
    })
    @GetMapping("/all")
    @ResponseBody
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordService.getAllBorrowRecords();
    }

    /**
     * 添加借阅记录（借阅教材）
     */
    @Operation(summary = "添加借阅记录", description = "添加新的借阅记录（借阅教材）")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "借阅成功/失败")
    })
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestBody BorrowRecord borrowRecord) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = borrowRecordService.borrowTextbook(borrowRecord);
            result.put("success", success);
            result.put("message", success ? "借阅成功" : "借阅失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "借阅失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新借阅记录
     */
    @Operation(summary = "更新借阅记录", description = "更新借阅记录信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功/失败")
    })
    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody BorrowRecord borrowRecord) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = borrowRecordService.updateBorrowRecord(borrowRecord);
            result.put("success", success);
            result.put("message", success ? "更新成功" : "更新失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除借阅记录
     */
    @Operation(summary = "删除借阅记录", description = "根据ID删除借阅记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "删除成功/失败")
    })
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> delete(@Parameter(description = "借阅记录ID", required = true) @PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = borrowRecordService.deleteBorrowRecord(id);
            result.put("success", success);
            result.put("message", success ? "删除成功" : "删除失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 归还教材
     */
    @Operation(summary = "归还教材", description = "根据ID归还教材")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "归还成功/失败")
    })
    @PostMapping("/return/{id}")
    @ResponseBody
    public Map<String, Object> returnTextbook(@Parameter(description = "借阅记录ID", required = true) @PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = borrowRecordService.returnTextbook(id);
            result.put("success", success);
            result.put("message", success ? "归还成功" : "归还失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "归还失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据条件搜索借阅记录
     */
    @Operation(summary = "搜索借阅记录", description = "根据条件搜索借阅记录列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回借阅记录列表")
    })
    @GetMapping("/search")
    @ResponseBody
    public List<BorrowRecord> search(@Parameter(description = "学生姓名", required = false) @RequestParam(required = false) String studentName,
                                   @Parameter(description = "学号", required = false) @RequestParam(required = false) String studentNumber,
                                   @Parameter(description = "状态", required = false) @RequestParam(required = false) String status) {
        return borrowRecordService.getBorrowRecordsByCondition(studentName, studentNumber, status);
    }

    /**
     * 获取借阅记录详情
     */
    @Operation(summary = "获取借阅记录详情", description = "根据ID获取借阅记录详细信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回借阅记录详情")
    })
    @GetMapping("/detail/{id}")
    @ResponseBody
    public BorrowRecord getDetail(@Parameter(description = "借阅记录ID", required = true) @PathVariable Integer id) {
        return borrowRecordService.getBorrowRecordById(id);
    }

    /**
     * 根据学生学号查询借阅记录
     */
    @Operation(summary = "根据学号查询借阅记录", description = "根据学生学号查询借阅记录列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回借阅记录列表")
    })
    @GetMapping("/student/{studentNumber}")
    @ResponseBody
    public List<BorrowRecord> getBorrowRecordsByStudent(@Parameter(description = "学生学号", required = true) @PathVariable String studentNumber) {
        return borrowRecordService.getBorrowRecordsByStudentNumber(studentNumber);
    }

    /**
     * 获取逾期记录（API）
     */
    @Operation(summary = "获取逾期记录", description = "获取所有逾期借阅记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功返回逾期借阅记录列表")
    })
    @GetMapping("/overdue/api")
    @ResponseBody
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecordService.getOverdueRecords();
    }
} 
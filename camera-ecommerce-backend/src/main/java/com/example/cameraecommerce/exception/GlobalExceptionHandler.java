package com.example.cameraecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST) // Redundant if ResponseEntity sets status
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("参数校验失败: {}", errors, ex); // Include exception for stack trace if needed
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    // @ResponseStatus(HttpStatus.FORBIDDEN) // Redundant
    public ResponseEntity<Map<String,String>> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("访问被拒绝: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", "访问被拒绝");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    // @ResponseStatus(HttpStatus.NOT_FOUND) // Redundant
    public ResponseEntity<Map<String,String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("资源未找到: {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", "资源未找到");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalStateException.class) // Catching IllegalStateException from getCurrentAuthenticatedUser
    public ResponseEntity<Map<String,String>> handleIllegalStateException(IllegalStateException ex) {
        logger.warn("非法状态异常 (通常由于认证问题): {}", ex.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("error", "认证失败或状态不正确");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    // Generic exception handler for anything not caught by more specific handlers
    @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Redundant
    public ResponseEntity<Map<String,String>> handleAllUncaughtException(Exception ex) {
        logger.error("发生未捕获的服务器内部错误: ", ex); // Log full stack trace for general exceptions
        Map<String, String> response = new HashMap<>();
        response.put("error", "服务器内部错误");
        response.put("message", "服务器遇到意外情况，请稍后再试。");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Example for a custom business exception
    // @ExceptionHandler(CustomBusinessException.class)
    // public ResponseEntity<Map<String,String>> handleCustomBusinessException(CustomBusinessException ex) {
    //     logger.warn("业务逻辑错误: {}", ex.getMessage(), ex);
    //     Map<String, String> response = new HashMap<>();
    //     response.put("error", "业务逻辑错误");
    //     response.put("message", ex.getMessage());
    //     return ResponseEntity.badRequest().body(response);
    // }
}

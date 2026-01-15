package com.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.dao")
@OpenAPIDefinition(
        info = @Info(
                title = "教材管理系统 API 文档",
                version = "v1.0.0",
                description = "本文档详细描述了教材管理系统的后端API接口。"
        )
)
public class TextbookManagementSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextbookManagementSpringbootApplication.class, args);
    }

}

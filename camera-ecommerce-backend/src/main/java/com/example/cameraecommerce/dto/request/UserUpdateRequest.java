package com.example.cameraecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email; // 允许更新邮箱，但需要考虑邮箱唯一性验证

    @Size(max = 20, message = "电话号码长度不能超过20个字符")
    private String phoneNumber;

    private String avatarUrl;
}

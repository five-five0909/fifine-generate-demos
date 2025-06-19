package com.example.cameraecommerce.service;

import com.example.cameraecommerce.dto.request.PasswordUpdateRequest;
import com.example.cameraecommerce.dto.request.UserUpdateRequest;
import com.example.cameraecommerce.entity.User;

public interface UserService {
    User getCurrentUser();
    User getUserById(Long id);
    User updateUserInfo(UserUpdateRequest userUpdateRequest);
    void updateUserPassword(PasswordUpdateRequest passwordUpdateRequest);
}

package com.example.cameraecommerce.service;

import com.example.cameraecommerce.dto.request.LoginRequest;
import com.example.cameraecommerce.dto.request.RegisterRequest;
import com.example.cameraecommerce.dto.response.JwtResponse;
import com.example.cameraecommerce.entity.User;

public interface AuthService {
    User register(RegisterRequest registerRequest);
    JwtResponse login(LoginRequest loginRequest);
}

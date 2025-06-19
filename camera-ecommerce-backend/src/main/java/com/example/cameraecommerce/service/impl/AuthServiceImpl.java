package com.example.cameraecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cameraecommerce.dto.request.LoginRequest;
import com.example.cameraecommerce.dto.request.RegisterRequest;
import com.example.cameraecommerce.dto.response.JwtResponse;
import com.example.cameraecommerce.entity.User;
import com.example.cameraecommerce.mapper.UserMapper;
import com.example.cameraecommerce.service.AuthService;
import com.example.cameraecommerce.service.JwtUserDetailsService;
import com.example.cameraecommerce.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;


    @Override
    @Transactional
    public User register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, registerRequest.getUsername())) > 0) {
            throw new RuntimeException("错误: 用户名已存在!");
        }

        // 检查邮箱是否已存在
        if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, registerRequest.getEmail())) > 0) {
            throw new RuntimeException("错误: 邮箱已注册!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setNickname(registerRequest.getNickname() != null ? registerRequest.getNickname() : registerRequest.getUsername());
        user.setStatus(1); // 默认启用
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);
        return user;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (DisabledException e) {
            throw new RuntimeException("用户已禁用", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("用户名或密码无效", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginRequest.getUsername())
                .or()
                .eq(User::getEmail, loginRequest.getUsername())); // 支持邮箱登录

        if (user == null) {
            // This case should ideally not be reached if authentication is successful,
            // as loadUserByUsername would have thrown an exception.
            // However, as a safeguard:
            throw new RuntimeException("登录后无法检索用户信息");
        }

        return new JwtResponse(token, user.getId(), user.getUsername(), user.getEmail());
    }
}

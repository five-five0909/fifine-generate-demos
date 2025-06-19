package com.example.cameraecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cameraecommerce.dto.request.PasswordUpdateRequest;
import com.example.cameraecommerce.dto.request.UserUpdateRequest;
import com.example.cameraecommerce.entity.User;
import com.example.cameraecommerce.mapper.UserMapper;
import com.example.cameraecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null; // 或者抛出异常
        }
        String username = authentication.getName();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            // This might happen if the user was deleted after token issuance but before this call.
            // Or if the token username somehow doesn't match a DB record.
            throw new UsernameNotFoundException("当前登录用户未在数据库中找到: " + username);
        }
        user.setPassword(null); // 不返回密码
        return user;
    }

    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUserInfo(UserUpdateRequest userUpdateRequest) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录或认证失败");
        }

        boolean updated = false;
        if (StringUtils.hasText(userUpdateRequest.getNickname())) {
            currentUser.setNickname(userUpdateRequest.getNickname());
            updated = true;
        }
        if (StringUtils.hasText(userUpdateRequest.getPhoneNumber())) {
            currentUser.setPhoneNumber(userUpdateRequest.getPhoneNumber());
            updated = true;
        }
        if (StringUtils.hasText(userUpdateRequest.getAvatarUrl())) {
            currentUser.setAvatarUrl(userUpdateRequest.getAvatarUrl());
            updated = true;
        }
        if (StringUtils.hasText(userUpdateRequest.getEmail())) {
            // 如果允许修改邮箱，需要检查邮箱是否已被其他用户使用
            if (!currentUser.getEmail().equals(userUpdateRequest.getEmail())) {
                 if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, userUpdateRequest.getEmail())
                        .ne(User::getId, currentUser.getId())) > 0) {
                    throw new RuntimeException("错误: 邮箱已存在!");
                }
                currentUser.setEmail(userUpdateRequest.getEmail());
                updated = true;
            }
        }

        if (updated) {
            currentUser.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(currentUser);
        }
        currentUser.setPassword(null);
        return currentUser;
    }

    @Override
    @Transactional
    public void updateUserPassword(PasswordUpdateRequest passwordUpdateRequest) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录或认证失败");
        }

        // 重新从数据库获取用户信息，确保密码是最新的
        User userFromDb = userMapper.selectById(currentUser.getId());
        if (userFromDb == null) {
             throw new UsernameNotFoundException("用户不存在");
        }

        if (!passwordEncoder.matches(passwordUpdateRequest.getOldPassword(), userFromDb.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }

        userFromDb.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        userFromDb.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(userFromDb);
    }
}

package com.example.cameraecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cameraecommerce.entity.User;
import com.example.cameraecommerce.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // 实际项目中应加载用户真实权限

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, username)); // 也允许邮箱登录
            if (user == null) {
                throw new UsernameNotFoundException("用户未找到，用户名或邮箱: " + username);
            }
        }
        if (user.getStatus() == null || user.getStatus() == 0) { // 假设0为禁用
            throw new UsernameNotFoundException("用户账号已被禁用: " + username);
        }
        // 第三个参数是权限列表，你需要根据你的用户角色和权限来填充
        // 对于实际的权限，通常会有一个 Role 表和 UserRole 关联表
        // 然后在这里查询用户的角色，并将其转换为 GrantedAuthority 对象列表
        // List<GrantedAuthority> authorities = user.getRoles().stream()
        //    .map(role -> new SimpleGrantedAuthority(role.getName()))
        //    .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>()); // 示例：使用空权限列表，实际应加载用户真实权限
    }
}

package com.toyshop.bean;

// 用户实体类
public class User {
    private int userId;       // 用户ID
    private String username;  // 用户名
    private String password;  // 密码
    private int role;         // 角色: 1 代表普通用户, 2 代表管理员

    // Getter 和 Setter 方法
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}

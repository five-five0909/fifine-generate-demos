package com.toyshop.bean;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private int userId;
    private String username;
    private String password;
    private int role; // 1 for normal user, 2 for admin

    // Default constructor
    public User() {
    }

    // Parameterized constructor (optional, but can be useful)
    public User(int userId, String username, String password, int role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Parameterized constructor without userId (for registration)
    public User(String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
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

    // toString() method (optional, for debugging)
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + "[PROTECTED]" + '\'' + // Avoid logging password
                ", role=" + role +
                '}';
    }
}

package com.qst.itoffer.bean;

/**
 * 用户实体类，包含用户凭证、角色和状态信息。
 */
public class User {
    // 用户ID
    private int userId;
    // 登录名
    private String userLogname;
    // 密码
    private String userPwd;
    // 真实姓名
    private String userRealname;
    // 邮箱地址
    private String userEmail;
    // 用户角色（1: 普通用户，2: 企业管理员，3: 系统管理员）
    private int userRole;
    // 用户状态（1: 启用，0: 禁用）
    private int userState;

    /**
     * 默认构造方法。
     */
    public User() {}

    /**
     * 除userId外的全字段构造方法。
     */
    public User(String userLogname, String userPwd, String userRealname, String userEmail, int userRole, int userState) {
        this.userLogname = userLogname;
        this.userPwd = userPwd;
        this.userRealname = userRealname;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.userState = userState;
    }

    /**
     * 包含userId的全字段构造方法。
     */
    public User(int userId, String userLogname, String userPwd, String userRealname, String userEmail, int userRole, int userState) {
        this.userId = userId;
        this.userLogname = userLogname;
        this.userPwd = userPwd;
        this.userRealname = userRealname;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.userState = userState;
    }

    /**
     * 获取用户ID。
     */
    public int getUserId() { return userId; }
    /**
     * 设置用户ID。
     */
    public void setUserId(int userId) { this.userId = userId; }
    /**
     * 获取登录名。
     */
    public String getUserLogname() { return userLogname; }
    /**
     * 设置登录名。
     */
    public void setUserLogname(String userLogname) { this.userLogname = userLogname; }
    /**
     * 获取密码。
     */
    public String getUserPwd() { return userPwd; }
    /**
     * 设置密码。
     */
    public void setUserPwd(String userPwd) { this.userPwd = userPwd; }
    /**
     * 获取真实姓名。
     */
    public String getUserRealname() { return userRealname; }
    /**
     * 设置真实姓名。
     */
    public void setUserRealname(String userRealname) { this.userRealname = userRealname; }
    /**
     * 获取邮箱地址。
     */
    public String getUserEmail() { return userEmail; }
    /**
     * 设置邮箱地址。
     */
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    /**
     * 获取用户角色。
     */
    public int getUserRole() { return userRole; }
    /**
     * 设置用户角色。
     */
    public void setUserRole(int userRole) { this.userRole = userRole; }
    /**
     * 获取用户状态。
     */
    public int getUserState() { return userState; }
    /**
     * 设置用户状态。
     */
    public void setUserState(int userState) { this.userState = userState; }
}
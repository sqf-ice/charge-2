package com.clouyun.charge.modules.demo.vo;

/**
 * 描述: 用户信息
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: 00063587
 * 版本: 2.0.0
 * 创建日期:2017年09月04日
 */
public class DemoUser {

    /**用户ID*/
    private  Integer userId;
    /**用户名*/
    private String userName;
    /**用户密码*/
    private String userPwd;
    /**性别*/
    private String userGender;
    /**手机号码*/
    private String userPhone;
    /**邮箱*/
    private String userEmail;
    /**住址*/
    private String userAddr;
    /**用户状态*/
    private String userStatus;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userAddr='" + userAddr + '\'' +
                ", userStatus='" + userStatus + '\'' +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
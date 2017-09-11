package com.clouyun.charge.common.domain;

import java.io.Serializable;
import java.util.Calendar;

public class LoginLock implements Serializable {

    private static final long serialVersionUID = -1374924976435702821L;

    /**
     * 登录错误次数
     */
    private int failTimes;

    /**
     * 登录用户ID
     */
    private Integer userId;

    private String loginName;

    /**
     * 最后一次登录失败时间
     */
    private Calendar lastFailTime;

    /**
     * 最先一次失败时间
     */
    private Calendar firstFailTime;

    public int getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(int failTimes) {
        this.failTimes = failTimes;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Calendar getLastFailTime() {
        return lastFailTime;
    }

    public void setLastFailTime(Calendar lastFailTime) {
        this.lastFailTime = lastFailTime;
    }

    public Calendar getFirstFailTime() {
        return firstFailTime;
    }

    public void setFirstFailTime(Calendar firstFailTime) {
        this.firstFailTime = firstFailTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}

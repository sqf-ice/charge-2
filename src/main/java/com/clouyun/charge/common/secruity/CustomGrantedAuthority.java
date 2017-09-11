package com.clouyun.charge.common.secruity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * 描述: 实现{@link GrantedAuthority}接口，用来封装用户角色和权限
 * <br>不使用{@link org.springframework.security.core.authority.SimpleGrantedAuthority}默认实现，定义了final，不可反序列化
 * <br>不能反序列化，自定义实现为了生成无参构造器和setter方法反序列化
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年07月04日
 */
public class CustomGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 410L;
    private String role;

    public CustomGrantedAuthority() {
    }

    public CustomGrantedAuthority(String role) {
        Assert.hasText(role, "CustomGrantedAuthority granted authority textual representation is required");
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuthority() {

        return this.role;
    }

    public void setAuthority(String role) {
        this.role = role;
    }

    public boolean equals(Object obj) {
        return this == obj ? true : (obj instanceof CustomGrantedAuthority ? this.role.equals(((CustomGrantedAuthority) obj).role) : false);
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }
}
package com.clouyun.charge.common.secruity;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

/**
 * 描述:所有参数不定义final，为了能够反序列化，自定义实现为了生成无参构造器和setter方法反序列化
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年07月04日
 */
public class JwtUser implements UserDetails {
    private int id;
    private String username;
    private transient String password;
    private String email;

    /*private final Collection<? extends GrantedAuthority> authorities;*/
    private List<CustomGrantedAuthority> authorities;
    private Date lastPasswordResetDate;

    private transient List roles;
    private transient List perms;
    private transient List menus;
    private transient String first;

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(List<CustomGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public void setPerms(List perms) {
        this.perms = perms;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }
    // 转json序列化时不需要
    @JsonIgnore
    public List getRoles() {
        return roles;
    }
    // 转json序列化时不需要
    @JsonIgnore
    public List getPerms() {
        return perms;
    }

    private transient String salt;

    private String userState;

    public String getSalt() {
        return salt;
    }

    public String getUserState() {
        return userState;
    }
    public JwtUser(){}
    public JwtUser(
            int id,
            String username,
            String password,
            String email,
           /* Collection<? extends GrantedAuthority> authorities,*/ List<CustomGrantedAuthority> authorities,
            Date lastPasswordResetDate,
            List roles, List perms,
            String salt, String userState) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.lastPasswordResetDate = lastPasswordResetDate;

        this.roles = roles;
        this.perms = perms;

        this.salt = salt;
        this.userState = userState;
    }

    //返回分配给用户的角色列表
   /* @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }*/

    @Override
    public List<CustomGrantedAuthority> getAuthorities() {
        return authorities;
    }

    //@JsonIgnore
    public int getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 账户是否未过期
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未锁定
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 密码是否未过期
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否激活
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 这个是自定义的，返回上次密码重置日期
    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    @JsonIgnore
    public List getMenus() {
        return menus;
    }

    public void setMenus(List menus) {
        this.menus = menus;
    }

    @JsonIgnore
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public DataVo toDataVo() {
        String json = JsonUtils.toJson(this);
        DataVo dataVo = JsonUtils.toBean(json, DataVo.class);
        return dataVo;
    }
}
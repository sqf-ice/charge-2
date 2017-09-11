package com.clouyun.charge.common.secruity;

import com.clouyun.boot.common.domain.DataVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    /*public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                mapToGrantedAuthorities(user.getRoles()),
                user.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }*/


    /**
     * 测试用的,指定了加密算法 去加密密码
     *
     * @param userVo
     * @return
     */
    public static JwtUser create(DataVo userVo) {
        List roles = userVo.getList("roles");

        userVo.put("lastPasswordResetDate", new Date());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPwd = "admin";//userVo.getString("password");
        String encodePwd = passwordEncoder.encode(rawPwd);
        userVo.put("password", encodePwd);
        Date lastPasswordResetDate = userVo.getDate("updateTime");

        return null;
        /*return new JwtUser(
                userVo.getString("id"),
                userVo.getString("loginName"),
                userVo.getString("password"),
                userVo.getString("email"),
                mapToGrantedAuthorities(roles),
                lastPasswordResetDate, null, null, null
        );*/
    }

    public static JwtUser build(DataVo userVo) {

        /*PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPwd = "admin";//userVo.getString("password");
        String encodePwd = passwordEncoder.encode(rawPwd);
        userVo.put("password", encodePwd);*/
        List roles = userVo.getList("roles");
        List perms = userVo.getList("perms");
        return new JwtUser(
                userVo.getInt("id"),
                userVo.getString("loginName"),
                userVo.getString("password"),
                userVo.getString("email"),
                mapToGrantedAuthorities(roles,perms),
                userVo.getDate("updateTime"),
                userVo.getList("roles"), userVo.getList("perms"),
                userVo.getString("salt"),
                userVo.getString("userState")
        );
    }

    /**
     * 将角色和权限集合都返回
     * @param roles
     * @param authorities
     * @return
     */
    private static List<CustomGrantedAuthority> mapToGrantedAuthorities(List roles,List authorities) {
        List<CustomGrantedAuthority> authoritys = new ArrayList<>();
        for (Object role : roles) {
            authoritys.add(new CustomGrantedAuthority("ROLE_" + role.toString()));
        }
        for (Object authoritie : authorities) {
            authoritys.add(new CustomGrantedAuthority(authoritie.toString()));
        }
        return authoritys;
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPwd = "admin";//userVo.getString("password");
        String encodePwd = passwordEncoder.encode(rawPwd);
        System.out.println(encodePwd);
    }
}
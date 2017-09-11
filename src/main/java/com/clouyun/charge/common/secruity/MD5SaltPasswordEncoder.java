package com.clouyun.charge.common.secruity;

import com.clouyun.charge.common.helper.UserHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: Administrator <Administrator@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年06月09日
 */
@Component
public class MD5SaltPasswordEncoder implements PasswordEncoder {


    public String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        String rawSalt = UserHelper.decodeSalt(salt);//将加密盐解密
        return UserHelper.encoderPassword(rawPassword.toString(), rawSalt);//MD5盐加密
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String rawSalt = UserHelper.decodeSalt(salt);//将加密盐解密
        return UserHelper.validatePassword(rawPassword.toString(), rawSalt, encodedPassword);
    }


}

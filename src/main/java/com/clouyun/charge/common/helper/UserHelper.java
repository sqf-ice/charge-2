package com.clouyun.charge.common.helper;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.utils.safety.AES;
import com.google.common.collect.Lists;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.util.List;
import java.util.Map;

/**
 * 描述: 用户帮助类
 * 版权: Copyright (c) 2016
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2016年12月8日
 */
public class UserHelper {
    // 2.0第一版要过滤的菜单
    public static List<Integer> filterPerms = Lists.newArrayList(7,8,701,702,801,802,803,804,601,602,603,604,10,1001,1002,504,407,203);//,102,102001,102002,102003,102004,102005
    //public static List<Integer> filterPerms = Lists.newArrayList();

    /**
     * generate rawSalt
     *
     *
     * @param user
     * @return
     */
    public static String buildRawSalt(Map user) {
        DataVo userData = new DataVo(user);

        int userId = userData.getInt("id");
        String loginName = userData.getString("loginName");
        String password = userData.getString("password");
        String salt = userId + loginName + System.currentTimeMillis();
        return salt;
    }

    public static String encodeSalt(String rawSalt) {
        return new AES().encrypt(rawSalt);
    }

    public static String decodeSalt(String enSalt) {
        return new AES().decrypt(enSalt);
    }

    public static String encoderPassword(String rawPass, String salt) {
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String result = md5.encodePassword(rawPass, salt);
        return result;
    }

    public static boolean validatePassword(String rawPass, String salt, String dbPass) {
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String result = md5.encodePassword(rawPass, salt);
        return dbPass.equals(result);
    }

    public static boolean decodePassword(String rawPass, String salt, String dbPass) {
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String result = md5.encodePassword(rawPass, salt);
        return dbPass.equals(result);
    }
    // ---------Session会话---------

    /**
     * 登陆的用户存入Session中key值
     */
    public static final String SESSION_LOGIN_USER = "dmsuser";

    /**
     * 登陆随机数
     */
    public static Integer RANDOM_NUM = 0;

    // ---------超级用户---------

    /**
     * 超级用户名称
     */
    public static final String ADMIN_NAME = "admin";

    /**
     * 超级用户密码
     */
    public static final String ADMIN_PWD = "admin";

    // ---------用户类型---------

    /**
     * 系统用户
     */
    public static int USER_TYPE_SYSTEM = 1;

    /**
     * 合约企业用户
     */
    public static int USER_TYPE_COMPANY = 2;


    /**
     * 重试次数
     */
    public static int worongCont = 5;

    /**
     * 等待时间
     */
    public static int waitMins = 15;

    // ---------用户状态---------

    /**
     * 正常
     */
    public static String USER_STATE_ENABLED = "01";

    /**
     * 已禁用
     */
    public static String USER_STATE_DISABLED = "02";

    /**
     * 已删除
     */
    public static String USER_STATE_REMOVED = "03";

    /**
     * 已锁定
     */
    public static String USER_STATE_LOCKED = "04";

}

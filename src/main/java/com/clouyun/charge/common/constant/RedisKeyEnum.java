package com.clouyun.charge.common.constant;

/**
 * 描述: 系统自定义redis缓存key值
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年05月02日
 */
public enum RedisKeyEnum {
    // 区域相关
    AREA_COMBOX_KEY("所有下拉字典key", "pub_combox_area_20170526"),
    AREA_01_KEY("一级区域字典key", "area_type_1"),
    AREA_NO_KEY("所有区域缓存key，key值为区域编码，value值为区域名称", "pub_no_area_20170526"),
    AREA_NAME_KEY("所有区域缓存key，key值为区域名称，value值为区域编码", "pub_name_area_20170526"),

    // 字典相关
    SYS_DICT_KEY("缓存库字典key值前缀", "sys_dict:"),

    // 用户相关
    LOCK_USER_KEY("用户锁key值前缀", "lock_user:"),
    JWT_USER_KEY("获取token的用户key值前缀", "jwt_user:"),

    // 权限相关
    RESOURCE_AUTH_KEY("资源对应所需权限key值", "resource_auth");

    public String desc;
    public String value;


    RedisKeyEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}

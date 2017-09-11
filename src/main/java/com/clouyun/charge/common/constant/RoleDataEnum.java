package com.clouyun.charge.common.constant;

/**
 * 描述: 角色对应数据权限枚举类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年05月02日
 */
public enum RoleDataEnum {
    ORG("运营商", 0),
    STATION("场站", 1);

    public String dataName;
    public Integer dataType;


    RoleDataEnum(String dataName, Integer dataType) {
        this.dataName = dataName;
        this.dataType = dataType;
    }

    // 普通方法
    public static String getName(int dataType) {
        for (RoleDataEnum c : RoleDataEnum.values()) {
            if (c.dataType == dataType) {
                return c.dataName;
            }
        }
        return null;
    }
    public static void main(String[] args) {
        System.out.println(RoleDataEnum.ORG);
        System.out.println(RoleDataEnum.getName(0));
    }
}

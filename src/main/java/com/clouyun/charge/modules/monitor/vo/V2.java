package com.clouyun.charge.modules.monitor.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年08月09日
 */
public class V2 {
    private String gunName;
    private List<Double> var1 = new ArrayList<>();//电流 //温度 //电量
    private List<Double> var2 = new ArrayList<>();//电压 //soc //收入
    private List<String> time = new ArrayList<>();

    public V2() {
        super();
    }

    public V2(String gunName) {
        this.gunName = gunName;
    }

    public V2(String gunName, List<Double> var1, List<Double> var2, List<String> time) {
        this.gunName = gunName;
        this.var1 = var1;
        this.var2 = var2;
        this.time = time;
    }

    public String getGunName() {
        return gunName;
    }

    public void setGunName(String gunName) {
        this.gunName = gunName;
    }

    public List<Double> getVar1() {
        return var1;
    }

    public void setVar1(List<Double> var1) {
        this.var1 = var1;
    }

    public List<Double> getVar2() {
        return var2;
    }

    public void setVar2(List<Double> var2) {
        this.var2 = var2;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}

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
public class V4 {
    private String gunName;
    private List<Double> dl = new ArrayList<>();
    private List<Double> sr = new ArrayList<>();
    private List<String> time = new ArrayList<>();

    public V4() {
        super();
    }

    public V4(String gunName) {
        this.gunName = gunName;
    }

    public V4(String gunName, List<Double> dl, List<Double> sr, List<String> time) {
        this.gunName = gunName;
        this.dl = dl;
        this.sr = sr;
        this.time = time;
    }

    public String getGunName() {
        return gunName;
    }

    public void setGunName(String gunName) {
        this.gunName = gunName;
    }

    public List<Double> getDl() {
        return dl;
    }

    public void setDl(List<Double> dl) {
        this.dl = dl;
    }

    public List<Double> getSr() {
        return sr;
    }

    public void setSr(List<Double> sr) {
        this.sr = sr;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}

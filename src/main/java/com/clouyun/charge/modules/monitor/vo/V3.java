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
public class V3 {
    private String gunName;
    private List<Double> wd = new ArrayList<>();
    private List<Double> soc = new ArrayList<>();
    private List<String> time = new ArrayList<>();

    public V3() {
        super();
    }

    public V3(String gunName) {
        this.gunName = gunName;
    }

    public V3(String gunName, List<Double> wd, List<Double> soc, List<String> time) {
        this.gunName = gunName;
        this.wd = wd;
        this.soc = soc;
        this.time = time;
    }

    public String getGunName() {
        return gunName;
    }

    public void setGunName(String gunName) {
        this.gunName = gunName;
    }

    public List<Double> getWd() {
        return wd;
    }

    public void setWd(List<Double> wd) {
        this.wd = wd;
    }

    public List<Double> getSoc() {
        return soc;
    }

    public void setSoc(List<Double> soc) {
        this.soc = soc;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }
}

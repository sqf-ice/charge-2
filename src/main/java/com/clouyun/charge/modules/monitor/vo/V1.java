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
public class V1 {

    private String gunName;
    private List<Double> process = new ArrayList<>();
    private List<String> time = new ArrayList<>();

    public V1(String gunName) {
        this.gunName = gunName;
    }

    public String getGunName() {
        return gunName;
    }

    public void setGunName(String gunName) {
        this.gunName = gunName;
    }

    public List<Double> getProcess() {
        return process;
    }

    public void setProcess(List<Double> process) {
        this.process = process;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public V1(String gunName, List<Double> process, List<String> time) {
        this.gunName = gunName;
        this.process = process;
        this.time = time;
    }

    public V1() {
        super();
    }
}

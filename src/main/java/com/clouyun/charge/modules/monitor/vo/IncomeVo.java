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
public class IncomeVo {

    private double todayAmount;
    private double totalAmount;
    private double totalPower;
    private List<V2> sr = new ArrayList<>();

    public IncomeVo() {
        super();
    }

    public IncomeVo(double todayAmount, double totalAmount, double totalPower, List<V2> sr) {
        this.todayAmount = todayAmount;
        this.totalAmount = totalAmount;
        this.totalPower = totalPower;
        this.sr = sr;
    }

    public double getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(double todayAmount) {
        this.todayAmount = todayAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(double totalPower) {
        this.totalPower = totalPower;
    }

    public List<V2> getSr() {
        return sr;
    }

    public void setSr(List<V2> sr) {
        this.sr = sr;
    }
}

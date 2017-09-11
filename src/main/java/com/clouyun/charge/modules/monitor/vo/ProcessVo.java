package com.clouyun.charge.modules.monitor.vo;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年08月09日
 */
public class ProcessVo {
    // 因前端需要BMS 收入监控，在线统计三个对象名称命名要统一
    private RealTimeVo use = new RealTimeVo();//用电实时数据
    private RealTimeVo charge = new RealTimeVo();//充电监测数据
    private BmsVo bms = new BmsVo();// BMS监测数据
    private IncomeVo income = new IncomeVo();//收入监控
    private OnlineVo online = new OnlineVo();//在线统计
    private Boolean charging = false;// 是否在充电状态
    private String type;// 桩类型 1：交流 2：直流

    public ProcessVo() {
        super();
    }

    public ProcessVo(RealTimeVo use, RealTimeVo charge, BmsVo bms, IncomeVo income, OnlineVo online, Boolean charging, String type) {
        this.use = use;
        this.charge = charge;
        this.bms = bms;
        this.income = income;
        this.online = online;
        this.charging = charging;
        this.type = type;
    }

    public RealTimeVo getUse() {
        return use;
    }

    public void setUse(RealTimeVo use) {
        this.use = use;
    }

    public RealTimeVo getCharge() {
        return charge;
    }

    public void setCharge(RealTimeVo charge) {
        this.charge = charge;
    }

    public BmsVo getBms() {
        return bms;
    }

    public void setBms(BmsVo bms) {
        this.bms = bms;
    }

    public IncomeVo getIncome() {
        return income;
    }

    public void setIncome(IncomeVo income) {
        this.income = income;
    }

    public OnlineVo getOnline() {
        return online;
    }

    public void setOnline(OnlineVo online) {
        this.online = online;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getCharging() {
        return charging;
    }

    public void setCharging(Boolean charging) {
        this.charging = charging;
    }
}

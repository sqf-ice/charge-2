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
public class RealTimeVo {
    private List<V1> u = new ArrayList<>();
    private List<V1> i = new ArrayList<>();
    private List<V1> p = new ArrayList<>();

    public RealTimeVo() {
        super();
    }

    public RealTimeVo(List<V1> u, List<V1> i, List<V1> p) {
        this.u = u;
        this.i = i;
        this.p = p;
    }

    public List<V1> getU() {
        return u;
    }

    public void setU(List<V1> u) {
        this.u = u;
    }

    public List<V1> getI() {
        return i;
    }

    public void setI(List<V1> i) {
        this.i = i;
    }

    public List<V1> getP() {
        return p;
    }

    public void setP(List<V1> p) {
        this.p = p;
    }
}

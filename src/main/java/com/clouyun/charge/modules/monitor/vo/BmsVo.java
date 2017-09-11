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
public class BmsVo {
    private List<V2> iu = new ArrayList<>();
    private List<V2> ws = new ArrayList<>();

    public BmsVo() {
        super();
    }

    public BmsVo(List<V2> iu, List<V2> ws) {
        this.iu = iu;
        this.ws = ws;
    }

    public List<V2> getIu() {
        return iu;
    }

    public void setIu(List<V2> iu) {
        this.iu = iu;
    }

    public List<V2> getWs() {
        return ws;
    }

    public void setWs(List<V2> ws) {
        this.ws = ws;
    }
}

package com.clouyun.charge.modules.monitor.vo;

import java.util.List;
import java.util.Map;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年08月09日
 */
public class OnlineVo {
    private String online;
    private String lostLine;
    private List<Map<String, String>> hearBeat;

    public OnlineVo() {
        super();
    }

    public OnlineVo(String online, String lostLine, List<Map<String, String>> hearBeat) {
        this.online = online;
        this.lostLine = lostLine;
        this.hearBeat = hearBeat;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getLostLine() {
        return lostLine;
    }

    public void setLostLine(String lostLine) {
        this.lostLine = lostLine;
    }

    public List<Map<String, String>> getHearBeat() {
        return hearBeat;
    }

    public void setHearBeat(List<Map<String, String>> hearBeat) {
        this.hearBeat = hearBeat;
    }
}

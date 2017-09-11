package com.clouyun.charge.modules.monitor.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

/**
 * 描述: 经营分析Mapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yi.kai
 * 版本: 1.0
 * 创建日期: 2017年6月21日
 */
@Mapper
public interface BusinessAnalysisMapper {
    /**
     * 得到场站充电量
     * @param dataMap
     * @return
     */
    List<DataVo> getStationPower(DataVo dataMap);

    /**
     * 场站收入
     * @param dataMap
     * @return
     */
    List<DataVo> getStationIncome(DataVo dataMap);

    /**
     * 得到利率
     * @param dataMap
     * @return
     */
    List<DataVo> getStationProfit(DataVo dataMap);

    /**
     * 硬件故障，通讯异常，支付异常，系统BUG，启动充电异常数
     * @param dataMap
     * @return
     */
    List<DataVo> faultTypeList(DataVo dataMap);

    /**
     * 得到场站
     * @param dataMap
     * @return
     */
    List<DataVo> getFaultStation(DataVo dataMap);

    /**
     * 得到上月日充电量
     * @param dataMap
     * @return
     */
    String getChargeTime(DataVo dataMap);

    /**
     * 得到总转换率
     * @param dataMap
     * @return
     */
    DataVo getConversion(DataVo dataMap);

    /**
     * 得到枪数
     * @param vo
     * @return
     */
    List<DataVo> getGun(DataVo vo);

    /**
     * 桩数
     * @param map1
     * @return
     */
    Integer getPileSize(DataVo map1);

    /**
     * 在线率Size
     * @param map1
     * @return
     */
    Integer onLineRate(DataVo map1);

    /**
     * 准确率
     * @param map1
     * @return
     */
    Integer accuracy(DataVo map1);

    Integer onLineRateDay(DataVo vo);

    Integer onLineRateWeek(DataVo vo);

    Integer accuracyWeek(DataVo vo);

    Integer accuracyDay(DataVo vo);
}

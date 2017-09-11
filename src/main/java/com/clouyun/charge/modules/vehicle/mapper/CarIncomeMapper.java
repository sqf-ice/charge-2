package com.clouyun.charge.modules.vehicle.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/5/11.
 */
@Mapper
public interface CarIncomeMapper {
    /**
     * 服务车辆数统计
     */
    public List<DataVo> carTypeCount(Map map);

    /**
     * 车辆收入24小时图表
     * @param map
     * @return
     */
    List<DataVo> carHourChart(Map map);

    /**
     * 当月单车充电排行取前十
     * carChgPowerTop10
     * @return
     */
    List<DataVo> carChgPowerTop10(DataVo map);
    /**
     * 查询充电前20的站场
     * carChgPowerTop10
     * @return
     */
    List<DataVo> carChgPowerTop20(Map map);

    /**
     * 查询卡号不用f开头的车牌
     * @param carLicensePlate
     */
    String selectCarPlate(String carLicensePlate);
    /**
     * 查询卡号用f开头的车牌
     * @param carLicensePlate
     */
    String selectCarPlateF(String carLicensePlate);

    /**
     * 查询卡号不用f开头的车牌
     */
    List<DataVo> queryCarPlate(DataVo vo);
    
    /**
     * 查询卡号用f开头的车牌
     */
    List<DataVo> queryCarPlateF(DataVo vo);
    /**
     * 查询卡号用Vin的车牌
     */
    List<DataVo> queryVins(DataVo vo);
    
    /**
     * 查询卡号用Vin的车牌
     * @param vin
     * @return
     */
    String selectCarvVim(String vin);

    /**
     * 查询车辆收入列表
     * @param map
     * @return
     */
    List<DataVo> carIncomeList(Map map);

    /**
     * 车辆收入详情
     * @param map
     * @return
     */
    List<DataVo> carIncomeDetail(Map map);

    /**
     * 车辆收入详情统计列表
     * @param map
     * @return
     */
    List<DataVo> carsCountDetailList(Map map);

    List<DataVo> carsStation(DataVo dataVo);
    List<DataVo> carList(DataVo map);

    List<DataVo> carListType2(DataVo map);

    DataVo carListTypeCount(DataVo map);

}

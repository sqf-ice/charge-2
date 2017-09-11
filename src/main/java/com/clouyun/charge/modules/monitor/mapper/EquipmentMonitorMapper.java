package com.clouyun.charge.modules.monitor.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 描述: 设备监控
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年8月8日 上午10:01:18
 */
@Mapper
public interface EquipmentMonitorMapper {
	//根据场站Id查询所有的充电桩信息
	List<DataVo> getPilesByStationId(DataVo map);

	BigDecimal queryStationIncome(DataVo map);

	//订单id获取订单信息
	DataVo queryBillPayById(Integer billPayId);

	//根据车辆的vin码获取车辆信息
	DataVo queryVehicleByVin(String vin);

	DataVo queryCConsByCarId(Integer carId);

	DataVo queryCCardById(String cardNo);

	DataVo queryVehicleByConsId(Integer consId);

	List<DataVo> queryMeterInfo(Map map);

	List<DataVo> queryPileInfoById(Integer pileId);

	DataVo queryConsByCardId(String cardId);

	List<DataVo> queryPileWarmRecord(Object[] pileAddr);

	List<DataVo> queryChgGunInfoById(Map map);

	List<DataVo> queryBillPayGroupByPile(Map map);

	List<DataVo> queryMeterCode(Map map);
	//查询桩的实际用电量
	DataVo queryStationChgPower(Map map);
}

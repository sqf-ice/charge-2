package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: VehicleStatusMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年8月14日
 */
@Mapper
public interface VehicleStatusMapper {
	//查询离线状态车辆  返回车辆Id
	List getVehicleStatus(DataVo vo);
	
	//查询地上铁车辆信息
	List getVehicleMonitor(DataVo vo);
	
	//获取车辆的实时状态
	List getAllVehicleStatus();
}

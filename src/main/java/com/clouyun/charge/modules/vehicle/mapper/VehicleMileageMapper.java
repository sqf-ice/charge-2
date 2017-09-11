package com.clouyun.charge.modules.vehicle.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 描述: 车辆里程
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年6月27日 上午9:04:18
 */
@Mapper
public interface VehicleMileageMapper {
	
	List<Map> queryHistory(Map map);
	
	void batchInser(List<Map> list);

	Integer queryMileageCount(Map map);
}
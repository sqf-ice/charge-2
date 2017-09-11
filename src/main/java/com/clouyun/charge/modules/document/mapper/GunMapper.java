package com.clouyun.charge.modules.document.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: GunMapper 充电枪管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月19日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface GunMapper {
		
	/**
	 * 获取充电枪列表
	 */
	List getGunAll(DataVo vo);
	
	/**
	 * 根据场站编号获取充电枪数量
	 */
	List getGunCountByStationId(DataVo vo);
	
	/**
	 * 根据设备内部编号,充电桩Id查询枪口 
	 */
	Map getGunPoint(DataVo vo);
	/**
	 * 添加枪信息
	 * gaohui
	 * 2.0.0
	 * 2017年5月4日
	 */
	
	void insertGun(Map map);
	/**
	 * 根据充电桩id删除枪信息
	 * gaohui
	 * 2.0.0
	 * 2017年5月4日
	 */
	void deleteGunByPileId(Map map);

	List getVehicleStatus();
}

package com.clouyun.charge.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
/**
 * 描述: 会员常用车辆认证
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0.0
 * 创建日期: 2017年6月15日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface VehicleAuthMapper {
	
	/**
	 * 认证的会员列表
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月15日
	 */
	List<Map> getVehicleAuths(Map map);
	/**
	 * 车辆认证
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月15日
	 */
	void authVehicle(Map map);

}

package com.clouyun.charge.modules.vehicle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: 驾驶员信息
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: fft
 * 版本: 2.0.0
 * 创建日期: 2017年4月15日 上午10:01:18
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface DriverMapper {
	
	//查询驾驶员列表
	List<DataVo> queryDrivers(DataVo map);
	
	/**
	 * 新增驾驶员
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月2日
	 */
	int insertDriver(Map map);
	/**
	 * 驾驶员详情
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月2日
	 */
	DataVo getDriver(Integer driverId);
	
	//修改驾驶员信息
	int updateDriverByDriverId(Map map);
	
	//根据车辆ID查询驾驶员列表
	List<DataVo> queryDriversByvehicleId(Integer vehicleId);
	/**
	 * 查询驾驶员信息列表(证号)
	 * 2.0.0
	 * gaohui
	 * 2017年5月20日
	 */
	List<DataVo> getDriverByVars(Map map);
	/**
	 * 批量添加驾驶员
	 * 2.0.0
	 * gaohui
	 * 2017年5月20日
	 */
	void insertDrivers(Map map);
	/**
	 * 批量更新驾驶员(根据证号结合)
	 * 2.0.0
	 * gaohui
	 * 2017年5月20日
	 */
	void updateDrivers(Map map);
	/**
	 * 电话号码是否重复
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月2日
	 */
	List<DataVo> queryDriverExist(Map map);
}

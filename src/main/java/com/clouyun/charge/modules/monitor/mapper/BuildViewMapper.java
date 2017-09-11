package com.clouyun.charge.modules.monitor.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 
 * 描述: 建设总览
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0.0
 * 创建日期: 2017年6月21日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface BuildViewMapper {
    /**
     * 获取有效的场站
     * 版本:2.0.0
     * 作者:gaohui
     * 日期:2017年6月21日
     */
	public List<DataVo> getStations();
	/**
	 * 获取第三方场站
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月21日
	 */
	public int getToStationCount();
	/**
	 * 根据场站id列表获取充电桩信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<Map> getPileByStationIds(Map map);
	/**
	 * 查询所有的有效的充电桩 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月27日
	 */
	public List<DataVo> getPiles();
	/**
	 * 第三方设备（充电桩）
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月27日
	 */
	public int getToPiles();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getPilesCount();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getStationByProv(Map map);
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getPileByProv();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
    public List<DataVo> getStationByCity(Map map);
    /**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getPileByCity();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getStationByTime(Map map);
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getVehicles();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getVehiclesByOrgIds();
	/**
	 * 获取平台订单总数和总充电量
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月27日
	 */
	public DataVo getBillPayCountAndSum(Map map);
	/**
	 * 获取订单车辆信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月27日
	 */
	public List<DataVo> getBillPayVehicles(Map map);
	/**
	 * 获取互联互通的订单总数和总充电量
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月27日
	 */
	public DataVo getToOrderAndSum(Map map);
	/**
	 * 查询合作的运营商
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月14日
	 */
	public List<DataVo> getJoinOrg();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月18日
	 */
	public List<DataVo> getPileByTime(Map map);
	/**
	 * 第三方充电桩统计
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月26日
	 */
	public List<DataVo> getToPileByTime(Map map);
	/**
	 * 第三方场站统计
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月26日
	 */
	public List<DataVo> getToStationByTime(Map map);
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月20日
	 */
	public List<DataVo> getAreaProv();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月31日
	 */
	public List<DataVo> getMileSum(Map map);
}

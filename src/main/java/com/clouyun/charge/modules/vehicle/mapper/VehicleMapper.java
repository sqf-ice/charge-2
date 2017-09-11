package com.clouyun.charge.modules.vehicle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
* 描述: 账户信息
* 版权: Copyright (c) 2017
* 公司: 科陆电子
* 作者: fft
* 版本: 2.0.0
* 创建日期: 2017年4月15日 上午10:01:18
*/
@Mapper
@SuppressWarnings("rawtypes")
public interface VehicleMapper {
	
	//查询车辆信息列表
	List<DataVo> getVehicles(Map map);
	/**
	 * 新增车辆信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	void insertVehicle(Map map);
	/**
	 * 更新车辆信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	void updateVehicle(Map map);
	/**
	 * 根据车辆id删除车辆与驾驶员关系信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	void deleteVehDriRelByVehId(Map map);
	/**
	 * 根据驾驶员id和驾驶员id删除车辆与驾驶员信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	void deleteVehDriRelBy2Id(Map map);
	/**
	 * 添加车辆和驾驶员关联信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	void insertVehDriRels(Map map);
	
	/**
	 * 添加车辆和会员关联信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	void insertVehMemRels(Map map);
	/**
	 * 根据车辆id获取车辆信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	DataVo getVehicle(Integer vehicleId);
	
	/**
	 * 查询车辆驾驶员关联关系
	 * gaohui
	 * 2017年5月16日
	 * 2.0.0
	 */
	List<DataVo> getVehDriRel(Map map);
	
	/**
	 * 查询车辆会员关联关系
	 * gaohui
	 * 2017年5月16日
	 * 2.0.0
	 */
	List<DataVo> getVehMemRel(Map map);
	
	/**
	 * 判断车牌号是否重复
	 * gaohui
	 * 2017年5月16日
	 * 2.0.0
	 */
	int getIsExistByLicense(Map map);
	/**
	 * 根据车牌号查询车辆列表
	 * gaohui
	 * 2017年5月16日
	 * 2.0.0
	 */
	List<DataVo> getVehiclesByLicenses(Map map);
	/**
	 * 批量新增车辆
	 * gaohui
	 * 2017年5月16日
	 * 2.0.0
	 */
	void batchInsertVehicles(Map map);
	/**
	 * 批量更新车辆
	 * gaohui
	 * 2017年5月16日
	 * 2.0.0
	 */
	void updateVehicleByPlate(Map map);
	
	/**
	 * 根据车辆id获取相关的会员信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月4日
	 */
	List<DataVo> getVehMemRelByVehicleId(Integer vehicleId);
	/**
	 * 根据车辆id获取相关的驾驶员信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月4日
	 */
	List<DataVo> getVehDriRelByVehicleId(Integer vehicleId);
	/**
	 * 获取会员
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月7日
	 */
	List<DataVo> getBillSum(Map map);
	
	/**
	 * 获取企业信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月8日
	 */
	List<DataVo> getOrgList(Map map);
	/**
	 * 获取场站信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月8日
	 */
	List<DataVo> getStationList(Map map);
	/**
	 * 获取会员信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月22日
	 */
	List<DataVo> getMemberList(Map map);
	/**
	 * 获取卡信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月22日
	 */
	List<DataVo> getCardList(Map map);
	/**
	 * 获取车牌列表
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月24日
	 */
    List<DataVo> getVehicleBrands();
    /**
     * 车型号业务字典
     * 版本:2.0.0
     * 作者:gaohui
     * 日期:2017年8月24日
     */
    List<DataVo> getVehicleModels(Map map);
    /**
     * 
     * 版本:2.0.0
     * 作者:gaohui
     * 日期:2017年9月4日
     */
    void updateCardVehicle(Map map);
    /**
     * 更新会员常用车辆
     * 版本:2.0.0
     * 作者:gaohui
     * 日期:2017年8月28日
     */
    void updateConsVehicle(Map map);
}

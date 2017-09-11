package com.clouyun.charge.modules.document.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述: StationMapper 场站
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年4月18日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface StationMapper {
	/**
	 * 场站列表
	 */
	List getStationAll(DataVo vo);
	/**
	 * 编辑场站
	 */
	void updateStation(Map vo);
	
	/**
	 * 场站置无效
	 */
	void dissStation(DataVo vo);
	
	/**
	 * 场站详情
	 */
	Map getStationById(DataVo vo);
	
	/**
	 * 第三方场站列表
	 */
	List getToSubsAll(DataVo vo);
	
	/**
	 * 获取场站名称(场站下拉框)
	 */
	List getStation(DataVo vo);
	
	/**
	 * 获取登录用户的场站Id,运营商Id
	 */
	List getUserOrgStation(DataVo vo);
	
	/**
	 * 新增场站信息
	 */
	void saveStation(Map vo);

	/**
	 * 根据企业ID集合获取场站ID集合
	 * @param orgIds
	 * @return
	 */
	List<Integer> getStationIdByOrgIds(@Param("orgIds") List<Integer> orgIds);

	/**
	 *  保存运营信息
	 * @param dataMap
	 */
	void saveBusiness(Map dataMap);

	/**
	 * 编辑运营信息
	 * @param dataMap
	 */
	void updateBusiness(Map dataMap);

	/**
	 * 查询business数量
	 * @param stationId
	 * @return
	 */
	Integer businessCount(Integer stationId);
	/**
	 * 获取orgNo
	 * @param map
	 * @return
	 */
	DataVo getOrgNo(Map map);

	/**
	 * 得到枪数
	 * @param id
	 * @return
	 */
	Integer getGunSize(Integer id);

	/**
	 * 得到枪类型功率和数
	 * @param id
	 * @return
	 */
	List<DataVo> getPileOrtModePower(Integer id);

	/**
	 * 得到总功率
	 * @param map
	 * @return
	 */
	DataVo getTotalRatePower(Map map);
	
	/**
	 * 查询第三方充电站Id
	 */
	List<DataVo> getOperatorIdByOperatorRelaId(@Param("operatorRelaIds") List<String> operatorRelaIds);
	
	/**
	 * 查询第三方运营商
	 */
	List getToSubOperatorIdsDict(DataVo vo);
	
	/**
	 * 查询第三方场站
	 */
	List getToStationinfoDict(DataVo vo);

	Integer validateArr(DataVo vo);
}

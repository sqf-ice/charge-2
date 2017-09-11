package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;




/**
 * 描述: 耗材管理接口类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年2月27日
 */
@SuppressWarnings("rawtypes")
@Mapper
public interface MaterialMapper {
	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List queryListByPage(Map map);
	/**
	 * 返回列表总行数
	 * @param map
	 * @return
	 */
	int queryListCount(Map map);
	/**
	 * 通过Id查询返回单行数据
	 * @param map
	 * @return
	 */
	Map queryById(Map map);
	/**
	 * 检查数据唯一性
	 * @param map
	 * @return
	 */
	int checkUniqueness(Map map);
	/**
	 * 新增数据
	 * @param map
	 * @return
	 */
	int insert(Map map);
	/**
	 * 修改数据
	 * @param map
	 * @return
	 */
	int update(Map map);
	/**
	 * 删除多条数据
	 * @param map
	 * @return
	 */
	int delete(Map map);
	/**
	 * 根据orgId查询物料列表
	 * @return
	 * 2017年3月24日
	 * gaohui
	 */
	List<Map> findAllMaterialByOrgId(Map map);
	/**
	 * 根据matId查询物料价格
	 * @param matId
	 * @return
	 * 2017年3月24日
	 * gaohui
	 */
	Map findAllMaterialById(Map map);
	/**
	 * 根据告警Id获取物料信息
	 * @param map
	 * @return
	 * 2017年3月29日
	 * gaohui
	 */
	List<Map> findAllMaterialByRecId(Map map);
	/**
	 * 查询物料是否正在使用
	 */
	int materialUse(Map map);
}

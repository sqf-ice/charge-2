package com.clouyun.charge.modules.electricity.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.charge.modules.electricity.entitys.LossModel;

/**
 * 损耗模型
 * @author fuft
 *
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface LossModelMapper {	
	 
	/**
	 * 统计损耗模型列表数量
	 */
	Integer count(Map map);
	/**
	 * 根据条件查询损耗模型列表
	 */
	List<LossModel> selectAll(Map map);
	
	/**
	 * 新增损耗模版
	 */
	void lossModelAdd(Map map);
	
	/**
	 * 损耗模版编辑
	 */
	void lossModelEdit(Map map);
	
	/**
	 * 根据stationId查找损耗模型
	 */
	LossModel findModelByStationId(Integer stationId);
}

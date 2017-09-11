package com.clouyun.charge.modules.document.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: MeterManagementMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@Mapper
public interface MeterManagementMapper {
	
	/**
	 * 查询表计列表数据
	 */
	List getMeterManagementAll(DataVo vo);
	
	/**
	 * 查询表计信息
	 */
	Map getMeterManagementById(DataVo vo);
	
	/**
	 * 根据表计Id 删除表计信息
	 */
	void delMeterManagement(DataVo vo);
	
	
	/**
	 * 新增表计
	 */
	void saveMeterManagement(Map vo);
	
	/**
	 * 编辑表计
	 */
	void updateMeterManagement(Map vo);
	
	/**
	 * 根据变压器Id,终端Id查询是否有表计信息
	 */
	int getMeterByObjIds(DataVo vo);
}

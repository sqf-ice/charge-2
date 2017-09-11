package com.clouyun.charge.modules.document.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: TransformerMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@Mapper
public interface TransformerMapper {
	/**
	 * 查询变压器列表
	 */
	List getTransformerAll(DataVo vo);
	
	/**
	 * 根据变压器Id查询变压器信息
	 */
	Map getTransformerById(DataVo vo);
	
	/**
	 * 根据变压器Id删除变压器信息
	 */
	void delTransformer(DataVo vo);
	
	/**
	 * 查询终端信息(业务字典)
	 */
	List getTransformer(DataVo vo);
	
	/**
	 * 新增变压器
	 */
	void saveTransformer(Map vo);
	
	/**
	 * 编辑变压器
	 */
	void updateTransformer(Map vo);
	
}

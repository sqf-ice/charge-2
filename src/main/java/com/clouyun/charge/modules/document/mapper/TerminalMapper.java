package com.clouyun.charge.modules.document.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: TerminalMapper 终端管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@Mapper
public interface TerminalMapper {
	/**
	 * 查询终端列表
	 */
	List getTerminalAll(DataVo vo);
	
	/**
	 * 根据终端Id查询终端信息
	 */
	Map getTerminalById(DataVo vo);
	
	/**
	 * 根据终端Id删除终端信息
	 */
	void delTerminal(DataVo vo);
	
	/**
	 * 根据终端名称查询终端信息列表(业务字典)
	 */
	List getTerminal(DataVo vo);
	
	/**
	 * 新增终端信息
	 */
	void saveTerminal(Map vo);
	
	/**
	 * 编辑终端信息
	 */
	void updateTerminal(Map vo);
}

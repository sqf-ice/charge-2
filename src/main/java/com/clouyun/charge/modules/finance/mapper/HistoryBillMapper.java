package com.clouyun.charge.modules.finance.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 描述: M
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月18日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface HistoryBillMapper {
	/**
	 * 分页查询财务记录
	 * @param map
	 * @return
	 * 2017年4月18日
	 * gaohui
	 */
	List<Map> get(Map map);
	/**
	 * 查询财务记录详情
	 * @param id
	 * @return
	 * 2017年4月18日
	 * gaohui
	 */
	Map getById(Integer id);

}

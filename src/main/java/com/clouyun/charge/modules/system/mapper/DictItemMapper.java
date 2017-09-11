package com.clouyun.charge.modules.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
/**
 * 字典表查询
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年3月21日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface DictItemMapper {
	/**
	 * 根据字典表的typeId查询
	 * @param data
	 * @return
	 * 2017年3月30日
	 * gaohui
	 */
	List<Map> queryDictItemList(Map data);
	/**
	 * 根据字典表的typeId和num查询
	 * @param data
	 * @return
	 * 2017年3月30日
	 * gaohui
	 */
	Map queryDictItem(Map data);
	/**
	 * 根据主键id和num更新字典表
	 * @param data
	 * @return
	 * 2017年3月30日
	 * gaohui
	 */
	int updateDictItembyIdAndNum(Map data);
	/**
	 * 根据typeId更新字典表
	 * @return
	 * 2017年3月30日
	 * gaohui
	 */
	int updateDictItembyTypeId(Map data);

}

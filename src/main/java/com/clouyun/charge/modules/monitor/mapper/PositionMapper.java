package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 位置信息
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月7日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface PositionMapper {
	/**
	 * 根据taskId获取位置信息
	 * @param map
	 * @return
	 * 2017年4月7日
	 * gaohui
	 */
	List<Map> findPositionsByTaskId(Map map);

}

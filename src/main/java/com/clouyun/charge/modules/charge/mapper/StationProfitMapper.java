package com.clouyun.charge.modules.charge.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 描述: 场站利润
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月24日 下午1:50:44
 */
@Mapper
public interface StationProfitMapper {
	
	List<Map> queryStationProfit(Map map);
	List<Map> queryStationProfitCache(Map map);
	
	Map queryProfitSubtotal(Map map);
	
	//收入趋势
	List<Map> queryIncomeTrends(Map map);
	
	Map queryStationProfitByKey(Integer spId);
	
	int batchInsert(List list);
	int batchUpdate(List list);

}


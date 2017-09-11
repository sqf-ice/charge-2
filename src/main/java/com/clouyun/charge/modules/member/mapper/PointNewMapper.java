package com.clouyun.charge.modules.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月6日 下午4:16:30
 */
@Mapper
public interface PointNewMapper {
	
	//积分列表
	List<Map> queryPoints(Map map);
	
	Map queryPubOrg(Map map);
	
	Map queryByType(Map map);
	
	int insertPoints(Map map);
	
	int updatePoints(Map map);
	
	/*
	 * 查询会员积分详情
	 */
	List<Map> queryPointsHistoryInfo(Map map);

	List<Map> queryPointsHistory(Map map);
}

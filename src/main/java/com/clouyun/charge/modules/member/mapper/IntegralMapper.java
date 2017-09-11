package com.clouyun.charge.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


/**
 * 描述: 积分管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月20日 下午7:02:52
 */
@Mapper
public interface IntegralMapper {
	//查询所有积分规则
	List<Map> queryIntegrals(List<Integer> list);
	
	//查询积分信息
	List<Map> queryIntegralHistoryInfo(Map map);
	
	//积分列表
	List<Map> queryIntegralInfo(Map map);
	
	int updateIntegral(Map map);
}

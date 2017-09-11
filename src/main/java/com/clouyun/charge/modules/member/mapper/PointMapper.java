package com.clouyun.charge.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;


/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月4日 下午8:14:34
 */
@Mapper
public interface PointMapper {
	
	//积分列表
	List<Map> queryPoints(DataVo map);
	
	Map queryPubOrg(DataVo map);
	
	Map queryByType(DataVo map);
	
	int insertPoints(DataVo map);
	
	int updatePoints(DataVo map);
}

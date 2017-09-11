package com.clouyun.charge.modules.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 描述: 集团信息
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月15日 上午10:01:18
 */
@Mapper
public interface GroupMapper {
	//查询所有集团信息
	List<Map> queryGroups(Map map);
	
	//根据集团id获取信息
	Map queryGroupByKey(@Param("groupId")Integer groupId);
	
	//新增集团
	int insertGroup(Map map);

	//更新集团
	int updateGroup(Map map);
	
	//集团名称唯一验证
	int checkGroupNameOnly(Map map);
	
	//集团字典服务
	List<Map> queryGroupDicts(Map map);
}

package com.clouyun.charge.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

@Mapper
public interface PubUserMapper {
	
	//新增企业管理员基本信息
	int insertPubUser(DataVo dv);
	
	//根据用户名，邮箱，密码查询企业管理员信息
	DataVo selectPubUser(DataVo dv);
	
	//根据userId查询企业管理员信息 
	DataVo selectPubUserByuserId(DataVo dv);
	
	//根据userId修改企业管理员基本信息 
	int updatePubUserByuserId(DataVo dv);
	
	//判断该用户名是否存在 
	List<DataVo> selectPubUserByuserName(DataVo dv);
	
	//给企业管理员分配角色
	int insertUserRole(DataVo dv);
	
	//给企业管理员分配角色
	int insertPubUserOrgStation(DataVo dv);
}
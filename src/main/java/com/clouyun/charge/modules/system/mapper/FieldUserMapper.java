package com.clouyun.charge.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.ui.TreeNode;
/**
 * 描述: 外勤人员管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年3月6日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface FieldUserMapper {
	/**
     * 分页查询外勤用户
     * @param data
     * @return
     * 2017年5月21日
     * gaohui
     * 2.0.0
     */
	public List<Map> get(Map data);
	/**
     * 外勤详情
     * @param data
     * @return
     * 2017年5月21日
     * gaohui
     * 2.0.0
     */
	public Map getById(Integer wqConsId);
	/**
     * 根据id删除外勤用户
     * @param data
     * @return
     * 2017年5月21日
     * gaohui
     * 2.0.0
     */
    public int removeFieldUser(Integer wqConsId);
	//添加外勤人员信息
	public int addFieldUser(Map data);
	
	//根据相应的条件查询满足条件的外勤人员信息
	public List<Map> queryAllFieldUser(Map data);
	
	//根据电话号码判断用户是否存在
	public List<Map> queryFieldUserExist(Map data);
	
	//根据id查询外勤用户
	public Map queryFieldUserById(Map data);
    
    //编辑相应的外勤人员信息
    public int modifyFieldUser(Map data);
    
    //根据组织ID和电话号码查询外勤用户
    public Map queryFieldUserByOrgIdAndTel(Map data);
    
    //根据id删除外勤用户
    public int deleteFieldUser(Map data);
    
    //添加关系表
    public int addFieldStationOrgRel(Map data);
    
    //删除关联信息
    public int deleteFieldStationOrgRel(Map data);
    
    //根据外勤用户的id查询关联关系
    public List<Map> queryFieldStationOrgRel(Map data);
    /**
     * 根据外勤用户的Ids查询用户
     * @param data
     * @return
     * 2017年4月11日
     * gaohui
     */
    public List<Map> queryFieldUserByIds(Map data);
    
    public List<TreeNode> getFieldOrgsByConsId(Integer wqConsId);
    
    public List<TreeNode> getFieldStationsByConsId(Integer wqConsId);
    

}

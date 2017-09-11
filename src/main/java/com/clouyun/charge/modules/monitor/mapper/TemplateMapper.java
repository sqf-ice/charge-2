package com.clouyun.charge.modules.monitor.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;




/**
 * 描述: 模板管理接口类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年2月27日
 */
@SuppressWarnings("rawtypes")
@Mapper
public interface TemplateMapper {
	
	/**
	 * 查询列表
	 * @param map
	 * @return
	 * 2.0.0
	 */
	List<Map> get(Map map);
	/**
	 * 查询模板详情
	 * @param templateId
	 * @return
	 * 2.0.0
	 */
	Map getById(Integer templateId);
	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List queryListByPage(DataVo vo);
	/**
	 * 返回列表总行数
	 * @param map
	 * @return
	 */
	int queryListCount(DataVo vo);
	/**
	 * 通过Id查询返回单行数据
	 * @param map
	 * @return
	 */
	Map queryById(DataVo vo);
	/**
	 * 检查数据唯一性
	 * @param map
	 * @return
	 */
	int checkUniqueness(DataVo vo);
	/**
	 * 新增数据
	 * @param map
	 * @return
	 */
	int insert(DataVo vo);
	/**
	 * 修改数据
	 * @param map
	 * @return
	 */
	int update(DataVo vo);
	/**
	 * 删除多条数据
	 * @param map
	 * @return
	 */
	int delete(DataVo vo);
	/**
	 * 根据企业ID查询模板列表
	 * @param vo
	 * @return
	 * 2017年3月22日
	 * gaohui
	 */
	List queryListByOrgId(DataVo vo);
	/**
	 * 根据模版id判断模版是否在使用
	 */
	int isUseTask(DataVo list);
	
}

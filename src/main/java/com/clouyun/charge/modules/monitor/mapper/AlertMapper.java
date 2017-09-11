package com.clouyun.charge.modules.monitor.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述: 任务、告警、物料关联
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月1日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface AlertMapper {
	/**
	 * 添加物料信息
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	void addAlert(Map map);
	/**
	 * 根据告警Id查询物料信息
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	List<Map> findAlertByRecId(Map map);
	/**
	 * 根据告警Id查询物料信息
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月23日
	 */
	List<Map> getAlertByRecId(Integer recId);
	/**
	 * 根据id删除
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	int deleteAlertById(Map map);

}

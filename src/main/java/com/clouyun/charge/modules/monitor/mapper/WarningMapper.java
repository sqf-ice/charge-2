package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: 告警Mapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0.0
 * 创建日期: 2017年3月23日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface WarningMapper {
	/**
	 * 根据警告的id更新警告信息
	 * 2017年4月20日
	 * 2.0.0
	 * gaohui
	 */
	void update(DataVo dataVo);
	
	/**
	 * 根据告警id查询告警信息
	 * @param id
	 * 2017年4月20日
	 * 2.0.0
	 * gaohui
	 */
    Map getById(Integer recId);
    /**
     * 查询告警列表
     * @param map
     * 2017年4月20日
     * 2.0.0
     * gaohui
     */
    List<DataVo> get(DataVo dataVo);
    /**
     * 根据parentId查询告警项
     * @param map
     * 2017年4月20日
     * 2.0.0
     * gaohui
     */
    List<Map> getAlarmItems(Integer parentId);
    /**
     * 告警项设置
     * @param map
     * 2017年4月20日
     * 2.0.0
     * gaohui
     */
    void modifyAlarmItem(Map map);
	/**
	 * 根据警告的id根据警告信息
	 * 2017年3月25日
	 * gaohui
	 */
	void updateWarningByRecId(Map map);
	
	/**
	 * 根据recId查询告警信息
	 * @param map
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
    Map findWarningByRecId(Map map);
    /**
     * 分页查询告警列表
     * @param map
     * @return
     * 2017年4月11日
     * gaohui
     */
    List<Map> findWarningListByPage(Map map);
    /**
     * 条件查询总页数
     * @param map
     * @return
     * 2017年4月11日
     * gaohui
     */
    Map findWarningCounts(Map map);
}

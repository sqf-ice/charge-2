package com.clouyun.charge.modules.monitor.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.charge.modules.monitor.mapper.AlertMapper;
import com.clouyun.charge.modules.monitor.mapper.WarningMapper;

/**
 * 
 * 描述:  任务、告警、物料关联
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月1日
 */
@Service
@SuppressWarnings("rawtypes")
public class AlertService {
	@Autowired
	private AlertMapper alertMapper;
	
	@Autowired
	private WarningMapper warningMapper;
	/**
	 * 添加物料信息
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	@Transactional
	public void addAlert(Map map){
		alertMapper.addAlert(map);
	}
	/**
	 * 根据告警Id查询物料信息
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	public List<Map> findAlertByRecId(Map map){
		return alertMapper.findAlertByRecId(map);
	}
	/**
	 * 根据id删除
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	public int deleteAlertById(Map map){
		return alertMapper.deleteAlertById(map);
	}

}

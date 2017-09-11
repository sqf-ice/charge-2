package com.clouyun.charge.modules.monitor.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.service.AlertService;

/**
 * 
 * 描述: 任务、告警、物料关联
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月1日
 */
@RestController
@RequestMapping("/api/alerts")
public class AlertController {
	@Autowired
    AlertService alertService;
	/**
	 * 添加物料信息
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	@RequestMapping(value = "/addAlert", method = RequestMethod.POST)
	public void addAlert(@RequestBody DataVo vo){
		alertService.addAlert(vo);
	}
	/**
	 * 根据告警Id查询物料信息
	 * @return
	 * 2017年4月1日
	 * gaohui
	 * @throws BizException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/findAlertByRecId", method = RequestMethod.POST)
	public ResultVo findAlertByRecId(@RequestBody DataVo vo) throws BizException{
		ResultVo resultVo = new ResultVo();
		List<Map> list = alertService.findAlertByRecId(vo);
		resultVo.setData(list);
		return resultVo;
	}
	/**
	 * 根据id删除
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
	@RequestMapping(value = "/delAlert", method = RequestMethod.POST)
	public int deleteAlertById(@RequestBody DataVo vo){
		return alertService.deleteAlertById(vo);
	}

}

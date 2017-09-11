package com.clouyun.charge.modules.charge.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.service.StationProfitTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 场站利润
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月14日 下午2:04:39
 */
@RestController
@RequestMapping("/api/profit/station/task")
public class StationProfitTaskController extends BusinessController{
	
	@Autowired
	StationProfitTaskService stationProfitTaskService;
	
	@RequestMapping(value = "/{month}", method = RequestMethod.GET)
	public ResultVo queryIncomeTrends(@PathVariable("month") String month) throws Exception {
		stationProfitTaskService.run(month);
		return new ResultVo();
	}
	
}

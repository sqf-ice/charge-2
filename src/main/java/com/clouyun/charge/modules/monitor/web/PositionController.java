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
import com.clouyun.charge.modules.monitor.service.PositionService;

/**
 * 位置信息
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月7日
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/positions")
public class PositionController {
	
	@Autowired
	private PositionService positionService;
	/**
	 * 根据taskId查询位置信息
	 * @param vo
	 * @return
	 * 2017年4月7日
	 * gaohui
	 */
	@RequestMapping(value = "/findPossTaskId", method = RequestMethod.POST)
    public ResultVo findPositionsByTaskId(@RequestBody DataVo vo){
		ResultVo resultVo = new ResultVo();
		List<Map> list = positionService.findPositionsByTaskId(vo);
		resultVo.setData(list);
		return resultVo;
    }

}

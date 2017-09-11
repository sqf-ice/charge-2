package com.clouyun.charge.modules.member.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.PointService;

/**
 * 描述: 积分
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月21日 上午11:21:36
 */
@RestController
@RequestMapping("/api/points")
public class PointController extends BusinessController{
	
	@Autowired
	PointService pointService;
	
	@RequestMapping(value = "/puborg", method = RequestMethod.POST)
	public ResultVo queryPubOrg(@RequestBody DataVo map) throws Exception {
		ResultVo vo = new ResultVo();
		Map queryPubOrg = pointService.queryPubOrg(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo queryPoints(@RequestBody DataVo map) throws Exception {
		ResultVo vo = new ResultVo();
		List<Map> queryPubOrg = pointService.queryPoints(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	
	@RequestMapping(value = "/type", method = RequestMethod.POST)
	public ResultVo queryByType(@RequestBody DataVo map) throws Exception {
		ResultVo vo = new ResultVo();
		Map queryPubOrg = pointService.queryByType(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResultVo insertPoints(@RequestBody DataVo map) throws Exception {
		ResultVo vo = new ResultVo();
		int queryPubOrg = pointService.insertPoints(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResultVo updatePoints(@RequestBody DataVo map) throws Exception {
		ResultVo vo = new ResultVo();
		int queryPubOrg = pointService.updatePoints(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	
}

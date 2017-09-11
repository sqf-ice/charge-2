package com.clouyun.charge.modules.member.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.CouponsService;


/**
 * 描述: 优惠券
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月17日 下午7:28:00
 */
@RestController
@RequestMapping("/api/coupons")
public class CouponsController extends BusinessController{
	
	@Autowired
	CouponsService couponsService;
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public ResultVo insertCoupons(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int count = couponsService.insertCoupons(map);
		vo.setData(count);
		return vo;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo queryCoupons(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		List<Map> result = couponsService.queryCoupons(map);
		vo.setData(result);
		return vo;
	}
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public ResultVo queryCouponsInfo(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		List<Map> result = couponsService.queryCouponsInfo(map);
		vo.setData(result);
		return vo;
	}
}

package com.clouyun.charge.modules.vehicle.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.modules.spring.web.BaseController;
import com.clouyun.charge.modules.vehicle.service.VehicleMileageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/**
 * 描述: 车辆里程
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年6月27日 上午9:04:18
 */
@RestController
@RequestMapping("/api/vehicle")
public class VehicleMileageController extends BaseController{
	
	@Autowired
	VehicleMileageService mileageService;
	
	@RequestMapping(value = "/mileage/run", method = RequestMethod.GET)
	public ResultVo queryDrivers(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		String runData = mileageService.runData();
		vo.setData(runData);
		return vo;
	}
	
	
}

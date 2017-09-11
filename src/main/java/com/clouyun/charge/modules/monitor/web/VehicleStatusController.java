package com.clouyun.charge.modules.monitor.web;

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
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.service.PhotoService;
import com.clouyun.charge.modules.monitor.service.VehicleStatusService;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 描述: VehicleStatusController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年8月14日
 */
@RestController
@RequestMapping("/api/monitor/vehicle")
public class VehicleStatusController {
	
	@Autowired
	VehicleStatusService vehicleStatusService;
	
	/**
	 * 
	 * 
	 * @api {get} /api/monitor/vehicle    车辆监控列表
	 * @apiName getVehicleMonitor
	 * @apiGroup VehicleStatusController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B  车辆监控列表
	 * <br/>
	 * @apiParam {Integer}    userId    用户Id
	 * @apiParam {Integer}    orgId     运营商Id
	 * @apiParam {String}    licensePlate    车牌号
	 * @apiParam {String}    manufacturer   车辆厂家
	 * @apiParam {String}    brand    车辆品牌
	 * @apiParam {String}    vehicleType   车辆类型
	 * @apiParam {Integer}    vehicleStatus    车辆状态
	 * @apiParam {Integer}    userId    用户Id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {String} data.list.orgName 运营商名称
	 * @apiSuccess {String} data.list.licensePlate 车牌号
	 * @apiSuccess {String} data.list.manufacturer 车辆厂家
	 * @apiSuccess {String} data.list.brand 车辆品牌
	 * @apiSuccess {String} data.list.vehicleType 车辆类型
	 * @apiSuccess {String} data.list.vehicleStatus 车辆状态
	 * @apiSuccess {Double} data.list.mileages 累计行驶里程
	 * @apiSuccess {String} data.list.GPSStatus 定位有效
	 * @apiSuccess {Double} data.list.vehicleSoc 电池容量(%)
	 * @apiSuccess {Date} data.list.timeTag 数据采集时间 (yyyy-MM-dd HH:mm:ss)
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping( method = RequestMethod.GET)
	public ResultVo getVehicleMonitor(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo list = vehicleStatusService.getVehicleMonitor(map);
		resultVo.setData(list);
		return resultVo;
	}

}

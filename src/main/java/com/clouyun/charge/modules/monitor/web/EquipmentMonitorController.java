package com.clouyun.charge.modules.monitor.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.service.EquipmentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 描述: 设备监控
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年8月8日 上午10:01:18
 */
@RestController
@RequestMapping("/api/monitor/equipment")
public class EquipmentMonitorController {
	
	@Autowired
	private EquipmentMonitorService equipmentMonitorService;

	/**
	 * @api {GET} /api/monitor/equipment/map/info     设备监控模块的场站详情
	 * @apiName mapToStationInfo
	 * @apiGroup EquipmentMonitorController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   根据场站id查看设备监控详情
	 * <br/>
	 * @apiParam {int}    userId 	 		登陆用户id
	 * @apiParam {int}    stationId   		场站id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object[]} data.totalInfo   合计数据
	 * @apiSuccess {String} data.totalInfo.totalIncome 			总收入
	 * @apiSuccess {String} data.totalInfo.dailyIncome 			日收入
	 * @apiSuccess {String} data.totalInfo.totalCharge 			总充电量
	 * @apiSuccess {String} data.totalInfo.totalElec 				总用电量
	 * @apiSuccess {String} data.totalInfo.daliyCharge 			日充电量
	 * @apiSuccess {String} data.totalInfo.dailyElec 				日用电量
	 * @apiSuccess {String} data.totalInfo.gunTotal 				总枪数
	 * @apiSuccess {String} data.totalInfo.dcGunTotal 				快枪数
	 * @apiSuccess {String} data.totalInfo.acGunTotal 				慢枪数
	 * @apiSuccess {String} data.totalInfo.chargingGun 			充电枪数
	 *
	 * @apiSuccess {Object[]} data.result							结果集
	 * @apiSuccess {String} data.result.title					头信息
	 * @apiSuccess {String} data.result.title.pileName 			充电桩名称
	 * @apiSuccess {String} data.result.title.status 				充电桩状态
	 * @apiSuccess {String} data.result.title.numberGun 			充电桩枪数
	 * @apiSuccess {String} data.result.title.begTime 				统计开始时间
	 * @apiSuccess {String} data.result.title.load 				负荷
	 * @apiSuccess {String} data.result.title.pileId 			充电桩id
	 *
	 * @apiSuccess {String} data.result.gunPointStatus         	枪口状态
	 * @apiSuccess {String} data.result.gunPointStatus.key 		这里的key表示枪,分别为01,02,03等等
	 * @apiSuccess {String} data.result.gunPointStatus.key.gunStatus 	枪状态
	 * @apiSuccess {String} data.result.gunPointStatus.key.UV 			电压
	 * @apiSuccess {String} data.result.gunPointStatus.key.IA 			电流
	 *
	 * @apiSuccess {String} data.result.totalCharge				累计电量
	 * @apiSuccess {String} data.result.totalCharge.key			这里的key表示枪,分别为01,02等,Z表示总表,loss表示损耗
	 * @apiSuccess {String} data.result.totalCharge.key.daily		当日(单位kWh)
	 * @apiSuccess {String} data.result.totalCharge.key.total		累计(单位kWh)
	 *
	 *
	 * @apiSuccess {String} data.result.chargeTimeMap				充电时长
	 * @apiSuccess {String} data.result.chargeTimeMap.key			这里的key表示枪,分别为01,02,03等等
	 * @apiSuccess {String} data.result.chargeTimeMap.key.daily	当日充电时长
	 * @apiSuccess {String} data.result.chargeTimeMap.key.total	累计充电时长
	 * @apiSuccess {String} data.result.chargeTimeMap.key.conversionRate	使用率
	 *
	 * @apiSuccess {String} data.result.powerMonitor				用电监控
	 * @apiSuccess {String} data.result.powerMonitor.key			这里的key表示枪,分别为01,02,03等等
	 * @apiSuccess {String} data.result.powerMonitor.key.motorTemp		电机温度
	 * @apiSuccess {String} data.result.powerMonitor.key.UV			电压
	 * @apiSuccess {String} data.result.powerMonitor.key.IA			电流
	 *
	 * @apiSuccess {String} data.result.bmsMap					BMS信息
	 * @apiSuccess {String} data.result.bmsMap.key				这里的key表示枪,分别为01,02,03等等
	 * @apiSuccess {String} data.result.bmsMap.key.bmsUV		BMS枪电压
	 * @apiSuccess {String} data.result.bmsMap.key.bmsIA		BMS枪电流
	 * @apiSuccess {String} data.result.bmsMap.key.bmsSoc		BMS的SOC
	 * @apiSuccess {String} data.result.bmsMap.key.isSoc		是否有soc,1:有;0:没有
	 *
	 * @apiSuccess {String} data.result.carMap						车辆信息
	 * @apiSuccess {String} data.result.carMap.key					这里的key表示枪,分别为01,02,03等等
	 * @apiSuccess {String} data.result.carMap.key.licensePlate	车牌
	 * @apiSuccess {String} data.result.carMap.key.brand			品牌
	 * @apiSuccess {String} data.result.carMap.key.onNumber		自编号
	 *
	 * @apiSuccess {String} data.result.consMap					会员信息
	 * @apiSuccess {String} data.result.consMap.key				这里的key表示枪,分别为01,02,03等等
	 * @apiSuccess {String} data.result.consMap.consPhone		会员电话号码
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *	"errorCode": 0,
	 *	"errorMsg": "操作成功!",
	 *	"total": 0,
	 *	"data": {
	 *		"totalInfo": {
	 *		"totalIncome": 98572.6,
	 *		"totalCharge": "4.03",
	 *		"daliyCharge": "0.00",
	 *		"acGunTotal": 15,
	 *		"dailyIncome": 215.61,
	 *		"chargingGun": 0,
	 *		"totalElec": "4.03",
	 *		"dailyElec": "0.15",
	 *		"gunTotal": 19,
	 *		"dcGunTotal": 4
	 *	},
	 *	"result": [
	 *		{
	 *		"title": {
	 *			"softVersion": "0000820000101112",
	 *			"numberGun": 2,
	 *			"pileModelNo": null,
	 *			"pileAddr": "82050590",
	 *			"pileNo": "000000001001001",
	 *			"pileLat": null,
	 *			"pileName": "1号桩",
	 *			"pileModelId": 1,
	 *			"hardVersion": "1112",
	 *			"pileLng": null,
	 *			"pileId": 45,
	 *			"load": 1.1,
	 *			"ortMode": 2,
	 *			"pileType": "4",
	 *			"gunTypeCode": null,
	 *			"stationName": "科陆大厦充电站",
	 *			"begTime": "2017-05-17 11:35:31",
	 *			"stationId": 64,
	 *			"status": "离线",
	 *			"statusNumber":2
	 *		}
	 *		"gunPointStatus": {
	 *			"01": {
	 *				"gunStatus": "未监控到",
	 *				"UV":"电机电压=637.7",
	 *				"IA":"电机电流=300.242"
	 *			},
	 *			"02": {
	 *				"gunStatus": "未监控到"
	 *			}
	 *		},
	 *		"totalCharge": {
	 *			"01": {
	 *				"total": "000.00",
	 *				"daily": "0"
	 *			},
	 *			"02": {
	 *				"total": "000.00",
	 *				"daily": "0"
	 *			},
	 *			"Z": {
	 *				"total": "0",
	 *				"daily": "0"
	 *			},
	 *			"loss": {}
	 *		},
	 *		"chargeTimeMap": {
	 *			"01": {
	 *				"total": "0分",
	 *				"daily": "0分",
	 *				"conversionRate": 0
	 *			},
	 *			"02": {
	 *				"total": "7时56分",
	 *				"daily": "0分",
	 *				"conversionRate": 0
	 *			}
	 *		},
	 *		"powerMonitor": {
	 *			"01": {
	 *				"motorTemp": 0
	 *			},
	 *			"02": {
	 *				"motorTemp": 0
	 *			}
	 *		},
	 *		"bmsMap": {},
	 *		"carMap": {},
	 *		"consMap": {
	 *			"01枪": {
	 *				"consPhone": ""
	 *			},
	 *			"02枪": {
	 *				"consPhone": ""
	 *			}
	 *		}
	 *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/map/info", method = RequestMethod.GET)
	public ResultVo mapToStationInfo(@RequestParam Map map) throws BizException {
		ResultVo resultVo = new ResultVo();
		Map resultMap = equipmentMonitorService.mapToStationInfo(map);
		resultVo.setData(resultMap);
		return resultVo;
	}


	/**
	 * @api {GET} /api/monitor/equipment/stopcharge/info/{pileId}     设备监控模块的场站详情根据桩id查看枪的状态
	 * @apiName stopChargeInfo
	 * @apiGroup EquipmentMonitorController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   根据桩id查看枪的状态
	 * <br/>
	 * @apiParam {int}    pileId   		桩id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object[]} data   数据
	 * @apiSuccess {String} data.gumPoint 			枪口名称
	 * @apiSuccess {String} data.status				状态
	 * @apiSuccess {Int} data.gunId					枪id
	 * @apiSuccess {Int} data.statusNum				枪状态id
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *	"errorCode": 0,
	 *	"errorMsg": "操作成功!",
	 *	"total": 0,
	 *	"data": [
	 *		{
	 *			"gumPoint": "01枪",
	 *			"status": "未监控到",
	 *			"gunId": 4912,
     *		    "statusNum":32
	 *		},
	 *		{
	 *			"gumPoint": "02枪",
	 *			"status": "未监控到",
	 *			"gunId": 4913,
     *		    "statusNum":32
	 *		}
	 *	],
	 *	"exception": null
	 *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/stopcharge/info/{pileId}", method = RequestMethod.GET)
	public ResultVo stopChargeInfo(@PathVariable("pileId") Integer pileId) throws BizException {
		ResultVo resultVo = new ResultVo();
		List<Map> maps = equipmentMonitorService.stopChargeInfo(pileId);
		resultVo.setData(maps);
		return resultVo;
	}

	/**
	 * @api {GET} /api/monitor/equipment/stopcharge/{gunId}     设备监控场站详情根据枪id停止充电
	 * @apiName stopCharge
	 * @apiGroup EquipmentMonitorController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   根据枪id停止充电
	 * <br/>
	 * @apiParam {int}    gunId   		枪id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {String} data   	停止充电返回信息,直接弹窗提示
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *		"errorCode": 0,
	 *		"errorMsg": "操作成功!",
	 *		"total": 0,
	 *		"data": "23450020:1,不在充电状态.32",
	 *		"exception": null
	 *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/stopcharge/{gunId}", method = RequestMethod.GET)
	public ResultVo stopCharge(@PathVariable("gunId") Integer gunId) throws BizException {
		ResultVo resultVo = new ResultVo();
		String message = equipmentMonitorService.stopCharge(gunId);
		resultVo.setData(message);
		return resultVo;
	}


	/**
	 * @api {GET} /api/monitor/equipment/stopcharge/batch/{gunId}     设备监控场站详情根据枪id批量停止充电
	 * @apiName batchStopCharge
	 * @apiGroup EquipmentMonitorController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   根据枪id批量停止充电(/api/monitor/equipment/stopcharge/batch/4912,4913)
	 * <br/>
	 * @apiParam {List}    gunIds   		枪id集合
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object[]} data   	停止充电返回信息
	 * @apiSuccess {String} data.gumPoint   	枪口名称
	 * @apiSuccess {String} data.message	   	停止充电返回信息
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *		"errorCode": 0,
	 *		"errorMsg": "操作成功!",
	 *		"total": 0,
	 *		"data": [
	 *		{
	 *			"gumPoint": "充电枪01",
	 *			"message": "23450020:1,不在充电状态.32"
	 *		},
	 *		{
	 *			"gumPoint": "充电枪02",
	 *			"message": "23450020:2,不在充电状态.32"
	 *		}
	 *	],
	 *		"exception": null
	 *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/stopcharge/batch/{gunIds}", method = RequestMethod.GET)
	public ResultVo batchStopCharge(@PathVariable("gunIds") List<Integer> gunIds) throws BizException {
		ResultVo resultVo = new ResultVo();
		List<Map> maps = equipmentMonitorService.batchStopCharge(gunIds);
		resultVo.setData(maps);
		return resultVo;
	}


}

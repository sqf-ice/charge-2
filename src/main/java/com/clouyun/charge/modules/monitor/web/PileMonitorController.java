package com.clouyun.charge.modules.monitor.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.service.PileMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author fft
 *
 */
@RestController
@RequestMapping("/api/monitor")
public class PileMonitorController {
	
	@Autowired
	private PileMonitorService pileMonitorService;
	
	/**
     * @api {GET} /api/monitor/piles/station/{stationId}     设备监控模块的场站详情
     * @apiName findStationInfo
     * @apiGroup PileMonitorController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾   设备监控模块的场站详情
     * <br/>
     * @apiParam {int}    stationId     		场站Id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        数据封装
     * @apiSuccess {double} data.zsr 	总收入
     * @apiSuccess {double} data.rsr 	日收入
     * @apiSuccess {double} data.ljcdl 	总充电量
     * @apiSuccess {double} data.ljhdl 	总用电量
     * @apiSuccess {double} data.dljcdl 日充电量
     * @apiSuccess {double} data.dljhdl 日用电量
     * @apiSuccess {int} data.gunTotal 总枪数
     * @apiSuccess {int} data.dcGunTotal 快
     * @apiSuccess {int} data.acGunTotal   慢
     * @apiSuccess {int} data.chargingGun   充电枪数
     * @apiSuccess {Object[]} data.pilesMap  充电桩详情
     * @apiSuccess {String} data.pilesMap.pileAddr   充电桩地址
     * @apiSuccess {String} data.pilesMap.pileName    设备名称
     * @apiSuccess {String} data.pilesMap.status     充电桩状态：充电，空闲，异常，停用,放电,离线
     * @apiSuccess {String} data.pilesMap.gunTypeCode	枪数
     * @apiSuccess {String} data.pilesMap.begTime 	统计开始时间
     * @apiSuccess {String} data.pilesMap.load 	桩上线情况：负荷：0.34 ，最近没上线
     * @apiSuccess {String} data.pilesMap.gunStatus 	枪口状态
     * @apiSuccess {String} data.pilesMap.gunDl    累计电量
     * @apiSuccess {String} data.pilesMap.cdscStr  充电时长
     * @apiSuccess {String} data.pilesMap.gunUse  AB枪累计时间
     * @apiSuccess {String} data.pilesMap.ydjkStr 用电监控
     * @apiSuccess {String} data.pilesMap.wd      温度
     * @apiSuccess {String} data.pilesMap.bmsStr   BMS
     * @apiSuccess {String} data.pilesMap.cphStr 车牌号
     * @apiSuccess {String} data.pilesMap.cppStr  车品牌
     * @apiSuccess {String} data.pilesMap.czbhStr  车自编号
     * @apiSuccess {String} data.pilesMap.pileType  桩类型
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
			"errorCode": 0,
			"errorMsg": "操作成功!",
			"total": 0,
			"data": {
			"gunTotal": 5,
			"zsr": 0,
			"dljcdl": 0,
			"dcGunTotal": 2,
			"dljhdl": 0,
			"ljhdl": 0,
			"ljcdl": 0,
			"rsr": 0,
			"chargingGun": 0,
			"acGunTotal": 3,
			"pilesMap": [
			{
			"pileModelId": 10,
			"gunDl": "A枪当日：<b style='color:#1E90FF;'>00.00</b>kWh;        A枪累计：<b style='color:#1E90FF;'>000.97</b>kWh;        ",
			"stationId": 684,
			"pileAddr": "82051001",
			"pileLng": null,
			"cphStr": "",
			"pileNo": "21068888028001",
			"gunUse": "",
			"pileTypeCope": "1",
			"ydjkStr": "电压：<b style='color:#1E90FF;'>0</b>V，电流：<b style='color:#1E90FF;'>0</b>A",
			"pileName": "交流单充1号",
			"begTime": "2016-12-26 10:05:35",
			"stationName": "充电桩测试用",
			"pileId": 2836,
			"pileLat": null,
			"status": "空闲",
			"softVersion": "0000820000101112",
			"cppStr": "",
			"czbhStr": "",
			"cdscStr": "A枪当日:<b style='color:#1E90FF;'>0分</b> 累计:<b style='color:#1E90FF;'>0分</b>;使用率:<b style='color:#1E90FF;'>0</b>%",
			"gunStatus": "A枪：空闲",
			"bmsStr": "",
			"pileType": "1",
			"pileModelNo": null,
			"gunTypeCode": 1,
			"hardVersion": "1112",
			"load": "负荷：1.1",
			"wd": "<b style='color:#1E90FF;'>0</b>℃"
			},
			{
			"pileModelId": 1,
			"gunDl": "A枪累计：<b style='color:#1E90FF;'>000.00</b>kWh;        ",
			"stationId": 684,
			"pileAddr": "66660001",
			"pileLng": null,
			"cphStr": "",
			"pileNo": "21068888028100",
			"gunUse": "",
			"pileTypeCope": "1",
			"ydjkStr": "电压：<b style='color:#1E90FF;'>0</b>V，电流：<b style='color:#1E90FF;'>0</b>A",
			"pileName": "01号桩",
			"begTime": "2016-12-23 08:45:31",
			"stationName": "充电桩测试用",
			"pileId": 2840,
			"pileLat": null,
			"status": "离线",
			"softVersion": "0000820000101112",
			"cppStr": "",
			"czbhStr": "",
			"cdscStr": "A枪当日:<b style='color:#1E90FF;'>0分</b> 累计:<b style='color:#1E90FF;'>0分</b>;使用率:<b style='color:#1E90FF;'>0</b>%",
			"gunStatus": "A枪：未监控到",
			"bmsStr": "",
			"pileType": "1",
			"pileModelNo": "clou1244345",
			"gunTypeCode": 1,
			"hardVersion": "1112",
			"load": "掉线时间：2017-06-05 17:51:10",
			"wd": "<b style='color:#1E90FF;'>0</b>℃"
			},
			{
			"pileModelId": 1,
			"gunDl": "A枪当日：<b style='color:#1E90FF;'>00.00</b>kWh;        A枪累计：<b style='color:#1E90FF;'>000.00</b>kWh;        ",
			"stationId": 684,
			"pileAddr": "88882017",
			"pileLng": null,
			"cphStr": "",
			"pileNo": "21068888028023",
			"gunUse": "",
			"pileTypeCope": "1",
			"ydjkStr": "电压：<b style='color:#1E90FF;'>0</b>V，电流：<b style='color:#1E90FF;'>0</b>A",
			"pileName": "15楼测试桩88882017",
			"begTime": "2017-06-07 09:20:32",
			"stationName": "充电桩测试用",
			"pileId": 2846,
			"pileLat": null,
			"status": "空闲",
			"softVersion": "0000820000101112",
			"cppStr": "",
			"czbhStr": "",
			"cdscStr": "A枪当日:<b style='color:#1E90FF;'>0分</b> 累计:<b style='color:#1E90FF;'>0分</b>;使用率:<b style='color:#1E90FF;'>0</b>%",
			"gunStatus": "A枪：空闲",
			"bmsStr": "",
			"pileType": "1",
			"pileModelNo": "clou1244345",
			"gunTypeCode": 1,
			"hardVersion": "1112",
			"load": "负荷：1.1",
			"wd": "<b style='color:#1E90FF;'>0</b>℃"
			},
			{
			"pileModelId": 1,
			"gunDl": "A枪当日：<b style='color:#1E90FF;'>00.00</b>kWh;        A枪累计：<b style='color:#1E90FF;'>000.98</b>kWh;        <br>B枪当日：<b style='color:#1E90FF;'>00.00</b>kWh;        B枪累计：<b style='color:#1E90FF;'>000.00</b>kWh;        <br>总表当日:<b>00.00</b>kWh;   总表累计:<b>000.98</b>kWh;   <br>&nbsp;&nbsp;&nbsp;&nbsp;总损耗:<b>0%</b>",
			"stationId": 684,
			"pileAddr": "86280104",
			"pileLng": null,
			"cphStr": "",
			"pileNo": "21068888028111",
			"gunUse": "",
			"pileTypeCope": "4",
			"ydjkStr": "电压：<b style='color:#1E90FF;'>220.0</b>V，电流：<b style='color:#1E90FF;'>5.0</b>A",
			"pileName": "龙岗315KW直流",
			"begTime": "2017-06-08 11:08:23",
			"stationName": "充电桩测试用",
			"pileId": 2853,
			"pileLat": null,
			"status": "空闲",
			"softVersion": "0000820000101112",
			"cppStr": "",
			"czbhStr": "",
			"cdscStr": "A枪当日:<b style='color:#1E90FF;'>0分</b> 累计:<b style='color:#1E90FF;'>18分</b>;使用率:<b style='color:#1E90FF;'>1</b>%<br>B枪当日:<b style='color:#1E90FF;'>0分</b> 累计:<b style='color:#1E90FF;'>1分</b>;使用率:<b style='color:#1E90FF;'>0</b>%",
			"gunStatus": "A枪：空闲<br>B枪：空闲",
			"bmsStr": "",
			"pileType": "4",
			"pileModelNo": "clou1244345",
			"gunTypeCode": 2,
			"hardVersion": "1112",
			"load": "负荷：1.1",
			"wd": ""
			}
			]
			}
			}
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/piles/station/{stationId}", method = RequestMethod.GET)
    public ResultVo findStationInfo(@PathVariable("stationId") Integer stationId){
		ResultVo resultVo = new ResultVo();
		Map stationMap = pileMonitorService.stationInfoBystationId(stationId);
		resultVo.setData(stationMap);
		return resultVo;
    }
	
	/**
	 * 
	 * 
	 * @api {get} /api/monitor/stations   查询监控中的充电桩列表
	 * @apiName getStationAll
	 * @apiGroup PileMonitorController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾   根据区域,运营商,场站,使用状态,经营状态,设备类型,设备状态,设备型号条件查询充电桩列表
	 * <br/>
	 * @apiParam {Integer} userId	 用户Id
	 * @apiParam {String} [provCode] 省 
	 * @apiParam {String} [cityCode] 市 
	 * @apiParam {Integer} [orgId] 运营商Id 
	 * @apiParam {String} [orgName] 运营商名称 
	 * @apiParam {String} [stationName] 场站名称 
	 * @apiParam {String} [useStatus] 使用状态     字典 zt: 0:有效 1:无效
	 * @apiParam {String} [stationType] 经营类型  字典 jingylx
	 * @apiParam {String} [ortMode] 交直模式 type:jzms  2.00新加字段
	 * @apiParam {String} [status] 设备状态 字典 sbzt 
	 * @apiParam {String} [pileModelId] 设备型号 字典 sbxh
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 数据对象数组
	 * @apiSuccess {String} data.list.orgName 运营商
	 * @apiSuccess {String} data.list.stationName  场站
	 * @apiSuccess {String} data.list.stationType 经营类型  字典 jingylx
	 * @apiSuccess {String} data.list.stationStatus 运营状态 字典 yyzt
	 * @apiSuccess {Integer(2)} data.list.pileCount 充电桩总数
	 * @apiSuccess {Integer(2)} data.list.acPile 交流充电桩数量
	 * @apiSuccess {Integer(2)} data.list.dcPile 直流充电桩
	 * @apiSuccess {Integer(2)} data.list.acDcPile 交直流充电桩
	 * @apiSuccess {Integer(2)} data.list.offlinePile 离线充电桩数量
	 * @apiSuccess {Integer(2)} data.list.kxPile 空闲充电桩数量
	 * @apiSuccess {Integer(2)} data.list.chargePile 充电充电桩数量
	 * @apiSuccess {Integer(2)} data.list.ycPile 异常充电桩数量
	 * @apiSuccess {Integer(2)} data.list.tyPile 停用充电桩数量
	 * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
     *  "errorCode": 0,
		  "errorMsg": "操作成功!",
		  "total": 0,
		  "data": [
		    {
		      "offlinePile": 9,
		      "orgId": 2,
		      "acPile": 9,
		      "pileCount": 9,
		      "stationStatus": "1",
		      "stationId": 188,
		      "orgName": "车电网",
		      "stationName": "宝深路充电站002",
		      "stationType": "100"
		    },
		    {
		      "offlinePile": 13,
		      "orgId": 2,
		      "acPile": 4,
		      "pileCount": 13,
		      "dcPile": 9,
		      "stationStatus": "50",
		      "stationId": 197,
		      "orgName": "车电网",
		      "stationName": "科兴科学园充电站2",
		      "stationType": "1"
		    },
		    {
		      "offlinePile": 1,
		      "orgId": 2,
		      "acPile": 1,
		      "pileCount": 1,
		      "stationStatus": "50",
		      "stationId": 198,
		      "orgName": "车电网",
		      "stationName": "科兴科学园充电站3",
		      "stationType": "1"
		    },
		    {
		      "offlinePile": 19,
		      "orgId": 2,
		      "pileCount": 19,
		      "dcPile": 13,
		      "acDcPile": 6,
		      "stationStatus": "50",
		      "stationId": 202,
		      "orgName": "车电网",
		      "stationName": "测试科兴科学园充电站",
		      "stationType": "1"
		    },
		    {
		      "offlinePile": 6,
		      "orgId": 16,
		      "pileCount": 6,
		      "dcPile": 6,
		      "stationStatus": "0",
		      "stationId": 72,
		      "orgName": "特斯拉",
		      "stationName": "e333",
		      "stationType": "1"
		    },
		    {
		      "offlinePile": 45,
		      "orgId": 17,
		      "pileCount": 45,
		      "dcPile": 45,
		      "stationStatus": "50",
		      "stationId": 130,
		      "orgName": "中原电子",
		      "stationName": "车电网场站",
		      "stationType": "1"
		    }]
     *    }
     * }
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/stations", method = RequestMethod.GET)
    public ResultVo getStationAll(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		List<Map> stationMap = pileMonitorService.getStationAll(map);
		resultVo.setData(stationMap);
		return resultVo;
    }
	/**
	 * @api {GET} /api/monitor/piles/{pileId}/chart 获取充电桩充电中枪的过程数据
	 * @apiName chargeProcessData
	 * @apiGroup PileMonitorController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅A   获取充电桩充电中枪的过程数据
	 * <br/>
	 * @apiParam {int}    pileId     	充电桩ID
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Boolean} data.charging        是否在充电(true|false 不在充电状态只有在线统计模块，别的模块不显示)
	 * @apiSuccess {String} data.type        充电桩类型(1:交流（交流桩没有用电实时数据，没有BMS监测数据模块） 2:直流（直流桩5块都有）)
	 * @apiSuccess {Object} data.use 	用电实时输数据
	 * @apiSuccess {Object[]} data.use.u 	电压监测
	 * @apiSuccess {String} data.use.u.gunName 	名称
	 * @apiSuccess {Double[]} data.use.u.process 	充电过程数据
	 * @apiSuccess {String[]} data.use.u.time 	充电过程时标
	 * @apiSuccess {Object[]} data.use.i 	电流监测
	 * @apiSuccess {String} data.use.i.gunName 	名称
	 * @apiSuccess {Double[]} data.use.i.process 	充电过程数据
	 * @apiSuccess {String[]} data.use.i.time 	充电过程时标
	 * @apiSuccess {Object[]} data.use.p 	功率监测
	 * @apiSuccess {String} data.use.p.gunName 	名称
	 * @apiSuccess {Double[]} data.use.p.process 	充电过程数据
	 * @apiSuccess {String[]} data.use.p.time 	充电过程时标
	 * @apiSuccess {Object} data.charge 充电监测数据
	 * @apiSuccess {Object[]} data.charge.u 	电压监测
	 * @apiSuccess {String} data.charge.u.gunName 	名称
	 * @apiSuccess {Double[]} data.charge.u.process 	充电过程数据
	 * @apiSuccess {String[]} data.charge.u.time 	充电过程时标
	 * @apiSuccess {Object[]} data.charge.i 	电流监测
	 * @apiSuccess {String} data.charge.i.gunName 	名称
	 * @apiSuccess {Double[]} data.charge.i.process 	充电过程数据
	 * @apiSuccess {String[]} data.charge.i.time 	充电过程时标
	 * @apiSuccess {Object[]} data.charge.p 	功率监测
	 * @apiSuccess {String} data.charge.p.gunName 	名称
	 * @apiSuccess {Double[]} data.charge.p.process 	充电过程数据
	 * @apiSuccess {String[]} data.charge.p.time 	充电过程时标
	 * @apiSuccess {Object} data.bms BMS监测数据
	 * @apiSuccess {Double[]} data.bms.iu 	电流电压
	 * @apiSuccess {String} data.bms.iu.gunName 	枪名
	 * @apiSuccess {Double[]} data.bms.iu.var1 	电流充电过程数据
	 * @apiSuccess {Double[]} data.bms.iu.var2 	电压充电过程数据
	 * @apiSuccess {String[]} data.bms.iu.time 	充电过程时标
	 * @apiSuccess {Double[]} data.bms.ws 	温度SOC
	 * @apiSuccess {String} data.bms.ws.gunName 	枪名
	 * @apiSuccess {Double[]} data.bms.ws.var1 	充电过程温度
	 * @apiSuccess {Double[]} data.bms.ws.var2 	电压充电过程SOC
	 * @apiSuccess {String[]} data.bms.ws.time 	充电过程时标
	 * @apiSuccess {Object} data.income 收入监控
	 * @apiSuccess {Double} data.income.todayAmount 	今日收入
	 * @apiSuccess {String} data.income.totalAmount 	历史总收入
	 * @apiSuccess {String} data.income.totalPower 	历史总电量
	 * @apiSuccess {Object[]} data.income.sr 	收入集合
	 * @apiSuccess {String} data.income.sr.gunName 	枪名
	 * @apiSuccess {Double[]} data.income.sr.var1 	充电量过程数据
	 * @apiSuccess {Double[]} data.income.sr.var2 	充电过程收入数据
	 * @apiSuccess {String[]} data.income.sr.time 	充电过程时标
	 * @apiSuccess {Object} data.online 在线统计
	 * @apiSuccess {String} data.online.online 	在线率
	 * @apiSuccess {String} data.online.lostLine 	掉线次数
	 * @apiSuccess {Object[]} data.online.hearBeat 	掉线描述
	 * @apiSuccess {String} data.online.hearBeat.sj 	时间
	 * @apiSuccess {String} data.online.hearBeat.ms 	描述
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例
	 * {
		"errorCode": 0,
		"errorMsg": "操作成功!",
		"total": 0,
		"data": {
		"use": {
		"u": [],
		"i": [],
		"p": []
		},
		"charge": {
		"u": [],
		"i": [],
		"p": []
		},
		"bms": {
		"iu": [],
		"ws": []
		},
		"income": {
		"todayAmount": 0,
		"totalAmount": 0,
		"totalPower": 0,
		"sr": []
		},
		"online": {
		"online": "0%",
		"lostLine": "0次",
		"hearBeat": [
		{
		"ms": "2017-08-09 18:11:20:10.13.3.27:59558=>10.13.3.27:6325.掉线",
		"sj": "2017-08-09 18:11:20"
		},
		{
		"ms": "2017-08-09 10:10:56:10.13.3.27:59558=>10.13.3.27:6325.上线",
		"sj": "2017-08-09 10:10:56"
		},
		{
		"ms": "2017-08-08 18:15:05:10.13.3.27:43577=>10.13.3.27:6325.掉线",
		"sj": "2017-08-08 18:15:05"
		}
		]
		},
		"isCharge": false,
		"type": null
		},
		"exception": null
		}
	 * */
    @RequestMapping(value = "/piles/{pileId}/chart", method = RequestMethod.GET)
	public ResultVo chargeProcessData(@PathVariable Integer pileId) throws Exception {
		ResultVo resultVo= new ResultVo();
		resultVo.setData(pileMonitorService.chargeProcessData(pileId));
		return resultVo;
	}
	

}

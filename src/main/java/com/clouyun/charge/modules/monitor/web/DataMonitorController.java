package com.clouyun.charge.modules.monitor.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.modules.monitor.service.DataMonitorService;

/**
 * 
 * @author fft
 *
 */
@RestController
@RequestMapping("/api/datamonitor")
public class DataMonitorController {
	
	@Autowired
	private DataMonitorService dataMonitorService;
	
	/**
	 * 
		 * 
		 * @api {get} /api/datamonitor/piles   充电桩监控信息
		 * @apiName findpiles
		 * @apiGroup PileMonitorController
		 * @apiVersion 2.0.0
		 * @apiDescription 付飞腾  充电桩监控信息
		 * <br/>
		 * @apiParam {int}    pileId     			桩Id
		 * @apiParam {int}    pileType     			桩类型 ：1单交流充电桩 2 单直流 3双交流 4双直流 5交直流充电桩
		 * @apiParam {int}    pileAddr     			充电桩地址
		 * <br/>
		 * @apiSuccess {String}    errorCode         	错误码
		 * @apiSuccess {String}    errorMsg    		消息说明
		 * @apiSuccess {Object}    data        		分页数据封装
		 * @apiSuccess {Object[]}  data.arr 	           当天在线率，当天掉线次数
		 * @apiSuccess {String}    data.arr[0]       当天在线率
		 * @apiSuccess {String}    data.arr[1]      当天掉线次数
		 * @apiSuccess {Object[]}   data.xt1 		全部在线情况(心跳信息)
		 * @apiSuccess {String}   data.xt1.ms 	描述
         * @apiSuccess {String}   data.xt1.sj 	时间
         * @apiSuccess {Object[]}   data.xt 		有效在线情况(心跳信息)
		 * @apiSuccess {String}   data.xt.ms 	描述
         * @apiSuccess {String}   data.xt.sj 	时间
         * @apiSuccess {boolean}   data.isCharge 是否处于充电状态(true :会有: 用电实时数据 ,充电监测数据,BMS 监测数据开始 ,收入监控开始,在线统计  false :只有在线统计)
         * @apiSuccess{String} data.aU  		  A枪电压
		* @apiSuccess{String} data.aI 	 A枪电流
		* @apiSuccess {String}data.aP  	 A枪功率
		* @apiSuccess {String}data.bU  	 B枪电压
		* @apiSuccess{String} data.bI  	 B枪电流
		* @apiSuccess{String} data.bP  	 B枪功率
		* @apiSuccess {String}data.zU 	 总表电压
		* @apiSuccess {String}data.zI  	 总表电流
		* @apiSuccess {String}data.zP  	 总表
		* @apiSuccess{String} data.aDl 	 A枪电量
		* @apiSuccess {String}data.aSr 	 A枪收入
		* @apiSuccess {String}data.bDl 	 B枪电量
		* @apiSuccess  {String}data.bSr 	 B枪收入
		* @apiSuccess {String} data.aWd 	 A枪温度
		* @apiSuccess  {String}data.bWd 	 B枪温度
		* @apiSuccess  {String}data.aBmsU	 A枪BMS电压
		* @apiSuccess {String}data.aBmsI 	 A枪BMS电流
		* @apiSuccess {String}data.bBmsU 	 B枪BMS电压
		* @apiSuccess {String} data.bBmsI 	 B枪BMS电流
		* @apiSuccess {String}data.AUipTime	 A枪时间轴
		* @apiSuccess {String} data.BUipTime 	 B枪时间轴
		* @apiSuccess  {String}data.ZUipTime 	 总表时间轴
		* @apiSuccess {String} data.ADlTime 	 A枪电量时间轴
		* @apiSuccess {String} data.BDlTime 	 B枪电量时间轴
		* @apiSuccess  {String}data.data.ABmsTime 	 A枪BMS时间轴
		* @apiSuccess  {String}data.BBmsTime 	 B枪BMS时间轴
		* @apiSuccess {String} data.AWdTime 	 A枪温度时间轴
		* @apiSuccess  {String}data.BWdTime 	 B枪温度时间轴
		* @apiSuccess  {String}data.socA 	 A枪 SOC
		* @apiSuccess  {String}data.socB 	 B枪 SOC
		* @apiSuccess  {Object[]}data.charge 	 收入情况
		* @apiSuccess  {String}data.charge[0] 	 当日收入
		* @apiSuccess  {String}data.charge[1] 	 桩总收入
		* @apiSuccess  {String}data.charge[2] 	 桩总充电量
		* <br/>
	 	* @apiSuccessExample {json} Success出参示例:{
	 	* {
				errorCode: 0,
				errorMsg: "操作成功!",
				total: 0,
				data: {
				arr: [
				100,
				0
				],
				xt1: [
				{
				ms: "2017-06-06 08:41:16:10.13.3.27:54702=>10.13.3.27:6325.上线",
				sj: "2017-06-06 08:41:16"
				},
				{
				ms: "2017-06-06 08:29:24:10.13.3.27:47367=>10.13.3.27:6325.掉线",
				sj: "2017-06-06 08:29:24"
				},
				{
				ms: "2017-06-06 08:12:17:10.13.3.27:47367=>10.13.3.27:6325.上线",
				sj: "2017-06-06 08:12:17"
				},
				{
				ms: "2017-06-06 08:11:06:10.13.3.27:40586=>10.13.3.27:6325.掉线",
				sj: "2017-06-06 08:11:06"
				}
				],
				isCharge: false,
				xt: [
				{
				ms: "2017-06-06 08:41:16:10.13.3.27:54702=>10.13.3.27:6325.上线",
				sj: "2017-06-06 08:41:16"
				},
				{
				ms: "2017-06-06 08:29:24:10.13.3.27:47367=>10.13.3.27:6325.掉线",
				sj: "2017-06-06 08:29:24"
				},
				{
				ms: "2017-06-06 08:12:17:10.13.3.27:47367=>10.13.3.27:6325.上线",
				sj: "2017-06-06 08:12:17"
				},
				{
				ms: "2017-06-06 08:11:06:10.13.3.27:40586=>10.13.3.27:6325.掉线",
				sj: "2017-06-06 08:11:06"
				}
				]
				}
				}
	    * @apiError -999  系统异常
	 	*/
	@RequestMapping(value = "/piles", method = RequestMethod.GET)
    public ResultVo findpiles(@RequestParam Map map){
		
		ResultVo resultVo = new ResultVo();
		Map dataMap = dataMonitorService.getDataMonitor(map);
		resultVo.setData(dataMap);
		return resultVo;
    }

}

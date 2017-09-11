package com.clouyun.charge.modules.charge.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.service.StationIncomeService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: StationIncomeService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年9月2日
 */

@RequestMapping(value="/api/income/station")
@RestController
public class StationIncomeController extends BusinessController{
	
	@Autowired
	StationIncomeService stationIncomeService;
	
	/**
	 * 
	 * @api {get} /api/income/station    场站收入
	 * @apiName getStationIncome
	 * @apiGroup StationIncomeController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 场站收入
	 * <br/>
	 * @apiParam {Int}        userId                 用户Id
	 * @apiParam {Date}       startDate              开始日期(yyyy-mm-dd)
	 * @apiParam {Date}       endDate                结束日期(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]            场站Id
	 * @apiParam {String}     [stationName]          场站名称 模糊查询
	 * @apiParam {int}        [orgId]                运营商Id
	 * @apiParam {String}     [orgName]              运营商名称
	 * @apiParam {int}        pageNum                页码
	 * @apiParam {int}        pageSize               页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                   	 错误码
	 * @apiSuccess {String}       errorMsg                    	 消息说明
	 * @apiSuccess {Object}       data                    		 分页数据封装
	 * @apiSuccess {Object[]}     data.list                    	 分页数据对象数组
	 * @apiSuccess {Double}       data.list.time                  日期   
	 * @apiSuccess {Integer}      data.list.orgId 			 	 运营商Id
	 * @apiSuccess {String}       data.list.orgName 			 运营商名
	 * @apiSuccess {Integer}      data.list.stationId 			 场站Id
	 * @apiSuccess {String}       data.list.stationName          场站名
	 * @apiSuccess {Double}       data.list.amount               总收入(元)
	 * @apiSuccess {Double}       data.list.elceFee              电费总收入(元)
	 * @apiSuccess {Double}       data.list.noElceFee            非费率总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg1                       尖价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg2                       峰价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg3                       平价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg4                       谷价总收入(元)
	 * @apiSuccess {Double}       data.list.servFee              服务费收入(元/kW·h)
	 * @apiSuccess {Double}       data.list.parkFee              停车费收入(元)
	 * @apiSuccess {Double}       data.list.chgPower             充电总量(kW·h)
	 * @apiSuccess {Double}       data.list.noChgPower           非费率电量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg1 			  尖价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg2                         峰价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg3                         平价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg4                         谷价总量(kW·h)
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1002001 {0}不能为空!
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResultVo getStationIncome(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo pageList = stationIncomeService.getStationIncome(map);
		resVo.setData(pageList);
		return resVo;
	}

	/**
	 * 
	 * @api {get} /api/income/station/_total    场站收入 [合计]
	 * @apiName getStationIncomeTotal
	 * @apiGroup StationIncomeController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 场站收入 [合计]
	 * <br/>
	 * @apiParam {Int}        userId                 用户Id
	 * @apiParam {Date}       startDate              开始日期(yyyy-mm-dd)
	 * @apiParam {Date}       endDate                结束日期(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]            场站Id
	 * @apiParam {String}     [stationName]          场站名称 模糊查询
	 * @apiParam {int}        [orgId]                运营商Id
	 * @apiParam {String}     [orgName]              运营商名称
	 * <br/>
	 * @apiSuccess {String}       errorCode                 错误码
	 * @apiSuccess {String}       errorMsg                  消息说明
	 * @apiSuccess {Object}       data                    	数据封装
	 * @apiSuccess {Integer}      data.stationId 			 主键（场站Id）
	 * @apiSuccess {String}       data.stationNo			 场站编号
	 * @apiSuccess {String}       data.stationName          场站名
	 * @apiSuccess {Double}       data.amount               总收入(元)
	 * @apiSuccess {Double}       data.elceFee              电费总收入(元)
	 * @apiSuccess {Double}       data.noElceFee            非费率总收入(元)
	 * @apiSuccess {Double}       data.incomeZxyg1                       尖价总收入(元)
	 * @apiSuccess {Double}       data.incomeZxyg2                       峰价总收入(元)
	 * @apiSuccess {Double}       data.incomeZxyg3                       平价总收入(元)
	 * @apiSuccess {Double}       data.incomeZxyg4                       谷价总收入(元)
	 * @apiSuccess {Double}       data.servFee              服务费收入(元/kW·h)
	 * @apiSuccess {Double}       data.parkFee              停车费收入(元)
	 * @apiSuccess {Double}       data.chgPower             充电总量(kW·h)
	 * @apiSuccess {Double}       data.noChgPower           非费率电量(kW·h)
	 * @apiSuccess {Double}       data.powerZxyg1 			  尖价总量(kW·h)
	 * @apiSuccess {Double}       data.powerZxyg2                         峰价总量(kW·h)
	 * @apiSuccess {Double}       data.powerZxyg3                         平价总量(kW·h)
	 * @apiSuccess {Double}       data.powerZxyg4                         谷价总量(kW·h)
	 * @apiSuccess {String}       data.orgName              运营商名称
	 * @apiSuccess {Integer}       data.orgId                运营商Id
	 * @apiSuccess {String}       data.time                  日期   
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1002001 {0}不能为空!
	 */
	@RequestMapping(value = "_total",method = RequestMethod.GET)
	public ResultVo getStationIncomeTotal(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		DataVo vo = stationIncomeService.getStationIncomeTotal(map);
		resVo.setData(vo);
		return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {get} /api/income/station/_export    场站收入导出
	 * @apiName exportStationIncome
	 * @apiGroup StationIncomeController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 场站收入导出
	 * <br/>
	 * @apiParam {Int}        userId                 用户Id
	 * @apiParam {Date}       startDate              开始日期(yyyy-mm-dd)
	 * @apiParam {Date}       endDate                结束日期(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]            场站Id
	 * @apiParam {String}     [stationName]          场站名称 模糊查询
	 * @apiParam {int}        [orgId]                运营商Id
	 * @apiParam {String}     [orgName]              运营商名称
	 * <br/>
	 * @apiSuccess {String}       data.list.time                  日期   
	 * @apiSuccess {String}       data.list.orgName 			 运营商名
	 * @apiSuccess {String}       data.list.stationName          场站名
	 * @apiSuccess {Double}       data.list.amount               总收入(元)
	 * @apiSuccess {Double}       data.list.elceFee              电费总收入(元)
	 * @apiSuccess {Double}       data.list.noElceFee            非费率总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg1                       尖价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg2                       峰价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg3                       平价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg4                       谷价总收入(元)
	 * @apiSuccess {Double}       data.list.servFee              服务费收入(元/kW·h)
	 * @apiSuccess {Double}       data.list.parkFee              停车费收入(元)
	 * @apiSuccess {Double}       data.list.chgPower             充电总量(kW·h)
	 * @apiSuccess {Double}       data.list.noChgPower           非费率电量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg1 			  尖价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg2                         峰价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg3                         平价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg4                         谷价总量(kW·h)
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_export",method = RequestMethod.GET)
	public void exportStationIncome(@RequestParam Map map,HttpServletResponse response) throws Exception{
		stationIncomeService.exportStationIncome(map,response);
	}
	
	/**
	 * 
	 * @api {get} /api/income/station/info    场站收入详情 显示场站每日数据汇总
	 * @apiName getStationByDayIncome
	 * @apiGroup StationIncomeController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 场站收入详情 显示场站每日数据汇总
	 * <br/>
	 * @apiParam {Int}        userId                 用户Id
	 * @apiParam {Date}       startDate              开始日期(yyyy-mm-dd)
	 * @apiParam {Date}       endDate                结束日期(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]            场站Id
	 * @apiParam {int}        [orgId]                运营商Id
	 * @apiParam {int}        pageNum                页码
	 * @apiParam {int}        pageSize               页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {String}       data.list.time                  日期   
	 * @apiSuccess {String}       data.list.orgName 			 运营商名
	 * @apiSuccess {String}       data.list.stationName          场站名
	 * @apiSuccess {Double}       data.list.amount               总收入(元)
	 * @apiSuccess {Double}       data.list.elceFee              电费总收入(元)
	 * @apiSuccess {Double}       data.list.noElceFee            非费率总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg1                       尖价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg2                       峰价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg3                       平价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg4                       谷价总收入(元)
	 * @apiSuccess {Double}       data.list.servFee              服务费收入(元/kW·h)
	 * @apiSuccess {Double}       data.list.parkFee              停车费收入(元)
	 * @apiSuccess {Double}       data.list.chgPower             充电总量(kW·h)
	 * @apiSuccess {Double}       data.list.noChgPower           非费率电量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg1 			  尖价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg2                         峰价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg3                         平价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg4                         谷价总量(kW·h)
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="/info",method = RequestMethod.GET)
	public ResultVo getStationByDayIncome(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo pageInfo = stationIncomeService.getStationByDayIncome(map);
		resVo.setData(pageInfo);
		return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {get} /api/income/station/info/_export    场站收入详情 显示场站每日数据汇总导出
	 * @apiName exportStationByDayIncome
	 * @apiGroup StationIncomeController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 场站收入详情 显示场站每日数据汇总导出
	 * <br/>
	 * @apiParam {Int}        userId                 用户Id
	 * @apiParam {Date}       startDate              开始日期(yyyy-mm-dd)
	 * @apiParam {Date}       endDate                结束日期(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]            场站Id
	 * @apiParam {int}        [orgId]                运营商Id
	 * <br/>
	 * @apiSuccess {String}       data.list.time                  日期   
	 * @apiSuccess {String}       data.list.orgName 			 运营商名
	 * @apiSuccess {String}       data.list.stationName          场站名
	 * @apiSuccess {Double}       data.list.amount               总收入(元)
	 * @apiSuccess {Double}       data.list.elceFee              电费总收入(元)
	 * @apiSuccess {Double}       data.list.noElceFee            非费率总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg1                       尖价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg2                       峰价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg3                       平价总收入(元)
	 * @apiSuccess {Double}       data.list.incomeZxyg4                       谷价总收入(元)
	 * @apiSuccess {Double}       data.list.servFee              服务费收入(元/kW·h)
	 * @apiSuccess {Double}       data.list.parkFee              停车费收入(元)
	 * @apiSuccess {Double}       data.list.chgPower             充电总量(kW·h)
	 * @apiSuccess {Double}       data.list.noChgPower           非费率电量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg1 			  尖价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg2                         峰价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg3                         平价总量(kW·h)
	 * @apiSuccess {Double}       data.list.powerZxyg4                         谷价总量(kW·h)
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001 {0}不能为空!
	 */
	@RequestMapping(value = "/info/_export",method = RequestMethod.GET)
	public void exportStationByDayIncome(@RequestParam Map map,HttpServletResponse response) throws Exception{
		stationIncomeService.exportStationByDayIncome(map, response);
	}
	
	
	/**
	 * 
	 * @api {get} /api/income/station/billPay/_export    场站收入  订单详情导出
	 * @apiName exportBillPayByStation
	 * @apiGroup StationIncomeController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 场站收入  订单详情导出
	 * <br/>
	 * @apiParam {Int}        userId                 用户Id
	 * @apiParam {Date}       startDate              开始日期(yyyy-mm-dd)
	 * @apiParam {Date}       endDate                结束日期(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]            场站Id
	 * @apiParam {int}        [orgId]                运营商Id
	 * <br/>
	 * @apiSuccess {String} billPayNo		订单号
	 * @apiSuccess {String} stationName 	场站名称
	 * @apiSuccess {String} pileName		设备名称
	 * @apiSuccess {String} createTime		订单时间
	 * @apiSuccess {String} startTime		充电开始时间
	 * @apiSuccess {String} endTime			充电结束时间
	 * @apiSuccess {String} hyCardOrPhone	会员卡号/手机号
	 * @apiSuccess {String} consName		会员名称
	 * @apiSuccess {String} groupName		集团名称
	 * @apiSuccess {String} licensePlate	车牌号
	 * @apiSuccess {String} line			线路
	 * @apiSuccess {String} socBeg			初始SOC
	 * @apiSuccess {String} socEnd			结束SOC
	 * @apiSuccess {String} chgPower		充电量(kW·h)
	 * @apiSuccess {String} powerZxyg1		尖价电量(kW·h)
	 * @apiSuccess {String} powerZxyg1Time	尖价充电时长(分钟)
	 * @apiSuccess {String} powerZxyg2		峰价电量(kW·h)
	 * @apiSuccess {String} powerZxyg2Time	峰价充电时长(分钟)
	 * @apiSuccess {String} powerZxyg3		平价电量(kW·h)
	 * @apiSuccess {String} powerZxyg3Time	平价充电时长(分钟)
	 * @apiSuccess {String} powerZxyg4		谷价电量(kW·h)
	 * @apiSuccess {String} powerZxyg4Time	谷价充电时长(分钟)
	 * @apiSuccess {String} amount			消费金额(元)
	 * @apiSuccess {String} prcZxygz1		尖价电费(元)
	 * @apiSuccess {String} prcZxygz2		峰价电费(元)
	 * @apiSuccess {String} prcZxygz3		平价电费(元)
	 * @apiSuccess {String} prcZxygz4		谷价电费(元)
	 * @apiSuccess {String} servFee			服务费(元)
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value = "billPay/_export",method = RequestMethod.GET)
	public void exportBillPayByStation(@RequestParam Map map,HttpServletResponse response) throws Exception{
		stationIncomeService.exportBillPayByStation(map,response);
	}
}

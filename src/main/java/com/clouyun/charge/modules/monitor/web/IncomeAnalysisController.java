package com.clouyun.charge.modules.monitor.web;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.clouyun.boot.common.domain.DataVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.modules.spring.web.BaseController;
import com.clouyun.charge.modules.monitor.service.IncomeAnalysisService;


/**
 * 描述: 运营总览收入分析接口类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: fft
 * 版本: 2.0.0
 * 创建日期: 2017年6月21日
 */
@RestController
@RequestMapping("/api/incomes")
public class IncomeAnalysisController extends BaseController{
	
	@Autowired
	IncomeAnalysisService incomeAnalysisService;
	
	
	/**
	 * 
	 * @api {get} /api/incomes/{userId}   查询多企业，单企业收入
	 * @apiName getIncomeInfo
	 * @apiGroup IncomeAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾   查询多企业，单企业收入
	 * <br/>
	 * @apiParam   {String}    userId               用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Integer}   data.dAmount 		昨日收入
	 * @apiSuccess {Integer}   data.dChgPower 		昨日充电量
	 * @apiSuccess {Integer}   data.dCount 			昨日服务次数
	 * @apiSuccess {Integer}   data.dReCharge 		昨日充值金额
	 * @apiSuccess {Integer}   data.dAmountFig 		昨日收入增减标识 1：增加 0：持平 -1：下降
	 * @apiSuccess {Integer}   data.dChgPowerFig 	昨日充电量增减标识 同上
	 * @apiSuccess {Integer}   data.dCountFig 	 	昨日服务增减标识 同上
	 * @apiSuccess {Integer}   data.dReChargeFig 	昨日充值增减标识 同上
	 * @apiSuccess {Integer}   data.mAmount 		当月收入
	 * @apiSuccess {Integer}   data.mChgPower 		当月充电量
	 * @apiSuccess {Integer}   data.mCount 			当月服务次数
	 * @apiSuccess {Integer}   data.mReCharge 		当月充值金额
	 * @apiSuccess {Integer}   data.mAmountFig 		当月收入增减标识 1：增加 0：持平 -1：下降
	 * @apiSuccess {Integer}   data.mChgPowerFig 	当月充电量增减标识 同上
	 * @apiSuccess {Integer}   data.mCountFig 		当月服务次数增减标识 同上
	 * @apiSuccess {Integer}   data.mReChargeFig 	当月充值增减标识 同上
	 * @apiSuccess {Integer}   data.yAmount 		当年收入
	 * @apiSuccess {Integer}   data.yChgPower 		当年充电量
	 * @apiSuccess {Integer}   data.yCount 			当年服务次数
	 * @apiSuccess {Integer}   data.yReCharge 		当年充值金额
	 * @apiSuccess {Integer}   data.yAmountFig 		当年收入增减标识 1：增加 0：持平 -1：下降
	 * @apiSuccess {Integer}   data.yChgPowerFig 	当年充电量增减标识 同上
	 * @apiSuccess {Integer}   data.yCountFig 		当年服务次数增减标识 同上
	 * @apiSuccess {Integer}   data.yReChargeFig 	当年充值增减标识 同上
	 * <br/>
	 *@apiSuccessExample {json} Success出参示例:{
	 *{
        errorCode: 0,
        errorMsg: "操作成功!",
        total: 0,
        data: {
        dCount: 8,
        yAmountFig: 0,
        dReCharge: 0,
        dAmountFig: 0,
        mReChargeFig: -1,
        mChgPowerFig: -1,
        mAmountFig: -1,
        dChgPowerFig: -1,
        yChgPowerFig: 0,
        dAmount: 240,
        yReCharge: 99819,
        mAmount: 1250969,
        yCount: 481523,
        yChgPower: 12535122,
        yCountFig: 0,
        dCountFig: -1,
        mCount: 31341,
        yAmount: 14889226,
        mReCharge: 11200,
        dReChargeFig: 0,
        mCountFig: -1,
        dChgPower: 160,
        mChgPower: 1063047,
        yReChargeFig: 1
     },
     exception: null
    }
	 *  
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/{userId}",method=RequestMethod.GET)
	public ResultVo getIncomeInfo(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map = incomeAnalysisService.queryIncomeInfo(userId);
		resVo.setData(map);
		return resVo;
	}
	
	
	/**
	 * 
	 * @throws ParseException 
	 * @api {get} /api/incomes/paytype/{userId}   查询单企业支付方式
	 * @apiName queryIncomePayType
	 * @apiGroup IncomeAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾   查询单企业支付方式
	 * <br/>
	 * @apiParam   {String}    userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Integer}   data.xj 				现金比例
	 * @apiSuccess {Integer}   data.wx 				微信支付比例
	 * @apiSuccess {String}    data.czk 			充值卡支付比例
	 * @apiSuccess {Integer}   data.zfb 			支付宝支付比例
	 * @apiSuccess {String}    data.yj 				月结支付比例
	 * <br/>
	 *@apiSuccessExample {json} Success出参示例:{
	 *{
		  "errorCode": 0,
		  "errorMsg": "操作成功!",
		  "total": 0,
		  "data": {
		    "xj": 0.01786090729514565,
		    "wx": 0.014614699599331465,
		    "czk": 0.6676748839654914,
		    "zfb": 0.00465192355840739,
		    "yj": 0.29519758558162423
		  },
		  "exception": null
		}
	 *  
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/paytype/{userId}",method=RequestMethod.GET)
	public ResultVo queryIncomePayType(@PathVariable("userId") Integer userId) throws BizException, ParseException {
		ResultVo resVo = new ResultVo();
		Map map = incomeAnalysisService.queryIncomePayType(userId);
		resVo.setData(map);
		return resVo;
	}
	/**
	 *
	 * @api {get} /api/incomes/station/charge  场站充电排行
	 * @apiName stationCharge
	 * @apiGroup IncomeAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   场站充电排行
	 * <br/>
	 * @apiParam   {Integer}     userId              用户Id
	 * @apiParam   {String}       type               1 日，2 月,3 年
	 * @apiParam   {String}       [month]            默认为上月(yyyy-mm-dd)
	 * @apiParam   {Integer}      [size]              默认为20
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {String}    data.chgPower    			电量
	 * @apiSuccess {String}    data.compare    			比较值（1.大于，0 等于 ,-1 小于）
	 * @apiSuccess {String}    data.percentage    			百分比
	 * @apiSuccess {String}    data.stationName    	    场站名称
	 * @apiError -999  系统异常
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/station/charge",method=RequestMethod.GET)
	public ResultVo stationCharge(@RequestParam Map map) throws Exception {
		ResultVo resVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")&&dataMap.isNotBlank("type")){
			List<DataVo> list = incomeAnalysisService.stationCharge(dataMap);
			resVo.setData(list);
			return resVo;
		}else {
			throw   new BizException(1700000,"请填入用户id和type");
		}
	}

	/**
	 * @api {get} /api/incomes/station/income  场站收入排行
	 * @apiName stationIncome
	 * @apiGroup IncomeAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   场站收入排行
	 * <br/>
	 * @apiParam   {Integer}    userId              用户Id
	 * @apiParam   {String}      type               1 日，2 月,3 年
	 * @apiParam   {String}       [month]           默认为上月(yyyy-mm-dd)
	 * @apiParam   {Integer}      size              默认为20
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {String}    data.amount    			金额
	 * @apiSuccess {String}    data.compare    			比较值（1.大于，0 等于 ,-1 小于）
	 * @apiSuccess {String}    data.percentage    			百分比
	 * @apiSuccess {String}    data.stationName    	    场站名称
	 * @apiError -999  系统异常
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/station/income",method=RequestMethod.GET)
	public ResultVo stationIncome(@RequestParam Map map) throws Exception {
		ResultVo resVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")&&dataMap.isNotBlank("type")){
			List<DataVo> list = incomeAnalysisService.stationIncome(dataMap);
			resVo.setData(list);
			return resVo;
		}else {
			throw   new BizException(1700000,"userId或者type");
		}
	}

	/**
	 * @api {get} /api/incomes/station/org  运营商排行
	 * @apiName orgIncome
	 * @apiGroup IncomeAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   运营商排行
	 * <br/>
	 * @apiParam   {Integer}    userId              用户Id
	 * @apiParam   {String}      type               1 日，2 月,3 年
	 * @apiParam   {String}       [month]            默认为上月(yyyy-mm-dd)
	 * @apiParam   {Integer}      size              默认为20
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {String}    data.orgAmount    			金额
	 * @apiSuccess {String}    data.compare    			比较值（1.大于，0 等于 ,-1 小于）
	 * @apiSuccess {String}    data.percentage    			百分比
	 * @apiSuccess {String}    data.orgName    	      运营商名称
	 * @apiError -999  系统异常
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/station/org",method=RequestMethod.GET)
	public ResultVo orgIncome(@RequestParam Map map) throws Exception {
		ResultVo resVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")&&dataMap.isNotBlank("type")){
			List<DataVo> list = incomeAnalysisService.orgIncome(dataMap);
			resVo.setData(list);
			return resVo;
		}else {
			throw   new BizException(1700000,"userId或者type");
		}
	}
	
	/**
	 * 
	 * @api {get} /api/incomes/curve       收入曲线
	 * @apiName  queryIncomeCurve
	 * @apiGroup IncomeAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉                收入曲线
	 * <br/>
	 * @apiParam   {Integer}    userId              用户Id
	 * @apiParam   {String}     type                类型(1--按时，2-按日，3--按月)
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Object[]}  data.list            数据对象集合
	 * @apiSuccess {String}    data.list.time       时间
	 * @apiSuccess {Double}    data.list.incomeSum 	收入
	 * @apiSuccess {Double}    data.list.powerSum 	电量
     * <br/>
	 * @apiError -999  系统异常
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/curve",method=RequestMethod.GET)
	public ResultVo queryIncomeCurve(@RequestParam Map map) throws BizException {
		ResultVo resVo = new ResultVo();
		resVo.setData(incomeAnalysisService.queryIncomeCurve(map));
		return resVo;
	}
	
	
	/**
	 * 
	 * @api {get} /api/incomes/dist/{userId}      收入分布
	 * @apiName  queryIncomeDist
	 * @apiGroup IncomeAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉                收入分布
	 * <br/>
	 * @apiParam   {Integer}    userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Object[]}  data.list            数据对象集合
	 * @apiSuccess {Double}    data.list.single     个人会员
	 * @apiSuccess {Double}    data.list.contract 	合约
	 * @apiSuccess {Double}    data.list.group 	           集团月结
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/dist/{userId}",method=RequestMethod.GET)
	public ResultVo queryIncomeDist(@PathVariable Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		resVo.setData(incomeAnalysisService.queryIncomeDist(userId));
		return resVo;
	}
}

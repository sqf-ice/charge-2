package com.clouyun.charge.modules.monitor.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.modules.spring.web.BaseController;
import com.clouyun.charge.modules.document.service.StationService;
import com.clouyun.charge.modules.monitor.service.OperationService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: MaterialController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: fuft
 * 版本: 2.0.0
 * 创建日期: 2017年4月20日
 */
@RestController
@RequestMapping("/api/operation")
public class OperationController extends BaseController {
	
	@Autowired
	OperationService operationService;
	
	/**
	 * 
	 * @api {get} /api/operation/stationsInfo/{userId}   查询场站建设信息
	 * @apiName getStationCount
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾    运营总览的场站建设信息块
	 * <br/>
	 * @apiParam {String}      userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Integer}   data.stationCount 	场站总数
	 * @apiSuccess {Integer}   data.pileCount 		充电桩总数
	 * @apiSuccess {Integer}   data.newStation 		场站新增
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 		{
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "pileCount": 1233,
			    "newStation": 0,
			    "stationCount": 380
			  }
			}
	 * }
	 *  
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/stationsInfo/{userId}",method=RequestMethod.GET)
	public ResultVo getStationCount(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map = operationService.queryStation(userId);
		resVo.setData(map);
		return resVo;
	}
	

	/**
	 * 
	 * @api {get} /api/operation/ccons/{userId}   查询用户信息
	 * @apiName queryCcons
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾   运营总览的用户情况模块
	 * <br/>
	 * @apiParam   {String}    userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Integer}   data.allCons 		用户总数
	 * @apiSuccess {Integer}   data.newCons 		新增用户
	 * @apiSuccess {String}    data.chgPro 			转化率
	 * <br/>
	 *@apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "allCons": 1073,
			    "chgCons": 176,
			    "newCons": 5,
			    "chgPro": "16.40%"
			  }
			}
	 * }
	 *  
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/ccons/{userId}",method=RequestMethod.GET)
	public ResultVo queryCcons(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map = operationService.queryCcons(userId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/income/{userId}   查询收入情况
	 * @apiName queryIncomeInfo
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾    运营总览的收入情况模块
	 * <br/>
	 * @apiParam {String}      userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Double}    data.countAmount 	累计收入
	 * @apiSuccess {Object[]}  data.top3 		            收入排名前三的省份
	 * @apiSuccess {String}    data.top3.areaName 	前三地区名称
	 * @apiSuccess {String}    data.top3.sum 	            前三地区 收入  
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "tops": [
			      {
			        "areaName": "广东省",
			        "sum": 60897.03
			      },
			      {
			        "areaName": "辽宁省",
			        "sum": 17.54
			      },
			      {
			        "areaName": "河北省",
			        "sum": 263.49
			      }
			    ],
			    "countAmount": 125632.3
			  }
			}
	 * }
	 *  
     * <br/>       
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/income/{userId}",method=RequestMethod.GET)
	public ResultVo queryIncomeInfo(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map = operationService.queryIncomeInfo(userId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/service/{userId}   查询充电服务和累计统计
	 * @apiName queryInitData
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾   运营总览的充电服务模块和累计三项块
	 * <br/>
	 * @apiParam {String}      userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Int}       data.currCount 		本月服务次数
	 * @apiSuccess {String}    data.currPower 		本月累计充电量
	 * @apiSuccess {String}    data.currAmount 	 	累计收入
	 * @apiSuccess {Int}       data.svCount 		总服务次数
	 * @apiSuccess {Int}       data.carCount 		服务车辆
	 * <br/>
	 *@apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "carCount": 192,
			    "currAmount": "46.4",
			    "svCount": 4005,
			    "currCount": 4,
			    "currPower": "23.2"
			  }
			}
	 * }
	 *  
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/service/{userId}",method=RequestMethod.GET)
	public ResultVo queryInitData(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map = operationService.queryInitData(userId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/rade/{userId}   查询总的增长率和各省前三增长率
	 * @apiName queryRade
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览月收入增长率模块
     * <br/>
     * @apiParam {String}      userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {String}    data.sumScale 		上个月总的增长率
	 * @apiSuccess {Object[]}  data.top3 		           上个月增长率排名前三的省份
	 * @apiSuccess {String}    data.top3.prov 	 	排名前三的省份名称
	 * @apiSuccess {String}    data.top3.scale 		排名前三的省份增长率
	 * @apiSuccess {Double}    data.top3.incAdd 	排名前三的省份增长金额
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "sumScale": "-98.40%",
			    "top3": [
			      {
			        "scale": "-4724.87%",
			        "prov": "广东省",
			        "incAdd": "-511514.00"
			      }
			    ]
			  }
			}
	 * } 
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/rade/{userId}",method=RequestMethod.GET)
	public ResultVo queryRade(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map = operationService.queryRade(userId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/useRade/station/{stationId}   查询场站利用率
	 * @apiName queryUseRade
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  场站利用率
	 * <br/>
     * @apiParam {String}      stationId           场站Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {String}    data.totalRate		场站总利用率
	 * @apiSuccess {String}    data.zRate 	 		场站直流桩利用率
	 * @apiSuccess {String}    data.jRate 			场站交流桩利用率
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "zRate": 0,
			    "totalRate": 0,
			    "jRate": 0
			  }
			}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/useRade/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryUseRade(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map1 = operationService.queryUseRade(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/warms/station/{stationId}   查询场站当月告警数量和修复数
	 * @apiName queryWarm
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  场站当月告警情况
	 * <br/>
     * @apiParam {String}      stationId           场站Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {String}    data.warnCount		场站当月告警数量
	 * @apiSuccess {String}    data.repairCount 	 场站当月修复数量
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "warnCount": 14,
			    "repairCount": 0
			  }
			}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/warms/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryWarm(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map1 = operationService.queryWarm(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/loss/station/{stationId}   查询场站损耗和用电量
	 * @apiName queryLoss
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  查询累计充电量/损耗
	 * <br/>
     * @apiParam {String}      stationId           场站Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Double}    data.totalLoss		累计损耗
	 * @apiSuccess {Double}    data.totalElec 		累计充电
	 * @apiSuccess {String}    data.tlRate			累计损耗率
	 * @apiSuccess {String}    data.ylRate 		  	昨日损耗率
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "ylRate": "0",
			    "totalElec": 0,
			    "totalLoss": 0,
			    "tlRate": "0"
			  }
			}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/loss/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryLoss(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map1 = operationService.queryLoss(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/pileRade/station/{stationId}   查询场站充电桩使用率前五的桩
	 * @apiName queryPileRade
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  当月充电桩使用率排行
	 * <br/>
     * @apiParam {String}      stationId           场站Id
     * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object[]}  data        			分页数据封装
	 * @apiSuccess {String}    data.pileName 	 	排名前五的充电桩名称
	 * @apiSuccess {String}    data.PileRate 		排名前五的充电桩增长率
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": [
			    {
			      "pileName": "1号桩",
			      "PileRate": "0.00%"
			    },
			    {
			      "pileName": "9号桩",
			      "PileRate": "0.00%"
			    }
			  ]
			}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/pileRade/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryPileRade(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		List<Map> map1 = operationService.queryPileUseRate(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/pileLossRade/station/{stationId}   查询场站充电桩损耗率前五的桩
	 * @apiName queryPileLossRade
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  当月充电桩损耗率排行
	 * <br/>
     * @apiParam {String}      stationId           场站Id
     * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object[]}  data        			分页数据封装
	 * @apiSuccess {String}    data.pileName 	 	损耗前五的充电桩名称
	 * @apiSuccess {String}    data.PileLossRate 	损耗前五的充电桩损耗率
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": [
			    {
			      "pileName": "1号桩",
			      "pileLossRate": "0.00%"
			    },
			    {
			      "pileName": "9号桩",
			      "pileLossRate": "0.00%"
			    }
			  ]
			}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/pileLossRade/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryPileLossRade(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		List<Map> map1 = operationService.queryPileLoss(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/pileIncome/station/{stationId}   查询场站充电桩月收入前五的桩
	 * @apiName queryPileIncome
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  当月充电桩收入排行
	 * <br/>
     * @apiParam {String}      stationId           场站Id
     * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object[]}  data        			分页数据封装
	 * @apiSuccess {String}    data.pileName 	 	收入前五的充电桩名称
	 * @apiSuccess {String}    data.power 	     	收入前五的充电桩总电量
	 * @apiSuccess {String}    data.income 	     	收入前五的充电桩总收入
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	  {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": []
			}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/pileIncome/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryPileIncome(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		List<Map> map1 = operationService.queryPileIncome(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/totalInfo/station/{stationId}   查询场站当月累计信息
	 * @apiName queryTotalInfo
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  当月场站的累计信息
	 * <br/>
     * @apiParam {String}      stationId           场站Id
     * <br/>
	 * @apiSuccess {String}    errorCode         	 错误码
	 * @apiSuccess {String}    errorMsg    			 消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Integer}   data.serverCount 	本月服务次数
	 * @apiSuccess {Double}    data.dicp 	     	本月累计充电
	 * @apiSuccess {Double}    data.diti 	     	本月累计收入
	 * @apiSuccess {Double}    data.ditsf 	     	总服务费
	 * @apiSuccess {Double}    data.ditcsf 	     	总分成服务费
	 * @apiSuccess {Double}    data.fcsr 	     	总分成收入
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "diti": 0,
			    "ditcsf": 0,
			    "serverCount": 0,
			    "ditsf": 0,
			    "dicp": 0,
			    "fcsr": 0
			  }
			}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/totalInfo/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryTotalInfo(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map map1 = operationService.queryTotalInfo(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/monitorInfo/{userId}   查询运营商的月(日)监控
	 * @apiName queryMonitorInfo
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  运营商的月(日)监控
     * <br/>
     * @apiParam {String}      userId              用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	 错误码
	 * @apiSuccess {String}    errorMsg    			 消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Object[]}  data.date 	 		包括当天在内的前12天每天日期 格式：yyyy-MM-dd
	 * @apiSuccess {Double}    data.date.power 	            包括当天在内的前12天运营商每天用电量
	 * @apiSuccess {Double}    data.date.amount 	 包括当天在内的前12天运营商每天收入
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "2017-04-23": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-04-22": {
			      "amount": 225.31,
			      "power": 225.31
			    },
			    "2017-04-25": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-04-24": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-04-27": {
			      "amount": 0.86,
			      "power": 0.36
			    },
			    "2017-04-26": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-04-29": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-04-28": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-05-01": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-05-02": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-04-30": {
			      "amount": 0,
			      "power": 0
			    },
			    "2017-05-03": {
			      "amount": 0,
			      "power": 0
			    }
			  }
			}
	 * }
	 *  
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/monitorInfo/{userId}",method=RequestMethod.GET)
	public ResultVo queryMonitorInfo(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map<String,Map<String,Object>> map1 = operationService.queryMonitorInfo(userId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/timeRade/{userId}   查询用户所有场站充电利用率
	 * @apiName queryTimeUseRade
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  查询用户所有场站充电利用率
	 * <br/>
     * @apiParam {String}      userId           	用户Id
	 * <br/>
	 * @apiSuccess {String}    errorCode         	错误码
	 * @apiSuccess {String}    errorMsg    			消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {String}    data.rate			场站总利用率 	保留了四位小数
	 * @apiSuccess {String}    data.allUse 	 		公共场站利用率    	保留了四位小数
	 * @apiSuccess {String}    data.busUse 			公交专用场站利用率  保留了四位小数
	 * @apiSuccess {String}    data.wlUse 			物流专用场站利用率   保留了四位小数
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * 	   {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "wlUse": 0,
			    "allUse": 0,
			    "rate": 0,
			    "busUse": 0
			  }
		}
	 * }
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/timeRade/{userId}",method=RequestMethod.GET)
	public ResultVo queryTimeUseRade(@PathVariable("userId") Integer userId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map<String,Object> map1 = operationService.queryTimeUseRade(userId);
		resVo.setData(map1);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/operation/monitorInfo/station/{stationId}   查询场站的月(日)监控
	 * @apiName queryMonitorInfoByStationId
	 * @apiGroup OperationController
	 * @apiVersion 2.0.0
	 * @apiDescription 付飞腾         运营总览  场站的月(日)监控
	 * <br/>
     * @apiParam {String}      stationId           场站Id
     * <br/>
	 * @apiSuccess {String}    errorCode         	 错误码
	 * @apiSuccess {String}    errorMsg    			 消息说明
	 * @apiSuccess {Object}    data        			分页数据封装
	 * @apiSuccess {Object[]}  data.date 	 		包括当天在内的前12天每天日期  格式：yyyy-MM-dd
	 * @apiSuccess {Double}    data.date.power 	            包括当天在内的前12天场站每天用电量
	 * @apiSuccess {Double}    data.date.income 	 包括当天在内的前12天场站每天收入
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 * {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "2017-04-23": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-04-25": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-04-24": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-04-27": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-04-26": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-04-29": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-04-28": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-05-01": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-05-02": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-04-30": {
			      "income": 0,
			      "power": 0
			    },
			    "2017-05-03": {
			      "income": 0,
			      "power": 0
			    }
			  }
			}
	 * }
	 *  
     * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value = "/monitorInfo/station/{stationId}",method=RequestMethod.GET)
	public ResultVo queryMonitorInfoByStationId(@PathVariable("stationId") Integer stationId) throws BizException {
		ResultVo resVo = new ResultVo();
		Map<String,Map<String,Object>> map1 = operationService.queryMonitorInfoByStationId(stationId);
		resVo.setData(map1);
		return resVo;
	}
	
}

package com.clouyun.charge.modules.monitor.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.monitor.service.AlertService;
import com.clouyun.charge.modules.monitor.service.BusinessAnalysisService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * BusinessAnalysisController
 * 描述: 经营分析控制类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yi.kai
 * 版本: 1.0
 * 创建日期: 2017年6月21日
 */
@RestController
@RequestMapping("/api/jyfx")
public class BusinessAnalysisController extends BusinessController {
	public static final Logger logger = LoggerFactory.getLogger(BusinessAnalysisController.class);
	@Autowired
	BusinessAnalysisService businessAnalysisService;

	/**
	 * @api {get} /api/jyfx/power   查询充电量完成情况
	 * @apiName power
	 * @apiGroup BusinessAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    查询充电量
	 * @apiParam {Integer}             userId                            用户Id
	 * @apiParam {Date}           [month]                             月份(默认为上月份 yyyy-mm)
	 * @apiParam {Integer}           [pageNum]                            页码(默认为当1)
	 * @apiParam {Integer}           [pageSize]                           页大小(默认为50)
	 * <br/>
	 * @apiSuccess {String}       errorCode                         错误码
	 * @apiSuccess {String}       errorMsg                          消息说明
	 * @apiSuccess {Object}         data                    		分页数据封装
	 * @apiSuccess {Integer}        data.total                    	总记录数
	 * @apiSuccess {Object[]}       data.list                    	分页数据对象数组
	 * @apiSuccess {Double}        data.list.target               目标充电量
	 * @apiSuccess {Double}       data.list.total                    	充电量
	 * @apiSuccess {Integer}       data.list.stationId                    	场站Id
	 * @apiSuccess {String}       data.list.stationName                    	场站名称
	 * @apiSuccess {String}       data.list.stationNameMint                    	场站缩写
	 * @apiSuccess {Double}       data.list.overPower                    	超出电量
	 * @apiSuccess {Double}       data.list.unFinishPower                      未完成电量
	 * @apiSuccess {Integer}       data.list.compare                      比较值(大于目标电量为1，小于-1，等于0)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/power", method = RequestMethod.GET)
	public ResultVo power(@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")){
			PageInfo pageList =businessAnalysisService.power(dataMap);
			resultVo.setData(pageList);
			return resultVo;
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}
	/**
	 * @api {get} /api/jyfx/income   查询充电收入完成情况
	 * @apiName income
	 * @apiGroup BusinessAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    查询充电收入
	 * @apiParam {Integer}              userId                              用户Id
	 * @apiParam {Date}               [month]                              月份(默认为上月份 yyyy-mm)
	 * @apiParam {Integer}            [pageNum]                            页码(默认为当1)
	 * @apiParam {Integer}            [pageSize]                           页大小(默认为50)
	 * <br/>
	 * @apiSuccess {String}       errorCode                         错误码
	 * @apiSuccess {String}       errorMsg                          消息说明
	 * @apiSuccess {Object}         data                    		分页数据封装
	 * @apiSuccess {Integer}        data.total                    	总记录数
	 * @apiSuccess {Object[]}       data.list                    	分页数据对象数组
	 * @apiSuccess {Double}        data.list.target               目标收入
	 * @apiSuccess {Double}       data.list.total                    	收入
	 * @apiSuccess {Integer}       data.list.stationId                    	场站Id
	 * @apiSuccess {String}       data.list.stationName                    	场站名称
	 * @apiSuccess {String}       data.list.stationNameMint                    	场站缩写
	 * @apiSuccess {Double}       data.list.overIncome                    	超出收入
	 * @apiSuccess {Double}       data.list.unFinishIncome                      未完成收入
	 * @apiSuccess {Integer}       data.list.compare                      比较值(大于目标收入为1，小于-1，等于0)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/income", method = RequestMethod.GET)
	public ResultVo income(@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")){
			PageInfo pageList =businessAnalysisService.income(dataMap);
			resultVo.setData(pageList);
			return resultVo;
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}

	/**
	 * @api {get} /api/jyfx/profit   查询场站利润排行
	 * @apiName profit
	 * @apiGroup BusinessAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    查询场站利润排行
	 * @apiParam {Integer}             userId                             用户Id
	 * @apiParam {Date}           [month]                             月份(yyyy-mm) （传入后根据月份日期查询，年月，不传入默认查询上月）
	 * @apiParam {Integer}           [size]                              个数(默认为10)
	 * <br/>
	 * @apiSuccess {String}       errorCode                              错误码
	 * @apiSuccess {String}       errorMsg                              消息说明
	 * @apiSuccess {Object}         data                    		     分页数据封装
	 * @apiSuccess {String}       data.stationName                    	 场站名称
	 * @apiSuccess {Double}       data.amount                       	 收入
	 * @apiSuccess {Integer}       data.compare                    	 比较值(大于上月利润为1，小于-1，等于0)
	 * @apiSuccess {String}       data.percentage                       百分比
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/profit", method = RequestMethod.GET)
	public ResultVo profit(@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")){
			List<DataVo> pageList =businessAnalysisService.profit(dataMap);
			resultVo.setData(pageList);
			return resultVo;
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}

	/**
	 * @api {get} /api/jyfx/fault   查询场站故障
	 * @apiName fault
	 * @apiGroup BusinessAnalysisController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    查询场站故障
	 * @apiParam {Integer}             userId                            用户Id
	 * @apiParam {Date}           [month]                             月份(默认为上月份 yyyy-mm)
	 * @apiParam {Integer}           [pageNum]                            页码(默认为当1)
	 * @apiParam {Integer}           [pageSize]                           页大小(默认为50)
	 * <br/>
	 * @apiSuccess {String}       errorCode                         错误码
	 * @apiSuccess {String}       errorMsg                          消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {String}       data.list.type1                    	硬件故障百分百
	 * @apiSuccess {String}       data.list.type2                    	通讯异常百分百
	 * @apiSuccess {String}       data.list.type3                    	支付异常百分百
	 * @apiSuccess {String}       data.list.type4                    	系统BUG数百分百
	 * @apiSuccess {String}       data.list.type5                    	启动充电异常百分百
	 * @apiSuccess {String}       data.list.type6                    	异常订单数百分百
	 * @apiSuccess {Object[]}       data.list.stations                    	场站集合
	 * @apiSuccess {String}       data.list.stations.stationName                    场站名称
	 * @apiSuccess {String}       data.list.stations.stationNameMint                    缩写
	 * @apiSuccess {String}       data.list.stations.faultSize                    	故障数
	 * @apiSuccess {String}       data.list.stations.repairSize                    	修复数
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/fault", method = RequestMethod.GET)
	public ResultVo fault(@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")){
			PageInfo vo =businessAnalysisService.fault(dataMap);
			resultVo.setData(vo);
			return resultVo;
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}

/**
 * @api {get} /api/jyfx/efficiency   查询场站效率
 * @apiName efficiency
 * @apiGroup BusinessAnalysisController
 * @apiVersion 2.0.0
 * @apiDescription 易凯    查询场站效率
 * @apiParam {Integer}             userId                            用户Id
 * @apiParam {Date}               [month]                             月份(默认为上月份 yyyy-mm)
 * <br/>
 * @apiSuccess {String}       errorCode                         错误码
 * @apiSuccess {String}       errorMsg                          消息说明
 * @apiSuccess {Object}         data                    		     数据封装
 * @apiSuccess {Object[]}       data.charTimeList                    	 数据封装 利用率
 * @apiSuccess {Integer}         data.charTimeList.dayChargeTime                          日充电时间
 * @apiSuccess {Integer}         data.charTimeList.weekChangerTime                         周充电时间
 * @apiSuccess {Integer}         data.charTimeList.chargeTime                           月充电时间
 * @apiSuccess {Integer}         data.charTimeList.chargeTimeCompare                           日充电时间比较  (大于上月利率为1，小于-1，等于0)
 * @apiSuccess {Integer}         data.charTimeList.weekChargeTimeCompare                       周充电时间比较  (大于上月利率为1，小于-1，等于0)
 * @apiSuccess {Integer}         data.charTimeList.chargeTimeCompare                          月充电时间比较   (大于上月利率为1，小于-1，等于0)
 * @apiSuccess {String}         data.charTimeList.percentage                               利用率百分比
 * @apiSuccess {Object[]}       data.conversionList                      数据封装  转换率
 * @apiSuccess {Double}        data.conversionList.diepDay                  日用电量
 * @apiSuccess {Double}        data.conversionList.dicpDay                   日充电量
 * @apiSuccess {Double}        data.conversionList.diepWeek                  周用电量
 * @apiSuccess {Double}        data.conversionList.dicpWeek                   周充电量
 * @apiSuccess {Double}        data.conversionList.diep                   月用电量
 * @apiSuccess {Integer}        data.conversionList.dicp                   月用电量比较
 * @apiSuccess {Integer}        data.conversionList.dicpCompare                   月充电量比较(大于上月利率为1，小于-1，等于0)
 * @apiSuccess {Integer}        data.conversionList.diepCompare                   月用电量比较(大于上月利率为1，小于-1，等于0)
 * @apiSuccess {Integer}        data.conversionList.diepWeekCompare               周用电量比较(大于上月利率为1，小于-1，等于0)
 * @apiSuccess {Integer}        data.conversionList.dicpWeekCompare               周充电量比较(大于上月利率为1，小于-1，等于0)
 * @apiSuccess {Integer}        data.conversionList.diepDayCompare                日用电量比较(大于上月利率为1，小于-1，等于0)
 * @apiSuccess {Integer}        data.conversionList.dicpDayCompare                日充电量比较(大于上月利率为1，小于-1，等于0)
 * @apiSuccess {String}        data.conversionList.percentage                   转换率百分比
 * @apiSuccess {Object[]}       data.onLineRateList                    	     数据封装 在线率
 * @apiSuccess {Integer}        data.onLineRateList.onLineRateDay                             日在线设备
 * @apiSuccess {Integer}        data.onLineRateList.onLineRateWeek                            周在线设备
 * @apiSuccess {Integer}        data.onLineRateList.onLineRate                                月在线设备
 * @apiSuccess {Integer}        data.onLineRateList.pileSizeDay                               日运营设备
 * @apiSuccess {Integer}        data.onLineRateList.pileSizeWeek                              周运营设备
 * @apiSuccess {String}        data.onLineRateList.pileSize                                  月运营设备
 * @apiSuccess {Integer}        data.onLineRateList.pileSizeDayCompare                            日运营设备比较
 * @apiSuccess {Integer}        data.onLineRateList.pileSizeWeekCompare                            周运营设备比较
 * @apiSuccess {Integer}        data.onLineRateList.pileSizeCompare                             月运营设备比较
 * @apiSuccess {Integer}        data.onLineRateList.onLineRateDayCompare                             日在线设备比较
 * @apiSuccess {Integer}        data.onLineRateList.onLineRateWeekCompare                             周在线设备比较
 * @apiSuccess {Integer}        data.onLineRateList.onLineRateCompare                             月在线设备比较
 * @apiSuccess {String}        data.onLineRateList.percentage                             在线率百分比
 * @apiSuccess {Object[]}       data.accuracy                           数据封装 准确率
 * @apiSuccess {Integer}        data.accuracy.lossSizeDay                     日丢单设备
 * @apiSuccess {Integer}        data.accuracy.operateSizeDay                 日运营设备
 * @apiSuccess {Integer}        data.accuracy.lossSizeWeek                   周丢单设备
 * @apiSuccess {Integer}        data.accuracy.operateSizeWeek               周运营设备
 * @apiSuccess {Integer}        data.accuracy.lossSize                      月丢单设备
 * @apiSuccess {Integer}        data.accuracy.operateSize                   月运营设备
 * @apiSuccess {Integer}        data.accuracy.lossSizeDayCompare             日丢单设备比较
 * @apiSuccess {Integer}        data.accuracy.operateSizeDayCompare           日在线设备比较
 * @apiSuccess {Integer}        data.accuracy.lossSizeWeekCompare           周丢单设备比较
 * @apiSuccess {Integer}        data.accuracy.operateSizeWeekCompare        周在线设备比较
 * @apiSuccess {Integer}        data.accuracy.lossSizeCompare               月丢单设备比较
 * @apiSuccess {Integer}        data.accuracy.operateSizeCompare          月在线设备比较
 * @apiSuccess {String}        data.accuracy.percentage                   月在线设备百分比
 * <br/>
 * @apiError -999 系统异常!
 * @apiError -888 请求方式异常!
 * @apiError -1700000 参数缺失
 */
	@RequestMapping(value = "/efficiency", method = RequestMethod.GET)
	public ResultVo efficiency(@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		DataVo dataMap = new DataVo(map);
		if(dataMap.isNotBlank("userId")){
			DataVo vo =businessAnalysisService.efficiency(dataMap);
			resultVo.setData(vo);
			return resultVo;
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}
}

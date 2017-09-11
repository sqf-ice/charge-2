package com.clouyun.charge.modules.charge.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.service.StationProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述: 场站利润
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月14日 下午2:04:39
 */
@RestController
@RequestMapping("/api/profit/station")
public class StationProfitController extends BusinessController{
	
	@Autowired
	StationProfitService stationProfitService;
	
	/**
	 * @api {GET} /api/profit/station   场站利润列表
	 * @apiName queryProfits
	 * @apiGroup StationProfitController.java
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟  场站的利润列表(列表UI 红色向上箭头表示同比上升，绿色向下箭头表示同比下降).列表展示按文档顺序展示即可
	 * <br/>
	 * @apiParam {int} pageNum 页码
	 * @apiParam {int} pageSize 页大小
	 * @apiParam {int} userId 登陆用户id
	 * @apiParam {String} settlementMonth			结算月份时间(yyyy-MM)
	 * @apiParam {String} [sort] 		排序字段
	 * @apiParam {String} [order] 		排序方向
	 * @apiParam {String} [orgName] 				运营商名称(支持模糊查询)
	 * @apiParam {String} [orgId] 					运营商id
	 * @apiParam {String} [stationType] 			场站类型(type=jingylx)
	 * @apiParam {String} [stationServiceType] 		场站服务类型(type=czfwlx)
	 * @apiParam {String} [stationCoopType] 		场站合作方式(type=czhzfs)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {int} 	total     	总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Int} data.list.spId 场站利润主键Id
	 * @apiSuccess {Int} data.list.stationId 场站利润场站Id
	 * @apiSuccess {Date} data.list.settlementMonth 结算月份(yyyy-MM)
	 * @apiSuccess {String} data.list.orgName 运营商名称
	 * @apiSuccess {String} data.list.stationName 场站名称
	 * @apiSuccess {BigDecimal} data.list.stationTargetCharge	 场站目标充电量
	 * @apiSuccess {Int} data.list.stationTargetChargeCompare 	场站目标充电量比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.stationTotalCharge  	充电总电量
	 * @apiSuccess {Int} data.list.stationTotalChargeCompare  	充电总电量比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.stationTargetIncome 	场站目标收入
	 * @apiSuccess {Int} data.list.stationTargetIncomeCompare 	场站目标收入比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.fixedIncomeSubtotal 	固定收入
	 * @apiSuccess {Int} data.list.fixedIncomeSubtotalCompare 	固定收入比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.unfixedIncomeSubtotal 非固定收入
	 * @apiSuccess {Int} data.list.unfixedIncomeSubtotalCompare 非固定收入比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.stationIncomeSubtotal 场站收入
	 * @apiSuccess {Int} data.list.stationIncomeSubtotalCompare 场站收入比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.fixedExpendSubtotal 	场站固定支出
	 * @apiSuccess {Int} data.list.fixedExpendSubtotalCompare 	场站固定支出比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.unfixedExpendSubtotal 场站非固定支出
	 * @apiSuccess {Int} data.list.unfixedExpendSubtotalCompare 场站非固定支出比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.costTotal 			场站成本
	 * @apiSuccess {Int} data.list.costTotalCompare 			场站成本比较(-1:下降; 0:持平;  1:上升)
	 * @apiSuccess {BigDecimal} data.list.stationGrossProfit 	场站毛利润
	 * @apiSuccess {Int} data.list.stationGrossProfitCompare 	场站毛利润比较(-1:下降; 0:持平;  1:上升)

     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     *	{
     *		errorCode: 0,
     *		errorMsg: "操作成功!",
     *		total: 0,
     *		data: {
     *			pageNum: 1,
     *			pageSize: 165,
     *			size: 165,
     *			startRow: 0,
     *			endRow: 164,
     *			total: 165,
     *			pages: 1,
     *			list: [
     *			{
     *				costAmortization: 0,
     *				stationTotalPower: "",
     *				pileTotalPower: 0,
     *				costTotal: 0,
     *				otherIncome: 0,
     *				stationId: 275,
     *				orgId: 48,
     *				inFacRentalFee: 0,
     *				stationTotalChargeCompare: 0,
     *				exLandRentalExpend: 0,
     *				costTotalCompare: 0,
     *				spId: 6918,
     *				fixedExpendSubtotalCompare: 0,
     *				stationName: "微纳园",
     *				pileTotalLoss: 0,
     *				artificialFee: 0,
     *				stationType: 1,
     *				stationIncomeSubtotal: 0,
     *				stationOfficeCharge: 0,
     *				exHouseRentalFee: 0,
     *				unfixedExpendSubtotalCompare: 0,
     *				networkFee: 0,
     *				parkingFee: 0,
     *				stationTotalCharge: 497.71,
     *				stationGrossProfit: 0,
     *				otherExpenses: 0,
     *				stationTargetIncome: 0,
     *				unfixedIncomeSubtotal: 0,
     *				orderIncome: 672.42,
     *				stationTestCharge: 0,
     *				propertyFee: 0,
     *				stationTargetIncomeCompare: 0,
     *				stationCoopType: "",
     *				inParkRentalFee: 0,
     *				lowConsumable: 0,
     *				unfixedExpendSubtotal: 0,
     *				stationTargetCharge: 0,
     *				eleFee: "",
     *				fixedIncomeSubtotalCompare: 0,
     *				inLandRentalFee: 0,
     *				waterFee: 0,
     *				depreFixedAsset: 0,
     *				diviExpend: "",
     *				repairFee: 0,
     *				stationUseCharge: 0,
     *				unfixedIncomeSubtotalCompare: 0,
     *				stationGoalCharge: 0,
     *				stationGrossProfitCompare: 0,
     *				settlementMonth: "2017-04",
     *				stationIncomeSubtotalCompare: 0,
     *				stationTargetChargeCompare: 0,
     *				orgName: "国联科陆无锡新动力有限公司",
     *				fixedExpendSubtotal: 0,
     *				stationServiceType: "",
     *				inPilePmIncome: 0,
     *				fixedIncomeSubtotal: 0,
     *				inPileTotalIncome: 0,
     *				exLandRentalFee: 0
     *			}
     *		]
     *			prePage: 0,
     *			nextPage: 0,
     *			isFirstPage: true,
     *			isLastPage: true,
     *			hasPreviousPage: false,
     *			hasNextPage: false,
     *			navigatePages: 8,
     *			navigatepageNums: [
     *				1
     *		],
     *			navigateFirstPage: 1,
     *			navigateLastPage: 1,
     *			firstPage: 1,
     *			lastPage: 1
     *			}
     *		}
     *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1000006 当前的登录用户为空!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo queryProfits(@RequestParam Map map) throws Exception {
		ResultVo vo = stationProfitService.queryStationProfits(map);
		return vo;
	}

	/**
	 * @api {GET} /api/profit/station/total   场站利润列合计
	 * @apiName queryProfitsTotal
	 * @apiGroup StationProfitController.java
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟  场站的利润合计(列表UI 红色向上箭头表示同比上升，绿色向下箭头表示同比下降).列表展示按文档顺序展示即可
	 * <br/>
	 * @apiParam {int} pageNum 页码
	 * @apiParam {int} pageSize 页大小
	 * @apiParam {int} userId 登陆用户id
	 * @apiParam {String} settlementMonth			结算月份时间(yyyy-MM)
	 * @apiParam {String} [sort] 		排序字段
	 * @apiParam {String} [order] 		排序方向
	 * @apiParam {String} [orgName] 				运营商名称(支持模糊查询)
	 * @apiParam {String} [orgId] 					运营商id
	 * @apiParam {String} [stationType] 			场站类型(type=jingylx)
	 * @apiParam {String} [stationServiceType] 		场站服务类型(type=czfwlx)
	 * @apiParam {String} [stationCoopType] 		场站合作方式(type=czhzfs)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {int} 	total     	总记录数
	 * @apiSuccess {Object[]} data	 合计对象
	 * @apiSuccess {BigDecimal} data.allStationTargetCharge 合计场站目标充电量
	 * @apiSuccess {Int} data.allStationTargetChargeCompare 合计场站目标充电量比较
	 * @apiSuccess {BigDecimal} data.allStationTotalCharge  合计充电充电量
	 * @apiSuccess {Int} data.allStationTotalChargeCompare  合计充电充电量比较
	 * @apiSuccess {BigDecimal} data.allStationTargetIncome 合计目标收入
	 * @apiSuccess {Int} data.allStationTargetIncomeCompare 合计目标收入比较
	 * @apiSuccess {BigDecimal} data.allFixedIncomeSubtotal 合计固定收入
	 * @apiSuccess {Int} data.allFixedIncomeSubtotalCompare 合计固定收入比较
	 * @apiSuccess {BigDecimal} data.allUnfixedIncomeSubtotal 合计非固定收入
	 * @apiSuccess {Int} data.allUnfixedIncomeSubtotalCompare 合计非固定收入比较
	 * @apiSuccess {BigDecimal} data.allStationIncomeSubtotal 合计场站收入
	 * @apiSuccess {Int} data.allStationIncomeSubtotalCompare 合计场站收入比较
	 * @apiSuccess {BigDecimal} data.allFixedExpendSubtotal 合计场站固定支出
	 * @apiSuccess {Int} data.allFixedExpendSubtotalCompare 合计场站固定支出比较
	 * @apiSuccess {BigDecimal} data.allUnfixedExpendSubtotal 合计场站非固定支出
	 * @apiSuccess {Int} data.allUnfixedExpendSubtotalCompare 合计场站非固定支出比较
	 * @apiSuccess {BigDecimal} data.allCostTotal 合计场站成本
	 * @apiSuccess {Int} data.allCostTotalCompare 合计场站成本比较
	 * @apiSuccess {BigDecimal} data.allStationGrossProfit 合计场站毛利润
	 * @apiSuccess {Int} data.allStationGrossProfitCompare 合计场站毛利润比较

	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *		errorCode: 0,
	 *		errorMsg: "操作成功!",
	 *		total: 0,
	 *		data: {
	 *			"allFixedIncomeSubtotal": 33114247.5,
	 *			"allStationTargetChargeCompare": 0,
	 *			"allUnfixedIncomeSubtotalCompare": 0,
	 *			"allUnfixedExpendSubtotal": 0,
	 *			"allFixedIncomeSubtotalCompare": -1,
	 *			"allStationGrossProfitCompare": -1,
	 *			"allStationTotalCharge": 5594443.43,
	 *			"allStationTargetIncomeCompare": 0,
	 *			"allStationTargetCharge": 0,
	 *			"allStationTargetIncome": 0,
	 *			"allFixedExpendSubtotalCompare": 0,
	 *			"allStationIncomeSubtotalCompare": -1,
	 *			"allStationIncomeSubtotal": 33114247.5,
	 *			"allUnfixedIncomeSubtotal": 0,
	 *			"allCostTotal": 0,
	 *			"allUnfixedExpendSubtotalCompare": 0,
	 *			"allCostTotalCompare": -1,
	 *			"allFixedExpendSubtotal": 0,
	 *			"allStationTotalChargeCompare": -1,
	 *			"allStationGrossProfit": 33114247.5
	 *		},
	 *		pageNum: 1,
	 *		pageSize: 165,
	 *		size: 165,
	 *		startRow: 0,
	 *		endRow: 164,
	 *		total: 165,
	 *		pages: 1,
	 *		]
	 *		prePage: 0,
	 *		nextPage: 0,
	 *		isFirstPage: true,
	 *		isLastPage: true,
	 *		hasPreviousPage: false,
	 *		hasNextPage: false,
	 *		navigatePages: 8,
	 *		navigatepageNums: [
	 *			1
	 *		],
	 *		navigateFirstPage: 1,
	 *		navigateLastPage: 1,
	 *		firstPage: 1,
	 *		lastPage: 1
	 *		}
	 *		}
	 *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1000006 当前的登录用户为空!
	 */
	@RequestMapping(value = "/total", method = RequestMethod.GET)
	public ResultVo queryProfitsTotal(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		Map map1 = stationProfitService.queryProfitSub(new DataVo(map));
		vo.setData(map1);
		return vo;
	}


	/**
	 * 
     * @api {GET} /api/profit/station/trends/{stationId}    场站利润详情收入趋势
     * @apiName queryIncomeTrends
     * @apiGroup StationProfitController.java
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据场站id获取场站利润详情收入趋势,默认前六个月
     * <br/>
     * @apiParam {int} stationId 场站id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} total     总记录数
     * @apiSuccess {BigDecimal} data.costTotal 成本合计
     * @apiSuccess {BigDecimal} data.stationGrossProfit 利润
     * @apiSuccess {BigDecimal} data.stationIncomeSubtotal 收入
     * @apiSuccess {Date} data.settlementMonth 结算月份(yyyy-MM)
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1102001 场站id不能为空!
	 */
	@RequestMapping(value = "/trends/{stationId}", method = RequestMethod.GET)
	public ResultVo queryIncomeTrends(@PathVariable("stationId") Integer stationId) throws Exception {
		ResultVo vo = new ResultVo();
		List<Map> incomeTrends = stationProfitService.queryIncomeTrends(stationId);
		vo.setData(incomeTrends);
		return vo;
	}
	
	/**
	 * 
     * @api {GET} /api/profit/station/{spId}   场站利润详情
     * @apiName queryProfitByKey
     * @apiGroup StationProfitController.java
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  场站的利润详情(按文档顺序即可)
     * <br/>
     * @apiParam {int} spId 场站利润id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} total     总记录数
     * @apiSuccess {Object[]} data.chargeInfo 充电信息封装
     * @apiSuccess {BigDecimal} data.chargeInfo.stationTotalPower 		场站总用电量
     * @apiSuccess {BigDecimal} data.chargeInfo.stationTargetCharge 	目标充电量
     * @apiSuccess {BigDecimal} data.chargeInfo.stationTotalCharge 		充电总电量
     * @apiSuccess {BigDecimal} data.chargeInfo.stationGoalCharge 		业务充电量
     * @apiSuccess {BigDecimal} data.chargeInfo.stationTestCharge 		测试电量
     * @apiSuccess {BigDecimal} data.chargeInfo.stationUseCharge 		自用电量
     * @apiSuccess {BigDecimal} data.chargeInfo.stationOfficeCharge 	办公用电
     * @apiSuccess {BigDecimal} data.chargeInfo.pileTotalLoss 			设备总损耗
     * @apiSuccess {BigDecimal} data.chargeInfo.pileTotalPower 			设备总用电量
     * @apiSuccess {Object[]} data.incomeInfo 收入信息封装
     * @apiSuccess {BigDecimal} data.incomeInfo.stationTargetIncome 	目标收入
     * @apiSuccess {BigDecimal} data.incomeInfo.orderIncome 			订单收入
     * @apiSuccess {BigDecimal} data.incomeInfo.inPileTotalIncome 		设备总收入
     * @apiSuccess {BigDecimal} data.incomeInfo.inPilePmIncome 			设备运维收入
     * @apiSuccess {BigDecimal} data.incomeInfo.inFacRentalFee 			充电设施租赁费
     * @apiSuccess {BigDecimal} data.incomeInfo.inParkRentalFee 		车位租赁费
     * @apiSuccess {BigDecimal} data.incomeInfo.inLandRentalFee 		土地租赁费
     * @apiSuccess {BigDecimal} data.incomeInfo.fixedIncomeSubtotal		 固定收入小计
     * @apiSuccess {BigDecimal} data.incomeInfo.otherIncome 			其他
     * @apiSuccess {BigDecimal} data.incomeInfo.unfixedIncomeSubtotal 	非固定收入小计
     * @apiSuccess {BigDecimal} data.incomeInfo.stationIncomeSubtotal 	场站收入合计
     * @apiSuccess {Object[]} data.costInfo 成本信息封装
     * @apiSuccess {BigDecimal} data.costInfo.eleFee 					电费
     * @apiSuccess {BigDecimal} data.costInfo.waterFee 					水费
     * @apiSuccess {BigDecimal} data.costInfo.networkFee				 网费
     * @apiSuccess {BigDecimal} data.costInfo.diviExpend 				分成支出
     * @apiSuccess {BigDecimal} data.costInfo.exLandRentalFee 			土地租赁费
     * @apiSuccess {BigDecimal} data.costInfo.exLandRentalExpend 		土地租赁分成费
     * @apiSuccess {BigDecimal} data.costInfo.exHouseRentalFee 			房屋租赁费
     * @apiSuccess {BigDecimal} data.costInfo.propertyFee 				物业管理费
     * @apiSuccess {BigDecimal} data.costInfo.artificialFee 			人工支出
     * @apiSuccess {BigDecimal} data.costInfo.depreFixedAsset 			固定资产折旧
     * @apiSuccess {BigDecimal} data.costInfo.costAmortization 			费用摊销
     * @apiSuccess {BigDecimal} data.costInfo.parkingFee 				停车费
     * @apiSuccess {BigDecimal} data.costInfo.fixedExpendSubtotal 		固定支出小计
     * @apiSuccess {BigDecimal} data.costInfo.repairFee 				维修费
     * @apiSuccess {BigDecimal} data.costInfo.lowConsumable 			低值易耗品
     * @apiSuccess {BigDecimal} data.costInfo.otherExpenses 			其他
     * @apiSuccess {BigDecimal} data.costInfo.unfixedExpendSubtotal 	非固定支出小计
     * @apiSuccess {BigDecimal} data.costInfo.costTotal 				成本合计
     * @apiSuccess {BigDecimal} data.costInfo.stationGrossProfit 		场站毛利润
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     *{
     *	errorCode: 0,
     *	errorMsg: "操作成功!",
     *	total: 0,
     *	data: {
     *		chargeInfo: {
     *			pileTotalPower: 0,
     *			stationGoalCharge: 0,
     *			stationTargetCharge: 0,
     *			stationTestCharge: 0,
     *			stationTotalCharge: 497.71,
     *			pileTotalLoss: 0,
     *			stationUseCharge: 0,
     *			stationOfficeCharge: 0
     *		},
     *		incomeInfo: {
     *			unfixedIncomeSubtotal: 0,
     *			inFacRentalFee: 0,
     *			orderIncome: 672.42,
     *			otherIncome: 0,
     *			inLandRentalFee: 0,
     *			inPilePmIncome: 0,
     *			fixedIncomeSubtotal: 0,
     *			stationIncomeSubtotal: 0,
     *			inPileTotalIncome: 0,
     *			stationTargetIncome: 0,
     *			inParkRentalFee: 0
     *		},
     *		costInfo: {
     *			unfixedExpendSubtotal: 0,
     *			exHouseRentalFee: 0,
     *			costTotal: 0,
     *			networkFee: 0,
     *			parkingFee: 0,
     *			eleFee: "",
     *			stationGrossProfit: 0,
     *			otherExpenses: 0,
     *			waterFee: 0,
     *			depreFixedAsset: 0,
     *			exLandRentalExpend: 0,
     *			propertyFee: 0,
     *			fixedExpendSubtotal: 0,
     *			diviExpend: "",
     *			repairFee: 0,
     *			artificialFee: 0,
     *			exLandRentalFee: 0,
     *			lowConsumable: 0
     *		}
     *	}
     *}
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1000006 场站利润id不能为空!
	 */
	@RequestMapping(value = "/{spId}", method = RequestMethod.GET)
	public ResultVo queryProfitByKey(@PathVariable("spId") Integer spId) throws Exception {
		ResultVo vo = new ResultVo();
		Map profitByKey = stationProfitService.queryProfitByKey(spId);
		vo.setData(profitByKey);
		return vo;
	}
	
	/**
     * @api {GET} /api/profit/station/_import    场站利润导入
     * @apiName importProfits
     * @apiGroup StationProfitController.java
     * @apiVersion 2.0.0
     * @apiDescription 曹伟    场站的利润常规数据导入
     * <br/>
     * @apiParam {MultipartFile} file 场站利润模板
     * @apiParam {Int} userId 当前登陆用户id
     * @apiParam {Date} settlementMonth 需要导入的月份(yyyy-MM)
     * <br/>
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} 	total    	 总记录数
     * @apiSuccess {String} data.impStatus 			数据状态
     * @apiSuccess {String} data.orgName 			运营商名称
     * @apiSuccess {String} data.stationName	 	场站名称
     * @apiSuccess {String} data.stationTotalPower 	场站总用电量
     * @apiSuccess {String} data.otherIncome 		非固定收入其他
     * @apiSuccess {String} data.eleFee 			电费
     * @apiSuccess {String} data.waterFee 			水费
     * @apiSuccess {String} data.repairFee 			维修费
     * @apiSuccess {String} data.lowConsumable 		低值易耗品
     * @apiSuccess {String} data.otherExpenses 		成本其他
     * @apiSuccess {String} data.description		数据校验结果
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -2000000 导入失败!
	 */
	@RequestMapping(value = "/_import", method = RequestMethod.POST)
	public ResultVo importProfits(@RequestParam("file") MultipartFile file,@RequestParam Map map) throws Exception {
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile excelFile=multipartRequest.getFile("file");
		ResultVo vo  = stationProfitService.importProfit(excelFile,map);
		return vo;
	}
	
	
	/**
     * @api {GET} /api/profit/station/_export  场站利润报表导出
     * @apiName exportStationProfits
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据查询条件导出数据
     * <br/>
     * @apiParam {int}    userId	     	登陆用户id
	 * @apiParam {int} pageNum 			页码
	 * @apiParam {int} pageSize 			页大小
	 * @apiParam {String} settlementMonth			结算月份时间(yyyy-MM)
	 * @apiParam {String} [sort] 					排序字段
	 * @apiParam {String} [order] 					排序方向
	 * @apiParam {String} [orgName] 				运营商名称(支持模糊查询)
	 * @apiParam {String} [orgId] 					运营商id
	 * @apiParam {String} [stationType] 			场站类型
	 * @apiParam {String} [stationServiceType] 		场站服务类型
	 * @apiParam {String} [stationCoopType] 		场站合作方式
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
    public ResultVo exportStationProfits(@RequestParam Map map,HttpServletResponse response) throws Exception {
		ResultVo vo = new ResultVo();
		stationProfitService.export(map,response);
		return vo;
    }
}

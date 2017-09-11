package com.clouyun.charge.modules.charge.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.service.CostService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 描述: 充电成本控制器 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: lips <lips@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年4月14日
 */
@RestController
@RequestMapping("/api/cost")
public class CostController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(CostController.class);
	
	@Autowired CostService costService;
	
	
	
	 /**
     * @api {get} /api/cost/cost   用电报表查询
     * @apiName selectCost
     * @apiGroup CostController
     * @apiVersion 2.0.0
     * @apiDescription 易凯     用电报表查询
     * <br/>
	 * @apiParam {String}           userId                                 用户Id
     * @apiParam {String}        [stationNo]                             场站编号
     * @apiParam {String}        [startDate]                             开始时间(yyyy-mm-dd)
     * @apiParam {String}        [endDate]                              结束时间(yyyy-mm-dd)
     * @apiParam {String}        [stationId]                           场站Id
	 * @apiParam {String}        [stationName]                           场站名称 模糊查询
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {Int}       data.list.prId                    	  主键(列表Id)
	 * @apiSuccess {Date}       data.list.prStartDate                  开始日期(yyyy-mm-dd)
	 * @apiSuccess {Date}       data.list.prEndDate                   结束日期(yyyy-mm-dd)
	 * @apiSuccess {String}       data.list.stationNo                  场站编号
	 * @apiSuccess {String}       data.list.stationName                场站名称
	 * @apiSuccess {Double}       data.list.prTotalPower               用电量(kW·h)
	 * @apiSuccess {Double}       data.list.prPowerChg                 充电设施用电量(kW·h)
	 * @apiSuccess {Double}       data.list.prChg                      充电量(kW·h)
	 * @apiSuccess {String}       data.list.prPowerEqip                其他设施用电量(kW·h)
	 * @apiSuccess {Double}       data.list.prTransLoss                 变压器损耗电量(kW·h)
	 * @apiSuccess {Double}       data.list.prChgLoss                  充电设施损耗量(kW·h)
	 * @apiSuccess {Date}       data.list.createDate                 报表生成时间（yyyy-mm-dd HH:mm:ss）
	 * <br/>
     * @apiError -999 系统异常!
	 * @apiError -888  请求方式异常
	 * @apiError -1700000  参数缺失
     */
	@RequestMapping(value = "/cost", method = RequestMethod.GET)
	public ResultVo selectCost(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=costService.selectCost(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	
	 /**
     * @api {get} /api/cost/_export   用电报表导出
     * @apiName exportCost
     * @apiGroup CostController
     * @apiVersion 2.0.0
     * @apiDescription 易凯     用电报表导出
     * <br/>
	 * @apiParam {String}           userId                                 用户Id
	 * @apiParam {String}        [stationNo]                             场站编号
	 * @apiParam {String}        [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {String}        [endDate]                              结束时间(yyyy-mm-dd)
	 * @apiParam {String}        [stationId]                           场站Id
	 * @apiParam {String}        [stationName]                           场站名称 模糊查询
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1700000   参数缺失
     */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
	public void exportCost(@RequestParam Map map,HttpServletResponse response) throws Exception {
	    DataVo dataVo=new DataVo(map);
		if(dataVo.isNotBlank("userId")) {
			costService.exportCost(dataVo, response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}
	/**
	 * @api {get} /api/cost/income   收入报表查询
	 * @apiName selectIncome
	 * @apiGroup CostController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    收入报表查询
	 * <br/>
	 * @apiParam {String}           userId                                 用户Id
	 * @apiParam {String}        [stationNo]                             场站编号
	 * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                              结束时间(yyyy-mm-dd)
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * @apiParam {String}        [stationId]                        场站Id
	 * @apiParam {String}        [stationName]                        场站名称 模糊查询
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}          data                 		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Int}        stationId                      列表主键(场站Id)
	 * @apiSuccess {Date}        data.list.irStartDate                 开始日期(yyyy-mm-dd)
	 * @apiSuccess {Date}        data.list.irEndDate                  结束日期(yyyy-mm-dd)
	 * @apiSuccess {String}       data.list.stationNo                场站编号
	 * @apiSuccess {String}       data.list.stationName              场站名称
	 * @apiSuccess {Double}       data.list.irTotalPower            总用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower1               尖时总用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower2              峰时总用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower3              平时总用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower4              谷时总用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPowerCost           用电成本(kW·h)
	 * @apiSuccess {Double}       data.list.irPowerChg            总冲电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg1            	  尖时总充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg2           	  峰时总充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg3                平时总充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg4                谷时总充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irIncome              充电收入(元)
	 * @apiSuccess {Double}       data.list.irProfit              销售毛利(元)
	 * @apiSuccess {Date}         data.list.createDate              报表生成时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {Int}         data.list.stationId             主键
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
	 */
	@RequestMapping(value = "/income", method = RequestMethod.GET)
	public ResultVo selectIncome(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=costService.selectIncome(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	/**
	 * @api {get} /api/cost/income/_export  收入报表导出
	 * @apiName exportIncome
	 * @apiGroup CostController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    收入报表导出
	 * <br/>
	 * @apiParam {String}           userId                                 用户Id
	 * @apiParam {String}        [stationNo]                             场站编号
	 * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                              结束时间(yyyy-mm-dd)
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * @apiParam {String}        [stationId]                        场站Id
	 * @apiParam {String}        [stationName]                        场站名称 模糊查询
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1700000  参数缺失
	 */


	@RequestMapping(value = "/income/_export", method = RequestMethod.GET)
	public void exportIncome(@RequestParam Map data,HttpServletResponse response) throws Exception {
		DataVo vo  =new DataVo(data);
		if(vo.isNotBlank("userId")){
			costService.exportIncome(vo,response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}

	}

	/**
	 * @api {get} /api/cost/income/detail   收入报表详情
	 * @apiName detailIncome
	 * @apiGroup CostController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    收入报表详情
	 * <br/>
	 * @apiParam {String}        stationId                             场站编号
	 * @apiParam {Date}         irStartDate                           开始时间(yyyy-mm-dd) 传入列表的开始日期
	 * @apiParam {Date}         irEndDate                              结束时间(yyyy-mm-dd)传入列表的结束日期
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}          data                 		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Date}       data.list.irStartDate                 开始日期(yyyy-mm-dd)
	 * @apiSuccess {Date}       data.list.irEndDate                  结束日期(yyyy-mm-dd)
	 * @apiSuccess {String}       data.list.pileNo                   充电桩编号
	 * @apiSuccess {String}       data.list.pileName            充电桩名称
	 * @apiSuccess {Double}       data.list.irTotalPower            用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower1               尖时用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower2              峰时用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower3              平时用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPower4              谷时用电量(kW·h)
	 * @apiSuccess {Double}       data.list.irPowerCostPile           用电成本(kW·h)
	 * @apiSuccess {Double}       data.list.irPowerChg            总电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg1            	  尖时充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg2           	  峰时充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg3                平时充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irChg4                谷时充电量(kW·h)
	 * @apiSuccess {Double}       data.list.irIncome              充电收入(元)
	 * @apiSuccess {Double}       data.list.irProfit              销售毛利(元)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
	 */
	@RequestMapping(value = "/income/detail", method = RequestMethod.GET)
	public ResultVo detailIncome(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo data = new DataVo(map);
		 if(data.isNotBlank("stationId")) {
			 PageInfo pageList=costService.detailIncome(data);
			 resultVo.setData(pageList);
		 } else {
			 throw   new BizException(1700000,"场站Id");
		 }
		return resultVo;
	}
}





package com.clouyun.charge.modules.finance.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.finance.service.HistoryBillService;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月18日
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/historys")
public class HistoryBillController {
	
	@Autowired
	private HistoryBillService historyBillService;
	/**
	 * 
	 * @api {GET} /api/historys/{id}   历史账单详情 
	 * @apiName  historyBill
	 * @apiGroup HistoryBillController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 根据id查询历史账单详情 
	 * <br/>
	 * @apiParam   {String} id 历史账单id
	 * <br/>
	 * @apiSuccess {String}   errorCode              错误码
	 * @apiSuccess {String}   errorMsg               消息说明
     * @apiSuccess {Object}   data                   数据封装
     * @apiSuccess {Object[]} data.list              数据对象集合
     * @apiSuccess {Integer}  data.list.id 	                           历史账单id
     * @apiSuccess {Integer}  data.list.contractId   合约id
     * @apiSuccess {Decimal}  data.list.eqipCost     总用电成本
     * @apiSuccess {Decimal}  data.list.serviceFee   总服务费
     * @apiSuccess {String}   data.list.orgName      运营商名称
     * @apiSuccess {String}   data.list.contractName 合约名称
     * @apiSuccess {Decimal}  data.list.stationName  合约场站名称
     * @apiSuccess {Decimal}  data.list.income       充电总收入
     * @apiSuccess {datetime} data.list.contractDate 合约计算日期
     * @apiSuccess {Date}     data.list.date         出账日期
     * @apiSuccess {Decimal} data.list.contractServiceFee 总分成服务费
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResultVo historyBill(@PathVariable Integer id)throws BizException{
		ResultVo resultVo = new ResultVo();
		Map historyBill = historyBillService.getHistoryBill(id);
		resultVo.setData(historyBill);
		return resultVo;
	}
	/**
	 * 
	 * @api {GET} /api/historys   分页查询历史账单列表
	 * @apiName  historyBillsPage
	 * @apiGroup HistoryBillController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 分页查询历史账单
	 * <br/>
	 * @apiParam   {Date}      startDate                开始时间(yyyy-MM-dd)
	 * @apiParam   {Date}      endDate                  结束时间(yyyy-MM-dd)
	 * @apiParam   {String}    status                   账单状态(待确认)
	 * @apiParam   {String}    contractName             合约名称
	 * @apiParam   {String}    stationName              合约场站名称 
	 * @apiParam   {int}       pageIndex                页码
	 * @apiParam   {int}       pageSize                 页大小
	 * @apiParam   {String}    sort                     排序字段
	 * @apiParam   {String}    order                    排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiSuccess {String}      errorCode                      错误码
	 * @apiSuccess {String}      errorMsg                       消息说明
     * @apiSuccess {Object}      data                           分页数据封装
     * @apiSuccess {Object}      data.pageInfo                  分页数据对象
     * @apiSuccess {Object[]}    data.pageInfo.list             数据对象集合
     * @apiSuccess {Integer}     data.pageInfo.list.id 		            历史账单id
     * @apiSuccess {Integer}     data.pageInfo.list.contractId  合约id
     * @apiSuccess {Decimal}     data.pageInfo.list.eqipCost    总用电成本
     * @apiSuccess {Decimal}     data.pageInfo.list.serviceFee  总服务费
     * @apiSuccess {String}      data.pageInfo.list.orgName 	运营商名称
     * @apiSuccess {String}      data.pageInfo.list.contractName合约名称 	
     * @apiSuccess {Decimal}     data.pageInfo.list.stationName 合约场站名称 
     * @apiSuccess {Decimal}     data.pageInfo.list.income 	           充电总收入
     * @apiSuccess {Datetime}    data.pageInfo.list.contractDate合约计算日期
     * @apiSuccess {Date}        data.pageInfo.list.date 		出账日期
     * @apiSuccess {Decimal}     data.pageInfo.list.contractServiceFee 总分成服务费
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo historyBillsPage(@RequestParam Map map)throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = historyBillService.getHistoryBillsPage(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
}

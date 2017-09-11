package com.clouyun.charge.modules.charge.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.service.GatheringService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 充值管理控制器 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: lips <lips@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年4月14日
 */
@RestController
@RequestMapping("/api/gathering")
public class GatheringController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(GatheringController.class);
	
	@Autowired GatheringService gatheringService;

	
	 /**
     * @api {get} /api/gathering/recharge   充值记录查询
     * @apiName selectRecharge
     * @apiGroup GatheringController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    充值记录查询
     * <br/>
	 * @apiParam {String}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                               结束时间(yyyy-mm-dd)
	 * @apiParam {String}        [billRechargeNo]                        订单号
	 * @apiParam {Int}        [rechargeType]                          充值方式
	 * @apiParam {Int}        [groupId]                             集团Id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
     * @apiParam {String}        [consName]                              会员名称
     * @apiParam {Int}        [consTypeCode]                          会员类型
     * @apiParam {String}        [phoneNo]                               手机号
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {String}		  data.list.billRechargeNo  		订单号
	 * @apiSuccess {Double}		  data.list.amount       		充值金额(元)
	 * @apiSuccess {String}		  data.list.consName             会员名
	 * @apiSuccess {String}		  data.list.consTypeCode         会员类型
	 * @apiSuccess {String}		  data.list.groupName    		集团名称
	 * @apiSuccess {String}		  data.list.consPhone  			手机号
	 * @apiSuccess {String}		  data.list.rechargeType			充值方式
	 * @apiSuccess {Date}		     data.list.createTime			创建时间创建时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}		  data.list.outBillNo			备注
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
     */

	@RequestMapping(value = "/recharge", method = RequestMethod.GET)
	public ResultVo selectRecharge(@RequestParam  Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=gatheringService.selectRecharge(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/gathering/recharge/_count   充值记录合计
	 * @apiName selectRechargeCount
	 * @apiGroup GatheringController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    充值记录合计
	 * <br/>
	 * @apiParam {String}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                               结束时间(yyyy-mm-dd)
	 * @apiParam {String}        [billRechargeNo]                        订单号
	 * @apiParam {Int}        [rechargeType]                          充值方式
	 * @apiParam {Int}        [groupId]                             集团Id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
	 * @apiParam {String}        [consName]                              会员名称
	 * @apiParam {Int}        [consTypeCode]                          会员类型
	 * @apiParam {String}        [phoneNo]                               手机号
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {double}         data                    		  充值金额(元)合计
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
	 */

	@RequestMapping(value = "/recharge/_count", method = RequestMethod.GET)
	public ResultVo selectRechargeCount(@RequestParam  Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			double amountCount=gatheringService.selectRechargeCount(vo);
			resultVo.setData(amountCount);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	 /**
     * @api {get} /api/gathering/recharge/_export   充值记录导出
     * @apiName exportRecharge
     * @apiGroup GatheringController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    充值记录导出
     * <br/>
	 * @apiParam {String}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                               结束时间(yyyy-mm-dd)
	 * @apiParam {String}        [billRechargeNo]                        订单号
	 * @apiParam {Int}        [rechargeType]                          充值方式
	 * @apiParam {Int}        [groupId]                             集团Id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
	 * @apiParam {String}        [consName]                              会员名称
	 * @apiParam {Int}        [consTypeCode]                          会员类型
	 * @apiParam {String}        [phoneNo]                               手机号
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
	 * @apiError -1700000  参数缺失
     */

	@RequestMapping(value = "/recharge/_export", method = RequestMethod.GET)
	public void exportRecharge(@RequestParam Map map,HttpServletResponse response) throws Exception {
			DataVo dataMap =  new DataVo(map);
			if(dataMap.isNotBlank("userId")){
				gatheringService.exportRecharge(dataMap,response);
			}else {
				throw   new BizException(1700000,"用户Id");
			}

	}

	
	/**
     * @api {get} /api/gathering/month   月结记录查询
     * @apiName selectYuejie
     * @apiGroup GatheringController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    月结记录查询
     * <br/>
	 * @apiParam {Int}           userId                                 用户Id
     * @apiParam {Int}        [groupId]                             集团Id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
     * @apiParam {String}        [gatherPerson]                          收款人员
	 * @apiParam {Date}         [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}         [endDate]                               结束时间(yyyy-mm-dd)
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}       data.list.seqId				主键
     * @apiSuccess {Date}       data.list.createTime              创建时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}       data.list.groupName			  集团名称
     * @apiSuccess {String}       data.list.acctNo               收款账号
     * @apiSuccess {Double}       data.list.curAmount            账号余额(元)
     * @apiSuccess {Double}       data.list.chgAmount            收款金额(元)
     * @apiSuccess {String}       data.list.seqDesc              备注
     * @apiSuccess {String}       data.list.gatherWay            收款方式
     * @apiSuccess {String}       data.list.groupNo              集团编号
     * @apiSuccess {String}       data.list.isInvoice            是否提供发票
     * @apiSuccess {String}       data.list.gatherPerson         收款人
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000  参数缺失!
     */
	@RequestMapping(value = "/month", method = RequestMethod.GET)
	public ResultVo selectYuejie(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=gatheringService.selectYuejie(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	/**
	 * @api {get} /api/gathering/month/_count   月结记录查询合计
	 * @apiName selectYuejieCount
	 * @apiGroup GatheringController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    月结记录查询合计
	 * <br/>
	 * @apiParam {Int}           userId                                 用户Id
	 * @apiParam {Int}        [groupId]                             集团Id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
	 * @apiParam {String}        [gatherPerson]                          收款人员
	 * @apiParam {Date}         [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}         [endDate]                               结束时间(yyyy-mm-dd)
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {double}         data                    		     合计收款金额(元)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000  参数缺失!
	 */
	@RequestMapping(value = "/month/_count", method = RequestMethod.GET)
	public ResultVo selectYuejieCount(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			double chgAmount=gatheringService.selectYuejieCount(vo);
			resultVo.setData(chgAmount);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	
	/**
     * @api {get} /api/gathering/month/_export   月结记录导出
     * @apiName exportYuejie
     * @apiGroup GatheringController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    月结记录导出
     * <br/>
	 * @apiParam {Int}           userId                                 用户Id
	 * @apiParam {Int}        [groupId]                             集团Id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
	 * @apiParam {String}        [gatherPerson]                          收款人员
	 * @apiParam {Date}         [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}         [endDate]                               结束时间(yyyy-mm-dd)
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
     */
	@RequestMapping(value = "/month/_export", method = RequestMethod.GET)
	public void exportYuejie(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataMap =  new DataVo(map);
		if(dataMap.isNotBlank("userId")) {
			gatheringService.exportYuejie(dataMap, response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}

	/**
     * @api {get} /api/gathering/cardRecharge   卡充值记录查询
     * @apiName selectKcz
     * @apiGroup GatheringController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    卡充值记录查询
     * <br/>
	 * @apiParam {Int}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                             开始时间
	 * @apiParam {Date}        [endDate]                               结束时间
	 * @apiParam {String}        [cardId]                                充值卡号
	 * @apiParam {Int}        [stationId]                           场站id
	 * @apiParam {String}        [stationName]                           场站名称
	 * @apiParam {Int}        [groupId]                             集团id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
	 * @apiParam {String}        [consName]                              会员名称
	 * @apiParam {String}        [consTypeCode]                          会员类型
	 * @apiParam {String}        [consTypeCode]                          会员类型
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      分页数据封装
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}       data.list.seqId      		订单号(主键)
     * @apiSuccess {String}       cdata.list.cardId   		充值卡号
     * @apiSuccess {String}       data.list.amount   		充值金额(元)
     * @apiSuccess {String}       data.list.groupName 		 集团名称
     * @apiSuccess {String}       data.list.rechargeTime 	 充值时间
     * @apiSuccess {String}       data.list.rechargeType   	付款方式(返回id,去查数据字典)
     * @apiSuccess {String}       data.list.stationName		收款场站
     * @apiSuccess {String}       data.list.consName   		 会员名
     * @apiSuccess {String}       data.list.consTypeCode  	会员类型
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
     */
	@RequestMapping(value = "/cardRecharge", method = RequestMethod.GET)
	public ResultVo selectKcz(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=gatheringService.selectKcz(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/gathering/cardRecharge/_count   卡充值记录合计
	 * @apiName selectKczCount
	 * @apiGroup GatheringController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    卡充值记录查询
	 * <br/>
	 * @apiParam {Int}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                             开始时间
	 * @apiParam {Date}        [endDate]                               结束时间
	 * @apiParam {String}        [cardId]                                充值卡号
	 * @apiParam {Int}        [stationId]                           场站id
	 * @apiParam {String}        [stationName]                           场站名称
	 * @apiParam {Int}        [groupId]                             集团id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
	 * @apiParam {String}        [consName]                              会员名称
	 * @apiParam {String}        [consTypeCode]                          会员类型
	 * @apiParam {String}        [consTypeCode]                          会员类型
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {double}         data                    		     合计充值金额
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/cardRecharge/_count", method = RequestMethod.GET)
	public ResultVo selectKczCount(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			double pageList=gatheringService.selectKczCount(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	/**
     * @api {get} /api/gathering/cardRecharge/_export   卡充值记录导出
     * @apiName exportKcz
     * @apiGroup GatheringController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    卡充值记录导出
     * <br/>
	 * @apiParam {Int}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                             开始时间
	 * @apiParam {Date}        [endDate]                               结束时间
	 * @apiParam {String}        [cardId]                                充值卡号
	 * @apiParam {Int}        [stationId]                           场站id
	 * @apiParam {String}        [stationName]                           场站名称
	 * @apiParam {Int}        [groupId]                             集团id
	 * @apiParam {String}        [groupName]                             集团名称 模糊查询
	 * @apiParam {String}        [consName]                              会员名称
	 * @apiParam {String}        [consTypeCode]                          会员类型
	 * @apiParam {String}        [consTypeCode]                          会员类型
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError
	 * -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1700000 参数缺失
     */
	
	@RequestMapping(value = "/cardRecharge/_export", method = RequestMethod.GET)
	public void exportKcz(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataMap =  new DataVo(map);
		if(dataMap.isNotBlank("userId")){
			gatheringService.exportKcz(dataMap,response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}

	}

	/**
	  * @api {post} /api/gathering/month                         新增月结记录
	 * @apiName addYuejie
	 * @apiGroup GatheringController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯     新增月结记录
	 * <br/>
	 * @apiParam {Date}        userId                             用户id
	 * @apiParam {Date}        gatherDate                            收款时间（yyyy-mm-dd）
	 * @apiParam {Date}        groupBillMonth                               结款时间（yyyy-mm）
	 * @apiParam {int}         groupId                               缴费用户
	 * @apiParam {int}         contractId                                合约Id
	 * @apiParam {Double}         billAmount                             结款金额
	 * @apiParam {int}         gatherWay                                收款方式
	 * @apiParam {Double}         chgAmount                           收款金额
	 * @apiParam {String}         gatherPerson                             收款人
	 * @apiParam {int}         isBill                                   提供发票
	 * @apiParam {int}         groupBillId                                 结款id
	 * @apiParam {String}     [seqDesc]                             备注
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		       新增成功返回值
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失!
	 * @apiError -888 请求方式错误!
	 */
	@RequestMapping(value = "/month", method = RequestMethod.POST)
	public ResultVo addYuejie(@RequestBody Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo dataVo = new DataVo(map);
		String[] list = {"userId","gatherDate","groupBillMonth","groupId","contractId","billAmount","gatherWay","chgAmount","gatherPerson","isBill","groupBillId"};
		Map  listMap = new HashMap();
		listMap.put("userId","用户Id");
		listMap.put("gatherDate","收款时间");
		listMap.put("groupBillMonth","结款时间");
		listMap.put("groupId","缴费用户Id");
		listMap.put("contractId","合约Id");
		listMap.put("billAmount","结款金额");
		listMap.put("gatherWay","收款方式");
		listMap.put("chgAmount","收款金额");
		listMap.put("gatherPerson","收款人");
		listMap.put("isBill","是否提供发票");
		listMap.put("groupBillId","结款id");
		ChargeManageUtil.isMapNullPoint(list,dataVo,listMap);
		gatheringService.addYuejie(dataVo);
		return resultVo;
	}

	/**
	 * @api {get} /api/gathering/bill/month                       新增月结中获取结款时间
	 * @apiName getGroupBillMonth
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    新增月结中获取结款时间
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		       新增成功返回值
	 * @apiSuccess {date}       data.groupBillMonth                  结款时间(yyyy-mm)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式错误!
	 */
	@RequestMapping(value = "/bill/month", method = RequestMethod.GET)
	public ResultVo getGroupBillMonth() throws Exception {
		    ResultVo resultVo = new ResultVo();
			List<DataVo> dataVoList=gatheringService.getGroupBillMonth();
			resultVo.setData(dataVoList);
		    return resultVo;
	}

	/**
	 * @api {get} /api/gathering/groupName                         新增月结中获取缴款用户
	 * @apiName getGroupName
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯     新增月结中获取缴款用户
	 * <br/>
	 * @apiParam {date}        groupBillMonth                             借款时间 yyyy-mm
	 * @apiParam {int}        userId                                 用户Id
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		       新增成功返回值
	 * @apiSuccess {String}       data.groupName                      缴费集团
	 * @apiSuccess {String}       data.groupId                   		 缴费集团Id
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失!
	 * @apiError -888 请求方式错误!
	 */
	@RequestMapping(value = "/groupName", method = RequestMethod.GET)
	public ResultVo getGroupName(@RequestParam Map map) throws Exception {
		DataVo vo  =  new DataVo(map);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("groupBillMonth")&&vo.isNotBlank("userId")){
			List<DataVo> dataVoList=gatheringService.getGroupName(vo);
			resultVo.setData(dataVoList);
		}else {
			throw   new BizException(1700000,"借款时间");
		}
		return resultVo;
	}
	/**
	 * @api {get} /api/gathering/tractName                         新增月结中合约名称
	 * @apiName getConTractName
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯     新增月结中合约名称
	 * <br/>
	 * @apiParam {date}        groupBillMonth                               借款时间      yyyy-mm
	 * @apiParam {int}        groupId                                集团id
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		       新增成功返回值
	 * @apiSuccess {String}       data.contractId                      缴费集团
	 * @apiSuccess {String}       data.contractName                   		 缴费集团Id
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失!
	 * @apiError -888 请求方式错误!
	 */
	@RequestMapping(value = "/tractName", method = RequestMethod.GET)
	public ResultVo getConTractName(@RequestParam Map map) throws Exception {
		DataVo vo  =  new DataVo(map);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("groupBillMonth")&&vo.isNotBlank("groupId")){
			List<DataVo> dataVoList=gatheringService.getConTractName(vo);
			resultVo.setData(dataVoList);
		}else {
			throw   new BizException(1700000,"借款时间");
		}
		return resultVo;
	}
	/**
	 * @api {get} /api/gathering/money                         新增月结中收款金额
	 * @apiName getMoney
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯     新增月结中合约名称
	 * <br/>
	 * @apiParam {date}        groupBillMonth                               借款时间      yyyy-mm
	 * @apiParam {int}        groupId                                集团id
	 * @apiParam {int}        contractId                                合约id
	 * @apiParam {userId}        userId                                用户Id
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		       新增成功返回值
	 * @apiSuccess {String}       data.groupBillStatus                  结款状态为1 表示已结款
	 * @apiSuccess {String}       data.groupBillId                   		 结款id
	 * @apiSuccess {String}       data.groupBillAmount                   	 结款金额
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失!
	 * @apiError -888 请求方式错误!
	 */
	@RequestMapping(value = "/money", method = RequestMethod.GET)
	public ResultVo getMoney(@RequestParam Map map) throws Exception {
		DataVo vo  =  new DataVo(map);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("groupBillMonth")&&vo.isNotBlank("groupId")&&vo.isNotBlank("contractId")&&vo.isNotBlank("userId")){
			DataVo dataVoList=gatheringService.getYuejieMoney(vo);
			resultVo.setData(dataVoList);
		}else {
			throw   new BizException(1700000,"借款时间,集团Id,合约id");
		}
		return resultVo;
	}

    /**
     * @api {get} /api/gathering/exception/order/{billPayId}                        异常订单查询
     * @apiName exceptionOrder
     * @apiGroup GatheringController
     * @apiVersion 2.0.0
     * @apiDescription 易凯     异常订单查询
     * <br/>
     * @apiParam {Int}        billPayId                            订单Id
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {String}       data                    		       新增成功返回值
     * @apiSuccess {String}       billPayId                    		 订单Id
     * @apiSuccess {String}       billPayNo                    		       订单号
     * @apiSuccess {String}       startDate                    		       开始时间
     * @apiSuccess {String}       endDate                    		          结算时间
     * @apiSuccess {String}       chargeLength                    		       充电时长
     * @apiSuccess {String}       chgPower                    		       充电量
     * @apiSuccess {String}       preAmount                    		       充电前余额
     * @apiSuccess {String}       curAmount                    		       充电后余额
     * @apiSuccess {String}       amount                    		       消费金额
     * @apiSuccess {String}       cardNo                    		       车编号
     * @apiSuccess {String}       state                    		       状态
	 * @apiSuccess {String}       trmAddr                    		       桩地址
	 * @apiSuccess {String}       portNo                    		       编号
	 * @apiSuccess {String}       pileId                    		       桩Id
	 * @apiSuccess {String}       seqId                    		       记录Id
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1700000 参数缺失!
     * @apiError -888 请求方式错误!
     */
    @RequestMapping(value = "/exception/order/{billPayId}", method = RequestMethod.GET)
    public ResultVo exceptionOrder(@PathVariable("billPayId") int billPayId) throws Exception {
        ResultVo resultVo = new ResultVo();
		resultVo.setData(gatheringService.exceptionOrder(billPayId));
        return resultVo;
    }

	/**
	 * @api {Post} /api/gathering/exception/order                        异常订单结算
	 * @apiName exceptionBalance
	 * @apiGroup GatheringController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯     异常订单结算
	 * <br/>
	 * @apiParam {Int}          billPayId                            订单Id
	 * @apiParam {String}       billPayNo                   		       订单号
	 * @apiParam {String}       [startDate]                  		       开始时间
	 * @apiParam {String}       [endDate]                   		       结束时间
	 * @apiParam {String}       chargeLength                 		       充电时长
	 * @apiParam {String}       chgPower                  		       充电量
	 * @apiParam {String}       preAmount                   		       充电前余额
	 * @apiParam {String}       curAmount                   		       充电后余额
	 * @apiParam {String}       amount                   		       消费金额
	 * @apiParam {String}       cardNo                    		       车编号
	 * @apiParam {int}       state                    		       状态(5,有开始充电记录,充电时间小于10分钟不允许结单，9.不允许结单,没找到对应订单，-1允许结单,未知原因")
	 * @apiParam {String}       trmAddr                   		       桩地址
	 * @apiParam {String}       portNo                   		       编号
	 * @apiParam {String}       pileId                    		       桩Id
	 * @apiParam {String}       [seqId]                   		       记录Id
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                             成功返回值
	 * @apiSuccess {String}       data.result                             为true 成功 false失败
	 * @apiSuccess {String}       data.message                             失败原因


	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失!
	 * @apiError -888 请求方式错误!
	 */
	@RequestMapping(value = "/exception/order", method = RequestMethod.POST)
	public ResultVo exceptionBalance(@RequestBody Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo vo = new DataVo(map);
		String [] list = {"state","pileId","billPayId","trmAddr",
				"cardNo"};
		ChargeManageUtil.isMapNull(list,vo);
		resultVo.setData( gatheringService.exceptionBalance(vo));
		return resultVo;
	}
}





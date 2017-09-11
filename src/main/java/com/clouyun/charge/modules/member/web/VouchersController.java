package com.clouyun.charge.modules.member.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.VouchersService;
import com.github.pagehelper.PageInfo;


/**
 * 描述: 优惠券
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月17日 下午7:28:00
 */
@RestController
@RequestMapping("/api/vouchers")
public class VouchersController extends BusinessController{
	
	@Autowired
	VouchersService vouchersService;
	
	/*
     * @api {GET} /api/vouchers     查询优惠券列表
     * @apiName getVouchers 
     * @apiGroup VouchersController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   优惠券列表查询
     * <br/>
     * @apiParam {int} pageNum 	页码
     * @apiParam {int} pageSize 	页大小
     * @apiParam {String} [sort] 		排序字段(默认operatingTime,desc)
     * @apiParam {String} [order] 		排序方向
     * @apiParam {int} [vouchersName] 	优惠券名称
     * @apiParam {int} [orgName] 		运营商名称
     * <br/>
     * @apiSuccess {String} errorCode   				错误码
     * @apiSuccess {String} errorMsg    				消息说明
     * @apiSuccess {Object} data        				分页数据封装
     * @apiSuccess {int} data.total     				总记录数
     * @apiSuccess {Object[]} data.list 				分页数据对象数组
     * @apiSuccess {int} data.list.vid  				优惠券id
     * @apiSuccess {String} data.list.name		  		优惠券名称
     * @apiSuccess {String} data.list.strategy  		优惠策略(cash:现金, percentage:百分比')
     * @apiSuccess {datetime} data.list.endTime  		结束时间(yyyy-MM-dd)
     * @apiSuccess {datetime} data.list.operatingTime  	操作时间(yyyy-MM-dd)
     * @apiSuccess {int} data.list.startSerial  		开始编号
     * @apiSuccess {int} data.list.endSerial 			结束编号
     * @apiSuccess {int} data.list.cnumber 				优惠卷数量
     * @apiSuccess {String} data.list.access  			优惠卷获取方式 (zc:注册、fx:分享)
     * @apiSuccess {decimal} data.list.money  			优惠卷金额
     * @apiSuccess {String} data.list.note  			备注信息
     * @apiSuccess {int} data.list.userId  				用户id
     * @apiSuccess {String} data.list.userName  		用户名称
     * @apiSuccess {String} data.list.userPhone  		用户电话
     * @apiSuccess {int} data.list.orgId  				运营商id
     * @apiSuccess {String} data.list.orgName  			运营商名称
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "pageNum" : 1,
     *	    "pageSize" : 58,
     *	    "size" : 58,
     *	    "startRow" : 0,
     *	    "endRow" : 57,
     *	    "total" : 58,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "endSerial" : 700,
     *	        "cnumber" : 101,
     *	        "strategy" : "cash",
     *	        "orgName" : "深圳陆科",
     *	        "endTime" : "2017-04-19 23:59:59",
     *	        "access" : "zc",
     *	        "operatingTime" : "2017-04-17 14:20:31",
     *	        "orgId" : 24,
     *	        "userId" : 349,
     *	        "name" : "注册送0.5元",
     *	        "money" : 0.50,
     *	        "userName" : "xiaosh123",
     *	        "userPhone" : "1234555555554",
     *	        "startSerial" : 600,
     *	        "vid" : 72,
     *	        "note" : ""
     *	      },
     *		 ],
     *		    "prePage" : 0,
     *		    "nextPage" : 0,
     *		    "isFirstPage" : true,
     *		    "isLastPage" : true,
     *		    "hasPreviousPage" : false,
     *		    "hasNextPage" : false,
     *		    "navigatePages" : 8,
     *		    "navigatepageNums" : [
     *		      1
     *		    ],
     *		    "navigateFirstPage" : 1,
     *		    "navigateLastPage" : 1,
     *		    "firstPage" : 1,
     *		    "lastPage" : 1
     *		  }
     *		}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo getVouchers(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = vouchersService.queryVouchers(map);
		vo.setData(pageInfo);
		return vo;
	}
	
	/*
     * @api {GET} /api/vouchers/details     优惠券详情列表
     * @apiName queryVoucherDetails
     * @apiGroup VouchersController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟    优惠券详情展示
     * <br/>
     * @apiParam {int} vouchersId 	优惠券id
     * @apiParam {int} pageNum 	页码
     * @apiParam {int} pageSize 	页大小
     * @apiParam {String} [sort] 	排序字段
     * @apiParam {String} [order] 	排序方向
     * @apiParam {int} [serial] 	优惠券编号
     * @apiParam {int} [state] 		优惠券状态(未使用/已使用/已过期)
     * @apiParam {int} [consPhone] 	会员手机号
     * @apiParam {int} [beginTime] 	优惠券领取开始日期(yyyy-MM-dd HH:mm:ss)
     * @apiParam {int} [endTime] 	优惠券领取结束日期(yyyy-MM-dd HH:mm:ss)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 			分页数据对象数组
     * @apiSuccess {Date} data.list.receivetime 	领取日期(yyyy-MM-dd)     
     * @apiSuccess {String} data.list.state 		状态(未使用/已使用/已过期)
     * @apiSuccess {int(11)} data.list.serial 		编号
     * @apiSuccess {Date} data.list.endtime 		到期时间(yyyy-MM-dd)
     * @apiSuccess {String} data.list.name 			优惠券名称
     * @apiSuccess {decimal} data.list.money 		优惠券金额(元)
     * @apiSuccess {String} data.list.consName 		会员名称
     * @apiSuccess {String} data.list.consPhone 	会员电话
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "pageNum" : 1,
     *	    "pageSize" : 1,
     *	    "size" : 1,
     *	    "startRow" : 0,
     *	    "endRow" : 0,
     *	    "total" : 1,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "endtime" : "2017-04-19 23:59:59",
     *	        "receivetime" : "2017-04-17 14:21:10",
     *	        "name" : "注册送0.5元",
     *	        "money" : 0.50,
     *	        "state" : "已使用",
     *	        "consName" : "",
     *	        "consPhone" : "13332956123",
     *	        "serial" : 600
     *	      }
     *	    ],
     *	    "prePage" : 0,
     *	    "nextPage" : 0,
     *	    "isFirstPage" : true,
     *	    "isLastPage" : true,
     *	    "hasPreviousPage" : false,
     *	    "hasNextPage" : false,
     *	    "navigatePages" : 8,
     *	    "navigatepageNums" : [
     *	      1
     *	    ],
     *	    "navigateFirstPage" : 1,
     *	    "navigateLastPage" : 1,
     *	    "lastPage" : 1,
     *	    "firstPage" : 1
     *	  }
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常
     * @apiError -888 请求方式异常
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ResultVo queryVoucherDetails(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = vouchersService.queryVoucherDetails(map);
		vo.setData(pageInfo);
		return vo;
	}
	
	/*
	 
     * @api {POST} /api/vouchers   优惠券新增
     * @apiName insertVouchers
     * @apiGroup VouchersController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   优惠券新增
     * <br/>
     * @apiParam {String} access 		优惠券方式(zc:注册,fc:分享)
     * @apiParam {String} name 			优惠券名称
     * @apiParam {int} startSerial 		优惠券开始编号
     * @apiParam {int} endSerial 		优惠券结束编号
     * @apiParam {int} cNumber			优惠券张数
     * @apiParam {int} money 			优惠券面额
     * @apiParam {datetime} endTime 	有效期至(yyyy-MM-dd)
     * @apiParam {String} note 			备注
     * <br/>
     * @apiParamExample {json} 入参示例:
     *	{
     *	  	"access": "zc",
     *		"name": "测试保存优惠券",
     *		"startSerial": 88,
     *		"endSerial": 99,
     *		"money": 8.8,
     *		"endTime": "2017-04-20",
     *		"note": "测试保存信息"
     *	}
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        受影响数
     * <br/>
     * @apiError 1203001 	优惠券名称不能为空
     * @apiError 1203000 	在{优惠券名称}优惠券到期前无法添加相同类型的优惠券
     * @apiError -999 系统异常
     * @apiError -888 请求方式异常
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo insertVouchers(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int count = vouchersService.insertVouchers(map);
		vo.setData(count);
		return vo;
	}
}

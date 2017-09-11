package com.clouyun.charge.modules.member.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.CouponsNewService;
import com.clouyun.charge.modules.member.service.CouponsService;
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
@RequestMapping("/api/newcoupons")
public class CouponsNewController extends BusinessController{
	
	@Autowired
	CouponsNewService couponsService;
	
	/**
	 * 
     * @api {POST} /api/newcoupons     新增优惠券
     * @apiName insertCoupons
     * @apiGroup CouponsNewController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  新增优惠券
     * <br/>
     * @apiParam {int} userId 			登陆用户id
     * @apiParam {int} couponType 		优惠券类型(1:注册 2:分享 3:充值',type=yhqfs)
     * @apiParam {String} couponName 	优惠券名称
     * @apiParam {String} startSerial 	开始编号
     * @apiParam {String} endSerial 	结束编号
     * @apiParam {int} giveMoney 		面额(元)
     * @apiParam {dateTime} endTime 	有效期至
     * @apiParam {String} remark 		备注
     * <br/>
     * @apiParamExample {json} 入参示例:
     *	{
     *	  	"couponType": "2",
     *		"couponName": "测试保存优惠券",
     *		"startSerial": 88,
     *		"endSerial": 99,
     *		"giveMoney": 8.8,
     *		"endTime": "2017-04-20",
     *		"remark": "测试保存信息"
     *	}
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo insertCoupons(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int count = couponsService.insertCoupons(map);
		vo.setData(count);
		return vo;
	}
	
	
	/**
	 * 
     * @api {GET} /api/newcoupons     优惠券列表
     * @apiName queryCoupons
     * @apiGroup CouponsNewController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  优惠券列表
     * <br/>
     * @apiParam {int} pageNum 	页码
     * @apiParam {int} pageSize 	页大小
     * @apiParam {int} userId 		登陆用户id
     * @apiParam {String} [sort] 		排序字段(默认operatingTime,desc)
     * @apiParam {String} [order] 		排序方向
     * @apiParam {int} [couponNameByQuery] 	优惠券名称
     * @apiParam {int} [orgName] 		运营商名称
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {int} data.list.couponId  			优惠券id
     * @apiSuccess {String} data.list.couponName		优惠券名称
     * @apiSuccess {int} data.list.couponType  			优惠券发放类型 1:注册 2:分享 3:充值',type=yhqfs
     * @apiSuccess {int} data.list.startSerial  		开始编号
     * @apiSuccess {int} data.list.endSerial 			结束编号
     * @apiSuccess {datetime} data.list.startTime  		开始日期(yyyy-MM-dd)
     * @apiSuccess {datetime} data.list.endTime  		结束日期(yyyy-MM-dd)
     * @apiSuccess {datetime} data.list.couponTime  	添加时间(yyyy-MM-dd)
     * @apiSuccess {int} data.list.couponQty 			优惠卷数量
     * @apiSuccess {decimal} data.list.rechargMoney  	充值金额-充值才有值
     * @apiSuccess {decimal} data.list.giveMoney  		赠送金额
     * @apiSuccess {String} data.list.remark  			备注信息
     * @apiSuccess {int} data.list.userId  				用户id
     * @apiSuccess {int} data.list.status  				有效状态 1:无效 0:有效(type=zt)
     * @apiSuccess {String} data.list.userName  		用户名称
     * @apiSuccess {String} data.list.orgName  			运营商名称
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo queryCoupons(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo result = couponsService.queryCoupons(map);
		vo.setData(result);
		return vo;
	}
	
	/**
     * @api {GET} /api/newcoupons/info     优惠券详情列表
     * @apiName queryCouponsInfo
     * @apiGroup CouponsNewController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟    优惠券详情展示
     * <br/>
     * @apiParam {int} couponId 	优惠券id
     * @apiParam {int} pageNum 	页码
     * @apiParam {int} pageSize 	页大小
     * @apiParam {String} [sort] 	排序字段
     * @apiParam {String} [order] 	排序方向
     * @apiParam {int} [serialNo] 	优惠券编号
     * @apiParam {int} [status] 		优惠券状态(未使用/已使用/已过期)
     * @apiParam {int} [consPhone] 		会员手机号
     * @apiParam {int} [grantTimeBegin] 优惠券领取开始日期(yyyy-MM-dd HH:mm:ss)
     * @apiParam {int} [grantTimeEnd] 	优惠券领取结束日期(yyyy-MM-dd HH:mm:ss)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 			分页数据对象数组
     * @apiSuccess {Date} data.list.grantTime 		领取日期(yyyy-MM-dd HH:mm:ss)     
     * @apiSuccess {String} data.list.status 		状态(未使用/已使用/已过期,type:yhqzt)
     * @apiSuccess {int(11)} data.list.serialNo 	编号
     * @apiSuccess {Date} data.list.endTime 		到期时间(yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {String} data.list.couponName 	优惠券名称
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
     *	    "pageSize" : 4,
     *	    "size" : 4,
     *	    "startRow" : 0,
     *	    "endRow" : 3,
     *	    "total" : 4,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "grantTime" : "2017-01-13 11:01:44",
     *	        "couponName" : "哈哈",
     *	        "consId" : 62,
     *	        "status" : 2,
     *	        "consPhone" : "15182518840",
     *	        "consName" : "",
     *	        "endTime" : "2017-01-13 11:01:44",
     *	        "usedTime" : "",
     *	        "couponId" : 5,
     *	        "startTime" : "2016-10-22 13:54:04",
     *	        "serialNo" : 9000,
     *	        "billPayId" : "",
     *	        "money" : 11
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
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ResultVo queryCouponsInfo(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo result = couponsService.queryCouponsInfo(map);
		vo.setData(result);
		return vo;
	}
}

package com.clouyun.charge.modules.member.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.MemberService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述: 会员信息控制层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月14日 下午2:04:39
 */
@RestController
@RequestMapping("/api/members")
public class MemberController extends BusinessController{
	
	@Autowired
	MemberService memberService;
	
	/**
     * @api {GET} /api/members     查询会员列表
     * @apiName getMembers
     * @apiGroup MemberController
     * @apiVersion 1.4.0
     * @apiDescription 曹伟   查询会员列表
     * <br/>
     * @apiParam {int}    pageNum     	页码
     * @apiParam {int}    pageSize  		页大小
     * @apiParam {int}    userId 	 		登陆用户id
     * @apiParam {int}    [groupId]   		集团Id(集团双击获取会员信息时必填)
     * @apiParam {String} [sort] 			排序字段
     * @apiParam {String} [order] 			排序方向
     * @apiParam {String} [provice] 		省份
     * @apiParam {String} [city]  			城市
     * @apiParam {String} [memberName]  	会员名
     * @apiParam {String} [memberPhone] 	会员手机
     * @apiParam {String} [memberType]  	会员类型
     * @apiParam {String} [memberStatus]  	会员状态
     * @apiParam {String} [consFrom]  		会员来源
     * @apiParam {String} [licensePlate]  	车牌号
     * @apiParam {String} [rechargeCard]  	充值卡号
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {int} data.list.consId 				会员id(主键)
     * @apiSuccess {int} data.list.groupId 				集团id
     * @apiSuccess {int} data.list.acctId 				账户id
     * @apiSuccess {int} data.list.openId 				会员微信公平台用户id
     * @apiSuccess {int} data.list.carId 				会员车辆id
     * @apiSuccess {int} data.list.appFrom 				运营商id
     * @apiSuccess {int} data.list.gainPoint 			会员积分
     * @apiSuccess {String} data.list.consNo 			会员编号
     * @apiSuccess {String} data.list.consName 			会员名称
     * @apiSuccess {String} data.list.consTypeCode 		会员类型(01:个人会员,02:集团会员)
     * @apiSuccess {String} data.list.consTruename 		会员真实姓名
     * @apiSuccess {String} data.list.consIdnumber 		会员身份证号
     * @apiSuccess {String} data.list.consGender 		会员性别(01:男,02:女)
     * @apiSuccess {String} data.list.consBirthday 		会员生日
     * @apiSuccess {String} data.list.consTradeCode 	会员从事行业
     * @apiSuccess {String} data.list.consPhone 		会员手机号
     * @apiSuccess {String} data.list.consEmail 		会员邮箱
     * @apiSuccess {String} data.list.consAddr 			会员地址
     * @apiSuccess {String} data.list.wechatNo 			会员微信号
     * @apiSuccess {String} data.list.consFrom 			会员来源
     * @apiSuccess {String} data.list.locationArea 		会员所在区域
     * @apiSuccess {String} data.list.rechargeCard 		会员充值卡号
     * @apiSuccess {String} data.list.consStatus 		会员状态(0:有效,1:无效)
     * @apiSuccess {datetime} data.list.updateTime 		会员更新时间(yyyy-MM-dd)
     * @apiSuccess {String} data.list.groupName 		集团名称
     * @apiSuccess {String} data.list.acctNo 			账户编号
     * @apiSuccess {decimal} data.list.acctAmount 		账户总金额(钱包余额/元)
     * @apiSuccess {String} data.list.acctStateCode 	账户状态(01:正常,02:冻结,03:注销)
     * @apiSuccess {String} data.list.onnumber 			车辆自编号
     * @apiSuccess {String} data.list.manuFacturer 		车辆厂家
     * @apiSuccess {String} data.list.licensePlate 		车辆车牌
     * @apiSuccess {String} data.list.belongs 			车辆所有人(""or1、个人，2、集团')
     * @apiSuccess {String} data.list.orgName 			运营商名称
     * @apiSuccess {BigDecimal} data.list.rechargeAmount 	充值金额
     * @apiSuccess {BigDecimal} data.list.chargeNum 	充电次数
     * @apiSuccess {BigDecimal} data.list.consAmount 	消费金额
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	    "data" : {
     *	    "pageNum" : 1,
     *	    "pageSize" : 1069,
     *	    "size" : 1069,
     *	    "startRow" : 0,
     *	    "endRow" : 1068,
     *	    "total" : 1069,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "consTruename" : "gj",
     *	        "consBirthday" : "",
     *	        "groupName" : "",
     *	        "carId" : 1002827,
     *	        "acctStateCode" : "01",
     *	        "acctNo" : "",
     *	        "consStatus" : 0,
     *	        "cityCode" : "110100",
     *	        "onnumber" : "",
     *	        "gainPoint" : 100,
     *	        "consEmail" : "45548@qq.com",
     *	        "consNo" : "",
     *	        "rechargeCard" : "0000000000000001,0755201609270001,1,121211212121",
     *	        "distCode" : "",
     *	        "consTypeCode" : "01",
     *	        "appFrom" : 24,
     *	        "consId" : 62,
     *	        "licenseplate" : "川E88888",
     *	        "updateTime" : "2017-03-25",
     *	        "regTime" : "2017-03-11 17:13:39",
     *	        "acctId" : 83,
     *	        "belongs" : 1,
     *	        "consPhone" : "15182518840",
     *	        "consName" : "",
     *	        "orgName" : "深圳陆科",
     *	        "acctAmount" : 895.90,
     *	        "consAddr" : "erew32",
     *	        "consFrom" : "05",
     *	        "groupId" : "",
     *	        "wechatNo" : "2233",
     *	        "consTradeCode" : "",
     *	        "manufacturer" : "",
     *	        "consIdnumber" : "2255522",
     *	        "consGender" : "02",
     *	        "provCode" : "110000",
     *	        "openId" : ""
     *	      },
     *		 ],
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
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo getMembers(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = memberService.queryMembers(map);
		vo.setData(pageInfo);
		return vo;
	}

	/**
	 * @api {GET} /api/members/coupons     查询会员优惠券详情
	 * @apiName getMembersCoupons
	 * @apiGroup MemberController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   根据会员id查询会员优惠券详情
	 * <br/>
	 * @apiParam {int}    pageNum     		页码
	 * @apiParam {int}    pageSize  		页大小
	 * @apiParam {int}    consId 	 		会员id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {int} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {String} data.list.couponName	 	会员优惠券名称
	 * @apiSuccess {Int} data.list.money	 			会员优惠券面值
	 * @apiSuccess {Date} data.list.endTime	 		会员优惠券有效结束时间
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *		"errorCode": 0,
	 *		"errorMsg": "操作成功!",
	 *		"total": 0,
	 *		"data": {
	 *		"pageNum": 1,
	 *		"pageSize": 50,
	 *		"size": 1,
	 *		"startRow": 1,
	 *		"endRow": 1,
	 *		"total": 1,
	 *		"pages": 1,
	 *		"list": [
	 *		{
	 *			"couponName": "注册有惊喜",
	 *			"money": 10,
	 *			"endTime": "2017-08-31 23:59:59",
	 *		}
	 *		],
	 *		"prePage": 0,
	 *		"nextPage": 0,
	 *		"isFirstPage": true,
	 *		"isLastPage": true,
	 *		"hasPreviousPage": false,
	 *		"hasNextPage": false,
	 *		"navigatePages": 8,
	 *		"navigatepageNums": [
	 *		1
	 *		],
	 *		"navigateFirstPage": 1,
	 *		"navigateLastPage": 1,
	 *		"firstPage": 1,
	 *		"lastPage": 1
	 *		},
	 *		"exception": null
	 *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/coupons", method = RequestMethod.GET)
	public ResultVo getMembersCoupons(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = memberService.queryConsCoupons(map);
		vo.setData(pageInfo);
		return vo;
	}

	/**
     * @api {GET} /api/members/flowWaters  会员账户流水
     * @apiName queryConsFlow
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  会员账户流水详情(直接取值展示,无需查询字典表)
     * <br/>
     * @apiParam {int} consId	 		会员id
     * @apiParam {int} pageNum 		页码
     * @apiParam {int} pageSize 		页大小
     * @apiParam {String} [sort] 		排序字段(默认createTime,desc排序)
     * @apiParam {String} [order] 		排序方向
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {decimal} data.list.amount 		交易金额(元)
     * @apiSuccess {String} data.list.transType 	交易类型
     * @apiSuccess {String} data.list.payState 		支付状态
     * @apiSuccess {String} data.list.payType 		付款方式
     * @apiSuccess {String} data.list.billPayId 	订单号
     * @apiSuccess {datetime} data.list.createTime 	创建时间(yyyy-MM-dd HH:mm:ss)
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "pageNum" : 1,
     *	    "pageSize" : 11,
     *	    "size" : 11,
     *	    "startRow" : 0,
     *	    "endRow" : 10,
     *	    "total" : 11,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "createtime" : "2016-09-14 08:58:49",
     *	        "transtype" : "03",
     *	        "amount" : "+999999.0",
     *	        "payState" : "支付成功",
     *	        "transType" : "现金",
     *	        "paystate" : "",
     *	        "billPayId" : "e2c1e4b2502e47dd81b2bac42d888c5d",
     *	        "paytype" : "03",
     *	        "payType" : "卡充值",
     *	        "billpayid" : "e2c1e4b2502e47dd81b2bac42d888c5d"
     *	      },
     *		],
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
     * 	}
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/flowWaters", method = RequestMethod.GET)
	public ResultVo queryConsFlow(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = memberService.queryConsFlow(map);
		vo.setData(pageInfo);
		return vo;
	}
	
	/**
     * @api {POST} /api/members   会员新增
     * @apiName insertMembers
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  会员新增基础信息,账户信息由后台处理
     * <br/>
     * @apiParam {Int} userId 				登陆用户id
     * @apiParam {String} consName 			会员名称
     * @apiParam {String} consPhone 		会员电话号码
     * @apiParam {String} consTypeCode 		会员类型(01:个人会员,02:集团会员)
     * @apiParam {String} consStatus 		会员状态(type=zt)
     * @apiParam {int} appFrom 				运营商id
     * @apiParam {String} acctStateCode 	账户状态(01:正常,02:冻结,03:注销)
     * @apiParam {int} [groupId] 			集团id
     * @apiParam {String} [consGender] 		会员性别(01:男,02:女)
     * @apiParam {String} [consBirthday] 	会员生日
     * @apiParam {String} [consEmail] 		会员邮箱
     * @apiParam {String} [wechatNo] 		会员微信号
     * @apiParam {String} [consAddr] 		会员地址
     * @apiParam {String} [consReferrerId] 	会员推荐人
     * @apiParam {String} [provCode] 		省级区域编码
     * @apiParam {String} [cityCode] 		市级区域编号
	 * @apiParam {String} [consFrom] 		会员来源
	 * @apiParam {String} [regTime] 		会员注册时间
	 * @apiParam {Int}	  [testMember] 		测试会员(0:否,1:是...默认是否         type=cshy)
     * <br/>
     * @apiParamExample {json} 入参示例:
     *	{
     *		"consName":"会员名称",
     *		"consPhone":"110120",
     *		"consTypeCode":"02",
     *		"appFrom":24,
     *		"groupId":18,
     *		"consGender":"01",
     *		"consBirthday":"2017-01-01",
     *		"regTime":"2017-01-01 23:59:59",
     *		"consEmail":"110@qq.com",
     *		"wechatNo":"110",
     *		"consAddr":"科陆大厦",
     *		"consReferrerId":"推荐人",
     *		"acctStateCode":"01",
     *		"testMember":0
     *	}
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        新增受影响行数(0:未新增;1:新增一个)
     * <br/>
     * @apiError -1200000 会员名称/会员手机号码/会员类型不能为空
     * @apiError -1200001 请选择运营商
     * @apiError -1200002 保存失败,手机号码已存在
     * @apiError -1201000 账户更新时间/账户金额/账户状态不能为空
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo insertMembers(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		memberService.insertMembers(map);
		return vo;
	}
	
	/**
	 * @api {PUT} /api/members   会员更新
	 * @apiName updateMembers
	 * @apiGroup MemberController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟  会员更新
	 * <br/>
	 * @apiParam {int} consId 				会员id
	 * @apiParam {int} acctId 				账户id
     * @apiParam {String} consName 			会员名称
     * @apiParam {String} consPhone 		会员电话号码
     * @apiParam {String} consTypeCode 		会员类型(01:个人会员,02:集团会员)
     * @apiParam {int} appFrom 				运营商id
     * @apiParam {String} acctNo 			账户编号
     * @apiParam {String} acctStateCode 	账户状态(01:正常,02:冻结,03:注销)
     * @apiParam {int} [groupId] 			集团id
     * @apiParam {String} [consTruename]	会员真实名称
     * @apiParam {String} [consIdnumber] 	会员身份证号码
     * @apiParam {String} [consGender] 		会员性别(01:男,02:女)
     * @apiParam {String} [consBirthday] 	会员生日
     * @apiParam {String} [consEmail] 		会员邮箱
     * @apiParam {String} [wechatNo] 		会员微信号
     * @apiParam {String} [consAddr] 		会员地址
     * @apiParam {String} [consReferrerId] 	会员推荐人
     * @apiParam {String} [provCode] 		省级区域编码
     * @apiParam {String} [cityCode] 		市级区域编号
	 * @apiParam {String} [consFrom] 		会员来源
	 * @apiParam {Int}	  [testMember] 		测试会员(0:否,1:是...默认是否    type=cshy)
	 * @apiParam {Date}	  [regTime] 		注册时间
	 * @apiParam {Int}	  [consStatus] 		会员状态
     * <br/>
     * @apiParamExample {json} 入参示例:
     *	{
     *		"consId":0,
     *		"acctId":0,
     *		"consName":"会员名称",
     *		"consPhone":"110120",
     *		"consTypeCode":"02",
     *		"appFrom":24,
     *		"groupId":18,
     *		"consGender":"01",
     *		"consBirthday":"2017-01-01",
     *		"consEmail":"110@qq.com",
     *		"wechatNo":"110",
     *		"consAddr":"科陆大厦",
     *		"consReferrerId":"推荐人",
     *		"acctNo":"110120",
     *		"acctStateCode":"01",
     *		"testMember":0
     *	}
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        更新受影响行数(0:未新增;1:更新一个)
	 * <br/>
	 * @apiError -1200000 	会员名称/会员手机号码/会员类型不能为空
	 * @apiError -1200001 	请选择运营商
	 * @apiError -1200002 	更新失败,手机号码已存在
	 * @apiError -1201000 	账户更新时间/账户金额/账户状态不能为空
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResultVo updateMembers(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		Integer count = memberService.updateMembers(map);
		vo.setData(count);
		return vo;
	}
	
	/**
     * @api {GET} /api/members/{memberId}    查询会员信息
     * @apiName selectMemberByKey
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   查询会员信息
     * <br/>
     * @apiParam {int} memberId 会员id(必填)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.acctId       			账户id
     * @apiSuccess {int} data.consId       			会员id
     * @apiSuccess {String} data.consName 			会员名称
     * @apiSuccess {String} data.consPhone 			会员电话号码
     * @apiSuccess {String} data.consTypeCode 		会员类型(01:个人会员,02:集团会员)
     * @apiSuccess {int} data.appFrom 				运营商id
     * @apiSuccess {int} data.orgName 				运营商名称
     * @apiSuccess {String} data.acctNo 			账户编号
     * @apiSuccess {decimal} data.acctAmount 		账户金额(钱包余额)
     * @apiSuccess {String} data.rechargeCard 		充值卡号
     * @apiSuccess {int} data.groupId 				所属集团id
     * @apiSuccess {String} data.groupName			所属集团名称
     * @apiSuccess {String} data.consTruename 		会员真实名称
     * @apiSuccess {String} data.consIdnumber 		会员身份证号码
     * @apiSuccess {String} data.consGender 		会员性别(01:男,02:女)
     * @apiSuccess {String} data.consBirthday 		会员生日
     * @apiSuccess {String} data.consEmail 			会员邮箱
     * @apiSuccess {String} data.wechatNo 			会员微信号
     * @apiSuccess {String} data.consAddr 			会员地址
     * @apiSuccess {String} data.consReferrerId 	会员推荐人
     * @apiSuccess {String} data.provCode 			省级区域编码
     * @apiSuccess {String} data.cityCode 			市级区域编号
     * @apiSuccess {String} data.userName 			会员修改人员名称
     * @apiSuccess {Date} data.updateTime 			会员更新时间(yyyy-MM-dd)
     * @apiSuccess {Date} data.regTime 				会员注册时间(yyyy-MM-dd)
     * @apiSuccess {Int} data.testMember	 		测试会员(0:否,1:是   type=cshy)
     * @apiSuccess {Int} data.consStatus	 		会员状态(0:有效,1:无效   type=zt)
     * @apiSuccess {Int} data.gainPoint	 			会员积分
     * @apiSuccess {Int} data.couponCount	 		会员优惠券数量
     * @apiSuccess {Object[]} data.vehicleInfo		 		会员常用车辆信息
     * @apiSuccess {String} data.vehicleInfo.licensePlate	 会员常用车辆车牌
     * @apiSuccess {String} data.vehicleInfo.brandName	             会员常用车辆品牌
     * @apiSuccess {String} data.vehicleInfo.modelName	            会员常用车辆型号
     * @apiSuccess {String} data.vehicleInfo.vin	 		会员常用车辆车架号
     * @apiSuccess {int} data.vehicleInfo.belongsType	 	会员常用车辆所属类型(type=clsslx)
     * @apiSuccess {String} data.vehicleInfo.belongs	           会员常用车辆所有人名称
     * @apiSuccess {String} data.vehicleInfo.stationName	会员常用车辆所属场站
     * @apiSuccess {String} data.vehicleInfo.locationArea	会员常用车辆所在地
     * @apiSuccess {String} data.vehicleInfo.line	 		会员常用车辆线路
     * @apiSuccess {Int} data.vehicleInfo.usingRoperty	 	会员常用车辆使用性质(type=clsyxz)
     * @apiSuccess {Int} data.vehicleInfo.operationRoperty	会员常用车辆营运性质(0-是、1-否  type=clyyxz)
     * @apiSuccess {String} data.vehicleInfo.remark	 		会员常用车辆备注
     * @apiSuccess {String} data.vehicleInfo.drivingUrl	 	会员常用车辆行驶证URL
     * @apiSuccess {Object[]} data.cardInfo				 	会员储值卡账户信息
     * @apiSuccess {String} data.cardInfo.cardId			会员储值卡账户卡号
     * @apiSuccess {Date} data.cardInfo.actvTime			会员储值卡账户开卡时间
     * @apiSuccess {String} data.cardInfo.stationName		会员储值卡账户所属场站
     * @apiSuccess {String} data.cardInfo.brand	 			会员储值卡账户车辆品牌
     * @apiSuccess {String} data.cardInfo.licensePlate	 	会员储值卡账户车辆车牌
     * @apiSuccess {String} data.cardInfo.binding	 		会员储值卡账户车辆是否绑定
     * @apiSuccess {String} data.cardInfo.model	 			会员储值卡账户车辆型号
     * @apiSuccess {int} data.cardInfo.vehicleType	 		会员储值卡账户车辆类型(type=cllx)
     * @apiSuccess {Int} data.cardInfo.usingRoperty	 		会员储值卡账户车辆使用性质(type=clsyxz)
     * @apiSuccess {Int} data.cardInfo.operationRoperty		会员储值卡账户车辆营运性质(0-是、1-否  type=clyyxz)
     * @apiSuccess {Double} data.cardInfo.historyAmount		会员储值卡账户历史消费金额
	 * @apiSuccess {Object[]} data.acctInfo					会员app账户信息
	 * @apiSuccess {Date} data.acctInfo.createTime			会员app账户注册时间(yyyy-MM-dd)
	 * @apiSuccess {Double} data.acctInfo.acctStateCode		会员app账户状态(01:正常,02:冻结,03:注销  type=zhzt)
	 * @apiSuccess {Double} data.acctInfo.couponCount		会员app账户优惠券数量
	 * @apiSuccess {Double} data.acctInfo.gainPoint			会员app账户积分
     * @apiSuccess {Double} data.acctInfo.consFrom			会员app账户来源
     * @apiSuccess {Double} data.acctInfo.acctAmount		会员app账户钱包余额
	 * @apiSuccess {Double} data.acctInfo.appName			会员app账户名称(type=appName)
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
     *	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : [
     *	    {
     *	      "consTruename" : "刘波",
     *	      "createTime" : "2017-05-08 11:08:34",
     *	      "consBirthday" : "",
     *	      "carId" : 1002750,
     *	      "acctStateCode" : "01",
     *	      "acctNo" : "18589095656",
     *	      "consStatus" : 0,
     *	      "cityCode" : "110200",
     *	      "consEmail" : "123450211789@qq.com",
     *	      "userName" : "xiaosh123",
     *		  "acctInfo": {
     *			"createTime": "2017-05-08 11:08:34",
     *			"acctStateCode": "01",
     *			"acctNo": "18589095656",
     *			"couponCount": 0,
     *			"gainPoint": 100,
     *			"consFrom": "05",
     *			"acctAmount": 54.09
     *		  },
     *	      "consTypeCode" : "02",
     *	      "consId" : 63,
     *	      "appFrom" : 49,
     *	      "cardInfo" : [
     *	        {
     *	          "cardId" : "5656",
     *	          "model" : "",
     *	          "consId" : 63,
     *	          "vehicleId" : 10,
     *	          "actvTime" : "2016-09-05",
     *	          "usingRoperty" : 0,
     *	          "binding" : "是",
     *	          "vehicleType" : 1,
     *	          "brand" : "",
     *	          "operationRoperty" : 0,
     *	          "stationName" : "",
     *	          "licensePlate" : "粤B134481"
     *			  "historyAmount":34.77
     *	        }
     *	      ],
     *	      "updateTime" : "2017-05-08",
     *	      "regTime" : "2017-05-08 11:08:34",
     *	      "acctId" : 84,
     *	      "consModifierId" : 349,
     *	      "testMember" : 0,
     *	      "couponCount" : 0,
     *	      "gainPoint" : 100,
     *	      "consPhone" : "18589095656",
     *	      "consName" : "科陆_刘武",
     *	      "vehicleInfo" : {
     *		        "initMileage" : null,
     *		        "createTime" : "2017-05-15 14:09:26",
     *		        "model" : null,
     *		        "vehicleId" : 9,
     *		        "remark" : null,
     *		        "registerDate" : null,
     *	    	    "cityCode" : null,
     *		        "usingRoperty" : 0,
     *		        "stationId" : null,
     *		        "line" : null,
     *		        "locationArea" : "",
     *		        "operationRoperty" : 0,
     *		        "createBy" : 1,
     *		        "drivingUrl" : null,
     *		        "licensePlate" : "粤B134481",
     *		        "orgId" : null,
     *		        "belongsName" : null,
     *		        "belongsType" : 1,
     *		        "vin" : null,
     *		        "loadNo" : null,
     *		        "stationName" : null,
     *		        "updateBy" : null,
     *		        "mileage" : null,
     *		        "updateTime" : null,
     *		        "proviceCode" : null,
     *		        "orgName" : null,
     *		        "engineNo" : null,
     *		        "vehicleSize" : null,
     *		        "loadWeight" : null,
     *		        "color" : null,
     *		        "manufacturer" : null,
     *		        "totalWeight" : null,
     *				"curbWeight" : null,
     *		        "vehicleType" : 1,
     *		        "brand" : null,
     *		        "onNumber" : null
     *	      },
     *	      "acctAmount" : 54.09,
     *	      "consAddr" : "常驻地址",
     *	      "consFrom" : "05",
     *	      "groupId" : 18,
     *	      "wechatNo" : "23333",
     *	      "consIdnumber" : "42113111563489523",
     *	      "consGender" : "01",
     *	      "consReferrerId" : "更新推荐人",
     *	      "provCode" : "110000"
     *	    }
     *	  ]
     *	}
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/{memberId}", method = RequestMethod.GET)
	public ResultVo selectMemberByKey(@PathVariable("memberId") Integer memberId) throws Exception {
		ResultVo vo = new ResultVo();
		List<Map> result= memberService.selectMemberByKey(memberId);
		vo.setData(result);
		return vo;
	}
	
	/**
     * @api {GET} /api/members/_export  会员导出
     * @apiName exportMembers
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据查询条件导出数据
     * <br/>
     * @apiParam {int}    userId	     	登陆用户id
     * @apiParam {int}    [pageNum]     	页码
     * @apiParam {int}    [pageSize]  		页大小
     * @apiParam {int}    [groupId]   		集团Id(集团双击获取会员信息时必填)
     * @apiParam {String} [sort] 			排序字段
     * @apiParam {String} [order] 			排序方向
     * @apiParam {String} [provice] 		省份
     * @apiParam {String} [city]  			城市
     * @apiParam {String} [memberName]  	会员名
     * @apiParam {String} [memberPhone] 	会员手机
     * @apiParam {String} [memberType]  	会员类型
     * @apiParam {String} [memberStatus]  	会员状态
     * @apiParam {String} [consFrom]  		会员来源
     * @apiParam {String} [licensePlate]  	车牌号
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
    public ResultVo exportMembers(@RequestParam Map map,HttpServletResponse response) throws Exception {
		ResultVo vo = new ResultVo();
		memberService.export(map,response);
		return vo;
    }
	
	/**
	 * 
     * @api {POST} /api/members/_import  会员导入
     * @apiName importMembers
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据会员导入模版导入保存数据
     * <br/>
     * @apiParam {MultipartFile} file 	会员模版
     * @apiParam {Int} userId 			当前登陆用户id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} 	total    	 总记录数
     * @apiSuccess {String} data.result 		数据状态
     * @apiSuccess {String} data.groupNo 		集团编号
     * @apiSuccess {String} data.consName	 	会员名
     * @apiSuccess {String} data.consPhone 		手机号
     * @apiSuccess {String} data.consEmail 		E-Mail
     * @apiSuccess {String} data.consAddr 		地址
     * @apiSuccess {String} data.carModel 		车型
     * @apiSuccess {String} data.carLicense 	车牌号
     * @apiSuccess {String} data.orgName 		所属企业
     * @apiSuccess {String} data.check		 	数据校验结果
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_import", method = RequestMethod.POST)
	public ResultVo importMembers(@RequestParam("file") MultipartFile file,@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile excelFile=multipartRequest.getFile("file");
		List<Map> importMembers = memberService.importMembers(excelFile,map);
		vo.setData(importMembers);
		return vo;
	}
	
	/**
	 * 
     * @api {DELETE} /api/members/{ids}    集团会员置为无效
     * @apiName dissGroupMember
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  把集团会员置为无效,更改为个人 会员,groupId为-99,请求方式为/api/members/95,94
     * <br/>
     * @apiParam {Integer[]} ids 需要置为无效的会员id
     * @apiParam {Integer} userId 当前登陆用户id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1200003 请选择需要置为无效的集团会员
	 */
	@RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
	public ResultVo dissGroupMember(@PathVariable("ids") List<Integer> ids,@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		DataVo dataVo = new DataVo(map);
		int count = memberService.dissGroupMember(ids,dataVo.getInt("userId"));
		vo.setData(count);
		return vo;
	}
	
	/**
	 * 
     * @api {GET} /api/members/flowWaters/{billPayNo}   会员流水的详情
     * @apiName memberFlowInfo
     * @apiGroup MemberController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据订单编号查询会员流水的详情
     * <br/>
     * @apiParam {String} billPayNo 订单编号
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} 	total     总记录数
     * @apiSuccess {String} data.billPayNo 订单号
     * @apiSuccess {Int} data.amount 总金额(元)
     * @apiSuccess {Date} data.startTime 订单时间(yyyy-MM-dd)
     * @apiSuccess {Date} data.endTime 付款时间(yyyy-MM-dd)
     * @apiSuccess {Int} data.elceFee 电费(元)
     * @apiSuccess {Int} data.servFee 服务费(元)
     * @apiSuccess {String} data.payType 付款方式
     * @apiSuccess {String} data.stationName 场站名
     * @apiSuccess {String} data.pileType 充电桩类型
     * @apiSuccess {String} data.pileNo 设备编号
     * @apiSuccess {String} data.gumPoint 充电枪口
     * @apiSuccess {String} data.chargeTime 充电时长
     * @apiSuccess {Int} data.chgPower 充电度数(kWh)
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1200004 订单号不能为空
	 */
	@RequestMapping(value = "/flowWaters/{billPayNo}", method = RequestMethod.GET)
	public ResultVo memberFlowInfo(@PathVariable("billPayNo") String billPayNo) throws Exception {
		ResultVo vo = new ResultVo();
		Map map2 = memberService.queryMemberFlowInfo(billPayNo);
		vo.setData(map2);
		return vo;
	}
}

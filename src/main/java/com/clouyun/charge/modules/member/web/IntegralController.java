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
import com.clouyun.charge.modules.member.service.IntegralService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: 积分
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月21日 上午11:21:36
 */
@RestController
@RequestMapping("/api/integrals")
public class IntegralController extends BusinessController{
	
	@Autowired
	IntegralService integralService;
	
	/*
     * @api {GET} /api/integrals/history   会员积分详情
     * @apiName queryIntegralInfo
     * @apiGroup IntegralController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   根据会员id查询积分详情
     * <br/>
     * @apiParam {int} consId 			会员id(必传)
     * @apiParam {int} pageNum 		页码
     * @apiParam {int} pageSize 		页大小
     * @apiParam {String} [sort] 		排序字段
     * @apiParam {String} [order] 		排序方向(DESC:降序,ASC:升序)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {String} data.list.consName			用户名称
     * @apiSuccess {String} data.list.integralName 		积分类型(xf:消费,积分累计规则;zc:完成注册;fx:分享;crz:车认证;info:完善个人信息)
     * @apiSuccess {String} data.list.integralTime 		获取积分时间(yyyy-MM-dd)
     * @apiSuccess {String} data.list.integralNumber 	积分数量
     * @apiSuccess {String} data.list.consumerUse 		积分消费用途
     * @apiSuccess {String} data.list.note 				备注
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
     *	        "integralTime" : "2016-11-15 05:09:08",
     *	        "consName" : "熊岳",
     *	        "number" : 100,
     *	        "consumerUse" : "",
     *	        "integralName" : "车辆认证",
     *	        "note" : ""
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
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public ResultVo queryIntegralHistory(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = integralService.queryIntegralHistoryInfo(map);
		vo.setData(pageInfo);
		return vo;
	}
	
	/*
	 * 
     * @api {GET} /api/integrals/info    积分管理列表
     * @apiName queryIntegralInfo
     * @apiGroup IntegralController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  积分管理列表
     * <br/>
     * @apiParam {int} [orgId] 			运营商id
     * @apiParam {String} [orgName] 	运营商名称
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {boolean} data.isEdit 			是否需要修改,是否需要展示运营商列表,权限未加之前默认为true.
     * @apiSuccess {String} data.userName 			操作人.
     * @apiSuccess {int} data.orgId 				运营商id.
     * @apiSuccess {String} data.orgName 			运营商名称.
     * @apiSuccess {datetime} data.operatingTime 	更新时间.(yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {int} data.money					消费金额(适用于积分累计规则).
     * @apiSuccess {int} data.number 				积分
     * @apiSuccess {String} data.type 				类型(xf:消费,积分累计规则;zc:完成注册;fx:分享;crz:车认证;info:完善个人信息)
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
     * 	--------	根据类型获取信息展示	----------
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : [
     *	    {
     *	      "orgId" : 1,
     *	      "id" : 6,
     *	      "isEdit" : true,
     *	      "money" : 5,
     *	      "userName" : "xiaosh",
     *	      "number" : 10,
     *	      "orgName" : "testt",
     *	      "type" : "xf",
     *	      "operatingTime" : "2017-02-04 10:58:59"
     *	    },
     *	    {
     *	      "orgId" : 1,
     *	      "id" : 7,
     *	      "isEdit" : true,
     *	      "money" : "",
     *	      "userName" : "xiaosh",
     *	      "number" : 12,
     *	      "orgName" : "testt",
     *	      "type" : "zc",
     *	      "operatingTime" : "2017-03-27 17:10:37"
     *	    },
     *	    {
     *	      "orgId" : 1,
     *	      "id" : 8,
     *	      "isEdit" : true,
     *	      "money" : "",
     *	      "userName" : "admin",
     *	      "number" : 10,
     *	      "orgName" : "testt",
     *	      "type" : "fx",
     *	      "operatingTime" : "2016-11-22 13:52:21"
     *	    },
     *	    {
     *	      "orgId" : 1,
     *	      "id" : 9,
     *	      "isEdit" : true,
     *	      "money" : "",
     *	      "userName" : "admin",
     *	      "number" : 100,
     *	      "orgName" : "testt",
     *	      "type" : "crz",
     *	      "operatingTime" : "2016-10-31 14:00:29"
     *	    },
     *	    {
     *	      "orgId" : 1,
     *	      "id" : 10,
     *	      "isEdit" : true,
     *	      "money" : "",
     *	      "userName" : "admin",
     *	      "number" : 84,
     *	      "orgName" : "testt",
     *	      "type" : "info",
     *	      "operatingTime" : "2016-10-26 14:04:32"
     *	    },
     *	    {
     *	      "orgId" : 1,
     *	      "id" : 11,
     *	      "isEdit" : true,
     *	      "money" : 2,
     *	      "userName" : "admin",
     *	      "number" : 10,
     *	      "orgName" : "testt",
     *	      "type" : "xf",
     *	      "operatingTime" : "2016-11-30 09:09:49"
     *	    }
     *	  ]
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ResultVo queryIntegralInfo(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		List<Map> list = integralService.queryIntegralInfo(map);
		vo.setData(list);
		return vo;
	}
	
	/*
	 * 
     * @api {PUT} /api/integrals/info  更新积分
     * @apiName updateIntegral    
     * @apiGroup IntegralController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   更新积分
     * <br/>
     * @apiParam {String} type 	积分类型(xf:消费,积分累计规则;zc:完成注册;fx:分享;crz:车认证;info:完善个人信息)
     * @apiParam {int} number 	积分
     * @apiParam {int} [money] 	消费金额/单笔(修改积分累计规则时必填,修改单次获取规则不填)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {int} data        	受影响行数
     * <br/>
     * @apiParamExample {json} 入参示例:
     *	{
     *	  	"money":20,
     *	  	"number":10,
     *	  	"type":"xf"
     *	}
     * <br/>
     * @apiError -1202000 积分类型不能为空
     * @apiError -1202001 数据异常,保存失败,请联系管理员
	 */
	@RequestMapping(value = "/info", method = RequestMethod.PUT)
	public ResultVo updateIntegral(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int list = integralService.updateIntegral(map);
		vo.setData(list);
		return vo;
	}
}

package com.clouyun.charge.modules.member.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.PointNewService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * 描述: 积分
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月21日 上午11:21:36
 */
@RestController
@RequestMapping("/api/newPoints")
public class PointNewController extends BusinessController{
	
	@Autowired
	PointNewService pointService;
	
	/**
     * @api {GET} /api/newPoints   积分最新列表
     * @apiName queryPoints
     * @apiGroup PointNewController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟    重构后的最新积分结构(这一块有疑问的找我,数据结构主要是兼容老平台页面)
     * <br/>
     * @apiParam {int} userId 			登陆用户id
     * @apiParam {int} orgId 			运营商id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data      	 分页数据封装
     * @apiSuccess {int} 	total     	总记录数
     * @apiSuccess {Int} orgId 			运营商id
     * @apiSuccess {Int} userId 		操作人id
     * @apiSuccess {Int} exchangeMoney 	消费金额兑换积分
     * @apiSuccess {String} userName 	操作人名称
     * @apiSuccess {Int} pointId 		积分id
     * @apiSuccess {Int} gainPoint 		积分
     * @apiSuccess {String} orgName 	运营商名称
     * @apiSuccess {Date} timetag 		更新时间
     * @apiSuccess {Int} pointType 		积分类型( 1:消费 2:注册 3:分享 4:车认证 5:完善信息 6:兑换')
     * <br/>
     * @apiSuccessExample {json} Success出参实例:{
     * {
     * 
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : [
     *	    {
     *	      
     *	          "orgId" : 1,
     *	          "userId" : 1,
     *	          "exchangeMoney" : "",
     *	          "userName" : "admin",
     *	          "pointId" : 8,
     *	          "gainPoint" : 10,
     *	          "orgName" : "testt",
     *	          "timetag" : "2016-11-22 13:52:21",
     *	          "pointType" : 3
     *	        
     *		}
     *	  ]
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1202000 用户不能为空
     * @apiError -1202002 请选择运营商
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo queryPoints(@RequestParam Map map) throws BizException {
		ResultVo vo = new ResultVo();
		Map<Integer,Map> queryPubOrg = pointService.queryPoints(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	
	/**
	 * 
     * @api {GET} /api/newPoints/history   会员积分详情
     * @apiName queryHistoryInfo
     * @apiGroup PointNewController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  会员积分详情
     * <br/>
     * @apiParam {int} pageNum 		页码
     * @apiParam {int} pageSize 		页大小
     * @apiParam {int} consId 			会员id(必传)
     * @apiParam {String} [sort] 		排序字段
     * @apiParam {String} [order] 		排序方向
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 		分页数据对象数组
     * @apiSuccess {Date} data.list.gainTime 	获取时间(yyyy-MM-dd)
     * @apiSuccess {String} data.list.consName 	会员名称
     * @apiSuccess {Int} data.list.gainPoint 	积分
     * @apiSuccess {String} data.list.consumerUse 	消费用途(文本,直接展示)
     * @apiSuccess {String} data.list.remark 	备注
     * @apiSuccess {Int} data.list.pointType 	积分类型( 1:消费 2:注册 3:分享 4:车认证 5:完善信息 6:兑换,type=jfgz)
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     *	{
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
     *	        "cPointId" : 426,
     *	        "consId" : 63,
     *	        "billPayId" : "",
     *	        "pointId" : 37,
     *	        "gainTime" : "2017-01-05 18:08:27",
     *	        "consName" : "科陆_刘武",
     *	        "gainPoint" : 100,
     *	        "pointType" : 5,
     *	        "totalPoints" : ""
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
     *	    "firstPage" : 1,
     *	    "lastPage" : 1
     *	  }
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public ResultVo queryHistoryInfo(@RequestParam Map map) throws BizException {
		ResultVo vo = new ResultVo();
		PageInfo queryPubOrg = pointService.queryPointsHistoryInfo(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	
	
	/**
	 * 
     * @api {POST} /api/newPoints/saveOrUpdate   积分新增或编辑
     * @apiName updatePoints
     * @apiGroup PointNewController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  积分新增或者编辑
     * <br/>
     * @apiParam {int} gainPoint 		积分
     * @apiParam {int} pointType 		积分类型(type=jfgz)
	 * @apiParam {int} userId	 		登陆用户id
	 * @apiParam {int} [pointId] 		积分规则id(有是更新,没有是新增)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        受影响数量(0:未成功,1:成功)
     * @apiSuccess {int} 	total     总记录数
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public ResultVo updatePoints(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int queryPubOrg = pointService.updatePoints(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	
	/**
     * @api {GET} /api/newPoints/tree   积分运营商树
     * @apiName queryPointsTree
     * @apiGroup PointNewController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   积分的运营商树
     * <br/>
	 * @apiParam {int} userId 			登陆用户id
	 * @apiParam {String} orgName 		运营商名称(支持模糊查询)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data      	 分页数据封装
     * @apiSuccess {int} 	total     	总记录数
     * @apiSuccess {Int} orgId 			运营商id
     * @apiSuccess {String} orgName 	运营商名称
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public ResultVo queryPointsTree(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		Set<Map> queryPubOrg = pointService.queryPointsTree(map);
		vo.setData(queryPubOrg);
		return vo;
	}
	
}

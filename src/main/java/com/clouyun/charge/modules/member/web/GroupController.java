package com.clouyun.charge.modules.member.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.GroupService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 描述: 集团Controller
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月15日 上午10:42:05
 */
@RestController
@RequestMapping("/api/groups")
public class GroupController extends BusinessController{
	
	@Autowired
	GroupService groupService;
	
	/**
     * @api {GET} /api/groups   查询集团列表
     * @apiName getGroups
     * @apiGroup GroupController
     * @apiVersion 2.0.0
     * @apiDescription  曹伟   根据查询条件查询集团信息,默认权限下所有
     * <br/>
     * @apiParam {int}    pageNum		       页码
     * @apiParam {int}    pageSize        页大小
     * @apiParam {int}    userId          当前登陆用户id
     * @apiParam {String} [sort] 		       排序字段
     * @apiParam {String} [order]		       排序方向(DESC:降序,ASC:升序)
     * @apiParam {String} [groupName]     集团名称
	 * @apiParam {String} [groupNo]       集团编号
	 * @apiParam {String} [groupMan]      集团联系人
	 * @apiParam {String} [groupPhone]    集团联系人电话
	 * @apiParam {String} [groupStatus]   集团状态(0:有效,1:无效)
	 * @apiParam {String} [payType]       支付方式(01:集团统一支付,02:个人自行支付)
     * <br/>
     * @apiSuccess {String} errorCode   				错误码
     * @apiSuccess {String} errorMsg   					消息说明
     * @apiSuccess {Object} data        				分页数据封装
     * @apiSuccess {int} data.total     				总记录数
     * @apiSuccess {Object[]} data.list 				分页数据对象数组
     * @apiSuccess {int} data.list.groupId 				集团id(主键)
     * @apiSuccess {int} data.list.acctId 				账户id
     * @apiSuccess {int} data.list.appFrom 				运营商id
     * @apiSuccess {String} data.list.groupNo 			集团编号
     * @apiSuccess {String} data.list.groupName 		集团名称
     * @apiSuccess {String} data.list.groupAddr 		集团地址
     * @apiSuccess {String} data.list.attentionName 	集团联系人名称
     * @apiSuccess {String} data.list.attentionPhone 	集团联系人电话
     * @apiSuccess {String} data.list.attentionEmail 	集团联系人邮箱
     * @apiSuccess {String} data.list.payModel 			支付方式(01:集团统一支付,02:个人自行支付)
     * @apiSuccess {String} data.list.groupStatus 		集团状态(0:有效,1:无效)
     * @apiSuccess {String} data.list.acctNo			账户编号
     * @apiSuccess {String} data.list.acctStateCode 	账户状态(01正常/02冻结/03注销)
     * @apiSuccess {decimal} data.list.acctAmount 		账户余额(元)
     * @apiSuccess {decimal} data.list.acctCashLimit 	账户提现额度
     * @apiSuccess {datetime} data.list.updateTime 		账户更新时间(yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {datetime} data.list.createTime 		账户创建时间(yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {String} data.list.remark 			集团团体人数
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "pageNum" : 1,
     *	    "pageSize" : 64,
     *	    "size" : 64,
     *	    "startRow" : 0,
     *	    "endRow" : 63,
     *	    "total" : 64,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "createTime" : "2016-11-09 14:26:18",
     *	        "groupName" : "科陆集团",
     *	        "acctStateCode" : "01",
     *	        "appFrom" : 49,
     *	        "acctNo" : "075521325478",
     *	        "acctCashLimit" : "",
     *	        "updateTime" : "2016-11-09 14:26:18",
     *	        "groupStatus" : "0",
     *	        "attentionEmail" : "szclou@szclou.com",
     *	        "remark" : "21",
     *	        "acctId" : 85,
     *	        "payModel" : "01",
     *	        "acctAmount" : 10078.18,
     *	        "groupNo" : "0001",
     *	        "groupId" : 18,
     *	        "attentionName" : "tester01",
     *	        "attentionPhone" : "075521325478",
     *	        "groupAddr" : "广东省深圳市南山区科技园北区科陆大厦A座"
     *	      }
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
     * @apiError -999 系统异常
     * @apiError -888 请求方式异常
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo getGroups(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = groupService.queryGroups(map);
		vo.setData(pageInfo);
		return vo;
	}
	
	/**
     * @api {GET} /api/groups/{groupId}   查询集团信息
     * @apiName getGroupByKey
     * @apiGroup GroupController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   根据集团id查询集团信息和账户信息
     * <br/>
     * @apiParam {int} groupId 集团id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.groupId 				集团id(主键)
     * @apiSuccess {int} data.acctId 				账户id(主键)
     * @apiSuccess {int} data.appFrom 				运营商id
     * @apiSuccess {String} data.orgName 			运营商名称
     * @apiSuccess {String} data.groupNo 			集团编号
     * @apiSuccess {String} data.groupName 			集团名称
     * @apiSuccess {String} data.groupAddr 			集团地址
     * @apiSuccess {String} data.attentionName 		集团联系人名称
     * @apiSuccess {String} data.attentionPhone 	集团联系人电话
     * @apiSuccess {String} data.attentionEmail 	集团联系人邮箱
     * @apiSuccess {String} data.payModel 			支付方式(01:集团统一支付,02:个人自行支付)
     * @apiSuccess {String} data.groupStatus 		集团状态(0:有效,1:无效)
     * @apiSuccess {String} data.acctNo			 	账户编号
     * @apiSuccess {String} data.acctStateCode 		账户状态(01正常/02冻结/03注销)
     * @apiSuccess {decimal} data.acctAmount 		账户余额(元)
     * @apiSuccess {decimal} data.acctCashLimit 	账户提现额度
     * @apiSuccess {datetime} data.updateTime 		账户更新时间(yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {datetime} data.createTime 		账户创建时间(yyyy-MM-dd HH:mm:ss)
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "createTime" : "2016-08-20 18:00:02",
     *	    "groupName" : "123123",
     *	    "acctStateCode" : "01",
     *	    "appFrom" : "",
     *	    "acctNo" : "",
     *	    "acctCashLimit" : "",
     *	    "updateTime" : "2016-08-20 18:00:02",
     *	    "groupStatus" : "0",
     *	    "attentionEmail" : "",
     *	    "remark" : "",
     *	    "acctId" : 144,
     *	    "payModel" : "01",
     *	    "acctAmount" : 0.00,
     *	    "groupNo" : "123",
     *	    "groupId" : 24,
     *	    "attentionName" : "123",
     *	    "attentionPhone" : "",
     *	    "groupAddr" : "",
     *		"orgName":"高路通"
     *	  }
     *	}
     * }
     * @apiError -999 系统异常
     * @apiError -888 请求方式异常
	 */
	@RequestMapping(value = "/{groupId}", method = RequestMethod.GET)
	public ResultVo getGroupByKey(@PathVariable("groupId") Integer groupId) throws Exception {
		ResultVo vo = new ResultVo();
		Map group = groupService.queryGroupByKey(groupId);
		vo.setData(group);
		return vo;
	}
	
	/**
     * @api {POST} /api/groups 集团新增
     * @apiName insertGroup
     * @apiGroup GroupController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟    新增集团,新增账户
     * <br/>
     * @apiParam {String} groupName        集团名称
     * @apiParam {String} groupNo          集团编号
     * @apiParam {String} attentionName    集团联系人名称
     * @apiParam {String} payModel         支付方式(01:集团统一支付,02:个人自行支付)
     * @apiParam {int} 	  appFrom          运营商
     * @apiParam {String} [groupAddr]        集团地址
     * @apiParam {String} [attentionPhone]   集团联系人电话
     * @apiParam {String} [attentionEmail]	   集团联系人邮箱
     * @apiParam {String} [acctNo]			   账户编号
     * @apiParam {String} [acctAmount]       账户余额(元)
     * @apiParam {String} [acctStateCode]    账户状态(01:正常,02:冻结,03:注销)
     * @apiParam {datetime} [createTime]     账户创建时间(yyyy-MM-dd HH:mm:ss)
     * @apiParam {datetime} [updateTime]     账户更新时间(yyyy-MM-dd HH:mm:ss)
     * <br/>
     * @apiParamExample {json} 入参示例:
     *	{
     *	  	"acctNo": "123",
     *		"acctAmount": 123,
     *		"acctStateCode": "01",
     *		"createTime": "2017-04-28",
     *		"updateTime": "2017-04-28",
     *		"groupName": "123124",
     *		"groupNo": "123124",
     *		"groupAddr": "123124",
     *		"attentionName": "123124",
     *		"attentionPhone": "123124",
     *		"attentionEmail": "123124",
     *		"payModel": "01",
     *		"appFrom": "18",
     *		"groupStatus":"1"
     *	}
     * <br/>
     * @apiSuccess {String} errorCode      错误码
     * @apiSuccess {String} errorMsg       消息说明
     * @apiSuccess {int}    data           受影响行数
     * <br/>
     * @apiError -1201000   集团名称不能为空/集团编号不能为空/集团联系人不能为空
     * @apiError -1201001   集团名称已存在
     * @apiError -999 系统异常
     * @apiError -888 请求方式异常
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo insertGroup(@RequestBody Map map)throws BizException{
		ResultVo vo = new ResultVo();
		int count = 0;
		count = groupService.insertGroup(map);
		vo.setData(count);
		return vo;
	}
	
	/**
     * @api {PUT} /api/groups 更新集团
     * @apiName updateGroup
     * @apiGroup GroupController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   更新集团,新增账户
     * <br/>
     * @apiParam {int}    groupId  		          集团id(隐藏域必传)
     * @apiParam {int} 	  acctId       	          账户id(隐藏域必传)
     * @apiParam {String} groupName        集团名称
     * @apiParam {String} groupNo          集团编号
     * @apiParam {String} attentionName    集团联系人名称
     * @apiParam {String} payModel         支付方式(01:集团统一支付,02:个人自行支付)
     * @apiParam {int} 	  appFrom          运营商
     * @apiParam {String} [groupAddr]        集团地址
     * @apiParam {String} [attentionPhone]   集团联系人电话
     * @apiParam {String} [attentionEmail]	   集团联系人邮箱
     * @apiParam {String} [acctNo]			   账户编号
     * @apiParam {String} [acctAmount]       账户余额(元)
     * @apiParam {String} [acctStateCode]    账户状态(01:正常,02:冻结,03:注销)
     * @apiParam {datetime} [createTime]     账户创建时间(yyyy-MM-dd HH:mm:ss)
     * @apiParam {datetime} [updateTime]     账户更新时间(yyyy-MM-dd HH:mm:ss)
     * <br/>
     * @apiParamExample {json} 入参示例:
     *	{
     *	  	"acctNo": "123",
     *		"acctAmount": 123,
     *		"acctStateCode": "01",
     *		"createTime": "2017-04-28",
     *		"updateTime": "2017-04-28",
     *		"groupName": "123124",
     *		"groupNo": "123124",
     *		"groupAddr": "123124",
     *		"attentionName": "123124",
     *		"attentionPhone": "123124",
     *		"attentionEmail": "123124",
     *		"payModel": "01",
     *		"appFrom": "18",
     *		"groupStatus":"1"
     *	}
     * <br/>
     * @apiSuccess {String} errorCode      错误码
     * @apiSuccess {String} errorMsg       消息说明
     * @apiSuccess {int}    data           受影响行数
     * <br/>
     * @apiError -1201000   集团名称不能为空/集团编号不能为空/集团联系人不能为空
     * @apiError -1201001   集团名称已存在
     * @apiError -999 系统异常
     * @apiError -888 请求方式异常
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResultVo updateGroup(@RequestBody Map map)throws Exception{
		ResultVo vo = new ResultVo();
		int count = 0;
		count = groupService.updateGroup(map);
		vo.setData(count);
		return vo;
	}
	
	/**
     * @api {GET} /api/groups/_export  集团导出
     * @apiName exportGroup
     * @apiGroup GroupController	
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据查询条件导出excel数据
     * <br/>
     * @apiParam {int}    [pageNum]     页码
     * @apiParam {int}    [pageSize]      页大小
     * @apiParam {String} [sort] 		       排序字段
     * @apiParam {String} [order]		       排序方向(DESC:降序,ASC:升序)
     * @apiParam {String} [groupName]     集团名称
	 * @apiParam {String} [groupNo]       集团编号
	 * @apiParam {String} [groupMan]      集团联系人
	 * @apiParam {String} [groupPhone]    集团联系人电话
	 * @apiParam {String} [groupStatus]   集团状态(0:有效,1:无效)
	 * @apiParam {String} [payType]       支付方式(01:集团统一支付,02:个人自行支付)
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
    public void exportGroup(@RequestParam Map map,HttpServletResponse response) throws Exception {
		groupService.export(map,response);
    }
	
	
	/**
     * @api {GET} /api/groups/dicts  集团业务字典
     * @apiName groupDicts
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  集团业务字典
     * <br/>
     * @apiParam {int} 		  userId   登陆用户id
	 * @apiParam {String}     [groupName] 	集团名称
	 * @apiParam {Integer}    [orgId]    	运营商Id
	 * @apiParam {Integer}    [groupId]		集团Id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} 	total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {Integer} data.list.groupId 集团Id
	 * @apiSuccess {String} data.list.groupName  集团名称
	 * @apiSuccessExample {json} Success出参示例:{
     *	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : [
     *	    {
     *	      "groupId" : 18,
     *	      "groupName" : "科陆集团"
     *	    }
     *	   ]
     *	}
     *}
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/dicts", method = RequestMethod.GET)
	public ResultVo groupDicts(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		List groupDicts = groupService.queryGroupDicts(map);
		//兼容分页的字典和不分页的字典
		vo.setData(new PageInfo(groupDicts));
		return vo;
	}
	
	
}

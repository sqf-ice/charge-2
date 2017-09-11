package com.clouyun.charge.modules.system.web;

import com.clou.common.utils.JSONUtils;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.system.service.OrgRegisterServiceImpl;
import com.clouyun.charge.modules.system.service.OrgService;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clouyun.charge.common.utils.CommonUtils.gerParamterMap;


/**
 * 描述: 企业注册管理控制器 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: lips <lips@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年1月3日
 */
@RestController
@RequestMapping("/api/org")
public class OrgController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(OrgController.class);
	
	@Autowired OrgRegisterServiceImpl orgRegisterService;

	@Autowired
	private OrgService orgService;


	/**
	 * @api {POST} /api/org/register 企业用户注册
	 * @apiName validateCode
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription  易凯  企业用户注册
	 * <br/>
	 * @apiParam {String}    loginName          用户名称
	 * @apiParam {String}    email    邮箱
	 * @apiParam {String}    password    密码
	 * @apiParam {String}    rePassword    2次密码
	 * @apiParam {String}    namePy     企业logo
	 * @apiParam {String}    orgName     企业名称
	 * @apiParam {String}    [orgAbbr]     企业简称
	 * @apiParam {String}    orgCode     组织机构代码
	 * @apiParam {String}    [orgFax]     企业传真
	 * @apiParam {String}    orgPhone     企业电话
	 * @apiParam {String}    orgEmail     企业邮箱
	 * @apiParam {int}    provCode     省份
	 * @apiParam {int}    cityCode     城市
	 * @apiParam {int}    distCode        地区
	 * @apiParam {String}    address     企业详细地址
	 * @apiParam {String}    remark     经营范围
	 * @apiParam {String}    businessLicence     营业执照
	 * @apiParam {String}    orgHead     负责人
	 * @apiParam {String}    memberPhone     联系电话
	 * @apiParam {Object[]}    station     场站业务
	 * @apiParam {int}    station.provCode     场站省份
	 * @apiParam {int}    station.cityCode      场站省份城市
	 * @apiParam {int}    station.distCode         场站省份地区
	 * @apiParam {String}    station.address     场站详细地址
	 * @apiParam {String}    station.stationModel     合作模式
	 * @apiParam {String}    station.stationType     场站类型( 字典 jingylx)
	 * @apiParam {String}    station.place    建设场所
	 * @apiParam {String}    station.jiaoLiu     交流充电桩个数
	 * @apiParam {String}    station.jiaoLiuP     交流功率
	 * @apiParam {String}    station.zhiLiu     直流充电桩
	 * @apiParam {String}    station.zhiLiuP     直流功率
	 * @apiParam {String}    [station.remark]     备注
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失!
	 * @apiError -1000014 参数格式不对!
	 * @apiError -1000018 密码不一致!
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResultVo register(@RequestBody Map data) throws Exception {
		DataVo vo  = new DataVo(data);
		String[] list1 = {"loginName","email","password","rePassword","namePy",
				"orgName","orgCode"
				,"orgPhone","orgEmail","provCode","cityCode",
				"distCode","address"
				,"remark","businessLicence","orgHead","memberPhone","station"};
		String[] list2 = {"provCode","cityCode",
				"distCode","address","stationModel","stationType","place","jiaoLiu",
				"jiaoLiuP","zhiLiu","zhiLiuP"};
		Map map = new HashMap();
		map.put("loginName","用户名称");
		map.put("email","邮箱");
		map.put("password","密码");
		map.put("rePassword","2次密码");
		map.put("namePy","企业logo");
		map.put("orgCode","组织机构代码");
		map.put("orgPhone","企业电话");
		map.put("orgEmail","企业邮箱");
		map.put("provCode","省份");
		map.put("cityCode","城市");
		map.put("distCode","地区");
		map.put("address","企业详细地址");
		map.put("remark","经营范围");
		map.put("businessLicence","营业执照");
		map.put("orgHead","负责人");
		map.put("memberPhone","联系电话");
		map.put("station","场站");
		ChargeManageUtil.isMapNullPoint(list1,vo,map);
	List<Map> lists= (List) JSONUtils.deserialize(vo.getString("station"),List.class);
		Map map1 = new HashMap();
		map1.put("provCode","场站省份");
		map1.put("cityCode","场站省份城市");
		map1.put("distCode","场站省份地区");
		map1.put("address","场站详细地址");
		map1.put("stationModel","合作模式");
		map1.put("stationType","场站类型");
		map1.put("place","建设场所");
		map1.put("jiaoLiu","交流充电桩个数");
		map1.put("jiaoLiuP","交流功率");
		map1.put("zhiLiu","直流充电桩");
		map1.put("zhiLiuP","直流功率");
	for (Map list :lists){
		ChargeManageUtil.isMapNullPoint(list2,new DataVo(list),map1);
	}
		orgRegisterService.register(vo);
		return new ResultVo();
	}

	
	
	/**
	 * 根据userId查询企业管理员信息 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/pubUser/query/userId", method = RequestMethod.POST)
	public 	ResultVo selectPubUserByuserId(@RequestBody DataVo data){
		DataVo dv=orgRegisterService.selectPubUserByuserId(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(dv);
		return resultVo;
	}
	
	/**
	 * 根据orgId查询企业信息 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/pubOrg/query/orgId", method = RequestMethod.POST)
	public 	ResultVo selectPubOrgByOrgId(@RequestBody DataVo data){
		DataVo dv=orgRegisterService.selectPubOrgByOrgId(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(dv);
		return resultVo;
	}
	
	
	
	
	/**
	 * 根据userId更新企业管理员信息  
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/pubUser/update/userId", method = RequestMethod.POST)
	public 	ResultVo updatePubUserByuserId(@RequestBody DataVo data){
		int resultCode=orgRegisterService.updatePubUserByuserId(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(resultCode);
		return resultVo;
	}
	
	/**
	 * 判断该用户名是否存在
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/pubUser/query/userName", method = RequestMethod.POST)
	public 	ResultVo selectPubUserByuserName(@RequestBody DataVo data){
		List<DataVo> dvlist=orgRegisterService.selectPubUserByuserName(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(dvlist);
		return resultVo;
	}
	/**
	 * 新增场站信息
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/chgStation", method = RequestMethod.POST)
	public 	ResultVo insertChgStation(@RequestBody DataVo data){
		int  resultCode=orgRegisterService.insertChgStation(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(data.get("stationId"));
		return resultVo;
	}
	
	/**
	 * 新增场站的充电桩类型信息
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/chgPileType", method = RequestMethod.POST)
	public 	ResultVo insertChgPileType(@RequestBody DataVo data){
		int  resultCode=orgRegisterService.insertChgPileType(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(resultCode);
		return resultVo;
	}
	
	
//	/**
//	 * 查询场站的充电桩类型信息
//	 * @param data
//	 * @return
//	 */
//	@RequestMapping(value = "/chgPileType/query", method = RequestMethod.POST)
//	public 	ResultVo selectChgPileType(@RequestBody DataVo data){
//		List<DataVo> dvlist=orgRegisterService.selectChgPileType(data);
//		ResultVo resultVo = new ResultVo();
//		resultVo.setData(dvlist);
//		return resultVo;
//	}
	
  	
	/**
	 * 根据orgId更新企业信息  
	 * @param data
	 * @return
	 */
  	@RequestMapping(value = "/pubOrg/update/orgId", method = RequestMethod.POST)
  	public 	ResultVo updatePubOrgByOrgId(@RequestBody DataVo data){
 		int resultCode=orgRegisterService.updatePubOrgByOrgId(data);

  		ResultVo resultVo = new ResultVo();
  		resultVo.setData(resultCode);
  		return resultVo;
  	}
  	
  	
  	
  	
//  	/**
//  	 * 根据地区编码查询
//  	 * @param data
//  	 * @return
//  	 */
//  	@RequestMapping(value = "/pubOrg/query/distCode", method = RequestMethod.POST)
//  	public 	ResultVo selectPubOrgByDistCode(@RequestBody DataVo data){
//  		List<DataVo> dvlist=orgRegisterService.selectPubOrgByDistCode(data);
//  		Integer sizeDistCode=0;
//  		if(dvlist!=null && dvlist.size()>0){
//  			 sizeDistCode=dvlist.size();
//  		}
//  		ResultVo resultVo = new ResultVo();
//  		resultVo.setData(sizeDistCode.toString());
//  		return resultVo;
//  	}
  	
  	
  	/**
  	 * 企业审核查询 
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/pubOrgAudit/query", method = RequestMethod.POST)
  	public 	ResultVo selectPubOrgAudit(@RequestBody DataVo data){
  		List<DataVo> dvlist=orgRegisterService.selectPubOrgAudit(data);
  		ResultVo resultVo = new ResultVo();
  		resultVo.setData(dvlist);
  		return resultVo;
  	}
  	
  	/**
  	 * 企业审核统计
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/pubOrgAudit/count", method = RequestMethod.POST)
  	public 	ResultVo countAudit(@RequestBody DataVo data){
  		Integer count=orgRegisterService.countAudit(data);
  		ResultVo resultVo = new ResultVo();
  		resultVo.setData(count.toString());
  		return resultVo;
  	}
  	
  	
  	/**
  	 * 企业未审核,审核通过，审核不通过的详细信息查询
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/pubOrgAudit/detail", method = RequestMethod.POST)
  	public 	ResultVo detailAudit(@RequestBody DataVo data){
  		List<DataVo> dvlist=orgRegisterService.detailAudit(data);
  		ResultVo resultVo = new ResultVo();
  		resultVo.setData(dvlist);
  		return resultVo;
  	}
  	
  	/**
  	 * 查询企业场站信息
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/auditChg/query", method = RequestMethod.POST)
  	public 	ResultVo detailAuditChg(@RequestBody DataVo data){
  		List<DataVo> dvlist=orgRegisterService.detailAuditChg(data);
  		ResultVo resultVo = new ResultVo();
  		resultVo.setData(dvlist);
  		return resultVo;
  	}
 
  	/**
  	 * 获取企业审核的审核人列表，查询企业表 
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/auditMember/query", method = RequestMethod.POST)
  	public 	ResultVo queryAuditMember(@RequestBody DataVo data){
  		List<DataVo> dvlist=orgRegisterService.queryAuditMember(data);
  		ResultVo resultVo = new ResultVo();
  		resultVo.setData(dvlist);
  		return resultVo;
  	}
   	
  	/**
  	 * 新增模板信息
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/pubPrice", method = RequestMethod.POST)
  	public 	ResultVo insertPubPrice(@RequestBody DataVo data){
  		int  resultCode=orgRegisterService.insertPubPrice(data);
  		ResultVo resultVo = new ResultVo();
  		resultVo.setData(data.get("prcId").toString());
  		return resultVo;
  	}
  	
  	/**
  	 * 新增费率对应的时间段 
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/modelTime", method = RequestMethod.POST)
  	public 	ResultVo insertModelTime(@RequestBody DataVo data){
  		int  resultCode=orgRegisterService.insertModelTime(data);
  		ResultVo resultVo = new ResultVo();
  		return resultVo;
  	}
  	
    /**
     *  给企业管理员分配角色
     * @param data
     * @return
     */
  	@RequestMapping(value = "/userRole", method = RequestMethod.POST)
  	public 	ResultVo insertUserRole(@RequestBody DataVo data){
  		int  resultCode=orgRegisterService.insertUserRole(data);
  		ResultVo resultVo = new ResultVo();
  		return resultVo;
  	}
  	
  	/**
  	 * 插入用户，企业，场站表
  	 * @param data
  	 * @return
  	 */
  	@RequestMapping(value = "/pubUserOrgStation", method = RequestMethod.POST)
  	public 	ResultVo insertPubUserOrgStation(@RequestBody DataVo data){
  		int  resultCode=orgRegisterService.insertPubUserOrgStation(data);
  		ResultVo resultVo = new ResultVo();
  		return resultVo;
  	}


  	/**
  	 * @api {GET} /api/org/{userId}/orgs 根据用户ID获取可查看企业
  	 * @apiName  getOrgNameByUserId
  	 * @apiGroup OrgController
  	 * @apiVersion 2.0.0
  	 * @apiDescription 根据用户获取可查看的企业列表组成下拉框
  	 * <br/>
  	 * @apiParam {int} userId
  	 * <br/>
  	 * @apiSuccess {String} errorCode 错误码
  	 * @apiSuccess {String} errorMsg 消息说明
	 * @apiSuccess {Object[]} data 分页数据
	 * @apiSuccess {String} data.id 下拉框value值
	 * @apiSuccess {String} data.text 下拉框文本值
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:
		{
		errorCode: 0,
		errorMsg: "操作成功!",
		total: 0,
		data: [
		{
		id: "1",
		text: "车电网企业"
		},
		{
		id: "2",
		text: "高路通企业"
		}
		]
		}
	 * <br/>
	 * @apiError -999 系统异常!
	 */
  	@RequestMapping(value = "/{userId}/orgs", method =  RequestMethod.GET)
	public ResultVo getOrgNameByUserId(@PathVariable("userId") Integer userId) throws Exception {
		ResultVo resultVo = new ResultVo();
		resultVo.setData(orgService.getOrgNameByUserId(userId));
		return resultVo;
	}

	/**
	 * @api {GET} /api/org/auditpeople 审核人下拉框
	 * @apiName  auditMemberCombox
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 企业审核页面审核人下拉框数据
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * @apiSuccess {Object[]} data 分页数据
	 * @apiSuccess {String} data.id 下拉框value值
	 * @apiSuccess {String} data.text 下拉框文本值
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:
		{
		errorCode: 0,
		errorMsg: "操作成功!",
		total: 0,
		data: [
			{
			id: "1",
			text: "admin"
			},
			{
			id: "2",
			text: "xiaoming"
			}
		]
		}
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/auditpeople", method = RequestMethod.GET)
	public ResultVo auditMemberCombox() throws Exception {
		ResultVo resultVo = new ResultVo();
		resultVo.setData(orgService.getAuditMember());
		return resultVo;
	}
	/**
	 * @api {GET} /api/org 查询企业列表
	 * @apiName queryOrgs
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 查询企业列表，包含分页，支持企业名称，企业编号模糊查询，根据当前登录人userId获取可查看的企业信息
	 * <br/>
	 * @apiParam {int} pageNum 页码
	 * @apiParam {int} pageSize 页大小
	 * @apiParam {String} [orgName] 企业名称模糊条件(非必填)
	 * @apiParam {String} [orgNo] 企业编号模糊条件(非必填)
	 * @apiParam {String} [sort] 排序字段
	 * @apiParam {String} [order] 排序方向
	 * @apiParam {int} userId 登录用户ID
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * @apiSuccess {Object} data 分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {int} data.list.orgId 单位标识(主键)
	 * @apiSuccess {Int(9)} data.list.pOrgName 上级企业名称
	 * @apiSuccess {String(32)} data.list.orgNo 单位编号
	 * @apiSuccess {String(128)} data.list.orgName 单位名称
	 * @apiSuccess {String(32)} data.list.orgAbbr 简称
	 * @apiSuccess {String(2)} data.list.orgType 单位类型(01:总公司||02:分公司)
	 * @apiSuccess {String(32)} data.list.orgPhone 企业电话
	 * @apiSuccess {String(256)} data.list.orgAddr 企业地址
	 * @apiSuccess {String(32)} data.list.orgFax 传真
	 * @apiSuccess {String(64)} data.list.orgEmail 电子邮件
	 * @apiSuccess {String(32)} data.list.orgHead 负责人
	 * @apiSuccess {String} data.list.remark 备注
	 * @apiSuccess {Int(11)} data.list.paymentProcess 支付流程(0:钱包支付流程||1:信用代扣支付流程)
	 * @apiSuccess {String} data.list.operateType 运营类型(01:自运营 02:合作运营)
	 * @apiSuccess {String(32)} data.list.memberPhone 联系电话
	 * @apiSuccess {String(9)} data.list.orgCode 运营商组织机构代码
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1000006 当前的登录用户为空!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
  	public ResultVo queryOrgs(@RequestParam Map data) throws Exception {
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = orgService.findOrgs(data);
		resultVo.setData(pageInfo);
		return resultVo;
	}
	/**
	 * @api {GET} /api/org/audit 查询审核未通过企业列表
	 * @apiName queryAuditOrgs
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 查询企业列表，包含分页，支持企业名称，注册时间，审核人，根据当前登录人userId获取可查看的企业信息
	 * <br/>
	 * @apiParam {int} pageNum 页码
	 * @apiParam {int} pageSize 页大小
	 * @apiParam {String} [orgName] 企业名称模糊条件(非必填)
	 * @apiParam {String} [sort] 排序字段
	 * @apiParam {String} [order] 排序方向
	 * @apiParam {String} [startDate] 申请时间start
	 * @apiParam {String} [endDate] 申请时间end
	 * @apiParam {int} [auditMember] 审核人
	 * @apiParam {String} [auditStatus] 审核状态
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * @apiSuccess {Object} data 分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {int} data.list.orgId 单位标识(主键)
	 * @apiSuccess {int} data.list.auditPeople 审核人
	 * @apiSuccess {String} data.list.auditingTime 审核时间
	 * @apiSuccess {String} data.list.orgisterTime 申请时间
	 * @apiSuccess {String} data.list.userName 用户名
	 * @apiSuccess {String} data.list.email 用户邮箱
	 * @apiSuccess {String(32)} data.list.orgNo 企业编号
	 * @apiSuccess {String(128)} data.list.orgName 企业名称
	 * @apiSuccess {String(256)} data.list.orgHead 负责人
	 * @apiSuccess {String(32)} data.list.memberPhone 联系电话
	 * @apiSuccess {String(64)} data.list.auditStatus 审核状态 0:未审核，-1:未通过，1:通过
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1000006 当前的登录用户为空!
	 */
	@RequestMapping(value = "/audit", method = RequestMethod.GET)
	public ResultVo queryAuditOrgs(@RequestParam Map data) throws Exception {
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = orgService.findAuditOrgs(data);
		resultVo.setData(pageInfo);
		return resultVo;
	}

	/**
	 * @api {GET} /api/org/audit/{orgId} 查询审核企业详细信息
	 * @apiName queryAuditOrgById
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 根据企业ID查询审核企业详细信息，包含场站信息，审核信息
	 * <br/>
	 * @apiParam {int} orgId 企业表主键ID
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * @apiSuccess {Object} data 数据封装
	 * @apiSuccess {Int} data.loginId 用户ID(主键)
	 * @apiSuccess {Int} data.loginName 用户名
	 * @apiSuccess {Int} data.email 用户邮箱
	 * @apiSuccess {Int} data.password 用户密码
	 * @apiSuccess {Int} data.orgId 单位标识(主键)
	 * @apiSuccess {String} data.namePy 企业logo图片地址
	 * @apiSuccess {String} data.businesslicence 营业执照图片地址
	 * @apiSuccess {String} data.orgName 单位名称
	 * @apiSuccess {String} data.orgCode 组织机构代码
	 * @apiSuccess {String} data.orgFax 传真
	 * @apiSuccess {String} data.orgPhone 企业电话
	 * @apiSuccess {String} data.orgEmail 电子邮件
	 * @apiSuccess {String} data.orgHead 负责人
	 * @apiSuccess {String} data.memberPhone 联系电话
	 * @apiSuccess {String} data.orgAddr 企业地址
	 * @apiSuccess {String} data.remark 经营范围
	 * @apiSuccess {String} data.auditStatus 审核状态 0:未审核，-1:未通过，1:通过
	 * @apiSuccess {String} data.orgisterTime 注册时间
	 * @apiSuccess {String} data.auditingTime 审核时间
	 * @apiSuccess {String} data.auditPeople 审核人
	 * @apiSuccess {String} data.noAuditReason 不通过原因
	 * @apiSuccess {Object[]} data.stations 场站集合
	 * @apiSuccess {String} data.stations.address 场站位置
	 * @apiSuccess {String} data.stations.stationType 场站类型 1：公共(对外开放) 2：个人 3：公交专用(不对外开放) 4：环卫专用(不对外开放) 5：物流专用(不对外开放) 6：出租车专用(不对外开放) 7：其他
	 * @apiSuccess {String} data.stations.stationModel 合作模式 01：自运营 02：合作运营 03：第三方
	 * @apiSuccess {String} data.stations.place 建设场所 1：居民区 2：公共机构
	 * @apiSuccess {String} data.stations.jiaoLiu 交流充电桩数量
	 * @apiSuccess {String} data.stations.jiaoLiuP 交流充电桩功率(kw)
	 * @apiSuccess {String} data.stations.zhiLiu 直流充电桩数量
	 * @apiSuccess {String} data.stations.zhiLiuP 直流充电桩功率(kw)
	 * @apiSuccess {String} data.stations.remark 备注
	 * }
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/audit/{orgId}", method = RequestMethod.GET)
	public ResultVo queryAuditOrgById(@PathVariable("orgId") Integer orgId) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo org = orgService.findAuditOrgById(orgId);
		resultVo.setData(org);
		return resultVo;
	}

	/**
	 * @api {PUT} /api/org/audit 审核企业信息
	 * @apiName  auditOrgById
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 审核企业，审核通过的企业创建角色并给企业用户赋权限
	 * <br/>
	 * @apiParam {String} auditStatus 审核状态 1：审核通过 -1：审核不通过
	 * @apiParam {int} orgId 企业ID
	 * @apiParam {int} auditMember 审核人ID
	 * @apiParam {String} [noAuditReason] 审核不通过理由
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * @apiSuccess {Object} data 分页数据封装
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1000012 企业名称不能为空!
	 * @apiError -1000012 审核人不能为空!
	 * @apiError -1000012 企业ID不能为空!
	 */
	@RequestMapping(value = "/audit", method = RequestMethod.PUT)
	public ResultVo auditOrgById(@RequestBody Map data, HttpServletRequest request) throws Exception {
		ResultVo resultVo = new ResultVo();
		orgService.updateAuditOrgById(data, request);
		return resultVo;
	}
	/**
	 * @api {GET} /api/org/{orgId} 查询企业详细信息
	 * @apiName  queryOrgById
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 根据企业ID查询企业详细信息
	 * <br/>
	 * @apiParam {int} orgId 企业表主键ID
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * @apiSuccess {Object} data 数据封装
	 * @apiSuccess {int} data.orgId 单位标识(主键)
	 * @apiSuccess {Int(9)} data.pOrgId 上级企业ID
	 * @apiSuccess {String(128)} data.pOrgName 上级企业名称
	 * @apiSuccess {String(32)} data.orgNo 单位编号
	 * @apiSuccess {String(128)} data.orgName 单位名称
	 * @apiSuccess {String(32)} data.orgAbbr 简称
	 * @apiSuccess {String(2)} data.orgType 单位类型(01:总公司||02:分公司)
	 * @apiSuccess {String(32)} data.orgPhone 企业电话
	 * @apiSuccess {String(256)} data.orgAddr 企业地址
	 * @apiSuccess {String(256)} data.namePy logon图片地址
	 * @apiSuccess {String(32)} data.orgFax 传真
	 * @apiSuccess {String(64)} data.orgEmail 电子邮件
	 * @apiSuccess {String(32)} data.orgHead 负责人
	 * @apiSuccess {String} data.coopOrgName 合作运营单位企业名称(为空或者不存在则没有合作运营企业，现在只返回一个，也只有一个)
	 * @apiSuccess {String} data.remark 备注
	 * @apiSuccess {Int(11)} data.paymentProcess 支付流程(0:钱包支付流程||1:信用代扣支付流程)
	 * @apiSuccess {String} data.operateType 运营类型(01:自运营 02:合作运营)
	 * @apiSuccess {String(32)} data.memberPhone 联系电话
	 * @apiSuccess {String(9)} data.orgCode 运营商组织机构代码
	 *
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
	public ResultVo queryOrgById(@PathVariable("orgId") Integer orgId) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo org = orgService.findOrgById(orgId);
		resultVo.setData(org);
		return resultVo;
	}

	/**
	 * @api {POST} /api/org 保存企业信息
	 * @apiName saveOrg
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 保存企业信息，对手机号码，企业电话，邮箱进行验证
	 * <br/>
	 * @apiParam {Int(9)} userId 登录用户Id
	 * @apiParam {Int(9)} [pOrgId] 上级企业ID
	 * @apiParam {String(32)} [orgNo] 单位编号
	 * @apiParam {String(128)} [orgName] 单位名称
	 * @apiParam {String} [coopOrgIds] 合作运营企业ID字符串(例：1,2,3,4)
	 * @apiParam {String(32)} [orgAbbr] 简称
	 * @apiParam {String(2)} [orgType] 单位类型(01:总公司||02:分公司)
	 * @apiParam {String(32)} [orgPhone] 企业电话
	 * @apiParam {String(256)} [orgAddr] 企业地址
	 * @apiParam {String(32)} [orgFax] 传真
	 * @apiParam {String(64)} [orgEmail] 电子邮件
	 * @apiParam {String(32)} [orgHead] 负责人
	 * @apiParam {String(32)} [namePy] 图片地址(在更新时原值回传，新增不用管这个字段)
	 * @apiParam {String} [remark] 备注
	 * @apiParam {Int(11)} [paymentProcess] 支付流程(0:钱包支付流程||1:信用代扣支付流程)
	 * @apiParam {String} [operateType] 运营类型(01:自运营 02:合作运营)
	 * @apiParam {String(32)} [memberPhone] 联系电话
	 * @apiParam {String(9)} [orgCode] 运营商组织机构代码
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1000014 手机号码格式不正确!
	 * @apiError -1000014 邮箱格式不正确!
	 * @apiError -1000014 电话格式不正确!
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo saveOrg(MultipartHttpServletRequest multiRequest) throws Exception {
		ResultVo resultVo = new ResultVo();
		MultipartFile file = multiRequest.getFile("file");
		Map data = CommonUtils.gerParamterMap(multiRequest.getParameterMap());
		orgService.insertOrg(data, file);
		//Enumeration<String> enumeration = multiRequest.getParameterNames();
		//while (enumeration.hasMoreElements()) {
		//	String name =  enumeration.nextElement();
		//	System.out.println(name);
		//}
		return resultVo;
	}


	/**
	 * @api {PUT} /api/org 更新企业信息
	 * @apiName  updateOrg
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 更新企业信息，对手机号码，企业电话，邮箱进行验证
	 * <br/>
	 * @apiParam {Int(9)} orgId 单位标识
	 * @apiParam {Int(9)} [pOrgId] 上级企业ID
	 * @apiParam {String(32)} [orgNo] 单位编号
	 * @apiParam {String(128)} [orgName] 单位名称
	 * @apiParam {String} [coopOrgIds] 合作运营企业ID字符串(例：1,2,3,4)
	 * @apiParam {String(32)} [orgAbbr] 简称
	 * @apiParam {String(2)} [orgType] 单位类型(01:总公司||02:分公司)
	 * @apiParam {String(32)} [orgPhone] 企业电话
	 * @apiParam {String(256)} [orgAddr] 企业地址
	 * @apiParam {String(32)} [orgFax] 传真
	 * @apiParam {String(64)} [orgEmail] 电子邮件
	 * @apiParam {String(32)} [orgHead] 负责人
	 * @apiParam {String} [remark] 备注
	 * @apiParam {Int(11)} [paymentProcess] 支付流程(0:钱包支付流程||1:信用代扣支付流程)
	 * @apiParam {String} [operateType] 运营类型(01:自运营 02:合作运营)
	 * @apiParam {String(32)} [memberPhone] 联系电话
	 * @apiParam {String(9)} [orgCode] 运营商组织机构代码
	 * @apiParam {String(64)} [namePy] 图片地址
	 * <br/>
	 * @apiSuccess {String} errorCode 错误码
	 * @apiSuccess {String} errorMsg 消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1000012 更新数据主键不能为空!
	 * @apiError -1000014 手机号码格式不正确!
	 * @apiError -1000014 邮箱格式不正确!
	 * @apiError -1000014 电话格式不正确!
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResultVo updateOrg(MultipartHttpServletRequest multiRequest) throws Exception {
		ResultVo resultVo = new ResultVo();
		MultipartFile file = multiRequest.getFile("file");
		Map data = gerParamterMap(multiRequest.getParameterMap());
		orgService.updateOrg(data, file);
		return resultVo;
	}


	/**
	 * @api {get} /api/org/orgtree    运营商下拉树
	 * @apiName getOrg
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据用户id,获取运营商下拉树
	 * <br/>
	 * @apiParam {Integer}    [userId]     用户Id
	 * @apiParam {Integer}    [orgId]      运营商Id
	 * @apiParam {String}    [orgName]     运营商名称
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Object[]} data.list 数据对象数组
	 * @apiSuccess {Integer} data.list.id 运营商Id
	 * @apiSuccess {String} data.list.name 运营商名称
	 * @apiSuccess {String} data.list.pid 上级运营商Id
	 * @apiSuccess {String} data.list.level 级别
	 * @apiSuccess {Object[]} data.list.children 二级运营商
	 * @apiSuccess {Integer} data.list.children.id 运营商Id
	 * @apiSuccess {String} data.list.children.name 运营商名称
	 * @apiSuccess {Integer} data.list.children.pid 上级运营商Id
	 * @apiSuccess {String} data.list.children.children 下级运营商
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="orgtree",method = RequestMethod.GET)
	public ResultVo getOrg(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		List  list = orgService.getOrg(map);
		resVo.setData(list);
		return resVo;
	}

	/**
	 * @api {GET} /api/org/_export 导出企业列表
	 * @apiName exportOrg
	 * @apiGroup OrgController
	 * @apiVersion 2.0.0
	 * @apiDescription 导出企业列表，支持企业名称，企业编号模糊查询，根据当前登录人userId获取可查看的企业信息
	 * <br/>
	 * @apiParam {String} [orgName] 企业名称模糊条件(非必填)
	 * @apiParam {String} [orgNo] 企业编号模糊条件(非必填)
	 * @apiParam {String} [sort] 排序字段
	 * @apiParam {String} [order] 排序方向
	 * @apiParam {int} userId 登录用户ID
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1000006 当前的登录用户为空!
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
	public ResultVo exportOrgs(@RequestParam Map data, HttpServletResponse response) throws Exception {
		orgService.exportOrgs(data, response);
		return new ResultVo();
	}

    /**
     * @api {GET} /api/org/audit/_export 导出审核未通过企业列表
     * @apiName exportAuditOrgs
     * @apiGroup OrgController
     * @apiVersion 2.0.0
     * @apiDescription 导出审核未通过企业列表，支持企业名称，注册时间，审核人，根据当前登录人userId获取可查看的企业信息
     * <br/>
     * @apiParam {String} [orgName] 企业名称模糊条件
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向
     * @apiParam {String} [startDate] 申请时间start
     * @apiParam {String} [endDate] 申请时间end
     * @apiParam {String} [auditStatus] 审核状态
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000006 当前的登录用户为空!
     */
    @RequestMapping(value = "/audit/_export", method = RequestMethod.GET)
    public ResultVo exportAuditOrgs(@RequestParam Map data, HttpServletResponse response) throws Exception {
        orgService.exportAuditOrgs(data, response);
        return new ResultVo();
    }
}





package com.clouyun.charge.modules.member.web;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.member.service.VehicleAuthService;
import com.github.pagehelper.PageInfo;
/**
 * 描述: 会员常用车辆认证
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0.0
 * 创建日期: 2017年6月14日
 */
@RestController
@RequestMapping("/api")
@SuppressWarnings("rawtypes")
public class VehicleAuthController extends BusinessController{
	
	@Autowired
	private VehicleAuthService vehicleAuthService;
	/**
     * @api {GET} /api/vehicleauths   认证列表查询
     * @apiName  getVehicleAuthsPage
     * @apiGroup VehicleAuthController
     * @apiVersion 2.0.0
     * @apiDescription    高辉      认证列表查询
     * <br/>
     * @apiParam {int}    pageNum		       页码
     * @apiParam {int}    pageSize        页大小
     * @apiParam {int}    userId          当前登陆用户id
     * @apiParam {String} [sort] 		       排序字段
     * @apiParam {String} [order]		       排序方向(DESC:降序,ASC:升序)
     * @apiParam {String} [startTime]     申请开始时间(yyyy-MM-dd)
	 * @apiParam {String} [endTime]       申请结束时间(yyyy-MM-dd)
     * @apiParam {String} [consTruename]  会员名称
	 * @apiParam {String} [consPhone]     手机号码
	 * @apiParam {String} [orgId]         运营客户来源
     * <br/>
     * @apiSuccess {String}     errorCode   				错误码
     * @apiSuccess {String}     errorMsg   					消息说明
     * @apiSuccess {Object}     data        				分页数据封装
     * @apiSuccess {Object[]}   data.list 				           分页数据对象数组
     * @apiSuccess {String}     data.list.consId 		           会员id
     * @apiSuccess {String}     data.list.vehicleId 	           车辆id
     * @apiSuccess {String}     data.list.time 		                       申请时间
     * @apiSuccess {String}     data.list.consTruename 	           会员名称
     * @apiSuccess {Integer}    data.list.consTypeCode 	           会员类型(字典 type=hylb)
     * @apiSuccess {String}     data.list.groupName 		集团名称
     * @apiSuccess {Integer}    data.list.consFrom 		           会员来源(字典 type=hyly)
     * @apiSuccess {String}     data.list.orgName 		           运营客户来源
     * @apiSuccess {Integer}    data.list.authStatus 	           认证状态(字典 type=clrzzt)
     * <br/>
     * @apiError -999 系统异常！
     * @apiError -888 请求方式异常！
     * @apiError -1000006 请求用户为空！
	 */
	@RequestMapping(value = "/vehicleauths", method = RequestMethod.GET)
	public ResultVo getVehicleAuthsPage(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = vehicleAuthService.getVehicleAuths(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
	/**
     * @api {PUT} /api/vehicleauths   车辆认证
     * @apiName  authVehicle
     * @apiGroup VehicleAuthController
     * @apiVersion 2.0.0
     * @apiDescription    高辉      车辆认证
     * <br/>
     * @apiParam {Integer}    userId          当前登陆用户id
	 * @apiParam {String}     vehicleId       车辆id
	 * @apiParam {String}     authStatus      认证状态(字典 type=clrzzt)
	 * @apiParam {String}     [authDesc]      认证不通过原因描述
     * <br/>
     * @apiSuccess {String}     errorCode   				错误码
     * @apiSuccess {String}     errorMsg   					消息说明
     * <br/>
     * @apiError -999 系统异常！
     * @apiError -888 请求方式异常！
	 */
	@RequestMapping(value = "/vehicleauths", method = RequestMethod.PUT)
	public ResultVo authVehicle(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		vehicleAuthService.authVehicle(map);
		return resultVo;
	}
}

package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.GroupBillService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 描述: GroupBillController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月26日
 */
@RestController
@RequestMapping("/api/groupbills")
public class GroupBillController extends BusinessController {
	
	@Autowired
	GroupBillService groupBillService;
	
	/**
	 * 
	 * @api {get} /api/groupbills  查询集团账单列表
	 * @apiName getGroupBillAll
	 * @apiGroup GroupBillController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据集团名称,集团编号,缴费状态,年,月...条件查询集团列表数据
	 * <br/>
	 * @apiParam {Integer}    userId    用户Id
	 * @apiParam {String}    [groupName]    集团名称
	 * @apiParam {Integer}   [groupNo]     集团编号
	 * @apiParam {Integer}   [groupBillStatus]    缴费状态	
	 * @apiParam {String}    [year]     年 2014
	 * @apiParam {String}    [month]    月 01
	 * @apiParam {Integer}    pageNum      页码
	 * @apiParam {Integer}    pageSize     页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.tractId 合约Id
	 * @apiSuccess {String} data.list.groupName 集团名称
	 * @apiSuccess {String} data.list.groupBillMonth 出账月份
	 * @apiSuccess {Double} data.list.curAmount 余额
	 * @apiSuccess {Integer} data.list.groupBillStatus 缴费状态
	 * @apiSuccess {Integer} data.list.stationId 场站id
	 * @apiSuccess {Date} data.list.groupBillDate 出账日期 yyyy-MM-dd
	 * @apiSuccess {Integer} data.list.groupBillId 集团账单Id,主键Id
	 * @apiSuccess {String} data.list.groupNo 集团账单编号
	 * @apiSuccess {Integer} data.list.groupId 集团Id
	 * @apiSuccess {Double} data.list.groupBillAmount 出账金额
	 * @apiSuccess {Double} data.list.contractName 合约名称
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResultVo getGroupBillAll(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo groupBillAll = groupBillService.getGroupBillAll(map);
		resVo.setData(groupBillAll);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/groupbills/infos    查询集团账单详情列表
	 * @apiName getGroupBillInfos
	 * @apiGroup GroupBillController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据集团账单Id,会员名称,会员账号... 查询集团账单详情列表
	 * <br/>
	 * @apiParam {Integer}   userId 	  用户Id
	 * @apiParam {Integer}   groupBillId  集团账单Id
	 * @apiParam {String}    [consName]   会员名称
	 * @apiParam {String}    [consPhone]  会员账号
	 * @apiParam {Integer}    pageNum      页码
	 * @apiParam {Integer}    pageSize     页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Object} data.total   合计数据项
	 * @apiSuccess {Double} data.total.chgPower   合计充电量
	 * @apiSuccess {Double} data.total.elceFee   合计电费(元)
	 * @apiSuccess {Double} data.total.servFee   合计服务费(元)
	 * @apiSuccess {Double} data.total.parkFee   合计停车费(元)
	 * @apiSuccess {Double} data.total.amount   合计消费金额(元)
	 * @apiSuccess {Object} data.groupBillInfos 分页数据对象
	 * @apiSuccess {Object[]} data.groupBillInfos.list 分页数据对象数组
	 * @apiSuccess {Date} data.groupBillInfos.list.createTime 订单时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {String} data.groupBillInfos.list.billPayNo 订单编号
	 * @apiSuccess {String} data.groupBillInfos.list.consPhone 会员账号
	 * @apiSuccess {String} data.groupBillInfos.list.consName 会员名称
	 * @apiSuccess {String} data.groupBillInfos.list.chgPower 充电量(kWh)
	 * @apiSuccess {String} data.groupBillInfos.list.elceFee 电费(元)
	 * @apiSuccess {String} data.groupBillInfos.list.servFee 服务费(元)
	 * @apiSuccess {String} data.groupBillInfos.list.parkFee 停车费(元)
	 * @apiSuccess {String} data.groupBillInfos.list.amount 消费金额(元)
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="infos",method=RequestMethod.GET)
	public ResultVo getGroupBillInfos(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		Map tractMap = groupBillService.getGroupBillInfos(map);
		resVo.setData(tractMap);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/groupbills/_export    导出集团账单信息
	 * @apiName exportGroupBill
	 * @apiGroup GroupBillController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据集团名称,集团编号,缴费状态,年,月...条件导出集团账单信息
	 * <br/>
	 * @apiParam {Integer}    userId    用户Id
	 * @apiParam {String}    [groupName]    集团名称
	 * @apiParam {Integer}   [groupNo]     集团编号
	 * @apiParam {Integer}   [groupBillStatus]    缴费状态	
	 * @apiParam {String}    [year]     年 2014
	 * @apiParam {String}    [month]    月 01
	 * @apiParam {Integer}   [pageNum]      页码
	 * @apiParam {Integer}   [pageSize]     页大小
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_export",method=RequestMethod.GET)
	public void exportGroupBill(@RequestParam Map map,HttpServletResponse response) throws Exception{
		groupBillService.exportGroupBill(map,response);
	}
	
	
	/**
	 * 
	 * @api {get} /api/groupbills/infos/_export    导出集团账单详情列表数据
	 * @apiName exportGroupBillInfo
	 * @apiGroup GroupBillController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据集团账单Id,会员名称,会员账号... 查询集团账单详情列表
	 * <br/>
	 * @apiParam {Integer}	 userId 	  用户Id
	 * @apiParam {Integer}   groupBillId  集团账单Id
	 * @apiParam {String}    [consName]   会员名称
	 * @apiParam {String}    [consPhone]  会员账号
	 * @apiParam {Integer}   [pageNum]      页码
	 * @apiParam {Integer}   [pageSize]     页大小
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="infos/_export",method=RequestMethod.GET)
	public void exportGroupBillInfo(@RequestParam Map map,HttpServletResponse response) throws Exception{
		groupBillService.exportGroupBillInfo(map, response);
	}
	
}

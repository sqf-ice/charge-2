package com.clouyun.charge.modules.finance.web;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.modules.spring.web.BaseController;
import com.clouyun.charge.modules.finance.service.WithdrawService;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 
 * 描述: 提现
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月17日
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/withdraws")
public class WithdrawController extends BaseController{
	
	@Autowired
	private WithdrawService withdrawService;
	/**
	 * @api {POST} /api/withdraws  新增提现记录
	 * @apiName   addWithdraw
	 * @apiGroup  WithdrawController
	 * @apiVersion 2.0.0
	 * @apiDescription 高辉 新增提现记录
	 * <br/>
	 * @apiParam   {String}    no           提现订单号
	 * @apiParam   {String}    tel          提现人电话
	 * @apiParam   {Integer}   type         提现方式(0-支付宝、1-微信)(typeId=48)
	 * @apiParam   {Date}      time         提现时间(yyyy-MM-dd)
	 * @apiParam   {String}    name         提现真实姓名
	 * @apiParam   {Integer}   status       提现状态(0-未通过、1-申请中、2-待转账、3-转账完成、4-审批确认)(typeId=47)
	 * @apiParam   {Decimal}   amount       提现金额
	 * @apiParam   {String}    remark       备注
	 * @apiParam   {Decimal}   remain       余额
	 * @apiParam   {String}    account      收款账号
	 * @apiParam   {Integer}   cConCompanyId       合约企业ID
	 * @apiParam   {String}    cConCompanyName     合约企业名字    
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo insertWithdraw(@RequestBody() Map map)throws BizException{
		ResultVo resultVo = new ResultVo();
		withdrawService.insertWithdraw(map);
		return resultVo;
	}
    /**
     * 
     * @api {DELETE} /api/withdraws/{id}   删除提现记录
     * @apiName   deleteWithdraw
     * @apiGroup  WithdrawController
     * @apiVersion 2.0.0
     * @apiDescription  高辉 根据提现Id删除提现记录
     * <br/>
     * @apiParam   {Integer}   id       提现记录id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * <br/>
     * @apiError -999 系统异常!
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResultVo deleteWithdraw(@PathVariable Integer id)throws BizException{
		ResultVo resultVo = new ResultVo();
		withdrawService.deleteWithdraw(id);
		return resultVo;
	}
	/**
	 * 
	 * @api {PUT} /api/withdraws  根据提现Id更新提现记录   
	 * @apiName updateWithdraw
	 * @apiGroup WithdrawController
	 * @apiVersion 2.0.0
	 * @apiDescription 高辉 根据提现Id更新提现记录
	 * <br/>
	 * @apiParam   {Integer}   id            提现记录id
	 * @apiParam   {String}    no            提现订单号
	 * @apiParam   {String}    tel           提现人电话
	 * @apiParam   {Integer}   type          提现方式(0-支付宝、1-微信)(typeId=48)
	 * @apiParam   {Date}      time          提现时间(yyyy-MM-dd)
	 * @apiParam   {String}    name          提现真实姓名
	 * @apiParam   {Integer}   status        提现状态(0-未通过、1-申请中、2-待转账、3-转账完成、4-审批确认)(typeId=47)
	 * @apiParam   {Decimal}   amount        提现金额
	 * @apiParam   {String}    remark        备注
	 * @apiParam   {Decimal}   remain        余额
	 * @apiParam   {String}    account       收款账号
	 * @apiParam   {Integer}   cConCompanyId       合约企业ID
	 * @apiParam   {String}    cConCompanyName     合约企业名字
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResultVo updateWithdraw(@RequestBody() Map map)throws BizException{
		ResultVo resultVo = new ResultVo();
		withdrawService.updateWithdraw(map);
		return resultVo;
	}
    /**
     * 
     * @api {GET} /api/withdraws/{id}  获取提现记录详情 
     * @apiName  getWithdraw
     * @apiGroup WithdrawController
     * @apiVersion 2.0.0
     * @apiDescription  高辉 根据提现Id获取提现记录详情 
     * <br/>
     * @apiParam   {Integer} id               提现记录id
     * <br/>
     * @apiSuccess {String}    errorCode      错误码
	 * @apiSuccess {String}    errorMsg       消息说明
	 * @apiSuccess {Object}    data           分页数据封装
	 * @apiSuccess {Integer}   data.id        提现id      
	 * @apiSuccess {String}    data.no        提现订单号
	 * @apiSuccess {String}    data.tel       提现人电话
	 * @apiSuccess {Integer}   data.type      提现方式(0-支付宝、1-微信)
	 * @apiSuccess {Date}      data.time      提现时间(yyyy-MM-dd)
	 * @apiSuccess {String}    data.name      提现真实姓名
	 * @apiSuccess {Integer}   data.status    提现状态(0-未通过、1-申请中、2-待转账、3-转账完成、4-审批确认)
	 * @apiSuccess {Decimal}   data.amount    提现金额
	 * @apiSuccess {String}    data.remark    备注
	 * @apiSuccess {Decimal}   data.remain    余额
	 * @apiSuccess {String}    data.account   收款账号 
	 * @apiSuccess {Integer}   data.cConCompanyId    合约企业ID
	 * @apiSuccess {Integer}   data.cConCompanyName  合约企业名字
	 * <br/>
     * @apiError -999 系统异常!
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResultVo getWithdraw(@PathVariable Integer id)throws BizException{
		ResultVo resultVo = new ResultVo();
		Map withdraw = withdrawService.getWithdraw(id);
		resultVo.setData(withdraw);
		return resultVo;
	}
	/**
	 * @api {GET} /api/withdraws   分页查询提现记录列表
	 * @apiName  getWithdrawsPage
	 * @apiGroup WithdrawController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 根据相应条件分页查询提现记录列表
	 * <br/>
	 * @apiParam   {Date}      startTime                开始时间(yyyy-MM-dd)
	 * @apiParam   {Date}      endTime                  结束时间(yyyy-MM-dd)
	 * @apiParam   {String}    no                       提现订单号
	 * @apiParam   {String}    status                   提现状态(同下)
	 * @apiParam   {String}    companyName              合约企业名称
	 * @apiParam   {String}    account                  收款账号
	 * @apiParam   {int}       pageNum                页码
	 * @apiParam   {int}       pageSize                 页大小
	 * @apiParam   {String}    sort                     排序字段
	 * @apiParam   {String}    order                    排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiSuccess {String}    errorCode                    错误码
	 * @apiSuccess {String}    errorMsg                     消息说明
	 * @apiSuccess {Object}    data                         分页数据封装
     * @apiSuccess {Object}    data.PageInfo                分页数据对象
     * @apiSuccess {List}      data.PageInfo.list           数据对象集合
	 * @apiSuccess {Integer}   data.PageInfo.list.id        提现id      
	 * @apiSuccess {String}    data.PageInfo.list.no        提现订单号
	 * @apiSuccess {String}    data.PageInfo.list.tel       提现人电话
	 * @apiSuccess {Integer}   data.PageInfo.list.type      提现方式(0-支付宝、1-微信 )
	 * @apiSuccess {Date}      data.PageInfo.list.time      提现时间(yyyy-MM-dd)
	 * @apiSuccess {String}    data.PageInfo.list.name      提现真实姓名
	 * @apiSuccess {Integer}   data.PageInfo.list.status    提现状态(0-未通过、1-申请中、2-待转账、3-转账完成、4-审批确认)
	 * @apiSuccess {Decimal}   data.PageInfo.list.amount    提现金额
	 * @apiSuccess {String}    data.PageInfo.list.remark    备注
	 * @apiSuccess {Decimal}   data.PageInfo.list.remain    余额
	 * @apiSuccess {String}    data.PageInfo.list.account   收款账号 
	 * @apiSuccess {Integer}   data.PageInfo.list.cConCompanyId    合约企业ID
	 * @apiSuccess {Integer}   data.PageInfo.list.cConCompanyName  合约企业名字
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo getWithdrawsPage(@RequestParam Map map)throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = withdrawService.getWithdrawsPage(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
}

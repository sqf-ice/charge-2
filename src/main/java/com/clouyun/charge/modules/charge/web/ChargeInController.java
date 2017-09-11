package com.clouyun.charge.modules.charge.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.service.AbnormalCharge;
import com.clouyun.charge.modules.charge.service.ChargeInService;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 描述: 充电收入控制器 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: lips <lips@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年4月14日
 */
@RestController
@RequestMapping("/api/chargein")
public class ChargeInController extends BusinessController {
	public static final Logger logger = LoggerFactory.getLogger(ChargeInController.class);
	@Autowired ChargeInService chargeInService;

	 /**
    * @api {get} /api/chargein/charging   查询充电收入
    * @apiName selectCdsr
    * @apiGroup ChargeInController
    * @apiVersion 2.0.0
    * @apiDescription 易凯    查询充电收入
	* @apiParam {int}        userId                              	   用户Id
	* @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	* @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	* @apiParam {String}        [rechargeCard]                           充值卡号
	* @apiParam {int}        [orgId]                              	  运营商
	* @apiParam {String}        [orgName]                               运营商姓名 模糊查询
	* @apiParam {int}        [consTypeCode]                           会员类型（字典表取值hylb2，01：个人会员 02：集团会员 03：其他）
	* @apiParam {int}        [payType]                                支付方式(字典表取值shoukfs,1:钱包，2:月结 3,微信 4,支付宝,5,储值卡)
	* @apiParam {int}        [stationId]                            场站id
	* @apiParam {String}        [stationName]                            场站名称模糊查询
	* @apiParam {int}        [groupId]                              集团id
	* @apiParam {String}        [groupName]                              集团名称模糊查询
	* @apiParam {int}        [payState]                               支付状态（字典表取值zfzt，1：待结算 2：未付费 3:已付费 4:异常付费）
	* @apiParam {String}        [pileId]                               设备名称
	* @apiParam {String}        [pileName]                               设备名称
	* @apiParam {String}        [consPhone]                              手机号模糊查询
	* @apiParam {String}        [billPayNo]                              订单号
	* @apiParam {int}           pageNum                            页码
	* @apiParam {int}           pageSize                           页大小
    * <br/>
    * @apiSuccess {String}       errorCode                         错误码
    * @apiSuccess {String}       errorMsg                          消息说明
	* @apiSuccess {Object}         data                    		分页数据封装
	* @apiSuccess {Integer}        data.total                    	总记录数
    * @apiSuccess {Object[]}       data.list                    	分页数据对象数组
    * @apiSuccess {String}        data.list.billPayId                    		      主键列表Id
	* @apiSuccess {String}       data.list.billPayNo                    		     订单编号
	* @apiSuccess {Double}       data.list.amount                    		        消费金额(元)
	* @apiSuccess {Double}       data.list.chgPower                    		    充电电量(kW·h)
	* @apiSuccess {String}       data.list.groupName                    		    集团名称
	* @apiSuccess {String}       data.list.consName                    		    会员名称
	* @apiSuccess {String}       data.list.consTypeCode                            会员类型
	* @apiSuccess {String}       data.list.rechargeCard                           充值卡号
	* @apiSuccess {Double}       data.list.elceFee                    		       电费(元)
	* @apiSuccess {Double}       data.list.servFee                    		      服务费(元/kW·h)
	* @apiSuccess {Double}       data.list.parkFee                    		     停车费(元)
	* @apiSuccess {String}       data.list.discountFee                         优惠折扣
	* @apiSuccess {String}       data.list.payState                    	   订单状态
	* @apiSuccess {String}       data.list.payType                    		   支付方式
	* @apiSuccess {String}       data.list.finishTime                         支付时间(yyyy-mm-dd HH:mm:ss)
	* @apiSuccess {String}       data.list.stationName                        场站名称
	* @apiSuccess {String}       data.list.pileType                    		   设备类型
	* @apiSuccess {String}      data.list.pileNo                    		      设备编号
	* @apiSuccess {String}      data.list.pileName                    		      设备名称
	* @apiSuccess {String}       data.list.consPhone                    		  手机号
    * @apiSuccess {String}       data.list.createTime                            订单时间(yyyy-mm-dd HH:mm:ss)
	* @apiSuccess {String}       data.list.startTime                    		  开始充电时间(yyyy-mm-dd HH:mm:ss)
    * @apiSuccess {String}       data.list.endTime                    		     结束充电时间(yyyy-mm-dd HH:mm:ss)
	* @apiSuccess {String}       data.list.useTime                              充电时长
	* @apiSuccess {String}       data.list.outBillNo                    	 	   备注
	* @apiSuccess {String}       data.list.gumPoint                    	 	   枪口编号
	* <br/>
    * @apiError -999 系统异常!
	* @apiError -888 请求方式异常!
	* @apiError -1700000 参数缺失
    */
	@RequestMapping(value = "/charging", method = RequestMethod.GET)
	public ResultVo selectCdsr(@RequestParam Map data) throws Exception {
        DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
        if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.selectCdsr(vo);
			resultVo.setData(pageList);
		}else {
			 throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/charging/_count   查询充电收入合计
	 * @apiName selectCdsrCount
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    查询充电收入合计
	 * @apiParam {int}        userId                              	   用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {String}        [rechargeCard]                           充值卡号
	 * @apiParam {int}        [orgId]                              	  运营商
	 * @apiParam {String}        [orgName]                               运营商姓名 模糊查询
	 * @apiParam {int}        [consTypeCode]                           会员类型（字典表取值hylb2，01：个人会员 02：集团会员 03：其他）
	 * @apiParam {int}        [payType]                                支付方式(字典表取值shoukfs,1:钱包，2:月结 3,微信 4,支付宝,5,储值卡)
	 * @apiParam {int}        [stationId]                            场站id
	 * @apiParam {String}        [stationName]                            场站名称模糊查询
	 * @apiParam {int}        [groupId]                              集团id
	 * @apiParam {String}        [groupName]                              集团名称模糊查询
	 * @apiParam {int}        [payState]                               支付状态（字典表取值zfzt，1：待结算 2：未付费 3:已付费 4:异常付费）
	 * @apiParam {String}        [pileId]                               设备名称
	 * @apiParam {String}        [pileName]                               设备名称
	 * @apiParam {String}        [consPhone]                              手机号模糊查询
	 * @apiParam {String}        [billPayNo]                              订单号
	 * <br/>
	 * @apiSuccess {String}       errorCode                         错误码
	 * @apiSuccess {String}       errorMsg                          消息说明
	 * @apiSuccess {Object}         data                    		分页数据封装
	 * @apiSuccess {String}       data.chgPower                	 	   合计充电量
	 * @apiSuccess {String}       data.amount                    	 	   合计金额
	 * @apiSuccess {String}       data.elceFee                    	 	   合计电费
	 * @apiSuccess {String}       data.servFee                    	 	   合计服务费
	 * @apiSuccess {String}       data.parkFee                   	 	   合计停车费
	 * @apiSuccess {String}       data.discount                   	 	   合计优惠折扣
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/charging/_count", method = RequestMethod.GET)
	public ResultVo selectCdsrCount(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			DataVo pageList=chargeInService.chargeCount(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	 /**
    * @api {get} /api/chargein/charging/_export   充电收入导出
    * @apiName exportCdsr
    * @apiGroup ChargeInController
    * @apiVersion 2.0.0
    * @apiDescription 易凯    充电收入导出
    * <br/>
	* @apiParam {int}        userId                              	   用户Id
	* @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	* @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	* @apiParam {String}        [rechargeCard]                           充值卡号
	* @apiParam {int}        [orgId]                              	  运营商
	* @apiParam {String}        [orgName]                               运营商姓名 模糊查询
	* @apiParam {int}        [consTypeCode]                           会员类型（字典表取值hylb，01：个人会员 02：集团会员）
	* @apiParam {int}        [payType]                                支付方式(字典表取值shoukfs,1:钱包，2:月结 3,微信 4,支付宝,5,储值卡)
	* @apiParam {int}        [stationId]                            场站id
	* @apiParam {String}        [stationName]                            场站名称模糊查询
	* @apiParam {int}        [groupId]                              集团id
	* @apiParam {String}        [groupName]                              集团名称模糊查询
	* @apiParam {int}        [payState]                               支付状态（字典表取值zfzt，1：待结算 2：未付费 3:已付费 4:异常付费）
	* @apiParam {String}        [pileId]                               设备名称
	* @apiParam {String}        [pileName]                               设备名称
	* @apiParam {String}        [consPhone]                              手机号模糊查询
	* @apiParam {String}        [billPayNo]                              订单号
	* @apiParam {int}           pageNum                            页码
	* @apiParam {int}           pageSize                           页大小
	* <br/>
	* @apiSuccess {String} errorCode   错误码
	* @apiSuccess {String} errorMsg    消息说明
	* @apiSuccess {Object} data        分页数据封装
	* @apiSuccess {Integer} data.total     总记录数
	* @apiSuccess {Object[]} data.list 分页数据对象数组
	* <br/>
	* @apiError -999  系统异常
	* @apiError -888 请求方式异常!
	* @apiError -1700000 参数缺失
    */
		@RequestMapping(value = "/charging/_export", method = RequestMethod.GET)
		public void exportCdsr(@RequestParam Map map,HttpServletResponse response) throws Exception {
			DataVo dataVo = new DataVo(map);
			if(dataVo.isNotBlank("userId")){
				chargeInService.exportCdsr(dataVo,response);
			}else {
				throw   new BizException(1700000,"用户Id");
			}

		}

	/**
	 * @api {get} /api/chargein/detail   充电详情
	 * @apiName detailChargein
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    充电详情
	 * <br/>
	 * @apiParam {int}        nodeId                             	         充电Id(充电列表的billPayId)
	 * @apiParam {String}        useTime                                    充电时长(传入列表的useTime字段)
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		   查询成功返回值
	 * @apiSuccess {String}         data.billPayNo                    		            订单号
	 * @apiSuccess {String}         data.createTime                    		        订单时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}         data.cardId                    		            充值卡号
	 * @apiSuccess {String}         data.consName                    		            会 员 名
	 * @apiSuccess {String}         data.consTypeCode                    		        会员类型
	 * @apiSuccess {String}         data.groupName                    		            集团名称
	 * @apiSuccess {String}         data.licensePlate                    		        车牌号
	 * @apiSuccess {String}         data.consPhone                    		            联系方式
	 * @apiSuccess {String}         data.startTime                    		            开始充电时间
	 * @apiSuccess {String}         data.endTime                    		           结束充电时间
	 * @apiSuccess {String}         data.useTime                    		           充电时长
	 * @apiSuccess {String}         data.payType                    		        付款方式
	 * @apiSuccess {String}         data.payState                    		        支付状态
	 * @apiSuccess {String}         data.amount                    		        消费金额(元)
	 * @apiSuccess {String}         data.priceType                    		        电费类型(电费类型为1 只用获取单价和充电量 为2获取尖峰平谷电价和电费)
	 * @apiSuccess {String}         data.singlePrice                    		       单价
	 * @apiSuccess {String}         data.powerZxyg1                    		        尖价电量(kW·h)
	 * @apiSuccess {String}         data.powerZxyg2                    		        峰价电量(kW·h)
	 * @apiSuccess {String}         data.powerZxyg3                    		        平价电量(kW·h)
	 * @apiSuccess {String}         data.powerZxyg4                    		        谷价电量(kW·h)
	 * @apiSuccess {String}         data.prcZxygz1                    		        尖价电费(元)
	 * @apiSuccess {String}         data.prcZxygz2                    		        峰价电费(元)
	 * @apiSuccess {String}         data.prcZxygz3                    		        平价电费(元)
	 * @apiSuccess {String}         data.prcZxygz4                   		        谷价电费(元)
	 * @apiSuccess {String}         data.elceFee                    		        总充电费(kW·h)
	 * @apiSuccess {String}         data.servFee                    		        总服务费(元)
	 * @apiSuccess {String}         data.servPrice                    		        服务费单价(元)
	 * @apiSuccess {String}         data.parkFee                    		        停车费(元)
	 * @apiSuccess {String}         data.preAmount                    		        卡充电前余额(元)
	 * @apiSuccess {String}         data.curAmount                    		        卡充电后余额(元)
	 * @apiSuccess {String}         data.discountFee                    		        优惠券抵扣金额(元)
	 * @apiSuccess {String}         data.stationName                    		        场站名
	 * @apiSuccess {String}         data.pileName                    		        设备名称
	 * @apiSuccess {String}         data.pileType                    		        设备类型
	 * @apiSuccess {String}         data.pileNo                    		        设备编号
	 * @apiSuccess {String}         data.orgName                    		        运营商
	 * @apiSuccess {String}         data.gumPoint                    		        枪口编号
	 * @apiSuccess {String}         data.chgPower                    		        总充电量(kW·h)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultVo detailChargein(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo data = new DataVo(map);
		if(data.isNotBlank("nodeId")){
			DataVo dvList=chargeInService.detailChargein(data);
			if(data.isNotBlank("useTime")){
				dvList.put("useTime",data.getString("useTime"));
			}
			resultVo.setData(dvList);
		}else {
			throw   new BizException(1700000,"充电Id");
		}

		return resultVo;
	}
	/**
	 * @api {get} /api/chargein/divide   分成收入查询
	 * @apiName selectFcsr
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    分成收入查询
	 * <br/>
	 * @apiParam {int}       userId                                      用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]                            场站Id
	 * @apiParam {int}        [contractId]                            合约Id
	 * @apiParam {String}        [stationName]                            场站名称 模糊查询
	 * @apiParam {String}        [contractName]                            合约名称 模糊查询
	 * @apiParam {String}        [contractType]                            合约类型(字典表hylx)
	 * @apiParam {int}           pageNum                               页码
	 * @apiParam {int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {int}       data.list.contractId                    		      主键列表Id
	 * @apiSuccess {date}       data.list.did 			 结算日期(yyyy-mm-dd)
	 * @apiSuccess {String}       data.list.orgName		运营商
	 * @apiSuccess {String}      data.list.contractName 	合约名称
	 * @apiSuccess {String}      data.list.stationName 	合约场站
	 * @apiSuccess {String}      data.list.contractType 	合约类型
	 * @apiSuccess {Double}       data.list.diep			设备用电量
	 * @apiSuccess {Double}       data.list.dicp 			设备充电量
	 * @apiSuccess {Double}       data.list.dicl 			设备损耗
	 * @apiSuccess {Double}       data.list.conEff 			充电效率
	 * @apiSuccess {Double}       data.list.diti 		 	充电收入
	 * @apiSuccess {Double}       data.list.ditsf 	 	 充电侧分成服务费
	 * @apiSuccess {Double}       data.list.diepcs     	用电侧分成服务费
	 * @apiSuccess {Double}       data.list.ditip     	分成收入
	 * @apiSuccess {String}       data.list.fcje     	合约企业&分成比例&分成金额
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
	 */
	@RequestMapping(value = "/divide", method = RequestMethod.GET)
	public ResultVo selectFcsr(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.selectFcsr(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/divide/_count   分成收入查询合计
	 * @apiName selectFcsrCount
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    分成收入查询合计
	 * <br/>
	 * @apiParam {int}       userId                                      用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]                            场站Id
	 * @apiParam {int}        [contractId]                            合约Id
	 * @apiParam {String}        [stationName]                            场站名称 模糊查询
	 * @apiParam {String}        [contractName]                            合约名称 模糊查询
	 * @apiParam {String}        [contractType]                            合约类型(字典表hylx)
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Double}       data.diep			设备用电量统计
	 * @apiSuccess {Double}       data.dicp			设备充电量统计
	 * @apiSuccess {Double}       data.dicl 			设备损耗统计
	 * @apiSuccess {Double}       data.conEff 			充电效率统计
	 * @apiSuccess {Double}       data.diti 		 	充电收入统计
	 * @apiSuccess {Double}       data.ditsf 	 	 充电侧分成服务费统计
	 * @apiSuccess {Double}       data.diepcs    	用电侧分成服务费统计
	 * @apiSuccess {Double}       data.ditip     	分成收入统计
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
	 */
	@RequestMapping(value = "/divide/_count", method = RequestMethod.GET)
	public ResultVo selectFcsrCount(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			DataVo dataVo=chargeInService.selectFcsrCount(vo);
			resultVo.setData(dataVo);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/divide/_export   分成收入导出
	 * @apiName exportFcsr
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    分成收入导出
	 * <br/>
	 * @apiParam {int}       userId                                      用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]                            场站Id
	 * @apiParam {int}        [contractId]                            合约Id
	 * @apiParam {String}        [stationName]                            场站名称 模糊查询
	 * @apiParam {String}        [contractName]                            合约名称 模糊查询
	 * @apiParam {int}           pageNum                               页码
	 * @apiParam {int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失!
	 */

	@RequestMapping(value = "/divide/_export", method = RequestMethod.GET)
	public void exportFcsr(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo vo = new DataVo(map);
		if(vo.isNotBlank("userId")){
			chargeInService.exportFcsr(vo,response);
		}else {

			throw   new BizException(1700000,"用户Id");
		}
	}

	/**
	 * @api {get} /api/chargein/divide/detail 分成收入详情
	 * @apiName detailIncome
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   分成收入详情
	 * <br/>
	 * @apiParam {Integer}             nodeId                              分成收入Id(分成列表的 contractId)
	 * @apiParam {Date}               did                                结算时间(yyyy-mm-dd)
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                      错误码
	 * @apiSuccess {String}       errorMsg                      消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {String}       data.list.didStr                     结算日期(yyyy-mm-dd)
	 * @apiSuccess {String}       data.list.conTractName              合约名称
	 * @apiSuccess {String}      data.list.stationName               合约场站
	 * @apiSuccess {String}      data.list.pileName                  设备名称
	 * @apiSuccess {Double}      data.list.ditp                      用电量
	 * @apiSuccess {Double}      data.list.diep                      设备用电量
	 * @apiSuccess {Double}      data.list.dicp                      车充电量(kW·h)
	 * @apiSuccess {Double}      data.list.dicl                      充电设备损耗(kW·h)
	 * @apiSuccess {Double}      data.list.diel                      其他设备用电损耗(kW·h)
	 * @apiSuccess {Double}      data.list.diec                      用电成本
	 * @apiSuccess {Double}      data.list.diti                      充电收入
	 * @apiSuccess {Double}      data.list.conEff                      充电效率
	 * @apiSuccess {Double}      data.list.ditsf                     服务费
	 * @apiSuccess {Double}      data.list.ditcsf                    分成服务费
	 * @apiSuccess {Double}      data.list.hm                        合约企业&分成比例&分成金额(hm对象key值和data.pileId 取得values值)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000  参数缺失!
	 */

	@RequestMapping(value = "/divide/detail", method = RequestMethod.GET)
	public ResultVo detailIncome(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		PageInfo pageList = chargeInService.detailIncome(map);
		resultVo.setData(pageList);
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/invoice   发票管理查询
	 * @apiName selectFpgl
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    发票管理查询
	 * <br/>
	 * @apiParam {int}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {String}        [consName]                               会员名称
	 * @apiParam {String}        [recipientPhone]                         手机号
	 * @apiParam {int}        [isInvoice]                              状态
	 * @apiParam {String}        [buyName]                                购方名称
	 * @apiParam {String}        [recipientName]                          收件人
	 * @apiParam {int}           pageNum                               页码
	 * @apiParam {int}           pageSize                             页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {int}           data.list.invoiceId                    		   列表主键
	 * @apiSuccess {String}        data.list.consName                    		   会员名称
	 * @apiSuccess {Double}       data.list.applyMoney                         开票金额(元)
	 * @apiSuccess {String}       data.list.buyName                    		   购方名称
	 * @apiSuccess {Double}       data.list.canAmount                    		  可开票金额(元)
	 * @apiSuccess {Date}         data.list.applyTime                    		   申请时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}       data.list.isInvoice                    		   处理状态
	 * @apiSuccess {String}       data.list.recipientAddr                      收件地址
	 * @apiSuccess {String}       data.list.recipientPhone                     手机号
	 * @apiSuccess {String}       data.list.recipientName                      收件人姓名
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数错误!
	 */
	@RequestMapping(value = "/invoice", method = RequestMethod.GET)
	public ResultVo selectFpgl(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.selectFpgl(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/invoice/_count   发票管理合计
	 * @apiName selectFpglCount
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    发票管理合计
	 * <br/>
	 * @apiParam {int}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {String}        [consName]                               会员名称
	 * @apiParam {String}        [recipientPhone]                         手机号
	 * @apiParam {int}        [isInvoice]                              状态
	 * @apiParam {String}        [buyName]                                购方名称
	 * @apiParam {String}        [recipientName]                          收件人
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {double}         data                    		      开票金额汇总
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数错误!
	 */


	@RequestMapping(value = "/invoice/_count", method = RequestMethod.GET)
	public ResultVo selectFpglCount(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			double amount=chargeInService.selectFpglCount(vo);
			resultVo.setData(amount);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/invoice/_export   发票管理导出
	 * @apiName exportFpgl
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    发票管理导出
	 * <br/>
	 * @apiParam {int}           userId                                 用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {String}        [consName]                               会员名称
	 * @apiParam {String}        [recipientPhone]                         手机号
	 * @apiParam {int}        [isInvoice]                              状态
	 * @apiParam {String}        [buyName]                                购方名称
	 * @apiParam {String}        [recipientName]                          收件人
	 * @apiParam {int}           pageNum                               页码
	 * @apiParam {int}           pageSize                             页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 请求方式异常!
	 */

	@RequestMapping(value = "/invoice/_export", method = RequestMethod.GET)
	public void exportFpgl(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataMap =  new DataVo(map);
		if(dataMap.isNotBlank("userId")) {
			chargeInService.exportFpgl(dataMap, response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}

	}
	/**
	 * @api {get} /api/chargein/invoice/detail/{invoiceId}    发票详情查询
	 * @apiName detailInvoice
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    发票详情查询
	 * <br/>
	 * @apiParam {String}           invoiceId                            发票id
	 * <br/>
	 * @apiSuccess {String}       errorCode                      错误码
	 * @apiSuccess {String}       errorMsg                      消息说明
	 * @apiSuccess {String}       data                    		 更新成功返回值
	 * @apiSuccess {Int}        data.expressCompany          快递公司字典
	 * @apiSuccess {String}        data.expressNo              快递公司订单号
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value = "/invoice/detail/{invoiceId}", method = RequestMethod.GET)
	public ResultVo detailInvoice(@PathVariable("invoiceId") Integer invoiceId) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo result=chargeInService.detailFpgl(invoiceId);
		resultVo.setData(result);
		return resultVo;
	}
	/**
	 * @api {get} /api/chargein/station   场站收入查询
	 * @apiName selectCzsr
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    场站收入查询
	 * <br/>
	 * @apiParam {Int}           userId                               用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {int}        [stationId]                            场站Id
	 * @apiParam {int}        [stationName]                            场站名称 模糊查询
	 * @apiParam {int}        [orgId]                             运营商Id
	 * @apiParam {int}           pageNum                               页码
	 * @apiParam {int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Object[]}       data.list.czsr                   		      场站收入集合
	 * @apiSuccess {String}       data.list.czsr.stationId 				     主键（列表Id）
	 * @apiSuccess {String}       data.list.czsr.stationNo				     场站编号
	 * @apiSuccess {String}       data.list.czsr.stationName                   场站名
	 * @apiSuccess {Double}       data.list.czsr.amount                     总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee                    电费总收入(元)
	 * @apiSuccess {Double}      data.list.czsr.noElceFee                  非费率总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee1                                                         尖价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee2                                                         峰价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee3                                                         平价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee4                                                         谷价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.servFee                    服务费收入(元/kW·h)
	 * @apiSuccess {Double}       data.list.czsr.parkFee                    停车费收入(元)
	 * @apiSuccess {Double}       data.list.czsr.chgPower                   充电总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.noChgPower                 非费率电量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg1 			                                       尖价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg2                                                   峰价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg3                                                  平价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg4                                                  谷价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.orgName                                                     运营商名称
	 * @apiSuccess {Double}       data.list.czsr.endTime                                                     统计时间
	 * @apiSuccess {Object[]}       data.list.count                   		      场站统计
	 * @apiSuccess {Double}       data.list.count.amount                     总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee                    电费总收入合计(元)
	 * @apiSuccess {Double}      data.list.count.noElceFee                  非费率总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee1                                                         尖价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee2                                                         峰价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee3                                                         平价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee4                                                         谷价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.servFee                    服务费收入合计(元/kW·h)
	 * @apiSuccess {Double}       data.list.count.parkFee                    停车费收入合计(元)
	 * @apiSuccess {Double}       data.list.count.chgPower                   充电总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.noChgPower                 非费率电量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg1 			                                       尖价总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg2                                                   峰价总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg3                                                  平价总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg4                                                  谷价总量合计(kW·h)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/station", method = RequestMethod.GET)
	public ResultVo selectCzsr(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.selectCzsr(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/station/_export   场站收入导出
	 * @apiName exportCzsr
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    场站收入导出
	 * <br/>
	 * @apiParam {Int}          userId                                  用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd)
	 * @apiParam {Int}        [stationId]                               场站Id
	 * @apiParam {int}        [stationName]                            场站名称 模糊查询
	 * @apiParam {Int}           pageNum                              页码
	 * @apiParam {Int}           pageSize                            页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -1700000  参数缺失
	 */
	@RequestMapping(value = "/station/_export", method = RequestMethod.GET)
	public void exportCzsr(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataVo  = new DataVo(map);
		if(dataVo.isNotBlank("userId")){
			chargeInService.exportCzsr(dataVo,response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}

	/**
	 * @api {get} /api/chargein/stationDetail 场站收入详情
	 * @apiName detailStation
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   场站收入详情
	 * <br/>
	 * @apiParam {Integer}        nodeId                              场站收入Id(列表的stationId)
	 * @apiParam {Date}           startDate                           开始时间(yyyy-mm-dd)
	 * @apiParam {Date}           endDate                           结束时间(yyyy-mm-dd)
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                      错误码
	 * @apiSuccess {String}       errorMsg                      消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Object[]}       data.list.czsr                   		      场站收入集合
	 * @apiSuccess {String}       data.list.czsr.stationId 				     主键（列表Id）
	 * @apiSuccess {String}       data.list.czsr.stationNo				     场站编号
	 * @apiSuccess {String}       data.list.czsr.stationName                   场站名
	 * @apiSuccess {Double}       data.list.czsr.amount                     总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee                    电费总收入(元)
	 * @apiSuccess {Double}      data.list.czsr.noElceFee                  非费率总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee1                                                         尖价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee2                                                         峰价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee3                                                         平价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.elceFee4                                                         谷价总收入(元)
	 * @apiSuccess {Double}       data.list.czsr.servFee                    服务费收入(元/kW·h)
	 * @apiSuccess {Double}       data.list.czsr.parkFee                    停车费收入(元)
	 * @apiSuccess {Double}       data.list.czsr.chgPower                   充电总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.noChgPower                 非费率电量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg1 			                                       尖价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg2                                                   峰价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg3                                                  平价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.powerZxyg4                                                  谷价总量(kW·h)
	 * @apiSuccess {Double}       data.list.czsr.orgName                                                     运营商名称
	 * @apiSuccess {Double}       data.list.czsr.endTime                                                     统计时间
	 * @apiSuccess {Object[]}       data.list.count                   		      场站统计
	 * @apiSuccess {Double}       data.list.count.amount                     总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee                    电费总收入合计(元)
	 * @apiSuccess {Double}      data.list.count.noElceFee                  非费率总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee1                                                         尖价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee2                                                         峰价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee3                                                         平价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.elceFee4                                                         谷价总收入合计(元)
	 * @apiSuccess {Double}       data.list.count.servFee                    服务费收入合计(元/kW·h)
	 * @apiSuccess {Double}       data.list.count.parkFee                    停车费收入合计(元)
	 * @apiSuccess {Double}       data.list.count.chgPower                   充电总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.noChgPower                 非费率电量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg1 			                                       尖价总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg2                                                   峰价总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg3                                                  平价总量合计(kW·h)
	 * @apiSuccess {Double}       data.list.count.powerZxyg4                                                  谷价总量合计(kW·h)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000  参数缺失!
	 */
	@RequestMapping(value = "/stationDetail", method = RequestMethod.GET)
	public ResultVo detailStation(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo data = new DataVo(map);
		if(data.isNotBlank("nodeId")){
			PageInfo pageList =chargeInService.detaiStationDate(data);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"场站收入Id");
		}

		return resultVo;
	}
	/**
	 * @api {get} /api/chargein/stationDetail/date/_export 场站收入详情导出
	 * @apiName detailStationDateExport
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   场站收入详情导出
	 * <br/>
	 * @apiParam {Integer}        nodeId                              场站收入Id(列表的stationId)
	 * @apiParam {Date}           startDate                           开始时间(yyyy-mm-dd)
	 * @apiParam {Date}           endDate                           结束时间(yyyy-mm-dd)
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1700000  参数缺失
	 */
	@RequestMapping(value = "/stationDetail/date/_export", method = RequestMethod.GET)
	public void detailStationDateExport(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataVo= new DataVo(map);
		if(dataVo.isNotBlank("nodeId")){
			chargeInService.exportCzsrDate(dataVo,response);
		}else {
			throw   new BizException(1700000," 场站收入Id");
		}
	}
	/**
	 * @api {get} /api/chargein/stationDetail/_export 场站收入汇总导出
	 * @apiName detailStationExport
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   场站收入详情导出
	 * <br/>
	 * @apiParam {Integer}        nodeId                              场站收入Id(列表的stationId)
	 * @apiParam {Date}           startDate                           开始时间(yyyy-mm-dd)
	 * @apiParam {Date}           endDate                           结束时间(yyyy-mm-dd)
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1700000  参数缺失
	 */
	@RequestMapping(value = "/stationDetail/_export", method = RequestMethod.GET)
	public void detailStationExport(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataVo= new DataVo(map);
		if(dataVo.isNotBlank("nodeId")){
			chargeInService.detailStationExport(dataVo,response);
		}else {
			throw   new BizException(1700000," 场站收入Id");
		}
	}
	/**
	 * @api {get} /api/chargein/tobe   待结收入查询
	 * @apiName selectDjsr
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    待结收入查询
	 * <br/>
	 * @apiParam {Int}        userId                              	         用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Int}        [pileType]                                设备类型
	 * @apiParam {Int}        [orgId]                              	   运营商
	 * @apiParam {Int}        [orgName]                              	   运营商姓名 模糊查询
	 * @apiParam {Int}        [consTypeCode]                           会员类型  （字典表取值hylb，01：个人会员 02：集团会员）
	 * @apiParam {String}        [rechargeCard]                           充值卡号
	 * @apiParam {int}        [stationId]                            场站Id
	 * @apiParam {String}        [stationName]                            场站名称 模糊查询
	 * @apiParam {Int}        [payType]                                支付方式   (字典表取值shoukfs,1:钱包，2:月结 3,微信 4,支付宝,5,储值卡)
	 * @apiParam {Int}        [groupId]                              集团Id
	 * @apiParam {String}        [groupName]                              集团名称 模糊查询
	 * @apiParam {String}        [pileName]                               设备名称
	 * @apiParam {String}        [consPhone]                              手机号
	 * @apiParam {Int}        [payType]                               支付状态订单状态  （字典表取值zfzt，1：待结算（这里只用取待结算））
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Object[]}       data.list.djsr                    		     待结收入列表数据
	 * @apiSuccess {Int}          data.list.djsr.billPayId       主键(列表Id)
	 * @apiSuccess {String}       data.list.djsr.orgName       运营商
	 * @apiSuccess {String}       data.list.djsr.billPayNo     订单编号
	 * @apiSuccess {Double}       data.list.djsr.amount         消费金额(元)
	 * @apiSuccess {Double}       data.list.djsr.chgPower		 充电电量(kW·h)
	 * @apiSuccess {String}       data.list.djsr.groupName		集团名称
	 * @apiSuccess {String}       data.list.djsr.consName		会员名称
	 * @apiSuccess {String}       data.list.djsr.consTypeCode    会员类型
	 * @apiSuccess {String}       data.list.djsr.rechargeCard    充值卡号
	 * @apiSuccess {Double}       data.list.djsr.elceFee      电费(元)
	 * @apiSuccess {Double}       data.list.djsr.servFee       服务费(元/kW·h)
	 * @apiSuccess {Double}       data.list.djsr.parkFee       停车费(元)
	 * @apiSuccess {String}       data.list.djsr.payState       订单状态
	 * @apiSuccess {String}       data.list.djsr.payType      支付方式
	 * @apiSuccess {Date}       data.list.djsr.finishTime      支付时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}       data.list.djsr.stationName     场站名称
	 * @apiSuccess {String}       data.list.djsr.pileType        设备类型
	 * @apiSuccess {String}       data.list.djsr.pileNo          设备编号
	 * @apiSuccess {String}       data.list.djsr.pileName        设备名称
	 * @apiSuccess {String}       data.list.djsr.consPhone       手机号
	 * @apiSuccess {Date}       data.list.djsr.createTime      订单时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {Date}       data.list.djsr.startTime       开始充电时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {Date}       data.list.djsr.endTime       结束充电时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}       data.list.djsr.useTime       充电时长(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}      data.list.djsr.outBillNo        备注
	 * @apiSuccess {String}      data.list.count.totalPowerCount        充电量合计
	 * @apiSuccess {String}      data.list.count.totalMoneyCount        金额合计
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/tobe1", method = RequestMethod.GET)
	public ResultVo selectDjsr1(@RequestParam Map map) throws Exception {
		DataVo vo  =  new DataVo(map);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.selectDjsr(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	
	/**
	 * @api {get} /api/chargein/tobe   待结收入-熊岳
	 * @apiName selectDjsr2
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 熊岳    待结收入查询
	 * <br/>
	 * @apiParam {Int}        userId                              	        用户Id
	 * @apiParam {Date}       [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Date}       [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Int}        [orgId]                              	        运营商ID
	 * @apiParam {int}        [stationId]                              场站Id
	 * @apiParam {String}     [billPayNo]                              订单编号(支持模糊查询)
	 * @apiParam {String}     [consPhone]                              手机号(支持模糊查询)
	 * @apiParam {Int}        pageNum                                  页码
	 * @apiParam {Int}        pageSize                                 页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Object[]}       data.list.djsr                    		     待结收入列表数据
	 * @apiSuccess {Int}            data.list.djsr.billPayId       主键(列表Id)
	 * @apiSuccess {Int}            data.list.djsr.type            类型1:APP桩已经停止充电,2:刷卡异常订单,3:APP正在充电中的
	 * @apiSuccess {String}         data.list.djsr.billPayNo       订单编号
	 * @apiSuccess {Double}         data.list.djsr.amount          消费金额(元)
	 * @apiSuccess {Double}         data.list.djsr.chgPower		       充电电量(kW·h)
	 * @apiSuccess {String}         data.list.djsr.payState        订单状态
	 * @apiSuccess {String}         data.list.djsr.payType         支付方式
	 * @apiSuccess {String}         data.list.djsr.orgName         运营商
	 * @apiSuccess {String}         data.list.djsr.stationName     场站名称
	 * @apiSuccess {String}         data.list.djsr.consName		       会员名称
	 * @apiSuccess {String}         data.list.djsr.consPhone       手机号
	 * @apiSuccess {String}         data.list.djsr.pileNo          设备编号
	 * @apiSuccess {String}         data.list.djsr.pileName        设备名称
	 * @apiSuccess {String}         data.list.djsr.pileAddr        通信地址
	 * @apiSuccess {Date}           data.list.djsr.createTime      订单时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {Date}           data.list.djsr.startTime       开始充电时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {Date}           data.list.djsr.endTime         结束充电时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}         data.list.djsr.useTime         充电时长(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}         data.list.djsr.billDesc        备注(由outBillNo改为billDesc)
	 * @apiSuccess {String}         data.list.count.totalPowerCount        充电量合计
	 * @apiSuccess {String}         data.list.count.totalMoneyCount        金额合计
	 * <br/>
	 * @apiSuccessExample {json} Success输出示例
	  {
    	"errorCode":0,
    	"errorMsg":"操作成功!",
    	"total":1,
    	"data":{
        	"list":[
            	{
	                "count":{
	                    "totalMoneyCount":"0.00",
	                    "totalPowerCount":"0.00"
	                },
	                "djsr":[
	                    {
	                        "type":3,
	                        "billPayId":804758,
	                        "billPayNo":"PAY201708301740046034",
	                        "chgPower":null,
	                        "amount":null,
	                        "useTime":null,
	                        "payState":1,
	                        "payType":null,
	                        "billDesc":null,
	                        "orderStatus":1,
	                        "consName":null,
	                        "consPhone":"17079335798",
	                        "stationName":"中亚硅谷充电站",
	                        "orgName":"深圳市车电网络有限公司",
	                        "pileNo":"000000001004006",
	                        "pileName":null,
	                        "pileAddr":"10010013",
	                        "gunNo":3,
	                        "createTime":"2017-08-30 17:40:04",
	                        "startTime":null,
	                        "endTime":null
	                    }
	                ]
            	}
        	],
        },
    	"exception":null
	  }
	 * @apiError -999 系统异常!
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/tobe", method = RequestMethod.GET)
	public ResultVo selectDjsr(@RequestParam Map map) throws Exception {
		DataVo vo  =  new DataVo(map);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=ac.queryAbnormalBill(map);
			resultVo.setData(pageList);
			resultVo.setTotal(pageList.getSize());
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	
	@Autowired
	AbnormalCharge ac;
	/**
	 * @api {get} /api/chargein/abnormalBillInfo/{billPayId} 异常结单查询-熊岳
	 * @apiName queryAbnormalBillInfo
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 熊岳    获取待结异常订单信息
	 * <br/> 
	 * @apiParam {Int}            billPayId                        订单ID
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}       data                    		       数据封装
	 * @apiSuccess {Integer}        data.billPayId                 主键(订单Id)
	 * @apiSuccess {String}         data.billPayNo                 订单编号
	 * @apiSuccess {String}         data.readOnly                  能否编辑时长，电量，金额(true不可变价,false可编辑)
	 * @apiSuccess {Integer}        data.startTime                 充电开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {Integer}        data.chargeTimes               充电时长,单位分钟，页面可以编辑，保存存进来
	 * @apiSuccess {Double}         data.chgPower                  充电电量,单位kWh,页面可以编辑，保存存进来
	 * @apiSuccess {Double}         data.amount                    充电金额,单位元,页面可以编辑，保存存进来
	 * @apiSuccess {Integer}        data.busiType                  订单业务处理类型，刷卡0不需要下发停止命令  1:app有结束事件，2,app无结束事件需下发停止命令 保存的时候原值传进来
	 * @apiSuccess {Integer}        data.msg                       保存前提示信息
	 * <br/>
	 * @apiSuccessExample {json}    Success出参示例:
      {
        errorCode: 0,
        errorMsg: "操作成功!",
        data: {
             billPayId: "2323",
             billPayNo: "PAY201708021055393850"
             readOnly:true
             startTime: "2017-8-1 12:23:23"
             chargeTimes: 23
             chgPower:10
             amount:20
             busiType:2
        }
      }
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失
	 * @apiError -2103001 根据订单ID查询不到订单信息
	 */
	@RequestMapping(value="/abnormalBillInfo/{billPayId}",method=RequestMethod.GET)
	public ResultVo queryAbnormalBillInfo(@PathVariable("billPayId") Integer billPayId) throws Exception{
		ResultVo resultVo = new ResultVo();
		if(billPayId!=null && billPayId>0){
			Map<String,Object>  map = ac.getAbnormalChargeInfo(billPayId);
			if(map!=null){
				resultVo.setData(map);
			}else{
				throw new BizException(2103001,billPayId);
			}
		}else{
			throw new BizException(1700000,"订单Id");
		}
		return resultVo;
	}
	
	
	/**
	 * @api {put} /api/chargein/abnormalBill    异常结单处理
	 * @apiName saveAbnormalBill
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 熊岳    异常结单处理
	 * <br/> 
	 * @apiParam {Integer}        billPayId                        订单ID
	 * @apiParam {Integer}        startTime                        充电开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Integer}        chargeTimes                      充电时长,单位分钟，页面可以编辑，保存存进来
	 * @apiParam {Double}         chgPower                         充电电量,单位kWh,页面可以编辑，保存存进来
	 * @apiParam {Double}         amount                           充电金额,单位元,页面可以编辑，保存存进来
	 * @apiParam {Integer}        busiType                         订单业务处理类型，查询时给出的       
	 * <br/> 
	 * @apiParamExample {json} 入参示例:
     * {
     *  "billPayId":2323,
     *  "startTime": "2017-8-1 12:23:23",
     *  "chargeTimes": 23,
     *  "chgPower": 10.23,
     *  "amount": 20.46,
     *  "busiType":1
     * }
     * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}       data                    		       数据封装
	 * <br/>
	 * @apiSuccessExample {json}    Success出参示例:
      {
        errorCode: 0,
        errorMsg: "操作成功!",
        data: {}
      }
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -999 系统异常!
	 * @apiError -1700000 参数缺失
	 * @apiError -2103002充电开始时间格式错误
	 * @apiError -2103003=异常结单遇到异常:{0}
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/abnormalBill",method=RequestMethod.PUT)
	public ResultVo saveAbnormalBill(@RequestBody Map data)throws Exception{
		Object o = data.get("billPayId");
		if(o==null || "".equals(o.toString().trim())){
			throw new BizException(1700000,"订单Id");
		}
		Integer billId = Integer.valueOf(o.toString().trim());
		o = data.get("startTime");
		if(o==null || "".equals(o.toString().trim())){
			throw new BizException(1700000,"充电开始时间");
		}
		Calendar startTime = CalendarUtils.getCalendar(o.toString());
		if(startTime==null){
			throw new BizException(2103002,o.toString());
		}
		o = data.get("busiType");
		if(o==null || "".equals(o.toString().trim())){
			throw new BizException(1700000,"异常订单业务处理类型");
		}
		Integer busiType = Integer.valueOf(o.toString());
		o = data.get("chargeTimes");
		if(o==null || "".equals(o.toString().trim())){
			throw new BizException(1700000,"充电时长");
		}
		Integer chargeTimes = Integer.valueOf(o.toString());
		o = data.get("chgPower");
		if(o==null || "".equals(o.toString().trim())){
			throw new BizException(1700000,"充电电量");
		}
		Double chgPower = Double.valueOf(o.toString());
		o = data.get("amount");
		if(o==null || "".equals(o.toString().trim())){
			throw new BizException(1700000,"充电金额");
		}
		Double amount = Double.valueOf(o.toString());
		String msg = ac.saveAbnormalCharge(billId, busiType, startTime, chargeTimes, chgPower, amount);
		ResultVo resultVo = new ResultVo(msg);
		return resultVo;
	}

	/**
	 * @api {get} /api/chargein/tobe/_export   待结收入导出
	 * @apiName exportDjsr
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    待结收入导出
	 * <br/>
	 * @apiParam {Int}        userId                              	         用户Id
	 * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {Int}        [pileType]                                设备类型
	 * @apiParam {Int}        [orgId]                              	   运营商
	 * @apiParam {Int}        [orgName]                              	   运营商姓名 模糊查询
	 * @apiParam {Int}        [consTypeCode]                           会员类型  （字典表取值hylb，01：个人会员 02：集团会员）
	 * @apiParam {String}        [rechargeCard]                           充值卡号
	 * @apiParam {int}        [stationId]                            场站Id
	 * @apiParam {String}        [stationName]                            场站名称 模糊查询
	 * @apiParam {Int}        [payType]                                支付方式   (字典表取值shoukfs,1:钱包，2:月结 3,微信 4,支付宝,5,储值卡)
	 * @apiParam {Int}        [groupId]                              集团Id
	 * @apiParam {String}        [groupName]                              集团名称 模糊查询
	 * @apiParam {String}        [pileName]                               设备名称
	 * @apiParam {String}        [consPhone]                              手机号
	 * @apiParam {Int}        [payType]                               支付状态订单状态  （字典表取值zfzt，1：待结算（这里只用取待结算））
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1700000  请求方式异常
	 */

	@RequestMapping(value = "/tobe/_export", method = RequestMethod.GET)
	public void exportDjsr(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo vo  = new DataVo(map);
		if(vo.isNotBlank("userId")){
			//chargeInService.exportDjsr(vo,response);
			ac.exportDjsr(map, response);
		}else {

			throw   new BizException(1700000,"用户Id");
		}

	}


	/**
	 * @api {get} /api/chargein/other   第三方充电收入查询
	 * @apiName selectCdsrCoop
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    第三方充电收入查询
	 * <br/>
	 * @apiParam {Integer}             userId                                 用户Id
	 * @apiParam {String}          [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {String}          [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {String}         [operatorId]                              	         运营商ID-组织机构代码
	 * @apiParam {String}         [operatorName]                              	         运营商组织机构名称 模糊查询
	 * @apiParam {String}        [startChargeSeq]                            流水订单号
	 * @apiParam {String}         [billStatus]                               支付状态（字典表取值zfzt，1：待结算 2：未付费 3:已付费 4:异常付费）
	 * @apiParam {String}        [billPayNo]                              内部订单号
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {String}       data.list.operatorName                    		   客户运营商
	 * @apiSuccess {String}       data.list.equipmentOperatorName                   设备运营商
	 * @apiSuccess {String}       data.list.startChargeSeq                    		   订单流水号
	 * @apiSuccess {String}       data.list.billPayNo                    		   内部订单编号
	 * @apiSuccess {String}       data.list.totalMoney                  		   消费金额(元)
	 * @apiSuccess {String}       data.list.totalPower                  		  充电电量(kW·h)
	 * @apiSuccess {String}       data.list.billStatus                    		   支付状态
	 * @apiSuccess {String}       data.list.payType                    		      支付方式
	 * @apiSuccess {Date}       data.list.startTime                    		   开始充电时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {Date}       data.list.endTime                    		  结束充电时间(yyyy-mm-dd HH:mm:ss)
	 * @apiSuccess {String}       data.list.useTime                    		   充电时长
	 * @apiSuccess {String}       data.list.consName                    		   会员名称
	 * @apiSuccess {String}       data.list.connectorId                    		   接口编号
	 * @apiSuccess {String}       data.list.equipmentId                    		   设备编号
	 * @apiSuccess {String}       data.list.stationName                    		   场站名称
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/other", method = RequestMethod.GET)
	public ResultVo selectCdsrCoop(@RequestParam Map data) throws Exception {
		 DataVo vo  =  new DataVo(data);
		 ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.selectCdsrCoop(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	/**
	 * @api {get} /api/chargein/other/_count   第三方充电收入合计
	 * @apiName selectCdsrCoopCount
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯    第三方充电收入合计
	 * <br/>
	 * @apiParam {Integer}             userId                                 用户Id
	 * @apiParam {String}          [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {String}          [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {String}         [operatorId]                              	         运营商ID-组织机构代码
	 * @apiParam {String}         [operatorName]                              	         运营商组织机构名称 模糊查询
	 * @apiParam {String}        [startChargeSeq]                            流水订单号
	 * @apiParam {String}         [billStatus]                               支付状态（字典表取值zfzt，1：待结算 2：未付费 3:已付费 4:异常付费）
	 * @apiParam {String}        [billPayNo]                              内部订单号
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {String}       data.totalPower                    		   合计电量
	 * @apiSuccess {String}       data.totalMoney                    		   合计消费金额
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/other/_count", method = RequestMethod.GET)
	public ResultVo selectCdsrCoopCount(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			DataVo pageList=chargeInService.selectCdsrCoopCount(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	/**
	 * @api {get} /api/chargein/other/_export   第三方充电导出
	 * @apiName exprotCdsrCoop
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   第三方充电导出
	 * <br/>
	 * @apiParam {Integer}             userId                                 用户Id
	 * @apiParam {String}          [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {String}          [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
	 * @apiParam {String}         [operatorId]                              	         运营商ID-组织机构代码
	 * @apiParam {String}         [operatorName]                              	         运营商组织机构名称 模糊查询
	 * @apiParam {String}        [startChargeSeq]                            流水订单号
	 * @apiParam {String}         [billStatus]                               支付状态（字典表取值zfzt，1：待结算 2：未付费 3:已付费 4:异常付费）
	 * @apiParam {String}        [billPayNo]                              内部订单号
	 * @apiParam {Integer}           pageNum                               页码
	 * @apiParam {Integer}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/other/_export", method = RequestMethod.GET)
	public void exprotCdsrCoop(@RequestParam Map data,HttpServletResponse response) throws Exception {
		DataVo dataVo = new DataVo(data);
		if(dataVo.isNotBlank("userId")){
			chargeInService.exprotCdsrCoop(dataVo,response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}


	}


    /**
     * @api {put} /api/chargein/invoice/handle    发票信息更新
     * @apiName handleInvoice
     * @apiGroup ChargeInController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    发票信息更新
     * <br/>
     * @apiParam {String}           invoiceId                            发票
	 * @apiParam {String}           expressNo                         快递公司订单号
	 * @apiParam {String}           expressCompany                    快递公司数据字典Id
     * <br/>
     * @apiSuccess {String}       errorCode                      错误码
     * @apiSuccess {String}       errorMsg                      消息说明
     * @apiSuccess {String}       data                    		 更新成功返回值
     * <br/>
     * @apiError -999 系统异常!
	 * @apiError -888  请求方式异常
	 * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/invoice/handle", method = RequestMethod.PUT)
    public ResultVo handleInvoice(@RequestBody Map map) throws Exception {
        ResultVo resultVo = new ResultVo();
        DataVo data = new DataVo(map);
        if(data.isNotBlank("invoiceId")&&data.isNotBlank("expressNo")&&data.isNotBlank("expressCompany")){
			map.put("isInvoice",1);
        	Integer result=chargeInService.updateFpgl(map);
			resultVo.setData(result);
		}else {
			throw   new BizException(1700000,"发票Id,快递公司订单号,快递公司数据字典Id");
		}

        return resultVo;
    }
    /**
     * 
     * @api {get} /api/chargein/_print  充电信息小票打印
     * @apiName printBillPay
     * @apiGroup ChargeInController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅B 根据订单Id查询充电信息
     * <br/>
     * @apiParam {Integer}    billPayId     参数名
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {Integer} data.powerZxyg3  
     * @apiSuccess {Integer} data.powerZxyg4  
     * @apiSuccess {Integer} data.powerZxyg2   
     * @apiSuccess {Integer} data.innerId   
     * @apiSuccess {Integer} data.billPayNo  
     * @apiSuccess {Integer} data.line  
     * @apiSuccess {Integer} data.endTime
     * @apiSuccess {Integer} data.servFee  
     * @apiSuccess {Integer} data.amountTotal   
     * @apiSuccess {Integer} data.startTime
     * @apiSuccess {Integer} data.amount  
     * @apiSuccess {Integer} data.pileNo  
     * @apiSuccess {Integer} data.billDesc   
     * @apiSuccess {Integer} data.pileName  
     * @apiSuccess {Integer} data.stationName  
     * @apiSuccess {Integer} data.pileId   
     * @apiSuccess {Integer} data.amountZxyg2  
     * @apiSuccess {Integer} data.amountZxyg3   
     * @apiSuccess {Integer} data.consId   
     * @apiSuccess {Integer} data.licenseplate   
     * @apiSuccess {Integer} data.chargeDateStr   
     * @apiSuccess {Integer} data.prcZxygz2   
     * @apiSuccess {Integer} data.prcZxygz3  
     * @apiSuccess {Integer} data.consPhone   
     * @apiSuccess {Integer} data.consName   
     * @apiSuccess {Integer} data.prcZxygz4  
     * @apiSuccess {Integer} data.orgName   
     * @apiSuccess {Integer} data.pileType   
     * @apiSuccess {Integer} data.servPrice  
     * @apiSuccess {Integer} data.payType   
     * @apiSuccess {Integer} data.powerTotal   
     * @apiSuccess {Integer} data.amountZxyg4   
     * <br/>
     * @apiError -999  系统异常
     * @apiError -888  请求方式异常
     */
    @RequestMapping(value="_print",method=RequestMethod.GET)
    public ResultVo printBillPay(@RequestParam Map map) throws Exception{
    	ResultVo resVo = new ResultVo();
    	Map chargeMap = chargeInService.printBillPay(map);
    	resVo.setData(chargeMap);
    	return resVo;
    }
    
    @RequestMapping(value="_prints",method=RequestMethod.GET)
    public ResultVo printBillPays(@RequestParam Map map) throws Exception{
    	ResultVo resVo = new ResultVo();
    	List list = chargeInService.printBillPays(map);
    	resVo.setData(list);
    	return resVo;
    }

	/**
	 *
	 * @api {get} /api/chargein/divide/conName   查询分成收入合约名称
	 * @apiName divideConName
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   查询分成收入合约名称
	 * <br/>
	 * @apiParam {Integer}           userId                               用户Id
	 * @apiParam {Integer}           pageNum                               页码(默认为1)
	 * @apiParam {Integer}           pageSize                              页大小（默认为50）
	 * @apiParam {Integer}           contractName                            合约名称(模糊查询)
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {String}       data.list.contractId                    		   合约id
	 * @apiSuccess {String}       data.list.contractName                   		   合约名称
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/divide/conName", method = RequestMethod.GET)
	public ResultVo divideConName(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.divideConName(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	      	return resultVo;
	}

	/**
	 *
	 * @api {get} /api/chargein/divide/stationName   查询分成收入合约场站
	 * @apiName divideStationName
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   查询分成收入合约场站
	 * <br/>
	 * @apiParam {Integer}           userId                               用户Id
	 * @apiParam {Integer}           pageNum                               页码(默认为1)
	 * @apiParam {Integer}           pageSize                              页大小（默认为50）
	 * @apiParam {Integer}           stationName                              场站名称(模糊查询)
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {String}       data.list.stationId                    		   场站Id
	 * @apiSuccess {String}       data.list.stationName                   		   场站名称
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
	 */
	@RequestMapping(value = "/divide/stationName", method = RequestMethod.GET)
	public ResultVo divideStationName(@RequestParam Map data) throws Exception {
		DataVo vo  =  new DataVo(data);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=chargeInService.divideStationName(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	
	/**
	 * 定时器补跑数据
	 * 
	 * @api {get} /api/chargein/billPayHistory  补定时器数据
	 * @apiName getBillPayByHistory
	 * @apiGroup ChargeInController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 补定时器数据-充电时长-收入指标
	 * <br/>
	 * @apiParam {String}    endTime   截止时间(定时器截止时间)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="/billPayHistory",params="ver=2.0.1",method = RequestMethod.GET)
	public ResultVo getBillPayByHistory(@RequestParam Map map){
	    chargeInService.getBillPayByHistory(map);
		return new ResultVo();
	}
	
}





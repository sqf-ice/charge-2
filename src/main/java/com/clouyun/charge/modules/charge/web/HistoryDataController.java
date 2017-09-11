package com.clouyun.charge.modules.charge.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.service.HistoryDataService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述: 历史数据控制器 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: lips <lips@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年4月14日
 */
@RestController
@RequestMapping("/api/historydata")
public class HistoryDataController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(HistoryDataController.class);
	
	@Autowired HistoryDataService historyDataService;
	
	
	
	 /**
     * @api {get} /api/historydata/historydata   历史数据查询
     * @apiName selectHistoryData
     * @apiGroup HistoryDataController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    历史数据查询
     * <br/>
	 * @apiParam {Int}         userId                                 用户Id
	 * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	 * @apiParam {Date}        [endDate]                               结束时间(yyyy-mm-dd)
     * @apiParam {Int}        [orgId]                                 运营商Id
	 * @apiParam {String}        [orgName]                                 运营商名称 模糊查询
     * @apiParam {String}        [billPayNo]                             订单编号
     * @apiParam {Int}        [pileId]                                设备id
	 * @apiParam {String}        [pileName]                                设备名称 模糊查询
     * @apiParam {Int}        [stationId]                            场站Id
	 * @apiParam {String}        [stationName]                            场站名称 模糊查询
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           [licensePlate]                         车牌号
	 * @apiParam {Int}           [brand]                              车品牌
	 * @apiParam {Int}           pageSize                              页大小
	 * @apiParam {Int}           vehicleType                              字典cllx
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}		  data.list.billPayId            主键
     * @apiSuccess {Date}		     data.list.createTime			    订单时间(yyyy-mm-dd hh:mm:ss)
	 * @apiSuccess {String}		  data.list.billPayNo			订单编号
	 * @apiSuccess {Date}		     data.list.startTime            开始充电时间(yyyy-mm-dd hh:mm:ss)
	 * @apiSuccess {Date}		    data.list.endTime              结束充电时间(yyyy-mm-dd hh:mm:ss)
	 * @apiSuccess {String}		  data.list.chgPower             充电电量(kW·h)
	 * @apiSuccess {String}		  data.list.orgName             运营商
     * @apiSuccess {String}		  data.list.consPhone            会员号码
     * @apiSuccess {String}		  data.list.consName             会员名称
     * @apiSuccess {String}		  data.list.consPhone  			会员号码
     * @apiSuccess {String}		  data.list.stationName          场站名称
     * @apiSuccess {String}		  data.list.line                 线路
     * @apiSuccess {String}		  data.list.licensePlate         车牌号
     * @apiSuccess {String}		  data.list.pileName             设备名称
     * @apiSuccess {String}		  data.list.vehicleType             车辆类型(字典表 cllx)
     * @apiSuccess {String}		  data.list.brand              车品牌
     * @apiSuccess {String}		  data.list.pileType              桩类型 字典cdzlx 直流和交直流显示（bms和用的实时数据)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000 参数缺失
     */

	@RequestMapping(value = "/historydata", method = RequestMethod.GET)
	public ResultVo selectHistoryData(@RequestParam Map map) throws Exception {
		DataVo vo  =  new DataVo(map);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=historyDataService.selectHistoryData(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
		return resultVo;
	}
	 /**
     * @api {get} /api/historydata/historydata/_export   历史数据导出
     * @apiName exportHistoryData
     * @apiGroup HistoryDataController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    历史数据导出
     * <br/>
	  * @apiParam {Int}         userId                                 用户Id
	  * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	  * @apiParam {Date}        [endDate]                               结束时间(yyyy-mm-dd)
	  * @apiParam {Int}        [orgId]                                 运营商Id
	  * @apiParam {String}        [orgName]                                 运营商名称 模糊查询
	  * @apiParam {String}        [billPayNo]                             订单编号
	  * @apiParam {Int}        [pileId]                                设备id
	  * @apiParam {String}        [pileName]                                设备名称 模糊查询
	  * @apiParam {Int}        [stationId]                            场站Id
	  * @apiParam {String}        [stationName]                            场站名称 模糊查询
	  * @apiParam {Int}           [licensePlate]                         车牌号
	  * @apiParam {Int}           [brand]                              车品牌
	  * @apiParam {Int}           [vehicleType]                              车型
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
	 * @apiError -1700000  参数缺失
     */
	
	@RequestMapping(value = "/historydata/_export", method = RequestMethod.GET)
	public void exportHistoryData(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataMap =  new DataVo(map);
		if(dataMap.isNotBlank("userId")) {
			historyDataService.exportHistoryData(dataMap, response);
		}else {
			throw   new BizException(1700000,"用户Id");
		}
	}
	



	/**
	 * @api {get} /api/historydata/historyAchieveData/{payId}   查询用电实时数据
	 * @apiName historyAchieveData
	 * @apiGroup HistoryDataController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯     查询用电实时数据（充电检测数据，bms检测数据，收入监控）
	 * <br/>
	 * @apiParam {Integer}        payId                               数据Id(列表的billPayId)
	 * <br/>
	 * @apiSuccess {String}          errorCode                        错误码
	 * @apiSuccess {String}          errorMsg                         消息说明
	 * @apiSuccess {Object}          data                    		         查询成功返回值
	 * @apiSuccess {String}		  data.charge              主键
	 * @apiSuccess {Double}		      data.u			    电压
	 * @apiSuccess {Double}		  data.i			   电流
	 * @apiSuccess {Double}		  data.p               功率
	 * @apiSuccess {Double}		  data.zu              桩电压
	 * @apiSuccess {Double}		  data.zi  			  桩电流
	 * @apiSuccess {Double}		  data.zp            桩功率
	 * @apiSuccess {Double}		      data.dl            充电电量
	 * @apiSuccess {Double}		      data.sr            收入
	 * @apiSuccess {Double}		  data.wd            温度
	 * @apiSuccess {Double}		  data.bmsU         bms电压
	 * @apiSuccess {Double}		  data.bmsI         bms电流
	 * @apiSuccess {Date}		  data.ZUipTime     桩电压电流功率时间
	 * @apiSuccess {Date}		  data.UipTime      电压电流功率时间
	 * @apiSuccess {String}		  data.WdTime      工作时间
	 * @apiSuccess {String}		  data.BmsTime     bms时间
	 * @apiSuccess {String}		  data.SrTime       收入时间
	 * @apiSuccess {object}		  data.soc               电池SOC数组
	 * @apiSuccess {String}		  data.sourse
	 * @apiSuccess {String}		  data.sourse.time       时间
	 * @apiSuccess {Double}		  data.sourse.power     电量
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/historyAchieveData/{payId}", method = RequestMethod.GET)
	public ResultVo historyAchieveData(@PathVariable("payId") String payId) throws Exception {
		ResultVo resultVo = new ResultVo();
		Map data  = new HashMap();
		data.put("payId",payId);
		Map<String, Object> dataMap= historyDataService.historyAchieveData(data);
		if(dataMap==null){
			resultVo.setError(0,"未采集到数据");
			return resultVo;
		}
		resultVo.setData(dataMap);
		return resultVo;
	}
	/**
	 * @api {get} /api/historydata/inter 互联互通设备监控
	 * @apiName interConnectivityOrder
	 * @apiGroup InterConnectivity
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯     互联互通设备监控
	 * <br/>
	 * @apiParam {String}        stationId                               场站Id
	 * @apiParam {String}        operatorId                               运营商id
	 * <br/>
	 * @apiSuccess {String}          errorCode                        错误码
	 * @apiSuccess {String}          errorMsg                         消息说明
	 * @apiSuccess {Object}          data                    		         查询成功返回值
	 * @apiSuccess {Object}          data.station                    		         场站信息
	 * @apiSuccess {String}          data.station.operatorId                  运营商Id
	 * @apiSuccess {String}          data.station.stationName              场站名称
	 * @apiSuccess {String}          data.station.stationId                   场站Id
	 * @apiSuccess {String}		  data.count                        统计项
	 * @apiSuccess {String}		  data.count.totalPowerCount         总电量
	 * @apiSuccess {String}		  data.count.totalPoweDate           日电量
	 * @apiSuccess {String}		  data.count.totalMoneyCount              总收入
	 * @apiSuccess {String}		  data.count.totalMoneyDate              日收入
	 * @apiSuccess {String}		  data.count.totalSize              总订单数
	 * @apiSuccess {String}		  data.count.totalDateSize              日订单数
	 * @apiSuccess {String}		  data.count.equipmentTypeCount               总桩数
	 * @apiSuccess {String}		  data.count.equipmentType1              快桩数
	 * @apiSuccess {String}		  data.count.equipmentType2              慢桩数
	 * @apiSuccess {String}		  data.count.chargeSize              充电中个数
	 * @apiSuccess {Object}		  data.count.list                         桩集合
	 * @apiSuccess {String}		  data.list.equipments                  桩
	 * @apiSuccess {String}		  data.list.equipments.date                  统计时间
	 * @apiSuccess {String}		  data.list.equipments.size                  桩数量
	 * @apiSuccess {String}		  data.list.equipments.equipmentName          桩名称
	 * @apiSuccess {String}		  data.list.equipments.equipmentId                  桩编号
	 * @apiSuccess {String}		  data.list.equipments.chargeState                  桩状态(1：空闲；0：充电 2:离线)
	 * @apiSuccess {Object}		  data.list.equipments.connectors                  枪集合
	 * @apiSuccess {String}		  data.list.equipments.connectors.connectorId        枪编号
	 * @apiSuccess {String}		  data.list.equipments.connectors.connectorName       枪名称
	 * @apiSuccess {String}		  data.list.equipments.connectors.chargeState                  枪状态(1：空闲；0：充电 2:离线)
	 * @apiSuccess {String}		  data.list.equipments.connectors.chargePower                  充电量
	 * @apiSuccess {String}		  data.list.equipments.connectors.chargeTime                  充电时间
	 * @apiSuccess {String}		  data.list.equipments.connectors.current                  电流
	 * @apiSuccess {String}		  data.list.equipments.connectors.voltageUpperLimits                  电压
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/inter", method = RequestMethod.GET)
	public ResultVo interConnectivityOrder(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo vo = new DataVo(map);
       DataVo dataList =  historyDataService.interConnectivityOrder(vo);
        resultVo.setData(dataList);
		return resultVo;
	}
}





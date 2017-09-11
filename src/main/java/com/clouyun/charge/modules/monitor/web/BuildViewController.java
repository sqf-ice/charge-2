package com.clouyun.charge.modules.monitor.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.modules.spring.web.BaseController;
import com.clouyun.charge.modules.monitor.service.BuildViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 描述: 建设总览 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0.0
 * 创建日期: 2017年6月21日
 */
@RestController
@RequestMapping("/api")
@SuppressWarnings("rawtypes")
public class BuildViewController extends BaseController{

	@Autowired
	private BuildViewService  buildViewService;
	/**
	 * @api {GET} /api/buildviews/distind             场站（桩）分布指标
	 * @apiName   getDistInd
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             场站（桩）分布指标
	 * <br/>
	 * @apiParam {Integer}     [provQty]              查询的省数量
	 * @apiParam {Integer}     [cityQty]              查询的城市数量
	 * <br/>
	 * @apiSuccess {String}    errorCode              错误码
	 * @apiSuccess {String}    errorMsg               消息说明
	 * @apiSuccess {Object}    data                   数据对象
	 * @apiSuccess {Object[]}  data.prov              数据集合
	 * @apiSuccess {Object[]}  data.prov.list         数据集合
	 * @apiSuccess {String}    data.list.provName     省份名称
	 * @apiSuccess {Integer}   data.list.stationSum   场站数量
	 * @apiSuccess {Integer}   data.list.pileSum      桩数量
	 * @apiSuccess {Object[]}  data.city              数据集合
	 * @apiSuccess {Object[]}  data.city.list         数据集合
	 * @apiSuccess {String}    data.list.cityName     城市名称
	 * @apiSuccess {Integer}   data.list.stationSum   场站数量
	 * @apiSuccess {Integer}   data.list.pileSum      桩数量
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/distind", method = RequestMethod.GET)
	public ResultVo getDistInd(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getDistInd(map));
		return resultVo;
	}
	/**
	 * @api {GET} /api/buildviews/acceind/station       场站接入指标
	 * @apiName   getStatAcceInd
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             场站接入指标
	 * <br/>
	 * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据对象
	 * @apiSuccess {Integer}   data.acceIndSum          场站指标
	 * @apiSuccess {Integer}   data.acceSum             接入场站总数
	 * @apiSuccess {Integer}   data.toSum               接入互联互通
	 * @apiSuccess {Object}    data.self                接入自营
	 * @apiSuccess {Integer}   data.self.selfSum        接入自营总数
	 * @apiSuccess {Integer}   data.self.selfFastSum    接入自营快充数
	 * @apiSuccess {Integer}   data.self.selfSlowSum    接入自营慢充数
	 * 
	 * @apiSuccess {Object}    data.join                接入加盟
	 * @apiSuccess {Integer}   data.join.joinSum        接入加盟总数
	 * @apiSuccess {Integer}   data.join.joinFastSum    接入加盟快充数
	 * @apiSuccess {Integer}   data.join.joinSlowSum    接入加盟慢充数
	 * 
	 * @apiSuccess {Object}    data.alone               接入独立运营
	 * @apiSuccess {Integer}   data.alone.aloneSum      接入独立运营总数
	 * @apiSuccess {Integer}   data.alone.aloneFastSum  接入独立运营快充数
	 * @apiSuccess {Integer}   data.alone.aloneSlowSum  接入独立运营慢充数
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/acceind/station", method = RequestMethod.GET)
	public ResultVo getStatAcceInd(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getStatAcceInd(map));
		return resultVo;
	}
	/**
	 * @api {GET} /api/buildviews/acceind/pile       桩接入指标
	 * @apiName   getPileAcceInd
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             桩接入指标
	 * <br/>
	 * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据对象
	 * @apiSuccess {Integer}   data.acceIndSum          场站指标
	 * @apiSuccess {Integer}   data.acceSum             接入场站总数
	 * @apiSuccess {Integer}   data.toSum               接入互联互通
	 * @apiSuccess {Object}    data.self                接入自营
	 * @apiSuccess {Integer}   data.self.selfSum        接入自营总数
	 * @apiSuccess {Integer}   data.self.selfDcSum      接入自营直流总数
	 * @apiSuccess {Integer}   data.self.selfFastSum    接入自营交流快充数
	 * @apiSuccess {Integer}   data.self.selfSlowSum    接入自营交流数
	 * 
	 * @apiSuccess {Object}    data.join                接入加盟
	 * @apiSuccess {Integer}   data.join.joinSum        接入加盟总数
	 * @apiSuccess {Integer}   data.join.joinDcSum      接入加盟总数
	 * @apiSuccess {Integer}   data.join.joinFastSum    接入加盟交流快充数
	 * @apiSuccess {Integer}   data.join.joinSlowSum    接入加盟交流数
	 * 
	 * @apiSuccess {Object}    data.alone               接入独立运营
	 * @apiSuccess {Integer}   data.alone.aloneSum      接入独立运营总数
	 * @apiSuccess {Integer}   data.alone.aloneDcSum     接入独立运营直流总数
	 * @apiSuccess {Integer}   data.alone.aloneFastSum  接入独立运营交流快充数
	 * @apiSuccess {Integer}   data.alone.aloneSlowSum  接入独立运营交流数
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/acceind/pile", method = RequestMethod.GET)
	public ResultVo getPileAcceInd(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getPileAcceInd(map));
		return resultVo;
	}
	/**
	 * @api {GET} /api/buildviews/acceind/vechservice   服务车辆指标
	 * @apiName   getServiceVehicleInd
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉            服务车辆指标
	 * <br/>
	 * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据对象
	 * @apiSuccess {Integer}   data.serviceSum          月服务次数
	 * @apiSuccess {Integer}   data.powerSum            月充电量
	 * @apiSuccess {Integer}   data.busSum              月服务的公交车辆数
	 * @apiSuccess {Integer}   data.comSum              月服务的通勤车辆数
	 * @apiSuccess {Integer}   data.phySum              月服务的物流车辆数
	 * @apiSuccess {Integer}   data.socSum              月服务的社会车辆数
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/acceind/vechservice", method = RequestMethod.GET)
	public ResultVo getServiceVehicleInd(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getServiceVehicleInd(map));
		return resultVo;
	}
	/**
	 * @api {GET} /api/buildviews/acceind/vechoperating    营运车辆指标
	 * @apiName   getOperatingVehicleInd
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉            营运车辆指标
	 * <br/>
	 * @apiParam   {String}    [conDate]               查询日期(yyyy-MM)
	 * <br/>
	 * @apiSuccess {String}    errorCode               错误码
	 * @apiSuccess {String}    errorMsg                消息说明
	 * @apiSuccess {Object}    data                    数据对象
	 * @apiSuccess {Integer}   data.dstVehSum          地上铁接入车辆数
	 * @apiSuccess {Integer}   data.zdlyVehSum         中电绿源接入车辆数
	 * @apiSuccess {Integer}   data.vehicleSum         车辆接入总数 
	 * @apiSuccess {Integer}   data.dstMileSum         地上铁接入车辆月行驶里程
	 * @apiSuccess {Integer}   data.zdlyMileSum        中电绿源接入车辆月行驶里程
	 * @apiSuccess {Double}    data.dstTarget          地上铁补贴目标完成率
	 * @apiSuccess {Double}    data.zdlyTarget         中电绿源补贴目标完成率
	 * @apiSuccess {Integer}   data.acceSum            车辆接入指标总数
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/acceind/vechoperating", method = RequestMethod.GET)
	public ResultVo getOperatingVehicleInd(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getOperatingVehicleInd(map));
		return resultVo;
	}
	
	/**
	 * @api {GET} /api/buildviews/acceind/speed               建设速度
	 * @apiName   getBuildSpeed
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             建设速度
	 * <br/>
	 * @apiParam   {Integer}   [scale]                 滑动显示范围
	 * <br/>
	 * @apiSuccess {String}    errorCode               错误码
	 * @apiSuccess {String}    errorMsg                消息说明
	 * @apiSuccess {Object}    data                    数据对象
	 * @apiSuccess {String}    data.time               时间
	 * @apiSuccess {Object}    data.speed              建设速度
	 * @apiSuccess {Integer}   data.speed.stationSum   场站总数 
	 * @apiSuccess {Integer}   data.speed.self         自营充电桩数
	 * @apiSuccess {Integer}   data.speed.join         加盟充电桩数
	 * @apiSuccess {Integer}   data.speed.alone        独自运营充电桩数
	 * @apiSuccess {Integer}   data.speed.toPile       第三方充电桩数
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/acceind/speed", method = RequestMethod.GET)
	public ResultVo getBuildSpeed(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getBuildSpeed(map));
		return resultVo;
	}

	/**
	 * @api {GET} /api/buildviews/buildSpeed               建设总览下的建设速度
	 * @apiName   queryBuildSpeed
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  曹伟             建设总览下的建设速度
	 * <br/>
	 * @apiParam   {Integer}   [scale]                 滑动显示范围
	 * <br/>
	 * @apiSuccess {String}    errorCode               错误码
	 * @apiSuccess {String}    errorMsg                消息说明
	 * @apiSuccess {Object}    data                    数据对象
	 * @apiSuccess {String}    data.time               时间
	 * @apiSuccess {Object}    data.speed              建设速度
	 * @apiSuccess {Integer}   data.speed.budgetAmount 预算金额
	 * @apiSuccess {Integer}   data.speed.investAmount 投资金额
	 * @apiSuccess {Integer}   data.speed.self         自营充电桩数
	 * @apiSuccess {Integer}   data.speed.join         加盟充电桩数
	 * @apiSuccess {Integer}   data.speed.alone        独自运营充电桩数
	 * @apiSuccess {Integer}   data.speed.toPile       第三方充电桩数
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/buildSpeed", method = RequestMethod.GET)
	public ResultVo queryBuildSpeed(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.queryBuildSpeed(map));
		return resultVo;
	}


	/**
	 * @api {GET} /api/buildviews/station/speed               场站建设速度
	 * @apiName   getStationBuildSpeed
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             场站建设速度
	 * <br/>
	 * @apiParam   {Integer}  userId                  用户id
	 * @apiParam   {String}   [name]                  运营商或场站名称
	 * @apiParam   {String}   [orgName]               运营商名称
	 * @apiParam   {String}   [stationName]           场站名称
	 * <br/>
	 * @apiSuccess {String}    errorCode               错误码
	 * @apiSuccess {String}    errorMsg                消息说明
	 * @apiSuccess {Object}    data                    数据对象
	 * @apiSuccess {Object}    data.speed              
	 * @apiSuccess {String}    data.speed.time         时间
	 * @apiSuccess {Integer}   data.speed.stationSum   月场站总数 
	 * @apiSuccess {Integer}   data.sum                场站总数 
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/station/speed", method = RequestMethod.GET)
	public ResultVo getStationBuildSpeed(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getStationBuildSpeed(map));
		return resultVo;
	}
	
	/**
	 * @api {GET} /api/buildviews/stationInd       场站接入指标
	 * @apiName   getStatInd
	 * @apiGroup  BuildViewController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             场站接入指标
	 * <br/>
	 * @apiSuccess {String}    errorCode               错误码
	 * @apiSuccess {String}    errorMsg                消息说明
	 * @apiSuccess {Object}    data                    数据对象
	 * @apiSuccess {Integer}   data.acceSum            场站总数
	 * @apiSuccess {Integer}   data.toSum              互联互通
	 * @apiSuccess {Integer}   data.selfSum            自营
	 * @apiSuccess {Integer}   data.joinSum            加盟
	 * @apiSuccess {Integer}   data.aloneSum           合作
	 * <br/
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/buildviews/stationInd", method = RequestMethod.GET)
	public ResultVo getStatInd(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(buildViewService.getStatInd(map));
		return resultVo;
	}
}

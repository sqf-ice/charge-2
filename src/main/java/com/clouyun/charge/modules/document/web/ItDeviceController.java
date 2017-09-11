package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.ItDeviceService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 描述: IT设备管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年6月13日 下午3:08:47
 */
@RestController
@RequestMapping("/api/device/its")
public class ItDeviceController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(ItDeviceController.class);
	
	@Autowired
	ItDeviceService itDeviceService;
	
	
	/**
     * @api {GET} /api/device/its  IT设备管理列表
     * @apiName queryItDevices
     * @apiGroup ItDeviceController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   IT设备管理列表
     * <br/>
     * @apiParam {int} pageNum 				页码
     * @apiParam {int} pageSize 			页大小
     * @apiParam {int} userId	 			登陆用户id
     * @apiParam {String} [sort] 			排序字段
     * @apiParam {String} [order] 			排序方向
     * @apiParam {String} [orgName] 		运营商名称
     * @apiParam {Int} [orgId] 				运营商id
	 * @apiParam {String} [deviceName] 		设备名称
	 * @apiParam {String} [deviceNo] 		设备编号
	 * @apiParam {String} [stationName] 	场站名称
     * @apiParam {String} [deviceType] 		设备类型(type=sblx)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {int} data.list.deviceId 				设备id(主键)
     * @apiSuccess {String} data.list.orgName 			运营商名称
     * @apiSuccess {String} data.list.stationName 		所属场站
     * @apiSuccess {String} data.list.deviceName 		设备名称
     * @apiSuccess {String} data.list.deviceNo 			设备编号
     * @apiSuccess {String} data.list.equipmentType 	设备型号
     * @apiSuccess {Int} data.list.deviceType 			设备类型(type=sblx)
     * @apiSuccess {String} data.list.parameter 		参数
     * @apiSuccess {Double} data.list.ratedPower 		额定功率
	 * @apiSuccess {String} data.list.manufacturer 		生产厂家
	 * @apiSuccess {Date} data.list.manufactureTime 	生产日期(yyyy-MM-dd)
	 * @apiSuccess {Date} data.list.guaranteeTime 		保质日期(yyyy-MM-dd)
	 * @apiSuccess {Date} data.list.installTime 		安装日期(yyyy-MM-dd)
	 * @apiSuccess {String} data.list.assetOwnership 	资产归属
	 * @apiSuccess {Int} data.list.deviceStatus 		设备状态(type=zt)
	 * @apiSuccess {String} data.list.remarks 			备注
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  data: {
	 *		pageNum: 1,
	 *		pageSize: 1,
	 *		size: 1,
	 *		startRow: 0,
	 *		endRow: 0,
	 *		total: 1,
	 *		pages: 1,
	 *		list: [
	 *		{
	 *			deviceType: 1,
	 *			ratedPower: 80.22,
	 *			writeTime: "2017-06-13 20:24:48",
	 *			guaranteeTime: "2017-06-13 00:00:00",
	 *			orgName: "无锡科陆新动力",
	 *			updateTime: "2017-06-14 10:29:40",
	 *			installTime: "2017-06-14 00:00:00",
	 *			deviceNo: "设备编号更改",
	 *			deviceId: 1,
	 *			deviceName: "设备名称更改",
	 *			equipmentType: "设备型号更改",
	 *			orgId: 48,
	 *			manufacturer: "生产厂家更改",
	 *			deviceStatus: 0,
	 *			manufactureTime: "2017-06-14 00:00:00",
	 *			parameter: "参数更改",
	 *			stationName: "科陆大厦充电站",
	 *			remarks: "备注更改",
	 *			assetOwnership: "资产归属更改",
	 *			stationId: 1
	 *		}
	 *		],
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
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1000006 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo queryItDevices(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = itDeviceService.selectAll(map);
		vo.setData(pageInfo);
		return vo;
	}

	/**
	 * @api {POST} /api/device/its   IT设备新增
	 * @apiName insertDevice
	 * @apiGroup ItDeviceController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟  IT设备基础信息新增
	 * <br/>
	 * @apiParam {Int} data.list.orgId	 			运营商id
	 * @apiParam {Int} data.list.stationId 			所属场站id
	 * @apiParam {Int} data.list.deviceType 			设备类型(type=sblx)
	 * @apiParam {String} data.list.deviceNo 			设备编号
	 * @apiParam {String} data.list.equipmentType	 	设备型号
	 * @apiParam {String} data.list.deviceName	 		设备名称
	 * @apiParam {String} data.list.manufacturer 		生产厂家
	 * @apiParam {String} data.list.parameter 			参数
	 * @apiParam {Date} data.list.manufactureTime 		生产日期(yyyy-MM-dd)
	 * @apiParam {Double} data.list.ratedPower 			额定功率(kw)
	 * @apiParam {Date} data.list.guaranteeTime 		保质日期(yyyy-MM-dd)
	 * @apiParam {String} data.list.assetOwnership 		资产归属
	 * @apiParam {Date} data.list.installTime 			安装日期(yyyy-MM-dd)
	 * @apiParam {Int} data.list.deviceStatus 			设备状态(type=zt)
	 * @apiParam {String} data.list.remarks 			备注
	 * <br/>
	 * @apiParamExample {json} 入参示例:
	 *	{
	 *		"orgId":24,
	 *		"stationId":168,
	 *		"deviceName":"设备名称",
	 *		"deviceNo":"设备编号",
	 *		"equipmentType":"设备型号",
	 *		"deviceType":"1",
	 *		"parameter":"参数",
	 *		"ratedPower":80.222,
	 *		"manufacturer":"生产厂家",
	 *		"manufactureTime":"2017-06-13",
	 *		"guaranteeTime":"2017-06-13",
	 *		"installTime":"2017-06-13",
	 *		"assetOwnership":"资产归属",
	 *		"deviceStatus":"1",
	 *		"remarks":"备注"
	 *	}
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        新增受影响行数(0:未新增;1:新增一个)
	 * <br/>
	 * @apiError -1200000 运营商/所属场站/设备类型/设备编号/设备型号/设备名称不能为空
	 * @apiError -1102017 额定功率不能超出四位数
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "",method = RequestMethod.POST)
	public ResultVo insertDevice(@RequestBody Map map)throws Exception{
		ResultVo vo = new ResultVo();
		int insertCount = itDeviceService.insert(map);
		vo.setData(insertCount);
		return vo;
	}

	/**
	 * @api {PUT} /api/device/its   IT设备更新
	 * @apiName updateDevice
	 * @apiGroup ItDeviceController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟  IT设备基础信息更新
	 * <br/>
	 * @apiParam {int} data.list.deviceId 				设备id(主键)
	 * @apiParam {Int} data.list.orgId	 			运营商id
	 * @apiParam {Int} data.list.stationId 			所属场站id
	 * @apiParam {Int} data.list.deviceType 			设备类型(type=sblx)
	 * @apiParam {String} data.list.deviceNo 			设备编号
	 * @apiParam {String} data.list.equipmentType	 	设备型号
	 * @apiParam {String} data.list.deviceName	 		设备名称
	 * @apiParam {String} data.list.manufacturer 		生产厂家
	 * @apiParam {String} data.list.parameter 			参数
	 * @apiParam {Date} data.list.manufactureTime 		生产日期(yyyy-MM-dd)
	 * @apiParam {Double} data.list.ratedPower 			额定功率(kw)
	 * @apiParam {Date} data.list.guaranteeTime 		保质日期(yyyy-MM-dd)
	 * @apiParam {String} data.list.assetOwnership 		资产归属
	 * @apiParam {Date} data.list.installTime 			安装日期(yyyy-MM-dd)
	 * @apiParam {Int} data.list.deviceStatus 			设备状态(type=zt)
	 * @apiParam {String} data.list.remarks 			备注
	 *
	 * <br/>
	 * @apiParamExample {json} 入参示例:
	 *	{
	 *		"orgId":48,
	 *		"stationId":1,
	 *		"deviceName":"设备名称更改",
	 *		"deviceNo":"设备编号更改",
	 *		"equipmentType":"设备型号更改",
	 *		"deviceType":"1",
	 *		"parameter":"参数更改",
	 *		"ratedPower":80.222,
	 *		"manufacturer":"生产厂家更改",
	 *		"manufactureTime":"2017-06-14",
	 *		"guaranteeTime":"2017-06-13",
	 *		"installTime":"2017-06-14",
	 *		"assetOwnership":"资产归属更改",
	 *		"deviceStatus":"12",
	 *		"remarks":"备注更改",
	 *		"deviceId":1
	 *	}
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        新增受影响行数(0:未更新;1:更新一个)
	 * <br/>
	 * @apiError -1200000 运营商/所属场站/设备类型/设备编号/设备型号/设备名称不能为空
	 * @apiError -1102017 额定功率不能超出四位数
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "",method = RequestMethod.PUT)
	public ResultVo updateDevice(@RequestBody Map map)throws Exception{
		ResultVo vo = new ResultVo();
		int updateCount = itDeviceService.update(map);
		vo.setData(updateCount);
		return vo;
	}

	/**
	 * @api {GET} /api/device/its/{deviceId}    查询IT设备信息
	 * @apiName queryItDeviceByKey
	 * @apiGroup ItDeviceController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   查询IT设备信息
	 * <br/>
	 * @apiParam {int} deviceId 设备id(必填)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {int} data.deviceId 				设备id(主键)
	 * @apiSuccess {String} data.orgName 			运营商名称
	 * @apiSuccess {String} data.stationName 		所属场站
	 * @apiSuccess {Int} data.deviceType 			设备类型(type=sblx)
	 * @apiSuccess {String} data.deviceNo 			设备编号
	 * @apiSuccess {String} data.equipmentType	 	设备型号
	 * @apiSuccess {String} data.deviceName	 		设备名称
	 * @apiSuccess {String} data.manufacturer 		生产厂家
	 * @apiSuccess {String} data.parameter 			参数
	 * @apiSuccess {Date} data.manufactureTime 		生产日期(yyyy-MM-dd)
	 * @apiSuccess {Double} data.ratedPower 		额定功率(kw)
	 * @apiSuccess {Date} data.guaranteeTime 		保质日期(yyyy-MM-dd)
	 * @apiSuccess {String} data.assetOwnership 	资产归属
	 * @apiSuccess {Date} data.installTime 			安装日期(yyyy-MM-dd)
	 * @apiSuccess {Int} data.deviceStatus 			设备状态(type=zt)
	 * @apiSuccess {String} data.remarks 			备注
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *		errorCode: 0,
	 *		errorMsg: "操作成功!",
	 *		total: 0,
	 *		data: {
	 *			deviceType: 1,
	 *			ratedPower: 80.22,
	 *			guaranteeTime: "2017-06-13",
	 *			orgName: "深圳陆科",
	 *			installTime: "2017-06-13",
	 *			deviceNo: "设备编号",
	 *			deviceId: 1,
	 *			deviceName: "设备名称",
	 *			equipmentType: "设备型号",
	 *			orgId: 24,
	 *			manufacturer: "生产厂家",
	 *			deviceStatus: 1,
	 *			manufactureTime: "2017-06-13",
	 *			parameter: "参数",
	 *			stationName: "sss",
	 *			remarks: "备注",
	 *			assetOwnership: "资产归属",
	 *			stationId: 168
	 *		},
	 *		exception: null
	 *		}
	 *	}
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
	public ResultVo queryItDeviceByKey(@PathVariable Integer deviceId) throws Exception {
		ResultVo vo = new ResultVo();
		Map map = itDeviceService.queryItDeviceByKey(deviceId);
		vo.setData(map);
		return vo;
	}

	/**
	 * @api {DELETE} /api/device/its/{deviceIds}    批量删除IT设备信息
	 * @apiName deleteDevice
	 * @apiGroup ItDeviceController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   批量删除IT设备信息,请求方式为/api/device/its/1,2
	 * <br/>
	 * @apiParam {Integer[]} deviceIds 需要删除的设备id(必填)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        新增受影响行数(0:未更新;1:更新一个)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1102016 请选择需要删除的IT设备!
	 */
	@RequestMapping(value = "/{deviceIds}", method = RequestMethod.DELETE)
	public ResultVo deleteDevice(@PathVariable("deviceIds") List<Integer> deviceIds) throws Exception {
		ResultVo vo = new ResultVo();
		Integer count = itDeviceService.deleteDevice(deviceIds);
		vo.setData(count);
		return vo;
	}

	/**
	 * @api {GET} /api/device/its/_export  IT设备管理导出
	 * @apiName exportItDevices
	 * @apiGroup ItDeviceController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟   IT设备管理导出
	 * <br/>
	 * @apiParam {int} [pageNum] 			页码
	 * @apiParam {int} [pageSize] 			页大小
	 * @apiParam {int} userId	 			登陆用户id
	 * @apiParam {String} [sort] 			排序字段
	 * @apiParam {String} [order] 			排序方向
	 * @apiParam {String} [orgName] 		运营商名称
	 * @apiParam {String} [deviceName] 		设备名称
	 * @apiParam {String} [deviceNo] 		设备编号
	 * @apiParam {String} [stationName] 	场站名称
	 * @apiParam {String} [deviceType] 		设备类型(type=sblx)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
	public ResultVo exportItDevices(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		itDeviceService.exportDevice(map,response);
		return vo;
	}

}
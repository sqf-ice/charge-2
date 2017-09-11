package com.clouyun.charge.modules.vehicle.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.modules.spring.web.BaseController;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.modules.vehicle.service.VehicleService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/api")
public class VehicleController extends BaseController{
	
	@Autowired
	private  VehicleService vehicleService;
	/**
     * @api {GET} /api/vehicles     车辆信息列表
     * @apiName  getVehiclesPage
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉   车辆信息列表
     * <br/>
     * @apiParam {Integer}    userId     	                 当前的登陆用户
     * @apiParam {Integer}    pageNum    	                 页码
     * @apiParam {Integer}    pageSize  		     页大小
     * @apiParam {String}     [sort]              排序字段
	 * @apiParam {String}     [order]             排序(DESC:降序|ASC:升序)
     * @apiParam {String}     [belongs]           所有人
     * @apiParam {Integer}    [stationId]         所属场站 
     * @apiParam {String}     [licensePlate]      车牌号
     * @apiParam {Integer}    [vehicleType]       车辆类型type =cllx
     * @apiParam {Integer}    [belongsType]       1-社会车辆、2-企业车辆 type =clsslx
     * @apiParam {String}     [line]              线路
     * @apiParam {String}     [consTrueName]      车辆会员
     * <br/>
     * @apiSuccess {String}    errorCode             错误码
     * @apiSuccess {String}    errorMsg              消息说明
     * @apiSuccess {Object}    data                  数据对象
     * @apiSuccess {Object}    list                  数据集合
     * @apiSuccess {Integer}   data.list.vehicleId        车辆id
     * @apiSuccess {Integer}   data.list.orgId            运营商id
     * @apiSuccess {Integer}   data.list.orgName          运营商名称
     * @apiSuccess {Integer}   data.list.belongsOrgId     企业id
     * @apiSuccess {String}    data.list.belongs          所有人
     * @apiSuccess {Integer}   data.list.stationId        所属场站id
     * @apiSuccess {Integer}   data.list.stationName      所属场站名称
     * @apiSuccess {String}    data.list.licensePlate     车牌号
     * @apiSuccess {Integer}   data.list.vehicleType      车辆类型
     * @apiSuccess {Integer}   data.list.belongsType      车辆所属类型
     * @apiSuccess {String}    data.list.brand            车品牌
     * @apiSuccess {String}    data.list.onNumber         自编号
     * @apiSuccess {String}    data.list.consTrueName     车辆会员
     * @apiSuccess {String}    data.list.line             线路
     * @apiSuccess {String}    data.list.model            车辆型号
     * @apiSuccess {Date}      data.list.updateTime       更新时间
     * @apiSuccess {Integer}   data.usingRoperty          使用性质
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/vehicles", method = RequestMethod.GET)
	public ResultVo getVehiclesPage(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = vehicleService.getVehiclesPage(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
	
	/**
     * @api {GET} /api/vehicles/{vehicleId}     车辆信息查询
     * @apiName  getVehicle
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉  车辆信息查询
     * <br/>
     * @apiParam   {Integer}   vehicleId     		 车辆id
     * <br/>
     * @apiSuccess {String}    errorCode             错误码
     * @apiSuccess {String}    errorMsg              消息说明
     * @apiSuccess {Object[]}  data                  数据集合
     * @apiSuccess {Integer}   data.list.orgId       运营商id
     * @apiSuccess {Integer}   data.list.orgName     运营商名称
     * @apiSuccess {Integer}   data.list.belongsOrgId企业id
     * @apiSuccess {String}    data.list.belongs     所有人
     * @apiSuccess {Integer}   data.stationId        所属场站id
     * @apiSuccess {Integer}   data.stationName      所属场站名称
     * @apiSuccess {String}    data.onNumber         自编号
     * @apiSuccess {String}    data.licensePlate     车牌号
     * @apiSuccess {String}    data.manufacturer     生产厂家
     * @apiSuccess {String}    data.brand            车品牌
     * @apiSuccess {String}    data.model            车辆型号
     * @apiSuccess {String}    data.color            颜色
     * @apiSuccess {String}    data.engineNo         发动机编号
     * @apiSuccess {String}    data.vehicleSize      车身尺寸
     * @apiSuccess {String}    data.vin              车架号
     * @apiSuccess {Integer}   data.usingRoperty     使用性质
     * @apiSuccess {Integer}   data.operationRoperty 营运属性
     * @apiSuccess {Integer}   data.vehicleType      车辆类型
     * @apiSuccess {String}    data.line             线路
     * @apiSuccess {Integer}   data.loadNo           核载人数
     * @apiSuccess {Double}    data.loadWeight       核载质量
     * @apiSuccess {Double}    data.totalWeight      总质量
     * @apiSuccess {Double}    data.curbWeight       整备质量
     * @apiSuccess {Double}    data.initMileage      初始里程数
     * @apiSuccess {Double}    data.mileage          里程数
     * @apiSuccess {Integer}   data.belongsType      车辆所属类型
     * @apiSuccess {String}    data.belongsName      所属人名称
     * @apiSuccess {String}    data.drivingUrl       行驶证url
     * @apiSuccess {String}    data.proviceCode      省编码
     * @apiSuccess {String}    data.cityCode         市编码
     * @apiSuccess {String}    data.remark           备注
     * @apiSuccess {Date}      data.updateTime       更新时间
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/vehicles/{vehicleId}", method = RequestMethod.GET)
	public ResultVo getVehicle(@PathVariable Integer vehicleId) throws Exception {
		ResultVo resultVo = new ResultVo();
		Map vehicle = vehicleService.getVehicle(vehicleId);
		resultVo.setData(vehicle);
		return resultVo;
	}
	
	/**
     * @api {GET} /api/vehicles/_export   导出车辆信息列表
     * @apiName  exportVehicles
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉         导出车辆信息列表
     * <br/>
     * @apiParam {String}    [belongss]         所有人
     * @apiParam {String}    [licensePlate]  	车牌号
     * @apiParam {String}    [consTrueName]     车辆会员
     * @apiParam {String}    [belongsType]  	车辆类型
     * @apiParam {String}    [belongsType]  	类型	：社会车辆、企业车辆
     * @apiParam {String}    [line]    	                        线路
     * @apiParam {String}    [brand]  	                        品牌
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/vehicles/_export", method = RequestMethod.GET)
    public void exportVehicles(@RequestParam Map map,HttpServletResponse response) throws Exception {
		vehicleService.exportVehicles(map,response);
    }
	/**
	 * @api {POST} /api/vehicles   新增车辆
     * @apiName  insertVehicle
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉          新增车辆
     * <br/>
     * @apiParam {Integer}   orgId          运营商id
     * @apiParam {Integer}   [stationId]      所属场站id
     * @apiParam {Integer}   [belongsOrgId]   所属运营商
     * @apiParam {String}    [onNumber]       自编号
     * @apiParam {String}    licensePlate     车牌号
     * @apiParam {String}    [manufacturer]   生产厂家
     * @apiParam {String}    [brand]          车品牌
     * @apiParam {String}    [model]          车辆型号
     * @apiParam {String}    [color]          颜色
     * @apiParam {String}    [engineNo]       发动机编号
     * @apiParam {String}    [vehicleSize]    车身尺寸
     * @apiParam {String}    [vin]            车架号
     * @apiParam {Integer}   usingRoperty     使用性质
     * @apiParam {Integer}   operationRoperty 营运属性
     * @apiParam {Integer}   vehicleType      车辆类型
     * @apiParam {String}    [line]           线路
     * @apiParam {Integer}   [loadNo]         核载人数
     * @apiParam {Double}    [loadWeight]     核载质量
     * @apiParam {Double}    [totalWeight]    总质量
     * @apiParam {Double}    [curbWeight]     整备质量
     * @apiParam {Double}    [initMileage]    初始里程数
     * @apiParam {Double}    [mileage]        里程数
     * @apiParam {Integer}   belongsType      车辆所属类型
     * @apiParam {String}    [belongsName]    所属人名称
     * @apiParam {String}    [drivingUrl]     行驶证url
     * @apiParam {String}    [proviceCode]    省编码
     * @apiParam {String}    [cityCode]       市编码
     * @apiParam {String}    [remark]         备注
     * @apiParam {Object}    [driverids]        [1,3,4]
     * <br/>
     * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
     * @apiError -999 系统异常！
     * @apiError -888 请求方式异常！
     * @apiError -1500000 请求方式异常！
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/vehicles", method = RequestMethod.POST)
	public ResultVo insertVehicle(MultipartHttpServletRequest multiRequest) throws Exception {
		ResultVo resultVo = new ResultVo();
		MultipartFile file = multiRequest.getFile("file");
		DataVo dataVo = new DataVo(CommonUtils.gerParamterMap(multiRequest.getParameterMap()));
		vehicleService.insertVehicle(dataVo,file);
		return resultVo;
	}
	/**
     * @api {POST} /api/vehicles/_update   编辑车辆
     * @apiName  importVehicles
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉          编辑车辆
     * <br/>
     * @apiParam {Integer}   vehicleId        车辆id
     * @apiParam {Integer}   [orgId]          运营商id
     * @apiParam {Integer}   [stationId]      所属场站id
     * @apiParam {Integer}   [longsOrgId]     所属运营商
     * @apiParam {String}    [onNumber]       自编号
     * @apiParam {String}    licensePlate     车牌号
     * @apiParam {String}    [manufacturer]   生产厂家
     * @apiParam {String}    [brand]          车品牌
     * @apiParam {String}    [model]          车辆型号
     * @apiParam {String}    [color]          颜色
     * @apiParam {String}    [engineNo]       发动机编号
     * @apiParam {String}    [vehicleSize]    车身尺寸
     * @apiParam {String}    [vin]            车架号
     * @apiParam {Integer}   usingRoperty     使用性质
     * @apiParam {Integer}   operationRoperty 营运属性
     * @apiParam {Integer}   vehicleType      车辆类型
     * @apiParam {String}    [line]           线路
     * @apiParam {Integer}   [loadNo]         核载人数
     * @apiParam {Double}    [loadWeight]     核载质量
     * @apiParam {Double}    [totalWeight]    总质量
     * @apiParam {Double}    [curbWeight]     整备质量
     * @apiParam {Double}    [initMileage]    初始里程数
     * @apiParam {Double}    [mileage]        里程数
     * @apiParam {Integer}   belongsType      车辆所属类型
     * @apiParam {String}    [belongsName]    所属人名称
     * @apiParam {String}    [drivingUrl]     行驶证url
     * @apiParam {String}    [proviceCode]    省编码
     * @apiParam {String}    [cityCode]       市编码
     * @apiParam {String}    [remark]         备注
     * @apiParam {Object}    [driverids]      [1,3,4]
     * <br/>
     * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
     * @apiError -999 系统异常！
     * @apiError -888 请求方式异常！
     * @apiError -1500000 请求方式异常！
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/vehicles/_update", method = RequestMethod.POST)
	public ResultVo updateVehicle(MultipartHttpServletRequest multiRequest) throws Exception {
		ResultVo resultVo = new ResultVo();
		MultipartFile file = multiRequest.getFile("file");
		DataVo dataVo = new DataVo(CommonUtils.gerParamterMap(multiRequest.getParameterMap()));
		vehicleService.updateVehicle(dataVo,file);
		return resultVo;
	}
	/**
     * @api {DELETE} /api/vehicles   根据驾驶员id和车辆id删除车辆的驾驶员信息
     * @apiName  deleteVehDriRelBy2Id
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉          根据驾驶员id和车辆id删除车辆的驾驶员信息
     * <br/>
     * @apiParam {Integer}  driverId            驾驶员id
     * @apiParam {Integer}  vehicleId          车牌id
     * <br/>
     * @apiError -999 系统异常！
     * @apiError -888 请求方式异常！
     * @apiError -1500000 参数为空异常！
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/vehicles", method = RequestMethod.DELETE)
	public ResultVo deleteVehDriRelBy2Id(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		vehicleService.deleteVehDriRelBy2Id(map);
		return resultVo;
	}
	/**
	 * 
     * @api {POST} /api/vehicles/_import   批量导入车辆
     * @apiName  importPiles
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉                      excel 批量导入车辆
     * <br/>
     * @apiParam {Integer}       userId                    用户id
     * @apiParam {MultipartFile} file                      充电桩模版
     * <br/>
     * @apiSuccess {String}    errorCode                  错误码   0-导入成功  1-导入失败
     * @apiSuccess {String}    errorMsg                   消息说明
     * @apiSuccess {Object}    data                       分页数据封装
     * @apiSuccess {String[]}  data.list 		                              数据状态
     * @apiSuccess {Integer}   data.list.stationName      所属场站
     * @apiSuccess {Integer}   data.list.orgName          运营商
     * @apiSuccess {String}    data.list.onNumber         自编号
     * @apiSuccess {String}    data.list.licensePlate     车牌号
     * @apiSuccess {String}    data.list.manufacturer     生产厂家
     * @apiSuccess {String}    data.list.brand            车品牌
     * @apiSuccess {String}    data.list.model            车辆型号
     * @apiSuccess {String}    data.list.color            颜色
     * @apiSuccess {String}    data.list.engineNo         发动机编号
     * @apiSuccess {String}    data.list.vehicleSize      车身尺寸
     * @apiSuccess {String}    data.list.vin              车架号
     * @apiSuccess {Integer}   data.list.rUsingRoperty    使用性质
     * @apiSuccess {Integer}   data.list.rOperationRoperty营运属性
     * @apiSuccess {Integer}   data.list.rVehicleType     车辆类型
     * @apiSuccess {String}    data.list.line             线路
     * @apiSuccess {Integer}   data.list.loadNo           核载人数
     * @apiSuccess {Double}    data.list.loadWeight       核载质量
     * @apiSuccess {Double}    data.list.totalWeight      总质量
     * @apiSuccess {Double}    data.list.curbWeight       整备质量
     * @apiSuccess {Double}    data.list.initMileage      初始里程数
     * @apiSuccess {Double}    data.list.mileage          里程数
     * @apiSuccess {Integer}   data.list.rBelongsType     车辆所属类型
     * @apiSuccess {String}    data.list.belongs          所有人
     * @apiSuccess {String}    data.list.remark           备注
     * @apiSuccess {Date}      data.list.updateTime       更新时间
     * @apiSuccess {String}    data.list.memberType       会员类型
     * @apiSuccess {String}    data.list.groupName        集团名
     *  @apiSuccess{String}    data.list.consName         会员名
     * @apiSuccess {String}    data.list.memberPhone      会员电话号码
     * @apiSuccess {String}    data.list.cardId           储值卡号
     * @apiSuccess {Date}      data.list.updateTime       更新时间
     * @apiSuccess {String}    data.list.description      导入失败描述
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/vehicles/_import",method=RequestMethod.POST)
	public ResultVo importVehicles(HttpServletRequest request,@RequestParam("file") MultipartFile file,@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile excelFile=multipartRequest.getFile("file");
		List<Map> list = vehicleService.importVehicles(excelFile,map);
		resultVo.setData(list);
		if(null==list || list.size()==0){
			resultVo.setErrorCode(0);
		}else{
			resultVo.setErrorCode(1);
		}
		return resultVo;
	}
	
	/**
     * @api {GET} /api/vehicles/{vehicleId}/member   根据车辆id获取车辆会员
     * @apiName  getMemberByVehicle
     * @apiGroup VehicleController
     * @apiVersion 2.0.0
     * @apiDescription 高辉          根据车辆id获取车辆会员
     * <br/>
     * @apiParam {Integer}  vehicleId                     车牌id
     * <br/>
     * @apiSuccess {String}    errorCode                  错误码   
     * @apiSuccess {String}    errorMsg                   消息说明
     * @apiSuccess {Object}    data                       分页数据封装
     * @apiSuccess {String[]}  data.list 		                              数据
     * @apiSuccess {Integer}   data.list.vehicleId        车辆id 
     * @apiSuccess {String}    data.list.consId           会员id
     * @apiSuccess {String}    data.list.cardId           储值卡号
     * @apiSuccess {String}    data.list.groupId          集团id
     * @apiSuccess {String}    data.list.groupName        集团名称
     * @apiSuccess {String}    data.list.consName         会员名称
     * @apiSuccess {String}    data.list.consTypeCode     会员类型
     * @apiSuccess {String}    data.list.consPhone        会员电话号码
     * @apiSuccess {String}    data.list.consSum          消费总金额
     * <br/>
     * @apiError -999 系统异常！
     * @apiError -888 请求方式异常！
	 */
	@RequestMapping(value = "/vehicles/{vehicleId}/member", method = RequestMethod.GET)
	public ResultVo getMemberByVehicle(@PathVariable Integer vehicleId) throws Exception {
		ResultVo resultVo = new ResultVo();
		resultVo.setData(vehicleService.getMemberByVehicleId(vehicleId));
		return resultVo;
	}
	
	
	/**
	 * @api {GET} /api/vehicles/dicts/brand   车品牌业务字典
     * @apiName  getVehicleBrandDicts
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription  高辉   车品牌业务字典 
     * <br/>
     * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {String}    data.list.id             车品牌id
	 * @apiSuccess {String}    data.list.text           车品牌名称
	 * @apiSuccessExample {json} Success出参示例:
	 * <br/>
	 * @apiError -999 系统异常!
     * @apiError -1803000 参数空异常!
     */
	@RequestMapping(value = "vehicles/dicts/brand", method = RequestMethod.GET)
	public ResultVo getVehicleBrandDicts() throws Exception{
		ResultVo resultVo =new ResultVo();
		resultVo.setData(vehicleService.getVehicleBrandDicts());
		return resultVo;
	}
	
	/**
	 * @api {GET} /api/vehicles/dicts/model   车类型业务字典
     * @apiName  getVehicleModelDicts
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription  高辉   车类型业务字典
     * <br/>
     * @apiParam   {Integer}   brandId                 车品牌id
     * <br/>
     * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {String}    data.list.id             车型号id
	 * @apiSuccess {String}    data.list.text           车型号名称
	 * @apiSuccessExample {json} Success出参示例:
	 * <br/>
	 * @apiError -999 系统异常!
     * @apiError -1803000 参数空异常!
     */
	@RequestMapping(value = "vehicles/dicts/model", method = RequestMethod.GET)
	public ResultVo getVehicleModelDicts(@RequestParam Map map) throws Exception{
		ResultVo resultVo =new ResultVo();
		resultVo.setData(vehicleService.getVehicleModelDicts(map));
		return resultVo;
	}
}

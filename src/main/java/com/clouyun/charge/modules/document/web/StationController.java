package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.document.service.StationService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: StationController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年4月18日
 */
@RestController
@RequestMapping("/api")
public class StationController extends BusinessController{
	
	@Autowired
	StationService stationService;
	
	/**
	 *
	 * @api {get} /api/stations   查询场站列表
	 * @apiName getStationAll
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据区域,运营商,场站,经营类型,运营状态,建设场所,使用状态,运营模式条件查询场站列表信息
	 * <br/>
	 * @apiParam {Integer} userId	用户Id
	 * @apiParam {Integer} [orgId] 运营商Id 
	 * @apiParam {String} [stationName] 场站名称 
	 * @apiParam {String} [stationType] 经营类型 
	 * @apiParam {String} [stationStatus] 运营状态 
	 * @apiParam {String} [construction] 建设场所 
	 * @apiParam {String} [useStatus] 使用状态0:有效 1:无效 
	 * @apiParam {String} [stationModel] 运营模式01：自运营；02：合作运营；03：其他运营商 
	 * @apiParam {String} [provCode] 省 
	 * @apiParam {String} [cityCode] 市 
	 * @apiParam {Integer} pageNum 页码 
	 * @apiParam {Integer} pageSize 页大小 
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.stationId 场站Id,主键自增
	 * @apiSuccess {Integer} data.list.orgId 运营商Id
	 * @apiSuccess {String} data.list.stationNo 场站编号
	 * @apiSuccess {String} data.list.stationName 充电站名称
	 * @apiSuccess {String} data.list.stationModel 运营模式01：自运营；02：合作运营；03：其他运营商
	 * @apiSuccess {String} data.list.stationType 经营类型
	 * @apiSuccess {String} data.list.stationStatus 运营状态
	 * @apiSuccess {String} data.list.useStatus 使用状态0:有效 1:无效
	 * @apiSuccess {String} data.list.construction 建设场所
	 * @apiSuccess {String} data.list.pileCount 充电桩数量
	 * @apiSuccess {String} data.list.gunCount 充电枪数量
	 * @apiSuccess {Integer(6)} data.list.parkNums 车位数量
	 * @apiSuccess {String} data.list.provCode 省
	 * @apiSuccess {String} data.list.cityCode 市
	 * @apiSuccess {String} data.list.distCode 区县
	 * @apiSuccess {String} data.list.address 详细地址
	 * @apiSuccess {Double} data.list.stationArea 场地面积
	 * @apiSuccess {Double} data.list.stationLng 经度
	 * @apiSuccess {Double} data.list.stationLat 纬度
	 * @apiSuccess {String} data.list.parkInfo 车位描述
	 * @apiSuccess {String} data.list.payMent 支付方式描述
	 * @apiSuccess {String} data.list.busineHours 营业时间  yyyy-MM-dd
	 * @apiSuccess {String} data.list.stationContact 联系人
	 * @apiSuccess {String} data.list.stationTel 联系电话
	 * @apiSuccess {String} data.list.serviceTel 服务电话
	 * @apiSuccess {Date} data.list.endTime 场站到期时间  yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.list.rentalstartdate 场站租赁有效期开始  yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.list.rentalenddate 场站租赁有效期结束  yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.list.stationstartdate 场站有效期开始  yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.list.stationenddate 场站有效期结束  yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {String} data.list.remark 备注
	 * @apiSuccess {String} data.list.createTime 创建时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {String} data.list.isDiss 是否将在1个月内过期 0:否  1:是
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value="stations",method=RequestMethod.GET)
	public ResultVo getStationAll(@RequestParam Map map) throws BizException {
		ResultVo resVo = new ResultVo();
		PageInfo data = stationService.getStationAll(map);
		resVo.setData(data);
		return resVo;
	}
	
	/**
	 * @api {put} /api/stations/{stationIds}    场站置为无效
	 * @apiName dissStation
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据场站ID,将场站置为无效状态
	 * <br/>
	 * @apiParam {String}    stationIds     场站Id 多个Id: 1,2,3   
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError 1102002     场站:{0},存在有效充电桩,不能做无效处理!
	 */
	@RequestMapping(value="stations/{stationIds}",method = RequestMethod.PUT)
	public ResultVo dissStation(@PathVariable(name = "stationIds", required = false) List<Integer> stationIds) throws BizException{
		ResultVo resVo = new ResultVo();
		stationService.dissStation(stationIds);
		return resVo;
		
	}
	
	/**
	 *
	 * @api {GET} /api/stations/{stationId}    查询场站信息
	 * @apiName getStationById
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯 根据场站id查询场站信息
	 * <br/>
	 * @apiParam {Integer}  stationId   场站id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Integer} data.stationId 场站Id,主键自增
	 * @apiSuccess {Integer} data.selfNumber  自编号
	 * @apiSuccess {Integer} data.orgNo  运营商编号
	 * @apiSuccess {String}    data.stationNo 			场站编号(运营商编号+自编号)
	 * @apiSuccess {String}    data.stationName    	场站名称
	 * @apiSuccess {String}    data.innerNo   内部编号
	 * @apiSuccess {String}    data.innerName  内部名称
	 * @apiSuccess {Integer}   data.orgId  			运营商Id （字典 运营商下拉树）
	 * @apiSuccess {Integer}   data.construction     建设场所 （jscs）
	 * @apiSuccess {Integer}    data.stationType     场站类型  (jingylx)
	 * @apiSuccess {String}   data.serviceUnit   服务单位
	 * @apiSuccess {Integer}   data.manageType    管理类型 (gllx)
	 * @apiSuccess {Integer}   data.serviceType   服务类型(fflx)
	 * @apiSuccess {Integer}    data.stationStatus   场站状态(yyzt)
	 * @apiSuccess {Integer}    data.useStatus     使用状态  (zt)
	 * @apiSuccess {String}    data.stationContact     联系人
	 * @apiSuccess {String}    data.stationTel     联系电话
	 * @apiSuccess {String}    data.repairMan    维修员
	 * @apiSuccess {String}    data.repairPhone    维修电话
	 * @apiSuccess {Integer}   data.employeeSize  员工数量
	 * @apiSuccess {Data}      data.busineHours	营业时间
	 * @apiSuccess {String}    data.supportOrder     是否支持预约(sf)
	 * @apiSuccess {String}    data.payMent  支付方式	,默认值:刷卡、线上、现金
	 * @apiSuccess {String}    data.electricityFee  充电费描述(元/度)
	 * @apiSuccess {String}    data.parkFee    停车费描述
	 * @apiSuccess {String}    data.serviceFee    服务费描述(元/度)
	 * @apiSuccess {Integer}    data.provCode     省 (通用区域字典)
	 * @apiSuccess {Integer}    data.cityCode    市(通用区域字典)
	 * @apiSuccess {Integer}    data.distCode    县(通用区域字典)
	 * @apiSuccess {Double}    data.address     详细地址
	 * @apiSuccess {Double}    data.stationArea     占地面积(平米)
	 * @apiSuccess {String}    data.stationLng     经度
	 * @apiSuccess {String}    data.stationLat     纬度
	 * @apiSuccess {String}    data.remark    备注
	 * @apiSuccess {String}    data.dcSize    直流数
	 * @apiSuccess {String}    data.acSize    交流数
	 * @apiSuccess {String}    data.acDcSize    交直流数
	 * @apiSuccess {String}    data.gunSize    枪数
	 * @apiSuccess {String}    data.totalRatePower    总功率
	 * @apiSuccess {String}    data.acRatePower    交流功率
	 * @apiSuccess {String}    data.dcRatePower    直流功率
	 * @apiSuccess {String}    data.acDcRatePower    交直流功率
	 * @apiSuccess {Integer}    data.stationEtwork    场站网络 (czwl)
	 * @apiSuccess {Integer}    data.businessPeople   电信运营商(czyys)
	 * @apiSuccess {String}    data.etworkSpeed   网络速率
	 * @apiSuccess {Integer}   data.etworkSize   数量
	 * @apiSuccess {String}    data.expiryTime  有效期至 yyyy-MM-dd
	 * @apiSuccess {String}    data.payTime   交费日期 yyyy-MM-dd
	 * @apiSuccess {String}    data.tariffInformation  资费信息
	 * @apiSuccess {Integer}   data.stationModel    运营模式(yyms)
	 * @apiSuccess {Integer}   data.stationCoopType   合作方式(hzfs)
	 * @apiSuccess {String}   data.businessBelong     运营权归属
	 * @apiSuccess {String}   data.businessPhone     运营联系电话
	 * @apiSuccess {String}   data.assetsBelong     资产归属
	 * @apiSuccess {String}   data.landBelong     土地归属
	 * @apiSuccess {Double}   data.investAmount     投资金额
	 * @apiSuccess {Double}   data.targetCharge     目标收入
	 * @apiSuccess {Double}   data.targetIncome     目标充电量
	 * @apiSuccess {Data}    data.rentalStartDate  场站租赁有效期开始  yyyy-MM-dd
	 * @apiSuccess {Data}    data.rentalEndDate     场站租赁有效期结束 yyyy-MM-dd
	 * @apiSuccess {Data}    data.stationStartDate  场站有效期开始 yyyy-MM-dd
	 * @apiSuccess {Data}    data.stationEndDate    场站有效期结束 yyyy-MM-dd
	 * @apiSuccess {Data}   data.testTime     试运营日期 yyyy-MM-dd
	 * @apiSuccess {Data}   data.formalTime     正式运营日期 yyyy-MM-dd
	 * @apiSuccess {String}   data.leasePic     租赁合同地址
	 * @apiSuccess {Integer}      data.serviceCars   服务车型(cllx)
	 * @apiSuccess {Integer}   data.bigCarSize     大车车位数
	 * @apiSuccess {Integer}   data.smallCarSize     小车车车位数
	 * @apiSuccess {String}   data.parkInfo     车位描述
	 * @apiSuccess {String}    data.transformerBelong    变电器归属方
	 * @apiSuccess {Double}    data.initialEnergy    初始电量
	 * @apiSuccess {Double}    data.purPrice  购电单价
	 * @apiSuccess {Double}    data.purPrice1   尖期价
	 * @apiSuccess {Double}    data.purPrice2   峰期价
	 * @apiSuccess {Double}    data.purPrice3    平期价
	 * @apiSuccess {Double}    data.purPrice4   谷期价
	 * @apiSuccess {Double}    data.networkFee   网络费
	 * @apiSuccess {String}    data.costAmortization  费用摊销
	 * @apiSuccess {Double}    data.parkingFee    停车费
	 * @apiSuccess {Double}    data.propertyFee    物业管理费
	 * @apiSuccess {Double}    data.artificialFee    人工支出
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="stations/{stationId}",method = RequestMethod.GET)
	public ResultVo getStationById(@PathVariable("stationId") Integer stationId) throws BizException{
		ResultVo resVo = new ResultVo();
		Map  map = stationService.getStationById(stationId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 *
	 *
	 * @api {get} /api/tosubs    第三方场站列表
	 * @apiName getToSubsAll
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
 	 * @apiDescription 杨帅B 根据充电桩,运营商,服务电话,运营状态,建设场所...条件查询第三方场站列表数据
	 * <br/>
	 * @apiParam {Integer} userId   用户Id
	 * @apiParam {String} [stationId] 充电站ID-运营商自定义的唯一编码
	 * @apiParam {String} [operatorId] 运营商ID-运营商ID
	 * @apiParam {String} [stationname] 充电站名称-充电站名称的描述
	 * @apiParam {String} [servicetel] 服务电话-平台服务电话，例如400的电话
	 * @apiParam {Integer} [stationstatus] 运营状态(站点状态)
	 * @apiParam {Integer} [construction] 建设场所
	 * @apiParam {Integer} pageNum 页码
	 * @apiParam {Integer} pageSize 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.id
	 * @apiSuccess {String} data.list.operatorname 运营商
	 * @apiSuccess {String} data.list.stationid 充电站ID-运营商自定义的唯一编码
	 * @apiSuccess {String} data.list.stationname 充电站名称-充电站名称的描述
	 * @apiSuccess {Integer} data.list.stationtype 场站类型
	 * @apiSuccess {Integer} data.list.stationstatus 运营状态(站点状态)
	 * @apiSuccess {Integer} data.list.construction 建设场所
	 * @apiSuccess {Integer} data.list.parknums 车位数量-可停放进行充电的车位总数，默认：0 未知
	 * @apiSuccess {Double} data.list.stationlng 经度-GCJ-02坐标系
	 * @apiSuccess {Double} data.list.stationlat 纬度-GCJ-02坐标系
	 * @apiSuccess {String} data.list.areacode 充电站省市辖区编码-填写内容为参照GB/T2260-2013
	 * @apiSuccess {String} data.list.address 详细地址-详细地址
	 * @apiSuccess {String} data.list.servicetel 服务电话-平台服务电话，例如400的电话
	 * @apiSuccess {String} data.list.matchcars 使用车型描述
	 * @apiSuccess {String} data.list.parkinfo 车位楼层及数量描述
	 * @apiSuccess {Integer} data.list.supportorder 是否支持预约-0为不支持预约、1为支持预约。不填默认为0
	 * @apiSuccess {String} data.list.payment 支付方式
	 * @apiSuccess {String} data.list.businehours 营业时间   yyyy-MM-dd
	 * @apiSuccess {String} data.list.stationtel 站点电话-能够联系场站工作人员进行协助的联系电话
	 * @apiSuccess {String} data.list.siteguide 站点引导-描述性文字，用于引导车主找到充电车位
	 * @apiSuccess {String} data.list.parkfee 停车费-停车费率描述
	 * @apiSuccess {String} data.list.electricityfee 充电电费率
	 * @apiSuccess {String} data.list.servicefee 服务费率
	 * @apiSuccess {String} data.list.remark 备注-其他备注信息
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="tosubs",method=RequestMethod.GET)
	public ResultVo getToSubsAll(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo list = stationService.getToSubsAll(map);
		resVo.setData(list);
		return resVo;
	}
	
	/**
	 *
	 * @api {get} /api/stations/station   查询场站业务字典
	 * @apiName getStation
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 查询场站业务字典 支持运营商关联
	 * <br/>
	 * @apiParam {Integer}    [userId]    登录用户Id
	 * @apiParam {Integer}    [orgId]     运营商Id
	 * @apiParam {String}	  [orgName]	  运营商名称
	 * @apiParam {String}    [stationName]     场站名称
	 * @apiParam {Integer}    [pageNum]    页码  不传默认为1
	 * @apiParam {Integer}    [pageSize]   页大小 不传默认为20
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.id 场站id
	 * @apiSuccess {String} data.list.text 场站名称
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="stations/station",method=RequestMethod.GET)
	public ResultVo getStationDict(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		List<ComboxVo> cvList= stationService.getStationDict(map);
		resVo.setData(cvList);
		return resVo;
	}
	/**
	 *
	 * @api {get} /api/stations/busidict   查询场站业务字典
	 * @apiName getStation
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 查询场站业务字典 支持运营商关联
	 * <br/>
	 * @apiParam {Integer}    [userId]    登录用户Id
	 * @apiParam {Integer}    [orgId]     运营商Id
	 * @apiParam {String}	  [orgName]	  运营商名称
	 * @apiParam {Integer}    [stationId]     场站名称
	 * @apiParam {String}    [stationName]     场站名称
	 * @apiParam {Integer}    [pageNum]    页码  不传默认为1
	 * @apiParam {Integer}    [pageSize]   页大小 不传默认为20
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.id 场站id
	 * @apiSuccess {String} data.list.stationNo 场站stationNo
	 * @apiSuccess {String} data.list.name 场站名称
	 * @apiSuccess {String} data.list.orgId 企业Id
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="stations/busidict",method=RequestMethod.GET)
	public ResultVo getStation(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo pageList= stationService.getStation(map);
		resVo.setData(pageList);
		return resVo;
	}
	/**
	 *
	 * @api {get} /api/stations/orgNo/{userId}   查询运营商编号
	 * @apiName getOrgNo
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯 查询新增场站业务字典 查询运营商编号
	 * <br/>
	 * @apiParam {Integer}    userId    登录用户Id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.orgId  场站id
	 * @apiSuccess {String} data.orgNo   运营商编号
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常-
	 */
	@RequestMapping(value="stations/orgNo/{userId}",method=RequestMethod.GET)
	public ResultVo getOrgNo(@PathVariable("userId") Integer userId) throws BizException{
		ResultVo resVo = new ResultVo();
		Map map = new HashMap();
		map.put("userId",userId);
		DataVo dataVo = stationService.getOrgNo(map);
		resVo.setData(dataVo);
		return resVo;
	}

	/**
	 *
	 * @api {POST} /api/stations    新增场站信息
	 * @apiName saveStation
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯 新增场站信息 post 表单类型MultipartHttpServletRequest(from-data)
	 * <br/>
	 * @apiParam {String}    stationNo 			场站编号(运营商编号+自编号)
	 * @apiParam {String}    stationName    	场站名称
	 * @apiParam {String}    [innerNo]    内部编号
	 * @apiParam {String}    [innerName]  内部名称
	 * @apiParam {Integer}   orgId     			运营商Id （字典 运营商下拉树）
	 * @apiParam {Integer}   construction     建设场所 （jscs）
	 * @apiParam {Integer}    stationType     场站类型  (jingylx)
	 * @apiParam {String}   serviceUnit    服务单位
	 * @apiParam {Integer}   manageType    管理类型 (gllx)
	 * @apiParam {Integer}   serviceType    服务类型(fflx)
	 * @apiParam {Integer}    stationStatus     场站状态(yyzt)
	 * @apiParam {Integer}    [useStatus]     使用状态  (zt)
	 * @apiParam {String}    [stationContact]     联系人
	 * @apiParam {String}    [stationTel]     联系电话
	 * @apiParam {String}    repairMan     维修员
	 * @apiParam {String}    repairPhone    维修电话
	 * @apiParam {Integer}    employeeSize    员工数量
	 * @apiParam {Data}      busineHours	营业时间
	 * @apiParam {String}    [supportOrder]     是否支持预约(sf)
	 * @apiParam {String}    payMent    支付方式	,默认值:刷卡、线上、现金
	 * @apiParam {String}    electricityFee    充电费描述(元/度)
	 * @apiParam {String}    parkFee    停车费描述
	 * @apiParam {String}    serviceFee     服务费描述(元/度)
	 * @apiParam {Integer}    provCode     省 (通用区域字典)
	 * @apiParam {Integer}    cityCode     市(通用区域字典)
	 * @apiParam {Integer}    distCode     县(通用区域字典)
	 * @apiParam {Double}    address     详细地址
	 * @apiParam {Double}    [stationArea]     占地面积(平米)
	 * @apiParam {String}    [stationLng]     经度
	 * @apiParam {String}    [stationLat]     纬度
	 * @apiParam {String}    [remark]    备注
	 * @apiParam {Integer}    stationEtwork    场站网络 (czwl)
	 * @apiParam {Integer}    businessPeople   电信运营商(czyys)
	 * @apiParam {String}    etworkSpeed   网络速率
	 * @apiParam {Integer}    etworkSize   数量
	 * @apiParam {String}    expiryTime  有效期至 yyyy-MM-dd
	 * @apiParam {String}    payTime   交费日期 yyyy-MM-dd
	 * @apiParam {String}    tariffInformation  资费信息
	 * @apiParam {Integer}   stationModel     运营模式(yyms)
	 * @apiParam {Integer}   stationCoopType     合作方式(hzfs)
	 * @apiParam {String}   [businessBelong]     运营权归属
	 * @apiParam {String}   [businessPhone]     运营联系电话
	 * @apiParam {String}   [assetsBelong]     资产归属
	 * @apiParam {String}   [landBelong]     土地归属
	 * @apiParam {Double}   [investAmount]     投资金额
	 * @apiParam {Double}   [budgetAmount]     预算金额
	 * @apiParam {Double}   [targetCharge]     目标收入
	 * @apiParam {Double}   [targetIncome]     目标充电量
	 * @apiParam {String}   [landBelong]     土地归属
	 * @apiParam {Data}    [rentalStartDate]   场站租赁有效期开始  yyyy-MM-dd
	 * @apiParam {Data}    [rentalEndDate]     场站租赁有效期结束 yyyy-MM-dd
	 * @apiParam {Data}    [stationStartDate]  场站有效期开始 yyyy-MM-dd
	 * @apiParam {Data}    [stationEndDate]    场站有效期结束 yyyy-MM-dd
	 * @apiParam {Data}   [testTime]     试运营日期 yyyy-MM-dd
	 * @apiParam {Data}   [formalTime]     正式运营日期 yyyy-MM-dd
	 * @apiParam {MultipartFile}   [leasePic]     租赁合同地址
	 * @apiParam {Integer}      [serviceCars]   服务车型(cllx)
	 * @apiParam {Integer}   [bigCarSize]     大车车位数
	 * @apiParam {Integer}   [smallCarSize]     小车车车位数
	 * @apiParam {String}   [parkInfo]     车位描述
	 * @apiParam {String}    [transformerBelong]    变电器归属方
	 * @apiParam {Double}    [initialEnergy]    初始电量
	 * @apiParam {Double}    [purPrice]  购电单价
	 * @apiParam {Double}    [purPrice1]   尖期价
	 * @apiParam {Double}    [purPrice2]   峰期价
	 * @apiParam {Double}    [purPrice3]    平期价
	 * @apiParam {Double}    [purPrice4]    谷期价
	 * @apiParam {Double}    [networkFee]   网络费
	 * @apiParam {String}    [costAmortization]   费用摊销
	 * @apiParam {Double}    [parkingFee]    停车费
	 * @apiParam {Double}    [propertyFee]    物业管理费
	 * @apiParam {Double}    [artificialFee]    人工支出
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001   {0}不能为空!
	 * @apiError -1102006   {0}格式异常!
	 */
	@RequestMapping(value="/stations",method=RequestMethod.POST)
	public ResultVo saveStation(MultipartHttpServletRequest multiRequest) throws Exception {
		MultipartFile file = multiRequest.getFile("leasePic");
		DataVo dataMap = new DataVo(CommonUtils.gerParamterMap(multiRequest.getParameterMap()));
		String [] list = {"stationNo","stationName","orgId","construction","stationType",
				"stationStatus", "busineHours",
				"payMent", "electricityFee","parkFee","serviceFee","provCode","cityCode","distCode","address",
				"stationModel", "stationCoopType","stationTel","stationLng","stationLat"};
		Map map  = new HashMap();
		map.put("stationNo","场站编号");
		map.put("stationName","场站名称");
		map.put("orgId","运营商Id ");
		map.put("construction","建设场所");
		map.put("stationType","场站类型");
		map.put("stationStatus","场站状态");
		map.put("busineHours","营业时间");
		map.put("payMent","支付方式");
		map.put("electricityFee","充电费描述");
		map.put("parkFee","停车费描述");
		map.put("serviceFee","服务费描述");
		map.put("provCode","省");
		map.put("cityCode","市");
		map.put("distCode","县");
		map.put("address","详细地址");
		map.put("stationModel","运营模式");
		map.put("stationCoopType","合作方式");
		map.put("stationTel","联系电话");
		map.put("stationLng","经度");
		map.put("stationLat","纬度");
//		map.put("purPrice","购电单价");
		ChargeManageUtil.isMapNullPoint(list,dataMap,map);
		stationService.saveStation(dataMap,file);
		return new ResultVo();
	}
	
	/**
	 *
	 * @api {PUT} /api/stations/_update    编辑场站信息
	 * @apiName updateStation
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯 根据场站Id编辑该场站信息
	 * <br/>
	 * @apiParam {String}    stationId 		      场站
	 * @apiParam {String}    [stationNo] 			场站编号(运营商编号+自编号)
	 * @apiParam {String}    [stationName]    	场站名称
	 * @apiParam {String}    [innerNo]    内部编号
	 * @apiParam {String}    [innerName]  内部名称
	 * @apiParam {Integer}   [orgId]    			运营商Id （字典 运营商下拉树）
	 * @apiParam {Integer}   [construction]     建设场所 （jscs）
	 * @apiParam {Integer}    [stationType]     场站类型  (jingylx)
	 * @apiParam {String}   [serviceUnit]   服务单位
	 * @apiParam {Integer}   [manageType]   管理类型 (gllx)
	 * @apiParam {Integer}   [serviceType]  服务类型(fflx)
	 * @apiParam {Integer}    [stationStatus]     场站状态(yyzt)
	 * @apiParam {Integer}    [useStatus]     使用状态  (zt)
	 * @apiParam {String}    [stationContact]     联系人
	 * @apiParam {String}    [stationTel]     联系电话
	 * @apiParam {String}    [repairMan]    维修员
	 * @apiParam {String}    [repairPhone]    维修电话
	 * @apiParam {Integer}   [employeeSize ]   员工数量
	 * @apiParam {Data}      [busineHours]	营业时间
	 * @apiParam {String}    [supportOrder]     是否支持预约(sf)
	 * @apiParam {String}    [payMent]   支付方式	,默认值:刷卡、线上、现金
	 * @apiParam {String}    [electricityFee]   充电费描述(元/度)
	 * @apiParam {String}    [parkFee]   停车费描述
	 * @apiParam {String}    [serviceFee]     服务费描述(元/度)
	 * @apiParam {Integer}    [provCode]     省 (通用区域字典)
	 * @apiParam {Integer}    [cityCode]     市(通用区域字典)
	 * @apiParam {Integer}    [distCode]     县(通用区域字典)
	 * @apiParam {Double}   [address]     详细地址
	 * @apiParam {Double}    [stationArea]     占地面积(平米)
	 * @apiParam {String}    [stationLng]     经度
	 * @apiParam {String}    [stationLat]     纬度
	 * @apiParam {String}    [remark]    备注
	 * @apiParam {Integer}    [stationEtwork]   场站网络 (czwl)
	 * @apiParam {Integer}   [ businessPeople]  电信运营商(czyys)
	 * @apiParam {String}    [etworkSpeed]  网络速率
	 * @apiParam {Integer}   [etworkSize]   数量
	 * @apiParam {String}    [expiryTime]  有效期至 yyyy-MM-dd
	 * @apiParam {String}    [payTime]   交费日期 yyyy-MM-dd
	 * @apiParam {String}    [tariffInformation]  资费信息
	 * @apiParam {Integer}   [stationModel]     运营模式(yyms)
	 * @apiParam {Integer}   [stationCoopType]    合作方式(hzfs)
	 * @apiParam {String}   [businessBelong]     运营权归属
	 * @apiParam {String}   [businessPhone]     运营联系电话
	 * @apiParam {String}   [assetsBelong]     资产归属
	 * @apiParam {String}   [landBelong]     土地归属
	 * @apiParam {Double}   [investAmount]     投资金额
	 * @apiParam {Double}   [budgetAmount]     预算金额
	 * @apiParam {Double}   [targetCharge]     目标收入
	 * @apiParam {Double}   [targetIncome]     目标充电量
	 * @apiParam {Data}    [rentalStartDate]   场站租赁有效期开始  yyyy-MM-dd
	 * @apiParam {Data}    [rentalEndDate]     场站租赁有效期结束 yyyy-MM-dd
	 * @apiParam {Data}    [stationStartDate]  场站有效期开始 yyyy-MM-dd
	 * @apiParam {Data}    [stationEndDate]    场站有效期结束 yyyy-MM-dd
	 * @apiParam {Data}   [testTime]     试运营日期 yyyy-MM-dd
	 * @apiParam {Data}   [formalTime]     正式运营日期 yyyy-MM-dd
	 * @apiParam {MultipartFile}   [leasePic]     租赁合同地址
	 * @apiParam {Integer}      [serviceCars]   服务车型(cllx)
	 * @apiParam {Integer}   [bigCarSize]     大车车位数
	 * @apiParam {Integer}   [smallCarSize]     小车车车位数
	 * @apiParam {String}   [parkInfo]     车位描述
	 * @apiParam {String}    [transformerBelong]    变电器归属方
	 * @apiParam {Double}    [initialEnergy]    初始电量
	 * @apiParam {Double}    [purPrice]  购电单价
	 * @apiParam {Double}    [purPrice1]   尖期价
	 * @apiParam {Double}    [purPrice2]   峰期价
	 * @apiParam {Double}    [purPrice3]    平期价
	 * @apiParam {Double}    [purPrice4]    谷期价
	 * @apiParam {Double}    [networkFee]   网络费
	 * @apiParam {String}    [costAmortization]   费用摊销
	 * @apiParam {Double}    [parkingFee]    停车费
	 * @apiParam {Double}    [propertyFee]    物业管理费
	 * @apiParam {Double}    [artificialFee]    人工支出
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001   {0}不能为空!
	 * @apiError -1102006   {0}格式异常!
	 */
	@RequestMapping(value="stations/_update",method=RequestMethod.POST)
	public ResultVo updateStation(MultipartHttpServletRequest multiRequest) throws Exception{
		MultipartFile file = multiRequest.getFile("leasePic");
		DataVo dataMap = new DataVo(CommonUtils.gerParamterMap(multiRequest.getParameterMap()));
		String [] list = {"stationNo","stationName","orgId","construction","stationType",
				"stationStatus", "busineHours",
				"payMent", "electricityFee","parkFee","serviceFee","provCode","cityCode","distCode","address",
				"stationModel", "stationCoopType","stationTel","stationLng","stationLat"};
		Map map  = new HashMap();
		map.put("stationNo","场站编号");
		map.put("stationName","场站名称");
		map.put("orgId","运营商Id ");
		map.put("construction","建设场所");
		map.put("stationType","场站类型");
		map.put("stationStatus","场站状态");
		map.put("busineHours","营业时间");
		map.put("payMent","支付方式");
		map.put("electricityFee","充电费描述");
		map.put("parkFee","停车费描述");
		map.put("serviceFee","服务费描述");
		map.put("provCode","省");
		map.put("cityCode","市");
		map.put("distCode","县");
		map.put("address","详细地址");
		map.put("stationModel","运营模式");
		map.put("stationCoopType","合作方式");
		map.put("stationTel","联系电话");
		map.put("stationLng","经度");
		map.put("stationLat","纬度");
//		map.put("purPrice","购电单价");
		ChargeManageUtil.isMapNullPoint(list,dataMap,map);
		if(dataMap.isNotBlank("stationId")){
			stationService.updateStation(dataMap,file);
			return new ResultVo();
		}else {
			throw   new BizException(1102001,"stationId");
		}

	}
	
	
	/**
	 *
	 * @api {get}  /api/stations/_export  场站列表信息导出
	 * @apiName exportStation
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据区域,运营商,场站,经营类型,运营状态,建设场所,使用状态,运营模式条件查询场站列表信息导出
	 * <br/>
	 * @apiParam {Integer} userId 用户Id
	 * @apiParam {Integer} [orgId] 运营商Id 
	 * @apiParam {String} [stationName] 场站名称 
	 * @apiParam {String} [stationType] 经营类型 
	 * @apiParam {String} [stationStatus] 运营状态 
	 * @apiParam {String} [construction] 建设场所 
	 * @apiParam {String} [useStatus] 使用状态0:有效 1:无效 
	 * @apiParam {String} [stationModel] 运营模式01：自运营；02：合作运营；03：其他运营商 
	 * @apiParam {String} [provCode] 省 
	 * @apiParam {String} [cityCode] 市 
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="stations/_export",method=RequestMethod.GET)
	public void exportStation(@RequestParam Map map,HttpServletResponse response) throws Exception{
		stationService.exportStation(map,response);
	}

	/**
	 * 
	 * @api {get} /api/tosubs/_export    第三方场站列表数据导出
	 * @apiName exportToStation
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据充电桩,运营商,服务电话,运营状态,建设场所...条件查询第三方场站列表数据导出
	 * <br/>
	 * @apiParam {String} [stationId] 充电站ID-运营商自定义的唯一编码  
	 * @apiParam {String} [operatorId] 运营商ID-运营商ID	
	 * @apiParam {String} [stationname] 充电站名称-充电站名称的描述	
	 * @apiParam {String} [servicetel] 服务电话-平台服务电话，例如400的电话	
	 * @apiParam {Integer} [stationstatus] 运营状态(站点状态)	
	 * @apiParam {Integer} [construction] 建设场所	
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="tosubs/_export",method=RequestMethod.GET)
	public void exportToStation(@RequestParam Map map,HttpServletResponse response) throws Exception{
		stationService.exportToStation(map,response);
	}
	
	/**
	 * 
	 * @api {post} /api/stations/_import    导入场站信息
	 * @apiName importMembers
	 * @apiGroup StationController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 导入场站信息
	 * <br/>
	 * @apiParam {MultipartFile}    file   文件
	 * @apiParam {Integer}   userId  用户Id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {String} data.list.orgName 分页数据对象数组
	 * @apiSuccess {String} data.list.result		数据状态
	 * @apiSuccess {String} data.list.stationNo		场站编号
	 * @apiSuccess {String} data.list.stationName	场站名称
	 * @apiSuccess {String} data.list.stationModel	运营模式
	 * @apiSuccess {String} data.list.construction	建设场所
	 * @apiSuccess {String} data.list.stationStatus 场站状态
	 * @apiSuccess {String} data.list.stationType	场站类型
	 * @apiSuccess {String} data.list.parkNums		车位数量
	 * @apiSuccess {String} data.list.provCode		省
	 * @apiSuccess {String} data.list.cityCode		市
	 * @apiSuccess {String} data.list.dictCode		县
	 * @apiSuccess {String} data.list.address		详细地址
	 * @apiSuccess {String} data.list.stationArea	场地面积
	 * @apiSuccess {String} data.list.parkInfo		车位描述
	 * @apiSuccess {String} data.list.payMent		支付方式
	 * @apiSuccess {String} data.list.busineHours	营业时间
	 * @apiSuccess {String} data.list.stationContact	联系人
	 * @apiSuccess {String} data.list.stationTel	联系电话
	 * @apiSuccess {String} data.list.remark		备注
	 * @apiSuccess {String} data.list.innerNo		内部编号
	 * @apiSuccess {String} data.list.innerName		内部名称
	 * @apiSuccess {String} data.list.purPrice		购电单价
	 * @apiSuccess {String} data.list.matchCars		使用车型
	 * @apiSuccess {String} data.list.supportOrder	是否支持预约
	 * @apiSuccess {String} data.list.electricityFee	充电费描述
	 * @apiSuccess {String} data.list.serviceFee	服务费描述
	 * @apiSuccess {String} data.list.parkFee		停车费描述
	 * @apiSuccess {String} data.list.rentalStartDate	场站租赁有效期开始
	 * @apiSuccess {String} data.list.rentalEndDate		场站租赁有效期结束
	 * @apiSuccess {String} data.list.stationStartDate	场站有效期开始
	 * @apiSuccess {String} data.list.stationEndDate	场站有效期结束
	 * @apiSuccess {String} data.list.stationLng	经度
	 * @apiSuccess {String} data.list.stationLat	纬度
	 * @apiSuccess {String} data.list.check			数据校验结果
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="stations/_import",method=RequestMethod.POST)
	public ResultVo importMembers(HttpServletRequest request,@RequestParam("file") MultipartFile file,@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile excelFile=multipartRequest.getFile("file");
		List importList =  stationService.importStation(excelFile,map);
		vo.setData(importList);
		return vo;
	}
	
	/**
	 * 
	 * 
	 * @api {get} /api/tosubs/dict/operatorid    查询第三方运营商业务字典
	 * @apiName getToSubOperatorIdsDict
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 查询第三方运营商业务字典
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Integer}    [pageNum]    页码  默认:1
	 * @apiParam {Integer}    [userId]     页大小 默认:20
	 * 
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.operatorId  第三方运营商Id
	 * @apiSuccess {String} data.list.operatorName 第三方运营商名称
	 * 
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001 {0}不能为空!
	 * 
	 */
	@RequestMapping(value="tosubs/dict/operatorid",method = RequestMethod.GET)
	public ResultVo getToSubOperatorIdsDict(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo list = stationService.getToSubOperatorIdsDict(map);
		resVo.setData(list);
	 	return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {get} /api/tosubs/dict/tostation   查询第三方场站业务字典
	 * @apiName getToStationinfoDict
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 查询第三方场站业务字典
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Integer}    [operatorId]    第三方运营商Id
	 * @apiParam {Integer}    [pageNum]    页码  默认:1
	 * @apiParam {Integer}    [userId]     页大小 默认:20
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {String} data.list.stationid 第三方场站Id
	 * @apiSuccess {String} data.list.stationName 第三方场站名称
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="tosubs/dict/tostation",method = RequestMethod.GET)
	public ResultVo getToStationinfoDict(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo list = stationService.getToStationinfoDict(map);
		resVo.setData(list);
		return resVo;
	}
	
}

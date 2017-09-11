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
import com.clouyun.charge.modules.vehicle.service.DriverService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/api/drivers")
public class DriverController extends BaseController{
	
	@Autowired
	DriverService driverService;
	
	
	/**
     * @api {GET} /api/drivers     驾驶员列表
     * @apiName queryDrivers
     * @apiGroup DriverController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾   驾驶员列表
     * <br/>
     * @apiParam {Integer}  userId    		用户Id
     * @apiParam {int}      pageNum         页码
     * @apiParam {int}      pageSize  		页大小
     * @apiParam {String} [driverName]     	驾驶员姓名
     * @apiParam {int}    [mobilePhone]  	手机号
     * @apiParam {Date}   [bigCardTime]  	初次领证日期 (开始时间) yyyy-MM-dd
     * @apiParam {Date}   [endCardTime] 	初次领证日期 (开始时间) yyyy-MM-dd
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {String} data.list.models 		准驾车型
     * @apiSuccess {Date} data.list.birthday 		出生日期   yyyy-MM-dd
     * @apiSuccess {Stirng} data.list.sex 			性别：1男，2女
     * @apiSuccess {Stirng} data.list.nationality 	国籍
     * @apiSuccess {int} data.list.mobilePhone 		手机号
     * @apiSuccess {int} data.list.driverId 		驾驶员id(主键)
     * @apiSuccess {Date} data.list.cardTime 		初次领证日期 yyyy-MM-dd
     * @apiSuccess {String} data.list.driverName 	驾驶员姓名
     * @apiSuccess {Date} data.list.operatingTime   操作时间 yyyy-MM-dd
     * @apiSuccess {Date} data.list.identityCard    身份证号
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "pageNum": 1,
			    "pageSize": 1,
			    "size": 1,
			    "startRow": 0,
			    "endRow": 0,
			    "total": 1,
			    "pages": 1,
			    "list": [
			      {
			        "models": "时空穿梭机",
			        "birthday": "2016-11-02",
			        "sex": "男",
			        "nationality": "外太空",
			        "mobilePhone": "18566766948",
			        "driverId": 1,
			        "cardTime": "2016-11-02",
			        "driverName": "曹智",
			        "operatingTime": "2016-12-22"
			      }
			    ],
			    "prePage": 0,
			    "nextPage": 0,
			    "isFirstPage": true,
			    "isLastPage": true,
			    "hasPreviousPage": false,
			    "hasNextPage": false,
			    "navigatePages": 8,
			    "navigatepageNums": [
			      1
			    ],
			    "navigateFirstPage": 1,
			    "navigateLastPage": 1,
			    "firstPage": 1,
			    "lastPage": 1
			  }
			}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo queryDrivers(@RequestParam Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = driverService.queryDrivers(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
	
	
	/**
     * @api {GET} /api/drivers/vehicle/{vehicleId}     通过车辆Id查询驾驶员信息
     * @apiName queryDriversByVehicleId
     * @apiGroup DriverController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾   通过车辆Id查询驾驶员信息
     * <br/>
     * @apiParam {int}    vehicleId  	车辆Id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object[]} data 数据对象数组
     * @apiSuccess {String} data.models 		准驾车型
     * @apiSuccess {Date} data.birthday 		出生日期   yyyy-MM-dd
     * @apiSuccess {Stirng} data.sex 			性别：1男，2女
     * @apiSuccess {Stirng} data.nationality 	国籍
     * @apiSuccess {int} data.mobilePhone 		手机号
     * @apiSuccess {int} data.driverId 			驾驶员id(主键)
     * @apiSuccess {Date} data.cardTime 		初次领证日期 yyyy-MM-dd
     * @apiSuccess {String} data.driverName 	驾驶员姓名
     * @apiSuccess {String} data.orgId 			企业Id
     * @apiSuccess {String} data.orgName		所属企业
     * @apiSuccess {String} data.validTerm 		驾驶证有效期开始时间
     * @apiSuccess {String} data.endValidTerm 	驾驶证失效时间
     * @apiSuccess {String} data.address 		驾驶员地址
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     *{
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": [
			    {
			      "birthday": "2016-06-08",
			      "sex": 2,
			      "identityCard": null,
			      "operatingTime": "2016-11-10",
			      "orgId": null,
			      "models": "C1,C2",
			      "nationality": "就哭了了解",
			      "mobilePhone": "18566766948",
			      "validTerm": "2016-08-10",
			      "address": "阿斯发放",
			      "driverId": 4,
			      "userId": null,
			      "endValidTerm": "2016-09-07",
			      "cardTime": "2016-08-24",
			      "certificateImgUrl": "images/1478777034465.png",
			      "driverName": "test 驾驶员"
			    },
			    {
			      "birthday": "2016-11-02",
			      "sex": 1,
			      "identityCard": null,
			      "operatingTime": "2017-05-19",
			      "orgId": 2,
			      "models": "时空穿梭机",
			      "nationality": "外太空",
			      "mobilePhone": "18566766948",
			      "validTerm": "2016-11-02",
			      "address": "未知星系",
			      "driverId": 5,
			      "userId": null,
			      "endValidTerm": "2016-11-02",
			      "cardTime": "2016-11-02",
			      "certificateImgUrl": "images/1482391853789.png",
			      "driverName": "大灰狼"
			    }
			  ]
	 *	  }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	//@GetMapping("/vehicle/{vehicleId}")
	@RequestMapping(value = "/vehicle/{vehicleId}", method = RequestMethod.GET)
	public ResultVo queryDriversByVehicleId(@PathVariable("vehicleId") Integer vehicleId) throws Exception {
		ResultVo vo = new ResultVo();
		List<DataVo> drivers = driverService.getDrivers(vehicleId);
		vo.setData(drivers);
		return vo;
	}
	
	/**
     * @api {GET} /api/drivers/{driverId}     查询驾驶员信息
     * @apiName queryDriver
     * @apiGroup DriverController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾         查询驾驶员信息
     * <br/>
     * @apiParam {int}    driverId  	驾驶员Id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object[]} data 数据对象数组
     * @apiSuccess {String} data.models 		准驾车型
     * @apiSuccess {Date} data.birthday 		出生日期   yyyy-MM-dd
     * @apiSuccess {Stirng} data.sex 			性别：1男，2女
     * @apiSuccess {Stirng} data.nationality 	国籍
     * @apiSuccess {int} data.mobilePhone 		手机号
     * @apiSuccess {int} data.driverId 			驾驶员id(主键)
     * @apiSuccess {Date} data.cardTime 		初次领证日期 yyyy-MM-dd
     * @apiSuccess {String} data.driverName 	驾驶员姓名
     * @apiSuccess {String} data.orgId 			所属企业
     * @apiSuccess {String} data.validTerm 		驾驶证有效期开始时间
     * @apiSuccess {String} data.endValidTerm 	驾驶证失效时间
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
			  "errorCode": 0,
			  "errorMsg": "操作成功!",
			  "total": 0,
			  "data": {
			    "birthday": "1972-11-08",
			    "sex": 2,
			    "vehicleId": null,
			    "identityCard": null,
			    "operatingTime": "2016-11-02",
			    "orgId": null,
			    "models": "C1,C2",
			    "nationality": "歪果仁",
			    "mobilePhone": "12345678945",
			    "validTerm": "2000-02-02",
			    "address": "不详",
			    "driverId": 3,
			    "userId": null,
			    "endValidTerm": "2020-12-23",
			    "cardTime": "2011-11-01",
			    "certificateImgUrl": "images/1478073488504.jpg",
			    "driverName": "天使投资"
			  }
			}
	 *	}
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/{driverId}", method = RequestMethod.GET)
	public ResultVo queryDriver(@PathVariable("driverId") Integer driverId) throws Exception {
		ResultVo vo = new ResultVo();
		Map driver = driverService.getDriver(driverId);
		vo.setData(driver);
		return vo;
	}
	
	/**
     * @api {GET} /api/drivers/_export   导出驾驶员列表
     * @apiName exportDrivers
     * @apiGroup DriverController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾   导出驾驶员列表
     * <br/>
     * @apiParam {int}    [pageNum]     页码
     * @apiParam {int}    [pageSize]  	页大小
     * @apiParam {String} [driverName]  驾驶员姓名
     * @apiParam {int}    [mobilePhone] 手机号
     * @apiParam {Date}   [bigCardTime] 初次领证日期 (开始时间) yyyy-MM-dd
     * @apiParam {Date}   [endCardTime] 初次领证日期 (开始时间) yyyy-MM-dd
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
    public void exportDrivers(@RequestParam Map map,HttpServletResponse response) throws Exception {
		driverService.export(map,response);
    }
	
	/**
     * @api {POST} /api/drivers   新增驾驶员
     * @apiName insertDriver
     * @apiGroup DriverController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾  新增驾驶员
     * <br/>
     * @apiParam {String} 	orgId     			企业ID
     * @apiParam {String}   driverName  		姓名
     * @apiParam {String}   sex  				性别 1：男 2：女
     * @apiParam {String}   nationality 		国籍
     * @apiParam {String} 	address     		地址
     * @apiParam {Date}    	birthday  			生日  yyyy-MM-dd
     * @apiParam {Date}   	cardTime  			初次领证日期  yyyy-MM-dd
     * @apiParam {String}   mobilePhone 		电话
     * @apiParam {String}   identityCard  		身份证号
     * @apiParam {String}   models  			准驾车型
     * @apiParam {Date}   	validTerm  			驾驶证有效期(开始时间) yyyy-MM-dd
     * @apiParam {Date}   	endValidTerm 		驾驶证有效期(结束时间) yyyy-MM-dd
     * @apiParam {String} 	certificateImgUrl 	驾驶证上传图片的路径地址
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
        "birthday": "2016-11-02",
        "sex": 1,
        "orgId": 2,
        "models": "时空穿梭机",
        "nationality": "外太空",
        "mobilePhone": "18566766948",
        "validTerm": "2016-11-02",
        "address": "未知星系",
        "endValidTerm": "2016-11-02",
        "identityCard" :"2134241546675"
        "cardTime": "2016-11-02",
        "certificateImgUrl": "images/1482391853789.png",
        "driverName": "大灰狼"
	 * }
     * <br/>
     * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        更新受影响行数(0:未新增;1:更新一个)
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1502000 字段不能为空!
     * @apiError -1502001 用户信息已存在!
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "", method = RequestMethod.POST)
    public ResultVo insertDriver(MultipartHttpServletRequest multiRequest) throws Exception {
		ResultVo resVo = new ResultVo();
		MultipartFile file = multiRequest.getFile("file");
		DataVo dataMap = new DataVo(CommonUtils.gerParamterMap(multiRequest.getParameterMap()));
		driverService.insertDriver(dataMap,file);
		return resVo;
		
    }
	
	/**
     * @api {POST} /api/drivers/_update   编辑驾驶员
     * @apiName updateDrivers
     * @apiGroup DriverController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾     	编辑驾驶员
     * <br/>
     * @apiParam {String} 	orgId     			企业ID
     * @apiParam {String} 	driverId     		驾驶员ID
     * @apiParam {String}   driverName  		姓名
     * @apiParam {String}   sex  				性别 1：男 2：女
     * @apiParam {String}   nationality 		国籍
     * @apiParam {String} 	address     		地址
     * @apiParam {Date}    	birthday  			生日  yyyy-MM-dd
     * @apiParam {Date}   	cardTime  			初次领证日期  yyyy-MM-dd
     * @apiParam {String}   mobilePhone 		电话
     * @apiParam {String}   identityCard  		身份证号
     * @apiParam {String}   models  			准驾车型
     * @apiParam {Date}   	validTerm  			驾驶证有效期(开始时间) yyyy-MM-dd
     * @apiParam {Date}   	endValidTerm 		驾驶证有效期(结束时间) yyyy-MM-dd
     * @apiParam {String} 	certificateImgUrl 	驾驶证上传图片的路径地址
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
        "birthday": "2016-11-02",
        "sex": 1,
        "orgId": 2,
        "models": "时空穿梭机",
        "nationality": "外太空",
        "mobilePhone": "18566766948",
        "validTerm": "2016-11-02",
        "address": "未知星系",
        "endValidTerm": "2016-11-02",
        "cardTime": "2016-11-02",
        "identityCard" :"2134241546675"
        "certificateImgUrl": "images/1482391853789.png",
        "driverName": "大灰狼"
        "driverId": 93
	 * }
     * <br/>
     * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        更新受影响行数(0:未编辑成功;1:编辑成功一个)
	 * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1502000 字段不能为空!
	 */
	@RequestMapping(value = "/_update", method = RequestMethod.POST)
    public ResultVo updateDrivers(MultipartHttpServletRequest multiRequest) throws Exception {
		ResultVo resVo = new ResultVo();
		MultipartFile file = multiRequest.getFile("file");
		DataVo dataMap = new DataVo(CommonUtils.gerParamterMap(multiRequest.getParameterMap()));
		driverService.updateDriver(dataMap,file);
		return resVo;
    }
	/**
	 * 
     * @api {POST} /api/drivers/_import   驾驶员导入
     * @apiName  importDrivers
     * @apiGroup DriverController
     * @apiVersion 2.0.0
     * @apiDescription 高辉    驾驶员批量导入
     * <br/>
     * @apiParam {MultipartFile}   file                      驾驶员模版
     * @apiParam {Integer}         userId                    用户id
     * <br/>
     * @apiSuccess {String}     errorCode                  错误码   0-导入成功  1-导入失败
     * @apiSuccess {String}     errorMsg                   消息说明
     * @apiSuccess {Object}     data                       分页数据封装
     * @apiSuccess {String[]}   data.list 		                                 数据状态
     * @apiSuccess {String}     data.list.driverName 	         驾驶员姓名
     * @apiSuccess {String}     data.list.sex	 	                     性别
     * @apiSuccess {String}     data.list.nationality      国籍
     * @apiSuccess {String}     data.list.mobilePhone      电话号码
     * @apiSuccess {String}     data.list.identityCard	        身份证号
     * @apiSuccess {String}     data.list.cardTime 		         初次领证日期
     * @apiSuccess {String}     data.list.models 		        准驾车型
     * @apiSuccess {String}     data.list.validTerm 	        有效期限
     * @apiSuccess {String}     data.list.orgName          所属企业
     * @apiSuccess {String}     data.list.licensePlate     常用车辆
     * @apiSuccess {String}     data.list.description      导入失败描述
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/_import",method=RequestMethod.POST)
	public ResultVo importDrivers(HttpServletRequest request,@RequestParam("file") MultipartFile file,@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile excelFile=multipartRequest.getFile("file");
		List<Map> list = driverService.importDrivers(excelFile,null);
		resultVo.setData(list);
		if(null==list){
			resultVo.setErrorCode(0);
		}else{
			resultVo.setErrorCode(1);
		}
		return resultVo;
	}
}

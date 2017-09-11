package com.clouyun.charge.modules.vehicle.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.modules.spring.web.BaseController;
import com.clouyun.charge.modules.vehicle.service.CarIncomeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/11.
 */
@RestController
@RequestMapping("/api/carIncome")
public class CarIncomeController extends BaseController {

    @Autowired
    CarIncomeService carIncomeService;


    /**
     * @api {GET} /api/carIncome/carType/_count     车辆总数与各类型车辆数统计
     * @apiName carTypeCount
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆总数与各类型车辆数统计
     * <br/>
     * @apiParam {String}           userId                        用户Id
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		 数据封装
     * @apiSuccess {String}         data.vehicleType                    车辆类型
     * @apiSuccess {Integer}         data.size                          个数
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
    {
    errorCode: 0,
    errorMsg: "操作成功!",
    total: 0,
    data: [
    {
    vehicleType: "纯电动轿车",
    size: 496
    },
    {
    vehicleType: "纯电动轿车22",
    size: 6
    }]
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000  参数缺失
     */
    @RequestMapping(value = "/carType/_count", method = RequestMethod.GET)
    public ResultVo carTypeCount(@RequestParam Map map) throws Exception {
        DataVo dataMap =  new DataVo(map);
        ResultVo resultVo = new ResultVo();
        if(dataMap.isNotBlank("userId")){
            List<DataVo> dataVo = carIncomeService.carTypeCount(dataMap);
            resultVo.setData(dataVo);
        }else {
            throw   new BizException(1700000,"用户Id");
        }
        return resultVo;
    }

    /**
     * @api {GET} /api/carIncome/carHourChart    车辆收入24小时图表
     * @apiName carHourChart
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆收入24小时图表
     * <br/>
     * @apiParam {String}           userId                                 用户Id
     * @apiParam    {Date}        dateTime                日期(默认查询今天)(yyyy-mm-dd)
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		数据封装
     * @apiSuccess {String}         data                    		数据封装
     * @apiSuccess {String}         data.amount                   收入
     * @apiSuccess {String}         data.chaPower                 电量
     * @apiSuccess {String}         data.hourTime                 小时
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
    {
    errorCode: 0,
    errorMsg: "操作成功!",
    total: 0,
    data: [
    {
    amount: 4.24,
    chaPower: 2.12,
    hourTime: "08"
    },
    {
    amount: 0.54,
    chaPower: 0.27,
    hourTime: "09"
    },
    {
    amount: 11.06,
    chaPower: 0.53,
    hourTime: "10"
    }
    ]
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失
     */
    @RequestMapping(value = "/carHourChart", method = RequestMethod.GET)
    public ResultVo carHourChart(@RequestParam Map map) throws Exception {
        DataVo dataMap =  new DataVo(map);
        ResultVo resultVo = new ResultVo();
        if(dataMap.isNotBlank("userId")){
            resultVo.setData(carIncomeService.carHourChart(dataMap));
        }else {
            throw   new BizException(1700000,"用户Id");
        }
        return resultVo;
    }

    /**
     * @api {GET} /api/carIncome/carChgPowerTop10    当月单车充电排行取前十
     * @apiName carChgPowerTop10
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   当月单车充电排行取前十
     *  <br/>
     * @apiParam {String}           userId                                 用户Id
     * @apiParam {int}              pageNum                           页码 默认为1
     * @apiParam {int}               pageSize  		                  页大小 默认为8
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		返回集
     * @apiSuccess {Object}         data.list                    		数据封装
     * @apiSuccess {String}         data.list.chaPower                   充电量
     * @apiSuccess {String}         data.list.size                       充电次数
     * @apiSuccess {String}         data.list.licensePlate                车牌号
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
    {
    errorCode: 0,
    errorMsg: "操作成功!",
    total: 0,
    data: [
    {
    licensePlate: "冀C28553",
    chgPower: 0.74,
    size: 4
    },
    {
    licensePlate: "8888",
    chgPower: 0.62,
    size: 2
    },
    {
    licensePlate: "蒙D35559",
    chgPower: 0.56,
    size: 1
    },
    {
    licensePlate: "川E88888",
    chgPower: 0.56,
    size: 3
    }
    ]
    }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失
     */
    @RequestMapping(value = "/carChgPowerTop10", method = RequestMethod.GET)
    public ResultVo carChgPowerTop10( @RequestParam Map map) throws Exception {
        DataVo dataMap =  new DataVo(map);
        ResultVo resultVo = new ResultVo();
        if(dataMap.isNotBlank("userId")){
            resultVo.setData(carIncomeService.carChgPowerTop10(dataMap));
        }else {
            throw   new BizException(1700000,"用户Id");
        }
        return resultVo;
    }

    /**
     * @api {GET} /api/carIncome/carChgPowerTop20    车辆在各站的充电数[充电中]（统计前20）
     * @apiName carChgPowerTop20
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆在各站的充电数[充电中]（统计前20）
     * <br/>
     * @apiParam {String}           userId                        用户Id
     * @apiParam {int}              pageNum                      页码 默认为1
     * @apiParam {int}               pageSize  		                  页大小 默认为8
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		结果集
     * @apiSuccess {Object}         data.list                   		数据封装
     * @apiSuccess {String}         data.list.stationId                 场站Id
     * @apiSuccess {String}         data.list.stationName                场站名称
     * @apiSuccess {String}         data.list.size                      车数
     * @apiSuccess {String}         data.list.vehicle                  车辆集合
     * @apiSuccess {String}         data.list.vehicle.chgTime           充电时间( 秒)
     * @apiSuccess {String}         data.list.vehicle.licensePlate     车牌
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
    {
    size: "1",
    stationName: "科陆大厦充电站",
    stationId: "64",
    vehicle: [
    {
    licensePlate: "",
    chgPower: "0.349",
    chgTime: "22375222"
    }
    ]
    },
    {
    size: "0",
    stationName: "国联人才公寓",
    stationId: "174",
    vehicle: [ ]
    }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失
     */
    @RequestMapping(value = "/carChgPowerTop20", method = RequestMethod.GET)
    public ResultVo carChgPowerTop20(@RequestParam Map map) throws Exception {
        DataVo dataMap =  new DataVo(map);
        ResultVo resultVo = new ResultVo();
        if(dataMap.isNotBlank("userId")){
            resultVo.setData(carIncomeService.carChgPowerTop20(dataMap));
        }else {
            throw   new BizException(1700000,"用户Id");
        }
        return resultVo;
    }


    /**
     * @api {GET} /api/carIncome/cars    车辆收入查询列表
     * @apiName carIncomeList
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆收入查询列表
     * @apiParam {Int}           userId                        用户Id
     * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Int}        [stationId]                              	所属场站Id
     * @apiParam {String}        [stationName]                              	所属场站名称 模糊查询
     * @apiParam {String}        [onNumber]                              	自编号
     * @apiParam {String}        [licensePlate]                             车牌号
     * @apiParam {Int}        [orgId]                                   企业Id
     * @apiParam {String}        [orgName]                                   企业名 模糊查询
     * @apiParam {String}        [line]                                   线路
     * @apiParam {Int}        [cdStationName]                             充电场站
     * @apiParam {Int}           pageNum                               页码
     * @apiParam {Int}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		数据封装
     * @apiSuccess {Integer}        data.total                    		      总记录数
     * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}         data.list.carId                        车辆ID
     * @apiSuccess {String}        data.list.endTime                      结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}         data.list.orgName                    企业名
     * @apiSuccess {String}         data.list.stationName                所属场站
     * @apiSuccess {String}        data.list.licensePlate                车牌号
     * @apiSuccess {String}         data.list.onNumber                  自编号
     * @apiSuccess {String}        data.list.brand                     车品牌
     * @apiSuccess {String}        data.list.line                     线路
     * @apiSuccess {String}        data.list.cdStationName           充电场站
     * @apiSuccess {String}        data.list.cdStationId          充电场站Id
     * @apiSuccess {String}        data.list.size                   充电次数
     * @apiSuccess {String}        data.list.chgPower             充电量
     * @apiSuccess {String}        data.list.amount               消费金额
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public ResultVo carIncomeList(@RequestParam Map map) throws Exception {
        DataVo vo  =  new DataVo(map);
        ResultVo resultVo = new ResultVo();
        if(vo.isNotBlank("userId")){
            PageInfo pageList=carIncomeService.carIncomeList(vo);
            resultVo.setData(pageList);
        }else {
            throw   new BizException(1700000,"用户Id");
        }
        return resultVo;
    }

    /**
     * @api {GET} /api/carIncome/cars/_count    车辆收入查询列表合计
     * @apiName carIncomeListCount
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆收入查询列表合计
     * @apiParam {Int}           userId                        用户Id
     * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Int}        [stationId]                              	所属场站Id
     * @apiParam {String}        [stationName]                              	所属场站名称 模糊查询
     * @apiParam {String}        [onNumber]                              	自编号
     * @apiParam {String}        [licensePlate]                             车牌号
     * @apiParam {Int}        [orgId]                                   企业Id
     * @apiParam {String}        [orgName]                                   企业名 模糊查询
     * @apiParam {String}        [line]                                   线路
     * @apiParam {Int}        [cdStationName]                             充电场站
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		数据封装
     * @apiSuccess {String}        data.amount               消费金额合计
     * @apiSuccess {String}        data.chgPower              充电量合计
     * @apiSuccess {String}        data.size                 充电次数合计
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/cars/_count", method = RequestMethod.GET)
    public ResultVo carIncomeListCount(@RequestParam Map map) throws Exception {
        DataVo vo  =  new DataVo(map);
        ResultVo resultVo = new ResultVo();
        if(vo.isNotBlank("userId")){
            DataVo dataVo=carIncomeService.getCarListTypeCount(vo);
            resultVo.setData(dataVo);
        }else {
            throw   new BizException(1700000,"用户Id");
        }
        return resultVo;
    }
    /**
     * @api {GET} /api/carIncome/cars/_export    车辆收入查询列表导出
     * @apiName carIncomeListExport
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆收入查询列表导出
     * @apiParam {Int}           userId                        用户Id
     * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Int}        [stationId]                              	所属场站Id
     * @apiParam {String}        [stationName]                              	所属场站名称 模糊查询
     * @apiParam {String}        [onNumber]                              	自编号
     * @apiParam {String}        [licensePlate]                             车牌号
     * @apiParam {Int}        [orgId]                                   企业Id
     * @apiParam {String}        [orgName]                                   企业名 模糊查询
     * @apiParam {String}        [line]                                   线路
     * @apiParam {Int}        [cdStationId]                             充电场站
     * @apiParam {Int}        [cdStationName]                             充电场站
     * @apiParam {Int}           pageNum                               页码
     * @apiParam {Int}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {Integer} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * <br/>
     * @apiError -999 系 统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/cars/_export", method = RequestMethod.GET)
    public void carIncomeListExport(@RequestParam Map map,HttpServletResponse response) throws Exception {
        DataVo dataMap =  new DataVo(map);
        if(dataMap.isNotBlank("userId")){
            carIncomeService.carIncomeListExport(dataMap,response);
        }else {
            throw   new BizException(1700000,"用户Id");
        }

    }

    /**
     * @api {GET} /api/carIncome/carDetail    车辆收入详情列表
     * @apiName carIncomeDetail
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    车辆收入详情列表
     * @apiParam {Int}             carId                                 车辆Id
     * @apiParam {Date}             startDate                              开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}             endDate                                结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}             stationId                                充电场站id
     * @apiParam {Int}           pageNum                               页码
     * @apiParam {Int}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		数据封装
     * @apiSuccess {Object}         data                    		      分页数据封装
     * @apiSuccess {Integer}        data.total                    		      总记录数
     * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}         data.list.startTime                   开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}         data.list.endTime                      结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}         data.list.carId                        车辆id
     * @apiSuccess {String}         data.list.billPayNo                    订单号
     * @apiSuccess {String}         data.list.orgName                      企业名称
     * @apiSuccess {String}         data.list.stationName                所属场站
     * @apiSuccess {String}        data.list.licensePlate                车牌号
     * @apiSuccess {String}         data.list.onNumber                  自编号
     * @apiSuccess {String}        data.list.brand                     车品牌
     * @apiSuccess {String}        data.list.vehicleType                车辆类型
     * @apiSuccess {String}        data.list.line                     线路
     * @apiSuccess {String}        data.list.price                     单价
     * @apiSuccess {String}        data.list.chgPower                 充电量
     * @apiSuccess {String}        data.list.amount                 消费金额
     * @apiSuccess {String}        data.list.cdStationName           充电场站
     * @apiSuccess {String}        data.list.pileNo                  充电桩编号
     * @apiSuccess {String}        data.list.useTime                  充电时长
     * @apiSuccess {String}        data.list.amount               消费金额
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/carDetail", method = RequestMethod.GET)
    public ResultVo carIncomeDetail(@RequestParam Map map) throws Exception {
        ResultVo resultVo = new ResultVo();
        DataVo data = new DataVo(map);
        if(data.isNotBlank("carId")) {
            PageInfo dataVo = carIncomeService.carIncomeDetail(data);
            resultVo.setData(dataVo);
        }else {
            throw   new BizException(1700000,"车Id");
        }

        return resultVo;
    }

    /**
     * @api {GET} /api/carIncome/carDetail/_export    车辆收入详情列表导出
     * @apiName carIncomeDetailExport
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯    车辆收入详情列表导出
     * @apiParam {Int}             carId                                 车辆Id
     * @apiParam {Date}             startDate                              开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}             endDate                                结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Int}           pageNum                               页码
     * @apiParam {Int}           pageSize                              页大小
     * <br/>
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
    @RequestMapping(value = "/carDetail/_export", method = RequestMethod.GET)
    public void carIncomeDetailExport(@RequestParam Map map,HttpServletResponse response) throws Exception {
        DataVo data = new DataVo(map);
        if(data.isNotBlank("carId")) {
            carIncomeService.carIncomeDetailExport(data,response);
        }else {
            throw   new BizException(1700000,"车Id");
        }

    }

    /**
     * @api {GET} /api/carIncome/carsCountDetailList    车辆收入详情统计汇总列表
     * @apiName carsCountDetailList
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆收入详情统计汇总列表
     * @apiParam {Int}           userId                        用户Id
     * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Int}        [stationId]                              	所属场站Id
     * @apiParam {String}        [stationName]                              	所属场站名称 模糊查询
     * @apiParam {String}        [onNumber]                              	自编号
     * @apiParam {String}        [licensePlate]                             车牌号
     * @apiParam {Int}        [orgId]                                   企业Id
     * @apiParam {Int}        [orgName]                                   企业名称 模糊查询
     * @apiParam {String}        [line]                                   线路
     * @apiParam {Int}        [cdStationId]                             充电Id
     * @apiParam {Int}        [cdStationName]                             充电场站 模糊查询
     * @apiParam {Integer}           pageNum                               页码
     * @apiParam {Integer}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		数据封装
     * @apiSuccess {Object}         data                    		      分页数据封装
     * @apiSuccess {Integer}        data.total                    		      总记录数
     * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}         data.list.startTime                   开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}       data.list.endTime                      结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}       data.list.pillNo                     订单编号
     * @apiSuccess {String}         data.list.orgName                    企业名
     * @apiSuccess {String}         data.list.stationName                所属场站
     * @apiSuccess {String}        data.list.licensePlate                车牌号
     * @apiSuccess {String}        data.list.vehicleType                车辆类型
     * @apiSuccess {String}         data.list.onNumber                  自编号
     * @apiSuccess {String}        data.list.line                     线路
     * @apiSuccess {String}        data.list.price                     单价
     * @apiSuccess {String}        data.list.useTime                     充电时长
     * @apiSuccess {String}        data.list.amount                   消费金额
     * @apiSuccess {String}        data.list.cdStationName           充电场站
     * @apiSuccess {String}        data.list.pileNo           充电编号

     * <br/>
     * @apiSuccessExample {json} Success出参示例:{{
    errorCode: 0,
    errorMsg: "操作成功!",
    total: 0,
    data: {
    pageNum: 1,
    pageSize: 1,
    size: 1,
    startRow: 1,
    endRow: 1,
    total: 1470,
    pages: 1470,
    list: [
    {
    billPayNo: "CARD20161220171224274",
    line: "123",
    orgName: "",
    endTime: "2016-12-20 16:23:14",
    licensePlate: "123",
    startTime: "2016-12-20 16:21:14",
    amount: 0.1,
    pileNo: "",
    price: 0,
    vehicleType: "",
    stationName: "",
    onNumber: "123",
    useTime: "0小时2分0秒",
    cdStationName: "",
    chgPower: 0.1
    }
    ],
    prePage: 0,
    nextPage: 2,
    isFirstPage: true,
    isLastPage: false,
    hasPreviousPage: false,
    hasNextPage: true,
    navigatePages: 8,
    navigatepageNums: [
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
    ],
    navigateFirstPage: 1,
    navigateLastPage: 8,
    firstPage: 1,
    lastPage: 8
    }
    }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/carsCountDetailList", method = RequestMethod.GET)
    public ResultVo carsDetailCount(@RequestParam Map map) throws Exception {
        DataVo vo  =  new DataVo(map);
        ResultVo resultVo = new ResultVo();
        if(vo.isNotBlank("userId")){
            PageInfo pageList=carIncomeService.carsCountDetailList(vo);
            resultVo.setData(pageList);
        }else {
            throw   new BizException(1700000,"用户Id");
        }
        return resultVo;
    }

    /**
     * @api {GET} /api/carIncome/carsCountDetailList/_export   车辆收入详情统计汇总列表导出
     * @apiName carsCountDetailListExprot
     * @apiGroup CarIncomeController
     * @apiVersion 2.0.0
     * @apiDescription 易凯     车辆收入详情统计汇总列表导出
     * @apiParam {Int}           userId                        用户Id
     * @apiParam {Date}        [startDate]                              开始时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Date}        [endDate]                                结束时间(yyyy-mm-dd HH:mm:ss)
     * @apiParam {Int}        [stationId]                              	所属场站Id
     * @apiParam {String}        [stationName]                              	所属场站名称 模糊查询
     * @apiParam {String}        [onNumber]                              	自编号
     * @apiParam {String}        [licensePlate]                             车牌号
     * @apiParam {Int}        [orgId]                                   企业Id
     * @apiParam {Int}        [orgName]                                   企业名称 模糊查询
     * @apiParam {String}        [line]                                   线路
     * @apiParam {Int}        [cdStationId]                             充电Id
     * @apiParam {Int}        [cdStationName]                             充电场站 模糊查询
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
     * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/carsCountDetailList/_export", method = RequestMethod.GET)
    public void carsCountDetailListExprot(@RequestParam Map map,HttpServletResponse response) throws Exception {
        DataVo dataMap =  new DataVo(map);
        if(dataMap.isNotBlank("userId")){
            carIncomeService.carsCountDetailListExprot(dataMap,response);
        }else {
            throw   new BizException(1700000,"用户Id");
        }

    }
    /**
     * @api {GET} /api/carIncome/stations    车辆所属场站
     * @apiName carsStation
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription 易凯   车辆所属场站
     * @apiParam {Integer}           pageNum                               页码 （默认1）
     * @apiParam {Integer}           pageSize                              页大小（默认50）
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
     * @apiSuccess {Object}         data                    		数据封装
     * @apiSuccess {Object}         data                    		      分页数据封装
     * @apiSuccess {Integer}        data.total                    		      总记录数
     * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}         data.list.stationName                   场站名称
     * @apiSuccess {String}         data.list.stationId                   场站id
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1700000 参数缺失!
     */
    @RequestMapping(value = "/stations", method = RequestMethod.GET)
    public ResultVo carsStation(@RequestParam Map map) throws Exception {
        ResultVo resultVo = new ResultVo();
            PageInfo pageList=carIncomeService.carsStation(new DataVo(map));
            resultVo.setData(pageList);
        return resultVo;
    }
}

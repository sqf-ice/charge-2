
package com.clouyun.charge.modules.monitor.web;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.service.WarningService;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0.0
 * 创建日期: 2017年4月20日
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/warnings")
public class WarningController {
	
	@Autowired
    private WarningService warningService;
	
	/**
     * @api {PUT} /api/warnings   告警手动处理
     * @apiName  manualWarning
     * @apiGroup WarningController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   告警手动处理
     * <br/>
	 * @apiParam {Integer}   data.recId                       告警id      
	 * @apiParam {Integer}   data.userId                      处理人Id
	 * @apiParam {String}    data.handleDesc                  处理描述
	 * @apiParam {List}      [data.artifactArrs]              物料信息 
	 * @apiParam {Integer}   data.artifactArrs.matId          物料id
	 * @apiParam {Integer}   data.artifactArrs.materialQuant  物料数量
	 * @apiParam {Double}    data.artifactArrs.materialTotal  物料总价
     * <br/>
     * @apiSuccess {String}    errorCode         错误码
	 * @apiSuccess {String}    errorMsg          消息说明 
	 * <br/>
     * @apiError -999 系统异常!
     */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResultVo manualWarning(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		warningService.manualWarning(map);
		return resultVo;
	}
	/**
     * @api {GET} /api/warnings/{recId} 查询告警详情
     * @apiName  getWarning
     * @apiGroup WarningController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   根据告警Id查询告警详情 
     * <br/>
     * @apiParam   {Integer}   recId               告警Id
     * <br/>
     * @apiSuccess {String}    errorCode           错误码
	 * @apiSuccess {String}    errorMsg            消息说明
	 * @apiSuccess {Object}    data                数据封装
	 * @apiSuccess {Integer}   data.id             告警id
	 * @apiSuccess {String}    data.dataTime       告警时间(yyyy-MM-dd HH:mm:ss)
	 * @apiSuccess {String}    data.orgName        运营商
	 * @apiSuccess {String}    data.stationName    场站名
	 * @apiSuccess {String}    data.pileNo         设备编号
	 * @apiSuccess {String}    data.pileName       设备名称     
	 * @apiSuccess {Integer}   data.pileType       设备类型
	 * @apiSuccess {Integer}   data.pileModelId    设备型号
	 * @apiSuccess {Integer}   data.alrCount       告警次数
	 * @apiSuccess {String}    data.alrDesc        告警详情
	 * @apiSuccess {Integer}   data.objType        告警对象
	 * @apiSuccess {Integer}   data.status         设备状态
	 * @apiSuccess {String}    data.handleTime     处理时间 (yyyy-MM-dd HH:mm:ss)
	 * @apiSuccess {String}    data.handleDesc     处理描述
	 * @apiSuccess {Integer}   data.handleMethod   处理人类型 
	 * @apiSuccess {Integer}   data.handleFlag     处理状态
	 * @apiSuccess {String}    data.userName       处理人名字   
	 * <br/>
     * @apiError -999 系统异常!
     */
	@RequestMapping(value = "/{recId}", method = RequestMethod.GET)
    public ResultVo getWarning(@PathVariable Integer recId) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(warningService.getWarning(recId));
		return resultVo;
    }
	/**
	 * @api {GET} /api/warnings   分页查询告警列表
	 * @apiName  getWarningsPage
	 * @apiGroup WarningController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 根据相应条件分页查询告警列表
	 * <br/>
	 * @apiParam   {Integer}   userId                   用户id
	 * @apiParam   {String}    [startTime]              开始时间(yyyy-MM-dd)
	 * @apiParam   {String}    [endTime]                结束时间(yyyy-MM-dd)
	 * @apiParam   {Integer}   [orgId]                  运营商id           
	 * @apiParam   {Integer}   [stationId]              场站id            
	 * @apiParam   {String}    [pileName]               设备名称                         
	 * @apiParam   {Integer}   [handleFlag]             处理状态                         
	 * @apiParam   {Integer}   [pileType]               设备类型
	 * @apiParam   {Integer}   [pileModelId]            设备型号
	 * @apiParam   {Integer}   [objType]                告警对象
	 * @apiParam   {Integer}   [alrNo]                  告警项
	 * @apiParam   {int}       pageNum                  页码
	 * @apiParam   {int}       pageSize                 页大小
	 * @apiParam   {String}    [sort]                   排序字段
	 * @apiParam   {String}    [order]                  排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     分页数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {Integer}   data.list.recId          告警id
	 * @apiSuccess {String}    data.list.dataTime       告警时间(yyyy-MM-dd HH:mm:ss)
	 * @apiSuccess {Integer}   data.list.orgId          运营商id
	 * @apiSuccess {String}    data.list.orgName        运营商
	 * @apiSuccess {Integer}   data.list.stationId      场站id
	 * @apiSuccess {String}    data.list.stationName    场站名
	 * @apiSuccess {String}    data.list.pileNo         设备编号
	 * @apiSuccess {String}    data.list.pileName       设备名称     
	 * @apiSuccess {String}    data.list.pileType       设备类型(type=cdzlx)
	 * @apiSuccess {Integer}   data.list.pileModelId    设备型号(type=sbxh)
	 * @apiSuccess {Integer}   data.list.alrCount       告警次数
	 * @apiSuccess {String}    data.list.alrDesc        告警详情
	 * @apiSuccess {Integer}   data.list.objType        告警对象(type=dxlx)
	 * @apiSuccess {Integer}   data.list.status         设备状态 (type=sbzt)
	 * @apiSuccess {Integer}   data.list.alrNo          告警项(type=gjx)
	 * @apiSuccess {Date}      data.list.handleTime     处理时间(yyyy-MM-dd HH:mm:ss) 
	 * @apiSuccess {String}    data.list.handleDesc     处理描述
	 * @apiSuccess {Integer}   data.list.handleFlag     处理状态  0- 待处理
	 * @apiSuccess {String}    data.list.userName       处理人名字  
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
    public ResultVo getWarningsPage(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = warningService.getWarnings(map);
		resultVo.setData(pageInfo);
		return resultVo;
    }
	/**
	 * @api {PUT} /api/warnings/_dispatchAgain   告警再次派单操作
	 * @apiName  dispatchAgain
	 * @apiGroup WarningController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉            告警再次派单操作
	 * <br/>
	 * @apiParam   {Integer}   recId                           告警id
	 * <br/>
	 * @apiSuccess {String}    errorCode                       错误码
	 * @apiSuccess {String}    errorMsg                        消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/_dispatchAgain", method = RequestMethod.PUT)
    public ResultVo dispatchAgain(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		warningService.dispatchAgain(map);
		return resultVo;
    }
	/**
	 * @api {PUT} /api/warnings/_dispatch    告警派单操作
	 * @apiName  dispatch
	 * @apiGroup WarningController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             告警派单操作
	 * <br/>
	 * @apiParam   {Integer}   recId          告警id
	 * @apiParam   {Integer}   orgId          运营商id
	 * @apiParam   {Integer}   wqConsId       外勤人员id(处理人id)
	 * @apiParam   {Date}      taskStartHour  任务开始时间(yyyy-MM-dd HH:mm:ss)
	 * @apiParam   {Date}      taskEndHour    任务结束时间(yyyy-MM-dd HH:mm:ss) 
	 * @apiParam   {String}    taskLevel      任务级别(type=gjjb)
	 * @apiParam   {Integer}   patternId      任务模板id
	 * @apiParam   {Integer}   stationId      场站id
	 * @apiParam   {Integer}   pileId         设备id
	 * <br/>
	 * @apiSuccess {String}    errorCode      错误码
	 * @apiSuccess {String}    errorMsg       消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1803000 入参空判断异常!
	 */
	@RequestMapping(value = "/_dispatch", method = RequestMethod.PUT)
    public ResultVo dispatch(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		warningService.dispatch(map);
		return resultVo;
    }
	/**
	 * @api {PUT} /api/warnings/_handleConfirm   告警确认处理
	 * @apiName  handleConfirm
	 * @apiGroup WarningController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉           告警确认处理
	 * <br/>
	 * @apiParam   {Integer}   recId             告警id
	 * <br/>
	 * @apiSuccess {String}    errorCode         错误码
	 * @apiSuccess {String}    errorMsg          消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/_handleConfirm", method = RequestMethod.PUT)
    public ResultVo handleConfirm(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		warningService.handleConfirm(map);
		return resultVo;
    }
	/**
	 * @api {GET} /api/warnings/alarmItems    告警项查询
	 * @apiName  getAlarmItems
	 * @apiGroup WarningController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉           告警项查询
	 * <br/>
	 * @apiSuccess {String}    errorCode           错误码
	 * @apiSuccess {String}    errorMsg            消息说明
	 * @apiSuccess {Object[]}  data                数据对象集合
	 * @apiSuccess {Integer}   data.id             id
	 * @apiSuccess {String}    data.value          数据值
	 * @apiSuccess {String}    data.lable          标签名
	 * @apiSuccess {String}    data.handle_way     处理方式
	 * @apiSuccessExample {json} Success出参示例:
	 * {
     *	"errorCode": 0,
     *	"errorMsg": "操作成功!",
     *	"data":[
     *           {
     *            "id": 346,
     *            "value": "0102",
     *            "handleWay": "1",
     *            "label": "维修门开"
     *           },
     *           {
     *            "id": 347,
     *            "value": "0103",
     *            "handleWay": "0",
     *            "label": "充电桩状态改变"
     *           }
     *        ]
     * }
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/alarmItems", method = RequestMethod.GET)
    public ResultVo getAlarmItems() throws BizException{
		ResultVo resultVo = new ResultVo();
		List<Map> itemList = warningService.getAlarmItems();
		resultVo.setData(itemList);
		return resultVo;
    }
	/**
	 * @api {PUT} /api/warnings/alarmItems    告警项处理方式设置
	 * @apiName  modifyAlarmItems
	 * @apiGroup WarningController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉           告警项处理方式设置
	 * <br/>
	 * @apiParam {Object[]}  items               对象集合
	 * @apiParam {Integer}   data.id             id
	 * @apiParam {String}    data.handle_way     处理方式   0-手动处理 、1-自动处理
	 * @apiParamExample {json} 入参示例:
     *{
     * "items": [
     *   {
     *       "id": 344,
     *       "handleWay": "1"
     *   },
     *   {
     *       "id": 345,
     *       "handleWay": "1"
     *    },
     *   {
     *       "id": 346,
     *       "handleWay": "1"
     *   }
     *  ]
     * }
	 * <br/>
	 * @apiSuccess {String}    errorCode           错误码
	 * @apiSuccess {String}    errorMsg            消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/alarmItems", method = RequestMethod.PUT)
    public ResultVo modifyAlarmItems(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		warningService.modifyAlarmItems(map);
		return resultVo;
    }
	/**
	 * @api {GET} /api/warnings/_export      导出告警列表
	 * @apiName  exportAlarms
	 * @apiGroup WarningController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             导出告警列表
	 * <br/>
	 * @apiParam   {Integer}   userId                   用户id
	 * @apiParam   {String}    [startTime]              开始时间(yyyy-MM-dd)
	 * @apiParam   {String}    [endTime]                结束时间(yyyy-MM-dd)
	 * @apiParam   {Integer}   [orgId]                  运营商id           
	 * @apiParam   {Integer}   [stationId]              场站id            
	 * @apiParam   {String}    [pileName]               设备名称                         
	 * @apiParam   {Integer}   [handleFlag]             处理状态                         
	 * @apiParam   {Integer}   [pileType]               设备类型
	 * @apiParam   {Integer}   [pileModelId]            设备型号
	 * @apiParam   {Integer}   [objType]                告警对象
	 * @apiParam   {Integer}   [alrNo]                  告警项
	 * @apiParam   {int}       pageNum                  页码
	 * @apiParam   {int}       pageSize                 页大小
	 * @apiParam   {String}    [sort]                   排序字段
	 * @apiParam   {String}    [order]                  排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiError -999 系统异常！
	 * @apiError -1803000 参数为空异常！
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
    public void exportAlarms(@RequestParam Map map,HttpServletResponse response) throws Exception{
		warningService.exportAlarms(map,response);
    }
	
	
	/**
	 * 根据警告的ID根据警告信息
	 * 2017年3月25日
	 * gaohui
	 * @throws BizException 
	 */
	@RequestMapping(value = "/updateByRecId", method = RequestMethod.POST)
	void updateWarningByRecId(@RequestBody DataVo vo) throws BizException{
		warningService.updateWarningByRecId(vo);
	}
	
	/**
	 * 根据recId查询告警信息
	 * @param map
	 * @return
	 * 2017年4月1日
	 * gaohui
	 * @throws BizException 
	 */
	@RequestMapping(value = "/findByRecId", method = RequestMethod.POST)
    public ResultVo findWarningByRecId(@RequestBody DataVo vo) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(warningService.findWarningByRecId(vo));
		return resultVo;
    }
	/**
	 * 查询告警列表
	 * @param vo
	 * @return
	 * @throws BizException
	 * 2017年4月11日
	 * gaohui
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.POST)
    public ResultVo findWarningList(@RequestBody DataVo vo) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(warningService.findWarningByRecId(vo));
		return resultVo;
    }
	/**
	 * 分页查询告警列表
	 * @param vo
	 * @return
	 * @throws BizException
	 * 2017年4月11日
	 * gaohui
	 */
	@RequestMapping(value = "/findListPage", method = RequestMethod.POST)
    public ResultVo findWarningListByPage(@RequestBody DataVo vo) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(warningService.findWarningListByPage(vo));
		return resultVo;
    }
	/**
	 * 查询告警条数
	 * @param vo
	 * @return
	 * @throws BizException
	 * 2017年4月11日
	 * gaohui
	 */
	@RequestMapping(value = "/findCounts", method = RequestMethod.POST)
    public ResultVo findWarningCounts(@RequestBody DataVo vo) throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(warningService.findWarningCounts(vo));
		return resultVo;
    }

}

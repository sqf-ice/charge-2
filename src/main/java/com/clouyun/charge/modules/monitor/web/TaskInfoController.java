
/**
 * 
 */
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
import com.clouyun.charge.modules.monitor.service.TaskInfoService;
import com.github.pagehelper.PageInfo;


/**
 * 描述: TaskInfoController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年3月11日
 */
@RestController
@SuppressWarnings("rawtypes")
public class TaskInfoController {
	@Autowired
	private TaskInfoService taskInfoService;
	/**
	 * @api {POST} /api/taskInfos    新增任务
	 * @apiName   addTaskInfo
	 * @apiGroup  TaskInfoController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 新增任务(任务管理新增)
	 * <br/>
	 * @apiParam   {Integer}  wqConsId       外勤人员id(处理人id)
	 * @apiParam   {Integer}  pileId         设备id(桩)
	 * @apiParam   {Date}     taskStartHour  任务开始时间(yyyy-MM-dd HH:mm:ss)
	 * @apiParam   {Date}     taskEndHour    任务结束时间(yyyy-MM-dd HH:mm:ss)
	 * @apiParam   {String}   taskLevel      任务级别(parentId=65)
	 * @apiParam   {Integer}  templateId     任务模板id
	 * @apiParam   {Integer}  stationId      场站id 
	 * @apiParam   {Integer}  orgId          运营商id 
	 * @apiParam   {String}   taskName       任务名称
	 * @apiParam   {String}   taskDesc       任务描述 (length>255)
	 * @apiParam   {String}   taskFreq       任务频率(派单任务0,作业任务...)
	 * @apiParam   {String}   [weeks]        星期数("星期一,星期二")
	 * @apiParam   {String}   [months]       月天数("1,2")
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/api/taskInfos", method = RequestMethod.POST)
	public ResultVo addTaskInfo(@RequestBody Map map) throws BizException{
		ResultVo resultVo  = new ResultVo();
		taskInfoService.addTaskInfo(map);
		return resultVo;
	}
	
	/**
	 * @api {GET} /api/taskInfos   分页查询作业任务列表
	 * @apiName   getTaskInfosPage
	 * @apiGroup  TaskInfoController 
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 分页查询作业任务列表
	 * <br/>
	 * @apiParam   {Integer}   userId                     用户id 
	 * @apiParam   {Integer}   [orgId]                    运营商id           
	 * @apiParam   {Integer}   [stationId]                场站id     
	 * @apiParam   {Integer}   [wqConsId]                 外勤人员id
	 * @apiParam   {Integer}   [templateId]               任务模板id
	 * @apiParam   {Integer}   [taskType]                 任务类型0:告警任务 1:作业任务(parentId=64)
	 * @apiParam   {Integer}   [taskStatus]               任务状态
	 * @apiParam   {String}    [pileName]                 设备名称
	 * @apiParam   {Integer}   handleStatus               处理状态 0:待处理
	 * @apiParam   {Integer}   pageNum                    页码
	 * @apiParam   {Integer}   pageSize                   页大小
	 * @apiParam   {String}    [sort]                     排序字段
	 * @apiParam   {String}    [order]                    排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     分页数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {Integer}   data.list.taskId         任务Id
     * @apiSuccess {Integer}   data.list.taskName       任务名称
     * @apiSuccess {Integer}   data.list.taskStatus     任务状态(parentId=65)
     * @apiSuccess {Date}      data.list.taskDate       任务日期(yyyy-MM-dd)
     * @apiSuccess {String}    data.list.templateName   任务模板名称
     * @apiSuccess {String}    data.list.wqConsName     外勤人员名称
     * @apiSuccess {Integer}   data.list.taskOvertime   任务是否超时处理 0:未超时 1:已超时
     * @apiSuccess {String}    data.list.taskLevel      任务级别(parentId=65)
     * @apiSuccess {Integer}   data.list.taskType       任务类型 0:告警任务 1:作业任务(parentId=64)
     * @apiSuccess {String}    data.list.stationName    所属场站名称
     * @apiSuccess {Date}      data.list.taskDealtTime  处理时间
     * @apiSuccess {String}    data.list.handleStatus   处理状态 
     * @apiSuccess {String}    data.list.pileName       设备名称
     * @apiSuccess {String}    data.list.pileNo         设备编号
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/api/taskInfos" ,method = RequestMethod.GET)
	public ResultVo getTaskInfosPage(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = taskInfoService.getTaskInfosPage(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
	/**
	 * @api {GET} /api/taskInfos/{taskId}   查询作业任务详情
	 * @apiName   getTaskInfos
	 * @apiGroup  TaskInfoController 
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 查询作业任务详情
	 * <br/>
	 * @apiParam   {Integer}   taskId                            任务id          
	 * <br/>
	 * @apiSuccess {String}    errorCode                         错误码
	 * @apiSuccess {String}    errorMsg                          消息说明
	 * @apiSuccess {Object}    data                              数据封装
     * @apiSuccess {Integer}   data.taskId                  任务Id
     * @apiSuccess {Integer}   data.taskName                任务名称
     * @apiSuccess {Integer}   data.taskStatus              任务状态(parentId=65)
     * @apiSuccess {Date}      data.taskDate                任务日期(yyyy-MM-dd)
     * @apiSuccess {String}    data.patternId               任务模板id
     * @apiSuccess {String}    data.templateName            任务模板名称
     * @apiSuccess {String}    data.wqConsName              外勤人员名称
     * @apiSuccess {Integer}   data.taskOvertime            任务是否超时处理 0:未超时 1:已超时
     * @apiSuccess {String}    data.taskLevel               任务级别(parentId=65)
     * @apiSuccess {Integer}   data.taskType                任务类型 0:告警任务 1:作业任务(parentId=64)
     * @apiSuccess {String}    data.stationName             所属场站名称
     * @apiSuccess {String}    data.taskTime                任务时间
     * @apiSuccess {String}    data.taskRejReason           退回原因
     * @apiSuccess {String}    data.taskOvertime            是否超时
     * @apiSuccess {String}    data.taskAgain               再次派单标识   1 为再次派单
     * @apiSuccess {String}    data.taskAgainTime           再次派单时间
     * @apiSuccess {object[]}  data.option                  选项
     * @apiSuccess {Integer}   data.option.optionId         选项id
     * @apiSuccess {String}    data.option.optionName       选项名称
     * @apiSuccess {String}    data.option.optionResources  选项资源
     * @apiSuccess {Integer}   data.option.optionType       是否为必填类型 
     * @apiSuccess {object[]}  data.option.reslist          可能为文本数据、照片、位置信息或物料信息
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	
	@RequestMapping(value = "/api/taskInfos/{taskId}" ,method = RequestMethod.GET)
	public ResultVo getTaskInfos(@PathVariable Integer taskId) throws BizException{
		ResultVo resultVo = new ResultVo();
		Map map= taskInfoService.getTaskInfos(taskId);
		resultVo.setData(map);
		return resultVo;
	}
	/**
	 * @api {PUT} /api/taskInfos/_dispatchAgain   任务再次派单操作
	 * @apiName  dispatchAgain
	 * @apiGroup TaskInfoController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             任务再次派单操作
	 * <br/>
	 * @apiParam   {Integer}   taskId                          任务id
	 * <br/>
	 * @apiSuccess {String}    errorCode                       错误码
	 * @apiSuccess {String}    errorMsg                        消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/api/taskInfos/_dispatchAgain", method = RequestMethod.PUT)
	public ResultVo dispatchAgain(@RequestBody Map map) throws BizException{
	    taskInfoService.dispatchAgain(map);
		return new ResultVo();
	}
	
	/**
	 * @api {PUT} /api/taskInfos/_handleConfirm   任务确认处理
	 * @apiName  handleConfirm
	 * @apiGroup TaskInfoController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             任务确认处理
	 * <br/>
	 * @apiParam   {Integer}   taskId                          任务id
	 * <br/>
	 * @apiSuccess {String}    errorCode                       错误码
	 * @apiSuccess {String}    errorMsg                        消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value="/api/taskInfos/_handleConfirm",method=RequestMethod.PUT)
	public ResultVo handleConfirm(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		taskInfoService.handleConfirm(map);
		return resultVo;
	}
	
	/**
	 * @api {GET} /api/taskInfos/dispatchRank   场站派单排行
	 * @apiName  getStaDispatchRank
	 * @apiGroup TaskInfoController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             任务紧急程度排行
	 * <br/>
	 * @apiParam   {Integer}   userId           用户id
	 * @apiParam   {Date}      [taskDateStart]  任务开始日期(yyyy-MM-dd)
	 * @apiParam   {Date}      [taskDateEnd]    任务结束日期(yyyy-MM-dd)
	 * @apiParam   {Integer}   [orgId]          运营商id
	 * @apiParam   {Integer}   [taskLevel]      任务级别
	 * <br/>
	 * @apiSuccess {String}    errorCode              错误码
	 * @apiSuccess {String}    errorMsg               消息说明
	 * @apiSuccess {Object}    data                   数据对象
	 * @apiSuccess {Object[]}  data.list              数据集合
	 * @apiSuccess {Integer}   data.list.stationId    场站id
	 * @apiSuccess {Integer}   data.list.countTask    任务数量
	 * @apiSuccess {String}    data.list.stationName  场站名称
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value = "/api/taskInfos/dispatchRank",method = RequestMethod.GET)
	public ResultVo getDispatchRank(@RequestParam Map map)throws BizException{
		ResultVo resultVo = new ResultVo();
		List list = taskInfoService.getDispatchRank(map);
		resultVo.setData(list);
		resultVo.setTotal(list.size());
		return  resultVo;
	}
	/**
	 * @api {GET} /api/taskInfos/finishedRateRank   外勤用户任务完成率排行
	 * @apiName  getlevelRanking
	 * @apiGroup TaskInfoController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             外勤用户任务完成率排行
	 * <br/>
	 * @apiParam   {Integer}   userId                用户id
	 * @apiParam   {Date}      [taskDateStart]       任务开始日期(yyyy-MM-dd)
	 * @apiParam   {Date}      [taskDateEnd]         任务结束日期(yyyy-MM-dd)
	 * @apiParam   {Integer}   [orgId]               运营商id
	 * <br/>
	 * @apiSuccess {String}    errorCode              错误码
	 * @apiSuccess {String}    errorMsg               消息说明
	 * @apiSuccess {Object}    data                   数据对象
	 * @apiSuccess {Object[]}  data.list              数据集合
	 * @apiSuccess {Integer}   data.list.wqConsId     外勤人员id
	 * @apiSuccess {Integer}   data.list.countTask    任务数量
	 * @apiSuccess {Double}    data.list.finishedRate 任务完成率
	 * @apiSuccess {String}    data.list.wqConsName   外勤人员名称
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value="/api/taskInfos/finishedRateRank",method=RequestMethod.GET)
	public ResultVo finishedRateRank(@RequestParam Map map)throws BizException{
		ResultVo resultVo =new  ResultVo();
		resultVo.setData(taskInfoService.finishedRateRank(map));
		return resultVo;
	}
	/**
	 * @api {GET} /api/taskInfos/levelRank   任务紧急程度排行
	 * @apiName  getlevelRank
	 * @apiGroup TaskInfoController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             任务紧急程度排行
	 * <br/>
	 * @apiParam   {Integer}   userId              用户id
	 * @apiParam   {Date}      [taskDateStart]     任务开始日期(yyyy-MM-dd)
	 * @apiParam   {Date}      [taskDateEnd]       任务结束日期(yyyy-MM-dd)
	 * @apiParam   {Integer}   [orgId]             运营商id
	 * @apiParam   {Integer}   [taskLevel]         任务级别
	 * <br/>
	 * @apiSuccess {String}    errorCode                 错误码
	 * @apiSuccess {String}    errorMsg                  消息说明
	 * @apiSuccess {Object}    data                      数据对象
	 * @apiSuccess {Object[]}  data.list                 数据集合
	 * @apiSuccess {Integer}   data.list.wqConsId        外勤人员id
	 * @apiSuccess {Integer}   data.list.countTask       外勤用户任务数
	 * @apiSuccess {Integer}   data.list.totalCount      任务总数量
	 * @apiSuccess {Double}    data.list.finishedRate    任务完成率(总任务数量)
	 * @apiSuccess {String}    data.list.wqConsName      外勤人员名称
	 * @apiSuccess {String}    data.list.taskCountRate   任务完成率
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value="/api/taskInfos/levelRank",method=RequestMethod.GET)
	public ResultVo getlevelRank(@RequestParam Map map)throws BizException{
		ResultVo resultVo =new  ResultVo();
		List<Map> list = taskInfoService.getlevelRank(map);
		resultVo.setData(list);
		resultVo.setTotal(list.size());
		return resultVo;
	}
	/**
	 * @api {GET} /api/taskInfos/_pieCounts     任务统计之饼图统计数据
	 * @apiName   getGraphData
	 * @apiGroup  TaskInfoController 
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             总任务、任务时效、任务紧急程度统计         
	 * <br/>
	 * @apiParam   {Integer}   userId                            用户id
	 * @apiParam   {Date}      taskTimeStart                     任务开始时间(yyyy-MM-dd 00:00:00)
	 * @apiParam   {Date}      taskTimeEnd                       任务结束时间 (yyyy-MM-dd HH:mm:ss)
	 * @apiParam   {Integer}   [orgId]                           运营商id            
	 * <br/>
	 * @apiSuccess {String}    errorCode                         错误码
	 * @apiSuccess {String}    errorMsg                          消息说明
	 * @apiSuccess {Object}    data                              分页数据封装
	 * @apiSuccess {Integer}   data.taskTotal                    任务总数
     * @apiSuccess {Integer}   data.dealCount                    已处理任务总数
     * @apiSuccess {Object[]}  data.totaldealList                总任务统计
     * @apiSuccess {String}    data.totaldealList.name           名称
     * @apiSuccess {Integer}   data.totaldealList.vlaue          数量
     * @apiSuccess {Object[]}  data.totalTimeList                任务时效统计
     * @apiSuccess {String}    data.totalTimeList.name           名称
     * @apiSuccess {Integer}   data.totalTimeList.vlaue          数量
     * @apiSuccess {Object[]}  data.totalLevelList               任务紧急程度统计
     * @apiSuccess {String}    data.totalLevelList.name          名称
     * @apiSuccess {Integer}   data.totalLevelList.vlaue         数量
	 * <br/>
	 * @apiError -999 系统异常!
	 * <br/>
	 */
	@RequestMapping(value="/api/taskInfos/_pieCounts",method=RequestMethod.GET)
	public ResultVo getGraphData(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		Map<String,List<Map>> resutMap = taskInfoService.getGraphData(map);
		resultVo.setData(resutMap);
		return resultVo;
	}
	/**
	 * @api {GET} /api/taskInfos/_export   导出任务列表
	 * @apiName   exportTaskInfos
	 * @apiGroup  TaskInfoController 
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉              导出任务列表
	 * <br/>
	 * @apiParam   {Integer}   userId                     用户id
	 * @apiParam   {Integer}   [orgId]                    运营商id           
	 * @apiParam   {Integer}   [stationId]                场站id     
	 * @apiParam   {Integer}   [wqConsId]                 外勤人员id
	 * @apiParam   {Integer}   [templateId]               任务模板id
	 * @apiParam   {Integer}   [taskType]                 任务类型0:告警任务 1:作业任务(parentId=64)
	 * @apiParam   {Integer}   [taskStatus]               任务状态
	 * @apiParam   {Integer}   handleStatus               处理状态 0:待处理
	 * @apiParam   {int}       pageNum                    页码
	 * @apiParam   {int}       pageSize                   页大小
	 * @apiParam   {String}    [sort]                     排序字段
	 * @apiParam   {String}    [order]                    排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -1803000 参数为空异常!
	 * <br/>
	 */
	@RequestMapping(value="/api/taskInfos/_export",method=RequestMethod.GET)
	public void exportTaskInfos(@RequestParam Map map,HttpServletResponse response) throws Exception{
		taskInfoService.exportTaskInfos(map,response);
	}
	/**
	 * 
	 * @param vo
	 * @return
	 * @throws BizException
	 * 2017年3月27日
	 * gaohui
	 */
	@RequestMapping(value = "/taskInfo/list", method = RequestMethod.POST)
	public ResultVo queryTaskInfoAll(@RequestBody DataVo vo){
		return taskInfoService.query(vo.toMap());
	}
	/**
	 * 
	 * @param vo
	 * @return
	 * @throws BizException
	 * 2017年3月27日
	 * gaohui
	 */
	@RequestMapping(value = "/taskInfo/insertTaskInfo", method = RequestMethod.POST)
	public ResultVo insertTaskInfo(@RequestBody DataVo vo) throws BizException{
		taskInfoService.insertTaskInfo(vo);
		return new ResultVo();
	}
	/**
	 * 
	 * @param vo
	 * @return
	 * 2017年3月24日
	 * gaohui
	 */
	@RequestMapping(value = "/taskInfo/findTaskInfoByRecId", method = RequestMethod.POST)
	public ResultVo findTaskInfoByRecId(@RequestBody DataVo vo){
		ResultVo resultVo = new ResultVo();
		Map map = taskInfoService.findTaskInfoByRecId(vo);
		resultVo.setData(map);
		return resultVo;
	}
	/**
	 * 根据taskId更新作业任务同时更新警告状态
	 * @param vo
	 * @return
	 * 2017年3月24日
	 * gaohui
	 * @throws BizException 
	 */
	@RequestMapping(value = "/taskInfo/uTaskAndWarning", method = RequestMethod.POST)
	public ResultVo updateTaskInfoAndWarning(@RequestBody DataVo vo) throws BizException{
	    taskInfoService.updateTaskInfoAndWarning(vo);
		return new ResultVo();
	}
	/**
	 * 再次派单操作
	 * @param vo
	 * @return
	 * @throws BizException
	 * 2017年3月29日
	 * gaohui
	 */
	@RequestMapping(value = "/taskInfo/sendOrderAgain", method = RequestMethod.POST)
	public ResultVo sendOrderAgain(@RequestBody DataVo vo) throws BizException{
	    taskInfoService.sendOrderAgain(vo);
		return new ResultVo();
	}
	
	/**
	 * 场站派单排行
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/taskInfo/subtop",method = RequestMethod.POST)
	public ResultVo getTaskSubTop(@RequestBody DataVo vo){
		return  taskInfoService.getTaskSubTop(vo);
	}
	
	/**
	 * 外勤用户任务完成率排行
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/taskInfo/finishtop",method=RequestMethod.POST)
	public ResultVo getFinishTaskTop(@RequestBody DataVo vo){
		return taskInfoService.getFinishTaskTop(vo);
	}
	/**
	 * 任务紧急程度完成排行
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/taskInfo/leveltop",method=RequestMethod.POST)
	public ResultVo getLevelTaskTop(@RequestBody DataVo vo){
		return taskInfoService.getLevelTaskTop(vo);
	}
	/**
	 * 任务详细
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/taskInfo/id",method=RequestMethod.POST)
	public ResultVo getTaskInfo(@RequestBody DataVo vo){
		return taskInfoService.getTaskInfo(vo);
	}
	/**
	 * 任务处理
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	@RequestMapping(value="/taskInfo/update",method=RequestMethod.POST)
	public ResultVo updateTaskInfo(@RequestBody Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		taskInfoService.updateTaskInfo(map);
		return resVo;
	}
	/**
	 * 根据recId批量查询任务信息
	 * @param map
	 * @return
	 * 2017年4月11日
	 * gaohui
	 */
	@RequestMapping(value="/taskInfo/gTaskByRecIds",method=RequestMethod.POST)
	public ResultVo getTaskInfoByRedIds(@RequestBody DataVo vo) throws BizException{
		ResultVo resVo = new ResultVo();
		resVo.setData(taskInfoService.getTaskInfoByRedIds(vo));
		return resVo;
	}
}

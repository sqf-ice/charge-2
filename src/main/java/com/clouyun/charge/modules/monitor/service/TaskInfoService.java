package com.clouyun.charge.modules.monitor.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.BigDecimalUtils;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.charge.common.constant.MonitorConstants;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.monitor.mapper.TaskInfoMapper;
import com.clouyun.charge.modules.monitor.mapper.TemplateOptionMapper;
import com.clouyun.charge.modules.monitor.mapper.WarningMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: TaskInfoService 
 * 版权: Copyright (c) 2017 
 * 公司: 科陆电子 
 * 作者: sim.y 
 * 版本: 1.0 
 * 创建日期:2017年3月11日
 */
@Service
@SuppressWarnings("rawtypes")
public class TaskInfoService {

	SimpleDateFormat SDF_MONTH = new SimpleDateFormat("dd");
	SimpleDateFormat SDF_WEEK  = new SimpleDateFormat("EEEE");
	@Autowired
	private TaskInfoMapper taskInfoMapper;
	@Autowired
	private WarningMapper warningMapper;
	@Autowired
	private TemplateOptionMapper templateOptionMapper;
    @Autowired
    private DictService  dictService;
    @Autowired
    private UserService  userService;
	/**
	 * 添加作业任务
	 * @param map
	 * @return 2.0.0 
	 * 2017年4月21日 
	 * gaohui
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void addTaskInfo(Map map) throws BizException {
		int max = 0;
		DataVo params = new DataVo(map);
		if (params.isBlank("taskFreq")){
			throw new BizException(1803000,"任务频率");
		}
		if (params.isBlank("wqConsId")){
			throw new BizException(1803000,"外勤人员");
		}
		if (params.isBlank("taskLevel")){
			throw new BizException(1803000,"任务级别");
		}
		if (params.isBlank("stationId")){
			throw new BizException(1803000,"场站");
		}
		if (params.isBlank("orgId")){
			throw new BizException(1803000,"运营商");
		}
		if (params.isBlank("taskStartHour")){
			throw new BizException(1803000,"开始时间");
		}
		if (params.isBlank("taskEndHour")){
			throw new BizException(1803000,"结束时间");
		}
		params.put("taskTime",new Date());//任务派单时间
		params.put("taskStatus", MonitorConstants.WTStatus.DS.getStatus());//已派单
		params.put("taskType", MonitorConstants.TaskType.TASK.getType());//作业任务
		String taskFreq = params.getString("taskFreq");
		String taskStartHour = params.getString("taskStartHour");
		String taskEndHour = params.getString("taskEndHour");
		if(MonitorConstants.TaskFreq.SINGLE.getType().equals(taskFreq)){//指定时间的新增作业任务
			if(DateUtils.parseDate(taskStartHour).before(new Date())){//开始时间小于当前时间
				throw new BizException(1803001,"开始时间小于当前时间!");
			}
			if(DateUtils.parseDate(taskEndHour)
			   .before(DateUtils.parseDate(taskStartHour))){//开始时间大于结束时间
				throw new BizException(1803002,"开始时间大于结束时间!");
			}
			params.put("taskFreqStr","单次任务");//单次任务
			params.put("taskDate", String.valueOf(taskStartHour).substring(0, 10));
		}else{//指定时间段的新增作业任务
			max = differentDays(
			CalendarUtils.convertStrToCalendar(taskStartHour,CalendarUtils.yyyyMMdd),
		    CalendarUtils.convertStrToCalendar(taskEndHour,CalendarUtils.yyyyMMdd));
			if(max<0){
				throw new BizException(1803003,"开始日期不能小于结束日期!");
			}
			for(int i=0;i<max+1;i++){
				Calendar cal = CalendarUtils.convertStrToCalendar(taskStartHour,CalendarUtils.yyyyMMdd);
				cal.add(Calendar.DATE, i);
				if(MonitorConstants.TaskFreq.DAY.getType().equals(taskFreq)){
					params.put("taskFreqStr", "每天");
				}else if(MonitorConstants.TaskFreq.WEEK.getType().equals(taskFreq)){//按周
					if (params.isBlank("weeks")){
						throw new BizException(1803000,"周频率");
					}
					String week = params.getString("weeks");
					String[] weeks = week.split(",");
					if(!getConMonthOrWeek(weeks,ConWeekLangue(SDF_WEEK.format(cal.getTime())))){
						throw new BizException(1803004, "每周频率不在指定时间段内!");
					}
					if (weeks.length == 7) {
						params.put("taskFreqStr", "每天");
					} else if (weeks.length == 5 && !getWorkDay(weeks)) {
						params.put("taskFreqStr", "工作日");
					} else {
						params.put("taskFreqStr", week);
					}
				}else if(MonitorConstants.TaskFreq.MONTH.getType().equals(taskFreq)){//按月
					if (params.isBlank("months")){
						throw new BizException(1803000,"月频率");
					}
					String month = params.getString("months");
					String[] months  = month.split(",");
					if(!getConMonthOrWeek(months,SDF_MONTH.format(cal.getTime()))){
						throw new BizException(1803005, "每月频率不在指定时间段内!");
					}
					if (months.length == 31) {
						params.put("taskFreqStr", "每天");
					} else {
						params.put("taskFreqStr", Arrays.toString(months));
					}
				}
				params.put("taskDate", CalendarUtils.formatCalendar(cal,CalendarUtils.yyyyMMdd));
			}
		}
		taskInfoMapper.insert(params);
		if (params.isNotBlank("recId")) {
			map.put("handleFlag", MonitorConstants.WTStatus.DS.getStatus());
			warningMapper.update(params);
		}
	}

	public static int differentDays(Calendar date1, Calendar date2) {
		int day1 = date1.get(Calendar.DAY_OF_YEAR);
		int day2 = date2.get(Calendar.DAY_OF_YEAR);
		int year1 = date1.get(Calendar.YEAR);
		int year2 = date2.get(Calendar.YEAR);
		if (year1 != year2) {
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
					timeDistance += 366;
				} else {
					timeDistance += 365;
				}
			}
			return timeDistance + (day2 - day1);
		} else {
			return day2 - day1;
		}
	}
	/**
	 * 分页查询作业任务列表
	 * @param map
	 * @return 2.0.0 
	 * 2017年4月21日 
	 * gaohui
	 */
	@SuppressWarnings("unchecked")
	public PageInfo getTaskInfosPage(Map map) throws BizException {
		PageInfo pageInfo = null;
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		Set<Integer> stationIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		if(null!= orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		if(null!= stationIds && stationIds.size()>0){
			params.put("stationIds", stationIds);
		}
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(map);
		}
		List<Map> result = taskInfoMapper.get(map);
		if (null != result && result.size() > 0) {
			for(Map res:result){
				String status = new DataVo(res).getString("taskStatus");
				if(MonitorConstants.WTStatus.CS.getStatus().equals(status)
				   ||MonitorConstants.WTStatus.RS.getStatus().equals(status)){
					res.put("handleStatus", "待处理");	
				}else if(MonitorConstants.WTStatus.ES.getStatus().equals(status)
						 ||MonitorConstants.WTStatus.ADS.getStatus().equals(status)){
					res.put("handleStatus", "已处理");	
				}else{
					res.put("handleStatus", "详情");
				}
			}
			pageInfo = new PageInfo(result);
		}
		return pageInfo;
	}
	/**
	 * 查询作业详情
	 * @param map
	 * @return 2.0.0 
	 * 2017年4月21日 
	 * gaohui
	 */
	@SuppressWarnings("unchecked")
	public Map getTaskInfos(Integer taskId)throws BizException{
		Map taskInfoMap = taskInfoMapper.getById(taskId);//获取任务信息
		Integer templateId = null;
		if(null!=taskInfoMap){
			templateId = (Integer) taskInfoMap.get("patternId");
			DataVo dataVo = new DataVo();
			dataVo.put("templateId", templateId);
			List option = templateOptionMapper.queryListByPage(dataVo);//获取任务模板项
			Double totalPrice = 0D;
			if (null!=option && option.size() > 0) {
				List<Integer> optionIds = new ArrayList<Integer>();
				for (int i = 0; i < option.size(); i++) {
					Map optionMap = (Map) option.get(i);
					optionIds.add(StringUtils.toInteger(optionMap.get("optionId")));//选项ID集合
				}
				dataVo.add("optionIds", optionIds);
				dataVo.add("taskId", taskId);
				List optList = taskInfoMapper.getTaskOptionValue(dataVo);//获取文本/数据值
				List photoList = taskInfoMapper.getTaskOptionPhoto(dataVo);//获取图片
				List positionList = taskInfoMapper.getTaskOptionPosition(dataVo);//获取位置
				List materielList = taskInfoMapper.getTaskOptionMateriel(dataVo);//获取物料
				
				for (int i = 0; i < option.size(); i++) {
					Map listMap = (Map) option.get(i);
					Integer optionId = StringUtils.toInteger(listMap.get("optionId"));
					String  optionRes = String.valueOf(listMap.get("optionResources"));
					if (MonitorConstants.OptRes.TEXT.getCode().equals(optionRes) 
							|| MonitorConstants.OptRes.DATA.getCode().equals(optionRes)) {//文本或数据
						List optListMap = null;
						if (optList != null && optList.size() > 0) {
							optListMap = new ArrayList();
							for (int j = 0; j < optList.size(); j++) {
								Map optMap = (Map) optList.get(j);
								Integer optId = StringUtils.toInteger(optMap.get("optionId"));
								if (optId.intValue() == optionId.intValue()) {
									optListMap.add(optMap);
								}
							}
						}
						listMap.put("reslist", optListMap);
					} else if (MonitorConstants.OptRes.PHOT.getCode().equals(optionRes) 
							|| MonitorConstants.OptRes.PHOG.getCode().equals(optionRes)) {//照片或照片(GPS)
						List photoListMap = null;
						if (photoList != null && photoList.size() > 0) {
							photoListMap = new ArrayList();
							for (int j = 0; j < photoList.size(); j++) {
								Map photoMap = (Map) photoList.get(j);
								Integer optId = StringUtils.toInteger(photoMap.get("optionId"));
								if (optId.intValue() == optionId.intValue()) {
									photoListMap.add(photoMap);
								}
							}
						}
						listMap.put("reslist", photoListMap);
					} else if (MonitorConstants.OptRes.POSI.getCode().equals(optionRes)) {//位置
						List positionListMap = null;
						if (positionList != null && positionList.size() > 0) {
							positionListMap = new ArrayList();
							for (int j = 0; j < positionList.size(); j++) {
								Map positionMap = (Map) positionList.get(j);
								Integer optId = StringUtils.toInteger(positionMap.get("optionId"));
								if (optId.intValue() == optionId.intValue()) {
									positionListMap.add(positionMap);
								}
							}
						}
						listMap.put("reslist", positionListMap);
					} else if (MonitorConstants.OptRes.MATE.getCode().equals(optionRes)) {//物料
						List materielListMap = null;
						if (materielList != null && materielList.size() > 0) {
							materielListMap = new ArrayList();
							for (int j = 0; j < materielList.size(); j++) {
								Map materielMap = (Map) materielList.get(j);
								Double quant = StringUtils.toDouble((materielMap.get("materialTotal")));
								totalPrice += quant;
								Integer optId = StringUtils.toInteger(materielMap.get("optionId"));
								if (optId.intValue() == optionId.intValue()) {
									materielListMap.add(materielMap);
								}
							}
						}
						listMap.put("reslist", materielListMap);
					}
				}
			}
			taskInfoMap.put("option", option);
			taskInfoMap.put("totalPrice", MonitorConstants.DF.format(totalPrice));
		}
		return taskInfoMap;
	}
	/**
	 * 再次派单操作
	 * @param map
	 * @throws BizException
	 * 2017年4月26日
	 * gaohui
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	public void dispatchAgain(Map map) throws BizException {
		DataVo params = new DataVo(map);
		params.put("taskStatus", MonitorConstants.WTStatus.ADS.getStatus());
		params.put("taskAgain", MonitorConstants.TASKAGAIN);
		params.put("taskAgainTime", new Date());
		taskInfoMapper.updateTaskInfo(params);
		Map result = taskInfoMapper.getById((Integer) params.get("taskId"));
		if(params.isNotBlank("recId")){
			params.put("recId", result.get("recId"));
			params.put("handleFlag", MonitorConstants.WTStatus.ADS.getStatus());
			warningMapper.updateWarningByRecId(params);
		}
	}
	
	/**
	 * 作业任务确认处理操作
	 * 2017年4月26日
	 * gaohui
	 * 2.0.0
	 */
    @SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	public void handleConfirm(Map map)throws BizException{
    	DataVo params = new DataVo(map);
    	params.put("taskStatus", MonitorConstants.WTStatus.ES.getStatus());
    	taskInfoMapper.updateTaskInfo(params);//更新作业信息(状态)
    	Map result = taskInfoMapper.getById(params.getInt("taskId"));
		if(params.isNotBlank("recId")){
			params.put("recId", result.get("recId"));
			params.put("handleFlag", MonitorConstants.WTStatus.ES.getStatus());
			warningMapper.updateWarningByRecId(params);//更新告警信息(状态)
		}
    }
	
	/**
	 * 饼图统计数据
	 * @param map
	 * @throws BizException
	 * 2017年3月29日
	 *  gaohui
	 *  2.0.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<Map>> getGraphData(Map map)throws BizException{
		int countS = 0;
		int countA = 0;
		int countB = 0;
		int countC = 0;
		int countD = 0;
		int dealed = 0;//已处理
		int undeal = 0;//未处理
		int returned = 0;//已退回
		int ontimeCount = 0;//按时处理
		int overtimeCount = 0;//超时处理
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!= orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		List<Map> list = taskInfoMapper.get(params);
		if(null!= list && list.size()>0){
			for (Map result:list) {
				DataVo data = new DataVo(result);
				String level = "";
				if(data.isBlank("taskLevel")){
					throw new BizException(1803000,"");
				}else{
					level = data.getString("taskLevel");
				}
				switch (level) {
				case MonitorConstants.S:
					countS++;
					break;
				case MonitorConstants.A:
					countA++;
					break;
				case MonitorConstants.B:
					countB++;
					break;
				case MonitorConstants.C:
					countS++;
					break;
				case MonitorConstants.D:
					countD++;
					break;
				}
				String  taskStatus = "";
				if(data.isNotBlank("taskStatus")){
					taskStatus= data.getString("taskStatus");
				}
				if(taskStatus.equals(MonitorConstants.WTStatus.ES.getStatus())){
					dealed++;
					String overTime = MonitorConstants.Time.ONTIME.getCode();
					if(data.isNotBlank("taskOvertime")){
						overTime =data.getString("taskOvertime");
					}
					if(MonitorConstants.Time.OVERTIME.getCode().equals(overTime)){//超时处理
						overtimeCount++;
					}else{
						ontimeCount ++;
					}
				}else if (taskStatus.equals(MonitorConstants.WTStatus.RS.getStatus())){//退回
					returned++;
				}else{//未处理
					undeal++;
				}
			}
		}
		Map totalTaskMap = new HashMap();
		List<Map> totalList = new ArrayList<Map>();
		//图表1
		Map totalMap = new HashMap();
		totalMap.put("value", dealed);
		totalMap.put("name", "已处理");
		totalList.add(totalMap);
		totalMap = new HashMap();
		totalMap.put("value", undeal);
		totalMap.put("name", "未处理");
		totalList.add(totalMap);
		totalMap =  new HashMap();
		totalMap.put("value", returned);
		totalMap.put("name", "已退回");
		totalList.add(totalMap);
		totalTaskMap.put("totaldealList", totalList);
		//图表2
		totalList = new ArrayList<Map>();
		totalMap = new HashMap();
		totalMap.put("value", ontimeCount);
		totalMap.put("name", "按时处理");
		totalList.add(totalMap);
		totalMap = new HashMap();
		totalMap.put("value", overtimeCount);
		totalMap.put("name", "超时处理");
		totalList.add(totalMap);
		totalTaskMap.put("dealCount", dealed);
		totalTaskMap.put("totalTimeList",totalList);
		//图表3
		totalList = new ArrayList<Map>();
		totalMap = new HashMap();
		totalMap.put("value", countS);
		totalMap.put("name",MonitorConstants.S);
		totalList.add(totalMap);
		totalMap = new HashMap();
		totalMap.put("value", countA);
		totalMap.put("name", MonitorConstants.A);
		totalList.add(totalMap);
		totalMap = new HashMap();
		totalMap.put("value", countB);
		totalMap.put("name", MonitorConstants.B);
		totalList.add(totalMap);
		totalMap = new HashMap();
		totalMap.put("value", countC);
		totalMap.put("name", MonitorConstants.C);
		totalList.add(totalMap);
		totalMap = new HashMap();
		totalMap.put("value", countD);
		totalMap.put("name", MonitorConstants.D);
		totalList.add(totalMap);
		totalTaskMap.put("taskTotal", list.size());
		totalTaskMap.put("totalLevelList", totalList);
		return totalTaskMap;
	}
	/**
	 * 导出任务列表
	 * @param map
	 * @throws Exception 
	 * 2017年5月10日
	 * gaohui
	 */
	@SuppressWarnings("unchecked")
	public void exportTaskInfos(Map map,HttpServletResponse response) throws Exception{
		DataVo params = new DataVo(map);
		if(params.isBlank("sort")){
			params.put("sort", "this_.task_date");
			params.put("order","DESC");
		}
		String header = "";
		if(params.getString("handleStatus").equals("0")){//待处理任务列表
			header = "待处理任务列表";
		}else{
			header = "任务列表";
		}
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!= orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		List<Map> taskList = taskInfoMapper.get(params);
		if (null != taskList && taskList.size() > 0) {
			List<ComboxVo> taskTypes = dictService.getDictByType("rwlx");//任务类型
			List<ComboxVo> taskStatuss = dictService.getDictByType("rwzt");
			for (Map task : taskList) {
				DataVo taskVo = new DataVo(task);
				if (taskVo.isNotBlank("taskType")) {
					for (ComboxVo comboxVo : taskTypes) {
						if (taskVo.getString("taskType").equals(comboxVo.getId())) {
							task.put("taskType", comboxVo.getText());
							continue;
						}

					}
				}
				
				if (taskVo.isNotBlank("taskStatus")) {
					String taskStatus = taskVo.getString("taskStatus");
					if(taskStatus.equals(MonitorConstants.WTStatus.CS.getCode())
					   ||taskStatus.equals(MonitorConstants.WTStatus.RS.getCode())){
						task.put("handleStatus", "待处理");
					}else if(taskStatus.equals(MonitorConstants.WTStatus.ES.getCode())
							 ||taskStatus.equals(MonitorConstants.WTStatus.ADS.getCode())){
						task.put("handleStatus", "已处理");
					}else{
						task.put("handleStatus", "详情");
					}
					for (ComboxVo comboxVo : taskStatuss) {
						if (taskStatus.equals(comboxVo.getId())) {
							task.put("taskStatus", comboxVo.getText());
							continue;
						}

					}
				}
			}
		}
		List<String> headers = new ArrayList<String>();
		headers.add("任务时间");
		headers.add("任务名称");
		headers.add("任务类型");
		headers.add("任务模板");
		headers.add("所属场站");
		headers.add("任务状态");
		headers.add("处理状态");
		headers.add("外勤人员");
		headers.add("处理时间");
		List<String> rows = new ArrayList<String>();
		rows.add("taskDate");
		rows.add("taskName");
		rows.add("taskType");
		rows.add("templateName");
		rows.add("stationName");
		rows.add("taskStatus");
		rows.add("handleStatus");
		rows.add("wqConsName");
		rows.add("taskDealtTime");
		ExportUtils.exportExcel(taskList, response, headers, rows, header);
		
	}
	
	@SuppressWarnings("unchecked")
	public List getDispatchRank(Map map) throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!= orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		List list = taskInfoMapper.getTaskSubTop(params);
		//int count = taskInfoMapper.getTaskSubCount(dataVo);
		return list;
	}
	
	/**
	 * 外勤用户任务完成数排行
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> finishedRateRank(Map map) throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!= orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		List<Map> list = taskInfoMapper.getFinishTaskTop(params);//获取用户任务数
		params.put("taskStatus", MonitorConstants.WTStatus.ES.getStatus());//获取用户完成任务数
		List finishList = taskInfoMapper.getFinishTaskTop(params);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map listMap = (Map) list.get(i);
				int wqConsId = (Integer) listMap.get("wqConsId");
				int taskCount = new Long((long) listMap.get("taskCount")).intValue();
				if (taskCount > 0) {
					int finishCount = 0;
					for (int j = 0; j < finishList.size(); j++) {
						Map finishListMap = (Map) finishList.get(j);
						int finishWqConsId = (Integer) finishListMap
								.get("wqConsId");
						if (wqConsId == finishWqConsId) {
							finishCount = new Long(
									(long) finishListMap.get("taskCount"))
									.intValue();
							break;
						}
					}
					if (finishCount > 0) {
						listMap.put("finishedRate", BigDecimalUtils.div(finishCount, taskCount,4));
					} else {
						listMap.put("finishedRate", new Double(0));
					}
				} else {
					listMap.put("finishedRate", new Double(0));
				}
			}
			// 匿名实现Comparator接口进行排序
			Collections.sort(list, new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					// 进行判断
					return ((Double) m2.get("finishedRate"))
							.compareTo((Double) m1.get("finishedRate"));
				}
			});
		}
		return list;
	}
	
	/**
	 * 任务紧急程度完成排行
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getlevelRank(Map mapVo) throws BizException{
		DataVo params = new DataVo(mapVo);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!= orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		List<Map> list = taskInfoMapper.getLevelTaskTop(params);//获取紧急程度任务下发外勤用户
		int totalCount = 0;//下发总数
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				totalCount += new Long((long) map.get("totalCount")).intValue();
			}
			params.add("taskStatus",MonitorConstants.WTStatus.ES.getStatus());// 获取任务完成数
			List finishList = taskInfoMapper.getFinishTaskTop(params);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				int wqConsId = (Integer) map.get("wqConsId");
				int finishCount = 0;
				if (finishList != null && finishList.size() > 0) {
					for (int j = 0; j < finishList.size(); j++) {
						Map finishMap = (Map) finishList.get(j);
						int finishWqConsId = (int) finishMap.get("wqConsId");
						if (wqConsId == finishWqConsId) {
							finishCount = new Long(
									(long) finishMap.get("taskCount"))
									.intValue();
							break;
						}
					}
					if (finishCount > 0) {
						map.put("finishedRate", BigDecimalUtils.div(finishCount, totalCount,4));
					} else {
						map.put("finishedRate", 0);
					}
				} else {
					map.put("finishedRate", 0);
				}
				map.put("taskCount", finishCount);

			}
			// 匿名实现Comparator接口进行排序
			Collections.sort(list, new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					// 进行判断
					return ((Integer) m2.get("taskCount"))
							.compareTo((Integer) m1.get("taskCount"));
				}
			});
		}
		return list;
	}
	
	public ResultVo query(Map map) {
		ResultVo vo = new ResultVo();
		String taskDateEnd = (String) map.get("taskDateEnd");
		if (taskDateEnd != null && !"".equals(taskDateEnd)) {
			taskDateEnd += "23:59:59";
			map.put("taskDateEnd", taskDateEnd);
		}
		List list = taskInfoMapper.queryListByPage(map);
		int count = taskInfoMapper.queryListCount(map);
		vo.setTotal(count);
		vo.setData(list);
		return vo;
	}

	public ResultVo queryById(Map map) {
		ResultVo vo = new ResultVo();
		vo.setData(taskInfoMapper.queryById(map));
		return vo;
	}

	public ResultVo checkUniqueness(Map map) {
		ResultVo vo = new ResultVo();
		vo.setData(taskInfoMapper.checkUniqueness(map));
		return vo;
	}

	public ResultVo insert(Map map) {
		ResultVo vo = new ResultVo();
		vo.setData(taskInfoMapper.insert(map));
		return vo;
	}

	public ResultVo delete(Map map) {
		ResultVo vo = new ResultVo();
		vo.setData(taskInfoMapper.delete(map));
		return vo;
	}

	/**
	 * 根据告警ID查询作业信息
	 * @param map
	 * @return 
	 * 2017年3月25日 
	 * gaohui
	 */
	public Map findTaskInfoByRecId(Map map) {
		return taskInfoMapper.findTaskInfoByRecId(map);
	}

	/**
	 * 派单插入任务同时更新警告状态
	 * @param map
	 * 2017年3月25日 gaohui
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void insertTaskInfo(Map map) throws BizException {
		if (!"".equals(map.get("flag")) && null != map.get("flag")) {
			if ((boolean) map.get("flag")) {
				String week = (String) map.get("week");
				String month = (String) map.get("month");
				int max = (int) map.get("max");
				String taskFreq = (String) map.get("taskFreq");
				String startHour = (String) map.get("startHour");
				String[] weeks = null;
				String[] months = null;
				if (!"".equals(week) && null != week) {
					weeks = week.split(",");
				}
				if (!"".equals(month) && null != month) {
					months = month.split(",");
				}
				if (max >= 0) {
					SimpleDateFormat sdf_month = new SimpleDateFormat("dd");
					SimpleDateFormat sdf_week = new SimpleDateFormat("EEEE");
					for (int i = 0; i < max + 1; i++) {
						Calendar cal = CalendarUtils.convertStrToCalendar(
								startHour, "yyyy-MM-dd");
						cal.add(Calendar.DATE, i);
						if (MonitorConstants.TaskFreq.DAY.getType().equals(taskFreq)) {// 按日
							map.put("taskFreqStr", "每天");
						} else if (MonitorConstants.TaskFreq.WEEK.getType().equals(taskFreq)) {// 按周
							if (weeks.length == 7) {
								map.put("taskFreqStr", "每天");
							} else if (weeks.length == 5 && !getWorkDay(weeks)) {
								map.put("taskFreqStr", "工作日");
							} else {
								map.put("taskFreqStr", week);
							}
							if (!getConMonthOrWeek(weeks,ConWeekLangue(sdf_week.format(cal.getTime())))) {
								continue;
							}
						} else if (MonitorConstants.TaskFreq.MONTH.getType().equals(taskFreq)) {// 按月
							if (month.length() == 31) {
								map.put("taskFreqStr", "每天");
							} else {
								map.put("taskFreqStr", month);
							}
							if (!getConMonthOrWeek(months,sdf_month.format(cal.getTime()))) {
								continue;
							}
						}
						map.put("taskDate", CalendarUtils.formatCalendar(cal,
								CalendarUtils.yyyyMMdd));
						taskInfoMapper.insert(map);
						if (null != map.get("recId")&& !"".equals(map.get("recId"))) {
							warningMapper.updateWarningByRecId(map);
						}
					}

				}
			}
		} else {
			taskInfoMapper.insert(map);
			if (null != map.get("recId") && !"".equals(map.get("recId"))) {
				warningMapper.updateWarningByRecId(map);
			}
		}
	}

	/**
	 * 判断是否包含在里面
	 * 
	 * @param months
	 * @param mons
	 * @return 2017年4月15日 gaohui
	 */
	public boolean getConMonthOrWeek(String[] strs, String str) {
		boolean flag = false;
		if (null != strs && strs.length > 0) {
			for (String temp : strs) {
				if (temp.trim().equals(str.trim())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public String ConWeekLangue(String str) {
		String week = "";
		if ("Monday".equalsIgnoreCase(str.trim())) {
			week = "星期一";
		} else if ("Tuesday".equalsIgnoreCase(str.trim())) {
			week = "星期二";
		} else if ("Wednesday".equalsIgnoreCase(str.trim())) {
			week = "星期三";
		} else if ("Thursday".equalsIgnoreCase(str.trim())) {
			week = "星期四";
		} else if ("Friday".equalsIgnoreCase(str.trim())) {
			week = "星期五";
		} else if ("Saturday".equalsIgnoreCase(str.trim())) {
			week = "星期六";
		} else if ("Sunday".equalsIgnoreCase(str.trim())) {
			week = "星期日";
		} else {
			week = str;
		}
		return week;
	}

	public boolean getWorkDay(String[] workDay) {
		boolean flag = true;
		for (String day : workDay) {
			if (day.equals("星期六") || day.equals("星期日")) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 *  根据taskId更新作业任务同时更新警告状态
	 *  2017年3月27日 
	 *  gaohui
	 */
	@Transactional
	public void updateTaskInfoAndWarning(Map map) throws BizException {
		taskInfoMapper.updateTaskInfoByRecId(map);
		if (null != map.get("recId") && !"".equals(map.get("recId"))) {
			warningMapper.updateWarningByRecId(map);
		}
	}

	/**
	 * 再次派单操作
	 * @param map
	 * @throws BizException
	 * 2017年3月29日
	 *  gaohui
	 */
	@Transactional
	public void sendOrderAgain(Map map) throws BizException {
		taskInfoMapper.updateTaskInfoByRecId(map);
		if (null != map.get("recId") && !"".equals(map.get("recId"))) {
			warningMapper.updateWarningByRecId(map);
		}
	}

	/**
	 * 场站派单排行
	 * 
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	public ResultVo getTaskSubTop(DataVo vo) {
		ResultVo resVo = new ResultVo();
		List list = taskInfoMapper.getTaskSubTop(vo);
		resVo.setData(list);
		int count = taskInfoMapper.getTaskSubCount(vo);
		resVo.setTotal(count);
		return resVo;
	}

	/**
	 * 外勤用户任务完成数排行
	 * @param vo
	 * @return
	 */
	public ResultVo getFinishTaskTop(DataVo vo) {
		ResultVo resVo = new ResultVo();
		List list = taskInfoMapper.getFinishTaskTop(vo);//获取用户任务数
		vo.put("taskStatus", 5);//获取用户完成任务数
		List finishList = taskInfoMapper.getFinishTaskTop(vo);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map listMap = (Map) list.get(i);
				int wqConsId = (Integer) listMap.get("wqConsId");
				int taskCount = new Long((long) listMap.get("taskCount"))
						.intValue();
				if (taskCount > 0) {
					int finishCount = 0;
					for (int j = 0; j < finishList.size(); j++) {
						Map finishListMap = (Map) finishList.get(j);
						int finishWqConsId = (Integer) finishListMap
								.get("wqConsId");
						if (wqConsId == finishWqConsId) {
							finishCount = new Long(
									(long) finishListMap.get("taskCount"))
									.intValue();
							break;
						}
					}
					if (finishCount > 0) {
						listMap.put("taskCount", (finishCount * 10000)
								/ taskCount);
					} else {
						listMap.put("taskCount", 0);
					}
				} else {
					listMap.put("taskCount", 0);
				}
			}
			// 匿名实现Comparator接口进行排序
			Collections.sort(list, new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					// 进行判断
					return ((Integer) m2.get("taskCount"))
							.compareTo((Integer) m1.get("taskCount"));
				}
			});
		}
		resVo.setData(list);
		return resVo;
	}

	/**
	 * 任务紧急程度完成排行
	 * 
	 * @param vo
	 * @return
	 */
	public ResultVo getLevelTaskTop(DataVo vo) {
		ResultVo resVo = new ResultVo();
		List list = taskInfoMapper.getLevelTaskTop(vo);//获取紧急程度任务下发外勤用户
		int totalCount = 0;//下发总数
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				totalCount += new Long((long) map.get("totalCount")).intValue();
			}
			vo.add("taskStatus",MonitorConstants.WTStatus.ES.getStatus());// 获取任务完成数
			List finishList = taskInfoMapper.getFinishTaskTop(vo);
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				int wqConsId = (Integer) map.get("wqConsId");
				int finishCount = 0;
				if (finishList != null && finishList.size() > 0) {
					for (int j = 0; j < finishList.size(); j++) {
						Map finishMap = (Map) finishList.get(j);
						int finishWqConsId = (int) finishMap.get("wqConsId");
						if (wqConsId == finishWqConsId) {
							finishCount = new Long(
									(long) finishMap.get("taskCount"))
									.intValue();
							break;
						}
					}
					if (finishCount > 0) {
						map.put("taskCountRate", (finishCount * 100)/totalCount);
					} else {
						map.put("taskCountRate", 0);
					}
				} else {
					map.put("taskCountRate", 0);
				}
				map.put("taskCount", finishCount);

			}
			// 匿名实现Comparator接口进行排序
			Collections.sort(list, new Comparator<Map>() {
				@Override
				public int compare(Map m1, Map m2) {
					// 进行判断
					return ((Integer) m2.get("taskCount"))
							.compareTo((Integer) m1.get("taskCount"));
				}
			});
		}
		resVo.setTotal(totalCount);
		resVo.setData(list);
		return resVo;
	}

	/**
	 * 获取单个任务的详细信息
	 * 
	 * @param vo
	 * @return
	 */
	public ResultVo getTaskInfo(DataVo vo) {
		ResultVo resVo = new ResultVo();
		// 获取任务信息
		Map taskInfoMap = taskInfoMapper.queryById(vo.toMap());
		Integer taskId = vo.getInt("taskId");
		Integer templateId = (Integer) taskInfoMap.get("patternId");
		vo = new DataVo();
		vo.put("templateId", templateId);
		// 获取任务模板项
		List templateOptionList = templateOptionMapper.queryListByPage(vo);
		Double totalPrice = 0d;
		if (templateOptionList != null && templateOptionList.size() > 0) {
			List<Integer> optionIds = new ArrayList<Integer>();
			for (int i = 0; i < templateOptionList.size(); i++) {
				Map optionMap = (Map) templateOptionList.get(i);
				optionIds.add((Integer) optionMap.get("optionId"));
			}
			vo.add("optionIds", optionIds);
			vo.add("taskId", taskId);
			// 获取文本/数据 值
			List optList = taskInfoMapper.getTaskOptionValue(vo);
			// 获取照片(照片+位置)
			List photoList = taskInfoMapper.getTaskOptionPhoto(vo);
			// 获取位置
			List positionList = taskInfoMapper.getTaskOptionPosition(vo);
			// 获取物料
			List materielList = taskInfoMapper.getTaskOptionMateriel(vo);

			for (int i = 0; i < templateOptionList.size(); i++) {
				Map listMap = (Map) templateOptionList.get(i);
				Integer optionId = (Integer) listMap.get("optionId");
				String optionRes = listMap.get("optionResources").toString();
				if ("01".equals(optionRes) || "02".equals(optionRes)) {
					List optListMap = new ArrayList();
					if (optList != null && optList.size() > 0) {
						for (int j = 0; j < optList.size(); j++) {
							Map optMap = (Map) optList.get(j);
							Integer optId = (Integer) optMap.get("optionId");
							if (optId.intValue() == optionId.intValue()) {
								optListMap.add(optMap);
							}
						}
					}
					listMap.put("list", optListMap);
				} else if ("03".equals(optionRes) || "04".equals(optionRes)) {
					List photoListMap = new ArrayList();
					if (photoList != null && photoList.size() > 0) {
						for (int j = 0; j < photoList.size(); j++) {
							Map photoMap = (Map) photoList.get(j);
							Integer optId = (Integer) photoMap.get("optionId");
							if (optId.intValue() == optionId.intValue()) {
								photoListMap.add(photoMap);
							}
						}
					}
					listMap.put("list", photoListMap);
				} else if ("05".equals(optionRes)) {
					List positionListMap = new ArrayList();
					if (positionList != null && positionList.size() > 0) {
						for (int j = 0; j < positionList.size(); j++) {
							Map positionMap = (Map) positionList.get(j);
							Integer optId = (Integer) positionMap
									.get("optionId");
							if (optId.intValue() == optionId.intValue()) {
								positionListMap.add(positionMap);
							}
						}
					}
					listMap.put("list", positionListMap);
				} else if ("06".equals(optionRes)) {
					List materielListMap = new ArrayList();
					if (materielList != null && materielList.size() > 0) {
						for (int j = 0; j < materielList.size(); j++) {
							Map materielMap = (Map) materielList.get(j);
							Double quant = Double.valueOf(materielMap.get(
									"materialTotal").toString());
							totalPrice += quant;
							Integer optId = (Integer) materielMap
									.get("optionId");
							if (optId.intValue() == optionId.intValue()) {
								materielListMap.add(materielMap);
							}
						}
					}
					listMap.put("list", materielListMap);
				}
			}
		}
		taskInfoMap.put("templateOptionList", templateOptionList);
		taskInfoMap.put("totalPrice", MonitorConstants.DF.format(totalPrice));
		resVo.setData(taskInfoMap);
		return resVo;
	}

	/**
	 * 任务处理
	 * 
	 * @param vo
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateTaskInfo(Map map) throws BizException {
		taskInfoMapper.updateTaskInfo(map);
		Map result = taskInfoMapper.getById((Integer)map.get("taskId"));
		if(null!=result.get("recId")&&!"".equals(result.get("recId"))){
			map.put("handleFlag", MonitorConstants.WTStatus.ES.getStatus());
			map.put("recId", result.get("recId"));
			warningMapper.updateWarningByRecId(map);//更新告警信息(状态)
		}
	}

	/**
	 * 根据recId批量查询任务信息
	 * @param vo
	 * @throws BizException
	 * 2017年4月11日 gaohui
	 */
	public List<DataVo> getTaskInfoByRedIds(DataVo vo) throws BizException {
		return taskInfoMapper.getTaskInfoByRedIds(vo);
	}
}

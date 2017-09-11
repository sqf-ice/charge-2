package com.clouyun.charge.modules.monitor.service;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.cdzcache.imp.CDZStatusGet;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.MonitorConstants;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.monitor.mapper.AlertMapper;
import com.clouyun.charge.modules.monitor.mapper.TaskInfoMapper;
import com.clouyun.charge.modules.monitor.mapper.WarningMapper;
import com.clouyun.charge.modules.system.mapper.FieldUserMapper;
import com.clouyun.charge.modules.system.mapper.UserMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 
 * 描述: 告警service
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0
 * 创建日期: 2017年4月20日
 */
@Service
@SuppressWarnings("rawtypes")
public class WarningService extends BusinessService{
	
	public static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private  UserMapper userMapper;
	@Autowired
	private  AlertService alertService;
	@Autowired
	private  AlertMapper alertMapper;
	@Autowired
    private  WarningMapper warningMapper;
	@Autowired
	private  TaskInfoMapper taskInfoMapper;
	@Autowired
	private  TaskInfoService taskInfoService;
	@Autowired
    private  FieldUserMapper fieldUserMapper;
	@Autowired
	private DictService dictService;
	@Autowired
	private  UserService userService;
	@Autowired
	private CDZStatusGet csg;
	
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	public void manualWarning(Map map)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("recId")){
			throw new BizException(1803000, "告警id");
		}
		if(params.isBlank("userId")){
			throw new BizException(1803000, "处理人");
		}
		if(params.isBlank("handleDesc")){
			throw new BizException(1803000, "处理描述");
		}
		if (params.isBlank("pileId")) {
			throw new BizException(1803000,"设备id");
		}
		params.put("handleFlag",MonitorConstants.WTStatus.ES.getStatus());
		params.put("taskType",MonitorConstants.TaskType.TASK.getType());
		params.put("handleMethod",MonitorConstants.UserType.USER.getType());
		Double countTotal = 0.00;
		List<DataVo> artifactArrs = (List<DataVo>) params.getList("artifactArrs");
		if(null!=artifactArrs && artifactArrs.size()>0){
			for (DataVo arr:artifactArrs){
				if(params.isBlank("matId")){
					throw new BizException(1803000, "物料id");
				}
				if(params.isBlank("materialQuant")){
					throw new BizException(1803000, "物料数量");
				}
				if(params.isBlank("materialTotal")){
					throw new BizException(1803000, "物料总价");
				}
				arr.put("taskId", -1);
				countTotal += arr.getDouble("materialTotal");
				alertService.addAlert(arr);
			}
		}
		params.put("artifactTotalPrice", countTotal);
		warningMapper.update(params);
	}
	
	/**
	 * 根据告警id查询告警详情
	 * @return
	 * 2017年4月20日
	 * 2.0.0
	 * gaohui
	 */
	@SuppressWarnings("unchecked")
    public Map getWarning(Integer recId)throws BizException{
    	Map<String,Object> result = new HashMap<String,Object>();
        Map task = taskInfoMapper.getTaskInfoByRecId(recId);//存在任务
        if(null!= task){
			DataVo taskVo = new DataVo(task);
        	Map taskMap = taskInfoService.getTaskInfos(taskVo.getInt("taskId"));
        	result.put("handleType","1");
        	result.put("detail", taskMap);
        	DataVo taskMapVo = new DataVo(taskMap);
        	result.put("status", taskMapVo.getString("taskStatus"));
        }else{
        	Map temp = new HashMap();
        	Map warnMap = warningMapper.getById(recId);
        	if(null!=warnMap){
        		DataVo tempMap = new DataVo(warnMap);
        		if(tempMap.isNotBlank("handleMethod")&& tempMap.isNotBlank("userId")){
        			String handleMethod = tempMap.getString("handleMethod");
        			if(MonitorConstants.UserType.FIELD.getType().equals(handleMethod)){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
        				Map fUser = fieldUserMapper.getById(tempMap.getInt("userId"));
        				warnMap.put("userName", fUser.get("wqConsName"));
        			}else if(MonitorConstants.UserType.USER.getType().equals(handleMethod)){
        				DataVo userVo = userMapper.getUserById(tempMap.getInt("userId"));
        				warnMap.put("userName", userVo.getString("loginName"));
        			}
        		}else{
        			warnMap.put("userName", "");
        		}
        		result.put("status", tempMap.getString("taskStatus"));
        	}
        	temp.putAll(warnMap);
        	List<Map> alertMap = alertMapper.getAlertByRecId(recId);
        	if(null!=alertMap && alertMap.size()>0){
        		List<Map> artifactList = new ArrayList<Map>();
        		for(Map alert:alertMap){
        			artifactList.add(alert);
        		}
        		temp.put("artifactArrs", artifactList);
        	}
        	
        	result.put("detail", temp);
        	result.put("handleType","2");
        }
        return result;
    }
    /**
     * 分页查询告警信息
     * @param map
     * @return
     * @throws BizException
     * 2017年4月20日
     * 2.0.0
     * gaohui
     */
    @SuppressWarnings("unchecked")
	public PageInfo getWarnings(Map map)throws BizException{
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
		if(params.isBlank("sort")){
			params.put("sort", "this_.data_time");
			params.put("order","DESC");
		}
		if(params.isNotBlank("startTime")){
			params.put("warnStartTime", params.getString("startTime")+" 00:00:00");
		}
		if(params.isNotBlank("endTime")){
			params.put("warnEndTime", params.getString("endTime")+" 23:59:59");
		}
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(map);
		}
		List<DataVo> result = warningMapper.get(params);
		buildList(result,false);
		if(null!=result&&result.size()>0){
			pageInfo = new PageInfo(result);
		}
		result = null;
		return pageInfo;
    }
    
   
    @SuppressWarnings("unchecked")
	private void buildList(List<DataVo> result, boolean flag)throws BizException{
    	Set<Integer> fUserIdSet = new HashSet<Integer>();
		Set<Integer> userIdSet = new HashSet<Integer>();
		Set<Integer> recIdSet = new HashSet<Integer>();
		Map<Integer, String> userMaps  = null;
		Map<Integer, String> fuserMaps = null;
		Map<Integer, String> recIdMaps = null;
		List<ComboxVo> pileTypes = null;
		List<ComboxVo> pileModelIds = null;
		List<ComboxVo> objTypes = null;
		List<ComboxVo> alrNos = null;
		List<ComboxVo> handleFlags = null;
		List<ComboxVo> statuss = null;
		if(null!=result && result.size()>0){
			if(flag){
				pileTypes = dictService.getDictByType("cdzlx");//设备型号
				pileModelIds = dictService.getDictByType("sbxh");//设备型号
				objTypes = dictService.getDictByType("dxlx");//告警对象
				alrNos = dictService.getDictByType("gjx");//告警项
				handleFlags = dictService.getDictByType("gjclzt");//告警项
				statuss = dictService.getDictByType("sbzt");//告警项
			}
			for(DataVo alarm:result){
				String handleMethod = alarm.getString("handleMethod");
				if(alarm.isNotBlank("handleMethod") && alarm.isNotBlank("userId")){
					if(MonitorConstants.UserType.FIELD.getType().equals(handleMethod)){
						fUserIdSet.add(alarm.getInt("userId"));//外勤用户Id
					}else if(MonitorConstants.UserType.USER.getType().equals(handleMethod)){
						userIdSet.add(alarm.getInt("userId"));//平台用户Id
					}
				}
				recIdSet.add(alarm.getInt("recId"));//告警Id
				alarm.put("status",csg.getCDZStatus(alarm.getString("pileAddr")));
			}
		}
		if(null!=fUserIdSet && fUserIdSet.size()>0){
			Map  fUserMap = new HashMap();
			fUserMap.put("wqConsIds", fUserIdSet);
			List<Map> list = (List<Map>) fieldUserMapper.queryAllFieldUser(fUserMap);
			if(null!=list && list.size()>0){
				fuserMaps = new HashMap<Integer, String>();
				for(Map fuser:list){
					DataVo fuserVo = new DataVo(fuser);
					fuserMaps.put(fuserVo.getInt("wqConsId"),fuserVo.getString("wqConsName"));
				}
			}
		}
		if(null!=userIdSet && userIdSet.size()>0){
			Map  userMap = new HashMap();
			userMap.put("userIds", userIdSet);
			List<Map> list = (List<Map>) userMapper.getUsersByIds(userMap);
			if(null!=list && list.size()>0){
				userMaps = new HashMap<Integer, String>();
				for(Map user:list){
					DataVo userVo = new DataVo(user);
					if(userVo.isNotBlank("loginName")){
						userMaps.put(userVo.getInt("id"),userVo.getString("loginName"));
					}
				}
			}
		}
		if(null!=recIdSet && recIdSet.size()>0){
			Map  recIdMap = new HashMap();
			recIdMap.put("recIds", recIdSet);
			List<DataVo> list = taskInfoMapper.getTaskInfoByRedIds(recIdMap);
			if(null!=list && list.size()>0){
				recIdMaps = new HashMap<Integer, String>();
				for(DataVo recVo : list){
					int recId = recVo.getInt("recId");
					String taskOvertime = recVo.getString("taskOvertime");
					if(recVo.isNotBlank("taskOvertime")){
						recIdMap.put(recId,taskOvertime);
					}
				}
			}
		}
		if(null!=result && result.size()>0){
			for(DataVo tempMap : result){
				int userId = tempMap.getInt("userId");
				int recId = tempMap.getInt("recId");
				String handleMethod = tempMap.getString("handleMethod");
				if(tempMap.isNotBlank("handleMethod")&&tempMap.isNotBlank("userId")){
					if(MonitorConstants.UserType.FIELD.getType().equals(handleMethod)){
						if(null!=fuserMaps){
							tempMap.put("userName", fuserMaps.get(userId));
						}else{
							tempMap.put("userName", "");
						}
					}else if(MonitorConstants.UserType.USER.getType().equals(handleMethod)){
						if(null!=userMaps){
							tempMap.put("userName", userMaps.get(userId));
						}else{
							tempMap.put("userName", "");
						}
					}
				}else{
					tempMap.put("userName", "");
				}
				if(null!=recIdMaps){
					tempMap.put("taskOvertime", recIdMaps.get(recId));
				}else{
					tempMap.put("taskOvertime", "");
				}
				if(flag){
					if(tempMap.isNotBlank("pileType")){
						for(ComboxVo comboxVo:pileTypes){
							if(tempMap.getString("pileType").equals(comboxVo.getId())){
								tempMap.put("pileType", comboxVo.getText());
								continue;
							}
						}
					}
					if(tempMap.isNotBlank("pileModelId")){
						for(ComboxVo comboxVo:pileModelIds){
							if(tempMap.getString("pileModelId").equals(comboxVo.getId())){
								tempMap.put("pileModelId", comboxVo.getText());
								continue;
							}
						}
					}
					if(tempMap.isNotBlank("objType")){
						for(ComboxVo comboxVo:objTypes){
							if(tempMap.getString("objType").equals(comboxVo.getId())){
								tempMap.put("objType", comboxVo.getText());
								continue;
							}
						}
					}
					if(tempMap.isNotBlank("alrNo")){
						for(ComboxVo comboxVo:alrNos){
							if(tempMap.getString("alrNo").equals(comboxVo.getId())){
								tempMap.put("alrNo", comboxVo.getText());
								continue;
							}
						}
					}
					if(tempMap.isNotBlank("handleFlag")){
						for(ComboxVo comboxVo:handleFlags){
							if(tempMap.getString("handleFlag").equals(comboxVo.getId())){
								tempMap.put("handleFlag", comboxVo.getText());
								continue;
							}
						}
					}
					if(tempMap.isNotBlank("status")){
						for(ComboxVo comboxVo:statuss){
							if(tempMap.getString("status").equals(comboxVo.getId())){
								tempMap.put("status", comboxVo.getText());
								continue;
							}
						}
					}
				}
			}
		}
		fUserIdSet = null;
		userIdSet = null;
		recIdSet = null;
		userMaps  = null;
		fuserMaps = null;
		recIdMaps = null;
		pileTypes = null;
		pileModelIds = null;
		objTypes = null;
		alrNos = null;
		handleFlags = null;
		statuss = null;
    }
    /**
	 * 告警派单操作
	 * 2017年4月25日
	 * gaohui
	 * 2.0.0
	 */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor=Exception.class)
	public void dispatch(Map map)throws BizException{
    	DataVo params = new DataVo(map);
    	if (params.isBlank("orgId")) {
			throw new BizException(1803000,"运营商");
		}
		if (params.isBlank("stationId")) {
			throw new BizException(1803000,"场站");
		}
		if (params.isBlank("wqConsId")) {
			throw new BizException(1803000,"外勤人员");
		}
		if (params.isBlank("taskLevel")) {
			throw new BizException(1803000,"任务级别");
		}
		if (params.isBlank("patternId")) {
			throw new BizException(1803000,"模板");
		}
		if (params.isBlank("taskStartHour")) {
			throw new BizException(1803000,"任务开始时间");
		}
		if (params.isBlank("taskEndHour")) {
			throw new BizException(1803000,"任务结束时间");
		}
		if (params.isBlank("recId")) {
			throw new BizException(1803000,"告警id");
		}
		if (params.isBlank("pileId")) {
			throw new BizException(1803000,"设备id");
		}
		params.put("taskStatus", MonitorConstants.WTStatus.DS.getStatus());//已派单
		params.put("taskFreq", MonitorConstants.TaskFreq.SINGLE.getType());//默认为单次任务0
		params.put("taskType", MonitorConstants.TaskType.WARN.getType());//任务类型为告警任务
		params.put("taskDate", ((String)map.get("taskStartHour")).substring(10));
		taskInfoMapper.insert(params);//派单任务添加
		params.put("handleFlag", MonitorConstants.WTStatus.DS.getStatus());//已派单
		params.put("handleMethod", MonitorConstants.UserType.FIELD.getType());
		params.put("userId", params.getInt("wqConsId"));
    	warningMapper.update(params);//更新告警信息(状态)
    }
    /**
	 * 告警再次派单操作
	 * 2017年4月25日
	 * gaohui
	 * 2.0.0
	 */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor=Exception.class)
	public void dispatchAgain(Map map)throws BizException{
    	DataVo params = new DataVo(map);
    	params.put("handleFlag", MonitorConstants.WTStatus.ADS.getStatus());//再次派单标识
    	warningMapper.update(params);//更新作业信息(状态)
    	params.put("taskAgain", MonitorConstants.TASKAGAIN);
    	params.put("taskStatus", MonitorConstants.WTStatus.ADS.getStatus());
    	params.put("taskAgainTime", new Date());
    	taskInfoMapper.updateTaskInfoByRecId(params);//更新作业信息(再次派单标识、再次派单时间、状态)
    }
    
    /**
	 * 告警确认处理操作
	 * 2017年4月25日
	 * gaohui
	 * 2.0.0
	 */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor=Exception.class)
	public void handleConfirm(Map map)throws BizException{
    	DataVo params = new DataVo(map);
    	params.put("handleFlag", MonitorConstants.WTStatus.ES.getStatus());//再次派单标识
    	warningMapper.update(params);//更新告警信息(状态)
    	params.put("taskStatus", MonitorConstants.WTStatus.ES.getStatus());
    	taskInfoMapper.updateTaskInfoByRecId(params);//更新作业信息(状态)
    }
    /**
	 * 获取告警项列表
	 * 2017年3月25日
	 * gaohui
	 * 2.0.0
	 */
    public List<Map> getAlarmItems(){
    	return warningMapper.getAlarmItems(MonitorConstants.PARENTID);
    }
    /**
	 * 告警项设置
	 * 2017年3月25日
	 * gaohui
	 * 2.0.0
	 */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor=Exception.class)
	public void modifyAlarmItems(Map map){
    	if(null!=map.get("items")){
    		List<Map> items = (List<Map>)map.get("items");
    		if(items!=null && items.size()>0){
    			for(Map item :items){
    				warningMapper.modifyAlarmItem(item);
    			}
    		}
    	}
    }
    /**
	 * 告警导出
	 * 2017年3月25日
	 * gaohui
	 * 2.0.0
     * @throws Exception 
	 */
    @SuppressWarnings("unchecked")
	public void exportAlarms(Map map,HttpServletResponse response) throws Exception{
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
		if(params.isBlank("sort")){
			params.put("sort", "this_.data_time");
			params.put("order","DESC");
		}
//		if (params.isBlank("pageNum") && params.isNotBlank("pageSize")) {
//            PageHelper.startPage(map);
//        }
    	List<DataVo> result = warningMapper.get(params);
    	buildList(result, true);
    	String header = "告警列表";
    	List<String> headers = new ArrayList<String>();//
    	headers.add("告警时间");
    	headers.add("运营商");
    	headers.add("场站名称");
    	headers.add("设备编号");
    	headers.add("设备名称");
    	headers.add("设备类型");
    	headers.add("设备型号");
    	headers.add("设备状态");
    	headers.add("告警对象");
    	headers.add("告警项");
    	headers.add("告警详情");
    	headers.add("告警次数");
    	headers.add("处理状态");
    	headers.add("处理时间");
    	headers.add("处理人");
    	headers.add("处理描述");
    	List<String> titleHeaders = new ArrayList<String>();//
    	titleHeaders.add("dataTime");
    	titleHeaders.add("orgName");
    	titleHeaders.add("stationName");
    	titleHeaders.add("pileNo");
    	titleHeaders.add("pileName");
    	titleHeaders.add("pileType");
    	titleHeaders.add("pileModelId");
    	titleHeaders.add("status");
    	titleHeaders.add("objType");
    	titleHeaders.add("alrNo");
    	titleHeaders.add("alrDesc");
    	titleHeaders.add("alrCount");
    	titleHeaders.add("handleFlag");
    	titleHeaders.add("handleTime");
    	titleHeaders.add("userName");
    	titleHeaders.add("handleDesc");
    	ExportUtils.exportExcel(result,response,headers,titleHeaders,header);
    }
    
    
    
    
	public void updateWarningByRecId(Map map)throws BizException{
		warningMapper.updateWarningByRecId(map);
	}
	
	/**
	 * 根据recId查询告警信息
	 * @param map
	 * @return
	 * 2017年4月1日
	 * gaohui
	 */
    public Map findWarningByRecId(Map map)throws BizException{
    	return warningMapper.findWarningByRecId(map);
    }
    /**
     * 分页查询告警信息
     * @param map
     * @return
     * @throws BizException
     * 2017年4月11日
     * gaohui
     */
    public List<Map> findWarningListByPage(Map map)throws BizException{
    	return warningMapper.findWarningListByPage(map);
    }
    /**
     * 查询告警条数
     * @param map
     * @return
     * @throws BizException
     * 2017年4月11日
     * gaohui
     */
    public Map findWarningCounts(Map map)throws BizException{
    	return warningMapper.findWarningCounts(map);
    }
}

package com.clouyun.charge.modules.monitor.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.mapper.VehicleStatusMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 
 * 描述: VehicleStatusService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年8月14日
 */
@Service
@SuppressWarnings("rawtypes")
public class VehicleStatusService {
	
	@Autowired
	private VehicleStatusMapper vehicleStatusMapper;
	
	public PageInfo getVehicleMonitor(Map map) throws BizException{
		DataVo vo = new DataVo(map); 
		if(vo.isBlank("userId")){
			throw new BizException(112001, "用户");
		}
		if(vo.isNotBlank("vehicleStatus")){
			Set<Integer> carIds = new HashSet<Integer>();
			DataVo ssVo = new DataVo();
			ssVo.put("vehicleStatus", vo.getInt("vehicleStatus"));
			List ids = vehicleStatusMapper.getVehicleStatus(ssVo);
			if(ids!=null && ids.size()>0){
				for (int i = 0; i < ids.size(); i++) {
					Map carMap = (Map) ids.get(i);
					carIds.add(Integer.valueOf(carMap.get("vehicleId").toString()));
				}
				vo.put("vehicleIds", carIds);
			}
		}
		if (vo.isBlank("pageNum") && vo.isNotBlank("pageSize")) {
            PageHelper.startPage(vo);
        }
		List list = vehicleStatusMapper.getVehicleMonitor(vo);
		
		//获取车辆实时状态
		List vehicleStatusList = vehicleStatusMapper.getAllVehicleStatus();
		Map<Integer, Map> vehicleStatusMap = new HashMap<Integer, Map>();
		if(vehicleStatusList != null && vehicleStatusList.size()>0){
			for (int i = 0; i < vehicleStatusList.size(); i++) {
				Map statusMap = (Map) vehicleStatusList.get(i);
				Integer vehicleId = Integer.valueOf(statusMap.get("vehicleId").toString());
				vehicleStatusMap.put(vehicleId, statusMap);
			}
		}
		if(list != null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Map vehicleMap = (Map) list.get(i);
				Integer vehicleId = Integer.valueOf(vehicleMap.get("vehicleId").toString());
				Map statusMap = vehicleStatusMap.get(vehicleId);
				if(statusMap != null){
					vehicleMap.put("GPSStatus","有效");
					if(statusMap.get("vehicleStatus") != null){
						Integer vehicleStatus = Integer.valueOf(statusMap.get("vehicleStatus").toString());
						vehicleMap.put("vehicleStatus",vehicleStatus == 0?"停止":vehicleStatus == 1?"行驶":"充电");
					}
					vehicleMap.put("vehicleSoc",statusMap.get("vehicleSoc")!=null?Double.valueOf(statusMap.get("vehicleSoc").toString()):0d);
					vehicleMap.put("mileage",statusMap.get("mileages")!=null?Double.valueOf(statusMap.get("mileages").toString()):0d);
					if(statusMap.get("timetag")!=null){
						vehicleMap.put("timeTag",statusMap.get("timetag").toString());	
					}
				}else{
					vehicleMap.put("GPSStatus", "无效");
					vehicleMap.put("vehicleStatus", "离线");
				}
			}
		}
		return new PageInfo(list);
	}
	
}

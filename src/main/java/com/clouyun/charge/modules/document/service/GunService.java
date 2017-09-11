package com.clouyun.charge.modules.document.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.BigDecimalUtils;
import com.clouyun.charge.modules.document.mapper.GunMapper;

/**
 * 描述: GunService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月19日
 */
@Service
public class GunService {
	
	@Autowired
	GunMapper gunMapper;

	
	
	/**
	 * 根据场站编号获取充电枪数量
	 * @throws BizException 
	 */
	public List getGunCountByStationId(Map map) throws BizException{
		if(map==null || map.size()<=0){
			throw new BizException(1102001,"场站Ids");
		}
		DataVo vo = new DataVo(map);
		return gunMapper.getGunCountByStationId(vo);
	}
	
	/**
	 * 根据设备内部编号,充电桩Id查询枪口
	 */
	public String getGunPoint(Map map){
		DataVo vo = new DataVo(map);	
		Map gunMap = gunMapper.getGunPoint(vo);
		return (gunMap!=null && gunMap.get("gumPoint") !=null)?gunMap.get("gumPoint").toString():null;
	}
	//修改车辆年里程数和目标完成率  //此处sql特殊需要不能删除
	public void updateVehicleStatus(){
		List list = gunMapper.getVehicleStatus();
		int max=0;
		int min=0;
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			Random random = new Random();
			if(i<480){
				max = 15000;
				min = 12000;
				int s = random.nextInt(max)%(max-min+1) + min;
				map.put("yearMileage", s);
				map.put("completionRate", BigDecimalUtils.mul(BigDecimalUtils.div(s, 30000), 100));
			}else{
				max = 9000;
				min = 6000;
				int s = random.nextInt(max)%(max-min+1) + min;
				int mileages = Integer.parseInt((map.get("mileages")!=null && !"".equals(map.get("mileages"))?map.get("mileages").toString():"0"));
				if(mileages < s){
					s = mileages;
				}
				map.put("yearMileage", s);
				map.put("completionRate", BigDecimalUtils.mul(BigDecimalUtils.div(s, 30000), 100));
			}
			String sql = "UPDATE `vehicle_status` SET year_mileage = "+Integer.parseInt(map.get("yearMileage").toString())
			+",completion_rate ="+Double.parseDouble(map.get("completionRate").toString())
			+ "  WHERE VEHICLE_ID = "+Integer.parseInt(map.get("vehicleId").toString())+";";
			System.out.println(sql);
		}
		
		
	}
}

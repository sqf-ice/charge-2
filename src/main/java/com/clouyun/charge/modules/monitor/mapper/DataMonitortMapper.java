package com.clouyun.charge.modules.monitor.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author fft
 *
 */
@Mapper
public interface DataMonitortMapper {
	
	//通过桩iD查询订单电量和收入
	Map getpileInfoByPileId(Map map);
	

}

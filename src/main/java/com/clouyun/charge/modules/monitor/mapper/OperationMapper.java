package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
/**
 * 
 * @author fuft
 *
 */
@Mapper
public interface OperationMapper {
	
	//获取场站建设情况
	List<Integer> getStationConstruction(Map map);
	
	//用户情况(app充电用户)
	List<Integer> getCcons(Map map);
	
	//收入情况(收入排行)
	List<Map> getIncomeInfo(Map map);
	
	//累计收入
	Double getIncomeCount(Map map);
	
	//充电服务和 月统计
	List<Double> getInitData(Map map);
	
	//各省月收入统计
	List<Map> getMoneyByMonth(Map map);
	
	//[0]：上个月总收入 [1]：上上个月总收入
	List<Double> getAountByMonth(Map map);
	
	//获取运营中所有场站的当月每日充电量及充电收入和充电量
	List<Map> getPowerAndMoney(Map map);
	
	//获取运营中所有场站的当月每日有充电充电记录日期
	List<String> getDate(Map map);
	
	//通过stationId查找出所有的充电桩地址
	Set<String> getPilesByStationId(Map map);
	
	//通过stationId查找告警
	Integer getWarmByStationId(Map map);
	
	//通过stationId查找损耗
	Map getLossByStationId(Map map);
	
	//通过stationId获取充电桩信息
	List<Map> getPileNamesByStationId(Map map);
	
	//通过stationId获取充电桩损耗
	List<Map> getPileLossByStationId(Map map);
	
	//通过stationId获取当月充电装收入排行
	List<Map> getPileIncomeByStationId(Map map);
	
	//通过stationId获取当月充电装收入排行
	Map getTotalByStationId(Map map);
	
	//通过stationId获取当月服务次数
	Integer getCountByStationId(Map map);
	
	//通过stationId获取当月服务次数
	List<Map> getFcsrByStationId(Map map);
	
	//获取运营中所有场站的当月每日充电量及充电收入和充电量
	List<Map> getPowerAndMoneyByStationId(Map map);
	
	//获取运营中所有场站的当月每日有充电充电记录日期
	List<String> getDateByStationId(Map map);
	
	//获取运营中所有场站的充电时长
	Integer getAllTimes(Map map);
	
	//获取运营中公交，公共，物流场站的充电时长
	List<Map> getTimesList(Map map);
	
	//获取运营中公交，公共，物流场站的充电时长
	Integer getGunCount(Map map);
}


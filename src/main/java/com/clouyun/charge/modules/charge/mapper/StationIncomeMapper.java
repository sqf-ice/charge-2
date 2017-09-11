package com.clouyun.charge.modules.charge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: StationIncomeMapper 
 * 场站收入 版权: Copyright (c) 2017
 * 公司: 科陆电子 
 * 作者: sim.y 
 * 版本:2.0 
 * 创建日期: 2017年9月2日
 */
@Mapper
public interface StationIncomeMapper {

	/**
	 * 场站收入 按时间段 显示场站信息汇总
	 */
	List<DataVo> getStationIncome(DataVo vo);

	/**
	 * 场站收入 按时间段 显示场站信息汇总[合计]
	 */
	DataVo getStationIncomeTotal(DataVo vo);

	/**
	 * 场站收入详情 显示场站每日数据汇总
	 */
	List<DataVo> getStationByDayIncome(DataVo vo);

	/**
	 * 查询时间段内场站的订单数据
	 */
	List<DataVo> getBillPayByStation(DataVo vo);

	/**
	 * 查询订单中的SOC值
	 */
	List<DataVo> getBillPaySOC(DataVo vo);
}

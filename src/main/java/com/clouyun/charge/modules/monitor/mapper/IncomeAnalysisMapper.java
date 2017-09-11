package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: 运营总览收入分析接口类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: fft
 * 版本: 2.0.0
 * 创建日期: 2017年6月21日
 */
@Mapper
public interface IncomeAnalysisMapper {
	
	//收入指标信息
	DataVo getIncomeInfo(Map map);
	
	//充值信息
	Double getReCharge(Map map);

	//查询单企业的收入支付方式
	List<Map> getPayType(Map map);

	/**
	 * 得到日充电收入
	 * @param dayVo
	 * @return
	 */
    List<DataVo> getChargeDayChgPower(DataVo dayVo);

	/**
	 * 日收入
	 * @param dayVo
	 * @return
	 */
	List<DataVo> getChargeDayAmount(DataVo dayVo);

	/**
	 * 月和年充电量
	 * @param dataMap
	 * @return
	 */
    List<DataVo> getChargeMonthOrYear(DataVo dataMap);

	/**
	 * 运营商收入排行
	 * @param dayVo
	 * @return
	 */
	List<DataVo> getOrgAmount(DataVo dayVo);
	
	/**
	 * 按时获取收入曲线(收入、充电量)
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月29日
	 */
	List<DataVo> getIncomeByHour(Map map);
	/**
	 * 按日获取收入曲线(收入、充电量)
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月29日
	 */
	List<DataVo> getIncomeByDay(Map map);
	/**
	 * 按月获取收入曲线(收入、充电量)
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月29日
	 */
	List<DataVo> getIncomeByMonth(Map map);
	/**
	 * 个人会员
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月30日
	 */
	List<DataVo> getIncomeCons(Map map);
	/**
	 * 合约
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月30日
	 */
	List<DataVo> getIncomeContract(Map map);
	/**
	 * 集团月结
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月30日
	 */
	List<DataVo> getIncomeCharge(Map map);
	/**
	 * 属于月结的订单
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月27日
	 */
	List<DataVo> getMonthPay(Map map);
	/**
	 * 会员合约关系
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月27日
	 */
	List<DataVo> getConsContract();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月28日
	 */
	List<DataVo> getContractPile();
	/**
	 * 
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年7月28日
	 */
	List<DataVo> getContract();
}

package com.clouyun.charge.modules.charge.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 描述: 场站利润
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月22日 下午7:04:18
 */
@Mapper
public interface StationProfitTaskMapper {
	
	// 批量添加
	void batchInser(List<Map> list);
	
	List<Map> queryBillPay(Map map);
	
	List<Map> queryBusinessChgPower(Map map);
	//场站设备用电量
	List<Map> queryStationPileChg(Map map);
	
	List<Map> queryStation();
	
	List<Map> queryPile();
	
	List<Map> queryLastMonthProfit(String settlementMonth);
	
	List<Map> queryPileCollData(Map map);
	
	List<Map> queryContract(Map map);

	List<Map> queryDailyIncome(Map map);
	
	List<Map> queryStationProfitCache(@Param(value = "settlementMonth") String settlementMonth);
	
	List<Map> queryGroupMonthlyCache(Map map);

	Integer queryStationProfitCount(String month);

	void deleteStationProfit(String month);

	List<DataVo> queryContractCache(@Param(value = "settlementMonth") String settlementMonth);
}
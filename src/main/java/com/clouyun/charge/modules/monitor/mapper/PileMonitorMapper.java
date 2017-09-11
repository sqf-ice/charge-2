package com.clouyun.charge.modules.monitor.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author fft
 *
 */
@Mapper
public interface PileMonitorMapper {
	//获取收入,用电量,充电量
	Map getStationInfoByStationId(Map map);
	
	//获取场站下所有的枪数
	Integer guncountByStationId(Integer stationId);
	
	//根据场站Id查询所有的充电桩信息
	List<Map> getPilesByStationId(Map map);
	
	//通过会员ID查询查询车辆信息
	List<Map> vehiclesByConsId(Integer consId);
	
	//通过卡id查询会员id信息
	Map consIdByCardId(String cardId);
	
	//通过桩id查询电表信息
	Map chgMeterBypileId(Integer pileId);
	
	//查询场站信息
	List<DataVo> getStationInfos(DataVo vo);
	
	//查询场站的充电桩信息
	List<DataVo> getPiles(DataVo vo);
	
	//查询第三方场站信息
	List<DataVo> getToStationInfos(DataVo vo);
	
	//查询第三方场站每个站的桩数量
	List<DataVo> getToPileCountByStationId(DataVo vo);
	
	//查询第三方场站的充电桩信息
	List<DataVo> getToPilesByStationId(DataVo vo);
	
	//查询第三方场站的枪信息
	List<DataVo> getToConnectorinfosByPileId(String pileId);

	/**
	 * 统计订单月度总金额和月度总电量
	 *
	 * @param vo
	 * @return
	 */
	DataVo getMonthTotalPayMoneyPower(DataVo vo);

	/**
	 * 统计今天总收入
	 * @param vo
	 * @return
	 */
	Double getTotalPayMoneyToday(DataVo vo);

}

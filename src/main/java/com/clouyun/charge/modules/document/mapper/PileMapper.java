package com.clouyun.charge.modules.document.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述: PileMapper 充电桩管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月19日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface PileMapper {
	
	/**
	 *  查询充电桩列表数据
	 */
	List getPileAll(DataVo vo);
	
	
	/**
	 * 根据场站获取充电桩数量
	 */
	List getPileCountByStationId(DataVo vo);
	
	/**
	 * 根据场站获取有效充电桩数量
	 */
	List getUsePileCountByStationId(DataVo vo);
	
	/**
	 * 充电桩置为无效
	 */
	void dissPile(DataVo vo);
	
	/**
	 * 根据充电桩Id获取充电桩信息
	 */
	Map getPileById(DataVo vo);
	
	/**
	 * 根据充电桩Id查询表计信息
	 */
	List getMeterInfo(DataVo vo);
	
	/**
	 * 查询充电桩业务字典
	 */
	List getPile(DataVo vo);
	/**
	 * 查询第三方充电桩列表数据
	 */
	List getToEquipmentinfoAll(DataVo vo);
	
	/**
	 * 根据第三方充电桩id查询第三方充电桩信息
	 */
	Map getToEquipmentinById(DataVo vo);
	
	/**
	 * 根据设备编码equipmentId查询充电设备接口信息
	 */
	List getToConnectorinfoById(DataVo vo);
	/**
	 * 新增充电桩
	 * author:gaohui
	 * 2.0.0
	 * 2017-05-02
	 */
	void insertPile(DataVo vo);
	/**
	 * 新增充电桩 表计
	 * author:gaohui
	 * 2.0.0
	 * 2017-05-04
	 */
	void insertMeter(DataVo vo);
	/**
	 * 根据pileNo或pileAddr是否有重复
	 * author:gaohui
	 * 2.0.0
	 * 2017-05-04
	 */
	int getPileExistByVar(Map map);
	/**
	 * 根据充电桩id删除表计信息
	 * author:gaohui
	 * 2.0.0
	 * 2017-05-05
	 */
	void deleteMeterByPileId(Map map);
	/**
	 * 编辑充电桩信息
	 * author:gaohui
	 * 2.0.0
	 * 2017-05-05
	 */
	void modifyPileByPileId(Map map);
	/**
	 * 根据pileNames|pileNos|pileAddrs 查询充电桩列表
	 * author:gaohui
	 * 2.0.0
	 * 2017-05-05
	 */
	List<Map> getPilesByVars(Map map);
	/**
	 * 批量新增充电桩(主要针对Excel批量导入)
	 * author:gaohui
	 * 2.0.0
	 * 2017-05-09
	 */
	void insertPiles(Map map);
	/**
	 * 批量更新充电桩
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月27日
	 */
	void batchUpdatePiles(Map map);
	/**
	 * 根据桩id获取桩和场站信息.无关企业
	 */
	Map queryPileStationInfo(Map map);
	
	/**
	 * 设备名称或设备编号业务字典
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月17日
	 */
	List<Map> getPileNoAndNameDict(Map map);
	
	/**
	 * 检查桩名称重复
	 */
	int checkPileName(DataVo vo);
	/**
	 * 检查枪二维码是否重复
	 */
	int checkQrCode(String qrCode);
}

package com.clouyun.charge.modules.document.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: ConTractMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月24日
 */
@Mapper
public interface ConTractMapper {

	/**
	 * 查询合约列表数据
	 */
	List getConTractAll(DataVo vo);
	/**
	 * 根据stationIds/tractId 查询月结合约关联表信息
	 */
	List getConTractStationRelaByTractId(DataVo vo);
	/**
	 * 通过tractIds查询CConTract月结信息
	 */
	List getConTractByStation(DataVo vo);
	
	/**
	 * 根据合约id查询合约信息
	 */
	Map getConTractById(DataVo vo);
	/**
	 * 根据合约id将合约置为无效
	 */
	void dissConTract(DataVo vo);
	
	/**
	 * 根据合约id获取合约关联信息
	 */
	List getConTractCompanyById(DataVo vo);
	
	/**
	 * 新增合约信息
	 */
	void saveConTract(Map vo);
	
	/**
	 * 新增合约场站关联信息
	 */
	void saveTractStation(DataVo vo);
	/**
	 * 删除合约场站关联信息
	 */
	void delTractStation(DataVo vo);
	/**
	 * 新增合约关联表信息
	 */
	void saveInTractCompany(DataVo vo);
	/**
	 * 删除合约关联表信息
	 */
	void delInTractCompany(DataVo vo);
	/**
	 * 编辑合约信息
	 */
	void updateConTract(Map vo);
	
	/**
	 * 检查收入合约,支出合约重复性
	 */
	int checkIncomsTract(DataVo vo);
	
	/**
	 * 检查月结合约重复性
	 */
	int checkMonthlyTract(DataVo vo);
	
	/**
	 * 根据合约企业查询所签订的合约
	 */
	List getTractByCompanyId(Integer companyId);
	
	/**
	 * 合约审批
	 */
	void updateAppConTract(DataVo vo);
}

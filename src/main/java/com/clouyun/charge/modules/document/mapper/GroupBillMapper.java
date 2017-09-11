package com.clouyun.charge.modules.document.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述: GroupBillMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月26日
 */
@Mapper
public interface GroupBillMapper {
	/**
	 * 查询集团账单
	 */
	List getGroupBillAll(DataVo vo);
	
	/**
	 * 根据集团账单Id查询集团账单信息
	 */
	Map getGroupBillById(DataVo vo);
	/**
	 * 查询集团账单详情列表
	 */
	List getGroupBillInfos(DataVo vo);

	DataVo getGroupBillInfosTotal(DataVo vo);
}

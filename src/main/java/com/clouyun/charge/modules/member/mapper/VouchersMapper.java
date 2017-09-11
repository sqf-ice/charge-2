package com.clouyun.charge.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 描述: 优惠券
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月17日 下午7:11:52
 */
@Mapper
public interface VouchersMapper {

	//优惠券列表
	List<Map> queryVouchers(Map map);
	
	//查询优惠券详情
	List<Map> queryVoucherHistory(Map map);
	
	//新增优惠券
	int insertVouchers(Map map);
}

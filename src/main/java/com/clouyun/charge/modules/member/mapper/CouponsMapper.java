package com.clouyun.charge.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月3日 上午9:37:39
 */
@Mapper
public interface CouponsMapper {

	//新增优惠券
	int insertCoupons(Map map);
	
	//新增优惠券规则
	int insertCouponRules(Map map);
	
	//验证优惠券是否过期
	List<Map> queryCouponsCount(Map map);
	
	//查询优惠券
	List<Map> queryCoupons(Map map);
	
	//优惠券详情
	List<Map> queryCouponsInfo(Map map);
}

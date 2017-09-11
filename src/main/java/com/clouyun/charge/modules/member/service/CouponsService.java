package com.clouyun.charge.modules.member.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.charge.modules.member.mapper.CouponsMapper;
import com.clouyun.charge.modules.member.mapper.VouchersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: 优惠券
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月19日 下午4:08:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponsService {

	public static final Logger logger = LoggerFactory.getLogger(CouponsService.class);

	@Autowired
	CouponsMapper couponsMapper;
	
	public int insertCoupons(Map map) throws BizException{
		Integer userId = Integer.parseInt(map.get("userId").toString());
		Integer orgId = Integer.parseInt(map.get("orgId").toString());
//		Integer userId = 1;
//		Integer orgId = 24;
		//获取登录id
		map.put("userId",userId);
		//根据登录用户id获取orgId
		map.put("orgId",orgId);
		//groupId暂时没存值
		
		Calendar cal = Calendar.getInstance();
		String time = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);
		map.put("couponTime", time);//添加时间为当前时间
		//检查参数
		List<Map> couponsCount = checkData(map, orgId, time);
		if(couponsCount != null && couponsCount.size() > 0){
			//在优惠券到期前无法添加相同类型的优惠券
			throw new BizException(1203000,couponsCount.get(0).get("couponName"));
		}else{
			//新增优惠券
			map.put("userId", userId);
			//开始时间为0点
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			time = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);
			map.put("startTime", time);
			if(null != map.get("endSerial") && null != map.get("startSerial")){
				//结束编号减去开始编号获得数量
				Integer couponQty = Integer.parseInt(map.get("endSerial").toString()) - Integer.parseInt(map.get("startSerial").toString());
				map.put("couponQty", couponQty + 1);
			}
			if(null != map.get("endTime")){
				Calendar endTime = CalendarUtils.convertStrToCalendar(map.get("endTime").toString(), CalendarUtils.yyyyMMdd);
				endTime.add(Calendar.HOUR_OF_DAY, 23);
				endTime.add(Calendar.MINUTE, 59);
				endTime.add(Calendar.SECOND, 59);
				String endTimes = CalendarUtils.formatCalendar(endTime, CalendarUtils.yyyyMMddHHmmss);
				map.put("endTime", endTimes);
			}
			if(!"3".equals(map.get("couponType"))){
				//注册,分享的优惠券状态都是有效的
				map.put("status", 0);
			}
		}
		int count = couponsMapper.insertCoupons(map);
		
		return count;
	}

	private List<Map> checkData(Map map, Integer orgId, String time)
			throws BizException {
		if(map.get("couponName") == null){
			throw new BizException(1203001, "优惠券名称");
		}
		if(map.get("couponType") == null){
			throw new BizException(1203001, "优惠券方式");
		}
		
		//根据企业,优惠券类型,以及优惠券到期时间判断是否存在优惠券
		Map queryMap = new HashMap();
		queryMap.put("couponType", map.get("couponType"));
		queryMap.put("orgId", orgId);
		queryMap.put("endTime", time);
		List<Map> couponsCount = couponsMapper.queryCouponsCount(queryMap);
		return couponsCount;
	}
	
	public List<Map> queryCoupons(Map map){
		return couponsMapper.queryCoupons(map);
	}
	public List<Map> queryCouponsInfo(Map map){
		if(null != map.get("grantTimeBegin") && !"".equals(map.get("grantTimeBegin"))){
			Calendar begin = CalendarUtils.convertStrToCalendar(map.get("grantTimeBegin").toString(), CalendarUtils.yyyyMMdd);
			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.SECOND, 0);
			begin.set(Calendar.MINUTE, 0);
			map.put("grantTimeBegin", CalendarUtils.formatCalendar(begin, CalendarUtils.yyyyMMddHHmmss));
		}
		if(null != map.get("grantTimeEnd") && !"".equals(map.get("grantTimeEnd"))){
			Calendar begin = CalendarUtils.convertStrToCalendar(map.get("grantTimeEnd").toString(), CalendarUtils.yyyyMMdd);
			begin.set(Calendar.HOUR_OF_DAY, 23);
			begin.set(Calendar.SECOND, 59);
			begin.set(Calendar.MINUTE, 59);
			map.put("grantTimeEnd", CalendarUtils.formatCalendar(begin, CalendarUtils.yyyyMMddHHmmss));
		}
		return couponsMapper.queryCouponsInfo(map);
	}
}

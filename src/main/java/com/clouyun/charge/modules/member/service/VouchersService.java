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
public class VouchersService {

	public static final Logger logger = LoggerFactory.getLogger(VouchersService.class);

	@Autowired
	VouchersMapper vouchersMapper;
	
	//优惠券列表
	public PageInfo queryVouchers(Map map){
		DataVo params = new DataVo(map);
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
        //默认排序
        if(map.get("sort") == null){
        	map.put("sort", "operatingTime");
        	map.put("order", "desc");
        }
        
		List<Map> result = vouchersMapper.queryVouchers(map);
		PageInfo page = new PageInfo(result);
		return page;
	}
	
	//优惠券详情列表
	public PageInfo queryVoucherDetails(Map map){
		DataVo params = new DataVo(map);
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
		List<Map> voucherHistory = vouchersMapper.queryVoucherHistory(map);
		PageInfo page = new PageInfo(voucherHistory);
		return page;
	}
	
	public int insertVouchers(Map map) throws BizException{
		
		if(map.get("name") == null){
			throw new BizException(1203001, "优惠券名称");
		}
		
		Integer userId = 1;
		Integer orgId = 48;
		//获取登录id
		//map.put("userId",userId);
		//根据登录用户id获取orgId
		//map.put("orgId",orgId);
		//groupId暂时没存值
		
		Calendar cal = Calendar.getInstance();
		String time = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);
		//根据企业,优惠券类型,以及优惠券到期时间判断是否存在优惠券
		Map queryMap = new HashMap();
		queryMap.put("access", map.get("access"));
		queryMap.put("orgId", orgId);
		queryMap.put("endTime", time);
		List<Map> vouchers = vouchersMapper.queryVouchers(queryMap);
		if(vouchers != null && vouchers.size() > 0){
			//在优惠券到期前无法添加相同类型的优惠券
			throw new BizException(1203000,map.get("name"));
		}else{
			//新增优惠券
			map.put("userId", userId);
			map.put("strategy", "cash");//目前默认为现金优惠券
			map.put("startTime", time);
			map.put("operatingTime", time);
			Integer cnumber = Integer.parseInt(map.get("endSerial").toString()) - Integer.parseInt(map.get("startSerial").toString());
			map.put("cnumber", cnumber);
			if(null != map.get("endTime") && !"".equals(map.get("endTime"))){
				Calendar endTime = CalendarUtils.convertStrToCalendar(map.get("endTime").toString(), CalendarUtils.yyyyMMdd);
				endTime.add(Calendar.HOUR_OF_DAY, 23);
				endTime.add(Calendar.MINUTE, 59);
				endTime.add(Calendar.SECOND, 59);
				String endTimes = CalendarUtils.formatCalendar(endTime, CalendarUtils.yyyyMMddHHmmss);
				map.put("endTime", endTimes);
			}
		}
		int count = vouchersMapper.insertVouchers(map);
		
		return count;
	}
}

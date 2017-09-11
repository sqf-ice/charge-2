package com.clouyun.charge.modules.member.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.modules.member.mapper.CouponsMapper;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
public class CouponsNewService extends BusinessService{

	public static final Logger logger = LoggerFactory.getLogger(CouponsNewService.class);

	@Autowired
	CouponsMapper couponsMapper;
	@Autowired
	UserService userService;
	
	public int insertCoupons(Map map) throws BizException{
		DataVo params = new DataVo(map);
		Integer orgId = null;
		if (params.isBlank("userId")){
        	throw new BizException(1000006);
        }else{
        	//获取登录id
        	map.put("userId",params.getInt("userId"));
        }
		//根据登录用户id获取orgId
        if (!userService.isSuperMan(params.getInt("userId")) || !userService.isGlobalMan(params.getInt("userId"))){
        	orgId = userService.getOrgIdByUserId(params.getInt("userId"));
        	map.put("orgId", orgId);
        }

		Calendar cal = Calendar.getInstance();
		String time = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);
		//检查参数
		List<Map> couponsCount = checkData(map, orgId, time);
		if(couponsCount != null && couponsCount.size() > 0){
			//在优惠券到期前无法添加相同类型的优惠券
			throw new BizException(1203000,couponsCount.get(0).get("couponName"));
		}else{
			//新增优惠券
			//开始时间为0点
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			time = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);
			map.put("startTime", time);
			if(null != map.get("endSerial") && null != map.get("startSerial")){
				//结束编号减去开始编号获得数量再加1
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
				endTime = null;
			}
			if(!"3".equals(map.get("couponType"))){
				//注册,分享的优惠券状态都是有效的
				map.put("status", 0);
			}
		}

		saveLogs("新增", OperateType.add.OperateId, params);
		int count = couponsMapper.insertCoupons(map);

		map = null;
		params = null;
		cal = null;
		couponsCount = null;
		return count;
	}

	private List<Map> checkData(Map map, Integer orgId, String time)
			throws BizException {
		DataVo vo = new DataVo(map);
		if(vo.isBlank("couponName")){
			throw new BizException(1203001, "优惠券名称");
		}
		if(vo.isBlank("couponType")){
			throw new BizException(1203001, "优惠券方式");
		}
		if(vo.isNotBlank("startSerial") && !ValidateUtils.Number(vo.getString("startSerial"))){
			throw  new BizException(1000014,"开始编号");
		}
		if(vo.isNotBlank("endSerial") && !ValidateUtils.Number(vo.getString("endSerial"))){
			throw  new BizException(1000014,"结束编号");
		}
		if(vo.isNotBlank("giveMoney") && !ValidateUtils.Number(vo.getString("giveMoney"))){
			throw  new BizException(1000014,"面额");
		}
		
		//根据企业,优惠券类型,以及优惠券到期时间判断是否存在优惠券
		Map queryMap = new HashMap();
		queryMap.put("couponType", map.get("couponType"));
		if(orgId != null){
			queryMap.put("orgId", orgId);
		}
		queryMap.put("endTime", time);
		List<Map> couponsCount = couponsMapper.queryCouponsCount(queryMap);

		queryMap = null;
		return couponsCount;
	}
	
	public PageInfo queryCoupons(Map map) throws BizException{
		DataVo params = new DataVo(map);
		getPermission(params);
		Set<Integer> userRoleDataById;


		// 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(params);
        }
		List<Map> result = couponsMapper.queryCoupons(params);
		
		PageInfo page = new PageInfo(result);

		params = null;
		userRoleDataById = null;
		result = null;
		return page;
	}

	private void getPermission(DataVo params) throws BizException {
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId")){
            throw new BizException(1000006);
        }
		// 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(userRoleDataById != null && userRoleDataById.size() > 0){
            params.put("orgIds", userRoleDataById);
        }
	}

	public PageInfo queryCouponsInfo(Map map) throws BizException{
		DataVo params = new DataVo(map);
		if(params.isNotBlank("grantTimeBegin")){
			Calendar begin = CalendarUtils.convertStrToCalendar(params.getString("grantTimeBegin"), CalendarUtils.yyyyMMdd);
			begin.set(Calendar.HOUR_OF_DAY, 0);
			begin.set(Calendar.SECOND, 0);
			begin.set(Calendar.MINUTE, 0);
			map.put("grantTimeBegin", CalendarUtils.formatCalendar(begin, CalendarUtils.yyyyMMddHHmmss));
		}
		if(params.isNotBlank("grantTimeEnd")){
			Calendar begin = CalendarUtils.convertStrToCalendar(params.getString("grantTimeEnd"), CalendarUtils.yyyyMMdd);
			begin.set(Calendar.HOUR_OF_DAY, 23);
			begin.set(Calendar.SECOND, 59);
			begin.set(Calendar.MINUTE, 59);
			map.put("grantTimeEnd", CalendarUtils.formatCalendar(begin, CalendarUtils.yyyyMMddHHmmss));
		}
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
        if(params.isBlank("couponId")){
        	throw new BizException(1203001,"优惠券id");
        }
		List<Map> result = couponsMapper.queryCouponsInfo(map);
		
		PageInfo page = new PageInfo(result);

		map = null;
		params = null;
		result = null;
		return page;
	}

	private void saveLogs(String arr, String operateId, DataVo vo){
		saveLog(arr+"优惠券", operateId,"优惠券名称:"+vo.getString("couponNames"),vo.getInt("userId"));
	}
}

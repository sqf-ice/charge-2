package com.clouyun.charge.modules.member.service;

import java.util.Calendar;
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
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.charge.modules.member.mapper.IntegralMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 描述: 积分
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月21日 上午11:11:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IntegralService{

	public static final Logger logger = LoggerFactory.getLogger(IntegralService.class);

	@Autowired
	IntegralMapper integralMapper;
	
	public PageInfo queryIntegralHistoryInfo(Map map) throws BizException{
		DataVo params = new DataVo(map);
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }

        if(null == map.get("consId")){
        	throw new BizException(1202000,"会员id");
        }
        
        List<Map> result = integralMapper.queryIntegralHistoryInfo(map);
		
        //积分类型转义
        if(result != null && result.size() > 0){
        	for (Map map2 : result) {
				String type = getType(map2.get("integralName").toString());
				map2.put("integralName", type);
			}
        }
        
		PageInfo page = new PageInfo(result);
		return page;
	}

	/*
	 * 积分列表
	 */
	public List<Map> queryIntegralInfo(Map map)throws BizException{
		
		//权限根据orgId查询
		/*
		 * 1.如果是超管或者全局展示所有包括树
		 * 2.如果是总企业也展示树,根据企业id查询数据
		 */
		List<Map> info = integralMapper.queryIntegralInfo(map);
		
		for (Map map2 : info) {
			map2.put("isEdit", true);
		}
		//暂时不封装数据,给前台调用试试看
		/*Map<String,List<Map>> resultMap = new LinkedHashMap<>();
		Map<String,Map<String,List<Map>>> infoMap = new LinkedHashMap<>();
		if(info != null && info.size() > 0){
			for (Map map2 : info) {
				if(map2.get("orgname") != null){
					String orgName = map2.get("orgname").toString();
					if(resultMap.containsKey(orgName)){
						List<Map> list = resultMap.get(orgName);
						list.add(map2);
						resultMap.put(orgName, list);
					}else{
						List<Map> list = new ArrayList<>();
						list.add(map2);
						resultMap.put(orgName, list);
					}
				}
				infoMap.put("orgName", resultMap);
			}
		}*/
	
		
		return info;
	}
	
	public int updateIntegral(Map map) throws BizException{
		
		if(map.get("type") == null){
			throw new BizException(1202000,"积分类型");
		}
		//获取登陆用户
//		map.put("userId", userId);
		//根据登陆用户获取orgId
		Integer orgId = 24;
		map.put("orgId", orgId);
		//根据orgId和type唯一确定获取integralId
		List<Map> info = integralMapper.queryIntegralInfo(map);
		
		if(info == null || info.size() != 1){
			throw new BizException(1202001);
		}
		
		Integer integralId = Integer.parseInt(info.get(0).get("integralId").toString());
		
		map.put("integralId", integralId);
		
		Calendar cal = Calendar.getInstance();
		String operatingTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);
		map.put("operatingTime", operatingTime);
		
		int count = integralMapper.updateIntegral(map);
		return count;
	}
	/***
	 * 转换积分类型
	 * @param type
	 * @return
	 */
	private String getType(String type){
		if (type.equals("zc")) {
			return "注册";
		}else if (type.equals("fx")) {
			return "分享";
		}else if (type.equals("crz")) {
			return "车辆认证";
		}else if (type.equals("xf")) {
			return "消费";
		}else if (type.equals("info")) {
			return "完善个人信息";
		}
		return "未知";
	}
	
}

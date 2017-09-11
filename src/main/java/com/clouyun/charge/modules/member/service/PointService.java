package com.clouyun.charge.modules.member.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.modules.member.mapper.PointMapper;


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
public class PointService{

	public static final Logger logger = LoggerFactory.getLogger(PointService.class);

	@Autowired
	PointMapper pointMapper;
	
	
	public Map queryPubOrg(DataVo map){
		return pointMapper.queryPubOrg(map);
	}
	
	public List<Map> queryPoints(DataVo map){
		List<Map> points = pointMapper.queryPoints(map);
		
		Map<String,Map> fuckMap = new LinkedHashMap<>();
		List<Map> pointResult = new ArrayList<>();
		if(points != null && points.size() > 0){
			for (Map map2 : points) {
				if(fuckMap.size() > 0){
					if(fuckMap.containsKey(map2.get("orgName").toString())){
						Map object = (Map) fuckMap.get(map2.get("orgName").toString());
						object.put(map2.get("pointType").toString(), map2);
					}else{
						Map<String,Map> val = new HashMap<>();
						val.put(map2.get("pointType").toString(), map2);
						fuckMap.put(map2.get("orgName").toString(), val);
					}
				}else{
					Map<String,Map> val = new HashMap<>();
					val.put(map2.get("pointType").toString(), map2);
					fuckMap.put(map2.get("orgName").toString(), val);
				}
			}
			pointResult.add(fuckMap);
		}
		return pointResult;
	}
	
	public Map queryByType(DataVo map){
		return pointMapper.queryByType(map);
	}
	
	public int insertPoints(DataVo map){
		return pointMapper.insertPoints(map);
	}
	public int updatePoints(DataVo map){
		return pointMapper.updatePoints(map);
	}
}

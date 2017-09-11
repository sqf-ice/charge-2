package com.clouyun.charge.modules.monitor.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.charge.modules.monitor.mapper.PositionMapper;

/**
 * 位置信息
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月7日
 */
@Service
@SuppressWarnings("rawtypes")
public class PositionService {
	
	@Autowired
	private PositionMapper positionMapper;
	/**
	 * 根据taskId获取位置信息
	 * @param map
	 * @return
	 * 2017年4月7日
	 * gaohui
	 */
	public List<Map> findPositionsByTaskId(Map map){
		return positionMapper.findPositionsByTaskId(map);
	}

}

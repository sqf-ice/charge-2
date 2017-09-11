package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 位置信息单元测试
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月8日
 */
public class PositionServiceTest extends ApplicationTests{
	
	@Autowired
	private  PositionService positionService;
	
	@Override
	public void initialize() {
		// TODO 
		
	}
	
	@Test
	public void queryPositions(){
		DataVo vo = new DataVo();
		vo.add("taskId", 9);
		List<Map> list = positionService.findPositionsByTaskId(vo);
	}

}

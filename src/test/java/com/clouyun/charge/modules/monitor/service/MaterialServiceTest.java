package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MaterialServiceTest extends ApplicationTests {

	
	@Autowired
	MaterialService materialService;
	
	@Test
	public void query() {
		DataVo map = new DataVo();
		result = materialService.query(map.toMap());
	}
	@Test
	public void queryById(){
		DataVo map = new DataVo();
		map.add("matId", 26);
		result = materialService.queryById(map.toMap());
	}
	@Override
	public void initialize() {
		
	}

}

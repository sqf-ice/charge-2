package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 
 * 描述: 图片信息单元测试 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月8日
 */
public class PhotoServiceTset extends ApplicationTests{
	
	@Autowired
	private PhotoService photoService;
	
	@Test
	public void queryPhotosByTaskId() throws BizException{
		DataVo vo = new DataVo();
		vo.add("taskId", 9);
		List<Map> list = photoService.queryPhotolistByTaskId(vo);
	}

	@Override
	public void initialize() {
		// TODO 
		
	}

}

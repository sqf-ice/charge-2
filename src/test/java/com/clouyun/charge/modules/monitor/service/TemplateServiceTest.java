/**
 * 
 */
package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 描述: TemplateServiceTest
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年3月14日
 */
public class TemplateServiceTest extends ApplicationTests {

	/* (non-Javadoc)
	 * @see com.clouyun.charge.ApplicationTests#initialize()
	 */
	@Override
	public void initialize() {

	}

	@Autowired
	TemplateService service;
	@Test
	public void insert() {
		DataVo dataVo = new DataVo();
		dataVo.put("templateName", "模板2");
		dataVo.put("createTime", "2017-02-22 19:26:55");
		dataVo.put("updateTime", "2017-02-22 19:26:55");
		dataVo.put("orgId", 67);
		dataVo.put("optionName", "假按揭啊2");
		dataVo.put("optionResouces","01");
		dataVo.put("optionType","asdjaoisdjoajs"); //会回滚
		ResultVo vo = new ResultVo();
		try {
			service.insert(dataVo);
		} catch (Exception e) {
			BizException bizErr = new BizException(1000010);
			vo.setError(bizErr.getErrorCode(), bizErr.getMessage());
		}
		result = vo;
	}
	
	@Test
	public void testQuery() throws Exception {
		DataVo vo = new DataVo();
		List<Integer> list = new ArrayList<Integer>();
		list.add(335);
		list.add(354);
		vo.add("orgIds", list);
//		vo.add("createTimeStart", "2017-03-27");
//		vo.add("createTimeEnd", "2017-03-28");
		result = service.query(vo);
	}
	@Test
	public void queryListByOrgId(){
		Set<Integer> orgIds = new HashSet<Integer>();
		orgIds.add(335);
		orgIds.add(354);
		DataVo data = new DataVo();
		data.add("orgIds", orgIds);
		result = service.queryListByOrgId(data);
	}
}

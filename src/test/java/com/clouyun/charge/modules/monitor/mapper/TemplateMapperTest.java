package com.clouyun.charge.modules.monitor.mapper;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.utils.JsonUtils;
import com.clouyun.charge.ApplicationTests;


/**
 * 描述: TemplateMapperTest
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年2月27日
 */
public class TemplateMapperTest extends ApplicationTests {

	@Autowired
	TemplateMapper templateMapper;
	
	/*@Test
	public void queryListCount() {
		DataVo vo = new DataVo();
		Map<Integer, String> a =new HashMap<Integer, String>();
		a.put(1, "1");
		a.put(8, "8");
		a.put(67, "1");
		vo.put("orgIds", a.keySet());
		result = templateMapper.queryListCount(vo);
	}*/
	/*@Test
	public void queryListByPage() {
		DataVo vo = new DataVo();
		Map<Integer, String> a =new HashMap<Integer, String>();
		a.put(1, "1");
		a.put(8, "8");
		a.put(67, "1");
		vo.put("orgIds", a.keySet());
		result = templateMapper.queryListByPage(vo);
	}*/
	/*@Test
	public void checkUniqueness(){
		DataVo vo = new DataVo();
		vo.put("templateName", "啊哈哈哈");
		vo.put("templateId", 3);
		result = templateMapper.checkUniqueness(vo);
	}*/
	@Test
	public void testQuery() throws Exception {
		DataVo vo = new DataVo();
		List<Integer> list = new ArrayList<Integer>();
		list.add(335);
		list.add(354);
		vo.add("orgIds", list);
//		vo.add("createTimeStart", "2017-03-27");
//		vo.add("createTimeEnd", "2017-03-28");
		result = templateMapper.queryListByPage(vo);
	}
	@Test
	public void insert(){
		DataVo vo = new DataVo();
		//vo.put("templateId", 0);
		vo.put("templateName", "模板2");
		vo.put("createTime", "2017-02-22 19:26:55");
		vo.put("updateTime", "2017-02-22 19:26:55");
		vo.put("orgId", 67);
		result = templateMapper.insert(vo);
		JsonUtils.print(vo.get("templateId"));
	}
	/*@Test
	public void update(){
		DataVo vo = new DataVo();
		vo.put("templateId", 1);
		vo.put("templateName", "模板2");
		vo.put("createTime", "2017-02-22 19:26:55");
		vo.put("updateTime", "2017-02-22 19:26:55");
		result = templateMapper.update(vo);
	}
	*/
	/*@Test
	public void queryById(){
		DataVo vo = new DataVo();
		vo.add("templateId", 1);
		result = templateMapper.queryById(vo);
	}*/
	/*@Test
	public void delete(){
		DataVo vo = new DataVo();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		vo.put("ids", ids);
		result = templateMapper.delete(vo);
	}*/
	@Override
	public void initialize() {
		
	}

}

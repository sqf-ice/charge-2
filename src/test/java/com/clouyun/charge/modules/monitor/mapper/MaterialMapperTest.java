package com.clouyun.charge.modules.monitor.mapper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.monitor.mapper.MaterialMapper;


/**
 * 描述: MaterialMapperTest
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年2月27日
 */
public class MaterialMapperTest extends ApplicationTests {

	@Autowired
	MaterialMapper materialMapper;
	
	/*@Test
	public void queryListCount() {
		DataVo vo = new DataVo();
		Map<Integer, String> a =new HashMap<Integer, String>();
		a.put(1, "1");
		a.put(8, "8");
		a.put(67, "1");
		vo.put("orgIds", a.keySet());
		result = materialMapper.queryListCount(vo.toMap());
	}*/
	@Test
	public void queryListByPage() {
		DataVo vo = new DataVo();
		Map<Integer, String> a =new HashMap<Integer, String>();
		a.put(1, "1");
		a.put(8, "8");
		a.put(67, "1");
		vo.put("orgIds", a.keySet());
		result = materialMapper.queryListByPage(vo.toMap());
	}
	/*@Test
	public void checkUniqueness(){
		DataVo vo = new DataVo();
		vo.put("matName", "啊哈哈哈");
		vo.put("matId", 3);
		result = materialMapper.checkUniqueness(vo.toMap());
	}*/
	/*@Test
	public void insert(){
		DataVo vo = new DataVo();
		vo.put("matName", "张三");
		vo.put("matPrice", 8.2);
		vo.put("matFactory", "哈哈哈哈哈");
		vo.put("matProductionTime", "2017-02-22 19:26:55");
		vo.put("matPurchaseTime", "2017-02-22 19:26:55");
		vo.put("orgId", 67);
		result = materialMapper.insert(vo.toMap());
	}*/
	/*@Test
	public void update(){
		DataVo vo = new DataVo();
		vo.put("matId", 5);
		vo.put("matName", "张三");
		vo.put("matPrice", 8.2);
		vo.put("matFactory", "哈哈哈哈哈");
		vo.put("matPurchaseTime", "2017-02-18 19:26:55");
		result = materialMapper.update(vo.toMap());
	}*/
	
	/*@Test
	public void delete(){
		DataVo vo = new DataVo();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(5);
		vo.put("ids", ids);
		result = materialMapper.delete(vo.toMap());
	}*/
	/*@Test
	public void queryById(){
		DataVo vo = new DataVo();
		vo.add("matId", 14);
		result = materialMapper.queryById(vo.toMap());
	}*/
	@Override
	public void initialize() {
		
	}

}

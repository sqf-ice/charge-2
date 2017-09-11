/**
 * 
 */
package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 描述: TaskInfoServiceTest
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年3月30日
 */
public class TaskInfoServiceTest extends ApplicationTests{
	
	@Autowired
	TaskInfoService service;
	
	@Test
	public void query() {
		DataVo map = new DataVo();
		map.add("type", 1);
		result = service.getTaskSubTop(map);
	}
	@Test
	public void getFinishTaskTop(){
		DataVo map =new DataVo();
		map.add("taskDateStart", "2017-12-12 00:00:00");
		result = service.getFinishTaskTop(map);
	}
	@Test
	public void getLevelTaskTop(){
		DataVo map = new DataVo();
		map.add("taskLevel", "S");
		map.add("taskDateStart", "2017-03-1 00:00:00");
		result = service.getLevelTaskTop(map);
	}
	@Test
	public void getListMapOrderDesc(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("name", "张三");//名字
		map1.put("age", 22);    //年龄
		list.add(map1);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("name", "王五");
		map3.put("age", 38);
		list.add(map3);
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("name", "谢七");
		map5.put("age", 20);
		list.add(map5);
		Map<String, Object> map6 = new HashMap<String, Object>();
		map6.put("name", "张三");
		map6.put("age", 22);
		list.add(map6);
		//匿名实现Comparator接口进行排序
		Collections.sort(list, new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				//进行判断
				return ((Integer)o2.get("age")).compareTo((Integer)o1.get("age"));
			}
		});
		for(Map<String,Object> m:list){
			System.out.println("Map[name="+m.get("name")+"age="+m.get("age")+"]");
		}
	}
	@Test
	public void queryTaskInfoAll(){
		Map map = new HashMap();
		map.put("taskDateStart", "2017-04-01");
		map.put("taskDateEnd", "2017-04-01");
		
		service.query(map);
	}
	@Test
	public void getTaskInfo(){
		DataVo vo = new DataVo();
		vo.add("taskId", 426);
		result = service.getTaskInfo(vo);
	}
	@Test
	public void updateTaskInfo() throws BizException{
		DataVo vo = new DataVo(); 
		vo.add("taskId", 605);
		vo.add("taskStatus","5");
		service.updateTaskInfo(vo);
	}
	/* (non-Javadoc)
	 * @see com.clouyun.charge.ApplicationTests#initialize()
	 */
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}

package com.clouyun.charge.modules.demo;


import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.demo.service.DemoUserService;
import com.clouyun.charge.modules.demo.vo.DemoUser;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.mapstruct.MapMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserServiceTest extends ApplicationTests {

	
	@Autowired
	DemoUserService demoUserService;

	@Test
	public void testFindUserById(){
		Integer userID = 11;
		DataVo user = demoUserService.findUserById(userID);
		System.out.println(user.toString());
	}

	@Test
	public void testFindUserByUserName(){
		String userName = "test3";
		DataVo user = demoUserService.findUserByUserName(userName);
		System.out.println(user.toString());
	}

	@Test
	public void testAddUser(){
		Map user = new HashMap();
		user.put("userName","test7");
		user.put("userPwd","123456");
		user.put("userGender","00");
		user.put("userEmail","913652367@163.com");
		user.put("userPhone","15578946541");
		user.put("userAddr","广东省深圳市南山区");
		user.put("userStatus","00");
		DataVo vo = new DataVo(user);
		demoUserService.addUser(vo);
	}

	@Test
	public void testDelUserById(){
		Integer userID = 1;
		demoUserService.delUserById(userID);
	}

	@Test
	public void testDelUserByIds(){
		List IDs = new ArrayList();
		IDs.add("1");
		IDs.add("2");
		demoUserService.delUserByIds(IDs);
	}

	@Test
	public void testUpdateUser(){
		Map user = new HashMap();
		user.put("userId",11);
		user.put("userName","test7");
		user.put("userPwd","123456");
		user.put("userGender","00");
		user.put("userEmail","913652367@163.com");
		user.put("userPhone","15578946541");
		user.put("userAddr","广东省深圳市南山区");
		user.put("userStatus","00");
		DataVo vo = new DataVo(user);
		demoUserService.updateUser(vo);
	}

	@Test
	public void testFindByCondition(){
		Map map = new HashMap();
		map.put("pageNum",1);
		map.put("pageSize",3);
		//map.put("userAddr","龙岗区");
		//user.setUserId(3);
		DataVo vo = new DataVo(map);
		ResultVo resultVo = new ResultVo();
		PageInfo pageList=demoUserService.findByCondition(vo);
		resultVo.setData(pageList);
		//System.out.println(user.toString());
	}


	@Override
	public void initialize() {

	}
}

package com.clouyun.charge.modules.demo;

import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.demo.web.DemoUserController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DemoUserControllerTest extends ApplicationTests{

	@Autowired
	DemoUserController  demoUserController;



	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(demoUserController).build();
	}


	public void testApi(String apiurl) throws Exception{
		//调用接口，传入添加的用户参数
		ResultActions ra = mockMvc.perform(get(apiurl))
				//判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
				.andExpect(status().isOk());
		//此处使用jsonPath解析返回值，判断具体的内容
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}
	/**
	 * 登录
	 * @throws Exception
	 */
	@Test
	public void testLogin() throws Exception {
		testApi("/api/demoUser/login?userName=test1&userPwd=123456");
	}
	/**
	 * 增加
	 * @throws Exception
	 */
	@Test
	public void testAddUser() throws Exception {
		testApi("/api/demoUser/addUser?userName=test8&userPwd=123456");
	}
	@Test
	public void testDelUser() throws Exception {
		testApi("/api/demoUser/delUserById/12");
	}
	@Test
	public void testDelUsers() throws Exception {
		testApi("/api/demoUser/delUsersByIds/3,6,4");
	}


	@Test
	public void testUpdateUser() throws Exception {
		testApi("/api/demoUser/updateUser?userId=12&userStatus=00");
	}

	@Test
	public void testQueryUsers() throws Exception {
		testApi("/api/demoUser/userlist?pageSize=3&pageNum=1");
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

}
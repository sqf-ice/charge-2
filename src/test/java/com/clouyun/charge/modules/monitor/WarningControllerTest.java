package com.clouyun.charge.modules.monitor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.monitor.web.WarningController;

/**
 * 描述: 告警单元测试 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子 
 * 作者: gaohui 
 * 版本: 1.0 创建日期:2017年4月20日
 */
public class WarningControllerTest extends ApplicationTests {

	@Autowired
	private WarningController warningController;

	@Override
	public void initialize() {

	}

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(warningController).build();
	}

	/**
	 * 根据告警id更新告警信息 
	 * 2017年4月20日 
	 * gaohui
	 * @throws BizException
	 */
	@Test
	public void updateWarning() throws Exception {
		String json = FileUtils.read("/modules/monitor/updateWarning.json");
		ResultActions ra = mockMvc.perform(put("/api/warnings").contentType(
				MediaType.APPLICATION_JSON_UTF8).content(json));
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}

	/**
	 * 根据告警id获取告警信息 
	 * 2017年4月20日 
	 * gaohui
	 * @throws BizException
	 */
	@Test
	public void getWarning() throws Exception {
		ResultActions ra = mockMvc.perform(get("/api/warnings/371")).andExpect(
				status().isOk());
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}

	/**
	 * 分页查询告警列表 
	 * 2017年4月20日 
	 * gaohui
	 * @throws BizException
	 */
	@Test
	public void getWarningsPage() throws Exception {
		ResultActions ra = mockMvc.perform(
				get("/api/warnings?pageNum=0&pageSize=10")).andExpect(
				status().isOk());
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}
	
	@Test
	public void alarmItems() throws Exception {
		String json = FileUtils.read("/modules/monitor/alarmItems.json");
		ResultActions ra = mockMvc.perform(put("/api/warnings/alarmItems").contentType(
				MediaType.APPLICATION_JSON_UTF8).content(json));
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}
	
	@Test
	public void dispatchAgain() throws Exception {
		String json = FileUtils.read("/modules/monitor/dispatchAgain.json");
		ResultActions ra = mockMvc.perform(put("/api/warnings/").contentType(
				MediaType.APPLICATION_JSON_UTF8).content(json));
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}
	
}

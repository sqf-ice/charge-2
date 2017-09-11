package com.clouyun.charge.modules.monitor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.monitor.web.TemplateController;

public class TemplateControllerTest extends ApplicationTests{
	@Autowired
	private TemplateController templateController;
	@Override
	public void initialize() {
		
	}
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(templateController).build();
	}
	@Test
	public void addTemplate() throws Exception {
		String json = FileUtils.read("/modules/monitor/addTemplate.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/templates")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
	}
}

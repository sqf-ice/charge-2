package com.clouyun.charge.modules.system;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.system.web.FieldUserController;

public class FieldUserControllerTest extends ApplicationTests{
	
	@Autowired
	private FieldUserController fieldUserController;

	@Override
	public void initialize() {
		
	}

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(fieldUserController).build();
    }
    
    @Test
	public void insertFieldUser() throws Exception {
		String json = FileUtils.read("/modules/system/insertFieldUser.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/fUsers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
	}
    
    @Test
	public void updateFieldUser() throws Exception {
		String json = FileUtils.read("/modules/system/insertFieldUser.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(put("/api/fUsers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
	}
    
    @Test
	public void exportFieldUser() throws Exception {
    	ResultActions ra = mockMvc.perform(
				get("/api/fUsers/_export")).andExpect(
				status().isOk());
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}
    
    @Test
	public void deleteFieldUser() throws Exception {
    	ResultActions ra = mockMvc.perform(
				delete("/api/fUsers/348,350")).andExpect(
				status().isOk());
		MvcResult mr = ra.andReturn();
		String result = mr.getResponse().getContentAsString();
		System.out.println("状态返回值：" + mr.getResponse().getStatus());
		System.out.println("测试数据返回值:" + result.toString());
	}
    
}

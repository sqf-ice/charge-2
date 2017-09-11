package com.clouyun.charge.modules.document;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import java.io.FileInputStream;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.document.web.PileController;

public class PileControllerTest extends ApplicationTests{
	
	@Autowired
	private PileController pileController;

	@Override
	public void initialize() {
		
	}

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pileController).build();
    }
    
    @Test
	public void insertPile() throws Exception {
		String json = FileUtils.read("/modules/document/insertPile.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/piles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
	}
    @Test
	public void upPile() throws Exception {
		String json = FileUtils.read("/modules/document/upPile.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(put("/api/piles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
	}
    
    @Test
	public void importPiles() throws Exception {
        ResultActions ra = this.mockMvc.perform(fileUpload("/file.xlsx").file(new MockMultipartFile("充电桩导入模板.xlsx", new FileInputStream("C:/Users/lipeng/Desktop"))));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
	}
}

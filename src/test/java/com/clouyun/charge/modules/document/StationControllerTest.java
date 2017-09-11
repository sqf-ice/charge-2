package com.clouyun.charge.modules.document;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.document.web.PileController;
import com.clouyun.charge.modules.document.web.StationController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class StationControllerTest extends ApplicationTests{
	
	@Autowired
	private StationController stationController;

	@Override
	public void initialize() {
		
	}

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(stationController).build();
    }
    


    @Test
    public void saveStation() throws Exception {
        String json = FileUtils.read("/modules/document/saveStation.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/stations")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(json));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
}

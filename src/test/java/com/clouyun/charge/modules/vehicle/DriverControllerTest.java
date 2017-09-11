package com.clouyun.charge.modules.vehicle;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.vehicle.web.DriverController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 
 * @author fft
 *
 */
public class DriverControllerTest extends ApplicationTests{

	@Autowired
    private DriverController driverController;//你要测试的Controller

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(driverController).build();
    }
    
    
    /**
     * 查询所有
     */
    //@Test
    public void queryall() throws Exception {
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(get("/api/drivers"))
                //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
                .andExpect(status().isOk());
                //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    
    /**
     * 查询驾驶员信息
     */
    //@Test
    public void queryDrivers() throws Exception {
        //调用接口，传入添加的用户参数
    	String url ="/api/drivers/_queryDrivers?driverName=曹";
        ResultActions ra = mockMvc.perform(get(url))
                //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
                .andExpect(status().isOk());
                //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    
    /**
     * 测试新增
     */

    @Test
    public void save() throws Exception {
        String dictJson = FileUtils.read("/modules/vehicle/saveDriver.json");
        System.out.println("读取到的Json文件:" + dictJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/drivers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(dictJson));
        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
        //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

package com.clouyun.charge.modules.vehicle;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.vehicle.web.VehicleController;

public class VehicleControllerTest extends ApplicationTests{
	@Autowired
    private VehicleController vehicleController;//你要测试的Controller

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
    }
    
    
    /**
     * 查询所有
     */
    //@Test
    public void queryall() throws Exception {
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(get("/api/vehicles"))
                //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
                .andExpect(status().isOk());
                //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    
    /**
     * 查询车辆信息
     */
    @Test
    public void queryVehicles() throws Exception {
        //调用接口，传入添加的用户参数
    	String url ="/api/vehicles/_queryVehicles?licensePlate=晋";
        ResultActions ra = mockMvc.perform(get(url))
                //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
                .andExpect(status().isOk());
                //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    
    @Test
	public void insertVehicle() throws Exception {
		String json = FileUtils.read("/modules/vehicle/insertVehicle.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
	}

    @Test
	public void updateVehicle() throws Exception {
		String json = FileUtils.read("/modules/vehicle/updateVehicle.json");
        System.out.println("读取到的Json文件:" + json);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(put("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json));
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

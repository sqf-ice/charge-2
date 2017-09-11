package com.clouyun.charge.modules.charge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.charge.web.ChargeInController;
import com.clouyun.charge.modules.charge.web.StationProfitController;

public class StationProfitControllerTest extends ApplicationTests{
	
	@Autowired
	StationProfitController  stationProfitController;
	

	
	 @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(stationProfitController).build();
    }



	@Override
	public void initialize() {
		
	}
	
	/**
     * 查询所有
     */
    public void query(String api) throws Exception {
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(get(api))
                //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
                .andExpect(status().isOk());
                //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    
    @Test
    public void queryAll() throws Exception{
    	query("/api/profit/station?userId=1");
    }
    
    @Test
    public void queryMonth() throws Exception{
//    	query("/api/profit/station?userId=1&settlementMonth=2017-03");
    	query("/api/profit/station?userId=1&settlementMonth=2017-04");
    }
    @Test
    public void queryOrgName() throws Exception{
    	query("/api/profit/station?userId=1&settlementMonth=2017-04&orgName=科");
    }
	 
}

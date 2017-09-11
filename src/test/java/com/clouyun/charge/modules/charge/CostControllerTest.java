package com.clouyun.charge.modules.charge;

import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.charge.web.CostController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CostControllerTest extends ApplicationTests {
	
	@Autowired
	CostController costController;
	
	 @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(costController).build();
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
	 * 查询用电报表
	 * @throws Exception
	 */
	@Test
	public void testSelectCost() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
        testApi("/api/cost/cost?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&stationNo=1&stationName=1");
	}


	/**
	 * 查询用电导出
	 * @throws Exception
	 */
	@Test
	public void testSelectCostExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/cost/cost/_export?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&stationNo=1&stationName=1");
	}
	/**
	 * 查询收入报表
	 * @throws Exception
	 */
	@Test
	public void testSelectIncome() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/cost/income?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&stationNo=1&stationName=1");
	}

	/**
	 * 收入报表导出
	 * @throws Exception
	 */
	@Test
	public void testSelectIncomeExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/cost/income/_export?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&stationNo=1&stationName=1");
	}

	/**
	 * 收入报表详情
	 * @throws Exception
	 */
	@Test
	public void testSelectIncomeDetail() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/cost/income/detail?stationId=612&irStartDate=1&irEndDate=1&pageNum=1&pageSize=1");
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

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
import com.clouyun.charge.modules.charge.web.HistoryDataController;

public class HistoryDataControllerTest extends ApplicationTests {
	
	@Autowired
	HistoryDataController historyDataController;
	
	 @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(historyDataController).build();
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
	 * 查询历史数据
	 * @throws Exception
	 */
	@Test
	public void testSelectHistorydata() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
        testApi("/api/historydata/historydata?userId=1&orgId=1&billPayNo=1pileId=1&stationId=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&orgName=1&stationName=1&pileName=1");
	}
	/**
	 * 历史数据导出
	 * @throws Exception
	 */
	@Test
	public void testSelectHistorydataExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/historydata/historydata/_export?userId=1&orgId=1&billPayNo=1pileId=1&stationId=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&orgName=1&stationName=1&pileName=1");
	}
	/**
	 * 历史数据报表详情
	 * @throws Exception
	 */
	@Test
	public void testSelectHistorydataDetail() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/historydata/historyAchieveData/278297");
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

package com.clouyun.charge.modules.charge;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.charge.web.GatheringController;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GatheringControllerTest extends ApplicationTests {
	
	@Autowired
	GatheringController gatheringController;
	
	 @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(gatheringController).build();
    }
	

	@Test
	public void testGetGroup() throws Exception {
		DataVo data=new DataVo();
		ResultVo rv=gatheringController.selectRecharge(data);
		System.out.println(rv);
	}

	@Test
	public void testGetChargeWay() {
		fail("Not yet implemented");
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
	 * 查询充值记录
	 * @throws Exception
	 */
	@Test
	public void testSelectRecharge() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
        testApi("/api/gathering/recharge?userId=1&startDate=1&endDate=1&billRechargeNo=1&rechargeType=1&groupId=1&consName=1&consTypeCode=1&phoneNo=1&pageNum=1&pageSize=1&groupName=1");
	}

	/**
	 * 充值记录导出
	 * @throws Exception
	 */
	@Test
	public void testSelectRechargeExport() throws Exception {
		testApi("/api/gathering/recharge/_export?userId=1&startDate=1&endDate=1&billRechargeNo=1&rechargeType=1&groupId=1&consName=1&consTypeCode=1&phoneNo=1&pageNum=1&pageSize=1&groupName=1");
	}


	/**
	 * 查询卡充值记录
	 * @throws Exception
	 */
	@Test
	public void testSelectCardRecharge() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/gathering/cardRecharge?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&cardId=1&stationId=1&stationName=1&groupId=1&groupName=1&consName=1&consTypeCode=1");
	}
	/**
	 * 卡充值记录导出
	 * @throws Exception
	 */
	@Test
	public void testSelectCardRechargeExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/gathering/cardRecharge/_export?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&cardId=1&stationId=1&stationName=1&groupId=1&groupName=1&consName=1&consTypeCode=1");
	}

	/**
	 * 月结记录查询
	 * @throws Exception
	 */
	@Test
	public void testSelectMonthRecharge() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/gathering/month?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&cardId=1&groupId=1&groupName=1&gatherPerson=1");
	}
	/**
	 * 月结记录导出
	 * @throws Exception
	 */
	@Test
	public void testSelectMonthRechargeExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/gathering/month/_export?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&cardId=1&groupId=1&groupName=1&gatherPerson=1");
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

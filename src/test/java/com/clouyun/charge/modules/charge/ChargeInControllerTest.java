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

public class ChargeInControllerTest extends ApplicationTests{
	
	@Autowired
	ChargeInController  chargeInController;
	

	
	 @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(chargeInController).build();
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
	 * 充电收入查询
	 * @throws Exception
	 */
	@Test
	public void testSelectCdsr() throws Exception {
		testApi("/api/chargein/charging?userId=1&startDate=2015-02-03&endDate=2015-06-07&rechargeCard=11111&orgId=1&consTypeCode=1&payType=1&stationId=1&groupId=1&payState=1&pileId=1&consPhone=111&billPayNo=111&pageNum=1&pageSize=1");
	}
	/**
	 * 充电收入导出
	 * @throws Exception
	 */
	@Test
	public void testSelectCdsrExport() throws Exception {
		testApi("/api/chargein/charging/_export?userId=1&startDate=2015-02-03&endDate=2015-06-07&rechargeCard=11111&orgId=1&consTypeCode=1&payType=1&stationId=1&groupId=1&payState=1&pileId=1&consPhone=111&billPayNo=111&pageNum=1&pageSize=1&groupName=sd&orgName=dfds&stationName=sdfds&pileName=sdfsd");
	}

	/**
	 * 充电收入详情
	 * @throws Exception
	 */
	@Test
	public void testSelectCdsrDetiali() throws Exception {
		testApi("/api/chargein/detail?nodeId=278296&useTime=0小时4分52秒");
	}


	/**
	 * 分成收入列表
	 * @throws Exception
	 */
	@Test
	public void testSelectdivide() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/divide?userId=1&startDate=2015-02-03&endDate=2015-02-03&stationId=1&stationName=1&contractId=1&contractName=2&pageNum=1&pageSize=1");
	}

	/**
	 * 分成收入导出
	 * @throws Exception
	 */
	@Test
	public void testSelectdivideExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/divide/_export?userId=1&startDate=2015-02-03&endDate=2015-02-03&stationId=1&stationName=1&contractId=1&contractName=2&pageNum=1&pageSize=1");
	}
	/**
	 * 分成收入详情
	 * @throws Exception
	 */
	@Test
	public void testSelectdivideDetail() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/divide/detail?nodeId=1&did=1&pageNum=1&pageSize=1");
	}
	/**
	 * 发票管理列表
	 * @throws Exception
	 */
	@Test
	public void testSelectfpgl() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/invoice?userId=1&consName=1&recipientPhone=1&isInvoice=1&buyName=1&recipientName=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1");
	}
	/**
	 * 发票管理导出
	 * @throws Exception
	 */
	@Test
	public void testSelectfpglExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/invoice/_export?userId=1&consName=1&recipientPhone=1&isInvoice=1&buyName=1&recipientName=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1");
	}
	/**
	 * 发票管理详情
	 * @throws Exception
	 */
	@Test
	public void testSelectfpglDetail() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/invoice/detail/19");
	}
	/**
	 * 查询场站收入
	 * @throws Exception
	 */
	@Test
	public void testSelectczsr() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/station?userId=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&stationId=1&stationName=11");
	}
	/**
	 * 场站收入导出
	 * @throws Exception
	 */
	@Test
	public void testSelectczsrExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/station/_export?userId=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&stationId=1&stationName=11");
	}
	/**
	 * 场站收入详情
	 * @throws Exception
	 */
	@Test
	public void testSelectczsrDetail() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/stationDetail?nodeId=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1");
	}
	/**
	 * 场站收入详情导出
	 * @throws Exception
	 */
	@Test
	public void testSelectczsrDetailExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/stationDetail/_export?nodeId=1&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1");
	}
	/**
	 * 待结收入查询
	 * @throws Exception
	 */
	@Test
	public void testSelectTobe() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/tobe?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&pileType=1&orgId=1&consTypeCode=1rechargeCard=1&stationId=1&payType=1&groupId=1&pileName&consPhone=1&orgName=11&stationName=111&groupName=111");
	}
	/**
	 * 待结收入导出
	 * @throws Exception
	 */
	@Test
	public void testSelectTobeExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/tobe/_export?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&pileType=1&orgId=1&consTypeCode=1rechargeCard=1&stationId=1&payType=1&groupId=1&pileName&consPhone=1&orgName=11&stationName=111&groupName=111");
	}
	/**
	 * 第三方收入查询
	 * @throws Exception
	 */
	@Test
	public void testSelectOther() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/other?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&startChargeSeq=1&billStatus=1&billPayNo=1&operatorId=1&operatorName=1");
	}
	/**
	 * 第三方收入导出
	 * @throws Exception
	 */
	@Test
	public void testSelectOtherExport() throws Exception {
		//&startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1
		testApi("/api/chargein/other/_export?startDate=2015-02-03&endDate=2015-02-03&pageNum=1&pageSize=1&userId=1&startChargeSeq=1&billStatus=1&billPayNo=1&operatorId=1&operatorName=1");
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

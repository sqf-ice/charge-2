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
import com.clouyun.charge.modules.charge.web.CostController;
import com.clouyun.charge.modules.charge.web.HistoryDataController;
import com.clouyun.charge.modules.charge.web.PriceModelController;

public class PriceModelControllerTest extends ApplicationTests {
	
	@Autowired
	PriceModelController priceModelController;
	
	 @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(priceModelController).build();
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
	 * 查询电价模板
	 * @throws Exception
	 */
	@Test
	public void testSelectPriceModel() throws Exception {
        testApi("/api/priceModel/priceModel");
	}
	
	/**
	 * 导出电价模板
	 * @throws Exception
	 */
	@Test
	public void testExportPriceModel() throws Exception {
		testApi("/api/priceModel/priceModel/export");
	}

	/**
	 * 统计电价模板
	 * @throws Exception
	 */
	@Test
	public void testCountPriceModel() throws Exception {
		testApi("/api/priceModel/priceModel/count");
	}
	
	
	
	/**
	 * 查询电价任务
	 * @throws Exception
	 */
	@Test
	public void testSelectPriceTask() throws Exception {
        testApi("/api/priceModel/priceTask");
	}
	
	/**
	 * 导出电价任务
	 * @throws Exception
	 */
	@Test
	public void testExportPriceTask() throws Exception {
		testApi("/api/priceModel/priceTask/export");
	}

	/**
	 * 统计电价任务
	 * @throws Exception
	 */
	@Test
	public void testCountPriceTask() throws Exception {
		testApi("/api/priceModel/priceTask/count");
	}
	
	/**
	 * 删除电价模板
	 * @throws Exception
	 */
	@Test
	public void testDeletePriceModel() throws Exception {
		testApi("/api/priceModel/priceModel/delete");
	}
	
	/**
	 * 删除电价任务
	 * @throws Exception
	 */
	@Test
	public void testDeletePriceTask() throws Exception {
		testApi("/api/priceModel/priceTask/delete");
	}
	
	
	
	/**
	 * 电价模板详情
	 * @throws Exception
	 */
	@Test
	public void testDetailPriceModel() throws Exception {
		testApi("/api/priceModel/priceModel/detail");
	}
	
	
	/**
	 * 电价任务详情
	 * @throws Exception
	 */
	@Test
	public void testDetailPriceTask() throws Exception {
		testApi("/api/priceModel/priceTask/detail");
	}
	
	
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

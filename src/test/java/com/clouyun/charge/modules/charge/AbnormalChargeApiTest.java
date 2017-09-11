package com.clouyun.charge.modules.charge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.charge.service.AbnormalCharge;
import com.clouyun.charge.modules.charge.vo.AbnormalBill;
import com.clouyun.charge.modules.charge.web.ChargeInController;
import com.clouyun.randomcall.core.commservice.CommunicateServiceImpl;
import com.github.pagehelper.PageInfo;

public class AbnormalChargeApiTest extends ApplicationTests {

	
	@Autowired
	private ChargeInController  chargeInController;
	@Autowired
	private AbnormalCharge ac;
	
	@Test
	public void testQueryAbnormalBillInfo(){
		try {
			int billPayId = 813237;
			String api = "/api/chargein/abnormalBillInfo/"+billPayId;
			testApi(api);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSaveAbnormalBill(){
		try {
			String jsonFilePath = "/modules/charge/abnormalBillInfo.json";
			String jsonStr = FileUtils.read(jsonFilePath);
	        System.out.println("读取到的Json文件:" + jsonStr);
	        //调用接口，传入添加的用户参数
	        ResultActions ra = mockMvc.perform(put("/api/chargein/abnormalBill")
	                .contentType(MediaType.APPLICATION_JSON_UTF8)
	                .content(jsonStr));
	        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
	        //此处使用jsonPath解析返回值，判断具体的内容
	        MvcResult mr = ra.andReturn();
	        String result = mr.getResponse().getContentAsString();
	        System.out.println("状态返回值：" + mr.getResponse().getStatus());
	        System.out.println("测试数据返回值:" + result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testQueryAbnormalBillApi(){
		//用户xiaosh2 230
		try {
			String api = "/api/chargein/tobe?startDate=2017-8-1&endDate=2017-9-1"
					+ "&pageNum=1&pageSize=4&userId=230";  //&stationNo=1&stationName=1   
			testApi(api);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryAbnormalBill(){
		try{
			Map<String, String> vo = new HashMap<>();
			vo.put("startDate", "2017-8-1");
			vo.put("endDate", "2017-9-1");
			vo.put("pageNum", "1");
			vo.put("pageSize", "10");
			vo.put("userId", "230");
			//vo.put("phone", "134");
			PageInfo<AbnormalBill> pi =  ac.queryAbnormalBill(vo);
			if(pi!=null && pi.getTotal()>0){
				System.out.println("异常订单条数:"+pi.getSize());
				for(AbnormalBill ab:pi.getList()){
					if(ab.getType()==1)
						System.out.println(ab);
				}
			}else{
				System.out.println(pi);
			}
		}catch(Exception t){
			t.printStackTrace();
		}
	}
	
	
	@Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(chargeInController).build();
    }
	 
	 
	 public String testApi(String apiurl) throws Exception{
		 //调用接口，传入添加的用户参数
		 ResultActions ra = mockMvc.perform(get(apiurl))
                //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
                .andExpect(status().isOk());
                //此处使用jsonPath解析返回值，判断具体的内容
		 MvcResult mr = ra.andReturn();
		 String result = mr.getResponse().getContentAsString();
		 System.out.println("状态返回值：" + mr.getResponse().getStatus());
		 System.out.println("测试数据返回值:" + result.toString());
		 return result.toString();
	 }
	
	
	 @Autowired
	 private CommunicateServiceImpl cs;
	 
	@Override
	public void initialize() {
		try {
			cs.startService(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

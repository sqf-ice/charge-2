package com.clouyun.charge.modules.electricity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.electricity.service.LossModelService;
import com.github.pagehelper.PageInfo;

public class LossModelServiceTest extends ApplicationTests{

	@Autowired
	 LossModelService lossModelService;
	
	@SuppressWarnings("unchecked")
	//@Test
	public void saveLossModel() throws Exception {
		 Map data=new HashMap();
		 data.put("stationId", 12);
		 data.put("orgId", 12);
		 data.put("lmNo", "1234");
		 data.put("lmName", "测试5");
		 data.put("lmType", 1);
		 String Msg = lossModelService.lossModelAdd(data);
		 System.out.println(Msg);   
	}
	
	@SuppressWarnings("unchecked")
	//@Test
	public void updateLossModel() throws Exception {
		Map data=new HashMap();
		data.put("stationId", 3132);
		data.put("orgId", 12);
		data.put("lmNo", "1234");
		data.put("lmName", "测试3");
		data.put("lmType", 0);
		data.put("lmId", 2);
		 
		String Msg = lossModelService.update(data);
		System.out.println(Msg);
	   
	}
	
	//@Test	 
	public void modelCount() throws Exception {
		Map data=new HashMap();
		data.put("stationId", 3);
		data.put("orgId", 12);
		Integer in = lossModelService.modelCount(data);
		System.out.println(in);
	   
	}
	
	@Test	 
	public void selectAll() throws Exception {
		Map data=new HashMap();
		data.put("stationId", 35);
		data.put("orgId", 24);
		PageInfo list = lossModelService.selectAll(data);
//		for(Map<String,Object> map :list){
//			for(Map.Entry entry :map.entrySet()){
//				System.out.println(entry.getValue());
//			}
//			
//		}
	   
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}

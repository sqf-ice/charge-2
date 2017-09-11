package com.clouyun.charge.modules.monitor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;

public class OperatationServiceTest extends ApplicationTests{

	@Autowired
	OperationService operationService;
	
	//@Test
	public void queryStation() throws BizException {
		
		Map<String,Integer> map = operationService.queryStation(1);
		for(Map.Entry<String,Integer> entry : map.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}
	
	//@Test
	public void queryCcons() throws BizException {
		
		Map<String,Integer> map = operationService.queryCcons(1);
		for(Map.Entry<String,Integer> entry : map.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}
	
	//@Test
	public void queryIncomeInfo() throws BizException {
		
		Map<String,Integer> map = operationService.queryIncomeInfo(1);
		for(Map.Entry<String,Integer> entry : map.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}
	
	
	
	//@Test
	public void queryInitData() throws BizException {
		
		Map<String,Integer> map = operationService.queryInitData(1);
		for(Map.Entry<String,Integer> entry : map.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}
	
	@SuppressWarnings("rawtypes")
	//@Test
	public void queryRade() throws BizException {
		
		Map map = operationService.queryRade(1);
		String sumScale=map.get("sumScale").toString();
		if(map.containsKey("top3")){
			List list =(List) map.get("top3");
			Map map2=(Map) list.get(0);
			System.out.println(map2.get("prov")+" "+map2.get("incAdd")+" "+map2.get("scale"));
		}
		
	}
	
	
	//@Test
	public void queryUseRade() throws BizException {
		Map map =new HashMap();
		map.put("stationId", 5);
		
		Map<String,Integer> map1 = operationService.queryUseRade(5);
		for(Map.Entry<String,Integer> entry : map1.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}
	
	//@Test
	public void queryWarm() throws BizException {
		Map map =new HashMap();
		map.put("stationId", 477);
		Map<String,Integer> map1 = operationService.queryWarm(5);
		for(Map.Entry<String,Integer> entry : map1.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}
	
	//@Test
	public void queryLoss() throws BizException {
		Map map =new HashMap();
		map.put("stationId", 136);
		Map<String,Object> map1 = operationService.queryLoss(5);
		for(Map.Entry<String,Object> entry : map1.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}

	//@Test
	public void queryPileUseRate() throws BizException {
		Map map =new HashMap();
		map.put("stationId", 201);
		List<Map> list= operationService.queryPileUseRate(5);
		if(list!=null&&!list.isEmpty())
		System.out.println(list.get(0).get("pileName")+"  "+list.get(0).get("PileRate"));
	}	
	
	//@Test
	public void queryPileLoss() throws BizException {
		Map map =new HashMap();
		map.put("stationId", 136);
		List<Map> list= operationService.queryPileLoss(5);
		if(list!=null&&!list.isEmpty())
		System.out.println(list.get(0).get("pileName")+"  "+list.get(0).get("pileLossRate"));
	}	
	
	
	//@Test
	public void queryPileIncome() throws BizException {
		Map map =new HashMap();
		map.put("stationId", 128);
		List<Map> list= operationService.queryPileIncome(4);
		if(list!=null&&!list.isEmpty())
		System.out.println(list.get(0).get("power")+"  "+list.get(0).get("pileName")+"  "+list.get(0).get("income"));
	}		
	
	//@Test
	public void queryTotalInfo() throws BizException {
		Map map =new HashMap();
		map.put("stationId", 543);
		Map<String,Object> map1 = operationService.queryTotalInfo(5);
		for(Map.Entry<String,Object> entry : map1.entrySet()){
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
	}	
	
	   // @Test
		public void queryMonitorInfo() throws BizException {
			Map<String, Map<String, Object>> map2= operationService.queryMonitorInfo(1);
			if(map2!=null&&!map2.isEmpty())
				for(Entry<String, Map<String, Object>> map3 : map2.entrySet()){
					String dateString=map3.getKey();
					Map<String, Object> map4=map3.getValue();
					System.out.println(dateString);
					for(Entry<String, Object> data : map4.entrySet()){
						System.out.println(data.getKey()+"  "+data.getValue());
					}
				}
		}	
	    
		 @Test
		public void queryMonitorInfoByStationId() throws BizException {
			    Map map =new HashMap();
				
				Map<String, Map<String, Object>> map2= operationService.queryMonitorInfoByStationId(543);
				if(map2!=null&&!map2.isEmpty())
					for(Entry<String, Map<String, Object>> map3 : map2.entrySet()){
						String dateString=map3.getKey();
						Map<String, Object> map4=map3.getValue();
						System.out.println(dateString);
						for(Entry<String, Object> data : map4.entrySet()){
							System.out.println(data.getKey()+"  "+data.getValue());
						}
					}
			}	
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

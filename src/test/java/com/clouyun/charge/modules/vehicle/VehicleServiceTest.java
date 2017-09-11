package com.clouyun.charge.modules.vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.vehicle.service.VehicleService;

public class VehicleServiceTest extends ApplicationTests{

	@Autowired
	VehicleService vehicleService;
	
	@Test
	public void saveLossModel() throws Exception {
		 Map data=new HashMap();
		 data.put("licensePlate", "粤B07777D");
		 data.put("orgId", 24);
		 data.put("userId",490);
		 data.put("vehicleType", 1);
		 data.put("belongsType", 1);
		 data.put("usingRoperty",1);
		 data.put("usingRoperty",1);
		 data.put("operationRoperty",1);
		 List<Integer> driverIds = new ArrayList<Integer>();
		 driverIds.add(12);
		 driverIds.add(13);
		 data.put("driverIds", driverIds);
		 vehicleService.insertVehicle(data, null);
	}
	
	//@Test
	public void updateLossModel() throws Exception {
		 Map data=new HashMap();
		 data.put("vehicleId", 1002681);
		 data.put("licensePlate", "粤B07777D");
		 data.put("orgId", 24);
		 data.put("userId",490);
		 data.put("vehicleType", 1);
		 data.put("belongsType", 1);
		 data.put("usingRoperty",1);
		 data.put("usingRoperty",1);
		 data.put("operationRoperty",1);
		 List<Integer> driverIds = new ArrayList<Integer>();
		 driverIds.add(12);
		 driverIds.add(199);
		 data.put("driverIds", driverIds);
		 vehicleService.updateVehicle(data,null);
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}

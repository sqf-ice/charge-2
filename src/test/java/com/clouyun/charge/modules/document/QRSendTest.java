package com.clouyun.charge.modules.document;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.document.service.QrCodeService;

public class QRSendTest extends ApplicationTests {
	
	@Autowired
	private QrCodeService servive;
	
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testSend(){
		Map m = new HashMap();
		m.put("pileId", 4136);
		m.put("numberGun", 1);
		m.put("ortMode", 1);
		m.put("pileProtocol", 51);
		
		Map m1 = new HashMap();
		m1.put("chgGunId", "8888888");
		m1.put("qrCode", "8888888");
		m1.put("gumPoint", "8888888");
		/*m1.put("chgGunId", "");
		m1.put("innerId", "");*/
		m.put("chgGunInfo", Arrays.asList(new Map[]{m1}));
		
		try {
			servive.issuedCode(m);
		} catch (BizException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}

package com.clouyun.charge.modules.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.finance.service.WithdrawService;

public class WithdrawServiceTest extends ApplicationTests{

	@Override
	public void initialize() {
		
	}
	@Autowired
	private WithdrawService withdrawService;
	@Test
	public void getWithdrawsPage() throws BizException{
		Map map = new HashMap();
		map.put("time", new Date());
		map.put("name","cccccc");
		map.put("id","33");
		withdrawService.updateWithdraw(map);
	}

}

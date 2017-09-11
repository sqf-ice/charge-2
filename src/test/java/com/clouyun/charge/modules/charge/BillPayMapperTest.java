package com.clouyun.charge.modules.charge;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.clou.entitys.authority.PubPrice;
import com.clou.entitys.data.BillPay;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.modules.charge.mapper.BillPayMapper;
import com.clouyun.charge.modules.charge.vo.AbnormalBill;
import com.github.pagehelper.PageInfo;

public class BillPayMapperTest extends ApplicationTests {
	
	@Autowired
	private BillPayMapper bpm;
	
	@Test
	public void testUpdateBillPay(){
		int billPayId = 66458;
		String startTime = "2016-09-09 07:13:36";
		String endTime = "2016-09-09 08:13:37";
		int payState = 2;
		double chgPower = 44D;
		double amount = 44D;
		bpm.updateBillPay(billPayId, startTime, endTime, payState, chgPower, amount,null,12D,null,null,null,null,null,null,23D,null);
	}
	
	@Test
	public void testGetPilePrice(){
		PubPrice prc = bpm.getPilePrice(51);
		if(prc!=null)
			System.out.println(prc.getPrcId()+","+prc.getPrcName()+","+prc.getPrcService());
	}
	
	@Test
	public void testQuery(){
		int id = 66458;
		BillPay bill = bpm.findById(id);
		System.out.println(bill.getBillPayNo()+","+CalendarUtils.formatCalendar(bill.getStartTime()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testQueryAbnormalBill(){
		Integer orgId = null;
		Integer stationId = null;
		String consPhone = null;
		String billPayNo = "PAY20170830133";
		
		List<AbnormalBill> list = bpm.queryAbnormalBill(orgId,stationId,"2017-7-1","2017-9-1",consPhone,billPayNo,null,2,10);
		PageInfo pageInfo = new PageInfo(list);
		System.out.println("总条数:"+pageInfo.getTotal());
		if(list!=null && !list.isEmpty()){
			System.out.println("查询异常订单条数:"+list.size());
			for(AbnormalBill ab:list){
				System.out.println(ab);
			}
			
		}else{
			System.out.println(list);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testQueryAbnormalBill1(){
		Integer orgId = null;
		Integer stationId = null;
		String consPhone = "13";
		
		DataVo vo = new DataVo();
		//vo.add("orgId", orgId);
		//vo.add("stationId", stationId);
		vo.add("startDate", "2017-7-1");
		vo.add("endDate", "2017-9-1");
		/*try {
			ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
		} catch (BizException e) {
			e.printStackTrace();
		}*/
		ChargeManageUtil.setPageInfo(vo);
		List<AbnormalBill> list = bpm.queryAbnormalBill1(vo);
		PageInfo pageInfo = new PageInfo(list);
		System.out.println("总条数:"+pageInfo.getTotal());
		if(list!=null && !list.isEmpty()){
			System.out.println("查询异常订单条数:"+list.size());
			for(AbnormalBill ab:list){
				System.out.println(ab);
			}
			
		}else{
			System.out.println(list);
		}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

}

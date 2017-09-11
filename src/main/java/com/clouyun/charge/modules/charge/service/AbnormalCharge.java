package com.clouyun.charge.modules.charge.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clou.entitys.authority.PubPrice;
import com.clou.entitys.data.BillPay;
import com.clou.entitys.data.ChgRecord;
import com.clou.entitys.document.ChgPile;
import com.clou.system.util.AmountUtil;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.cdz.CdzBusi;
import com.clouyun.cdz.CdzBusiClou;
import com.clouyun.cdzcache.imp.CDZStatusGet;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.mapper.BillPayMapper;
import com.clouyun.charge.modules.charge.vo.AbnormalBill;
import com.github.pagehelper.PageInfo;


@Service
public class AbnormalCharge {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private BillPayMapper  billMap;
	@Autowired
	private CDZStatusGet csg;
	@Autowired
	private CdzBusi cdzBusi;
	@Autowired
	private CdzBusiClou cdzBusiClou;
	
	public List<AbnormalBill> queryAbnormalRrcord(DataVo vo) throws BizException{
		List<AbnormalBill> dvList = billMap.queryAbnormalBill1(vo);
		if(dvList!=null && !dvList.isEmpty()){
			String[] str = {"hylb","zfzt","shoukfs"};//会员类型字典(hylb) ,支付状态字典(zfzt),支付方式字典(shoukfs),设备类型字典(cdzlx)
			Map<String,List<ComboxVo>> dicMap =   ChargeManageUtil.getDictMap(str);//获取字典表的值
			Map<Integer, List<AbnormalBill>> rmap = new HashMap<>();
			DecimalFormat df = new DecimalFormat("000000000000000");
			for(AbnormalBill ab:dvList){
				if(ab.getStartTime()!=null && ab.getEndTime()!=null){
					Calendar bc = CalendarUtils.getCalendar(ab.getStartTime());
					Calendar ec = CalendarUtils.getCalendar(ab.getEndTime());
					ab.setUseTime((int)((ec.getTimeInMillis()-bc.getTimeInMillis()) / (1000*60) ));
				}
				if(ab.getPayType()!=null & ab.getPayTypeValue()==5){  //刷卡充电订单
					ab.setType(2);
				}else{
					byte status = csg.getCDQStatus(ab.getPileAddr(), ab.getGunNo());
					Double[] vs = csg.getCDQLastDlJe(ab.getPileAddr(), ab.getGunNo(), null,df.format(ab.getBillPayId()));
					if(vs!=null){
						if(status==0 && vs[2]==0){
							ab.setType(3);  //正在充电状态
						}else{
							ab.setType(1);  //停止充电状态
						}
						ab.setChgPower(vs[0]);
						ab.setAmount(vs[1]);
						ab.setUseTime((int)(vs[3]/60));
					}else if(status==0){
						ab.setType(3);  //正在充电状态
					}else{
						ab.setType(1);  //停止充电状态
					}
				}
				
				List<AbnormalBill> list = rmap.get(ab.getType());
				if(list==null){
					rmap.put(ab.getType(), list=new ArrayList<>());
				}
				ab.setPayState(getDictName(dicMap.get("zfzt"), ab.getPayState()));
				ab.setPayType(getDictName(dicMap.get("shoukfs"), ab.getPayType()));
				list.add(ab);
			}
			dvList.clear();
			if(rmap.containsKey(1))
				dvList.addAll(rmap.get(1));
			if(rmap.containsKey(2))
				dvList.addAll(rmap.get(2));
			if(rmap.containsKey(3))
				dvList.addAll(rmap.get(3));
			rmap.clear();
			rmap = null;
		}
		return dvList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageInfo<AbnormalBill> queryAbnormalBill(Map map) throws Exception {
		DataVo vo = new DataVo(map);
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<AbnormalBill> dvList = queryAbnormalRrcord(vo);
		
		Double dl=0D,je=0D;
		if(dvList!=null && !dvList.isEmpty()){
			for(AbnormalBill ab:dvList){
				if(ab.getChgPower()!=null){
					dl += ab.getChgPower();
				}
				if(ab.getAmount()!=null){
					je += ab.getAmount();
				}
			}
		}
		
		PageInfo pageInfo = new PageInfo(dvList);
		List<DataVo> returnList = new ArrayList<>();
		DataVo returnMap = new DataVo();
		returnMap.add("djsr",dvList);
		returnList.add(returnMap);
		DataVo count = new DataVo();
		count.put("totalPowerCount",ChargeManageUtil.df.format(dl));
		count.put("totalMoneyCount",ChargeManageUtil.df.format(je));
		returnMap.add("count",count);
		pageInfo.setList(returnList);
		return pageInfo;
	}
	
	public static String getDictName(List<ComboxVo> boxList,String dictId){
		if(boxList!=null&&boxList.size()>0){
			for (ComboxVo box : boxList) {
				if (box.getId().equals(dictId)) {
					return box.getText();
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取待结异常订单的信息(充电开始时间,充电时长,电量,金额)
	 * @param billPayId
	 * @return
	 */
	public Map<String,Object> getAbnormalChargeInfo(int billPayId){
		BillPay bill = billMap.findById(billPayId);
		if(bill==null)return null;
		ChgPile pile = billMap.getPileById(bill.getPileId());
		if(pile==null)return null;
		
		String msg = "异常结单下发停止充电命令";
		int busiType = 2;  //刷卡0不需要下发停止命令  1:app有结束事件，2,app无结束事件需下发停止命令
		if("5".equals(bill.getPayType())){
			busiType = 0;
			msg = "刷卡异常结单";
		}
		
		Double dl=bill.getChgPower()!=null?bill.getChgPower():0;
		Double je=bill.getAmount()!=null?bill.getAmount():0;
		Integer min = 0;
		try{
			DecimalFormat df = new DecimalFormat("000000000000000");
			List<ChgRecord> rlist = billMap.findChargeRecordById(billPayId);
			String cardNo = df.format(bill.getBillPayId());
			String seqNo = null;
			if("5".equals(bill.getPayType()) && bill.getBillDesc()!=null && bill.getBillDesc().contains("_")){
				String[] ss = bill.getBillDesc().split("_");
				cardNo = ss[0];
				seqNo = ss[1];
			}
			if("5".equals(bill.getPayType()) && seqNo==null){
				cardNo = null;
			}
			if((rlist==null || rlist.isEmpty()) && cardNo!=null){
				rlist = billMap.findChargeRecord(seqNo, cardNo);
			}
			boolean hasEndEvent = false;
			if(rlist!=null && !rlist.isEmpty()){
				long minWt=Long.MAX_VALUE,maxWt=Long.MIN_VALUE;
				long startLt=0;
				for(ChgRecord r:rlist){
					try{
						if(seqNo==null)
							seqNo = r.getPileSeqId();
						if(r.getChgType()!=0){
							hasEndEvent = true;
							if(!"5".equals(bill.getPayType())){
								busiType = 1;
								msg = "APP异常结单有充电结束事件";
							}
							double tmp = (r.getPreZxygz()!=null&&r.getCurZxygz()!=null)?(r.getCurZxygz()-r.getPreZxygz()):0D;
							if(tmp>0 && tmp<500 && tmp>dl)
								dl = tmp;
							tmp = (r.getPreAmount()!=null&&r.getCurAmount()!=null)?(r.getPreAmount()-r.getCurAmount()):0D;
							if(tmp>0 && tmp<1000 && tmp>je)
								je = tmp;
							if(r.getWriteTime().getTimeInMillis()>maxWt)
								maxWt = r.getWriteTime().getTimeInMillis();
						}else if(r.getWriteTime().getTimeInMillis()<minWt){
							minWt = r.getWriteTime().getTimeInMillis();
						}
						
						if(r.getStaTime()!=null && r.getEndTime()!=null){
							long lt = CalendarUtils.getCalendar(r.getEndTime()).getTimeInMillis()-CalendarUtils.getCalendar(r.getStaTime()).getTimeInMillis();
							min = (int)(lt/1000/60);
						}
						if(startLt==0 && r.getStaTime()!=null){
							startLt = CalendarUtils.getCalendar(r.getStaTime()).getTimeInMillis();
						}
					}catch(Throwable t){
						t.printStackTrace();
					}
				}
				if(hasEndEvent){
					if(min==0) min = (int)((maxWt-minWt)/1000/60);
				}else if(min==0 && System.currentTimeMillis() - startLt< 1440*60*1000 ){
					min = (int)((System.currentTimeMillis()-startLt)/1000/60);
				}
				rlist.clear();
				rlist = null;
			}
			
			String trmAddr = pile.getPileAddr();
			int portNo = bill.getInnerId();
			if(dl==0 || dl>500 || je==0 || je>1000){
				Double[] vs = csg.getCDQLastDlJe(trmAddr, portNo, seqNo,cardNo);
				if(vs!=null && vs.length==4){
					log.warn("从内存库读取数据:"+Arrays.toString(vs));
					boolean validData = false;
					if(vs[0]>0 && vs[0]<500 && vs[1]>0 && vs[1]<500 )
						validData = true;
					if(validData && vs[0]>dl)
						dl = Double.valueOf(vs[0]);
					if(validData && vs[1]>je)
						je = Double.valueOf(vs[1]);
					if(validData && vs[2].intValue()>0){
						busiType = 1;
						msg = "充电异常停止后，有别的订单启动充电，不下发停止命令.";
						min = vs[3].intValue()/60;
					}
				}
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
		
		boolean readOnly = false;
		if(dl>0 && dl<100 && je>0 && je<500 && !"5".equals(bill.getPayType()) ){
			readOnly = true;
		}
		DecimalFormat df = new DecimalFormat("0.00");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("billPayId", bill.getBillPayId());
		map.put("billPayNo", bill.getBillPayNo());
		map.put("readOnly", readOnly);
		map.put("startTime", CalendarUtils.formatCalendar(bill.getCreateTime()));
		if(min<0 || min>2*1440)
			min = 0;
		map.put("chargeTimes", min);
		map.put("chgPower", Double.valueOf(df.format(dl)));
		map.put("amount", Double.valueOf(df.format(je)));
		map.put("busiType", busiType);
		map.put("msg",msg);
		
		bill = null;
		pile = null;
		
		return map;
	}
	
	/**
	 * @param billPayId
	 * @param busiType    刷卡0不需要下发停止命令  1:app有结束事件，2,app无结束事件需下发停止命令
	 * @param startTime
	 * @param min
	 * @param chgPower
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public String saveAbnormalCharge(final int billPayId,int busiType,Calendar startTime,int min,Double chgPower,Double amount)throws Exception{
		String str="异常结单成功.";
		if(busiType<0 || busiType>2)
			throw new BizException(2103003,"业务处理类型错误.");
		
		Calendar endTime = Calendar.getInstance();
		endTime.setTimeInMillis(startTime.getTimeInMillis());
		endTime.add(Calendar.MINUTE, min);
		int payState = 4;
		if(busiType>0 && chgPower<500 && amount<1000)
			payState = 2;
		
		final BillPay bill = billMap.findById(billPayId);
		final ChgPile pile = billMap.getPileById(bill.getPileId());
		Double dl1 = null,dl2=null,dl3=null,dl4=null;
		Double pr1 = null,pr2=null,pr3=null,pr4=null,prServ=null;
		PubPrice pp = billMap.getPilePrice(bill.getPileId());
		if(pp!=null && pp.getPrcService()!=null){
			prServ = pp.getPrcService();
		}
		if(busiType==2){
			DecimalFormat df = new DecimalFormat("0000000000000000");
			final String cardNo = df.format(billPayId);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						
						if("62".equals(pile.getPileProtocol())){
							List<ChgRecord> rlist = billMap.findChargeRecord(null, bill.getBillPayId().toString());
							if(rlist!=null && !rlist.isEmpty()){
								ChgRecord r = rlist.iterator().next();
								String seq = r.getPileSeqId();
								if(seq!=null && seq.length()==18){
									short randomNo = Short.valueOf(seq.substring(seq.length()-4));
									Calendar c = Calendar.getInstance();
									c.setTimeInMillis(0);
									c.set(Calendar.YEAR, 2000+Integer.valueOf(seq.substring(0, 2)));
									c.set(Calendar.MONTH, Integer.valueOf(seq.substring(2, 4))-1);
									c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(seq.substring(4, 6)));
									c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(seq.substring(6, 8)));
									c.set(Calendar.MINUTE, Integer.valueOf(seq.substring(8, 10)));
									c.set(Calendar.SECOND, Integer.valueOf(seq.substring(10, 12)));
									Double balance = bill.getPreAmount();
									cdzBusiClou.startStopClou(bill.getPileId(), pile.getPileAddr(), bill.getInnerId(), (byte)3, 
											bill.getBillPayId().longValue(), randomNo, c.getTimeInMillis(), balance);
								}
							}
							rlist.clear();
							rlist = null;
						}else{
							cdzBusi.startStopPort(bill.getPileId(), pile.getPileAddr(), bill.getInnerId(), (byte)3, cardNo, null);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			t.start();
			TimeUnit.SECONDS.sleep(10);
			List<ChgRecord> rlist = billMap.findChargeRecord(null, cardNo);
			if(rlist!=null && !rlist.isEmpty()){
				payState = 2;
				ChgRecord r = rlist.iterator().next();
				if(r.getChgType()!=0){
					Double preAmount = r.getPreAmount();
					Double curAmount = r.getCurAmount();
					Double pv = chgPower;
					Double av = amount;
					if(preAmount!=null && curAmount!=null)
						av = AmountUtil.sub(preAmount, curAmount);
					if(r.getCurZxygz()!=null && r.getPreZxygz()!=null)
						pv = AmountUtil.sub(r.getCurZxygz(), r.getPreZxygz());
					if(pv==null && av!=null){
						pv = av/r.getPrcZxygz1();
					}
					if(pv!=null && pv>chgPower && pv>0 && pv<500)
						chgPower = pv;
					if(av!=null && av>amount && av>0 && av<1000)
						amount = av;
					
					if(r.getCurZxygz1()!=null && r.getPreZxygz1()!=null)dl1=r.getCurZxygz1()-r.getPreZxygz1();
					if(r.getCurZxygz2()!=null && r.getPreZxygz2()!=null)dl2=r.getCurZxygz2()-r.getPreZxygz2();
					if(r.getCurZxygz3()!=null && r.getPreZxygz3()!=null)dl3=r.getCurZxygz3()-r.getPreZxygz3();
					if(r.getCurZxygz4()!=null && r.getPreZxygz4()!=null)dl4=r.getCurZxygz4()-r.getPreZxygz4();
				}
				pr1 = r.getPrcZxygz1()!=null?r.getPrcZxygz1():null;
				pr2 = r.getPrcZxygz2()!=null?r.getPrcZxygz2():null;
				pr3 = r.getPrcZxygz3()!=null?r.getPrcZxygz3():null;
				pr4 = r.getPrcZxygz4()!=null?r.getPrcZxygz4():null;
				
				rlist.clear();
				rlist = null;
			}
		}
		if(dl1==null&&dl2==null&&dl3==null&&dl4==null)
			dl3 = chgPower;
		if(prServ!=null && "51".equals(pile.getPileProtocol()) ){  //深圳协议才需要重算服务
			DecimalFormat df = new DecimalFormat("0.00");
			if(pr1!=null){
				pr1 = Double.valueOf(df.format(pr1-prServ));
			}
			if(pr2!=null){
				pr2 = Double.valueOf(df.format(pr2-prServ));
			}
			if(pr3!=null){
				pr3 = Double.valueOf(df.format(pr3-prServ));
			}
			if(pr4!=null){
				pr4 = Double.valueOf(df.format(pr4-prServ));
			}
		}
		String billPayNo = bill.getBillPayNo();
		billPayNo += "E";
		billMap.updateBillPay(billPayId, CalendarUtils.formatCalendar(startTime), CalendarUtils.formatCalendar(endTime), payState, chgPower, amount,dl1,dl2,dl3,dl4,pr1,pr2,pr3,pr4,prServ,billPayNo);
		endTime.clear();
		endTime = null;
		return str;
	}
	
	/**
	 * 待结收入导出
	 * @param map
	 * @param response
	 */
	public void exportDjsr(Map<String,Object> map, HttpServletResponse response)throws Exception {
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("运营商");
		headList.add("订单编号");
		headList.add("消费金额(元)");
		headList.add("充电电量(kW·h)");
		headList.add("运营商名称");
		headList.add("会员名称");
		headList.add("订单状态");
		headList.add("支付方式");
		headList.add("场站名称");
		headList.add("设备编号");
		headList.add("设备名称");
		headList.add("手机号");
		headList.add("订单时间");
		headList.add("开始充电时间");
		headList.add("结束充电时间");
		headList.add("充电时长");
		headList.add("备注");
		
		valList.add("orgName");
		valList.add("billPayNo");
		valList.add("amount");
		valList.add("chgPower");
		valList.add("orgName");
		valList.add("consName");
		valList.add("payState");
		valList.add("payType");
		valList.add("stationName");;
		valList.add("pileNo");
		valList.add("pileName");
		valList.add("consPhone");
		valList.add("createTime");
		valList.add("startTime");
		valList.add("endTime");
		valList.add("useTime");
		valList.add("billDesc");
		
		DataVo vo = new DataVo(map);
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<AbnormalBill> list = queryAbnormalRrcord(vo);
		List<DataVo> dvList= new ArrayList<DataVo>();
		Double dl=0D,je=0D;
		if(list!=null && !list.isEmpty()){
			for(AbnormalBill ab:list){
				if(ab.getChgPower()!=null){
					dl += ab.getChgPower();
				}
				if(ab.getAmount()!=null){
					je += ab.getAmount();
				}
				DataVo e = new DataVo();
				e.add("orgName", ab.getOrgName());
				e.add("billPayNo", ab.getBillPayNo());
				e.add("amount", ab.getAmount() );
				e.add("chgPower", ab.getChgPower() );
				e.add("orgName", ab.getOrgName() );
				e.add("consName", ab.getConsName() );
				e.add("payState", ab.getPayState() );
				e.add("payType", ab.getPayType() );
				e.add("stationName", ab.getStationName() );
				e.add("pileNo", ab.getPileNo() );
				e.add("pileName", ab.getPileName() );
				e.add("consPhone", ab.getConsPhone() );
				e.add("createTime", ab.getCreateTime() );
				e.add("startTime", ab.getStartTime() );
				e.add("endTime", ab.getEndTime() );
				e.add("useTime", ab.getUseTime() );
				e.add("billDesc", ab.getBillDesc() );
				dvList.add(e);
			}
		}
		DataVo count = new DataVo();
		count.put("chgPower",dl);
		count.put("amount",je);
		count.put("billPayNo","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList,response,headList,valList,"待结收入表");
	}
	
	
}

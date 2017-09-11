package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.boot.common.utils.JsonUtils;
import com.clouyun.cdzcache.CacheServiceCdz;
import com.clouyun.cdzcache.imp.CDProcData;
import com.clouyun.cdzcache.imp.CDZStatusGet;
import com.clouyun.cdzcache.obj.LoginRec;
import com.clouyun.cdzcache.obj.QPData;
import com.clouyun.cdzcache.obj.ZPData;
import com.clouyun.cdzcache.util.Utility;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.PileTypeUtils;
import com.clouyun.charge.modules.document.mapper.PileMapper;
import com.clouyun.charge.modules.monitor.mapper.PileMonitorMapper;
import com.clouyun.charge.modules.monitor.vo.ProcessVo;
import com.clouyun.charge.modules.monitor.vo.RealTimeVo;
import com.clouyun.charge.modules.monitor.vo.V1;
import com.clouyun.charge.modules.monitor.vo.V2;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.OrgService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author fft
 *
 */
@Service
public class PileMonitorService {
	
	public static final Logger logger = LoggerFactory.getLogger(PileMonitorService.class);
	
	@Autowired
	private PileMonitorMapper pileMonitorMapper;
	
	@Autowired
	private PileMonitorQueryResultImp pileMonitorQueryResultImp;
	
	@Autowired
	private DictService dictService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrgService orgService;
	
	@Autowired
	private PileMapper pileMapper;
	
	@Autowired
	private CDZStatusGet csg;
	@Autowired
	private CacheServiceCdz csc;
	@Autowired
	private CDProcData cpd;

	private static byte aU = 0, // A枪电压
			aI = 1, 	// A枪电流
			aP = 2, 	// A枪功率
			bU = 3, 	// B枪电压
			bI = 4, 	// B枪电流
			bP = 5, 	// B枪功率
			zU = 6, 	// 总表电压
			zI = 7, 	// 总表电流
			zP = 8, 	// 总表
			aDl = 9,	// A枪电量
			aSr = 10,	// A枪收入
			bDl = 11,	// B枪电量
			bSr = 12,	// B枪收入
			aWd = 13,	// A枪温度
			bWd = 14,	// B枪温度
			aBmsU = 15,	// A枪BMS电压
			aBmsI = 16,	// A枪BMS电流
			bBmsU = 17,	// B枪BMS电压
			bBmsI = 18,	// B枪BMS电流
			AUipTime = 0,	// A枪时间轴
			BUipTime = 1,	// B枪时间轴
			ZUipTime = 2,	// 总表时间轴
			ADlTime = 3,	// A枪电量时间轴
			BDlTime = 4,	// B枪电量时间轴
			ABmsTime = 5,	// A枪BMS时间轴
			BBmsTime = 6,	// B枪BMS时间轴
			AWdTime = 7,	// A枪温度时间轴
			BWdTime = 8;	// B枪温度时间轴

	//场站总收入,当日的收入,总/当日用电量和总/当日充电量
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public Map stationInfoBystationId(Integer stationId){
		Double zsr=0D;//总收入 
		Double rsr=0D;//日收入 
		Double ljcdl=0D;//总充电量
		Double ljhdl=0D;//总用电量
		Double dljcdl=0D;//日充电量
		Double dljhdl=0D;//日用电量
		Integer gunTotal = 0; //总枪数  
		Integer  acPileTotal = 0;//交流 慢
		Integer  dcPileTotal = 0;//直流 快
		Integer  chargingPile = 0;//充电中
		Map resultMap=new HashMap();
		String dateTime=DateUtils.getDate();//得到当前日期字符串 格式（yyyy-MM-dd）
		Map map=new HashMap();
		map.put("stationId", stationId);
		List<Map> pilesMap=pileMonitorMapper.getPilesByStationId(map);//查询场站下所有的桩信息
		Map totalMap= pileMonitorMapper.getStationInfoByStationId(map);//总计
		Integer gunCount=pileMonitorMapper.guncountByStationId(stationId);
		if(totalMap!=null&&!totalMap.isEmpty()){
			zsr=totalMap.get("totalIncome")==null? 0D : Double.valueOf(totalMap.get("totalIncome").toString());
			ljcdl=totalMap.get("chargePower")==null? 0D : Double.valueOf(totalMap.get("chargePower").toString());
			ljhdl=totalMap.get("totalPower")==null? 0D : Double.valueOf(totalMap.get("totalPower").toString());
		}
		
		map.put("dateTime", dateTime);
		Map dayMap= pileMonitorMapper.getStationInfoByStationId(map);//当天
		if(dayMap!=null&&!dayMap.isEmpty()){
			rsr=dayMap.get("totalIncome")==null? 0D : Double.valueOf(dayMap.get("totalIncome").toString());
			dljcdl=dayMap.get("chargePower")==null? 0D : Double.valueOf(dayMap.get("chargePower").toString());
			dljhdl=dayMap.get("totalPower")==null? 0D : Double.valueOf(dayMap.get("totalPower").toString());
		}
		
		resultMap.put("zsr", zsr);
		resultMap.put("rsr", rsr);
		resultMap.put("ljcdl", ljcdl);
		resultMap.put("ljhdl", ljhdl);
		resultMap.put("dljcdl", dljcdl);
		resultMap.put("dljhdl", dljhdl);
		resultMap.put("gunTotal", gunCount);
		
		
		pileMonitorQueryResultImp.fillterData(pilesMap, resultMap);//进一步填充信息
		resultMap.put("pilesMap", pilesMap);

		return resultMap;
	}
	
	
	//设备查询
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getStationAll(Map map) throws BizException {
		List<Map> rusult =new ArrayList<Map>();
		// 条件查询
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		Set<Integer> stationIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.STATION.dataType);
		if(stationIds !=null){
			vo.put("stationIds", stationIds);
		}
		// 分页信息
		if (vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")) {
			PageHelper.startPage(vo);
		}
		// 查询监控场站信息
		List<DataVo> list = pileMonitorMapper.getStationInfos(vo);//己方场站
		if (list != null && list.size() > 0) {
//			// 获取场站Id
//			stationIds = new HashSet<Integer>();
//			for (int i = 0; i < list.size(); i++) {
//				Map maplist = (Map) list.get(i);
//				stationIds.add(Integer.valueOf(maplist.get("stationId")
//						.toString()));
//			}
//			DataVo vo1= new DataVo();
//			vo1.add("stationIds", stationIds);
//			// 通过场站id 查询出充电桩信息 获取充电桩数量
//			int pileTotal = 0;
//			List pileList = pileMapper.getPileCountByStationId(vo1);
			for (int i = 0; i < list.size(); i++) {
				Map stationMap = (Map) list.get(i);
				Integer subId = Integer.valueOf(stationMap.get("stationId")
						.toString());
				DataVo vo2 = new DataVo();
			    vo2.add("stationId", subId);
			    vo2.add("ortMode", map.get("ortMode"));
			    vo2.add("pileModelId", map.get("pileModelId"));
			    int pstatus=map.get("status")==null? -1 :Integer.parseInt(map.get("status").toString());//传入的桩状态
				List<DataVo> piles=pileMonitorMapper.getPiles(vo2);//桩详情页

				
//				for (int j = 0; j < pileList.size(); j++) {
//					Map pileMap = (Map) pileList.get(j);
//					Integer stationId = Integer.valueOf(pileMap
//							.get("stationId").toString());
//					if (subId.intValue() == stationId.intValue()) {
//						pileTotal = Integer.valueOf(pileMap.get("count")
//								.toString());
//						break;
//					}
//				}
//				if(pileTotal==0){
//					continue;
//				}
//				stationMap.put("pileCount", pileTotal);	
				
				//平台枪状态 0 正在充电；1 终端就绪；2 终端故障：3 终端禁用;5放电, 32:掉线
				 //互联互通接口状态 0：离网 ；1：空闲；2：占用（未充电）；3：占用（充电中）；4：占用（预约锁定）；255：故障
				/**
				 *  1空闲2充电3异常4停用5	离线  显示字典10  对应平台 1,0，25 ，3,32
				 */
				CDZStatusGet statusService = csg;
				if(piles!=null&&!piles.isEmpty()){
					for (DataVo chgPile : piles) {
					      if (chgPile.get("pileAddr")!=null||!"".equals(chgPile.get("pileAddr"))){
					    	  byte status = statusService.getCDZStatus(chgPile.get("pileAddr").toString());
					    	  switch (status)
					            {
					            case 0: //2
					            	stationMap.put("chargePile", Integer.valueOf((stationMap.get("chargePile") != null ? Integer.valueOf(stationMap.get("chargePile").toString()).intValue() : 0) + 1));
					              break;
					            case 1: //1
					            	stationMap.put("kxPile", Integer.valueOf((stationMap.get("kxPile") != null ? Integer.valueOf(stationMap.get("kxPile").toString()).intValue() : 0) + 1));
					              break;
					            case 2: //3
					            	stationMap.put("ycPile", Integer.valueOf((stationMap.get("ycPile") != null ? Integer.valueOf(stationMap.get("ycPile").toString()).intValue() : 0) + 1));
					              break;
					            case 3: //4
					            	stationMap.put("tyPile", Integer.valueOf((stationMap.get("tyPile") != null ? Integer.valueOf(stationMap.get("tyPile").toString()).intValue() : 0) + 1));
					              break;
					            case 5: //3
					            	stationMap.put("ycPile", Integer.valueOf((stationMap.get("ycPile") != null ? Integer.valueOf(stationMap.get("ycPile").toString()).intValue() : 0) + 1));
					              break;
					            case 32: //5
					            	stationMap.put("offlinePile", Integer.valueOf((stationMap.get("offlinePile") != null ? Integer.valueOf(stationMap.get("offlinePile").toString()).intValue() : 0) + 1));
					            }
					      } 
					      /**
							 * 设备类型
							 *  1	交流充电桩
								2	直流充电桩
								3	交直流一体充电桩
							 */
					      if(chgPile.get("ortMode")!=null && !"".equals(chgPile.get("ortMode"))){
								switch (Integer.parseInt(chgPile.get("ortMode").toString())){
								case 1 : 
									stationMap.put("acPile", Integer.valueOf((stationMap.get("acPile") != null ? Integer.valueOf(stationMap.get("acPile").toString()).intValue() : 0) + 1));
								break;
								case 2 : 
									stationMap.put("dcPile", Integer.valueOf((stationMap.get("dcPile") != null ? Integer.valueOf(stationMap.get("dcPile").toString()).intValue() : 0) + 1));
								break;
								case 3 : 
									stationMap.put("acDcPile", Integer.valueOf((stationMap.get("acDcPile") != null ? Integer.valueOf(stationMap.get("acDcPile").toString()).intValue() : 0) + 1));
								break;
								}
							}
					        
				      }  
				}
			      
			           
				rusult.add(stationMap);		
			}
		}
		
		//第三方场站信息
		//条件查询时运营状态转换
		if(vo.isNotBlank("stationStatus")){
			vo.add("stationStatus", stationStatus2Interface(Integer.parseInt(vo.get("stationStatus").toString())));
		}
		List<DataVo> toList=pileMonitorMapper.getToStationInfos(vo);
		if (toList != null && toList.size() > 0) {
			// 获取场站Id
			Set<String> stationIds1 = new HashSet<String>();
			for (int i = 0; i < toList.size(); i++) {
				Map maplist = (Map) toList.get(i);
				stationIds1.add(maplist.get("stationId").toString());
			}
			DataVo vo1= new DataVo();
			vo1.add("stationIds", stationIds1);
			// 通过场站id 查询出充电桩信息 获取充电桩数量
			int pileTotal = 0;
			List pileList =pileMonitorMapper.getToPileCountByStationId(vo1);
			for (int i = 0; i < toList.size(); i++) {
				//互联互通运营状态转换				
				Map stationMap = (Map) toList.get(i);
				if(stationMap.get("stationStatus")!=null&&!"".equals(stationMap.get("stationStatus"))){
					stationMap.put("stationStatus", stationStatus3Interface(Integer.parseInt(stationMap.get("stationStatus").toString())));
				}
				if(stationMap.get("stationType") != null && !"".equals(stationMap.get("stationType"))){
					if(Integer.valueOf(stationMap.get("stationType").toString()) == 1){
						stationMap.put("stationType","-1");
					}
				}
				String subId =stationMap.get("stationId").toString();
				DataVo vo2 = new DataVo();
			    vo2.add("stationId", subId);
			    vo2.add("ortMode", map.get("ortMode"));
			    vo2.add("pileModelId", map.get("pileModelId"));
			    int pstatus=map.get("status")==null? -1 :Integer.parseInt(map.get("status").toString());//传入的枪状态
				List<DataVo> piles=pileMonitorMapper.getToPilesByStationId(vo2);//桩详情页
				for (int j = 0; j < pileList.size(); j++) {
					Map pileMap = (Map) pileList.get(j);
					String stationId = pileMap.get("stationId").toString();
					if (subId .equals(stationId)) {
						pileTotal = Integer.valueOf(pileMap.get("count")
								.toString());
						break;
					}
				}
				
				if(pileTotal==0){
					continue;
				}
				
				stationMap.put("pileCount", pileTotal);	
				
				//平台枪状态 0 正在充电；1 终端就绪；2 终端故障：3 终端禁用;5放电, 32:掉线
				 //互联互通接口状态 0：离网 ；1：空闲；2：占用（未充电）；3：占用（充电中）；4：占用（预约锁定）；255：故障
				/**
				 *  1空闲2充电3异常4停用5	离线  显示字典  对应平台 1,0，25 ，3,32
				 */
				
				if(piles!=null&&!piles.isEmpty()){
					for (DataVo chgPile : piles) {
						String pileId=chgPile.get("pileId").toString();
						List<DataVo> clist=pileMonitorMapper.getToConnectorinfosByPileId(pileId);
					    	  byte status = getEquipmentStatus(clist);
					    	  switch (status)
					            {
					            case 0: //5
					            	stationMap.put("offlinePile", Integer.valueOf((stationMap.get("offlinePile") != null ? Integer.valueOf(stationMap.get("offlinePile").toString()).intValue() : 0) + 1));
					              break;
					            case 1: //1
					            	stationMap.put("kxPile", Integer.valueOf((stationMap.get("kxPile") != null ? Integer.valueOf(stationMap.get("kxPile").toString()).intValue() : 0) + 1));
					              break;
					            case 2: //2
					            	stationMap.put("chargePile", Integer.valueOf((stationMap.get("chargePile") != null ? Integer.valueOf(stationMap.get("chargePile").toString()).intValue() : 0) + 1));
					              break;
					            case 3: //2
					            	stationMap.put("chargePile", Integer.valueOf((stationMap.get("chargePile") != null ? Integer.valueOf(stationMap.get("chargePile").toString()).intValue() : 0) + 1));
					              break;
					            case 4: //2
					            	stationMap.put("chargePile", Integer.valueOf((stationMap.get("chargePile") != null ? Integer.valueOf(stationMap.get("chargePile").toString()).intValue() : 0) + 1));
					              break;
					            case -1: //3
					            	stationMap.put("ycPile", Integer.valueOf((stationMap.get("ycPile") != null ? Integer.valueOf(stationMap.get("ycPile").toString()).intValue() : 0) + 1));
					              break;
								default :
									stationMap.put("offlinePile", Integer.valueOf((stationMap.get("offlinePile") != null ? Integer.valueOf(stationMap.get("offlinePile").toString()).intValue() : 0) + 1));
					            }
					      /**
							 * 设备类型
							 *  1	单交流充电桩
								2	单直流充电桩
								3	交直流充电桩
							 */
					      if(chgPile.get("ortMode")!=null||!"".equals(chgPile.get("ortMode"))){
								switch (Integer.parseInt(chgPile.get("ortMode").toString())){
								case 1 : 
									stationMap.put("acPile", Integer.valueOf((stationMap.get("acPile") != null ? Integer.valueOf(stationMap.get("acPile").toString()).intValue() : 0) + 1));
								break;
								case 2 : 
									stationMap.put("dcPile", Integer.valueOf((stationMap.get("dcPile") != null ? Integer.valueOf(stationMap.get("dcPile").toString()).intValue() : 0) + 1));
								break;
								case 3 : 
									stationMap.put("acDcPile", Integer.valueOf((stationMap.get("acDcPile") != null ? Integer.valueOf(stationMap.get("acDcPile").toString()).intValue() : 0) + 1));
								break;

									
								}
							}
					        
				      }  
				
			}
			           
				rusult.add(stationMap);		
			}
		}
		
		
		
		
		
		return rusult;
	}
	
	/**
	 * 场站运营状态对应互联互通接口值,平台换互联
	 * 
	 * 
	 * @param status
	 * @return
	 */
	private static int stationStatus2Interface(int status) {
		int interfaceStatus;
		switch (status) {
		case 2:
			interfaceStatus = 5;
			break;
		case 3:
			interfaceStatus = 6;
			break;
		default:
			interfaceStatus = status;
			break;
		}
		return interfaceStatus;
	}
	
	/**
	 * 场站运营状态对应互联互通接口值，互联换平台
	 * 
	 * 
	 * @param status
	 * @return
	 */
	private static int stationStatus3Interface(int status) {
		int interfaceStatus;
		switch (status) {
		case 5:
			interfaceStatus = 2;
			break;
		case 6:
			interfaceStatus = 3;
			break;
		default:
			interfaceStatus = status;
			break;
		}
		return interfaceStatus;
	}
	
	//获取桩状态
	public byte getEquipmentStatus(List<DataVo> clist)
	  {
	    if ((clist == null) || (clist.isEmpty())) {
	      return 0;
	    }
	    byte status = 0;
	    Set<String> keys = new HashSet();
	    for (DataVo c : clist) {
	      keys.add(c.get("operatorId") + "_" + c.get("connectorId"));
	    }
	    try
	    {
	      Map<String, byte[]> map = csc.get(keys);
	      if (map != null) {
	        for (String key : map.keySet())
	        {
	          int qs = Utility.hexToInt(((byte[])map.get(key))[0]);
	          if ((qs >= 2) && (qs <= 4))
	          {
	            status = 2;
	            break;
	          }
	          status = 1;
	        }
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      logger.info("查询互联互通站状态报错:" + e.getMessage());
	    }
	    return status;
	  }

	/**
	 * 获取充电桩过程数据
	 *
	 * @param pileId
	 * @return
	 * @throws BizException
	 */
	public ProcessVo chargeProcessData(Integer pileId) throws BizException {
		Map pile = pileMapper.getPileById(new DataVo().add("pileId", pileId));
		DataVo pileVo = new DataVo(pile);
		if (pile == null || pile.isEmpty())
			throw new BizException(1000015, "充电桩");
		ProcessVo processVo = new ProcessVo();
		String pileAddr = pileVo.getString("pileAddr");
		int ortMode = pileVo.getInt("ortMode");
		ZPData data = cpd.getCdzProcData((byte) ortMode, pileId, pileAddr);
		LinkedHashMap<Long, LoginRec> xt = data.getLoginMap();
		List<Map<String, String>> maps = xtMapToList(xt, true);
		// 如果当前桩未处于充电状态，则只返回心跳信息
		int arr[] = data.getCurDayOnlineStat();
		processVo.getOnline().setHearBeat(maps);
		processVo.getOnline().setOnline(arr[0] + "%");
		processVo.getOnline().setLostLine(arr[1] + "次");
		if (csg.getCDZStatus(pileAddr) != 0) {
			return processVo;
		}
		processVo.setCharging(true);
		Map<String, Integer> innerNoMap = PileTypeUtils.getGunInnerId(pileVo.getInt("numberGun"), pileVo.getInt("ortMode"));
		if (innerNoMap == null || innerNoMap.isEmpty()) {
			throw new BizException(1000015, "充电桩类型");
		}
		if (ortMode == 1 || ortMode == 3) {// 交流或者交直流(交直流算交流)
			processVo.setType("1");
			SortedMap<Long, QPData> djaMap;
			for (Map.Entry<String, Integer> entry : innerNoMap.entrySet()) {
				djaMap = data.getQPDate(entry.getValue());
				if (mapIsNotNull(djaMap)) {
					organize1(djaMap, processVo, "充电枪".concat(entry.getKey()));
				}
			}
		} else if (ortMode == 2) {// 直流
			processVo.setType("2");
			SortedMap<Long, QPData> dzaMap;
			for (Map.Entry<String, Integer> entry : innerNoMap.entrySet()) {
				dzaMap = data.getQPDate(entry.getValue());
				if (mapIsNotNull(dzaMap)) {
					if ("Z".equals(entry.getKey())) {
						organize5(dzaMap, processVo, "总表");
					} else {
						organize2(dzaMap, processVo, "充电枪".concat(entry.getKey()));
					}
				}
			}
		}
		Double charge[] = getChargeInfo(pileId);
		processVo.getIncome().setTodayAmount(charge[0]);
		processVo.getIncome().setTotalAmount(charge[1]);
		processVo.getIncome().setTotalPower(charge[2]);
		return processVo;
	}

	public static void main(String[] args) {
		System.out.println(DateUtils.formatDate(new Date(),"yyyy-MM-01"));
		System.out.println(DateUtils.formatDate(new Date()));
		ProcessVo processVo = new ProcessVo();
		V2 v4 = new V2();
		v4.setGunName("充电枪01");
		List<Double> a = Lists.newArrayList(123.13,123.1,123.1,123.1,123.1,123.1);
		List<String> b = Lists.newArrayList("2017-06-05 12:32:12","2017-06-05 12:32:12","2017-06-05 12:32:12","2017-06-05 12:32:12","2017-06-05 12:32:12","2017-06-05 12:32:12");
		v4.setVar2(a);
		v4.setVar1(a);
		v4.setTime(b);
		V2 v5 = new V2();
		v5.setGunName("充电枪02");
		v5.setVar2(a);
		v5.setVar1(a);
		v5.setTime(b);
		processVo.getIncome().getSr().add(v4);
		processVo.getIncome().getSr().add(v5);
		System.out.println(JsonUtils.toJson(processVo));
	}

	/**
	 * 统计订单表 今日收入、月总收入、月总充电量
	 *
	 * @param trmId
	 * @return[今日收入，历史总收入，历史总充电量]
	 */
	private Double[] getChargeInfo(int trmId) {
		// TODO Auto-generated method stub
		Double arr[] = new Double[]{0D, 0D, 0D};
		Date date = new Date();
		String nextDay = DateUtils.formatDate(DateUtils.addDays(date, 1));
		DataVo pay = pileMonitorMapper.getMonthTotalPayMoneyPower(new DataVo().add("pileId", trmId).add("startDate", DateUtils.formatDate(date,"yyyy-MM-01")).add("endDate",nextDay));
		// 今日总收入
		Double todayAmount = pileMonitorMapper.getTotalPayMoneyToday(new DataVo().add("pileId", trmId).add("startDate", DateUtils.formatDate(date)).add("endDate", nextDay));
		if (pay != null && !pay.isEmpty()) {
			// 历史总收入
			arr[1] = pay.getDouble("amount");
			// 历史总充电量
			arr[2] = pay.getDouble("chgPower");
		}
		if (todayAmount != null)
			arr[0] = todayAmount;
		return arr;
	}

	/**
	 * 交流枪
	 * 所有相关联数据统计一条完整相关的数据
	 *
	 * @param map
	 * @param processVo
	 * @param gunName
	 */
	private void organize1(SortedMap<Long, QPData> map, ProcessVo processVo, String gunName) {
		V1 vu = new V1(gunName);// 充电监测数据电压
		V1 vi = new V1(gunName);// 充电监测数据电流
		V1 vp = new V1(gunName);// 充电监测数据功率
		V2 vsr = new V2(gunName);// 收入监控数据
		QPData qpData;
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			qpData = entry.getValue();
			Double u = qpData.getU();
			Double a = qpData.getI();
			Double p = qpData.getP();
			Double dL = qpData.getDl();
			Double je = qpData.getJe();
			String time = longToDate(entry.getKey());//数据时标
			if (null != u && null != a && null != p) {
				vu.getProcess().add(u);
				vi.getProcess().add(a);
				vp.getProcess().add(p);
				vu.getTime().add(time);
				vi.getTime().add(time);
				vp.getTime().add(time);
			}
			if (null != dL && null != je) {
				vsr.getVar1().add(dL);
				vsr.getVar2().add(je);
				vsr.getTime().add(time);
			}
		}
		RealTimeVo realTimeVo = processVo.getCharge();
		realTimeVo.getU().add(vu);
		realTimeVo.getI().add(vi);
		realTimeVo.getP().add(vp);
		processVo.getIncome().getSr().add(vsr);
	}

	/**
	 * 直流枪
	 * 所有相关联数据统计一条完整相关的数据
	 * @param map
	 * @param processVo
	 * @param gunName
	 */
	private void organize2(SortedMap<Long, QPData> map, ProcessVo processVo, String gunName) {
		V1 vu = new V1(gunName);// 充电监测数据电压
		V1 vi = new V1(gunName);// 充电监测数据电流
		V1 vp = new V1(gunName);// 充电监测数据功率
		V2 vsr = new V2(gunName);// 收入监控数据
		V2 viu = new V2(gunName);
		V2 vws = new V2(gunName);// 收入监控数据
		QPData qpData;
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			qpData = entry.getValue();
			Double u = qpData.getU();
			Double a = qpData.getI();
			Double p = qpData.getP();
			Double ti = qpData.getTi();
			Double tu = qpData.getTu();
			Double dl = qpData.getDl();
			Double je = qpData.getJe();
			Double w = qpData.getTmp();
			Double soc = qpData.getSoc();
			String time = longToDate(entry.getKey());//数据时标
			if (null != u && null != a && null != p) {
				vu.getProcess().add(u);
				vi.getProcess().add(a);
				vp.getProcess().add(p);
				vu.getTime().add(time);
				vi.getTime().add(time);
				vp.getTime().add(time);
			}
			if (null != dl && null != je) {
				vsr.getVar1().add(dl);
				vsr.getVar2().add(je);
				vsr.getTime().add(time);
			}
			if (null != w && null != soc) {
				vws.getVar1().add(w);
				vws.getVar2().add(soc);
				vws.getTime().add(time);
			}
			if (null != ti && null != tu) {
				viu.getVar1().add(ti);
				viu.getVar2().add(tu);
				viu.getTime().add(time);
			}
		}
		RealTimeVo realTimeVo = processVo.getCharge();
		realTimeVo.getU().add(vu);
		realTimeVo.getI().add(vi);
		realTimeVo.getP().add(vp);
		processVo.getIncome().getSr().add(vsr);
		processVo.getBms().getIu().add(viu);
		processVo.getBms().getWs().add(vws);
	}

	/**
	 * 交流B枪
	 * 所有相关联数据统计一条完整相关的数据
	 *
	 * @param map
	 * @param lists
	 */
	private void organize3(SortedMap<Long, QPData> map, List<List<Double>> lists, List<List<String>> listTime) {
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			Double u = entry.getValue().getU();
			Double a = entry.getValue().getI();
			Double p = entry.getValue().getP();
			Double dL = entry.getValue().getDl();
			Double je = entry.getValue().getJe();
			Double w = entry.getValue().getTmp();
			String time = longToDate(entry.getKey());//数据时标
			if (null != u && null != a && null != p) {
				lists.get(bU).add(u);
				lists.get(bI).add(a);
				lists.get(bP).add(p);
				listTime.get(BUipTime).add(time);
			}
			if (null != dL && null != je) {
				lists.get(bDl).add(dL);
				lists.get(bSr).add(je);
				listTime.get(BDlTime).add(time);

			}
			if (null != w) {
				lists.get(bWd).add(w);
				listTime.get(BWdTime).add(time);
			}
		}
	}

	/**
	 * 直流B枪
	 * 所有相关联数据统计一条完整相关的数据
	 *
	 * @param map
	 * @param lists
	 */
	private void organize4(SortedMap<Long, QPData> map, List<List<Double>> lists, List<List<String>> listTime) {
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			Double u = entry.getValue().getU();
			Double a = entry.getValue().getI();
			Double p = entry.getValue().getP();
			Double ti = entry.getValue().getTi();
			Double tu = entry.getValue().getTu();
			Double dL = entry.getValue().getDl();
			Double je = entry.getValue().getJe();
			Double w = entry.getValue().getTmp();
			String time = longToDate(entry.getKey());//数据时标
			if (null != u && null != a && null != p) {
				lists.get(bU).add(u);
				lists.get(bI).add(a);
				lists.get(bP).add(p);
				listTime.get(BUipTime).add(time);
			}
			if (null != dL && null != je) {
				lists.get(bDl).add(dL);
				lists.get(bSr).add(je);
				listTime.get(BDlTime).add(time);
			}
			if (null != w) {
				lists.get(bWd).add(w);
				listTime.get(BWdTime).add(time);
			}
			if (null != ti && null != tu) {
				lists.get(bBmsI).add(ti);
				lists.get(bBmsU).add(tu);
				listTime.get(BBmsTime).add(time);
			}
		}
	}

	/**
	 * 总表
	 * @param map
	 * @param processVo
	 * @param gunName
	 */
	private void organize5(SortedMap<Long, QPData> map, ProcessVo processVo, String gunName) {
		V1 vu = new V1(gunName);// 充电监测数据电压
		V1 vi = new V1(gunName);// 充电监测数据电流
		V1 vp = new V1(gunName);// 充电监测数据功率
		QPData qpData;
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			qpData = entry.getValue();
			Double u = qpData.getU();
			Double a = qpData.getI();
			Double p = qpData.getP();
			String time = longToDate(entry.getKey());//数据时标
			if (null != u && null != a && null != p) {
				vu.getProcess().add(u);
				vi.getProcess().add(a);
				vp.getProcess().add(p);
				vu.getTime().add(time);
				vi.getTime().add(time);
				vp.getTime().add(time);
			}
		}
		RealTimeVo realTimeVo = processVo.getUse();
		realTimeVo.getU().add(vu);
		realTimeVo.getI().add(vi);
		realTimeVo.getP().add(vp);
	}

	private static Double isEmpty(Object d1, Double d2) {
		if (null != d1)
			return Double.valueOf(d1.toString());
		return d2;
	}

	private List<Map<String, String>> xtMapToList(LinkedHashMap<Long, LoginRec> xt, boolean valid) {
		List<Map<String, String>> mapList = new ArrayList<>();
		if (xt != null && !xt.isEmpty()) {
			for (Map.Entry<Long, LoginRec> entry : xt.entrySet()) {
				if (null != entry.getKey()) {
					LoginRec lr = entry.getValue();
					if (valid && !lr.isValid())
						continue;
					Map<String, String> map = new HashMap<>();
					map.put("sj", longToDate(entry.getKey()));
					map.put("ms", lr.toString());
					mapList.add(map);
				}
			}
		}
		return mapList;
	}

	/**
	 * long类型时间转换成字符串时间
	 *
	 * @param l
	 * @return
	 */
	private static String longToDate(Long l) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(l);
		return format.format(date);
	}

	/**
	 * 判断map是否为空
	 *
	 * @param map
	 * @return
	 */
	private static boolean mapIsNotNull(SortedMap<Long, QPData> map) {
		return map != null && !map.isEmpty();
	}
}

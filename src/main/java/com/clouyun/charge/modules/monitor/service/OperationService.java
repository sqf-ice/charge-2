package com.clouyun.charge.modules.monitor.service;


import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.BigDecimalUtils;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.cdzcache.imp.CDZStatusGet;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.modules.monitor.mapper.OperationMapper;
import com.clouyun.charge.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


/**
 * 
 * @author fuft
 *
 */
@Service
@SuppressWarnings("rawtypes")
public class OperationService {
	
	@Autowired
	private OperationMapper operationMapper;
	
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private CDZStatusGet csg;
	
	@SuppressWarnings("unchecked")
	public Map queryStation(Integer userId)throws BizException{
		Map<String, Integer> map=new HashMap<String, Integer>();
		Date time=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
		String createTime=sf.format(time)+"%";
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
		Map map2=new HashMap();
		map2.put("createTime", createTime);
		map2.put("orgIds", orgIds);
		List<Integer> list=operationMapper.getStationConstruction(map2);
		map.put("stationCount",list.get(0));
		map.put("pileCount",list.get(1));
		map.put("newStation",list.get(2));
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map queryCcons(Integer userId)throws BizException{
		Map map=new HashMap();
		Date time=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
		String regTime=sf.format(time)+"%";
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
		Map map2=new HashMap();
		map2.put("regTime", regTime);
		map2.put("orgIds", orgIds);
		List<Integer> list=operationMapper.getCcons(map2);
		map.put("allCons",list.get(0));//全部用户
		map.put("chgCons", list.get(1));//产生订单用户
		map.put("newCons",list.get(2));//新增用户
		
		//转化率
		double chgPro1=0d;
		 if (list.get(0) > 0) {
			 chgPro1=list.get(1)/(double)list.get(0);
		 }
		 
		 if(chgPro1>1){
			 chgPro1=1;
		 }
		 
		String chgPro = String.format("%.2f", 100*chgPro1)+"%";
		map.put("chgPro", chgPro);//转化率
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map queryIncomeInfo(Integer userId)throws BizException{
		Map map=new HashMap();
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
		Map map2=new HashMap();
		map2.put("orgIds", orgIds);
		List<Map> top3=operationMapper.getIncomeInfo(map2);
		double amount=operationMapper.getIncomeCount(map2);
		map.put("countAmount", amount);
		map.put("tops", top3);
		
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map queryInitData(Integer userId)throws BizException{
		Map map=new HashMap();
		Date time=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
		String endTime=sf.format(time)+"%";
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
		Map map2=new HashMap();
		map2.put("endTime", endTime);
		map2.put("orgIds", orgIds);
		List<Double> list=operationMapper.getInitData(map2);
		map.put("currCount", (int)list.get(0).doubleValue());//本月服务次数
		map.put("currPower", list.get(1).toString());//本月累计充电量
		map.put("currAmount", list.get(2).toString());//本月累计收入
		map.put("svCount", (int)list.get(3).doubleValue());//总服务次数
		map.put("carCount", (int)list.get(4).doubleValue());//服务车辆	
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map queryRade(Integer userId)throws BizException{//增长率
		Map map=new HashMap();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
		String preMonth=sf.format(calendar.getTime())+"%";//上个月 %
		calendar.add(Calendar.MONTH, -1);
		String pre2Month = sf.format(calendar.getTime())+"%";//上上个月%
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
		Map map2=new HashMap();
		map2.put("endTime", preMonth);
		map2.put("orgIds", orgIds);
		List<Map> list=operationMapper.getMoneyByMonth(map2);//上一个月各省的收入
		
		Map map3=new HashMap();
		map3.put("endTime", pre2Month);
		map3.put("orgIds",orgIds );
		List<Map> list1=operationMapper.getMoneyByMonth(map3);//上上一个月各省的收入
		
		Map map4=new HashMap();
		map4.put("endTime1", preMonth);
		map4.put("endTime2", pre2Month);
		map4.put("orgIds", orgIds);
		List<Double> list2=operationMapper.getAountByMonth(map4);//[0]：上个月总收入 [1]：上上个月总收入
		
		double preSum = Double.parseDouble(list2.get(0).toString());//上个月总收入
		double pre2Sum = Double.parseDouble(list2.get(1).toString());//上上个月总收入
		// 上个月比上上个月总收入的增长率
		double sca = 0D;
		double sumAdd = 0D;//总增长
		if(preSum>0&&pre2Sum>0){
			sumAdd =preSum-pre2Sum ;
			sca = sumAdd/pre2Sum;
		}
		
		String sumScale = String.format("%.2f", 100*sca)+"%";
		map.put("sumScale",sumScale);
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>(3);
		Map<String, Object> map5 = null;
		if(!list.isEmpty()&&!list1.isEmpty()){
			for (Map map6 : list) {
				for (Map map7 : list1) {
					double d = Double.parseDouble(map6.get("amount").toString());// 上个月收入
					if (map6.get("code").toString().equals(map7.get("code"))) {
						map5 = new HashMap<String, Object>();
						double m=Double.parseDouble(map7.get("amount").toString());//上上个月收入
						double inc = d-m;//增长金额
						 map5.put("prov", map6.get("code").toString());// 省份名称
						map5.put("incAdd", String.format("%.2f", 100*inc));// 增长金额
						String rade=null;
						if(map7.get("amount").toString()!=null){
							rade=String.format("%.2f", 100*(inc/d))+"%";
						}
						
						map5.put("scale", rade);//增长率
						list3.add(map5);
					}
				}
			}
			
			listSort(list3);
			map.put("top3", list3.subList(0, list3.size()>3?3:list3.size()));
		}
		
		
		return map;
	}
	
	//对结果集根据增长金额降序排序
		private void listSort(List<Map<String, Object>> list3) {
			Collections.sort(list3, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> m1, Map<String, Object> m2) {
					Double d1 = Double.parseDouble(m1.get("incAdd").toString());
					Double d2 = Double.parseDouble(m2.get("incAdd").toString());
					return d2.compareTo(d1);
				}
			});
		}
		
		//查询场站利用率
		@SuppressWarnings({ "unused", "unchecked" })
		public Map queryUseRade(Integer stationId)throws BizException{
			//Set<Integer> orgIds = AuthService.getOrgsByUserId();//鉴权方法
			//通过orgids获取所有的场站stationIds
			Set<String> zSet = new HashSet<String>();//直流
			zSet.add("2");
			zSet.add("4");

			Set<String> jSet = new HashSet<String>();//交流
			jSet.add("1");
			jSet.add("3");
			Map map2 =new HashMap();
			Map map3 =new HashMap();
			map2.put("stationId", stationId);
			map2.put("pileTypes", zSet);
			Set<String> zAddrSet =operationMapper.getPilesByStationId(map2);
			map3.put("stationId", stationId);
			map3.put("pileTypes", jSet);
			Set<String> jAddrSet=operationMapper.getPilesByStationId(map3);
			int[] useRate = csg.getStationUserRate(zAddrSet, jAddrSet);
			Map map4 =new HashMap();
			map4.put("zRate", useRate[1]);
			map4.put("jRate", useRate[2]);
			map4.put("totalRate", useRate[0]);

			return map4;
			
		}
		
		//查询告警数量和告警修复数
		@SuppressWarnings("unchecked")
		public Map queryWarm(Integer stationId)throws BizException{
			//Set<Integer> orgIds = AuthService.getOrgsByUserId();//鉴权方法
			//通过orgids获取所有的场站stationIds
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
			String preMonth=sf.format(calendar.getTime())+"%";//当月
			Integer warnCount = 0;
			Integer repairCount = 0;
			Map map1 = new HashMap();
			map1.put("stationId", stationId);
			map1.put("dataTime", preMonth);
			warnCount=operationMapper.getWarmByStationId(map1);//告警数量
			map1.put("handleFlag", "1");
			repairCount=operationMapper.getWarmByStationId(map1);//修复数量
			Map map2 = new HashMap();
			map2.put("warnCount", warnCount);
			map2.put("repairCount", repairCount);
			return map2;	
		}
		
		//查询损耗量
		@SuppressWarnings("unchecked")
		public Map queryLoss(Integer stationId)throws BizException{
			//Set<Integer> orgIds = AuthService.getOrgsByUserId();//鉴权方法
			//通过orgids获取所有的场站stationIds
			Map map1=new HashMap();
			map1.put("stationId", stationId);
			// 查询staionLoss的综合损耗
			Map totalSl =new HashMap();
			totalSl=operationMapper.getLossByStationId(map1);
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			String preDay=sf.format(calendar.getTime());//昨日
			// 查询staionLoss的综合损耗
			map1.put("slDate", preDay);
			Map ysl=new HashMap();
			ysl=operationMapper.getLossByStationId(map1);

			// 累计损耗
			Double totalLoss = 0d;
			// 场站总用电量
			Double totalElec = 0d;

			// 场站昨日损耗
			Double yLoss = 0d;

			// 场站昨日用电量
			Double yElec = 0d;

			// 定义累计损耗率和昨日损耗率
			String tlRate = "0";
			String ylRate = "0";
			if (!totalSl.isEmpty()) {
				if (totalSl.get("loss")!=null) {
					totalLoss = Double.valueOf(totalSl.get("loss").toString()) ;
				}

				if (totalSl.get("elec")!=null) {
					totalElec = Double.valueOf(totalSl.get("elec").toString());
					if (totalElec == 0d) {
						// 如果查询累计的出场站用电量为0,则场站用电量为场站充电用电量
						if (totalSl.get("chg")!=null) {
							totalElec = Double.valueOf(totalSl.get("elec").toString());
						}

					}

				}
			}

			if (!ysl.isEmpty()) {

				if (ysl.get("loss")!=null) {
					yLoss = Double.valueOf(totalSl.get("loss").toString()) ;
				}

				if (ysl.get("elec")!=null) {

					yElec = Double.valueOf(totalSl.get("elec").toString());

					if (yElec == 0d) {
						// 如果查询昨日累计的出场站用电量为0,则场站用电量为场站充电用电量
						if (ysl.get("chg")!=null) {
							yElec = Double.valueOf(totalSl.get("elec").toString());
						}

					}

				}
			}

			if (totalElec != 0d) {
				tlRate =String.format("%.2f", 100*(totalLoss/totalElec))+"%";
			}

			if (yElec != 0d) {
				ylRate =String.format("%.2f", 100*(yLoss/yElec))+"%";
			}

			Map map2=new HashMap();
			map2.put("totalLoss", totalLoss);
			map2.put("totalElec", totalElec);
			map2.put("tlRate", tlRate);
			map2.put("ylRate", ylRate);
	
			return map2;
			
		}
		
		//查询充电桩使用率排行
		@SuppressWarnings("unchecked")
		public List<Map> queryPileUseRate(Integer stationId)throws BizException{
			//Set<Integer> orgIds = AuthService.getOrgsByUserId();//鉴权方法
			//通过orgids获取所有的场站stationIds
			List<Map> pilesRate=new ArrayList<Map>();
			Map map1 = new HashMap();
			map1.put("stationId", stationId);
			Set<String> pileAddrs =operationMapper.getPilesByStationId(map1);
			// 调用接口,返回桩的使用率(按使用率降序排序)
			LinkedHashMap<String, Double> userRates = csg.getCDZUserRate(pileAddrs, (byte) 2);
			if(userRates!=null&&!userRates.isEmpty()){
				List<Map> piles=operationMapper.getPileNamesByStationId(map1);
				Map<String, Object> map2 = new LinkedHashMap<String, Object>();
				// 计数,页面展示五个桩
				for (Map<String,String>  map3 : piles) {
					Double rate = userRates.get(map3.get("pileAddr"));
					// 把桩名和使用率返回给页面
					map2.put(map3.get("pileName"), rate);
				}
				// 这里将map.entrySet()转换成list
				Set<Entry<String, Object>> entries = map2.entrySet();
				List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(
						entries);
				// 然后通过比较器来实现排序
				Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
					// 降序排序
					public int compare(Entry<String, Object> o1,
							Entry<String, Object> o2) {
						if ((Double) o1.getValue() < (Double) o2.getValue()) {
							return 1;
						} else if ((Double) o1.getValue() == (Double) o2.getValue()) {
							return 0;
						} else {
							return -1;
						}
					}
				});
				
				// 经过降序排序之后获取前五个给页面展示
				int number = 0;
				for (Map.Entry<String, Object> mapping : list) {
					Map<String, Object> maps = new LinkedHashMap<>();
					maps.put("pileName",mapping.getKey());
					maps.put("PileRate", String.format("%.2f", 100*((Double)mapping.getValue()))+"%");
					pilesRate.add(maps);
					number++;
					if (number == 5) {
						break;
					}
				}
		
			}
			
			return pilesRate;
			
		}	
		
		//查询充电桩损耗率排行
		@SuppressWarnings("unchecked")
		public List<Map> queryPileLoss(Integer stationId)throws BizException{
			//Set<Integer> orgIds = AuthService.getOrgsByUserId();//鉴权方法
			//通过orgids获取所有的场站stationIds
			List<Map> pilesLossRate=new ArrayList<Map>();
			Map map1=new HashMap();
			map1.put("stationId", stationId);
			//仅仅获取桩名称
			List<Map> noMonthList = operationMapper.getPileLossByStationId(map1);
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
			String plMonth=sf.format(calendar.getTime());//获取当月份
			map1.put("plMonth", plMonth);
			//这是添加了按月查询的
			List<Map> plList=operationMapper.getPileLossByStationId(map1);
			// 定义一个数组用来存放桩名称
			String[] plNameArray ;
			Double[] plValueArray;
			//用map来封装数据,便于排序
			Map<String,Double> plMap = new LinkedHashMap<String,Double>();
			
			if(plList != null && plList.size() == 0){
				plNameArray = new String[noMonthList.size()];
				plValueArray = new Double[noMonthList.size()];

				for (int i = 0; i < noMonthList.size(); i++) {
					plNameArray[i] = noMonthList.get(i).get("pileName").toString();
					plValueArray[i] = 0d;
					
					plMap.put(plNameArray[i],plValueArray[i] );
				}
			}else{
				plNameArray = new String[plList.size()];
				plValueArray = new Double[plList.size()];
				for (int i = 0; i < plList.size(); i++) {
					plNameArray[i] = plList.get(i).get("pileName").toString();
					Double tl = Double.parseDouble(plList.get(i).get("totalLoss").toString());
					Double te = Double.parseDouble(plList.get(i).get("totalElec").toString());
					if (te != 0) {
						plValueArray[i]=tl/te;
					} else {
						plValueArray[i] = 0d;
					}
					plMap.put(plNameArray[i],plValueArray[i] );
				}
			}
			
			// 这里将map.entrySet()转换成list
			Set<Entry<String,Double>> entries = plMap.entrySet();
			List<Map.Entry<String,Double>> plMapList = new ArrayList<Map.Entry<String,Double>>(
					entries);
			// 然后通过比较器来实现排序
			Collections.sort(plMapList, new Comparator<Map.Entry<String,Double>>() {
				// 降序排序
				public int compare(Entry<String,Double> o1,
						Entry<String,Double> o2) {
					if (o1.getValue() < o2.getValue()) {
						return 1;
					} else if ( o1.getValue() ==  o2.getValue()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
				
			// 经过降序排序之后获取前五个给页面展示
			int number = 0;
			for (Map.Entry<String, Double> mapping : plMapList) {
				Map<String, Object> maps = new LinkedHashMap<>();
				maps.put("pileName",mapping.getKey());
				maps.put("pileLossRate", String.format("%.2f", 100*(mapping.getValue()))+"%");
				pilesLossRate.add(maps);
				number++;
				if (number == 5) {
					break;
				}
			}
			
			return pilesLossRate;
			
		}	
		
		
	    //查询充电桩收入排行
		@SuppressWarnings("unchecked")
		public List<Map> queryPileIncome(Integer stationId)throws BizException{
			//Set<Integer> orgIds = AuthService.getOrgsByUserId();//鉴权方法
			//通过orgids获取所有的场站stationIds
			Map map1=new HashMap();
			map1.put("stationId", stationId);
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
			String incomeDate=sf.format(calendar.getTime())+"%";//获取当月份
			map1.put("incomeDate", incomeDate);
			//这是添加了按月查询的
			List<Map> plIncome=operationMapper.getPileIncomeByStationId(map1);
			return plIncome;
		}
		
		// 获取当前场站当月的所有数据量
		@SuppressWarnings("unchecked")
		public Map queryTotalInfo(Integer stationId)throws BizException{
			//Set<Integer> orgIds = AuthService.getOrgsByUserId();//鉴权方法
			//通过orgids获取所有的场站stationIds
			Map map=new HashMap();
			map.put("stationId", stationId);
			Integer serverCount = 0;//本月服务次数（次）
			Double dicp = 0d;//本月累计充电（千瓦时）
			Double diti = 0d;//本月累计收入（元）
			Double ditsf = 0d;//总服务费（元）
			Double ditcsf = 0d;//总分成服务费（元）
			Double fcsr = 0d;//总分成收入（元）
			Map totalInfo=new HashMap();			
			Map parameter=new HashMap();
			parameter.put("stationId", stationId);
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
			String incomeDate=sf.format(calendar.getTime())+"%";//模糊查询获取当月份
			parameter.put("incomeDate", incomeDate);
			//这是添加了按月查询的
			Map plIncome=operationMapper.getTotalByStationId(parameter);
			DecimalFormat df = new DecimalFormat("0.00");
			if (plIncome!=null&&!plIncome.isEmpty()) {
				if (plIncome.get("power")!=null&&!StringUtils.isBlank(plIncome.get("power").toString())) {
					String a = df.format(plIncome.get("power"));
					dicp=Double.valueOf(a);
				}
				
				if (plIncome.get("income")!=null&&!StringUtils.isBlank(plIncome.get("income").toString())) {
					String a = df.format(plIncome.get("income"));
					diti=Double.valueOf(a);
				}
				
				if (plIncome.get("fee")!=null&&!StringUtils.isBlank(plIncome.get("fee").toString())) {
					String a = df.format(plIncome.get("fee"));
					ditsf=Double.valueOf(a);
				}
				
				if (plIncome.get("confee")!=null&&!StringUtils.isBlank(plIncome.get("confee").toString())) {
					String a = df.format(plIncome.get("confee"));
					ditcsf=Double.valueOf(a);
				}

			}
			
			if(operationMapper.getCountByStationId(parameter)!=null){
				serverCount= operationMapper.getCountByStationId(parameter);//获取服务次数
			}
			
			Integer companyId = -1;
//			// 第一步，获取当前登陆用户信息
//			PubUser user = CommUtils.getLoginUser();
//
//			// 判断登陆用户为系统用户还是合约企业用户
//			if (!ObjectUtils.isNull(user)
//					&& !ObjectUtils.isNull(user.getUserTypeCode())) {
//
//				if ("02".equals(user.getUserTypeCode())) {
//					// 合约企业用户
//					CConCompany cConCompany = CommUtils.getLoginUserCompany();
//					companyId = cConCompany.getConCompanyId();
//				}
//
//			}
			
			// 根据companyId判断是否为合约企业，如果为-1，则为系统用户，否则为合约
			
			List<Map> InConTractCompany=operationMapper.getFcsrByStationId(map);
			Double czfcPercentage = 100d;
			for (Map map1 : InConTractCompany) {
				czfcPercentage = getSubtraction(czfcPercentage,
						map1.get("percentage"));
			}

		
			BigDecimal decimal = new BigDecimal(
					(czfcPercentage * ditcsf) / 100);
			fcsr = decimal.setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			totalInfo.put("serverCount", serverCount);
			totalInfo.put("dicp", dicp);
			totalInfo.put("diti", diti);
			totalInfo.put("ditsf", ditsf);
			totalInfo.put("ditcsf", ditcsf);
			totalInfo.put("fcsr", fcsr);

			return totalInfo;
		}
		
		
		@SuppressWarnings("unchecked")
		public Map<String,Map<String,Object>> queryMonitorInfo(Integer userId)throws BizException{// 获取运营中所有场站的当月每日充电量及充电收入的充电监控 
 			Map<String,Map<String,Object>> result=new HashMap<String,Map<String,Object>>();
			List<String> list =new LinkedList<String>();
			//获取到日期--天
			int size = 11;
			Calendar calendar = null;
			String data="";
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			for (int i =size; i > 0 ; i--) {
				calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -i);
				data =sf.format(calendar.getTime());
				list.add(size-i,data);
			}
			calendar = Calendar.getInstance();
			data =sf.format(calendar.getTime());
			list.add(size, data);
			String startTime=list.get(0);
			String endTime=sf.format(calendar.getTime());
			Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
			Map map2=new HashMap();
			map2.put("startTime", startTime);
			map2.put("endTime", endTime);
			map2.put("orgIds", orgIds);
			List<Map> monitors=operationMapper.getPowerAndMoney(map2);
			List<String> datesList=operationMapper.getDate(map2);
			
			for(String dataString : list){
				if(datesList.contains(dataString)){
					for(Map map3 : monitors){
						if(StringUtils.equals(map3.get("date").toString(), dataString)){
							Map<String,Object> map1=new HashMap<String,Object>();
							map1.put("power", map3.get("power"));
							map1.put("amount",map3.get("amount"));
							result.put(dataString,map1);
							}
						}
					}else{
						Map<String,Object> map1=new HashMap<String,Object>();
						map1.put("power",(double) 0);
						map1.put("amount",(double) 0);
						result.put(dataString,map1);
				}
			}
			
			

			return result;
			
			
		}
		
		
		public Map<String,Map<String,Object>> queryMonitorInfoByStationId(Integer stationId)throws BizException{// 获取运营中单个场站的当月每日充电量及充电收入的充电监控 
 			Map<String,Map<String,Object>> result=new HashMap<String,Map<String,Object>>();
			List<String> list =new LinkedList<String>();
			//获取到日期--天
			int size = 10;
			Calendar calendar = null;
			String data="";
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			for (int i =size; i > 0 ; i--) {
				calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -i);
				data =sf.format(calendar.getTime());
				list.add(size-i,data);
			}
			calendar = Calendar.getInstance();
			data =sf.format(calendar.getTime());
			list.add(size, data);
			String startTime=list.get(0);
			String endTime=sf.format(calendar.getTime());
			Map map2=new HashMap();
			map2.put("startTime", startTime);
			map2.put("endTime", endTime);
			map2.put("stationId",stationId);
			List<Map> monitors=operationMapper.getPowerAndMoneyByStationId(map2);
			List<String> datesList=operationMapper.getDateByStationId(map2);
			
			for(String dataString : list){
				if(datesList.contains(dataString)){
					for(Map map3 : monitors){
						if(StringUtils.equals(map3.get("date").toString(), dataString)){
							Map<String,Object> map1=new HashMap<String,Object>();
							map1.put("power", map3.get("power"));
							map1.put("income",map3.get("income"));
							result.put(dataString,map1);
							}
						}
					}else{
						Map<String,Object> map1=new HashMap<String,Object>();
						map1.put("power",(double) 0);
						map1.put("income",(double) 0);
						result.put(dataString,map1);
				}
			}
			
			return result;
			
			
		}
		
		
		//运营商下面的场站充电时间利用率,总利用率和公交，公共，物流
		@SuppressWarnings("unchecked")
		public Map<String,Object> queryTimeUseRade(Integer userId)throws BizException{
			Map map=new HashMap();
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
			String finishTime=sf.format(calendar.getTime());//模糊查询获取当月份
			Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
			map.put("finishTime", finishTime);
			map.put("orgIds", orgIds);
			Map map3=new HashMap();
			Integer chargeTime1=operationMapper.getAllTimes(map);//所有场站的充电订单总时长
			Double chargeTime=0D;
			Double gunTime=1D;
			Integer gunInteger=operationMapper.getGunCount(map)*24*60*60;
			if(gunInteger!=0){
				gunTime=Double.valueOf(operationMapper.getGunCount(map)*24*60*60);//获取冲过电的所有场站的有效利用时间
			}
			if(chargeTime1!=null){
				chargeTime=Double.valueOf(chargeTime1);
			}
			
			map3.put("rate", BigDecimalUtils.div(chargeTime, gunTime, 4));//利用率
			List<Map> top3List=operationMapper.getTimesList(map);//获取运营中公交，公共，物流场站的充电时长
			if(top3List!=null&&!top3List.isEmpty()){
				for(Map map2 :top3List){
					
					if("1".equals(map2.get("type").toString())){//公共
						 map3.put("allUse", BigDecimalUtils.div(Double.valueOf(map2.get("count").toString()), gunTime, 4));
					}else if("100".equals(map2.get("type").toString())){//公交专用
						map3.put("busUse", BigDecimalUtils.div(Double.valueOf(map2.get("count").toString()), gunTime, 4));
					}else{
						map3.put("wlUse", BigDecimalUtils.div(Double.valueOf(map2.get("count").toString()), gunTime, 4));//物流专用
					}					
				}
				
				if(map3.get("allUse")==null){
					map3.put("allUse", 0);
				}
				if(map3.get("busUse")==null){
					map3.put("busUse", 0);
				}
				if(map3.get("wlUse")==null){
					map3.put("wlUse", 0);
				}
				
			}else{
				map3.put("allUse", 0);
				map3.put("busUse", 0);
				map3.put("wlUse", 0);
			}
			
			
			return map3;
			
		}
		
		/**
		 * 获取两个数值相减后的值
		 * 
		 * @param dData
		 * @param xData
		 * @return
		 */
		private Double getSubtraction(Double dData, Object xData) {
			if (dData==null) {
				dData = 0.0D;
			}
			if (xData==null) {
				xData = 0.0D;
			}
			return new BigDecimal(String.valueOf(dData)).subtract(
				new BigDecimal(String.valueOf(xData))).doubleValue();
		}
}

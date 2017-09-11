package com.clouyun.charge.modules.monitor.service;
import java.text.ParseException;
import java.util.*;

import com.clouyun.charge.modules.charge.ChargeManageUtil;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.BigDecimalUtils;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.modules.monitor.mapper.IncomeAnalysisMapper;
import com.clouyun.charge.modules.system.service.UserService;

/**
 * 描述: 运营总览收入分析接口类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: fft
 * 版本: 2.0.0
 * 创建日期: 2017年6月21日
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class IncomeAnalysisService {
	
	 @Autowired
	 IncomeAnalysisMapper incomeAnalysisMapper;
	
	 @Autowired
	 UserService userService;
	
	public Map<String ,Object> queryIncomeInfo(Integer userId)throws BizException{
		Map<String ,Object> result =new HashMap();
		
		//当前日期天  -1
		Double dAmount1 = 0D;//收入
		Double dChgPower1 = 0D;//充电量
		int dCount1 = 0;//服务次数
		Double dReCharge1 = 0D;//充值金额
		//当前日期天  -2
		Double dAmount2 = 0D;//收入
		Double dChgPower2 = 0D;//充电量
		int dCount2 = 0;//服务次数
		Double dReCharge2 = 0D;//充值金额
		
		int dAmountFig = 0;//同比表示 ，默认持平
		int dChgPowerFig = 0;//同比表示 ，默认持平
		int dCountFig = 0;//同比表示 ，默认持平
		int dReChargeFig = 0;//同比表示 ，默认持平----充值金额
		//本月---
		Double mAmount1 = 0D;//本月收入
		Double mChgPower1 = 0D;//充电量
		int mCount1 = 0;//服务次数
		Double mReCharge1 = 0D;//充值金额
		//上月---
		Double mAmount2 = 0D;//收入
		Double mChgPower2 = 0D;//充电量
		int mCount2 = 0;//服务次数
		//Double mReCharge2 = 0D;//充值金额
		
		int mAmountFig = 0;//同比表示 ，默认持平
		int mChgPowerFig = 0;//同比表示 ，默认持平
		int mCountFig = 0;//同比表示 ，默认持平
		int mReChargeFig = 0;//同比表示 ，默认持平
		
		
		Double yAmount1 = 0D;//全年收入
		Double yChgPower1 = 0D;//充电量
		int yCount1 = 0;//服务次数

		Double yAmount2 = 0D;//全年收入
		Double yChgPower2 = 0D;//充电量
		int yCount2 = 0;//服务次数
		Double yReCharge2 = 0D;//充值金额
		
		int yAmountFig = 0;//同比表示 ，默认持平
		int yChgPowerFig = 0;//同比表示 ，默认持平
		int yCountFig = 0;//同比表示 ，默认持平
		int yReChargeFig = 0;//同比表示 ，默认持平
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
		Set<Integer> stationIds = userService.getUserRoleDataById(userId, RoleDataEnum.STATION.dataType);
		//日统计
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);//昨天
		String createDate = CalendarUtils.formatCalendar(calendar, "yyyy-MM-dd");
		Map<String,Object> map = new HashMap();
		map.put("orgIds", orgIds);
		map.put("stationIds", stationIds);
		map.put("createDate", createDate);
		map.put("type",1);
		DataVo resultMap1 = incomeAnalysisMapper.getIncomeInfo(map);
		dReCharge1 = incomeAnalysisMapper.getReCharge(map);
		calendar.add(Calendar.DAY_OF_MONTH, -1);//前天
		String createDate1 = CalendarUtils.formatCalendar(calendar, "yyyy-MM-dd");
		map.put("createDate", createDate1);
		DataVo resultMap2 = incomeAnalysisMapper.getIncomeInfo(map);
		dReCharge2 = incomeAnalysisMapper.getReCharge(map);
		//日同比标识6
		if(dReCharge1 - dReCharge2 > 0){
			dReChargeFig = 1;//增长
		}else if(dReCharge1 - dReCharge2 < 0){
			dReChargeFig = -1;//下降
		}
		if(null != resultMap1 && resultMap1.size() > 0){
			dAmount1 = BigDecimalUtils.round(resultMap1.getDouble("amount"), 0);
			dChgPower1 = BigDecimalUtils.round(resultMap1.getDouble("chgPower"), 0);
			dCount1 = resultMap1.getInt("count");
		}
		if(null != resultMap2 && resultMap2.size() > 0){
			dAmount2 = BigDecimalUtils.round(resultMap2.getDouble("amount"), 0);
			dChgPower2 = BigDecimalUtils.round(resultMap2.getDouble("chgPower"), 0);
			dCount2 = resultMap2.getInt("count");
		}	
		if(dAmount1 - dAmount2 > 0){
			dAmountFig = 1;//增长
		}else if(dAmount1-dAmount2 < 0){
			dAmountFig = -1;//下降
		}
		if(dChgPower1 - dChgPower2 > 0){
			dChgPowerFig = 1;//增长
		}else if(dChgPower1 - dChgPower2 < 0){
			dChgPowerFig = -1;//下降
		}
		if(dCount1 - dCount2 > 0){
			dCountFig = 1;//增长
		}else if(dCount1 - dCount2 < 0){
			dCountFig = -1;//下降
		}
		//月统计
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.DAY_OF_MONTH, -1);//昨天
		String endDate = CalendarUtils.formatCalendar(calendar1, "yyyy-MM-dd");//当月
		map.put("endDate", endDate);
		map.put("startDate", endDate.substring(0, 7)+"-01");
		map.put("type", 2);
		DataVo mResultMap1 = incomeAnalysisMapper.getIncomeInfo(map);
		mReCharge1 = incomeAnalysisMapper.getReCharge(map);//当月
		if(null != mResultMap1 && mResultMap1.size() > 0){
			mAmount1 = BigDecimalUtils.round(mResultMap1.getDouble("amount"), 0);
			mChgPower1 = BigDecimalUtils.round(mResultMap1.getDouble("chgPower"), 0);
			mCount1 = mResultMap1.getInt("count");
		}
		//上月同期，计算天数
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.MONTH ,-1);
		calendar1.add(Calendar.MONTH ,-1);
		String startDate2 = CalendarUtils.formatCalendar(calendar2, "yyyy-MM")+"-01"; 
	    String endDate2 = CalendarUtils.formatCalendar(calendar1, "yyyy-MM-dd");
		Map<String,Object> map3 = new HashMap();
		map3.put("orgIds", orgIds);
		map3.put("stationIds", stationIds);
		map3.put("endDate", endDate2);
		map3.put("startDate", startDate2);
		DataVo mResultMap2 = incomeAnalysisMapper.getIncomeInfo(map3);
		Double mReCharge2 = incomeAnalysisMapper.getReCharge(map3);
		//月同比标识
		if(mReCharge1 - mReCharge2 > 0){
			mReChargeFig = 1;//增长
		}else if(mReCharge1-mReCharge2 < 0){
			mReChargeFig = -1;//下降
		}
		if(null != mResultMap2 && mResultMap2.size() > 0){
			mAmount2 = BigDecimalUtils.round(mResultMap2.getDouble("amount"), 0);
			mChgPower2 = BigDecimalUtils.round(mResultMap2.getDouble("chgPower"), 0);
			mCount2 = mResultMap2.getInt("count");
		}
		if(mAmount1 - mAmount2 > 0){
			mAmountFig = 1;//增长
		}else if(mAmount1 - mAmount2 < 0){
			mAmountFig = -1;//下降
		}
		if(mChgPower1 - mChgPower2 > 0){
			mChgPowerFig = 1;//增长
		}else if(mChgPower1 - mChgPower2 < 0){
			mChgPowerFig = -1;//下降
		}
		if(mCount1 - mCount2 > 0){
			mCountFig = 1;//增长
		}else if(mCount1 - mCount2 < 0){
			mCountFig = -1;//下降
		}
		//当年收入
		Calendar calendar3 = Calendar.getInstance();
		calendar3.add(Calendar.DAY_OF_MONTH, -1);//昨天
		String yearString = CalendarUtils.formatCalendar(calendar2, "yyyy");//当年
		map.put("startDate",yearString+"-01-01");
		map.put("endDate",CalendarUtils.formatCalendar(calendar3, "yyyy-MM-dd"));
		DataVo yResultMap1 = incomeAnalysisMapper.getIncomeInfo(map);
		Double yReCharge1 = incomeAnalysisMapper.getReCharge(map);//当年
		if(null != yResultMap1 && yResultMap1.size() > 0){
			yAmount1 = BigDecimalUtils.round(yResultMap1.getDouble("amount"), 0);
			yChgPower1 = BigDecimalUtils.round(yResultMap1.getDouble("chgPower"), 0);
			yCount1 = yResultMap1.getInt("count");			
		}
		Calendar calendar4 = Calendar.getInstance();
		calendar4.add(Calendar.YEAR, -1);//去年,封顶时间
		calendar3.add(Calendar.YEAR, -1);
		map.put("startDate",CalendarUtils.formatCalendar(calendar4, "yyyy")+"-01-01");
		map.put("endDate",CalendarUtils.formatCalendar(calendar3, "yyyy"));
	    DataVo yResultMap2 = incomeAnalysisMapper.getIncomeInfo(map);
		//年同比标识
		if(yReCharge1 - yReCharge2 > 0){
			yReChargeFig = 1;//增长
		}else if(yReCharge1 - yReCharge2 < 0){
			yReChargeFig = -1;//下降
		}
		if(null != yResultMap2 && yResultMap2.size()>0){
			yAmount2 = BigDecimalUtils.round(yResultMap2.getDouble("amount"), 0);
			yChgPower2 = BigDecimalUtils.round(yResultMap2.getDouble("chgPower"), 0);
			yCount2 = yResultMap2.getInt("count");
		}
		if(yAmount1 - yAmount2 > 0){
			yAmountFig = 1;//增长
		}else if(yAmount1 - yAmount2 < 0){
			yAmountFig = -1;//下降
		}
		
		if(yChgPower1 - yChgPower2 > 0){
			yChgPowerFig = 1;//增长
		}else if(yReCharge1 - yReCharge2 < 0){
			yChgPowerFig = -1;//下降
		}
		if(yCount1 - yCount2 > 0){
			yCountFig = 1;//增长
		}else if(yCount1 - yCount2 < 0){
			yCountFig = -1;//下降
		}
		result.put("dAmount", Math.round(dAmount1));
		result.put("dChgPower", Math.round(dChgPower1));
		result.put("dCount", dCount1);
		result.put("dReCharge", Math.round(dReCharge1));
		result.put("dAmountFig", dAmountFig);
		result.put("dChgPowerFig", dChgPowerFig);
		result.put("dCountFig", dCountFig);
		result.put("dReChargeFig", dReChargeFig);
		result.put("mAmount", Math.round(mAmount1));
		result.put("mChgPower", Math.round(mChgPower1));
		result.put("mCount", mCount1);
		result.put("mReCharge", Math.round(mReCharge1));
		result.put("mAmountFig", mAmountFig);
		result.put("mChgPowerFig", mChgPowerFig);
		result.put("mCountFig", mCountFig);
		result.put("mReChargeFig", mReChargeFig);
		result.put("yAmount", Math.round(yAmount1/10000));
		result.put("yChgPower",Math.round(yChgPower1/1000));
		result.put("yCount", yCount1);
		result.put("yReCharge", Math.round(yReCharge1));
		result.put("yAmountFig", yAmountFig);
		result.put("yChgPowerFig", yChgPowerFig);
		result.put("yCountFig", yCountFig);
		result.put("yReChargeFig", yReChargeFig);
		return result;
	}
	
	
	//单企业各支付方式占比
	public Map<String ,Object> queryIncomePayType(Integer userId)throws BizException, ParseException{
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );//鉴权方法
		Map<String ,Object> result = new HashMap();
		double xj = 0D;//现金占比
		double yj = 0D;//月结占比
		double wx = 0D;//微信占比
		double zfb = 0D;//支付宝占比
		double czk = 0D;//储值卡占比
		double xj1 = 0D;//现金
		double xj2 = 0D;//现金
		double yj1 = 0D;//月结
		double wx1 = 0D;//微信
		double wx2 = 0D;//微信
		double zfb1 = 0D;//支付宝
		double zfb2 = 0D;//支付宝
		double czk1 = 0D;//储值卡
		double czk2 = 0D;//储值卡
		double other = 0D;
		double zs = 0D;
		Map<String,Object> map = new HashMap();
		if(null != orgIds){
			map.put("orgIds", orgIds);
		}
		List<DataVo>  ctList = incomeAnalysisMapper.getContract();//有效合约的有效时间段
		Map<String,Map<String, String>> ctMap  = new HashMap();
		if(null != ctList && ctList.size() > 0){
			for(DataVo ctVo : ctList){
				String contractId = ctVo.getString("contractId");
				String start = ctVo.getString("contractExpirationStart");
				String end = ctVo.getString("contractExpirationEnd");
				if(!ctMap.containsKey(contractId)){
					Map<String,String> time = new HashMap();
					time.put("start",start);
					time.put("end",end);
					ctMap.put(contractId,time);
				}
			}
		}
		//查询会员 --月结合约关系
		List<DataVo> ccList = incomeAnalysisMapper.getConsContract();
		Map<String,Set<Integer>> cg = getMapByList(ccList);
		//月结合约--场站的关系(桩)
		List<DataVo> cpList = incomeAnalysisMapper.getContractPile();
		Map<String,Set<Integer>> sg  = new HashMap();  //桩---合约的关系
		if(null != cpList && cpList.size()>0){
			for(DataVo cpVo:cpList){
				String mapValue = cpVo.getString("mapValue");
				int mapKey = cpVo.getInt("mapKey");
				if(!sg.containsKey(mapValue)){
					Set<Integer> con = new HashSet();
					con.add(mapKey);
					sg.put(mapValue, con);
				}else{
					sg.get(mapValue).add(mapKey);
				}
			}
		}
		Map<String,Set<Integer>> gs = getMapByList(cpList);
		//查询合约会员 --合约场站(桩)
		Map<String,Set<Integer>> cs = new HashMap();
		if(null != cg && cg.size() > 0){
			for (Map.Entry<String,Set<Integer>> entry : cg.entrySet()) {
				Set<Integer> temp = new HashSet();
				for(Integer contractId:entry.getValue()){
					Set<Integer> set = gs.get(contractId.toString());
					if(null != set){
						temp.addAll(set);
					}
				}
				cs.put(entry.getKey(), temp);
			}
		}
		Calendar calendar = Calendar.getInstance();
		String endTime = CalendarUtils.formatCalendar(calendar, "yyyy-MM-dd");
		String startTime = endTime.substring(0, 7)+"-01";
		map.put("endTime", endTime);
		map.put("startTime",startTime);
		List<DataVo> monthPayList = incomeAnalysisMapper.getMonthPay(map);//当前月
		if(CollectionUtils.isNotEmpty(monthPayList)){
			for(DataVo monthPayVo:monthPayList){
				String payType = monthPayVo.getString("payType");
				Double amount = monthPayVo.getDouble("amount");
				String pileId = monthPayVo.getString("pileId");
				String consId = monthPayVo.getString("consId");
				String tempEndTime = monthPayVo.getString("endTime");
				if(null != sg.get(pileId) && sg.get(pileId).size() > 0){//桩存在对应的合约
					if(null != cg.get(consId)){//会员对应有合约
						Set<Integer> temp = cg.get(consId);
						temp.retainAll(sg.get(pileId));
						String contractId = "";
						if(null != temp && temp.size() == 1){
							for(Integer conId:temp){
								contractId = conId.toString();
							}
							if(null != ctMap.get(contractId)){
								Map<String,String> time = ctMap.get(contractId);
								long end = CalendarUtils.getDate(time.get("end")+" 23:59:59").getTime();
								long start = CalendarUtils.getDate(time.get("start")+" 00:00:00").getTime();
								long tempTime = CalendarUtils.getDate(tempEndTime).getTime();
								if(end > tempTime && start < tempTime){//在合约有效期内的月结数据
									yj1 += amount;//月结
									switch (payType){
									case "1"://月结现金
										xj1 += amount;
										break;
									case "3"://月结微信
										wx1 += amount;
										break;
									case "4"://月结支付宝
										zfb1 += amount;
										break;
									case "5"://月结充值卡
										czk1 += amount;
										break;
									}   
								}
							}
						}
					}
				}
				zs +=  amount;//总数
				switch (payType) {
				case "1"://总现金
					xj2 += amount;
					break;
				case "3"://总微信
					wx2 += amount;
					break;
				case "4"://总支付宝
					zfb2 += amount;
					break;
				case "5"://总充值卡
					czk2 += amount;
					break;
				}
			}
		}
		if(zs != 0D){
			xj = (xj2 - xj1)/zs;//现金占比
			yj = yj1/zs;//月结占比
			wx = (wx2 - wx1)/zs;//微信占比
			zfb = (zfb2 - zfb1)/zs;//支付宝占比
			czk = (czk2 - czk1)/zs;//充值卡占比
			other = (zs - xj2 + xj1 - yj1 - wx2 + wx1 - zfb2 + zfb1 - czk2 + czk1)/zs;//其他占比
		}
		result.put("xj", Math.round(xj*10000)/10000.00);//现金
		result.put("yj", Math.round(yj*10000)/10000.00);//月结
		result.put("wx", Math.round(wx*10000)/10000.00);//微信
		result.put("zfb", Math.round(zfb*10000)/10000.00);//支付宝
		result.put("czk", Math.round(czk*10000)/10000.00);//充值卡
		result.put("other", Math.round(other*10000)/10000.00);//其他
		return result;
	}
	/**
	 * 收入曲线
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月29日
	 */
	public List<Map<String,Object>> queryIncomeCurve(Map map)throws BizException{
		String type = "1";//默认按时
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(111000);
		}
		if(params.isNotBlank("type")){//默认按时查询
			type = params.getString("type");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"),RoleDataEnum.ORG.dataType );
		Set<Integer> stationIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		Calendar calendar = Calendar.getInstance();
		if(null != stationIds && stationIds.size() > 0){
			map.put("stationIds", stationIds);
		}
		if(null != orgIds && orgIds.size() > 0){
			map.put("orgIds", orgIds);
		}
	    List<Map<String,Object>> resultList = new ArrayList();
		if("1".equals(type)){//按时6
			map.put("startTime",CalendarUtils.formatCalendar(calendar, "yyyy-MM-dd") + " 00:00:00");
			map.put("endTime", CalendarUtils.formatCalendar(calendar, "yyyy-MM-dd") + " 23:59:59");
			Map<String,Object> resultMap = convertToCurveMap(incomeAnalysisMapper.getIncomeByHour(map));
			for(int i = 0;i < 24;i++){
				String hour = String.valueOf(i);
				if(i < 10){
					hour = "0"+i;
				}
				resultList.add(getResultMap(hour + ":00",resultMap));
			}
		}else if("2".equals(type)){//按日
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			map.put("endTime", CalendarUtils.formatCalendar(calendar, "yyyy-MM-dd"));
			calendar.add(Calendar.DAY_OF_MONTH, -30);
			map.put("startTime", CalendarUtils.formatCalendar(calendar, "yyyy-MM-dd"));
			Map<String,Object> resultMap = convertToCurveMap(incomeAnalysisMapper.getIncomeByDay(map));
			for(int i = 30;i > 0;i--){
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -i);
				resultList.add(getResultMap(CalendarUtils.formatCalendar(cal, "yyyy-MM-dd"),resultMap));
			}
		}else if("3".equals(type)){//按月
			calendar.add(Calendar.MONTH, -1);
			map.put("endTime", CalendarUtils.formatCalendar(calendar, "yyyy-MM"));
			calendar.add(Calendar.MONTH, -12);
			map.put("startTime",CalendarUtils.formatCalendar(calendar, "yyyy-MM"));
			Map<String,Object> resultMap = convertToCurveMap(incomeAnalysisMapper.getIncomeByMonth(map));
			for(int i = 12;i > 0;i--){
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -i);
				resultList.add(getResultMap(CalendarUtils.formatCalendar(cal, "yyyy-MM"),resultMap));
			}
		}
		return resultList;
	}
	/**
	 * 收入分布
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月30日
	 * @throws BizException 
	 */
	public List<Map<String,Object>> queryIncomeDist(Integer userId) throws BizException{
		List<Map<String,Object>> resultList = new ArrayList();
		if(null == userId){
			throw new BizException(111000);
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType );
		Set<Integer> stationIds = userService.getUserRoleDataById(userId, RoleDataEnum.STATION.dataType);
		Map map = new HashMap();
		if(null != orgIds && orgIds.size() > 0){
			map.put("orgIds", orgIds);
		}
		if(null != stationIds && stationIds.size() > 0){
			map.put("stationIds", stationIds);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		map.put("endTime", CalendarUtils.formatCalendar(calendar, "yyyy-MM"));
		calendar.add(Calendar.MONTH, -11);
		map.put("startTime",CalendarUtils.formatCalendar(calendar, "yyyy-MM"));
		Map<String,Double> incomeConsMap = convertToDistMap(incomeAnalysisMapper.getIncomeCons(map));
		Map<String,Double> incomeContractMap = convertToDistMap(incomeAnalysisMapper.getIncomeContract(map));
		Map<String,Double> incomeChargeMap = convertToDistMap(incomeAnalysisMapper.getIncomeCharge(map));
		for(int i = 1;i <= 12;i++){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			Map<String,Object> monthMap = new HashMap();
			String time = CalendarUtils.formatCalendar(cal, "yyyy-MM");
			monthMap.put("time",time);
			
			if(null != incomeConsMap.get(time)){
				monthMap.put("single", Math.round(incomeConsMap.get(time)));
			}else{
				monthMap.put("single", 0);
			}
			if(null != incomeContractMap.get(time)){
				monthMap.put("contract",Math.round(incomeContractMap.get(time)));
			}else{
				monthMap.put("contract", 0);
			}
			if(null != incomeChargeMap.get(time)){
				monthMap.put("group", Math.round(incomeChargeMap.get(time)));
			}else{
				monthMap.put("group", 0);
			}
			resultList.add(monthMap);
		}
		return resultList;
	}
	
	private Map<String,Object> convertToCurveMap(List<DataVo> list){
    	Map<String,Object> statTemp = new HashMap<String, Object>();
    	if(null != list && list.size() > 0){
    		for(DataVo statVo : list){
    			Map<String,Double> temp = new HashMap();
    			if(!statTemp.containsKey(statVo.getString("time")))	{
    				temp.put("amount", statVo.getDouble("amount"));
    				temp.put("powers", statVo.getDouble("powers"));
    				statTemp.put(statVo.getString("time"), temp);
    			}
    		}
    	} 
       return statTemp;
    }
	
	private Map<String,Double> convertToDistMap(List<DataVo> list){
    	Map<String,Double> temp = new HashMap();
    	if(null != list && list.size() > 0){
    		for(DataVo statVo : list){
    			String time = statVo.getString("time");
    			Double sum = statVo.getDouble("sum");
    			if(!temp.containsKey(time))	{
    				temp.put(time, sum);
    			}
    		}
    	} 
       return temp;
    }
	
	private Map<String,Object> getResultMap(String key,Map<String,Object> map){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("time",key);
		if(map.containsKey(key)){
			result.put("incomeSum", ((Map<String,Double>)map.get(key)).get("amount"));
			result.put("powerSum",  ((Map<String,Double>)map.get(key)).get("powers"));
		}else{
			result.put("incomeSum", 0);
			result.put("powerSum", 0);
		}
		return result;
	}

	/**
	 * 场站充电量排行
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	public List<DataVo> stationCharge(DataVo dataMap) throws Exception {
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.STATION.dataType,"stationIds");
		List<DataVo> list =null;
		if(dataMap.isBlank("size")){
			dataMap.put("size",20);
		}
		if(dataMap.getInt("type")==1){
			list=	stationChargeMonthOrYear(dataMap,"chgPower",1);
		}
		if(dataMap.getInt("type")==2){
			list=	stationChargeMonthOrYear(dataMap,"chgPower",2);
		}
		if(dataMap.getInt("type")==3){
			list=	stationChargeMonthOrYear(dataMap,"chgPower",3);
		}
		return list;
	}

	/**
	 * 收入排行
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	public List<DataVo> stationIncome(DataVo dataMap) throws Exception {
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.STATION.dataType,"stationIds");
		List<DataVo> list =null;
		if(dataMap.isBlank("size")){
			dataMap.put("size",20);
		}
		if(dataMap.getInt("type")==1){
			list=	stationChargeMonthOrYear(dataMap,"amount",1);
		}
		if(dataMap.getInt("type")==2){
			list=	stationChargeMonthOrYear(dataMap,"amount",2);
		}
		if(dataMap.getInt("type")==3){
			list=	stationChargeMonthOrYear(dataMap,"amount",3);
		}
		return list;
	}
	private  List<DataVo> setListDataVo(DataVo dayVo,String sumType){
		if(sumType.equalsIgnoreCase("chgPower")){
			return incomeAnalysisMapper.getChargeDayChgPower(dayVo);
		}else if(sumType.equalsIgnoreCase("amount")) {
			return incomeAnalysisMapper.getChargeDayAmount(dayVo);
		} else  if(sumType.equalsIgnoreCase("orgAmount")){
			return incomeAnalysisMapper.getOrgAmount(dayVo);
		}
		return null;
	}
	private  List<DataVo>  stationChargeMonthOrYear(DataVo dayVo,String sumType,int type) throws Exception {
		List list  = new ArrayList();
		ChargeManageUtil.setDateCompare(dayVo,type);
		dayVo.put("flag",0);
		List<DataVo> dayList = setListDataVo(dayVo,sumType);
		if(dayList!=null&&dayList.size()>0){
			Set<Integer> stationIds = new HashSet<>();
			for(DataVo vo :dayList){
				stationIds.add(vo.getInt("stationId"));
			}
			dayVo.put("flag",1);
			dayVo.put("stationIds",stationIds);
			List<DataVo> upDayList = setListDataVo(dayVo,sumType);
			if(dayList!=null&&dayList.size()>0){
				for(DataVo vo1 :  dayList){
					String percentage ="100%";
					Integer compare =1;
					DataVo returnMap = new DataVo();
					Integer stationId1 = vo1.getInt("stationId");
					Double amount1 = vo1.getDouble(sumType);
					if(upDayList!=null&&upDayList.size()>0){
						for(DataVo vo2 :  upDayList){
							Integer stationId2 = vo2.getInt("stationId");
							Double amount2 = vo2.getDouble(sumType);
							if(stationId1.equals(stationId2)){
								if(amount2>0&&amount1>0)
								{
									if(amount1>amount2){
										percentage = ChargeManageUtil.percentage((amount1-amount2)/amount2);
										compare=1;
									}else if(amount1<amount2){
										percentage =  ChargeManageUtil.percentage((amount2-amount1)/amount2);
										compare=-1;
									}else {
										 compare=0;
										 percentage ="0%";
									}
								}else  if(amount2==0&&amount1==0){
									percentage ="0%";
									compare=0;
								} else if(amount1>0){
									compare =1;
									percentage ="100%";
								}else  if(amount2>0){
									compare =-1;
									percentage ="100%";
								}
							}
						}
					}
					returnMap.add(sumType,Math.round(amount1));
					returnMap.add("percentage",percentage);
					returnMap.add("compare",compare);
					if(sumType.equalsIgnoreCase("orgAmount")){
						returnMap.add("orgName",vo1.getString("orgName"));
					}else {
						returnMap.add("stationName",vo1.getString("stationName"));
					}
					list.add(returnMap);
				}
			}
		}
		return  list;
	}

	/**
	 *
	 * @param dataMap
	 * @return
	 */
	public List<DataVo> orgIncome(DataVo dataMap) throws Exception {
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.ORG.dataType,"orgIds");
		List<DataVo> list =null;
		if(dataMap.isBlank("size")){
			dataMap.put("size",20);
		}
		if(dataMap.getInt("type")==1){
			list=	stationChargeMonthOrYear(dataMap,"orgAmount",1);
		}
		if(dataMap.getInt("type")==2){
			list=	stationChargeMonthOrYear(dataMap,"orgAmount",2);
		}
		if(dataMap.getInt("type")==3){
			list=	stationChargeMonthOrYear(dataMap,"orgAmount",3);
		}
		return list;
	}
	
	private Map<String,Set<Integer>> getMapByList(List<DataVo> list){
		Map<String,Set<Integer>> result = null;
	    if(null != list && list.size()>0){
	    	result = new HashMap<String, Set<Integer>>();
	    	for(DataVo mapVo:list){
	    		String mapKey = mapVo.getString("mapKey");
	    		int mapValue = mapVo.getInt("mapValue");
	    		if(!result.containsKey(mapKey)){
	    			Set<Integer> set = new HashSet<Integer>();
	    			set.add(mapValue);
	    			result.put(mapKey, set);
	    		}else{
	    			Set<Integer> temp = result.get(mapKey);
	    			temp.add(mapValue);
	    		}
	        }
	    }
		return result;
	}
	
}

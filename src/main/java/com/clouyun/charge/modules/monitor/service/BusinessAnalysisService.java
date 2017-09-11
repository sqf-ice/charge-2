package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.DateUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.monitor.mapper.BusinessAnalysisMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 描述:  经营分析服务类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yi.kai
 * 版本: 1.0
 * 创建日期: 2017年6月21日
 */
@Service
public class BusinessAnalysisService {
	@Autowired
	private BusinessAnalysisMapper businessAnalysisMapper;
	/**
	 * 查询充电量完成情况
	 * @param dataMap
	 * @return
	 */
	public PageInfo power(DataVo dataMap) throws Exception {
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.STATION.dataType,"stationIds");
		if(dataMap.isBlank("month")){
            dataMap.put("month",DateUtils.getMonth(DateUtils.getUpMonth(new Date())));
		}
		ChargeManageUtil.setPageInfo(dataMap);
		Double overPower =0.0;
		Double unFinishPower =0.0;
		List<DataVo> dvList =businessAnalysisMapper.getStationPower(dataMap);
		for(DataVo vo : dvList){
			Integer compare=0;
            Double total = vo.getDouble("total");//充电量
			Double target = vo.getDouble("target"); //目标充电量
			if(target>total){
				compare =-1;
				unFinishPower = target-total;
			}else if(target<total) {
				compare =1;
				overPower = total-target;
			}else {
				compare =0;
				unFinishPower =0.0;
				overPower =0.0;
			}
			if(vo.getString("stationName").length()>4){
				vo.put("stationNameMint",vo.getString("stationName").substring(0,4)+"...");
			}else {
				vo.put("stationNameMint",vo.getString("stationName"));
			}
			DecimalFormat df = new DecimalFormat("#.00");
			vo.put("overPower",df.format(overPower));
			vo.put("unFinishPower",df.format(unFinishPower));
			vo.put("compare",compare);
	}
		return  new PageInfo(dvList);
	}

	/**
	 * 场站收入
	 * @param dataMap
	 * @return
	 */
	public PageInfo income(DataVo dataMap) throws Exception {
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.STATION.dataType,"stationIds");
        if(dataMap.isBlank("month")){
            dataMap.put("month",DateUtils.getMonth(DateUtils.getUpMonth(new Date())));
        }
		ChargeManageUtil.setPageInfo(dataMap);
		List<DataVo> dvList =businessAnalysisMapper.getStationIncome(dataMap);
		Double overIncome =0.0;
		Double unFinishIncome =0.0;
		Integer compare =0;
		for(DataVo vo : dvList){
			Double amount = vo.getDouble("total");//收入
			Double target = vo.getDouble("target");//目标收入
			if(target>amount){
				compare =-1;
				unFinishIncome =target-amount;
			}else if(target<amount) {
				compare =1;
				overIncome = amount-target;
			}else {
				compare =0;
				unFinishIncome =0.0;
				overIncome =0.0;
			}
			if(vo.getString("stationName").length()>4){
				vo.put("stationNameMint",vo.getString("stationName").substring(0,4)+"...");
			}else {
				vo.put("stationNameMint",vo.getString("stationName"));
			}
			DecimalFormat df = new DecimalFormat("#.00");
			vo.put("overIncome",df.format(overIncome));
			vo.put("unFinishIncome",df.format(unFinishIncome));
			vo.put("compare",df.format(compare));
		}
		return  new PageInfo(dvList);
	}

	/**
	 * 查询场站利润排行
	 * @param dataMap
	 * @return
	 */
	public List<DataVo> profit(DataVo dataMap) throws Exception {
        ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.STATION.dataType,"stationIds");
		if(dataMap.isBlank("month")){
			dataMap.put("month",DateUtils.getMonth(DateUtils.getUpMonth(new Date())));
		}
         if(dataMap.isBlank("size")) {
			 dataMap.put("size", 10);
		 }
        List<DataVo> list =businessAnalysisMapper.getStationProfit(dataMap);
        List lists  = new ArrayList();
        if(list!=null&&list.size()>0){
            dataMap.put("month",DateUtils.getMonth(DateUtils.getUpMonth(DateUtils.getMonth(dataMap.getString("month")))));
            Set set = new HashSet();
            for(DataVo dataVo:list){
				set.add(dataVo.getInt("stationId"));
			}
            dataMap.put("stationIds",set);
            List<DataVo> upList =businessAnalysisMapper.getStationProfit(dataMap);
            for (DataVo vo1:list){
                DataVo returnMap = new DataVo();
                Integer compare =1;
				String percentage ="100%";
				Double amount1 = vo1.getDouble("profit");
                if(upList!=null&&upList.size()>0){
                    for (DataVo vo2:upList){
                        Double amount2 = vo2.getDouble("profit");
                        if(vo1.getString("stationId").equals(vo2.getString("stationId"))){
                            if(amount2>0&&amount1>0)
                            {
                                if(amount1>amount2){
                                    percentage = ChargeManageUtil.percentage((amount1-amount2)/amount2);
                                    compare=1;
                                }else if(amount1<amount2){
                                    percentage =  ChargeManageUtil.percentage((amount2-amount1)/amount2);
                                    compare=-1;
                                }else {
									compare =0;
									percentage ="0%";
								}
                            }else  if(amount2==0&&amount1==0){
								compare =0;
                                percentage ="0%";
                            } else if(amount1>0){
                                compare =1;
                            }else  if(amount2>0){
                                compare =-1;
                            }
                            continue;
                        }
                    }
                }
                returnMap.add("stationName",vo1.getString("stationName"));
                returnMap.add("amount",Math.round(amount1));
                returnMap.add("percentage",percentage);
                returnMap.add("compare",compare);
                lists.add(returnMap);
            }
        }
		return lists;
	}

	/**
	 * 查询故障
	 * @param dataMap
	 * @return
	 */
    public PageInfo fault(DataVo dataMap) throws Exception {
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.ORG.dataType,"orgIds");
		if(dataMap.isBlank("month")){
			dataMap.put("month",DateUtils.getMonth(DateUtils.getUpMonth(new Date())));
		}
		DataVo returnMap = new DataVo();
		Integer type1  = 0;//硬件故障数
		Integer type2  = 0;//通讯异常数
		Integer type3  = 0;//支付异常数
		Integer type4  = 0;//系统BUG数
		Integer type5  = 0;//启动充电异常
		Integer type6  = 0;//订单数据异常
		List<DataVo> faultTypeList =  businessAnalysisMapper.faultTypeList(dataMap);//获取故障类型数
		for(DataVo vo:faultTypeList){
			if(vo.getString("templateName").equals("硬件故障")){
				type1 = vo.getInt("size");
			}
			if(vo.getString("templateName").equals("通讯异常")){
				type2 = vo.getInt("size");
			}
			if(vo.getString("templateName").equals("支付异常")){
				type3 = vo.getInt("size");
			}
			if(vo.getString("templateName").equals("系统BUG")){
				type4 = vo.getInt("size");
			}
			if(vo.getString("templateName").equals("启动充电异常")){
				type5 = vo.getInt("size");
			}
			if(vo.getString("templateName").equals("订单数据异常")){
				type6 = vo.getInt("size");
			}
		}
		DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
		Integer countSize = type1+type2+type3+type4+type5+type6;
		if(countSize>0){
			returnMap.put("type1",df1.format((double)type1/countSize));
			returnMap.put("type2",df1.format((double)type2/countSize));
			returnMap.put("type3",df1.format((double)type3/countSize));
			returnMap.put("type4",df1.format((double)type4/countSize));
			returnMap.put("type5",df1.format((double)type5/countSize));
			Integer type1Str = Integer.parseInt(returnMap.getString("type1").substring(0,returnMap.getString("type1").indexOf("%")));
			Integer type2Str = Integer.parseInt(returnMap.getString("type2").substring(0,returnMap.getString("type2").indexOf("%")));
			Integer type3Str = Integer.parseInt(returnMap.getString("type3").substring(0,returnMap.getString("type3").indexOf("%")));
			Integer type4Str = Integer.parseInt(returnMap.getString("type4").substring(0,returnMap.getString("type4").indexOf("%")));
			Integer type5Str = Integer.parseInt(returnMap.getString("type5").substring(0,returnMap.getString("type5").indexOf("%")));
			int count =100-type1Str-type2Str-type3Str-type4Str-type5Str;
			if(count<0){
				count=0;
			}
			returnMap.put("type6",count+"%");
		}else {
			returnMap.put("type1","0%");
			returnMap.put("type2","0%");
			returnMap.put("type3","0%");
			returnMap.put("type4","0%");
			returnMap.put("type5","0%");
			returnMap.put("type6","0%");
		}
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.STATION.dataType,"stationIds");
		ChargeManageUtil.setPageInfo(dataMap);
		dataMap.put("task",0);
        List<DataVo> stations1 = businessAnalysisMapper.getFaultStation(dataMap);//总故障
		dataMap.put("task",1);
		List<DataVo> stations2 = businessAnalysisMapper.getFaultStation(dataMap);//类型为5的故障
		List<DataVo> stations  = new ArrayList<>();
		if(stations1!=null&&stations1.size()>0){
			for (DataVo vo1:stations1){
				DataVo vo = new DataVo();
				String repairSize =0+"";//修复数
				vo.put("faultSize",vo1.getString("size"));//故障数
				if(stations2!=null&&stations1.size()>0){
					for(DataVo vo2:stations2){
                      if(vo1.getString("stationId").equals(vo2.getString("stationId"))){
						  repairSize = vo2.getString("size");
						  continue;
					  }
					}
				}
				vo.put("repairSize",repairSize);//故障数
				vo.put("stationName",vo1.getString("stationName"));//场站名
				stations.add(vo);
			}
		}
		returnMap.put("stations",stations);
		for(DataVo st :stations){
			if(st.getString("stationName").length()>4){
				st.put("stationNameMint",st.getString("stationName").substring(0,4)+"...");
			}else {
				st.put("stationNameMint",st.getString("stationName"));
			}
		}
		List returnMapList = new ArrayList();
		returnMapList.add(returnMap);
		return new PageInfo(returnMapList);

    }

	/**
	 * 效率提升
	 * @param dataMap
	 * @return
	 * @throws BizException
	 */
	public DataVo efficiency(DataVo dataMap) throws Exception {
		DataVo returnMap = new DataVo();
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.ORG.dataType,"orgIds");
		ChargeManageUtil.getUserRoleData(dataMap, RoleDataEnum.STATION.dataType,"stationIds");
		returnMap.add("charTimeList",setChargetTime(dataMap));//设置充电时长
		returnMap.add("conversionList",setConversion(dataMap));//设置转换率
		returnMap.add("onLineRateList",setOnLineRate(dataMap));//设置在线率
		returnMap.add("accuracy",setAccuracy(dataMap));//设置准确率
    	return returnMap;
	}
	/**
	 	 * 准确率
	 	 * @param vo
	 	 * @return
	 	 */
	private DataVo setAccuracy(DataVo vo) throws Exception {
		ChargeManageUtil.setDateCompare(vo);
		DataVo returnMap = new DataVo();
		int day = vo.getInt("day");
		int	upDay = vo.getInt("upDay");//上月天数
		int week = vo.getInt("week");//周数
		int upWeek = vo.getInt("upWeek");//上月周数
		int weekDay = vo.getInt("weekDay");
		int upWeekDay = vo.getInt("upWeekDay");
		vo.put("type",1);//1.月 2.上月
		vo.put("flag",1);//1.月 2.周
		Integer lossSizeDay =	 businessAnalysisMapper.accuracyDay(vo); //日丢单设备
		Integer lossSize =	 businessAnalysisMapper.accuracy(vo);//月丢单设备
		Integer operateSize =	 businessAnalysisMapper.getPileSize(vo)/day;//月运营设备
		vo.put("flag",2);//1.月 2.周
		Integer lossSizeWeek =	 businessAnalysisMapper.accuracyWeek(vo);  //得到周准确率
		Integer operateSizeWeek =	 businessAnalysisMapper.getPileSize(vo)/weekDay;//得到周运营设备
		vo.put("type",2);//1.月 2.上月
		vo.put("flag",1);//1.月 2.周
		Integer upLossSizeDay =	 businessAnalysisMapper.accuracyDay(vo);//日丢单设备
		Integer upLossSize =	 businessAnalysisMapper.accuracy(vo);//得到上月月丢单设
		Integer upOperateSize =	 businessAnalysisMapper.getPileSize(vo)/upDay;//得到上月运营设备

		vo.put("flag",2);//1.月 2.周
		Integer upLossSizeWeek =	 businessAnalysisMapper.accuracyWeek(vo);//得到上月周丢单设
		Integer upOperateSizeWeek =	 businessAnalysisMapper.getPileSize(vo)/upWeekDay;//得到上周月运营设备
		if(lossSize==0||operateSize==0){
			returnMap.put("percentage","100%");
		}else {
			double percentage =(double)(operateSize-lossSize)/operateSize;
			ChargeManageUtil.percentage(returnMap,percentage);

		}
		Double lossSizeDayDouble = (double)lossSizeDay/day;
		Double operateSizeDayDouble = (double)operateSize;
		Double lossSizeWeekDouble = (double)lossSizeWeek/7/week;
		Double operateSizeWeekDouble = (double)operateSizeWeek;
		Double lossSizeDouble = (double)lossSize;
		Double operateSizeDouble = (double)operateSize;
	    returnMap.put("lossSizeDay",Math.round(lossSizeDayDouble));//日丢单设备
		returnMap.put("operateSizeDay",Math.round(operateSizeDayDouble));//日运营设备
		returnMap.put("lossSizeWeek",Math.round(lossSizeWeekDouble));//周丢单设备
		returnMap.put("operateSizeWeek",Math.round(operateSizeWeekDouble));//周运营设备
		returnMap.put("lossSize",Math.round(lossSizeDouble));//月丢单设备
		returnMap.put("operateSize",Math.round(operateSizeDouble));//月运营设备
		returnMap.put("lossSizeDayCompare",boolCompare(lossSizeDayDouble,(double)upLossSizeDay/upDay));
		returnMap.put("operateSizeDayCompare",boolCompare(operateSizeDayDouble,(double)upOperateSize));
		returnMap.put("lossSizeWeekCompare",boolCompare(lossSizeWeekDouble,(double)upLossSizeWeek/7/upWeek));
		returnMap.put("operateSizeWeekCompare",boolCompare(operateSizeWeekDouble,(double)upOperateSizeWeek));
		returnMap.put("lossSizeCompare",boolCompare(lossSizeDouble,(double)upLossSize));
		returnMap.put("operateSizeCompare",boolCompare(operateSizeDouble,(double)upOperateSize));
		return  returnMap;
	}
	/**
	 * 在线率
	 * @param vo
	 * @return
	 */
	private DataVo setOnLineRate(DataVo vo) throws Exception {
		ChargeManageUtil.setDateCompare(vo);
		DataVo returnMap = new DataVo();
		int day = vo.getInt("day");
		int	upDay = vo.getInt("upDay");//上月天数
		int week = vo.getInt("week");//周数
		int upWeek = vo.getInt("upWeek");//上月周数
		int weekDay = vo.getInt("weekDay");
		int upWeekDay = vo.getInt("upWeekDay");
		vo.put("type",1);//1.月 2.上月
		vo.put("flag",1);//1.月 2.周
		Integer onLineRateSize =	 businessAnalysisMapper.onLineRate(vo); //得到月在线率
		Integer pileSize =	 businessAnalysisMapper.getPileSize(vo)/day;//得到月桩数
		onLineRateSize=onLineRateSize==null?0:onLineRateSize;
		pileSize = pileSize==null?0:pileSize;
		vo.put("flag",2);//1.月 2.周
		Integer onLineRateWeekSize=0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String firstForm = vo.getString("firstForm");
		String midFormat = vo.getString("midFormat");
		for (int i=0;i<week;i++){
			calendar.setTime(sdf.parse(vo.getString("midFormat")));
			calendar.add(Calendar.DATE, 7);//昨天
			vo.put("lastFormat",DateUtils.getDay(calendar.getTime()));
			onLineRateWeekSize =onLineRateWeekSize+businessAnalysisMapper.onLineRateWeek(vo);//得到周在线率
			vo.put("midFormat",DateUtils.getDay(calendar.getTime()));
		}
		vo.put("midFormat",midFormat);
		Integer pileSizeWeek =	 businessAnalysisMapper.getPileSize(vo)/weekDay;//得到周桩数
		vo.put("type",2);//1.月 2.上月
		vo.put("flag",1);//1.月 2.周
		Integer upOnLineRateSize =	 businessAnalysisMapper.onLineRate(vo); //得到上月在线率
		Integer upPileSize =	 businessAnalysisMapper.getPileSize(vo)/upDay;//得到上月桩数
		vo.put("flag",2);//1.月 2.周
		Integer upOnLineRateWeekSize=0;
		for (int i=0;i<upWeek;i++){
			calendar.setTime(sdf.parse(vo.getString("firstForm")));
			calendar.add(Calendar.DATE, 7);//昨天
			vo.put("midFormat",DateUtils.getDay(calendar.getTime()));
			upOnLineRateWeekSize=upOnLineRateWeekSize+businessAnalysisMapper.onLineRateWeek(vo);//得到上月周在线率
			vo.put("firstForm",DateUtils.getDay(calendar.getTime()));
		}
		vo.put("firstForm",firstForm);
		Integer upPileSizeWeek =	 businessAnalysisMapper.getPileSize(vo)/upWeekDay;//得到上月周桩数
	    Integer	onLineRateDay =	businessAnalysisMapper.onLineRateDay(vo);
		//获取上月月在线率
		vo.put("dateFormat",vo.getString("upDateFormat"));
		Integer	upOnLineRateDay =	businessAnalysisMapper.onLineRateDay(vo);
	     Double	onLineRateDayDouble = (double) onLineRateDay/day;
	     Double	onLineRateWeekDouble = (double) onLineRateWeekSize/7/week;
	     Double	onLineRateDouble = (double) onLineRateSize;
	     Double	pileSizeDayDouble = (double) pileSize;
	     Double	pileSizeWeekDouble = (double) pileSizeWeek;
	     Double	pileSizeDouble = (double) pileSize;
		returnMap.put("onLineRateDay",Math.round(onLineRateDayDouble));//日
		returnMap.put("onLineRateWeek",Math.round(onLineRateWeekDouble));//周
		returnMap.put("onLineRate",Math.round(onLineRateDouble));//月
		returnMap.put("pileSizeDay",Math.round(pileSizeDayDouble));//日桩
		returnMap.put("pileSizeWeek",Math.round(pileSizeWeekDouble));//周桩
		returnMap.put("pileSize",Math.round(pileSizeDouble));//月桩

		returnMap.put("pileSizeDayCompare",boolCompare(pileSizeDayDouble,(double)upPileSize));//日桩数比较
		returnMap.put("pileSizeWeekCompare",boolCompare(pileSizeWeekDouble,(double)upPileSizeWeek));//周桩数比较
		returnMap.put("pileSizeCompare",boolCompare(pileSizeDouble,(double)upPileSize));//月桩比较

		returnMap.put("onLineRateDayCompare",boolCompare(onLineRateDayDouble,(double)upOnLineRateDay/upDay));//日在线率比较
		returnMap.put("onLineRateWeekCompare",boolCompare(onLineRateWeekDouble,(double)upOnLineRateWeekSize/upDay));//周在线率比较
		returnMap.put("onLineRateCompare",boolCompare(onLineRateDouble,(double)upOnLineRateSize));//月在线率比较
		if(pileSize==0||onLineRateDay==0){
			returnMap.put("percentage","0%");
		}else {
			double percentage =(double)onLineRateDay/day/pileSize;
			ChargeManageUtil.percentage(returnMap,percentage);
		}
        return  returnMap;
	}
	/**
	 * 充电时间
	 * @param vo 本月
	 */
	private DataVo setChargetTime(DataVo vo) throws Exception {
		ChargeManageUtil.setDateCompare(vo);
		DataVo returnMap = new DataVo();
		List<DataVo> gunList	= businessAnalysisMapper.getGun(vo);//得到枪数
		int gunTimeDay = 0;
		if(gunList!=null&&gunList.size()>0){
			int day = vo.getInt("day");
			int	upDay = vo.getInt("upDay");//上月天数
			int weekDay = vo.getInt("weekDay");//月周天数
			int week = vo.getInt("week");//周数
			int upWeek = vo.getInt("upWeek");//上月周数
			for (DataVo gun :gunList){ //计算天数和枪数
				if(gun.isNotBlank("startTime")){
					String startTime = gun.getString("startTime");
					String lastMonthFormat =vo.getString("lastMonthFormat");
					int lastDay = DateUtils.getDays(startTime,lastMonthFormat);
					if(lastDay<weekDay){
						gunTimeDay=gunTimeDay+lastDay;
					}else {
						gunTimeDay=gunTimeDay+day;
					}

				}else {
					gunTimeDay=gunTimeDay+day;
				}
			}

			vo.put("type",1);//1.月 2.上月
			vo.put("flag",1);//1.月 2.周
			String monthTimeStr =	 businessAnalysisMapper.getChargeTime(vo);//得到月总充电时间
			vo.put("flag",2);//1.月 2.周
			String weekTimeStr =	 businessAnalysisMapper.getChargeTime(vo);//得到周总充电时间
			vo.put("type",2);//1.月 2.上月
			vo.put("flag",1);//1.月 2.周
			String upMonthTimeStr =	 businessAnalysisMapper.getChargeTime(vo);//得到上月月总充电时间
			vo.put("flag",2);
			String upWeekTimeStr =	 businessAnalysisMapper.getChargeTime(vo);//得到周总充电时间
			DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
			if(monthTimeStr!=null&&monthTimeStr!=""){
				BigDecimal bigDecimalB = new BigDecimal(gunTimeDay*24*60);
				BigDecimal bigDecimalc = new BigDecimal(monthTimeStr);
				returnMap.put("percentage",df1.format(bigDecimalc.divide(bigDecimalB,10, BigDecimal.ROUND_HALF_UP)));
			}else {
				returnMap.put("percentage","0%");
			}
			Double monthTime =0.0;
			Double weekTime =0.0;
			Double upMonthTime =0.0;
			Double upWeekTime =0.0;
			try {
				monthTime= Double.parseDouble(monthTimeStr);
			}catch (Exception e){
				monthTime =0.0;
			}
			try {
				weekTime= Double.parseDouble(weekTimeStr)/week;
			}catch (Exception e){
				weekTime =0.0;
			}
			try {
				upMonthTime=Double.parseDouble(upMonthTimeStr);
			}catch (Exception e){
				upMonthTime =0.0;
			}
			try {
				upWeekTime=Double.parseDouble(upWeekTimeStr)/upWeek;
			}catch (Exception e){
				upWeekTime =0.0;
			}
			Double  dayTime =  monthTime/day/60;
			Double  upDayTime = upMonthTime/upDay/60;
			returnMap.put("chargeTimeCompare",boolCompare(monthTime,upMonthTime));
			returnMap.put("weekChargeTimeCompare",boolCompare(weekTime,upWeekTime));
			returnMap.put("dayChargeTimeCompare",boolCompare(dayTime,upDayTime));
			returnMap.put("chargeTime",Math.round(monthTime/60));
			returnMap.put("weekChangerTime",Math.round(weekTime/60));
			returnMap.put("dayChargeTime",Math.round(dayTime));
			return  returnMap;
		}
		return null;
	}
	/**
	 * 转换率
	 * @param vo 月
	 */
	private DataVo setConversion(DataVo vo) throws Exception {
		ChargeManageUtil.setDateCompare(vo);
		int day = vo.getInt("day");
		int	upDay = vo.getInt("upDay");//上月天数
		int week = vo.getInt("week");//周数
		int upWeek = vo.getInt("upWeek");//上月周数
		vo.put("type",1);//1.月 2.上月
		vo.put("flag",1);//1.月 2.周
		DataVo conversionTime =	 businessAnalysisMapper.getConversion(vo);//得到月总转换率
		Double diep = 0.0;
		Double dicp = 0.0;
		if(conversionTime!=null){
			diep=conversionTime.getDouble("diep");//设备用电量
			dicp=conversionTime.getDouble("dicp");//车充电量
		}
		vo.put("flag",2);//1.月 2.周
		DataVo conversionWeek =	 businessAnalysisMapper.getConversion(vo);//得到月周充电时间
		Double diepWeek = 0.0;
		Double dicpWeek = 0.0;
				if(conversionWeek!=null){
			diepWeek=conversionWeek.getDouble("diep");//设备用电量
			dicpWeek=conversionWeek.getDouble("dicp");//车充电量
		}
		vo.put("type",2);//1.月 2.上月
		vo.put("flag",1);//1.月 2.周
		DataVo upConversionTime=	 businessAnalysisMapper.getConversion(vo);//得到上月总充电时长
		Double upDiep = 0.0;
		Double upDicp = 0.0;
		if(upConversionTime!=null){
			upDiep=upConversionTime.getDouble("diep");//设备用电量
			upDicp=upConversionTime.getDouble("dicp");//车充电量
		}
		vo.put("flag",2);//1.月 2.周
		DataVo upConversionWeek =	 businessAnalysisMapper.getConversion(vo);//得到上月周总充电时间
		Double upDiepWeek = 0.0;
		Double upDicpWeek = 0.0;
		if(upConversionWeek!=null){
			upDiepWeek=upConversionWeek.getDouble("diep");//设备用电量
			upDicpWeek=upConversionWeek.getDouble("dicp");//车充电量
		}
	   DataVo 	returnMap  = new DataVo();
		Double diepDay = diep/day/10000;//日用电量
		Double dicpDay = dicp/day/10000;//日充电量
		diepWeek = diepWeek/week/10000;//周用电量
		dicpWeek =dicpWeek/week/10000;//周用电量
		returnMap.put("diepDay",Math.round(diepDay));     //日用电量           ;
		returnMap.put("dicpDay",Math.round(dicpDay));       //日充电量
		returnMap.put("diepWeek",Math.round(diepWeek));     //周用电量        ;
		returnMap.put("dicpWeek",Math.round(dicpWeek));      //周充电量
		returnMap.put("diep",Math.round(diep/10000));         //月用的量
		returnMap.put("dicp",Math.round(dicp/10000));       //月充电量
		returnMap.put("diepDayCompare",boolCompare(diepDay,upDiep/upDay/10000));
		returnMap.put("dicpDayCompare",boolCompare(dicpDay,upDicp/upDay/10000));
		returnMap.put("diepWeekCompare",boolCompare(diepWeek,upDiepWeek/upWeek/10000));
		returnMap.put("dicpWeekCompare",boolCompare(dicpWeek,upDicpWeek/upWeek/10000));
		returnMap.put("diepCompare",boolCompare( diep,upDiep));
		returnMap.put("dicpCompare",boolCompare(dicp,upDicp));
        if(dicp==0||diep==0){
			returnMap.put("percentage","0%");
              }else {
			double percentage =dicp/diep;
        	ChargeManageUtil.percentage(returnMap,percentage);
		}
		return  returnMap;
}

	/**
	 * 判断2值大小
	 * @return
	 */
	public    int boolCompare(Double a,Double b){
		if(a==b) {
			return 0;
		}
		if(a>b){
			return  1;
		}else {
			return -1;
		}
	}
}

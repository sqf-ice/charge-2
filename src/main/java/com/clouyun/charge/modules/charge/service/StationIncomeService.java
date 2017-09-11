package com.clouyun.charge.modules.charge.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.RateTimeUtils;
import com.clouyun.charge.modules.charge.mapper.StationIncomeMapper;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: StationIncomeService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年9月2日
 */
@Service
public class StationIncomeService {
	
	@Autowired
	StationIncomeMapper stationIncomeMapper;
	
	@Autowired
	UserService userService;
	
	
	BigDecimal noElceFee = null;
	BigDecimal noChgPower = null;
	
	double elceFee = 0;
	double incomeZxyg1 =0;  //尖 收入
	double incomeZxyg2 =0;	//峰 收入
	double incomeZxyg3 =0;	//平 收入
	double incomeZxyg4 =0;	//谷 收入 
	
	double chgPower = 0;	
	double powerZxyg1 = 0;	//尖
	double powerZxyg2 = 0;	//峰
	double powerZxyg3 = 0;	//平
	double powerZxyg4 = 0;	//谷
	
	/**
	 * 场站收入 按时间段 显示场站信息汇总
	 * @throws Exception 
	 */
	public PageInfo getStationIncome(Map map) throws Exception{
		
		DataVo vo = getVaildate(map);
		String endDate = vo.getString("endDate");
		Calendar endCalendar = CalendarUtils.convertStrToCalendar(endDate, CalendarUtils.yyyyMMdd);
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		vo.put("endDate", CalendarUtils.formatCalendar(endCalendar, CalendarUtils.yyyyMMdd));
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List<DataVo> stationIncomes = stationIncomeMapper.getStationIncome(vo);
		
		//计算非费率总收入(电费总收入-尖峰平谷总收入)
		if(stationIncomes != null && !stationIncomes.isEmpty()){
			for (DataVo dataVo : stationIncomes) {
				dataVo = getScal(dataVo);
				//设置列表显示日期
				dataVo.put("time", vo.getString("startDate")+" 至 "+endDate);
			}
		}
		return new PageInfo(stationIncomes);
	}

	/**
	 * 场站收入 按时间段 显示场站信息汇总[合计]
	 * @throws Exception 
	 */
	public DataVo getStationIncomeTotal(Map map) throws Exception{
		DataVo vo = getVaildate(map);
		if(vo.isNotBlank("pageNum")){
			vo.remove("pageNum");
		}
		if(vo.isNotBlank("pageSize")){
			vo.remove("pageSize");
		}
		String endDate = vo.getString("endDate");
		Calendar endCalendar = CalendarUtils.convertStrToCalendar(endDate, CalendarUtils.yyyyMMdd);
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		vo.put("endDate", CalendarUtils.formatCalendar(endCalendar, CalendarUtils.yyyyMMdd));
		vo = stationIncomeMapper.getStationIncomeTotal(vo);
		if(vo != null){
			vo = getScal(vo);
			vo.remove("orgName");
			vo.remove("stationName");
		}
		return vo;
	}
	
	/**
	 * 场站收入详情 显示场站每日数据汇总
	 */
	public PageInfo getStationByDayIncome(Map map) throws Exception{
		//数据验证及权限
		DataVo vo = getVaildate(map);
		if(vo.isBlank("stationId")){
			throw new BizException(1102001, "场站Id");
		}
		String endDate = vo.getString("endDate");
		Calendar endCalendar = CalendarUtils.convertStrToCalendar(endDate, CalendarUtils.yyyyMMdd);
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		vo.put("endDate", CalendarUtils.formatCalendar(endCalendar, CalendarUtils.yyyyMMdd));
		//设置分页
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List<DataVo> stationByDayIncomelist = stationIncomeMapper.getStationByDayIncome(vo);
		
		//计算非费率总收入(电费总收入-尖峰平谷总收入)
		if(stationByDayIncomelist != null && !stationByDayIncomelist.isEmpty()){
			for (DataVo dataVo : stationByDayIncomelist) {
				dataVo = getScal(dataVo);
			}
		}
		return new PageInfo(stationByDayIncomelist);
	}
	
	
	
	
	/**
	 * 场站收入导出
	 * @throws Exception 
	 */
	public void exportStationIncome(Map map,HttpServletResponse response) throws Exception{
		map.remove("pageSize");
		map.remove("pageNum");
		List list = getStationIncome(map).getList();
		
		List headList = setExcelHead();
		List titleList = setExcelTitle();
		
		DataVo total = getStationIncomeTotal(map);
		total.put("time", "合计");
		list.add(total);
		ExportUtils.exportExcel(list,response,headList,titleList,"场站收入表");
	}
	
	/**
	 * 场站收入详情导出
	 * @throws Exception 
	 */
	public void exportStationByDayIncome(Map map,HttpServletResponse response) throws Exception{
		map.remove("pageSize");
		map.remove("pageNum");
		List list = getStationByDayIncome(map).getList();
		List headList = setExcelHead();
		List titleList = setExcelTitle();
		
		DataVo total = getStationIncomeTotal(map);
		total.put("time", "合计");
		list.add(total);
		ExportUtils.exportExcel(list,response,headList,titleList,"场站每日收入表");
	}
	
	
	/**
	 * 设置导出文件ExcelHead
	 */
	private List setExcelHead(){
		List headList = new ArrayList();
		headList.add("日期");
		headList.add("运营商");
		headList.add("场站");
		headList.add("总收入(元)");
		headList.add("电费总收入(元)");
		headList.add("非费率总收入(元)");
		headList.add("尖价总收入(元)");
		headList.add("峰价总收入(元)");
		headList.add("平价总收入(元)");
		headList.add("谷价总收入(元)");
		headList.add("服务费收入(元)");
		headList.add("停车费收入(元)");
		headList.add("充电总量(kW·h)");
		headList.add("非费率电量(kW·h)");
		headList.add("尖价总量(kW·h)");
		headList.add("峰价总量(kW·h)");
		headList.add("平价总量(kW·h)");
		headList.add("谷价总量(kW·h)");
		return headList;
	}
	
	/**
	 * 设置导出文件ExcelTitle
	 */
	private List setExcelTitle(){
		List titleList = new ArrayList();
		titleList.add("time");
		titleList.add("orgName");
		titleList.add("stationName");
		titleList.add("amount");
		titleList.add("elceFee");
		titleList.add("noElceFee");
		titleList.add("incomeZxyg1");
		titleList.add("incomeZxyg2");
		titleList.add("incomeZxyg3");
		titleList.add("incomeZxyg4");
		titleList.add("servFee");
		titleList.add("parkFee");
		titleList.add("chgPower");
		titleList.add("noChgPower");
		titleList.add("powerZxyg1");
		titleList.add("powerZxyg2");
		titleList.add("powerZxyg3");
		titleList.add("powerZxyg4");
		return titleList;
	}
	/**
	 * 数据验证及权限
	 */
	private DataVo getVaildate(Map map) throws Exception{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		if(vo.isBlank("startDate")){
			throw new BizException(1102001,"开始日期");
		}
		if(vo.isBlank("endDate")){
			throw new BizException(1102001,"开始日期");
		}
		
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		Set<Integer> stationIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.STATION.dataType);
		if(stationIds !=null){
			vo.put("stationIds", stationIds);
		}
		return vo;
	}
	
	/**
	 * 计算非费率总收入
	 * 计算非费率总电量
	 */
	private DataVo getScal(DataVo dataVo){
		//非费率总收入
		elceFee = dataVo.getDouble("elceFee");
		incomeZxyg1 = dataVo.getDouble("incomeZxyg1");
		incomeZxyg2 = dataVo.getDouble("incomeZxyg2");
		incomeZxyg3 = dataVo.getDouble("incomeZxyg3");
		incomeZxyg4 = dataVo.getDouble("incomeZxyg4");
		noElceFee = new BigDecimal(elceFee);
		noElceFee = noElceFee.subtract(new BigDecimal(incomeZxyg1));
		noElceFee = noElceFee.subtract(new BigDecimal(incomeZxyg2));
		noElceFee = noElceFee.subtract(new BigDecimal(incomeZxyg3));
		noElceFee = noElceFee.subtract(new BigDecimal(incomeZxyg4));
		dataVo.put("noElceFee", noElceFee.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		//非费率总电量
		chgPower = dataVo.getDouble("chgPower");
		powerZxyg1 = dataVo.getDouble("powerZxyg1");
		powerZxyg2 = dataVo.getDouble("powerZxyg2");
		powerZxyg3 = dataVo.getDouble("powerZxyg3");
		powerZxyg4 = dataVo.getDouble("powerZxyg4");
		noChgPower = new BigDecimal(chgPower);
		noChgPower = noChgPower.subtract(new BigDecimal(powerZxyg1));
		noChgPower = noChgPower.subtract(new BigDecimal(powerZxyg2));
		noChgPower = noChgPower.subtract(new BigDecimal(powerZxyg3));
		noChgPower = noChgPower.subtract(new BigDecimal(powerZxyg4));
		dataVo.put("noChgPower", noChgPower.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return dataVo;
	}
	
	/**
	 * 场站收入  订单详情
	 * @throws Exception 
	 */
	public PageInfo getBillPayByStation(Map map) throws Exception{
		//数据验证及权限
		DataVo vo = getVaildate(map);
		
		String endDate = vo.getString("endDate");
		Calendar endCdr = CalendarUtils.convertStrToCalendar(endDate, CalendarUtils.yyyyMMdd);
		endCdr.add(Calendar.DAY_OF_MONTH, 1);
		vo.put("endDate", CalendarUtils.formatCalendar(endCdr, CalendarUtils.yyyyMMdd));
		
		if(vo.isBlank("stationId")){
			throw new BizException(1102001, "场站Id");
		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List<DataVo> billPayList = stationIncomeMapper.getBillPayByStation(vo);
		
		
		List<Integer> billPayIds = new ArrayList<Integer>();
		for (DataVo dataVo : billPayList) {
			billPayIds.add(dataVo.getInt("billPayId"));
		}
		//根据订单查询SOC
		vo.clear();
		vo.put("billPayIds", billPayIds);
		List<DataVo> socList = stationIncomeMapper.getBillPaySOC(vo);
		Calendar createCalendar = null;
		Calendar startCalendar = null;
		Calendar endCalendar = null;
		Long time = null;
		BigDecimal bd = new BigDecimal(60);
		Integer billPayId = null;
		String billPayNo = "";
		String[] cards = null;
		Double prcZxygz1 = null;
		Double prcZxygz2 = null;
		Double prcZxygz3 = null;
		Double prcZxygz4 = null;

		
		for (DataVo dataVo : billPayList) {
			billPayId = dataVo.getInt("billPayId");
			
			//订单日期不要时分秒
			if(dataVo.isNotBlank("createTime")){
				createCalendar = CalendarUtils.convertStrToCalendar(dataVo.getString("createTime"), CalendarUtils.yyyyMMddHHmmss);
				dataVo.put("createTime", CalendarUtils.formatCalendar(createCalendar, CalendarUtils.yyyyMMdd));
			}
			
			//判断订单的启动方式  显示充电卡号or会员手机号
			billPayNo = dataVo.getString("billPayNo");
			if(billPayNo.contains("CARD")){
				cards = dataVo.getString("billDesc").split("_");
				dataVo.put("hyCardOrPhone", cards[0]);
			}else if(billPayNo.contains("PAY") ){
				dataVo.put("hyCardOrPhone", dataVo.getString("consPhone"));
			}
			
			if(dataVo.isBlank("startTime") || dataVo.isBlank("endTime")){
				continue;
			}
			//根据充电开始时间结束时间获取峰平谷时长 (将返回秒转换为分钟)
			startCalendar = CalendarUtils.convertStrToCalendar(dataVo.getString("startTime"), CalendarUtils.yyyyMMddHHmmss);
			endCalendar = CalendarUtils.convertStrToCalendar(dataVo.getString("endTime"), CalendarUtils.yyyyMMddHHmmss);
			Map<String, Long> rateTimeLen = RateTimeUtils.rateTimeLen(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis());
			if(rateTimeLen != null){
				time = rateTimeLen.get("尖");
				if(time != null){
					dataVo.put("powerZxyg1Time", new BigDecimal(time).divide(bd,2,BigDecimal.ROUND_HALF_UP));
				}else{
					dataVo.put("powerZxyg1Time", 0);
				}
				time = rateTimeLen.get("峰");
				if(time != null){
					dataVo.put("powerZxyg2Time", new BigDecimal(time).divide(bd,2, BigDecimal.ROUND_HALF_UP));
				}else{
					dataVo.put("powerZxyg2Time", 0);
				}
				time = rateTimeLen.get("平");
				if(time != null){
					dataVo.put("powerZxyg3Time", new BigDecimal(time).divide(bd,2, BigDecimal.ROUND_HALF_UP));
				}else{
					dataVo.put("powerZxyg3Time", 0);
				}
				time = rateTimeLen.get("谷");
				if(time != null){
					dataVo.put("powerZxyg4Time", new BigDecimal(time).divide(bd,2, BigDecimal.ROUND_HALF_UP));
				}else{
					dataVo.put("powerZxyg4Time", 0);
				}
			}
			//计算尖峰平谷价(单价*电量)
			prcZxygz1 = dataVo.getDouble("prcZxygz1", 0d);
			powerZxyg1 = dataVo.getDouble("powerZxyg1", 0d);
			dataVo.put("prcZxygz1",new BigDecimal(prcZxygz1).multiply(new BigDecimal(powerZxyg1)).setScale(2, BigDecimal.ROUND_HALF_UP));
			prcZxygz2 = dataVo.getDouble("prcZxygz2", 0d);
			powerZxyg2 = dataVo.getDouble("powerZxyg2", 0d);
			dataVo.put("prcZxygz2",new BigDecimal(prcZxygz2).multiply(new BigDecimal(powerZxyg2)).setScale(2, BigDecimal.ROUND_HALF_UP));
			prcZxygz3 = dataVo.getDouble("prcZxygz3", 0d);
			powerZxyg3 = dataVo.getDouble("powerZxyg3", 0d);
			dataVo.put("prcZxygz3",new BigDecimal(prcZxygz3).multiply(new BigDecimal(powerZxyg3)).setScale(2, BigDecimal.ROUND_HALF_UP));
			prcZxygz4 = dataVo.getDouble("prcZxygz4", 0d);
			powerZxyg4 = dataVo.getDouble("powerZxyg4", 0d);
			dataVo.put("prcZxygz4",new BigDecimal(prcZxygz4).multiply(new BigDecimal(powerZxyg4)).setScale(2, BigDecimal.ROUND_HALF_UP));
			for (DataVo socVo : socList) {
				if(billPayId.equals(socVo.getInt("billPayId"))){
					dataVo.put("socBeg", socVo.get("socBeg"));
					dataVo.put("socEnd", socVo.get("socEnd"));
					break;
				}
			}
		}
		return new PageInfo(billPayList);
	}
	
	/**
	 * 场站收入  订单详情导出
	 */
	public void exportBillPayByStation(Map map,HttpServletResponse response) throws Exception{
		map.remove("pageSize");
		map.remove("pageNum");
		List<DataVo> list = getBillPayByStation(map).getList();
		List<String>	headList =  new ArrayList<>();
		List<String>	titleList =  new ArrayList<>();
		headList.add("订单号");
		headList.add("充电场站名称");
		headList.add("设备名称");
		headList.add("订单日期");
		headList.add("充电开始时间");
		headList.add("充电结束时间 ");
		headList.add("会员卡号/电话");
		headList.add("会员名称");
		headList.add("集团名");
		headList.add("车牌号");
		headList.add("线路");
		headList.add("初始SOC");
		headList.add("结束SOC");
		headList.add("充电量(kW·h)");
		headList.add("尖价电量(kW·h)");
		headList.add("尖价充电时长(分钟)");
		headList.add("峰价电量(kW·h)");
		headList.add("峰价充电时长(分钟)");
		headList.add("平价电量(kW·h)");
		headList.add("平价充电时长(分钟)");
		headList.add("谷价电量(kW·h)");
		headList.add("谷价充电时长(分钟)");
		headList.add("消费金额(元)");
		headList.add("尖价电费(元)");
		headList.add("峰价电费(元)");
		headList.add("平价电费(元)");
		headList.add("谷价电费(元)");
		headList.add("服务费(元)");
		titleList.add("billPayNo");
		titleList.add("stationName");
		titleList.add("pileName");
		titleList.add("createTime");
		titleList.add("startTime");
		titleList.add("endTime");
		titleList.add("hyCardOrPhone");
		titleList.add("consName");
		titleList.add("groupName");
		titleList.add("licensePlate");
		titleList.add("line");
		titleList.add("socBeg");
		titleList.add("socEnd");
		titleList.add("chgPower");
		titleList.add("powerZxyg1");
		titleList.add("powerZxyg1Time");
		titleList.add("powerZxyg2");
		titleList.add("powerZxyg2Time");
		titleList.add("powerZxyg3");
		titleList.add("powerZxyg3Time");
		titleList.add("powerZxyg4");
		titleList.add("powerZxyg4Time");
		titleList.add("amount");
		titleList.add("prcZxygz1");
		titleList.add("prcZxygz2");
		titleList.add("prcZxygz3");
		titleList.add("prcZxygz4");
		titleList.add("servFee");
		if(list != null && !list.isEmpty()){
			DataVo totalVo = new DataVo();
			BigDecimal chgPowerTotal = BigDecimal.ZERO;
			BigDecimal powerZxyg1Total = BigDecimal.ZERO;
			BigDecimal powerZxyg1TimeTotal = BigDecimal.ZERO;
			BigDecimal powerZxyg2Total = BigDecimal.ZERO;
			BigDecimal powerZxyg2TimeTotal = BigDecimal.ZERO;
			BigDecimal powerZxyg3Total = BigDecimal.ZERO;
			BigDecimal powerZxyg3TimeTotal = BigDecimal.ZERO;
			BigDecimal powerZxyg4Total = BigDecimal.ZERO;
			BigDecimal powerZxyg4TimeTotal = BigDecimal.ZERO;
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal prcZxygz1Total = BigDecimal.ZERO;
			BigDecimal prcZxygz2Total = BigDecimal.ZERO;
			BigDecimal prcZxygz3Total = BigDecimal.ZERO;
			BigDecimal prcZxygz4Total = BigDecimal.ZERO;
			BigDecimal servFeeTotal = BigDecimal.ZERO;
			for (DataVo vo : list) {
				chgPowerTotal = chgPowerTotal.add(new BigDecimal(vo.getDouble("chgPower")));
				powerZxyg1Total = powerZxyg1Total.add(new BigDecimal(vo.getDouble("powerZxyg1")));
				powerZxyg1TimeTotal = powerZxyg1TimeTotal.add(new BigDecimal(vo.getDouble("powerZxyg1Time")));
				powerZxyg2Total = powerZxyg2Total.add(new BigDecimal(vo.getDouble("powerZxyg2")));
				powerZxyg2TimeTotal = powerZxyg2TimeTotal.add(new BigDecimal(vo.getDouble("powerZxyg2Time")));
				powerZxyg3Total = powerZxyg3Total.add(new BigDecimal(vo.getDouble("powerZxyg3")));
				powerZxyg3TimeTotal = powerZxyg3TimeTotal.add(new BigDecimal(vo.getDouble("powerZxyg3Time")));
				powerZxyg4Total = powerZxyg4Total.add(new BigDecimal(vo.getDouble("powerZxyg4")));
				powerZxyg4TimeTotal = powerZxyg4TimeTotal.add(new BigDecimal(vo.getDouble("powerZxyg4Time")));
				amount = amount.add(new BigDecimal(vo.getDouble("amount")));
				prcZxygz1Total = prcZxygz1Total.add(new BigDecimal(vo.getDouble("prcZxygz1")));
				prcZxygz2Total = prcZxygz2Total.add(new BigDecimal(vo.getDouble("prcZxygz2")));
				prcZxygz3Total = prcZxygz3Total.add(new BigDecimal(vo.getDouble("prcZxygz3")));
				prcZxygz4Total = prcZxygz4Total.add(new BigDecimal(vo.getDouble("prcZxygz4")));
				servFeeTotal = servFeeTotal.add(new BigDecimal(vo.getDouble("servFee")));
			}
			totalVo.put("billPayNo", "合计");
			totalVo.put("chgPower", chgPowerTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg1", powerZxyg1Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg1Time", powerZxyg1TimeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg2", powerZxyg2Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg2Time", powerZxyg2TimeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg3", powerZxyg3Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg3Time", powerZxyg3TimeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg4", powerZxyg4Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("powerZxyg4Time", powerZxyg4TimeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("prcZxygz1", prcZxygz1Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("prcZxygz2", prcZxygz2Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("prcZxygz3", prcZxygz3Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("prcZxygz4", prcZxygz4Total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			totalVo.put("servFee", servFeeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			//置空
			chgPowerTotal =null;
			powerZxyg1Total =null;
			powerZxyg1TimeTotal =null;
			powerZxyg2Total =null;
			powerZxyg2TimeTotal =null;
			powerZxyg3Total =null;
			powerZxyg3TimeTotal =null;
			powerZxyg4Total =null;
			powerZxyg4TimeTotal =null;
			amount =null;
			prcZxygz1Total =null;
			prcZxygz2Total =null;
			prcZxygz3Total =null;
			prcZxygz4Total =null;
			servFeeTotal =null;
			list.add(totalVo);
		}
		
		ExportUtils.exportExcel(list,response,headList,titleList,"场站订单汇总表");
	}
	
}

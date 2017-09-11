package com.clouyun.charge.modules.charge.service;

import com.clou.common.utils.ObjectUtils;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.modules.charge.mapper.StationProfitMapper;
import com.clouyun.charge.modules.document.mapper.StationMapper;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.*;



/**
 * 描述: 场站利润业务类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月14日 上午10:14:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StationProfitService {

	public static final Logger logger = LoggerFactory.getLogger(StationProfitService.class);
	
	@Autowired
	StationProfitMapper stationProfitMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
	StationMapper stationMapper;
	
	public ResultVo queryStationProfits(Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		DataVo params = new DataVo(map);
		initVal(params);
		// 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(params);
        }

		params.put("sort","station_total_charge");
		params.put("order","desc");

		List<Map> result = stationProfitMapper.queryStationProfit(params);
		
		PageInfo page = new PageInfo(result);
		//查询合计
		//Map profitSubtotal = queryProfitSub(params);

//		Map map2 = new HashMap();
//		map2.put("pageList", page);
		//map2.put("totalMap", profitSubtotal);
		resultVo.setData(page);

		map = null;
		params = null;
		result = null;

		return resultVo;
	}

	public Map queryProfitSub(DataVo params) throws BizException {
		//获取权限
		initVal(params);
		params.remove("pageSize");
		params.remove("pageNum");
		Map profitSubtotal = stationProfitMapper.queryProfitSubtotal(params);
		Calendar cal;
		String settlementMonth = params.getString("settlementMonth");
		if(settlementMonth != null && !"".equals(settlementMonth)){
			cal = CalendarUtils.convertStrToCalendar(settlementMonth, CalendarUtils.yyyyMM);
		}else{
			cal = Calendar.getInstance();
		}
		cal.add(Calendar.MONTH,-1);
		String lastMonth = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMM);
		params.put("settlementMonth", lastMonth);

		Map lastProfitSubtotal = stationProfitMapper.queryProfitSubtotal(params);

		profitSubtotal.put("allStationTargetChargeCompare",0);
		profitSubtotal.put("allStationTotalChargeCompare",0);
		profitSubtotal.put("allStationTargetIncomeCompare",0);
		profitSubtotal.put("allFixedIncomeSubtotalCompare",0);
		profitSubtotal.put("allUnfixedIncomeSubtotalCompare",0);
		profitSubtotal.put("allStationIncomeSubtotalCompare",0);
		profitSubtotal.put("allFixedExpendSubtotalCompare",0);
		profitSubtotal.put("allUnfixedExpendSubtotalCompare",0);
		profitSubtotal.put("allCostTotalCompare",0);
		profitSubtotal.put("allStationGrossProfitCompare",0);

		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allStationTargetCharge")) && !ObjectUtils.isNull(profitSubtotal.get("allStationTargetCharge"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allStationTargetCharge").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allStationTargetCharge").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allStationTargetChargeCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allStationTotalCharge")) && !ObjectUtils.isNull(profitSubtotal.get("allStationTotalCharge"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allStationTotalCharge").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allStationTotalCharge").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allStationTotalChargeCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allStationTargetIncome")) && !ObjectUtils.isNull(profitSubtotal.get("allStationTargetIncome"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allStationTargetIncome").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allStationTargetIncome").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allStationTargetIncomeCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allFixedIncomeSubtotal")) && !ObjectUtils.isNull(profitSubtotal.get("allFixedIncomeSubtotal"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allFixedIncomeSubtotal").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allFixedIncomeSubtotal").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allFixedIncomeSubtotalCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allUnfixedIncomeSubtotal")) && !ObjectUtils.isNull(profitSubtotal.get("allUnfixedIncomeSubtotal"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allUnfixedIncomeSubtotal").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allUnfixedIncomeSubtotal").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allUnfixedIncomeSubtotalCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allStationIncomeSubtotal")) && !ObjectUtils.isNull(profitSubtotal.get("allStationIncomeSubtotal"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allStationIncomeSubtotal").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allStationIncomeSubtotal").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allStationIncomeSubtotalCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allFixedExpendSubtotal")) && !ObjectUtils.isNull(profitSubtotal.get("allFixedExpendSubtotal"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allFixedExpendSubtotal").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allFixedExpendSubtotal").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allFixedExpendSubtotalCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allUnfixedExpendSubtotal")) && !ObjectUtils.isNull(profitSubtotal.get("allUnfixedExpendSubtotal"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allUnfixedExpendSubtotal").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allUnfixedExpendSubtotal").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allUnfixedExpendSubtotalCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allCostTotal")) && !ObjectUtils.isNull(profitSubtotal.get("allCostTotal"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allCostTotal").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allCostTotal").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allCostTotalCompare",compareTo);
		}
		if(!ObjectUtils.isNull(lastProfitSubtotal.get("allStationGrossProfit")) && !ObjectUtils.isNull(profitSubtotal.get("allStationGrossProfit"))){
			BigDecimal now =  new BigDecimal(profitSubtotal.get("allStationGrossProfit").toString());
			BigDecimal last = new BigDecimal(lastProfitSubtotal.get("allStationGrossProfit").toString());
			int compareTo = now.compareTo(last);
			profitSubtotal.put("allStationGrossProfitCompare",compareTo);
		}

		cal = null;
		lastProfitSubtotal = null;

		return profitSubtotal;
	}


	private void initVal(DataVo params) throws BizException {
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId")){
            throw new BizException(1000006);
        }
		// 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		if (!userService.isSuperMan(params.getInt("userId")) && !userService.isGlobalMan(params.getInt("userId"))){
        	if(userRoleDataById != null && userRoleDataById.size() > 0){
				params.put("stationIds", userRoleDataById);
        	}
        }
	}

	public List<Map> queryIncomeTrends(Integer stationId) throws BizException{
		
		if(stationId == null){
			throw new BizException(1102001,"场站id");
		}
		
		Map map = new HashMap();
		map.put("stationId", stationId);
		List<Map> incomeTrends = stationProfitMapper.queryIncomeTrends(map);
		if(incomeTrends != null && incomeTrends.size() > 0){
			Map map1 = incomeTrends.get(incomeTrends.size() - 1);
			Calendar settlementMonth = CalendarUtils.convertStrToCalendar(map1.get("settlementMonth").toString(), CalendarUtils.yyyyMM);
			addProfitTrendsVal(incomeTrends, settlementMonth);
		}else{
			Calendar settlementMonth = Calendar.getInstance();
			addProfitTrendsVal(incomeTrends, settlementMonth);
		}

		if (null != incomeTrends && incomeTrends.size() > 0) {
			Collections.sort(incomeTrends,new Comparator<Map>() {
				@Override
				public int compare(Map o1, Map o2) {
					int ret = 0;
					//比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
					Calendar o1Time = CalendarUtils.convertStrToCalendar(o1.get("settlementMonth").toString(), CalendarUtils.yyyyMM);
					Calendar o2Time = CalendarUtils.convertStrToCalendar(o2.get("settlementMonth").toString(), CalendarUtils.yyyyMM);
					ret = o1Time.compareTo(o2Time);//逆序的话就用o2.compareTo(o1)即可
					return ret;
				}
			});
		}
		map = null;
		return incomeTrends;
	}

	private void addProfitTrendsVal(List<Map> incomeTrends, Calendar settlementMonth) {
		int listSize = incomeTrends.size();
		for (int i = 0; i < 6 - listSize; i++) {
            settlementMonth.add(Calendar.MONTH,-1);
            Map map2 = new HashMap();
            map2.put("stationIncomeSubtotal", 0);
            map2.put("stationGrossProfit", 0);
            map2.put("costTotal", 0);
            map2.put("settlementMonth", CalendarUtils.formatCalendar(settlementMonth,CalendarUtils.yyyyMM));
            incomeTrends.add(map2);
        }
	}

	public Map queryProfitByKey(Integer spId) throws BizException{
		if(spId == null){
			throw new BizException(1102001,"场站利润Id");
		}
		Map map = stationProfitMapper.queryStationProfitByKey(spId);

		if (map == null || map.size() <= 0){
			throw new BizException(1102020);
		}

		Map resultMap = new HashMap();
		
		Map chargeInfo = new HashMap();
		DataVo vo = new DataVo(map);

		chargeInfo.put("stationTotalPower", map.get("stationTotalPower"));
		chargeInfo.put("stationTargetCharge", map.get("stationTargetCharge"));
		chargeInfo.put("stationTotalCharge", map.get("stationTotalCharge"));
		chargeInfo.put("stationGoalCharge", map.get("stationGoalCharge"));
		chargeInfo.put("stationTestCharge", map.get("stationTestCharge"));
		chargeInfo.put("stationUseCharge", map.get("stationUseCharge"));
		chargeInfo.put("stationOfficeCharge", map.get("stationOfficeCharge"));
		chargeInfo.put("pileTotalLoss", map.get("pileTotalLoss"));
		chargeInfo.put("pileTotalPower", map.get("pileTotalPower"));
		resultMap.put("chargeInfo", chargeInfo);
		
		Map incomeInfo = new HashMap();
		incomeInfo.put("stationTargetIncome", map.get("stationTargetIncome"));
		incomeInfo.put("orderIncome", map.get("orderIncome"));
		incomeInfo.put("inPileTotalIncome", map.get("inPileTotalIncome"));
		incomeInfo.put("inPilePmIncome", map.get("inPilePmIncome"));
		incomeInfo.put("inFacRentalFee", map.get("inFacRentalFee"));
		incomeInfo.put("inParkRentalFee", map.get("inParkRentalFee"));
		incomeInfo.put("inLandRentalFee", map.get("inLandRentalFee"));
		incomeInfo.put("fixedIncomeSubtotal", map.get("fixedIncomeSubtotal"));
		incomeInfo.put("otherIncome", map.get("otherIncome"));
		incomeInfo.put("unfixedIncomeSubtotal", map.get("unfixedIncomeSubtotal"));
		incomeInfo.put("stationIncomeSubtotal", map.get("stationIncomeSubtotal"));
		resultMap.put("incomeInfo", incomeInfo);
		
		Map costInfo = new HashMap();
		costInfo.put("eleFee", map.get("eleFee"));
		costInfo.put("waterFee", map.get("waterFee"));
		costInfo.put("networkFee", map.get("networkFee"));
		costInfo.put("diviExpend", map.get("diviExpend"));
		costInfo.put("exLandRentalFee", map.get("exLandRentalFee"));
		costInfo.put("exLandRentalExpend", map.get("exLandRentalExpend"));
		costInfo.put("exHouseRentalFee", map.get("exHouseRentalFee"));
		costInfo.put("propertyFee", map.get("propertyFee"));
		costInfo.put("artificialFee", map.get("artificialFee"));
		costInfo.put("depreFixedAsset", map.get("depreFixedAsset"));
		costInfo.put("costAmortization", map.get("costAmortization"));
		costInfo.put("parkingFee", map.get("parkingFee"));
		costInfo.put("fixedExpendSubtotal", map.get("fixedExpendSubtotal"));
		costInfo.put("repairFee", map.get("repairFee"));
		costInfo.put("lowConsumable", map.get("lowConsumable"));
		costInfo.put("otherExpenses", map.get("otherExpenses"));
		costInfo.put("unfixedExpendSubtotal", map.get("unfixedExpendSubtotal"));
		costInfo.put("costTotal", map.get("costTotal"));
		costInfo.put("stationGrossProfit", map.get("stationGrossProfit"));
		resultMap.put("costInfo", costInfo);

		map = null;
		return resultMap;
	}

	public ResultVo importProfit(MultipartFile excelFile, Map map) throws BizException,Exception{
		ResultVo vo = new ResultVo();
		String canSave = "true";
		DataVo params = new DataVo(map);
		if (params.isBlank("userId")){
        	throw new BizException(1000006);
        }
        //选择需要结算的月份
        if(params.isBlank("settlementMonth") || "undefined".equals(params.getString("settlementMonth"))){
			throw new BizException(1102021);
		}
		List<Map<String, String>> allProfits = new ArrayList<>();

		//解析excel
		List<Object[]> arrayList = parseExcel(excelFile);

		Set<Integer> stationIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);

		List<Map> stationAll = stationMapper.getStationAll(new DataVo());
		Map<String,Map> stationInfoMap = new HashMap();
		Map<String,List> stationOrgInfoMap = new HashMap();

		if(stationAll != null && stationAll.size() > 0){
			for (Map obj : stationAll) {
				if(obj.get("stationName") != null){
					stationInfoMap.put(obj.get("stationName").toString(), obj);
				}
				if(obj.get("orgName") != null){
					List<Map> list = null;
					if (stationOrgInfoMap.get(obj.get("orgName").toString()) != null){
						list = stationOrgInfoMap.get(obj.get("orgName").toString());
					}else{
						list = new ArrayList();
					}
					list.add(obj);
					stationOrgInfoMap.put(obj.get("orgName").toString(), list);
				}
			}
		}

		if(stationIds != null && stationIds.size() > 0){
    		map.put("stationIds", stationIds);
    	}
		
		Map<String,List<Map>> stationProfitOrgMap = new HashMap();
		Map<String,Map> stationProfitStationMap = new HashMap();
		List<Map> stationProfits = stationProfitMapper.queryStationProfitCache(map);

		if(stationProfits != null && stationProfits.size() > 0){
			for (Map map2 : stationProfits) {
				if(!stationProfitOrgMap.containsKey(map2.get("orgName"))){
					List<Map> list = new ArrayList();
					list.add(stationProfitStationMap);
					stationProfitOrgMap.put(map2.get("orgName").toString(), list);
				}
				stationProfitStationMap.put(map2.get("stationName").toString(), map2);
			}
		}

		for (Object[] obj : arrayList) {
			Map<String, String> profits = null;
			try {
				List stationOrgInfoList = stationOrgInfoMap.get(objToStr(obj[0]));
				Map omap = stationInfoMap.get(objToStr(obj[1]));
				profits = transObjToMap(obj,omap, stationIds,stationOrgInfoList);

				profits.put("settlementMonth", objToStr(map.get("settlementMonth")));
			} catch (NullPointerException e) {
				throw new BizException(1400001, objToStr(obj[0]));
			}
			if (!"校验正确".equals(profits.get("description"))) {
				canSave = "false";
			}
			if (mapNotExist(allProfits, profits)){
				allProfits.add(profits);
			}
		}
		
		if (canSave.equals("false")) {
			updateFail(allProfits, stationProfitOrgMap);
		} else {
			try {
				saveImportData(allProfits,stationProfitOrgMap,stationInfoMap);

				for (Map<String, String> c : allProfits) {
					Map stationMap = stationProfitStationMap.get(c.get("stationName").toString());
					if (stationMap != null) {
						c.put("impStatus", "更新成功");
					} else {
						c.put("impStatus", "保存成功");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				updateFail(allProfits, stationProfitOrgMap);
			}
		}
		vo.setData(allProfits);

		map = null;
		excelFile = null;
		params = null;
		arrayList = null;
		stationIds = null;
		stationAll = null;
		stationInfoMap = null;
		stationOrgInfoMap = null;
		stationProfitOrgMap = null;
		stationProfitStationMap = null;
		stationProfits = null;

		return vo;
	}
	
	/**
	 * 保存
	 * @param allProfits
	 * @throws Exception
	 */
	private void saveImportData(List<Map<String, String>> allProfits,Map<String,List<Map>> stationProfitOrgMap,
			Map<String,Map> stationInfoMap) throws Exception {
		
		List updateList = new ArrayList();
		List saveList = new ArrayList();
		
		if(allProfits != null && allProfits.size() > 0){
			for (Map map : allProfits) {
				String orgName = objToStr(map.get("orgName"));
				String stationName = objToStr(map.get("stationName"));
				Integer orgId = null;
				Integer stationId = null;
				Map stationMap = stationInfoMap.get(stationName);
				
				if(stationMap != null && stationMap.size() > 0){
					if(stationMap.get("stationId") != null){
						stationId = Integer.parseInt(stationMap.get("stationId").toString());
					}
					if(stationMap.get("orgId") != null){
						orgId = Integer.parseInt(stationMap.get("orgId").toString());
					}
				}
				map.put("orgId", orgId);
				map.put("stationId", stationId);
				List<Map> list = stationProfitOrgMap.get(orgName);
				if(list != null && list.size() > 0){
					boolean flag = false;
					Map profitMap = null;
					for (Map map2 : list) {
						profitMap = (Map) map2.get(stationName);
						if(profitMap != null && profitMap.size() > 0){
							flag = true;
							profitMap.putAll(map);
							break;
						}
					}
					if(flag){
						updateList.add(profitMap);
					}else{
						saveList.add(map);
					}
				}else{
					saveList.add(map);
				}
			}
		}
		
		if(saveList.size() > 0){
			stationProfitMapper.batchInsert(saveList);
		}
		if(updateList.size() > 0){
			stationProfitMapper.batchUpdate(updateList);
		}
	}
	
	private Map<String, String> transObjToMap(Object[] obj,Map omap,
			Set<Integer> stationIds,List stationOrgInfo) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String desc = obj[9].toString();
		
		if(stationIds != null && !stationIds.contains(omap.get("stationId"))){
			desc = "校验失败:没有该充电场站的权限";
		}
		if(stationOrgInfo == null || stationOrgInfo.size() <= 0 || omap == null){
			desc = "校验失败:运营商或充电场站不存在,请核对";
		}
		boolean falg = false;
		if (stationOrgInfo != null && stationOrgInfo.size() > 0){
			for (int i = 0; i < stationOrgInfo.size(); i++) {
				Map map1 = (Map) stationOrgInfo.get(i);
				if (map1.containsValue(objToStr(obj[1]))){
					falg = true;
					break;
				}
			}
		}
		if(!falg){
			desc = "校验失败:运营商或充电场站不存在,请核对";
		}

		map.put("orgName", objToStr(obj[0]));
		map.put("stationName", objToStr(obj[1]));
		map.put("stationTotalPower", getReplaceVal(obj[2]));//场站总用电量
		map.put("otherIncome", getReplaceVal(obj[3]));//非固定收入其他
		map.put("eleFee", getReplaceVal(obj[4]));//电费
		map.put("waterFee", getReplaceVal(obj[5]));//水费
		map.put("repairFee", getReplaceVal(obj[6]));//维修费
		map.put("lowConsumable", getReplaceVal(obj[7]));//低值易耗品
		map.put("otherExpenses", getReplaceVal(obj[8]));//成本其他

		for (int i = 2; i < 9; i++) {
			if(objToNum(obj[i]).contains(",")){
				desc = "校验失败:导入数据格式需为文本格式";
			}
		}

		if(!"校验正确".equals(desc)){
			desc = desc.replace("校验正确", "校验失败：");
		}
		map.put("description", desc);

		return map;
	}

	private String getReplaceVal(Object object) {
		return objToNum(object).replace(",","");
	}

	public List<Object[]> parseExcel(MultipartFile fileName) throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Workbook wb = null;
		Sheet sheet = null;
		try {
			wb = new HSSFWorkbook(fileName.getInputStream());
			// System.out.println("xls格式");
		} catch (Exception e) {
			wb = new XSSFWorkbook(fileName.getInputStream());
			// System.out.println("xlsx格式");
		}
		sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		if (rows == 1) {
			throw new Exception("excel文档无数据，请核对！");
		}
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				// 获取到Excel文件中的所有的列
				int cells = 10;
				if (i == 0) {// 首行表头过滤
					continue;
				}
				Object[] arra = new Object[10];
				String checkStr = "校验正确";
				for (int j = 0; j < cells; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							arra[j] = cell.getCellFormula();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							NumberFormat ddf1=NumberFormat.getNumberInstance() ; 
							ddf1.setMaximumFractionDigits(2); 
							arra[j] = ddf1.format(cell.getNumericCellValue()) ; 
							break;
						case Cell.CELL_TYPE_STRING:
							arra[j] = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_BLANK:
							break;
						default:
							break;
						}
					}
					if (j < 9) {

						if (arra[j] == null || "".equals(arra[j])) {						
							if(j==0){
								checkStr = "运营商不能为空";
							}
							
							if(j==1){
								checkStr = "场站不能为空";
							}
						}
					}
				}
				arra[cells - 1] = checkStr;
				result.add(arra);
				row = null;
			}
		}
		wb = null;
		sheet = null;

		return result;
	}
	
	/**
	 * 去掉前后空格
	 * @param object
	 * @return
	 */
	private static String objToStr(Object object){
		if(null == object || "".equals(object))
			return "";
		return (object+"").trim();
	}
	private static String objToNum(Object object){
		if(null == object || "".equals(object))
			return "0";
		return (object+"").trim();
	}
	
	/**
	 * Excel中是否已经存在同名或者同编号的数据
	 * @param listMap
	 * @param map1
	 * @return
	 */
	private static Boolean mapNotExist(List<Map<String, String>> listMap,
			Map<String, String> map1) {
		String stationName = map1.get("stationName");
		String orgName = map1.get("orgName");
		for (Map<String, String> map : listMap) {
			if (map.get("stationName").equals(stationName) && map.get("orgName").equals(orgName)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 通用更新失败
	 * 
	 * @param allPiles
	 * @param pileMap1
	 */
	private void updateFail(List<Map<String, String>> allPiles,Map<String,List<Map>> pileMap1) {
		for (Map<String, String> c : allPiles) {
			String orgName = objToStr(c.get("orgName"));
			List<Map> list = pileMap1.get(orgName);
			c.put("impStatus", "保存失败");

			if (list != null && list.size() > 0) {
				for (Map map : list) {
					if(map.containsValue(c.get("stationName"))){
						c.put("impStatus", "更新失败");
					}
				}
			}
		}
	}
	
	
	public void export(Map map,HttpServletResponse response) throws Exception{
		DataVo params = new DataVo(map);
		getPermission(params);

		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(params);
		}
  
		//结果集
		params.put("sort","station_total_charge");
		params.put("order","desc");
		List<Map> list = stationProfitMapper.queryStationProfit(params);

		// 创建HSSFWorkbook对象(excel的文档对象)
		XSSFWorkbook wb = new XSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		XSSFSheet sheet = wb.createSheet("场站利润");

		XSSFCellStyle style = wb.createCellStyle(); // 样式对象

		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// 生成一个字体
		XSSFFont font = wb.createFont();
		font.setColor(HSSFColor.BLACK.index);// HSSFColor.VIOLET.index //字体颜色
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
		// 把字体应用到当前的样式
		style.setFont(font);
		sheet.setColumnWidth(0, 8500);
		if(null!=list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				Map map2 = list.get(i);
				int length = map2.get("stationName").toString().length();
				if(length <= 5){
					sheet.setColumnWidth(i+1, length * 1500);
				}else if(length >= 5 && length <= 12){
					sheet.setColumnWidth(i+1, length * 850);
				}else{
					sheet.setColumnWidth(i+1, length * 700);
				}
			} 
		}
		sheet.createFreezePane(0, 1, 0, 1);//冻结首行
		// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
		List valList = new ArrayList();
		String[] title = new String[2];
		putTitleAndColumn(valList, title);

		XSSFCellStyle titleStyle = wb.createCellStyle(); // 样式对象

		titleStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);


		XSSFCellStyle subStyle = wb.createCellStyle(); // 样式对象
		subStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
		subStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		Map stationMap=null;
		BigDecimal total = null;
		XSSFRow rowrow;
		Double value;
		String titleVal;
		XSSFCell cell=null;


		if(valList != null && valList.size() > 0){
			for (int j = 0; j < valList.size(); j++) {
				rowrow = sheet.createRow(j);
				rowrow.setHeight((short) 400);
				title = (String[]) valList.get(j);
				cell = rowrow.createCell(0);
				cell.setCellValue(title[0].toString());
				if(j==0){
					cell.setCellStyle(style);
				}else if(j == 1 || j == 11 || j == 24){
					cell.setCellStyle(titleStyle);
				}else if(j == 20 || j == 22 || j == 23 || j == 37 || j == 43 || j == 41 || j == 42){
					cell.setCellStyle(subStyle);
				}
				total = BigDecimal.ZERO;
				if(list != null && list.size() > 0){
					for (int i = 0; i < list.size() + 1; i++) {
						if (i < list.size()){
							stationMap = list.get(i);
						}
						if(j == 0){
							cell = rowrow.createCell(i+1);
							if(i == list.size()){
								cell.setCellValue("合计");
							}else{
								titleVal = stationMap.get(title[1]).toString();
								cell.setCellValue(titleVal);
							}
							cell.setCellStyle(style);
						}else{
							if(!"".equals(title[1])){
								cell = rowrow.createCell(i+1);
								value = Double.parseDouble(stationMap.get(title[1]).toString());

								if(i == list.size()){
									cell.setCellValue(total.toString());
								}else{
									total = total.add(new BigDecimal(value)).setScale(2, BigDecimal.ROUND_HALF_UP);
									cell.setCellValue(value);
								}
								if(j == 20 || j == 22 || j == 23 || j == 37 || j == 43 || j == 41 || j == 42){
									cell.setCellStyle(subStyle);
								}else{
//								cell.setCellStyle(valStyle);
								}
							}else{
								if(i == 0){
									//合并单元格
									if(j == 1){
										sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, list.size() + 1));
									}else if (j == 11){
										sheet.addMergedRegion(new CellRangeAddress(11, 11, 1, list.size() + 1));
									}else if (j == 24){
										sheet.addMergedRegion(new CellRangeAddress(24, 24, 1, list.size() + 1));
									}
									cell = rowrow.createCell(1);
									cell.setCellValue(""); // 跨单元格显示的数据
									cell.setCellStyle(titleStyle);
								}
							}
						}
					}
				}
			}
		}
		sheet.createFreezePane(1,1,1,1);
		// 输出Excel文件
		String as = "充电站经营数据报表.xlsx";
		String fileName = as;// = Java.NET.URLEncoder.encode(as, "UTF-8");
		/* 根据request的locale 得出可能的编码，中文操作系统通常是gb2312 */
		// fileName = new String(as.getBytes("GB2312"), "ISO_8859_1");
		fileName = new String(as.getBytes("GB2312"), "ISO_8859_1");
		as = fileName;
		OutputStream output = null;

		try {
			output = response.getOutputStream();
			response.reset();

			response.setHeader(
					"Content-disposition",
					"attachment; filename="
							+ URLDecoder.decode(URLEncoder.encode(as, "UTF-8"),
							"UTF-8"));
			response.setContentType("application/msexcel;charset=UTF-8");

			wb.write(output);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null){
				output.close();
			}
			map = null;
			params = null;
			list = null;
			wb = null;
			style = null;
			font = null;
			valList = null;
			title = null;
			titleStyle = null;
			subStyle = null;
			stationMap = null;
			total = null;
			rowrow = null;
			cell = null;
		}
	}

	private void getPermission(DataVo params) throws BizException {
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId")){
            throw new BizException(1000006);
        }
		// 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		if (!userService.isSuperMan(params.getInt("userId")) && !userService.isGlobalMan(params.getInt("userId"))){
        	if(userRoleDataById != null && userRoleDataById.size() > 0){
				params.put("stationIds", userRoleDataById);
        	}
        }
	}

	private void putTitleAndColumn(List valList, String[] title) {
		title[0]="场站名称";
		title[1]="stationName";
		valList.add(title);
		title = new String[2];
		title[0]="场站用电/充电(kW)";
		title[1]="";
		valList.add(title);
		title = new String[2];
		title[0]="场站总用电量";
		title[1]="stationTotalPower";
		valList.add(title);
		title = new String[2];
		title[0]="目标充电量";
		title[1]="stationTargetCharge";
		valList.add(title);
		title = new String[2];
		title[0]="充电总电量";
		title[1]="stationTotalCharge";
		valList.add(title);
		title = new String[2];
		title[0]="业务充电量";
		title[1]="stationGoalCharge";
		valList.add(title);
		title = new String[2];
		title[0]="测试电量";
		title[1]="stationTestCharge";
		valList.add(title);
		title = new String[2];
		title[0]="自用电量";
		title[1]="stationUseCharge";
		valList.add(title);
		title = new String[2];
		title[0]="办公用电";
		title[1]="stationOfficeCharge";
		valList.add(title);
		title = new String[2];
		title[0]="设备总损耗";
		title[1]="pileTotalLoss";
		valList.add(title);
		title = new String[2];
		title[0]="设备总用电量";
		title[1]="pileTotalPower";
		valList.add(title);
		title = new String[2];
		title[0]="收入（元）";
		title[1]="";
		valList.add(title);
		title = new String[2];
		title[0]="目标收入";
		title[1]="stationTargetIncome";
		valList.add(title);
		title = new String[2];
		title[0]="（非合约）订单收入";
		title[1]="orderIncome";
		valList.add(title);
		title = new String[2];
		title[0]="（合约）集团月结";
		title[1]="groupMonthly";
		valList.add(title);
		title = new String[2];
		title[0]="（分成合约）设备总收入";
		title[1]="inPileTotalIncome";
		valList.add(title);
		title = new String[2];
		title[0]="（分成合约）设备运维收入";
		title[1]="inPilePmIncome";
		valList.add(title);
		title = new String[2];
		title[0]="（固定收入合约）充电设施租赁费（保底）";
		title[1]="inFacRentalFee";
		valList.add(title);
		title = new String[2];
		title[0]="（固定收入合约）车位租赁费";
		title[1]="inParkRentalFee";
		valList.add(title);
		title = new String[2];
		title[0]="（固定收入合约）土地租赁费";
		title[1]="inLandRentalFee";
		valList.add(title);
		title = new String[2];
		title[0]="固定收入小计";
		title[1]="fixedIncomeSubtotal";
		valList.add(title);
		title = new String[2];
		title[0]="其他";
		title[1]="otherIncome";
		valList.add(title);
		title = new String[2];
		title[0]="非固定收入小计";
		title[1]="unfixedIncomeSubtotal";
		valList.add(title);
		title = new String[2];
		title[0]="场站收入合计";
		title[1]="stationIncomeSubtotal";
		valList.add(title);
		title = new String[2];
		title[0]="成本（元）";
		title[1]="";
		valList.add(title);
		title = new String[2];
		title[0]="电费";
		title[1]="eleFee";
		valList.add(title);
		title = new String[2];
		title[0]="水费";
		title[1]="waterFee";
		valList.add(title);
		title = new String[2];
		title[0]="网络费";
		title[1]="networkFee";
		valList.add(title);
		title = new String[2];
		title[0]="（分成合约）分成支出";
		title[1]="diviExpend";
		valList.add(title);
		title = new String[2];
		title[0]="（固定支出合约）土地租赁费";
		title[1]="exLandRentalFee";
		valList.add(title);
		title = new String[2];
		title[0]="（固定支出合约）土地租赁分成费";
		title[1]="exLandRentalExpend";
		valList.add(title);
		title = new String[2];
		title[0]="（固定支出合约）房屋租赁费";
		title[1]="exHouseRentalFee";
		valList.add(title);
		title = new String[2];
		title[0]="物业管理费";
		title[1]="propertyFee";
		valList.add(title);
		title = new String[2];
		title[0]="人工支出";
		title[1]="artificialFee";
		valList.add(title);
		title = new String[2];
		title[0]="固定资产折旧";
		title[1]="depreFixedAsset";
		valList.add(title);
		title = new String[2];
		title[0]="费用摊销";
		title[1]="costAmortization";
		valList.add(title);
		title = new String[2];
		title[0]="停车费";
		title[1]="parkingFee";
		valList.add(title);
		title = new String[2];
		title[0]="固定支出小计";
		title[1]="fixedExpendSubtotal";
		valList.add(title);
		title = new String[2];
		title[0]="维修费";
		title[1]="repairFee";
		valList.add(title);
		title = new String[2];
		title[0]="低值易耗品";
		title[1]="lowConsumable";
		valList.add(title);
		title = new String[2];
		title[0]="其他";
		title[1]="otherExpenses";
		valList.add(title);
		title = new String[2];
		title[0]="非固定支出小计";
		title[1]="unfixedExpendSubtotal";
		valList.add(title);
		title = new String[2];
		title[0]="成本合计";
		title[1]="costTotal";
		valList.add(title);
		title = new String[2];
		title[0]="场站毛利润";
		title[1]="stationGrossProfit";
		valList.add(title);
	}
}

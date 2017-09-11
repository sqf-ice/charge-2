package com.clouyun.charge.modules.document.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.OSSUploadFileUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.document.mapper.GunMapper;
import com.clouyun.charge.modules.document.mapper.PileMapper;
import com.clouyun.charge.modules.document.mapper.StationMapper;
import com.clouyun.charge.modules.system.service.AreaService;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.OrgService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 描述: StationService 版权: Copyright (c) 2017 公司: 科陆电子 作者: sim.y 版本: 1.0 创建日期:
 * 2017年4月18日
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StationService {

	// 第三方运营状态map
	private static SortedMap<Integer, String> stationStatusMap;
	// 第三方建设场所map
	private static SortedMap<Integer, String> constructionMap;

	private static SortedMap<Integer, String> supportorderMap;

	// 暂时这样用，互联互通表最终会合并平台场站表
	static {
		stationStatusMap = new TreeMap<Integer, String>();
		stationStatusMap.put(-1, "全部");
		stationStatusMap.put(0, "未知");
		stationStatusMap.put(1, "建设中");
		stationStatusMap.put(5, "关闭下线");
		stationStatusMap.put(6, "维护中");
		stationStatusMap.put(50, "正常使用");

		constructionMap = new TreeMap<Integer, String>();
		constructionMap.put(-1, "全部");
		constructionMap.put(1, "居民区");
		constructionMap.put(2, "公共机构");
		constructionMap.put(3, "企事业单位");
		constructionMap.put(4, "写字楼");
		constructionMap.put(5, "工业园区");
		constructionMap.put(6, "交通枢纽");
		constructionMap.put(7, "大型文体设施");
		constructionMap.put(8, "城市绿地");
		constructionMap.put(9, "大型建筑配建停车场");
		constructionMap.put(10, "路边停车位");
		constructionMap.put(11, "城际高速服务区");
		constructionMap.put(255, "其他");

		supportorderMap = new TreeMap<Integer, String>();
		supportorderMap.put(0, "不支持预约");
		supportorderMap.put(1, "支持预约");
	}

	public static final String DESCRIPTION = "description";
	public static final String IMPSTATUS = "impStatus";

	@Autowired
	StationMapper stationMapper;
	@Autowired
	PileMapper pileMapper;
	@Autowired
	GunMapper gunMapper;

	@Autowired
	DictService dictService;

	@Autowired
	UserService userService;

	@Autowired
	OrgService orgService;

	@Autowired
	AreaService areaService;

	/**
	 * 场站列表查询
	 * @throws BizException 
	 */
	public PageInfo getStationAll(Map map) throws BizException {
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
		// 查询场站信息
		List list = stationMapper.getStationAll(vo);
		if (list != null && list.size() > 0) {
			// 获取场站Id
			stationIds = new HashSet<Integer>();
			for (int i = 0; i < list.size(); i++) {
				Map maplist = (Map) list.get(i);
				stationIds.add(Integer.valueOf(maplist.get("stationId")
						.toString()));
			}
			vo = new DataVo();
			vo.add("stationIds", stationIds);
			// 通过场站id 查询出充电桩信息 获取充电桩数量
			List pileList = pileMapper.getPileCountByStationId(vo);
			// 通过场站id 查询出充电枪信息 获取充电枪数量
			List gunList = gunMapper.getGunCountByStationId(vo);
			// 将充电桩数量和充电枪数量添加到场站列表中
			for (int i = 0; i < list.size(); i++) {
				int pileTotal = 0;
				int gunTotal = 0;
				Map stationMap = (Map) list.get(i);
				Integer subId = Integer.valueOf(stationMap.get("stationId")
						.toString());
				for (int j = 0; j < pileList.size(); j++) {
					Map pileMap = (Map) pileList.get(j);
					Integer stationId = Integer.valueOf(pileMap
							.get("stationId").toString());
					if (subId.intValue() == stationId.intValue()) {
						pileTotal = Integer.valueOf(pileMap.get("count")
								.toString());
						break;
					}
				}
				stationMap.put("pileCount", pileTotal);
				for (int j = 0; j < gunList.size(); j++) {
					Map gunMap = (Map) gunList.get(j);
					Integer stationId = Integer.valueOf(gunMap.get("stationId")
							.toString());
					if (subId.intValue() == stationId.intValue()) {
						gunTotal = Integer.valueOf(gunMap.get("count")
								.toString());
						;
						break;
					}
				}
				stationMap.put("gunCount", gunTotal);

				// 场站到期时间
				Date stationenddate = (stationMap.get("stationenddate") != null && !""
						.equals(stationMap.get("stationenddate").toString())) ? DateUtils
						.parseDate(stationMap.get("stationenddate").toString())
						: null;
				Date rentalenddate = (stationMap.get("rentalenddate") != null && !""
						.equals(stationMap.get("rentalenddate").toString())) ? DateUtils
						.parseDate(stationMap.get("rentalenddate").toString())
						: null;
				stationMap.put("endTime","");
				if (stationenddate != null && rentalenddate != null) {
					if (stationenddate.getTime() > rentalenddate.getTime()) {
						stationMap.put("endTime",
								stationMap.get("rentalenddate").toString());
					} else {
						stationMap.put("endTime",
								stationMap.get("stationenddate").toString());
					}

				} else if (stationenddate != null) {
					stationMap.put("endTime", stationMap.get("stationenddate")
							.toString());
				} else if (rentalenddate != null) {
					stationMap.put("endTime", stationMap.get("rentalenddate")
							.toString());
				}
				String endTime = stationMap.get("endTime").toString();
				if(!"".equals(endTime)){
					Calendar endCalendar = CalendarUtils.convertStrToCalendar(endTime, CalendarUtils.yyyyMMdd);
					endCalendar.add(Calendar.MONTH, -1);
					if(Calendar.getInstance().getTimeInMillis() > endCalendar.getTimeInMillis()){
						stationMap.put("isDiss", 1);
					}else{
						stationMap.put("isDiss", 0);
					}
				}
				
				
				Integer parkNum = 0;
				if(stationMap.get("bigCarSize") != null && !"".equals(stationMap.get("bigCarSize"))){
					parkNum += Integer.valueOf(stationMap.get("bigCarSize").toString()); 
				}
				if(stationMap.get("smallCarSize") != null && !"".equals(stationMap.get("smallCarSize"))){
					parkNum += Integer.valueOf(stationMap.get("smallCarSize").toString()); 
				}
				stationMap.put("parkNums", parkNum);
			}

		}
		return new PageInfo(list);
	}

	/**
	 * 根据场站Id将场站置无效
	 * 
	 * @throws BizException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void dissStation(List stationIds) throws BizException {
		if (stationIds == null || stationIds.size() <= 0) {
			throw new BizException(1102001, "场站id");
		}
		DataVo vo = new DataVo();
		vo.add("stationIds", stationIds);
		// 判断场站是否存在有效桩
		List pileList = pileMapper.getUsePileCountByStationId(vo);
		for (int i = 0; i < pileList.size(); i++) {
			Map pileMap = (Map) pileList.get(i);
			String stationName = pileMap.get("stationName").toString();
			if (pileMap.get("count") == null
					|| Integer.valueOf(pileMap.get("count").toString())
							.intValue() > 0) {
				throw new BizException(1102002, stationName);
			}
		}
		// 场站置为无效
		stationMapper.dissStation(vo);
	}

	/**
	 * 根据场站Id获取场站信息
	 * 
	 * @throws BizException
	 */
	public Map getStationById(Integer id) throws BizException {
		if (id == null) {
			throw new BizException(1102001, "场站id");
		}
		DataVo vo = new DataVo();
		vo.add("stationId", id);
		DataVo returnMap = new DataVo(stationMapper.getStationById(vo));

		if(returnMap!=null&&!returnMap.isEmpty()){
			String selfNumber="";
			if(returnMap.isNotBlank("orgNo")){
				String stationNo = returnMap.getString("stationNo");
				if(stationNo!=null&&""!=stationNo){
					String orgNo =returnMap.getString("orgNo");
					if(stationNo.indexOf(orgNo)>=0) {
						selfNumber = stationNo.replace(orgNo,"");
					}
				}
				returnMap.put("selfNumber",selfNumber);

				dateTimeToDate(returnMap,"stationStartDate");
				dateTimeToDate(returnMap,"stationEndDate");
				dateTimeToDate(returnMap,"expiryTime");
				dateTimeToDate(returnMap,"payTime");
				dateTimeToDate(returnMap,"testTime");
				dateTimeToDate(returnMap,"formalTime");

				BigDecimal purPrice = new BigDecimal(returnMap.getString("purPrice"));
				if(returnMap.isNotBlank("purPrice") && purPrice.compareTo(BigDecimal.ZERO) != 0 ){
					returnMap.put("priceType",0);
				}
				if((returnMap.isBlank("purPrice") || purPrice.compareTo(BigDecimal.ZERO) == 0)&& (returnMap.isNotBlank("purPrice1") || returnMap.isNotBlank("purPrice2")
						|| returnMap.isNotBlank("purPrice3") || returnMap.isNotBlank("purPrice4"))){
					returnMap.put("priceType",1);
				}

				if(returnMap.isNotBlank("leasePic")){
					String leasePic = CommonUtils.PATH + returnMap.getString("leasePic");
					returnMap.put("leasePic",leasePic);
				}
			}


		 List<DataVo> pileTypes = stationMapper.getPileOrtModePower(id);
			Integer dcSize = 0; //直流数
			Integer  acSize = 0;//交流数
			Integer acDcSize = 0;//交直流数
			Double dcRatePower = 0.00; //直流功率
			Double  acRatePower = 0.00;//交流功率
			Double acDcRatePower = 0.00;//交直功率
			for(DataVo pile :pileTypes){
				switch (pile.getInt("ortMode")){
					case 1: //交流
						acSize = acSize+pile.getInt("size");
						acRatePower = acRatePower+pile.getDouble("rp");
                     break;
					case 2: //直流
						dcSize=dcSize+pile.getInt("size");
						dcRatePower =dcRatePower+pile.getDouble("rp");
						break;
					case 3: //交直一体
						acDcSize=acDcSize+pile.getInt("size");
						acDcRatePower =acDcRatePower+pile.getDouble("rp");
						break;
				}
			}
			returnMap.put("acSize",acSize);//交流数
			returnMap.put("dcSize",dcSize);//直流
			returnMap.put("acDcSize",acDcSize);//交直流
			returnMap.put("acRatePower",acRatePower);//交流功率
			returnMap.put("dcRatePower",dcRatePower);//直流功率
			returnMap.put("acDcRatePower",acDcRatePower);//交直流功率

			Integer gunSize =	stationMapper.getGunSize(id); //得到枪数
			returnMap.put("gunSize",gunSize);//枪数
			Map queryMap = new HashMap();
			queryMap.put("id",id);
			queryMap.put("table","equip_terminal");
			DataVo eteRatePowers =  stationMapper.getTotalRatePower(queryMap); //得到总功率

			queryMap.put("table", "equip_transformer");
			DataVo etrRatePowers =  stationMapper.getTotalRatePower(queryMap); //得到总功率

			queryMap.put("table", "it_device");
			DataVo itRatePowers =  stationMapper.getTotalRatePower(queryMap); //得到总功率

			queryMap.put("table", "chg_meter_management");
			DataVo cmmRatePowers =  stationMapper.getTotalRatePower(queryMap); //得到总功率
			Double totalRatePower = 0.00;
			if(eteRatePowers != null) {
				totalRatePower += eteRatePowers.getDouble("rp");
			}
			if(etrRatePowers != null) {
				totalRatePower += etrRatePowers.getDouble("rp");
			}
			if(itRatePowers != null) {
				totalRatePower += itRatePowers.getDouble("rp");
			}
			if(cmmRatePowers != null) {
				totalRatePower += cmmRatePowers.getDouble("rp");
			}
			totalRatePower = acRatePower + dcRatePower + acDcRatePower + totalRatePower;
			returnMap.put("totalRatePower",totalRatePower);//得到总功率
		}
		return returnMap;
	}

	private void dateTimeToDate(DataVo returnMap,String dateTime){
		String time = returnMap.getString(dateTime);
		String date = CalendarUtils.formatCalendar(CalendarUtils.getCalendar(time), CalendarUtils.yyyyMMdd);
		returnMap.put(dateTime,date);
	}

	/**
	 * 按条件查询第三方场站列表数据
	 */
	public PageInfo getToSubsAll(Map map) throws BizException {
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		List<String> operatorIds = getOperatorIds(vo.getInt("userId"));
		if(operatorIds!=null && operatorIds.size()>0){
			vo.put("operatorIds", operatorIds);
		}
		
		
		
		// 设置分页
		if (vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")) {
			PageHelper.startPage(vo);
		}
		List list = stationMapper.getToSubsAll(vo);
		for (int i = 0; i < list.size(); i++) {
			Map subsMap = (Map) list.get(i);
			if (subsMap.get("stationstatus") != null
					&& !"".equals(subsMap.get("stationstatus").toString())) {
				subsMap.put(
						"stationstatus",
						stationStatusMap.get(Integer.valueOf(subsMap.get(
								"stationstatus").toString())));
			} else {
				subsMap.put("stationstatus", "");
			}
			if (subsMap.get("construction") != null
					&& !"".equals(subsMap.get("construction").toString())) {
				subsMap.put(
						"construction",
						constructionMap.get(Integer.valueOf(subsMap.get(
								"construction").toString())));
			} else {
				subsMap.put("construction", "");
			}
			if (subsMap.get("supportorder") != null
					&& !"".equals(subsMap.get("supportorder").toString())) {
				subsMap.put(
						"supportorder",
						supportorderMap.get(Integer.valueOf(subsMap.get(
								"supportorder").toString())));
			} else {
				subsMap.put("supportorder", "");
			}
		}
		return new PageInfo(list);
	}

	/**
	 * 获取第三方机构代码集合 
	 * @throws BizException 
	 */
	public List<String> getOperatorIds(Integer userId) throws BizException{
		List<String> set= new ArrayList<String>();
		List<String> sets =new ArrayList<String>();
		if(!userService.isSuperMan(userId) && !userService.isGlobalMan(userId) && !userService.isOrgMan(userId)){
			sets.add("-1");
			return sets;
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(userId, RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			//查询运营商表 返回orgCode集合
			List<DataVo> orgCodeVo = orgService.getOrgCodeByIds(orgIds);
			if(orgCodeVo!=null && orgCodeVo.size()>0){
				for (int i = 0; i < orgCodeVo.size(); i++) {
					DataVo codeVo = orgCodeVo.get(i);
					if(codeVo.isNotBlank("orgCode")){
						set.add(codeVo.getString("orgCode"));
					}
				}
			}
			//
			if(set.size() == 0){
				sets.add("-1");
				return sets;
			}
			//查询ToOperatorRela 返回operatorId集合
			List<DataVo> operatorIds = stationMapper.getOperatorIdByOperatorRelaId(set);
			if(operatorIds != null && operatorIds.size() >0){
				for (int i = 0; i < operatorIds.size(); i++) {
					if(operatorIds.get(i).isNotBlank("operatorId")){	
						sets.add(operatorIds.get(i).getString("operatorId"));
					}
				}
			}else{
				sets.add("-1");
			}
		}
		return sets;
	}
	
	
	/**
	 * 查询场站业务字典 {id,text}
	 * @throws BizException 
	 */
	public List<ComboxVo> getStationDict(Map map) throws Exception {
		PageInfo pageList = getStation(map);
		List<ComboxVo> cvList = new ArrayList<ComboxVo>();
		for (int i = 0; i < pageList.getList().size(); i++) {
			Map cvMap = (Map) pageList.getList().get(i);
			cvList.add(new ComboxVo(cvMap.get("id").toString(), cvMap.get(
					"name").toString()));
		}
		return cvList;
	}
	/**
	 * 查询场站业务字典 {id,name,orgId,stationNo}
	 * @throws BizException 
	 */
	public PageInfo getStation(Map map) throws Exception{
		DataVo vo = new DataVo(map);
		if (vo.isBlank("pageNum")) {
			vo.put("pageNum", 1);
		}
		if (vo.isBlank("pageSize")) {
			vo.put("pageSize", 20);
		}
		if (vo.isNotBlank("userId")) {
			Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
			if(orgIds !=null){
				vo.put("orgIds", orgIds);
			}
			Set<Integer> stationIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.STATION.dataType);
			if(stationIds !=null){
				vo.put("stationIds", stationIds);
			}
		}
		PageHelper.startPage(vo);
		List list = stationMapper.getStation(vo);
		PageInfo pageList = new PageInfo(list);
		return pageList;
	}
	
	/**
	 * 新增场站信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveStation(DataVo dataMap,MultipartFile file) throws BizException {

		if(dataMap.isBlank("selfNumber")){
			throw new BizException(1102001,"场站自编号");
		}

		if(dataMap.isNotBlank("selfNumber") && dataMap.getString("selfNumber").length() > 3 ){
			throw new BizException(1102022);
		}
		if(dataMap.isNotBlank("selfNumber") && !ValidateUtils.Number(dataMap.getString("selfNumber")) ){
			throw new BizException(1102006,"场站自编号");
		}
		chek(dataMap);
		if (file != null && !file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String imageFileName = "images/" + new Date().getTime() + fileName.substring(fileName.lastIndexOf("."));
			OSSUploadFileUtils.toUploadFile(file, imageFileName);
			dataMap.put("leasePic", imageFileName);
		}

		if(dataMap.isNotBlank("stationNo") && dataMap.isNotBlank("selfNumber")){
			String stationNo = dataMap.getString("stationNo");
			String selftNum = dataMap.getString("selfNumber");
			if (selftNum.length() < 3) {
				StringBuffer sb = new StringBuffer("");
				int zeroSize = 3 - selftNum.length();
				for (int j = 0; j < zeroSize; j++) {
					sb.append("0");
				}
				sb.append(selftNum);
				selftNum = sb.toString();
			}
			stationNo += selftNum;
			dataMap.put("stationNo",stationNo);
		}

		DataVo valVo = new DataVo();
		valVo.put("orgId",dataMap.getInt("orgId"));
		if (dataMap.isNotBlank("stationId")){
			valVo.put("stationId",dataMap.getInt("stationId"));
		}
		valVo.put("column","station_no");
		valVo.put("arr",dataMap.getString("stationNo"));
		if(stationMapper.validateArr(valVo) > 0){
			throw new BizException(1102007,"场站编号");
		}


		Map map = convertVal(dataMap);
		if(map.get("useStatus") == null || "".equals(map.get("useStatus").toString())){
			map.put("useStatus",0);//默认有效
		}



		stationMapper.saveStation(map);
		stationMapper.saveBusiness(map);
	}

	private Map convertVal(DataVo dataMap) {
		List<String> list = new ArrayList<>();
		list.add("stationEtwork");
		list.add("businessPeople");
		list.add("expiryTime");
		list.add("payTime");
		list.add("testTime");
		list.add("formalTime");
		list.add("bigCarSize");
		list.add("smallCarSize");
		list.add("manageType");
		list.add("employeeSize");
		list.add("serviceCars");
		list.add("stationArea");
		list.add("parkNums");
		list.add("stationLng");
		list.add("stationLat");
		list.add("minFee");
		list.add("rentalStartDate");
		list.add("rentalEndDate");
		list.add("stationStartDate");
		list.add("stationEndDate");
		list.add("purPrice");
		list.add("purPrice1");
		list.add("purPrice2");
		list.add("purPrice3");
		list.add("purPrice4");
		list.add("investAmount");
		list.add("targetCharge");
		list.add("targetIncome");
		list.add("fastCharge");
		list.add("networkFee");
		list.add("parkingFee");
		list.add("artificialFee");
		list.add("propertyFee");
		list.add("costAmortization");
		list.add("serviceType");
		list.add("stationCoopType");
		list.add("passDate");
		return CommonUtils.convertDefaultVal(dataMap,list);
	}

	private void chek(DataVo vo) throws BizException {
		//是否是整型
		String[] list = {"orgId","construction","stationType",
				         "manageType","manageType","serviceType","stationStatus",
				        "useStatus","employeeSize","provCode","cityCode","distCode",
				          "stationEtwork","etworkSize","stationModel",
				          "investAmount","targetIncome",
				          "targetCharge","serviceCars","bigCarSize","smallCarSize",
				          "initialEnergy","networkFee","parkingFee","propertyFee",
						"artificialFee","stationCoopType","stationEtwork"};
		Map<String,String> map  = new HashMap();
		map.put("orgId","运营商");
		map.put("construction","建设场所");
		map.put("stationType","场站类型");
		map.put("manageType","管理类型");
		map.put("serviceType","服务类型");
		map.put("stationStatus","场站状态");
		map.put("useStatus","使用状态");
		map.put("employeeSize","员工数量");
		map.put("provCode","省");
		map.put("cityCode","市");
		map.put("distCode","县");
		map.put("stationEtwork","场站网络");
		map.put("etworkSize","数量");
		map.put("stationModel","运营模式");
		map.put("investAmount","投资金额");
		map.put("targetIncome","目标收入");
		map.put("targetCharge","目标充电量");
		map.put("serviceCars","服务车型");
		map.put("bigCarSize","大车车位数");
		map.put("smallCarSize","小车车车位数");
		map.put("initialEnergy","初始电量");
		map.put("purPrice","购电单价");
		map.put("purPrice1","尖期价");
		map.put("purPrice2","峰期价");
		map.put("purPrice3","平期价");
		map.put("purPrice4","谷期价");
		map.put("networkFee","网络费");
		map.put("parkingFee","停车费");
		map.put("propertyFee","物业管理费");
		map.put("artificialFee","人工支出");
		map.put("stationCoopType","合作方式");
		map.put("stationEtwork","场站网络");
		//类型
		for(String str : list){
			if(vo.isNotBlank(str)){
				if(vo.isNotBlank("priceType") && "0".equals(vo.getString("priceType"))){
					if(!"purPrice1".equals(str) && !"purPrice2".equals(str) && !"purPrice3".equals(str) && !"purPrice4".equals(str)){
						ChargeManageUtil.isNumeric(vo.getString(str),map.get(str));
					}
				}else if(vo.isNotBlank("priceType") && "1".equals(vo.getString("priceType"))){
					if(!"purPrice".equals(str)){
						ChargeManageUtil.isNumeric(vo.getString(str),map.get(str));
					}
				}else{
					ChargeManageUtil.isNumeric(vo.getString(str),map.get(str));
				}
			}
		}

		if (vo.isNotBlank("priceType")){
			if("0".equals(vo.getString("priceType")) && vo.isBlank("purPrice")){
				throw new BizException(1102006,"购电单价");
			}else if ("0".equals(vo.getString("priceType")) && vo.isNotBlank("purPrice")){
				vo.put("purPrice1",0);
				vo.put("purPrice2",0);
				vo.put("purPrice3",0);
				vo.put("purPrice4",0);
			}
			if ("1".equals(vo.getString("priceType")) && vo.isBlank("purPrice1") &&
					vo.isBlank("purPrice2") && vo.isBlank("purPrice3") && vo.isBlank("purPrice4")){
				throw new BizException(1102006,"费率电价");
			}else if("1".equals(vo.getString("priceType")) && (vo.isNotBlank("purPrice1") ||
					vo.isNotBlank("purPrice2") || vo.isNotBlank("purPrice3") || vo.isNotBlank("purPrice4"))){
				vo.put("purPrice",0);
			}
		}else{
			throw new BizException(1102006,"电价");
		}


		// 电话
//		if (vo.isNotBlank("stationTel")
//				&& !ValidateUtils.Tel(vo.getString("stationTel"))
//				&& !ValidateUtils.Mobile(vo.getString("stationTel"))) {
//			throw new BizException(1102006, "场站电话");
//		}
		if (vo.isNotBlank("repairPhone")
				&& !ValidateUtils.Tel(vo.getString("repairPhone"))
				&& !ValidateUtils.Mobile(vo.getString("repairPhone"))) {
			throw new BizException(1102006, "维修电话");
		}
		if (vo.isNotBlank("businessPhone")
				&& !ValidateUtils.Tel(vo.getString("businessPhone"))
				&& !ValidateUtils.Mobile(vo.getString("businessPhone"))) {
			throw new BizException(1102006, "运营电话");
		}

		DataVo valVo = new DataVo();
		valVo.put("column","station_name");
		valVo.put("arr",vo.getString("stationName"));
		valVo.put("orgId",vo.getInt("orgId"));
		if (vo.isNotBlank("stationId")){
			valVo.put("stationId",vo.getInt("stationId"));
		}
		if(stationMapper.validateArr(valVo) > 0){
			throw new BizException(1102007,"场站名称");
		}

		if(vo.isBlank("investAmount")){
			vo.put("investAmount", 0);
		}
		if(vo.isBlank("budgetAmount")){
			vo.put("budgetAmount", 0);
		}

       //时间格式
     String[] dataList  = {"expiryTime","payTime","testTime","formalTime","rentalStartDate","rentalEndDate","stationStartDate","stationEndDate"};
		for(String label :dataList){
			if(vo.isNotBlank(label)) {
				ChargeManageUtil.isDate(vo.getString(label), label);
			}
		}
	}

	/**
	 * 场站编辑
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateStation(DataVo dataMap,MultipartFile file) throws BizException {
		chek(dataMap);
		String infoPic = dataMap.getString("leasePic");

		if (infoPic.contains(CommonUtils.PATH)){
			String substring = infoPic.substring(CommonUtils.PATH.length(),infoPic.length());
			dataMap.put("leasePic",substring);
			infoPic = infoPic.substring(0, CommonUtils.PATH.length());
		}

		if (file != null && !file.isEmpty()) {
			String imageFileName;
			// 旧图片地址
			infoPic = dataMap.getString("leasePic");

			// 如果不存在则新命名，否则直接覆盖旧文件
			if (StringUtils.isBlank(infoPic)) {
				String fileName = file.getOriginalFilename();
				imageFileName = "images/" + CommonUtils.randomNum() + fileName.substring(fileName.lastIndexOf("."));
				dataMap.put("leasePic", imageFileName);
			} else {
				imageFileName = infoPic;
				// 移除该字段，不再重复更新
				dataMap.remove("leasePic");
			}
			OSSUploadFileUtils.toUploadFile(file, imageFileName);
		}

		if("1".equals(dataMap.getString("priceType"))){
			dataMap.put("purPrice","0");
		}else if("0".equals(dataMap.getString("priceType"))){
			dataMap.put("purPrice1","0");
			dataMap.put("purPrice2","0");
			dataMap.put("purPrice3","0");
			dataMap.put("purPrice4","0");

		}
		if(dataMap.isBlank("businessPeople")){
			dataMap.put("businessPeople", 0);
		}
		if(dataMap.isBlank("bigCarSize")){
			dataMap.put("bigCarSize", 0);
		}
		if(dataMap.isBlank("smallCarSize")){
			dataMap.put("smallCarSize", 0);
		}
		if(dataMap.isBlank("manageType")){
			dataMap.put("manageType", 0);
		}
		if(dataMap.isBlank("employeeSize")){
			dataMap.put("employeeSize", 0);
		}
		if(dataMap.isBlank("serviceCars")){
			dataMap.put("serviceCars", 0);
		}

		Map map = convertVal(dataMap);
		if(map.get("useStatus") == null || "".equals(map.get("useStatus").toString())){
			map.put("useStatus",0);//默认有效
		}
		stationMapper.updateStation(map);

		if(stationMapper.businessCount(dataMap.getInt("stationId")) > 0){
			stationMapper.updateBusiness(map);
		}else{
			stationMapper.saveBusiness(map);
		}
	}

	/**
	 * 数据验证
	 */
	private void getDataValidate(DataVo vo, String type) throws BizException {
		// 数据验证
		if ("save".equals(type)) {
			if (vo.isBlank("orgNo")) {
				throw new BizException(1102001, "运营商编号");
			} else if (vo.isBlank("stationNo")) {
				throw new BizException(1102001, "场站编号");
			} else if (vo.isBlank("orgId")) {
				throw new BizException(1102001, "运营商Id");
			}
		} else {
			if (vo.isBlank("stationId")) {
				throw new BizException(1102001, "场站Id");
			}
		}
		if (vo.isBlank("parkNums")) {
			vo.put("parkNums", 0);
		}
		if (vo.isBlank("stationName")) {
			throw new BizException(1102001, "场站名称");
		} else if (vo.isBlank("stationStatus")) {
			throw new BizException(1102001, "场站状态(运营状态)");
		} else if (vo.isBlank("stationType")) {
			throw new BizException(1102001, "场站类型");
		} else if (vo.isBlank("useStatus")) {
			throw new BizException(1102001, "使用状态");
		} else if (vo.isBlank("purPrice")) {
			throw new BizException(1102001, "购电单价");
		} else if (vo.isBlank("provCode")) {
			throw new BizException(1102001, "省");
		} else if (vo.isBlank("cityCode")) {
			throw new BizException(1102001, "市");
		} else if (vo.isBlank("distCode")) {
			throw new BizException(1102001, "县");
		} else if (vo.isBlank("stationLng")) {
			throw new BizException(1102001, "经度");
		} else if (vo.isBlank("stationLat")) {
			throw new BizException(1102001, "纬度");
		} else if (vo.isBlank("supportOrder")) {
			throw new BizException(1102001, "是否支持预约");
		}

		if (vo.isBlank("stationArea")) {
			vo.put("stationArea", 0);
		}
		if (vo.isNotBlank("stationTel")
				&& !ValidateUtils.Tel(vo.getString("stationTel"))
				&& !ValidateUtils.Mobile(vo.getString("stationTel"))) {
			throw new BizException(1102006, "联系电话");
		}
		if (vo.isNotBlank("serviceTel")
				&& !ValidateUtils.Tel(vo.getString("serviceTel"))
				&& !ValidateUtils.Mobile(vo.getString("serviceTel"))) {
			throw new BizException(1102006, "服务电话");
		}
		if (vo.isNotBlank("rentalStartDate")
				&& !ValidateUtils.Date(vo.getString("rentalStartDate"))) {
			throw new BizException(1102006, "场站开始租赁有效期");
		}
		if (vo.isNotBlank("rentalEndDate")
				&& !ValidateUtils.Date(vo.getString("rentalEndDate"))) {
			throw new BizException(1102006, "场站结束租赁有效期");
		}
		if (vo.isNotBlank("stationStartDate")
				&& !ValidateUtils.Date(vo.getString("stationStartDate"))) {
			throw new BizException(1102006, "场站开始有效期");
		}
		if (vo.isNotBlank("stationEndDate")
				&& !ValidateUtils.Date(vo.getString("stationEndDate"))) {
			throw new BizException(1102006, "场站结束有效期");
		}
	}

	/**
	 * 场站列表导出
	 * 
	 * @throws Exception
	 */
	public void exportStation(Map map, HttpServletResponse response)
			throws Exception {
		List stationList = getStationAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		headList.add("运营商");
		headList.add("场站编号");
		headList.add("场站名称");
		headList.add("运营模式");
		headList.add("场站类型");
		headList.add("运营状态");
		headList.add("使用状态");
		headList.add("建设场所");
		headList.add("充电桩数量");
		headList.add("充电枪数量");
		headList.add("车位数量");
		headList.add("所在区域");
		headList.add("详细地址");
		headList.add("场地面积");
		headList.add("经度");
		headList.add("纬度");
		headList.add("车位描述");
		headList.add("支付方式");
		headList.add("营业时间");
		headList.add("联系人");
		headList.add("联系电话");
		headList.add("服务电话");
		headList.add("到期时间");
		headList.add("备注");

		titleList.add("orgName");
		titleList.add("stationNo");
		titleList.add("stationName");
		titleList.add("stationModel");
		titleList.add("stationType");
		titleList.add("stationStatus");
		titleList.add("useStatus");
		titleList.add("construction");
		titleList.add("pileCount");
		titleList.add("gunCount");
		titleList.add("parkNums");
		titleList.add("areaStr");
		titleList.add("address");
		titleList.add("stationArea");
		titleList.add("stationLng");
		titleList.add("stationLat");
		titleList.add("parkInfo");
		titleList.add("payMent");
		titleList.add("busineHours");
		titleList.add("stationContact");
		titleList.add("stationTel");
		titleList.add("serviceTel");
		titleList.add("endTime");
		titleList.add("remark");
		for (int i = 0; i < stationList.size(); i++) {
			Map stationMap = (Map) stationList.get(i);
			// 获取字典数据 建设场所=jscs ,场站类型=jingylx,使用状态=zt ,是否预约=sf
			// 运营模式
			if (stationMap.get("stationModel") != null
					&& !"".equals(stationMap.get("stationModel").toString())) {
				stationMap.put(
						"stationModel",
						dictService.getDictLabel("yyms",
								stationMap.get("stationModel").toString()));
			} else {
				stationMap.put("stationModel", "");
			}
			// 场站类型
			if (stationMap.get("stationType") != null
					&& !"".equals(stationMap.get("stationType").toString())) {
				stationMap.put(
						"stationType",
						dictService.getDictLabel("jingylx",
								stationMap.get("stationType").toString()));
			} else {
				stationMap.put("stationType", "");
			}
			// 运营状态
			if (stationMap.get("stationStatus") != null
					&& !"".equals(stationMap.get("stationStatus").toString())) {
				stationMap.put(
						"stationStatus",
						dictService.getDictLabel("yyzt",
								stationMap.get("stationStatus").toString()));
			} else {
				stationMap.put("stationStatus", "");
			}
			// 使用状态
			if (stationMap.get("useStatus") != null
					&& !"".equals(stationMap.get("useStatus").toString())) {
				stationMap.put(
						"useStatus",
						dictService.getDictLabel("zt",
								stationMap.get("useStatus").toString()));
			} else {
				stationMap.put("useStatus", "");
			}
			// 建设场所
			if (stationMap.get("construction") != null
					&& !"".equals(stationMap.get("construction").toString())) {
				stationMap.put(
						"construction",
						dictService.getDictLabel("jscs",
								stationMap.get("construction").toString()));
			} else {
				stationMap.put("construction", "");
			}
			//所在区域
			Object provCodeObj = stationMap.get("provCode");
			if(provCodeObj != null && !"".equals(provCodeObj.toString())){
				stationMap.put("areaStr", areaService.getAreaNameByNo(provCodeObj.toString()));
			}else{
				stationMap.put("areaStr","");
			}
		}

		ExportUtils.exportExcel(stationList, response, headList, titleList,
				"场站信息");
	}

	/**
	 * 第三方场站列表导出
	 */
	public void exportToStation(Map map, HttpServletResponse response)
			throws Exception {
		List subsList = getToSubsAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		headList.add("运营商");
		headList.add("场站编号");
		headList.add("场站名称");
		headList.add("场站类型");
		headList.add("运营状态");
		headList.add("建设场所");
		headList.add("车位数量");
		headList.add("经度");
		headList.add("纬度");
		headList.add("所在区域");
		headList.add("详细地址");
		headList.add("服务电话");
		headList.add("使用车型描述");
		headList.add("车位描述");
		headList.add("是否支持预约");
		headList.add("支付方式");
		headList.add("营业时间描述");
		headList.add("联系电话");
		headList.add("站点引导");
		headList.add("停车费描述");
		headList.add("充电电费率");
		headList.add("服务费率");
		headList.add("备注");

		titleList.add("operatorname");
		titleList.add("stationid");
		titleList.add("stationname");
		titleList.add("stationtype");
		titleList.add("stationstatus");
		titleList.add("construction");
		titleList.add("parknums");
		titleList.add("stationlng");
		titleList.add("stationlat");
		titleList.add("areacode");
		titleList.add("address");
		titleList.add("servicetel");
		titleList.add("matchcars");
		titleList.add("parkinfo");
		titleList.add("supportorder");
		titleList.add("payment");
		titleList.add("businehours");
		titleList.add("stationtel");
		titleList.add("siteguide");
		titleList.add("parkfee");
		titleList.add("electricityfee");
		titleList.add("servicefee");
		titleList.add("remark");
		for (int i = 0; i < subsList.size(); i++) {
			Map subsMap = (Map) subsList.get(i);
			// 场站类型
			if (subsMap.get("stationtype") != null
					&& !"".equals(subsMap.get("stationtype").toString())) {
				subsMap.put(
						"stationtype",
						dictService.getDictLabel("jingylx",
								subsMap.get("stationtype").toString()));
			} else {
				subsMap.put("stationtype", "");
			}
		}
		ExportUtils.exportExcel(subsList, response, headList, titleList,
				"第三方场站信息");
	}

	/**
	 * 导入场站信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public List importStation(MultipartFile excelFile, Map map)
			throws Exception {
		DataVo vo = new DataVo(map);
		if (vo.isBlank("userId")) {
			throw new BizException(1102001, "用户Id");
		}
		Integer userId = vo.getInt("userId");
		// 获取有效企业
		Map<String, Map> omap = getOrgId(vo);
		String canSave = "true";
		// 解析excel
		List<Map<String, String>> allStation = new ArrayList<Map<String, String>>();
		List<Object[]> arrayList = parseExcel(excelFile);
		List<String> names = new ArrayList<String>();
		List<String> nos = new ArrayList<String>();
		Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType);
		List<ComboxVo> orgComboxs = orgService.getOrgNameByUserId(userId);
		for (Object[] obj : arrayList) {
			Map<String, String> station = null;
			try {
				station = transObjToMap(obj, omap, orgIds);
			} catch (NullPointerException e) {
				throw new BizException(1102015, objToStr(obj[0]));
			}
			if (null != obj[2] && !names.contains(obj[2].toString()))
				names.add(obj[2].toString());
			String stationNo = station.get("stationNo");
			if (null != stationNo && !nos.contains(stationNo)) {
				nos.add(stationNo);
			}
			if (!"校验正确".equals(station.get(DESCRIPTION))) {
				canSave = "false";
			}
			if (mapNotExist(allStation, station))
				allStation.add(station);
		}

		Map<String, Map> stationMap = getStationByNames(names);
		Map<String, Map> stationMap1 = getStationByNos(nos);
		if(canSave.equals("false")){
			for (Map<String, String> c : allStation) {
				Map exisStation = stationMap.get(c.get("stationName"));
				Map exisStation1 = stationMap1.get(c.get("stationNo"));
				if (exisStation != null || exisStation1 != null) {
					if ("false".equals(canSave)) {
						c.put(IMPSTATUS, "更新失败");
					} else {
						c.put(IMPSTATUS, "更新成功");
					}
				} else {
					if ("false".equals(canSave)) {
						c.put(IMPSTATUS, "保存失败");
					} else {
						c.put(IMPSTATUS, "保存成功");
					}
				}
			}
		}else{
			// 将数据放入list中 准备新增
			List saveList = new ArrayList();
			List updateList = new ArrayList();
			Set stationIds = new HashSet();
			for (Map<String, String> c : allStation) {
				Map exisStation = stationMap.get(c.get("stationName"));
				Map exisStation1 = stationMap1.get(c.get("stationNo"));
				String orgName = c.get("orgId")!=null?c.get("orgId").toString():"";
				for (ComboxVo combox : orgComboxs) {
					if(orgName.equals(combox.getText())){
						c.put("orgId", combox.getId());
						break;
					}
				}
				String place = "";
				if(c.get("provCode") != null){
					place = c.get("provCode").toString();
				}
				if (!"".equals(place)) {
					String arr[] = place.split("\\\\");
					c.put("provCode", areaService.getAreaNoByName(arr[0]));
					if (arr.length > 1){
						c.put("cityCode", areaService.getAreaNoByName(arr[1]));
					}
					if (arr.length > 2){
						c.put("distCode", areaService.getAreaNoByName(arr[2]));
					}
				}
				String rental ="";
				if(c.get("rentalstartdate") != null){
					rental = c.get("rentalstartdate").toString();
				}
				if (!"".equals(rental)) {
					String arr[] = rental.split("-");
					c.put("rentalStartDate", arr[0].replace("/", "-"));
					if (arr.length > 1){
						c.put("rentalEndDate", arr[1].replace("/", "-"));
					}
				}
				String stationTime ="";
				if(c.get("stationstartdate") != null){
					stationTime = c.get("stationstartdate").toString();
				}
				if (!"".equals(stationTime)) {
					String arr[] = stationTime.split("-");
					c.put("stationStartDate", arr[0].replace("/", "-"));
					if (arr.length > 1){
						c.put("rentalEndDate", arr[1].replace("/", "-"));
					}
				}
				c.put("useStatus","0");//默认设置有效
				
				if (exisStation == null && exisStation1 == null) {
					c.put(IMPSTATUS, "新增");
					saveList.add(c);
				} else if (exisStation != null && exisStation1 == null) {
					 if(orgIds == null){
						c.put(IMPSTATUS, "已存在");
						c.put(DESCRIPTION, "校验失败:场站名称已存在");
					 }else if(orgIds != null && !orgIds.contains(exisStation.get("orgId"))){
						 c.put(IMPSTATUS,"已存在");
						 c.put(DESCRIPTION,"校验失败:场站名称已存在");
					 }
				} else if (exisStation != null
						&& orgIds != null
						&& !orgIds.contains(Integer.parseInt(exisStation.get(
								"orgId").toString()))) {
					c.put(IMPSTATUS, "无法更新");
					c.put(DESCRIPTION, "校验失败:平台已存在该场站名称");
				}
				// 其中一个不为空或者2个都不为空但是同一个对象，说明名称和编号是相同的，执行更新操作
				else if ((exisStation != null && exisStation1 == null)
						|| (exisStation == null && exisStation1 != null)
						|| (exisStation != null && exisStation1 != null && exisStation
						.equals(exisStation1))) {
					if(exisStation != null){
						c.put("stationId", exisStation.get("stationId").toString());
					}else{
						c.put("stationId", exisStation1.get("stationId").toString());
					}
					c.put(IMPSTATUS, "已存在");
					updateList.add(c);
				} else {
					c.put(IMPSTATUS, "无法更新");
					c.put(DESCRIPTION, "校验失败:场站名称和场站编号都存在重复值，");
				}
			}
			if (updateList.size() > 0) {
				vo = new DataVo();
				vo.put("updateList", updateList);
				stationMapper.updateStation(vo);
			}
			if (saveList.size() > 0) {
				vo = new DataVo();
				vo.put("list", saveList);
				stationMapper.saveStation(vo);
				for (int i = 0; i < saveList.size(); i++) {
					Map subMap = (Map) saveList.get(i);
					if(subMap.get("stationId")!=null){
						stationIds.add(Integer.valueOf(subMap.get("stationId").toString()));
					}
				}
				userService.insertRoleDataByUserId(userId, stationIds, RoleDataEnum.STATION.dataType);
			}
		}
		
		return allStation;
	}

	/**
	 * 通用更新失败
	 * 
	 * @param allStation
	 * @param stationMap
	 * @param stationMap1
	 */
	private void updateFail(List<Map<String, String>> allStation,
			Map<String, Map> stationMap, Map<String, Map> stationMap1) {
		for (Map<String, String> c : allStation) {
			Map exisStation = stationMap.get(c.get("stationName"));
			Map exisStation1 = stationMap1.get(c.get("stationNo"));
			if (exisStation != null || exisStation1 != null) {
				c.put(IMPSTATUS, "更新失败");
			} else {
				c.put(IMPSTATUS, "保存失败");
			}
		}
	}


	/**
	 * 解析xml
	 */
	private List parseExcel(MultipartFile fileName) throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Workbook wb = null;
		Sheet sheet = null;
		try {
			wb = new HSSFWorkbook(fileName.getInputStream());
		} catch (Exception e) {
			wb = new XSSFWorkbook(fileName.getInputStream());
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
				int cells = 30;
				if (i == 0) {// 首行表头过滤
					continue;
				}
				Object[] arra = new Object[30];
				String checkStr = "校验正确";
				for (int j = 0; j < cells; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							arra[j] = cell.getCellFormula();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							arra[j] = cell.getNumericCellValue() + "";
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
					if (arra[j] == null || "".equals(arra[j])) {
						switch (j) {
						case 0:
							checkStr = "校验失败:运营商名称不能为空";
							break;
						case 1:
							checkStr = "校验失败:场站编号不能为空";
							break;
						case 2:
							checkStr = "校验失败:场站名称不能为空";
							break;
						case 8:
							checkStr = "校验失败:地理位置不能为空";
							break;
						case 19:
							checkStr = "校验失败:购电单价不能为空，";
							break;
						default:
							break;
						}
					} else {
						switch (j) {
						case 1:
							if (!ValidateUtils.Number(arra[j].toString()))
								checkStr = "校验失败:场站编号必须为数字";
							if (arra[j].toString().length() > 3)
								checkStr = "校验失败:场站编号长度不能超过3位数字";
							break;
						case 7:
							if (!ValidateUtils.Number(arra[j].toString()))
								checkStr = "校验失败:车位数量必须为数字";
							break;
						case 10:
							if (!ValidateUtils.Number(arra[j].toString()))
								checkStr = "校验失败:场地面积必须为数字";
							break;
						case 19:
							if (!ValidateUtils.Number(arra[j].toString()))
								checkStr = "校验失败:购电单价必须为数字";
							break;
						case 27:
							if (!ValidateUtils.Number(arra[j].toString()))
								checkStr = "校验失败:经度必须为数字";
							break;
						case 28:
							if (!ValidateUtils.Number(arra[j].toString()))
								checkStr = "校验失败:纬度必须为数字";
							break;

						default:
							break;
						}
					}
				}
				arra[cells - 1] = checkStr;
				if ((null != arra[1] && !"".equals(arra[1].toString()))
						|| (null != arra[2] && !"".equals(arra[2].toString()))) {
					result.add(arra);
				}
			}
		}
		return result;
	}

	/**
	 * object[]类型转换成Map<String,String>类型
	 * 
	 * @param obj
	 * @param omap
	 * @return
	 * @throws NullPointerException
	 * @throws BizException
	 */
	private Map<String, String> transObjToMap(Object[] obj,
			Map<String, Map> omap, Set<Integer> orgIds) throws Exception {
		//DecimalFormat format = new DecimalFormat("###");
		Map<String, String> map = new HashMap<String, String>();
		String desc = obj[29].toString();
		Map org;
		try {
			Double parseOrgId = Double.parseDouble(obj[0] + "");
			org = omap.get(parseOrgId.intValue() + "");
		} catch (Exception e) {
			org = omap.get(obj[0]);
		}
		if (orgIds != null) {
			if (!orgIds.contains(Integer.valueOf(org.get("orgId").toString()))) {
				desc = "校验失败：没有给当前运营商导入场站的权限";
			}
		}
		map.put("orgId", org.get("orgName").toString());
		if (null != obj[1]) {
			// 转换场站编号
			Double stationNo = Double.parseDouble(obj[1] + "");
			String newStationNo = stationNo.intValue() + "";
			if (newStationNo.length() < 3) {
				StringBuffer sb = new StringBuffer("");
				int zeroSize = 3 - newStationNo.length();
				for (int j = 0; j < zeroSize; j++) {
					sb.append("0");
				}
				sb.append(newStationNo);
				newStationNo = sb.toString();
			}

			map.put("stationNo", org.get("orgNo").toString() + newStationNo);

		}

		map.put("stationName", objToStr(obj[2]));
		map.put("stationModel", objToStr(obj[3]));
		if (null != obj[3]
				&& "".equals(dictService.getDictValue("yyms", obj[3].toString()))) {
			desc = "校验失败：运营模式不存在";
		}
		map.put("construction", objToStr(obj[4]));
		if (null != obj[4]
				&& "".equals(dictService.getDictValue("jscs", obj[4].toString()))) {
			desc = "校验失败：建设场所不存在";
		}
		map.put("stationStatus", objToStr(obj[5]));
		if (null != obj[5]
				&& "".equals(dictService.getDictValue("yyzt", obj[5].toString())))
			desc = "校验失败：场站状态不存在";
		map.put("stationType", objToStr(obj[6]));
		if (null != obj[6]
				&& "".equals(dictService.getDictValue("jingylx",
						obj[6].toString()))) {
			desc = "校验失败：场站类型不存在";
		}
		if (null == obj[6]) {
			desc = "校验失败：场站类型为空";
		}
		map.put("parkNums", objToStr(obj[7]));
		String place = objToStr(obj[8]);
		map.put("provCode", place);
		if (!"".equals(place)) {
			String arr[] = place.split("\\\\");
			if ("".equals(areaService.getAreaNoByName(arr[0].toString())))
				desc = "校验失败：地理位置省份不存在";
			if (arr.length > 1) {
				if ("".equals(areaService.getAreaNoByName(arr[1].trim())))
					desc = "校验失败：地理位置市不存在";
			}
			if (arr.length > 2) {
				if ("".equals(areaService.getAreaNoByName(arr[2].trim())))
					desc = "校验失败：地理位置县(区)不存在";
			}
		}
		map.put("address", objToStr(obj[9]));
		map.put("stationArea", objToStr(obj[10]));
		map.put("parkInfo", objToStr(obj[11]));
		map.put("payMent", objToStr(obj[12]));
		map.put("busineHours", objToStr(obj[13]));
		map.put("stationContact", objToStr(obj[14]));
		try {
			BigDecimal stationTel = new BigDecimal(objToStr(obj[15]));
			map.put("stationTel", stationTel.toPlainString());
		} catch (Exception e) {
			map.put("stationTel", objToStr(obj[15]));
		}
		map.put("remark", objToStr(obj[16]));
		try {
			map.put("innerNo", objToStr(obj[17]).equals("") ? ""
					: objToStr(obj[17]));
		} catch (Exception e) {
			desc = "校验失败：内部编号数据类型异常";
		}
		map.put("innerName", objToStr(obj[18]));
		map.put("purPrice", objToStr(obj[19]));
		// 适用车型不做限制判断,直接存库
		String matchCar = objToStr(obj[20]);
		map.put("matchCars", matchCar);
		if (StringUtils.isBlank(matchCar)) {
			String arr[] = matchCar.split(",");
			if ("".equals(dictService.getDictValue("sylx", arr[0])))
				desc = "校验失败：车型不存在，";
			if (arr.length > 1
					&& "".equals(dictService.getDictValue("sylx", arr[1])))
				desc = "校验失败：车型不存在，";
		}
		map.put("supportOrder", objToStr(obj[21]));
		if (null != obj[21]
				&& "".equals(dictService.getDictValue("sf", obj[21].toString())))
			desc = "校验失败：是否支持预约状态不存在，";
		map.put("electricityFee", objToStr(obj[22]));
		map.put("serviceFee", objToStr(obj[23]));
		map.put("parkFee", objToStr(obj[24]));
		String rental = objToStr(obj[25]);
		map.put("rentalstartdate", rental);
		map.put("stationstartdate", objToStr(obj[26]));
		if (obj[27] != null && !"".equals(obj[27])) {
			BigDecimal lngDecimal = new BigDecimal(objToStr(obj[27]));
			map.put("stationLng", lngDecimal.toPlainString());
		}
		if (obj[28] != null && !"".equals(obj[28])) {
			BigDecimal latDecimal = new BigDecimal(objToStr(obj[28]));
			// 新增经纬度
			map.put("stationLat", latDecimal.toPlainString());
		}
		if (!"校验正确".equals(desc))
			desc = desc.replace("校验正确", "校验失败：");
		map.put(DESCRIPTION, desc);
		return map;
	}

	/**
	 * 去掉前后空格
	 * 
	 * @param object
	 * @return
	 */
	private static String objToStr(Object object) {
		return null != object ? object.toString().trim() : "";
	}

	/**
	 * Excel中是否已经存在同名或者同编号的数据
	 * 
	 * @param listMap
	 * @param map1
	 * @return
	 */
	private static Boolean mapNotExist(List<Map<String, String>> listMap,
			Map<String, String> map1) {
		String name = map1.get("stationName");
		String no = map1.get("stationNo");
		for (Map<String, String> map : listMap) {
			if (map.get("stationName").equals(name)
					|| map.get("stationNo").equals(no))
				return false;
		}
		return true;
	}

	/**
	 * 根据场站名称获取集合
	 * 
	 * @param names
	 * @return
	 */
	private Map<String, Map> getStationByNames(List<String> names) {
		Map<String, Map> stationMap = new HashMap<String, Map>();
		DataVo vo = new DataVo();
		vo.put("stationNames", names);
		List<Map> consList = stationMapper.getStationAll(vo);
		if (consList != null && consList.size() > 0) {
			for (int i = 0; i < consList.size(); i++) {
				Map map = consList.get(i);
				stationMap.put(map.get("stationName").toString(), map);
			}
		}
		return stationMap;
	}

	/**
	 * 根据场站编号获取集合
	 * 
	 * @param nos
	 * @return
	 */
	private Map<String, Map> getStationByNos(List<String> nos) {
		Map<String, Map> stationMap1 = new HashMap<String, Map>();
		DataVo vo = new DataVo();
		vo.put("stationNos", nos);
		List<Map> consList = stationMapper.getStationAll(vo);
		if (consList != null && consList.size() > 0) {
			for (Map c : consList) {
				stationMap1.put(c.get("stationNo").toString(), c);
			}
		}
		return stationMap1;
	}

	/**
	 * 获取已经存在的运行商
	 * 
	 * @return
	 * @throws BizException
	 */
	private Map<String, Map> getOrgId(DataVo vo) throws BizException {
		List list = orgService.findOrgs(vo).getList();
		Map<String, Map> omap = new HashMap<String, Map>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			omap.put(map.get("orgName").toString(), map);
		}
		return omap;
	}

	/**
	 * 获取orgNo
	 * @param map
	 * @return
	 */
	public DataVo getOrgNo(Map map) {
		return stationMapper.getOrgNo(map);
	}
	
	/**
	 * 查询第三方运营商业务字典
	 * @throws Exception 
	 */
	public PageInfo getToSubOperatorIdsDict(Map map) throws Exception{
		List list = null;
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		if(vo.isBlank("pageNum")){
			vo.put("pageNum", 1);
		}
		if(vo.isBlank("pageSize")){
			vo.put("pageSize", 20);
		}
		List<String> operatorIds = getOperatorIds(vo.getInt("userId"));
		if(operatorIds	!= null && operatorIds.size() > 0){
			vo.put("operatorIds", operatorIds);
		}
		PageHelper.startPage(vo);
		list = stationMapper.getToSubOperatorIdsDict(vo);
		return new PageInfo(list);
	}
	
	
	/**
	 * 查询第三方场站业务字典
	 * @throws Exception 
	 */
	public PageInfo getToStationinfoDict(Map map) throws Exception{
		List list = null;
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		if(vo.isBlank("pageNum")){
			vo.put("pageNum", 1);
		}
		if(vo.isBlank("pageSize")){
			vo.put("pageSize", 20);
		}
		List<String> operatorIds = getOperatorIds(vo.getInt("userId"));
		if(operatorIds != null && operatorIds.size()>0){
			vo.put("operatorIds", operatorIds);
		}
		PageHelper.startPage(vo);
		list = stationMapper.getToStationinfoDict(vo);
		return new PageInfo(list);
	}

}

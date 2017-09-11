package com.clouyun.charge.modules.document.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.PileTypeUtils;
import com.clouyun.charge.modules.charge.service.PriceModelService;
import com.clouyun.charge.modules.document.mapper.GunMapper;
import com.clouyun.charge.modules.document.mapper.PileMapper;
import com.clouyun.charge.modules.document.mapper.QrCodeMapper;
import com.clouyun.charge.modules.document.mapper.StationMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 描述: PileService 充电桩管理 版权: Copyright (c) 2017 公司: 科陆电子 作者: sim.y 版本: 2.0 创建日期:
 * 2017年4月19日
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class PileService {

	private static final String DESCRIPTION = "description";

	@Autowired
	GunMapper gunMapper;
	@Autowired
	PileMapper pileMapper;
	@Autowired
	private QrCodeMapper codeMapper;
	@Autowired
	StationMapper stationMapper;
	
	@Autowired
	StationService stationService;
	
	@Autowired
	private DictService dictService;

	@Autowired
	PriceModelService priceModelService;
	
	@Autowired
	UserService userService;
	@Autowired
	private QrCodeService qrCodeService;

	private final Integer IS_QRCODE = 1;
	private final Integer SZ_PILE_PROTOCOL = 51;
	
	/**
	 * 获取充电桩列表
	 */
	public PageInfo getPileAll(Map map) throws BizException {
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds != null){
			vo.put("orgIds", orgIds);
		}
		Set<Integer> stationIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.STATION.dataType);
		if(stationIds != null){
			vo.put("stationIds", stationIds);
		}
		// 设置分页
		if (vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")) {
			PageHelper.startPage(vo);
		}
		// 查询充电桩列表
		List pileList = pileMapper.getPileAll(vo);
		// 获取设备Id
		Set<Integer> pileIds = new HashSet<Integer>();
		for (int i = 0; i < pileList.size(); i++) {
			Map pileMap = (Map) pileList.get(i);
			pileIds.add(Integer.valueOf(pileMap.get("pileId").toString()));
			// 将模板中的电价设置到 prcIdPre
			if(pileMap.get("prcIdPre")==null || "".equals(pileMap.get("prcIdPre").toString())){
				pileMap.put("prcIdPre", "");
				pileMap.put("priceDesc", "");
			}else{
				Integer prcTypeCode = (pileMap.get("prcTypeCode")!=null && !"".equals(pileMap.get("prcTypeCode").toString()))?Integer.parseInt(pileMap.get("prcTypeCode").toString()):null;
				String priceDesc ="";
				if(prcTypeCode != null){
					if(prcTypeCode == 1){
						priceDesc = "电价(￥/kWh):" + (pileMap.get("prcZxygz1")!=null?pileMap.get("prcZxygz1").toString():"0");
					}else{
						priceDesc = "电价(￥/kWh):" + "费率1:"+(pileMap.get("prcZxygz1")!=null?pileMap.get("prcZxygz1").toString():"0")
								+ "," + "费率2:"+(pileMap.get("prcZxygz2")!=null?pileMap.get("prcZxygz2").toString():"0")
										+ "," + "费率3:"+(pileMap.get("prcZxygz3")!=null?pileMap.get("prcZxygz3").toString():"0")
												+ "," + "费率4:"+(pileMap.get("prcZxygz4")!=null?pileMap.get("prcZxygz4").toString():"0");
					}
					priceDesc += ",服务费(￥/kWh):" + (pileMap.get("prcService")!=null?pileMap.get("prcService").toString():"0");
				}
				pileMap.put("priceDesc", priceDesc);
			}
		}

		return new PageInfo(pileList);
	}

	/**
	 * 根据场站获取设备数量
	 * 
	 * @throws BizException
	 */
	public List getPileCountByStationId(Map map) throws BizException {
		if (map == null || map.size() <= 0) {
			throw new BizException(1102001, "场站Ids");
		}
		DataVo vo = new DataVo(map);
		return pileMapper.getPileCountByStationId(vo);
	}

	/**
	 * 根据设备Id将设备置为无效
	 */
	@Transactional(rollbackFor = Exception.class)
	public void dissPile(List pileIds) throws BizException {
		if (pileIds == null || pileIds.size() <= 0) {
			throw new BizException(1102001, "充电桩Ids");
		}
		DataVo vo = new DataVo();
		vo.put("pileIds", pileIds);
		pileMapper.dissPile(vo);
	}

	/**
	 * 根据充电桩Id获取充电桩信息
	 */
	public Map getPileById(Integer pileId) throws BizException {
		if (pileId == null) {
			throw new BizException(1102001, "充电桩Id");
		}
		Map meter = null, gun = null, meters = null;
		Integer innerId = null;
		DataVo vo = new DataVo();
		vo.add("pileId", pileId);
		// 获取充电桩信息
		Map pileMap = pileMapper.getPileById(vo);
		if (pileMap != null && pileMap.size() > 0) {
			String stationNo = pileMap.get("stationNo") != null ? pileMap.get(
					"stationNo").toString() : "";
			String pileNo = pileMap.get("pileNo") != null ? pileMap.get(
					"pileNo").toString() : "";
			String selfNo = pileNo.substring(stationNo.length());
			pileMap.put("selfNo", selfNo);
			Integer prcIdPre = (pileMap.get("prcIdPre") != null && !"".equals(pileMap.get("prcIdPre").toString())) ? Integer
					.valueOf(pileMap.get("prcIdPre").toString()) : -1;
			if (prcIdPre.intValue() != -1) {
				// 获取该桩的电价模板信息
					Integer prcTypeCode = (pileMap.get("prcTypeCode")!=null && !"".equals(pileMap.get("prcTypeCode").toString()))?Integer.parseInt(pileMap.get("prcTypeCode").toString()):null;
					String priceDesc ="";
					if(prcTypeCode != null){
						if(prcTypeCode == 1){
							priceDesc = "电价(￥/kWh):" + (pileMap.get("prcZxygz1")!=null?pileMap.get("prcZxygz1").toString():"0");
						}else{
							priceDesc = "电价(￥/kWh):" + "费率1:"+(pileMap.get("prcZxygz1")!=null?pileMap.get("prcZxygz1").toString():"0")
									+ "," + "费率2:"+(pileMap.get("prcZxygz2")!=null?pileMap.get("prcZxygz2").toString():"0")
											+ "," + "费率3:"+(pileMap.get("prcZxygz3")!=null?pileMap.get("prcZxygz3").toString():"0")
													+ "," + "费率4:"+(pileMap.get("prcZxygz4")!=null?pileMap.get("prcZxygz4").toString():"0");
						}
						priceDesc += ",服务费(￥/kWh):" + (pileMap.get("prcService")!=null?pileMap.get("prcService").toString():"0");
					}
					pileMap.put("priceDesc", priceDesc);
			}else{
				pileMap.put("prcName", "");
				pileMap.put("priceDesc", "");
			}
			// 获取该桩的表计信息
			List meterList = pileMapper.getMeterInfo(vo);
			// 获取该桩的充电枪信息
			List gunList = gunMapper.getGunAll(vo);
			Object numberGunObj = pileMap.get("numberGun");
			int numberGun = (numberGunObj != null && !"".equals(numberGunObj.toString())) ? Integer.valueOf(pileMap.get("numberGun").toString()):-1;
			Object ortModeObj = pileMap.get("ortMode");
			int ortMode = (ortModeObj != null && !"".equals(ortModeObj.toString())) ? Integer.valueOf(pileMap.get("ortMode").toString()):-1;
			if(numberGun > 0 && ortMode > 0){
				Map<String, Integer> gunNoMap=PileTypeUtils.getGunInnerId(numberGun, ortMode);
				if(meterList != null && meterList.size()>0 ){
					String gunNoKey = "";
					meters = new TreeMap();
					for (int i = 0; i < meterList.size(); i++) {
						meter = (Map) meterList.get(i);
						innerId = Integer.valueOf(meter.get("innerId").toString());
						if(innerId != null){
							for (String key : gunNoMap.keySet()) {
								if(gunNoMap.get(key).intValue() == innerId.intValue()){
									gunNoKey = key;
									break;
								}
							}
							for (int j = 0; j < gunList.size(); j++) {
								gun = (Map) gunList.get(j);
								if(innerId.intValue() == Integer.valueOf(gun.get("innerId").toString()).intValue()){
									meter.put("gun", gun);
									break;
								}
							}
							meter.put("key",gunNoKey);
							meters.put("meter"+gunNoKey, meter);
						}
					}
				}else{
					meters = getMeterMap(numberGun, ortMode);
				}
			}
			pileMap.put("meter", meters);
		}
		return pileMap;
	}

	/**
	 * 查询第三方充电桩列表数据
	 */
	public PageInfo getToEquipmentinfoAll(Map map) throws BizException {
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		if (vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")) {
			PageHelper.startPage(vo);
		}
		List<String> operatorIds = stationService.getOperatorIds(vo.getInt("userId"));
		if(operatorIds != null && operatorIds.size()>0){
			vo.put("operatorIds", operatorIds);
		}
		List list = pileMapper.getToEquipmentinfoAll(vo);
		return new PageInfo(list);
	}

	/**
	 * 根据第三方充电桩id查询第三方充电桩信息
	 */
	public Map getToEquipmentinById(Integer id) throws BizException {
		if (id == null) {
			throw new BizException(1102001, "第三方充电桩id");
		}
		DataVo vo = new DataVo();
		vo.add("id", id);
		// 根据第三方充电桩id查询第三方充电桩信息
		Map toEqipmentMap = pileMapper.getToEquipmentinById(vo);
		//String equipmentType = dictService.getDictLabel("dsfcdzlx",toEqipmentMap.get("equipmenttype").toString()); 
		toEqipmentMap.put("equipmenttype", toEqipmentMap.get("equipmenttype"));
		vo = new DataVo();
		String equipmentId = toEqipmentMap.get("equipmentid").toString();
		vo.add("equipmentId", equipmentId);
		// 根据设备编码equipmentId查询充电设备接口信息
		List toConnList = pileMapper.getToConnectorinfoById(vo);
		for (int i = 0; i < toConnList.size(); i++) {
			Map map = (Map) toConnList.get(i);
			// 接口中必填，不考虑空的情况
			int type = Integer.valueOf(map.get("connectortype").toString());
			map.put("connectortype", statusConvert(type));
		}
		toEqipmentMap.put("toConnList", toConnList);
		return toEqipmentMap;
	}

	/**
	 * 查询充电桩业务字典
	 * @throws BizException 
	 */
	public PageInfo getPile(Map map) throws BizException {
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
		List list = pileMapper.getPile(vo);
		PageInfo pageList = new PageInfo(list);
		List<ComboxVo> cvList = new ArrayList<ComboxVo>();
		for (int i = 0; i < pageList.getList().size(); i++) {
			Map cvMap = (Map) pageList.getList().get(i);
			cvList.add(new ComboxVo(cvMap.get("pileId").toString(), cvMap.get(
					"pileName").toString()));
		}
		return new PageInfo(list);
	}

	/**
	 * 充电设备接口类型转换
	 * 
	 * @param i
	 * @return
	 */
	private static String statusConvert(int i) {
		String type = "";
		switch (i) {
		case 1:
			type = "家用插座（模式2）";
			break;
		case 2:
			type = "交流接口插座（模式3，连接方式B ）";

			break;
		case 3:
			type = "交流接口插头（带枪线，模式3，连接方式C）";

			break;
		case 4:
			type = "直流接口枪头（带枪线，模式4）";

			break;

		default:
			break;
		}
		return type;
	}

	private List<String> list = Arrays.asList(new String[]{
			"pileModelId",
			"productionDate",
			"installDate",
			"auxiPower",
			"meterPower",
			"meterRatio"
	});
	
	
	/**
	 * 充电桩新增
	 * 2017年5月2日
	 * gaohui 2.0.0
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertPile(Map map) throws BizException {
		DataVo params = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(params,list);
		if (params.isBlank("subsNo")) {
			throw new BizException(1102001, "场站编号");
		}
		String selfNo = "";
		if (params.isBlank("selfNo")) {
			throw new BizException(1102001, "充电桩自编号");
		}
		if(params.getString("selfNo").length()>3){
			throw new BizException(1000017, "充电桩自编号",3);
		}else if(!ValidateUtils.Number(params.getString("selfNo"))){
			throw new BizException(1102006, "充电桩自编号");
		}else{
			if(params.getString("selfNo").length()==1){
				selfNo = "00" + params.getString("selfNo");
			}else if(params.getString("selfNo").length()==2){
				selfNo = "0" + params.getString("selfNo");
			}else{
				selfNo = params.getString("selfNo");
			}
		}
		checkPile(params);
		String pileNo = params.getString("subsNo") + selfNo;
		DataVo nVo = new DataVo();
		nVo.put("pileNo", pileNo);
		if (pileMapper.getPileExistByVar(nVo) > 0) {
			throw new BizException(1102007, "充电桩编号");// 已存在!
		}
		DataVo aVo = new DataVo();
		aVo.put("pileAddr", params.getString("pileAddr"));
		if (pileMapper.getPileExistByVar(aVo) > 0) {
			throw new BizException(1102007, "通讯地址");// 已存在!
		}
		params.put("pileNo", pileNo);

		ortModeToPileType(params);

		pileMapper.insertPile(params);
		//新增表计和枪
		MeterAndGun(params);
	/*	List<Map> list = (List<Map>) params.get("meter");
		if (list.size() > 0) {
			MeterAndGun(list, params);
		}
		CpCommObj cp = new CpCommObj();
		cp.setTrmId(params.getInt("pileId"));*/
//		BusiService.service.refresh(cp, RefreshObj.CHANGETYPE_MOD);
	}

	private void ortModeToPileType(DataVo params){
		int ortMode = params.getInt("ortMode");
		int numberGun = params.getInt("numberGun");
		//1 交流  2  直流   3  交直一体
		if (ortMode == 1){
			if(numberGun == 1){
				params.put("pileType",1);
			}else if(numberGun >= 2){
				params.put("pileType",3);
			}
		}else if(ortMode == 2){
			if(numberGun == 1){
				params.put("pileType",2);
			}else if(numberGun >= 2){
				params.put("pileType",4);
			}
		}else if(ortMode == 3){
			params.put("pileType",5);
		}
	}


	/**
	 * 充电桩编辑
	 *  2017年5月2日 
	 *  gaohui 2.0.0
	 * @throws BizException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void modifyPile(Map map) throws BizException {
		DataVo params = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(params,list);
		if (params.isBlank("pileId")) {
			throw new BizException(1102001, "充电桩id");
		}
		if (params.isBlank("pileNo")) {
			throw new BizException(1102001, "充电桩编号");
		}
		checkPile(params);
		DataVo nVo = new DataVo();
		nVo.put("pileNo", params.getString("pileNo"));
		nVo.put("pileId", params.getInt("pileId"));
		if (pileMapper.getPileExistByVar(nVo) > 0) {
			throw new BizException(1102007, "充电桩编号");// 已存在!
		}
		DataVo aVo = new DataVo();
		aVo.put("pileId", params.getInt("pileId"));
		aVo.put("pileAddr", params.getString("pileAddr"));
		if (pileMapper.getPileExistByVar(aVo) > 0) {
			throw new BizException(1102007, "通讯地址");// 已存在!
		}
		ortModeToPileType(params);

		MeterAndGun(params);

        paramMap.putAll(params);
		pileMapper.modifyPileByPileId(paramMap);
//		BusiService.service.refresh(cp, RefreshObj.CHANGETYPE_MOD);
	}

	/**
	 * 充电桩导出 
	 * 2017年5月4日 
	 * gaohui 
	 * 2.0.0
	 * @throws Exception
	 */
	public void exportPiles(Map map, HttpServletResponse response)throws Exception {
		String header = "充电桩列表";
		DataVo params = new DataVo(map);
        PageInfo pileAll = getPileAll(params);
        List<Map> pileList = pileAll.getList();
        if (null != pileList && pileList.size() > 0) {
			List<ComboxVo> jzmsList = dictService.getDictByType("jzms");
			List<ComboxVo> qsList = dictService.getDictByType("qs");
			List<ComboxVo> glmsList = dictService.getDictByType("glms");
			List<ComboxVo> pileStatuss = dictService.getDictByType("zt");
			List<ComboxVo> pileProtocols = dictService.getDictByType("txxy");
			List<ComboxVo> conCycles = dictService.getDictByType("xtzq");
			List<ComboxVo> manufacturerIds = dictService.getDictByType("cdzcj");
			List<ComboxVo> pileModelIds = dictService.getDictByType("sbxh");
			for (Map pile : pileList) {
				DataVo pileVo = new DataVo(pile);
				if (pileVo.isNotBlank("ortMode")) {
					for (ComboxVo comboxVo : jzmsList) {
						if (pileVo.getString("ortMode").equals(
								comboxVo.getId())) {
							pile.put("ortMode", comboxVo.getText());
							continue;
						}

					}
				}
				if (pileVo.isNotBlank("numberGun")) {
					for (ComboxVo comboxVo : qsList) {
						if (pileVo.getString("numberGun").equals(
								comboxVo.getId())) {
							pile.put("numberGun", comboxVo.getText());
							continue;
						}

					}
				}
				if (pileVo.isNotBlank("powerMode")) {
					for (ComboxVo comboxVo : glmsList) {
						if (pileVo.getString("powerMode").equals(
								comboxVo.getId())) {
							pile.put("powerMode", comboxVo.getText());
							continue;
						}

					}
				}
				if (pileVo.isNotBlank("pileStatus")) {
					for (ComboxVo comboxVo : pileStatuss) {
						if (pileVo.getString("pileStatus").equals(
								comboxVo.getId())) {
							pile.put("pileStatus", comboxVo.getText());
							continue;
						}
					}
				}
				if (pileVo.isNotBlank("pileProtocol")) {
					for (ComboxVo comboxVo : pileProtocols) {
						if (pileVo.getString("pileProtocol").equals(
								comboxVo.getId())) {
							pile.put("pileProtocol", comboxVo.getText());
							continue;
						}
					}
				}
				if (pileVo.isNotBlank("conCycle")) {
					for (ComboxVo comboxVo : conCycles) {
						if (pileVo.getString("conCycle").equals(
								comboxVo.getId())) {
							pile.put("conCycle", comboxVo.getText());
							continue;
						}
					}
				}
				if (pileVo.isNotBlank("manufacturerId")) {
					for (ComboxVo comboxVo : manufacturerIds) {
						if (pileVo.getString("manufacturerId").equals(
								comboxVo.getId())) {
							pile.put("manufacturerId", comboxVo.getText());
							continue;
						}
					}
				}
				if (pileVo.isNotBlank("pileModelId")) {
					for (ComboxVo comboxVo : pileModelIds) {
						if (pileVo.getString("pileModelId").equals(
								comboxVo.getId())) {
							pile.put("pileModelId", comboxVo.getText());
							continue;
						}
					}
				}
			}
		}
		List<String> headers = new ArrayList<String>();
		headers.add("运营商");
		headers.add("充电桩名称");
		headers.add("充电桩编号");
		headers.add("所属场站");
		headers.add("枪数");
		headers.add("交直模式");
		headers.add("功率模式");
		headers.add("使用状态");
		headers.add("通讯协议");
		headers.add("通讯地址");
		headers.add("心跳周期");
		headers.add("当前电价");
		headers.add("软件版本");
		headers.add("硬件版本");
		headers.add("生产厂家");
		headers.add("设备型号");
		headers.add("额定功率");
		headers.add("生产日期");
		headers.add("安装日期");
		List<String> rows = new ArrayList<String>();
		rows.add("orgName");
		rows.add("pileName");
		rows.add("pileNo");
		rows.add("stationName");
		rows.add("numberGun");
		rows.add("ortMode");
		rows.add("powerMode");
		rows.add("pileStatus");
		rows.add("pileProtocol");
		rows.add("pileAddr");
		rows.add("conCycle");
		rows.add("prcIdPre");
		rows.add("softVersion");
		rows.add("hardVersion");
		rows.add("manufacturerId");
		rows.add("pileModelId");
		rows.add("ratePower");
		rows.add("productionDate");
		rows.add("installDate");
		ExportUtils.exportExcel(pileList, response, headers, rows, header);
	}

	/**
	 * 第三方充电桩导出 
	 * 2017年5月4日 
	 * gaohui 2.0.0
	 * @throws Exception
	 */
	public void exportEquipments(Map map, HttpServletResponse response)throws Exception {
		String header = "第三方充电桩列表";
		DataVo params = new DataVo(map);
		List<Map> pileList = pileMapper.getToEquipmentinfoAll(params);
		if (null != pileList && pileList.size() > 0) {
			List<ComboxVo> equipmenttypes = dictService.getDictByType("cdzlx");
			List<ComboxVo> equipmentmodels = dictService.getDictByType("sbxh");
			for (Map pile : pileList) {
				DataVo pileVo = new DataVo(pile);
				if (pileVo.isNotBlank("equipmenttype")) {
					for (ComboxVo comboxVo : equipmenttypes) {
						if (pileVo.getString("equipmenttype").equals(
								comboxVo.getId())) {
							pile.put("equipmenttype", comboxVo.getText());
							continue;
						}

					}
				}

				if (pileVo.isNotBlank("equipmentmodel")) {
					for (ComboxVo comboxVo : equipmentmodels) {
						if (pileVo.getString("equipmentmodel").equals(
								comboxVo.getId())) {
							pile.put("equipmentmodel", comboxVo.getText());
							continue;
						}
					}
				}
			}
		}
		List<String> headers = new ArrayList<String>();
		headers.add("运营商");
		headers.add("充电桩名称");
		headers.add("充电桩编号");
		headers.add("所属场站");
		headers.add("充电桩类型");
		headers.add("充电设备总功率");
		headers.add("充电设备经度");
		headers.add("充电设备纬度");
		headers.add("设备生产商组");
		headers.add("设备名称");
		headers.add("设备型号");
		List<String> rows = new ArrayList<String>();
		rows.add("operatorname");
		rows.add("equipmentname");
		rows.add("equipmentid");
		rows.add("stationname");
		rows.add("equipmenttype");
		rows.add("power");
		rows.add("equipmentlng");
		rows.add("equipmentlat");
		rows.add("manufacturerid");
		rows.add("manufacturername");
		rows.add("equipmentmodel");
		ExportUtils.exportExcel(pileList, response, headers, rows, header);
	}

	public void checkPile(DataVo params) throws BizException {
		if (params.isBlank("pileName")) {
			throw new BizException(1102001, "充电桩名称");
		}
		if (params.isBlank("orgId")) {
			throw new BizException(1102001, "运营商");
		}
		if (params.isBlank("stationId")) {
			throw new BizException(1102001, "所属场站");
		}
		if (params.isBlank("pileStatus")) {
			throw new BizException(1102001, "使用状态");// 有效-0 无效-1
		}
		if(params.isBlank("numberGun")){
			throw new BizException(1102001,"枪数");
		}
		if(params.isBlank("ortMode")){
			throw new BizException(1102001,"交直模式");
		}
		if(params.isBlank("powerMode")){
			throw new BizException(1102001,"功率模式");
		}
		if (params.isBlank("pileProtocol")) {
			throw new BizException(1102001, "通讯协议");
		}
		if (params.isBlank("conCycle")) {
			throw new BizException(1102001, "心跳周期");// 15、30、60
		}
		if (params.isBlank("pileAddr")) {
			throw new BizException(1102001, "通讯地址");
		}
		if(params.isBlank("ratePower")){
			throw new BizException(1102001,"充电桩额定功率");
		}
		if (!ValidateUtils.Number(params.getString("ratePower"))){
			throw  new BizException(1700001,"充电桩额定功率");
		}

		Map meter = (Map) params.get("meter");
		if(meter != null && meter.size() > 0){
			for (Object key: meter.keySet()) {
				Map map2 = (Map) meter.get(key);
				DataVo vo = new DataVo(map2);
				if(vo.isNotBlank("meterPower")){
					if (!ValidateUtils.Number(vo.getString("meterPower"))){
						throw  new BizException(1700001,vo.getString("meterName"));
					}
				}
			}
		}

		// if(params.isBlank("prcIdPre")){
		// throw new BizException(1,"当前电价");
		// }
		// if(params.isBlank("priceDesc")){
		// throw new BizException(1,"电价描述");
		// }
		// if(params.isBlank("softVersion")){
		// throw new BizException(1,"当前版本");
		// }
		// if(params.isBlank("hardVersion")){
		// throw new BizException(1,"硬件版本");
		// }
//		if (params.isBlank("manufacturerId")) {
//			throw new BizException(1102001, "生产厂家");// 1 深圳科陆
//		}
//		if (params.isBlank("pileModelId")) {
//			throw new BizException(1102001, "设备型号");// 1 、11
//		}
		// if(params.isBlank("pileCap")){
		// throw new BizException(1,"桩额定功率");//kw
		// }
//		if (params.isBlank("pileGbProtocol")) {
//			throw new BizException(1102001, "国标类型");// 1 2
//		}
		// if(params.isBlank("productDate")){
		// throw new BizException(1,"生产日期");
		// }
		// if(params.isBlank("instDate")){
		// throw new BizException(1,"安装日期");
		// }
		/*if (params.isBlank("meter")) {
			throw new BizException(1102001, "表计信息");
		}*/
		
		
		int count = pileMapper.checkPileName(params);
		if(count > 0){
			throw new BizException(1102007, "充电桩名称");
		}

		//通讯协议
//		Integer pileProtocol = params.getInt("pileProtocol");
		String pileModeId = params.getString("pileModelId");
		Integer isQrcode = params.getInt("isQrcode");
		Integer pileProtocol = params.getInt("pileProtocol");


		List<String> modelId = new ArrayList<>();
		//选择屏显二维码-->是
		if(IS_QRCODE == isQrcode && SZ_PILE_PROTOCOL == pileProtocol){
			List<ComboxVo> pxsbxh = dictService.getDictByType("pxsbxh");
			//深圳只支持两种型号
			if (pxsbxh != null && pxsbxh.size() > 0){
				for (ComboxVo c : pxsbxh) {
					modelId.add(c.getId());
				}
			}
		}

		if (modelId.size() > 0 && !modelId.contains(pileModeId)){
			throw new BizException(1102024);
		}
	}
	/**
	 * 出线表信息编辑
	 */
	public void MeterAndGun(DataVo params) throws BizException {
		Integer numberGun = params.getInt("numberGun");
		Integer ortMode = params.getInt("ortMode");
		DataVo meterVo = null;
		DataVo gunVo = new DataVo();
		String msg = "";
		if(params.isBlank("meter")){
			throw new BizException(1102001, "出线表信息");
		}
		Set<String> qrCodes = new HashSet<String>();
		DataVo meter = new DataVo(params.getMap("meter"));
		Map<String, Integer> gunInnerIdMap = PileTypeUtils.getNumberGun(numberGun, ortMode);

        DataVo vo = new DataVo();
        if(params.isNotBlank("pileId")){
            Set<Integer> pileIds = new HashSet<Integer>();
            pileIds.add(params.getInt("pileId"));
            vo.put("pileIds", pileIds);
        }

        List list = new ArrayList();
        list.add(params.getInt("pileId"));
        List<Map> qrCodeInfos = codeMapper.selectChgGunByPileId(list);
        Map qrCodeMap = new HashMap();
		if(qrCodeInfos != null && qrCodeInfos.size() > 0){
            for (Map map : qrCodeInfos) {
                qrCodeMap.put(map.get("gumPoint"),map.get("qrCode"));
            }
        }

        if(params.isNotBlank("pileId")){
            pileMapper.deleteMeterByPileId(vo);
            gunMapper.deleteGunByPileId(vo);
        }

        for (String key : gunInnerIdMap.keySet()) {
			if("Z".equals(key)){
				msg = "总表";
			}else{
				msg = msg+"枪出线表";
			}
			if(meter.isBlank("meter"+key)){
				throw new BizException(1102001, msg);
			}
			meterVo = new DataVo(meter.getMap("meter"+key));
			if(meterVo.isBlank("meterName")){
				throw new BizException(1102001,msg+",表计名称");
			}
			if(meterVo.isBlank("innerId")){
				throw new BizException(1102001,msg+",桩内表编号");
			}
			if("Z".equals(key)){
				if(meterVo.isBlank("meterRatio")){
					meterVo.put("meterRatio", 1);
				}
			}else{
				//枪
				if(meterVo.isBlank("gun")){
					throw new BizException(1102001, msg+",枪口信息");
				}
				gunVo = new DataVo(meterVo.getMap("gun"));
				if(gunVo.isBlank("qrCode")){
					throw new BizException(1102001,msg+",二维码");
				}
				if(gunVo.isBlank("gumPoint")){
					throw new BizException(1102001,msg+",枪口名称");
				}
				qrCodes.add(gunVo.getString("qrCode"));
				int count = pileMapper.checkQrCode(gunVo.getString("qrCode"));
				if(count > 0){
					throw new BizException(1102007, msg+",二维码");
				}

                Object oldQrCode = qrCodeMap.get("充电枪" + key);
				if (!gunVo.get("qrCode").equals(oldQrCode)){
				    params.put("qrCodeState","0");
					params.put("hlhtQrcodeState","0");
                }
            }

			meterVo.put("pileId", params.getInt("pileId"));
			pileMapper.insertMeter(meterVo);
			
			if(!"Z".equals(key)){
				gunVo.put("pileId", params.getInt("pileId"));
				gunVo.put("innerId", meterVo.getString("innerId"));
				gunVo.put("meterId", meterVo.getInt("meterId"));
				gunVo.put("connectorId", params.getString("pileNo")+ "0" + gunInnerIdMap.get(key));
				gunMapper.insertGun(gunVo);
			}
		}
		/*if(qrCodes.size() < numberGun){
			throw new BizException(1102007, "二维码");
		}*/
	}

	/*private Map<String, String> getConnectorIdMap(DataVo params) {
		Map pile = pileMapper.getPileById(params);
		DataVo chgPile = new DataVo(pile);
		List<Map> listMap = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		if (null != pile) {
			map.put("pileNo", chgPile.getString("pileNo"));
			map.put("stationNo", chgPile.getString("stationNo"));
			map.put("orgCode", chgPile.getString("orgCode"));
			map.put("orgNo", chgPile.getString("orgNo"));
			map.put("gumPoint", params.getString("gumPoint"));
			map.put("innerId", params.getString("innerId"));
			listMap.add(map);
		}
		Map<String, String> connector = ConnectorIdUtil.newGetConnector(listMap);
		return connector;
	}*/

	/**
	 * 互联互通二维码导出 
	 * 2017年5月4日 
	 * gaohui 
	 * 2.0.0
	 */
	public void exportQrcodes(Map map, HttpServletResponse response,HttpServletRequest request)throws Exception {
		DataVo params = new DataVo(map);
		if (params.isBlank("userId")){
			throw new BizException(1000006);
		}
		// 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		if(userRoleDataById != null && userRoleDataById.size() > 0){
			params.put("stationIds", userRoleDataById);
		}
		List<Map> all = pileMapper.getPileAll(params);
		handleGunInfoResult(all);
		qrCodeService.exportExcel(all,response,request);
	}

	// 结果集处理
		private void handleGunInfoResult(List<Map> all) {
			if (all.size() > 0) {
				// 将桩Id封装查询枪信息
				Set<Integer> set = new HashSet<>();
				for (Map map2 : all) {
					set.add(Integer.parseInt(map2.get("pileId").toString()));
				}

				//获取枪信息
				List<Integer> list = new ArrayList();
				list.addAll(set);
				List<Map> allGun = codeMapper.selectChgGunByPileId(list);
				// 将枪信息封装入Map返回
				for (Map pileMap : all) {
					List<Map> list1 = new ArrayList<>();
					for (Map gunMap : allGun) {
						if (pileMap.get("pileId").equals(gunMap.get("pileId"))) {
							list1.add(gunMap);
						}
					}
					pileMap.put("gunInfo",list1);
				}
			}
		}

	/**
	 * 解析Excel文件
	 * gaohui
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> parseExcel(MultipartFile fileName)
			throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(fileName.getInputStream());
		} catch (Exception e) {
			wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook(
					fileName.getInputStream());
		}
		Sheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		if (rows == 1) {
			throw new BizException(1102008);
		}
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				int cells = 20;
				if (i == 0) {// 首行表头过滤
					continue;
				}
				Object[] arra = new Object[20];
				for (int j = 0; j < cells; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							arra[j] = cell.getCellFormula();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							DecimalFormat df = new DecimalFormat("0");
							arra[j] = df.format(cell.getNumericCellValue()) + "";
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

				}
				result.add(arra);
			}
		}
		return result;
	}
    @Transactional(rollbackFor=Exception.class)
	public List<Map> importPiles(MultipartFile excelFile,Map map) throws Exception {
    	DataVo params = new DataVo(map);
    	if(params.isBlank("userId")){
    		throw new BizException(1102001,"用户Id");
    	}
		List<Map> result = null;
		List<Object[]> arraylist = parseExcel(excelFile);
		Set<Integer> stationIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		Map<String, DataVo> stationMap = getStationsMap(stationIds);// 场站权限-----场站名称需唯一.
		List<String> pileAddrs = new ArrayList<String>();// 通讯地址
		List<String> pileNos = new ArrayList<String>();// 充电桩编号
		//List<String> pileNames = new ArrayList<String>();// 充电桩名称
		List<Map> saveList = new ArrayList<Map>();
		List<Map> updateList = new ArrayList<Map>();
		List<Integer> delList = new ArrayList<Integer>();
		boolean flags = true;
		List<Map> pileList = new ArrayList<Map>();
		if (null != arraylist && arraylist.size() > 0) {
			for (int i = 0; i < arraylist.size(); i++) {
				boolean flag = true;
				Object[] obj = (Object[]) arraylist.get(i);
				Map pile = new HashMap<>();
				String desc = "";
				//充电桩名称
				if (obj[0] == null || "".equals(obj[0])) {
					if ("".equals(desc)) {
						desc = "校验失败:充电桩名称不能为空";
					}
					flag = false;
				} 
				pile.put("pileName", String.valueOf(obj[0]));
				
				//充电桩自编号
				String subsNo = "";
				if (obj[1] == null || "".equals(obj[1])) {
					if ("".equals(desc)) {
						desc = "校验失败:充电桩自编号不能为空";
					}
					flag = false;
				} else if (ValidateUtils.Number(obj[1].toString()) && obj[1].toString().length() > 3) {
					if ("".equals(desc)) {
						desc = "校验失败:充电桩自编号必须为数字且长度小于4";
					}
					flag = false;
				} else {
					if(obj[1].toString().length()==1){
						subsNo = "00"+obj[1].toString();
					}else if(obj[1].toString().length()==2){
						subsNo = "0"+obj[1].toString();
					}else{
						subsNo = obj[1].toString();
					}
				}
				pile.put("subNo", String.valueOf(obj[1]));
				//所属场站
				if (obj[2] == null || "".equals(obj[2])) {// 充电桩所属场站空检验
					if ("".equals(desc)) {
						desc = "校验失败:充电桩所属场站不能为空";
					}
					flag = false;
				} else if (null==stationMap.get(obj[2].toString())) {// 场站是否存在
					if ("".equals(desc)) {
						desc = "校验失败：没有给当前场站导入桩的权限";
					}
					flag = false;
				} else {
					Integer stationId = stationMap.get(obj[2].toString()).getInt("stationId");
					String stationNo = stationMap.get(obj[2].toString()).getString("stationNo");
					Integer orgId = stationMap.get(obj[2].toString()).getInt("orgId");
					if(!"".equals(subsNo)){
						String pileNo = stationNo + subsNo;
						pile.put("pileNo", pileNo);//充电桩编号
						if(pileNos.contains(pileNo.trim())){
							desc = "校验失败:充电桩编号重复";
						}
						pileNos.add(pileNo.trim());
					}
					pile.put("stationId",stationId);//场站Id
					pile.put("orgId", orgId);
				}
				pile.put("stationName", String.valueOf(obj[2]));

				if (obj[3] == null || "".equals(obj[3])) {// 交直模式空检验
					if ("".equals(desc)) {
						desc = "校验失败:交直模式不能为空";
					}
					flag = false;
				} else{// 充电桩类型是否存在
					String exist = dictService.getDictValue("jzms", obj[3].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:交直模式不存在";
						}
						flag = false;
					} else {
						pile.put("ortMode", exist);
					}
				} 

				if (obj[4] == null || "".equals(obj[4])) {// 枪数空检验
					if ("".equals(desc)) {
						desc = "校验失败:枪数不能为空";
					}
					flag = false;
				} else{// 枪数是否存在
					String exist = dictService.getDictValue("qs", obj[4].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:枪数不存在";
						}
						flag = false;
					} else {
						pile.put("numberGun", exist);
					}
				}

				if (obj[5] == null || "".equals(obj[5])) {// 功率模式空检验
					if ("".equals(desc)) {
						desc = "校验失败:功率模式不能为空";
					}
					flag = false;
				} else{// 枪数是否存在
					String exist = dictService.getDictValue("glms", obj[5].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:功率模式不存在";
						}
						flag = false;
					} else {
						pile.put("powerMode", exist);
					}
				}


				if (obj[6] == null || "".equals(obj[6])) {
					if ("".equals(desc)) {
						desc = "校验失败:通讯协议不能为空";
					}
					flag = false;
				} else {
					String exist = dictService.getDictValue("txxy", obj[6].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:通讯协议不存在";
						}
						flag = false;
					} else {
						pile.put("pileProtocol", exist);
					}
				} 
				pile.put("protocol", String.valueOf(obj[6]));


				// 通讯地址
				if (obj[7] == null || "".equals(obj[7])) {
					if ("".equals(desc)) {
						desc = "校验失败:通讯地址不能为空";
					}
					flag = false;
				} else {
					if(pileAddrs.contains(obj[7].toString().trim())){
						if ("".equals(desc)) {
							desc = "校验失败:通讯地址重复";
						}
						flag = false;
					}else{
						pileAddrs.add(obj[7].toString().trim());
					}
				}
				pile.put("pileAddr", String.valueOf(obj[7]));
				
				// 心跳周期
				if (obj[8] == null || "".equals(obj[8])) {
					if ("".equals(desc)) {
						desc = "校验失败:心跳周期不能为空";
					}
					flag = false;
				} else {
					String exist = dictService.getDictValue("xtzq", obj[8].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:心跳周期不存在";
						}
						flag = false;
					} else {
						pile.put("conCycle", exist);
					}
				} 
				pile.put("pileConCycle", String.valueOf(obj[8]));
				// 生产厂家
				if (obj[9] == null || "".equals(obj[9])) {
					if ("".equals(desc)) {
						desc = "校验失败:生产厂家不能为空";
					}
					flag = false;
				} else {
					String exist = dictService.getDictValue("cdzcj", obj[9].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:生产厂家不存在";
						}
						flag = false;
					} else {
						pile.put("manufacturerId", exist);
					}
				} 
				pile.put("manufacturer", String.valueOf(obj[9]));

				pile.put("serialNumber", String.valueOf(obj[10]));


				// 设备型号
				if (obj[11] == null || "".equals(obj[11])) {
					if ("".equals(desc)) {
						desc = "校验失败:设备型号不能为空";
					}
					flag = false;
				} else {
					String exist = dictService.getDictValue("sbxh", obj[11].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:设备型号不存在";
						}
						flag = false;
					} else {
						pile.put("pileModelId", exist);
					}
				} 
				pile.put("pileModel", String.valueOf(obj[11]));
				
//				if (obj[9] == null || "".equals(obj[9])) {
//					if ("".equals(desc)) {
//						desc = "校验失败:生产日期不能为空";
//					}
//					flag = false;
//				}
				pile.put("productionDate", String.valueOf(obj[12]));
				
				if (obj[13] == null || "".equals(obj[13])) {
					if ("".equals(desc)) {
						desc = "校验失败:安装日期不能为空";
					}
					flag = false;
				}
				pile.put("installDate", String.valueOf(obj[13]));
				
//				if (obj[11] == null || "".equals(obj[11])) {
//					if ("".equals(desc)) {
//						desc = "校验失败:内部编号不能为空";
//					}
//					flag = false;
//				}
				pile.put("innerPileNo", String.valueOf(obj[14]));
				
//				if (obj[12] == null || "".equals(obj[12])) {
//					if ("".equals(desc)) {
//						desc = "校验失败:内部名称不能为空";
//					}
//					flag = false;
//				}
				pile.put("innerPileName", String.valueOf(obj[15]));
				if (obj[16] == null || "".equals(obj[16])) {
					if ("".equals(desc)) {
						desc = "校验失败:额定功率不能为空";
					}
					flag = false;
				}
				pile.put("ratePower", String.valueOf(obj[16]));
				//
				if (obj[17] == null || "".equals(obj[17])) {
					if ("".equals(desc)) {
						desc = "校验失败:国标类型不能为空";
					}
					flag = false;
				} else {
					String exist = dictService.getDictValue("gbxy", obj[17].toString());
					if (null == exist || "".equals(exist)) {
						if ("".equals(desc)) {
							desc = "校验失败:国标类型不存在";
						}
						flag = false;
					} else {
						pile.put("pileGbProtocol", exist);
					}
				} 
				pile.put("gbProtocol", String.valueOf(obj[17]));
				if (obj[18] == null || "".equals(obj[18])) {
					if ("".equals(desc)) {
						desc = "校验失败:屏显二维码不能为空";
					}
					flag = false;
				} else {
					if("是".equals(obj[18].toString())){
						pile.put("isQrcode",1);
					}else if("否".equals(obj[18].toString())){
						pile.put("isQrcode",0);
					}
				}
				pile.put("isCode", String.valueOf(obj[18]));
				if (!flag) {
					pile.put(DESCRIPTION, desc);
					flags = false;
				} else {
					pile.put(DESCRIPTION, "检验成功");
				}

				//交直模式兼容充电桩类型
				DataVo dataVo = new DataVo(pile);
				ortModeToPileType(dataVo);

				pileList.add(dataVo);
			}
		}
		boolean baseFalg = false;
		if (flags) {
			baseFalg = true;
			Map<String,Map> pileAddrsMap = getVarsMap(pileAddrs,"pileAddrs","pileAddr");//通讯地址
			Map<String,Map> pileNosMap = getVarsMap(pileNos,"pileNos","pileNo");//充电桩编号
			if (pileList.size() > 0) {
				for (Map pile : pileList) {
					DataVo pileVo = new DataVo(pile);
					Map pileAddrMap = null;
					Map pileNoMap   = null;
					if(null!=pileAddrsMap){
						pileAddrMap = pileAddrsMap.get(pileVo.getString("pileAddr"));
					}
					if(null!=pileNosMap){
						pileNoMap = pileNosMap.get(pileVo.getString("pileNo"));
					}
					if (null==pileNoMap && null == pileAddrMap) {//两者都不存在，进行添加操作
						pile.put("pileStatus",0);//0---有效
						saveList.add(pile);
					} else if(null == pileNoMap && null != pileAddrMap){//充电桩编号不存在，通讯地址存在，校验失败.
						baseFalg = false;
						pile.put(DESCRIPTION,"校验失败:通讯地址已存在");
					}else if(null != pileNoMap && null == pileAddrMap){//充电桩编号存在，通讯地址不存在，进行更新操作
						updateList.add(pile);
						if(!pileNoMap.get("ortMode").equals(pile.get("ortMode")) && !pileNoMap.get("numberGun").equals(pile.get("numberGun"))){
							delList.add(Integer.parseInt(String.valueOf(pileNoMap.get("pileId"))));
						}
					}else if(null != pileNoMap && null != pileAddrMap){//两者都存在，
						DataVo dataVo = new DataVo();
						dataVo.put("pileId", Integer.parseInt(pileAddrMap.get("pileId").toString()));
						dataVo.put("pileNo", pileVo.getString("pileNo"));
						dataVo.put("pileAddr", pileVo.getString("pileAddr"));
						if(pileMapper.getPileExistByVar(dataVo) <= 0){
							updateList.add(pile);
							if(!pileNoMap.get("ortMode").equals(pile.get("ortMode")) && !pileNoMap.get("numberGun").equals(pile.get("numberGun"))){
								delList.add(Integer.parseInt(String.valueOf(pileNoMap.get("pileId"))));
							}
						}else{
							baseFalg = false;
							pile.put(DESCRIPTION,"校验失败:充电桩编码&通讯地址已存在");
						}
						
					}
				}
			}
		}
		if(baseFalg){
			if (null != saveList && saveList.size() > 0) {
				//保存
				Map addMap = new HashMap();
				addMap.put("piles", saveList);
				pileMapper.insertPiles(addMap);
			}
			if (null != updateList && updateList.size() > 0) {
				//更新
				Map updateMap = new HashMap();
				updateMap.put("piles", updateList);
				pileMapper.batchUpdatePiles(updateMap);
			}
            if (null != delList && delList.size() > 0) {//删除表计和枪信息
            	DataVo dataVo = new DataVo();
            	dataVo.put("pileIds",delList);
            	pileMapper.deleteMeterByPileId(dataVo);
            	gunMapper.deleteGunByPileId(dataVo);
			}
		}else {
			if (pileList.size() > 0) {
				for (Map pile : pileList) {
					pile.put("impStatus", "导入失败!");
				}
			}
			result = pileList;
		}
		return result;
	}

	/**
	 * 获取所有的有效场站
	 * gaohui 
	 * 2017-5-8
	 */
	private Map<String, DataVo> getStationsMap(Set<Integer> stationIds) {
		Map<String, DataVo> stationMap = null;
		DataVo dataVo = new DataVo();
		if(null!=stationIds && stationIds.size()>0){
			dataVo.put("stationIds", stationIds);// 场站权限
		}
		dataVo.put("useStatus", 0);// 查询有效的场站,1的是无效的场站
		List<Map> stations = stationMapper.getStationAll(dataVo);
		if(null!=stations && stations.size()>0){
			stationMap = new HashMap<String,DataVo>();
			for (Map map : stations) {
				DataVo station = new DataVo(map);
				stationMap.put(station.getString("stationName"), station);//场站名称必须唯一.
			}
		}
		return stationMap;
	}
	
	private Map<String, Map> getVarsMap(List<String> varList,String var,String key){
		Map<String, Map> result = null;
		if(null!=varList && varList.size()>0){
			Map data  = new HashMap();
			data.put(var, varList);
			List<Map> list = pileMapper.getPilesByVars(data);
			if(null!=list && list.size()>0){
				result = new HashMap<String, Map>();
				for(Map map:list){
					result.put((String) map.get(key), map);
				}
			}
		}
		return result;
	}
	/**
	 * 设备名称或设备编号业务字典
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月17日
	 */
	public List<Map> getPileNoAndNameDict(Map map) throws BizException{
		DataVo params = new DataVo(map);
		if (params.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}else{
			Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
			if(orgIds !=null){
				params.put("orgIds", orgIds);
			}
			Set<Integer> stationIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
			if(stationIds !=null){
				params.put("stationIds", stationIds);
			}
		}
		return pileMapper.getPileNoAndNameDict(params);
	}
	
	/**
	 * 根据枪数和交直模式查询 出线表信息
	 */
	public Map<String, Map> getGunNoInnerIds(Map map){
		Map<String, Map> meterMap = new HashMap<String, Map>();
		DataVo vo = new DataVo(map);
		if(vo.isBlank("numberGun") || vo.isBlank("ortMode")){
			return null;
		}
		Map<String, Map> meterVo = getMeterMap(vo.getInt("numberGun"),vo.getInt("ortMode"));
		meterMap.put("meter", meterVo);
		return meterMap;
	}
	
	
	private Map<String,Map> getMeterMap(Integer numberGun,Integer ortMode){
		Map<String, Integer> gunInnerId = PileTypeUtils.getNumberGun(numberGun, ortMode);
		
		Map meter = null, gunVo = null;
		
		Map<String, Map> meterVo = new TreeMap();
		
		for (String key : gunInnerId.keySet()) {
			String msg = "";
			meter = new HashMap();
			if("Z".equals(key)){
				msg = "总表";
			}else{
				msg = key+"枪";
				gunVo = new DataVo();
				gunVo.put("gumPoint", "充电枪"+key);
				meter.put("gun", gunVo);
			}
			meter.put("meterName", msg);
			meter.put("innerId", gunInnerId.get(key));
			meter.put("key", key);
			meterVo.put("meter"+key, meter);
			
		}
		return meterVo;
	}
	
}

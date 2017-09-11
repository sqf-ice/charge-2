package com.clouyun.charge.modules.document.service;

import com.clou.common.utils.ObjectUtils;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.cdz.CdzBusi;
import com.clouyun.cdz.CdzBusiClou;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ConnectorIdUtil;
import com.clouyun.charge.common.utils.PileTypeUtils;
import com.clouyun.charge.common.utils.QRCodeUtil;
import com.clouyun.charge.modules.document.mapper.QrCodeMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月25日 下午2:18:41
 */
@Service
@Transactional(rollbackFor = BizException.class)
public class QrCodeService extends BusinessService{

	public static final Logger logger = LoggerFactory.getLogger(QrCodeService.class);

	@Autowired
	private QrCodeMapper codeMapper;

	@Autowired
	private DictService dictService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CdzBusi cdzBusi;
	@Autowired
	private CdzBusiClou cdzBusiClou;

	private String  canSave = "true";
	private final String KL_PILE_PROTOCOL = "62";
	private final String SZ_PILE_PROTOCOL = "51";


	/*
	 * 查询二维码管理信息
	 */
	@SuppressWarnings("rawtypes")
	public Map selectByPrimaryKey(Integer pileId) throws BizException {
		Map result = codeMapper.selectByPrimaryKey(pileId);
		DataVo vo = new DataVo(result);
		if(result == null || result.size() < 0){
			throw new BizException(1102014);
		}

		//获取枪信息
		List list = new ArrayList();
		list.add(pileId);
		List<Map> chgGunMap = codeMapper.selectChgGunByPileId(list);

		Map map = new HashMap();
		List chgGunInfoList = new ArrayList();
		if(chgGunMap != null && chgGunMap.size() > 0){
			for (Map map2 : chgGunMap) {
				Map chgGunInfoMap = new HashMap();
				chgGunInfoMap.put("qrCode", map2.get("qrCode"));
				chgGunInfoMap.put("chgGunId", map2.get("gunId"));
				chgGunInfoMap.put("gumPoint", map2.get("gumPoint"));
				chgGunInfoMap.put("innerId", map2.get("innerId"));
				chgGunInfoList.add(chgGunInfoMap);
			}
		}
		map.put("chgGunInfo",chgGunInfoList);
		map.putAll(result);

		vo = null;
		chgGunMap = null;

		return map;
	}

	/*
	 * 查询二维码管理列表
	 */
	public PageInfo selectAll(Map map) throws BizException {
		DataVo params = new DataVo(map);
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		getPermission(params);
		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(params);
		}
		List<Map> all = queryResult(params);
		PageInfo page = new PageInfo(all);

		map = null;
		params = null;
		all = null;

		return page;
	}

	private List<Map> queryResult(DataVo map) {
		check(map);
		//查询支持屏显的二维码
		map.put("isQrCode", 1);
		List<Map> all = codeMapper.selectAll(map);
		// 结果集处理
		handleResult(all);
		return all;
	}

	// 结果集处理
	private void handleResult(List<Map> all) {
		if (all.size() > 0) {
			// 将桩Id封装查询枪信息
			Set<Integer> set = new HashSet<>();
			for (Map map2 : all) {
				if (map2.get("pileId") != null){
					set.add(Integer.parseInt(map2.get("pileId").toString()));
				}
			}

			//获取枪信息
			List<Integer> list = new ArrayList();
			list.addAll(set);
			List<Map> allGun = codeMapper.selectChgGunByPileId(list);
			// 将枪信息封装入Map返回
			for (Map pileMap : all) {
				for (Map gunMap : allGun) {
					if (pileMap.get("pileId").equals(gunMap.get("pileId"))) {
						if ("充电枪01".equals(gunMap.get("gumPoint"))) {
							pileMap.put("aQrCode", gunMap.get("qrCode").toString());
							pileMap.put("aGumPoint", gunMap.get("gumPoint").toString());
							pileMap.put("aGunId", gunMap.get("gunId").toString());
							pileMap.put("aInnerId", gunMap.get("innerId").toString());
							pileMap.put("aConnectorId",gunMap.get("connectorId").toString());
						}
						if ("充电枪02".equals(gunMap.get("gumPoint"))) {
							pileMap.put("bQrCode", gunMap.get("qrCode").toString());
							pileMap.put("bGumPoint", gunMap.get("gumPoint").toString());
							pileMap.put("bGunId", gunMap.get("gunId").toString());
							pileMap.put("bInnerId", gunMap.get("innerId").toString());
							pileMap.put("bConnectorId",gunMap.get("connectorId").toString());
						}
					}
				}
			}
			allGun = null;
		}
	}

	private List<Map> queryGunInfoResult(DataVo map) {
		check(map);
		//查询支持屏显的二维码
		map.put("isQrCode", 1);
		List<Map> all = codeMapper.selectAll(map);
		// 结果集处理
		handleGunInfoResult(all);
		return all;
	}

	// 结果集处理
	private void handleGunInfoResult(List<Map> all) {
		if (all.size() > 0) {
			// 将桩Id封装查询枪信息
			Set<Integer> set = new HashSet<>();
			for (Map map2 : all) {
				if(map2.get("pileId") != null){
					set.add(Integer.parseInt(map2.get("pileId").toString()));
				}
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


	/*
	 * 参数检查
	 */
	private void check(DataVo map) {
		// 异常字符校验
		String partten = "[A-Za-z0-9\u4E00-\u9FA5]*";
		// 按照场站名称模糊查询
		if (map.isNotBlank("stationName")) {
			if (!Pattern.matches(partten, map.get("stationName").toString())) {
				map.put("stationName", "-999");
			}
		}

		// 按照运营商名称模糊查询
		if (map.isNotBlank("orgName")) {
			if (!Pattern.matches(partten, map.get("orgName").toString())) {
				map.put("orgName", "-999");
			}
		}
	}

	/*
	 * 下发二维码
	 */
	public int issuedCode(Map map) throws BizException {
		DataVo vo = new DataVo(map);

		// 更新或者新增二维码并更新桩状态
		Map pileInfo = updateOrSaveQRCode(vo);
		List<Integer> pileIds = new ArrayList<>();
		pileIds.add(vo.getInt("pileId"));
		vo.put("list", pileIds);
		int count = 0;
		try {
			List<Map> chgGunInfo = (List<Map>) vo.get("chgGunInfo");
			if (chgGunInfo != null && chgGunInfo.size() > 0){
				issuedCodeService(chgGunInfo,pileInfo);
			}

			// 设置下发状态为已下发
			vo.put("qrCodeState", 1);
			count = codeMapper.updatePileCodeState(vo);
		} catch (Exception e) {
			// 设置下发状态为下发失败
			vo.put("qrCodeState", 2);
			e.printStackTrace();
			// 设置桩的二维码状态和互联互通二维码状态为未下发
			count = codeMapper.updatePileCodeState(vo);
			throw new BizException(1102026,e.getMessage());
		}

		//codeMapper.updateHLHTCodeState(map);
		return count;
	}
	/*
	 * 下发互联互通二维码
	 */
	public int hlhtIssuedCode(Map map) throws BizException {
		DataVo vo = new DataVo(map);

		// 更新或者新增二维码并更新桩状态
		Map chgPileMap = updateOrSaveQRCode(vo);

		if(chgPileMap.get("pileProtocol") == null || SZ_PILE_PROTOCOL.equals(chgPileMap.get("pileProtocol").toString())){
			throw new BizException(1102025);
		}

		//获取拼接互联互通二维码
		List<Map> hthlCodeList = getHTHLCode(chgPileMap);
		List list = new ArrayList();
		list.add(vo.getInt("pileId"));
		vo.put("list",list);

		int count = 0;
		try {
			if (hthlCodeList != null && hthlCodeList.size() > 0){
				issuedCodeService(hthlCodeList,chgPileMap);
			}
			// 设置下发状态为已下发
			vo.put("hlhtQRCodeState", 1);
			count = codeMapper.updateHLHTCodeState(vo);
		} catch (Exception e) {
			// 设置下发状态为下发失败
			vo.put("hlhtQRCodeState", 2);
			e.printStackTrace();
			// 设置桩的二维码状态和互联互通二维码状态为未下发
			count = codeMapper.updateHLHTCodeState(vo);
			throw new BizException(1102026,e.getMessage());
		}
		return count;
	}

	/*
	 * 拼接互联互通二维码
	 */
	private List<Map> getHTHLCode(Map map) {

		List<Map> hlhtList = new ArrayList<>();
		if (map.size() > 0) {
			// 组织机构代码
			String orgCode = "";
			if (null != map.get("orgCode")) {
				orgCode = map.get("orgCode").toString();
			}
			//获取枪信息
			List list = new ArrayList();
			list.add(Integer.parseInt(map.get("pileId").toString()));
			List<Map> chgGunMap = codeMapper.selectChgGunByPileId(list);

			if(chgGunMap.size() > 0){
				for (Map map3 : chgGunMap) {
					StringBuffer qrCodeOne = new StringBuffer(128);//充电A枪互联互通二维码
					// 自定义部分
					String customA = map3.get("qrCode").toString().replaceAll("_", "");
					qrCodeOne.append("hlht://").append(map3.get("connectorId")).append(".").append(orgCode)
							 .append("/").append(customA);
					map3.put("qrCode", qrCodeOne.toString());
					hlhtList.add(map3);
				}
			}
		}
		return hlhtList;
	}

	@SuppressWarnings("rawtypes")
	public Map updateOrSaveQRCode(DataVo vo) throws BizException {
		List<Map> list = new ArrayList();
		// 参数检查
		Map omap = checkData(vo, list);

		int numberGun = vo.getInt("numberGun");
		int ortMode = vo.getInt("ortMode");
		Map<String, Integer> gunInnerMap = PileTypeUtils.getGunInnerId(numberGun, ortMode);

		List<Map> chgGunInfo = (List<Map>) vo.get("chgGunInfo");
		if (chgGunInfo != null && chgGunInfo.size() > 0){
			List<Map> chgMeterList = new ArrayList<Map>();
			List<Map> chgGunList = new ArrayList<Map>();
			boolean whether = false;
			for (Map map: chgGunInfo) {
				if (map.get("chgGunId") != null){
					whether = true;
				}else{
					// 保存数据转义
					if (vo.isNotBlank("pileId")) {
						String gumPoint = map.get("gumPoint").toString();
						String letter = gumPoint.substring(gumPoint.length()-2, gumPoint.length());

						Map<String, Object> saveMap = new HashMap<>();
						saveMap.put("pileId", vo.get("pileId"));
						saveMap.put("innerId", gunInnerMap.get(letter));
						saveMap.put("meterName", letter + "枪出线表");
						saveMap.put("gumPoint", "充电枪" + letter);

						if (null != map.get("qrCode")) {
							saveMap.put("qrCode", map.get("qrCode"));
						}

						List<Map> connectorList = putConnectorMap(omap ,"充电枪" + letter ,gunInnerMap.get(letter));
						//生成connectorId
						Map<String, String> connector = ConnectorIdUtil.newGetConnector(connectorList);
						saveMap.put("connectorId", connector.get("充电枪" + letter));
						chgMeterList.add(saveMap);
						chgGunList.add(saveMap);

						connectorList = null;
						connector = null;
					}
				}
			}

			if (whether){
				//更新枪二维码
				codeMapper.updateGumPointQRCode(list);
				saveLog("更新二维码", OperateType.update.OperateId,"充电桩:"+vo.getString("pileName")+"的充电枪二维码修改或下发",vo.getInt("userId"));
			}

			if (chgMeterList != null && chgMeterList.size() > 0) {
				//直流添加总表
				if(2 == ortMode){
					Map<String, Object> saveMap = new HashMap<>();
					saveMap.put("pileId", vo.get("pileId"));
					saveMap.put("innerId", gunInnerMap.get("Z"));
					saveMap.put("meterName", "总表");
					chgMeterList.add(saveMap);
				}
				//新增表计
				codeMapper.inserChgMeter(chgMeterList);
				saveLog("表计新增",OperateType.add.OperateId,"充电桩:"+vo.getString("pileName")+"表计新增",vo.getInt("userId"));
			}
			if (chgGunList != null && chgGunList.size() > 0) {
				for (int i = 0; i < chgGunList.size(); i++) {
					// 总表不插枪
					if (chgGunList.get(i).get("gumPoint") == null) {
						chgGunList.remove(i);
					}
				}
				//新增枪
				codeMapper.inserChgGun(chgGunList);
				saveLog("充电枪新增",OperateType.add.OperateId,"充电桩:"+vo.getString("pileName")+"充电枪新增",vo.getInt("userId"));
			}

			chgGunList = null;
			chgMeterList = null;
		}
		return omap;
	}

	@SuppressWarnings("rawtypes")
	private Map checkData(DataVo vo, List list) throws BizException {
		// 桩id
		Integer pileId = vo.getInt("pileId");
		Map chgPileMap = codeMapper.selectByPrimaryKey(pileId);

		List<Map> chgGunInfoList = (List<Map>) vo.get("chgGunInfo");
		if (chgGunInfoList == null || chgGunInfoList.size() <= 0) {
			throw new BizException(1102001, "二维码");
		}

		int numberGun = vo.getInt("numberGun");

		if (numberGun > 0 && chgGunInfoList.size() < numberGun) {
			throw new BizException(1102003, "充电枪二维码");
		}

		//空值已抛异常,不用判断非空
		for (Map infoMap: chgGunInfoList) {
			//二维码唯一验证
			Map dataMap = new HashMap();
			dataMap.put("qrCode", infoMap.get("qrCode"));
			dataMap.put("chgGunId", infoMap.get("chgGunId"));
			dataMap.put("gumPoint", infoMap.get("gumPoint"));
			dataMap.put("pileId",pileId);
			Integer count = codeMapper.QRCodeCount(dataMap);
			if (count > 0) {
				throw new BizException(1102003, infoMap.get("gumPoint"));
			}
			list.add(dataMap);
		}

		// 根据桩Id获取枪信息
		List pileIdlist = new ArrayList();
		pileIdlist.add(pileId);
		List<Map> chgGunMap = codeMapper.selectChgGunByPileId(pileIdlist);
		//获取下发状态
		String qrCodeState = chgPileMap.get("qrCodeState").toString();
		String hlhtQrcodeState = chgPileMap.get("hlhtQrcodeState").toString();
		// 普通二维码下发成功未修改不可再次下发
		// 互联互通二维码下发成功未修改不可再次下发
		if ("1".equals(qrCodeState) || "1".equals(hlhtQrcodeState)) {
			if (chgGunMap != null && chgGunMap.size() > 0) {
				for (Map chgGun : chgGunMap) {
					for (Map infoMap: chgGunInfoList) {
						if (null != infoMap.get("qrCode") && infoMap.get("qrCode").equals(chgGun.get("qrCode"))) {
							throw new BizException(1102004);
						}
					}
				}
			}
		}
		return chgPileMap;
	}


	private List<Map> putConnectorMap(Map<String, String> c,
			String ret, Integer value) {
		List<Map> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("pileNo", c.get("pileNo"));
		map.put("stationNo", c.get("stationNo"));
		map.put("orgNo", c.get("orgNo"));
		map.put("gumPoint", ret);
		map.put("innerId", value);
		list.add(map);
		return list;
	}

	/*
	 * 批量下发二维码
	 */
	public List<Map> batchIssued(List pileIds) throws BizException{

		if(pileIds == null || pileIds.size() <= 0){
			throw new BizException(1102005);
		}

		List<Map> list = new ArrayList<>();
		//桩
		List<Map> pileList = codeMapper.selectByListKey(pileIds);
		//枪
		List<Map> chgGuns = codeMapper.selectChgGunByPileId(pileIds);

		Map<Integer,Map>  allPileMap = new HashMap<>();
		for (Map map : pileList) {
			allPileMap.put(Integer.parseInt(map.get("pileId").toString()), map);
		}

		Map<Integer,List<Map>> chgGunInfo = new HashMap<>();
		//chgGunInfo信息
		putChgGunInfo(chgGuns, chgGunInfo);

		List<Integer> codeSuccessId = new ArrayList<>();
		List<Integer> codeErrorId = new ArrayList<>();

		for (int i = 0; i < pileIds.size(); i++) {
			//转义
			Integer pileId = Integer.parseInt(pileIds.get(i).toString());
			Map allInfo = allPileMap.get(pileId);
			String pileName = allInfo.get("pileName").toString();
			String pileNo = allInfo.get("pileNo").toString();
			try {
				List<Map> chgGunMap = chgGunInfo.get(pileId);
				if(chgGunMap != null && chgGunMap.size() > 0){
					String codeState = "-99";
					String hlhtCodeState = "-99";

					if (allInfo.get("qrCodeState") != null){
						codeState = allInfo.get("qrCodeState").toString();
					}
					if (allInfo.get("hlhtQrcodeState") != null){
						hlhtCodeState = allInfo.get("hlhtQrcodeState").toString();
					}

					for (Map map : chgGunMap) {
						if(("".equals(map.get("qrCode")) || null == map.get("qrCode"))){
							String letter = "";
							if (map.get("gumPoint") != null){
								String gumPoint = map.get("gumPoint").toString();
								letter = gumPoint.substring(gumPoint.length() - 1, gumPoint.length());
							}
							throw new BizException(1102018,letter);
						}
						//1-->表示下发成功,0-->未下发,2-->下发失败
						//普通二维码下发成功未修改不可再次下发
						if("1".equals(codeState) || "1".equals(hlhtCodeState)){
							if(null != map.get("qrCode") && map.get("qrCode").equals(map.get("qrCode"))){
								throw new BizException(1102004);
							}
						}
					}
				}else{
					throw new BizException(1102019);
				}

				// 批量下发二维码
				try {
					if (null != chgGunMap && chgGunMap.size() > 0) {
						issuedCodeService(chgGunMap,allInfo);
					}
					codeSuccessId.add(pileId);
				} catch (Exception e) {
					// 下发过程中有异常跳过,记录错误信息
					Map<String, String> map = putErrorMsg(pileName, pileNo, e);
					list.add(map);
					codeErrorId.add(pileId);
					//跳过当前循环
					continue;
				}
			}catch (Exception e) {
				// 下发过程中有异常跳过,记录错误信息
				Map<String, String> map = putErrorMsg(pileName, pileNo, e);
				list.add(map);
				//跳过当前循环
				continue;
			}
		}

		if(codeSuccessId != null && codeSuccessId.size() > 0){
			Map map = new HashMap();
			map.put("list", codeSuccessId);
			map.put("qrCodeState", 1);
			codeMapper.updatePileCodeState(map);
		}

		if(codeErrorId != null && codeErrorId.size() > 0){
			Map map = new HashMap();
			map.put("list", codeErrorId);
			map.put("qrCodeState", 2);
			codeMapper.updatePileCodeState(map);
		}

		pileIds = null;
		pileList = null;
		chgGuns = null;
		allPileMap = null;
		chgGunInfo = null;
		codeSuccessId = null;
		codeErrorId = null;
		return list;
	}

	private Map<String, String> putErrorMsg(String pileName, String pileNo, Exception e) {
		Map<String,String> map = new HashMap<>();
		map.put("pileName", pileName);
		map.put("msg", e.getMessage());
		map.put("pileNo", pileNo);
		return map;
	}

	/*
	 * 互联互通批量下发
	 */
	public List<Map> hlhtBatchIssued(List pileIds) throws BizException{

		if(pileIds == null || pileIds.size() <= 0){
			throw new BizException(1102005);
		}
		List<Map> list = new ArrayList<>();
		//桩
		List<Map> pileList = codeMapper.selectByListKey(pileIds);
		//枪
		List<Map> chgGuns = codeMapper.selectChgGunByPileId(pileIds);

		Map<Integer,Map>  allPileMap = new HashMap<>();
		for (Map map : pileList) {
			allPileMap.put(Integer.parseInt(map.get("pileId").toString()), map);
		}

		Map<Integer,List<Map>> chgGunInfo = new HashMap<>();
		putChgGunInfo(chgGuns, chgGunInfo);


		List<Integer> codeSuccessId = new ArrayList<>();
		List<Integer> codeErrorId = new ArrayList<>();
		Map allInfo = new HashMap();
		for (int i = 0; i < pileIds.size(); i++) {
			//转义
			Integer pileId = Integer.parseInt(pileIds.get(i).toString());
			allInfo = allPileMap.get(pileId);
			String pileName = allInfo.get("pileName").toString();
			String pileNo = allInfo.get("pileNo").toString();
			try {
				Object pileProtocol = allInfo.get("pileProtocol");
				if(pileProtocol == null || SZ_PILE_PROTOCOL.equals(pileProtocol.toString())){
					throw new BizException(1102025);
				}

				List<Map> chgGunMap = chgGunInfo.get(pileId);
				if(chgGunMap != null && chgGunMap.size() > 0){
					Integer qrCodeState = Integer.parseInt(allInfo.get("qrCodeState").toString());
					Integer hlhtQrcodeState = Integer.parseInt(allInfo.get("hlhtQrcodeState").toString());

					for (Map map : chgGunMap) {
						if(("".equals(map.get("qrCode")) || null == map.get("qrCode"))){
							String gumPoint = map.get("gumPoint").toString();
							String letter = gumPoint.substring(gumPoint.length() - 1, gumPoint.length());
							throw new BizException(1102018,letter);
						}

						//1-->表示下发成功,0-->未下发,2-->下发失败
						//普通二维码下发成功未修改不可再次下发
						if("1".equals(qrCodeState.toString()) || "1".equals(hlhtQrcodeState.toString())){
							if(null != map.get("qrCode") && map.get("qrCode").equals(map.get("qrCode"))){
								throw new BizException(1102004);
							}
						}
					}
				}else{
					throw new BizException(1102019);
				}
				// 批量下发二维码
				try {
					//获取拼接互联互通二维码
					// 生成互联互通二维码
					List<Map> hthlCodeList = getHTHLCode(allInfo);

					if (null != hthlCodeList && hthlCodeList.size() > 0) {
						issuedCodeService(hthlCodeList,allInfo);
					}
					codeSuccessId.add(pileId);
					hthlCodeList = null;
				} catch (Exception e) {
					// 下发过程中有异常跳过,记录错误信息
					Map<String, String> map = putErrorMsg(pileName, pileNo, e);
					list.add(map);

					codeErrorId.add(pileId);
					//跳过当前循环
					continue;
				}
			}catch (Exception e) {
				// 下发过程中有异常跳过,记录错误信息
				Map<String, String> map = putErrorMsg(pileName, pileNo, e);
				list.add(map);
				//跳过当前循环
				continue;
			}
		}

		if(codeSuccessId != null && codeSuccessId.size() > 0){
			Map map = new HashMap();
			map.put("list", codeSuccessId);
			map.put("hlhtQRCodeState", 1);
			codeMapper.updateHLHTCodeState(map);
		}

		if(codeErrorId != null && codeErrorId.size() > 0){
			Map map = new HashMap();
			map.put("list", codeErrorId);
			map.put("hlhtQRCodeState", 2);
			codeMapper.updateHLHTCodeState(map);
		}
		pileIds = null;
		pileList = null;
		chgGuns = null;
		allPileMap = null;
		chgGunInfo = null;
		codeSuccessId = null;
		codeErrorId = null;
		return list;
	}

	private void putChgGunInfo(List<Map> chgGuns, Map<Integer, List<Map>> chgGunInfo) {
		for (Map map : chgGuns) {
			Object pileId = map.get("pileId");
			if(null != pileId && !"".equals(pileId)){
				if(chgGunInfo.get(Integer.parseInt(pileId.toString())) != null){
					int id = Integer.parseInt(pileId.toString());
					List<Map> chgGunList = chgGunInfo.get(id);
					chgGunList.add(map);
					chgGunInfo.put(id, chgGunList);
				}else{
					List<Map> chgGunList = new ArrayList<>();
					chgGunList.add(map);
					chgGunInfo.put(Integer.parseInt(pileId.toString()), chgGunList);
				}
			}
		}
	}

	private void issuedCodeService(List<Map> chgGunMap,Map pileInfo)throws Exception {
		DataVo pileVo = new DataVo(pileInfo);
		Integer pileId = pileVo.getInt("pileId");
		String pileAddr = pileVo.getString("pileAddr");
		for (Map gunInfo: chgGunMap) {
			DataVo vo = new DataVo(gunInfo);
			int innerId = vo.getInt("innerId");
			String qrCode = vo.getString("qrCode");
			String pileProtocol = pileVo.getString("pileProtocol");

			// 调用接口下发二维码
			if(KL_PILE_PROTOCOL.equals(pileProtocol)){
				// 调用接口下发二维码
				cdzBusiClou.sendQRCode(pileId, pileAddr,innerId,qrCode);
			}
			if(SZ_PILE_PROTOCOL.equals(pileProtocol)){
				// 调用接口下发二维码
				cdzBusi.sendQRCodeGe(pileId, pileAddr,innerId,qrCode);
			}
        }
	}

	/*
	 * 二维码导出
	 */
	public void export(Map map, HttpServletResponse response, HttpServletRequest request) throws Exception{
		DataVo params = new DataVo(map);
		getPermission(params);
		Set<Integer> userRoleDataById;


//		List<Map> all = queryResult(map);
		List<Map> all = queryGunInfoResult(params);
		exportExcel(all,response,request);
		params = null;
		all = null;
		userRoleDataById = null;
	}

	private void getPermission(DataVo params) throws BizException {
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId")){
            throw new BizException(1000006);
        }
		// 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		if(userRoleDataById != null && userRoleDataById.size() > 0){
            params.put("stationIds", userRoleDataById);
        }
	}

	public void exportExcel(List<Map> list,HttpServletResponse response, HttpServletRequest request) throws Exception{

		QRCodeUtil qrc = new QRCodeUtil();

		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("充电桩档案表");

		HSSFCellStyle style = wb.createCellStyle(); // 样式对象

        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平    

		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //生成一个字体
        HSSFFont font=wb.createFont();
        font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short)15);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);         //字体增粗
        //把字体应用到当前的样式
        style.setFont(font);

        int columnLenth = 24;

		for (int i = 0; i <= columnLenth ; i++) {
			if (i <= 3){
				sheet.setColumnWidth(i, 6000);
			}else if ((i & 1) != 0){
				sheet.setColumnWidth(i, 10000);
			}else{
				sheet.setColumnWidth(i, 14000);
			}
		}

		HSSFRow row2 = sheet.createRow(0);

		// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        row2.setHeightInPoints(50);

        List headList = new ArrayList();

		headList.add("场站编号");
		headList.add("场站名");
		headList.add("充电桩编号");
		headList.add("充电桩名");
		headList.add("01枪源码");
		headList.add("01枪互联互通二维码");
		headList.add("02枪源码");
		headList.add("02枪互联互通二维码");
		headList.add("03枪源码");
		headList.add("03枪互联互通二维码");
		headList.add("04枪源码");
		headList.add("04枪互联互通二维码");
		headList.add("05枪源码");
		headList.add("05枪互联互通二维码");
		headList.add("06枪源码");
		headList.add("06互联互通二维码");
		headList.add("07枪源码");
		headList.add("07枪互联互通二维码");
		headList.add("08枪源码");
		headList.add("08枪互联互通二维码");
		headList.add("09枪源码");
		headList.add("09枪互联互通二维码");
		headList.add("10枪源码");
		headList.add("10枪互联互通二维码");


		for (int i = 0; i < headList.size(); i++) {
			HSSFCell cell0 = row2.createCell(i);
			cell0.setCellValue(headList.get(i).toString());
			cell0.setCellStyle(style);
		}

		for (int i = 0; i < list.size(); i++) {

			Map map = list.get(i);

			// 在sheet里创建第三行...
			HSSFRow rowrow = sheet.createRow(i+1);
			rowrow.setHeight( (short) 3000);

			if(null != map.get("stationNo")){
				rowrow.createCell(0).setCellValue(map.get("stationNo").toString());
			}else{
				rowrow.createCell(0).setCellValue("");
			}

			if(null != map.get("stationName")){
				rowrow.createCell(1).setCellValue(map.get("stationName").toString());
			}else{
				rowrow.createCell(1).setCellValue("");
			}

			if(null != map.get("pileNo")){
				rowrow.createCell(2).setCellValue(map.get("pileNo").toString());
			}else{
				rowrow.createCell(2).setCellValue("");
			}

			if(null != map.get("pileName")){
				rowrow.createCell(3).setCellValue(map.get("pileName").toString());
			}else{
				rowrow.createCell(3).setCellValue("");
			}

			for (int j = 4; j < headList.size(); j++) {
				rowrow.createCell(j).setCellValue("");
			}

			DataVo vo = new DataVo(map);
			int numberGun = vo.getInt("numberGun");
			int ortMode = vo.getInt("ortMode");
			if(null != map && map.size() > 0){
				// 组织机构代码
				String orgCode = "";
				//组织机构代码
				if (null != map.get("orgCode")) {
					orgCode = map.get("orgCode").toString();
				}

				Map<String, Integer> gunNoMap = PileTypeUtils.getNumberGun(numberGun, ortMode);

				List<Map> gunInfoList = (List<Map>) map.get("gunInfo");

				int arr = 5;
				int brr = 6;
				int crr = 4;
				for (String key : gunNoMap.keySet()) {
					if("Z".equals(key)){
						continue;
					}
					for (int j = 0; j < gunInfoList.size(); j++) {
						Map gun = gunInfoList.get(j);
						if(gun.get("gumPoint") != null && gun.get("qrCode") != null){
							String gumPoint = gun.get("gumPoint").toString();
							if(("充电枪"+key).equals(gumPoint)){
								StringBuffer qrCodeOne = new StringBuffer(128);
								if (j != 0){
									arr += 2;
									brr += 2;
									crr += 2;
								}
								// 自定义部分
								String customA = gun.get("qrCode").toString().replaceAll("_", "");

								qrCodeOne.append("hlht://").append(gun.get("connectorId")).append(".").append(orgCode)
										.append("/").append(customA);
								rowrow.createCell(crr).setCellValue(qrCodeOne.toString());

								//生成二维码
								BufferedImage bi=null;
								if(null != qrCodeOne.toString() && !"".equals(qrCodeOne.toString())){
									try {
										bi = qrc.ExportImage(qrCodeOne.toString());
									} catch (Exception e) {
										e.printStackTrace();
									}
									ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
									ImageIO.write(bi, "jpg", byteArrayOut);
									// anchor主要用于设置图片的属性

									HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) arr, i+1, (short) brr,i+2);
									// 插入图片
									patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
								}
							}
						}
					}
				}
			}
		}
		// 输出Excel文件
		String as = "二维码档案.xls";
        String fileName = as;// = Java.NET.URLEncoder.encode(as, "UTF-8");
        /*根据request的locale 得出可能的编码，中文操作系统通常是gb2312*/
        fileName = new String(as.getBytes("GB2312"), "ISO_8859_1");
        as = fileName;
        OutputStream output = null;
		try {
			output = response.getOutputStream();
			response.reset();

			response.setHeader("Content-disposition","attachment; filename="+ URLDecoder.decode(URLEncoder.encode(as,"UTF-8"), "UTF-8"));
			response.setContentType("application/msexcel;charset=UTF-8");

			wb.write(output);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (output != null){
				output.close();
			}

			qrc = null;
			wb = null;
			sheet = null;
			style = null;
			font = null;
			row2 = null;
			patriarch = null;
			headList = null;
		}
		//档案压缩下载
//		ExportZipUtils.export(request,response,wb);
	}

	public ResultVo importQrCodes(MultipartFile excelFile, Map data) throws Exception{
		ResultVo vo = new ResultVo();
		DataVo params = new DataVo(data);
		// 此处应根据登录用户ID获取到能查看的企业，未实现
        if (params.isBlank("userId")){
        	throw new BizException(1000006);
        }

		List<Map<String, String>> allPiles = new ArrayList<>();
		canSave = "true";
		//解析文件
		List<Object[]> arraylist = parseExcel(excelFile);
		List<String> nos = new ArrayList<String>();
		//查出所有的枪
		List<Map> chgGuns = codeMapper.selectChgGunByPileId(null);
		//获取可以查看的场站
		Set<Integer> stationIds =userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		//查询所有符合条件的桩信息
		//查询支持屏显的二维码
		List<Map> allPileInfo = codeMapper.selectAll(new HashMap());
		Map<String,Map> pileInfo = new HashMap();
		for (Map map2 : allPileInfo) {
			pileInfo.put(map2.get("pileNo").toString(), map2);
		}

		List<String> validateCodeList = new ArrayList();

		for (Object[] obj : arraylist) {

			//根据导入的充电桩编号得到充电桩信息(返回pileNo,pileId,pilename,stationId)
			Map omap = pileInfo.get(obj[0].toString().trim());

			Map<String, String> pile = null;
			try {
				pile = transObjToMap(obj, omap, stationIds,validateCodeList);
			} catch (NullPointerException e) {
				e.printStackTrace();
				throw new BizException(1102013, objToStr(obj[0]));
			}
			String no = pile.get("pileNo").toString();
			if (!nos.contains(no) && StringUtils.notBlank(no)){
				nos.add(no);
			}
			if (!"校验正确".equals(pile.get("description"))) {
				canSave = "false";
			}
			if (mapNotExist(allPiles, pile)){
				allPiles.add(pile);
			}
		}

		List<String> pileNoList = validateExcel(allPiles, nos,pileInfo,chgGuns);

		if (canSave.equals("false")) {
			updateFail(allPiles, pileNoList);
			vo.setErrorCode(-2000000);
			vo.setErrorMsg("导入失败");
		} else {
			try {
				saveImportData(allPiles,pileInfo,chgGuns);

				for (Map<String, String> c : allPiles) {
					if (pileNoList.contains(c.get("pileNo"))) {
						c.put("impStatus", "更新成功");
					} else {
						c.put("impStatus", "保存成功");
					}
				}
			} catch (Exception e) {
				canSave = "false";
				e.printStackTrace();
				updateFail(allPiles, pileNoList);
				vo.setErrorCode(-2000000);
				vo.setErrorMsg("导入失败");
			}
		}
		vo.setData(allPiles);

		data = null;
		params = null;
		arraylist = null;
		nos = null;
		chgGuns = null;
		stationIds = null;
		validateCodeList = null;
		return vo;
	}

	/**
	 * 保存
	 * @param allPiles
	 * @throws Exception
	 */
	private void saveImportData(List<Map<String, String>> allPiles,Map pileInfo,List<Map> chgGuns) throws Exception {

		Map<Integer,List<Map>> chgGunInfo = new HashMap<>();
		for (Map map : chgGuns) {
			if(!"".equals(map.get("pileId")) && null != map.get("pileId")){
				if(chgGunInfo.get(map.get("pileId")) != null){
					List<Map> list = chgGunInfo.get(Integer.parseInt(map.get("pileId").toString()));
					list.add(map);
					chgGunInfo.put(Integer.parseInt(map.get("pileId").toString()), list);
				}else{
					List<Map> list = new ArrayList<>();
					list.add(map);
					chgGunInfo.put(Integer.parseInt(map.get("pileId").toString()), list);
				}
			}
		}

		List<Map> chgMeters = codeMapper.selectChgmeterByPileId(null);

		Map<Integer,List<Map>> chgMetersInfo = new HashMap<>();
		for (Map map : chgMeters) {
			if(!"".equals(map.get("pileId")) && null != map.get("pileId")){
				if(chgMetersInfo.get(Integer.parseInt(map.get("pileId").toString())) != null){
					List<Map> list = chgMetersInfo.get(Integer.parseInt(map.get("pileId").toString()));
					list.add(map);
					chgMetersInfo.put(Integer.parseInt(map.get("pileId").toString()), list);
				}else{
					List<Map> list = new ArrayList<>();
					list.add(map);
					chgMetersInfo.put(Integer.parseInt(map.get("pileId").toString()), list);
				}
			}
		}

		List updateList = new ArrayList();
		List saveList = new ArrayList();

		for (int i = 0;i < allPiles.size(); i++) {
			Map<String, String> c = allPiles.get(i);

			//根据导入的充电桩编号得到充电桩信息(返回pileNo,pileId,pilename,stationId)
			Map omap = (Map) pileInfo.get(c.get("pileNo").toString());

			Integer pileId = Integer.parseInt(omap.get("pileId").toString());
			Integer ortMode = Integer.parseInt(omap.get("ortMode").toString());
			Integer numberGun = Integer.parseInt(omap.get("numberGun").toString());

			//获取充电枪信息
			List<Map> ChgGunMap = chgGunInfo.get(pileId);

			//获取表计信息
			List<Map> ChgMeterMap = chgMetersInfo.get(pileId);

			//根据枪数和交直模式获取枪字母,和innerId
			Map<String, Integer> gunInnerMap = PileTypeUtils.getNumberGun(numberGun, ortMode);
			//枪表有数据更新,没有新增
			if(ChgGunMap != null && ChgGunMap.size() > 0){
				for (String letter: gunInnerMap.keySet()) {
					if(!"Z".equals(letter)){
						int number = Integer.parseInt(letter);
						if(number > numberGun){
							break;
						}
					}
					Map<String,Object> saveMap = new HashMap<>();
					saveMap.put("pileId", pileId);
					if(ChgGunMap.size() > 0){
						for (Map map : ChgGunMap) {
							if(("充电枪"+letter).equals(map.get("gumPoint").toString())){
								saveMap.put("chgGunId", map.get("gunId"));
							}
						}
					}
					if(ChgMeterMap.size() > 0){
						for (Map map : ChgMeterMap) {
							if((letter + "枪出线表").equals(map.get("meterName").toString()) || "总表".equals(map.get("meterName").toString())){
								saveMap.put("meterId", map.get("meterId"));
							}
						}
					}
					if(!"Z".equals(letter)){
						//总表不插枪
						saveMap.put("gumPoint", "充电枪"+letter);
					}

					saveMeterInfo(c, letter, saveMap);

					if(StringUtils.notBlank(c.get(letter + "qrCode"))){
						saveMap.put("qrCode", c.get(letter + "qrCode"));
					}
					if(StringUtils.notBlank(c.get(letter + "parkNum"))){//对应车位
						saveMap.put("parkNum", c.get(letter + "parkNum"));
					}

					List<Map> connectorList = putConnectorMap(omap ,"充电枪" + letter ,gunInnerMap.get(letter));
					//生成connectorId
					Map<String, String> connector = ConnectorIdUtil.newGetConnector(connectorList);
					saveMap.put("connectorId", connector.get("充电枪" + letter));
					updateList.add(saveMap);
				}

			}else{

				for (String letter: gunInnerMap.keySet()) {

					Map<String,Object> saveMap = new HashMap<>();
					saveMap.put("pileId", pileId);
					saveMap.put("innerId", gunInnerMap.get(letter));
					saveMeterInfo(c, letter, saveMap);

					if(!"Z".equals(letter)){
						//总表不插枪
						saveMap.put("gumPoint", "充电枪" + letter);
						if (StringUtils.notBlank(c.get(letter + "qrCode"))) {
							saveMap.put("qrCode", c.get(letter + "qrCode"));
						}
						if (StringUtils.notBlank(c.get(letter + "parkNum"))) {//对应车位
							saveMap.put("parkNum", c.get(letter + "parkNum"));
						}
						List<Map> connectorList = putConnectorMap(omap, "充电枪" + letter, gunInnerMap.get(letter));
						//生成connectorId
						Map<String, String> connector = ConnectorIdUtil.newGetConnector(connectorList);
						saveMap.put("connectorId", connector.get("充电枪" + letter));
					}

					saveList.add(saveMap);
				}
			}
		}

		if(saveList.size() > 0){
			Map vo = new HashMap();
			vo.put("saveList", saveList);
			inserGunMeters(vo);
		}
		if(updateList.size() > 0){
			Map vo = new HashMap();
			vo.put("updateList", updateList);
			updateGumPointQRCode(vo);
		}

		chgGunInfo = null;
		chgMeters = null;
		chgMetersInfo = null;
		saveList = null;
		updateList = null;

	}

	private void saveMeterInfo(Map<String, String> c, String letter, Map<String, Object> saveMap) throws BizException {
		if("Z".equals(letter)){
			saveMap.put("meterName", "总表");
		}else{
			saveMap.put("meterName", letter + "枪出线表");
		}
		if (StringUtils.notBlank(c.get(letter + "meterType"))) {
            saveMap.put("meterType", c.get(letter + "meterType"));
        }
		if (StringUtils.notBlank(c.get(letter + "meterPower"))) {
            saveMap.put("meterPower", c.get(letter + "meterPower"));//额定功率
        }else{
			saveMap.put("meterPower", "0");//额定功率
		}
		if (StringUtils.notBlank(c.get(letter + "meterRatio"))) {
			saveMap.put("meterRatio", c.get(letter + "meterRatio"));
		}else{
			saveMap.put("meterRatio", "1");
		}
	}


	private List<String> validateExcel(List<Map<String, String>> allPiles,
			List<String> nos,Map<String,Map> pileInfo,List<Map> chgGuns) {
		// 根据场站编号查询已存在记录
		List<String> pileNoList = new ArrayList<>();
		for (String pileNo : nos) {
			if(null != pileInfo.get(pileNo)){
				Map map = pileInfo.get(pileNo);
				pileNoList.add(map.get("pileNo").toString());
			}
		}

		for (Map<String, String> c : allPiles) {
			if(pileNoList.contains(c.get("pileNo"))){
				c.put("impStatus", "新增");
			}else if(!pileNoList.contains(c.get("pileNo"))){
				c.put("impStatus", "充电桩编号不存在");
				canSave = "false";
				continue;
			}else {
				c.put("impStatus", "无法更新");
				c.put("description", "校验失败:充电桩编号重复");
				canSave = "false";
				continue;
			}

			List<String> letterList = getLetterList();

			for (Map chgGun : chgGuns) {
				if(null != chgGun.get("qrCode") && !"".equals(chgGun.get("qrCode"))){
					for (String letter: letterList) {
						if(chgGun.get("qrCode").equals(c.get(letter + "qrCode"))){
							c.put("impStatus", letter + "枪二维码重复");
							c.put("description", "校验失败:"+letter+"枪二维码重复");
							canSave = "false";
							break;
						}
					}
				}
			}
		}

		return pileNoList;
	}

	private List<String> getLetterList() {
		List<String> letterList = new ArrayList<>();
		letterList.add("01");
		letterList.add("02");
		letterList.add("03");
		letterList.add("04");
		letterList.add("05");
		letterList.add("06");
		letterList.add("07");
		letterList.add("08");
		letterList.add("09");
		letterList.add("10");
		return letterList;
	}


	private Map<String, String> transObjToMap(Object[] obj,
			Map omap, Set<Integer> stationIds,List valiCodeList) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String desc = objToStr(obj[58]);
		if(stationIds != null && !stationIds.contains(omap.get("stationId"))){
			if(!desc.contains("校验失败")){
				desc = "校验失败:没有给充电桩导入二维码的权限";
			}
		}
		map.put("pileNo", objToStr(obj[0]));//充电桩名称
		String numberGun = dictService.getDictValue("qs", objToStr(obj[1]));
		map.put("numberGun", numberGun);//枪数
		//验证类型是否存在
		if(StringUtils.isBlank(numberGun)){
			if(!desc.contains("校验失败")){
				desc = "校验失败:枪数类型不存在";
			}
		}
		if(!omap.get("numberGun").toString().equals(numberGun)){
			if(!desc.contains("校验失败")){
				desc = "校验失败:导入枪数类型不符";
			}
		}

		String ortMode = dictService.getDictValue("jzms", objToStr(obj[2]));
		map.put("ortMode", ortMode);//交直模式
		//验证类型是否存在
		if(StringUtils.isBlank(ortMode)){
			if(!desc.contains("校验失败")){
				desc = "校验失败:交直模式不存在";
			}
		}
		if(!omap.get("ortMode").toString().equals(ortMode)){
			if(!desc.contains("校验失败")){
				desc = "校验失败:导入交直模式不符";
			}
		}

		String powerMode = dictService.getDictValue("glms", objToStr(obj[3]));
		map.put("powerMode", powerMode);//功率模式
		//验证类型是否存在
		if(StringUtils.isBlank(powerMode)){
			if(!desc.contains("校验失败")){
				desc = "校验失败:功率模式不存在";
			}
		}
		if(!omap.get("powerMode").toString().equals(powerMode)){
			if(!desc.contains("校验失败")){
				desc = "校验失败:导入功率模式不符";
			}
		}

		int first = 4 ;
		Integer[] arr ;

		List<String> letterList = getLetterList();

		for (String letter : letterList) {
			arr = getObjNumber(first);
			desc = transObjToGun(obj, map, desc ,letter,arr,valiCodeList);
			first += 5;
		}

		map.put("ZmeterName", objToStr(obj[54]));//总表枪表计名称
		map.put("ZmeterType", objToStr(obj[55]));//总表表计类型

		try {
			if(StringUtils.notBlank(objToStr(obj[56]))){//总表额定功率
				map.put("ZmeterPower",objToStr(obj[56]));
			}
		} catch (Exception e) {
			if(!desc.contains("校验失败")){
				desc = "校验失败:总表额定功率数据类型异常";
			}
		}

		//总表变比等字典type
		map.put("ZmeterRatio", objToStr(obj[57]));//总表变比
		if(null != objToStr(obj[47]) && !"".equals(objToStr(obj[57]))){
			String zMeterRatio = dictService.getDictValue("meterRatio", objToStr(obj[57]));
			if(StringUtils.isBlank(zMeterRatio)){
				if(!desc.contains("校验失败")){
					desc = "校验失败:总表变比类型不存在";
				}
			}
		}
		map.put("description", desc);

		return map;
	}

	private Integer[] getObjNumber(int first) {
		Integer[] arr = new Integer[5];
		for (int i = 0; i < 5 ; i++) {
			arr[i] = first;
			first ++;
		}
		return arr;
	}

	private String transObjToGun(Object[] obj, Map<String, String> map, String desc,String letter,Integer[] arr,List<String> valiCodeList) throws BizException {
		map.put(letter + "qrCode", objToStr(obj[arr[0]]));//二维码

		if(valiCodeList.contains(objToStr(obj[arr[0]]))){
			if(!desc.contains("校验失败")){
				desc = "校验失败:导入" + letter + "枪二维码重复";
			}
		}else{
			if(!ObjectUtils.isNull(objToStr(obj[arr[0]]))){
				valiCodeList.add(objToStr(obj[arr[0]]));
			}
		}

		map.put(letter + "meterName", objToStr(obj[arr[1]]));//表计名称
		map.put(letter + "meterType", objToStr(obj[arr[2]]));//表计类型

		try {
			if(StringUtils.notBlank(objToStr(obj[arr[3]]))){//额定功率
				Double dou = Double.parseDouble(objToStr(obj[arr[3]]));
				map.put(letter + "meterPower",dou.toString());
			}
		} catch (Exception e) {
			if(!desc.contains("校验失败")){
				desc = "校验失败:" + letter + "枪额定功率数据类型异常";
			}
		}

		map.put(letter + "parkNum", objToStr(obj[arr[4]]));//对应车位
		return desc;
	}

	public static List<Object[]> parseExcel(MultipartFile fileName) throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Workbook wb = null;
		Sheet sheet = null;
		int arrNum = 59;//数组的最大长度
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
				int cells = arrNum;//支持十枪
				if (i == 0 || i == 1) {// 首行表头过滤
					continue;
				}
				Object[] arra = new Object[arrNum];
				String checkStr = "校验正确";
				for (int j = 0; j < cells; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_FORMULA://公式型
							arra[j] = cell.getCellFormula();
							break;
						case Cell.CELL_TYPE_NUMERIC://数值型
							 DecimalFormat df = new DecimalFormat("0");
							 arra[j] = df.format(cell.getNumericCellValue()) + "";
							break;
						case Cell.CELL_TYPE_STRING://字符串类型
							arra[j] = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_BLANK://空值
							break;
						default:
							break;
						}
					}
					if (arra[j] == null || "".equals(arra[j])) {
						switch (j) {
						case 0:
							checkStr = "校验失败:充电桩编号不能为空";
							break;
						case 1:
							checkStr = "校验失败:枪数不能为空";
							break;
						case 2:
							checkStr = "校验失败:交直模式不能为空";
							break;
						case 3:
							checkStr = "校验失败:功率模式不能为空";
							break;
						case 4:
							checkStr = "校验失败:二维码不能为空";
							break;
						default:
							break;
						}
					}
				}
				arra[cells - 1] = checkStr;
				if(!ObjectUtils.isNull(arra[1]) || !ObjectUtils.isNull(arra[2]) || !ObjectUtils.isNull(arra[4])){
					result.add(arra);
				}
			}
		}
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

	/**
	 * Excel中是否已经存在同名或者同编号的数据
	 * @param listMap
	 * @param map1
	 * @return
	 */
	private static Boolean mapNotExist(List<Map<String, String>> listMap,
			Map<String, String> map1) {
		String pileNo = map1.get("pileNo");
		for (Map<String, String> map : listMap) {
			if (map.get("pileNo").equals(pileNo)){
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
	private void updateFail(List<Map<String, String>> allPiles,List<String> pileMap1) {
		for (Map<String, String> c : allPiles) {
			if (pileMap1.contains(c.get("pileNo"))) {
				c.put("impStatus", "更新失败");
			} else {
				c.put("impStatus", "保存失败");
			}
		}
	}

	/**
	 * 插入表计表
	 * @param vo
	 * @return
	 */
	public int inserGunMeters(Map vo) throws BizException {
		List<Map> list = (List<Map>) vo.get("saveList");
		codeMapper.inserChgMeter(list);
		List saveChgGunList = new ArrayList();
		List<Integer> pileIds  = new ArrayList<>();
		for (Map map : list) {
			//总表不插枪
			if(null != map.get("gumPoint")){
				saveChgGunList.add(map);
			}
			pileIds.add(Integer.parseInt(map.get("pileId").toString()));
		}
		int inserChgGun = codeMapper.inserChgGun(saveChgGunList);
		//新增时设置下发状态为未下发
		vo.put("qrCodeState", 0);
		vo.put("hlhtQRCodeState", 0);
		vo.put("list", pileIds);
		codeMapper.updatePileCodeState(vo);
		codeMapper.updateHLHTCodeState(vo);
		return inserChgGun;
	}

	public int updateGumPointQRCode(Map dataVo)throws BizException{
		List<Map> list = (List<Map>) dataVo.get("updateList");
		int updateChgGun;
		if(dataVo.get("pileId") != null){
			//页面修改
			updateChgGun = codeMapper.updateGumPointQRCode(list);
		}else{
			//导入
			codeMapper.updateChgMeter(list);
			List saveChgGunList = new ArrayList();
			List<Integer> pileIds  = new ArrayList<>();
			for (Map map : list) {
				//总表不插枪
				if(null != map.get("gumPoint")){
					saveChgGunList.add(map);
				}
				pileIds.add(Integer.parseInt(map.get("pileId").toString()));
			}
			updateChgGun = codeMapper.updateGumPointQRCode(saveChgGunList);
			//新增时设置下发状态为未下发
			dataVo.put("qrCodeState", 0);
			dataVo.put("hlhtQRCodeState", 0);
			dataVo.put("list", pileIds);
			codeMapper.updatePileCodeState(dataVo);
			codeMapper.updateHLHTCodeState(dataVo);
		}
		return updateChgGun;
	}

}

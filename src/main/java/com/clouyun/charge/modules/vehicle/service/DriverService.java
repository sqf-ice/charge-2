package com.clouyun.charge.modules.vehicle.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clou.system.util.OperateType;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.OSSUploadFileUtils;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.OrgService;
import com.clouyun.charge.modules.system.service.UserService;
import com.clouyun.charge.modules.vehicle.mapper.DriverMapper;
import com.clouyun.charge.modules.vehicle.mapper.VehicleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
/**
 * 
 * @author fft
 *
 */
@Service
public class DriverService extends BusinessService{
	@Autowired
	private DriverMapper driverMapper;
	
	@Autowired
	private VehicleMapper vehicleMapper;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private UserService userService;
	
	/*
	 * 驾驶员列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageInfo queryDrivers(Map map) throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1500000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		PageInfo page = null;
		// 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
		List<DataVo> drivers = driverMapper.queryDrivers(params);
		if(null != drivers && drivers.size() > 0){
			 for(DataVo driversVo : drivers){
				 String sex = driversVo.getString("sex");
				 if(null != sex && !"".equals(sex)){
					 driversVo.put("sex", "0" + sex);//数据库设计和系统字典不吻合，暂时先这样处理
				 }
			 }
			 page = new PageInfo(drivers);
		}
		return page;
	}
	
	
	/**
	 * 通过车辆ID查适配驾驶员
	 */
	@SuppressWarnings("rawtypes")
	public List<DataVo> getDrivers(Integer vehicleId){
		List<DataVo> list = driverMapper.queryDriversByvehicleId(vehicleId);
		if(null != list && list.size() > 0){
			 for(DataVo driversVo : list){
				 String sex = driversVo.getString("sex");
				 if(null != sex && !"".equals(sex)){
					 driversVo.put("sex", "0" + sex);//数据库设计和系统字典不吻合，暂时先这样处理
				 }
			 }
		}
		return list;
	}
	
	/**
	 * 驾驶员详情
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月2日
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getDriver(Integer driverId){
		DataVo mapVo = driverMapper.getDriver(driverId);
		if(null != mapVo){
			String sex = mapVo.getString("sex");
			if(null != sex){
				mapVo.put("sex", "0" + sex);//数据库设计和系统字典不吻合，暂时先这样处理
			}
			String certificateImgUrl = mapVo.getString("certificateImgUrl");
			if(null!=certificateImgUrl && !"".equals(certificateImgUrl)){
				mapVo.put("certificateImgUrl", CommonUtils.PATH+certificateImgUrl);
			 }
		}
		return mapVo;
	}
	
	/*
	 * 驾驶员导出
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void export(Map map,HttpServletResponse response) throws Exception{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1500000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		List<DataVo> drivers = driverMapper.queryDrivers(params);
		for (DataVo driverVo : drivers) {
			String sex = driverVo.getString("sex");
			driverVo.put("sex", null == sex ? "" : "1".equals(sex) ? "男" : "女");
		}
		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		headList.add("姓名");
		headList.add("手机号码");
		headList.add("性别");
		headList.add("国籍");
		headList.add("出生日期");
		headList.add("初次领证日期");
		headList.add("准驾车型");
		headList.add("更新时间");
		valList.add("driverName");
		valList.add("mobilePhone");
		valList.add("sex");
		valList.add("nationality");
		valList.add("birthday");
		valList.add("cardTime");
		valList.add("models");
		valList.add("operatingTime");
		ExportUtils.exportExcel(drivers, response,headList,valList,"驾驶员信息");
	}
	
	/**
	 * 驾驶员信息添加
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public void insertDriver(Map map,MultipartFile file) throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1502000,"用户id");
		}
		if(params.isBlank("orgId")){
			throw new BizException(1502000,"运营商名称");
		}
		if(params.isBlank("driverName")){
			throw new BizException(1502000,"姓名");
		}else if(params.getString("driverName").length()>32){
			throw new BizException(1502005,"姓名");
		}else if(!ValidateUtils.isConSpeCharacters(params.getString("driverName"))){
			throw new BizException(1502004,"姓名");
		}
		if(params.isNotBlank("nationality")){
			if(params.getString("nationality").length()>32){
				throw new BizException(1502005,"国籍");
			}
			if(!ValidateUtils.isConSpeCharacters(params.getString("nationality"))){
				throw new BizException(1502004,"国籍");
			}
		}
		if(params.isNotBlank("address")){
			if(params.getString("address").length()>255){
				throw new BizException(1502005,"地址");
			}
			if(!ValidateUtils.isConSpeCharacters(params.getString("address"))){
				throw new BizException(1502004,"地址");
			}
		}
		if(params.isNotBlank("sex")){
			params.put("sex", params.getInt("sex"));
		}
		if(params.isNotBlank("mobilePhone")){
			if(!ValidateUtils.Mobile(params.getString("mobilePhone"))){
				throw new BizException(1502006,"电话号码");
			}
			DataVo map2=new DataVo();
			map2.put("mobilePhone", params.getString("mobilePhone"));
			map2.put("orgId", params.getString("orgId"));
			List<DataVo> drivers = driverMapper.queryDriverExist(map2);
			if(null != drivers&&drivers.size() > 0){
				throw new BizException(1500001,"电话号码");
			}
		}
		if(params.isNotBlank("identityCard")){
			if(!ValidateUtils.IDcard(params.getString("identityCard"))){
				throw new BizException(1502006,"身份证号码");
			}
		}
		if(params.isNotBlank("models")){
			if(params.getString("models").length()>32){
				throw new BizException(1502005,"准驾车型");
			}
			if(!ValidateUtils.isConSpeCharacters(params.getString("models"))){
				throw new BizException(1502004,"准驾车型");
			}
		}
		if (file != null && !file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String imageFileName = "images/" + new Date().getTime() + fileName.substring(fileName.lastIndexOf("."));
			OSSUploadFileUtils.toUploadPictureFile(file, imageFileName);
			params.put("certificateImgUrl", imageFileName);
		}
		driverMapper.insertDriver(params);
		logService.insertLog("添加驾驶员操作", OperateType.add.OperateName, "驾驶员新增", params.getInt("userId"));
	}
	
	//编辑驾驶员信息
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void updateDriver(Map map,MultipartFile file) throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1502000,"用户id");
		}
		if(params.isBlank("driverId")){
			throw new BizException(1502000,"驾驶员Id");
		}
		if(params.isBlank("orgId")){
			throw new BizException(1502000,"运营商");
		}
		if(params.isBlank("driverName")){
			throw new BizException(1502000,"姓名");
		}else if(params.getString("driverName").length()>32){
			throw new BizException(1502005,"姓名");
		}else if(!ValidateUtils.isConSpeCharacters(params.getString("driverName"))){
			throw new BizException(1502004,"姓名");
		}
		if(params.isNotBlank("nationality")){
			if(params.getString("nationality").length()>32){
				throw new BizException(1502005,"国籍");
			}
			if(!ValidateUtils.isConSpeCharacters(params.getString("nationality"))){
				throw new BizException(1502004,"国籍");
			}
		}
		if(params.isNotBlank("address")){
			if(params.getString("address").length()>255){
				throw new BizException(1502005,"地址");
			}
			if(!ValidateUtils.isConSpeCharacters(params.getString("address"))){
				throw new BizException(1502004,"地址");
			}
		}
		if(params.isNotBlank("sex")){
			params.put("sex", params.getInt("sex"));
		}
		if(params.isNotBlank("mobilePhone")){
			if(!ValidateUtils.Mobile(params.getString("mobilePhone"))){
				throw new BizException(1502006,"电话号码");
			}
			DataVo map2=new DataVo();
			map2.put("mobilePhone", params.getString("mobilePhone"));
			map2.put("orgId", params.getString("orgId"));
			map2.put("driverId", params.getInt("driverId"));
			List<DataVo> drivers = driverMapper.queryDriverExist(map2);
			if(null != drivers && drivers.size() > 0){
				throw new BizException(1500001,"电话号码");
			}
		}
		if(params.isNotBlank("identityCard")){
			if(!ValidateUtils.IDcard(params.getString("identityCard"))){
				throw new BizException(1502006,"身份证号码");
			}
		}
		if(params.isNotBlank("models")){
			if(params.getString("models").length()>32){
				throw new BizException(1502005,"准驾车型");
			}
			if(!ValidateUtils.isConSpeCharacters(params.getString("models"))){
				throw new BizException(1502004,"准驾车型");
			}
		}
		String certificateImgUrl = "";
		if(params.isNotBlank("certificateImgUrl")){
			certificateImgUrl = params.getString("certificateImgUrl");
			if (certificateImgUrl.contains(CommonUtils.PATH)){
				params.put("certificateImgUrl",certificateImgUrl.substring(CommonUtils.PATH.length(),certificateImgUrl.length()));
			}
		}
		if (file != null && !file.isEmpty()){                  
			String imageFileName = null;
			certificateImgUrl = params.getString("certificateImgUrl");
			if (StringUtils.isBlank(certificateImgUrl)) {
				String fileName = file.getOriginalFilename();
				imageFileName = "images/" + CommonUtils.randomNum() + fileName.substring(fileName.lastIndexOf("."));
				params.put("certificateImgUrl", imageFileName);
			} else {
				imageFileName = certificateImgUrl;
			}
			OSSUploadFileUtils.toUploadPictureFile(file, imageFileName);
		}
		driverMapper.updateDriverByDriverId(params);
		logService.insertLog("更新操作", OperateType.edit.OperateName, "更新驾驶员信息", params.getInt("userId"));
		
	}
	/**
	 * 驾驶员批量导入
	 * 2.0.0
	 * gaohui
	 * 2017年5月16日
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> importDrivers(MultipartFile excelFile,Map map) throws Exception{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1502000,"用户");
		}
		boolean flags = true;
		List<Map> result = null;
		List<Object[]> arraylist = parseExcel(excelFile);//解析EXCEL
		List<Map> saveList = new ArrayList<Map>();
		List<Map> updateList = new ArrayList<Map>();
		List<String> identityCards = new ArrayList<String>();// 身份证号
		List<String> licenses = new ArrayList<String>();//车辆牌照
		List<Map> driverList = new ArrayList<Map>();
		if(null != arraylist && arraylist.size() > 0){
			List<ComboxVo> orgs = orgService.getOrgNameByUserId(params.getInt("userId"));
			if(null == orgs || orgs.size() < 1){
				throw new BizException(1502007);
			}
			List<Map> tempList = new ArrayList<Map>();
			for(Object[] obj:arraylist){
				boolean flag = true;
				//校验成功后的驾驶员列表
				Map driver = new HashMap<>();
				//校验失败返回的驾驶员
				Map reDriver = new HashMap<>();
				//检验失败描述
				String desc = "";
				//驾驶员姓名
				if (null == obj[0] || "".equals(obj[0])) {
					if ("".equals(desc)) {
						desc = "校验失败:姓名不能为空";
					}
					flag = false;
				}else{//TODO   再加上长度校验
					driver.put("driverName",obj[0].toString());
				}
				reDriver.put("driverName", String.valueOf(obj[0]));
				//驾驶员性别
				if(null != obj[1] && !"".equals(obj[1])){
					String sex = dictService.getDictValue("xb",obj[1].toString().trim());
					if(null==sex || "".equals(sex)){
						if ("".equals(desc)) {
							desc = "校验失败:性别和字典不匹配，请核对！";
						}
					}else{
						driver.put("sex",sex);
					}
				}
				reDriver.put("sex", String.valueOf(obj[1]));
				//国籍
				driver.put("nationality", String.valueOf(obj[2]));
				reDriver.put("nationality", String.valueOf(obj[2]));
				//电话号码
				if(null!=obj[3] && !"".equals(obj[3])){//若电话号码不为空
					if(tempList.size()>0){//
						for(Map tempMap:tempList){
							DataVo mapVo =new DataVo(tempMap);
							if(null!=obj[6] && !"".equals(obj[6])){//如果证号不为空
								if(!obj[6].toString().trim().equals(mapVo.getString("identityCard").trim())
								   && String.valueOf(obj[10]).equals(mapVo.getString("orgName").toString())
								   && obj[3].toString().equals(mapVo.getString("mobilePhone").toString())){
										if ("".equals(desc)) {
											desc = "校验失败:同一企业下电话号码重复，请核对！";
											continue;
										}
								}
							}else{//证号为空
								if(String.valueOf(obj[10]).equals(mapVo.getString("orgName").toString())
								   && obj[3].toString().equals(mapVo.getString("mobilePhone").toString())){
										if ("".equals(desc)) {
											desc = "校验失败:同一企业下电话号码重复，请核对！";
											continue;
										}
									}
							}
						}
					}else{
						Map tempExist = new HashMap();
						tempExist.put("identityCard", String.valueOf(obj[6]));
						tempExist.put("mobilePhone", obj[3].toString());
						tempExist.put("orgName", String.valueOf(obj[6]));
						tempList.add(tempExist);
					}
				}
				driver.put("mobilePhone", String.valueOf(obj[3]));
				//出生日期
				if(null!=obj[4] && !"".equals(obj[4])){
					if(DateUtils.isDate(obj[4].toString())){
						driver.put("birthday",obj[4].toString());
					}else{
						if ("".equals(desc)) {
							desc = "校验失败:出生日期格式错误，请核对！";
						}
					}
				}
				reDriver.put("birthday", String.valueOf(obj[4]));
				//地址
				driver.put("address", String.valueOf(obj[5]));
				reDriver.put("address", String.valueOf(obj[5]));
				//身份证号
				if(null!=obj[6] && !"".equals(obj[6])){
					identityCards.add(obj[6].toString());//添加身份证到list
					driver.put("identityCard", obj[6].toString());
				}
				reDriver.put("identityCard", String.valueOf(obj[6]));
				//初次领证日期
				if(null!=obj[7] && !"".equals(obj[7])){
					if(DateUtils.isDate(obj[7].toString())){
						driver.put("cardTime",obj[7].toString());
					}else{
						if ("".equals(desc)) {
							desc = "校验失败:初次领证日期格式错误，请核对！";
						}
					}
				}
				reDriver.put("cardTime", String.valueOf(obj[7]));
				//准驾车型
				driver.put("models", String.valueOf(obj[8]));
				reDriver.put("models", String.valueOf(obj[8]));
				//有效期
				if(null!=obj[9] && !"".equals(obj[9])){
					if(DateUtils.isDate(obj[9].toString())){
						driver.put("validTerm",obj[9].toString());
					}else{
						if ("".equals(desc)) {
							desc = "校验失败:有效日期格式错误，请核对！";
						}
					}
				}
				reDriver.put("validTerm", String.valueOf(obj[9]));
				//所属企业
				if (null==obj[10]|| "".equals(obj[10])) {
					if ("".equals(desc)) {
						desc = "校验失败:所属企业不能为空！";
					}
					flag = false;
				}else{
					boolean orgFlag = false;
					for(ComboxVo comboxVo:orgs){
						if(comboxVo.getText().trim().equals(obj[10].toString().trim())){
							driver.put("orgId", comboxVo.getId());
							orgFlag = true;
							continue;
						}
					}
					if(!orgFlag){
						desc = "校验失败:用户对"+obj[10].toString()+"企业没有操作权限！";
					}
				}
				reDriver.put("orgName", String.valueOf(obj[10]));
				
				//常用车辆
				if(null!=obj[11] && "".equals(obj[11])){
					licenses.add(obj[11].toString());
				}
				driver.put("licensePlate", String.valueOf(obj[11]));
				reDriver.put("licensePlate", String.valueOf(obj[11]));
				
				if (!flag) {
					driver.put("description", desc);
					flags = false;
				}else{
					driver.put("description", "校验成功");
				}
				driverList.add(driver);
			}
		}
		boolean baseFalg = false;
		if (flags) {
			baseFalg = true;
			Map identityMap = new HashMap();
			Map<String,Map> identitysMaps  = new HashMap<String, Map>();
			Map<String,Map> licenseMaps  = new HashMap<String, Map>();
			if(null!=driverList && driverList.size()>0){
				identityMap.put("identityCards", identityCards);
				List<DataVo> identityList = driverMapper.getDriverByVars(identityMap);
				Map licensesMap = new HashMap();
				licensesMap.put("licensePlates", licenses);
				//驾驶员导入已去掉，此方法稍后优化
				List<Map> licensesList = new ArrayList<Map>();//vehicleMapper.getVehiclesByLicenses(licenseMaps);//根据驾照列表获取相应的驾驶员信息
				if(null != identityList && identityList.size() > 0){
					for(DataVo driverVo : identityList){
						identitysMaps.put(driverVo.getString("identityCard"), driverVo);//---->Map
					}
				}
				if(null!=licensesList && licensesList.size()>0){
					for(Map license:licensesList){
						DataVo licenseVo = new DataVo();
						licenseMaps.put(licenseVo.getString("licensePlate"), license);//---->Map
					}
				}
				for(Map driver:driverList){
					DataVo driverVo = new DataVo(driver);
					if(driverVo.isNotBlank("licensePlate")){//常用车辆处理
						Map driverMap = licenseMaps.get(driverVo.getString("licensePlate"));
						if(null!=driverMap){
							Integer vehicleId = new DataVo(driverMap).getInt("vehicleId");
							if(null == vehicleId ||vehicleId ==0){
								baseFalg = false;
								driverVo.put("description", "校验失败:不存在车牌为"+driverVo.getString("licensePlate")+"的车辆！");
							}else{
								driver.put("vehicleId", vehicleId);
							}
						} 
					}
					if(driverVo.isNotBlank("identityCard")){
						if(null!=identitysMaps.get(driverVo.getString("identityCard"))){
							//添加
							saveList.add(driver);
						}else{
							updateList.add(driver);
							//更新
						}
					}else{
						saveList.add(driver);
						//添加
					}
				}
			}
		}
		if(baseFalg){
			if (null != saveList && saveList.size() > 0) {
				//批量保存
				HashMap saveMap = new HashMap();
				saveMap.put("drivers", saveList);
				driverMapper.insertDrivers(saveMap);
			}
			if (null != updateList && updateList.size() > 0) {
				//更新
				HashMap updateMap = new HashMap();
				updateMap.put("drivers", updateList);
				driverMapper.updateDrivers(updateMap);
			}
		}else {
			result = driverList;
		}
		return result;
	}
	
	/**
	 * 解析Excel文件
	 * gaohui
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> parseExcel(MultipartFile fileName) throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(fileName.getInputStream());
		} catch (Exception e) {
			wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook(fileName.getInputStream());
		}
		Sheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		if (rows == 1) {
			throw new BizException(1502008);
		}
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				int cells = 12;
				if (i == 0) {// 首行表头过滤
					continue;
				}
				Object[] arra = new Object[cells];
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

				}
				result.add(arra);
			}
		}
		return result;
	}
}

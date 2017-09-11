package com.clouyun.charge.modules.vehicle.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.MonitorConstants;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.OSSUploadFileUtils;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.clouyun.charge.modules.vehicle.mapper.VehicleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
* 描述: 账户信息
* 版权: Copyright (c) 2017
* 公司: 科陆电子
* 作者: fft
* 版本: 2.0.0
* 创建日期: 2017年4月15日 上午10:01:18
*/
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VehicleService extends BusinessService{
	
	private static final String DESCRIPTION = "description";
	
	@Autowired
	private VehicleMapper vehicleMapper;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private DictService dictService;
	
	/**
	 * 车辆信息列表
	 * gaohui
	 * 2.0.0
	 * 2017-5-15
	 */
	public PageInfo getVehiclesPage(Map map) throws BizException{
		PageInfo pageInfo = null;
		DataVo params = new DataVo(map);
		convertParams(params);
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }

        List<DataVo> vehicles = vehicleMapper.getVehicles(params);
		Set<Integer> vehicleIds = null;
		Map<String,String> relMemMap= null;
		if(CollectionUtils.isNotEmpty(vehicles)){
			vehicleIds = new HashSet();
			for(DataVo tempVo :vehicles){
				int vehicleId = tempVo.getInt("vehicleId");
				vehicleIds.add(vehicleId);
			}
			DataVo dataVo  = new DataVo();
			dataVo.put("consTrueName", params.getString("consTrueName"));
			List<DataVo> relMemList = vehicleMapper.getVehMemRel(dataVo);
			if(CollectionUtils.isNotEmpty(relMemList)){
				relMemMap=new HashMap();
				for(DataVo relVo : relMemList){
					String vehicleId = relVo.getString("vehicleId");
					String consTrueName = relVo.getString("consTrueName");
					if(!relMemMap.containsKey(vehicleId)){
						String name= consTrueName;
						relMemMap.put(vehicleId, name);
					}else{
						String name = relMemMap.get(vehicleId)+"/"+consTrueName;
						relMemMap.put(vehicleId, name);
					}
				}
			}
			for(DataVo vehicleVo : vehicles){
				String vehicleId = vehicleVo.getString("vehicleId");
				String belongsType = vehicleVo.getString("belongsType");
				String belongsOrgName = vehicleVo.getString("belongsOrgName");
				String belongsName = vehicleVo.getString("belongsName");
				if(null != relMemMap && relMemMap.size() > 0){
					vehicleVo.put("consTrueName", relMemMap.get(vehicleId));
				}else{
					vehicleVo.put("consTrueName", "");
				}
				if(MonitorConstants.BelongsType.COM.getCode().equals(belongsType)){
					vehicleVo.put("belongs",belongsOrgName);
				}else if(MonitorConstants.BelongsType.SOL.getCode().equals(belongsType)){
					vehicleVo.put("belongs",belongsName);
				}
			}
			relMemMap = null;
			pageInfo = new PageInfo(vehicles);
		}
		params = null;
		vehicles = null;
		return pageInfo;
	}
    
	public DataVo getVehicle(Integer vehicleId)throws BizException{
		DataVo vehicleVo = vehicleMapper.getVehicle(vehicleId);
		if(null != vehicleVo){
			String drivingUrl = vehicleVo.getString("drivingUrl");
			String belongsType = vehicleVo.getString("belongsType");
			String belongsOrgName = vehicleVo.getString("belongsOrgName");
			String belongsName = vehicleVo.getString("belongsName");
			if(null != drivingUrl && !"".equals(drivingUrl)){
				vehicleVo.put("drivingUrl", CommonUtils.PATH+drivingUrl);
			}
			if(MonitorConstants.BelongsType.COM.getCode().equals(belongsType)){
				vehicleVo.put("belongs",belongsOrgName);
			}else if(MonitorConstants.BelongsType.SOL.getCode().equals(belongsType)){
				vehicleVo.put("belongs", belongsName);
			}
		}
		return vehicleVo;
	}
	
	private void convertParams(DataVo params) throws BizException{
		if(params.isBlank("userId")){
			throw new BizException(1500000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(CollectionUtils.isNotEmpty(orgIds)){
			params.put("orgIds", orgIds);
		}
		if(params.isBlank("sort")){
			params.put("sort", "this_.create_time");
			params.put("order","DESC");
		}
		String belongsType = params.getString("belongsType");
		int belongs = params.getInt("belongs");
		if(MonitorConstants.BelongsType.COM.getCode().equals(belongsType)){
			if(params.isNotBlank("belongs")){
				params.put("belongsOrgId", belongs);
			}
		}else if(MonitorConstants.BelongsType.SOL.getCode().equals(belongsType)){
			params.put("belongsName", belongs);
		}
	}
	/*
	 * 车辆信息导出
	 */
	public void exportVehicles(Map map,HttpServletResponse response) throws Exception{
		String header = "车辆信息列表";
		DataVo params = new DataVo(map);
		convertParams(params);
		Set<Integer> vehicleIds = null;
		Map<String,String> relMemMap= new HashMap();
		List<DataVo> vehicles = vehicleMapper.getVehicles(params);
		if(CollectionUtils.isNotEmpty(vehicles)){
			List<ComboxVo> vehicleTypes = dictService.getDictByType("cllx");//车辆类型
			List<ComboxVo> belongsTypes = dictService.getDictByType("clsslx");//车辆所属类型
			List<ComboxVo> useingTypes = dictService.getDictByType("clsyxz");//车辆所属类型
			List<ComboxVo> operTypes = dictService.getDictByType("clyyxz");//车辆所属类型
			vehicleIds = new HashSet();
			for(DataVo tempVo : vehicles){
				vehicleIds.add(tempVo.getInt("vehicleId"));
			}
			DataVo dataVo  = new DataVo();
			dataVo.put("consTrueName", params.getString("consTrueName"));
			List<DataVo> relMemList = vehicleMapper.getVehMemRel(dataVo);
			if(CollectionUtils.isNotEmpty(relMemList)){
				for(DataVo relVo : relMemList){
					String vehicleId = relVo.getString("vehicleId");
					String consTrueName = relVo.getString("consTrueName");
					if(!relMemMap.containsKey(vehicleId)){
						String name= consTrueName;
						relMemMap.put(vehicleId, name);
					}else{
						String temp = relMemMap.get(vehicleId)+"/"+consTrueName;
						relMemMap.put(vehicleId, temp);
					}
				}
			}
			for(Map vehicle:vehicles){
				DataVo vehicleVo = new DataVo(vehicle);
				String vehicleId = vehicleVo.getString("vehicleId");
				String belongsType = vehicleVo.getString("belongsType");
				if(null!= relMemMap && relMemMap.size()>0){
					vehicle.put("consTrueName", relMemMap.get(vehicleId));
				}else{
					vehicle.put("consTrueName", "");
				}
				if(MonitorConstants.BelongsType.COM.getCode().equals(belongsType)){
					vehicle.put("belongs", vehicleVo.getString("belongsOrgName"));
				}else if(MonitorConstants.BelongsType.SOL.getCode().equals(belongsType)){
					vehicle.put("belongs", vehicleVo.getString("belongsName"));
				}
				if(null!=vehicleTypes && vehicleTypes.size()>0){
					for(ComboxVo comboxVo:vehicleTypes){
						if(vehicleVo.getString("vehicleType").equals(comboxVo.getId())){
							vehicle.put("vehicleType", comboxVo.getText());
						}
					}
				}
				if(null!=belongsTypes && belongsTypes.size()>0){
					for(ComboxVo comboxVo:belongsTypes){
						if(vehicleVo.getString("belongsType").equals(comboxVo.getId())){
							vehicle.put("belongsType", comboxVo.getText());
						}
					}
				}
				
				if(null!=useingTypes && useingTypes.size()>0){
					for(ComboxVo comboxVo:useingTypes){
						if(vehicleVo.getString("usingRoperty").equals(comboxVo.getId())){
							vehicle.put("usingRoperty", comboxVo.getText());
						}
					}
				}
				
				if(null!=operTypes && operTypes.size()>0){
					for(ComboxVo comboxVo:operTypes){
						if(vehicleVo.getString("operationRoperty").equals(comboxVo.getId())){
							vehicle.put("operationRoperty", comboxVo.getText());
						}
					}
				}
			}
			vehicleTypes = null;//车辆类型
			belongsTypes = null;//车辆所属类型
			useingTypes = null;//车辆所属类型
			operTypes = null;//车辆所属类型
		}
		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		headList.add("所有人");
		headList.add("车牌号");
		headList.add("所属类型");
		headList.add("车辆类型");
		headList.add("使用性质");
	    headList.add("运营性质");
	    headList.add("线路");
		headList.add("会员名");
		valList.add("belongs");
		valList.add("licensePlate");
		valList.add("belongsType");
		valList.add("vehicleType");
		valList.add("usingRoperty");
		valList.add("operationRoperty");
		valList.add("line");
		valList.add("consTrueName");
		params = null;
		ExportUtils.exportExcel(vehicles, response,headList,valList,header);
	}
	
	/**
	 * 新增车辆信息
	 * @param map
	 * @return 
	 * 2017年5月12日 
	 * gaohui
	 * 2.0.0
	 */
	@Transactional(rollbackFor=Exception.class)
	public void insertVehicle(Map map,MultipartFile file)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1500000,"创建用户");
		}
		if(params.isNotBlank("belongsOrgId")){
			params.put("belongsOrgId", params.getInt("belongsOrgId"));
		}
		if(params.isBlank("licensePlate")){
			throw new BizException(1500000,"车牌号");
		}else if(!isVehicleNumber(params.getString("licensePlate"))){
			throw new BizException(1502009,"车牌号");
		}
		if(params.isNotBlank("vin")){
			if(params.getString("vin").length()!=17){
				throw new BizException(1502009,"vin码");
			}
		}
		if(params.isNotBlank("initMileage")){
			if(!ValidateUtils.Number(params.getString("initMileage"))){
				throw new BizException(1502006,"初始里程数");
			}
		}
		if(params.isBlank("vehicleType")){
			throw new BizException(1500000,"车辆类型");
		}
		params.add("vehicleType",params.getInt("vehicleType"));
		if(params.isNotBlank("loadNo")){
			if(!ValidateUtils.Z_index(params.getString("loadNo"))){
				throw new BizException(1502006,"核载人数");
			}
		}
		if(params.isNotBlank("totalWeight")){
			if(!ValidateUtils.Number(params.getString("totalWeight"))){
				throw new BizException(1502006,"总质量");
			}
		}
		if(params.isNotBlank("curbWeight")){
			if(!ValidateUtils.Number(params.getString("curbWeight"))){
				throw new BizException(1502006,"整备质量");
			}
		}
		if(params.isNotBlank("loadWeight")){
			if(!ValidateUtils.Number(params.getString("loadWeight"))){
				throw new BizException(1502006,"核定载质量");
			}
		}
		if(params.isNotBlank("mileage")){
			if(!ValidateUtils.Number(params.getString("mileage"))){
				throw new BizException(1502006,"里程数");
			}
		}
		if(params.isBlank("belongsType")){
			throw new BizException(1500000,"所属类型");
		}
		params.add("belongsType",params.getInt("belongsType"));
		if(params.isBlank("usingRoperty")){
			throw new BizException(1500000,"使用性质");
		}
		params.add("usingRoperty",params.getInt("usingRoperty"));
		if(params.isBlank("operationRoperty")){
			throw new BizException(1500000,"运营性质");
		}
		if(params.isBlank("orgId")){
			throw new BizException(1500000,"运营商");
		}
		params.add("operationRoperty",params.getInt("operationRoperty"));
		if(vehicleMapper.getIsExistByLicense(params)>0){
			throw new BizException(1500001,"车牌号");
		}
		params.add("createBy",params.getInt("userId"));
		if (file != null && !file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String imageFileName = "images/" + new Date().getTime() + fileName.substring(fileName.lastIndexOf("."));
			OSSUploadFileUtils.toUploadPictureFile(file, imageFileName);
			params.put("drivingUrl", imageFileName);
		}
		vehicleMapper.insertVehicle(params);
		if(params.isNotBlank("driverIds")){
			String[] driverIds = params.getString("driverIds").split(",");
			if(null!=driverIds && driverIds.length>0){
				for(String driverId:driverIds){
					params.put("driverId", Integer.parseInt(driverId));
					vehicleMapper.insertVehDriRels(params);
				}
			  }
		   }
		logService.insertLog("新增车辆操作", OperateType.add.OperateName, "车辆新增", params.getInt("userId"));
	}
	/**
	 * 更新车辆信息
	 * @param map
	 * @return 
	 * 2017年5月12日 
	 * gaohui
	 * 2.0.0
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateVehicle(Map map,MultipartFile file)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("vehicleId")){
			throw new BizException(1500000,"车辆id");
		}
		if(params.isBlank("userId")){
			throw new BizException(1500000,"创建用户");
		}
		if(params.isNotBlank("belongsOrgId")){
			params.put("belongsOrgId", params.getInt("belongsOrgId"));
		}
		if(params.isBlank("licensePlate")){
			throw new BizException(1500000,"车牌号");
		}else if(params.getString("licensePlate").length()>16){
			throw new BizException(1502005,"车牌号");
		}else if(!isVehicleNumber(params.getString("licensePlate"))){
			throw new BizException(1502009,"车牌号");
		}
		if(params.isNotBlank("vin")){
			if(params.getString("vin").length()!=17){
				throw new BizException(1502009,"vin码");
			}
		}
		if(params.isBlank("belongsType")){
			throw new BizException(1500000,"所属类型");
		}
		
		if(params.isBlank("usingRoperty")){
			throw new BizException(1500000,"使用性质");
		}
		if(params.isBlank("operationRoperty")){
			throw new BizException(1500000,"运营性质");
		}
		params.put("operationRoperty",params.getInt("operationRoperty"));
		if(params.isNotBlank("initMileage")){
			if(!ValidateUtils.Number(params.getString("initMileage"))){
				throw new BizException(1502006,"初始里程数");
			}
		}
		if(params.isBlank("vehicleType")){
			throw new BizException(1500000,"车辆类型");
		}
		if(params.isNotBlank("loadNo")){
			if(!ValidateUtils.Z_index(params.getString("loadNo"))){
				throw new BizException(1502006,"核载人数");
			}
		}
		if(params.isNotBlank("loadWeight")){
			if(!ValidateUtils.Number(params.getString("loadWeight"))){
				throw new BizException(1502006,"核定载质量");
			}
		}
		if(params.isNotBlank("totalWeight")){
			if(!ValidateUtils.Number(params.getString("totalWeight"))){
				throw new BizException(1502006,"总质量");
			}
		}
		if(params.isNotBlank("curbWeight")){
			if(!ValidateUtils.Number(params.getString("curbWeight"))){
				throw new BizException(1502006,"整备质量");
			}
		}
		if(params.isNotBlank("mileage")){
			if(!ValidateUtils.Number(params.getString("mileage"))){
				throw new BizException(1502006,"里程数");
			}
		}
		if(params.isBlank("orgId")){
			throw new BizException(1500000,"运营商");
		}
		if(vehicleMapper.getIsExistByLicense(params)>0){
			throw new BizException(1500001,"车辆牌号");
		}
		params.put("updateBy",params.getInt("userId"));
		String drivingUrl = "";
		if(params.isNotBlank("drivingUrl")){
			drivingUrl = params.getString("drivingUrl");
			if (drivingUrl.contains(CommonUtils.PATH)){
				params.put("drivingUrl",drivingUrl.substring(CommonUtils.PATH.length(),drivingUrl.length()));
			}
		}else{
			params.put("drivingUrl","");
		}
		if (file != null && !file.isEmpty()){                  
			String imageFileName = null;
			drivingUrl = params.getString("drivingUrl");
			if (StringUtils.isBlank(drivingUrl)) {
				String fileName = file.getOriginalFilename();
				imageFileName = "images/" + CommonUtils.randomNum() + fileName.substring(fileName.lastIndexOf("."));
				params.put("drivingUrl", imageFileName);
			} else {
				imageFileName = drivingUrl;
			}
			OSSUploadFileUtils.toUploadPictureFile(file, imageFileName);
		}
		vehicleMapper.updateVehicle(params);
		Set<Integer> vehicleIds = new HashSet<Integer>();
		vehicleIds.add(params.getInt("vehicleId"));
		params.put("vehicleIds", vehicleIds);
		vehicleMapper.deleteVehDriRelByVehId(params); 
		if(params.isNotBlank("driverIds")){
		String[] driverIds = params.getString("driverIds").split(",");//配合前端啊！
		if(null != driverIds && driverIds.length > 0){
			for(String driverId:driverIds){
				params.put("driverId", Integer.parseInt(driverId));
				vehicleMapper.insertVehDriRels(params);
			}
		  }
	   }
	   logService.insertLog("更新车辆操作", OperateType.edit.OperateName, "车辆更新", params.getInt("userId"));
	}
	/**
	 * 根据驾驶员id和车辆id删除车辆与驾驶员信息
	 * gaohui
	 * 2017年5月12日
	 * 2.0.0
	 */
	public void deleteVehDriRelBy2Id(Map map)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("vehicleId")){
			throw new BizException(1500000,"车辆id");
		}
		if(params.isBlank("driverId")){
			throw new BizException(1500000,"驾驶员id");
		}
		vehicleMapper.deleteVehDriRelBy2Id(map);
	}
	
	/**
	 * 查询车辆会员列表
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月7日
	 */
	public List<DataVo> getMemberByVehicleId(Integer vehicleId) throws BizException{
		List<DataVo> memberList = vehicleMapper.getVehMemRelByVehicleId(vehicleId);
		Map<String,Object> billMap = new HashMap();
		Set<Integer> memberIds = null;
		if(null != memberList && memberList.size() > 0){
			memberIds = new HashSet();
			for(DataVo memberVo : memberList){
				memberIds.add(memberVo.getInt("consId"));
			}
		}
		if(null != memberIds && memberIds.size() > 0){
			Map memMap = new HashMap();
			memMap.put("consId", memberIds);
			List<DataVo> billList = vehicleMapper.getBillSum(memMap);
			if(null != billList && billList.size() > 0){
				for(DataVo billVo : billList){
					String consId = billVo.getString("consId");
					if(!billMap.containsKey(consId)){
						billMap.put(consId, billVo.getDouble("sum", 0.00));
					}
				}
			}
		}
		if(null != memberList && memberList.size() > 0){
			List<ComboxVo> hylbs = dictService.getDictByType("hylb");
			for(DataVo memberVo : memberList){
				String consId = memberVo.getString("consId");
				String consTypeCode = memberVo.getString("consTypeCode");
				if(null != billMap.get(consId)){
					memberVo.put("consSum", billMap.get(consId));
				}else{
					memberVo.put("consSum",0);
				}
				for(ComboxVo hylb:hylbs){
					if(hylb.getId().equals(consTypeCode)){
						memberVo.put("consTypeCode", hylb.getText());
						continue;
					}
				}
			}
		}
		return memberList;
	}
	/**
	 * 车品牌业务字典
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月24日
	 */
	public List<ComboxVo> getVehicleBrandDicts()throws BizException{
		List<ComboxVo> dicts = null;
		List<DataVo> brands = vehicleMapper.getVehicleBrands();
		if(null != brands && brands.size() > 0){
			dicts = new ArrayList<ComboxVo>();
			for(DataVo brandVo : brands){
				String id = brandVo.getString("id");
				String brandName = brandVo.getString("brandName");
				ComboxVo comboxVo = new ComboxVo(id,brandName);
				dicts.add(comboxVo);
			}
		}
		return dicts; 
	}
	/**
	 * 车型号业务字典
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月24日
	 */
	public List<ComboxVo> getVehicleModelDicts(Map map)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("brandId")){
			throw new BizException(1500000,"车辆品牌id");
		}
		List<ComboxVo> dicts = null;
		List<DataVo> models = vehicleMapper.getVehicleModels(map);
		if(null != models && models.size() > 0){
			dicts = new ArrayList<ComboxVo>();
			for(DataVo modelVo : models){
				String id = modelVo.getString("id");
				String modelName = modelVo.getString("modelName");
				ComboxVo comboxVo = new ComboxVo(id,modelName);
				dicts.add(comboxVo);
			}
		}
		return dicts; 
	}
	/**
	 * 批量导入车辆信息
	 * gaohui
	 * 2017年5月16日
	 * 2.0.0
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<Map> importVehicles(MultipartFile excelFile,Map map) throws Exception {
		List<Map> result = new ArrayList();
		List<Object[]> excelList = parseExcel(excelFile);
		DataVo params = new DataVo(map);
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		Set<Integer> stationIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		Map conOrgMap = new HashMap();
		Map<String,Integer> orgMap = new HashMap();
		Map<String,List<Map>> statMap = new HashMap();
		Map<String,Object> cardMap = new HashMap();
		Map<String,String> brandMap = new HashMap();
		Map<String,List<Map>> modelMap = new HashMap();
		if(null!=orgIds && orgIds.size()>0){
			conOrgMap.put("orgIds", orgIds);
		}
		List<DataVo> orgList = vehicleMapper.getOrgList(conOrgMap);//获取用户的企业列表--企业名称-企业id
		List<DataVo> memberList = vehicleMapper.getMemberList(conOrgMap);//查询会员信息
		List<DataVo> cardList = vehicleMapper.getCardList(conOrgMap);//卡信息
		List<DataVo> brands = vehicleMapper.getVehicleBrands();
		List<DataVo> models = vehicleMapper.getVehicleModels(null);
		if(null != models && models.size() > 0){
			for(DataVo modelVo : models){
				String brandId = modelVo.getString("brandId");
				if(!modelMap.containsKey(brandId)){
					List<Map> temp = new ArrayList<Map>();
					temp.add(modelVo);
					modelMap.put(brandId, temp);
				}else{
					modelMap.get(brandId).add(modelVo);
				}
			}
		}
		if(null != brands && brands.size() > 0){
			for(DataVo brandVo : brands){
				String brandName = brandVo.getString("brandName");
				String id = brandVo.getString("id");
				if(!brandMap.containsKey(brandName)){
					brandMap.put(brandName, id);
				}
			}
		}
		if(null != cardList && cardList.size() > 0){
			for(DataVo cardVo : cardList){
				String cardId = cardVo.getString("cardId");
				String consId = cardVo.getString("consId");
				if(!cardMap.containsKey(cardId)){
					cardMap.put(cardId,consId);
				}
			}
		}
		
		if(null != orgList && orgList.size() > 0){
			orgMap = getOrgIdMap(orgList);
		}
		if(null != stationIds && stationIds.size() > 0){
			conOrgMap.put("stationIds", stationIds);
		}
		List<DataVo> stationList = vehicleMapper.getStationList(conOrgMap);//获取用户的企业列表--企业名称-企业id
		if(null != stationList && stationList.size() > 0){
			for(DataVo stationVo : stationList){
				String orgId = stationVo.getString("orgId");
				if(!statMap.containsKey(orgId)){
					List<Map> temp = new ArrayList();
					temp.add(stationVo);
					statMap.put(orgId, temp);
				}else{
					statMap.get(orgId).add(stationVo);
				}
			}
		}
		boolean flags = true;
		List<Map<String,Object>> licensePlates = new ArrayList();
		List<DataVo> vehicleList = new ArrayList();
		if (CollectionUtils.isNotEmpty(excelList)) {
			List<ComboxVo> vehicleTypes = dictService.getDictByType("cllx");//车辆类型
			List<ComboxVo> belongsTypes = dictService.getDictByType("clsslx");//车辆所属类型
			List<ComboxVo> usingRopertys = dictService.getDictByType("clsyxz");//clyyxz
			List<ComboxVo> operationRopertys = dictService.getDictByType("clyyxz");//车辆运营性质
			List<ComboxVo> memberTypes = dictService.getDictByType("hylb");//会员类别
			for (int i = 0; i < excelList.size(); i++) {
				boolean flag = true;
				DataVo vehicle = new DataVo();
				Map<String,Object> licenseMap = new HashMap<String, Object>();
				Object[] obj = excelList.get(i);
				String desc = "";
				//车牌号
				if (obj[0] == null || "".equals(obj[0])) {
					if ("".equals(desc)) {
						desc = "校验失败:车牌号不能为空";
					}
					flag = false;
				}else if(obj[0].toString().length()>16){
					if ("".equals(desc)) {
						desc = "校验失败:车牌长度不能大于16";
					}
					flag = false;
				}else if(!ValidateUtils.isConSpeCharacters(obj[0].toString())){
					if ("".equals(desc)) {
						desc = "校验失败:车牌号格式错误！";
					}
					flag = false;
				}else if(!isVehicleNumber(obj[0].toString())){
					if ("".equals(desc)) {
						desc = "校验失败:车牌号格式错误！";
					}
					flag = false;
				}else{
					licenseMap.put("licensePlate", obj[0].toString());
				}
				vehicle.put("licensePlate", String.valueOf(obj[0]));
				//vin
				if(obj[1] != null && !"".equals(obj[1])){
					if(obj[1].toString().length()>32){
						if ("".equals(desc)) {
							desc = "校验失败:vin码长度不能大于32";
						}
						flag = false;
					}
				}
				//车辆类型
				if (obj[8] == null || "".equals(obj[8])) {
					if ("".equals(desc)) {
						desc = "校验失败:车辆类型不能为空";
					}
					flag = false;
				}else{
					for(ComboxVo comboxVo:vehicleTypes){
						if(comboxVo.getText().equals(obj[8].toString().trim())){
							vehicle.put("vehicleType",comboxVo.getId());
							break;
						}
					}
				}
				vehicle.put("rVehicleType", String.valueOf(obj[8]));
				//车辆所属类型
				if (obj[14] == null || "".equals(obj[14])) {
					if ("".equals(desc)) {
						desc = "校验失败:车辆所属类型不能为空";
					}
					flag = false;
				}else{
					for(ComboxVo comboxVo:belongsTypes){
						if(comboxVo.getText().equals(obj[14].toString().trim())){
							vehicle.put("belongsType",comboxVo.getId());
							break;
						}
					}
					if("社会车辆".equals(obj[14].toString())){
						if(null!=obj[15] && !"".equals(obj[15])){
							vehicle.put("belongsName", obj[15].toString());
						}
					}else if("企业车辆".equals(obj[14].toString())){
						if(null != obj[15] && !"".equals(obj[15])){
							Integer tempOrgId = orgMap.get(obj[15].toString()+"");
							if(null != tempOrgId){
								vehicle.put("belongsOrgId", tempOrgId);//企业车辆的话----获取orgId
								if(obj[17] != null && !"".equals(obj[17])){//如果所属场站不为空
									if(null != statMap.get(tempOrgId+"")){
										if(null != getIsConList(statMap.get(tempOrgId+""),obj[17].toString())){
											vehicle.put("stationId",getIsConList(statMap.get(tempOrgId+""),obj[17].toString()));//企业车辆的话----获取orgId
										}else{
											if ("".equals(desc)) {
												desc = "校验失败:"+obj[15].toString()+"下不存在场站:"+obj[17].toString();
											}
											flag = false;
										}
									}else{
										if ("".equals(desc)) {
											desc = "校验失败:"+obj[15].toString()+"下不存在场站:"+obj[17].toString();
										}
										flag = false;
									}
									vehicle.put("stationName", String.valueOf(obj[17]));
								}
							}else{
								if ("".equals(desc)) {
									desc = "校验失败:所有人(企业)不存在！";
								}
								flag = false;
							}
							vehicle.put("rBelongsOrgName", String.valueOf(obj[15]));
						}
					}
				}
				vehicle.put("rBelongsType", String.valueOf(obj[14]));
				if(obj[16] == null || "".equals(obj[16])){
					if ("".equals(desc)) {
						desc = "校验失败:所属企业不能为空！";
					}
					flag = false;
				}else{
					if(null != orgMap.get(obj[16].toString())){
						vehicle.put("orgId",orgMap.get(obj[16].toString())+"");
						licenseMap.put("orgId",orgMap.get(obj[16].toString())+"");
					}else{
						if ("".equals(desc)) {
							desc = "校验失败:用户没有导入"+obj[16].toString()+"的权限！";
						}
						flag = false;
					}
				}
				licensePlates.add(licenseMap);
				vehicle.put("orgName", String.valueOf(obj[16]));
				//车辆使用性质
				if (obj[20] == null || "".equals(obj[20])) {
					if ("".equals(desc)) {
						desc = "校验失败:车辆使用性质不能为空";
					}
					flag = false;
				}else{
					for(ComboxVo comboxVo:usingRopertys){
						if(comboxVo.getText().equals(obj[20].toString().trim())){
							vehicle.put("usingRoperty",comboxVo.getId());
							break;
						}
					}
				}
				vehicle.put("rUsingRoperty", String.valueOf(obj[20]));
				//车辆营运性质
				if (obj[21] == null || "".equals(obj[21])) {
					if ("".equals(desc)) {
						desc = "校验失败:车辆营运性质不能为空";
					}
					flag = false;
				}else{
					for(ComboxVo comboxVo:operationRopertys){
						if(comboxVo.getText().equals(obj[21].toString().trim())){
							vehicle.put("operationRoperty",comboxVo.getId());
							break;
						}
					}
				}
				vehicle.put("rOperationRoperty",String.valueOf(obj[21]));
				
				if(null != obj[1] && !"".equals(obj[1])){
					if(obj[1].toString().length() != 17){
						if ("".equals(desc)) {
							desc = "校验失败:vin码长度错误！";
						}
						flag = false;
					}
					vehicle.put("vin",obj[1].toString());//win 码
				}
				if(null != obj[2] && !"".equals(obj[2])){
					vehicle.put("engineNo",obj[2].toString());//发动机号
				}
				if(null != obj[3] && !"".equals(obj[3])){
					vehicle.put("manufacturer",obj[3].toString());//生产厂家
				}
				if(null != obj[4] && !"".equals(obj[4])){
					if(null != brandMap.get(obj[4].toString())){
						vehicle.put("brand",brandMap.get(obj[4].toString()));//品牌
					}else{
						if ("".equals(desc)) {
							desc = "校验失败:品牌"+obj[4].toString()+"不存在，请联系管理员！";
						}
						flag = false;
					}
					vehicle.put("rBrand",obj[4].toString());//品牌
				}
				if(null != obj[5] && !"".equals(obj[5])){
					if(null != obj[4] && !"".equals(obj[4])){
						if(null != modelMap.get(vehicle.get("brand"))){
							List<Map> temps = modelMap.get(vehicle.get("brand"));
							boolean f = false; 
							if(temps.size()>0){
								for(Map temp:temps){
									DataVo tempVo = new DataVo(temp);
									if(obj[5].equals(tempVo.getString("modelName"))){
										vehicle.put("model",tempVo.getString("id"));//品牌
										f = true;
										continue;
									}
								}
							}
							if(!f){
								if ("".equals(desc)) {
									desc = "校验失败:此车辆品牌下不存在此车类型，请联系管理员！";
								}
								flag = false;
							}
						}else{
							if ("".equals(desc)) {
								desc = "校验失败:此车辆品牌下不存在此车类型，请联系管理员！";
							}
							flag = false;
						}
					}else{
						if ("".equals(desc)) {
							desc = "校验失败:车辆品牌不能为空！";
						}
						flag = false;
					}
					vehicle.put("rModel",obj[5].toString());//品牌
				}
				if(null != obj[6] && !"".equals(obj[6])){
					vehicle.put("color",obj[6].toString());//颜色
				}
				if(null != obj[13] && !"".equals(obj[13])){
					vehicle.put("vehicleSize",obj[13].toString());//外郭尺寸
				}
				if(null != obj[18] && !"".equals(obj[18])){
					vehicle.put("onNumber",obj[18].toString());//自编号
				}
				if(null != obj[19] && !"".equals(obj[19])){
					vehicle.put("line",obj[19].toString());//线路
				}
				if(null != obj[22] && !"".equals(obj[22])){
					vehicle.put("remark",obj[22].toString());//备注
				}
				//数据库类型处理
				if(null != obj[7] && !"".equals(obj[7])){
					if(!ValidateUtils.Number(obj[7].toString())){
						throw new BizException(1502006,"初始里程数");
					}
					vehicle.put("initMileage",obj[7].toString());
				}
				if(null != obj[9] && !"".equals(obj[9])){
					if(!ValidateUtils.Number(obj[9].toString())){
						throw new BizException(1502006,"核载人数");
					}
					vehicle.put("loadNo",obj[9].toString());
				}
				if(null != obj[10] && !"".equals(obj[10])){
					if(!ValidateUtils.Number(obj[10].toString())){
						throw new BizException(1502006,"总质量");
					}
					vehicle.put("totalWeight",obj[10].toString());
				}
				if(null != obj[11] && !"".equals(obj[11])){
					if(!ValidateUtils.Number(obj[11].toString())){
						throw new BizException(1502006,"整备质量");
					}
					vehicle.put("curbWeight",obj[11].toString());
				}
				if(null != obj[12] && !"".equals(obj[12])){
					if(!ValidateUtils.Number(obj[12].toString())){
						throw new BizException(1502006,"核定载质量");
					}
					vehicle.put("loadWeight",obj[12].toString());
				}
				//新增会员信息
				if(null != obj[26] && !"".equals(obj[26])){//会员电话号码不为空的情况
					// 根据会员电话号码和orgId唯一确定会员id(consId)
					vehicle.put("memberType", String.valueOf(obj[23]));
					vehicle.put("groupName",  String.valueOf(obj[24]));
					vehicle.put("consName", String.valueOf(obj[25]));
					vehicle.put("memberPhone", String.valueOf(obj[26]));
					vehicle.put("reCardId", String.valueOf(obj[27]));
					boolean f = false;
					if(CollectionUtils.isNotEmpty(memberList)){//此用户权限下存在相应的会员
						for(Map member : memberList){
							DataVo memberVo = new DataVo(member);
							if(memberVo.getString("orgId").equals(orgMap.get(obj[16].toString())+"")&& memberVo.getString("consPhone").equals(obj[26].toString())){//相同的企业下存会员的情况下
								f = true;
								if(null != obj[23] && !"".equals(obj[23])){//会员类型------字典
									//会员类型匹配
									boolean f1 = false;
									for(ComboxVo comboxVo:memberTypes){
										if(comboxVo.getText().equals(obj[23].toString().trim())){
											if(comboxVo.getId().equals(memberVo.getString("consTypeCode"))){
												f1= true;
												break;
											}
										}
									}
									if(!f1){
										if ("".equals(desc)) {
											desc = "校验失败:会员类型不匹配！";
										}
										flag = false;
									}
								}
								if(null != obj[24] && !"".equals(obj[24])){//集团名
									//集团名匹配
									if(!obj[24].toString().equals(memberVo.getString("groupName"))){
										if ("".equals(desc)) {
											desc = "校验失败:集团名不匹配！";
										}
										flag = false;
									}
								}
								if(null != obj[25] && !"".equals(obj[25])){//会员名
									//会员名匹配
									if(!obj[25].toString().equals(memberVo.getString("consName"))){
										if ("".equals(desc)) {
											desc = "校验失败:会员名不匹配！";
										}
										flag = false;
									}
								}
								vehicle.put("consId", memberVo.getString("consId"));
								if(null != obj[27] && !"".equals(obj[27])){//储值卡号 -------------更新卡表里面的车辆id
									if(null != cardMap.get(obj[27].toString())){//卡号不存在
										if(memberVo.getString("consId").equals(cardMap.get(obj[27].toString()))){
											vehicle.put("cardId", obj[27].toString());
										}else{
											if ("".equals(desc)) {
												desc = "校验失败:卡号与会员不匹配！";
											}
											flag = false;
										}
									}else{
										if ("".equals(desc)) {
											desc = "校验失败:卡号不存！";
										}
										flag = false;
									}
								}
								break;
							}
						}
					}else{
						if ("".equals(desc)) {
							desc = "校验失败:用户没有导入此会员的权限！";
						}
						flag = false;
					}
					if(!f){
						if ("".equals(desc)) {
							desc = "校验失败:此会员不存在！";
						}
						flag = false;
					}
				}
				if (!flag) {
					vehicle.put(DESCRIPTION, desc);
					result.add(vehicle);
					flags = false;
				}
				vehicleList.add(vehicle);
			}
			vehicleTypes = null;//车辆类型
			belongsTypes = null;//车辆所属类型
			usingRopertys = null;//clyyxz
			operationRopertys = null;//车辆运营性质
			memberTypes = null;//会员类别
		}
		if (flags) {
			Map Licenses = new HashMap();
			Map<String,Object> licensePlatesMap = new HashMap<String, Object>();
			Licenses.put("licensePlates", licensePlates);
			List<DataVo> tempVehicles = vehicleMapper.getVehiclesByLicenses(Licenses);
			if(CollectionUtils.isNotEmpty(tempVehicles)){//已车辆ID为key----车辆信息为value
				for(DataVo tempVo : tempVehicles){
					String licensePlate = tempVo.getString("licensePlate");
					String orgId = tempVo.getString("orgId");
					if(!licensePlatesMap.containsKey(licensePlate+orgId)){
						licensePlatesMap.put(licensePlate+orgId+"", tempVo);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(vehicleList)){
				for(DataVo vehicleVo : vehicleList){
					String licensePlate = vehicleVo.getString("licensePlate");
					String orgId = vehicleVo.getString("orgId");
					String cardId = vehicleVo.getString("cardId");
					String vehicleId = vehicleVo.getString("vehicleId");
					if(!licensePlatesMap.containsKey(licensePlate+orgId)){
						vehicleVo.put("createBy", params.getInt("userId"));
						vehicleMapper.insertVehicle(vehicleVo);//新增车辆
					}else{
						vehicleVo.put("updateBy", params.getInt("userId"));
						vehicleMapper.updateVehicleByPlate(vehicleVo);
					}
					if(vehicleVo.isNotBlank("consId")){//会员校验通过
						if(vehicleVo.isBlank("vehicleId")){
							vehicleVo.put("vehicleId", ((DataVo)licensePlatesMap.get(licensePlate+orgId+"")).getString("vehicleId"));
						}
						if(vehicleVo.isNotBlank("cardId")){
							//更新卡片表中的车辆id字段
							Map cardVeh = new HashMap();
							cardVeh.put("cardId", cardId);
							cardVeh.put("vehicleId", vehicleId);
							vehicleMapper.updateCardVehicle(cardVeh);
						}else{
							vehicleMapper.insertVehMemRels(vehicleVo);//------------暂时定位新增车辆和会员的关系
							//更新常用车辆
							vehicleMapper.updateConsVehicle(vehicleVo);
						}
					}
				}
				licensePlatesMap = null;
			}
			conOrgMap = null;
			statMap = null;
			cardMap = null;
			brandMap = null;
			modelMap = null;
			orgList = null;//获取用户的企业列表--企业名称-企业id
			memberList = null;//查询会员信息
			cardList = null;//卡信息
			brands = null;
			models = null;
			vehicleList = null;
		}
		return result;
	}
	
	private Integer getIsConList(List<Map> list,String name){
		Integer id = null;
		if(CollectionUtils.isNotEmpty(list)){
			for(Map map : list){
				DataVo mapVo  = new DataVo(map);
				if(name.equals(mapVo.getString("name"))){
					id = mapVo.getInt("id");
					continue;
				}
			}
		}
		return id;
	}
	public Map<String,Integer> getOrgIdMap(List<DataVo> list){
		Map<String,Integer> result = new HashMap();
		if(CollectionUtils.isNotEmpty(list)){
			for(DataVo tempVo : list){
				String name  = tempVo.getString("name");
				int id = tempVo.getInt("id");
				if(!result.containsKey(name)){
					result.put(name.trim(), id);
				}
			}
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
	public static List<Object[]> parseExcel(MultipartFile fileName)throws Exception {
		List<Object[]> result = new ArrayList();
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(fileName.getInputStream());
		} catch (Exception e) {
			wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook(fileName.getInputStream());
		}
		Sheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		if (rows < 3) {
			throw new BizException(1500008);
		}
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			if (!isRowEmpty(row)) {
				int cells = 28;//-----------
				if (i <= 1) {// 首行表头过滤
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
	/**
	 * 车牌正则表达式  --- 本想加入boot 但---
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月16日
	 */
	private static boolean isVehicleNumber(String vehicleNumber) {
		  boolean flag = false;
	      String express1 = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4,5}[A-Z0-9挂学警港澳]{1}$";
	      Pattern pattern = Pattern.compile(express1);
	      Matcher matcher = pattern.matcher(vehicleNumber);
	      flag =  matcher.matches();
	      return flag;
	}
	/**
	 * EXCEL row 空判断
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年8月16日
	 */
	public static boolean isRowEmpty(Row row) {
	    for (int c = row.getFirstCellNum(); c < 28; c++) {//车辆模板的限制---只能暂时写死
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
	            return false;
	    }
	    return true;
	} 
}


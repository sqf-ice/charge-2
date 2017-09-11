package com.clouyun.charge.modules.document.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.OSSUploadFileUtils;
import com.clouyun.charge.modules.document.mapper.ConTractMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageInfo;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述: ConTractService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月25日
 */
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ConTractService {
	
	@Autowired
	ConTractMapper conTractMapper;
	
	@Autowired
	DictService dictService;
	
	@Autowired
	UserService userService;
	/**
	 * 查询合约列表
	 * @throws BizException 
	 */
	public PageInfo getConTractAll(Map map,String type) throws BizException{
		Set<Integer> tractIds = new HashSet();
		List yjList = null;
		DataVo vo = new DataVo(map);
		String stationName = "";
		if(vo.isNotBlank("stationName")){
			stationName = vo.getString("stationName");
		}
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		Integer pageSize =  50;
		Integer pageNum =  0;
		if(vo.isNotBlank("pageSize")){
			pageSize = vo.getInt("pageSize");
		}
		if(vo.isNotBlank("pageNum")){
			pageNum = vo.getInt("pageNum");
		}
		DataVo userVo = userService.findUserById(vo.getInt("userId"));
		//合约企业登录访问  获取该合约企业所签订的合约  此时没有月结合约
		if(userVo.isNotBlank("userType") && "02".equals(userVo.getString("userType"))){
			if(userVo.isNotBlank("companyId")){
				List<Integer> companyIds = new ArrayList<Integer>();
				Integer companyId = userVo.getInt("companyId");
				List<DataVo> tractIdList = conTractMapper.getTractByCompanyId(companyId);
				for (int i = 0; i < tractIdList.size(); i++) {
					DataVo dv = tractIdList.get(i);
					companyIds.add(dv.getInt("contractId"));
				}
				vo.put("tractIds", companyIds);
			}
		}else{
			//根据登录用户获取场站集合
			Set<Integer> stationIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.STATION.dataType);
			if(stationIds != null){
				vo.put("stationIds", stationIds);
			}
			vo.remove("pageSize");
			vo.remove("pageNum");
			yjList = conTractMapper.getConTractStationRelaByTractId(vo); //查询月结合约
		}
		List<Integer> conTractTypes = new ArrayList<Integer>();
		conTractTypes.add(0);
		conTractTypes.add(2);
		vo.put("conTractTypes", conTractTypes);
		List list = conTractMapper.getConTractAll(vo);
		
		
		
		
		Integer contractId = null ;
		if(yjList !=null ){
			for (int i = 0; i < yjList.size(); i++) {
				Map yjMap = (Map) yjList.get(i);
				contractId = Integer.valueOf(yjMap.get("contractId").toString());
				tractIds.add(contractId);
			}
		}
		if(list != null && yjList != null){
			for (int i = 0; i < list.size(); i++) {
				Map tractMap = (Map) list.get(i);
				contractId = Integer.valueOf(tractMap.get("contractId").toString());
				tractIds.add(contractId);
			}
		}
		if(tractIds.size()>0){
			vo = new DataVo();
			vo.set("tractIds", tractIds);
			if(!"export".equals(type)){
				vo.put("pageSize", pageSize);
				vo.put("pageNum", pageNum);
			}
			list = conTractMapper.getConTractAll(vo);
		}
		if(list != null ){
			for (int i = 0; i < list.size(); i++) {
				Map tractMap = (Map) list.get(i);
				if(tractMap.get("stationId") == null || "".equals(tractMap.get("stationId").toString())){
					continue;
				}
				Integer stationId =Integer.valueOf(tractMap.get("stationId").toString());
				if(stationId.intValue() == -1){
					Integer tractId  = Integer.valueOf(tractMap.get("contractId").toString());
					stationName = tractMap.get("stationName")!=null?tractMap.get("stationName").toString():"";
					if(yjList != null){
						for (int j = 0; j < yjList.size(); j++) {
							Map yjMap = (Map) yjList.get(j);
							contractId = Integer.valueOf(yjMap.get("contractId").toString());
							if(contractId.intValue() == tractId.intValue()){
								String subName = yjMap.get("stationName")!=null?yjMap.get("stationName").toString():"";
								if(subName!=""){
									stationName = "".equals(stationName)?stationName+subName : stationName+","+subName;
								}
							}
						}
					}
					tractMap.put("stationName", stationName);
				}
			}
		}
		return new PageInfo(list);
	}
	
	/**
	 * 根据合约Id查询合约信息
	 */
	public Map getTractById(Integer tractId) throws BizException{
		if(tractId ==null ){
			throw new BizException(1102001, "合约id");
		}
		DataVo vo = new DataVo();
		vo.put("tractId", tractId);
		Map map = conTractMapper.getConTractById(vo);
		if(map!=null){
			if(null != map.get("contractFile")  && !"".equals(map.get("contractFile").toString())){
				map.put("fileUrl", CommonUtils.PATH+map.get("contractFile").toString());
			}else{
				map.put("fileUrl", "");
			}
			map.put("userAppId", map.get("userId"));
			List list = conTractMapper.getConTractCompanyById(vo);
			if(list != null && list.size()>0){
				  for (int i = 0; i < list.size(); i++) {
					   Map stationMap = (Map) list.get(i);
					   stationMap.put("groupId", stationMap.get("trGroupId"));
					   stationMap.put("cConCompanyId", stationMap.get("trCompanyId"));
				   }
			}
			map.put("conTractCompanyList", list);
			Integer catractType = map.get("contractType")!=null && !"".equals(map.get("contractType").toString())?Integer.valueOf(map.get("contractType").toString()):-1;
			if(catractType.intValue() == 1){ //月结合约
			   list = conTractMapper.getConTractStationRelaByTractId(vo);
			   if(list != null && list.size()>0){
				   for (int i = 0; i < list.size(); i++) {
					   Map stationMap = (Map) list.get(i);
					   stationMap.put("id", stationMap.get("stationId"));
				   }
			   }
			   map.put("stationList", list);
			}
		}
		return map;
	}
	
	
	/**
	 * 合约置为无效
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void dissConTract(List<Integer> tractIds) throws BizException{
		if(tractIds==null || tractIds.size()<=0){
			throw new BizException(1102001, "合约ids");
		}
		DataVo vo = new DataVo();
		vo.put("tractIds", tractIds);
		conTractMapper.dissConTract(vo);
	}
	
	/**
	 * 合约信息导出
	 */
	public void exportConTract(Map map,HttpServletResponse response) throws Exception{
		List list = getConTractAll(map,"export").getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		if(null == map.get("exportType")){
			throw new BizException(1102001, "导出合约类型");
		}
		Integer exportType = Integer.valueOf(map.get("exportType").toString());  // 0:合约管理列表  1:月结合约列表  2.收入合约列表  3.支出合约列表
		headList.add("合约名称");
		if(exportType == 0){
			headList.add("合约类型");
		}
		if(exportType == 2 || exportType == 3){
			headList.add("合约分类");
		}
		headList.add("场站名称");
		headList.add("有效期");
		headList.add("更新日期");
		headList.add("状态");
		headList.add("审批状态");
		if(exportType == 0){
			headList.add("审批人");
		}else{
			headList.add("申请人");
		}
		
		titleList.add("contractName");
		if(exportType == 0){
			titleList.add("contractType");
		}
		if(exportType == 2){
			titleList.add("contractIncomeType");
		}else if(exportType == 3){
			titleList.add("contractCostType");
		}
		titleList.add("stationName");
		titleList.add("contractExpiration");
		titleList.add("contractUpdateDate");
		titleList.add("contractStatus");
		titleList.add("contractApproveStatus");
		if(exportType == 0){
			titleList.add("userName");
		}else{
			titleList.add("contractUserName");
		}
		
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Map tractMap = (Map) list.get(i);
				if(tractMap.get("contractType")!=null && !"".equals(tractMap.get("contractType").toString())){
					tractMap.put("contractType",dictService.getDictLabel("hylx", tractMap.get("contractType").toString()));
				}
				if(tractMap.get("contractStatus")!=null && !"".equals(tractMap.get("contractStatus").toString())){
					tractMap.put("contractStatus",dictService.getDictLabel("zt", tractMap.get("contractStatus").toString()));
				}
				if(tractMap.get("contractApproveStatus")!=null && !"".equals(tractMap.get("contractApproveStatus").toString())){
					tractMap.put("contractApproveStatus",dictService.getDictLabel("hyspzt", tractMap.get("contractApproveStatus").toString()));
				}
				String contractExpiration ="";
				if(tractMap.get("contractExpirationStart")!=null && !"".equals(tractMap.get("contractExpirationStart").toString())){
					contractExpiration +=tractMap.get("contractExpirationStart").toString();
				}
				contractExpiration+="至";
				if(tractMap.get("contractExpirationEnd")!=null && !"".equals(tractMap.get("contractExpirationEnd").toString())){
					contractExpiration+=tractMap.get("contractExpirationEnd").toString();
				}
				tractMap.put("contractExpiration", contractExpiration);
				if(exportType == 2){
					if(tractMap.get("contractIncomeType")!=null && !"".equals(tractMap.get("contractIncomeType").toString())){
						tractMap.put("contractIncomeType", dictService.getDictLabel("srhylx", tractMap.get("contractIncomeType").toString()));
					}
				}else if(exportType == 3){
					if(tractMap.get("contractCostType")!=null && !"".equals(tractMap.get("contractCostType").toString())){
						tractMap.put("contractCostType", dictService.getDictLabel("zchylx", tractMap.get("contractCostType").toString()));
					}
				}
			}
		}
		String msg = "";
		switch (exportType) {
		case 1:
			msg ="月结";
			break;
		case 2:
			msg ="收入";
			break;
		case 3:
			msg ="支出";
			break;
		}
		ExportUtils.exportExcel(list, response, headList, titleList, msg+"合约信息");
	}
	
	/**
	 * 新增合约信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveConTract(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		getConTractValidate(vo,"save");
		vo.put("contractStatus", 0); //合约状态 无效
		vo.put("contractApproveStatus", 1);//合约审批状态 申请中
		//添加合约信息
		conTractMapper.saveConTract(vo);
		//添加合约关联表数据
		editData(vo,"save");
	}
	
	
	/**
	 * 编辑合约信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateConTract(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		getConTractValidate(vo,"update");
		//更新合约信息
		conTractMapper.updateConTract(vo);
		//添加合约关联表数据
		editData(vo, "update");
	}
	
	/**
	 * 数据校验及业务处理
	 * @throws BizException 
	 */
	private void getConTractValidate(DataVo vo,String type) throws BizException{
		if("update".equals(type)){
			if(vo.isBlank("contractId")){
				throw new BizException(1102001, "合约Id");
			}
		}
		if(vo.isBlank("contractType")){
			throw new BizException(1102001, "合约类型");
		}else if(vo.isBlank("contractName")){
			throw new BizException(1102001, "合约名称");
		}else if(vo.isBlank("contractPeriod")){
			throw new BizException(1102001, "合约结算周期");
		}else if(vo.isBlank("contractExpirationStart")){
			throw new BizException(1102001, "合约有效期开始");
		}else if(vo.isBlank("contractExpirationEnd")){
			throw new BizException(1102001, "合约有效期结束");
		}else if(vo.isBlank("contractDate")){
			throw new BizException(1102001, "合约结算日期");
		}else if(vo.isBlank("userId")){
			throw new BizException(1102001, "合约审批人");
		}
		String contractType = vo.getString("contractType");
		//合约类型为0.分成合约时 验证合约分成项目,单场站,合约企业不能为空  else 合约类型为1.月结合约时 验证多场站,集团不能为空
		if("0".equals(contractType)){
			if(vo.isBlank("contractShareType")){
				throw new BizException(1102001, "合约分成项目");
			}else if(vo.isBlank("stationId")){
				throw new BizException(1102004, "场站");
			}else if(vo.isBlank("companyIdStr")){
				throw new BizException(1102004, "合约企业");
			}
		}else if("1".equals(contractType)){
			if(vo.isBlank("stationIdStr")){
				throw new BizException(1102004, "场站");
			}else if(vo.isBlank("groupIdStr")){
				throw new BizException(1102004, "集团");
			}
			vo.put("stationId", -1);
		}
	}
	
	/**
	 * 添加合约关联表数据
	 */
	private void editData(DataVo vo,String type){
		DataVo dv = null;
		Integer contractId = vo.getInt("contractId");
		if("update".equals(type)){
			//删除关联关系表
			conTractMapper.delInTractCompany(vo);
			conTractMapper.delTractStation(vo);
		}
		//0:收入合约  1:月结合约  2:支出合约
		if(vo.getInt("contractType") != 1){ //分成合约
			String companyIdStr = vo.getString("companyIdStr");
			String[] idAges = companyIdStr.split(",");
			if(idAges != null && idAges.length>0){
				for (int i = 0; i < idAges.length; i++) {
					String[] idAge = idAges[i].split("_");
					dv = new DataVo();
					dv.put("contractId", contractId);
					dv.put("companyId", Integer.parseInt(idAge[0]));
					dv.put("percentage", Double.parseDouble(idAge[1]));
					conTractMapper.saveInTractCompany(dv);
				}
			}
		}else{
			String stationIdStr = vo.getString("stationIdStr");
			String[] stationIds = stationIdStr.split(",");
			//添加合约场站关联表信息
			for (int i = 0; i < stationIds.length; i++) {
				dv = new DataVo();
				dv.put("contractId", contractId);
				dv.put("stationId", Integer.parseInt(stationIds[i]));
				conTractMapper.saveTractStation(dv);
			}
			String groupIdStr = vo.getString("groupIdStr");
			String[] groupIds = groupIdStr.split(",");
			//添加月结合约 合约关联信息
			for (int i = 0; i < groupIds.length; i++) {
				dv = new DataVo();
				dv.put("contractId", contractId);
				dv.put("groupId", Integer.parseInt(groupIds[i]));
				conTractMapper.saveInTractCompany(dv);
			}
		}
	}
	
	/**
	 * 合约审批
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void contractApprove(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("contractId")){
			throw new BizException(1102001, "合约Id");
		}else if(vo.isBlank("contractApproveStatus")){
			throw new BizException(1102001, "合约审核状态");
		}
		if(vo.getInt("contractApproveStatus") == 0 && vo.isBlank("contractReason")){
			throw new BizException(1102001, "拒绝理由");
		}
		if(vo.getString("contractReason").length() > 100){
			throw new BizException(1102023);
		}
		if(vo.getInt("contractApproveStatus") == 1){
			vo.put("contractStatus", 0);
		}
		conTractMapper.updateAppConTract(vo);
	}
	
	
	
	
	
	//------------------------1.5.3--------------------------------------------\\
	//设置默认值为NULL  
	private List<String> list = Arrays.asList(new String[]{
			"contractType","stationId","userAppId","contractUpdateDate","contractPeriod","contractExpirationStart","contractExpirationEnd",
			"contractApproveStatus","contractStatus","contractShareType","contractServiceCharge","contractEfficiency","tractPrice","discount",
			"contractIncomeType","contractCostType","contractAmount","contractConsTotal","contractConsAmount","contractCardTotal"
			});
	
	/**
	 * 新增合约信息
	 * ver:1.5.3
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveConTract153(Map map,MultipartFile file) throws Exception{
		DataVo vo = new DataVo(map);
		Map paramMap =CommonUtils.convertDefaultVal(vo,list);
		getConTractValidate153(vo,file,"save");
		vo.put("contractStatus", 1); //合约状态 无效
		vo.put("contractApproveStatus", 2);//合约审批状态 申请中
		//添加合约信息
		conTractMapper.saveConTract(vo);
		//添加合约关联表数据
		editData(vo,"save");
	}
	

	/**
	 * 编辑合约信息
	 * ver:1.5.3
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateConTract153(Map map,MultipartFile file) throws Exception{
		DataVo vo = new DataVo(map);
		Map paramMap =CommonUtils.convertDefaultVal(vo,list);
		getConTractValidate153(vo,file,"update");
		//更新合约信息
		conTractMapper.updateConTract(paramMap);
		//添加合约关联表数据
		editData(vo, "update");
	}
	
	/**
	 * 数据校验及业务处理
	 * ver:1.5.3
	 */
	private void getConTractValidate153(DataVo vo,MultipartFile file,String type) throws Exception{
		
		DataVo checkVo = new DataVo();
		checkVo.put("contractStatus", 0);
		int count = 0;
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		if("update".equals(type)){
			if(vo.isBlank("contractId")){
				throw new BizException(1102001, "合约");
			}
			checkVo.put("contractId", vo.getInt("contractId"));
		}
		if(vo.isBlank("contractType")){
			throw new BizException(1102001, "合约类型");
		}else if(vo.isBlank("contractName")){
			throw new BizException(1102001, "合约名称");
		}
		checkVo.put("contractType", vo.getInt("contractType"));
		//收入合约
		if(vo.getInt("contractType") == 0){
			if(vo.isBlank("contractIncomeType")){
				throw new BizException(1102001,"收入合约类型");
			}
		}else if(vo.getInt("contractType") == 2){
			if(vo.isBlank("contractCostType")){
				throw new BizException(1102001,"支出合约类型");
			}
		}
		if(vo.isBlank("contractPeriod")){ 
			throw new BizException(1102001, "合约结算周期");
		}
		//按日/年不需要验证合约结算日期
		if(vo.getInt("contractPeriod") !=0 && vo.getInt("contractPeriod") != 2 && vo.isBlank("contractDate")){
			throw new BizException(1102001, "合约结算日期");
		}	
		if(vo.getInt("contractPeriod") == 2){
			vo.put("contractDate", "12-31");
		}
		if(vo.getInt("contractPeriod") == 0){
			vo.put("contractDate", "00");
		}
		if(vo.getInt("contractType") != 1){
			if(vo.getInt("contractType") == 0 && vo.isNotBlank("contractIncomeType")){//收入合约类型
				int contractIncomeType = vo.getInt("contractIncomeType");
				checkVo.put("contractIncomeType", contractIncomeType);
				//0.设备收入合约(合约) 1.设备运维合约(合约) 2.设备租赁合约(固定) 3.车位租赁合约(固定) 4.土地租赁合约(固定)
				if(contractIncomeType == 0 || contractIncomeType == 1){
					if(vo.isBlank("contractShareType")){
						throw new BizException(1102001, "合约分成项目");
					}
					//0-充电侧服务费  1-用电侧服务费
					if(vo.getInt("contractShareType") == 1){
						if(vo.isBlank("contractServiceCharge")){
							throw new BizException(1102001,"服务费");
						}
					}
					if(vo.isBlank("contractEfficiency")){
						throw new BizException(1102001,"充电效率");
					}else if(!ValidateUtils.Number(vo.getString("contractEfficiency"))){
						throw new BizException(1102006, "充电效率");
					}else if(vo.getDouble("contractEfficiency")<=0){
						throw new BizException(1105002,"充电效率");
					}
				}else{
					if(vo.isBlank("contractAmount")){
						throw new BizException(1102001,"收入金额");
					}
				}
			}else if(vo.getInt("contractType") == 2 && vo.isNotBlank("contractCostType")){//支出合约类型
				int contractCostType = vo.getInt("contractCostType");
				checkVo.put("contractCostType", contractCostType);
				//0.分成支出合约(分成) 1.土地租赁合约(固定) 2.土地租赁分成合约(固定) 3.房屋租赁合约(固定)
				if(contractCostType == 0){
					if(vo.isBlank("contractShareType")){
						throw new BizException(1102001, "合约分成项目");
					}
					if(vo.isBlank("contractEfficiency")){
						throw new BizException(1102001,"充电效率");
					}else if(!ValidateUtils.Number(vo.getString("contractEfficiency"))){
						throw new BizException(1102006, "充电效率");
					}else if(vo.getDouble("contractEfficiency")<=0){
						throw new BizException(1105002,"充电效率");
					}
					//0-充电侧服务费  1-用电侧服务费
					if(vo.getInt("contractShareType") == 1){
						if(vo.isBlank("contractServiceCharge")){
							throw new BizException(1102001,"服务费");
						}
					}
				}else{
					if(vo.isBlank("contractAmount")){
						throw new BizException(1102001,"收入金额");
					}
				}
			}
		}
		if(vo.isBlank("userAppId")){
			throw new BizException(1102001, "合约审批人");
		}else if(vo.isBlank("contractExpirationStart")){
			throw new BizException(1102001, "合约有效期开始");
		}else if(vo.isBlank("contractExpirationEnd")){
			throw new BizException(1102001, "合约有效期结束");
		}
		checkVo.put("contractExpirationStart", vo.getObject("contractExpirationStart"));
		//合约类型为0.收入合约 1.月结合约 2.支出合约
		if(vo.getInt("contractType") == 0 || vo.getInt("contractType") == 2){
			if(vo.isBlank("stationId")){
				throw new BizException(1102001, "场站");
			}else if(vo.isBlank("companyIdStr")){
				throw new BizException(1102001, "合约企业");
			}
			checkVo.put("stationId", vo.getInt("stationId"));
			//验证收入和支出合约重复性
			count = conTractMapper.checkIncomsTract(checkVo);
		}else if(vo.getInt("contractType") == 1){
			if(vo.isBlank("stationIdStr")){
				throw new BizException(1102001, "场站");
			}else if(vo.isBlank("groupIdStr")){
				throw new BizException(1102001, "集团");
			}
			
			if(vo.isBlank("tractPrice")){
				throw new BizException(1102001,"合约价类型");
			}
			if(vo.isBlank("contractEfficiency")){
				throw new BizException(1102001,"充电效率");
			}else if(!ValidateUtils.Number(vo.getString("contractEfficiency"))){
				throw new BizException(1102006, "充电效率");
			}else if(vo.getDouble("contractEfficiency")<=0){
				throw new BizException(1105002,"充电效率");
			}
			if(vo.isBlank("discount")){
				throw new BizException(1102001,"服务费折扣");
			}else if(vo.getInt("discount")<0 && vo.getInt("discount")>100){
				throw new BizException(1105002,"服务费折扣");
			}
			vo.put("stationId", -1);
			
			
			List<Integer> groupIds = new ArrayList<Integer>();
			String groupIdStr =  vo.getString("groupIdStr");
			String[] groupIdStrs = groupIdStr.split(",");
			for (int i = 0; i < groupIdStrs.length; i++) {
				groupIds.add(Integer.valueOf(groupIdStrs[i]));
			}
			
			checkVo.put("groupIds", groupIds);
			//验证月结合约重复性
			count = conTractMapper.checkMonthlyTract(checkVo);
		}
		if(null != file){
			//验证格式
			String fileName = file.getOriginalFilename();
			if(!fileName.endsWith(".doc") && !fileName.endsWith(".txt") && !fileName.endsWith(".pdf") && !fileName.endsWith(".docx")){
				throw new BizException(1105001);
			}
			String imageFileName = "contract/" + CommonUtils.randomNum() + fileName.substring(fileName.lastIndexOf("."));
			vo.put("contractFile", imageFileName);
			//上传
			OSSUploadFileUtils.toUploadFile(file, imageFileName);
		}else{
			throw new BizException(1102001, "合约文件");
		}
		
		
		if(count > 0){
			String msg = "场站",tractType ="";
			if(checkVo.getInt("contractType")==1){
				msg = "集团";
				tractType ="月结";
			}else if(checkVo.getInt("contractType")==0){
				tractType ="收入";
			}else if(checkVo.getInt("contractType")==2){
				tractType ="支出";
			}
			throw new BizException(1105005,msg,tractType);
		}
	}
	
}



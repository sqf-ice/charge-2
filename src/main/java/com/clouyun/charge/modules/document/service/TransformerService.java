package com.clouyun.charge.modules.document.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.TransformerMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: TransformerService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@Service
public class TransformerService {
	
	@Autowired
	TransformerMapper transformerMapper;
	
	@Autowired
	DictService dictService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MeterManagementService managementService;
	/**
	 * 查询变压器列表
	 */
	public PageInfo getTransformerAll(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		//设置分页
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List list = transformerMapper.getTransformerAll(vo);
		return new PageInfo(list);
	}
	
	/**
	 * 根据变压器Id查询变压器信息
	 * @throws BizException 
	 */
	public Map getTransformerById(Integer transId) throws BizException{
		if(transId ==null){
			throw new BizException(1102001, "变压器Id");
		}
		DataVo vo = new DataVo();
		vo.add("transId", transId);
		Map map = transformerMapper.getTransformerById(vo);
		return map;
	}
	
	/**
	 * 根据变压器Id删除变压器信息
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delTransformer(List transIds) throws BizException{
		if(transIds ==null || transIds.size()<= 0){
			throw new BizException(1102001, "变压器Id");
		}
		DataVo vo = new DataVo();
		vo.put("transIds", transIds);
		//查询是否有关联表计
		int count = managementService.getMeterByObjIds(vo);
		if(count>0){
			throw new BizException(1105003, "变压器");
		}
		transformerMapper.delTransformer(vo);
	}
	/**
	 * 查询终端信息(业务字典)
	 * @throws BizException 
	 */
	public PageInfo getTransformer(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List list = transformerMapper.getTransformer(vo);
		return new PageInfo(list);
	}
	
	
	private List<String> list = Arrays.asList(new String[]{
			"shelfLifeTime",
			"ratedPower"
	});
	/**
	 * 新增变压器
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveTransformer(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(vo,list);
		getTransformerValidate(vo, "save");
		transformerMapper.saveTransformer(paramMap);
	}
	
	/**
	 * 编辑变压器
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateTransformer(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(vo,list);
		getTransformerValidate(vo, "update");
		transformerMapper.updateTransformer(paramMap);
	}
	
	/**
	 * 数据验证
	 * @throws BizException 
	 */
	private void getTransformerValidate(DataVo vo,String type) throws BizException{
		if("update".equals(type)){
			if(vo.isBlank("transId")){
				throw new BizException(1102001, "变压器Id");
			}
		}
		if(vo.isBlank("orgId")){
			throw new BizException(1102001, "运营商Id");
		}else if(vo.isBlank("stationId")){
			throw new BizException(1102001, "场站Id");
		}else if(vo.isBlank("transNo")){
			throw new BizException(1102001, "变压器编号");
		}else if(vo.isBlank("transTestLoss")){
			throw new BizException(1102001, "考核线损率");
		}else if(vo.isBlank("transName")){
			throw new BizException(1102001, "变压器名称");
		}else if(vo.isBlank("transThLoss")){
			throw new BizException(1102001, "理论线损率");
		}else if(vo.isBlank("transType")){
			throw new BizException(1102001, "变压器型号");
		}else if(vo.isBlank("transAssetId")){
			throw new BizException(1102001, "资产编号");
		}else if(vo.isBlank("transStatus")){
			throw new BizException(1102001, "变压器状态");
		}else if(vo.isBlank("transMark")){
			throw new BizException(1102001, "公专变标志");
		}else if(vo.isBlank("transVolume")){
			throw new BizException(1102001, "变压器容量");
		}
		if(vo.isNotBlank("ratedPower") && !ValidateUtils.Number(vo.getString("ratedPower"))){
			throw new BizException(1000014,"额定功率");
		}
	}
	
	
	/**
	 * 变压器信息导出
	 * @throws Exception 
	 */
	public void exportTransformer(Map map,HttpServletResponse response) throws Exception{
		List list = getTransformerAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		
		headList.add("运营商名称");
		headList.add("场站名称");
		headList.add("变压器编号");
		headList.add("变压器名称");
		headList.add("变压器容量");
		headList.add("变压器状态");
		headList.add("保质日期");
		headList.add("额定功率");
		
		titleList.add("orgName");
		titleList.add("stationName");
		titleList.add("transNo");
		titleList.add("transName");
		titleList.add("transVolume");
		titleList.add("transStatus");
		titleList.add("shelfLifeTime");
		titleList.add("ratedPower");
		
		for (int i = 0; i < list.size(); i++) {
			 Map transMap = (Map) list.get(i);
			 //变压器状态 = byqzt
			 if(transMap.get("transStatus")!=null && !"".equals(transMap.get("transStatus").toString())){
				 transMap.put("transStatus", dictService.getDictLabel("byqzt", transMap.get("transStatus").toString()));
			 }else{
				 transMap.put("transStatus", "");
			 }
		}
		
		ExportUtils.exportExcel(list, response,headList,titleList,"变压器信息");
	}
}

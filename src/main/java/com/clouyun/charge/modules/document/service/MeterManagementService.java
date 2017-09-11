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
import com.clouyun.charge.modules.document.mapper.MeterManagementMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: MeterManagementService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@Service
public class MeterManagementService {

	@Autowired
	MeterManagementMapper meterManagementMapper;
	
	@Autowired
	DictService dictService;
	
	@Autowired
	UserService userService;
	/**
	 * 查询表计列表数据
	 */
	public PageInfo getMeterManagementAll(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List list = meterManagementMapper.getMeterManagementAll(vo);
		return new PageInfo(list);
	}
	
	/**
	 * 根据表计Id查询表计信息
	 */
	public Map getMeterManagementById(Integer meterManagementId) throws BizException {
		if(meterManagementId == null){
			throw new BizException(1102001, "表计Id");
		}
		DataVo vo = new DataVo();
		vo.put("meterManagementId", meterManagementId);
		Map map = meterManagementMapper.getMeterManagementById(vo);
		return map;
	}
	
	/**
	 * 根据表计Id删除表计信息
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delMeterManagement(List meterManagementIds) throws BizException{
		if(meterManagementIds==null || meterManagementIds.size()<=0){
			throw new BizException(1102001,"表计id");
		}
		DataVo vo = new DataVo();
		vo.add("meterManagementIds", meterManagementIds);
		meterManagementMapper.delMeterManagement(vo);
	}
	
	
	private List<String> list = Arrays.asList(new String[]{
			"shelfLifeTime",
			"ratedPower"
	});
	/**
	 * 新增表计
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveMeterManagement(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(vo,list);
		getMeterManagementValidate(vo,"save");
		meterManagementMapper.saveMeterManagement(paramMap);
	}
	
	/**
	 * 编辑表计
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateMeterManagement(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(vo,list);
		getMeterManagementValidate(vo,"update");
		meterManagementMapper.updateMeterManagement(paramMap);
	}
	
	/**
	 * 数据验证
	 * @throws BizException 
	 */
	private void getMeterManagementValidate(DataVo vo, String type) throws BizException {
		
		if("update".equals(type)){
			if(vo.isBlank("meterManagementId")){
				throw new BizException(1102001, "表计Id");
			}
		}
		if(vo.isBlank("orgId")){
			throw new BizException(1102001, "运营商Id");
		}else if(vo.isBlank("stationId")){
			throw new BizException(1102001, "场站Id");
		}else if(vo.isBlank("transId")){
			throw new BizException(1102001, "变压器Id");
		}else if(vo.isBlank("terminalId")){
			throw new BizException(1102001, "终端Id");
		}else if(vo.isBlank("meterManagementNo")){
			throw new BizException(1102001, "表计资产编号");
		}else if(vo.isBlank("meterManagementName")){
			throw new BizException(1102001, "表计名称");
		}else if(vo.isBlank("meterManagementType")){
			throw new BizException(1102001, "表计类型");
		}else if(vo.isBlank("meterManagementPurpose")){
			throw new BizException(1102001, "表计用途");
		}else if(vo.isBlank("meterManagementCt")){
			throw new BizException(1102001, "CT互感器变比");
		}else if(vo.isBlank("meterManagementPt")){
			throw new BizException(1102001, "PT互感器变比");
		}else if(vo.isBlank("meterManagementAddr")){
			throw new BizException(1102001, "通信地址");
		}else if(vo.isBlank("meterManagementRate")){
			throw new BizException(1102001, "综合倍率");
		}else if(vo.isBlank("meterManagementMeasureNo")){
			throw new BizException(1102001, "测量点编号");
		}else if(vo.isBlank("meterManagementProtocol")){
			throw new BizException(1102001, "通信规约");
		}
		if(vo.isNotBlank("ratedPower") && !ValidateUtils.Number(vo.getString("ratedPower"))){
			throw new BizException(1000014,"额定功率");
		}
	}
	
	/**
	 * 表计导出
	 */
	public void exportMeterManagement(Map map,HttpServletResponse response) throws Exception{
		List list = getMeterManagementAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		
		headList.add("运营商名称");
		headList.add("所属场站");
		headList.add("测量点编号");
		headList.add("终端名称");
		headList.add("变压器名称");
		headList.add("表计资产编号");
		headList.add("表计名称");
		headList.add("表计用途");
		headList.add("表计类型");
		headList.add("通信地址");
		headList.add("通信规约");
		headList.add("CT互感器变比");
		headList.add("PT互感器变比");
		headList.add("综合倍率");
		headList.add("保质日期");
		headList.add("额定功率");
		headList.add("创建时间");
		headList.add("更新时间");
		
		titleList.add("orgName");
		titleList.add("stationName");
		titleList.add("meterManagementMeasureNo");
		titleList.add("terminalName");
		titleList.add("transName");
		titleList.add("meterManagementNo");
		titleList.add("meterManagementName");
		titleList.add("meterManagementPurpose");
		titleList.add("meterManagementType");
		titleList.add("meterManagementAddr");
		titleList.add("meterManagementProtocol");
		titleList.add("meterManagementCt");
		titleList.add("meterManagementPt");
		titleList.add("meterManagementRate");
		titleList.add("shelfLifeTime");
		titleList.add("ratedPower");
		titleList.add("meterManagementCreatetime");
		titleList.add("meterManagementUpdatetime");
		
		for (int i = 0; i < list.size(); i++) {
			Map meterMap = (Map) list.get(i);
			//表计类型  bjlx
			if(meterMap.get("meterManagementType") !=null && !"".equals(meterMap.get("meterManagementType").toString())){
				meterMap.put("meterManagementType", dictService.getDictLabel("bjlx", meterMap.get("meterManagementType").toString()));
			}else{
				meterMap.put("meterManagementType", "");
			}
			//表计用途 bjyt
			if(meterMap.get("meterManagementPurpose") !=null && !"".equals(meterMap.get("meterManagementPurpose").toString())){
				meterMap.put("meterManagementPurpose", dictService.getDictLabel("bjyt", meterMap.get("meterManagementPurpose").toString()));
			}else{
				meterMap.put("meterManagementPurpose", "");
			}
			//CT互感器变比  ct
			if(meterMap.get("meterManagementCt") !=null && !"".equals(meterMap.get("meterManagementCt").toString())){
				meterMap.put("meterManagementCt", dictService.getDictLabel("ct", meterMap.get("meterManagementCt").toString()));
			}else{
				meterMap.put("meterManagementCt", "");
			}
			//PT互感器变比 pt
			if(meterMap.get("meterManagementPt") !=null && !"".equals(meterMap.get("meterManagementPt").toString())){
				meterMap.put("meterManagementPt", dictService.getDictLabel("pt", meterMap.get("meterManagementPt").toString()));
			}else{
				meterMap.put("meterManagementPt", "");
			}
		}
		ExportUtils.exportExcel(list, response,headList,titleList,"表计信息");
	}
	
	/**
	 * 根据变压器Id,终端Id查询是否有表计信息
	 */
	public Integer getMeterByObjIds(DataVo vo){
		return meterManagementMapper.getMeterByObjIds(vo);
	}
	
}

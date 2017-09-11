package com.clouyun.charge.modules.document.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.MaterialsMapper;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 描述: MaterialService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年2月27日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialsService {
	
	@Autowired
	MaterialsMapper materialMapper;
	
	@Autowired
	UserService userService;
	
	public PageInfo query(Map map)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			map.put("orgIds", orgIds);
		}
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
		List list = materialMapper.queryListByPage(map);
		PageInfo page = new PageInfo(list);
		return page;
	}
	
	public Map queryById(Integer matId){
		Map map = materialMapper.queryById(matId);
		return map;
	}
	/*
	 * 新增耗材
	 */
	public int insert(Map map)throws BizException{
		//检查参数
		check(map);
		int count = materialMapper.insert(map);
		return count;
	}
	
	/*
	 * 检查参数
	 */
	private void check(Map map) throws BizException{
		String partten = "[A-Za-z0-9\u4E00-\u9FA5]*";
		if(map.get("matName") == null || "".equals(map.get("matName").toString())) {
			throw new BizException(1106000,"耗材名称");
		}else{
			int count = materialMapper.checkUniqueness(map);
			if(count > 0){
				//耗材名称已存在
				throw new BizException(1106001);
			}
		}
		if(map.get("matFactory") == null || "".equals(map.get("matFactory").toString())) {
			throw new BizException(1106000,"生产厂家");
		}
		if(map.get("orgId") == null || "".equals(map.get("orgId").toString())) {
			throw new BizException(1106000,"运营商");
		}
		if(map.get("matPrice") == null || "".equals(map.get("matPrice").toString())) {
			throw new BizException(1106000,"耗材成本");
		}
		if(!Pattern.matches(partten, map.get("matName").toString())){
			throw new BizException(1106002,"耗材名称");
		}
		if(!Pattern.matches(partten, map.get("matFactory").toString())){
			throw new BizException(1106002,"生产厂家");
		}
	}
	/*
	 * 更新耗材
	 */
	public int update(Map map) throws BizException{
		if(null != map.get("matId") && !"".equals(map.get("matId").toString())){
			List<Integer> list = new ArrayList<>();
			list.add(Integer.parseInt(map.get("matId").toString()));
			int materialUse = materialMapper.materialUse(list);
			//检查耗材是否正在被使用
			if(materialUse > 0){
				throw new BizException(1106003,"不可修改");
			}
		}else{
			throw new BizException(1106000,"matId");
		}
		
		//检查参数
		check(map);
		int count = materialMapper.update(map);
		return count;
	}
	
	/*
	 * 删除耗材
	 */
	public int delete(List ids) throws BizException{
		if(ids == null || ids.size() <= 0){
			throw new BizException(1106004);
		}
		
		int materialUse = materialMapper.materialUse(ids);
		//检查耗材是否正在被使用
		if(materialUse > 0){
			throw new BizException(1106003,"不可删除");
		}
		
		int count = materialMapper.delete(ids);
		return count;
	}
	
	/**
	 * 根据组织ID获取物料列表
	 * @param orgId
	 * @return
	 * @throws BizException
	 * 2017年3月24日
	 * gaohui
	 */
    @SuppressWarnings("rawtypes")
	public List<ComboxVo> findMaterialDictByOrgId(Map map) throws BizException{
    	List<ComboxVo> dicts = null;
    	DataVo params = new DataVo(map);
    	if(params.isBlank("userId")){
    		throw new BizException(1102001, "用户Id");
    	}
    	Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!= orgIds && orgIds.size()>0){
			map.put("orgIds", orgIds);
		}
		if(params.isNotBlank("orgId")){
			map.put("orgId", params.getInt("orgId"));
    	}
    	List<Map> list = materialMapper.findMaterialDictByOrgId(map);
    	if(null!=list && list.size()>0){
    		dicts = new ArrayList<ComboxVo>();
    		for(Map material:list){
    			ComboxVo comboxVo = new ComboxVo();
    			DataVo materialVo = new DataVo(material);
    			comboxVo.setId(materialVo.getString("matId"));
    			comboxVo.setText(materialVo.getString("matName"));
    			dicts.add(comboxVo);
    		}
    	}
    	return dicts;
    }
    /**
     * 根据物料id获取物料的价格
     * @param matId
     * @return
     * @throws BizException
     * 2017年3月24日
     * gaohui
     */
    public Map findMaterialPriceById(Integer matId)throws BizException{
    	return materialMapper.findMaterialPriceById(matId);
    }
    /**
     * 根据告警id获取物料
     * @param map
     * @return
     * @throws BizException
     * 2017年3月29日
     * gaohui
     */
    public List<Map> findAllMaterialByRecId(Map map)throws BizException{
    	
    	return materialMapper.findAllMaterialByRecId(map);
    }
    
    /*
     * 耗材导出
     */
    public void export(Map map,HttpServletResponse response) throws Exception{
    	DataVo params = new DataVo(map);
    	if(params.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			map.put("orgIds", orgIds);
		}
    	List list = materialMapper.queryListByPage(map);
    	List<String> headList = new ArrayList<>();
    	List<String> valList = new ArrayList<>();
    	
    	headList.add("耗材名称");
    	headList.add("成本价(元)");
    	headList.add("生产厂家");
    	headList.add("生产日期");
    	headList.add("采购日期");
    	headList.add("保质日期");
    	
    	valList.add("matName");
    	valList.add("matPrice");
    	valList.add("matFactory");
    	valList.add("matProductionTime");
    	valList.add("matPurchaseTime");
    	valList.add("shelfLifeTime");
    	ExportUtils.exportExcel(list, response, headList, valList, "耗材信息");
    }
    
}

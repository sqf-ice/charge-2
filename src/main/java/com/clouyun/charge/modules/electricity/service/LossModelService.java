package com.clouyun.charge.modules.electricity.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.modules.electricity.entitys.LossModel;
import com.clouyun.charge.modules.electricity.mapper.LossModelMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @author fuft
 *
 */
@Service
public class LossModelService {
	@Autowired
	LossModelMapper lossModelMapper;
	
	public Integer selectCount(Map map){	
		
		return lossModelMapper.count(map);
	}
	
	/**
	 * 添加损耗模版 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String lossModelAdd(Map map) {
		Integer stationId=(Integer) map.get("stationId");
		LossModel lossModel=lossModelMapper.findModelByStationId(stationId);
		//先去验证数据库是否存在该场站的损耗模型，如果存在，则不允许新增，只能修改
		if(lossModel!=null){
			return "损耗模型已存在！";
		}else{
			//新增
			if(map.get("orgId")==null){
				return "运营商不能为空！";
			}
			
			if(map.get("stationId")==null){
				return "场站不能为空！";
			}
			
			if(map.get("lmNo")==null){
				return "模型编号不能为空！";
			}
			
			if(map.get("lmName")==null){
				return "模型名称不能为空！";
			}
			
			if(map.get("lmType")==null){
				return "请选择一种模型类型！";
			}
			
			Timestamp c =new Timestamp(System.currentTimeMillis());
			map.put("createDate", c);
			map.put("updateDate", c);
			lossModelMapper.lossModelAdd(map);
		}
		
		return "添加损耗模型成功！";

	}
	/**
	 * 根据条件查询损耗模型的数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer modelCount(Map map){
		//Set<Integer> stationIds = AuthService.getSubsByUserId();
		String regex= "[A-Za-z0-9\u4E00-\u9FA5]*";
		if(Pattern.matches(regex, map.get("stationId").toString())){
			map.put("stationName",map.get("stationId").toString());
		}
		
		return lossModelMapper.count(map);		
	}
	
	/**
	 * 根据条件查询损耗模型列表
	 */
	@SuppressWarnings("unchecked")
	public PageInfo selectAll(Map map){
		String regex= "[A-Za-z0-9\u4E00-\u9FA5]*";
		if(Pattern.matches(regex, map.get("stationId").toString())){
			map.put("stationName",map.get("stationId").toString());
		}
		
		DataVo params = new DataVo(map);
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
		List<Map<String,Object>> data =new ArrayList<Map<String,Object>>();
		Map map2=new HashMap();
		List<LossModel> lossModels=lossModelMapper.selectAll(map);
		for(LossModel lossModel:lossModels){
			map2.put("lmId", lossModel.getLmId());
			map2.put("orgId", lossModel.getOrgId());
			map2.put("stationId", lossModel.getStationId());
			map2.put("lmNo", lossModel.getLmNo());
			map2.put("lmName", lossModel.getLmName());
			map2.put("lmType", lossModel.getLmType());
			map2.put("createDate", lossModel.getCreateDate());
			map2.put("updateDate", lossModel.getUpdateDate());
			map2.put("lmCzzb", lossModel.getLmCzzb());
			map2.put("lmCdss", lossModel.getLmCdss());
			map2.put("lmSjcd", lossModel.getLmSjcd());
			map2.put("lmQtyd", lossModel.getLmQtyd());
			data.add(map2);
		}
			
        PageInfo page = new PageInfo(data);
		return page;
		
	}
	
	/**
	 * 编辑损耗模型
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String update(Map map){
		if(map.get("orgId")==null){
			return "运营商不能为空！";
		}
		
		if(map.get("stationId")==null){
			return "场站不能为空！";
		}
		
		if(map.get("lmNo")==null){
			return "模型编号不能为空！";
		}
		
		if(map.get("lmName")==null){
			return "模型名称不能为空！";
		}
		
		if(map.get("lmType")==null){
			return "请选择一种模型类型！";
		}
		
		Timestamp c =new Timestamp(System.currentTimeMillis());
		map.put("updateDate", c);
		lossModelMapper.lossModelEdit(map);
	    return "编辑损耗模型成功！";
		
		
	}
	
}

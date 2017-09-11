package com.clouyun.charge.modules.monitor.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.mapper.MaterialMapper;


/**
 * 描述: MaterialService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年2月27日
 */
@SuppressWarnings("rawtypes")
@Service
public class MaterialService {
	
	@Autowired
	MaterialMapper materialMapper;
	
	public ResultVo query(Map map){
		ResultVo vo = new ResultVo();
		List list = materialMapper.queryListByPage(map);
		int count = materialMapper.queryListCount(map);
		vo.setTotal(count);
		vo.setData(list);
		return vo;
	}
	
	public ResultVo queryById(Map map){
		ResultVo vo = new ResultVo();
		vo.setData(materialMapper.queryById(map));
		return vo;
	}
	
	public ResultVo checkUniqueness(Map map){
		ResultVo vo = new ResultVo();
		vo.setData(materialMapper.checkUniqueness(map));
		return vo;
	}
	
	public ResultVo insert(Map map){
		ResultVo vo = new ResultVo();
		vo.setData(materialMapper.insert(map));
		return vo;
	}
	
	public ResultVo update(Map map){
		ResultVo vo = new ResultVo();
		vo.setData(materialMapper.update(map));
		return vo;
	}
	
	public ResultVo delete(Map map){
		ResultVo vo = new ResultVo();
		vo.setData(materialMapper.delete(map));
		return vo;
	}
	/**
	 * 根据组织ID获取物料列表
	 * @param orgId
	 * @return
	 * @throws BizException
	 * 2017年3月24日
	 * gaohui
	 */
    public List<Map> findAllMaterialByOrgId(Map map) throws BizException{
    	
    	return materialMapper.findAllMaterialByOrgId(map);
    }
    /**
     * 根据物料id获取物料的价格
     * @param matId
     * @return
     * @throws BizException
     * 2017年3月24日
     * gaohui
     */
    public Map findAllMaterialById(Map map)throws BizException{
    	return materialMapper.findAllMaterialById(map);
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
    /**
     * 查询物料是否正在使用
     */
    public Integer materialUse(Map map)throws BizException{
    	int count = materialMapper.materialUse(map);
    	return count;
    }
}

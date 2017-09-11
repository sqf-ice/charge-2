package com.clouyun.charge.modules.monitor.web;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.modules.monitor.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
/**
 * 描述: MaterialController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年2月27日
 */
@RestController
public class MaterialController {
	
	@Autowired
	MaterialService materialService;
	
	@RequestMapping(value = "/material/list", method = RequestMethod.POST)
	public ResultVo queryMaterialAll(@RequestBody DataVo vo){
		return materialService.query(vo.toMap());
	}
	
	@RequestMapping(value = "/material/id",method = RequestMethod.POST)
	public ResultVo queryById(@RequestBody DataVo vo){
		return  materialService.queryById(vo.toMap());
	}
	
	@RequestMapping(value = "/material/save",method = RequestMethod.POST)
	public ResultVo insertMaterial(@RequestBody DataVo vo){
		return materialService.insert(vo.toMap());
	}
	
	
	@RequestMapping(value = "/material/update",method = RequestMethod.POST)
	public ResultVo updateMaterial(@RequestBody DataVo vo){
		return materialService.update(vo.toMap());
	}
	
	
	@RequestMapping(value = "/material/del",method = RequestMethod.POST)
	public ResultVo deleteMaterial(@RequestBody DataVo vo){
		return materialService.delete(vo.toMap());
	}
	
	@RequestMapping(value = "/material/check",method = RequestMethod.POST)
	public ResultVo checkMaterial(@RequestBody DataVo vo){
		return materialService.checkUniqueness(vo.toMap());
	}
	/**
	 * 根据orgId查询物料列表
	 * @param vo
	 * @return
	 * 2017年3月24日
	 * gaohui
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/material/queryMaterialByOrgId",method = RequestMethod.POST)
	public ResultVo queryMaterialByOrgId(@RequestBody DataVo vo) throws Exception{
		ResultVo resultVo = new ResultVo();
		List<Map> list = materialService.findAllMaterialByOrgId(vo);
		resultVo.setData(list);
		return resultVo;
	}
	
	/**
	 * 根据matId查询物料信息
	 * @param vo
	 * @return
	 * 2017年3月24日
	 * gaohui
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/material/queryMaterialById",method = RequestMethod.POST)
	public ResultVo queryMaterialById(@RequestBody DataVo vo) throws Exception{
		ResultVo resultVo = null;
		Map map = materialService.findAllMaterialById(vo);
		if(null!=map && map.size()>0){
			resultVo = new ResultVo();
			resultVo.setData(map);
		}
		return resultVo;
	}
	
	/**
	 * 查询物料是否正在使用
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/material/isUse",method = RequestMethod.POST)
	public ResultVo materialUse(@RequestBody DataVo vo) throws Exception{
		ResultVo resultVo = null;
		Integer count = materialService.materialUse(vo);
		if(null != count){
			resultVo = new ResultVo();
			resultVo.setData(count);
		}
		return resultVo;
	}
	
	
	
}

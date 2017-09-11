package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.modules.system.mapper.DictItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典表
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年3月21日
 */
@Service
public class DictItemService {
	
	@Autowired
	private DictItemMapper dictItemMapper;
	/**
	 * 根据类型ID查询
	 * @param vo
	 * @return
	 * 2017年3月21日
	 * gaohui
	 */
	public ResultVo queryDictItemList(DataVo vo){
		ResultVo resultVo = new ResultVo();
		resultVo.setData(dictItemMapper.queryDictItemList(vo));
		return resultVo;
	}
	/**
	 * 根据类型ID和num查询
	 * @param vo
	 * @return
	 * 2017年3月30日
	 * gaohui
	 */
	public ResultVo queryDictItem(DataVo vo){
		ResultVo resultVo = new ResultVo();
		resultVo.setData(dictItemMapper.queryDictItem(vo));
		return resultVo;
	}
	/**
	 *  根据主键ID和num更新字典表
	 * @param vo
	 * @return
	 * 2017年3月21日
	 * gaohui
	 */
	@Transactional
    public ResultVo updateDictItembyIdAndNum(DataVo vo){
    	ResultVo resultVo = new ResultVo();
    	dictItemMapper.updateDictItembyTypeId(vo);
    	vo.put("reserve1", 1);
    	if(null!=vo.get("itemNums")&&!"".equals(vo.get("itemNums"))){
    		resultVo.setData(dictItemMapper.updateDictItembyIdAndNum(vo));
    	}
		return resultVo;
    }
}

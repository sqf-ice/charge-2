package com.clouyun.charge.modules.finance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.finance.mapper.HistoryBillMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月18日
 */
@Service
@SuppressWarnings("rawtypes")
public class HistoryBillService {
	
	@Autowired
	private HistoryBillMapper historyBillMapper;
	/**
	 * 分页查询财务记录
	 * @param map
	 * @return
	 * 2017年4月18日
	 * gaohui
	 */
	@SuppressWarnings("unchecked")
	public PageInfo getHistoryBillsPage(Map map)throws BizException{
		PageInfo pageInfo = null;
		DataVo params = new DataVo(map);
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
		List<Map> list = historyBillMapper.get(map);
		if(null!=list && list.size()>0){
			pageInfo = new PageInfo(list);
		}
		return pageInfo;
	}
    
	/**
	 * 查询财务记录详情
	 * @param id
	 * @return
	 * 2017年4月18日
	 * gaohui
	 */
	public Map getHistoryBill(Integer id)throws BizException{
		return historyBillMapper.getById(id);
	}
}

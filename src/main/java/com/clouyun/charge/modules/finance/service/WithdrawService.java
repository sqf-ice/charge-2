package com.clouyun.charge.modules.finance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.finance.mapper.WithdrawMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 描述: 提现Service 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月17日
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WithdrawService {
	
	@Autowired
	private WithdrawMapper withdrawalMapper;
	/**
	 * 新增提现记录
	 * @param map
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	public int insertWithdraw(Map map)throws BizException{
		//
		return withdrawalMapper.insert(map);
	}
	/**
	 * 删除提现记录
	 * @param withdrawId
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	public int deleteWithdraw(Integer id)throws BizException{
		return withdrawalMapper.delete(id);
	}
    /**
     * 根据id更新提现记录
     * @param map
     * @return
     * 2017年4月17日
     * gaohui
     */
	public int updateWithdraw(Map map)throws BizException{
		return withdrawalMapper.update(map);
	}
	/**
	 * 根据id更新提现记录详情
	 * @param withdrawId
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	public Map getWithdraw(Integer id)throws BizException{
		return withdrawalMapper.getById(id);
	}
	/**
	 * 分页查询提现记录
	 * @param map
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	public PageInfo getWithdrawsPage(Map map)throws BizException{
		PageInfo pageInfo = null;
		DataVo params = new DataVo(map);
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
		List<Map> list = withdrawalMapper.get(map);
		if(null!=list && list.size()>0){
			pageInfo = new PageInfo(list);
		}
		return pageInfo;
	}
}

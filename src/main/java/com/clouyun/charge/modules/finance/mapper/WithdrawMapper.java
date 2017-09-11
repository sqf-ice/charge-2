package com.clouyun.charge.modules.finance.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
/**
 * 描述: 提现Mapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月17日
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface WithdrawMapper {
	/**
	 * 新增
	 * @param domain
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	int insert(Map map);
	/**
	 * 删除
	 * @param withdrawId
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	int delete(Integer withdrawId);
	/**
	 * 根据id获取提现详情
	 * @param id
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	Map getById(Integer withdrawId);
	/**
	 * 根据id更新提现记录
	 * @param domain
	 * @return
	 * 2017年4月17日
	 * gaohui
	 */
	int update(Map map);
    /**
     * 查询提现列表
     * @return
     * 2017年4月17日
     * gaohui
     */
	List<Map> get(Map map);

}

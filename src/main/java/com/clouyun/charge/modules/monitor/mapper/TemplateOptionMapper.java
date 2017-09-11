/**
 * 
 */
package com.clouyun.charge.modules.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: TemplateOptionMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 1.0
 * 创建日期: 2017年3月14日
 */
@Mapper
public interface TemplateOptionMapper {
	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List queryListByPage(DataVo vo);
	/**
	 * 返回列表总行数
	 * @param map
	 * @return
	 */
	int queryListCount(DataVo vo);
	/**
	 * 通过Id查询返回单行数据
	 * @param map
	 * @return
	 */
	Map queryById(DataVo vo);
	/**
	 * 检查数据唯一性
	 * @param map
	 * @return
	 */
	int checkUniqueness(DataVo vo);
	/**
	 * 新增数据
	 * @param map
	 * @return
	 */
	int insert(DataVo vo);
	/**
	 * 修改数据
	 * @param map
	 * @return
	 */
	int update(DataVo vo);
	/**
	 * 删除多条数据
	 * @param map
	 * @return
	 */
	int delete(DataVo vo);
}

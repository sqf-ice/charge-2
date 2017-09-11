package com.clouyun.charge.modules.monitor.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 描述: 图片信息
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月5日
 */
@Mapper
public interface PhotoMapper {
	/**
	 * 根据作业Id查询图片信息
	 * @return
	 * 2017年4月5日
	 * gaohui
	 */
	List<Map> findPhotoListByTaskId(Map map);

}

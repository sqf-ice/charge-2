package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 描述: 文件操作表(sys_oss)
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年07月19日
 */
@Mapper
public interface FileMapper {
	
	/**
	 * 获取所有文件列表
	 * @return
	 */
	List<DataVo> getFiles();
	
}

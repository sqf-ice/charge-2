package com.clouyun.charge.modules.document.mapper;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述: CompanyMapper 合约企业
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月24日
 */
@Mapper
public interface CompanyMapper {
	/**
	 * 查询合约企业列表数据
	 */
	List getCompanyAll(DataVo vo);
	
	/**
	 * 查询合约企业信息
	 */
	Map getCompanyById(DataVo vo);
	
	/**
	 * 查询合约企业列表(业务字典)
	 */
	List getCompany(DataVo vo);
	
	/**
	 * 新增合约企业
	 */
	void saveCompany(DataVo vo);
	/**
	 * 编辑合约企业
	 */
	void updateCompany(DataVo vo);

	/**
	 * 合约企业字典
	 * @param data
	 * @return
	 */
    List<ComboxVo> getCompanyDict(Map data);
    
    /**
     * 检查合约企业是否重复
     */
    Integer checkCompany(Map vo);
}


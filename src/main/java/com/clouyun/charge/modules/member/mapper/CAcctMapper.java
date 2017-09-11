package com.clouyun.charge.modules.member.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 描述: 账户信息
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月15日 上午10:01:18
 */
@Mapper
public interface CAcctMapper {

	//新增账户
	int insertCAcct(Map map);
	
	//更新账户
	int updateCAcct(Map map);
	
	//批量新增
	int batchInserCAcct(List list);
	
	//批量更新
	int batchUpdateCAcct(List list);

	//账户流水
	int insertCAcctSeq(Map map);

	//查询账户状态
	DataVo queryCAcctStatus(Map map);
}

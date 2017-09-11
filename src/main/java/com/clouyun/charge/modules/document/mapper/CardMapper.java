package com.clouyun.charge.modules.document.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

/**
 * 描述: CardMapper
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年6月14日
 */
@Mapper
public interface CardMapper {
	/**
	 * 查询卡片列表
	 */
	List getCardAll(DataVo vo);
	
	/**
	 * 查询卡片信息
	 */
	Map getCardById(DataVo vo);
	
	/**
	 * 开卡
	 */
	void saveCardInfo(DataVo vo);
	
	/**
	 * 编辑卡片信息
	 */
	void updateCardInfo(DataVo vo);
	
	/**
	 * 充值
	 */
	void rechargeCard(DataVo vo);
	
	/**
	 * 解锁
	 */
	void unlockCard(DataVo vo);
	
	/**
	 * 根据会员查询车辆信息
	 */
	Map getCardByConsId(DataVo vo);
	
	/**
	 * 根据会员电话和运营商关联会员信息
	 */
	Map queryConsInfo(DataVo vo);
	
	/**
	 * 判断卡片是否存在
	 */
	int getCardIdIsExist(String cardId);
	
	/**
	 * 加载卡秘钥
	 */
	List<DataVo> querCardKeys(DataVo vo);
}

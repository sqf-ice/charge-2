package com.clouyun.charge.modules.member.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

 /**
 * 描述: 会员信息
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月13日 下午8:51:09
 */
@Mapper
public interface MemberMapper {
	//查询所有会员信息
	List<Map> queryMembers(Map map);
	//根据卡号查询会员id
	List<Integer> queryConsByCardId(String rechargeCard);
	//查询会员账户流水详情
	List<Map> queryConsFlow(Map map);
	//查询billPay
	List<Map> queryBillPay(List list);
	//查询BillRecharge
	List<Map> queryBillRecharge(List list);
	//查询CCardRecharge
	List<Map> queryCCardRecharge(List list);
	//根据集团id查询会员数
	List<Map> queryCountByGroupId(List list);
	//查询充点卡信息
	List<Map> queryAllCCard(List<Integer> list);
	 //卡充值金额
	 List<Map> queryAllCCardRecharge(List<Integer> list);
	 //app充值金额
	 List<Map> queryAllBillRecharge(List<Integer> list);
	 //新增会员
	int insertMembers(Map map);
	 //更新
	int updateMembers(Map map);
	//会员信息
	List<Map> queryMemberByKey(Integer consId);
	//校验电话号码
	Integer checkPhone(Map map);
	//根据电话号码查询会员信息
	List<Map> queryInfoByPhone(Map map);
	//根据orgId查询车辆信息
	List<Map> queryVehicleInfoByOrgId(Object[] orgIds);
	//新增车辆
	int inserVehicle(List list);
	//批量新增
	int batchInsertCons(List list);
	//批量更新
	int batchUpdateCons(List list);
	//查询会员车辆关系表
	List<Map> queryMemberCarRel(Map map);

	//根据carId查询会员信息
	List<Map> queryMemberById(Map map);
	//集团会员置无效为个人会员
	int dissGroupMembers(List list);
	
	List<Map> queryBillPayAmount(Integer consId);
	//查询订单的次数和消费金额,会员id分组
	List<Map> queryBillPayCountAndAmount(List<Integer> list);

	//会员账户交易流水
	List<DataVo> queryCAcctSeq(Map map);
}

package com.clouyun.charge.modules.charge.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.clou.entitys.authority.PubPrice;
import com.clou.entitys.data.BillPay;
import com.clou.entitys.data.ChgRecord;
import com.clou.entitys.document.ChgPile;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.modules.charge.vo.AbnormalBill;

@Mapper
public interface BillPayMapper {

	// 根据ID获取对象
	public BillPay findById(int id);
	
	/**
	 * 根据充电桩Id获取充电桩信息
	 */
	public ChgPile getPileById(int pileId);

	// 根据主键更新
	public void updateByPrimaryKey(BillPay pay);

	
	/**
	 * 查找订单对应的充电记录，根据条件like查询%%
	 * @param seqNo    充电记录随机数
	 * @param cardNo   充电卡号
	 * @return
	 */
	public List<ChgRecord> findChargeRecord(@Param("seqno") String seqNo,@Param("cardno") String cardNo);
	
	public List<ChgRecord> findEndChargeRecord(@Param("cardno") String cardNo);
	
	/**
	 * 根据订单Id通过Id对应关系表查询充电事件
	 * @param billPayId
	 * @return
	 */
	public List<ChgRecord> findChargeRecordById(@Param("billPayId") int billPayId);
	
	public PubPrice getPilePrice(@Param("pileId") int pileId);
	
	/**
	 * 更新待结订单
	 * @param billPayId
	 * @param startTime
	 * @param endTime
	 * @param payState
	 * @param chgPower
	 * @param amount
	 */
	public void updateBillPay(@Param("billPayId") Integer billPayId,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("payState") int payState,
			@Param("chgPower") double chgPower,
			@Param("amount") double amount,
			@Param("dl1") Double dl1,
			@Param("dl2") Double dl2,
			@Param("dl3") Double dl3,
			@Param("dl4") Double dl4,
			@Param("prc1") Double prc1,
			@Param("prc2") Double prc2,
			@Param("prc3") Double prc3,
			@Param("prc4") Double prc4,
			@Param("prcServ") Double prcServ,
			@Param("billPayNo") String billPayNo
			);
	
	
	/**
	 * 查询需要异常结单的订单
	 * @param orgId  按运营商
	 * @param stationId  按场站
	 * @param startDate  按充电结束时间字段 ,
	 * @param endDate
	 * @param consPhone 按用户电话查询
	 * @param billPayNo 按订单编号
	 * @return
	 */
	public List<AbnormalBill> queryAbnormalBill(@Param("orgId") Integer orgId,@Param("stationId") Integer stationId,
			@Param("startDate") String startDate,@Param("endDate") String endDate,
			@Param("consPhone") String consPhone,@Param("billPayNo") String billPayNo,
			@Param("stationIds") Collection<Integer> stationIds,
			@Param("pageNum") int pageNum,@Param("pageSize") int pageSize);
	
	/**
	 * 查询需要异常结单的订单
	 * @param vo
	 * @return
	 */
	public List<AbnormalBill> queryAbnormalBill1(DataVo vo);
	
	
	
}
package com.clouyun.charge.modules.charge.service;

import com.clou.business_2015.cdz.CdzBusi;
import com.clou.common.utils.CalendarUtils;
import com.clou.randomcall_2015.core.commservice.CommunicateService;
import com.clou.randomcall_2015.core.commservice.ServiceFactory;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.mapper.GatheringMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class GatheringService   extends BusinessService {

	@Autowired
	GatheringMapper gatheringMapper;
	@Autowired
	DictService dictService;
	@Autowired
	UserService userService;

	/**
	 * 获取集团名称
	 */
	public List<DataVo> getGroup(DataVo dv) throws BizException {
		ChargeManageUtil.orgIdsCondition(dv, RoleDataEnum.ORG.dataType);
		return gatheringMapper.getGroup(dv);
	}


	/**
	 * 查询充值记录
	 */
	public PageInfo selectRecharge(DataVo vo) throws BizException {
		List<DataVo> dataVos = selectRechargeList(vo);//充值记录list
		return new PageInfo(dataVos);
	}

	/**
	 * 查询充值List
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	public List<DataVo> selectRechargeList(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> dataVos = gatheringMapper.selectRecharge(vo);
		if(dataVos!=null&&dataVos.size()>0){
			List<ComboxVo> boxList1 = dictService.getDictByType("hylb");
			List<ComboxVo> boxList2 = dictService.getDictByType("czfs");
			for (DataVo dataVo : dataVos) {
				ChargeManageUtil.setDataVoPut("consTypeCode", boxList1, dataVo);
				ChargeManageUtil.setDataVoPut("rechargeType", boxList2, dataVo);
			}
		}

		return  dataVos;
	}

	/**
	 * 充值记录合计
	 * @return
	 */
	public double selectRechargeCount(DataVo vo) throws BizException {
		vo.remove("pageNum");
		vo.remove("pageSize");

		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);

		DataVo dataCount  = gatheringMapper.selectRechargeCount(vo);
		double amountCount=0.0;
		if(dataCount!=null&&dataCount.size()>0){
			amountCount=dataCount.getDouble("amount");
		}
		return amountCount;
	}
	/**
	 * 充值记录导出
	 *
	 * @param data
	 * @param response
	 */
	public void exportRecharge(DataVo data, HttpServletResponse response) throws Exception {
		List dvList = selectRechargeList(data);
		double amount = selectRechargeCount(data);
		DataVo count = new DataVo();
		count.put("amount",amount);
		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		headList.add("订单号");
		headList.add("充值金额(元)");
		headList.add("会员名");
		headList.add("会员类型");
		headList.add("集团名称");
		headList.add("手机号");
		headList.add("充值方式");
		headList.add("创建时间");
		headList.add("备注");
		valList.add("billRechargeNo");
		valList.add("amount");
		valList.add("consName");
		valList.add("consTypeCode");
		valList.add("groupName");
		valList.add("consPhone");
		valList.add("rechargeType");
		valList.add("createTime");
		valList.add("outBillNo");
		count.put("billRechargeNo","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList, response, headList, valList, "充值记录表");
	}
	/**
	 * 查询月结记录
	 */
	public PageInfo selectYuejie(DataVo vo) throws BizException {
		List<DataVo> dataVos =selectYuejieList(vo);//月结记录list
		return new PageInfo(dataVos);
	}

	/**
	 * 月结记录查询
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	public List<DataVo> selectYuejieList(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> dataVos = gatheringMapper.selectYuejie(vo);
		List<ComboxVo> boxList1 = dictService.getDictByType("skfs");
		List<ComboxVo> boxList2 = dictService.getDictByType("sftgfp");
		for (DataVo dataVo : dataVos) {
			ChargeManageUtil.setDataVoPut("gatherWay", boxList1, dataVo);
			ChargeManageUtil.setDataVoPut("isInvoice", boxList2, dataVo);
		}
		return dataVos;
	}

	/**
	 * 月结记录合计
	 * @param vo
	 * @return
	 */
	public double selectYuejieCount(DataVo vo) throws BizException {
		vo.remove("pageNum");
		vo.remove("pageSize");

		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);

		DataVo dataCount = gatheringMapper.selectYuejieCount(vo);
		double chgAmountCount =0.0;
		if(dataCount!=null&&dataCount.size()>0){
			chgAmountCount = dataCount.getDouble("chgAmount");
		}
		return chgAmountCount;
	}
	/**
	 * 月结记录导出
	 *
	 * @param data
	 * @param response
	 */
	public void exportYuejie(DataVo data, HttpServletResponse response) throws Exception {
		List dvList = selectYuejieList(data);
		double chgAmount = selectYuejieCount(data);
		DataVo count = new DataVo();
		count.put("chgAmount",chgAmount);
		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		headList.add("集团编号");
		headList.add("集团名称");
		headList.add("收款金额(元)");
		headList.add("付款账号");
		headList.add("账号余额(元)");
		headList.add("收款人");
		headList.add("收款方式");
		headList.add("是否提供发票");
		headList.add("时间");
		headList.add("备注");
		valList.add("groupNo");
		valList.add("groupName");
		valList.add("chgAmount");
		valList.add("acctNo");
		valList.add("curAmount");
		valList.add("gatherPerson");
		valList.add("gatherWay");
		valList.add("isInvoice");
		valList.add("createTime");
		valList.add("seqDesc");
		count.put("groupNo","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList, response, headList, valList, "月结记录表");
	}
	/**
	 * 查询卡充值记录
	 */
	public PageInfo selectKcz(DataVo vo) throws BizException {
		List<DataVo> dvList = selectKczList(vo);//卡充值记录List
		return new PageInfo(dvList);
	}

	/**
	 * 卡充值list
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	public List<DataVo> selectKczList(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);
		ChargeManageUtil.stationIdsCondition(vo, RoleDataEnum.STATION.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> dvList = gatheringMapper.selectKcz(vo);
		List<ComboxVo> boxList1 = dictService.getDictByType("hylb");
		List<ComboxVo> boxList2 = dictService.getDictByType("kczfs");
		for (DataVo list : dvList) {
			ChargeManageUtil.setDataVoPut("consTypeCode", boxList1, list);
			ChargeManageUtil.setDataVoPut("rechargeType", boxList2, list);
		}

		return dvList;
	}

	/**
	 * 卡充值合计
	 * @param vo
	 * @return
	 */
	public  double selectKczCount(DataVo vo) throws BizException {
		vo.remove("pageNum");
		vo.remove("pageSize");

		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);
		ChargeManageUtil.stationIdsCondition(vo, RoleDataEnum.STATION.dataType);

		DataVo dvListCount = gatheringMapper.selectKczCount(vo);
		double amountCount =0.0;
		if(dvListCount!=null&&dvListCount.size()>0){
			amountCount =dvListCount.getDouble("amount");;
		}
		return  amountCount;
	}
	/**
	 * 卡充值记录导出
	 *
	 * @param data
	 * @param response
	 */
	public void exportKcz(DataVo data, HttpServletResponse response) throws Exception {
		List dvList =  	selectKczList(data);
		double amount =selectKczCount(data);
		DataVo count = new DataVo();
		count.put("amount",ChargeManageUtil.df.format(amount));
		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		headList.add("订单号");
		headList.add("充值金额(元)");
		headList.add("充值卡号");
		headList.add("充值时间");
		headList.add("会员名");
		headList.add("会员类型");
		headList.add("集团名称");
		headList.add("付款方式");
		headList.add("收款场站");
		valList.add("seqId");
		valList.add("amount");
		valList.add("cardId");
		valList.add("rechargeTime");
		valList.add("consName");
		valList.add("consTypeCode");
		valList.add("groupName");
		valList.add("rechargeType");
		valList.add("stationName");
		count.put("seqId","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList, response, headList, valList, "卡充值记录表");
	}
	/**
	 * 查询集团表
	 */
	public List<DataVo> selectCgroup(Map dv) {
		return gatheringMapper.selectCgroup(dv);
	}

	/**
	 * 查询会员账户信息表
	 */
	public List<DataVo> selectCacct(Map dv) {
		return gatheringMapper.selectCacct(dv);
	}

	/**
	 * 更新账户资金表
	 */
	public Integer updateCacct(Map dv) {
		return gatheringMapper.updateCacct(dv);
	}







	/**
	 * 得到月结新增的缴费用户
	 *
	 * @param vo
	 * @return
	 */
	public List<DataVo> getGroupName(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);
		return gatheringMapper.getGroupName(vo);
	}

	/**
	 * 新增月结中获取结款时间
	 *
	 * @return
	 */
	public List<DataVo> getGroupBillMonth() {
		return gatheringMapper.getGroupBillMonth();
	}

	/**
	 * 新增月结中获取合约名称
	 *
	 * @param vo
	 * @return
	 */
	public List<DataVo> getConTractName(DataVo vo) {
		return gatheringMapper.getConTractName(vo);
	}

	/**
	 * 新增月结中获取收款金额
	 *
	 * @param vo
	 * @return
	 */
	public DataVo getYuejieMoney(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo, RoleDataEnum.ORG.dataType);
		DataVo dataVo = gatheringMapper.getYuejieMoney(vo);
		return dataVo;
	}

	/**
	 * 添加月结收入
	 *
	 * @param dataMap
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int addYuejie(DataVo dataMap) throws BizException {
		DataVo cacct = gatheringMapper.findCacct(dataMap);//得到用户
	    int acctId = cacct.getInt("acctId");//得到用户id
		DataVo yjDatavo = new DataVo();
		yjDatavo.put("groupBillMonth",dataMap.getString("groupBillMonth"));
		yjDatavo.put("acctId",acctId);
		yjDatavo.put("groupBillId", dataMap.getString("groupBillId"));
        Integer yjSize =  gatheringMapper.getYuejiList(yjDatavo);//查询是否有月结记录
		if(yjSize==null||yjSize==0){
			if (cacct != null) {
				// 收款前余额
				double preAmount = cacct.getDouble("acctAmount");
				// 收款后余额
				double curAmount = new BigDecimal(String.valueOf(preAmount))
						.add(new BigDecimal(String.valueOf(dataMap.getDouble("chgAmount"))))
						.subtract(new BigDecimal(String.valueOf(dataMap.getDouble("billAmount"))))
						.doubleValue();
				cacct.put("acctAmount", curAmount);
				gatheringMapper.updateCacct(cacct);
				DataVo cas = new DataVo();
				cas.put("acctId", cacct.getString("acctId"));
				cas.put("seqFlag", 1);
				cas.put("preAmount", preAmount);
				cas.put("curAmount", curAmount);
				cas.put("chgAmount", dataMap.getDouble("chgAmount"));
				cas.put("chgType", "02");
				cas.put("seqDesc", dataMap.getString("seqDesc"));
				cas.put("isInvoice", dataMap.getString("isBill"));
				cas.put("createTime", dataMap.getString("gatherDate"));
				cas.put("gatherPerson", dataMap.getString("gatherPerson"));
				cas.put("gatherWay", dataMap.getString("gatherWay"));
				cas.put("groupBillId", dataMap.getString("groupBillId"));
				//更新cacctseq
				gatheringMapper.intoCacctSeq(cas);
				ModifyBillpayStatus(dataMap.getString("groupBillId"),dataMap.getInt("userId"),dataMap.getString("groupBillMonth"));
			}
		}else {
			throw   new BizException(1700006);
		}



		return 0;
	}

	//修改集团账单详情状态为已付，集团账单状态改为已交 操作表： C_Group_bill表
	public void ModifyBillpayStatus(String groupBillId,int userId,String groupBillMonth) throws BizException {
		Map map = new HashMap();
		map.put("groupBillId", groupBillId);
		DataVo cgb = gatheringMapper.selectCGrupBill(map);
		if (cgb != null) {
			Set<Integer> stationIds = getStationRela(cgb.getString("tractId"));
			DataVo dataVo = new DataVo();
			if (stationIds != null && stationIds.size() > 0) {
				dataVo.put("stationIds", stationIds);
				dataVo.put("groupId", cgb.getString("groupId"));
				dataVo.put("userId", userId);
				dataVo.put("groupBillMonth", groupBillMonth);
				ChargeManageUtil.orgIdsCondition(dataVo, RoleDataEnum.ORG.dataType);
				List<DataVo> listBillPay = gatheringMapper.getyujieBill(dataVo);
				Set billPayIdSet = new HashSet();
				if (listBillPay != null && listBillPay.size() > 0) {
					for (DataVo bp : listBillPay) {
                        billPayIdSet.add(bp.getInt("billPayId"));
					}
                    //设置billpay的PayState为3
                    //支付方式为2
                    if(billPayIdSet.size()>0) {
                        DataVo billPayMap = new DataVo();
                        billPayMap.put("billPayIds", billPayIdSet);
                        gatheringMapper.updateBillPay(billPayMap);
                    }
					//设置setGroupBillStatus 为1
					gatheringMapper.updateBgb(cgb.getString("groupBillId"));

				}
			}

		}
	}

	private Set<Integer> getStationRela(String tractId) {

		return gatheringMapper.getStationRela(tractId);
	}

	/**
	 * 异常订单查询
	 *
	 * @param billPayId
	 */
	public DataVo exceptionOrder(int billPayId) {
		String cardNo = "";
		String billDesc = "";
		String rechargeCard = "";
		String pileSeqId = "";
		String isReadOnly = "";
		int pileId = 0;
		String trmAddr = "";
		Double preAmount = 0.0;
		Double curAmount = 0.0;
		Double amount = 0.0;
		Double chgPower = 0.0;
		Byte state = -1;
		String startDate = "";
		String endDate = "";
		String chargeLength = "";
		Integer portNo = null;
		String seqId="";
		DataVo billPay = gatheringMapper.getBillPay(billPayId);
		cardNo = billPayId + "";
		billDesc = billPay.getString("billDesc");
		if (billDesc != null && billDesc != "" && billDesc.contains("_")) {
			rechargeCard = billDesc.substring(0, billDesc.indexOf("_"));
			pileSeqId = billDesc.substring(billDesc.indexOf("_") + 1);
			cardNo = rechargeCard;
		}
		if (billPay.getString("billPayNo").contains("CARD")) {
			isReadOnly = "true";
		} else {
			isReadOnly = "false";
		}
		if (ChargeManageUtil.isNull(rechargeCard)) {
			//为app支付
			//判断billPayId的长度，自动补全为16位
			cardNo = billPayId + "";
			for (int i = 0; i < 16 - cardNo.length(); i++) {
				cardNo = "0" + cardNo;
			}
		}
		pileId = billPay.getInt("pileId");
		DataVo pile = gatheringMapper.getPile(pileId);
		if (pile == null) {
			pile = gatheringMapper.getPileNew(pileId);
		}
		if (pile != null) {
			trmAddr = pile.getString("pileAddr");
		}

		DataVo vo = new DataVo();
		vo.put("rechargeCard", rechargeCard);
		vo.put("pileSeqId", pileSeqId);
		vo.put("cardNo", cardNo);
		List<DataVo> list = gatheringMapper.ChgRecord(vo);
		if (list.size() > 0) {
			DataVo cr = list.get(0);
			if (cr.getInt("chgType") != 0) {
				preAmount = cr.getDouble("preAmount");
				curAmount = cr.getDouble("curAmount");
				seqId = cr.getInt("seqId")+"";
				if (preAmount != null && curAmount != null)
					amount = sub(preAmount, curAmount);
				if (cr.isNotBlank("curZxygz") && cr.isNotBlank("preZxygz"))
					chgPower = sub(cr.getDouble("curZxygz"), cr.getDouble("preZxygz"));
				if (chgPower == null && amount != null) {
					chgPower = amount / cr.getDouble("preZxygz1");
				}
			}
			if (cr.isNotBlank("staTime"))
				startDate = cr.getString("staTime");
			if (cr.getInt("chgType") != 0 && cr.isNotBlank("endTime"))
				endDate = cr.getString("endTime");
			if (startDate != null && endDate != null) {
				Calendar bc = convertStrToCalendar(startDate, "yyyy-MM-dd HH:mm:ss");
				Calendar ec = convertStrToCalendar(endDate, "yyyy-MM-dd HH:mm:ss");
				chargeLength = (ec.getTimeInMillis() - bc.getTimeInMillis()) / (60 * 1000) + "";
			}
			if (cr.getInt("chgType") == 1 || cr.getInt("chgType") == 2) {
				portNo = cr.getInt("gunNo");
				state = 2;
				billPay.put("chgPower", chgPower);
				billPay.put("amount", amount);
				billPay.put("powerZxyg1", cr.getDouble("curZxygz1") - cr.getDouble("preZxygz1"));
				billPay.put("powerZxyg2", cr.getDouble("curZxygz2") - cr.getDouble("preZxygz2"));
				billPay.put("powerZxyg3", cr.getDouble("curZxygz3") - cr.getDouble("preZxygz3"));
				billPay.put("powerZxyg4", cr.getDouble("curZxygz4") - cr.getDouble("preZxygz4"));
				calcAmount(pile, billPay);
				gatheringMapper.updateBill(billPay);
			} else if (cr.getInt("chgType") == 0 && startDate != null) {
				portNo = cr.getInt("gunNo");
				Calendar cb = getCalendar(startDate);
				if (((System.currentTimeMillis() - cb.getTimeInMillis()) < 10 * 60 * 1000L)) {
					state = 5;
				} else {
					state = 4;
				}
			}
		} else {
			state = 9;
		}
		DataVo returnMap = new DataVo();
		returnMap.put("billPayId", billPayId);
		returnMap.put("seqId", seqId);
		returnMap.put("startDate", startDate);
		returnMap.put("endDate", endDate);
		returnMap.put("chargeLength", chargeLength);
		returnMap.put("chgPower", chgPower);
		returnMap.put("preAmount", preAmount);
		returnMap.put("curAmount", curAmount);
		returnMap.put("amount", amount);
		returnMap.put("state", state);
		returnMap.put("trmAddr", trmAddr);
		returnMap.put("rechargeCard", rechargeCard);
		returnMap.put("portNo", portNo);
		returnMap.put("pileId", pileId);
		returnMap.put("cardNo", cardNo);
		returnMap.put("billPayNo", billPay.get("billPayNo"));
		returnMap.put("pileId", billPay.get("pileId"));
		return returnMap;

	}

	/**
	 * 字符串格式yyyy-MM-dd HH:mm:ss转换成日期格式Calendar
	 *
	 * @param sTime
	 * @return
	 */
	public static Calendar getCalendar(String sTime) {
		return convertStrToCalendar(sTime, "yyyy-MM-dd HH:mm:ss");
	}

	public static void calcAmount(DataVo price, DataVo bill) {
		if (price.isNotBlank("prcId")) {
			int prcTypeCode = price.getInt("prcTypeCode");
			if (prcTypeCode == 1 || prcTypeCode == 3 || prcTypeCode == 4) {//单一电价
				Double elceFee = calcDl(price.getDouble("prcZxygz1"), bill.getDouble("chgPower"));
				if (elceFee > bill.getDouble("amount"))
					elceFee = bill.getDouble("amount");
				bill.put("elceFee", elceFee);
				bill.put("servFee", bill.getDouble("amount") - elceFee);
			} else { //费率电价    //所有费率电量为空
				if (bill.isNotBlank("powerZxyg1") && bill.isNotBlank("powerZxyg2") && bill.isNotBlank("powerZxyg3") && bill.isNotBlank("powerZxyg4")) {
					if (price.isNotBlank("prcService")) {
						bill.put("servFee", bill.getDouble("chgPower") * price.getDouble("prcService"));
					} else {
						bill.put("servFee", 0d);
					}
					bill.put("elceFee", bill.getDouble("amount") - bill.getDouble("servFee"));
				} else {
					Double elceFee = calcDl(price.getDouble("prcZxygz1"), bill.getDouble("powerZxyg1")) + calcDl(price.getDouble("prcZxygz2"), bill.getDouble("powerZxyg2"))
							+ calcDl(price.getDouble("prcZxygz3"), bill.getDouble("powerZxyg3")) + calcDl(price.getDouble("prcZxygz4"), bill.getDouble("powerZxyg4"));
					if (elceFee > bill.getDouble("amount"))
						elceFee = bill.getDouble("amount");
					bill.put("elceFee", elceFee);
					bill.put("servFee", bill.getDouble("amount") - elceFee);
				}
			}
		} else {
			bill.put("servFee", 0d);
			bill.put("elceFee", bill.getDouble("amount"));
		}
	}

	/**
	 * 格式化输出
	 *
	 * @param d
	 * @return <p></p>
	 */
	public static double format(Double d) {
		BigDecimal b = new BigDecimal(Double.toString(d));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 两数相乘如果某值为null直接返回0
	 *
	 * @param price
	 * @param power
	 * @return <p></p>
	 */
	private static Double calcDl(Double price, Double power) {
		if (price == null || power == null)
			return 0.0;
		return format(price * power);
	}

	/**
	 * 字符串按指定格式转换成日期格式Calendar yyyy-MM-dd HH:mm:ss/yyyy-MM-dd
	 *
	 * @param sTime
	 * @return
	 */
	public static Calendar convertStrToCalendar(String sTime, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		java.util.Date date = new java.util.Date();
		try {
			date = sdf.parse(sTime);
		} catch (java.text.ParseException ex) {
			return null;
		}
		Calendar cRt = Calendar.getInstance();
		cRt.setTime(date);
		return cRt;
	}

	/**
	 * 减法运算
	 *
	 * @param v1 被减数
	 * @param v2 减数
	 * @return
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	public DataVo exceptionBalance(DataVo vo) throws InterruptedException {
		boolean result =true;
		String message = "成功";
		int state = vo.getInt("state");
		final int pileId = vo.getInt("pileId");
		final String cardNo = vo.getString("cardNo");
		String no = vo.getString("billPayId");
		final String trmAddr = vo.getString("trmAddr");
		final int portNo = vo.getInt("portNo");
		double preAmount = 0.0;
		double curAmount = 0.0;
		Double chgPower = vo.getDouble("chgPower");
		Double amount = vo.getDouble("amount");
		String startDate = vo.getString("startDate");
		String endDate = vo.getString("endDate");
		String chargeLength = vo.getString("chargeLength");
		if (2 == state || 4 == state || 7 == state) {
			DataVo pile = gatheringMapper.getPile(pileId);
			if (pile == null) {
				pile = gatheringMapper.getPileNew(pileId);
			}
			CommunicateService cs = ServiceFactory.getCommunicateService();
			while (no.length() < 16) {
				no = "0" + no;
			}
			boolean isCardBill = false;
			if (cardNo != null && !cardNo.equals(no))
				isCardBill = true;
				if (state == 4 || state == 2) {
					if (!isCardBill && state == 4 && cs != null && cs.getTermiStatus(pileId)
							&& vo.isNotBlank("pileId") && vo.isNotBlank("cardNo") && vo.isNotBlank("trmAddr") && vo.isNotBlank("portNo")) {
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									CdzBusi.getInstance().startStopPort(pileId, trmAddr, portNo, (byte) 3, cardNo, null);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});
						t.start();
						TimeUnit.SECONDS.sleep(5);
					}
					DataVo r = gatheringMapper.getChgRecord(vo);
					if (r != null) {
						state = 2;
						preAmount = r.getDouble("preAmount");
						curAmount = r.getDouble("curAmount");
						Double pv = chgPower;
						Double av = amount;
						if (preAmount != 0 && curAmount != 0)
							av = sub(preAmount, curAmount);
						if (r.isNotBlank("curZxygz") && r.isNotBlank("cpreZxygz"))
							pv = sub(r.getDouble("curZxygz"), r.getDouble("perZxygz"));
						if (pv == null && av != null) {
							pv = av / r.getDouble("prcZxygz1");
						}
						if (pv != null && pv > 0 && pv < 500)
							chgPower = pv;
						if (av != null && av > 0 && av < 1000)
							amount = av;
						vo.put("chgPower", chgPower);
						vo.put("amount", amount);
						vo.put("powerZxyg1", r.getDouble("curZxygz1"));
						vo.put("powerZxyg2", r.getDouble("curZxygz2"));
						vo.put("powerZxyg3", r.getDouble("curZxygz3"));
						vo.put("powerZxyg4", r.getDouble("curZxygz4"));
						Calendar bc = CalendarUtils.convertStrToCalendar(r.getString("staTime"), CalendarUtils.yyyyMMddHHmmss);
						if (bc.getTimeInMillis() < System.currentTimeMillis() - 365L * 1440 * 60 * 1000L || bc.getTimeInMillis() > System.currentTimeMillis() + 365L * 1440 * 60 * 1000L) {
							bc = CalendarUtils.convertStrToCalendar(startDate, CalendarUtils.yyyyMMddHHmmss);
						}
						vo.put("startTime", bc);
						Calendar ec = CalendarUtils.convertStrToCalendar(r.getString("endTime"), CalendarUtils.yyyyMMddHHmmss);
						if (ec.getTimeInMillis() < System.currentTimeMillis() - 365L * 1440 * 60 * 1000L || ec.getTimeInMillis() > System.currentTimeMillis() + 365L * 1440 * 60 * 1000L) {
							if (chargeLength != null && !"".equals(chargeLength.trim())) {
								ec = CalendarUtils.convertStrToCalendar(startDate, CalendarUtils.yyyyMMddHHmmss);
								ec.add(Calendar.MINUTE, Integer.valueOf(chargeLength));
							}
						}
						calcAmount(pile, vo);
					}
				}
				if(state==2){
					vo.put("payState",2);
					message="保存为待支付成功";
				}else if(state==4){
					vo.put("payState",4);
					message="保存为异常结单,只有启动事件.";
				}else if(state==7){
					vo.put("payState",4);
					message="没有充电事件,保存为异常结单";
				}
				if(isCardBill){
					vo.put("payState",4);
					message ="刷卡充电,保存为异常结单";
				}
				vo.put("innerId",portNo);
				vo.put("chgPower",chgPower);
				vo.put("amount",amount);
				//payState 1：待结算；2：未付费；3：已付费
				//4：只有启动事件且充电超过了10分钟
				//5:只有充电开始记录且没超过10分钟不允许结单  7:没有停上电事件
				vo.put("isStop",1);
				gatheringMapper.updateBill(vo);
			saveLog("订单异常结算", String.valueOf(OperateType.add),"订单异常结算,订单号:"+vo.getString("billPayId")+","+"异常状态:"+state,vo.getInt("userId"));
		}else if(5==state){
			result=false;
			message ="有开始充电记录,充电时间小于10分钟不允许结单";

		}else if(state==9){
			result=false;
			message ="不允许结单,没找到对应订单";
		}else if(state==-1){
			result=false;
			message ="允许结单,未知原因";
		}
		DataVo returnMap = new DataVo();
		returnMap.put("result",result);
		returnMap.put("message",message);
		return returnMap;

	}
}
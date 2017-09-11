package com.clouyun.charge.modules.member.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.GunMapper;
import com.clouyun.charge.modules.document.mapper.PileMapper;
import com.clouyun.charge.modules.member.mapper.CouponsMapper;
import com.clouyun.charge.modules.member.mapper.GroupMapper;
import com.clouyun.charge.modules.member.mapper.MemberMapper;
import com.clouyun.charge.modules.member.mapper.PointNewMapper;
import com.clouyun.charge.modules.system.mapper.PubOrgMapper;
import com.clouyun.charge.modules.system.service.AreaService;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.clouyun.charge.modules.vehicle.service.VehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;



/**
 * 描述: Member业务类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月14日 上午10:14:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService extends BusinessService{

	public static final Logger logger = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	MemberMapper memberMapper;

	@Autowired
    DictService dictService;//字典服务

	@Autowired
	PointNewMapper pointNewMapper;//积分

	@Autowired
	CAcctService acctService;//账户

	@Autowired
	GroupMapper groupMapper;//集团

	@Autowired
	PubOrgMapper pubOrgMapper;//企业

	@Autowired
	AreaService areaService;//地图

	@Autowired
	UserService userService;

	@Autowired
	PileMapper pileMapper;

	@Autowired
	GunMapper gunMapper;

	@Autowired
	CouponsMapper couponsMapper;

	@Autowired
	VehicleService vehicleService;
	/*
	 * 会员列表
	 */
	public PageInfo queryMembers(Map map) throws BizException{
		DataVo params = new DataVo(map);

		getPermission(params);

		// 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(params);
        }

		List<Map> result = queryMembersResult(params);

		PageInfo page = new PageInfo(result);

		params = null;
		map = null;
		result = null;

		return page;
	}

	private void getPermission(DataVo params) throws BizException {
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId")){
            throw new BizException(1000006);
        }
		// 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataByIdCoopRev(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(userRoleDataById != null && userRoleDataById.size() > 0){
            params.put("orgIds", userRoleDataById);
        }
	}

	private List<Map> queryMembersResult(DataVo map) throws BizException {
		//卡号查询
		List<Integer> queryConsByCardId = null;
		if(null != map.get("rechargeCard") && !"".equals(map.get("rechargeCard"))){
			queryConsByCardId = memberMapper.queryConsByCardId(map.get("rechargeCard").toString());
			if(queryConsByCardId == null || queryConsByCardId.size() <= 0){
				queryConsByCardId = new ArrayList<>();
				queryConsByCardId.add(-99);
			}
			map.put("consIds", queryConsByCardId);
		}

		//会员来源
		List<String> consFromList = new ArrayList<>();
		if(null != map.get("consFrom")){
			if("01".equals(map.get("consFrom")) || "05".equals(map.get("consFrom"))){
				consFromList.add("01");
				consFromList.add("05");
				map.put("consFroms", consFromList);
			}else{
				consFromList.add(map.get("consFrom").toString());
				map.put("consFroms", consFromList);
			}
		}

		List<Map> result = memberMapper.queryMembers(map);

		if(result != null && result.size() > 0){
			//结果集处理,积分,充值卡号拼接
			resultProcessing(result,map);
		}

		queryConsByCardId = null;
		consFromList = null;

		return result;
	}

	/*
	 *	结果集处理,积分,充值卡号拼接
	 */
	private void resultProcessing(List<Map> result,Map map2) throws BizException {
		List<Integer> consIds = new ArrayList<>();
		Map consIdMap = new HashMap();
		StringBuffer sb = new StringBuffer(60);
		if(result != null && result.size() > 0){
			for (Map map : result) {
				consIds.add(Integer.parseInt(map.get("consId").toString()));
				//省市需要拼接起来
    			if(map.get("provCode") != null && !"".equals(map.get("provCode"))){
    				sb.append(areaService.getAreaNameByNo(map.get("provCode").toString()));
    			}
    			if(map.get("cityCode") != null && !"".equals(map.get("cityCode"))){
    				sb.append(areaService.getAreaNameByNo(map.get("cityCode").toString()));
    			}
    			if(map.get("distCode") != null && !"".equals(map.get("distCode"))){
    				sb.append(areaService.getAreaNameByNo(map.get("distCode").toString()));
    			}
    			map.put("locationArea", sb.toString());
    			sb.setLength(0);

    			if("05".equals(map.get("consFrom"))){
    				map.put("consFrom", "01");
    			}
			}
		}
		consIdMap.put("consIds", consIds);

		if(map2.get("pageSize") == null && map2.get("pageNum") == null){
			consIdMap = null;
			consIds = null;
		}
		//积分规则
		getGainPoint(result, consIdMap);
		//充值卡号
		rechargeCard(result,consIds);

		sb = null;
		consIdMap = null;
		consIds = null;

	}

	/*
	 * 获取积分规则
	 */
	private void getGainPoint(List<Map> result, Map consIdMap) {
		//获取积分规则
		List<Map> integrals = pointNewMapper.queryPointsHistoryInfo(consIdMap);
		Map<String,Integer> integralMap = new HashMap<>();
		if(integrals != null && integrals.size() > 0){
			Integer integral = 0;
			for (Map map2 : integrals) {
				if(null != map2.get("gainPoint")){
					integral = Integer.parseInt(map2.get("gainPoint").toString());
				}
				if(map2.get("consId") != null && !"".equals(map2.get("consId"))){
					integralMap.put(map2.get("consId").toString(), integral);
				}
			}
		}
		//获取积分
		if(result != null && result.size() > 0){
			for (Map dataMap : result) {
				Integer gainPoint = 0;
				Integer point = integralMap.get(dataMap.get("consId").toString());
				if (point != null){
					gainPoint = point;
				}
				dataMap.put("gainPoint", gainPoint);
			}
		}

		integralMap = null;
		integrals = null;
	}

	/*
	 * 处理充值卡号
	 */
	private void rechargeCard(List<Map> result,List<Integer> consIds) {
		//充值卡号拼接
		List<Map> ccardLists = memberMapper.queryAllCCard(consIds);
		List<Map> ccardRechargeLists = memberMapper.queryAllCCardRecharge(consIds);
		List<Map> billPayLists = memberMapper.queryBillPayCountAndAmount(consIds);
		List<Map> billRechargeList = memberMapper.queryAllBillRecharge(consIds);
		Map<Integer,List<String>> ccardMap = new HashMap();
		Map<Integer,BigDecimal> amountMap = new HashMap<>();
		Map<Integer,Map> billPayMap = new HashMap<>();
		if(ccardLists != null && ccardLists.size() > 0){
			for (Map map2 : ccardLists) {
				if(map2.get("consId") != null && !"".equals(map2.get("consId"))){
					Integer consId = Integer.parseInt(map2.get("consId").toString());
					if(ccardMap.containsKey(consId)){
						List<String> list = ccardMap.get(consId);
						list.add(map2.get("cardId").toString());
						ccardMap.put(consId, list);
					}else{
						List<String> list = new ArrayList<>();
						list.add(map2.get("cardId").toString());
						ccardMap.put(consId, list);
					}
				}
			}
		}

		//获取充值金额
		getRechargeAmount(ccardRechargeLists, amountMap);
		//获取消费金额
		getRechargeAmount(billRechargeList, amountMap);

		if(billPayLists != null && billPayLists.size() > 0){
			for (Map map2: billPayLists) {
				DataVo vo = new DataVo(map2);
				if(vo.isNotBlank("consId")){
					Integer consId = Integer.parseInt(map2.get("consId").toString());
					billPayMap.put(consId,map2);
				}
			}
		}


        StringBuffer cards = new StringBuffer(128) ;
        for (Map map2 : result) {
            Integer consId = Integer.parseInt(map2.get("consId").toString());
            if(ccardMap != null && ccardMap.size() > 0){
                List<String> RechargeCardList = ccardMap.get(consId);
                if(RechargeCardList != null){
                    for (String card : RechargeCardList) {
                        cards.append(card).append(",");
                    }
                    String card = cards.toString();
                    if(card.length() > 1){
                        //截取最后一个逗号
                        card = card.substring(0, card.length() - 1);
                    }
                    //拼接出充值卡号
                    map2.put("rechargeCard", card);
                }
                //清空拼接
                cards.setLength(0);
            }
            //充值金额
            BigDecimal amount = BigDecimal.ZERO;
            if (amountMap.get(consId) != null){
                amount = amountMap.get(consId);
            }
            map2.put("rechargeAmount",amount);
            //充电次数,消费金额
            Map billPayMaps = billPayMap.get(consId);

            Double consCount = 0.0D;
            Double amountSum = 0.0D;

            if(billPayMaps != null && billPayMaps.size() > 0){
                if(billPayMaps.get("conscount") != null && !"".equals(billPayMaps.get("conscount"))){
                    String conscount = billPayMaps.get("conscount").toString();
                    consCount = Double.parseDouble(conscount);
                }
                if(billPayMaps.get("amountsum") != null && !"".equals(billPayMaps.get("amountsum"))){
                    String amountsum = billPayMaps.get("amountsum").toString();
                    amountSum = Double.parseDouble(amountsum);
                }
            }


            map2.put("chargeNum", consCount);
            map2.put("consAmount", amountSum);

		}
		ccardMap = null;
		amountMap = null;
		billPayMap = null;
		ccardLists = null;
		ccardRechargeLists = null;
		billPayLists = null;
		billRechargeList = null;
	}

	private void getRechargeAmount(List<Map> ccardRechargeLists, Map<Integer, BigDecimal> amountMap) {
		if(ccardRechargeLists != null && ccardRechargeLists.size() > 0){
			for (Map map2: ccardRechargeLists) {
				DataVo vo = new DataVo(map2);
				if(map2.get("consId") != null && !"".equals(map2.get("consId"))){
					Integer consId = Integer.parseInt(map2.get("consId").toString());
					if(amountMap.containsKey(consId)){
						BigDecimal amount = amountMap.get(consId);
						if(vo.isNotBlank("amount")){
							amount = amount.add(new BigDecimal(vo.getString("amount")));
						}
						amountMap.put(consId, amount);
					}else{
						BigDecimal amount = BigDecimal.ZERO;
						if(vo.isNotBlank("amount")){
							amount = new BigDecimal(vo.getString("amount"));
						}
						amountMap.put(consId, amount);
					}
				}
			}
		}
	}

	/*
	 * 会员账户流水详情
	 */
	public PageInfo queryConsFlow(Map map) throws BizException{
		DataVo params = new DataVo(map);
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(params);
        }
        //默认排序条件和方式
        if(params.isBlank("sort")){
        	params.put("sort", "create_time");
        	params.put("order", "DESC");
        }
		params.put("orderStatus", -1);
		//会员流水
        List<Map> result = memberMapper.queryConsFlow(params);
		//注销流水
		removePageSizeNum(params);
		List<DataVo> cAcctSeqResult = memberMapper.queryCAcctSeq(params);

        if(result != null && result.size() > 0){

        	//充值方式(01,支付宝,02,微信)
        	List<ComboxVo> rechargeWayList = dictService.getDictByType("czfs");
        	//交易类型(01.充值,02.支付)
        	List<ComboxVo> tradeTypeList = dictService.getDictByType("jylx");
        	//收款方式
        	List<ComboxVo> gatherWayList = dictService.getDictByType("shoukfs");
        	//支付状态
        	List<ComboxVo> payStatusList = dictService.getDictByType("zfzt");
        	//卡充值方式
        	List<ComboxVo> cardRechargeTypeList = dictService.getDictByType("kczfs");

        	for (Map dataMap : result) {
				DataVo vo = new DataVo(dataMap);

				Double amount = 0d;
        		if(vo.isNotBlank("amount")){
        			amount = Double.parseDouble(vo.getString("amount"));
        		}

        		dataMap.put("amount", getChgAmount(vo.getString("transType"),amount));

				String payType = vo.getString("payType");
				String transType = vo.getString("transType");
				String payState = vo.getString("payState");
				if("01".equals(transType)){
        			//支付方式
					dataMap.put("payType", dictService.getDictLabel("czfs",payType));
					//支付状态
					dataMap.put("payState","支付成功");
        			//交易类型
    				dataMap.put("transType",dictService.getDictLabel("jylx",transType));
        		}else if ("02".equals(transType)){
        			//收款方式
					dataMap.put("payType", dictService.getDictLabel("shoukfs",payType));
					//支付状态
					dataMap.put("payState",dictService.getDictLabel("zfzt",payState));
					//交易类型
					dataMap.put("transType",dictService.getDictLabel("jylx",transType));
				}else if ("03".equals(transType)){
        			dataMap.put("payType",dictService.getDictLabel("kczfs",transType));
					dataMap.put("transType","卡充值");
					dataMap.put("payState", "支付成功");
        		}
			}

			rechargeWayList = null;
			tradeTypeList = null;
			gatherWayList = null;
			payStatusList = null;
			cardRechargeTypeList = null;
        }

        if (cAcctSeqResult != null && cAcctSeqResult.size() > 0){
			for (DataVo seq : cAcctSeqResult) {
				Map arr = new HashMap();
				String chgAmount = seq.getString("chgAmount");
				String amount = "0.0";
				//注销金额,展示为负数
				//浮点小数比较不准
				if(new BigDecimal(chgAmount).compareTo(BigDecimal.ZERO) > 0){
					amount = "-" + chgAmount;
				}

				arr.put("amount", amount);//注销-->支出
				arr.put("createTime", seq.get("createTime"));
				String label = dictService.getDictLabel("jylx", seq.getString("chgType"));
				if (label != null && !"".equals(label)){
					arr.put("transType", label);
					arr.put("payState", "已" + label);
				}
				result.add(arr);
			}
		}
        PageInfo page = new PageInfo(result);

		map = null;
		params = null;
		result = null;
		cAcctSeqResult = null;
		return page;
	}

	private void removePageSizeNum(DataVo params) {
		params.remove("pageNum");
		params.remove("pageSize");
	}

	/*
	 * 新增会员
	 */
	public DataVo insertMembers(Map map) throws BizException{

		DataVo vo = new DataVo(map);
        //获取登陆用户的orgId
		if (vo.isBlank("userId")){
			throw new BizException(1000006);
		}
		saveLog("会员新增", OperateType.add.OperateId,"新增会员:"+vo.getString("consName"),vo.getInt("userId"));
		vo.put("orgId", userService.getOrgIdByUserId(vo.getInt("userId")));
		//检查参数
		check(vo);

		//账户信息
		vo.put("acctAmount", 0);
		vo.put("acctNo", vo.getString("consPhone"));
		vo.put("acctStateCode", "01");
		//新增账户
		acctService.insertCAcct(vo);

		//修改人,获取登陆用户id
		vo.put("consModifierId", vo.getInt("userId"));
		if(vo.isBlank("consFrom")){
			vo.put("consFrom","03");
		}
		vo.put("isCrc",0);
		vo.put("consStatus", 0);
		memberMapper.insertMembers(vo);

		map = null;
		return vo;
	}

	/*
	 * 更新会员
	 */
	public int updateMembers(Map map) throws BizException{
		DataVo vo =  new DataVo(map);
		if (vo.isBlank("userId")){
        	throw new BizException(1000006);
        }
		saveLog("会员修改", OperateType.update.OperateId,"修改会员:"+vo.getString("consName")+";会员id:"+vo.getInt("consId"),vo.getInt("userId"));

		//获取登陆用户的orgId
		if(vo.isBlank("orgId")){
			vo.put("orgId", userService.getOrgIdByUserId(vo.getInt("userId")));
		}
		//检查参数
		check(vo);
		if(vo.isBlank("consFrom")){
			//来源为空默认是用户导入
			vo.put("consFrom", "03");
		}

		//获取账户状态,层级取值
		Map acctInfo = (Map) vo.get("acctInfo");
		if(acctInfo != null){
			vo.put("acctStateCode", acctInfo.get("acctStateCode"));
			vo.put("acctAmount",acctInfo.get("acctAmount"));
			//更改账户
			acctService.updateCAcct(vo);
		}

		//修改人,获取登陆用户id
		map.put("consModifierId", vo.getInt("userId"));

		int insertCount = memberMapper.updateMembers(vo);

		map = null;
		vo = null;
		acctInfo = null;
		return insertCount;
	}

	/*
	 * 检查参数
	 */
	private void check(DataVo map) throws BizException {

		if(map.isBlank("consName")){
			throw new BizException(1200000,"会员名称");
		}
		if(map.isBlank("consPhone")){
			throw new BizException(1200000,"会员手机号码");
		}
		if(map.isBlank("consTypeCode")){
			throw new BizException(1200000,"会员类型");
		}else{
			//集团会员
			if ("02".equals(map.getString("consTypeCode")) && map.isBlank("groupId")){
				throw new BizException(1200000,"所属集团");
			}

		}
		if(map.isBlank("appFrom")){
			throw new BizException(1200001);
		}

		if (!ValidateUtils.Number(map.getString("consPhone"))){
			throw new BizException(1000014, "手机号码");
		}else if(map.getString("consPhone").length() != 11) {
			throw new BizException(1006001, "手机号码",11);
		}
		if (map.isNotBlank("consEmail") && !ValidateUtils.Email(map.getString("consEmail"))){
			throw new BizException(1000014, "邮箱");
		}

		//多账号查询
		Integer checkPhoneCount = memberMapper.checkPhone(map);

		if(checkPhoneCount > 0){
			throw new BizException(1200002);
		}

		//个人会员
		if (map.isNotBlank("consTypeCode") && "01".equals(map.getString("consTypeCode"))){
			map.put("groupId", "-99");
		}
	}

	/*
	 * 查询所有的订单
	 */
	/*private void queryAllOrder(Map<Object, Map> billPayMap,
			Map<Object, Map> billRechargeMap, Map<Object, Map> cCardRechargeMap,List<String> billPayIds) {
		List<Map> billPayList = memberMapper.queryBillPay(billPayIds);
        List<Map> billRechargeList = memberMapper.queryBillRecharge(billPayIds);
        List<Map> cCardRechargeList = memberMapper.queryCCardRecharge(billPayIds);

        //消费订单
        for (Map billPay : billPayList) {
        	billPayMap.put(billPay.get("billPayNo"), billPay);
		}
        //充值订单
        for (Map billRecharge : billRechargeList) {
        	billRechargeMap.put(billRecharge.get("billRechargeNo"), billRecharge);
        }
        //充值卡充值
        for (Map cardRecharge : cCardRechargeList) {
        	cCardRechargeMap.put(cardRecharge.get("seqId"), cardRecharge);
        }
	}*/

	public PageInfo queryConsCoupons(Map map) throws BizException{
		DataVo params = new DataVo(map);
		//获取优惠券
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(map);
		}
		//查询优惠券未使用
		params.put("status","0");
		List<Map> couponsInfo = couponsMapper.queryCouponsInfo(params);

		PageInfo page = new PageInfo(couponsInfo);

		map = null;
		params = null;
		couponsInfo = null;

		return page;
	}


	/*
	 * 会员信息
	 */
	public List<Map> selectMemberByKey(Integer consId) throws BizException{

		List<Map> members = memberMapper.queryMemberByKey(consId);
		//获取积分
		List consIds = new ArrayList();
		consIds.add(consId);
		Map consIdMap = new HashMap();
		consIdMap.put("consIds", consIds);
		//获取积分规则
		getGainPoint(members, consIdMap);

		//获取优惠券
		consIdMap.put("consId", consId);
		consIdMap.put("status","0");
		List<Map> couponsInfo = couponsMapper.queryCouponsInfo(consIdMap);

		//充值卡号
		List<Map> ccardLists = memberMapper.queryAllCCard(consIds);

		List<Map> bpAmountList = memberMapper.queryBillPayAmount(consId);

		Map acctMap = new HashMap();
		Map<Object,BigDecimal> bpAmountMap = new HashMap();

		if(bpAmountList != null && bpAmountList.size() > 0){
			for (Map map : bpAmountList) {
				DataVo vo = new DataVo(map);
				Object cardId = vo.get("cardId");
				if(bpAmountMap.get(cardId) != null){
					BigDecimal amount = bpAmountMap.get(cardId);
					if(vo.isNotBlank("amount")){
						amount = amount.add(new BigDecimal(vo.getString("amount")));
					}
					bpAmountMap.put(cardId, amount);
				}else{
					BigDecimal amount = BigDecimal.ZERO;
					if(vo.isNotBlank("amount")){
						amount = new BigDecimal(vo.getString("amount"));
					}
					bpAmountMap.put(cardId, amount);
				}
			}
		}


		if(members != null && members.size() > 0){
			StringBuffer sb = new StringBuffer(128);
			for (Map member : members) {
				Object carId = member.get("carId");
				member.put("vehicleInfo", "");
				if(carId != null && !"".equals(carId)){
					Map vehicle = vehicleService.getVehicle(Integer.parseInt(carId.toString()));
					//省市需要拼接起来
					if (vehicle != null && vehicle.size() > 0){
						if(vehicle.get("proviceCode") != null && !"".equals(vehicle.get("proviceCode"))){
							sb.append(areaService.getAreaNameByNo(vehicle.get("proviceCode").toString()));
						}
						if(vehicle.get("cityCode") != null && !"".equals(vehicle.get("cityCode"))){
							sb.append(areaService.getAreaNameByNo(vehicle.get("cityCode").toString()));
						}
						vehicle.put("locationArea", sb.toString());
					}
					member.put("vehicleInfo", vehicle);
					sb.setLength(0);
				}

				if(ccardLists != null && ccardLists.size() > 0){
					for (Map card : ccardLists) {
						if(card.get("vehicleId") != null && !"".equals(card.get("vehicleId"))){
							card.put("binding", "是");
						}else{
							card.put("binding", "否");
						}
						String cardIdObj = "";
						String newCardAmount = "";
						if(card.get("cardId") != null){
							cardIdObj = card.get("cardId").toString();
							if(cardIdObj.length() < 16 && null != cardIdObj){
								StringBuffer sbf= new StringBuffer(20);
								int zeroSize = 16 - cardIdObj.length();
								for(int j = 0;j < zeroSize;j++){
									sb.append("0");
								}
								sb.append(cardIdObj);
								newCardAmount=sb.toString();
							}else{
								newCardAmount=cardIdObj;
							}
						}
						card.put("historyAmount", bpAmountMap.get(newCardAmount));
					}
					member.put("cardInfo", ccardLists);
				}else{
					member.put("cardInfo", "");
				}
				acctMap.put("acctAmount", member.get("acctAmount"));
				acctMap.put("acctStateCode", member.get("acctStateCode"));
				acctMap.put("createTime", member.get("createTime"));
				acctMap.put("gainPoint", member.get("gainPoint"));
				acctMap.put("appName", member.get("appName"));
				Object consFrom = member.get("consFrom");
				if(consFrom != null && "05".equals(consFrom.toString())){
					//5和1都是安卓,字典没有5,这里兼容一下
					consFrom = "01";
				}
				acctMap.put("consFrom", consFrom);
				acctMap.put("couponCount", couponsInfo.size());

				member.put("acctInfo", acctMap);
			}
		}

		consIds = null;
		consIdMap = null;
		couponsInfo = null;
		bpAmountList = null;
		bpAmountMap = null;
		return members;
	}

	/*
	 * 对交易金额进行正负判断
	 */
	private String getChgAmount(String seqFlag,Double chgAmount){
		if(chgAmount == 0){
			return chgAmount + "";
		}
		if("01".equals(seqFlag) || "03".equals(seqFlag)){
			return "+" + chgAmount;
		}else{
			return "-" + chgAmount;
		}
	}

	/*
	 * 会员导出
	 */
	public void export(Map map,HttpServletResponse response) throws Exception{
		DataVo params = new DataVo(map);
		//权限
		getPermission(params);

		//结果集
		List<Map> list = queryMembersResult(params);
		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		//转义字典
		escapeResult(list);

		headList.add("会员名称");
		headList.add("电话号码");
		headList.add("会员类型");
		headList.add("所属集团");
		headList.add("充值卡号");
		headList.add("账户余额(元)");
		headList.add("来源");
		headList.add("车牌");
		headList.add("所在区域");
		headList.add("详细地址");
		headList.add("积分");
		headList.add("运营客户来源");
		headList.add("状态");

		valList.add("consName");
		valList.add("consPhone");
		valList.add("consTypeCode");
		valList.add("groupName");
		valList.add("rechargeCard");
		valList.add("acctAmount");
		valList.add("consFrom");
		valList.add("licensePlate");
		valList.add("locationArea");
		valList.add("consAddr");
		valList.add("gainPoint");
		valList.add("orgName");
		valList.add("consStatus");
		ExportUtils.exportExcel(list, response,headList,valList,"会员信息");

	}

	private void escapeResult(List<Map> list) throws BizException {
		//会员类型
    	List<ComboxVo> consTypeList = dictService.getDictByType("hylb");
    	//会员来源
    	List<ComboxVo> consFromList = dictService.getDictByType("hyly");
    	//会员状态
    	List<ComboxVo> consStatusList = dictService.getDictByType("zt");

    	StringBuffer sb = new StringBuffer(60);
		if(list != null && list.size() > 0){
			for (Map result : list) {
				//会员类型
    			if(result.get("consTypeCode") != null){
    				for (ComboxVo comboxVo : consTypeList) {
    					if(result.get("consTypeCode").equals(comboxVo.getId())){
    						result.put("consTypeCode", comboxVo.getText());
    					}
    				}
    			}
    			//会员来源
    			if(result.get("consFrom") != null){
    				for (ComboxVo comboxVo : consFromList) {
    					//老平台上的数据转换,平移过来
    					if("05".equals(result.get("consFrom"))){
    						result.put("consFrom","01");
    					}

    					if(result.get("consFrom").equals(comboxVo.getId())){
    						result.put("consFrom", comboxVo.getText());
    					}
    				}
    			}

    			//会员类型
    			if(result.get("consStatus") != null){
    				for (ComboxVo comboxVo : consStatusList) {
    					if(result.get("consStatus").toString().equals(comboxVo.getId())){
    						result.put("consStatus", comboxVo.getText());
    					}
    				}
    			}

    			//省市需要拼接起来
    			if(result.get("provCode") != null && !"".equals(result.get("provCode"))){
    				sb.append(areaService.getAreaNameByNo(result.get("provCode").toString()));
    			}
    			if(result.get("cityCode") != null && !"".equals(result.get("cityCode"))){
    				sb.append(areaService.getAreaNameByNo(result.get("cityCode").toString()));
    			}
    			if(result.get("distCode") != null && !"".equals(result.get("distCode"))){
    				sb.append(areaService.getAreaNameByNo(result.get("distCode").toString()));
    			}
    			result.put("locationArea", sb.toString());
    			sb.setLength(0);
			}
		}

		consTypeList = null;
		consFromList = null;
		consStatusList = null;
	}

	/*
	 * 导入会员
	 */
	public List<Map> importMembers(MultipartFile excelFile,Map map) throws Exception{
		String canSave = "true";
		DataVo params = new DataVo(map);
		if (params.isBlank("userId")){
        	throw new BizException(1000006);
        }

		//解析excel
		List<Object[]> arrayList = parseExcel(excelFile);
		//key存放groupNo,value存放groupId
		Map<String, Integer> gmap = getGroupId();
		//key存放orgName,value存放orgId
		Map<String, Integer> orgMap = getAllOrg();
		//所属企业的orgId,如果没有获取到,查询-1.
		Integer appFromId = -1;
		//根据企业id获取车辆信息,key是车牌,value是车辆id
		Map<String, Integer> vehicleInfoMap = getVehicleInfoMapForOrgId(params);

		Set<String> phoneSets = new HashSet<String>();
		List<String> theOrgName =  new ArrayList<>();
		List<Map> allConsMap = new ArrayList<Map>();
		//先获取所有的新增车牌信息，并向车牌信息里面新增
		List<Map> vList = new ArrayList<Map>();
		Integer orgId = userService.getOrgIdByUserId(params.getInt("userId"));

		for(Object[] obj : arrayList){
			if(obj[6] != null){
				if(null == vehicleInfoMap.get(obj[6].toString())){
					//为新车牌，需要向车辆信息表中添加车辆信息
					Map vi = new HashMap();
					if(obj[5] != null){
						vi.put("model",obj[5].toString());
					}
					vi.put("licensePlate",obj[6].toString());
					//获取登陆用户的orgId
					vi.put("orgId",orgId);
					//默认社会车辆
					vi.put("belongsType","1");
					//营运性质默认为否
					vi.put("operationRoperty","1");
					vList.add(vi);
				}
			}
		}
		if(vList.size()>0){
			//新增车辆
			memberMapper.inserVehicle(vList);

			//之前在判断外,应该是有车牌新增或者修改之后再去调用查询
			vehicleInfoMap = getVehicleInfoMapForOrgId(params);
		}

		//导入数据转义
		for (Object[] obj : arrayList) {
			Map cons = transObj(obj, gmap);
			if(!phoneSets.contains("" + obj[2])){
				appFromId = orgMap.get(obj[12].toString());
				phoneSets.add("" + obj[2]);
				if(obj[13] != null){
					cons.put("check" , obj[13].toString());
				}

				if(null != cons.get("carLicense") && vehicleInfoMap.containsKey(cons.get("carLicense"))){
					cons.put("carId",Integer.parseInt(vehicleInfoMap.get(cons.get("carLicense")).toString()));
				}
				//一次导入的过程中只能导入同企业的数据
				if(theOrgName.size() == 0){
					theOrgName.add(obj[12].toString());
				}else{
					if(!theOrgName.contains(obj[12]+"")){
						if(cons.values().contains("校验正确")){
							cons.put("check","校验错误:只能导入同企业数据");
						}
						canSave = "false";
					}
				}
				if(obj[13]!=null&&!obj[13].equals("校验正确")){
					canSave = "false";
					cons.put("check",obj[13].toString());
				}

				if(obj[12] != null){
					//获取登陆用户的企业id
		        	Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);

		        	if(null != orgIds){
						if(!orgIds.contains(appFromId)){
							canSave = "false";
							if(cons.values().contains("校验正确")) {
								cons.put("check","校验错误：所属企业没有导入权限！");
							}
						}
					}

					if(null != appFromId){
						cons.put("appFrom", appFromId);
					}else{
						canSave = "false";
						if(cons.values().contains("校验正确")) {
							cons.put("check","校验错误：所属企业不存在！");
						}
					}

					orgIds = null;
				}

				if(cons.get("consPhone") != null){
					String consPhone = cons.get("consPhone").toString();
					if (!ValidateUtils.Number(consPhone)){
						if(cons.values().contains("校验正确")) {
							map.put("check", "校验失败:手机号码格式不正确");
						}
					}else if(consPhone.length() != 11) {
						if(cons.values().contains("校验正确")) {
							map.put("check","校验失败:手机号码必须为11位");
						}
					}
				}

				allConsMap.add(cons);
			}
		}


		List<Map> consList = new ArrayList<Map>();
		Map<String, Map> consMap = new HashMap<String, Map>();
		Map queryMap = new HashMap();
		//通过手机号和企业id查询会员信息
		queryMap.put("phoneNos", phoneSets);
		queryMap.put("orgId", appFromId);
		List<Map> infoByPhone = memberMapper.queryInfoByPhone(queryMap);
		//校验手机号是否存在
		List<Map> cList = new ArrayList<Map>();
		if(infoByPhone.size() > 0){
			for (Map cCons : infoByPhone) {
//				cCons.put("check","手机号已存在");
				cList.add(cCons);
			}
		}
		consList.addAll(cList);

		if (consList.size() > 0) {
			for (Map c : consList) {
				consMap.put(c.get("consPhone").toString(), c);
			}
		}
		//新增list
		List<Map> saveList = new ArrayList<Map>();
		//更新list
		List<Map> updateList = new ArrayList<Map>();

		for (Map c : allConsMap) {
			String consPhone = null;
			Object cConsPhone = c.get("consPhone");
			if(cConsPhone != null){
				consPhone = cConsPhone.toString();
			}
			Map exisCons = consMap.get(consPhone);
			if (exisCons != null) {
				if(c.get("orgName").equals(exisCons.get("orgName")) && cConsPhone.equals(exisCons.get("consPhone"))){
					if(c.values().contains("校验正确")) {
						c.put("check", "手机号已存在");
					}
					canSave = "false";
				}
				c.put("consId",exisCons.get("consId"));
				c.put("acctId",exisCons.get("acctId"));
				updateList.add(c);
			}else{
				c.put("acctAmount",0d);
				c.put("acctStateCode","01");
				c.put("acctNo",c.get("consPhone"));
				saveList.add(c);
			}
		}
		if(canSave.equals("false")){
			for (Map c : allConsMap) {
				Map exisCons = consMap.get(c.get("consPhone"));
				if (exisCons != null) {
					c.put("result", "更新失败");
				}else{
					c.put("result", "保存失败");
				}
			}
		}else{
			try{
				if(updateList.size()>0){
					//批量更新
					memberMapper.batchUpdateCons(updateList);
				}
				if(saveList.size()>0){
					//批量插入账户
					acctService.batchInserCAcct(saveList);
					//批量插入汇演
					memberMapper.batchInsertCons(saveList);
				}
				putImportConsResult(allConsMap, consMap , "成功");
			}catch(Exception e){
				canSave = "false";
				putImportConsResult(allConsMap, consMap , "失败");
			}
		}
		escapeResult(allConsMap);

		arrayList = null;
		gmap = null;
		orgMap = null;
		vehicleInfoMap = null;
		phoneSets = null;
		theOrgName = null;
		vList = null;
		consList = null;
		consMap = null;
		queryMap = null;
		infoByPhone = null;
		cList = null;
		saveList = null;
		updateList = null;

		return allConsMap;
	}

	private void putImportConsResult(List<Map> allConsMap, Map<String, Map> consMap,String arr) {
		for (Map c : allConsMap) {
            Map exisCons = consMap.get(c.get("consPhone").toString());
            if (exisCons != null) {
                c.put("result","更新" + arr);
            }else{
                c.put("result","保存" + arr);
            }
        }
	}

	private Map transObj(Object[] obj, Map<String, Integer> gmap) {
		Map map = new LinkedHashMap();
		if(gmap.get(obj[0])!=null){
			//集团会员
			map.put("groupId", gmap.get(obj[0]).toString());
			map.put("consTypeCode", "02");
		}else{
			//个人会员
			map.put("consTypeCode", "01");
		}

		if(obj[1] != null){
			map.put("consName", obj[1].toString());
		}

		if(obj[2] != null){
			map.put("consPhone", obj[2].toString());
		}
		if(obj[3] != null){
			map.put("consEmail", obj[3].toString());
		}
		if(obj[4] != null){
			map.put("consAddr", obj[4].toString());
		}
		if(obj[5] != null){
			map.put("carModel", obj[5].toString());
		}
		if(obj[6] != null){
			map.put("carLicense", obj[6].toString());
		}
		if(obj[7] != null){
			map.put("consNo", obj[7].toString());
		}
		//导入用户来源默认导入
		map.put("consFrom", "03");

		if(obj[10] != null){
			map.put("wechatNo", obj[10].toString());
		}

		if(null != obj[11]){
			map.put("consReferrerId", obj[11].toString());
		}

		if(null != obj[12]){
			map.put("orgName", obj[12].toString());
		}
		//会员状态有效
		map.put("consStatus", 0);
		//默认不是华润用户
		map.put("isCrc", 0);
		return map;
	}

	/*
	 * key存放licensePlate,value存放vehicleId
	 */
	private Map<String,Integer> getVehicleInfoMapForOrgId(DataVo params) throws BizException{
		Set<Integer> orgsByUserId = new HashSet<Integer>();
		Map<String,Integer> map = new HashMap<>();

		 // 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(userRoleDataById != null && userRoleDataById.size() > 0){
			orgsByUserId.addAll(userRoleDataById);
		}

		List<Map> queryVehicleInfoByOrgId = memberMapper.queryVehicleInfoByOrgId(orgsByUserId.toArray());

		if (queryVehicleInfoByOrgId != null && queryVehicleInfoByOrgId.size() > 0){
			for(Map vi: queryVehicleInfoByOrgId){
				map.put(vi.get("licensePlate").toString(), Integer.parseInt(vi.get("vehicleId").toString()));
			}

		}

		orgsByUserId = null;
		userRoleDataById = null;
		queryVehicleInfoByOrgId = null;
		return map;
	}


	/*
	 * key存放orgName,value存放orgId
	 */
	private Map<String,Integer> getAllOrg(){
		Map<String,Integer> orgMap = new HashMap<>();
		List<DataVo> list = pubOrgMapper.getOrgsByPage(new HashMap());
		if(list.size() > 0){
			for (DataVo pubOrg : list) {
				orgMap.put(pubOrg.get("orgName").toString(), pubOrg.getInt("orgId"));
			}
		}
		list = null;
		return orgMap;
	}

	/*
	 * key存放groupNo,value存放groupId
	 */
	private Map<String, Integer> getGroupId() {
		List<Map> groupList = groupMapper.queryGroups(new HashMap());
		Map<String, Integer> gmap = new HashMap<String, Integer>();
		for (Map c : groupList) {
			gmap.put(c.get("groupNo").toString(), Integer.parseInt(c.get("groupId").toString()));
		}
		groupList = null;
		return gmap;
	}


	/*
	 * 解析excel
	 */
	public List<Object[]> parseExcel(MultipartFile fileName) throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Workbook wb = null;
		Sheet sheet = null;

		try {
			//xls格式
			wb = new HSSFWorkbook(fileName.getInputStream());
		} catch (Exception e) {
			//xlsx格式
			wb = new XSSFWorkbook(fileName.getInputStream());
		}
		sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		if (rows == 1) {
			throw new Exception("excel文档无数据，请核对！");
		}
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				// 获取到Excel文件中的所有的列
				int cells = 14;
				if (i == 0) {// 首行表头过滤
					continue;
				}
				Object[] arra = new Object[14];
				String checkStr = "校验正确";
				for (int j = 0; j < cells; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							arra[j] = cell.getCellFormula();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							arra[j] = Math.round(cell.getNumericCellValue())+"";
							break;
						case Cell.CELL_TYPE_STRING:
							arra[j] = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_BLANK:
							break;
						default:
							break;
						}
					}
					if (j < 12) {
						if (arra[j] == null || "".equals(arra[j])) {
							if(j==2){
								checkStr = "手机号不能为空";
							}

							if(j==6){
								checkStr = "车牌号不能为空";
							}
						}
					}
				}
				arra[cells - 1] = checkStr;
				result.add(arra);
			}

			row = null;
		}

		wb = null;
		sheet = null;
		return result;
	}

	public List<Map> queryMemberByCar(Integer carId){
		Map map = new HashMap();
		map.put("carId", carId);
		//查询会员车辆关系表
		List<Map> memberByCarId = memberMapper.queryMemberCarRel(map);

		List consIds = new ArrayList();
		if(memberByCarId != null && memberByCarId.size() > 0){
			for (Map result : memberByCarId) {
				consIds.add(result.get("consId"));
			}
		}

		Map consMap = new HashMap();
		consMap.put("consIds", consIds);
		List<Map> queryMemberById = memberMapper.queryMemberById(consMap);


		map = null;
		memberByCarId = null;
		consIds = null;
		consMap = null;

		return queryMemberById;
	}

	/*
	 * 把集团会员置为无效,把集团会员更改为个人会员
	 */
	public int dissGroupMember(List<Integer> consIds,Integer userId) throws BizException{

		if(consIds == null || consIds.size() < 0){
			//请选择需要置为无效的集团会员
			throw new BizException(1200003);
		}

		List list = new ArrayList();
		for (Integer consId : consIds) {
			Map map = new HashMap();
			map.put("consTypeCode", "01");
			map.put("groupId", -99);
			map.put("consId", consId);
//			map.put("consStatus",1);
			list.add(map);
		}

		int count = memberMapper.dissGroupMembers(list);

		saveLog("集团会员置为无效", OperateType.del.OperateId,"置为无效的集团会员id:"+list,userId);
		list = null;
		consIds = null;

		return count;
	}

	public Map queryMemberFlowInfo(String billPayNo) throws BizException{

		Map resultMap = new HashMap();
		if(billPayNo == null || "".equals(billPayNo)){
			throw new BizException(1200004);
		}
		List list = new ArrayList();
		list.add(billPayNo);
		List<Map> billPays = memberMapper.queryBillPay(list);

		if(billPays != null && billPays.size() > 0){
			Map map = billPays.get(0);
			DataVo vo = new DataVo(map);
			resultMap.put("billPayNo", map.get("billPayNo"));
			resultMap.put("amount", map.get("amount"));
			resultMap.put("startTime", map.get("startTime"));
			resultMap.put("endTime", map.get("endTime"));
			resultMap.put("elceFee", map.get("elceFee"));
			resultMap.put("servFee", map.get("servFee"));
			resultMap.put("chgPower", map.get("chgPower"));
			resultMap.put("payType", dictService.getDictLabel("shoukfs", map.get("payType")==null?"":map.get("payType").toString()));
			if (vo.isNotBlank("startTime")&& vo.isNotBlank("endTime")) {
				Calendar endTime = CalendarUtils.convertStrToCalendar(vo.getString("endTime"), CalendarUtils.yyyyMMddHHmmss);
				Calendar startTime = CalendarUtils.convertStrToCalendar(vo.getString("startTime"), CalendarUtils.yyyyMMddHHmmss);
				resultMap.put("chargeTime",CalendarUtils.compareTime(endTime, startTime, Calendar.MINUTE)+"分钟");
			}

			if(map.get("pileId") != null && !"".equals(map.get("pileId"))){
				Map pileStationInfo = pileMapper.queryPileStationInfo(map);

				resultMap.put("stationName", pileStationInfo.get("stationName"));
				resultMap.put("pileNo", pileStationInfo.get("pileNo"));
				String pileType = "";
				Object infoPileType = pileStationInfo.get("pileType");
				if (infoPileType != null){
					pileType = infoPileType.toString();
				}
				resultMap.put("pileType", dictService.getDictLabel("cdzlx", pileType));

				vo = new DataVo(map);
				List gunAll = gunMapper.getGunAll(vo);
				Object gumPoint = "";
				if(gunAll != null && gunAll.size() > 0){
					Map gunMap  = (Map) gunAll.get(0);
					gumPoint = gunMap.get("gumPoint");
				}

				resultMap.put("gumPoint", gumPoint);

				gunAll = null;
				pileStationInfo = null;
			}

			map = null;
			vo = null;
		}

		list = null;
		billPays = null;
		return resultMap;
	}
}

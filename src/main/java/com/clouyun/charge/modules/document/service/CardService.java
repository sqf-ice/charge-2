package com.clouyun.charge.modules.document.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clou.common.utils.JSONUtils;
import com.clou.system.dataservice.AuthService;
import com.clou.system.util.CommUtils;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.ReadXml;
import com.clouyun.charge.modules.document.mapper.CardMapper;
import com.clouyun.charge.modules.member.service.MemberService;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.clouyun.charge.modules.vehicle.mapper.VehicleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: CardService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年6月14日
 */
@Service
public class CardService extends BusinessService{
	@Autowired
	CardMapper cardMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DictService dictService;
	
	@Autowired
	VehicleMapper vehicleMapper;
	
	@Autowired
	MemberService memberService;
	/**
	 * 查询卡片列表
	 * @throws Exception 
	 */
	public PageInfo getCardAll(Map map) throws Exception{
		DataVo vo = new DataVo(map); 
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		if(vo.isNotBlank("endTime")){
			Calendar endCalendar = CalendarUtils.convertStrToCalendar(vo.getString("endTime"), CalendarUtils.yyyyMMdd);
			endCalendar.add(Calendar.DAY_OF_MONTH, 1);
			vo.put("endTime", CalendarUtils.formatCalendar(endCalendar, CalendarUtils.yyyyMMdd));
		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		//查询卡片列表信息
		List list = cardMapper.getCardAll(vo);
		return new PageInfo(list);
	}
	
	/**
	 * 查询卡片信息
	 * @throws Exception 
	 */
	public Map getCardById(String cardId,Map map) throws Exception{
		DataVo vo = new DataVo(map);
		vo.put("cardId", cardId);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		
		if(vo.isBlank("cardId")){
			throw new BizException(1102001, "卡号");
		}
		Map cardMap = cardMapper.getCardById(vo);
		if(cardMap != null && cardMap.get("orgId") != null){
			if(orgIds!=null && !orgIds.contains(Integer.valueOf(cardMap.get("orgId").toString()))){
				throw new BizException(1103001);
			}
		}else{
			throw new BizException(1103001);
		}
		return cardMap;
	} 
	
	/**
	 * 开卡
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveCardInfo(Map map) throws Exception{
		Map vehicleVo = new HashMap();
		Map consVo = new HashMap();
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		if(vo.isBlank("cardId")){
			throw new BizException(1102001, "卡号");
		}
		if(vo.isBlank("orgId")){
			throw new BizException(1102001,"运营商Id");
		}
		if(vo.isBlank("actvTime")){
			throw new BizException(1102001, "开卡日期");
		}else{
			if(!ValidateUtils.Date(vo.getString("actvTime"))){
				throw new BizException(1102006,"开卡日期");
			}
		}
		if(vo.isBlank("consPhone")){
			throw new BizException(1102001,"电话号码");
		}else if(!vo.getString("consPhone").matches("^[0-9]{11}$")){
			throw new BizException(1102006,"电话号码");
		}
		if(vo.isBlank("consName")){
			throw new BizException(1102001,"会员名称");
		}
		if(vo.isBlank("consTypeCode")){
			throw new BizException(1102001,"会员类型");
		}else if("02".equals(vo.getString("consTypeCode")) && vo.isBlank("groupName")){
			throw new BizException(1102001,"集团名称");
		}
		
		if(vo.isBlank("licensePlate")){
			throw new BizException(1102001,"车牌号");
		}
		if(vo.isBlank("isCheck")){
			throw new BizException(1102001,"卡是否绑定状态");
		}
		
		// 存在车辆Id则修改车辆信息,不存在则新增车辆信息
		vehicleVo.put("licensePlate", vo.getString("licensePlate"));
		vehicleVo.put("model", vo.getString("model"));
		vehicleVo.put("brand", vo.getString("brand"));
		if(vo.isNotBlank("vehicleId")){
			//存在,修改
			vehicleVo.put("vehicleId", vo.getInt("vehicleId"));
			//调用修改方法
			//vehicleMapper.updateVehicle(vehicleVo);
		}else{
			//不存在 新增
			vehicleVo.put("orgId", vo.getInt("orgId"));
			
			if("01".equals(vo.getString("consTypeCode"))){
				vehicleVo.put("belongsType", 1);
			}else{
				vehicleVo.put("belongsType", 2);
			}
			//调用新增方法
			vehicleMapper.insertVehicle(vehicleVo);
		}
		vo.put("vehicleId", Integer.valueOf(vehicleVo.get("vehicleId").toString()));
		
		// 存在会员Id则修改会员信息,不存在则添加会员信息
		consVo.put("carId", Integer.valueOf(vehicleVo.get("vehicleId").toString()));
		consVo.put("consPhone", vo.getString("consPhone"));
		consVo.put("consName", vo.getString("consName"));
		consVo.put("consTypeCode", vo.getString("consTypeCode"));
		consVo.put("appFrom",vo.getInt("orgId"));
		consVo.put("groupId", vo.getInt("groupId"));
		consVo.put("userId", vo.getInt("userId"));
		if(vo.isNotBlank("consId")){
			//修改
			consVo.put("consId", vo.getInt("consId"));
			memberService.updateMembers(consVo);
		}else{
			//新增
			consVo.put("consFrom", "07"); //新增会员默认来源为储值卡
			consVo = memberService.insertMembers(consVo);
		}
		vo.put("consId",Integer.valueOf(consVo.get("consId").toString()));
		//新增卡信息
		cardMapper.saveCardInfo(vo);
		saveLog("开卡", OperateType.card.OperateName, String.format("用户Id为[%s]的用户开卡!", vo.getInt("userId")), vo.getInt("userId"));
	}
	
	/**
	 * 充值
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void rechargeCard(Map map) throws Exception{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户");
		}
		if(vo.isBlank("orgId")){
			throw new BizException(1102001, "运营商");
		}
		if(vo.isBlank("cardId")){
			throw new BizException(1102001, "卡号");
		}
		if(vo.isBlank("amount")){
			throw new BizException(1102001, "充值金额");
		}
		if(vo.isBlank("verifyType")){
			throw new BizException(1102001, "付款方式");
		}
		if(vo.isBlank("stationId")){
			throw new BizException(1102001, "收款场站");
		}
		vo.put("seqId", UUID.randomUUID().toString().replace("-", ""));
		cardMapper.rechargeCard(vo);
		saveLog("充值", OperateType.recharge.OperateName, String.format("用户Id为[%s]的用户充值卡:%s元!", vo.getInt("userId"),vo.getInt("amount")), vo.getInt("userId"));
	}
	
	/**
	 * 导出
	 * @throws Exception 
	 */
	public void exportCard(Map map,HttpServletResponse response) throws Exception{
		List list = getCardAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		headList.add("开卡时间");
		headList.add("卡号");
		headList.add("会员名称");
		headList.add("会员类型");
		headList.add("集团名称");
		headList.add("电话号码");
		headList.add("车牌号码");
		headList.add("车型");
		
		titleList.add("actvTime");
		titleList.add("cardId");
		titleList.add("consName");
		titleList.add("consTypeCode");
		titleList.add("groupName");
		titleList.add("consPhone");
		titleList.add("licensePlate");
		titleList.add("model");
		
		for (int i = 0; i < list.size(); i++) {
			Map cardMap = (Map) list.get(i);
			if(cardMap.get("consTypeCode")!=null && !"".equals(cardMap.get("consTypeCode").toString())){
				cardMap.put("consTypeCode", dictService.getDictLabel("hylb", cardMap.get("consTypeCode").toString()));
			}
		}
		ExportUtils.exportExcel(list, response,headList,titleList,"卡片信息");
	}
	
	/**
	 * 解锁  保存流水表
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void unlockCard(Map map) throws Exception{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户");
		}
		if(vo.isBlank("orgId")){
			throw new BizException(1102001,"运营商");
		}
		if(vo.isBlank("cardId")){
			throw new BizException(1102001,"卡号");
		}
		if(vo.isBlank("cardMoney")){
			throw new BizException(1102001, "余额");
		}
		if(vo.isBlank("amount")){
			throw new BizException(1102001, "调济金额");
		}
		if(vo.isBlank("newMoney")){
			throw new BizException(1102001, "调济后金额");
		}
		cardMapper.unlockCard(vo);
	}
	
	/**
	 * 加载秘钥
	 * @throws Exception 
	 */
	public Map getPassMap(Map map) throws Exception {
		Map<String,String> passMap = new HashMap<String,String>();
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.ORG.dataType);
		//权限
		if(orgIds != null){ 
			vo.put("orgIds",orgIds);
		}
		List<DataVo> cardKeys = cardMapper.querCardKeys(vo);
		
		StringBuffer sb1 = null;
		StringBuffer sb2 = null;
		Integer orgId = null;
		Map<Integer,Map<Integer,String>> keys = new HashMap<>();
		for (DataVo dataVo : cardKeys) {
			orgId = dataVo.getInt("orgId");
			sb1 = new StringBuffer();
			sb2 = new StringBuffer();
			// String pass1 ,pass2 ,pass3 ,pass4 ,pass5, pass6 ,pass7 ,pass8 ,pass9,
			// pass10, pass11, pass12 ,pass13 ,pass14, pass15, pass16 ;
			
			//卡充值 6  2
			sb1.append(dataVo.getString("key6"));
			sb1.append(dataVo.getString("key2"));
			//开卡 1-9  A-D pin
			sb2.append(dataVo.getString("key1"));
			sb2.append(dataVo.getString("key2"));
			sb2.append(dataVo.getString("key3"));
			sb2.append(dataVo.getString("key4"));
			sb2.append(dataVo.getString("key5"));
			sb2.append(dataVo.getString("key6"));
			sb2.append(dataVo.getString("key7"));
			sb2.append(dataVo.getString("key8"));
			sb2.append(dataVo.getString("key9"));
			sb2.append(dataVo.getString("key10"));//A
			sb2.append(dataVo.getString("key11"));//B
			sb2.append(dataVo.getString("key12"));//C
			sb2.append(dataVo.getString("key13"));//D
			sb2.append(dataVo.getString("key14"));//Pin
			passMap.put(orgId+"_1", sb1.toString());
			passMap.put(orgId+"_2", sb2.toString());
			//调剂金额 A  7
			passMap.put(orgId+"_3", dataVo.getString("key10"));
			passMap.put(orgId+"_4", dataVo.getString("key7"));
		}
		return passMap;
	}
	
	/**
	 * 加载秘钥2
	 * @throws Exception 
	 */
	public Map getPassMap2(Map map) throws Exception {
		Map<String,String> passMap = new HashMap<String,String>();
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001,"用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.ORG.dataType);
		if(orgIds == null){ //系统管理员,全局管理员
			orgIds = ReadXml.getInstance().getOrgMap().keySet();
		}
		Map<Integer, String> mappass;
		for (Integer orgId : orgIds) {
			mappass = ReadXml.getInstance().getOrgMap().get(orgId);
			if(mappass==null){
				continue;
			}
			// String pass1 ,pass2 ,pass3 ,pass4 ,pass5, pass6 ,pass7 ,pass8 ,pass9,
			// pass10, pass11, pass12 ,pass13 ,pass14, pass15, pass16 ;
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			//卡充值 6  2
			sb1.append(mappass.get(6));
			sb1.append(mappass.get(2));
			//开卡 1-9  A-D pin
			sb2.append(mappass.get(1));
			sb2.append(mappass.get(2));
			sb2.append(mappass.get(3));
			sb2.append(mappass.get(4));
			sb2.append(mappass.get(5));
			sb2.append(mappass.get(6));
			sb2.append(mappass.get(7));
			sb2.append(mappass.get(8));
			sb2.append(mappass.get(9));
			sb2.append(mappass.get(10));//A
			sb2.append(mappass.get(11));//B
			sb2.append(mappass.get(12));//C
			sb2.append(mappass.get(13));//D
			sb2.append(mappass.get(14));//Pin
			passMap.put(orgId+"_1", sb1.toString());
			passMap.put(orgId+"_2", sb2.toString());
			//调剂金额 A  7
			passMap.put(orgId+"_3", mappass.get(10));
			passMap.put(orgId+"_4", mappass.get(7));
		}
		return passMap;
	}
	
	/**
	 * 编辑卡信息
	 * @throws Exception 
	 * 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateCardInfo(Map map) throws Exception{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户");
		}
		//仅修改车辆Id,开卡日期,是否绑定状态
		cardMapper.updateCardInfo(vo);
	}
	
	/**
	 * 根据会员电话和运营商关联会员信息
	 * @throws Exception 
	 */
	public Map queryConsInfo(Map map) throws Exception{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户");
		}
		if(vo.isBlank("appFrom")){
			throw new BizException(1102001,"运营商");
		}
		if(vo.isBlank("consPhone")){
			throw new BizException(1102001,"电话号码");
		}
		return cardMapper.queryConsInfo(vo);
	}
	/**
	 * 判断卡片是否存在
	 * @throws Exception 
	 */
	public void getCardIdIsExist(String cardId) throws Exception{
		int count = cardMapper.getCardIdIsExist(cardId) ;
		if(count > 0){
			throw new BizException(1103002);
		}
		cardMapper.getCardIdIsExist(cardId);
	}
	
}

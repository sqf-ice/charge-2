package com.clouyun.charge.modules.document.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.ConTractMapper;
import com.clouyun.charge.modules.document.mapper.GroupBillMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 描述: GroupBillService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月26日
 */
@Service
public class GroupBillService {

	
	@Autowired
	GroupBillMapper groupBillMapper;
	
	@Autowired
	ConTractMapper conTractMapper;
	
	@Autowired
	DictService dictService;
	
	@Autowired
	UserService userService;
	/**
	 * 查询集团账单
	 */
	public PageInfo getGroupBillAll(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
   		if(vo.isNotBlank("groupBillMonth")){
   			vo.put("groupBillMonth", vo.getString("groupBillMonth"));
   		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List list = groupBillMapper.getGroupBillAll(vo);
		return new PageInfo(list);
	}
	
	/**
	 * 根据集团账单id查询集团账单详情
	 * @throws BizException 
	 */
	public Map getGroupBillInfos(Map map) throws BizException{
		DecimalFormat df = new DecimalFormat("######0.00");   
		Map groupBillInfosMap = new LinkedHashMap();
		if(map.get("userId") == null){
			throw new BizException(1102001,"用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(Integer.valueOf(map.get("userId").toString()), RoleDataEnum.ORG.dataType);
		
		String consName = map.get("consName")!=null?map.get("consName").toString():null;
		String consPhone = map.get("consPhone")!=null?map.get("consPhone").toString():null;
		Integer pageNum = map.get("pageNum")!=null?Integer.valueOf(map.get("pageNum").toString()):null;
		Integer pageSize = map.get("pageSize")!=null?Integer.valueOf(map.get("pageSize").toString()):null;
		if(map.get("groupBillId") == null || "".equals(map.get("groupBillId"))){
			throw new BizException(1102001, "集团账单Id");
		}
		DataVo vo = new DataVo();
		vo.put("groupBillId", Integer.valueOf(map.get("groupBillId").toString()));

		//List conTractList = conTractMapper.getConTractStationRelaByTractId(vo);
		//查询集团账单信息
		Map groupBillMap = groupBillMapper.getGroupBillById(vo);
		List list = null;
		if(groupBillMap != null && groupBillMap.size() > 0){
			vo = new DataVo(groupBillMap);
			if(vo.getInt("contractType") == 1){
				DataVo conTractVo = new DataVo();
				conTractVo.put("tractId", vo.getInt("tractId"));
				List conTractList = conTractMapper.getConTractStationRelaByTractId(conTractVo);
				Set<Integer> stationIds = new HashSet<Integer>();
				for (int i = 0; i < conTractList.size(); i++) {
					Map tractMap = (Map) conTractList.get(i);
					if(tractMap.get("stationId")!=null && !"".equals(tractMap.get("stationId"))){
						stationIds.add(Integer.valueOf(tractMap.get("stationId").toString()));
					}
				}
				if(stationIds != null && stationIds.size() > 0){
					vo.put("stationIds",stationIds);
				}
			}
			if(orgIds !=null){
				vo.put("orgIds", orgIds);
			}
			if(consName != null){
				vo.put("consName", consName);
			}
			if(consPhone != null){
				vo.put("consPhone", consPhone);
			}
			if(vo.get("groupBillDate")!=null && !"".equals(vo.get("groupBillDate"))){
				String groupBillDate =vo.get("groupBillDate").toString();
				Calendar calendar = CalendarUtils.convertStrToCalendar(groupBillDate, CalendarUtils.yyyyMMdd);


				calendar.add(Calendar.MONTH, -1);
				vo.put("groupBillDateStart", CalendarUtils.formatCalendar(calendar, CalendarUtils.yyyyMMdd));
				calendar.add(Calendar.MONTH, 1);
				vo.put("groupBillDateEnd",  CalendarUtils.formatCalendar(calendar, CalendarUtils.yyyyMMdd));
			}

			DataVo groupBillInfosTotal = groupBillMapper.getGroupBillInfosTotal(vo);

			if(pageNum !=null && pageSize != null){
				vo.put("pageNum", pageNum);
				vo.put("pageSize", pageSize);
				PageHelper.startPage(vo);
			}
			//查询集团账单详情信息
			list = groupBillMapper.getGroupBillInfos(vo);

			/*BigDecimal totalElceFee = BigDecimal.ZERO;
			BigDecimal totalAmount = BigDecimal.ZERO;
			BigDecimal totalChgPower = BigDecimal.ZERO;
			BigDecimal totalParkFee = BigDecimal.ZERO;
			BigDecimal totalServFee = BigDecimal.ZERO;
			//数据统计
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					DataVo totalMap = new DataVo((Map) list.get(i));
					if (totalMap.isNotBlank("amount")){
						BigDecimal amount = new BigDecimal(totalMap.getString("amount"));
						totalAmount = totalAmount.add(amount);
						amount = null;
					}
					if (totalMap.isNotBlank("elceFee")){
						BigDecimal elceFee = new BigDecimal(totalMap.getString("elceFee"));
						totalElceFee = totalElceFee.add(elceFee);
						elceFee = null;
					}
					if (totalMap.isNotBlank("chgPower")){
						BigDecimal chgPower = new BigDecimal(totalMap.getString("chgPower"));
						totalChgPower = totalChgPower.add(chgPower);
						chgPower = null;
					}
					if (totalMap.isNotBlank("parkFee")){
						BigDecimal parkFee = new BigDecimal(totalMap.getString("parkFee"));
						totalParkFee = totalParkFee.add(parkFee);
						parkFee = null;
					}
					if (totalMap.isNotBlank("servFee")){
						BigDecimal servFee = new BigDecimal(totalMap.getString("servFee"));
						totalServFee = totalServFee.add(servFee);
						servFee = null;
					}
				}
			}
			Map totalMap = new HashMap();
			totalMap.put("amount", totalAmount);
			totalMap.put("elceFee", totalElceFee);
			totalMap.put("chgPower", totalChgPower);
			totalMap.put("parkFee", totalParkFee);
			totalMap.put("servFee", totalServFee);
			groupBillInfosMap.put("total", totalMap);*/
			groupBillInfosMap.put("total", groupBillInfosTotal);
		}
		groupBillInfosMap.put("groupBillInfos", new PageInfo(list));
		return groupBillInfosMap;
	}
	
	/**
	 * 导出集团账单列表
	 * @throws Exception 
	 */
	public void exportGroupBill(Map map,HttpServletResponse response) throws Exception{
		List list = getGroupBillAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		
		headList.add("出账月份");
		headList.add("合约名称");
		headList.add("集团名称");
		headList.add("集团编号");
		headList.add("出账金额");
		headList.add("出账日期");
		headList.add("缴费状态");
		headList.add("余额");
		
		titleList.add("groupBillMonth");
		titleList.add("contractName");
		titleList.add("groupName");
		titleList.add("groupNo");
		titleList.add("groupBillAmount");
		titleList.add("groupBillDate");
		titleList.add("groupBillStatus");
		titleList.add("curAmount");
		
		for (int i = 0; i < list.size(); i++) {
			Map groupBillMap = (Map) list.get(i);
			if(groupBillMap.get("groupBillStatus")!=null && !"".equals(groupBillMap.get("groupBillStatus").toString())){
				groupBillMap.put("groupBillStatus",dictService.getDictLabel("jfzt", groupBillMap.get("groupBillStatus").toString()));
			}else{
				groupBillMap.put("groupBillStatus","");
			}
		}
		
		ExportUtils.exportExcel(list, response, headList, titleList, "集团账单");
	}
	
	/**
	 * 导出集团账单详情列表数据
	 * @throws Exception 
	 */
	public void exportGroupBillInfo(Map map,HttpServletResponse response) throws Exception{
		Map groupMap = getGroupBillInfos(map);
		List list = ((PageInfo) groupMap.get("groupBillInfos")).getList();
		Map totalMap = (Map) groupMap.get("total");
		totalMap.put("createTime", "合计");
		list.add(totalMap);
		List headList = new ArrayList();
		List titleList =new ArrayList();
		
		headList.add("订单时间");
		headList.add("订单编号");
		headList.add("会员账号");
		headList.add("会员名");
		headList.add("充电量(kWh)");
		headList.add("电费(元)");
		headList.add("服务费(元)");
		headList.add("停车费(元)");
		headList.add("消费金额(元)");
		
		titleList.add("createTime");
		titleList.add("billPayNo");
		titleList.add("consPhone");
		titleList.add("consName");
		titleList.add("chgPower");
		titleList.add("elceFee");
		titleList.add("servFee");
		titleList.add("parkFee");
		titleList.add("amount");
		
		ExportUtils.exportExcel(list, response, headList, titleList, "集团账单详情列表");
	}
}

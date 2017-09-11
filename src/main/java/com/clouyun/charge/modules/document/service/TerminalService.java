package com.clouyun.charge.modules.document.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.TerminalMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: TerminalService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@Service
public class TerminalService {

	
	@Autowired
	TerminalMapper terminalMapper;
	
	@Autowired
	DictService dictService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MeterManagementService managementService;
	
	/**
	 * 查询终端列表
	 */
	public PageInfo getTerminalAll(Map map) throws BizException {
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		//设置分页
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List list = terminalMapper.getTerminalAll(vo);
		return new PageInfo(list);
	}
	/**
	 * 根据终端Id查询终端信息
	 */
	public Map getTerminalById(Integer terminalId) throws BizException {
		if(terminalId == null){
			throw new BizException(1102001, "终端Id");
		}
		DataVo vo = new DataVo();
		vo.add("terminalId", terminalId);
		Map map = terminalMapper.getTerminalById(vo);
		return map;
	}
	/**
	 * 根据终端Id删除终端信息
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delTerminal(List terminalIds) throws BizException{
		if(terminalIds == null || terminalIds.size()<=0){
			throw new BizException(1102001, "终端Id");
		}
		DataVo vo = new DataVo();
		vo.put("terminalIds", terminalIds);
		
		//查询是否有关联表计
		int count = managementService.getMeterByObjIds(vo);
		if(count>0){
			throw new BizException(1105003, "终端");
		}
		terminalMapper.delTerminal(vo);
	}
	
	/**
	 * 根据终端名称查询终端信息列表(业务字典)
	 * @throws BizException 
	 */
	public PageInfo getTerminal(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(orgIds !=null){
			vo.put("orgIds", orgIds);
		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(map);
		}
		List list = terminalMapper.getTerminal(vo);
		return new PageInfo(list);
	}
	
	private List<String> list = Arrays.asList(new String[]{
			"shelfLifeTime",
			"ratedPower"
	});
	
	/**
	 * 新增终端信息
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveTerminal(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(vo,list);
		getTerminalVaildate(vo,"save");
		terminalMapper.saveTerminal(paramMap);
	}
	
	/**
	 * 编辑终端信息
	 * @throws BizException 
	 */
	public void updateTerminal(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		Map paramMap = CommonUtils.convertDefaultVal(vo,list);
		getTerminalVaildate(vo,"update");
		terminalMapper.updateTerminal(paramMap);
	}
	
	/**
	 * 数据验证终端信息
	 * @throws BizException 
	 */
	private void getTerminalVaildate(DataVo vo, String type) throws BizException {
		
		if("update".equals(type)){
			if(vo.isBlank("terminalId")){
				throw new BizException(1102001, "终端Id");
			}
		}
		if(vo.isBlank("orgId")){
			throw new BizException(1102001, "运营商Id");
		}else if(vo.isBlank("stationId")){
			throw new BizException(1102001, "场站Id");
		}else if(vo.isBlank("terminalNo")){
			throw new BizException(1102001, "终端编号");
		}else if(vo.isBlank("terminalName")){
			throw new BizException(1102001, "终端名称");
		}else if(vo.isBlank("terminalIp")){
			throw new BizException(1102001, "终端IP地址");
		}else if(vo.isBlank("terminalCommType")){
			throw new BizException(1102001, "终端通信方式");
		}else if(vo.isBlank("terminalCommProtocol")){
			throw new BizException(1102001, "通讯协议");
		}else if(vo.isBlank("terminalPort")){
			throw new BizException(1102001, "通讯端口号");
		}else if(vo.isBlank("terminalProtocolStatus")){
			throw new BizException(1102001, "终端状态");
		}
		if(vo.isNotBlank("ratedPower") && !ValidateUtils.Number(vo.getString("ratedPower"))){
			throw new BizException(1000014,"额定功率");
		}
	}
	
	/**
	 * 导出终端信息
	 * @throws Exception 
	 */
	public void exportTerminal(Map map,HttpServletResponse response) throws Exception{
		List list = getTerminalAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		
		headList.add("运营商名称");
		headList.add("所属场站");
		headList.add("终端名称");
		headList.add("终端编号");
		headList.add("终端IP地址");
		headList.add("终端通信方式");
		headList.add("通信协议");
		headList.add("通信端口号");
		headList.add("终端状态");
		headList.add("保质日期");
		headList.add("额定功率");
		headList.add("创建时间");
		headList.add("更新时间");
		
		titleList.add("orgName");
		titleList.add("stationName");
		titleList.add("terminalName");
		titleList.add("terminalNo");
		titleList.add("terminalIp");
		titleList.add("terminalCommType");
		titleList.add("terminalCommProtocol");
		titleList.add("terminalPort");
		titleList.add("terminalProtocolStatus");
		titleList.add("shelfLifeTime");
		titleList.add("ratedPower");
		titleList.add("terminalCreatetime");
		titleList.add("terminalUpdatetime");
		
		for (int i = 0; i < list.size(); i++) {
			Map terminalMap = (Map) list.get(i);
			//终端通信方式 zdtxfs
			if(terminalMap.get("terminalCommType") !=null && !"".equals(terminalMap.get("terminalCommType").toString())){
				terminalMap.put("terminalCommType",dictService.getDictLabel("zdtxfs", terminalMap.get("terminalCommType").toString()));
			}else{
				terminalMap.put("terminalCommType","");
			}
			// 终端通信协议 zdtxxy 
			if(terminalMap.get("terminalCommProtocol") !=null && !"".equals(terminalMap.get("terminalCommProtocol").toString())){
				terminalMap.put("terminalCommProtocol",dictService.getDictLabel("zdtxxy", terminalMap.get("terminalCommProtocol").toString()));
			}else{
				terminalMap.put("terminalCommType","");
			}
			// 终端通信协议状态 zdzt 
			if(terminalMap.get("terminalProtocolStatus") !=null && !"".equals(terminalMap.get("terminalProtocolStatus").toString())){
				terminalMap.put("terminalProtocolStatus",dictService.getDictLabel("zdzt", terminalMap.get("terminalProtocolStatus").toString()));
			}else{
				terminalMap.put("terminalProtocolStatus","");
			}
		}
		
		ExportUtils.exportExcel(list, response,headList,titleList,"终端信息");
	}
}

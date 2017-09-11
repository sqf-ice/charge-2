package com.clouyun.charge.modules.document.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.CompanyMapper;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述: CompanyService
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月24日
 */
@Service
public class CompanyService {
	
	
	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	UserService userService;
	/**
	 * 查询合约企业列表数据
	 * @throws BizException 
	 */
	public PageInfo getCompanyAll(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		DataVo userVo = userService.findUserById(vo.getInt("userId"));
		if(userVo.isNotBlank("userType") && "02".equals(userVo.getString("userType"))){
			if(userVo.isNotBlank("companyId")){
				vo.put("companyId", userVo.getInt("companyId"));
			}
		}else{
			Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.ORG.dataType);
			if(orgIds !=null){
				vo.put("orgIds", orgIds);
			}
		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List list = companyMapper.getCompanyAll(vo);
		return new PageInfo(list);
	}
	
	/**
	 * 根据合约企业Id查询合约企业信息
	 * @throws BizException 
	 */
	public Map getCompanyById(Integer companyId) throws BizException{
		if(companyId == null){
			throw new BizException(1102001, "合约企业Id");
		}
		DataVo vo = new DataVo();
		vo.put("companyId", companyId);
		Map map = companyMapper.getCompanyById(vo);
		return map;
	}
	
	/**
	 * 查询合约企业列表(业务字典)
	 * @throws BizException 
	 */
	public PageInfo getCompany(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"),RoleDataEnum.ORG.dataType);
		if(orgIds != null){
			vo.put("orgIds", orgIds);
		}
		if(vo.isNotBlank("pageNum") && vo.isNotBlank("pageSize")){
			PageHelper.startPage(vo);
		}
		List list = companyMapper.getCompany(vo);
		return new PageInfo(list);
	}

	/**
	 * 查询合约企业列表(业务字典)
	 * @throws BizException
	 */
	public List<ComboxVo> getCompanyDict(Map data) throws BizException{
		DataVo params = new DataVo(data);
		if(params.isBlank("userId"))
			throw new BizException(1102001, "用户Id");
		if (params.isBlank("limit"))
			data.put("limit",20);
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"),RoleDataEnum.ORG.dataType);
		params.put("orgIds", orgIds);
		List<ComboxVo> list = companyMapper.getCompanyDict(data);
		return list;
	}
	
	/**
	 * 新增合约企业
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveCompany(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		getCompanyVaildate(vo, "save");
		//默认账户状态正常
		vo.put("cConAccountStatus", "01");
		//默认账户id为联系电话
		vo.put("cConAccountId", vo.getString("cConTel"));
		//默认账户密码 000000
		vo.put("cConPassword", "000000");
		//默认账户余额0
		vo.put("cConAmount", 0d);
		//默认合约企业总提现 0
		vo.put("cConIncome", 0d);
		//默认合约企业状态 有效0
		vo.put("cConStatus", 0);
		companyMapper.saveCompany(vo);
	}
	
	/**
	 * 编辑合约企业
	 * @throws BizException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateCompany(Map map) throws BizException{
		DataVo vo =new DataVo(map);
		getCompanyVaildate(vo, "update");
		companyMapper.updateCompany(vo);
	}
	
	/**
	 * 数据验证
	 * @throws BizException 
	 */
	private void getCompanyVaildate(DataVo vo,String type) throws BizException{
		List<String> list = Arrays.asList(new String[]{
				"cConAlipay","cConWechat","cConBankaccount","cConEmail","cConAddress"
		});
		Map paramMap = CommonUtils.convertDefaultVal(vo,list);
		DataVo checkVo = new DataVo();
		if("update".equals(type)){
			if(vo.isBlank("cConCompanyId")){
				throw new BizException(1102001, "合约企业Id");
			}
			checkVo.put("conCompanyId", vo.getInt("cConCompanyId"));
		}
		if(vo.isBlank("cConCompanyName")){
			throw new BizException(1102001, "合约企业名称");
		}else if(vo.isBlank("orgId")){
			throw new BizException(1102001, "运营商");
		}else if(vo.isBlank("cConTel")){
			throw new BizException(1102001, "联系电话");
		}
		checkVo.put("orgId", vo.getInt("orgId"));
		checkVo.put("conCompanyName", vo.getString("cConCompanyName"));
		int count =  companyMapper.checkCompany(paramMap);
		if(count > 0){
			throw new BizException(1102007, "合约企业");
		}
	}
	
	/**
	 * 导出合约企业
	 */
	public void exportCompany(Map map,HttpServletResponse response) throws Exception{
		List list = getCompanyAll(map).getList();
		List headList = new ArrayList();
		List titleList = new ArrayList();
		
		headList.add("合约企业名称");
		headList.add("创建时间");
		headList.add("更新时间");
		
		titleList.add("cConCompanyName");
		titleList.add("cConCreateDate");
		titleList.add("cConUpdateDate");
		ExportUtils.exportExcel(list, response,headList,titleList,"信息");
	}
}

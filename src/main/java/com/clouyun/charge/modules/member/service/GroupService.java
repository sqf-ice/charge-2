package com.clouyun.charge.modules.member.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.member.mapper.GroupMapper;
import com.clouyun.charge.modules.member.mapper.MemberMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述: 集团service
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月15日 上午10:31:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupService extends BusinessService{

	public static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	@Autowired
	GroupMapper groupMapper;
	@Autowired
	MemberMapper memberMapper;
	@Autowired
	CAcctService acctService;
	@Autowired
	DictService dictService;
	@Autowired
	UserService userService;
	
	public PageInfo queryGroups(Map map) throws BizException{
		DataVo params = new DataVo(map);

		getPermission(params);//获取权限

		// 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(params);
        }
		List<Map> result = queryGroupsResult(params);
		
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
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(userRoleDataById != null && userRoleDataById.size() > 0){
            params.put("orgIds", userRoleDataById);
        }
	}

	private List<Map> queryGroupsResult(DataVo map) {
		List<Map> result = groupMapper.queryGroups(map);
		//团体人数
		List<Integer> list  = new ArrayList<Integer>();
		if(result != null && result.size() > 0){
			for (Map map2 : result) {
				if(null != map2.get("groupId")){
					list.add(Integer.parseInt(map2.get("groupId").toString()));
				}
			}
		}

		Map<String,String> remarkMap = new HashMap();
		if(list != null && list.size() > 0){
			List<Map> remarkList = memberMapper.queryCountByGroupId(list);
			if(remarkList != null && remarkList.size() > 0){
				for (Map map2 : remarkList) {
					String count = "0";
					if(null != map2.get("count")){
						count = map2.get("count").toString();
					}
					if (map2.get("groupId") != null){
						remarkMap.put(map2.get("groupId").toString(), count);
					}
				}
			}
			
			if(result != null && result.size() > 0){
				for (Map map2 : result) {
					String remark = "0";
					if (map2.get("groupId") != null){
						String groupId = map2.get("groupId").toString();
						String count = remarkMap.get(groupId);
						if(null != count){
							remark = remarkMap.get(groupId).toString();
						}
					}

					map2.put("remark", remark);
				}
			}
			remarkList = null;
		}

		list = null;
		remarkMap = null;
		return result;
	}

	/**
	 * 根据集团id获取集团信息
	 * @param groupId
	 * @return
	 */
	public Map queryGroupByKey(Integer groupId) throws BizException{
		Map result = groupMapper.queryGroupByKey(groupId);
		return result;
	}
	
	/**
	 * 新增集团信息,关联新增账户信息
	 */
	public int insertGroup(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		checkData(vo);

		saveLogs("新增", OperateType.add.OperateId, vo);
		//集团默认是有效的
		vo.put("groupStatus", "0");
		vo.put("acctNo", vo.getString("attentionPhone"));
		acctService.insertCAcct(vo);
		int insertGroup = groupMapper.insertGroup(vo);

		map = null;
		vo = null;
		return insertGroup;
	}

	/**
	 * 更新集团信息
	 */
	public int updateGroup(Map map)throws BizException{
		//检查参数
		DataVo vo = new DataVo(map);
		checkData(vo);
		if(vo.isBlank("groupId")){
			throw new BizException(1201000,"集团id");
		}
		saveLogs("修改", OperateType.update.OperateId, vo);
		//更新账户
		acctService.updateCAcct(vo);
		//更新集团信息
		int count = groupMapper.updateGroup(vo);

		map = null;
		vo = null;
		//返回集团受影响行数
		return count;
	}
	
	/**
	 * 检查参数
	 */
	private void checkData(DataVo vo) throws BizException {

		int groupNameOnly = groupMapper.checkGroupNameOnly(vo);
		if(groupNameOnly > 0){
			throw new BizException(1201001);
		}
		
		if(vo.isBlank("groupName")){
			throw new BizException(1201000,"集团名称");
		}
		if(vo.isBlank("groupNo")){
			throw new BizException(1201000,"集团编号");
		}
		if(vo.isBlank("attentionName")){
			throw new BizException(1201000,"集团联系人名称");
		}
		if(vo.isBlank("appFrom")){
			throw new BizException(1201000,"运营商");
		}
		if(vo.isBlank("attentionPhone")){
			throw new BizException(1201000,"集团联系电话");
		}
		if (vo.isNotBlank("attentionEmail") && !ValidateUtils.Email(vo.getString("attentionEmail"))){
			throw new BizException(1000014, "邮箱");
		}
	}
	
	
	/*
	 * 集团导出
	 */
	public void export(Map map,HttpServletResponse response) throws Exception{
		DataVo params = new DataVo(map);
		getPermission(params);//获取权限
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(params);
        }
		//结果集
		List<Map> list = queryGroupsResult(params);

		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		//转义字典
		escapeResult(list);
		
		headList.add("集团名称");
		headList.add("集团编号");
		headList.add("付费方式");
		headList.add("账户余额(元)");
		headList.add("团体人数");
		headList.add("集团联系人");
		headList.add("集团联系人电话");
		headList.add("集团电子邮箱");
		headList.add("集团地址");
		headList.add("集团状态");
		
		valList.add("groupName");
		valList.add("groupNo");
		valList.add("payModel");
		valList.add("acctAmount");
		valList.add("remark");
		valList.add("attentionName");
		valList.add("attentionPhone");
		valList.add("attentionEmail");
		valList.add("groupAddr");
		valList.add("groupStatus");

		map = null;
		params = null;
		//list在工具类使用完毕之后清空
		ExportUtils.exportExcel(list, response,headList,valList,"集团信息");
	}
	
	/*
	 * 字典转义
	 */
	private void escapeResult(List<Map> list) throws BizException {
		//集团付费方式
    	List<ComboxVo> groupPayModelList = dictService.getDictByType("jthyzffs");
    	//状态
    	List<ComboxVo> groupStatusList = dictService.getDictByType("zt");

		if(list != null && list.size() > 0){
			for (Map result : list) {
				//集团付费方式
    			if(result.get("payModel") != null){
    				for (ComboxVo comboxVo : groupPayModelList) {
    					if(result.get("payModel").equals(comboxVo.getId())){
    						result.put("payModel", comboxVo.getText());
    					}
    				}
    			}
    			
    			//状态
    			if(result.get("groupStatus") != null){
    				for (ComboxVo comboxVo : groupStatusList) {
    					if(result.get("groupStatus").toString().equals(comboxVo.getId())){
    						result.put("groupStatus", comboxVo.getText());
    					}
    				}
    			}
			}
		}

		groupPayModelList = null;
		groupStatusList = null;
	}

	public List<Map> queryGroupDicts(Map map) throws BizException{
		DataVo params = new DataVo(map);
		
		getPermission(params);

		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(params);
		}
		List<Map> groupDicts = groupMapper.queryGroupDicts(params);

		map = null;
		params = null;

		return groupDicts;
	}

	private void saveLogs(String arr, String operateId, DataVo vo){
		saveLog(arr+"集团", operateId,"集团名称:"+vo.getString("groupName")+";电话号码:"+vo.getString("attentionPhone"),vo.getInt("userId"));
	}
}

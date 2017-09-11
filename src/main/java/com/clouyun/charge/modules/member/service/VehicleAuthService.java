package com.clouyun.charge.modules.member.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.helper.UserHelper;
import com.clouyun.charge.modules.member.mapper.VehicleAuthMapper;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: 会员常用车辆认证 
 * 版权: Copyright (c) 2017 
 * 公司: 科陆电子 
 * 作者: gaohui 
 * 版本: 2.0.0 
 * 创建日期:2017年6月15日
 */
@Service
@SuppressWarnings("rawtypes")
public class VehicleAuthService {

	@Autowired
	private UserService  userService;
	
	@Autowired
	private VehicleAuthMapper vehicleAuthMapper;

	/**
	 * 车辆认证列表 版本:2.0.0 作者:gaohui 日期:2017年6月15日
	 */
	@SuppressWarnings("unchecked")
	public PageInfo getVehicleAuths(Map map) throws BizException {
		PageInfo pageInfo = null;
		DataVo params = new DataVo(map);
		if (params.isBlank("userId")) {
			throw new BizException(1000006);
		}
		if (params.isNotBlank("startTime")) {
			map.put("startTime", params.getString("startTime") + "00:00:00");
		}
		if (params.isNotBlank("endTime")) {
			map.put("endTime", params.getString("startTime") + "23:59:59");
		}
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if (userRoleDataById != null && userRoleDataById.size() > 0) {
			map.put("orgIds", userRoleDataById);
		}
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(map);
		}
		List<Map> result = vehicleAuthMapper.getVehicleAuths(map);

		if (null != result && result.size() > 0) {
			pageInfo = new PageInfo(result);
		}
		return pageInfo;
	}

	/**
	 * 会员车辆认证 
	 * 版本:2.0.0 
	 * 作者:gaohui 
	 * 日期:2017年6月15日
	 */
	@SuppressWarnings("unchecked")
	public void authVehicle(Map map) throws BizException{
		DataVo params = new DataVo(map);
		if (params.isBlank("userId")) {
			throw new BizException(1000006);
		}
		if (params.isBlank("vehicleId")) {
			throw new BizException(1000012,"车辆id");
		}
		if (params.isBlank("authStatus")) {
			throw new BizException(1000012,"认证状态");
		}
		map.put("authStatus", params.getInt("authStatus"));
		vehicleAuthMapper.authVehicle(map);
	}

}

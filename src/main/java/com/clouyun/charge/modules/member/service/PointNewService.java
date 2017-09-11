package com.clouyun.charge.modules.member.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.modules.member.mapper.PointNewMapper;
import com.clouyun.charge.modules.system.mapper.PubOrgMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月8日 下午5:36:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PointNewService extends BusinessService{

	public static final Logger logger = LoggerFactory.getLogger(PointNewService.class);

	@Autowired
	PointNewMapper pointMapper;
	@Autowired
	UserService userService;
	@Autowired
	DictService dictService;
	@Autowired
	PubOrgMapper orgMapper;
	
	public Map<Integer,Map> queryPoints(Map map) throws BizException{
		DataVo params = new DataVo(map);
		// 此处应根据登录用户ID获取到能查看的企业，未实现
        if (params.isBlank("userId")){
        	throw new BizException(1000006);
        }
        if (params.isBlank("orgId")){
        	//throw new BizException(1202002);
            Integer orgIdByUserId = userService.getOrgIdByUserId(params.getInt("userId"));
            if(orgIdByUserId > 0){
				params.put("orgId", orgIdByUserId);
            }
        }
        // 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
    	Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
    	if(userRoleDataById != null && userRoleDataById.size() > 0){
			params.put("orgIds", userRoleDataById);
    	}
		
		List<Map> points = pointMapper.queryPoints(params);

    	List<Map> result = new ArrayList<>();
    	if (points != null && points.size() > 0){
			Iterator<Map> iterator = points.iterator();
			while (iterator.hasNext()){
				Map next = iterator.next();
				String pointType = next.get("pointType").toString();
				if ("1".equals(pointType) || "2".equals(pointType)){
					result.add(next);
				}
			}
		}else{
			int arr = 2;
			for (int i = 0; i < arr; i++) {
				Map zeroMap = new HashMap();
				zeroMap.put("timetag","");
				zeroMap.put("orgName","");
				zeroMap.put("pointId","");
				zeroMap.put("exchangeMoney",0);
				zeroMap.put("gainPoint",0);
				zeroMap.put("pointType",i + 1);
				zeroMap.put("userName","");
				zeroMap.put("userId",params.getInt("userId"));
				zeroMap.put("orgId",params.getInt("orgId"));
				result.add(zeroMap);
				zeroMap = null;
			}
		}


		Map<Integer,Map> resultMap = new HashMap<>();

    	if(result != null && result.size() > 0){
			for (Map r:result) {
				DataVo vo = new DataVo(r);
				if (vo.isNotBlank("pointType")){
					resultMap.put(vo.getInt("pointType"),r);
				}
			}
		}

		for (int i = 1; i <= 2 ; i++) {
			putDefaultVal(resultMap, i, params.getInt("userId"), params.getInt("orgId"));
		}

		params = null;
		userRoleDataById = null;
		points = null;
		return resultMap;
	}

	private void putDefaultVal(Map<Integer, Map> resultMap, int key, int userId2, int orgId2) {
		if (!resultMap.containsKey(key)) {
			Map zeroMap = new HashMap();
			zeroMap.put("timetag", "");
			zeroMap.put("orgName", "");
			zeroMap.put("pointId", "");
			zeroMap.put("exchangeMoney", 0);
			zeroMap.put("gainPoint", 0);
			zeroMap.put("pointType", key);
			zeroMap.put("userName", "");
			zeroMap.put("userId", userId2);
			zeroMap.put("orgId", orgId2);
			resultMap.put(key, zeroMap);
		}
	}

	public Set<Map> queryPointsTree(Map map) throws BizException{
		DataVo params = new DataVo(map);
		getPermission(params);

		List<Map> points = pointMapper.queryPoints(params);
		Map<String,String> fuMap = new LinkedHashMap();
		Set<Map> pointResult = new LinkedHashSet<>();
		if(points != null && points.size() > 0){
			for (Map map2 : points) {
				if(map2.get("orgId") != null && map2.get("orgName") != null){
					fuMap = new LinkedHashMap<>();
					fuMap.put("orgId", map2.get("orgId").toString());
					fuMap.put("orgName", map2.get("orgName").toString());
				}
				pointResult.add(fuMap);
			}
		}else{
			Integer orgId = userService.getOrgIdByUserId(params.getInt("userId"));
			DataVo vo = orgMapper.getOrgById(orgId);
			String orgIdArr = "";
			Map<String,String> zeroMap = new LinkedHashMap<>();
			if (orgId != null){
				orgIdArr = orgId.toString();
			}
			zeroMap.put("orgId",orgIdArr);
			zeroMap.put("orgName",vo.getString("orgName"));
			pointResult.add(zeroMap);

			vo = null;
		}

		map = null;
		params = null;
		points = null;

		return pointResult;
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


	public PageInfo queryPointsHistoryInfo(Map map) throws BizException{
		DataVo params = new DataVo(map);
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
        
        if(null == map.get("consId")){
        	throw new BizException(1202000,"会员id");
        }
        List<Map> result = pointMapper.queryPointsHistory(map);
        
        PageInfo page = new PageInfo(result);

        map = null;
        params = null;
        result = null;

		return page;
	}
	
	public int updatePoints(Map map) throws BizException{
		DataVo params = new DataVo(map);
		// 此处应根据登录用户ID获取到能查看的企业，未实现
        if (params.isBlank("userId")){
        	throw new BizException(1000006);
        }
		if(params.isNotBlank("gainPoint") && !ValidateUtils.Number(params.getString("gainPoint"))){
			throw  new BizException(1000014,"积分");
		}

		map.put("orgId", userService.getOrgIdByUserId(params.getInt("userId")));
		map.put("userId", params.getInt("userId"));
		int count = 0;
		if(params.isBlank("pointId")){
			//新增
			count = pointMapper.insertPoints(map);
			saveLogs("新增", OperateType.add.OperateId,new DataVo(map));
		}else{
			//更新
			count = pointMapper.updatePoints(map);
			saveLogs("编辑", OperateType.update.OperateId,new DataVo(map));
		}

		map = null;
		params = null;

		return count;
	}

	private void saveLogs(String arr, String operateId, DataVo vo) throws BizException {
		saveLog(arr+"积分", operateId,"运营商id"+vo.getInt("orgId")+";积分类型:"+dictService.getDictLabel("jfgz",vo.getString("pointType")),vo.getInt("userId"));
	}
}

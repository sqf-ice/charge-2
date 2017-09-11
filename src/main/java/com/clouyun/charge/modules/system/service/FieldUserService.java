package com.clouyun.charge.modules.system.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.domain.ui.Tree;
import com.clouyun.boot.common.domain.ui.TreeNode;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.system.mapper.FieldUserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;

/**
 * 描述: 外勤用户管理 版权: Copyright (c) 2017 公司: 科陆电子 作者: gaohui 版本: 1.0 创建日期: 2017年3月6日
 */
@Service
@SuppressWarnings("rawtypes")
public class FieldUserService {
	@Autowired
	private FieldUserMapper fieldUserMapper;

	@Autowired
	private DictService dictService;
	
	@Autowired
	private UserService userService;
	/**
	 * 分页查询外勤用户
	 * @param map
	 * @return 
	 * 2017年5月2日 
	 * gaohui
	 * 2.0.0
	 */
	@SuppressWarnings("unchecked")
	public PageInfo getFieldUsersPage(Map map)throws BizException{
		PageInfo pageInfo = null;
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1001003,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		if(params.isBlank("sort")){
			params.put("sort", "this_.wq_cons_id");
			params.put("order","DESC");
		}
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(map);
		}
		List<Map> list = fieldUserMapper.get(params);
		if(null!=list && list.size()>0){
			pageInfo = new PageInfo(list);
		}
		return pageInfo;
	}
	/**
	 * 查询外勤详情
	 * @param map
	 * @return 
	 * 2017年5月2日 
	 * gaohui
	 * 2.0.0
	 */
	public Map getFieldUser(Integer wqConsId){
		return fieldUserMapper.getById(wqConsId);
	}
	/**
	 * 删除外勤用户
	 * @param map
	 * @return 
	 * 2017年5月2日 
	 * gaohui
	 * 2.0.0
	 */
	public void removeFieldUser(List wqConsIds){
		DataVo params = new DataVo();
		if(null!=wqConsIds){
			params.put("wqConsIds", wqConsIds);
		}
		fieldUserMapper.deleteFieldUser(params);
		params.add("wqConsstationConsIds",wqConsIds);
		fieldUserMapper.deleteFieldStationOrgRel(params);
	}
	/**
	 * 新增外勤用户
	 * @param map
	 * @return 
	 * 2017年5月2日 
	 * gaohui
	 * 2.0.0
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	public void insertFieldUser(Map map)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("wqConsOrgId")){
			throw new BizException(1001003,"外勤用户所属企业");
		}
		if(params.isBlank("wqConsName")){
			throw new BizException(1001003,"外勤用户真实姓名");
		}
		if(params.isBlank("wqConsTel")){
			throw new BizException(1001003,"外勤用户电话");
		}
		if(params.isBlank("wqConsStatus")){
			throw new BizException(1001003,"外勤用户状态");
		}
		List<Map> telList = fieldUserMapper.queryFieldUserExist(params);
		if(null!=telList && telList.size()>0){
			throw new BizException(1001005);
		}
		fieldUserMapper.addFieldUser(params);
		List<DataVo> resultList = null;
		if (params.isNotBlank("orgStationRel")) {
			List<Map> list = params.getList("orgStationRel");
			if(null!=list && list.size()>0){
				resultList = new ArrayList<DataVo>();
				for(Map vo:list){
					if(null!=vo && vo.size()>0){
						DataVo dataVo = new DataVo(vo);
						if(dataVo.isNotBlank("children")){
							List<Map> statList = dataVo.getList("children");
							if(null!=statList && statList.size()>0){
								for(Map stat : statList){
									DataVo statVo = new DataVo(stat);
									DataVo tempVo = new DataVo();
									tempVo.put("wqConsstationConsId",params.getInt("wqConsId"));
									tempVo.put("wqConsstationOrgId", statVo.getInt("pid"));
									tempVo.put("wqConsstationStationId", statVo.getInt("id"));
									resultList.add(tempVo);
								}
							}else{
								DataVo tempVo = new DataVo();
								tempVo.put("wqConsstationConsId",params.getInt("wqConsId"));
								tempVo.put("wqConsstationOrgId", dataVo.getInt("id"));
								resultList.add(tempVo);
							}
						}else{
							DataVo tempVo = new DataVo();
							tempVo.put("wqConsstationConsId",params.getInt("wqConsId"));
							tempVo.put("wqConsstationOrgId", dataVo.getInt("id"));
							resultList.add(tempVo);
						}
				    }
				}
			}
		}
		if(null!= resultList && resultList.size()>0){
        	for(Map data:resultList){
        		fieldUserMapper.addFieldStationOrgRel(data);
        	}
        }
		
	}
	/**
	 * 编辑外勤用户
	 * @param map
	 * @return 
	 * 2017年5月2日 
	 * gaohui
	 * 2.0.0
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	public void updateFieldUser(Map map)throws BizException{
		DataVo params = new DataVo(map);
		if(params.isBlank("wqConsId")){
			throw new BizException(1001003,"外勤用户id");
		}
		List<Map> telList = fieldUserMapper.queryFieldUserExist(params);
		if(null!=telList && telList.size()>0){
			throw new BizException(1001005);
		}
		Set<Integer> wqConsstationConsIds = new HashSet<Integer>();
		wqConsstationConsIds.add(params.getInt("wqConsId"));
		fieldUserMapper.modifyFieldUser(params);
		params.put("wqConsstationConsIds", wqConsstationConsIds);
		fieldUserMapper.deleteFieldStationOrgRel(params);
		List<DataVo> resultList = null;
		if (params.isNotBlank("orgStationRel")) {
			List<Map> list = params.getList("orgStationRel");
			if(null!=list && list.size()>0){
				resultList = new ArrayList<DataVo>();
				for(Map vo:list){
					if(null!=vo && vo.size()>0){
						DataVo dataVo = new DataVo(vo);
						if(dataVo.isNotBlank("children")){
							List<Map> statList = dataVo.getList("children");
							if(null!=statList && statList.size()>0){
								for(Map stat : statList){
									DataVo statVo = new DataVo(stat);
									DataVo tempVo = new DataVo();
									tempVo.put("wqConsstationConsId",params.getInt("wqConsId"));
									tempVo.put("wqConsstationOrgId", statVo.getInt("pid"));
									tempVo.put("wqConsstationStationId", statVo.getInt("id"));
									resultList.add(tempVo);
								}
							}else{
								DataVo tempVo = new DataVo();
								tempVo.put("wqConsstationConsId",params.getInt("wqConsId"));
								tempVo.put("wqConsstationOrgId", dataVo.getInt("id"));
								resultList.add(tempVo);
							}
						}else{
							DataVo tempVo = new DataVo();
							tempVo.put("wqConsstationConsId",params.getInt("wqConsId"));
							tempVo.put("wqConsstationOrgId", dataVo.getInt("id"));
							resultList.add(tempVo);
						}
					}
				}
			}
		}
        if(null!=resultList && resultList.size()>0){
        	for(DataVo vo:resultList){
        		fieldUserMapper.addFieldStationOrgRel(vo);
        	}
        }
	}
	/**
	 * 导出外勤用户
	 * @param map
	 * @return 
	 * 2017年5月2日 
	 * gaohui
	 * 2.0.0
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void exportFieldUsers(Map map,HttpServletResponse response) throws Exception{
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1001003,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		if(params.isBlank("sort")){
			params.put("sort", "this_.wq_cons_id");
			params.put("order","DESC");
		}
		String header = "外勤用户列表";
		List<Map> list = fieldUserMapper.get(params);
		if(null!=list&&list.size()>0){
			List<ComboxVo> wqConsStatuss = dictService.getDictByType("wqzt");
			if(null==wqConsStatuss||wqConsStatuss.size()<1){
				throw new BizException(1001003,"外勤用户状态字典");
			}
			for(Map fUser:list){
				DataVo fUserVo = new DataVo(fUser); 
				for(ComboxVo comboxVo :wqConsStatuss){
					if(fUserVo.getString("wqConsStatus").equals(comboxVo.getId())){
						fUser.put("wqConsStatus", comboxVo.getText());
						continue;
					}
				}
			}
		}
		List<String> headers = new ArrayList<String>();
		headers.add("所属企业");
		headers.add("真实姓名");
		headers.add("手机号码");
		headers.add("用户状态");
		headers.add("部门");
		headers.add("住址");
		headers.add("备注");
		List<String> rows = new ArrayList<String>();
		rows.add("orgName");
		rows.add("wqConsName");
		rows.add("wqConsTel");
		rows.add("wqConsStatus");
		rows.add("wqDepartment");
		rows.add("wqConsAddr");
		rows.add("wqConsRemark");
		ExportUtils.exportExcel(list, response, headers, rows, header);
	}
	
	
	@SuppressWarnings({"unchecked"})
	public List<ComboxVo> getFieldUserDicts(Map map)throws BizException{
		List<ComboxVo> dicts = null;
		DataVo params= new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1001003,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		if(params.isNotBlank("stationId")){
		   params.put("wqConsstationStationId", params.getInt("stationId"));
		}
		List<Map> fusers = fieldUserMapper.get(params);
		if(null!=fusers && fusers.size()>0){
			dicts = new ArrayList<ComboxVo>();
			for(Map fuser:fusers){
				DataVo fuserVo = new DataVo(fuser);
				ComboxVo comboxVo = new ComboxVo(fuserVo.getString("wqConsId"), fuserVo.getString("wqConsName"));
				dicts.add(comboxVo);
			}
		}
		return dicts; 
	}
	/**
	 * 获取用户已拥有的管理场站
	 * @param data
	 * @return 
	 * 2017年3月6日 
	 * gaohui
	 * 2.0.0
	 */
	@SuppressWarnings("unchecked")
	public List<TreeNode> getFieldStationOrgRel(Integer wqConsId) {
		List<TreeNode>  orgs = fieldUserMapper.getFieldOrgsByConsId(wqConsId);
		List<TreeNode>  stations = fieldUserMapper.getFieldStationsByConsId(wqConsId);
        Set<Integer> tempOrgIds = Sets.newHashSet();
        CollectionUtils.collect(orgs, new Transformer() {
            @Override
            public Object transform(Object o) {
                return ((TreeNode) o).getId();
            }
        }, tempOrgIds);
        // 过滤掉不存在运营商权限的场站
        if(null!=stations && stations.size()>0){
        	for (TreeNode station : stations) {
        		if (tempOrgIds.contains(station.getPid())) {
        			orgs.add(station);
        		}
        	}
        }
		return new Tree().list2(orgs);
	}
	
	
	/**
	 * 新增外勤用户
	 * 
	 * @param data
	 * @return 2017年3月6日 gaohui
	 */
	@Transactional
	public void addFieldUser(DataVo data) {
		fieldUserMapper.addFieldUser(data);
		List<DataVo> list = null;
		if (data.isNotBlank("stationIds")) {
			list = convertData(data.getString("stationIds"),data.getInt("wqConsId"));
		}
		if(null!=list && list.size()>0){
        	for(DataVo vo:list){
        		fieldUserMapper.addFieldStationOrgRel(vo);
        	}
        }
	}

	/**
	 * 查询所用的外勤人员
	 * 
	 * @param data
	 * @return 2017年3月6日 gaohui
	 */
	public ResultVo queryAllFieldUser(DataVo data) {
		ResultVo resultVo = new ResultVo();
		List<Map> all = fieldUserMapper.queryAllFieldUser(data);
		resultVo.setData(all);
		resultVo.setTotal(all.size());
		return resultVo;
	}
	/**
	 * 根据电话号码和企业ID判断用户是否存在
	 * @param data
	 * @return
	 * 2017年3月17日
	 * gaohui
	 */
	public List<Map> queryFieldUserExist(DataVo data)throws BizException {
		return  fieldUserMapper.queryFieldUserExist(data);
	}
	
	/**
	 * 编辑外勤用户信息
	 * @param data
	 * 2017年3月6日 
	 * gaohui
	 */
	@Transactional
	public void modifyFieldUser(DataVo data) {
		fieldUserMapper.modifyFieldUser(data);
		fieldUserMapper.deleteFieldStationOrgRel(data);
		List<DataVo> list = null;
		if (data.isNotBlank("stationIds")) {
			list = convertData(data.getString("stationIds"),data.getInt("wqConsId"));
		}
        if(null!=list && list.size()>0){
        	for(DataVo vo:list){
        		fieldUserMapper.addFieldStationOrgRel(vo);
        	}
        }
	}

	/**
	 * 根据Id删除外勤用户
	 * 
	 * @param id
	 * 2017年3月7日 gaohui
	 */
	@Transactional
	public void deleteFieldUser(DataVo data) {
		fieldUserMapper.deleteFieldUser(data);
		data.add("wqConsstationConsIds", data.get("wqConsIds"));
		fieldUserMapper.deleteFieldStationOrgRel(data);
	}

	/**
	 * 根据外勤用户Id查询外勤用户信息
	 * 
	 * @param data
	 * @return 2017年3月8日 gaohui
	 */
	public Map queryFieldUserById(DataVo data) {
		return fieldUserMapper.queryFieldUserById(data);
	}

	/**
	 * 添加外勤用户和场站、运营商的关联关系
	 * @param data
	 * 2017年3月8日 
	 * gaohui
	 */
	public int addFieldStationOrgRel(DataVo data) {
		return fieldUserMapper.addFieldStationOrgRel(data);
	}

	/**
	 * 删除外勤用户和场站、运营商的关联关系
	 * @param data
	 * 2017年3月8日
	 *  gaohui
	 */
	public int deleteFieldStationOrgRel(DataVo data) {
		return fieldUserMapper.deleteFieldStationOrgRel(data);
	}

	/**
	 * 根据外勤用户id查询外勤用户和场站、运营商的关联关系
	 * 
	 * @param data
	 * @return 
	 * 2017年3月8日 
	 * gaohui
	 */
	public ResultVo queryFieldStationOrgRel(DataVo data) {
		ResultVo resultVo = new ResultVo();
		List<Map> list = fieldUserMapper.queryFieldStationOrgRel(data);
		resultVo.setData(list);
		return resultVo;
	}
	/**
	 * 去除重复的场站id
	 * @param data
	 * @return
	 * 2017年3月17日
	 * gaohui
	 */
	public List<DataVo> convertData(String stationIds,Integer wqConsId) {
		Set<String>   set = null;
		List<DataVo> list = null;
		String[] idsArrs = stationIds.split(",");
		if (idsArrs.length > 0) {
			set = new HashSet<String>();
			for (String idsArr : idsArrs) {
				set.add(idsArr);
			}
		}
		if (set != null && set.size() > 0) {
			list = new ArrayList<DataVo>();
			for (String arr : set) {
				if (!arr.trim().equals("")) {
					String[] arrs = arr.split("_");
					String orgId = arrs[0];
					if (arrs.length > 1) {
						for (int i = 1; i < arrs.length; i++) {
							if (!"".equals(arrs[i]) && null!=arrs[i]) {
								DataVo dataVo = new DataVo();
								dataVo.add("wqConsstationConsId",wqConsId);
								dataVo.add("wqConsstationOrgId", orgId);
								dataVo.add("wqConsstationStationId", arrs[i]);
								list.add(dataVo);
							}
						}
					}else{
						DataVo dataVo = new DataVo();
						dataVo.add("wqConsstationConsId",wqConsId);
						dataVo.add("wqConsstationOrgId", orgId);
						list.add(dataVo);
					}
				}
			}
		}
		return list;
	}
	/**
	 * 根据外勤用户的Ids查询用户
	 * @param map
	 * @return
	 * @throws BizException
	 * 2017年4月11日
	 * gaohui
	 */
	public List<Map> queryFieldUserByIds(Map map)throws BizException{
		return fieldUserMapper.queryFieldUserByIds(map);
	}
}

package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.domain.ui.TreeNode;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.system.mapper.RoleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 描述: 角色管理服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年04月24日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService extends BusinessService {

    private static Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DictService dictService;

    /**
     * 查询角色列表（含分页、排序、条件搜索参数）
     *
     * @param data
     * @return
     */
    public PageInfo queryRoles(Map data) throws BizException {
        DataVo params = new DataVo(data);
        // data参数中userId不能为空，为空的话获取所有的，不应该有这个权限
        if (params.isBlank("userId"))
            throw new BizException(1000006);
        data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"),RoleDataEnum.ORG.dataType));
        List<Integer> roleTypes = userService.getRoleTypeByUserId(params.getInt("userId"));
        // 这块限制很大，字典刚好是等级越高值越低，只能看同级及以下(业务要求...)
        if (CollectionUtils.isNotEmpty(roleTypes)){
			data.put("roleType", Integer.toString(Collections.min(roleTypes)));
        }
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(data);
        }
        List roles = roleMapper.getRolesByPage(data);
        PageInfo page = new PageInfo(roles);
        return page;
    }

    /**
     * 查找角色信息
     *
     * @param roleId
     * @return
     */
    public DataVo findRoleById(Integer roleId) throws BizException {
        DataVo role = roleMapper.getRoleById(roleId);
        if(role==null||role.isEmpty())
            throw new BizException(1000015,"当前角色");
        return role;
    }

    /**
     * 根据角色ID获取所有权限
     *
     * @param roleId
     * @return
     */
    public List<TreeNode> findPermsByRoleId(Integer roleId) throws BizException {
        return roleMapper.getPermsByRoleId(roleId);
    }
    /**
     * 根据角色ID获取所有权限ID集合
     *
     * @param roleId
     * @return
     */
    public List<Integer> findPermIdsByRoleId(Integer roleId) throws BizException {
        return roleMapper.getPermIdsByRoleId(roleId);
    }

    /**
     * 新增角色同时新增菜单权限
     *
     * @param data
     * @throws BizException
     */
    public void insertRolePerms(Map data) throws BizException {
        DataVo params1 = new DataVo(data);
        String roleType = params1.getString("roleType");
        int createBy = params1.getInt("createBy");
        CommonUtils.idIsEmpty(createBy, "创建者");
        CommonUtils.valIsEmpty(params1.getString("name"), "角色名称");
        CommonUtils.valIsEmpty(roleType, "管理员等级");
        // 菜单权限ID集合
        String perm = params1.getString("permIds");
        if (StringUtils.isBlank(perm))
            throw new BizException(1003002);
        roleNameExist(params1.getId(), "name", params1.getString("name"), "角色名称");
        List<Integer> roleTypes = userService.getRoleTypeByUserId(params1.getInt("createBy"));
        if (CollectionUtils.isNotEmpty(roleTypes) && Integer.valueOf(roleType) < (Collections.min(roleTypes))) {
            throw new BizException(1003003);
        }
        Integer orgId = userService.getOrgIdByUserId(createBy);
        data.put("orgId", orgId);
        roleMapper.insertRole(data);
        DataVo params = new DataVo(data);

        Integer roleId = params.getId();
        if (StringUtils.isNotBlank(perm)) {
            List permIds = Arrays.asList(perm.split(","));
            if (CollectionUtils.isNotEmpty(permIds)) {
                // 类型在数据库自动转换
                int addCount = roleMapper.insertPermsByRoleId(roleId, permIds);
                logger.info("新增角色保存成功，角色ID[{}]下新增[{}]条菜单权限.", roleId, addCount);
            }

        }
    }

    /**
     *
     * @param roleId
     * @param chkFileName
     * @param chkValue
     * @param nullMessage
     * @throws BizException
     */
    private void roleNameExist(Integer roleId, String chkFileName, String chkValue, String nullMessage) throws BizException {
        Integer count = roleMapper.getRoleCountByName(roleId, chkFileName, chkValue);
        if (count > 0)
            throw new BizException(1000011, nullMessage);

    }

    /**
     * 更新角色信息同时更新菜单权限
     *
     * @param data
     * @throws BizException
     * @description 执行多查一遍判断少量删除新增而不是统一全部删除，再全部新增
     */
    public void updateRolePerms(Map data) throws BizException {
        DataVo params = new DataVo(data);
        CommonUtils.idIsEmpty(params.getId(), "更新数据主键ID");
        // -1为系统管理员角色ID，不允许修改
        if (params.getId() == -1)
            throw new BizException(1003004, "系统管理员角色");
        String roleType = params.getString("roleType");
        CommonUtils.valIsEmpty(params.getString("name"), "角色名称");
        CommonUtils.idIsEmpty(params.getInt("updateBy"), "更新者");
        CommonUtils.valIsEmpty(roleType, "管理员等级");
        String perm = params.getString("permIds");
        if (StringUtils.isBlank(perm))
            throw new BizException(1003002);
        roleNameExist(params.getId(), "name", params.getString("name"), "角色名称");
        List<Integer> roleTypes = userService.getRoleTypeByUserId(params.getInt("updateBy"));
		if (CollectionUtils.isNotEmpty(roleTypes) && Integer.valueOf(roleType) < (Collections.min(roleTypes))) {
			throw new BizException(1003003);
		}
        roleMapper.updateRoleById(data);
        // 更新角色菜单权限
        Integer roleId = params.getId();
        // 菜单权限ID集合
        this.updateRolePerms(roleId, perm);
    }


    /**
     * 更新角色对应权限
     *
     * @param roleId 角色ID
     * @param perm   权限ID字符串
     * @description 方法公有化使事物生效
     */
    public void updateRolePerms(Integer roleId, String perm) throws BizException {
        // 当权限集合都为空，这说明当前角色没有
        if (StringUtils.isBlank(perm)) {
            //删除所有角色关联
            int deleteCount = roleMapper.deletePermsByRoleId(roleId, null);
            logger.info("编辑后权限为空，删除角色ID[{}]下的[{}]条菜单权限.", roleId, deleteCount);
        } else {
            List<Integer> permIds = Lists.newArrayList();
            //String集合转Integer集合
            CollectionUtils.collect(Arrays.asList(perm.split(",")),
                    new Transformer() {
                        @Override
                        public Object transform(Object input) {
                            return Integer.valueOf((String) input);
                        }
                    }, permIds);
            //查询角色关联的权限ID集合
            List<Integer> dbPerms = roleMapper.getPermIdsByRoleId(roleId);

            //定义可删除权限集合
            List deletePerms = new ArrayList();
            for (Integer id : dbPerms) {
                if (id != null && !permIds.contains(id)) {
                    deletePerms.add(id);
                }
            }
            // 删除当前角色下编辑后没有的权限
            if (deletePerms.size() > 0) {
                int deleteCount = roleMapper.deletePermsByRoleId(roleId, deletePerms);
                logger.info("编辑后权限不为空，删除角色ID[{}]下[{}]条菜单权限.", roleId, deleteCount);
            }

            //和数据库旧权限比较添加需要新增的
            List addPerms = new ArrayList();
            for (Integer permId : permIds) {
                if (permId != null && !dbPerms.contains(permId)) {
                    addPerms.add(permId);
                }
            }
            if (addPerms.size() > 0) {
                int addCount = roleMapper.insertPermsByRoleId(roleId, addPerms);
                logger.info("编辑后角色ID[{}]新增[{}]条菜单权限.", roleId, addCount);
            }
        }
    }

    /**
     * 根据角色ID删除角色同时删除角色对应权限关联表数据
     *
     * @param roleIds
     */
    public String deleteRoleByIds(Integer userId, List<Integer> roleIds) throws BizException {
        if (CollectionUtils.isEmpty(roleIds))
            throw new BizException(1001001);
        List<DataVo> usedRoles = roleMapper.getUsedRoles(roleIds);
        List<Integer> delRoleIds = Lists.newArrayList();
        if(CollectionUtils.isEmpty(usedRoles)){
            delRoleIds = roleIds;
        }
        else {
            for (Integer roleId : roleIds) {
                for (DataVo vo : usedRoles) {
                    if (roleId != vo.getInt("roleId")) {
                        delRoleIds.add(roleId);
                    }
                }
            }
        }
        // 删除角色
        if (delRoleIds.size() == 0) {
            return "所有删除角色已关联用户，不允许删除!";
        }
        Map<Integer, String> map = Maps.newHashMap();
        for (DataVo vo : usedRoles) {
            map.put(vo.getInt("roleId"), vo.getString("name"));
        }
        Integer delRole = roleMapper.deleteRolesById(delRoleIds);
        // 删除角色对应菜单权限关联
        Integer delPerm = roleMapper.deletePermsByRoleIds(delRoleIds);
        logger.info("删除[{}]个角色同时删除[{}]条角色对应菜单权限.", delRole, delPerm);
        saveLog("删除角色", OperateType.del.OperateId, String.format("用户ID为[%s]的用户删除%s个角色，角色ID为：%s，同时删除角色对应的%s条菜单权限.", userId, delRoleIds.size(), delRoleIds.toString(), delPerm), userId);
        String msg = "";
        // 不可删除的角色，已经被使用
        Collection<Integer> subtractList = CollectionUtils.subtract(roleIds, delRoleIds);
        if (delRoleIds.size() == 0) {
            msg = "所有删除角色已关联用户，不允许删除!";
        } else if (subtractList.size() > 0) {
            for (Integer id : subtractList) {
                msg += "【" + map.get(id) + "】、";
            }
            msg = "角色" + msg.substring(0, msg.length() - 1) + "已关联用户，不允许删除，其他角色删除成功!";
        }
        return msg;
    }

    /**
     * 根据用户ID获取角色下拉框
     *
     * @param userId
     * @return
     */
    public List<ComboxVo> queryRoleDict(Integer userId, String name) throws BizException {
        Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType);
        List<Integer> roleTypes = userService.getRoleTypeByUserId(userId);
        // 这块限制很大，字典刚好是等级越高值越低，只能看同级及以下(业务要求...)
        String roleType = "";
        if (CollectionUtils.isNotEmpty(roleTypes)){
            roleType = Integer.toString((Collections.min(roleTypes)));
        }
        List<ComboxVo> list = roleMapper.getRoleDictByOrgIds(name, orgIds, roleType);
        return list;
    }

    /**
     * 根据用户ID获取角色用户下拉框
     *
     * @param userId
     * @return
     */
    public List<ComboxVo> queryRoleUserDict(Integer userId, String loginName) throws BizException {
        Set<Integer> orgIds = userService.getUserRoleDataById(userId,RoleDataEnum.ORG.dataType);
        return roleMapper.getRoleUserDictByUserId(orgIds, loginName);
    }

    /**
     * 导出角色列表
     *
     * @param data
     * @param response
     * @throws Exception
     */
    public void export(Map data, HttpServletResponse response) throws Exception {
        DataVo params = new DataVo(data);
        // data参数中userId不能为空，为空的话获取所有的，不应该有这个权限
        if (params.isBlank("userId"))
            throw new BizException(1000006);
        data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"),RoleDataEnum.ORG.dataType));
        //结果集
        List<Map> list = roleMapper.getRolesByPage(data);
        List<String> headList = Lists.newArrayList();
        List<String> valList = Lists.newArrayList();
        headList.add("角色名称");
        headList.add("角色描述");
        headList.add("角色创建人");

        valList.add("name");
        valList.add("remark");
        valList.add("createBy");
        ExportUtils.exportExcel(list, response, headList, valList, "角色管理");
    }


	/**
	 * 根据用户获取可操作的管理员等级
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ComboxVo> queryRoleLevelDict(Integer userId) throws Exception {
		List<ComboxVo> list = dictService.getDictByType("glydj");
		final List<Integer> roleTypes = userService.getRoleTypeByUserId(userId);
		if (CollectionUtils.isNotEmpty(roleTypes)) {
			CollectionUtils.filter(list, new Predicate() {
				@Override
				public boolean evaluate(Object o) {
					for (Integer roleType : roleTypes) {
						if (Integer.valueOf(((ComboxVo) o).getId()) >= roleType) {
							return true;
						}
					}
					return false;
				}
			});
		}
		return list;
	}
}

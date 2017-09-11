package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.domain.ui.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述: 角色表操作（sys_role）
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年02月20日
 */
@Mapper
public interface RoleMapper {

    /**
     * 分页查询角色列表
     *
     * @param data
     * @return
     */
    public List getRolesByPage(Map data);

    /**
     * 根据角色ID查询
     *
     * @param roleId
     * @return
     */
    public DataVo getRoleById(@Param("id") Integer roleId);


    Integer getRoleCountByName(@Param("roleId")Integer roleId, @Param("chkFileName")String chkFileName,@Param("chkValue")String chkValue);

    /**
     * 查询已经被使用的角色ID和名称
     * @return
     */
    List<DataVo> getUsedRoles(@Param("roleIds") List roleIds);
    /**
     * 新增角色
     *
     * @param data
     * @return
     */
    public Integer insertRole(Map data);

    /**
     * 更新角色
     *
     * @param data
     * @return
     */
    public Integer updateRoleById(Map data);

    /**
     * 删除单个角色
     *
     * @param roleId
     * @return
     */
    public Integer deleteRoleById(@Param("roleId") Integer roleId);

    /**
     * 删除多个角色
     *
     * @param roleIds 角色ID集合
     * @return
     */
    public Integer deleteRolesById(@Param("roleIds") List roleIds);

    /**
     * 根据角色ID获取权限
     * @param roleId 角色ID
     * @return
     */
    List <TreeNode> getPermsByRoleId(@Param("roleId") Integer roleId);


    /**
     * 根据角色ID获取权限ID集合
     * @param roleId 角色ID
     * @return
     */
    List <Integer> getPermIdsByRoleId(@Param("roleId") Integer roleId);


    /**
     * 根据角色ID保存关联角色权限表
     * @param roleId 角色ID
     * @param permIds 权限ID集合
     * @return
     */
    Integer insertPermsByRoleId(@Param("roleId") Integer roleId, @Param("permIds") List permIds);

    /**
     * 根据角色ID和权限ID删除角色权限关联表中的数据
     * @param roleId 角色ID
     * @param permIds 权限ID集合
     * @return
     */
    Integer deletePermsByRoleId(@Param("roleId") Integer roleId, @Param("permIds") List permIds);


    /**
     * 根据角色ID集合删除对应菜单权限
     * @param roleIds
     */
    Integer deletePermsByRoleIds(@Param("roleIds") List roleIds);

    /**
     * 根据用户运营商权限获取角色下拉框
     * @param orgIds
     * @param name
     * @return
     */
    List<ComboxVo> getRoleDictByOrgIds(@Param("name") String name, @Param("orgIds") Set<Integer> orgIds, @Param("roleType") String roleType);

    /**
     * 根据用户运营商权限获取角色创建人账号
     * @param orgIds
     * @param loginName
     * @return
     */
    List<ComboxVo> getRoleUserDictByUserId(@Param("orgIds") Set<Integer> orgIds, @Param("loginName") String loginName);


    ///**
    // * 根据登录用户ID获取可查看的数据ID集合
    // * @param userId
    // * @return
    // */
    ////List<DataVo> getRoleDataByUserId(@Param("userId") Integer userId);

}

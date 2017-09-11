package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述: 用户表操作（sys_user）
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年02月20日
 */
@Mapper
public interface UserMapper {

    /**
     * 查询旧平台用户表所有数据
     * @return
     */
    List<DataVo> findPubUsers();


    /**
     * 分页查询用户列表
     *
     * @param data
     * @return
     */
    public List getUsersByPage(Map data);

    /**
     * 根据用户ID查询
     *
     * @param userId
     * @return
     */
    public DataVo getUserById(Integer userId);

    /**
     * 根据用户名密码查询
     *
     * @param loginName
     * @return
     */
    public List<DataVo> getUserByName(@Param("loginName") String loginName);

    /**
     * 根据用户名ID查询用户数量
     *
     * @param loginName
     * @return
     */
    Integer getUserCountByName(@Param("id") Integer id,@Param("loginName") String loginName);

    /**
     * 新增用户
     *
     * @param data
     * @return
     */
    public Integer insertUser(Map data);

    /**
     * 新增多个用户
     *
     * @param data
     * @return
     */
    public Integer insertUsers(List data);


    public Integer updateUsers(List data);

    /**
     * 更新用户
     *
     * @param data
     * @return
     */
    public Integer updateUserById(Map data);

    /**
     * 删除单个用户
     *
     * @param userId
     * @return
     */
    public Integer deleteUserById(@Param("userId") Integer userId);

    /**
     * 删除多个用户
     *
     * @param userIds
     * @return
     */
    public Integer deleteUsersById(@Param("userIds") List userIds, @Param("status") String status);


    /**
     * 根据用户ID查询角色IDs
     *
     * @param userId
     * @return
     */
    public List getRoleIdsByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId
     * @return
     */
    public List getRolesByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID查询角色类型集合
     * @param userId
     * @return
     */
    List<Integer> getRoleTypeByUserId(@Param("userId") Integer userId);

    public List getRoleNamesByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID新增角色关联
     *
     * @param userId
     * @return
     */
    public Integer insertRolesByUserId(@Param("userId") Integer userId, @Param("roleIds") List roleIds);

    /**
     * 根据用户ID删除角色关联
     *
     * @param userId
     * @param roleIds
     * @return
     */
    public Integer deleteRolesByUserId(@Param("userId") Integer userId, @Param("roleIds") List roleIds);


    /**
     * 根据用户IDS删除相关角色关联
     *
     * @param userIds
     * @return
     */
    void deleteRolesByUserIds(@Param("userIds") List userIds);
    /**
     * 查询用户的权限列表
     *
     * @param userId
     * @return
     */
    public List<TreeNode> getPermsByUserId(@Param("userId") Integer userId, @Param("permTypes") List<Integer> permTypes);


    public List getPermNamesByUserId(@Param("userId") Integer userId, @Param("permType") Integer permType);


    public List<DataVo> getPermissionsByUserId(@Param("userId") Integer userId, @Param("permType") Integer permType);
    /**
     * 根据用户Ids查询用户s
     * @param map
     * @return
     * 2017年4月11日
     * gaohui
     */
    public List<Map> getUsersByIds(Map map);

    /**
     * 修改个人信息
     * @param data
     * @return
     */
    Integer updateByUserName(Map data);

    /**
     * 根据用户IDs删除关联的角色
     * @param userIds
     * @return
     */
    Integer deleteUserRolesByUserIds(@Param("userIds") List userIds);

    /**
     * 根据角色ID集合和数据类型获取当前角色集合所拥有的数据权限ID集合
     * @param roleIds
     * @param dataType
     * @return
     */
    List<Integer> getUserRoleDataByRoleIds(@Param("roleIds") List<Integer> roleIds, @Param("dataType")Integer dataType);
    
    
    /**
     * 用户业务下拉
     */
    List getUsersSelect(DataVo vo);


    /**
     * 添加用户数据权限
     * @param userId 用户ID
     * @param dataIds 数据ID集合
     * @param dataType 数据类型
     * @return
     */
    Integer insertUserDataByUserId(@Param("userId") Integer userId, @Param("dataIds") Set<Integer> dataIds, @Param("dataType") Integer dataType);


    /**
     * 根据用户ID获取可查看的数据ID集合
     * @param userId
     * @return
     */
    List<DataVo> getUserDataByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID和数据类型删除用户对应的数据权限
     * @param userId
     * @param dataIds
     * @param dataType
     * @return
     */
    Integer deleteDatasByUserId(@Param("userId") Integer userId, @Param("dataIds") Set<Integer> dataIds, @Param("dataType") Integer dataType);

    /**
     * 根据运营商ID集合获取orgName,返回{@link TreeNode}对象
     * @param orgIds
     * @return
     */
    List<TreeNode> getOrgNameById(@Param("orgIds") Set<Integer> orgIds);

    /**
     * 根据运营商名称获取orgName,返回{@link TreeNode}对象
     * @param orgName
     * @return
     */
    List<TreeNode> getOrgNameByName(@Param("orgName") String orgName);

    /**
     * 根据场站ID集合查询场站，返回{@link TreeNode}集合
     * @param stationIds
     * @return
     */
    List<TreeNode> getStationNameById(@Param("stationIds") Set<Integer> stationIds);

    /**
     * 根据场站名称查询场站，返回{@link TreeNode}集合
     * @param stationName
     * @return
     */
    List<TreeNode> getStationNameByName(@Param("stationName") String stationName);

    /**
     * 根据orgIds查询场站
     * @param orgIds
     * @return
     */
    List<TreeNode> getStationNameByOrgId(@Param("orgIds") Set<Integer> orgIds);

    /**
     * 根据用户ID集合删除对应数据权限
     * @param userIds
     */
    Integer deleteUserDataByUserIds(@Param("userIds") List userIds);

    /**
     * 根据用户ID和数据类型获取用户数据权限
     * @param userId
     * @param dataType
     * @return
     */
    List<Integer> getUserDataIdsByUserId(@Param("userId") Integer userId, @Param("dataType") Integer dataType);

    List<Integer> getAllChildren(@Param("orgId") Integer orgId);

    /**
     * 查询所有父企业不为空的企业
     * @return
     */
    List<DataVo> getAllOrgs();

    /**
     * 前端按钮权限对应生成  临时用，可删除
     * @return
     */
    List<DataVo> getAllPerm();

    /**
     * 系统初始化时获取目标URL地址和对应权限组成权限资源
     * @return
     */
    List<DataVo> getAllPermTarget();


    List<DataVo> area();
}

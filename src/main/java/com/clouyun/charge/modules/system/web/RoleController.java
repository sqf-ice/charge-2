package com.clouyun.charge.modules.system.web;

import com.clouyun.boot.common.Constants;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.domain.ui.Tree;
import com.clouyun.boot.common.domain.ui.TreeNode;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.system.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述: 角色管理控制器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年1月3日
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController extends BusinessController {

    public static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * @api {GET} /api/roles 查询角色列表
     * @apiName queryRoles
     * @apiGroup RoleController
     * @apiVersion 2.0.0
     * @apiDescription 查询角色列表
     * <br/>
     * @apiParam {int} userId 登录用户ID
     * @apiParam {int} pageNum 页码
     * @apiParam {int} pageSize 页大小
     * @apiParam {String} [name] 角色下拉框名称直接查询
     * @apiParam {String} [roleId] 角色下拉框ID查询
     * @apiParam {String} [loginName] 创建人下拉框名称直接查询
     * @apiParam {String} [createBy] 创建人下拉框ID查询
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向  (asc:升序||desc:倒序)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {Int(11)} data.list.id 角色表ID(主键)
     * @apiSuccess {String(100)} data.list.name 角色名称
     * @apiSuccess {String(255)} data.list.remark 备注信息
     * @apiSuccess {Int(11)} data.list.createBy 角色创建人
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
     * {
        errorCode: 0,
        errorMsg: "操作成功!",
        total: 0,
        data: {
        pageNum: 1,
        pageSize: 87,
        size: 87,
        startRow: 0,
        endRow: 86,
        total: 87,
        pages: 1,
        list: [
            {
                id: -1,
                remark: "系统管理员",
                name: "系统管理员",
                createBy: "admin"
            },
            {
                id: 1,
                remark: "企业管理员",
                name: "企业管理员",
                createBy: "admin"
            }
        ]
       }
     }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000006 当前的登录用户为空!
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResultVo queryRoles(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        PageInfo roles = roleService.queryRoles(data);
        resultVo.setData(roles);
        return resultVo;
    }
    /**
     * @api {GET} /api/roles/{roleId} 查询角色详情
     * @apiName  queryRoleById
     * @apiGroup RoleController
     * @apiVersion 2.0.0
     * @apiDescription 根据角色ID查询角色详情
     * <br/>
     * @apiParam {int} roleId 角色表ID(主键)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 数据封装
     * @apiSuccess {int} data.id 编号
     * @apiSuccess {String(100)} data.name 角色名称
     * @apiSuccess {String(255)} data.remark 备注信息
     * @apiSuccess {Int(1)} data.roleType 管理员等级
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
    public ResultVo queryRoleById(@PathVariable("roleId") Integer roleId) throws Exception{
        ResultVo resultVo = new ResultVo();
        DataVo role = roleService.findRoleById(roleId);
        role.put("perms",roleService.findPermIdsByRoleId(roleId));
        resultVo.setData(role);
        return resultVo;
    }

    /**
     * @api {GET} /api/roles/{roleId}/perms 根据角色ID获取角色下关联权限
     * @apiName  queryPermsByRoleId
     * @apiGroup RoleController
     * @apiVersion 2.0.0
     * @apiDescription 根据角色ID获取关联权限，根据tree判断是否生成树形结构
     * <br/>
     * @apiParam {int} roleId 角色ID
     * @apiParam {Boolean} [tree] 是否以树形展示(true||false)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装数组
     * @apiSuccess {int} data.id 权限ID(主键)
     * @apiSuccess {String} data.name: 权限名称
     * @apiSuccess {int} data.level 等级,
     * @apiSuccess {String} data.href 请求地址
     * @apiSuccess {String} data.target 指向
     * @apiSuccess {String} data.icon 图标
     * @apiSuccess {int} data.pid 父节点ID
     * @apiSuccess {Object[]} data.child 子级数组
     * @apiSuccess {String} data.child.name: 权限名称
     * @apiSuccess {int} data.child.level 等级,
     * @apiSuccess {String} data.child.href 请求地址
     * @apiSuccess {String} data.child.target 指向
     * @apiSuccess {String} data.child.icon 图标
     * @apiSuccess {int} data.child.pid 父节点ID
     * <br/>
     * @apiSuccessExample {json} Success树形出参示例:
        {
        errorCode: 0,
        errorMsg: "操作成功!",
        total: 0,
        data: [
            {
            id: 1,
            name: "实时监控",
            level: 0,
            href: null,
            target: null,
            icon: null,
            pid: 0,
            child: [
                {
                id: 100,
                name: "运营总览",
                level: 0,
                href: null,
                target: null,
                icon: null,
                pid: 1,
                child: [ ]
                },
                {
                id: 101,
                name: "设备监控",
                level: 0,
                href: null,
                target: null,
                icon: null,
                pid: 1,
                child: [ ]
              }
            ]
        }
     * <br/>
     * @apiSuccessExample {json} Success非树形出参示例:
        {
        errorCode: 0,
        errorMsg: "操作成功!",
        total: 0,
        data: [
            {
            id: 1,
            name: "实时监控",
            level: 0,
            href: null,
            target: null,
            icon: null,
            pid: 0,
            child: []
            },
            {
            id: 100,
            name: "运营总览",
            level: 0,
            href: null,
            target: null,
            icon: null,
            pid: 1,
            child: [ ]
            },
            {
            id: 101,
            name: "设备监控",
            level: 0,
            href: null,
            target: null,
            icon: null,
            pid: 1,
            child: [ ]
            }
          ]
        }
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{roleId}/perms", method = RequestMethod.GET)
    public ResultVo queryPermsByRoleId(@PathVariable("roleId") Integer roleId, @RequestParam(value = "tree", required = false) Boolean tree, HttpServletRequest request) throws Exception{
        ResultVo resultVo = new ResultVo();
        List<TreeNode> perms = roleService.findPermsByRoleId(roleId);
        Boolean hasTree = request.getParameterMap().containsKey("tree");

        if (Boolean.FALSE.equals(hasTree) || Boolean.FALSE.equals(tree)) {
            resultVo.setData(perms);
        } else if (Boolean.TRUE.equals(hasTree) || Boolean.TRUE.equals(tree)) {
            Tree permTree = new Tree();
            resultVo.setData(permTree.list(perms));
        } else {

        }

        return resultVo;
    }

    /**
     * @api {POST} /api/roles 新增角色以及关联的权限
     * @apiName insertRole
     * @apiGroup RoleController
     * @apiVersion 2.0.0
     * @apiDescription 新增角色，同时保存角色关联的权限
     * <br/>
     * @apiParam {String(32)} [roleNo] 角色编号
     * @apiParam {String(100)} name 角色名称
     * @apiParam {Int(1)} [roleType] 管理员等级 （-1,1,2,3,4）
     * @apiParam {String(255)} [remark] 角色描述
     * @apiParam {Int(11)} createBy 创建者
     * @apiParam {String} [permIds] 角色对应菜单权限ID集合(例子：1,2,3,4)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResultVo insertRole(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        roleService.insertRolePerms(data);
        return resultVo;
    }
    /**
     * @api {PUT} /api/roles 更新角色以及关联的权限
     * @apiName updateRoles
     * @apiGroup RoleController
     * @apiVersion 2.0.0
     * @apiDescription 更新角色，同时更新角色关联的权限
     * <br/>
     * @apiParam {Int(11)} id 编号(主键)
     * @apiParam {String(32)} [roleNo] 角色编号
     * @apiParam {String(100)} name 角色名称
     * @apiParam {Int(1)} [roleType] 管理员等级 （-1,1,2,3,4）
     * @apiParam {String(255)} [remark] 角色描述
     * @apiParam {Int(11)} updateBy 更新者
     * @apiParam {String} [permIds] 角色对应权限ID集合(例子：1,2,3,4)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResultVo updateRole(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        roleService.updateRolePerms(data);
        return resultVo;
    }

    /**
     * @api {GET} /api/roles/{roleIds} 删除多个角色
     * @apiName  deleteRoleByIds
     * @apiGroup RoleController
     * @apiVersion 2.0.0
     * @apiDescription 删除角色同时删除角色对应的权限
     * <br/>
     * @apiParam {String} roleIds 角色ID集合(例：1,2,3,4,5)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{roleIds}", method = RequestMethod.DELETE)
    public ResultVo deleteRoleByIds(@PathVariable("roleIds") List<Integer> roleIds,@RequestParam("userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        String msg = roleService.deleteRoleByIds(userId, roleIds);
        resultVo.setErrorMsg(StringUtils.isBlank(msg)? Constants.INVOKE_SUCCESS_MSG:msg);
        return resultVo;
    }
    /**
     * @api {GET} /api/roles/dict 获取角色下拉框
     * @apiName  queryRoleDict
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription 根据用户ID获取可查看的角色组合成下拉框，同时支持模糊查询
     * <br/>
     * @apiParam {int} userId 登录用户ID
     * @apiParam (String) [name] 文本名称模糊查询字段
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装数组
     * @apiSuccess {String} data.id ID值，实际传后台值
     * @apiSuccess {String} data.name 展示文本值
     * @apiSuccess {String} data.pid 父角色ID
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/dict", method = RequestMethod.GET)
    public ResultVo queryRoleDict(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "name", required = false) String name) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(roleService.queryRoleDict(userId, name));
        return resultVo;
    }


    /**
     * @api {GET} /api/roles/{userId}/level/dict 获取管理员等级
     * @apiName  queryRoleLevelDict
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription 根据用户ID角色获取可操作的管理员等级级别
     * <br/>
     * @apiParam {int} userId 登录用户ID
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装数组
     * @apiSuccess {String} data.id ID值，实际传后台值
     * @apiSuccess {String} data.text 展示文本值
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{userId}/level/dict", method = RequestMethod.GET)
    public ResultVo queryRoleLevelDict(@PathVariable(value = "userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(roleService.queryRoleLevelDict(userId));
        return resultVo;
    }

    /**
     * @api {GET} /api/roles/user/dict 获取角色下的用户下拉框
     * @apiName  queryRoleUserDict
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription 根据用户ID查询用户角色所对应的创人间账号生成下拉框，同时支持模块查询
     * <br/>
     * @apiParam {int} userId 登录用户ID
     * @apiParam {String} [loginName] 文本模糊查询条件
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装数组
     * @apiSuccess {String} data.id ID值，实际传后台值
     * @apiSuccess {String} data.text 展示文本值
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/user/dict", method = RequestMethod.GET)
    public ResultVo queryRoleUserDict(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "loginName", required = false) String loginName) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(roleService.queryRoleUserDict(userId, loginName));
        return resultVo;
    }


    /**
     * @api {GET} /api/roles/_export 导出角色列表
     * @apiName exportRoles
     * @apiGroup RoleController
     * @apiVersion 2.0.0
     * @apiDescription 导出角色列表
     * <br/>
     * @apiParam {int} userId 登录用户ID
     * @apiParam {String} [name] 角色下拉框名称直接查询
     * @apiParam {String} [roleId] 角色下拉框ID查询
     * @apiParam {String} [loginName] 创建人下拉框名称直接查询
     * @apiParam {String} [createBy] 创建人下拉框ID查询
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向  (asc:升序||desc:倒序)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000006 当前的登录用户为空!
     */
    @RequestMapping(value = "_export", method = RequestMethod.GET)
    public ResultVo exportRoles(@RequestParam Map data, HttpServletResponse response) throws Exception {
        roleService.export(data, response);
        return new ResultVo();
    }
}
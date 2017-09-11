
package com.clouyun.charge.modules.system.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.domain.security.Subject;
import com.clouyun.boot.common.domain.security.UsernamePasswordToken;
import com.clouyun.boot.common.domain.ui.Tree;
import com.clouyun.boot.common.domain.ui.TreeNode;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.helper.UserHelper;
import com.clouyun.charge.common.secruity.JwtTokenUtil;
import com.clouyun.charge.common.secruity.JwtUser;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 描述: 用户管理控制器，就是做一个测试
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年1月3日
 */
@RestController
@RequestMapping("/api/users")
public class UserController extends BusinessController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // token前缀
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    // token参数名
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserService userService;

    /*UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();*/

    //用户旧表数据导入新表
    @RequestMapping(value = "/data/export", method = RequestMethod.GET)
    public ResultVo userDataExport() throws Exception {
        System.out.println("测试Jenkins版本回滚问题.");
        ResultVo resultVo = new ResultVo();
        userService.dataExport();
        return resultVo;
    }

    /**
     * @api {GET} /api/users 查询用户列表
     * @apiName queryUsers
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 查询用户列表, 包含分页、条件搜索、排序
     * <br/>
     * @apiParam {int} pageNum 页码
     * @apiParam {int} pageSize 页大小
     * @apiParam {int} userId 用户ID
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向
     * @apiParam {String} [loginName] 登录名
     * @apiParam {String} [roleName] 角色下拉框名称直接查询
     * @apiParam {String} [roleId] 角色下拉框ID查询
     * @apiParam {String} [orgName] 运营商下拉框name查询
     * <br/>
     * @apiSuccess {Int(11)} data.list.id 编号（原USER_ID）
     * @apiSuccess {String(100)} data.list.loginName 登录名（原USER_NAME）
     * @apiSuccess {String(200)} data.list.phone 电话（原USER_PHONE）
     * @apiSuccess {String(200)} data.list.roleName 用户角色
     * @apiSuccess {String(200)} data.list.orgName 所属企业
     * @apiSuccess {String(255)} data.list.address 住址（原USER_ADDR）
     * @apiSuccess {Int(1)} data.list.userState 用户状态 （02: 禁用 01: 正常 04: 锁定 03: 删除）
     * @apiSuccess {Int(1)} data.list.userType 用户类型（原USER_TYPE_CODE 01: 系统用户 02: 合约企业用户）
     * @apiSuccess {String(255)} data.list.remark 备注
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResultVo queryUsers(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        PageInfo pageInfo = userService.findUsers(data);
        resultVo.setData(pageInfo);
        return resultVo;
    }

    @RequestMapping(value = "/getSession", method = RequestMethod.GET)
    public ResultVo getSession(String key) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(session.getAttribute(key));
        return resultVo;
    }

    /**
     * @api {GET} /api/users/{userId} 查询用户信息
     * @apiName queryUserById
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 查询用户信息
     * <br/>
     * @apiParam {int} userId 用户ID
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 数据封装
     * @apiSuccess {Int(11)} data.id 编号（原USER_ID）(主键)
     * @apiSuccess {String(100)} data.userNo 用户编号
     * @apiSuccess {Int(11)} data.companyId 归属公司（原COMPANY_ID）
     * @apiSuccess {Int(11)} data.orgId 所属企业ID
     * @apiSuccess {String(128)} data.orgName 所属企业名称
     * @apiSuccess {String(100)} data.loginName 登录名（原USER_NAME）
     * @apiSuccess {String(100)} data.password 密码（原USER_PWD）
     * @apiSuccess {String(255)} data.salt MD5加密盐值
     * @apiSuccess {String(100)} data.name 姓名
     * @apiSuccess {String(8)} data.gender 性别（原USER_GENDER；01: 男，02: 女）
     * @apiSuccess {String(200)} data.email 邮箱（原USER_EMAIL）
     * @apiSuccess {String(200)} data.phone 电话（原USER_PHONE）
     * @apiSuccess {String(200)} data.mobile 手机
     * @apiSuccess {String(255)} data.address 住址（原USER_ADDR）
     * @apiSuccess {Int(1)} data.userState 用户状态（ 02: 禁用 01: 正常 04: 锁定 03: 删除）
     * @apiSuccess {Int(1)} data.userType 用户类型（原USER_TYPE_CODE 01: 系统用户 02: 合约企业用户）
     * @apiSuccess {String(1000)} data.photo 用户头像
     * @apiSuccess {String(100)} data.loginIp 最后登陆IP
     * @apiSuccess {Date} data.loginTime 最后登陆时间（格式: yy-MM-dd HH:mm:ss）
     * @apiSuccess {Int(1)} data.loginFlag 是否可登录（0: 不可登录 1: 可用登录 默认: 1）
     * @apiSuccess {String(255)} data.remark 备注信息（原REMARK）
     * @apiSuccess {Int(11)} data.roleIds 用户角色
     * @apiSuccess {Int(11)} data.createBy 创建者（-1:手动操作 默认: -1）
     * @apiSuccess {Date} data.createTime 创建时间（格式: yy-MM-dd HH:mm:ss）
     * @apiSuccess {Int(11)} data.updateBy 更新者（-1:手动操作 默认: -1）
     * @apiSuccess {Date} data.updateTime 更新时间（格式: yy-MM-dd HH:mm:ss）
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResultVo queryUserById(@PathVariable("userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        Map user = userService.findUserById(userId);
        if (user != null && !user.isEmpty()) {
            List<Integer> roles = userService.getRoleIdsByUserId(userId);
            user.put("roleIds", CollectionUtils.isNotEmpty(roles) ? roles.get(0) : null);
            user.remove("salt");
        }
        resultVo.setData(user);
        return resultVo;
    }


    /**
     * @api {POST} /api/users/_exist 用户登录校验
     * @apiName verifyUser
     * @apiGroup UserController
     * @apiVersion 1.4.0
     * @apiDescription 校验用户名和密码
     * <br/>
     * @apiParam {String} loginName 用户登录名
     * @apiParam {String} password 用户密码
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/_exist", method = RequestMethod.POST)
    public ResultVo verifyUser(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        ResultVo resultVo = new ResultVo();
        Map user = userService.checkUser(username, password);
        resultVo.setData(user);
        return resultVo;
    }

    /**
     * @api {GET} /api/user/_login 用户登录
     * @apiName login
     * @apiGroup UserController
     * @apiVersion 1.4.0
     * @apiDescription TODO
     * <br/>
     * @apiParam {String} paramName paramDesc
     * @apiParam {String} paramName paramDesc
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/_login", method = RequestMethod.POST)
    public Map verifyUser(@RequestBody UsernamePasswordToken token) throws Exception {
        ResultVo resultVo = new ResultVo();

        //DataVo user = userService.checkUser(token.getUsername(), token.getPassword());
        DataVo user = userService.login(token.getUsername(), token.getPassword());
        if (null == user) {
            Subject subject = new Subject();
            subject.setAuthenticated(false);
            return subject.toKeyBean();
        } else {

            List roles = userService.findUserRoleNames(user.getInt("id"));
            List perms = userService.findUserPermNames(user.getInt("id"), null);

            Subject subject = new Subject();
            subject.setPrincipal(user);
            subject.setCredentials(user);
            subject.setRoles(roles);
            subject.setPermissions(perms);
            subject.setAuthenticated(true);

            resultVo.setData(subject.toKeyBean());
            return subject.toKeyBean();
        }

        // return resultVo;

    }

    /**
     * @api {GET} /api/users/auth 请求token
     * @apiName  jwtUser
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 用户登录同时生产认证token返回
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
     *  "token":
     *      {
     *          "principal":"admin",
     *          "credentials":"123456"
     *      }
     * }
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/${jwt.route.authentication.path}", method = RequestMethod.POST)
    public Map jwtUser(@RequestBody UsernamePasswordToken useranemPassword) throws Exception {
        ResultVo resultVo = new ResultVo();
        JwtUser user = userService.jwtLogin(useranemPassword.getUsername(), useranemPassword.getPassword());
        //获取当前Token
        final String token = tokenHead + jwtTokenUtil.generateToken(user);

        if (null == user) {
            Subject subject = new Subject();
            subject.setAuthenticated(false);
            return subject.toKeyBean();
        } else {
            Subject subject = new Subject();
            subject.setPrincipal(user.toDataVo());
            DataVo vo = new DataVo();
            vo.set("token", token);
            vo.set("menus", user.getMenus());
            vo.set("first", user.getFirst());
            subject.setCredentials(vo);
            subject.setRoles(user.getRoles());
            subject.setPermissions(user.getPerms());
            subject.setAuthenticated(true);

            resultVo.setData(subject.toKeyBean());
            return subject.toKeyBean();
        }
    }

    /**
     * @api {GET} /api/users/refresh 刷新token
     * @apiName  refreshToken
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 验证旧token同时返回新的token，请求头里包含旧token，在旧token失效前一小时内允许刷新token，旧token失效后重写发起请求token
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
     *  "Authorization":Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE0OTc0MzI5NzEsInN1YiI6ImFkbWluIiwiY3JlYXRlZCI6MTQ5NzM0NjU3MTQ2NX0.PSGVEnDR2HCFmmBIDywIcg4GZgLEVSd9fpmtL3nyomCJFQ_VPvwDrm4UTI7YHKY4aadR514WX9m9mK71ryZVCA
     * }
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public String refreshToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader(tokenHeader);
        final String refreshedToken = tokenHead + userService.refresh(token);
        return refreshedToken;
    }

    /**
     * @api {POST} /api/users/_loginOut 用户退出登录
     * @apiName loginOut
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 用户退出系统
     * <br/>
     * @apiParam {int} userId
     * @apiParam {String} loginName
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -1000012 用户ID不能为空!
     */
    @RequestMapping(value = "/_loginOut", method = RequestMethod.POST)
    public ResultVo loginOut(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        userService.loginOut(data);
        return resultVo;
    }

    /**
     * @api {POST} /api/users 保存用户
     * @apiName addUser
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 新增用户，对手机号，邮箱，电话进行验证，同时对密码进行加盐保存
     * <br/>
     * @apiParam {String(100)} [userNo] 用户编号
     * @apiParam {Int(11)} [companyId] 合约企业（原COMPANY_ID）
     * @apiParam {Int(11)} orgId 所属企业
     * @apiParam {String(100)} [loginName] 登录名（原USER_NAME）
     * @apiParam {String(100)} [password] 密码（原USER_PWD）
     * @apiParam {String(100)} [name] 姓名
     * @apiParam {Int(1)} [gender] 性别（原USER_GENDER；01: 男，02: 女 默认: 01）
     * @apiParam {String(200)} [email] 邮箱（原USER_EMAIL）
     * @apiParam {String(200)} [phone] 电话（原USER_PHONE）
     * @apiParam {String(200)} [mobile] 手机
     * @apiParam {String(255)} [address[ 住址（原USER_ADDR）
     * @apiParam {Int(1)} [userState] 用户状态（原USER_STATE_CODE, 02: 禁用 01: 正常 04: 锁定 03: 删除 默认: 01）
     * @apiParam {Int(1)} [userType] 用户类型（原USER_TYPE_CODE 01: 系统用户 02: 合约企业用户）
     * @apiParam {String(1000)} [photo] 用户头像
     * @apiParam {String} [roleIds] 用户角色(暂时只有一个，以后可能有多个，例：1,2,3,4)
     * @apiParam {Int(11)} createBy 创建者（-1:手动操作 默认: -1）
     * @apiParam {String(255)} [remark] 备注信息（原REMARK）
     * @apiParam {String} [orgIds] 角色对应数据权限运营商ID集合(例子：1,2,3,4)
     * @apiParam {String} [stationIds] 角色对应数据权限场站ID集合(例子：1,2,3,4)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 创建者不能为空!
     * @apiError -1000014 手机号码格式不正确!
     * @apiError -1000014 邮箱格式不正确!
     * @apiError -1000014 电话格式不正确!
     */
    @RequestMapping(value = "", method = RequestMethod.POST)//required = false
    public ResultVo addUser(@RequestBody() Map data) throws Exception {
        ResultVo resultVo = new ResultVo();

        userService.addUser(data);
        return resultVo;
    }

    /**
     * @api {PUT} /api/users 更新用户
     * @apiName updateUser
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 更新用户，对手机号，邮箱，电话进行验证，同时对密码进行加盐保存
     * <br/>
     * @apiParam {Int(11)} id 编号（原USER_ID）
     * @apiParam {String(100)} [userNo] 用户编号
     * @apiParam {Int(11)} [companyId] 合约企业（原COMPANY_ID）
     * @apiParam {Int(11)} orgId 所属企业
     * @apiParam {String(100)} [loginName] 登录名（原USER_NAME）
     * @apiParam {String(100)} [password] 密码（原USER_PWD）
     * @apiParam {String(100)} [name] 姓名
     * @apiParam {Int(1)} [gender] 性别（原USER_GENDER；01: 男，02: 女）
     * @apiParam {String(200)} [email] 邮箱（原USER_EMAIL）
     * @apiParam {String(200)} [phone] 电话（原USER_PHONE）
     * @apiParam {String(200)} [mobile] 手机
     * @apiParam {String(255)} [address[ 住址（原USER_ADDR）
     * @apiParam {Int(1)} [userState] 用户状态（原USER_STATE_CODE, 02: 禁用 01: 正常 04: 锁定 03: 删除 默认: 01）
     * @apiParam {Int(1)} [userType] 用户类型（原USER_TYPE_CODE 01: 系统用户 02: 合约企业用户）
     * @apiParam {String(1000)} [photo] 用户头像
     * @apiParam {String} [roleIds] 用户角色(暂时只有一个，以后可能有多个，例：1,2,3,4)
     * @apiParam {String(255)} [remark] 备注信息（原REMARK）
     * @apiParam {Int(11)} updateBy 更新者
     * @apiParam {String} [orgIds] 角色对应数据权限运营商ID集合(例子：1,2,3,4)
     * @apiParam {String} [stationIds] 角色对应数据权限场站ID集合(例子：1,2,3,4)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 更新者不能为空!
     * @apiError -1000012 更新数据主键不能为空!
     * @apiError -1000014 手机号码格式不正确!
     * @apiError -1000014 邮箱格式不正确!
     * @apiError -1000014 电话格式不正确!
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResultVo updateUser(@RequestBody() Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        userService.updateUser(data);
        return resultVo;
    }

    /**
     * @api {DELETE} /api/users/{userIds} 根据ID删除用户
     * @apiName deleteUsers
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 根据ID删除用户同时删除用户的角色和数据权限
     * <br/>
     * @apiParam {String} userIds 用户ids(例:1,2,3,4)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{userIds}", method = {RequestMethod.DELETE})
    public ResultVo deleteUsers(@PathVariable("userIds") List userIds,@RequestParam("userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        userService.deleteUsersById(userId,userIds);
        return resultVo;
    }

    /**
     * @api {GET} /api/users/{userId}/orgs 获取登录用户对应的数据权限
     * @apiName queryLoginUserOrgStation
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 根据当前登录用户获取可查看数据权限返回树形结构，一级菜单是运营商，二级菜单是场站
     * <br/>
     * @apiParam {int} userId 登录用户ID
     * @apiParam {String} [orgName] 查询运营商名称
     * @apiParam {String} [stationName] 查询场站ID
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装
     * @apiSuccess {Object} data.id 主键ID值
     * @apiSuccess {Object} data.name 展示文本名称
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{userId}/orgs")
    public ResultVo queryLoginUserOrgStation(@PathVariable("userId") Integer userId,
                                             @RequestParam(name = "orgName", required = false) String orgName,
                                             @RequestParam(name = "stationName", required = false) String stationName) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(userService.queryOrgStationByUserId(userId, orgName, stationName));
        return resultVo;
    }


    /**
     * @api {GET} /api/users/do/{userId}/orgs 获取编辑用户对应的数据权限
     * @apiName queryUserOrgStation
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 根据当前编辑用户获取可查看数据权限返回树形结构，一级菜单是运营商，二级菜单是场站
     * <br/>
     * @apiParam {int} userId 登录用户ID
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装
     * @apiSuccess {Object} data.id 主键ID值
     * @apiSuccess {Object} data.name 展示文本名称
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/do/{userId}/orgs")
    public ResultVo queryUserOrgStation(@PathVariable("userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(userService.queryOrgStationByUserId(userId));
        return resultVo;
    }

    /**
     * @api {GET} /api/users/{userId}/roles 查询用户拥有的角色集合
     * @apiName queryRoles
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 查询用户拥有的角色集合
     * <br/>
     * @apiParam {String} userId 用户ID
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * @apiSuccess {Object[]} data 分页数据对象数组
     * @apiSuccess {Int(11)} data.id 编号
     * @apiSuccess {String(32)} data.roleNo 角色编号
     * @apiSuccess {String(100)} data.name 角色名称
     * @apiSuccess {Int(11)} data.orgId 归属机构
     * @apiSuccess {String(255)} data.enName 英文名称
     * @apiSuccess {Int(1)} data.roleState 角色状态 （0: 不可用 1: 可用 默认: 1）
     * @apiSuccess {Int(1)} data.roleType 角色类型
     * @apiSuccess {Int(1)} data.dataScope 数据范围
     * @apiSuccess {String(255)} data.remark 备注信息
     * @apiSuccess {Int(11)} data.createBy 创建者（-1:手动操作 默认: -1）
     * @apiSuccess {Date} data.createTime 创建时间（格式: yy-MM-dd HH:mm:ss）
     * @apiSuccess {Int(11)} data.updateBy 更新者（-1:手动操作 默认: -1）
     * @apiSuccess {Date} data.updateTime 更新时间（格式: yy-MM-dd HH:mm:ss）
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{userId}/roles", method = RequestMethod.GET)
    public ResultVo queryRoles(@PathVariable("userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        List roles = userService.findUserRoles(userId);
        resultVo.setData(roles);
        return resultVo;
    }


    /**
     * @api {PUT} /api/users/{userId}/roles/{roleIds} 编辑用户关联的角色
     * @apiName editRoles
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription roleIds为空，表示删除用户关联的所有角色
     * <br/>
     * @apiParam {String} userId 用户ID
     * @apiParam {String} [roleIds] 多个角色ID eg:1,2,3, 可选
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = {"/{userId}/roles/{roleIds}", "/{userId}/roles"}/*, params = {"method=login"}*/, method = RequestMethod.PUT)
    public ResultVo editRoles(@PathVariable("userId") Integer userId, @PathVariable(name = "roleIds", required = false) List<Integer> roleIds) throws Exception {
        ResultVo resultVo = new ResultVo();
        userService.modifyUserRoles(userId, roleIds);
        return resultVo;
    }

    /**
     * @api {GET} /api/users/{userId}/perms 查询用户权限
     * @apiName queryPerms
     * @apiGroup UserController
     * @apiVersion 1.4.0
     * @apiDescription 查询用户权限，根据请求是否返回树形结构返回数据
     * <br/>
     * @apiParam {int} userId 用户ID (必填)
     * @apiParam {Boolean} tree 是否以树形展示(true||false,非必填)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * @apiSuccess {int} data.count 总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * <br/>
     * @apiError -1000000 未输入账号密码!
     */

    @RequestMapping(value = "/{userId}/perms", method = RequestMethod.GET)
    public ResultVo queryPerms(@PathVariable("userId") Integer userId, @RequestParam(value = "tree", required = false) Boolean tree, HttpServletRequest request) throws Exception {
        ResultVo resultVo = new ResultVo();
        List<TreeNode> perms = userService.findUserPerms(userId, null);
        CollectionUtils.filter(perms, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return !UserHelper.filterPerms.contains(((TreeNode)o).getId());
            }
        });
        Boolean hasTree = request.getParameterMap().containsKey("tree");

        if (Boolean.FALSE.equals(hasTree) || Boolean.FALSE.equals(tree)) {
            resultVo.setData(perms);
        } else if (Boolean.TRUE.equals(hasTree) || Boolean.TRUE.equals(tree)) {
            // 多做一层过滤，避免不是一级菜单的显示在页面，前端修改角色权限时，如果自动把子节点去掉是不会这个情况的
            Tree permTree = new Tree();
            List<TreeNode> list = permTree.list(perms);
            CollectionUtils.filter(list, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return ((TreeNode) o).getLevel() == 1;
                }
            });
            resultVo.setData(list);
        }
        return resultVo;
    }


    @RequestMapping(value = "/{userId}/menus", method = RequestMethod.GET)
    public ResultVo queryMenus(@PathVariable("userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        List<TreeNode> perms = userService.findUserPerms(userId, Lists.newArrayList(1));
        resultVo.setData(new Tree().list(perms));
        return resultVo;
    }


    /**
     * 根据ID获取用户信息
     *
     * @param data
     * @return
     * @throws Exception 2017年4月7日
     *                   gaohui
     */
    @RequestMapping(value = "/id", method = RequestMethod.POST)
    public ResultVo queryUserById(@RequestBody DataVo data) throws Exception {
        ResultVo resultVo = new ResultVo();
        Map user = userService.findUserById(((Double) data.get("userId")).intValue());
        resultVo.setData(user);
        return resultVo;
    }

    @RequestMapping(value = "/ids", method = RequestMethod.POST)
    public ResultVo getUsersByIds(@RequestBody DataVo data) throws Exception {
        ResultVo resultVo = new ResultVo();
        List<Map> user = (List<Map>) userService.getUsersByIds(data);
        resultVo.setData(user);
        return resultVo;
    }

    /**
     * @api {PUT} /api/users/personal 修改个人信息
     * @apiName modifyPersonalInfo
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 修改个人信息,包含邮箱，密码
     * <br/>
     * @apiParam {String} loginName 用户名
     * @apiParam {String} oldPassword 旧密码
     * @apiParam {String} newpassword1 新密码
     * @apiParam {String} newpassword2 重复新密码
     * @apiParam {String} phone 手机号
     * @apiParam {String} email 邮箱
     * @apiParam {String} id 用户ID
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
     * "id":1004,
     * "loginName": "xiaosh123",
     * "oldPassword": "123456",
     * "newpassword1": "456789",
     * "newpassword2": "456789",
     * "phone": "18727063643",
     * "email": "123456@qq.com"
     * }
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
     * {
     * errorCode: 0,
     * errorMsg: "操作成功!",
     * total: 0
     * }
     * @apiError -999 系统异常!
     * @apiError -1000000 未输入账号密码!
     * @apiError -1000014 手机号码格式不正确!
     * @apiError -1000014 邮箱格式不正确!
     * @apiError -1000004 当前账号异常，原因：用户不存在!
     * @apiError -1000011 当前账号重复!
     * @apiError -1000010 两次新密码输入不一致!
     * @apiError -1000004 当前账号异常，原因：原密码错误!
     */
    @RequestMapping(value = "/personal", method = RequestMethod.PUT)
    public ResultVo modifyPersonalInfo(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        userService.modifyPersonalInfo(data);
        return resultVo;
    }


    /**
     * @api {GET} /api/users/_export 导出平台用户
     * @apiName exportUsers
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 导出平台用户, 包含条件搜索、排序
     * <br/>
     * @apiParam {int} pageIndex 页码
     * @apiParam {int} pageSize 页大小
     * @apiParam {int} userId 用户ID
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向
     * @apiParam {String} [loginName] 登录名
     * @apiParam {String} [roleName] 角色下拉框名称直接查询
     * @apiParam {String} [roleId] 角色下拉框ID查询
     * @apiParam {String} [orgId] 运营商下拉框ID查询
     */
    @RequestMapping(value = "/_export", method = RequestMethod.GET)
    public ResultVo exportUsers(@RequestParam Map data, HttpServletResponse response) throws Exception {
        userService.export(data, response);
        return new ResultVo();
    }

    /**
     * @api {get} /api/users/_select    用户业务下拉字典
     * @apiName getUsersSelect
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription 杨帅B 用户业务下拉字典
     * <br/>
     * @apiParam {Integer}    [userId]     用户Id
     * @apiParam {Integer}	  [userAppId]  审批人Id
     * @APIParam {Integer}	  [pageNum]		页码 默认:1
     * @APIParam {Integer}	  [pageSize]	页大小 默认:20
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {Integer} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {Integer} data.list.id 用户Id
     * @apiSuccess {String} data.list.name 用户名称
     * <br/>
     * @apiError -999  系统异常
     * @apiError -888  请求方式异常
     */
    @RequestMapping(value = "_select", method = RequestMethod.GET)
    public ResultVo getUsersSelect(@RequestParam Map map) throws BizException {
        ResultVo resVo = new ResultVo();
        PageInfo pageList = userService.getUsersSelect(map);
        resVo.setData(pageList);
        return resVo;
    }

    /**
     * 前端按钮权限对应生成  临时用，可删除
     * @return
     * @throws Exception
     */
    @GetMapping("/authDoc")
    public ResultVo authDoc() throws Exception {
        userService.authDoc();
        return new ResultVo();
    }

    /**
     * @api {get} /api/users/{userId}/stationIds   获取用户场站数据权限
     * @apiName getUserStationIds
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 对外提供调用用户数据权限服务(场站)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        数据权限集合(stationId集合)
     * <br/>
     * @apiError -1000012  用户ID不能为空
     * @apiError -1000012  数据类型不能为空
     */
    @RequestMapping(value = "/{userId}/stationIds", method = RequestMethod.GET)
    public ResultVo getUserStationIds(@PathVariable Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(userService.getUserRoleDataById(userId, RoleDataEnum.STATION.dataType));
        return resultVo;
    }

    /**
     * @api {get} /api/users/{userId}/orgIds   获取用户运营商数据权限
     * @apiName getUserRoleData
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 对外提供调用用户数据权限服务(运营商)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        数据权限集合(orgId集合)
     * <br/>
     * @apiError -1000012  用户ID不能为空
     * @apiError -1000012  数据类型不能为空
     */
    @RequestMapping(value = "/{userId}/orgIds", method = RequestMethod.GET)
    public ResultVo getUserOrgIds(@PathVariable Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(userService.getUserRoleDataById(userId, RoleDataEnum.ORG.dataType));
        return resultVo;
    }

    /**
     * @api {get} /api/users/{userId}/roleTypes   获取用户角色类型
     * @apiName getUserRoleTypes
     * @apiGroup UserController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 对外提供调用用户角色类型
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        角色类型集合
     * <br/>
     * @apiError -1000012  用户ID不能为空
     */
    @RequestMapping(value = "/{userId}/roleTypes", method = RequestMethod.GET)
    public ResultVo getUserRoleTypes(@PathVariable Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(userService.getRoleTypeByUserId(userId));
        return resultVo;
    }
}

package com.clouyun.charge.modules.system.service;

import com.clou.common.utils.CalendarUtils;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.Tree;
import com.clouyun.boot.common.domain.ui.TreeNode;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ErrorUtils;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.boot.services.CacheService;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RedisKeyEnum;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.domain.LoginLock;
import com.clouyun.charge.common.helper.RoleHelper;
import com.clouyun.charge.common.helper.UserHelper;
import com.clouyun.charge.common.secruity.JwtTokenUtil;
import com.clouyun.charge.common.secruity.JwtUser;
import com.clouyun.charge.common.secruity.JwtUserFactory;
import com.clouyun.charge.common.secruity.MD5SaltPasswordEncoder;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.StationMapper;
import com.clouyun.charge.modules.document.service.CompanyService;
import com.clouyun.charge.modules.system.mapper.CoopOperMapper;
import com.clouyun.charge.modules.system.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.jsonwebtoken.Claims;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.clouyun.charge.common.helper.UserHelper.waitMins;
import static com.clouyun.charge.common.helper.UserHelper.worongCont;
import static org.apache.commons.collections.CollectionUtils.collect;

/**
 * 描述: 用户管理服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年02月21日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends BusinessService implements UserDetailsService {

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String SYS_USER = "01";// 系统用户
    private static final String COMPANY_USER = "02";// 合约企业用户
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CoopOperMapper coopOperMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    public MD5SaltPasswordEncoder md5Salt;

    @Autowired
    private DictService dictService;


    /**
     * 旧平台数据导入
     *
     * @throws BizException
     */
    public void dataExport() throws BizException, InterruptedException {
        //List<DataVo> ls = userMapper.getUsersByPage(Maps.newHashMap());
        //if (CollectionUtils.isNotEmpty(ls))
        //    throw new BizException(1000011, "用户导数据任务执行");
        List<DataVo> list = userMapper.findPubUsers();
        for (DataVo params : list) {
            params.put("id", params.getInt("userId"));
            params.put("loginName", params.getString("userName"));
            String rawSalt = UserHelper.buildRawSalt(params);//构建原始盐
            String enPassword = UserHelper.encoderPassword(params.getString("userPwd"), rawSalt);//MD5盐加密
            String enSalt = UserHelper.encodeSalt(rawSalt);//加密盐
            params.put("salt", enSalt);
            params.put("password", enPassword);
            params.put("gender", params.getString("userGender"));
            params.put("createBy", -1);
            params.put("updateBy", -1);
            params.put("userState", params.getString("userStateCode"));
            params.put("phone", params.getString("userPhone"));
            params.put("mobile", params.getString("userPhone"));
            params.put("email", params.getString("userEmail"));
            params.put("userType", params.getString("userTypeCode"));
            params.put("photo", params.getString("nameBy"));
            params.put("address", params.getString("userAddr"));
        }
        for (DataVo vo : list) {
            System.out.println(vo.getId());
        }
        //for (int i = 0; i < list.size() / 200 + 1; i++) {
        //    if ((i + 1) * 200 < list.size()) {
        //        userMapper.insertUsers(list.subList(i * 200, (i + 1) * 200));
        //    } else {
        userMapper.insertUsers(list);//list.subList(i * 200, list.size())
        //    }
        //}
    }

    /**
     * 查找用户列表
     *
     * @param data
     * @return
     * @throws BizException
     */
    public PageInfo findUsers(final Map data) throws BizException {
        DataVo params = new DataVo(data);
        if (params.isBlank("userId"))
            throw new BizException(1000006);

        //根据登录用户获取可以查看的机构信息
        data.put("orgIds", getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(data);
        }
        List users = userMapper.getUsersByPage(data);
        PageInfo page = new PageInfo(users);
        //List<DataVo> list = userMapper.area();
        //for (DataVo dataVo : list) {
        //    System.out.println("{name:'"+dataVo.getString("areaName")+"', geoCoord:["+dataVo.getString("stationLng")+", "+dataVo.getString("stationLat")+"]},");
        //}
        return page;
    }
    // Page page = new Page(1,50);
    // RowBounds rowBounds = new RowBounds(50,50);// offset起始行，limit偏移量
    // List users = userMapper.getUsersByPage(data,rowBounds);
    // page.addAll(users);
    // page.setTotal(自定义查询count);
    // PageInfo pageInfo = new PageInfo(page);
    /**
     * 查找用户信息
     *
     * @param userId
     * @return
     */
    public DataVo findUserById(Integer userId) throws BizException {
        DataVo user = userMapper.getUserById(userId);
        return user;
    }

    /**
     * 新增用户
     *
     * @param data
     * @return
     */
    public void addUser(Map data) throws BizException {
        DataVo params = new DataVo(data);
        CommonUtils.idIsEmpty(params.getInt("createBy"), "创建者");
        this.vertifyUser(params);
        String password = params.getString("password"); //获取登录密码

        String rawSalt = UserHelper.buildRawSalt(params);//构建原始盐
        String enPassword = UserHelper.encoderPassword(password, rawSalt);//MD5盐加密
        String enSalt = UserHelper.encodeSalt(rawSalt);//加密盐

        params.put("salt", enSalt);
        params.put("password", enPassword);
        params.put("userNo", "nouse");
        userMapper.insertUser(params);
        Integer userId = params.getInt("id");
        // 同时保存用户角色
        String roleStr = params.getString("roleIds");
        if (StringUtils.isNoneBlank(roleStr)) {
            List roleIds = Arrays.asList(roleStr.split(","));
            Integer addCount = userMapper.insertRolesByUserId(userId, roleIds);
            logger.info("新增用户返回ID为[{}]，同时新增[{}]条角色.", userId, addCount);
        }
        // 数据权限运营商ID集合
        String org = params.getString("orgIds");
        // 数据权限场站ID集合
        String station = params.getString("stationIds");
        // 添加角色数据权限(运营商)
        this.insertUserData(userId, org, RoleDataEnum.ORG.dataType);
        // 添加角色数据权限(场站)
        this.insertUserData(userId, station, RoleDataEnum.STATION.dataType);
    }

    /**
     * 校验数据正确性
     * @param params
     * @throws BizException
     */
    private void vertifyUser(DataVo params) throws BizException {
        String userType = params.getString("userType");
        CommonUtils.valIsEmpty(userType, "用户类型");
        if (COMPANY_USER.equals(userType)){
            CommonUtils.idIsEmpty(params.getInt("companyId"), "合约企业");
            Map comany = companyService.getCompanyById(params.getInt("companyId"));
            if (comany==null||comany.isEmpty())
                throw new BizException(1000015, "合约企业");
            params.set("orgId",comany.get("orgId"));
        }
        CommonUtils.idIsEmpty(params.getInt("orgId"), "所属企业");
        String mobile = params.getString("mobile");
        String email = params.getString("email");
        String phone = params.getString("phone");
        // 监测用户是否存在，存在直接抛出异常
        loginNameExist(params.getId(), params.getString("loginName"));
        CommonUtils.valIsEmpty(params.getString("roleIds"),"角色配置");
        // 手机号码正则验证
        if (StringUtils.isNotBlank(mobile) && !ValidateUtils.Mobile(mobile))
            throw new BizException(1000014, "手机号码");

        // 邮箱正则验证
        if (StringUtils.isNotBlank(email) && !ValidateUtils.Email(email))
            throw new BizException(1000014, "邮箱");

        // 电话正则验证
        if (StringUtils.isNotBlank(phone) && phone.length()>=15)
            throw new BizException(1000017, "联系电话", 15);
    }

    /**
     * 添加用户数据权限
     *
     * @param userId
     * @param data
     * @param dataType
     */
    public void insertUserData(Integer userId, String data, Integer dataType) throws BizException {
        if (StringUtils.isNotBlank(data)) {
            //String集合转Integer集合
            Set<Integer> dataIds = Sets.newHashSet(CollectionUtils.collect(Arrays.asList(data.split(",")),
                    new Transformer() {
                        @Override
                        public Object transform(Object input) {
                            return Integer.valueOf((String) input);
                        }
                    }));
            if (CollectionUtils.isNotEmpty(dataIds)) {
                int addCount = userMapper.insertUserDataByUserId(userId, dataIds, dataType);
                logger.info("新增用户保存成功，用户ID[{}]下新增[{}]条[{}]数据权限.", userId, addCount, RoleDataEnum.getName(dataType));
            }

        }
    }

    /**
     * 更新用户信息
     *
     * @param data
     * @throws BizException
     */
    public void updateUser(Map data) throws BizException {
        DataVo params = new DataVo(data);
        Integer userId = params.getInt("id");
        CommonUtils.idIsEmpty(userId, "更新数据主键ID");
        CommonUtils.idIsEmpty(params.getInt("updateBy"), "更新者");
        this.vertifyUser(params);
        String password = params.getString("password"); //获取登录密码
        if (StringUtils.isNotBlank(password)) {
            String rawSalt = UserHelper.buildRawSalt(params);//构建原始盐
            String enPassword = UserHelper.encoderPassword(password, rawSalt);//MD5盐加密
            String enSalt = UserHelper.encodeSalt(rawSalt);//加密盐

            params.put("salt", enSalt);
            params.put("password", enPassword);
        } else {
            params.remove("password");
        }
        userMapper.updateUserById(params);

        // 更新用户角色
        String roleStr = params.getString("roleIds");
        List roleIds = Arrays.asList(roleStr.split(","));

        this.modifyUserRoles(params.getId(), roleIds);
        // 数据权限运营商ID集合
        String org = params.getString("orgIds");
        // 数据权限场站ID集合
        String station = params.getString("stationIds");
        List<Integer> dbOrgIds = Lists.newArrayList();
        List<Integer> dbStationIds = Lists.newArrayList();
        List<DataVo> datas = userMapper.getUserDataByUserId(userId);
        for (DataVo data1 : datas) {
            int dataType = data1.getInt("dataType");
            if (RoleDataEnum.ORG.dataType == dataType)
                dbOrgIds.add(data1.getInt("dataId"));
            else if (RoleDataEnum.STATION.dataType == dataType)
                dbStationIds.add(data1.getInt("dataId"));
        }
        this.updateUserDatas(userId, org, dbOrgIds, RoleDataEnum.ORG.dataType);
        this.updateUserDatas(userId, station, dbStationIds, RoleDataEnum.STATION.dataType);
    }

    /**
     * 更新用户数据权限
     *
     * @param userId    用户ID
     * @param dataStr   用户编写的数据权限字符串
     * @param dbDataIds 数据库数据权限ID集合
     * @param dataType  数据类型
     */
    public void updateUserDatas(Integer userId, String dataStr, List<Integer> dbDataIds, Integer dataType) throws BizException {
        // 当数据权限集合都为空，这说明当前角色没有
        String dataName = RoleDataEnum.getName(dataType);
        if (StringUtils.isBlank(dataStr)) {
            //删除所有角色关联数据权限集合
            int deleteCount = userMapper.deleteDatasByUserId(userId, null, dataType);
            logger.info("用户编辑后[{}]数据权限为空，删除用户ID为[{}]下的[{}]条[{}]数据权限.", dataName, userId, deleteCount, dataName);
        } else {
            List<Integer> dataIds = Lists.newArrayList();
            //String集合转Integer集合
            collect(Arrays.asList(dataStr.split(",")),
                    new Transformer() {
                        @Override
                        public Object transform(Object input) {
                            return Integer.valueOf((String) input);
                        }
                    }, dataIds);
            //和数据库旧权限比较添加需要新增的
            Set<Integer> addDatas = Sets.newHashSet();
            for (Integer permId : dataIds) {
                if (permId != null && !dbDataIds.contains(permId)) {
                    addDatas.add(permId);
                }
            }
            //定义可删除权限集合
            Set<Integer> deleteDatas = Sets.newHashSet();
            for (Integer id : dbDataIds) {
                if (id != null && !dataIds.contains(id)) {
                    deleteDatas.add(id);
                }
            }
            // 删除当前角色下编辑后没有的权限
            if (deleteDatas.size() > 0) {
                int deleteCount = userMapper.deleteDatasByUserId(userId, deleteDatas, dataType);
                logger.info("编辑后用户数据权限不为空，删除用户ID[{}]下[{}]条[{}]数据权限.", userId, deleteCount, dataName);
            }

            if (addDatas.size() > 0) {
                int addCount = userMapper.insertUserDataByUserId(userId, addDatas, dataType);
                logger.info("编辑后用户ID[{}]新增[{}]条[{}]数据权限.", userId, addCount, dataName);
            }
        }
    }

    /**
     * 监测用户是否存在，存在直接抛出异常
     *
     * @param id
     * @param loginName
     * @throws BizException
     */
    private void loginNameExist(Integer id, String loginName) throws BizException {
        CommonUtils.valIsEmpty(loginName, "用户名");
        Integer count = userMapper.getUserCountByName(id, loginName);
        if (count > 0)
            throw new BizException(1000016, "当前用户名");
    }

    /**
     * 验证用户名密码
     *
     * @return
     */
    public DataVo checkUser(String loginName, String password) throws BizException {
        List<DataVo> users = userMapper.getUserByName(loginName);
        if (CollectionUtils.isEmpty(users))
            throw new BizException(1000015, "当前登录用户");
        DataVo user = users.get(0);
        String enSalt = user.getString("salt");//得到加密后的盐
        String rawSalt = UserHelper.decodeSalt(enSalt);//将盐解密

        String enPassword = user.getString("password");//获取数据库加密后的密码

        boolean isOk = UserHelper.validatePassword(password, rawSalt, enPassword);
        if (isOk) {
            /**
             * 移除用户密码和加密盐
             */
            user.remove("salt");
            user.remove("password");
            return user;
        } else {
            return null;
        }

    }

    /**
     * 用户登录
     *
     * @param loginName 账号
     * @param passWord  密码
     * @return
     * @throws BizException
     */
    @Transactional(noRollbackFor = Exception.class)
    public DataVo login(String loginName, String passWord) throws BizException {
        //根据用户名查询用户
        List<DataVo> users = userMapper.getUserByName(loginName);
        if (CollectionUtils.isEmpty(users))
            throw new BizException(1000015, "当前登录用户");
        if (users.size() > 1)
            throw new BizException(1000011, "当前登录用户");
        //当前登录用户
        DataVo loginUser = null;//userLogin(loginName, passWord, users.get(0));

        // 添加系统日志
        saveLog("用户登录", OperateType.longin.OperateId, loginName + "登录系统", loginUser.getId());
        return loginUser;
    }

    /**
     * 用户JWT登录
     *
     * @param loginName 账号
     * @param passWord  密码
     * @return
     * @throws BizException
     */
    @Transactional(noRollbackFor = Exception.class)
    public JwtUser jwtLogin(String loginName, String passWord) throws BizException {
        //当前登录用户
        JwtUser loginUser = userLogin(loginName, passWord);

        // 用户请求token授权后把用户信息缓存，缓存时间是token失效时间
        cacheService.set(RedisKeyEnum.JWT_USER_KEY.value + loginName, loginUser, expiration);
        // 添加系统日志
        saveLog("用户登录", OperateType.longin.OperateId, loginName + "登录系统", loginUser.getId());

        //前端不需要，但是API认证需要，所以在存入缓存库之后设置为空
        loginUser.setAuthorities(null);
        return loginUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<DataVo> users = userMapper.getUserByName(username);//根据用户名查询用户
        if (CollectionUtils.isEmpty(users)) {
            throw new UsernameNotFoundException(ErrorUtils.getErrorMsg(1000015, "当前登录用户"));
        } else if (users.size() > 1) {
            throw new UsernameNotFoundException(ErrorUtils.getErrorMsg(1000011, "当前登录用户"));
        } else {
            try {
                DataVo user = users.get(0);
                Integer userId = user.getInt("id");
                List roles = findUserRoleNames(userId);
                List<DataVo> tempPerms = userMapper.getPermissionsByUserId(userId, null);
                List<String> perms = Lists.newArrayList();
                for (DataVo perm : tempPerms) {
                    if(!UserHelper.filterPerms.contains(perm.getId()))
                        perms.add(perm.getString("permission"));
                }
				Set<Integer> orgs = this.getUserRoleDataById(userId, RoleDataEnum.ORG.dataType);
				if (orgs == null) {// 超级管理员或者多企业管理员
					perms.add("monitor:overview:buildview");
					perms.add("monitor:overview:businessanalysis");
					perms.add("monitor:overview:dincomeanalysis");
				} else if (orgs.size() == 1) {//单企业用户
					perms.add("monitor:overview:businessanalysis");
					perms.add("monitor:overview:sincomeanalysis");
				} else if (orgs.size() > 1) {// 多企业用户
					perms.add("monitor:overview:businessanalysis");
					perms.add("monitor:overview:dincomeanalysis");
				} else {// 不存在或者为普通管理员
				    // 建设总览:  monitor:overview:buildview
				    // 经营分析:  monitor:overview:businessanalysis
				    // 单企业收入分析:  monitor:overview:sincomeanalysis
				    // 多企业收入分析:  monitor:overview:dincomeanalysis
				}
                user.put("roles", roles);
                user.put("perms", perms);
                JwtUser jwtUser = JwtUserFactory.build(user);
                md5Salt.setSalt(jwtUser.getSalt());//设置加密盐
                return jwtUser;
            } catch (BizException e) {
                logger.error("", e);
                throw new UsernameNotFoundException(e.getMessage());
            }
        }
    }

    /**
     * 过滤用户权限组成菜单
     * @param treeNodes
     * @return
     * @throws BizException
     */
    private List<TreeNode> filterPermissions(List<TreeNode> treeNodes) throws BizException {
        CollectionUtils.filter(treeNodes, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return !UserHelper.filterPerms.contains(((TreeNode)o).getId());
            }
        });
        Tree permTree = new Tree();
        List<TreeNode> list = permTree.list(treeNodes);
        CollectionUtils.filter(list, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return ((TreeNode) o).getLevel() == 1;
            }
        });
        return list;
    }

    /**
     * 刷新token
     *
     * @param oldToken
     * @return
     */
    public String refresh(String oldToken) throws BizException {
        if (StringUtils.isBlank(oldToken))
            throw new BizException(1);
        final String token = oldToken.substring(tokenHead.length());
        final Claims claims = jwtTokenUtil.getClaimsFromToken(token);
        if (claims == null)
            throw new BizException(1005001);
        final Date expiration1 = claims.getExpiration();
        // 距离失效时间差(分钟)
        int diffTime = CalendarUtils.compareTime(CalendarUtils.getCalendar(expiration1), Calendar.getInstance(), Calendar.MINUTE);
        // 在失效时间前十分之一允许刷新
        if (diffTime >= expiration / 60 / 10)
            throw new BizException(1005000);
        String username = claims.getSubject();
        JwtUser user = (JwtUser) this.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }


    /**
     * 用户登录，加验证
     *
     * @param passWord 用户输入的密码
     * @return
     * @throws BizException
     */
    private JwtUser userLogin(String loginName, String passWord) throws BizException {

        JwtUser user = (JwtUser) loadUserByUsername(loginName);
        // 2017-07-29 15:18修改
        List<TreeNode> treeNodes = this.findUserPerms(user.getId(), null);
        user.setMenus(this.filterPermissions(treeNodes));
        String first = "";
        List<TreeNode> tempTreeNodes;
        lebel1:
        for (TreeNode treeNode : treeNodes) {
            tempTreeNodes = treeNode.getChildren();
            if (CollectionUtils.isNotEmpty(tempTreeNodes)) {
                for (TreeNode tempTreeNode : tempTreeNodes) {
                    if (tempTreeNode.getLevel() == 2 && StringUtils.isNotBlank(tempTreeNode.getHref())) {
                        first = tempTreeNode.getHref();
                        break lebel1;
                    }

                }
            }
        }
        user.setFirst(first);
        int userId = user.getId();
        String key = RedisKeyEnum.LOCK_USER_KEY.value + userId;
        //获取登录锁
        LoginLock lock = cacheService.get(key);
        String userState = user.getUserState();
        // 当用户被锁定时且锁信息已经失效则解锁用户
        if (UserHelper.USER_STATE_LOCKED.equals(userState)) {
            if (lock != null) {
                // 获取最后一次输出密码的时间到现在的分钟时间差
                int lockTime = CalendarUtils.compareTime(Calendar.getInstance(), lock.getLastFailTime(), Calendar.MINUTE);
                //判断用户是否符合解锁条件
                if (lockTime >= UserHelper.waitMins) {
                    //解锁用户
                    this.updateUser(userId, UserHelper.USER_STATE_ENABLED);
                    //清除登录锁信息
                    cacheService.remove(key);
                    lock = null;// 锁置空
                } else {
                    throw new BizException(1000002, worongCont, (waitMins - lockTime));
                }
            } else {
                //解锁用户
                this.updateUser(userId, UserHelper.USER_STATE_ENABLED);
            }
        }
        //---当用户被禁用或删除时
        if (userState.equals(UserHelper.USER_STATE_DISABLED))
            throw new BizException(1000004, "用户被禁用");
        if (userState.equals(UserHelper.USER_STATE_REMOVED))
            throw new BizException(1000004, "用户被删除");

        String enSalt = user.getSalt();//得到加密后的盐
        String rawSalt = UserHelper.decodeSalt(enSalt);//将盐解密
        String enPassword = user.getPassword();//获取数据库加密后的密码
        boolean isOk = UserHelper.validatePassword(passWord, rawSalt, enPassword);

        //确认用户密码
        if (!isOk) {
            //将登陆后的信息传递给SpringSec（没效果）
            //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginName, null, null);
            //authentication.setAuthenticated(false);
            //SecurityContextHolder.getContext().setAuthentication(authentication);
            // 判断登录锁是否为空
            if (lock == null) {
                //新建一个锁
                lock = newLock(userId);
            } else {
                Calendar now = Calendar.getInstance();
                //15分钟内连续登陆错误才累计错误次数，否则重新计算
                if (CalendarUtils.compareTime(now, lock.getFirstFailTime(), Calendar.MINUTE) < UserHelper.waitMins) {
                    lock.setFailTimes(lock.getFailTimes() + 1);
                    lock.setLastFailTime(now);//上次失败时间
                } else {
                    // 15分钟之后的重新开始计算
                    lock.setFirstFailTime(now);
                    lock.setFailTimes(1);
                    lock.setLastFailTime(now);
                }
                //如果用户连续5次登录失败,锁定用户
                if (lock.getFailTimes() >= 5) {
                    //更改数据库状态,锁住
                    this.updateUser(userId, UserHelper.USER_STATE_LOCKED);
                    // 锁住之后要把最后一次错误时间更新到缓存库
                    cacheService.set(key, lock);
                    throw new BizException(1000005, UserHelper.worongCont, UserHelper.waitMins);
                }
            }
            //设置登录锁到Redis
            cacheService.set(key, lock);
            throw new BizException(1000001, UserHelper.worongCont);
        } else {
            //将登陆后的信息传递给SpringSec(没效果)
            //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            //SecurityContextHolder.getContext().setAuthentication(authentication);
            //清除Redis中的登录锁信息
            if (cacheService.exists(key))
                cacheService.remove(key);
        }
        return user;
    }

    /**
     * 依赖于SpringSec 进行用户名密码验证
     *
     * @param loginName
     * @param passWord
     * @return
     */
    public Authentication validatePassword(String loginName, String passWord) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(loginName, passWord);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


    /**
     * 更新用户账号状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @throws BizException
     * @description 私有化, 不走事物
     */
    private void updateUser(int userId, String status) throws BizException {
        Map data = Maps.newHashMap();
        data.put("id", userId);
        data.put("userState", status);
        userMapper.updateUserById(data);
    }

    /**
     * 用户初次登录,新建登录锁
     */
    public static LoginLock newLock(int userId) {
        LoginLock lock = new LoginLock();
        lock.setUserId(userId);
        lock.setFailTimes(1);
        lock.setFirstFailTime(Calendar.getInstance());
        lock.setLastFailTime(Calendar.getInstance());
        return lock;
    }

    /**
     * 删除多个用户
     *
     * @param userIds
     * @return
     * @throws BizException
     */
    public Integer deleteUsersById(Integer userId, List userIds) throws BizException {
        if (userId == null)
            throw new BizException(1000006);
        if (CollectionUtils.isEmpty(userIds))
            throw new BizException(1001001);
        // 用户置无效
        Integer delUser = userMapper.deleteUsersById(userIds,UserHelper.USER_STATE_REMOVED);
        //Integer delUserRole = userMapper.deleteUserRolesByUserIds(userIds);
        //logger.info("删除[{}]个用户同时删除[{}]条用户对应角色.", delUser, delUserRole);
        // 删除用户对应数据权限关联
        //Integer delData = userMapper.deleteUserDataByUserIds(userIds);
        //logger.info("删除[{}]个用户同时删除[{}]条用户对应数据权限.", delUser, delData);
        //saveLog("删除用户", OperateType.del.OperateId, String.format("用户ID为[%s]的用户无效%s个用户，ID为：%s，同时删除用户对应的%s条角色，%s条数据权限.", userId, userIds.size(), userIds.toString(), delUserRole, delData), userId);
        saveLog("无效用户", OperateType.del.OperateId, String.format("用户ID为[%s]的用户无效%s个用户，ID为：%s.", userId, userIds.size(), userIds.toString()), userId);
        return delUser;
    }

    /**
     * 查询用户拥有的角色集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    public List findUserRoles(Integer userId) throws BizException {
        List roles = userMapper.getRolesByUserId(userId);
        return roles;
    }

    public List findUserRoleNames(Integer userId) throws BizException {
        List roles = userMapper.getRoleNamesByUserId(userId);
        return roles;
    }

    /**
     * 修改用户关联的角色
     *
     * @param userId
     * @param roleIds
     * @return
     * @throws BizException
     */
    public void modifyUserRoles(Integer userId, List roleIds) throws BizException {

        if (CollectionUtils.isEmpty(roleIds)) {
            //删除所有角色关联
            int deleteCount = userMapper.deleteRolesByUserId(userId, null);
            logger.info("删除用户ID为[{}]下的[{}]条关联角色.", userId, deleteCount);
        } else {
            //查询用户的角色关联
            List<Integer> dbRoleIds = userMapper.getRoleIdsByUserId(userId);
            // Integer类型角色ID集合
            List<Integer> iRoleIds = Lists.newArrayList();
           CollectionUtils.collect(roleIds, new Transformer() {
                @Override
                public Object transform(Object o) {
                    return Integer.valueOf((String) o);
                }
            }, iRoleIds);
            //需要新增的
            List addRoles = new ArrayList();
            for (Integer id : iRoleIds) {
                if (id != null && !dbRoleIds.contains(id)) {
                    addRoles.add(id);
                }
            }
            if (addRoles.size() > 0) {
                int addCount = userMapper.insertRolesByUserId(userId, addRoles);
                logger.info("用户ID为[{}]用户添加[{}]条角色关联.", userId, addCount);
            }

            //需要删除的
            List<Integer> deleteRoles = Lists.newArrayList();
            for (Integer id : dbRoleIds) {
                if (id != null && !iRoleIds.contains(id)) {
                    deleteRoles.add(id);
                }
            }
            if (deleteRoles.size() > 0) {
                int deleteCount = userMapper.deleteRolesByUserId(userId, deleteRoles);
                logger.info("用户ID为[{}]下删除[{}]条角色关联.", userId, deleteRoles, deleteCount);
            }

        }
    }


    /**
     * 查询用户拥有的权限集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    public List<TreeNode> findUserPerms(Integer userId, List<Integer> permTypes) throws BizException {
        List<TreeNode> perms = userMapper.getPermsByUserId(userId, permTypes);
        return perms;
    }

    public List findUserPermNames(Integer userId, Integer permType) throws BizException {
        List perms = userMapper.getPermNamesByUserId(userId, permType);
        return perms;
    }

    /**
     * 根据用户Ids查询用户s
     *
     * @param map
     * @return
     * @throws BizException 2017年4月11日
     *                      gaohui
     */
    public List<Map> getUsersByIds(Map map) throws BizException {
        return userMapper.getUsersByIds(map);
    }

    /**
     * 修改个人信息
     *
     * @param data
     * @throws BizException
     */
    public void modifyPersonalInfo(Map data) throws BizException {
        DataVo dataVo = new DataVo(data);
        String loginName = dataVo.getString("loginName");
        String oldPassword = dataVo.getString("oldPassword");
        String phone = dataVo.getString("phone");
        String email = dataVo.getString("email");
        int id = dataVo.getId();
        CommonUtils.idIsEmpty(id, "更新数据主键ID");
        loginNameExist(id, loginName);
        // 如果用户名或者密码为空
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(oldPassword))
            throw new BizException(1000000);
        // 手机号码正则验证
        if (StringUtils.isNotBlank(phone) && !ValidateUtils.Mobile(phone))
            throw new BizException(1000014, "手机号码");

        // 邮箱正则验证
        if (StringUtils.isNotBlank(email) && !ValidateUtils.Email(email))
            throw new BizException(1000014, "邮箱");

        // 判断新密码是否相同
        String newpassword1 = dataVo.getString("newpassword1");
        if (!newpassword1.equals(dataVo.getString("newpassword2"))) {
            throw new BizException(1000010);
        }

        // 获取数据库数据
        Map map = userMapper.getUserById(id);
        DataVo user = new DataVo(map);
        String enSalt = user.getString("salt");//得到加密后的盐
        String decodeSalt = UserHelper.decodeSalt(enSalt);//将盐解密

        String password = user.getString("password");//获取数据库加密后的密码

        // 判断用户输入原密码是否和数据库密码相同
        if (!UserHelper.validatePassword(oldPassword, decodeSalt, password))
            throw new BizException(1000004, "原密码错误!");

        // 新密码构建
        if (StringUtils.isNotBlank(newpassword1)) {
            String rawSalt = UserHelper.buildRawSalt(dataVo);//构建原始盐
            String enPassword = UserHelper.encoderPassword(newpassword1, rawSalt);//MD5盐加密
            String encodeSalt = UserHelper.encodeSalt(rawSalt);//加密盐
            dataVo.put("salt", encodeSalt);
            dataVo.put("password", enPassword);
        }
        userMapper.updateUserById(dataVo);
    }

    /**
     * 根据登录用户ID获取可查看运营商场站权限
     *
     * @param userId
     * @param orgName
     * @param stationName
     * @return
     * @description 旧功能平移，有stationId的时候不考虑运营商的查询
     */
    public List<TreeNode> queryOrgStationByUserId(Integer userId, String orgName, String stationName) throws BizException {
        if (userId == null)
            throw new BizException(1000006);
        List<TreeNode> orgs = Lists.newArrayList();
        List<TreeNode> stations = Lists.newArrayList();
        // 如果搜索条件中有场站名称，则以这个为首要查询条件
        if (StringUtils.isNotBlank(stationName)) {
            stations = userMapper.getStationNameByName(stationName);
            if (CollectionUtils.isNotEmpty(stations)) {
                // 根据用户ID获取场站数据权限
                final Set<Integer> stationIds = this.getUserStationByUserId(userId);
                if(CollectionUtils.isNotEmpty(stationIds))
                    //过滤掉不具备的数据权限
                    filter(stations, stationIds);
                orgs = userMapper.getOrgNameById(Sets.<Integer>newHashSet(CollectionUtils.collect(stations, new Transformer() {
                    @Override
                    public Object transform(Object o) {
                        return ((TreeNode) o).getPid();
                    }
                })));
                if (CollectionUtils.isNotEmpty(orgs))
                    orgs.addAll(stations);
            }
        } else if (StringUtils.isBlank(stationName) && StringUtils.isNotBlank(orgName)) {
            // 根据运营商名称作为查询条件获取运营商
            orgs = userMapper.getOrgNameByName(orgName);
            if (CollectionUtils.isNotEmpty(orgs)) {
                // 根据用户ID获取运营商数据权限
               final Set<Integer> orgIds = this.getUserOrgByUserId(userId);
                // 系统管理员和全局管理员是返回null
                if (CollectionUtils.isNotEmpty(orgIds))
                    // 根据名称查询出来的不包含的数据根据权限数据过滤掉
                    filter(orgs, orgIds);
                Set<Integer> tempOrgIds = Sets.newHashSet();
                CollectionUtils.collect(orgs, new Transformer() {
                    @Override
                    public Object transform(Object o) {
                        return ((TreeNode) o).getId();
                    }
                }, tempOrgIds);
                stations = userMapper.getStationNameByOrgId(tempOrgIds);
                if (CollectionUtils.isNotEmpty(stations)) {
                    // 过滤场站数据权限
                    final Set<Integer> stationIds = this.getUserStationByUserId(userId);
                    if(CollectionUtils.isNotEmpty(stationIds))
                        this.filter(stations, stationIds);
                    orgs.addAll(stations);
                }
            }
        } else {// 如果查询条件都为空，则根据用户运营商数据权限和场站数据权限获取数据
            Set<Integer> orgIds = this.getUserOrgByUserId(userId);
            Set<Integer> stationIds = this.getUserStationByUserId(userId);
            orgs = userMapper.getOrgNameById(orgIds);
            stations = userMapper.getStationNameById(stationIds);
            Set<Integer> tempOrgIds = Sets.newHashSet();
            CollectionUtils.collect(orgs, new Transformer() {
                @Override
                public Object transform(Object o) {
                    return ((TreeNode) o).getId();
                }
            }, tempOrgIds);
            // 过滤掉不存在运营商权限的场站
            for (TreeNode station : stations) {
                if (tempOrgIds.contains(station.getPid())) {
                    orgs.add(station);
                }
            }
        }
        return new Tree().list2(orgs);
    }

    /**
     * 过滤用户的数据权限，将不属于的查询数据过滤
     * @param nodes
     * @param dataIds
     */
    private void filter(List<TreeNode> nodes, final Set<Integer> dataIds) {
        CollectionUtils.filter(nodes, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return dataIds.contains(((TreeNode)o).getId());
            }
        });
    }

    /**
     * 根据用户ID回显数据权限
     *
     * @param userId
     * @return
     * @description 旧功能平移
     */
    public List<TreeNode> queryOrgStationByUserId(Integer userId) throws BizException {
        if (userId == null)
            throw new BizException(1000006);
        // 必须有这个数据权限才可以获取数据
        List<Integer> roles = userMapper.getRoleTypeByUserId(userId);
        //Set<Integer> orgIds = this.getUserEchoOrgByUserId(roles, userId);
        Set<Integer> orgIds = Sets.newHashSet();
        List<Integer> dataIds = userMapper.getUserDataIdsByUserId(userId, RoleDataEnum.ORG.dataType);
        if (CollectionUtils.isEmpty(dataIds)) {
            orgIds.add(-999);
        }
        orgIds.addAll(dataIds);
        if (isOrgMan(roles)) { //企业管理员
            orgIds.addAll(coopOperMapper.getCoopOperOrgIds(dataIds));
            orgIds.addAll(this.getAllChildOrg(orgIds));
        }
        List<TreeNode> orgs = userMapper.getOrgNameById(orgIds);
        orgIds.removeAll(dataIds);
        Set<Integer> stationIds = this.getUserEchoStationByUserId(roles, userId, Lists.newArrayList(orgIds));
        if (CollectionUtils.isEmpty(orgs))
            return null;
        List<TreeNode> stations = userMapper.getStationNameById(stationIds);
        Set<Integer> tempOrgIds = Sets.newHashSet();
        CollectionUtils.collect(orgs, new Transformer() {
            @Override
            public Object transform(Object o) {
                return ((TreeNode) o).getId();
            }
        }, tempOrgIds);
        // 过滤掉不存在运营商权限的场站
        for (TreeNode station : stations) {
            if (tempOrgIds.contains(station.getPid())) {
                orgs.add(station);
            }
        }
        return new Tree().list2(orgs);
    }

    /**
     * 根据登录用户获取orgId
     *
     * @param userId
     * @return orgId
     * @throws BizException
     */
    public Integer getOrgIdByUserId(Integer userId) throws BizException {
        Map user = this.findUserById(userId);
        if (user == null || user.isEmpty())
            throw new BizException(1000015, "当前用户");
        DataVo params = new DataVo(user);
        if (params.isBlank("orgId"))
            return null;
        return params.getInt("orgId");
    }

    /**
     * 根据登录用户获取orgName
     *
     * @param userId
     * @return orgName
     * @throws BizException
     */
    public String getOrgNameByUserId(Integer userId) throws BizException {
        Map user = this.findUserById(userId);
        if (user == null || user.isEmpty())
            throw new BizException(1000015, "当前用户");
        DataVo params = new DataVo(user);
        return params.getString("orgName");
    }

    /**
     * 根据登录用户获取orgCode
     *
     * @param userId
     * @return orgCode
     * @throws BizException
     */
    public String getOrgCodeByUserId(Integer userId) throws BizException {
        DataVo user = this.findUserById(userId);
        if (user == null || user.isEmpty())
            throw new BizException(1000015, "当前用户");
        return user.getString("orgCode");
    }

    /**
     * 根据用户ID获取用户角色集合，当前功能平移只有单角色
     *
     * @param userId
     * @return
     */
    public List<DataVo> getUserRoleById(Integer userId) throws BizException {
        CommonUtils.idIsEmpty(userId, "用户ID");
        return userMapper.getRolesByUserId(userId);
    }

    /**
     * 用户新增数据权限
     *
     * @param userId   用户ID
     * @param dataIds  数据ID集合
     * @param dataType 数据类型
     */
    public void insertRoleDataByUserId(Integer userId, Set<Integer> dataIds, Integer dataType) throws BizException {
        // 根据现有规则，应该只有一个角色
        if (userId == null || userId == 0)
            throw new BizException(1000012, "用户ID");
        if (dataType == null)
            throw new BizException(1000012, "数据类型");
        if (CollectionUtils.isNotEmpty(dataIds)) {
            int addCount = userMapper.insertUserDataByUserId(userId, dataIds, dataType);
            logger.info("用户ID[{}]下新增[{}]条[{}]数据权限.", userId, addCount, RoleDataEnum.getName(dataType));
        }

    }


    /**
     * 用户业务下拉字典
     */
    public PageInfo getUsersSelect(Map map) throws BizException {
        DataVo vo = new DataVo(map);
        if (vo.isBlank("pageNum")) {
            vo.put("pageNum", 1);
        }
        if (vo.isBlank("pageSize")) {
            vo.put("pageSize", 20);
        }
        Set<Integer> orgIds = getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
        if(orgIds != null){
        	vo.put("orgIds", orgIds);
        }
        PageHelper.startPage(vo);
        List list = userMapper.getUsersSelect(vo);
        return new PageInfo(list);
    }

    /**
     * 用户退出登录
     *
     * @param data
     * @throws BizException
     */
    public void loginOut(Map data) throws BizException {
        DataVo params = new DataVo(data);
        int userId = params.getInt("userId");
        String loginName = params.getString("loginName");
        CommonUtils.idIsEmpty(userId, "用户ID");
        CommonUtils.valIsEmpty(loginName, "用户名");
        saveLog("用户退出登录", OperateType.longinOut.OperateId, loginName + "退出系统", userId);
    }

    /**
     * 根据用户ID获取所有角色ID集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    public List<Integer> getRoleIdsByUserId(Integer userId) throws BizException {
        return userMapper.getRoleIdsByUserId(userId);
    }

    /**
     * 通过传入用户Id判断是否为企业管理员
     *
     * @param userId
     * @return
     */
    public boolean isOrgMan(Integer userId) throws BizException {
        List<Integer> roles = userMapper.getRoleTypeByUserId(userId);
        return roles != null && roles.contains(RoleHelper.ORG_ADMIN);
    }

    /**
     * 判断用户是否为超级用户
     *
     * @param userId
     * @return
     */
    public boolean isSuperMan(Integer userId) throws BizException {
        List<Integer> roles = userMapper.getRoleTypeByUserId(userId);
        return roles != null && roles.contains(RoleHelper.ONE_ADMIN);
    }

    /**
     * 判断传入用户是否为全局用户
     *
     * @param userId
     * @return
     */
	public boolean isGlobalMan(Integer userId) throws BizException {
		List<Integer> roles = userMapper.getRoleTypeByUserId(userId);
        if(CollectionUtils.isNotEmpty(roles)){
            for (Integer role : roles) {
                if(role==RoleHelper.GLOBAL_ADMIN || role==RoleHelper.TWO_ADMIN)
                    return true;
            }
        }
        return false;
	}

    /**
     * 判断当前角色集合中是否为企业管理员
     *
     * @param roles
     * @return
     */
    public boolean isOrgMan(List<Integer> roles) throws BizException {
        if(CollectionUtils.isEmpty(roles))
            return false;
        return roles.contains(RoleHelper.ORG_ADMIN);
    }

    /**
     * 判断当前角色集合中是否为超级用户
     *
     * @param roles
     * @return
     */
    public boolean isSuperMan(List<Integer> roles) throws BizException {
        if(CollectionUtils.isEmpty(roles))
            return false;
       return roles.contains(RoleHelper.ONE_ADMIN);
    }

    /**
     * 判断当前角色集合中是否为全局用户
     *
     * @param roles
     * @return
     */
    public boolean isGlobalMan(List<Integer> roles) throws BizException {
        if (CollectionUtils.isNotEmpty(roles)) {
            for (Integer role : roles) {
                if (role == RoleHelper.GLOBAL_ADMIN || role == RoleHelper.TWO_ADMIN)
                    return true;
            }
        }
        return false;
    }

    /**
     * 根据登录用户ID和数据类型获取数据权限资源ID集合，枚举数据类型:{@link com.clouyun.charge.common.constant.RoleDataEnum}
     * <br>
     * 超级管理员返回null，普通用户没有数据权限返回[-999]
     *
     * @param userId   用户ID
     * @param dataType 数据类型
     * @return
     * @throws BizException
     * @see com.clouyun.charge.common.constant.RoleDataEnum
     */
    public Set<Integer> getUserRoleDataById(Integer userId, Integer dataType) throws BizException {
        // dataType可以为0
        if (userId == null)
            throw new BizException(1000012, "用户ID");
        // dataType可以为0
        if (dataType == null)
            throw new BizException(1000012, "数据类型");
        if (dataType == RoleDataEnum.ORG.dataType)
            return this.getUserOrgByUserId(userId);
        else if (dataType == RoleDataEnum.STATION.dataType)
            return this.getUserStationByUserId(userId);
        return Sets.newHashSet(-999);
    }

    /**
     * 根据登录用户ID和数据类型获取数据权限资源ID集合，枚举数据类型:{@link com.clouyun.charge.common.constant.RoleDataEnum}
     * <br>
     * 超级管理员返回null，普通用户没有数据权限返回[-999]
     *
     * @param userId   用户ID
     * @param dataType 数据类型
     * @return
     * @throws BizException
     * @see com.clouyun.charge.common.constant.RoleDataEnum
     */
    public Set<Integer> getUserRoleDataByIdCoopRev(Integer userId, Integer dataType) throws BizException {
        // dataType可以为0
        if (userId == null)
            throw new BizException(1000012, "用户ID");
        // dataType可以为0
        if (dataType == null)
            throw new BizException(1000012, "数据类型");
        if (dataType == RoleDataEnum.ORG.dataType)
            return this.getUserOrgByUserIdCoopRev(userId);
        else if (dataType == RoleDataEnum.STATION.dataType)
            return this.getUserStationByUserIdCoopRev(userId);
        return Sets.newHashSet(-999);
    }


    /**
     * 获取用户可查看企业ID集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    private Set<Integer> getUserOrgByUserId(Integer userId) throws BizException {
        long start = System.currentTimeMillis();
        // 查询出用户所拥有的角色
        List<Integer> roles = this.getRoleTypeByUserId(userId);
        if (isSuperMan(roles) || isGlobalMan(roles)) { //全局管理员或系统管理员
            return null;
        }
        Set<Integer> orgIds = Sets.newHashSet();
        List<Integer> dataIds = userMapper.getUserDataIdsByUserId(userId, RoleDataEnum.ORG.dataType);
        if (CollectionUtils.isEmpty(dataIds)) {
            orgIds.add(-999);
            logger.info("用户[{}]获取[运营商]数据权限耗时：{}ms.", userId, System.currentTimeMillis() - start);
            return orgIds;
        }
        orgIds.addAll(dataIds);
        if (isOrgMan(roles)) { //企业管理员
            orgIds.addAll(coopOperMapper.getCoopOperOrgIds(dataIds));
            orgIds.addAll(this.getAllChildOrg(orgIds));
        }
        logger.info("用户[{}]获取[运营商]数据权限耗时：{}ms.", userId, System.currentTimeMillis() - start);
        return orgIds;
    }

    public List<Integer> getRoleTypeByUserId(Integer userId) throws BizException{
        return userMapper.getRoleTypeByUserId(userId);
    }

    /**
     * 获取用户可查看企业ID集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    private Set<Integer> getUserOrgByUserIdCoopRev(Integer userId) throws BizException {
        long start = System.currentTimeMillis();
        // 查询出用户所拥有的角色等级
        List<Integer> roles = this.getRoleTypeByUserId(userId);
        if (isSuperMan(roles) || isGlobalMan(roles)) { //全局管理员或系统管理员
            return null;
        }
        Set<Integer> orgIds = Sets.newHashSet();
        List<Integer> dataIds = userMapper.getUserDataIdsByUserId(userId, RoleDataEnum.ORG.dataType);
        if (CollectionUtils.isEmpty(dataIds)) {
            orgIds.add(-999);
            logger.info("用户[{}]获取[运营商]数据权限耗时：{}ms.", userId, System.currentTimeMillis() - start);
            return orgIds;
        }
        orgIds.addAll(dataIds);
        if (isOrgMan(roles)) { //企业管理员
            orgIds.addAll(coopOperMapper.getReverseCoopOperOrgIds(dataIds));
            orgIds.addAll(this.getAllChildOrg(orgIds));
        }
        logger.info("用户[{}]获取[运营商]数据权限耗时：{}ms.", userId, System.currentTimeMillis() - start);
        return orgIds;
    }

    /**
     * 根据企业ID集合获取企业的子企业ID集合
     *
     * @param orgIds
     * @return
     * @throws BizException
     */
    private Set<Integer> getAllChildOrg(Set<Integer> orgIds) throws BizException {
        List<DataVo> list = userMapper.getAllOrgs();
        Set<Integer> childs = Sets.newHashSet();
        Children children = new Children(list);
        for (Integer orgId : orgIds) {
            childs.addAll(children.getChildren(orgId, childs));
        }
        return childs;
    }

    /**
     * 获取用户可查询场站ID集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    private Set<Integer> getUserStationByUserId(Integer userId) throws BizException {
        long start = System.currentTimeMillis();
        // 查询出用户所拥有的角色等级
        List<Integer> roles = this.getRoleTypeByUserId(userId);
        Set<Integer> subIds = Sets.newHashSet();
        // 如果是超级管理员或者是全局用户可以查看所有数据
        if (isSuperMan(roles) || isGlobalMan(roles)) {
            return null;
        } else if (isOrgMan(roles)) {
            // 如果是企业管理员可查看用户数据权限以及对应对应子企业旗下的场站
            List<Integer> orgIds = Lists.newArrayList();
            List<Integer> stationIds = Lists.newArrayList();
            List<DataVo> datas = userMapper.getUserDataByUserId(userId);
            for (DataVo data : datas) {
                int dataType = data.getInt("dataType");
                if (RoleDataEnum.ORG.dataType == dataType)
                    orgIds.add(data.getInt("dataId"));
                else if (RoleDataEnum.STATION.dataType == dataType)
                    stationIds.add(data.getInt("dataId"));
            }
            if (CollectionUtils.isNotEmpty(orgIds)) {
                orgIds.addAll(coopOperMapper.getCoopOperOrgIds(orgIds));
                // 用户可查看企业以及合作运营企业的子企业
                orgIds.addAll(this.getAllChildOrg(Sets.newHashSet(orgIds)));
                // 根据可查看企业ID获取场站ID集合
                stationIds.addAll(stationMapper.getStationIdByOrgIds(orgIds));
            }
            subIds.addAll(stationIds);
        } else {
            subIds.addAll(userMapper.getUserDataIdsByUserId(userId, RoleDataEnum.STATION.dataType));
        }
        if (CollectionUtils.isEmpty(subIds))
            subIds.add(-999);
        logger.info("用户[{}]获取[场站]数据权限耗时：{}ms.", userId, System.currentTimeMillis() - start);
        return subIds;
    }
    //
    ///**
    // * 获取用户回显可查看企业ID集合
    // *
    // * @param userId
    // * @return
    // * @throws BizException
    // */
    //private Set<Integer> getUserEchoOrgByUserId(List<Integer> roles, Integer userId) throws BizException {
    //    Set<Integer> orgIds = Sets.newHashSet();
    //    List<Integer> dataIds = userMapper.getUserDataIdsByUserId(userId, RoleDataEnum.ORG.dataType);
    //    if (CollectionUtils.isEmpty(dataIds)) {
    //        orgIds.add(-999);
    //        return orgIds;
    //    }
    //    orgIds.addAll(dataIds);
    //    if (isOrgMan(roles)) { //企业管理员
    //        orgIds.addAll(coopOperMapper.getCoopOperOrgIds(dataIds));
    //        orgIds.addAll(this.getAllChildOrg(orgIds));
    //    }
    //    return orgIds;
    //}

    /**
     * 获取用户回显场站ID集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    private Set<Integer> getUserEchoStationByUserId(List<Integer> roles, Integer userId, List<Integer> orgIds) throws BizException {
        Set<Integer> subIds = Sets.newHashSet();
        List<Integer> stationIds = userMapper.getUserDataIdsByUserId(userId, RoleDataEnum.STATION.dataType);
        subIds.addAll(stationIds);
        // 如果是企业管理员可以看相关企业数据
        if (isOrgMan(roles)) {
            // 如果是企业管理员可查看用户数据权限以及对应对应子企业旗下的场站
            if (CollectionUtils.isNotEmpty(orgIds)) {
                // 根据可查看企业ID获取场站ID集合
                subIds.addAll(stationMapper.getStationIdByOrgIds(orgIds));
            }
        }
        if (CollectionUtils.isEmpty(subIds))
            subIds.add(-999);
        return subIds;
    }

    /**
     * 获取用户可查询场站ID集合
     *
     * @param userId
     * @return
     * @throws BizException
     */
    private Set<Integer> getUserStationByUserIdCoopRev(Integer userId) throws BizException {
        long start = System.currentTimeMillis();
        // 查询出用户所拥有的角色等级
        List<Integer> roles = this.getRoleTypeByUserId(userId);
        Set<Integer> subIds = Sets.newHashSet();
        // 如果是超级管理员或者是全局用户可以查看所有数据
        if (isSuperMan(roles) || isGlobalMan(roles)) {
            return null;
        } else if (isOrgMan(roles)) {
            // 如果是企业管理员可查看用户数据权限以及对应对应子企业旗下的场站
            List<Integer> orgIds = Lists.newArrayList();
            List<Integer> stationIds = Lists.newArrayList();
            List<DataVo> datas = userMapper.getUserDataByUserId(userId);
            for (DataVo data : datas) {
                int dataType = data.getInt("dataType");
                if (RoleDataEnum.ORG.dataType == dataType)
                    orgIds.add(data.getInt("dataId"));
                else if (RoleDataEnum.STATION.dataType == dataType)
                    stationIds.add(data.getInt("dataId"));
            }
            if (CollectionUtils.isNotEmpty(orgIds)) {
                orgIds.addAll(coopOperMapper.getReverseCoopOperOrgIds(orgIds));
                // 用户可查看企业以及合作运营企业的子企业
                orgIds.addAll(this.getAllChildOrg(Sets.newHashSet(orgIds)));
                // 根据可查看企业ID获取场站ID集合
                stationIds.addAll(stationMapper.getStationIdByOrgIds(orgIds));
            }
            subIds.addAll(stationIds);
        } else {
            subIds.addAll(userMapper.getUserDataIdsByUserId(userId, RoleDataEnum.STATION.dataType));
        }
        if (CollectionUtils.isEmpty(subIds))
            subIds.add(-999);
        logger.info("用户[{}]获取[场站]数据权限耗时：{}ms.", userId, System.currentTimeMillis() - start);
        return subIds;
    }

    private static class Children {
        private List<DataVo> list = Lists.newArrayList();

        public Children(List<DataVo> list) {
            this.list = list;
        }

        public Set<Integer> getChildren(Integer orgId, Set<Integer> childs) {
            for (DataVo vo : list) {
                if (orgId != 0 && orgId == vo.getInt("pOrgId")) {
                    childs.add(vo.getInt("orgId"));
                    childs.addAll(getChildren(vo.getInt("orgId"), childs));
                }
            }
            return childs;
        }
    }

    /**
     * 导出平台用户列表
     *
     * @param data
     * @param response
     * @throws Exception
     */
    public void export(Map data, HttpServletResponse response) throws Exception {
        DataVo params = new DataVo(data);

        if (params.isBlank("userId"))
            throw new BizException(1000006);
        data.put("orgIds", getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
        List<DataVo> list = userMapper.getUsersByPage(data);
        for (DataVo vo : list) {
            vo.set("userType", dictService.getDictLabel("yhlx", vo.getString("userType")));
            vo.set("userState", dictService.getDictLabel("xtyhzt", vo.getString("userState")));
        }
        //结果集
        List<String> headList = Lists.newArrayList();
        List<String> valList = Lists.newArrayList();
        headList.add("用户名称");
        headList.add("用户角色");
        headList.add("所属企业");
        headList.add("联系电话");
        headList.add("用户类型");
        headList.add("状态");
        headList.add("住址");
        headList.add("备注");

        valList.add("loginName");
        valList.add("roleName");
        valList.add("orgName");
        valList.add("phone");
        valList.add("userType");
        valList.add("userState");
        valList.add("address");
        valList.add("remark");
        ExportUtils.exportExcel(list, response, headList, valList, "平台用户");
    }


    /**
     * 前端按钮权限对应生成  临时用，可删除
     */
    public void authDoc() {
        List<DataVo> list = userMapper.getAllPerm();
        for (DataVo vo : list) {
            if (vo.getInt("pid") == 0) {
                System.out.println("|- " + vo.getString("name") + " \t" + vo.getString("permission"));
                children(vo.getInt("id"), list, 1);
            }
        }
    }

    /**
     * 系统初始化时获取目标URL地址和对应权限组成权限资源
     */
    public List<DataVo> getAllPermTarget() {
        return userMapper.getAllPermTarget();
    }

    /**
     * 前端按钮权限对应生成  临时用，可删除
     *
     * @param id
     * @param list
     * @param level
     */
    public void children(Integer id, List<DataVo> list, int level) {
        for (DataVo vo : list) {
            if (id == vo.getInt("pid")) {
                for (int i = 0; i < level; i++) {
                    System.out.print("\t");
                }
                System.out.println("|- " + vo.getString("name") + " \t" + vo.getString("permission"));
                children(vo.getInt("id"), list, level + 1);
            }
        }
    }
}
package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.system.mapper.LogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述: 日志管理服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年04月10日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LogService extends BusinessService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DictService dictService;

    private static Logger logger = LoggerFactory.getLogger(LogService.class);

    /**
     * 查找日志列表
     *
     * @param data
     * @return
     * @throws BizException
     */
    public PageInfo findLogs(Map data) throws BizException {
        DataVo params = new DataVo(data);

        CommonUtils.idIsEmpty(params.getInt("userId"), "登录用户");
        if (params.isBlank("orgId")) {
            data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
        }
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(data);
        }
        List logs = logMapper.getLogsByPage(data);
        PageInfo page = new PageInfo(logs);
        return page;
    }

    /**
     * 查找日志详细信息
     *
     * @param logId
     * @return
     */
    public DataVo findLogById(Integer logId) throws BizException {
        DataVo user = logMapper.getLogById(logId);
        return user;
    }

    /**
     * 保存日志
     *
     * @param title    日志标题
     * @param type     操作类型
     * @param desc     日志描述
     */
    public void insertLog(String title, String type,String desc, Integer objectId) {
        DataVo params = new DataVo();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            //UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            //OperatingSystem os = userAgent.getOperatingSystem();
            //System.out.println(os.toString());
            params.put("userAgent", request.getHeader("User-Agent"));
            params.put("remoteAddr", request.getRemoteAddr());
            params.put("title", title);
            params.put("method", request.getMethod());
            params.put("objectId", objectId);
            params.put("type", type);
            params.put("createBy", objectId);
            params.put("desc", desc);
            CommonUtils.idIsEmpty(objectId, "创建者");
            if (StringUtils.isNotBlank(title) && title.length() > 255)
                throw new BizException(1000017, "日志标题", 255);
            if (StringUtils.isNotBlank(type) && type.length() > 32)
                throw new BizException(1000017, "操作类型", 32);
            if (StringUtils.isNotBlank(desc) && desc.length() > 255)
                throw new BizException(1000017, "日志描述", 255);
            // 不判断是否存在用户，不存在直接进异常，不保存该条日志信息
            DataVo user = userService.findUserById(objectId);
            params.set("orgId", user.getInt("orgId"));
            params.set("orgName", user.getString("orgName"));
            params.set("loginName", user.getString("loginName"));
            logMapper.insertLog(params);
        } catch (Exception e) {
            logger.error("业务日志保存失败，失败数据：{}", params, e);
        }
    }

    public void exportLogs(Map data, HttpServletResponse response) throws Exception {
        DataVo params = new DataVo(data);

        CommonUtils.idIsEmpty(params.getInt("userId"), "登录用户");

        if (params.isBlank("orgId")) {
            data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
        }
        List<DataVo> list = logMapper.getLogsByPage(data);
        for (DataVo vo : list) {
            vo.set("type", dictService.getDictLabel("czlx", vo.getString("type")));
        }
        ////结果集
        List<String> headList = Lists.newArrayList();
        List<String> valList = Lists.newArrayList();
        headList.add("用户名");
        headList.add("运营单位");
        headList.add("操作日期");
        headList.add("请求类型");
        headList.add("操作类型");
        headList.add("日志标题");
        headList.add("操作IP地址");
        headList.add("操作日志");
        valList.add("loginName");
        valList.add("orgName");
        valList.add("createDate");
        valList.add("method");
        valList.add("type");
        valList.add("title");
        valList.add("remoteAddr");
        valList.add("desc");
        ExportUtils.exportExcel(list, response, headList, valList, "系统日志");
    }
}

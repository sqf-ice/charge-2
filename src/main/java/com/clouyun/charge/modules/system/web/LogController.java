package com.clouyun.charge.modules.system.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.system.service.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 描述: 日志管理控制器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年04月11日
 */
@RestController
@RequestMapping("/api/logs")
public class LogController extends BusinessController {
    @Autowired
    private LogService logService;

    /**
     * @api {GET} /api/logs 查询日志列表
     * @apiName queryUsers
     * @apiGroup LogController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 查询日志列表，包含分页，根据创建人ID查询
     * <br/>
     * @apiParam {int} pageNum 页码
     * @apiParam {int} pageSize 页大小
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向
     * @apiParam {String} [startDate] 时间段开始时间
     * @apiParam {String} [endDate] 时间段结束时间
     * @apiParam {String} [type] 操作类型
     * @apiParam {String} [title] 日志标题
     * @apiParam {String} [orgName] 运营单位名称
     * @apiParam {String} [desc] 操作日志
     * @apiParam {String} [loginName] 操作用户
     * @apiParam {int} userId 登录人ID
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {Int(11)} data.list.id 日志表主键
     * @apiSuccess {String(1)} data.list.type 操作类型
     * @apiSuccess {String(255)} data.list.title 日志标题
     * @apiSuccess {String(255)} data.list.createBy 创建者
     * @apiSuccess {String(255)} data.list.loginName 用户名
     * @apiSuccess {String(255)} data.list.orgName 运营单位名称
     * @apiSuccess {Date} data.list.createDate 操作日期(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {String(255)} data.list.remoteAddr 操作IP地址
     * @apiSuccess {String(255)} data.list.userAgent 用户代理
     * @apiSuccess {String(255)} data.list.requestUri 请求URI
     * @apiSuccess {String(255)} data.list.method 请求类型
     * @apiSuccess {String} data.list.params 操作提交的数据
     * @apiSuccess {String} data.list.exception 异常信息
     * @apiSuccess {String} data.list.desc 操作日志
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 登录用户ID不能为空!
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResultVo queryLogs(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        PageInfo pageInfo = logService.findLogs(data);
        resultVo.setData(pageInfo);
        return resultVo;
    }

    /**
     * @api {GET} /api/logs/{logId} 查询日志信息详情
     * @apiName queryLogById
     * @apiGroup LogController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 根据日志ID查询日志详情，可以查看，不可更改
     * <br/>
     * @apiParam {int} logId 日志表ID(主键)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 数据封装
     * @apiSuccess {Int(11)} data.id 日志表ID(主键)
     * @apiSuccess {String(32)} data.loginName 用户名
     * @apiSuccess {String(1)} data.type 操作类型 (add:新增  update:更新  login:登录  loginOut:退出  delete：删除)
     * @apiSuccess {String(255)} data.title 对象标题
     * @apiSuccess {String(256)} data.orgName 企业名称
     * @apiSuccess {Date} data.createDate 操作日期(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {String} data.desc 操作日志
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{logId}", method = RequestMethod.GET)
    public ResultVo queryLogById(@PathVariable("logId") Integer logId) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(logService.findLogById(logId));
        return resultVo;
    }

    /**
     * @api {GET} /api/logs/_export 导出日志列表
     * @apiName exportLogs
     * @apiGroup LogController
     * @apiVersion 2.0.0
     * @apiDescription 导出日志列表，根据创建人ID查询
     * <br/>
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向
     * @apiParam {int} userId 登录人ID
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000006 当前的登录用户为空!
     */
    @RequestMapping(value = "/_export", method = RequestMethod.GET)
    public ResultVo exportLogs(@RequestParam Map data, HttpServletResponse response) throws Exception {
        logService.exportLogs(data, response);
        return new ResultVo();
    }
}

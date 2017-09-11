package com.clouyun.charge.modules.system.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.modules.system.service.InfomationService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 描述: 资讯管理控制器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年04月11日
 */
@RestController
@RequestMapping("/api/infomations")
public class InfomationController extends BusinessController {
    @Autowired
    private InfomationService infomationService;

    /**
     * @api {GET} /api/infomations 查询资讯列表
     * @apiName  queryInfomations
     * @apiGroup InfomationController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 查询资讯列表，包含分页
     * <br/>
     * @apiParam {int} pageNum 页码
     * @apiParam {int} pageSize 页大小
     * @apiParam {String} [sort] 排序字段()
     * @apiParam {String} [order] 排序方向(asc:升序||desc:倒序)
     * @apiParam {String} [infoHead] 资讯标题
     * @apiParam {int} [infoType] 资讯类型(0:动态||1:活动)
     * @apiParam {int} [infoStatus] 资讯状态(0:无效||1:成功)
     * @apiParam {Date} [startDate] 资讯创建开始时间(格式类型：yyyy-MM-dd)
     * @apiParam {Date} [endDate] 资讯创建结束时间(格式类型：yyyy-MM-dd)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 分页数据封装
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {Int(9)} data.list.infoId 主键
     * @apiSuccess {String(32)} data.list.infoHead 资讯标题
     * @apiSuccess {String(20480)} data.list.infoBody 资讯正文
     * @apiSuccess {String(256)} data.list.infoPic 缩略图地址
     * @apiSuccess {Date} data.list.infoPublishTime 资讯创建时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Date} data.list.infoExpireDate 资讯有效期(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Int(9)} data.list.infoBeingLiked 点赞次数
     * @apiSuccess {Int(1)} data.list.infoType 资讯类型(0:动态||1:活动)
     * @apiSuccess {Int(1)} data.list.infoStatus 资讯状态(0:无效||1:成功)
     * @apiSuccess {Date} data.list.infoStartTime 活动开始时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Date} data.list.infoEndTime 活动结束时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Int(9)} data.list.infoBeingShared 资讯被分享的次数
     * @apiSuccess {Date} data.list.upTime 置顶时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {String(50)} data.list.infoUrl Html页面链接
     * @apiSuccess {String(128)} data.list.orgName 运营商名称
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResultVo queryInfomations(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        PageInfo pageInfo = infomationService.findInfomations(data);
        resultVo.setData(pageInfo);
        return resultVo;
    }

    /**
     * @api {GET} /api/infomations/{infoId} 查询资讯详情
     * @apiName queryInfomationById
     * @apiGroup InfomationController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 根据资讯ID查询资讯详细信息
     * <br/>
     * @apiParam {int} infoId 资讯ID(主键)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 数据封装
     * @apiSuccess {Int(9)} data.infoId 主键
     * @apiSuccess {String(32)} data.infoHead 资讯标题
     * @apiSuccess {String(20480)} data.infoBody 资讯正文
     * @apiSuccess {String(256)} data.infoPic 缩略图地址
     * @apiSuccess {Date} data.infoPublishTime 资讯创建时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Date} data.infoExpireDate 资讯有效期(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Int(9)} data.infoBeingLiked 点赞次数
     * @apiSuccess {Int(1)} data.infoType 资讯类型(0:动态||1:活动)
     * @apiSuccess {Int(1)} data.infoStatus 资讯状态(0:无效||1:成功)
     * @apiSuccess {Date} data.infoStartTime 活动开始时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Date} data.infoEndTime 活动结束时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Int(9)} data.infoBeingShared 资讯被分享的次数
     * @apiSuccess {Date} data.upTime 置顶时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {String(50)} data.infoUrl Html页面链接
     * @apiSuccess {Int(9)} data.appFrom 运营商ID
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{infoId}", method = RequestMethod.GET)
    public ResultVo queryInfomationById(@PathVariable("infoId") Integer infoId) throws Exception {
        ResultVo resultVo = new ResultVo();
        Map infomation = infomationService.findInfomationById(infoId);
        resultVo.setData(infomation);
        return resultVo;
    }
    /**
     * @api {POST} /api/infomations 保存资讯
     * @apiName saveInfomation
     * @apiGroup InfomationController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 保存资讯信息
     * <br/>
     * @apiParam {String(32)} infoHead 资讯标题
     * @apiParam {String(20480)} [infoBody] 资讯正文
     * @apiParam {Int(1)} [infoType] 资讯类型(0:动态||1:活动)
     * @apiParam {Date} [infoStartTime] 活动开始时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiParam {Date} [infoEndTime] 活动结束时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiParam {Int(9)} [appFrom] 运营商ID
     * <br>
     * @apiParam {String} infoUrl Html页面链接
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 资讯标题不能为空!
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResultVo saveInfomation(MultipartHttpServletRequest multiRequest) throws Exception {
        ResultVo resultVo = new ResultVo();
        MultipartFile file = multiRequest.getFile("file");
        infomationService.insertInfomation(CommonUtils.gerParamterMap(multiRequest.getParameterMap()),file);
        return resultVo;
    }
    /**
     * @api {PUT} /api/infomations 更新资讯
     * @apiName updateInfomation
     * @apiGroup InfomationController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 更新资讯
     * <br>
     * @apiParam {Int(9)} infoId 主键
     * @apiParam {String(32)} infoHead 资讯标题
     * @apiParam {String(20480)} [infoBody] 资讯正文
     * @apiParam {String(256)} [infoPic] 缩略图地址
     * @apiParam {String(50)} [infoUrl] Html页面链接
     * @apiParam {Int(1)} [infoType] 资讯类型(0:动态||1:活动)
     * @apiParam {Date} [infoStartTime] 活动开始时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiParam {Date} [infoEndTime] 活动结束时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiParam {Int(9)} [appFrom] 运营商ID
     * @apiParam {Int(1)} [infoStatus] 资讯状态 0:无效 1:有效 2:置顶 3:取消置顶
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 更新数据主键ID不能为空!
     * @apiError -1000012 资讯标题不能为空!
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResultVo updateInfomation(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        infomationService.updateInfomation(data, null);
        return resultVo;
    }

    /**
     * @api {DELETE} /api/infomations/{infoIds} 删除资讯
     * @apiName  delInfomationById
     * @apiGroup InfomationController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 根据Ids删除资讯
     * <br/>
     * @apiParam {String} infoIds 字典表ID(主键，例：1,2,3)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{infoIds}", method = RequestMethod.DELETE)
    public ResultVo delInfomationByIds(@PathVariable("infoIds") List infoIds) throws Exception {
        ResultVo resuleVo = new ResultVo();
        infomationService.delInfomationByIds(infoIds);
        return resuleVo;
    }

    /**
     * @api {GET} /api/infomations/_export 导出资讯列表
     * @apiName exportInfomations
     * @apiGroup InfomationController
     * @apiVersion 2.0.0
     * @apiDescription 导出资讯列表，提供根据条件查询导出
     * <br/>
     * @apiParam {String} [sort] 排序字段()
     * @apiParam {String} [order] 排序方向(asc:升序||desc:倒序)
     * @apiParam {String} [infoHead] 资讯标题
     * @apiParam {int} [infoType] 资讯类型(0:动态||1:活动)
     * @apiParam {int} [infoStatus] 资讯状态(0:无效||1:成功)
     * @apiParam {Date} [startDate] 资讯创建开始时间(格式类型：yyyy-MM-dd)
     * @apiParam {Date} [endDate] 资讯创建结束时间(格式类型：yyyy-MM-dd)
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000006 当前的登录用户为空!
     */
    @RequestMapping(value = "/_export", method = RequestMethod.GET)
    public ResultVo exportInfomations(@RequestParam Map data, HttpServletResponse response) throws Exception {
        infomationService.exportInfo(data, response);
        return new ResultVo();
    }
}

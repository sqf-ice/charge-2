package com.clouyun.charge.modules.system.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.system.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 区域字典控制器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年05月02日
 */
@RestController
@RequestMapping("/api/areas")
public class AreaController extends BusinessController {

    @Autowired
    private AreaService areaService;
    /**
     * @api {GET} /api/areas/label/{pno}  通用区域字典下拉框
     * @apiName getAreas
     * @apiGroup AreaController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 通用区域字典获取，pno为空时返回省级区域字典，不为空时返回下属区域
     * <br/>
     * @apiParam {String} [pno] 父地区编码
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
        {
        errorCode: 0,
        errorMsg: "操作成功!",
        total: 0,
        data: [
            {
                id: "130000",
                text: "河北省"
            },
            {
                id: "140000",
                text: "山西省"
            }
        ]
        }
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = {"/label", "/label/{pno}"}, method = RequestMethod.GET)
    public ResultVo getAreas(@PathVariable(name = "pno", required = false) String pno) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(areaService.getAreasByNo(pno));
        return resultVo;
    }
}

package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.common.utils.PileTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 描述: 获取InnerId
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年6月15日 下午2:04:39
 */
@RestController
@RequestMapping("/api/piles/utils")
public class OrtModeUtilController extends BusinessController {


    /**
     * @api {GET} /api/piles/utils/_getInnerId   根据枪数和交直模式获取对应的innerId
     * @apiName getInnerId
     * @apiGroup BusiDicts
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据枪数和交直模式获取对应的innerId
     * <br/>
     * @apiParam {int} numberGun 枪数(type=qs)
     * @apiParam {int} ortMode 交直模式(type=jzms)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {int} total     总记录数
     * @apiSuccess {Object[]} data      分页数据对象数组
     * @apiSuccess {String} data.key 	对应的是枪口名称(比如说A枪B枪;Z表示总表)
     * @apiSuccess {Int} data.value 	对应的是枪口的innerId(比如说A枪的innerId是3;B枪的innerId是1)
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     *    {
     *        errorCode: 0,
     *        errorMsg: "操作成功!",
     *        total: 0,
     *        data: {
     *            A: 3,
     *            B: 1
     *        },
     *        exception: null
     *    }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     */
    @RequestMapping(value = "/_getInnerId", method = RequestMethod.GET)
    public ResultVo getInnerId(@RequestParam Map map) throws Exception {
        ResultVo vo = new ResultVo();
        DataVo dataVo = new DataVo(map);
        Map<String, Integer> gunInnerIdMap = PileTypeUtils.getGunInnerId(dataVo.getInt("numberGun"), dataVo.getInt("ortMode"));
        vo.setData(gunInnerIdMap);
        return vo;
    }
}

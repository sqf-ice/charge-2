package com.clouyun.charge.modules.electricity.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.modules.electricity.service.LossModelService;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/lossModels")
public class LossModelController {

    public static final Logger logger = LoggerFactory.getLogger(LossModelController.class);

    @Autowired
    LossModelService lossModelService;

    /**
     * @api {post} /api/lossModels/increased   新增损耗模型
     * @apiName saveLossModel
     * @apiGroup LossModelController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾    根据输入的运营商ID,场站ID,模型编号，模型名称，模型类型等生成损耗模板
     * <br/>
     * @apiParam {Integer}       orgId                         运营商ID
     * @apiParam {Integer}       stationId                     场站ID
     * @apiParam {String}        lmNo                          模型编号
     * @apiParam {String}        lmName                        损耗模型名称
     * @apiParam {Integer}       lmType                        模型类型      0:充电场站  1：充电设施
     * @apiParam {String}        lmCzzb                        场站总表表计
     * @apiParam {String}        lmCdss                        充电设施总表表计
     * @apiParam {String}        lmSjcd                        实际充电计量表计
     * @apiParam {String}        lmQtyd                        其他用电设施表计
     * <br/>
     * @apiSuccess {String}       errorCode                    错误码
     * @apiSuccess {String}       errorMsg                     消息说明
     * @apiSuccess {String}       data                         添加成功返回值("添加损耗模型成功!")
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/increased", method = RequestMethod.POST)
    public ResultVo saveLossModel(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        String Msg = lossModelService.lossModelAdd(data);
        resultVo.setData(Msg);
        return resultVo;
    }

    /**
     * @api {put} /api/lossModels/renewal   编辑损耗模型
     * @apiName updateLossModel
     * @apiGroup LossModelController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾    根据输入的运营商ID, 场站ID, 模型编号，模型名称，模型类型等编辑损耗模板
     * <br/>
     * @apiParam {Integer}       orgId                         运营商ID
     * @apiParam {Integer}       stationId                     场站ID
     * @apiParam {String}        lmNo                          模型编号
     * @apiParam {String}        lmName                        损耗模型名称
     * @apiParam {Integer}       lmType                        模型类型   0:充电场站  1：充电设施
     * @apiParam {String}        lmCzzb                        场站总表表计
     * @apiParam {String}        lmCdss                        充电设施总表表计
     * @apiParam {String}        lmSjcd                        实际充电计量表计
     * @apiParam {String}        lmQtyd                        其他用电设施表计
     * <br/>
     * @apiSuccess {String}      errorCode                     错误码
     * @apiSuccess {String}      errorMsg                      消息说明
     * @apiSuccess {String}      data                          编辑成功返回值("编辑损耗模型成功!")
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/renewal", method = RequestMethod.PUT)
    public ResultVo updateLossModel(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        String Msg = lossModelService.update(data);
        resultVo.setData(Msg);
        return resultVo;
    }

    /**
     * @api {get} /api/lossModels   查询损耗模型列表
     * @apiName selectAllLossModel
     * @apiGroup LossModelController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾   根据输入的运营商ID, 场站ID, 模型类型等条件查询损耗模型列表，可以选择不传，则查询所有
     * <br/>            	
     * @apiParam {Integer}           	orgId                     运营商ID
     * @apiParam {Integer}      		stationId                 场站ID
     * @apiParam {Integer}       		lmType                    模型类型
     * @apiParam {Integer}      		stationId                 场站ID
     * @apiParam {Integer}       		lmType                    模型类型
     * <br/>
     * @apiSuccess {String}       		errorCode                     错误码
     * @apiSuccess {String}       		errorMsg                      消息说明
     * @apiSuccess {Object[]}           data                		      查询成功返回值
     * @apiSuccess {Integer}            data.lmID					      损耗模型ID
     * @apiSuccess {Integer}       		data.orgId                    运营商ID
     * @apiSuccess {Integer}       		data.stationId                场站ID
     * @apiSuccess {String}        		data.lmNo                     模型编号
     * @apiSuccess {String}        		data.lmName                   损耗模型名称
     * @apiSuccess {Integer}       		data.lmType                   模型类型          0:充电场站  1：充电设施
     * @apiSuccess {Date}        		data.createDate               模型生成时间   格式：yyyy-MM-dd HH:mm:ss
     * @apiSuccess {Date}        		data.updateDate               模型修改时间   格式：yyyy-MM-dd HH:mm:ss
     * @apiSuccess {String}        		data.lmCzzb                   场站总表表计
     * @apiSuccess {String}        		data.lmCdss                   充电设施总表表计
     * @apiSuccess {String}        		data.lmSjcd                   实际充电计量表计
     * @apiSuccess {String}        		data.lmQtyd                   其他用电设施表计
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResultVo selectAllLossModel(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        PageInfo Msg = lossModelService.selectAll(data);
        resultVo.setData(Msg);
        return resultVo;
    }

    /**
     * @api {get} /api/lossModels/_count   统计损耗模型数量
     * @apiName modelCount
     * @apiGroup LossModelController
     * @apiVersion 2.0.0
     * @apiDescription 付飞腾   根据输入的运营商ID, 场站ID, 模型类型等条件查询损耗模型列表，可以选择不传，则统计所有
     * <br/>
     * @apiParam {Integer}           	orgId                         运营商ID
     * @apiParam {Integer}      		stationId                     场站ID
     * @apiParam {Integer}       		lmType                        模型类型        0:充电场站  1：充电设施
     * <br/>
     * @apiSuccess {String}       		errorCode                     错误码
     * @apiSuccess {String}       		errorMsg                      消息说明
     * @apiSuccess {Integer}            data                		      返回的模型数量
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/_count", method = RequestMethod.GET)
    public ResultVo modelCount(@RequestBody Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        Integer Msg = lossModelService.modelCount(data);
        resultVo.setData(Msg);
        return resultVo;
    }

}

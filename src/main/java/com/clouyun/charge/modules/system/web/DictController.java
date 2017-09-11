package com.clouyun.charge.modules.system.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.system.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 描述: 字典管理控制器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年04月12日
 */
@RestController
@RequestMapping("/api/dicts")
public class DictController extends BusinessController {

    @Autowired
    private DictService dictService;


    /**
     * @api {GET} /api/dicts  返回所有字典
     * @apiName  findAllDicts
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 返回所有字典，字典类型对应所有字典集合
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 数据封装
     * @apiSuccess {Object[]} data.字典type
     * @apiSuccess {String} data.字典type.id 字典value值
     * @apiSuccess {String} data.字典type.text 字典文本值
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
        {
        errorCode: 0,
        errorMsg: "操作成功!",
        total: 0,
        data: {
            kczfs: [
                {
                id: "01",
                text: "微信"
                },
                {
                id: "02",
                text: "支付宝"
                },
                {
                id: "03",
                text: "现金"
                },
                {
                id: "04",
                text: "其他"
                }
            ],
            byqzt: [
                {
                id: "1",
                text: "运行"
                },
                {
                id: "2",
                text: "调试"
                },
                {
                id: "3",
                text: "停运"
                },
                {
                id: "4",
                text: "故障"
                },
                {
                id: "5",
                text: "未运行"
                }
            ]
            }
        }
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResultVo findAllDicts() throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(dictService.findAllDicts());
        return resultVo;
    }
    /**
     * @api {GET} /api/dicts/level/1 查询一级字典生成树
     * @apiName queryLevel1Dicts
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 查询一级字典, 包含排序
     * <br/>
     * @apiParam {String} sort 排序字段(id:主键||name:名称)
     * @apiParam {String} order 排序方向  (asc:升序||desc:倒序)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object[]} data 数据封装
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
     * {
     *  errorCode: 0,
     *  errorMsg: "操作成功!",
     *  total: 0,
     *  data: [
     *      {
     *      id: -1,
     *      name: "可编辑类",
     *      level: 0,
     *      href: null,
     *      target: null,
     *      icon: null,
     *      pid: 0,
     *      child: [
     *          {
     *              id: 2,
     *              name: "性别",
     *              level: 0,
     *              href: "sex",
     *              target: null,
     *              icon: null,
     *              pid: -1,
     *              child: [ ]
     *          },
     *          {
     *              id: 3,
     *              name: "经营类型",
     *              level: 0,
     *              href: "",
     *              target: null,
     *              icon: null,
     *              pid: -1,
     *              child: [ ]
     *          },
     *          ]
     *      }
     *    ]
     * }
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/level/1", method = RequestMethod.GET)
    public ResultVo queryLevel1Dicts(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        List list = dictService.findLeve1Dicts(data);
        resultVo.setData(list);
        return resultVo;
    }

    /**
     * @api {GET} /api/dicts/level/2 查询二级字典
     * @apiName queryLevel2Dicts
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 查询二级字典, 包含排序
     * <br/>
     * @apiParam {int} pageNum 页码
     * @apiParam {int} pageSize 页大小
     * @apiParam {String} [sort] 排序字段
     * @apiParam {String} [order] 排序方向
     * @apiParam {int} pid 父节点ID(一级字典的ID)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiSuccessExample {json} Success出参示例:
     * {
     *  errorCode: 0,
     *  errorMsg: "操作成功!",
     *  total: 0,
     *  data: {
     *      pageNum: 1,
     *      pageSize: 1,
     *      size: 1,
     *      startRow: 1,
     *      endRow: 1,
     *      total: 3,
     *      pages: 3,
     *      list: [
     *          {
     *              updateBy: "0",
     *              id: 243,
     *              parentId: 2,
     *              sort: 10,
     *              description: "描述",
     *              value: "01",
     *              remarks: null,
     *              label: "男",
     *              createDate: "2017-04-17 10:46:06",
     *              type: "sex",
     *              createBy: "0",
     *              updateDate: "2017-04-17 10:46:06",
     *              delFlag: "0"
     *          }
     *         ],
     *      prePage: 0,
     *      nextPage: 2,
     *      isFirstPage: true,
     *      isLastPage: false,
     *      hasPreviousPage: false,
     *      hasNextPage: true,
     *      navigatePages: 8,
     *      navigatepageNums: [
     *          1,
     *          2,
     *          3
     *      ],
     *      navigateFirstPage: 1,
     *      navigateLastPage: 3,
     *      firstPage: 1,
     *      lastPage: 3
     *      }
     *  }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 二级字典父节点ID不能为空!
     */
    @RequestMapping(value = "/level/2", method = RequestMethod.GET)
    public ResultVo queryLevel2Dicts(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        PageInfo pageInfo = dictService.findLeve2Dicts(data);
        resultVo.setData(pageInfo);
        return resultVo;
    }

    /**
     * @api {GET} /api/dicts/{dictId} 查询字典详情
     * @apiName queryDictById
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 根据字典ID查询字典详情
     * <br/>
     * @apiParam {int} dictId 字典表ID
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * @apiSuccess {Object} data 数据封装
     * @apiSuccess {Int(11)} data.id 主键ID
     * @apiSuccess {String(100)} data.value 数据值
     * @apiSuccess {String(100)} data.label 标签名
     * @apiSuccess {String(100)} data.type 类型
     * @apiSuccess {String(100)} data.description 描述
     * @apiSuccess {Double(10)} data.sort 排序字段
     * @apiSuccess {Int(11)} data.parentId 父级编号
     * @apiSuccess {Int(11)} data.createBy 创建者
     * @apiSuccess {Date} data.createDate 创建时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {Int(64)} data.updateBy 更新者
     * @apiSuccess {Date} data.updateDate 更新时间(格式类型：yyyy-MM-dd HH:mm:ss)
     * @apiSuccess {String(255)} data.remarks 备注信息
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/{dictId}", method = RequestMethod.GET)
    public ResultVo queryDictById(@PathVariable("dictId") Integer dictId) throws Exception {
        ResultVo resultVo = new ResultVo();
        Map dict = dictService.findDictById(dictId);
        resultVo.setData(dict);
        return resultVo;
    }

    /**
     * @api {POST} /api/dicts 保存字典
     * @apiName saveDict
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 保存字典信息，需传父节点ID
     * <br/>
     * @apiParam {String(100)} value 数据值
     * @apiParam {String(100)} label 标签名
     * @apiParam {String(100)} type 类型
     * @apiParam {String(100)} [description] 描述
     * @apiParam {Double(10)} sort 排序字段
     * @apiParam {Int(11)} parentId 父级编号
     * @apiParam {Int(11)} createBy 创建者
     * @apiParam {Int(64)} updateBy 更新者
     * @apiParam {String(255)} [remarks] 备注信息
     * <br>
     * @apiParamExample {json} 入参示例:
     * {
     *  "value": "0",
     *  "label": "测试保存",
     *  "type": "test111",
     *  "description": "测试描述",
     *  "sort": 20,
     *  "createBy": 1,
     *  "updateBy": 1,
     *  "parentId": 1001,
     *  "remarks":"备注信息"
     * }
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 字典类型不能为空!
     * @apiError -1000012 更新数据主键ID不能为空!
     * @apiError -1000012 规则编码不能为空!
     * @apiError -1000012 规则名称不能为空!
     * @apiError -1000012 更新者不能为空!
     * @apiError -1000012 排序字段不能为空!
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResultVo saveDict(@RequestBody Map data) throws Exception {
        ResultVo resuleVo = new ResultVo();
        dictService.insertDict(data);
        return resuleVo;
    }

    /**
     * @api {PUT} /api/dicts 更新字典
     * @apiName updateDict
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 更新字典，参数为sys_dict表字段驼峰式命名
     * <br>
     * @apiParam {Int(11)} id 字典表ID(主键)
     * @apiParam {String(100)} value 数据值
     * @apiParam {String(100)} label 标签名
     * @apiParam {String(100)} type 类型
     * @apiParam {String(100)} [description] 描述
     * @apiParam {Double(10)} sort 排序字段
     * @apiParam {Int(11)} parentId 父级编号(只提供二级字典的更新，所以不能为空)
     * @apiParam {Int(64)} updateBy 更新者
     * @apiParam {String(255)} [remarks] 备注信息
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
     *  "id":1004,
     *  "value": "0",
     *  "label": "测试保存",
     *  "type": "test111",
     *  "description": "测试描述",
     *  "sort": 10,
     *  "updateBy": 1,
     *  "parentId": 1001,
     *  "remarks":"备注信息"
     * }
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1000012 二级字典父节点ID不能为空!
     * @apiError -1000012 字典类型不能为空!
     * @apiError -1000012 更新数据主键ID不能为空!
     * @apiError -1000012 规则编码不能为空!
     * @apiError -1000012 规则名称不能为空!
     * @apiError -1000012 更新者不能为空!
     * @apiError -1000012 排序字段不能为空!
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResultVo updateDict(@RequestBody Map data) throws Exception {
        ResultVo resuleVo = new ResultVo();
        dictService.updateDict(data);
        return resuleVo;
    }

    /**
     * @api {DELETE} /api/dicts/{dictIds} 删除多个字典
     * @apiName delDictsByIds
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 根据Ids删除字典，因一级字典不允许删除，删除字典中如果包含一级字典则删除失败
     * <br/>
     * @apiParam {String} dictIds 字典表ID(主键，例：1,2,3)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -1001000 删除字典中包含一级字典，不允许删除!
     * @apiError -1001001 请至少选择一条数据删除!
     */
    @RequestMapping(value = "/{dictIds}", method = RequestMethod.DELETE)
    public ResultVo delDictsByIds(@PathVariable("dictIds") List dictIds) throws Exception {
        ResultVo resultVo = new ResultVo();
        dictService.delDictsByIds(dictIds);
        return resultVo;

    }

    /**
     * @api {GET} /api/dicts/label/{type} 通用根据字典类型获取下拉框字典
     * @apiName getDict
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 根据一级字典类型获取二级字典组合下拉框
     * <br/>
     * @apiParam {String} type 一级字典类型
     * <br/>
     * @apiParamExample {String} 入参值:
     * qyjb 企业级别字典
     * xb 性别字典
     * jingylx 经营类型字典
     * yyzt 运营状态字典
     * zt 状态字典
     * jscs 建设场所字典
     * cdzlx 充电桩类型字典
     * sf 是否字典
     * xtzq 心跳周期字典
     * sbzt 设备状态字典
     * txxy 通讯协议字典
     * cdzcj 充电桩厂家字典
     * pt PT字典
     * ct CT字典
     * gjx 告警项字典
     * dxlx 对象类型字典
     * gjclzt 告警处理状态字典
     * hylb 会员类别字典
     * jthyzffs 集团会员支付方式字典
     * zhzt 账户状态字典
     * czfs 充值方式字典
     * yjfs 月结方式字典
     * zfzt 支付状态字典
     * sftgfp 是否提供发票字典
     * sbxh 设备型号字典
     * sfzx 是否执行字典
     * djlx 电价类型字典
     * txpz 通讯配置字典
     * skfs 收款方式字典
     * bjcj 表计厂家字典
     * yyms 运营模式字典
     * clzt 处理状态字典
     * wlgs 物流公司字典
     * jylx 交易类型字典
     * kczfs 卡充值方式字典
     * sylx 适用类型字典
     * hyly 会员来源字典
     * zflc 支付流程字典
     * zxlx 资讯类型字典
     * zxzt 资讯状态字典
     * hylx 合约类型字典
     * hyjszq 合约结算周期字典
     * hyspzt 合约审批状态字典
     * hyfcxm 合约分成项目 字典
     * jfzt 缴费状态字典
     * txzt 提现状态字典
     * txfs 提现方式字典
     * byqzt 变压器状态字典
     * mxlx 模型类型字典
     * gzbbzlx 公专变标志类型字典
     * bjlx 表计类型字典
     * bjyt 表计用途字典
     * zdtxxy 终端通讯协议字典
     * zdtxfs 终端通信方式字典
     * zdzt 终端状态字典
     * cklx 串口类型字典
     * gbxy 国标协议字典
     * cllx 车辆类型字典
     * ewmxfzt 二维码下发状态字典
     * mbxx 模板选项字典
     * rwzt 任务状态字典
     * rwlx 任务类型字典
     * gjjb 告警级别字典
     * xtyhzt 系统用户状态字典
     * yhlx 用户类型字典
     * czlx 操作类型字典
     * xtzdlb 系统字典类别字典
     * glydj 管理员等级字典
     * shzt 审核状态字典
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
                id: "01",
                text: "男"
            },
            {
                id: "02",
                text: "女"
            }
            ]
      }
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/label/{type}", method = RequestMethod.GET)
    public ResultVo getDict(@PathVariable("type") String type) throws Exception {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(dictService.getDictByType(type));
        return resultVo;
    }

    /**
     * @api {DELETE} /api/dicts/cache/{keys} 删除多个字典缓存
     * @apiName deleteCacheDict
     * @apiGroup DictController
     * @apiVersion 2.0.0
     * @apiDescription 杨帅A 根据字典缓存key集合删除字典缓存，本方法只提供删除字典缓存，如果输入其他缓存key，自动过滤
     * <br/>
     * @apiParam {String} keys 字典缓存key(例：xb,czlx,hylx)
     * <br/>
     * @apiSuccess {String} errorCode 错误码
     * @apiSuccess {String} errorMsg 消息说明
     * <br/>
     * @apiError -999 系统异常!
     */
    @RequestMapping(value = "/cache/{keys}", method = RequestMethod.DELETE)
    public ResultVo deleteCacheDict(@PathVariable("keys") List<String> keys) throws Exception {
        ResultVo resultVo = new ResultVo();
        dictService.delCacheDict(keys);
        return resultVo;
    }

}

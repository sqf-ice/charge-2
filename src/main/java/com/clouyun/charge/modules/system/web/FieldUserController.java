package com.clouyun.charge.modules.system.web;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.system.service.FieldUserService;
import com.github.pagehelper.PageInfo;
/**
 * 描述: 外勤用户管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年3月6日
 */
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api")
public class FieldUserController extends BusinessController{

	@Autowired
    private FieldUserService fieldUserService;
	
	/**
	 * @api {GET} /api/fUsers   分页查询外勤用户列表
	 * @apiName  getFieldUsersPage
	 * @apiGroup FieldUserController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 根据相应条件分页查询告警列表
	 * <br/>
	 * @apiParam   {Integer}   userId                   用户id
	 * @apiParam   {Integer}   [wqConsOrgId]            所属企业id
	 * @apiParam   {Integer}   [wqConsOrgName]          所属企业名称
	 * @apiParam   {Integer}   [wqConsstationOrgId]     运营商id
	 *  @apiParam  {String}    [wqConsstationOrgName]   运营商名称
	 * @apiParam   {String}    [wqConsName]             真实姓名          
	 * @apiParam   {Integer}   [wqConsstationStationId] 管理场站id            
	 * @apiParam   {String}    [wqConsTel]              手机号码                         
	 * @apiParam   {String}    [wqConsStatus]           用户状态       1---是有效  0---是无效                 
	 * @apiParam   {int}       pageNum                  页码
	 * @apiParam   {int}       pageSize                 页大小
	 * @apiParam   {String}    [sort]                   排序字段
	 * @apiParam   {String}    [order]                  排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     分页数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {Integer}   data.list.wqConsId       外勤用户id
	 * @apiSuccess {String}    data.list.orgName        运营商名称
	 * @apiSuccess {Integer}   data.list.wqConsOrgId    运营商id
	 * @apiSuccess {String}    data.list.department     部门
	 * @apiSuccess {String}    data.list.wqConsStatus   外勤用户状态
	 * @apiSuccess {Integer}   data.list.wqConsAddr     外勤用户地址
	 * @apiSuccess {String}    data.list.wqConsEmail    外勤用户邮件
	 * @apiSuccess {String}    data.list.wqConsRemark   外勤用户备注
	 * @apiSuccess {String}    data.list.wqConsTel      外勤用户电话  
	 * @apiSuccess {String}    data.list.wqConsName     真实姓名
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/fUsers",method = RequestMethod.GET)
	public ResultVo getFieldUsersPage(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = fieldUserService.getFieldUsersPage(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
	/**
	 * @api {GET} /api/fUsers/{wqConsId}  外勤用户详情
	 * @apiName  getFieldUser
	 * @apiGroup FieldUserController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉 根据外勤用户id获取外勤用户详情
	 * <br/>
	 * @apiParam   {Integer}   wqConsId          外勤用户id
	 * <br/>
	 * @apiSuccess {String}    errorCode           错误码
	 * @apiSuccess {String}    errorMsg            消息说明
	 * @apiSuccess {Object}    data                数据对象
     * @apiSuccess {Integer}   data.wqConsId       外勤用户id
	 * @apiSuccess {String}    data.orgName        运营商名称
	 * @apiSuccess {Integer}   data.wqConsOrgId    运营商id
	 * @apiSuccess {String}    data.wqConsStatus   外勤用户状态
	 * @apiSuccess {String}    data.wqDepartment   部门
	 * @apiSuccess {Integer}   data.wqConsAddr     外勤用户地址
	 * @apiSuccess {String}    data.wqConsEmail    外勤用户邮件
	 * @apiSuccess {String}    data.wqConsRemark   外勤用户备注
	 * @apiSuccess {String}    data.wqConsTel      外勤用户电话  
	 * @apiSuccess {String}    data.wqConsName     真实姓名
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/fUsers/{wqConsId}",method = RequestMethod.GET)
	public ResultVo getFieldUser(@PathVariable("wqConsId") Integer wqConsId) throws BizException{
		ResultVo resultVo = new ResultVo();
		Map map =  fieldUserService.getFieldUser(wqConsId);
		resultVo.setData(map);
		return resultVo;
	}
	/**
	 * @api {DELETE} /api/fUsers/{wqConsIds}   外勤用户无效设置
	 * @apiName  removeFieldUser
	 * @apiGroup FieldUserController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉              外勤用户无效设置
	 * <br/>
	 * @apiParam   {String}    wqConsIds       外勤用户ids
	 * <br/>
	 * @apiSuccess {String}    errorCode       错误码
	 * @apiSuccess {String}    errorMsg        消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/fUsers/{wqConsIds}",method = RequestMethod.DELETE)
	public ResultVo removeFieldUser(@PathVariable("wqConsIds") List wqConsIds){
		ResultVo resultVo = new ResultVo();
		fieldUserService.removeFieldUser(wqConsIds);
		return resultVo;
	}
	
	/**
	 * @api {POST} /api/fUsers     新增外勤用户信息
	 * @apiName  insertFieldUser
	 * @apiGroup FieldUserController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉               新增外勤用户信息
	 * <br/>
	 * @apiParam   {Integer}   wqConsOrgId        所属企业
	 * @apiParam   {String}    wqConsTel          外勤用户电话
	 * @apiParam   {String}    wqConsName         外勤用户真实姓名
	 * @apiParam   {String}    wqConsStatus       外勤用户状态  1-有效 0-无效
	 * @apiParam   {String}    [wqConsEmail]      外勤用户邮件
	 * @apiParam   {String}    [wqConsAddr]       外勤用户地址
	 * @apiParam   {String}    [wqConsRemark]     外勤用户备注
	 * @apiParam   {String}    [wqDepartment]     外勤用户部门
	 * @apiParam   {Object}    [orgStationRel]    外勤用户管理的场站
	 * @apiParamExample {json} 入参示例:
     * {
         "wqConsOrgId": 1,
         "wqConsTel": "12312312321",
         "wqConsName": "xxxxx11111111",
         "wqConsStatus": "1",
         "orgStationRel": [
                           {
                             "id": 24,
                             "pid": 0,
                             "name": "深圳市车电网络有限公司",
                             "level": 1,
                             "href": null,
                             "target": null,
                             "icon": null,
                             "children": [
                                           {
                                            "id": 310,
                                            "pid": 24,
                                            "name": "上步中学（不对外开放）",
                                            "level": 2,
                                            "href": null,
                                            "target": null,
                                            "icon": null,
                                            "children": null
                                           }
                                         ]
                                }
                           ]
           }
	 * <br/>
	 * @apiSuccess {String}    errorCode           错误码
	 * @apiSuccess {String}    errorMsg            消息说明
	 * <br/>
	 * @apiError -999 系统异常！
	 * @apiError -1001003 参数空异常！
	 */
	/*{
    "wqConsOrgId": 1,
    "wqConsTel": "12312312321",
    "wqConsName": "xxxxx11111111",
    "wqConsStatus": "1",
    "orgStationRel": [
                      {
                        "id": 24,
                        "children": [
                                     310,45
                                    ]
                      }
                     ]
      }*/
	@RequestMapping(value = "/fUsers",method = RequestMethod.POST)
	public ResultVo insertFieldUser(@RequestBody Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		fieldUserService.insertFieldUser(map);
		return resultVo;
	}
	/**
	 * @api {PUT} /api/fUsers     编辑外勤用户信息
	 * @apiName  updateFieldUser
	 * @apiGroup FieldUserController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉               编辑外勤用户信息
	 * <br/>
	 * @apiParam   {Integer}   wqConsId           外勤用户id
	 * @apiParam   {Integer}   wqConsOrgId        所属企业
	 * @apiParam   {String}    wqConsTel          外勤用户电话
	 * @apiParam   {String}    wqConsName         外勤用户真实姓名
	 * @apiParam   {String}    wqConsStatus       外勤用户状态  1-有效 0-无效
	 * @apiParam   {String}    [wqConsEmail]      外勤用户邮件
	 * @apiParam   {String}    [wqConsAddr]       外勤用户地址
	 * @apiParam   {String}    [wqConsRemark]     外勤用户备注
	 * @apiParam   {String}    [wqDepartment]     外勤用户部门
	 * @apiParam   {Object}    [orgStationRel]    外勤用户管理的场站
	 * <br/>
	 * @apiSuccess {String}    errorCode           错误码
	 * @apiSuccess {String}    errorMsg            消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/fUsers",method = RequestMethod.PUT)
	public ResultVo updateFieldUser(@RequestBody Map map) throws BizException{
		fieldUserService.updateFieldUser(map);
		return new ResultVo();
	}
	
	/**
	 * @api {PUT} /api/fUsers/_export     导出外勤用户
	 * @apiName  exportFieldUsers
	 * @apiGroup FieldUserController
	 * @apiVersion 2.0.0
	 * @apiDescription  高辉             导出外勤用户
	 * <br/>
	 * @apiParam   {Integer}   userId                   用户id
	 * @apiParam   {Integer}   [orgId]                  所属企业id
	 * @apiParam   {Integer}   [wqConsstationOrgId]     运营商id
	 * @apiParam   {String}    [wqConsName]             真实姓名          
	 * @apiParam   {Integer}   [wqConsstationStationId] 管理场站id            
	 * @apiParam   {String}    [wqConsTel]              手机号码                         
	 * @apiParam   {String}    [wqConsStatus]           用户状态                        
	 * @apiParam   {int}       [pageNum]                页码
	 * @apiParam   {int}       [pageSize]               页大小
	 * @apiParam   {String}    [sort]                   排序字段
	 * @apiParam   {String}    [order]                  排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiSuccess {String}    errorCode           错误码
	 * @apiSuccess {String}    errorMsg            消息说明
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "fUsers/_export",method = RequestMethod.GET)
	public void exportFieldUsers(@RequestParam Map map,HttpServletResponse response) throws Exception{
		fieldUserService.exportFieldUsers(map,response);
	}
	
	/**
	 * @api {GET} /api/dicts/fuser   外勤用户业务字典
     * @apiName  getTemplateDicts
     * @apiGroup FieldUserController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   外勤用户业务字典 
     * <br/>
     * @apiParam   {Integer}   userId                   用户id
	 * @apiParam   {Integer}   [wqConsOrgId]            所属企业id
	 * @apiParam   {Integer}   [stationId]              管理的场站id
     * <br/>
     * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {String}    data.list.id             外勤用户id
	 * @apiSuccess {String}    data.list.text           外勤用户名称
	 * @apiSuccessExample {json} Success出参示例:
	 * 
	 * <br/>
	 * @apiError -999 系统异常!
     * @apiError -1803000 参数空异常!
     */
	@RequestMapping(value = "/dicts/fuser", method = RequestMethod.GET)
	public ResultVo getTemplateDicts(@RequestParam Map map) throws Exception{
		ResultVo resultVo =new ResultVo();
		resultVo.setData(fieldUserService.getFieldUserDicts(map));
		return resultVo;
	}
	
	/**
	 * @api {GET} /fUsers/{wqConsId}/rels   外勤用户所管理的企业及场站
     * @apiName  getFieldStationOrgRel
     * @apiGroup FieldUserController
     * @apiVersion 2.0.0
     * @apiDescription  高辉             外勤用户所管理的企业及场站
     * <br/>
	 * @apiParam   {Integer}   wqConsId                               外勤用户id
     * <br/>
     * @apiSuccess {String}    errorCode                              错误码
	 * @apiSuccess {String}    errorMsg                               消息说明
	 * @apiSuccess {Object}    data                                   数据封装
	 * @apiSuccess {Integer}   data.orgId                             外勤用管理企业id
	 * @apiSuccess {Object[]}  data.stationId                         外勤用管理的场站集合
	 * @apiSuccessExample {json} Success出参示例:
	 * <br/>
	 * @apiError -999 系统异常!
     */
	@RequestMapping(value = "/fUsers/{wqConsId}/rels", method = RequestMethod.GET)
	public ResultVo getFieldStationOrgRel(@PathVariable("wqConsId") Integer wqConsId) throws Exception{
		ResultVo resultVo =new ResultVo();
		resultVo.setData(fieldUserService.getFieldStationOrgRel(wqConsId));
		return resultVo;
	}
    /**
     * 保存外勤用户
     * @param data
     * @return
     * @throws Exception
     * 2017年3月6日
     * gaohui
     */
	@RequestMapping(value = "/fUsers/saveFieldUser", method = RequestMethod.POST)
    public ResultVo saveFieldUser(@RequestBody DataVo data){
        ResultVo resultVo = new ResultVo();
        fieldUserService.addFieldUser(data);
        return resultVo;
    }
    /**
     * 编辑外勤用户信息
     * @param data
     * @return
     * 2017年3月6日
     * gaohui
     */
	@RequestMapping(value = "/fUsers/modifyFieldUser",method = RequestMethod.POST)
	public ResultVo modifyFieldUser(@RequestBody DataVo data){
		ResultVo vo = new ResultVo();
		fieldUserService.modifyFieldUser(data);
		return vo;
	}
	/**
	 * 分页查询满足条件的所有外勤用户
	 * @param data
	 * @return
	 * 2017年3月6日
	 * gaohui
	 */
	@RequestMapping(value = "/fUsers/queryAllFieldUser",method = RequestMethod.POST)
	public ResultVo queryAllFieldUser(@RequestBody DataVo data){
		return fieldUserService.queryAllFieldUser(data);
	}
	/**
	 * 根据电话号码和企业ID判断用户是否存在
	 * @param data
	 * @return
	 * 2017年3月6日
	 * gaohui
	 * @throws BizException 
	 */
	@RequestMapping(value = "/fUsers/queryAllFieldUserExist",method = RequestMethod.POST)
	public ResultVo queryAllFieldUserExist(@RequestBody DataVo data) throws BizException{
		ResultVo resultVo = new ResultVo();
		List<Map> list = fieldUserService.queryFieldUserExist(data);
		resultVo.setData(list);
		return resultVo;
	}
    /**
     * 根据id删除外勤用户
     * @param id
     * @return
     * 2017年3月7日
     * gaohui
     */
	@RequestMapping(value = "/fUsers/deleteFieldUser",method = RequestMethod.POST)
	public ResultVo deleteFieldUser(@RequestBody DataVo data){
		ResultVo vo = new ResultVo();
		fieldUserService.deleteFieldUser(data);
		return vo;
	}
	/**
	 * 根据外勤用户id查询外勤用户信息
	 * @param data
	 * @return
	 * 2017年3月8日
	 * gaohui
	 */
	@RequestMapping(value = "/fUsers/queryFieldUserById",method = RequestMethod.POST)
	public ResultVo queryFieldUserById(@RequestBody DataVo data){
		Map map =  fieldUserService.queryFieldUserById(data);
		ResultVo vo = new ResultVo();
		vo.setData(map);
		return vo;
	}
	/**
	 * 添加外勤用户和场站、运营商的关联关系
	 * @param data
	 * @return
	 * 2017年3月8日
	 * gaohui
	 */
	@RequestMapping(value = "/fUsers/addFieldStationOrgRel",method = RequestMethod.POST)
	public ResultVo addFieldStationOrgRel(@RequestBody DataVo data){
		ResultVo vo = new ResultVo();
		vo.setData(fieldUserService.addFieldStationOrgRel(data));
		return vo;
	}
	/**
	 * 删除外勤用户和场站、运营商的关联关系
	 * @param data
	 * @return
	 * 2017年3月8日
	 * gaohui
	 */
	@RequestMapping(value = "/fUsers/deleteFieldStationOrgRel",method = RequestMethod.POST)
	public ResultVo deleteFieldStationOrgRel(@RequestBody DataVo data){
		ResultVo vo = new ResultVo();
		vo.setData(fieldUserService.deleteFieldStationOrgRel(data));
		return vo;
	}
	/**
	 * 查询外勤用户和场站、运营商的关联关系
	 * @param data
	 * @return
	 * 2017年3月8日
	 * gaohui
	 */
	@RequestMapping(value = "/fUsers/queryFieldStationOrgRel",method = RequestMethod.POST)
	public ResultVo queryFieldStationOrgRel(@RequestBody DataVo data){
		return fieldUserService.queryFieldStationOrgRel(data);
	}
	
	
	/**
	 * 根据外勤用户的Ids查询用户
	 * @param map
	 * @return
	 * @throws BizException
	 * 2017年4月11日
	 * gaohui
	 */
	@RequestMapping(value = "/fUsers/qFUserByIds",method = RequestMethod.POST)
	public ResultVo queryFieldUserByIds(@RequestBody DataVo data)throws BizException{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(fieldUserService.queryFieldUserByIds(data));
		return resultVo;
	}
	
}

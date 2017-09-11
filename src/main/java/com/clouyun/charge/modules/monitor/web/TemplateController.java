package com.clouyun.charge.modules.monitor.web;
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
import com.clouyun.charge.modules.monitor.service.TemplateService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: TemplateController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0.0
 * 创建日期: 2017年2月27日
 */
@RestController
public class TemplateController {
	
	@Autowired
	TemplateService templateService;
	
	/**
	 * @api {GET} /api/templates   分页查询模板列表
     * @apiName  updateWarning
     * @apiGroup TemplateController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   分页查询模板列表 
     * <br/>
     * @apiParam   {Integer}   userId                   用户id
	 * @apiParam   {String}    [createTimeStart]        开始时间(yyyy-MM-dd 00:00:00)
	 * @apiParam   {String}    [createTimeEnd]          结束时间(yyyy-MM-dd 23:59:59)
	 * @apiParam   {String}    [templateName]           模板名称           
	 * @apiParam   {int}       pageNum                  页码
	 * @apiParam   {int}       pageSize                 页大小
	 * @apiParam   {String}    [sort]                   排序字段
	 * @apiParam   {String}    [order]                  排序(DESC:降序|ASC:升序)
     * <br/>
     * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {Integer}   data.list.templateId     模板id
	 * @apiSuccess {String}    data.list.templateName   模板名称
	 * @apiSuccess {String}    data.list.createTime     模板创建时间(yyyy-MM-dd HH:mm:ss)
	 * @apiSuccessExample {json}  Success出参示例
	 * {
     * 	"errorCode": 0,
     *	"errorMsg": "操作成功!",
     *  "data": {
     *           "pageNum": 1,
     *           "pageSize": 2,
     *           "size": 2,
     *           "startRow": 1,
     *           "endRow": 2,
     *           "total": 271,
     *           "pages": 136,
     *           "list": [
     *                    {
     *                     "orgId": 24,
     *                     "templateId": 357,
     *                     "createTime": "2017-04-19 19:22:43",
     *                     "updateTime": "",
     *                     "orgName": "深圳陆科",
     *                     "templateName": "照片GPS必填"
     *                    },
     *                    {
     *                     "orgId": 24,
     *                     "templateId": 356,
     *                     "createTime": "2017-04-19 19:21:18",
     *                     "updateTime": "",
     *                     "orgName": "深圳陆科",
     *                     "templateName": "定位必填"
     *                   }
     *                  ],
     *          "prePage": 0,
     *          "nextPage": 2,
     *          "isFirstPage": true,
     *          "isLastPage": false,
     *          "hasPreviousPage": false,
     *          "hasNextPage": true,
     *          "navigatePages": 8,
     *          "navigatepageNums": [
     *                                 1,
     *                                 2,
     *                                 3,
     *                                 4,
     *                                 7,
     *                                 8
     *                              ],
     *          "navigateFirstPage": 1,
     *          "navigateLastPage": 8,
     *          "firstPage": 1,
     *          "lastPage": 8
     *     }
     * }
	 * <br/>
     * @apiError -999 系统异常!
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/templates", method = RequestMethod.GET)
	public ResultVo getTemplatesPage(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		PageInfo pageInfo = templateService.getTemplatesPage(map);
		resultVo.setData(pageInfo);
		return resultVo;
	}
	
	/**
	 * @api {POST} api/templates   新增模板
     * @apiName  addTemplate
     * @apiGroup TemplateController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   新增模板
     * <br/>
     * @apiParam   {Integer} orgId                      运营商id
     * @apiParam   {String}  templateName               模板名称     
	 * @apiParam   {object}  option                     数据资源    
	 * @apiParam   {String}  option.optionResources     数据资源    
	 * @apiParam   {String}  option.optionNames         选项名称
	 * @apiParam   {String}  option.optionType          是否为必填类型
	 * @apiSuccessExample {json}  Success出参示例
	 * {
     *   "orgId": 11,
     *   "templateName":"模板名称",
     *   "options": [
     *               {
     *                 "optionResources": "01",
     *                 "optionName":"选项名称",
     *                 "optionType": 1
     *               }
     *              ]
     *}
     * <br/>
     * @apiSuccess {String}  errorCode                  错误码
	 * @apiSuccess {String}  errorMsg                   消息说明
	 * <br/>
     * @apiError -999 系统异常!
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/templates",method = RequestMethod.POST)
	public ResultVo addTemplate(@RequestBody Map map) throws BizException{
		ResultVo resultVo =new ResultVo();
		templateService.addTemplate(map);
		return resultVo;
	}
	/**
	 * @api {PUT} api/templates   更新模板(已废弃)
     * @apiName  modifyTemplate
     * @apiGroup TemplateController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   更新模板
     * <br/>
     * @apiParam   {Integer}   templateId                 模板id
     * @apiParam   {Integer}   orgId                      运营商id
     * @apiParam   {String}    templateName               模板名称     
	 * @apiParam   {object[]}  option                     数据项    
	 * @apiParam   {String}    option.optionResources     数据资源    
	 * @apiParam   {String}    option.optionNames         选项名称
	 * @apiParam   {String}    option.optionType          是否为必填类型
     * <br/>
     * @apiSuccess {String}  errorCode                错误码
	 * @apiSuccess {String}  errorMsg                 消息说明
	 * <br/>
     * @apiError -999 系统异常!
     */
	@RequestMapping(value = "/api/templates",method = RequestMethod.PUT)
	public ResultVo modifyTemplate(@RequestBody Map map) throws BizException{
		ResultVo resVo =new ResultVo();
		templateService.modifyTemplate(map);
		return resVo;
	}
	/**
	 * @api {GET} /api/templates/{templateId}   查询模板详细信息
     * @apiName  getTemplate
     * @apiGroup TemplateController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   查询模板详细信息
     * <br/>
	 * @apiParam   {Integer}   templateId                  模板id           
     * <br/>
     * @apiSuccess {String}    errorCode                   错误码
	 * @apiSuccess {String}    errorMsg                    消息说明
	 * @apiSuccess {Object}    data                        数据对象
     * @apiSuccess {Integer}   data.templateId             模板id
	 * @apiSuccess {String}    data.templateName           模板名称
	 * @apiSuccess {String}    data.createTime             模板创建时间(yyyy-MM-dd HH:mm:ss)
	 * @apiSuccess {Object[]}  data.option                 数据项
	 * @apiSuccess {String}    data.option.optionResources 数据资源    
	 * @apiSuccess {String}    data.option.optionNames     选项名称
	 * @apiSuccess {String}    data.option.optionType      是否为必填类型
	 * @apiSuccessExample{json} Success出参示例
	 * {
     * 	"errorCode": 0,
     *	"errorMsg": "操作成功!",
     *  "data": {
     *   			"orgId": 354,
     *   			"templateId": 45,
     *   			"createTime": "2017-03-26 00:00:00",
     *   			"updateTime": "2017-04-01 10:27:11",
     *   			"orgName": "76子企业",
     *              "templateName": "123",
     *   			"optionList": [
     *       						{
     *           						"optionId": 399,
     *           						"optionName": "123",
     *           						"optionResources": "03",
     *           						"optionType": 0
     *       						}
     *   						  ]
     *         }
     * }
	 * <br/>
     * @apiError -999 系统异常!
     */
	@RequestMapping(value = "/api/templates/{templateId}",method = RequestMethod.GET)
	public ResultVo getTemplate(@PathVariable Integer templateId) throws BizException{
		ResultVo resultVo =new ResultVo();
		resultVo.setData(templateService.getTemplate(templateId));
		return resultVo;
	}
	/**
	 * @api {DELETE} /api/templates   删除模板 
     * @apiName  deleteTemplates
     * @apiGroup TemplateController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   删除模板 
     * <br/>
	 * @apiParam   {String}    ids                         模板id(1,2,..)       
     * <br/>
     * @apiSuccess {String}    errorCode                   错误码
	 * @apiSuccess {String}    errorMsg                    消息说明
	 * <br/>
     * @apiError -999 系统异常!
     */
	@RequestMapping(value = "/api/templates",method = RequestMethod.DELETE)
	public ResultVo deleteTemplates(@RequestBody DataVo dataVo) throws BizException{
		ResultVo resultVo =new ResultVo();
		templateService.deleteTemplate(dataVo);
		return resultVo;
	}
	
	/**
	 * @api {GET} /api/dicts/template   模板业务字典
     * @apiName  getTemplateDicts
     * @apiGroup TemplateController
     * @apiVersion 2.0.0
     * @apiDescription  高辉   模板业务字典 
     * <br/>
     * @apiParam   {Integer}   userId                   用户id
	 * @apiParam   {Integer}   [orgId]                  运营商id
     * <br/>
     * @apiSuccess {String}    errorCode                错误码
	 * @apiSuccess {String}    errorMsg                 消息说明
	 * @apiSuccess {Object}    data                     数据封装
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {String}    data.list.id             模板id
	 * @apiSuccess {String}    data.list.text           模板名称
	 * @apiSuccessExample {json} Success出参示例:
	 * 
	 * <br/>
	 * @apiError -999 系统异常!
     * @apiError -1803000 参数空异常!
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/dicts/template", method = RequestMethod.GET)
	public ResultVo getTemplateDicts(@RequestParam Map map) throws Exception{
		ResultVo resultVo =new ResultVo();
		resultVo.setData(templateService.getTemplateDicts(map));
		return resultVo;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/templates/templates", method = RequestMethod.GET)
	public void exportTemplates(@RequestParam Map map,HttpServletResponse response) throws Exception{
		templateService.exportTemplates(map,response);
	}
	
	@RequestMapping(value = "/template/list", method = RequestMethod.POST)
	public ResultVo queryTemplateAll(@RequestBody DataVo vo){
		return templateService.query(vo);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/template/id",method = RequestMethod.POST)
	public ResultVo queryById(@RequestBody DataVo vo){
		ResultVo resVo = new ResultVo();
		Map map = templateService.queryById(vo);
		resVo.setData(map);
		return resVo;
	}
	
	@RequestMapping(value = "/template/save",method = RequestMethod.POST)
	public ResultVo insertTemplate(@RequestBody DataVo vo){
		ResultVo resVo =new ResultVo();
		try {
			templateService.insert(vo);
		} catch (Exception e) {
			BizException bizErr = new BizException(1000010);
			resVo.setError(bizErr.getErrorCode(), bizErr.getMessage());
		}
		return resVo;
	}
	
	
	@RequestMapping(value = "/template/update",method = RequestMethod.POST)
	public ResultVo updateTemplate(@RequestBody DataVo vo){
		ResultVo resVo =new ResultVo();
		try {
			templateService.update(vo);
		} catch (Exception e) {
			BizException bizErr = new BizException(1000011);
			resVo.setError(bizErr.getErrorCode(), bizErr.getMessage());
		}
		return resVo;
	}
	
	
	@RequestMapping(value = "/template/del",method = RequestMethod.POST)
	public ResultVo deleteTemplate(@RequestBody DataVo vo){
		ResultVo resVo =new ResultVo();
		try {
			 templateService.delete(vo);
		} catch (Exception e) {
			BizException bizErr = new BizException(1000012);
			resVo.setError(bizErr.getErrorCode(), bizErr.getMessage());
		}
		return resVo;
	}
	
	@RequestMapping(value = "/template/check",method = RequestMethod.POST)
	public ResultVo checkTemplate(@RequestBody DataVo vo){
		return templateService.checkUniqueness(vo);
	}
	/**
	 * 根据企业ID查询模板列表
	 * @param vo
	 * @return
	 * 2017年3月22日
	 * gaohui
	 */
	@RequestMapping(value = "/template/queryListByOrgId",method = RequestMethod.POST)
	public ResultVo queryListByOrgId(@RequestBody DataVo vo){
		return templateService.queryListByOrgId(vo);
	}
	/**
	 * 根据模版id判断模版是否在使用
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/template/isUseTask",method = RequestMethod.POST)
	public ResultVo useTask(@RequestBody DataVo list){
		int count = templateService.isUseTask(list);
		ResultVo vo = new ResultVo();
		vo.setData(count);
		return vo;
	}
}

package com.clouyun.charge.modules.monitor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.monitor.mapper.TemplateMapper;
import com.clouyun.charge.modules.monitor.mapper.TemplateOptionMapper;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 描述: TemplateService 版权: Copyright (c) 2017 公司: 科陆电子 作者: sim.y 版本: 1.0 创建日期:
 * 2017年2月27日
 */
@SuppressWarnings("rawtypes")
@Service
public class TemplateService {

	@Autowired
	private TemplateMapper templateMapper;
	
	@Autowired
	private UserService userService;

	@Autowired
	private TemplateOptionMapper templateOptionMapper;

	/**
	 * 查询模板列表
	 * 
	 * @param map
	 * @return 2.0.0 gaohui
	 */
	@SuppressWarnings("unchecked")
	public PageInfo getTemplatesPage(Map map) throws BizException {
		PageInfo pageInfo = null;
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		if (params.isBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(map);
        }
		if (null == map.get("sort")) {
			map.put("sort", "this_.create_time");
			map.put("order", "DESC");
		}
		List<Map> result = templateMapper.get(map);
		if (null != result && result.size() > 0) {
			pageInfo = new PageInfo(result);
		}
		return pageInfo;
	}

	/**
	 * 新增模板
	 * 
	 * @param map
	 * @return 2.0.0 gaohui
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	public void addTemplate(Map map)throws BizException{
		DataVo params= new DataVo(map);
		if(params.isBlank("orgId")){
			throw new BizException(1803000,"运营商");
		}
		if(params.isBlank("templateName")){
			throw new BizException(1803000,"模板名称");
		}
		params.put("createTime", new Date());
		templateMapper.insert(params);
		List<DataVo> options = (List<DataVo>) params.get("options");
		if(null!=options && options.size()>0){
			for (DataVo optMap:options) {
				if(params.isBlank("templateId")){
					throw new BizException(1803000,"模板Id");
				}
				if(params.isBlank("optionResources")){
					throw new BizException(1803000,"数据资源");
				}
				if(params.isBlank("optionType")){
					throw new BizException(1803000,"是否为必填类型");
				}
				if(params.isBlank("optionName")){
					throw new BizException(1803000,"选项名称");
				}
				DataVo data = new DataVo();
				data.add("templateId", params.getInt("templateId"));
				data.add("optionResources", optMap.getString("optionResources"));
				data.add("optionType", optMap.get("optionType"));
				data.add("optionName", optMap.getString("optionName"));
				templateOptionMapper.insert(data);
			}
		}
	}

	/**
	 * 更新模板
	 * 
	 * @param map
	 * @return 2.0.0 gaohui
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	public void modifyTemplate(Map map) throws BizException {
		templateMapper.update(new DataVo(map));
		DataVo data = new DataVo();
		List<Integer> templateIds = new ArrayList<Integer>();
		templateIds.add(StringUtils.toInteger(map.get("templateId")));
		data.put("ids", templateIds);
		templateOptionMapper.delete(data);
		List<Map> options = (List<Map>) map.get("options");
		if (null != options && options.size() > 0) {
			for (Map option : options) {
				DataVo optionVo = new DataVo();
				optionVo.add("templateId",StringUtils.toInteger(map.get("templateId")));
				optionVo.add("optionResources", option.get("optionResources"));
				optionVo.add("optionType", option.get("optionType"));
				optionVo.add("optionName", option.get("optionName"));
				templateOptionMapper.insert(optionVo);
			}
		}
	}

	/**
	 * 查询模板详情
	 * 
	 * @param map
	 * @return 2.0.0 gaohui
	 */
	@SuppressWarnings("unchecked")
	public Map getTemplate(Integer templateId) {
		DataVo optionVo = new DataVo();
		optionVo.add("templateId", templateId);
		Map template = templateMapper.getById(templateId);
		if (null != template) {
			List list = templateOptionMapper.queryListByPage(optionVo);
			if (null != list) {
				template.put("option",
						templateOptionMapper.queryListByPage(optionVo));
			}
		}
		return template;
	}

	/**
	 * 删除模板
	 * @param map
	 * @return 
	 * 2.0.0 
	 * gaohui
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteTemplate(DataVo dataVo) {
		templateMapper.delete(dataVo);
		templateOptionMapper.delete(dataVo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ComboxVo> getTemplateDicts(Map map)throws BizException{
		List<ComboxVo> dicts = null;
		DataVo params= new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		List<Map> templates = templateMapper.queryListByOrgId(params);
		if(null!=templates && templates.size()>0){
			dicts = new ArrayList<ComboxVo>();
			for(Map template:templates){
				DataVo templateVo = new DataVo(template);
				ComboxVo comboxVo = new ComboxVo(templateVo.getString("templateId"), templateVo.getString("templateName"));
				dicts.add(comboxVo);
			}
		}
		return dicts; 
	}

	/**
	 * 导出模板
	 * @param map
	 * @return 2.0.0 
	 * gaohui
	 */
	@SuppressWarnings({ "unchecked" })
	public void exportTemplates(Map map,HttpServletResponse response) throws Exception {
		DataVo params = new DataVo(map);
		if(params.isBlank("userId")){
			throw new BizException(1803000,"用户id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType);
		if(null!=orgIds && orgIds.size()>0){
			params.put("orgIds", orgIds);
		}
		if (null == map.get("sort")) {
			map.put("sort", "this_.create_time");
			map.put("order", "DESC");
		}
		List<Map> list = templateMapper.get(map);
		String header = "第三方充电桩列表";
		List<String> headers = new ArrayList<String>();
		headers.add("模板id");
		headers.add("模板名称");
		headers.add("创建时间");
		List<String> rows = new ArrayList<String>();
		rows.add("templateId");
		rows.add("templateName");
		rows.add("createTime");
		ExportUtils.exportExcel(list, response, headers, rows, header);
	}

	public ResultVo query(DataVo dataVo) {
		ResultVo vo = new ResultVo();
		List list = templateMapper.queryListByPage(dataVo);
		int count = templateMapper.queryListCount(dataVo);
		vo.setTotal(count);
		vo.setData(list);
		return vo;
	}

	public Map queryById(DataVo dataVo) {
		DataVo optionVo = new DataVo();
		optionVo.add("templateId", dataVo.get("templateId"));
		Map template = templateMapper.queryById(dataVo);
		template.put("templateOptionList",
				templateOptionMapper.queryListByPage(optionVo));
		return template;
	}

	public ResultVo checkUniqueness(DataVo dataVo) {
		ResultVo vo = new ResultVo();
		vo.setData(templateMapper.checkUniqueness(dataVo));
		return vo;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void insert(DataVo dataVo) {
		DataVo data = null;
		templateMapper.insert(dataVo);
		List<Integer> resIds = (List<Integer>) dataVo.get("resIds");
		List<String> types = (List<String>) dataVo.get("type");
		List<String> optionNames = (List<String>) dataVo.get("optionName");
		for (int i = 0; i < resIds.size(); i++) {
			data = new DataVo();
			data.add("templateId", dataVo.get("templateId"));
			data.add("optionResources", resIds.get(i));
			data.add("optionType", types.get(i));
			data.add("optionName", optionNames.get(i));
			templateOptionMapper.insert(data);
		}
	}

	public void update(DataVo dataVo) {
		DataVo data = null;
		templateMapper.update(dataVo);
		data = new DataVo();
		List<Integer> templateIds = new ArrayList<Integer>();
		templateIds.add(dataVo.getInt("templateId"));
		data.put("ids", templateIds);
		templateOptionMapper.delete(data);
		List<String> resIds = (List<String>) dataVo.get("resIds");
		List<String> types = (List<String>) dataVo.get("type");
		List<String> optionNames = (List<String>) dataVo.get("optionName");
		for (int i = 0; i < resIds.size(); i++) {
			data = new DataVo();
			data.add("templateId", dataVo.get("templateId"));
			data.add("optionResources", resIds.get(i));
			data.add("optionType", types.get(i));
			data.add("optionName", optionNames.get(i));
			templateOptionMapper.insert(data);
		}
	}

	public void delete(DataVo dataVo) {
		templateMapper.delete(dataVo);
		templateOptionMapper.delete(dataVo);
	}

	/**
	 * 根据企业ID查询模板列表
	 * 
	 * @return 2017年3月22日 gaohui
	 */
	public ResultVo queryListByOrgId(DataVo data) {
		ResultVo resultVo = new ResultVo();
		List list = templateMapper.queryListByOrgId(data);
		resultVo.setData(list);
		return resultVo;
	}

	public int isUseTask(DataVo list) {
		return templateMapper.isUseTask(list);
	}

}

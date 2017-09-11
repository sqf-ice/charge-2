package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.domain.ui.Tree;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.domain.OrgTreeNode;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.MailUtils;
import com.clouyun.charge.common.utils.OSSUploadFileUtils;
import com.clouyun.charge.modules.system.mapper.CoopOperMapper;
import com.clouyun.charge.modules.system.mapper.PubOrgMapper;
import com.clouyun.charge.modules.system.mapper.RoleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述: 企业管理服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月22日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgService extends BusinessService {
	
	private static Logger logger = LoggerFactory.getLogger(OrgService.class);
	
	@Autowired
	private PubOrgMapper orgMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private CoopOperMapper coopOperMapper;
	
	// 运营类型-合作运营
	private static final String OPERATE_TYPE_02 = "02";
	
	// 公司类型-总公司
	private static final String ORG_TYPE_01 = "01";
	
	/**
	 * 查询企业列表分页
	 *
	 * @param data
	 * @return
	 * @throws BizException
	 */
	public PageInfo findOrgs(Map data) throws BizException {
		DataVo params = new DataVo(data);
		
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId"))
			throw new BizException(1000006);
		data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
		
		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(data);
		}
		List orgs = orgMapper.getOrgsByPage(data);
		PageInfo page = new PageInfo(orgs);
		return page;
	}
	
	/**
	 * 查询未审核企业列表分页
	 *
	 * @param data
	 * @return
	 * @throws BizException
	 */
	public PageInfo findAuditOrgs(Map data) throws BizException {
		DataVo params = new DataVo(data);
		
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId"))
			throw new BizException(1000006);
		
		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(data);
		}
		List orgs = orgMapper.getAuditOrgsByPage(data);
		PageInfo page = new PageInfo(orgs);
		return page;
	}
	
	/**
	 * 根据企业id获取需要审核信息
	 *
	 * @param orgId
	 * @return
	 * @throws BizException
	 */
	public DataVo findAuditOrgById(Integer orgId) throws BizException {
		DataVo org = orgMapper.getAuditOrgById(orgId);
		if (org == null || org.isEmpty())
			throw new BizException(1000015, "未审核企业或审核不通过企业");
		
		// 根据企业ID获取临时场站表信息
		List<DataVo> stations = orgMapper.getStationTempByOrgId(orgId);
		org.set("prov", areaService.getAreaNameByNo(org.getString("provCode")));
		org.set("city", areaService.getAreaNameByNo(org.getString("cityCode")));
		org.set("dist", areaService.getAreaNameByNo(org.getString("distCode")));
		org.set("namePy", CommonUtils.PATH + org.getString("namePy"));
		org.set("businesslicence", CommonUtils.PATH + org.getString("businesslicence"));
		for (DataVo station : stations) {
			station.set("address", areaService.getAreaNameByNo(station.getString("provCode")) + areaService.getAreaNameByNo(station.getString("cityCode")) + areaService.getAreaNameByNo(station.getString("distCode")));
		}
		org.set("stations", stations);
		return org;
	}
	
	/**
	 * 根据企业Id获取企业详细信息
	 *
	 * @param orgId 企业ID
	 * @return
	 * @throws BizException
	 */
	public DataVo findOrgById(Integer orgId) throws BizException {
		DataVo vo = orgMapper.getOrgById(orgId);
		if (vo.isNotBlank("namePy"))
			vo.set("namePy", CommonUtils.PATH + vo.getString("namePy"));
		// 如果是合作运营，获取合作运营ID集合
		if (OPERATE_TYPE_02.equals(vo.getString("operateType"))) {
			List<String> orgNames = coopOperMapper.getOrgNamesByCoopOrgId(orgId);
			vo.set("coopOrgName", CollectionUtils.isEmpty(orgNames) ? "" : orgNames.get(0));
		}
		return vo;
	}
	
	/**
	 * 更新企业合作运营数据表
	 *
	 * @param coopOrgId 被合作运营企业ID
	 * @param dataStr   合作运营企业ID字符串拼接
	 */
	public void updateCoopOperDatas(Integer coopOrgId, String dataStr) throws BizException {
		// 当数据权限集合都为空，这说明当前角色没有
		if (StringUtils.isBlank(dataStr)) {
			//删除所有角色关联数据权限集合
			int deleteCount = coopOperMapper.deleteCoopOperByCoopId(coopOrgId, null);
			logger.info("企业ID为[{}]编辑后合作运营为空，删除旧合作运营表[{}]条数据.", coopOrgId, deleteCount);
		} else {
			List<Integer> dbDataIds = coopOperMapper.getOrgIdsByCoopOrgId(coopOrgId);
			List<Integer> dataIds = Lists.newArrayList();
			//String集合转Integer集合
			CollectionUtils.collect(Arrays.asList(dataStr.split(",")), new Transformer() {
				
				@Override
				public Object transform(Object input) {
					return Integer.valueOf((String) input);
				}
			}, dataIds);
			//和数据库旧权限比较添加需要新增的
			List<Integer> addDatas = Lists.newArrayList();
			for (Integer orgId : dataIds) {
				if (orgId != null && !dbDataIds.contains(orgId)) {
					addDatas.add(orgId);
				}
			}
			if (addDatas.size() > 0) {
				int addCount = coopOperMapper.insertCoopOperByCoopId(coopOrgId, addDatas);
				logger.info("企业ID为[{}]编辑后新增[{}]条合作运营企业.", coopOrgId, addCount);
			}
			
			//定义可删除权限集合
			List<Integer> deleteDatas = Lists.newArrayList();
			for (Integer orgId : dbDataIds) {
				if (orgId != null && !dataIds.contains(orgId)) {
					deleteDatas.add(orgId);
				}
			}
			// 删除当前角色下编辑后没有的权限
			if (deleteDatas.size() > 0) {
				int deleteCount = coopOperMapper.deleteCoopOperByCoopId(coopOrgId, deleteDatas);
				logger.info("企业ID为[{}]编辑后删除[{}]条合作运营企业.", coopOrgId, deleteCount);
			}
		}
	}
	
	/**
	 * 新增企业信息
	 *
	 * @param data
	 * @param file
	 * @throws BizException
	 * @description 新增和编辑合并，方法暂不合并，只对是否有ID进行判断
	 */
	public void insertOrg(Map data, MultipartFile file) throws BizException {
		DataVo params = new DataVo(data);
		if (params.getInt("orgId") > 0) {
			this.updateOrg(data, file);
		} else {
			int userId = params.getInt("userId");
			CommonUtils.idIsEmpty(userId, "登录用户");
			this.verifyOrg(params);// 对企业表单数据验证
			// 合作运营企业ID字符串拼接(1,2,3,4)
			String coop = params.getString("coopOrgIds");
			// 运营类型
			String operateType = params.getString("operateType");
			if (file != null && !file.isEmpty()) {
				String fileName = file.getOriginalFilename();
				String imageFileName = "images/" + CommonUtils.randomNum() + this.subFileSuffix(fileName);
				OSSUploadFileUtils.upload(file, imageFileName);
				params.put("namePy", imageFileName);
			}
			orgMapper.insertOrg(params);
			int orgId = params.getInt("orgId");// 保存成功后返回的主键
			// 超级管理员和全局用户不添加数据权限(创建这个企业的人要有查看它的权限)
			if (!userService.isSuperMan(userId) && !userService.isGlobalMan(userId))
				userService.insertRoleDataByUserId(userId, Sets.newHashSet(orgId), RoleDataEnum.ORG.dataType);
			// 运营类型不为空且为合作运营且有合作运营单位
			if (OPERATE_TYPE_02.equals(operateType) && StringUtils.isNotBlank(coop)) {
				int addCount = coopOperMapper.insertCoopOperByCoopId(orgId, Arrays.asList(coop.split(",")));
				logger.info("保存企业后ID为[{}]新增[{}]条合作运营企业.", orgId, addCount);
			}
			// 新增企业成功后为企业创建企业管理员
            Map user = Maps.newHashMap();
            String orgName = params.getString("orgName");
            if(orgName.contains("企业")){
                user.put("loginName",orgName+"管理员");
            }else{
                user.put("loginName",orgName+"企业管理员");
            }
            user.put("userStateCode", "01");
            user.put("password", "123456");
            user.put("roleIds", 1);
            user.put("createBy", userId);
            user.put("orgIds", orgId);//数据权限
            user.put("orgId", orgId);
            user.put("userType", "01");
            userService.addUser(user);
		}
	}
	
	/**
	 * 对企业表单数据进行数据验证
	 * @param params
	 * @throws BizException
	 */
	private void verifyOrg(DataVo params) throws BizException {
		String orgNo = params.getString("orgNo");// 对企业编码进行补零
		String orgCode = params.getString("orgCode");// 企业组织机构代码
		int orgId = params.getInt("orgId");// 为空
		String memberPhone = params.getString("memberPhone");
		String orgEmail = params.getString("orgEmail");
		String orgPhone = params.getString("orgPhone");
		// 对企业编码格式验证，必须为正整数数字，长度不能大于9位
		CommonUtils.valIsEmpty(orgNo,"企业编码");
		CommonUtils.valIsEmpty(orgCode,"组织机构代码");
		CommonUtils.valIsEmpty(params.getString("orgName"),"企业名称");
//		if (!ValidateUtils.Z_index0(orgNo) || orgNo.length() > 9)
//			throw new BizException(1000014, "企业编码");
		if (orgCode.length()!=9)
			throw new BizException(1006001, "组织机构代码",9);
		orgNo = String.format("%09d", Integer.valueOf(orgNo));//长度不足9位的补零
		orgNameExist(orgId, "org_name", params.getString("orgName"), "企业名称");
		orgNameExist(orgId, "org_no", orgNo, "企业编号");
		orgNameExist(orgId, "org_code", params.getString("orgCode"), "企业组织机构代码");
		
		// 手机号码正则验证
		if (StringUtils.isNotBlank(memberPhone) && !ValidateUtils.Mobile(memberPhone))
			throw new BizException(1000014, "手机号码");
		
		// 邮箱正则验证
		if (StringUtils.isNotBlank(orgEmail) && !ValidateUtils.Email(orgEmail))
			throw new BizException(1000014, "邮箱");
		
		// 电话正则验证
		if (StringUtils.isNotBlank(orgPhone) && !ValidateUtils.Mobile(orgPhone))
			throw new BizException(1000014, "电话");
		params.put("orgNo", orgNo);
	}
	
	/**
	 * 判断文件格式是否符合要求，返回后缀
	 * @param fileName
	 * @return
	 * @throws BizException
	 */
	private String subFileSuffix(String fileName) throws BizException {
		String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		if (".jpg".equals(suffix) || ".png".equals(suffix) || ".gif".equals(suffix))
			return suffix;
		else
			throw new BizException(1006000, "请上传jpg,PNG,gif格式图片");
	}
	
	/**
	 * 更新企业信息
	 *
	 * @param data
	 * @throws BizException
	 */
	public void updateOrg(Map data, MultipartFile file) throws BizException {
		DataVo params = new DataVo(data);
		int orgId = params.getInt("orgId");
		// 更新情况下主键不能为空
		CommonUtils.idIsEmpty(orgId, "更新数据主键ID");
		this.verifyOrg(params);// 对表单数据进行验证
		// 合作运营企业ID字符串拼接(1,2,3,4)
		String coop = params.getString("coopOrgIds");
		// 运营类型
		String operateType = params.getString("operateType");
		if (file != null && !file.isEmpty()) {
			String imageFileName;
			// 旧图片地址
			String namePy = params.getString("namePy");
			// 这个图片能不能直接覆盖呢？先采取先删除后上传，也可下载后比较2个文件的值来确定是否重新上传
			String fileName = file.getOriginalFilename();
			if (StringUtils.isNotBlank(namePy) && namePy.startsWith(CommonUtils.PATH)) {
				imageFileName = namePy.substring(CommonUtils.PATH.length());
				OSSUploadFileUtils.delete(imageFileName);
			}
			imageFileName = "images/" + CommonUtils.randomNum() + this.subFileSuffix(fileName);
			params.set("namePy", imageFileName);
			OSSUploadFileUtils.upload(file, imageFileName);
		}
		// 如果公司类型是总公司，则移除父企业ID
		if (ORG_TYPE_01.equals(params.getString("orgType")))
			params.set("pOrgId", null);
		orgMapper.updatePubOrgByOrgId(params);
		// 更新时如果是合作运营则对选项进行更新，否则删除当前企业的所有合作运营
		if (OPERATE_TYPE_02.equals(operateType))
			this.updateCoopOperDatas(orgId, coop);
		else
			coopOperMapper.deleteCoopOperByCoopId(orgId, null);
	}
	
	/**
	 * 根据企业名称和ID判断是否存在
	 *
	 * @param orgId       企业ID
	 * @param chkFileName 检查字段
	 * @param chkValue    检查值
	 * @param nullMessage 字段名称
	 * @throws BizException
	 */
	private void orgNameExist(Integer orgId, String chkFileName, String chkValue, String nullMessage) throws BizException {
		Integer count = orgMapper.getOrgCountByName(orgId, chkFileName, chkValue);
		if (count > 0)
			throw new BizException(1000011, nullMessage);
		
	}
	
	/**
	 * 运营商下拉树
	 *
	 * @throws BizException
	 */
	public List getOrg(Map map) throws BizException {
		DataVo vo = new DataVo(map);
		if (vo.isBlank("userId")) {
			throw new BizException(1102001, "用户Id");
		}
		Set<Integer> orgIds = userService.getUserRoleDataById(vo.getInt("userId"), RoleDataEnum.ORG.dataType);
		if (orgIds != null) {
			vo.put("orgIds", orgIds);
		}
		List list = orgMapper.getOrg(vo);
		OrgTreeNode node = null;
		List nodeList = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			Map orgMap = (Map) list.get(i);
			node = new OrgTreeNode();
			node.setId(Integer.valueOf(orgMap.get("orgId").toString()));
			node.setName(orgMap.get("orgName").toString());
			node.setOrgNo(orgMap.get("orgNo").toString());
			Object pOrgId = orgMap.get("pOrgId");
			//判断当前节点是否为父节点
			if (pOrgId != null && !"".equals(pOrgId)) {
				boolean flag = false;
				for (int j = 0; j < list.size(); j++) {
					Map org2Map = (Map) list.get(j);
					Object pOrgIdObj = org2Map.get("pOrgId");
					if (pOrgIdObj != null && !"".equals(pOrgIdObj)) {
						if (Integer.valueOf(pOrgId.toString()).intValue() == Integer.valueOf(pOrgIdObj.toString()).intValue()) {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					node.setPid(Integer.valueOf(pOrgId.toString()).intValue());
				} else {
					node.setPid(0);
				}
			} else {
				node.setPid(0);
			}
			nodeList.add(node);
		}
		List<OrgTreeNode> treeList = new Tree().list(nodeList);
		return treeList;
	}
	
	/**
	 * 企业审核获取审核人下拉框
	 *
	 * @return
	 * @throws BizException
	 */
	public List<ComboxVo> getAuditMember() throws BizException {
		return orgMapper.getAuditMember();
	}
	
	/**
	 * 根据登录用户获取可查看的企业下拉
	 *
	 * @param userId
	 * @return
	 * @throws BizException
	 */
	public List<ComboxVo> getOrgNameByUserId(Integer userId) throws BizException {
		Set<Integer> orgIds = userService.getUserRoleDataById(userId, RoleDataEnum.ORG.dataType);
		return orgMapper.getOrgNameById(orgIds);
	}
	
	/**
	 * 企业审核，审核通过后给企业新建角色，并给企业用户赋权限
	 *
	 * @param data
	 * @throws BizException
	 */
	public void updateAuditOrgById(Map data, HttpServletRequest request) throws BizException {
		DataVo params = new DataVo(data);
		//// 用户邮箱
		//String userEmail = params.getString("email");
		//// 用户名
		//String loginName = params.getString("loginName");
		// 审核状态
		String auditStatus = params.getString("auditStatus");
		// 企业名称
		//String orgName = params.getString("orgName");
		// 用户ID
		//int loginId = params.getInt("loginId");
		// 企业ID
		int orgId = params.getInt("orgId");
		// 审核人ID
		int auditMember = params.getInt("auditMember");
		//CommonUtils.idIsEmpty(loginId, "用户ID");
		//CommonUtils.valIsEmpty(userEmail, "用户邮箱");
		//CommonUtils.valIsEmpty(loginName, "用户账号");
		CommonUtils.valIsEmpty(auditStatus, "审核状态");
		//CommonUtils.valIsEmpty(orgName, "企业名称");
		CommonUtils.idIsEmpty(auditMember, "审核人");
		CommonUtils.idIsEmpty(orgId, "企业ID");
		// 只根据企业ID获取企业详情，不从前端传过多冗余信息
		DataVo org = orgMapper.getAuditOrgById(orgId);
		if (org == null || org.isEmpty())
			throw new BizException(1000009);
		
		String now = DateUtils.getDateTime();
		params.set("auditingTime", now);
		String url = "http://" + request.getServerName() + ":" + request.getServerPort();
		orgMapper.updatePubOrgByOrgId(params);
		StringBuilder builder = new StringBuilder();
		String message = "";
		// 审核通过，为企业新增一个角色并为企业用户赋角色
		String loginName = org.getString("loginName");
		if ("1".equals(auditStatus)) {
			// 不判断用户名长度了，用户名不能为空且必须只有有一个字符
			message = "<p>&nbsp;&nbsp;&nbsp;&nbsp;您提交的企业 " + org.getString("orgName") + "&nbsp;&nbsp;注册内容审核已通过！用户名：" + loginName.substring(0, 1) + "***" + loginName.substring(loginName.length() - 1) + "，请妥善保管用户名和密码。</p>";
			// 为用户赋角色
			Map user = Maps.newHashMap();
			user.put("id", org.getInt("userId"));
			user.put("updateBy", auditMember);
			user.put("roleIds", 1);
			user.put("orgIds", orgId);
			user.put("loginName", loginName);
			userService.updateUser(user);
		} else {
			message = "<p>&nbsp;&nbsp;&nbsp;&nbsp;很抱歉，您提交的企业注册资料不全或有误，审核未通过，请您核对后重新注册！</p>";
		}
		builder.append("<p>尊敬的" + loginName + "用户，您好！</p>").append(message).append("<p>&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + url + "'>点击注册</a></p>").append("<p>&nbsp;&nbsp;&nbsp;&nbsp;感谢您使用车电网智慧充电网络云平台，使用过程中遇到问题，请联系我们。</p>").append("<p>&nbsp;&nbsp;&nbsp;&nbsp;服务热线：400-808-6233  网站：http://www.carenergynet.com/</p>").append("<p>&nbsp;&nbsp;&nbsp;&nbsp;(系统邮箱，请勿回复)</p>").append("<p>深圳市车电网络有限公司</p>");
		sendMail(org.getString("email"), builder.toString());
		
	}
	
	//审核之后发送邮件
	public void sendMail(String to, String info) throws BizException {
		if (StringUtils.isBlank(to))
			return;
		//给用户发送邮件
		//SMTP服务器
		String smtp = "smtp.szclou.com";
		//用户名
		String user = "platform";
		//密码
		String password = "szCLOU123";
		//标题
		String subject = "智能充电云平台审核结果";
		//邮件内容
		//发件人邮箱
		String from = "platform@szclou.com";
		//MailUtils.send("smtp.163.com","13277033835@163.com","421182", subject, info, "13277033835@163.com", to);
		MailUtils.send(smtp, user, password, subject, info, from, to);
	}
	
	/**
	 * 企业管理导出
	 *
	 * @param data
	 * @param response
	 * @throws Exception
	 */
	public void exportOrgs(Map data, HttpServletResponse response) throws Exception {
		DataVo params = new DataVo(data);
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId"))
			throw new BizException(1000006);
		// 获取用户运营数据权限
		data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
		List<DataVo> list = orgMapper.getOrgsByPage(data);
		for (DataVo vo : list) {
			vo.set("orgType", dictService.getDictLabel("qyjb", vo.getString("orgType")));
			vo.set("operateType", dictService.getDictLabel("yyms", vo.getString("operateType")));
			vo.set("paymentProcess", dictService.getDictLabel("zflc", vo.getString("paymentProcess")));
		}
		//结果集
		List<String> headList = Lists.newArrayList();
		List<String> valList = Lists.newArrayList();
		headList.add("企业名称");
		headList.add("企业编号");
		headList.add("企业类别");
		headList.add("上级企业名称");
		headList.add("联系电话");
		headList.add("企业地址");
		headList.add("企业邮箱");
		headList.add("经营范围");
		headList.add("运营类型");
		headList.add("支付流程");
		
		valList.add("orgName");
		valList.add("orgNo");
		valList.add("orgType");
		valList.add("pOrgName");
		valList.add("orgPhone");
		valList.add("orgAddr");
		valList.add("orgEmail");
		valList.add("remark");
		valList.add("operateType");
		valList.add("paymentProcess");
		ExportUtils.exportExcel(list, response, headList, valList, "企业管理");
	}
	
	/**
	 * 导出审核未通过和未审核企业列表
	 *
	 * @param data
	 * @param response
	 * @throws Exception
	 */
	public void exportAuditOrgs(Map data, HttpServletResponse response) throws Exception {
		DataVo params = new DataVo(data);
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId"))
			throw new BizException(1000006);
		
		// 根据审核人权限查看
		data.put("auditMember", params.getInt("userId"));
		data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
		List<DataVo> list = orgMapper.getAuditOrgsByPage(data);
		for (DataVo vo : list) {
			vo.set("auditStatus", dictService.getDictLabel("shzt", vo.getString("auditStatus")));
		}
		List<String> headList = Lists.newArrayList();
		List<String> valList = Lists.newArrayList();
		headList.add("审核人");
		headList.add("审核时间");
		headList.add("申请时间");
		headList.add("用户名");
		headList.add("邮箱");
		headList.add("企业编号");
		headList.add("企业名称");
		headList.add("负责人");
		headList.add("联系电话");
		headList.add("审核状态");
		
		valList.add("auditPeople");
		valList.add("auditingTime");
		valList.add("orgisterTime");
		valList.add("userName");
		valList.add("email");
		valList.add("orgNo");
		valList.add("orgName");
		valList.add("orgHead");
		valList.add("memberPhone");
		valList.add("auditStatus");
		ExportUtils.exportExcel(list, response, headList, valList, "企业审核");
	}
	
	/**
	 * 根据orgIds查询orgCode
	 */
	public List<DataVo> getOrgCodeByIds(Set<Integer> orgIds) {
		return orgMapper.getOrgCodeByIds(orgIds);
	}
}

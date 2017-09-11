package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.ConTractService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: ConTractController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月25日
 */
@RestController
@RequestMapping("/api/tracts")
public class ConTractController extends BusinessController {
	
	@Autowired
	ConTractService conTractService;
	
	/**
	 * 
	 * @api {get} /api/tracts    查询合约信息列表
	 * @apiName getConTractAll
	 * @apiGroup ConTractController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据合约名称,场站名称,合约状态,合约类型,审批状态,有效期 ...条件 查询合约信息
	 * <br/>
	 * @apiParam {Integer} userId 用户Id
	 * @apiParam {String}  [conTractName]   合约名称
	 * @apiParam {String}  [stationName]    场站名称
	 * @apiParam {Date}    [conTractExpirationStart]   有效期开始时间 yyyy-MM-dd
	 * @apiParam {Date}    [conTractExpirationEnd]   有效期结束时间 yyyy-MM-dd
	 * @apiParam {Integer} [conTractStatus] 合约状态 0.有效  1.无效
	 * @apiParam {Integer} [conTractType] 合约类型  0.分成合约  1.月结合约
	 * @apiParam {Integer} [contractIncomeType] 收入合约分类:srhyfl
	 * @apiParam {Integer} [contractCostType] 	支出合约分类:zchyfl
	 * @apiParam {Integer} [conTractApproveStatus]  审批状态 0.未通过 1.已通过 2.申请中
	 * @apiParam {Integer} pageNum 页码
	 * @apiParam {Integer} pageSize 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.contractId  合约Id
	 * @apiSuccess {String} data.list.contractName 合约名称
	 * @apiSuccess {Integer} data.list.stationId 场站Id
	 * @apiSuccess {String} data.list.stationName  场站名称
	 * @apiSuccess {Integer} [conTractType] 合约类型  0.收入合约  1.月结合约 2.支出合约
	 * @apiSuccess {Integer} data.list.contractStatus 合约状态 0.有效  1.无效
	 * @apiSuccess {Integer} data.list.contractApproveStatus 审批状态 0.未通过 1.已通过 2.申请中
	 * @apiSuccess {String} data.list.userName  合约申请人
	 * @apiSuccess {Date} data.list.contractUpdateDate  更新时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.list.contractExpirationStart 有效期-开始 yyyy-MM-dd  
	 * @apiSuccess {Date} data.list.contractExpirationEnd 有效期-结束 yyyy-MM-dd
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResultVo getConTractAll(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo pageList = conTractService.getConTractAll(map,"list");
		resVo.setData(pageList);
		return resVo;
	}
	
	/**
	 * 
	 * @throws BizException 
	 * @api {get} /api/tracts/{tractId}   查询合约信息
	 * @apiName getConTractById
	 * @apiGroup ConTractController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据合约id查询合约信息
	 * <br/>
	 * @apiParam {Integer}	userId 		  用户Id
	 * @apiParam {Integer}    tractId     据合约id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Integer} data.contractId    合约Id 主键,自增长
	 * @apiSuccess {String} data.contractName 合约名称
	 * @apiSuccess {Integer(4)} data.contractType 合约类型 0：收入合约 1：月结合约 2.支出合约
	 * @apiSuccess {Integer} data.stationId    合约场站ID
	 * @apiSuccess {String} data.stationNo 场站编号
	 * @apiSuccess {String} data.stationName   场站名称
	 * @apiSuccess {Integer} data.userId 	合约审批人ID
	 * @apiSuccess {Date} data.contractUpdateDate 合约更新日期 yyyy-MM-dd
	 * @apiSuccess {Integer(4)} data.contractPeriod 合约结算周期 0:日 1:月 2:年 3:周
	 * @apiSuccess {String} data.contractDate 合约结算日期 yyyy-MM-dd
	 * @apiSuccess {Date} data.contractExpirationStart 合约有效期-开始 yyyy-MM-dd
	 * @apiSuccess {Date} data.contractExpirationEnd 合约有效期-结束 yyyy-MM-dd
	 * @apiSuccess {Integer(4)} data.contractApproveStatus 合约审批状态 0:未通过 1:已通过 2:申请中
	 * @apiSuccess {Integer(4)} data.contractStatus 合约状态 0:有效1:无效
	 * @apiSuccess {String} data.contractReason 合约未通过理由
	 * @apiSuccess {Integer(4)} data.contractShareType 合约分成项目 0: 充电侧服务费 1.用电侧服务费
	 * @apiSuccess {String} data.contractUserName 合约创建人
	 * @apiSuccess {Date} data.contractDissTime 置无效时间
	 * @apiSuccess {Integer(4)} data.contractIncomeType 收入合约分类srhylx:0.设备收入合约 1.设备运维合约 2.设备租赁合约 3.车位租赁合约 4.土地租赁合约
	 * @apiSuccess {Integer(4)} data.contractCostType 支出合约分类zchylx:0.分成支出合约 1.土地租赁合约 2.土地租赁分成合约 3.房屋租赁合约
	 * @apiSuccess {Double(11)} data.contractServiceCharge 服务费(元/kWh)
	 * @apiSuccess {Integer(4)} data.contractEfficiency 充电效率(1-100整数)
	 * @apiSuccess {Double(11)} data.contractAmount 收入金额
	 * @apiSuccess {String}     data.contractFile 合约文件(支持格式doc/txt/pdf) 
	 * @apiSuccess {Integer(9)} data.contractConsTotal app账户数
	 * @apiSuccess {Double(11)} data.contractConsAmount  app账户金额上限
	 * @apiSuccess {Integer(9)} data.contractCardTotal 后付费卡数
	 * @apiSuccess {Double(11)} data.contractCardAmount  后付费卡金额上限
	 * @apiSuccess {Integer}    data.tractPrice	        合约价类型(月结合约):hyjlx  0.门市价 1.合约电价
	 * @apiSuccess {Double}     data.discount			服务费折扣(月结合约)
	 * @apiSuccess {String}		data.remark	备注
	 * 
	 * @apiSuccess {Object[]} data.conTractCompanyList 合约关联数据
	 * @apiSuccess {String} data.conTractCompanyList.groupName 集团名称 
	 * @apiSuccess {Double} data.conTractCompanyList.percentage 分成比例
	 * @apiSuccess {Integer} data.conTractCompanyList.groupId  集团Id
	 * @apiSuccess {Integer} data.conTractCompanyList.trGroupId  集团Id
	 * @apiSuccess {String} data.conTractCompanyList.cConCompanyName  合约企业名称
	 * @apiSuccess {Integer} data.conTractCompanyList.contractId  合约Id
	 * @apiSuccess {Integer} data.conTractCompanyList.cConCompanyId  合约企业Id
	 * @apiSuccess {Integer} data.conTractCompanyList.trCompanyId  合约企业Id
	 * @apiSuccess {Integer} data.conTractCompanyList.ccid 主键id
	 * @apiSuccess {Object[]} data.stationList 月结合约时场站集合
	 * @apiSuccess {Integer} data.stationList.id 合约场站ID
	 * @apiSuccess {Integer} data.stationList.stationId 合约场站ID
	 * @apiSuccess {Integer} data.stationList.contractId 合约ID
	 * @apiSuccess {String} data.stationList.stationName 合约场站名称
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{tractId}",method = RequestMethod.GET)
	public ResultVo getConTractById(@PathVariable("tractId") Integer tractId) throws BizException{
		ResultVo resVo = new ResultVo();
		Map map = conTractService.getTractById(tractId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * @api {put} /api/tracts/{tractIds}    合约置为无效
	 * @apiName dissConTract
	 * @apiGroup ConTractController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据合约id将合约置为无效
	 * <br/>
	 * @apiParam {Integer[]}    tractIds   合约Id   多个:1,2,3
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{tractIds}",method=RequestMethod.PUT)
	public ResultVo dissConTract(@PathVariable("tractIds") List<Integer> tractIds) throws BizException{
		ResultVo resVo = new ResultVo();
		conTractService.dissConTract(tractIds);
		return resVo;
	}
	
	
	/**
	 * 
	 * @api {get} /api/tracts/_export   导出合约信息
	 * @apiName exportConTract
	 * @apiGroup ConTractController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据合约名称,场站名称,合约状态,合约类型,审批状态,有效期 ...条件 导出合约信息
	 * <br/>
	 * @apiParam {Integer} exportType 	  	导出合约类型 : 区分导出列
	 * @apiParam {String}  [conTractName]   合约名称
	 * @apiParam {String}  [stationName]    场站名称
	 * @apiParam {Date}    [conTractExpirationStart]   有效期开始时间 yyyy-MM-dd
	 * @apiParam {Date}    [conTractExpirationEnd]   有效期结束时间 yyyy-MM-dd
	 * @apiParam {Integer} [conTractStatus] 合约状态 0.有效  1.无效
	 * @apiParam {Integer} [conTractType] 合约类型  0.分成合约  1.月结合约
	 * @apiParam {Integer} [conTractApproveStatus]  审批状态 0.未通过 1.已通过 2.申请中
	 * @apiParam {Integer} [pageNum] 页码
	 * @apiParam {Integer} [pageSize] 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_export",method=RequestMethod.GET)
	public void exportConTract(@RequestParam Map map,HttpServletResponse response) throws Exception{
		conTractService.exportConTract(map, response);
	}
	
	/***
	 * 
	 * @api {post} /api/tracts   新增合约信息
	 * @apiName saveConTract
	 * @apiGroup ConTractController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 新增合约信息
	 * <br/>
	 * @apiParam {Integer}    contractType    合约类型0:分成合约 :月结合约
	 * @apiParam {String}    contractName     合约名称
	 * @apiParam {Integer}    contractPeriod  合约结算周期 0:日 1:月 2:年 3:周
	 * @apiParam {Date}    contractExpirationStart    合约有效期开始
	 * @apiParam {Date}    contractExpirationEnd   	  合约有效期结束
	 * @apiParam {String}    contractDate    合约结算日期
	 * @apiParam {Integer}    userId       	  合约审批人
	 * 
	 * @apiParam {Integer}    contractShareType    合约分成项目 (分成合约)
	 * @apiParam {Integer}    stationId    	 场站Id (分成合约)
	 * @apiParam {String}    companyIdStr    	合约企业IdStr (分成合约) 多个: 1_2_3
	 * @apiParam {String}    percentageStr   	合约企业分成比例 (分成合约) 多个: 1_2_3
	 * 
	 * @apiParam {String}    stationIdStr    	 场站IdStr (月结合约)
	 * @apiParam {String}    groupIdStr   		集团IdStr (月结合约)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResultVo saveConTract(@RequestBody Map map) throws BizException{
		conTractService.saveConTract(map);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {put} /api/tracts   编辑合约信息
	 * @apiName updateConTract
	 * @apiGroup ConTractController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 编辑合约信息
	 * <br/>
	 * @apiParam {Integer}    contractId    合约Id
	 * @apiParam {Integer}    [contractType]    合约类型0:分成合约 1:月结合约
	 * @apiParam {String}    [contractName]     合约名称
	 * @apiParam {Integer}    [contractPeriod]  合约结算周期 0:日 1:月 2:年 3:周
	 * @apiParam {Date}    [contractExpirationStart]    合约有效期开始
	 * @apiParam {Date}    [contractExpirationEnd]   	  合约有效期结束
	 * @apiParam {String}    [contractDate]    合约结算日期
	 * @apiParam {Integer}    [userId]       	  合约审批人
	 * 
	 * @apiParam {Integer}    [contractShareType]    合约分成项目 (分成合约)
	 * @apiParam {Integer}    [stationId]   	 场站Id (分成合约)
	 * @apiParam {String}    [companyIdStr]    	合约企业IdStr (分成合约) 多个: 1_2_3
	 * 
	 * @apiParam {String}    [stationIdStr]    	 场站IdStr (月结合约) 多个: 1_2_3
	 * @apiParam {String}    [groupIdStr]   		集团IdStr (月结合约) 多个: 1_2_3
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public ResultVo updateConTract(@RequestBody Map map) throws BizException{
		conTractService.updateConTract(map);
		return new ResultVo(); 
	}
	
	/**
	 * 
	 * @throws BizException 
	 * @api {put} /api/tracts/_approve    合约审批
	 * @apiName contractApprove
	 * @apiGroup ConTractController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 合约审批
	 * <br/>
	 * @apiParam {Integer}    contractId    合约Id
	 * @apiParam {Integer}    contractApproveStatus    合约审批状态 0:拒绝 1:通过
	 * @apiParam {String}    [contractReason]    合约审批不通过理由
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001  {0}不能为空!
	 */
	@RequestMapping(value="_approve",method=RequestMethod.PUT)
	public ResultVo contractApprove(@RequestBody Map map) throws BizException{
		conTractService.contractApprove(map);
		return new ResultVo();
	}
	

	/***
	 * 
	 * @api {post} /api/tracts   新增合约信息
	 * @apiName saveConTract
	 * @apiGroup ConTractController
	 * @apiVersion 1.5.3
	 * @apiDescription 杨帅B 新增合约信息
	 * <br/>
	 * @apiParam Integer userId 用户Id
	 * @apiParam {Integer}    contractType    合约类型  hylx  0:收入合约 1:月结合约 2.分成合约
	 * @apiParam {String}    contractName     合约名称
	 * @apiParam {Integer}    contractPeriod  合约结算周期 0:日 1:月 2:年 3:周
	 * @apiParam {Date}    contractExpirationStart    合约有效期开始
	 * @apiParam {Date}    contractExpirationEnd   	  合约有效期结束
	 * @apiParam {Integer(4)} contractIncomeType 收入合约分类srhylx:0.设备收入合约 1.设备运维合约 2.设备租赁合约 3.车位租赁合约 4.土地租赁合约
	 * @apiParam {Integer(4)} contractCostType 支出合约分类zchylx:0.分成支出合约 1.土地租赁合约 2.土地租赁分成合约 3.房屋租赁合约	
	 * @apiParam {String}    contractDate    合约结算日期
	 * @apiParam {Integer}    userAppId       	  合约审批人
	 * @apiParam {String}	  contractUserName 	 	创建人
	 * @apiParam {String}	  [remark] 	 	备注
	 * @apiParam {MultipartFile} file 合约文件(txt/pdf/doc)
	 * @apiParam {Integer}   [contractEfficiency] 充电效率   
	 * 
	 * @apiParam {Integer}    contractShareType    合约分成项目
	 * @apiParam {Integer}    stationId    	 场站Id (分成合约)
	 * @apiParam {String}    companyIdStr    	合约企业IdStr_分成比例 (分成合约) 多个: 1_2,1_3
	 * @apiParam {Integer}   contractIncomeType  合约分类
	 * @apiParam {Double}	 [contractServiceCharge]  服务费(元/kWh)
	 * @apiParam {Double}	 contractAmount   收入金额 (当合约为固定合约时此为必填项)
	 * 
	 * @apiParam {Integer}	 tractPrice		合约价类型(月结合约):hyjlx  0.门市价 1.合约电价
	 * @apiParam {Double}	 [discount]			服务费折扣(月结合约)
	 * @apiParam {String}    stationIdStr    	 场站IdStr (月结合约)
	 * @apiParam {String}    groupIdStr   		集团IdStr (月结合约)
	 * @apiParam {Integer}   contractConsTotal  app账户数
	 * @apiParam {Double}   contractConsAmount  app账户金额上限
	 * @apiParam {Integer}  contractCardTotal 后付费卡数
	 * @apiParam {Double}   contractCardAmount 后付费卡金额上限
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001 {0}不能为空!
	 * @apiError -1105001 不支持的合约文件格式
	 */
	@RequestMapping(params="ver=1.5.3",method=RequestMethod.POST)
	public ResultVo saveConTract153(MultipartHttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (String key : parameterMap.keySet()) {
			String[] param =parameterMap.get(key);
			map.put(key, param[0]);
			//System.err.println(key+":"+param[0]);
		}
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		conTractService.saveConTract153(map,file);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {put} /api/tracts/_update    编辑合约信息
	 * @apiName updateConTract153
	 * @apiGroup ConTractController
	 * @apiVersion 1.5.3
	 * @apiDescription 杨帅B 编辑合约信息
	 * <br/>
	 * @apiParam {Integer}    userId    用户Id
	 * @apiParam {Integer}    contractId    合约Id
	 * @apiParam {Integer}    contractType    合约类型  hylx  0:收入合约 1:月结合约 2.分成合约
	 * @apiParam {String}    contractName     合约名称
	 * @apiParam {Integer}    contractPeriod  合约结算周期 0:日 1:月 2:年 3:周
	 * @apiParam {Date}    contractExpirationStart    合约有效期开始
	 * @apiParam {Date}    contractExpirationEnd   	  合约有效期结束
	 * @apiSuccess {Integer(4)} data.contractIncomeType 收入合约分类srhylx:0.设备收入合约 1.设备运维合约 2.设备租赁合约 3.车位租赁合约 4.土地租赁合约
	 * @apiSuccess {Integer(4)} data.contractCostType 支出合约分类zchylx:0.分成支出合约 1.土地租赁合约 2.土地租赁分成合约 3.房屋租赁合约
	 * @apiParam {String}    contractDate    合约结算日期
	 * @apiParam {Integer}    userAppId       	  合约审批人
	 * @apiParam {String}	  contractUserName 	 创建人
	 * @apiParam {String}	  [remark] 	 	备注
	 * @apiParam {MultipartFile} file 合约文件(txt/pdf/doc)
	 * @apiParam {Integer}   contractEfficiency 充电效率   (当合约为分成合约时此为必填项)
	 * 
	 * @apiParam {Integer}    contractShareType    合约分成项目
	 * @apiParam {Integer}    stationId    	 场站Id (分成合约)
	 * @apiParam {String}    companyIdStr    	合约企业IdStr (分成合约) 多个: 1_2_3
	 * @apiParam {String}    percentageStr   	合约企业分成比例 (分成合约) 多个: 1_2_3
	 * @apiParam {Integer}   contractIncomeType  合约分类
	 * @apiParam {Double}	 [contractServiceCharge]  服务费(元/kWh)
	 * @apiParam {Double}	 contractAmount   收入金额 (当合约为固定合约时此为必填项)
	 * 
	 * @apiParam {Integer}	 tractPrice		合约价类型(月结合约):hyjlx  0.门市价 1.合约电价
	 * @apiParam {Double}	 [discount]			服务费折扣(月结合约) 
	 * @apiParam {String}    stationIdStr    	 场站IdStr (月结合约)
	 * @apiParam {String}    groupIdStr   		集团IdStr (月结合约)
	 * @apiParam {Integer}   contractConsTotal  app账户数
	 * @apiParam {Double}   contractConsAmount  app账户金额上限
	 * @apiParam {Integer}  contractCardTotal 后付费卡数
	 * @apiParam {Double}   contractCardAmount 后付费卡金额上限
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001 {0}不能为空!
	 * @apiError -1105001 不支持的合约文件格式
	 */
	@RequestMapping(value = "_update", params="ver=1.5.3",method=RequestMethod.POST)
	/*public ResultVo updateConTract153(@RequestBody Map map,HttpServletRequest request,@RequestParam("file") MultipartFile file) throws BizException{
		MultipartRequest multipartRequest=(MultipartRequest) request;
		file=multipartRequest.getFile("file");
		conTractService.updateConTract153(map,file);
		return new ResultVo(); 
	}*/
	
	public ResultVo updateConTract153(MultipartHttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (String key : parameterMap.keySet()) {
			String[] param =parameterMap.get(key);
			map.put(key, param[0]);
		}
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		conTractService.updateConTract153(map,file);
		return new ResultVo();
	}
	
}

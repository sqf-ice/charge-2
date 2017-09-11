package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.CompanyService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 描述: CompanyController 合约企业
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月24日
 */
@RestController
@RequestMapping("/api/companys")
public class CompanyController extends BusinessController {
		
		@Autowired
		CompanyService companyService;
		
		/**
		 * 
		 * @api {get} /api/companys    查询合约企业列表
		 * @apiName getCompanyAll
		 * @apiGroup CompanyController
		 * @apiVersion 2.0.0
		 * @apiDescription 杨帅B 根据合约企业名称,更新日期... 查询合约企业列表
		 * <br/>
		 * @apiParam {Integer} userId 用户Id
		 * @apiParam {String} [cConCompanyName] 合约企业名称
		 * @apiParam {String} [conUpdateDateLt] 更新日期 - 开始 yyyy-MM-dd
		 * @apiParam {String} [conUpdateDateRt] 更新日期 - 结束 yyyy-MM-dd
		 * @apiParam {Integer(9)} pageNum 页码
		 * @apiParam {Integer(9)} pageSize 页大小
		 * <br/>
		 * @apiSuccess {String} errorCode   错误码
		 * @apiSuccess {String} errorMsg    消息说明
		 * @apiSuccess {Object} data        分页数据封装
		 * @apiSuccess {Integer} data.total     总记录数
		 * @apiSuccess {Object[]} data.list 分页数据对象数组
		 * @apiSuccess {Integer(9)} data.list.cConCompanyId 合约企业ID
		 * @apiSuccess {String} data.list.cConCompanyName 合约企业名称
		 * @apiSuccess {Integer(9)} data.list.orgId 所属企业ID
		 * @apiSuccess {Date} data.list.cConCreateDate 合约企业创建日期  yyyy-MM-dd HH:mm:ss
		 * @apiSuccess {Date} data.list.cConUpdateDate 合约企业更新日期  yyyy-MM-dd HH:mm:ss
		 * @apiSuccess {Integer(11)} data.list.cConStatus 合约企业状态 0:有效  1:无效
		 * <br/>
		 * @apiError -999  系统异常
		 * @apiError -888  请求方式异常
		 */
		@RequestMapping(method=RequestMethod.GET)
		public ResultVo getCompanyAll(@RequestParam Map map) throws BizException{
			ResultVo resVo = new ResultVo();
			PageInfo pageList = companyService.getCompanyAll(map);
			resVo.setData(pageList);
			return resVo;
		}
		
		/**
		 * 
		 * @api {get} /api/companys/{companyId}    查询合约企业信息
		 * @apiName getCompanyById
		 * @apiGroup CompanyController
		 * @apiVersion 2.0.0
		 * @apiDescription 杨帅B 根据合约企业companyId查询合约企业信息
		 * <br/>
		 * @apiParam {Integer}    companyId    合约企业Id
		 * <br/>
		 * @apiSuccess {String} errorCode   错误码
		 * @apiSuccess {String} errorMsg    消息说明
		 * @apiSuccess {Object} data        数据封装
		 * @apiSuccess {Integer(9)} data.cConCompanyId 合约企业ID
		 * @apiSuccess {String} data.cConCompanyName 合约企业名称
		 * @apiSuccess {Integer(9)} data.orgId 所属企业ID
		 * @apiSuccess {Integer(9)} data.userId 合约企业管理员ID
		 * @apiSuccess {String} data.cConAccountId 合约企业账户
		 * @apiSuccess {String} data.cConPassword 合约企业账户密码
		 * @apiSuccess {String} data.cConAlipay 合约企业支付宝
		 * @apiSuccess {String} data.cConWechat 合约企业微信
		 * @apiSuccess {String} data.cConBankaccount 合约企业银行账号
		 * @apiSuccess {String} data.cConTel 合约企业联系电话
		 * @apiSuccess {String} data.cConEmail 合约企业邮箱
		 * @apiSuccess {String} data.cConAddress 合约企业地址
		 * @apiSuccess {Double} data.cConAmount 合约企业账户余额
		 * @apiSuccess {Date} data.cConCreateDate 合约企业创建日期 yyyy-MM-dd HH:mm:ss
		 * @apiSuccess {Date} data.cConUpdateDate 合约企业更新日期 yyyy-MM-dd HH:mm:ss
		 * @apiSuccess {Double} data.cConIncome 企业总收入
		 * @apiSuccess {Double} data.cConCashout 合约企业总提现
		 * @apiSuccess {String} data.cConAccountStatus 合约企业账户状态01：正常 02：冻结 03:注销
		 * @apiSuccess {Integer(11)} data.cConStatus 合约企业状态 0:有效  1:无效
		 * <br/>
		 * @apiError -999  系统异常
		 * @apiError -888  请求方式异常
		 */
		@RequestMapping(value="{companyId}",method = RequestMethod.GET)
		public ResultVo getCompanyById(@PathVariable("companyId") Integer companyId) throws Exception{
			ResultVo resVo = new ResultVo();
			Map map = companyService.getCompanyById(companyId);
			resVo.setData(map);
			return resVo; 
		}
		
		/**
		 * 
		 * @throws BizException 
		 * @api {get} /api/companys/company   查询合约企业列表(业务字典)
		 * @apiName getCompany
		 * @apiGroup BusiDicts
		 * @apiVersion 2.0.0
		 * @apiDescription 杨帅B 根据用户Id,合约企业名称查询合约企业列表(业务字典)
		 * <br/>
		 * @apiParam {Integer}    userId     用户Id
		 * @apiParam {String}    [companyName]     合约企业名称
		 * <br/>
		 * @apiSuccess {String} errorCode   错误码
		 * @apiSuccess {String} errorMsg    消息说明
		 * @apiSuccess {Object} data        分页数据封装
		 * @apiSuccess {Object[]} data.list 分页数据对象数组
		 * @apiSuccess {Integer} data.list.cConCompanyId 合约企业Id
		 * @apiSuccess {String} data.list.cConCompanyName 合约企业名称
		 * <br/>
		 * @apiError -999  系统异常
		 * @apiError -888  请求方式异常
		 */
		@RequestMapping(value="company",method=RequestMethod.GET)
		public ResultVo getCompany(@RequestParam Map map) throws BizException{
			ResultVo resVo = new ResultVo();
			PageInfo pageList = companyService.getCompany(map);
			resVo.setData(pageList);
			return resVo;
		}

	/**
	 *
	 * @throws BizException
	 * @api {get} /api/companys/dict   用户管理模块查询合约企业列表(业务字典)
	 * @apiName getCompanyDict
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅A 根据用户Id,合约企业名称查询合约企业列表(业务字典)
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {String}    [companyName]     合约企业名称
	 * @apiParam {Integer}    [limit]     加载数量
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Object[]} data.list.id 合约企业Id
	 * @apiSuccess {Object[]} data.list.text 合约企业名称
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
		@RequestMapping(value = "/dict", method = RequestMethod.GET)
		public ResultVo getCompanyDict(@RequestParam Map map) throws BizException {
			ResultVo resVo = new ResultVo();
			resVo.setData(companyService.getCompanyDict(map));
			return resVo;
		}

		/**
		 * 
		 * @api {post} /api/companys   新增合约企业
		 * @apiName saveCompany
		 * @apiGroup CompanyController
		 * @apiVersion 2.0.0
		 * @apiDescription 杨帅B 新增合约企业
		 * <br/>
		 * @apiParam {String}    cConCompanyName    合约企业名称
		 * @apiParam {Integer}    orgId    运营商Id
		 * @apiParam {String}    [cConAlipay]    收款支付宝账号
		 * @apiParam {String}    [cConWechat]    收款微信账号
		 * @apiParam {String}    [cConBankaccount]    收款银行账号
		 * @apiParam {String}    cConTel    联系电话
		 * @apiParam {String}    [cConEmail]    邮箱
		 * @apiParam {String}    [cConAddress]   合约企业地址
		 * <br/>
		 * @apiSuccess {String} errorCode   错误码
		 * @apiSuccess {String} errorMsg    消息说明
		 * @apiSuccess {Object} data        分页数据封装
		 * <br/>
		 * @apiError -999  系统异常
		 * @apiError -888  请求方式异常
		 */
		@RequestMapping(method =RequestMethod.POST)
		public ResultVo saveCompany(@RequestBody Map map) throws BizException{
			companyService.saveCompany(map);
			return new ResultVo();
		}
		
		
		/**
		 * 
		 * @api {put} /api/companys    编辑合约企业
		 * @apiName updateCompany
		 * @apiGroup CompanyController
		 * @apiVersion 2.0.0
		 * @apiDescription 杨帅B 编辑合约企业
		 * <br/>
		 * @apiParam {Integer}    cConCompanyId     合约企业Id
		 * @apiParam {String}    [cConCompanyName]    合约企业名称
		 * @apiParam {Integer}   [orgId]    运营商Id
		 * @apiParam {String}    [cConAlipay]    收款支付宝账号
		 * @apiParam {String}    [cConWechat]    收款微信账号
		 * @apiParam {String}    [cConBankaccount]    收款银行账号
		 * @apiParam {String}    [cConTel]    联系电话
		 * @apiParam {String}    [cConEmail]    邮箱
		 * @apiParam {String}    [cConAddress]   合约企业地址
		 * <br/>
		 * @apiSuccess {String} errorCode   错误码
		 * @apiSuccess {String} errorMsg    消息说明
		 * @apiSuccess {Object} data        分页数据封装
		 * <br/>
		 * @apiError -999  系统异常
		 * @apiError -888  请求方式异常
		 */
		@RequestMapping(method=RequestMethod.PUT)
		public ResultVo updateCompany(@RequestBody Map map) throws BizException{
			companyService.updateCompany(map);
			return new ResultVo();
		}
		
		/**
		 * 
		 * 
		 * @throws Exception 
		 * @api {get} /api/companys/_export     导出合约企业信息
		 * @apiName exportCompany
		 * @apiGroup CompanyController
		 * @apiVersion 2.0.0
		 * @apiDescription 杨帅B 导出合约企业信息
		 * <br/>
		 * @apiParam {Integer} userId 用户Id
		 * @apiParam {String} [cConCompanyName] 合约企业名称
		 * @apiParam {String} [conUpdateDateLt] 更新日期 - 开始 yyyy-MM-dd
		 * @apiParam {String} [conUpdateDateRt] 更新日期 - 结束 yyyy-MM-dd
		 * @apiParam {Integer} [pageNum] 页码
		 * @apiParam {Integer} [pageSize] 页大小
		 * <br/>
		 * @apiError -999  系统异常
		 * @apiError -888  请求方式异常
		 */
		@RequestMapping(value="_export",method=RequestMethod.GET)
		public void exportCompany(@RequestParam Map map,HttpServletResponse response) throws Exception{
			companyService.exportCompany(map,response);
		}
		
}

package com.clouyun.charge.modules.charge.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.clouyun.boot.common.exception.BizException;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.service.PriceModelService;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述: 电价设置控制器 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: lips <lips@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年4月14日
 */
@RestController
@RequestMapping("/api/priceModel")
public class PriceModelController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(PriceModelController.class);
	
	@Autowired
	PriceModelService priceModelService;
	/**
	 * @api {post} /api/priceModel/priceModel   电价模板新增
	 * @apiName addPriceModel
	 * @apiGroup PriceModelController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯  电价模板新增 传入json数据{org:"",modelTime:[{timeStart:"",modelTime:""},]}
	 * <br/>
	 * @apiParam {int}            orgId                             运营商id
	 * @apiParam {String}        prcName                             模板名称
	 * @apiParam {int}          agreement                             协议Id(51,表示深圳电动汽车充电协议；62，表示科陆电动汽车充电协议)
	 * @apiParam {String}        userId                             用户id
	 * @apiParam {int}        prcTypeCode                        电价类型(1.单电价 2.多费率 3.单电价固定费率 4.单电价比例费率 5. 多费率固定 6.多费率比例)
	 * @apiParam {Double}        prcZxygz1                             费率1
	 * @apiParam {Double}        prcZxygz2                             费率2
	 * @apiParam {Double}        prcZxygz3                             费率3
	 * @apiParam {Double}        prcZxygz4                             费率4
	 * @apiParam {Double}        prcService                           服务费
	 * @apiParam {List}        modelTime                          费率时间（科陆电动汽车充电协议使用）
	 * @apiParam {Date}        modelTime.timeStart                         开始时间（hh:mm）
	 * @apiParam {Date}        modelTime.timeEnd                        结束时间（hh:mm）
	 * @apiParam {Date}        modelTime.feilv                       费率模板的费率名称（1,2,3,4）
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		   删除模板成功返回值
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */

	@RequestMapping(value = "/priceModel", method = RequestMethod.POST)
	public ResultVo addPriceModel(@RequestBody Map map) throws Exception {
		ResultVo resultVo = new ResultVo();
		DataVo dataVo = new DataVo(map);
       String[] list = {"orgId","prcName","userId","prcTypeCode","prcService"};
       Map map1 = new HashMap();
		map1.put("orgId","运营商id");
		map1.put("prcName","模板名称");
		map1.put("userId","用户Id");
		map1.put("prcTypeCode","电价类型");
		map1.put("prcService","服务费");
         ChargeManageUtil.isMapNullPoint(list,dataVo,map1);
		 priceModelService.addPriceModel(dataVo);
		return resultVo;
	}
	
	 /**
     * @api {get} /api/priceModel/priceModel   电价模板查询
     * @apiName selectPriceModel
     * @apiGroup PriceModelController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   电价模板查询
     * <br/>
	 * @apiParam {Int}         userId                                 用户Id
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
     * <br/>
     * @apiSuccess {String}       errorCode                        错误码
     * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
     * @apiSuccess {String}       data.list.prcId                     主键(列表id)
     * @apiSuccess {String}       data.list.createTime                创建时间(yyyy-mm-dd HH:mm:ss)
     * @apiSuccess {String}       data.list.userName                  创建人
     * @apiSuccess {String}       data.list.prcName                   模板名称
     * @apiSuccess {String}       data.list.orgName                   运营商
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000  参数缺失
     */

	
	@RequestMapping(value = "/priceModel", method = RequestMethod.GET)
	public ResultVo selectPriceModel(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=priceModelService.selectPriceModel(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"userId");
		}
		return resultVo;
	}
	
	
	 /**
     * @api {get} /api/priceModel/priceModel/_export   电价模板导出
     * @apiName exportPriceModel
     * @apiGroup PriceModelController
     * @apiVersion 2.0.0
     * @apiDescription 易凯   电价模板导出
     * <br/>
	 * @apiParam {Int}         userId                                 用户Id
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1700000  参数缺失
     */
	
	
	@RequestMapping(value = "/priceModel/_export", method = RequestMethod.GET)
	public void exportPriceModel(@RequestParam Map map,HttpServletResponse response) throws Exception {
		DataVo dataMap =  new DataVo(map);
		if(dataMap.isNotBlank("userId")){
			priceModelService.exportPriceModel(dataMap,response);
		}else {
			throw   new BizException(1700000,"userId");
		}

	}

	 /**
    * @api {get} /api/priceModel/priceTask    电价任务查询
    * @apiName selectDjrw
    * @apiGroup PriceModelController
    * @apiVersion 2.0.0
    * @apiDescription 易凯   电价任务查询
    * <br/>
	* @apiParam {Int}         userId                                 用户Id
    * @apiParam {Int}        [taskId]                               任务Id
	* @apiParam {String}        [taskName]                               任务名称 模糊查询
    * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
    * @apiParam {Date}        [endDate]                               结束时间(yyyy-mm-dd)
    * @apiParam {Int}        [prcId]                            模板名称
	* @apiParam {Int}        [prcName]                            模板Id 模糊查询
	* @apiParam {Int}           pageNum                               页码
	* @apiParam {Int}           pageSize                              页大小
    * <br/>
    * @apiSuccess {String}       errorCode                        错误码
    * @apiSuccess {String}       errorMsg                         消息说明
	* @apiSuccess {Object}         data                    		      分页数据封装
	* @apiSuccess {Integer}        data.total                    		      总记录数
	* @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
    * @apiSuccess {Int}       data.list.taskId                  	   主键(列表Id)
    * @apiSuccess {String}       data.list.prcId                      模板编号
    * @apiSuccess {String}       data.list.prcName                    模板名称
    * @apiSuccess {String}       data.list.taskExecFlag               是否执行
    * @apiSuccess {Date}       data.list.taskExecTime               执行时间(yyyy-mm-dd hh:mm:ss)
    * @apiSuccess {String}       data.list.taskName                   任务名称
	* <br/>
	* @apiError -999 系统异常!
	* @apiError -888 请求方式异常!
	* @apiError -1700000  参数缺失
    */
	
	@RequestMapping(value = "/priceTask", method = RequestMethod.GET)
	public ResultVo selectDjrw(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=priceModelService.selectDjrw(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"userId");
		}
		return resultVo;
	}

	/**
	 * @api {get} /api/priceModel/price/task    电价任务业务字典
	 * @apiName priceTask
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   电价任务业务字典
	 * <br/>
	 * @apiParam {Int}         userId                                 用户Id
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Int}       data.list.id                  	   主键
	 * @apiSuccess {String}       data.list.text                   任务名称
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000  参数缺失
	 */

	@RequestMapping(value = "/price/task", method = RequestMethod.GET)
	public ResultVo priceTask(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=priceModelService.priceTask(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"userId");
		}
		return resultVo;
	}
	
	 /**
	    * @api {get} /api/priceModel/priceTask/_export    电价任务导出
	    * @apiName exportDjrw
	    * @apiGroup PriceModelController
	    * @apiVersion 2.0.0
	    * @apiDescription 易凯   电价任务导出
	    * <br/>
	    * @apiParam {Int}         userId                                 用户Id
	    * @apiParam {Int}        [taskId]                               任务Id
	    * @apiParam {String}        [taskName]                               任务名称 模糊查询
	    * @apiParam {Date}        [startDate]                             开始时间(yyyy-mm-dd)
	    * @apiParam {Date}        [endDate]                               结束时间(yyyy-mm-dd)
	    * @apiParam {Int}        [prcId]                            模板名称
	    * @apiParam {Int}        [prcName]                            模板Id 模糊查询
	    * @apiParam {Int}           pageNum                               页码
	    * @apiParam {Int}           pageSize                              页大小
	    * <br/>
	    * @apiSuccess {String} errorCode   错误码
	    * @apiSuccess {String} errorMsg    消息说明
	    * @apiSuccess {Object} data        分页数据封装
	    * @apiSuccess {Integer} data.total     总记录数
	    * @apiSuccess {Object[]} data.list 分页数据对象数组
	    * <br/>
	    * @apiError -999  系统异常
	    * @apiError -888  请求方式异常
	    * @apiError -1700000  参数缺失
	    */
		
		@RequestMapping(value = "/priceTask/_export", method = RequestMethod.GET)
		public void exportDjrw(@RequestParam Map map,HttpServletResponse response) throws Exception {
			DataVo dataMap =  new DataVo(map);
			if(dataMap.isNotBlank("userId")){
				priceModelService.exportDjrw(dataMap,response);
			}else {
				throw   new BizException(1700000,"userId");
			}

		}
	 /**
	    * @api {delete} /api/priceModel/priceModel/{prcId}   电价模板删除
	    * @apiName deletePriceModel
	    * @apiGroup PriceModelController
	    * @apiVersion 2.0.0
	    * @apiDescription 易凯  电价模板删除
	    * <br/>
	    * @apiParam {String}        prcId                             电价模板id
	    * <br/>
	    * @apiSuccess {String}       errorCode                        错误码
	    * @apiSuccess {String}       errorMsg                         消息说明
	    * @apiSuccess {String}       data                    		   删除模板成功返回值
		* <br/>
	    * @apiError -999 系统异常!
	    * @apiError -888 请求方式异常!
	    */
	
	@RequestMapping(value = "/priceModel/{prcId}", method = RequestMethod.DELETE)
	public ResultVo deletePriceModel(@PathVariable("prcId") String prcId) throws Exception {
		DataVo data = new DataVo();
		data.put("prcId", prcId);
		Integer count=0;
		count=priceModelService.delPriceModel(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(count);
		return resultVo;
	}
	
	

	 /**
	    * @api {delete} /api/priceModel/priceTask/{taskId}   电价任务删除
	    * @apiName deletePriceTask
	    * @apiGroup PriceModelController
	    * @apiVersion 2.0.0
	    * @apiDescription 易凯 电价任务删除
	    * <br/>
	    * @apiParam {String}        prcId                             电价任务id
	    * <br/>
	    * @apiSuccess {String}       errorCode                        错误码
	    * @apiSuccess {String}       errorMsg                         消息说明
	    * @apiSuccess {String}       data                    		   删除模板成功返回值
		* <br/>
	    * @apiError -999 系统异常!
	    * @apiError -888 请求方式异常!
	    */
	
	@RequestMapping(value = "/priceTask/{taskId}", method = RequestMethod.DELETE)
	public ResultVo deletePriceTask(@PathVariable("taskId") String taskId) throws Exception {
		Map data = new HashMap();
		data.put("taskId",taskId);
		Integer count=0;
		count=priceModelService.delPriceTask(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(count);
		return resultVo;
	}
	
	

	 /**
	    * @api {get} /api/priceModel/{prcId}   电价模板详情
	    * @apiName detailPriceModel
	    * @apiGroup PriceModelController
	    * @apiVersion 2.0.0
	    * @apiDescription 易凯  电价模板详情
	    * <br/>
	    * @apiParam {String}         prcId                             电价模板id(prcId)
	    * <br/>
	    * @apiSuccess {String}       errorCode                        错误码
	    * @apiSuccess {String}       errorMsg                         消息说明
	    * @apiSuccess {String}       data                    		   查询模板成功返回值
	    * @apiSuccess {int}       data.prcTypeCode        		  电价类型(1.单电价 2.多费率 3.单电价固定费率 4.单电价比例费率 5. 多费率固定 6.多费率比例)
	    * @apiSuccess {Double}       data.prcZxygz1          		 费率1
	     *@apiSuccess {Double}        prcService                           服务费
	    * @apiSuccess {Double}       data.prcZxygz2                                                   费率2
	    * @apiSuccess {Double}       data.prcZxygz3                                                   费率3
	    * @apiSuccess {Double}       data.prcZxygz4                                                    费率4
	    * @apiSuccess {String}       data.agreementName             通讯协议
	    * @apiSuccess {String}       data.orgName                   运营商
	    * @apiSuccess {list}       data.modelTimeList             费率时间段(返回一个list集合,timeStart：开始时间,timeEnd:结束时间,feiLv:费率)
		* <br/>
	    * @apiError -999 系统异常!
	    * @apiError -888 请求方式异常!
	    */
	
	
	@RequestMapping(value = "/{prcId}", method = RequestMethod.GET)
	public ResultVo detailPriceModel(@PathVariable("prcId") String prcId) throws Exception {
		  Map data = new HashMap();
		  data.put("prcId",prcId);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(priceModelService.detailPriceModel(data));
		return resultVo;
		
	}
	
	 /**
	    * @api {get} /api/priceModel/priceTask/{taskId}   电价任务详情
	    * @apiName detailPriceTask
	    * @apiGroup PriceModelController
	    * @apiVersion 2.0.0
	    * @apiDescription 易凯  电价任务详情
	    * <br/>
	    * @apiParam {String}         taskId                         电价任务id(列表taskId)
	    * <br/>
	    * @apiSuccess {String}       errorCode                        错误码
	    * @apiSuccess {String}       errorMsg                         消息说明
	    * @apiSuccess {String}       data                    		   查询模板成功返回值
	    * @apiSuccess {String}       data.orgName        		    运营商          
	    * @apiSuccess {String}       data.taskName               任务名称         
	    * @apiSuccess {String}       data.prcName                模板名称
	    * @apiSuccess {String}       data.execDate				  执行时间                                                  
	    * @apiSuccess {String}       data.priceDes               电费描述
	    * @apiSuccess {String}       data.prcServiceDes          服务费描述
	    * @apiSuccess {String}       data.pileNames              已执行设备
	    * @apiSuccess {String}       data.noPileNames           未执行设备
	    * 
		* <br/>
	    * @apiError -999 系统异常!
	    * @apiError -888 请求方式异常!
	    */

	@RequestMapping(value = "/priceTask/{taskId}", method = RequestMethod.GET)
	public ResultVo detailPriceTask(@PathVariable("taskId") String taskId) throws Exception {
		Map data = new HashMap();
		data.put("taskId", taskId);
		//根据taskId查询电价任务信息
		DataVo dataVo=priceModelService.queryDjrw(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(dataVo);
		return resultVo;
		
	}
	
	

	 /**
	    * @api {post} /api/priceModel/priceTask   电价任务新增
	    * @apiName savePriceTask
	    * @apiGroup PriceModelController
	    * @apiVersion 2.0.0
	    * @apiDescription 易凯  电价任务新增
	    * <br/>
	    * @apiParam {String}        orgId                             运营商
	    * @apiParam {String}        taskName                          任务名称
	    * @apiParam {String}        prcId                             模板Id
	    * @apiParam {String}        execDate                          执行时间(yyyy-mm-dd)
	    * @apiParam {String}        priceDes                          电费描述
	    * @apiParam {String}        prcServiceDes                     服务费描述
	    * @apiParam {String}        pileIds                           下发范围
	    * @apiParam {String}        userId                           操作用户Id
	    * <br/>
	    * @apiSuccess {String}       errorCode                        错误码
	    * @apiSuccess {String}       errorMsg                         消息说明
	    * @apiSuccess {String}       data                    		   新增电价任务成功返回值
		* <br/>
	    * @apiError -999 系统异常!
	    * @apiError -888 请求方式异常!
	    */
	
	@RequestMapping(value = "/priceTask", method = RequestMethod.POST)
	public ResultVo savePriceTask(@RequestBody Map data) throws Exception {
		String[] list = {"orgId","taskName","prcId","execDate","pileIds","userId"};
		Map  listMap = new HashMap();
		listMap.put("orgId","运营商Id");
		listMap.put("taskName","任务名称");
		listMap.put("prcId","模板Id");
		listMap.put("execDate","执行时间");
		listMap.put("pileIds","下发范围");
		listMap.put("userId","用户Id");
		DataVo dataVo = new DataVo(data);
		ChargeManageUtil.isMapNullPoint(list,dataVo,listMap);
		ResultVo resultVo = new ResultVo();
		priceModelService.savePriceTask(dataVo);
		return resultVo;
	}

	/**
	 * @api {get} /api/priceModel/_describe/{pileId}    电价描述
	 * @apiName describetPriceModel
	 * @apiGroup PriceModelController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   电价描述
	 * <br/>
	 * @apiParam {String}        pileId                            电价Id
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		   查询成功返回值
	 * @apiSuccess {String}       data.pilePriceDesc               是否设置任务(任务标识 为0暂未设置任务)
	 * @apiSuccess {String}       data.taskName                   任务名称
	 * @apiSuccess {String}       data.priceDes                   电价描述
	 * @apiSuccess {String}       data.prcService                 服务费
	 * @apiSuccess {String}       data.execDate               执行时间

	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */

	@RequestMapping(value = "/_describe/{pileId}", method = RequestMethod.GET)
	public ResultVo describetPriceModel(@PathVariable("pileId") String pileId) throws Exception {
		Map data = new HashMap();
		data.put("pileId",pileId);
		DataVo dvlist=priceModelService.describetPriceModel(data);
		ResultVo resultVo = new ResultVo();
		resultVo.setData(dvlist);
		return resultVo;
	}

	/**
	 * @api {get} /api/priceModel/price/model    电价模板业务字典
	 * @apiName priceModel
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯   电价模板业务字典
	 * <br/>
	 * @apiParam {Int}         userId                                 用户Id
	 * @apiParam {Int}           pageNum                               页码
	 * @apiParam {Int}           pageSize                              页大小
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {Object}         data                    		      分页数据封装
	 * @apiSuccess {Integer}        data.total                    		      总记录数
	 * @apiSuccess {Object[]}       data.list                    		      分页数据对象数组
	 * @apiSuccess {Int}       data.list.id                  	   主键
	 * @apiSuccess {String}       data.list.text                   任务名称
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 * @apiError -1700000  参数缺失
	 */

	@RequestMapping(value = "/price/model", method = RequestMethod.GET)
	public ResultVo priceModel(@RequestParam Map dataMap) throws Exception {
		DataVo vo  =  new DataVo(dataMap);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			PageInfo pageList=priceModelService.priceModel(vo);
			resultVo.setData(pageList);
		}else {
			throw   new BizException(1700000,"userId");
		}
		return resultVo;
	}
	/**
	 * @api {get} /api/priceModel/stations   场站桩下拉树
	 * @apiName stationPile
	 * @apiGroup PriceModelController
	 * @apiVersion 2.0.0
	 * @apiDescription 易凯  场站桩下拉树
	 * <br/>
	 * @apiParam {int}             userId                         用户Id
	 * @apiParam {String}         [stationName]                         场站名称
	 * <br/>
	 * @apiSuccess {String}       errorCode                        错误码
	 * @apiSuccess {String}       errorMsg                         消息说明
	 * @apiSuccess {String}       data                    		   查询模板成功返回值
	 * @apiSuccess {String}       data.stationName        		    运营商名称
	 * @apiSuccess {Object}       data.piles                   桩集合
	 * @apiSuccess {String}      data.piles.pileId                桩Id
	 * @apiSuccess {String}      data.piles.pileName                桩名称
	 * @apiSuccess {String}      data.piles.state                桩状态（1.有详情,2.无详情）
	 * <br/>
	 * @apiError -999 系统异常!
	 * @apiError -888 请求方式异常!
	 */

	@RequestMapping(value = "/stations", method = RequestMethod.GET)
	public ResultVo stationPile(@RequestParam Map map) throws Exception {
		DataVo vo  =  new DataVo(map);
		ResultVo resultVo = new ResultVo();
		if(vo.isNotBlank("userId")){
			List<DataVo> dataVo=priceModelService.stationPile(vo);
			resultVo.setData(dataVo);
		}else {
			throw   new BizException(1700000,"userId");
		}

		return resultVo;

	}
}





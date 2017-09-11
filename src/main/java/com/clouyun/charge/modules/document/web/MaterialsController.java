package com.clouyun.charge.modules.document.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.MaterialsService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: 耗材管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月27日 下午7:19:09
 */
@RestController
@RequestMapping("/api/materials")
public class MaterialsController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(MaterialsController.class);
	
	@Autowired
	MaterialsService materialsService;
	
	/**
     * @api {GET} /api/materials   耗材列表
     * @apiName getQrCodes
     * @apiGroup MaterialsController    
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  耗材列表展示
     * <br/>
     * @apiParam {Integer} userId			用户Id
     * @apiParam {Int} pageNum 			页码
     * @apiParam {Int} pageSize				页大小
     * @apiParam {String} [sort] 			排序字段
     * @apiParam {String} [order] 			排序方向
     * @apiParam {String} [matFactory] 		生产厂家
     * @apiParam {String} [matName] 		耗材名称
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {Int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {Int} data.list.matId 				耗材id
     * @apiSuccess {String} data.list.matFactory 		生产厂家
     * @apiSuccess {String} data.list.matName 			耗材名称
     * @apiSuccess {Decimal} data.list.matPrice 		成本价
     * @apiSuccess {Date} data.list.matProductionTime 	生产日期
     * @apiSuccess {Date} data.list.matPurchaseTime 	采购日期
     * @apiSuccess {Date} data.list.orgName 			运营商名称
     * @apiSuccess {Date} data.list.shelfLifeTime		保质日期 yyyy-MM-dd
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "pageNum" : 1,
     *	    "pageSize" : 47,
     *	    "size" : 47,
     *	    "startRow" : 0,
     *	    "endRow" : 46,
     *	    "total" : 47,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "matFactory" : "科陆厂家2",
     *	        "matPurchaseTime" : "2017-02-25",
     *	        "matProductionTime" : "2017-02-24",
     *	        "matPrice" : 22.00,
     *	        "orgName" : "20",
     *	        "matName" : "张四2",
     *	        "matId" : 17,
     *			"orgName":"20",
     *			"shelfLifeTime":"2017-06-14"
     *	      },
     *	  	],
     *	    "prePage" : 0,
     *	    "nextPage" : 0,
     *	    "isFirstPage" : true,
     *	    "isLastPage" : true,
     *	    "hasPreviousPage" : false,
     *	    "hasNextPage" : false,
     *	    "navigatePages" : 8,
     *	    "navigatepageNums" : [
     *	      1
     *	    ],
     *	    "navigateFirstPage" : 1,
     *	    "navigateLastPage" : 1,
     *	    "firstPage" : 1,
     *	    "lastPage" : 1
     *	  }
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo getMaterials(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo page = materialsService.query(map);
		vo.setData(page);
		return vo;
	}
	
	/**
	 * 
     * @api {POST} /api/materials  新增耗材
     * @apiName insertMaterials
     * @apiGroup MaterialsController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  新增耗材(运营商,耗材名称,生产厂家,耗材成本都不可为空)
     * <br/>
     * @apiParam {String} matFactory 			生产厂家
     * @apiParam {String} matPrice 				耗材成本(元)
     * @apiParam {String} matName 				耗材名称
     * @apiParam {Int} orgId 				运营商Id
     * @apiParam {Date} [matProductionTime] 	生产日期(yyyy-MM-dd)
     * @apiParam {Date} [matPurchaseTime] 	采购日期(yyyy-MM-dd)
     * @apiParam {Date} [shelfLifeTime]		保质日期 yyyy-MM-dd
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
     * 		"matFactory":"四川成都",
     * 		"matPrice":110,
     * 		"matName":"一碗麻辣烫",
     * 		"orgId":24,
     * 		"matProductionTime":"2017-04-28",
     * 		"matPurchaseTime":"2017-04-28",
     * 		"shelfLifeTime":"2017-06-14"
     * }
     * 
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        受影响的行数(0:新增失败,1:新增成功一个)
     * @apiSuccess {Int} 	total     	总记录数
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1106000 耗材名称不可为空
     * @apiError -1106000 生产厂家不可为空
     * @apiError -1106000 运营商不可为空
     * @apiError -1106000 耗材成本不可为空
     * @apiError -1106001 耗材名称已存在
     * @apiError -1106002 耗材名称不能包含特殊字符
     * @apiError -1106002 生产厂家不能包含特殊字符
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo insertMaterials(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int count = materialsService.insert(map);
		vo.setData(count);
		return vo;
	}
	
	/**
     * @api {GET} /api/materials/{matId}   查询耗材信息
     * @apiName getMaterial
     * @apiGroup MaterialsController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据耗材id查询耗材信息
     * <br/>
     * @apiParam {Int} 	matId 	耗材id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {Int} 	total     	总记录数
     * @apiSuccess {Int} data.matId 				耗材id
     * @apiSuccess {Int} data.orgId 				运营商id
     * @apiSuccess {String} data.matFactory 		生产厂家
     * @apiSuccess {Date} data.matPurchaseTime 		采购时间(yyyy-MM-dd)
     * @apiSuccess {Date} data.matProductionTime 	采购时间(yyyy-MM-dd)
     * @apiSuccess {Int} data.matPrice 				耗材成本(元)
     * @apiSuccess {String} data.matName 			耗材名称
     * @apiSuccess {Date}  data.shelfLifeTime		保质日期
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * {
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "matFactory" : "海底捞",
     *	    "orgId" : 71,
     *	    "matPurchaseTime" : "2017-03-01",
     *	    "matProductionTime" : "2017-03-01",
     *	    "matPrice" : 100.00,
     *	    "matName" : "火锅",
     *	    "matId" : 18,
     *		"shelfLifeTime":"2017-06-14"
     *	  }
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/{matId}", method = RequestMethod.GET)
	public ResultVo	getMaterial(@PathVariable("matId") Integer matId) throws Exception {
		ResultVo vo = new ResultVo();
		Map map = materialsService.queryById(matId);
		vo.setData(map);
		return vo;
	}
	
	/**
	 * 
     * @api {PUT} /api/materials  更新耗材
     * @apiName updateMaterials
     * @apiGroup MaterialsController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  更新耗材(耗材id,运营商,耗材名称,生产厂家,耗材成本都不可为空)
     * <br/>
     * @apiParam {Int} 	  matId 				耗材Id(必填)
     * @apiParam {Int} 	  orgId 				运营商Id
     * @apiParam {String} matFactory 			生产厂家
     * @apiParam {String} matPrice 				耗材成本(元)
     * @apiParam {String} matName 				耗材名称
     * @apiParam {Date} [matProductionTime] 	生产日期(yyyy-MM-dd)
     * @apiParam {Date} [matPurchaseTime] 		采购日期(yyyy-MM-dd)
     * @apiParam {Date} [shelfLifeTime]			保质日期yyyy-MM-dd
     * <br/>
     * @apiParamExample {json} 入参示例:
     * {
     * 		"matFactory":"四川成都",
     * 		"matPrice":110,
     * 		"matName":"一碗麻辣烫",
     * 		"orgId":24,
     * 		"matProductionTime":"2017-04-28",
     * 		"matPurchaseTime":"2017-04-28",
     * 		"matId":20,
     * 		"shelfLifeTime":"2017-06-14"
     * }
     * 
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        受影响的行数(0:新增失败,1:新增成功一个)
     * @apiSuccess {int} 	total     	总记录数
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1106000 耗材名称不可为空
     * @apiError -1106000 生产厂家不可为空
     * @apiError -1106000 运营商不可为空
     * @apiError -1106000 耗材成本不可为空
     * @apiError -1106001 耗材名称已存在
     * @apiError -1106002 耗材名称不能包含特殊字符
     * @apiError -1106002 生产厂家不能包含特殊字符
     * @apiError -1106003 耗材已被使用,不可修改
     * @apiError -1106000 matId不可为空
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResultVo updateMaterials(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int count = materialsService.update(map);
		vo.setData(count);
		return vo;
	}
	
	/**
     * @api {DELETE} /api/materials/{ids}   耗材删除
     * @apiName  deleteMaterials
     * @apiGroup MaterialsController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  耗材删除,请求方式为/api/materials/95,94
     * <br/>
     * @apiParam {List} ids 需要删除的耗材id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {Int} 	total     	总记录数
     * @apiSuccess {Int} 	data 		删除受影响的个数(0:删除失败,>=1:删除成功个数)
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1106003 耗材已被使用,不可删除!
     * @apiError -1106004 请选择需要删除的耗材!
	 */
	@RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
	public ResultVo deleteMaterials(@PathVariable("ids") List ids) throws Exception {
		ResultVo vo = new ResultVo();
		int count = materialsService.delete(ids);
		vo.setData(count);
		return vo;
	}

	/**
     * @api {GET} /api/materials/_export  耗材导出
     * @apiName exportMaterials
     * @apiGroup MaterialsController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据查询条件导出耗材信息
     * <br/>
     * @apiParam {Integer} userId 			用户ID
     * @apiParam {Int} [pageNum] 			页码
     * @apiParam {Int} [pageSize]			页大小
     * @apiParam {String} [sort] 			排序字段
     * @apiParam {String} [order] 			排序方向
     * @apiParam {String} [matFactory] 		生产厂家
     * @apiParam {String} [matName] 		耗材名称
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "_export",method = RequestMethod.GET)
	public void exportMaterials(@RequestParam Map map,HttpServletResponse response) throws Exception{
		materialsService.export(map, response);
	}
	
	/**
	 * 
     * @api {GET} /api/materials/dict  耗材业务字典
     * @apiName  queryMaterialDictByOrgId
     * @apiGroup MaterialsController
     * @apiVersion 2.0.0
     * @apiDescription 高辉       
     * <br/>
     * @apiParam   {Integer}    orgId 			 运营商id
     * @apiParam   {Integer}    userId 			 用户id
     * <br/>
     * @apiSuccess {String}    errorCode                错误码
     * @apiSuccess {String}    errorMsg                 消息说明
     * @apiSuccess {Object}    data        
     * @apiSuccess {Object[]}  data.list                数据对象集合
     * @apiSuccess {String}    data.list.id             耗材id
	 * @apiSuccess {String}    data.list.text           耗材名称
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/dict",method = RequestMethod.GET)
	public ResultVo queryMaterialDictByOrgId(@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		resultVo.setData( materialsService.findMaterialDictByOrgId(map));
		return resultVo;
	}
	/**
	 * 
     * @api {GET} /api/materials/price/{matId}         根据耗材id获取耗材单价
     * @apiName  findMaterialPriceById
     * @apiGroup MaterialsController
     * @apiVersion 2.0.0
     * @apiDescription 高辉              根据耗材id获取耗材单价
     * <br/>
     * @apiParam   {Integer}    matId 			                        运营商id
     * <br/>
     * @apiSuccess {String}    errorCode                错误码
     * @apiSuccess {String}    errorMsg                 消息说明
     * @apiSuccess {Object}    data        
     * @apiSuccess {Double}    data.matPrice            耗材单价
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/price/{matId}",method = RequestMethod.GET)
	public ResultVo findMaterialPriceById(@PathVariable Integer matId) throws Exception{
		ResultVo resultVo = new ResultVo();
		resultVo.setData(materialsService.findMaterialPriceById(matId));
		return resultVo;
	}
}
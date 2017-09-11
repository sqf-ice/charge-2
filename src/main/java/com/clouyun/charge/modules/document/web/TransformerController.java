package com.clouyun.charge.modules.document.web;

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

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.TransformerService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: TransformerController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@RestController
@RequestMapping("/api/transformers")
public class TransformerController extends BusinessController {
	
	@Autowired
	TransformerService transformerService;
	
	/**
	 * @api {get} /api/transformers    查询变压器列表
	 * @apiName getTransformerAll
	 * @apiGroup TransformerController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据运营商,变压器名称,变压器状态查询变压器列表数据
	 * <br/>
	 * @apiParam {Integer} userId  用户Id
	 * @apiParam {Integer} [orgId] 运营商ID
	 * @apiParam {String} [transName] 变压器名称
	 * @apiParam {Integer} [transStatus] 变压器状态 0:未使用 1：运行 2：调试 3：停运 4：故障
	 * @apiParam {Integer(9)} pageNum 页码
	 * @apiParam {Integer(9)} pageSize 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer(11)} data.list.transId 变压器ID
	 * @apiSuccess {Integer(11)} data.list.stationId 场站ID
	 * @apiSuccess {String} data.list.stationName 场站名称
	 * @apiSuccess {Integer(11)} data.list.orgId 运营商ID
	 * @apiSuccess {String} data.list.orgName 运营商名称
	 * @apiSuccess {String} data.list.transName 变压器名称
	 * @apiSuccess {Integer(11)} data.list.transStatus 变压器状态 0:未使用 1：运行 2：调试 3：停运 4：故障
	 * @apiSuccess {String} data.list.transVolume 变压器容量
	 * @apiSuccess {Integer(11)} data.list.transMark 公专变标志0：公用 1：专用
	 * @apiSuccess {String} data.list.transThLoss 理论线损率
	 * @apiSuccess {String} data.list.transTestLoss 考核线损率
	 * @apiSuccess {String} data.list.transAssetId 变压器资产编号
	 * @apiSuccess {String} data.list.transNo 变压器编号
	 * @apiSuccess {String} data.list.transType 变压器型号
	 * @apiSuccess {Date} data.list.shelfLifeTime 保质日期 yyyy-MM-dd
	 * @apiSuccess {Double} data.list.ratedPower 额定功率
	 * @apiSuccess {Date} data.list.transCreatetime 创建时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.list.transUpdatetime 修改时间 yyyy-MM-dd HH:mm:ss
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResultVo getTransformerAll(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo pageList = transformerService.getTransformerAll(map);
		resVo.setData(pageList);
		return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {get} /api/transformers/{transId}    查询变压器信息
	 * @apiName getTransformerById
	 * @apiGroup TransformerController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据变压器Id查询变压器信息
	 * <br/>
	 * @apiParam {Integer}    transId     变压器Id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Integer} data.transId 变压器ID
	 * @apiSuccess {Integer} data.stationId 场站ID
	 * @apiSuccess {String} data.stationNo 场站编号
	 * @apiSuccess {String} data.stationName 场站名称
	 * @apiSuccess {Integer} data.orgId 运营商ID
	 * @apiSuccess {String} data.orgName 运营商名称
	 * @apiSuccess {String} data.transName 变压器名称
	 * @apiSuccess {Integer} data.transStatus 变压器状态 0:未使用 1：运行 2：调试 3：停运 4：故障
	 * @apiSuccess {String} data.transVolume 变压器容量
	 * @apiSuccess {Integer} data.transMark 公专变标志0：公用 1：专用
	 * @apiSuccess {String} data.transThLoss 理论线损率
	 * @apiSuccess {String} data.transTestLoss 考核线损率
	 * @apiSuccess {String} data.transAssetId 变压器资产编号
	 * @apiSuccess {String} data.transNo 变压器编号
	 * @apiSuccess {String} data.transType 变压器型号
	 * @apiSuccess {Date} data.shelfLifeTime 保质日期 yyyy-MM-dd
	 * @apiSuccess {Double} data.ratedPower 额定功率
	 * @apiSuccess {Date} data.transCreatetime 创建时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.transUpdatetime 修改时间 yyyy-MM-dd HH:mm:ss
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{transId}",method=RequestMethod.GET)
	public ResultVo getTransformerById(@PathVariable("transId") Integer transId) throws Exception{
		ResultVo resVo = new ResultVo();
		Map map = transformerService.getTransformerById(transId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {delete} /api/transformers/{transIds}    删除变压器信息
	 * @apiName delTransformer
	 * @apiGroup TransformerController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据变压器Id删除变压器信息
	 * <br/>
	 * @apiParam {String}    transIds     变压器Id 多个:1,2,3
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{transIds}",method=RequestMethod.DELETE)
	public ResultVo delTransformer(@PathVariable("transIds") List<Integer> transIds) throws Exception{
		transformerService.delTransformer(transIds);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {get}  /api/transformers/transinfos  查询变压器信息(业务字典)
	 * @apiName getTransformer
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据用户id查询变压器信息(业务字典)
	 * <br/>
	 * @apiParam {Integer}   [userId]   用户id
	 * @apiParam {String}   [provCode]   省编码
	 * @apiParam {String}   [cityCode]   市编码
	 * @apiParam {String}   [distCode]   县编码
	 * @apiParam {Integer}   [stationId]   场站id
	 * @apiParam {String}   [stationName]   场站名称
	 * @apiParam {Integer}   [orgId]   运营商id
	 * @apiParam {String}	 [orgName] 运营商名称
	 * @apiParam {Integer}   [pageNum]   页码
	 * @apiParam {Integer}   [pageSize]   页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.transId 变压器id
	 * @apiSuccess {String} data.list.transName 变压器名称
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="transinfos",method=RequestMethod.GET)
	public ResultVo getTransformer(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo pageList = transformerService.getTransformer(map);
		resVo.setData(pageList);
		return resVo;
	}
	
	/**
	 * 
	 * @api {post} /api/transformers    新增变压器
	 * @apiName saveTransformer
	 * @apiGroup TransformerController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 新增变压器
	 * <br/>
	 * @apiParam {Integer}    orgId    运营商Id
	 * @apiParam {Integer}    stationId    场站Id
	 * @apiParam {String}    transNo    变压器编号
	 * @apiParam {String}    transTestLoss    考核线损率
	 * @apiParam {String}    transName   变压器名称
	 * @apiParam {String}    transThLoss   理论线损率
	 * @apiParam {String}    transType   变压器型号
	 * @apiParam {String}    transAssetId    资产编号
	 * @apiParam {Integer}    transStatus    变压器状态
	 * @apiParam {Integer}    transMark    公专变标志
	 * @apiParam {String}    transVolume    变压器容量
	 * @apiParam {Date}		[shelfLifeTime] 保质日期 yyyy-MM-dd
	 * @apiParam {Double}	[ratedPower] 	额定功率
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001 {0}不能为空!
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResultVo saveTransformer(@RequestBody Map map) throws BizException{
		transformerService.saveTransformer(map);
		return new ResultVo();
	}
	
	
	/**
	 * 
	 * @api {put} /api/transformers   编辑变压器
	 * @apiName updateTransformer
	 * @apiGroup TransformerController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 编辑变压器
	 * <br/>
	 * @apiParam {Integer}    transId     变压器Id
	 * @apiParam {Integer}    [orgId]    运营商Id
	 * @apiParam {Integer}    [stationId]    场站Id
	 * @apiParam {String}    [transNo]    变压器编号
	 * @apiParam {String}    [transTestLoss]    考核线损率
	 * @apiParam {String}    [transName]   变压器名称
	 * @apiParam {String}    [transThLoss]   理论线损率
	 * @apiParam {String}    [transType]  变压器型号
	 * @apiParam {String}    [transAssetId]    资产编号
	 * @apiParam {Integer}   [transStatus]    变压器状态
	 * @apiParam {Integer}   [transMark]    公专变标志
	 * @apiParam {String}    [transVolume]    变压器容量
	 * @apiParam {Date}		 [shelfLifeTime]  保质日期 yyyy-MM-dd
	 * @apiParam {Double}	 [ratedPower]	 额定功率
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
	@RequestMapping(method=RequestMethod.PUT)
	public ResultVo updateTransformer(@RequestBody Map map) throws BizException{
		transformerService.updateTransformer(map);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {get} /api/transformers/_export    变压器信息导出
	 * @apiName exportTransformer
	 * @apiGroup TransformerController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 变压器信息导出
	 * <br/>
	 * @apiParam {Integer} userId  用户Id
 	 * @apiParam {Integer} [orgId] 运营商ID
	 * @apiParam {String} [transName] 变压器名称
	 * @apiParam {Integer} [transStatus] 变压器状态 0:未使用 1：运行 2：调试 3：停运 4：故障
	 * @apiParam {Integer(9)} [pageNum] 页码
	 * @apiParam {Integer(9)} [pageSize] 页大小
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_export",method=RequestMethod.GET)
	public void exportTransformer(@RequestParam Map map,HttpServletResponse response) throws Exception{
		transformerService.exportTransformer(map, response);
	}
}

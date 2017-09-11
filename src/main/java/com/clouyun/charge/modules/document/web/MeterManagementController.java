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
import com.clouyun.charge.modules.document.service.MeterManagementService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: MeterManagementController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@RestController
@RequestMapping("/api/meters")
public class MeterManagementController extends BusinessController {
	
	@Autowired
	MeterManagementService meterManagementService;
	
	/**
	 * 
	 * @api {get} /api/meters    查询表计列表数据
	 * @apiName getMeterManagementAll
	 * @apiGroup MeterManagementController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据运营商,场站名称,变压器名称,表计资产编号...条件查询表计列表数据
	 * <br/>
	 * @apiParam {Integer}  userId 	   用户Id
	 * @apiParam {Integer(11)} [orgId] 运营商ID
	 * @apiParam {String} [stationName] 场站名称
	 * @apiParam {String} [transName] 变压器名称
	 * @apiParam {String} [terminalName] 终端名称
	 * @apiParam {String} [meterManagementNo] 表计资产编号
	 * @apiParam {Integer(9)} pageNum 页码
	 * @apiParam {Integer(9)} pageSize 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer(11)} data.list.meterManagementId 表计ID
	 * @apiSuccess {String} data.list.meterManagementNo 表计资产编号
	 * @apiSuccess {String} data.list.meterManagementType 表计类型
	 * @apiSuccess {String} data.list.meterManagementProtocol 通讯协议
	 * @apiSuccess {String} data.list.meterManagementPurpose 表计用途
	 * @apiSuccess {String} data.list.meterManagementCt CT变化
	 * @apiSuccess {String} data.list.meterManagementPt PT变化
	 * @apiSuccess {String} data.list.meterManagementAddr 通讯地址
	 * @apiSuccess {String} data.list.meterManagementRate 综合倍率
	 * @apiSuccess {Integer(11)} data.list.orgId 运营商ID
	 * @apiSuccess {String} data.list.orgName 运营商Name
	 * @apiSuccess {Integer(11)} data.list.stationId 场站ID
	 * @apiSuccess {String} data.list.stationNo 场站编号
	 * @apiSuccess {String} data.list.stationName 场站名称
	 * @apiSuccess {Integer(11)} data.list.transId 变压器ID
	 * @apiSuccess {String} data.list.transName 变压器名称
	 * @apiSuccess {Date} data.list.meterManagementCreatetime 创建时间
	 * @apiSuccess {Date} data.list.meterManagementUpdatetime 修改时间
	 * @apiSuccess {Integer(11)} data.list.terminalId 终端Id
	 * @apiSuccess {String} data.list.terminalName 终端名称
	 * @apiSuccess {String} data.list.meterManagementMeasureNo 测量点编号
	 * @apiSuccess {String} data.list.meterManagementName 表计名称
	 * @apiSuccess {Date}	data.list.shelfLifeTime 保质日期 yyyy-MM-dd
	 * @apiSuccess {Double} data.list.ratedPower  额定功率
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResultVo getMeterManagementAll(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo pageList = meterManagementService.getMeterManagementAll(map);
		resVo.setData(pageList);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/meters/{meterManagementId}    查询表计信息
	 * @apiName getMeterManagementById
	 * @apiGroup MeterManagementController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据表计Id查询表计信息
	 * <br/>
	 * @apiParam {Integer}    meterManagementId     表计Id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Integer(11)} data.meterManagementId 表计ID
	 * @apiSuccess {String} data.meterManagementNo 表计资产编号
	 * @apiSuccess {String} data.meterManagementType 表计类型
	 * @apiSuccess {String} data.meterManagementProtocol 通讯协议
	 * @apiSuccess {String} data.meterManagementPurpose 表计用途
	 * @apiSuccess {String} data.meterManagementCt CT变化
	 * @apiSuccess {String} data.meterManagementPt PT变化
	 * @apiSuccess {String} data.meterManagementAddr 通讯地址
	 * @apiSuccess {String} data.meterManagementRate 综合倍率
	 * @apiSuccess {Integer(11)} data.stationId 场站ID
	 * @apiSuccess {String} data.stationNo 场站编号
	 * @apiSuccess {String} data.stationName 场站名称
	 * @apiSuccess {Integer(11)} data.orgId 运营商ID
	 * @apiSuccess {String} data.orgName 运营商名称
	 * @apiSuccess {Integer(11)} data.transId 变压器ID
	 * @apiSuccess {String} data.transName 变压器名称
	 * @apiSuccess {Date} data.meterManagementCreatetime 创建时间
	 * @apiSuccess {Date} data.meterManagementUpdatetime 修改时间
	 * @apiSuccess {Integer(11)} data.terminalId 终端Id
	 * @apiSuccess {String} data.terminalName 终端名称
	 * @apiSuccess {String} data.meterManagementMeasureNo 测量点编号
	 * @apiSuccess {String} data.meterManagementName 表计名称
	 * @apiSuccess {Date}	data.shelfLifeTime 保质日期 yyyy-MM-dd
	 * @apiSuccess {Double} data.ratedPower    额定功率
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{meterManagementId}",method=RequestMethod.GET)
	public ResultVo getMeterManagementById(@PathVariable("meterManagementId") Integer meterManagementId) throws Exception{
		ResultVo resVo = new ResultVo();
		Map map = meterManagementService.getMeterManagementById(meterManagementId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * @api {delete} /api/meters/{meterManagementIds}    删除表计信息
	 * @apiName delMeterManagement
	 * @apiGroup MeterManagementController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据表计Id删除表计信息   
	 * <br/>
	 * @apiParam {String}    meterManagementIds     表计Id 多个Id: 1,2,3   (必填)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{meterManagementIds}",method=RequestMethod.DELETE)
	public ResultVo delMeterManagement(@PathVariable("meterManagementIds") List<Integer> meterManagementIds) throws Exception{
		meterManagementService.delMeterManagement(meterManagementIds);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {post} /api/meters    新增表计
	 * @apiName saveMeterManagement
	 * @apiGroup MeterManagementController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 新增表计
	 * <br/>
	 * @apiParam {Integer}    orgId     运营商Id
	 * @apiParam {Integer}    stationId    场站Id
	 * @apiParam {Integer}    transId     变压器Id
	 * @apiParam {Integer}    terminalId     终端Id
	 * @apiParam {Integer}    meterManagementNo     表计资产编号
	 * @apiParam {Integer}    meterManagementName    表计名称
	 * @apiParam {Integer}    meterManagementType     表计类型  bjlx 
	 * @apiParam {Integer}    meterManagementPurpose    表计用途 bjyt
	 * @apiParam {Integer}    meterManagementCt     CT互感器变比  ct
	 * @apiParam {Integer}    meterManagementPt     PT互感器变比 pt
	 * @apiParam {Integer}    meterManagementAddr    通信地址
	 * @apiParam {Integer}    meterManagementRate   综合倍率
	 * @apiParam {Integer}    meterManagementMeasureNo   测量点编号
	 * @apiParam {Integer}    meterManagementProtocol   通信规约
	 * @apiParam {Date}		  [shelfLifeTime] 	保质日期 yyyy-MM-dd
	 * @apiParam {Double}	  [ratedPower]		额定功率
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResultVo saveMeterManagement(@RequestBody Map map) throws BizException{
		meterManagementService.saveMeterManagement(map);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {put} /api/meters    编辑表计信息
	 * @apiName updateMeterManagement
	 * @apiGroup MeterManagementController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 编辑表计信息
	 * <br/>
	 * @apiParam {Integer}    meterManagementId     表计Id
	 * @apiParam {Integer}    [orgId]     运营商Id
	 * @apiParam {Integer}    [stationId]    场站Id
	 * @apiParam {Integer}    [transId]     变压器Id
	 * @apiParam {Integer}    [terminalId]     终端Id
	 * @apiParam {Integer}    [meterManagementNo]     表计资产编号
	 * @apiParam {Integer}    [meterManagementName]    表计名称
	 * @apiParam {Integer}    [meterManagementType]     表计类型  bjlx 
	 * @apiParam {Integer}    [meterManagementPurpose]    表计用途 bjyt
	 * @apiParam {Integer}    [meterManagementCt]     CT互感器变比  ct
	 * @apiParam {Integer}    [meterManagementPt]     PT互感器变比 pt
	 * @apiParam {Integer}    [meterManagementAddr]    通信地址
	 * @apiParam {Integer}    [meterManagementRate]   综合倍率
	 * @apiParam {Integer}    [meterManagementMeasureNo]   测量点编号
	 * @apiParam {Integer}    [meterManagementProtocol]   通信规约
	 * @apiParam {Date}		  [shelfLifeTime]   保质日期 yyyy-MM-dd
	 * @apiParam {Double}	  [ratedPower]		额定功率
	 * <br/>	
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public ResultVo updateMeterManagement(@RequestBody Map map) throws BizException{
		meterManagementService.updateMeterManagement(map);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {get} /api/meters/_export   导出表计信息
	 * @apiName exportMeterManagement
	 * @apiGroup MeterManagementController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 导出表计信息
	 * <br/>
	 * @apiParam {Integer} userId 用户Id
	 * @apiParam {Integer} [orgId] 运营商ID
	 * @apiParam {String} [stationName] 场站名称
	 * @apiParam {String} [transName] 变压器名称
	 * @apiParam {String} [terminalName] 终端名称
	 * @apiParam {String} [meterManagementNo] 表计资产编号
	 * @apiParam {Integer} [pageNum] 页码
	 * @apiParam {Integer} [pageSize] 页大小
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_export",method=RequestMethod.GET)
	public void exportMeterManagement(@RequestParam Map map,HttpServletResponse response) throws Exception{
		meterManagementService.exportMeterManagement(map,response);
	}
}

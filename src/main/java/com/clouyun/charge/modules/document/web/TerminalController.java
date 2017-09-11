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
import com.clouyun.charge.modules.document.service.TerminalService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: TerminalController  
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月22日
 */
@RestController
@RequestMapping("/api/terminals")
public class TerminalController extends BusinessController {
	
	@Autowired
	TerminalService terminalService;
	
	/**
	 * 
	 * @api {get} /api/terminals    查询终端列表
	 * @apiName getTransformerAll
	 * @apiGroup TerminalController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据运营商,所属场站查询终端列表
	 * <br/>
	 * @apiParam {Integer} userId       用户Id
	 * @apiParam {String} [stationName] 所属场站
	 * @apiParam {Integer} [orgId] 运营商ID
	 * @apiParam {Integer} pageNum 页码
	 * @apiParam {Integer} pageSize 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.terminalId 终端ID
	 * @apiSuccess {Integer} data.list.stationId 场站ID
	 * @apiSuccess {String} data.list.stationName 场站名称
	 * @apiSuccess {Integer} data.list.orgId 运营商ID
	 * @apiSuccess {String} data.list.orgName 运营商名称
	 * @apiSuccess {String} data.list.terminalName 终端名称
	 * @apiSuccess {String} data.list.terminalIp 终端IP地址
	 * @apiSuccess {Integer} data.list.terminalCommType 终端通信方式
	 * @apiSuccess {String} data.list.terminalCommProtocol 终端通信协议
	 * @apiSuccess {Integer} data.list.terminalPort 终端通信协议端口
	 * @apiSuccess {Integer} data.list.terminalProtocolStatus 终端通信协议状态
	 * @apiSuccess {Date} data.list.terminalCreatetime 创建时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.list.terminalUpdatetime 修改时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {String} data.list.terminalNo 终端编号
	 * @apiSuccess {Date} data.list.shelfLifeTime 保质日期 yyyy-MM-dd
	 * @apiSuccess {Double} data.list.ratedPower 额定功率
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ResultVo getTransformerAll(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		PageInfo pageList = terminalService.getTerminalAll(map);
		resVo.setData(pageList);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/terminals/{transId}    查询终端信息
	 * @apiName getTransformerById
	 * @apiGroup TerminalController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据终端Id查询终端信息
	 * <br/>
	 * @apiParam {Integer}    transId     终端ID
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.terminalId 终端ID
	 * @apiSuccess {Integer} data.stationId 场站ID
	 * @apiSuccess {String} data.stationNo 场站编号
	 * @apiSuccess {String} data.stationName 场站名称
	 * @apiSuccess {Integer} data.orgId 运营商ID
	 * @apiSuccess {String} data.orgName 运营商名称
	 * @apiSuccess {String} data.terminalName 终端名称
	 * @apiSuccess {String} data.terminalIp 终端IP地址
	 * @apiSuccess {Integer} data.terminalCommType 终端通信方式
	 * @apiSuccess {String} data.terminalCommProtocol 终端通信协议
	 * @apiSuccess {Integer} data.terminalPort 终端通信协议端口
	 * @apiSuccess {Integer} data.terminalProtocolStatus 终端通信协议状态
	 * @apiSuccess {Date} data.terminalCreatetime 创建时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {Date} data.terminalUpdatetime 修改时间 yyyy-MM-dd HH:mm:ss
	 * @apiSuccess {String} data.terminalNo 终端编号
	 * @apiSuccess {Date} data.shelfLifeTime 保质日期 yyyy-MM-dd
	 * @apiSuccess {Double} data.ratedPower 额定功率
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{terminalId}",method=RequestMethod.GET)
	public ResultVo getTransformerById(@PathVariable("terminalId") Integer terminalId) throws Exception{
		ResultVo resVo = new ResultVo();
		Map map = terminalService.getTerminalById(terminalId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {delete} /api/terminals/{transIds}    删除终端信息
	 * @apiName delTerminal
	 * @apiGroup TerminalController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据终端Id删除终端信息
	 * <br/>
	 * @apiParam {String}    terminalIds     终端ID 多个Id:1,2,3 (必填)
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{terminalIds}",method=RequestMethod.DELETE)
	public ResultVo delTerminal(@PathVariable("terminalIds") List<Integer> terminalIds) throws Exception{
		terminalService.delTerminal(terminalIds);
		return new ResultVo();
	}
	
	/**
	 * @throws BizException 
	 * @api {get} /api/terminals/terminal   查询终端信息列表(业务字典)
	 * @apiName getTerminal
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据用户Id,终端名称查询终端信息列表(业务字典)
	 * <br/>
	 * @apiParam {Integer}    [userId]   用户Id
	 * @apiParam {String}    [terminalName]   终端名称
	 * @apiParam {Integer}    [orgId]   运营商Id
	 * @apiParam {String}    [orgName]   运营商名称
	 * @apiParam {Integer}    [stationId]   场站Id
	 * @apiParam {String}    [stationName]   场站名称
	 * 
	 * @apiParam {Integer}    [pageNum]   页码
	 * @apiParam {Integer}    [pageSize]   页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.terminalId 分页数据对象数组
	 * @apiSuccess {String} data.list.terminalName 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="terminal",method=RequestMethod.GET)
	public ResultVo getTerminal(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo  pageList = terminalService.getTerminal(map);
		resVo.setData(pageList);
		return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {post} /api/terminals   新增终端
	 * @apiName saveTerminal
	 * @apiGroup TerminalController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 编辑终端
	 * <br/>
	 * @apiParam {Integer}   orgId    运营商Id
	 * @apiParam {Integer}   stationId    场站Id
	 * @apiParam {String}    terminalNo    变压器编号
	 * @apiParam {String}    terminalName    变压器名称
	 * @apiParam {String}    terminalIp    终端IP地址
	 * @apiParam {Integer}   terminalCommType    终端通信方式 zdtxfs
	 * @apiParam {String}    terminalCommProtocol    终端通信协议 zdtxxy
	 * @apiParam {Integer}   terminalPort    终端通信协议端口
	 * @apiParam {Integer}   terminalProtocolStatus    终端通信协议状态 zdzt
	 * @apiParam {Date}		 [shelfLifeTime] 保质日期 yyyy-MM-dd
	 * @apiParam {Double}	 [ratedPower]    额定功率
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResultVo saveTerminal(@RequestBody Map map) throws BizException{
		terminalService.saveTerminal(map);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {put} /api/terminals    编辑终端信息
	 * @apiName updateTerminal
	 * @apiGroup TerminalController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 编辑终端信息
	 * <br/>
	 * @apiParam {Integer}   [orgId]    运营商Id
	 * @apiParam {Integer}   [stationId]    场站Id
	 * @apiParam {String}    [terminalNo]    变压器编号
	 * @apiParam {String}    [terminalName]    变压器名称
	 * @apiParam {String}    [terminalIp]    终端IP地址
	 * @apiParam {Integer}   [terminalCommType]    终端通信方式 zdtxfs
	 * @apiParam {String}    [terminalCommProtocol]    终端通信协议 zdtxxy
	 * @apiParam {Integer}   [terminalPort]    终端通信协议端口
	 * @apiParam {Integer}   [terminalProtocolStatus]    终端通信协议状态 zdzt
	 * @apiParam {Date}		 [shelfLifeTime] 	保质日期 yyyy-MM-dd
	 * @apiParam {Double}	 [ratedPower]		额定功率
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public ResultVo updateTerminal(@RequestBody Map map) throws BizException {
		terminalService.updateTerminal(map);
		return new ResultVo();
	}
	
	/**
	 * 
	 * @api {get}  /api/terminals/_export   导出终端信息
	 * @apiName exportTerminal
	 * @apiGroup TerminalController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 导出终端信息
	 * <br/>
	 * @apiParam {Integer} userId 用户Id
	 * @apiParam {String} [stationName] 所属场站
	 * @apiParam {Integer} [orgId] 运营商ID
	 * @apiParam {Integer} [pageNum] 页码
	 * @apiParam {Integer} [pageSize] 页大小
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_export",method=RequestMethod.GET)
	public void exportTerminal(@RequestParam Map map,HttpServletResponse response) throws Exception{
		terminalService.exportTerminal(map,response);
	}
}

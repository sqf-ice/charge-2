package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.QrCodeService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述: 二维码管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月24日 下午4:08:47
 */
@RestController
@RequestMapping("/api/qrcodes")
public class QrCodeController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(QrCodeController.class);
	
	@Autowired
	QrCodeService codeService;
	
	
	/**
     * @api {GET} /api/qrcodes  二维码管理列表
     * @apiName getQrCodes
     * @apiGroup QrCodeController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   二维码管理列表
     * <br/>
     * @apiParam {int} pageNum 				页码
     * @apiParam {int} pageSize 			页大小
     * @apiParam {int} userId	 			登陆用户id
     * @apiParam {String} [sort] 			排序字段
     * @apiParam {String} [order] 			排序方向
     * @apiParam {String} [orgName] 		运营商名称
     * @apiParam {String} [stationName] 	场站名称
	 * @apiParam {Int} [orgId] 				运营商id
	 * @apiParam {Int} [stationId] 			场站id
     * @apiParam {String} [qrCodeState] 	二维码状态(0:未下发,1:已下发,2:下发失败)
     * @apiParam {String} [hlhtQrCodeState] 互联互通二维码状态(0:未下发,1:已下发,2:下发失败)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {int} data.list.pileId 				充电桩id(主键)
     * @apiSuccess {String} data.list.orgName 			运营商名称
     * @apiSuccess {String} data.list.stationName 		场站名称
     * @apiSuccess {String} data.list.pileName 			充电桩名称
     * @apiSuccess {String} data.list.pileNo 			充电桩编号
	 * @apiSuccess {String} data.list.numberGun 枪数(type=qs)
	 * @apiSuccess {String} data.list.ortMode 交直模式(type=jzms)
	 * @apiSuccess {String} data.list.powerMode 功率模式(type=glms)
     * @apiSuccess {String} data.list.aQrCode 			a枪二维码
     * @apiSuccess {String} data.list.bQrCode 			b枪二维码
     * @apiSuccess {int(2)} data.list.qrCodeState 		二维码状态(0:未下发,1:已下发,2:下发失败)
     * @apiSuccess {int(2)} data.list.hlhtQrcodeState 	互联互通二维码状态(0:未下发,1:已下发,2:下发失败)
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : {
     *	    "pageNum" : 1,
     *	    "pageSize" : 609,
     *	    "size" : 609,
     *	    "startRow" : 0,
     *	    "endRow" : 608,
     *	    "total" : 609,
     *	    "pages" : 1,
     *	    "list" : [
     *	      {
     *	        "qrCodeState" : 2,
     *	        "pileNo" : "Z000051123",
     *	        "pileType" : "1",
     *	        "hlhtQrcodeState" : 2,
     *	        "aQrCode" : "12312345880",
     *	        "pileName" : "2号充电桩",
     *	        "stationName" : "XXX充电站",
     *	        "pileId" : 3,
     *			"orgName" : "深圳陆科",
     *			"bQrCode" : "2222"
     *	      }
     *	    ],
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
     *	    "lastPage" : 1,
     *	    "firstPage" : 1
     *	  }
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo getQrCodes(@RequestParam Map map) throws Exception {
		ResultVo vo = new ResultVo();
		PageInfo pageInfo = codeService.selectAll(map);
		vo.setData(pageInfo);
		return vo;
	}
	

	/**
     * @api {GET} /api/qrcodes/{pileId}   查询二维码信息
     * @apiName getPile
     * @apiGroup QrCodeController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据充电桩id查询二维码信息
     * <br/>
     * @apiParam {int} pileId 充电桩id(必填)
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        分页数据封装
     * @apiSuccess {int} data.total     总记录数
     * @apiSuccess {Object[]} data.list 分页数据对象数组
     * @apiSuccess {int} data.list.pileId 				充电桩id
     * @apiSuccess {String} data.list.orgName 			运营商名称
     * @apiSuccess {String} data.list.stationName 		场站名称
     * @apiSuccess {String} data.list.pileName 			充电桩名称
     * @apiSuccess {String} data.list.pileNo 			充电桩编号
	 * @apiSuccess {String} data.list.numberGun 		枪数(type=qs)
	 * @apiSuccess {String} data.list.ortMode 			交直模式(type=jzms)
	 * @apiSuccess {String} data.list.powerMode 		功率模式(type=glms)
     * @apiSuccess {Object[]} data.list.chgGunInfo 		枪信息
	 * @apiSuccess {String} data.list.chgGunInfo.qrCode 	二维码信息
	 * @apiSuccess {int} data.list.chgGunInfo.chuGunId 		枪id
	 * @apiSuccess {int} data.list.chgGunInfo.chuGunName 	枪名称
	 * <br/>
	 * @apiSuccessExample {json} Success出参示例:{
	 *	{
	 *	errorCode: 0,
	 *	errorMsg: "操作成功!",
	 *	total: 0,
	 *	data: {
	 *		qrCodeState: 0,
	 *		numberGun: 4,
	 *		orgName: "深圳陆科",
	 *		stationNo: "21068888028",
	 *		pileAddr: "20170619",
	 *		pileNo: "21068888028188",
	 *		pileName: "龙岗一桩四枪",
	 *		orgId: 24,
	 *		chgGunInfo: [
	 *			{
	 *				qrCode: "2017061901",
	 *				chgGunId: 2649,
	 *				chgGunName: "充电枪01"
	 *			},
	 *			{
	 *				qrCode: "2017061902",
	 *				chgGunId: 2650,
	 *				chgGunName: "充电枪02"
	 *			},
	 *			{
	 *				qrCode: "2017061903",
	 *				chgGunId: 2651,
	 *				chgGunName: "充电枪03"
	 *			},
	 *			{
	 *				qrCode: "2017061904",
	 *				chgGunId: 2652,
	 *				chgGunName: "充电枪04"
	 *			}
	 *		],
	 *		pileId: 4104,
	 *		orgNo: "21068888",
	 *		powerMode: 1,
	 *		orgCode: "MA5DA0053",
	 *		ortMode: 2,
	 *		pileType: "4",
	 *		stationName: "充电桩测试用",
	 *		hlhtQrcodeState: 0,
	 *		stationId: 684
	 *	},
	 *	exception: null
	 *	}
	 * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1102014 该充电桩未选择屏显二维码
	 */
	@RequestMapping(value = "/{pileId}", method = RequestMethod.GET)
	public ResultVo getPile(@PathVariable("pileId") Integer pileId) throws BizException {
		ResultVo vo = new ResultVo();
		Map map = codeService.selectByPrimaryKey(pileId);
		vo.setData(map);
		return vo;
	}
	
	/**
     * @api {PUT} /api/qrcodes/_issued   下发二维码
     * @apiName issuedQRCode
     * @apiGroup QrCodeController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  下发二维码
     * <br/>
     * @apiParam {int} pileId 		充电桩id(必填)
     * @apiParam {int} numberGun 	枪数(必填)
     * @apiParam {int} ortMode 		交直模式(必填)
     * @apiParam {String} pileName 		充电桩名称
     * @apiParam {Object[]} chgGunInfo 	枪信息封装
	 * @apiParam {String} chgGunInfo.qrCode 		二维码
	 * @apiParam {int} [chgGunInfo.chgGunId] 	枪id(选填,查询信息没有返回b枪id则不用传值)
     * @apiParam {String} [chgGunInfo.gumPoint] 	枪口名称
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        数据封装(1:下发成功一个)
     * @apiSuccess {int} 	total     	总记录数
     * <br/>
     * @apiError -999 系统异常!
	 */
	@RequestMapping(value = "/_issued", method = RequestMethod.PUT)
	public ResultVo issuedQRCode(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int count = codeService.issuedCode(map);
		vo.setData(count);
		return vo;
	}
	
	/**
	 * @api {PUT} /api/qrcodes/_hlhtIssued   下发互联互通二维码
	 * @apiName hlhtIssuedCode
	 * @apiGroup QrCodeController
	 * @apiVersion 2.0.0
	 * @apiDescription 曹伟  下发互联互通二维码
	 * <br/>
	 * @apiParam {int} pileId 		充电桩id(必填)
	 * @apiParam {int} numberGun 	枪数(必填)
	 * @apiParam {int} ortMode 		交直模式(必填)
	 * @apiParam {String} pileName 		充电桩名称
	 * @apiParam {Object[]} chgGunInfo 	枪信息封装
	 * @apiParam {String} chgGunInfo.qrCode 		二维码
	 * @apiParam {int} [chgGunInfo.chgGunId] 	枪id(选填,查询信息没有返回b枪id则不用传值)
	 * @apiParam {String} [chgGunInfo.gumPoint] 	枪口名称
	 * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {Object} data        数据封装(1:下发成功一个)
     * @apiSuccess {int} 	total     	总记录数
	 * <br/>
	 * @apiError -999 系统异常!
	 */
	
	@RequestMapping(value = "/_hlhtIssued", method = RequestMethod.PUT)
	public ResultVo hlhtIssuedCode(@RequestBody Map map) throws Exception {
		ResultVo vo = new ResultVo();
		int count = codeService.hlhtIssuedCode(map);
		vo.setData(count);
		return vo;
	}
	
	/**
     * @api {GET} /api/qrcodes/_batchIssued/{pileIds}  二维码批量下发
     * @apiName batchIssued
     * @apiGroup QrCodeController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟   二维码批量下发请求方式如:/api/qrcodes/_batchIssued/2361,2363
     * <br/>
     * @apiParam {List} pileIds 需要下发的桩Id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {int} 	total     	总记录数
     * @apiSuccess {Object[]} data 		分页数据对象数组
     * @apiSuccess {String} data.pileNo 		下发失败充电桩编号
     * @apiSuccess {String} data.pileName 		下发失败充电桩名称
     * @apiSuccess {String} data.msg 			下发失败原因
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : [
     *		全部下发成功时,data是没有数据的,只有下发失败的情况下才有数据
     *	    {
     *	      "pileNo" : "003001001",
     *	      "pileName" : "1号桩",
     *	      "msg" : "下发失败:终端不在线"
     *	    },;
     *	    {
     *	      "pileNo" : "003001003",
     *	      "pileName" : "3号桩",
     *	      "msg" : "下发失败:终端不在线"
     *	    }
     *	  ]
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_batchIssued/{pileIds}", method = RequestMethod.GET)
    public ResultVo batchIssued(@PathVariable("pileIds") List pileIds) throws Exception {
        ResultVo resultVo = new ResultVo();
        List<Map> map = codeService.batchIssued(pileIds);
        resultVo.setData(map);
        return resultVo;
    }
	
	/**
     * @api {GET} /api/qrcodes/_hlhtBatchIssued/{pileIds}  互联互通二维码批量下发
     * @apiName hlhtBatchIssued
     * @apiGroup QrCodeController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  互联互通 二维码批量下发请求方式如:/api/qrcodes/_hlhtBatchIssued/2361,2363
     * <br/>
     * @apiParam {List} pileIds 需要下发的桩Id
     * <br/>
     * @apiSuccess {String} errorCode   错误码
     * @apiSuccess {String} errorMsg    消息说明
     * @apiSuccess {int} 	total     	总记录数
     * @apiSuccess {Object[]} data 		分页数据对象数组
     * @apiSuccess {String} data.pileNo 		下发失败充电桩编号
     * @apiSuccess {String} data.pileName 		下发失败充电桩名称
     * @apiSuccess {String} data.msg 			下发失败原因
     * <br/>
     * @apiSuccessExample {json} Success出参示例:{
     * 	{
     *	  "errorCode" : 0,
     *	  "errorMsg" : "操作成功!",
     *	  "total" : 0,
     *	  "data" : [
     *		全部下发成功时,data是没有数据的,只有下发失败的情况下才有数据
     *	    {
     *	      "pileNo" : "003001001",
     *	      "pileName" : "1号桩",
     *	      "msg" : "下发失败:终端不在线"
     *	    },;
     *	    {
     *	      "pileNo" : "003001003",
     *	      "pileName" : "3号桩",
     *	      "msg" : "下发失败:终端不在线"
     *	    }
     *	  ]
     *	}
     * }
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_hlhtBatchIssued/{pileIds}", method = RequestMethod.GET)
    public ResultVo hlhtBatchIssued(@PathVariable("pileIds") List pileIds) throws Exception {
        ResultVo resultVo = new ResultVo();
        List<Map> map = codeService.hlhtBatchIssued(pileIds);
        resultVo.setData(map);
        return resultVo;
    }
	
	/**
	 * 
     * @api {GET} /api/qrcodes/_export  二维码导出
     * @apiName exportQrCode
     * @apiGroup QrCodeController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  二维码及二维码图片导出,无参数返回,直接下载文件
     * <br/>
     * @apiParam {String} [sort] 			排序字段
     * @apiParam {String} [order] 			排序方向
     * @apiParam {String} [orgName] 		运营商名称
     * @apiParam {String} [stationName] 	场站名称
     * @apiParam {String} [qrCodeState] 	二维码状态(0:未下发,1:已下发,2:下发失败)
     * @apiParam {String} [hlhtQrCodeState] 互联互通二维码状态(0:未下发,1:已下发,2:下发失败)
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@RequestMapping(value = "/_export", method = RequestMethod.GET)
    public void exportQrCode(@RequestParam Map map,HttpServletResponse response,HttpServletRequest request) throws Exception {
        codeService.export(map,response,request);
    }
	
	
	/**
	 * 
	 * @api {POST} /api/qrcodes/_import   二维码导入
     * @apiName importQrCodes
     * @apiGroup QrCodeController
     * @apiVersion 2.0.0
     * @apiDescription 曹伟  根据枪模版导入二维码信息
     * <br/>
     * @apiParam {MultipartFile} file 枪模版
     * @apiParam {Int} userId 登陆用户id
     * <br/>
     * @apiSuccess {String} errorCode   	错误码
     * @apiSuccess {String} errorMsg    	消息说明
     * @apiSuccess {Object} data        	分页数据封装
     * @apiSuccess {int} 	total    	 	总记录数
     * @apiSuccess {String} data.impStatus 	数据状态
     * @apiSuccess {String} data.pileNo 	充电桩编号
	 * @apiSuccess {String} data.numberGun 	枪数
	 * @apiSuccess {String} data.ortMode 	交直模式
	 * @apiSuccess {String} data.powerMode 	功率模式
	 * @apiSuccess {String} data.01qrCode 	01枪二维码
	 * @apiSuccess {String} data.01meterName 表计名称
     * @apiSuccess {String} data.01meterType 表计类型
     * @apiSuccess {String} data.01ratPower 	额定功率
     * @apiSuccess {String} data.01parkNum 	对应车位
	 * @apiSuccess {String} data.02qrCode 	02枪二维码
	 * @apiSuccess {String} data.02meterName 表计名称
	 * @apiSuccess {String} data.02meterType 表计类型
	 * @apiSuccess {String} data.02ratPower 	额定功率
	 * @apiSuccess {String} data.02parkNum 	对应车位
	 * @apiSuccess {String} data.03qrCode 	03枪二维码
	 * @apiSuccess {String} data.03meterName 表计名称
	 * @apiSuccess {String} data.03meterType 表计类型
	 * @apiSuccess {String} data.03ratPower 	额定功率
	 * @apiSuccess {String} data.03parkNum 	对应车位
	 * @apiSuccess {String} data.04qrCode 	04枪二维码
	 * @apiSuccess {String} data.04meterName 表计名称
	 * @apiSuccess {String} data.04meterType 表计类型
	 * @apiSuccess {String} data.04ratPower 	额定功率
	 * @apiSuccess {String} data.04parkNum 	对应车位
	 * @apiSuccess {String} data.05qrCode 	05枪二维码
	 * @apiSuccess {String} data.05meterName 表计名称
	 * @apiSuccess {String} data.05meterType 表计类型
	 * @apiSuccess {String} data.05ratPower 	额定功率
	 * @apiSuccess {String} data.05parkNum 	对应车位
	 * @apiSuccess {String} data.06qrCode 	06枪二维码
	 * @apiSuccess {String} data.06meterName 表计名称
	 * @apiSuccess {String} data.06meterType 表计类型
	 * @apiSuccess {String} data.06ratPower 	额定功率
	 * @apiSuccess {String} data.06parkNum 	对应车位
	 * @apiSuccess {String} data.07qrCode 	07枪二维码
	 * @apiSuccess {String} data.07meterName 表计名称
	 * @apiSuccess {String} data.07meterType 表计类型
	 * @apiSuccess {String} data.07ratPower 	额定功率
	 * @apiSuccess {String} data.07parkNum 	对应车位
	 * @apiSuccess {String} data.08qrCode 	08枪二维码
	 * @apiSuccess {String} data.08meterName 表计名称
	 * @apiSuccess {String} data.08meterType 表计类型
	 * @apiSuccess {String} data.08ratPower 	额定功率
	 * @apiSuccess {String} data.08parkNum 	对应车位
     * @apiSuccess {String} data.09qrCode 	09枪二维码
     * @apiSuccess {String} data.09meterName 表计名称
     * @apiSuccess {String} data.09meterType 表计类型
     * @apiSuccess {String} data.09ratPower 	额定功率
     * @apiSuccess {String} data.09parkNum 	对应车位
     * @apiSuccess {String} data.10qrCode 	10枪二维码
     * @apiSuccess {String} data.10meterName 表计名称
     * @apiSuccess {String} data.10meterType 表计类型
     * @apiSuccess {String} data.10ratPower 	额定功率
     * @apiSuccess {String} data.10parkNum 	对应车位
     * @apiSuccess {String} data.ZmeterName 总表表计名称
     * @apiSuccess {String} data.ZmeterType 总表表计类型
     * @apiSuccess {String} data.ZratPower 	总表额定功率
     * @apiSuccess {String} data.ZmeterRatio 		总表变比
     * @apiSuccess {String} data.description 		描述
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 * @apiError -2000000 导入失败
	 */
	@RequestMapping(value = "/_import", method = RequestMethod.POST)
	public ResultVo importQrCodes(HttpServletRequest request,
            @RequestParam("file") MultipartFile file,@RequestParam Map map) throws Exception {
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile excelFile=multipartRequest.getFile("file");
		ResultVo vo = codeService.importQrCodes(excelFile,map);
		return vo;
	}
}
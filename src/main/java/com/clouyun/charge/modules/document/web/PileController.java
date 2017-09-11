package com.clouyun.charge.modules.document.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.PileService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述: PileController
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年4月19日
 */
@RestController
@RequestMapping("/api")
public class PileController extends BusinessController {
	@Autowired
	PileService pileService;
	
	/**
	 * 
	 * 
	 * @api {get} /api/piles    查询充电桩列表
	 * @apiName getPileAll
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据区域,运营商,场站,生产厂家,充电桩名称,充电桩类型,使用状态,通讯协议条件查询充电桩列表
	 * <br/>
	 * @apiParam {Integer} userId	 用户Id
	 * @apiParam {String} [pileName] 充电桩名称  
	 * @apiParam {String} [ortMode]  交直模式(type=jzms)
	 * @apiParam {String} [pileStatus] 使用状态，有效、无效 
	 * @apiParam {String} [pileProtocol] 通讯协议 
	 * @apiParam {String} [manufacturerId] 生产厂家 
	 * @apiParam {Integer(9)} [orgId] 所属单位标识 
	 * @apiParam {String} [stationId] 场站id 
	 * @apiParam {String} [stationName] 场站名称 
	 * @apiParam {String} [provCode] 省 
	 * @apiParam {String} [cityCode] 市 
	 * @apiParam {Integer(9)} pageNum 页码 
	 * @apiParam {Integer(9)} pageSize 页大小 
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer(9)} data.list.pileId 充电设备标识
	 * @apiSuccess {Integer(9)} data.list.orgName 运营商
	 * @apiSuccess {String} data.list.pileName 设备名称
	 * @apiSuccess {String} data.list.pileNo 充电设备编码
	 * @apiSuccess {Integer(9)} data.list.stationName 所属场站
	 * @apiSuccess {String} data.list.numberGun 枪数(type=qs)
	 * @apiSuccess {String} data.list.ortMode 交直模式(type=jxms)
	 * @apiSuccess {String} data.list.powerMode 功率模式(type=glms)
	 * @apiSuccess {String} data.list.pileStatus 使用状态，有效、无效
	 * @apiSuccess {String} data.list.pileProtocol 通信协议
	 * @apiSuccess {String} data.list.pileAddr 充电桩地址
	 * @apiSuccess {Integer(2)} data.list.conCycle 心跳周期
	 * @apiSuccess {Integer(9)} data.list.prcIdPre 当前电价
	 * @apiSuccess {String} data.list.softVersion 软件版本
	 * @apiSuccess {String} data.list.hardVersion 硬件版本
	 * @apiSuccess {String} data.list.manufacturerId 生产厂家
	 * @apiSuccess {Integer(9)} data.list.pileModelId 设备型号
	 * @apiSuccess {String} data.list.ratePower 额定功率
	 * @apiSuccess {Date} data.list.productionDate 生产日期
	 * @apiSuccess {Date} data.list.installDate 安装日期
	 * <br/>
	 * @apiError -999  系统异常
	 */
	@RequestMapping(value="piles",method=RequestMethod.GET)
	public ResultVo getPileAll(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo pileAll = pileService.getPileAll(map);
		resVo.setData(pileAll); 
		return resVo;
	}
	
	/**
	 * 
	 * 
	 * @api {put} /api/piles/{pileIds}    充电桩置为无效
	 * @apiName dissPiles
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据充电桩Ids将充电桩置为无效
	 * <br/>
	 * @apiParam {Integer[]}    pileIds     充电桩Id  多个Id: 1,2,3 
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError 1102001  充电桩Id不能为空
	 */
	@RequestMapping(value="piles/{pileIds}",method=RequestMethod.PUT)
	public ResultVo dissPiles(@PathVariable(name="pileIds") List<Integer> pileIds) throws BizException{
		ResultVo resVo = new ResultVo();
		pileService.dissPile(pileIds);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/piles/{pileId}    查询充电桩信息
	 * @apiName getPileById
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据充电桩Id查询充电桩信息
	 * <br/>
	 * @apiParam {Integer}    pileId    充电桩Id 
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {Integer(9)} data.pileId 充电设备标识
	 * @apiSuccess {String} data.pileNo 充电设备编码
	 * @apiSuccess {String} data.pileName 设备名称
	 * @apiSuccess {String} data.pileAddr 充电桩地址
	 * @apiSuccess {String} data.pileType 设备类型;字典7
	 * @apiSuccess {String} data.pileStatus 使用状态，有效、无效
	 * @apiSuccess {String} data.pileProtocol 通信协议
	 * @apiSuccess {String} data.gunTypeCode 接口类型;字典8
	 * @apiSuccess {Integer(2)} data.conCycle 心跳周期
	 * @apiSuccess {Integer(9)} data.prcName 当前费率模板
	 * @apiSuccess {Integer(9)} data.prcIdRes 备用费率模板
	 * @apiSuccess {String} data.priceDesc 电价描述
	 * @apiSuccess {Date} 	data.prcChgTime 费率切换时间;格式:yyyy-mm-dd HH24:mi:ss
	 * @apiSuccess {String} data.manufacturerId 生产厂家
	 * @apiSuccess {Integer(9)} data.pileModelId 设备型号
	 * @apiSuccess {String} data.softVersion 软件版本
	 * @apiSuccess {String} data.hardVersion 硬件版本
	 * @apiSuccess {Date} 	data.productionDate 生产日期 yyyy-mm-dd
	 * @apiSuccess {Date} 	data.installDate 安装日期 yyyy-mm-dd
	 * @apiSuccess {Double} data.pileLng 经度 
	 * @apiSuccess {Double} data.pileLat 纬度
	 * @apiSuccess {Integer(9)} data.orgId 所属单位标识
	 * @apiSuccess {Integer(9)} data.stationId 所属场站标识
	 * @apiSuccess {Double} data.ratVolt 额定电压
	 * @apiSuccess {Double} data.ratCurr 额定电流
	 * @apiSuccess {Double} data.ratePower 额定功率
	 * @apiSuccess {Double} data.powerUp 过负荷发生比值
	 * @apiSuccess {Double} data.powerUpRecov 过负荷恢复比值
	 * @apiSuccess {Double} data.voltUp 过压发生比值
	 * @apiSuccess {Double} data.voltUpRecov 过压恢复比值
	 * @apiSuccess {Double} data.voltDown 欠压发生比值
	 * @apiSuccess {Double} data.voltDownRecov 欠压恢复比值
	 * @apiSuccess {Double} data.curMin 最小电流比值
	 * @apiSuccess {Double} data.curMax 最大输出电流
	 * @apiSuccess {String} data.innerPileNo 内部编号
	 * @apiSuccess {String} data.innerPileName 内部名称
	 * @apiSuccess {Double} data.pileCap 充电设施容量
	 * @apiSuccess {String} data.pileGbProtocol 国标协议
	 * @apiSuccess {Integer(11)} data.floatchargeMinElectric 浮充最小电流
	 * @apiSuccess {Integer(11)} data.floatchargeTime 浮充时间
	 * @apiSuccess {Integer(11)} data.chargepriTime 充电优先设置间隔时间
	 * @apiSuccess {Integer(2)} data.isQrcode 屏显二维码
	 * @apiSuccess {Integer(2)} data.qrCodeState 二维码下发状态
	 * @apiSuccess {Integer(2)} data.hlhtQrcodeState 互联互通二维码下发状态
	 * @apiSuccess {Object} data.meter
	 * @apiSuccess {Object} data.meter.meter01
	 * @apiSuccess {Integer(9)} data.meter.meter01.meterId 测量点标识
	 * @apiSuccess {Integer(9)} data.meter.meter01.pileId 所属充电桩标识
	 * @apiSuccess {Integer(2)} data.meter.meter01.innerId 设备内部编号
	 * @apiSuccess {String} data.meter.meter01.meterName 测量点名称
	 * @apiSuccess {String} data.meter.meter01.meterType 表计类型
	 * @apiSuccess {String} data.meter.meter01.meterRatio 变比
	 * @apiSuccess {Double} data.meter.meter01.meterPower 额定功率
	 * @apiSuccess {Object} data.meter.meter01.gun
	 * @apiSuccess {Integer(9)} data.meter.meter01.gun.gunId 充电枪Id,主键
	 * @apiSuccess {String} data.meter.meter01.gun.gumNo 充电枪编号
	 * @apiSuccess {String} data.meter.meter01.gun.gumPoint 充电枪枪口号
	 * @apiSuccess {Integer(2)} data.meter.meter01.gun.innerId 充电枪内部编号
	 * @apiSuccess {String} data.meter.meter01.gun.qrCode 二维码
	 * @apiSuccess {String} data.meter.meter01.gun.parkNum 对应车位编号
	 * @apiSuccess {Integer(9)} data.meter.meter01.gun.pileId 所属充电桩标识
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="piles/{pileId}",method=RequestMethod.GET)
	public ResultVo getPileById(@PathVariable("pileId") Integer pileId) throws BizException{
		ResultVo resVo = new ResultVo();
		Map map = pileService.getPileById(pileId);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * @api {get} /api/toequipments    查询第三方充电桩列表数据
	 * @apiName getToEquipmentinfoAll
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据设备名称,设备型号,充电桩类型,充电桩名称,所属场站,运营商...条件查询第三方充电桩列表数据
	 * <br/>
	 * @apiParam {String} [manufacturername] 设备名称 
	 * @apiParam {String} [equipmentmodel] 设备型号	
	 * @apiParam {Integer} [equipmenttype] 充电桩类型	
	 * @apiParam {String} [equipmentname] 充电桩名称	
	 * @apiParam {String} [stationId] 所属场站ID		
	 * @apiParam {String} [operatorId] 运营商ID-组织机构代码	
	 * @apiParam {Integer} pageNum 页码		
	 * @apiParam {Integer} pageSize 页大小	
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组 
	 * @apiSuccess {Integer} data.list.id 主键Id
	 * @apiSuccess {String} data.list.operatorname 运营商
	 * @apiSuccess {String} data.list.equipmentid 设备编码-设备唯一 对同一运营商，保证唯一
	 * @apiSuccess {String} data.list.stationname 所属场站
	 * @apiSuccess {Integer} data.list.equipmenttype 充电桩类型
	 * @apiSuccess {Double} data.list.power 充电设备总功率
	 * @apiSuccess {Double} data.list.equipmentlng 充电设备经度-GCJ-02坐标系
	 * @apiSuccess {Double} data.list.equipmentlat 充电设备纬度-GCJ-02坐标系
	 * @apiSuccess {String} data.list.manufacturerid 设备生产商组织机构代码
	 * @apiSuccess {String} data.list.equipmentname  充电桩名称
	 * @apiSuccess {String} data.list.manufacturername  设备名称
	 * @apiSuccess {String} data.list.equipmentmodel 设备型号
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="toequipments",method=RequestMethod.GET)
	public ResultVo getToEquipmentinfoAll(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo pageList = pileService.getToEquipmentinfoAll(map);
		resVo.setData(pageList);
		return resVo;
	} 
	
	/**
	 * @api {get} /api/toequipments/{id}    查询第三方充电桩信息
	 * @apiName getToEquipmentinById
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据第三方充电桩id查询第三方充电桩信息
	 * <br/>
	 * @apiParam {Integer}  id  第三方充电桩id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer(11)} data.id 
	 * @apiSuccess {String} data.equipmentid 设备编码-设备唯一 对同一运营商，保证唯一
	 * @apiSuccess {String} data.manufacturerid 设备生产商组织机构代码
	 * @apiSuccess {String} data.manufacturername 
	 * @apiSuccess {String} data.equipmentmodel 设备型号
	 * @apiSuccess {String} data.productiondate 设备生产日期-YYYY-MM-DD
	 * @apiSuccess {String} data.equipmenttype 设备类型
	 * @apiSuccess {Double} data.equipmentlng 充电设备经度-GCJ-02坐标系
	 * @apiSuccess {Double} data.equipmentlat 充电设备纬度-GCJ-02坐标系
	 * @apiSuccess {Double} data.power 
	 * @apiSuccess {String} data.equipmentname 
	 * @apiSuccess {String} data.stationid 充电站ID
	 * @apiSuccess {String} data.operatorid 运营商ID-组织机构代码
	 * @apiSuccess {Object[]} data.toConnList 充电设备接口信息
	 * @apiSuccess {Integer(11)} data.toConnList.id 
	 * @apiSuccess {String} data.toConnList.connectorid 充电设备接口编码
	 * @apiSuccess {String} data.toConnList.connectorname 充电设备接口名称
	 * @apiSuccess {String} data.toConnList.connectortype 充电设备接口类型
	 * @apiSuccess {Integer(11)} data.toConnList.voltageupperlimits 额定电压上限
	 * @apiSuccess {Integer(11)} data.toConnList.voltagelowerlimits 额定电压下限
	 * @apiSuccess {Integer(11)} data.toConnList.current 额定电流
	 * @apiSuccess {Double} data.toConnList.power 额定功率
	 * @apiSuccess {String} data.toConnList.parkno 车位号
	 * @apiSuccess {String} data.toConnList.equipmentid 设备编码-设备唯一 对同一运营商，保证唯一
	 * @apiSuccess {String} data.toConnList.operatorid 运营商ID-组织机构代码
	 * @apiSuccess {Integer(11)} data.toConnList.nationalstandard 国家标准
	 * 
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="toequipments/{id}",method=RequestMethod.GET)
	public ResultVo getToEquipmentinById(@PathVariable("id") Integer id) throws BizException{
		ResultVo resVo = new ResultVo();
		Map  map = pileService.getToEquipmentinById(id);
		resVo.setData(map);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/piles/pile    充电桩业务字典
	 * @apiName getPile
	 * @apiGroup BusiDicts
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据用户Id查询充电桩业务字典
	 * <br/>
	 * @apiParam {Integer}    [userId]  	用户Id
	 * @apiParam {String}     [pileName] 	充电桩名称
	 * @apiParam {Integer}    [orgId]    	运营商Id
	 * @apiParam {Integer}    [stationId]	充电站Id
	 * @apiParam {Integer}    [pileProtocol]	通讯协议(51,表示深圳电动汽车充电协议；62，表示科陆电动汽车充电协议)
	 * @apiParam {Integer}    [pageNum]    页码  不传默认为1
	 * @apiParam {Integer}    [pageSize]   页大小 不传默认为20
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Integer} data.list.pileId 充电桩Id
	 * @apiSuccess {String} data.list.pileNo  充电桩编号
	 * @apiSuccess {String} data.list.pileName 充电桩名称
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="piles/pile",method=RequestMethod.GET)
	public ResultVo getPile(@RequestParam Map map) throws BizException{
		ResultVo resVo = new ResultVo();
		PageInfo list = pileService.getPile(map);
		resVo.setData(list);
		return resVo;
	}
	
	/**
	 * @api {POST} /api/piles    新增充电桩
	 * @apiName  insertPile
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 高辉             
	 * <br/>
	 * @apiParam {String}     pileName  	          充电桩名称
	 * @apiParam {String}     subsNo 	                       充电桩编号
	 * @apiParam {String}     selfNo    	           自编号
	 * @apiParam {Integer}    orgId	                       运营商id
	 * @apiParam {Integer}    stationId       所属场站id
	 * @apiParam {String}    pileType        充电桩类型
	 * @apiParam {String}    pileStatus      使用状态
	 * @apiParam {String}     [innerPileNo]     内部编码
	 * @apiParam {String}    [innerPileName]  	内部名称
	 * @apiParam {Integer}    pileProtocol  	通信协议   1 深圳科陆
	 * @apiParam {Integer}    [isQRCode]  	           屏显二维码
	 * @apiParam {Integer}     conCycle       心跳周期 15、30、60
	 * @apiParam {String}      pileAddr  	           通讯地址  51- 深圳  62 -科陆
	 * @apiParam {Integer}    [prcIdPre]  	           当前电价
	 * @apiParam {String}    [priceDesc]  	          电价描述
	 * @apiParam {String}    [softVersion]     软件版本
	 * @apiParam {String}    [hardVersion]  	硬件版本
	 * @apiParam {String}     manufacturerId  生产厂家
	 * @apiParam {Integer}    pileModelId  	设备型号
	 * @apiParam {Integer}    [pileCap]  	         桩额定功率   
	 * @apiParam {String}    pileGbProtocol  国际类型  // 1 2
	 * @apiParam {Date}      [productDate]  	生产日期
	 * @apiParam {Date}      [instDate]        安装日期
	 * @apiParam {Int}      [numberGun]        枪数
	 * @apiParam {Int}      [ortMode]        交直模式
	 * @apiParam {Int}      [powerMode]      功率模式
	 * @apiParam {String}      [serialNumber]      出厂编号
	 * @apiParam {String}      [screenVersion]      屏幕版本
	 * @apiParam {Int}      [auxiPower]      辅助电源
	 * @apiParam {Double}      [ratePower]      额定电源
	 *
	 * @apiParam {object[]}   meter               表计
	 * @apiParam {String}     meter.meterName     表计名称
	 * @apiParam {String}     [meter.meterType]   表计类型
	 * @apiParam {Decimal}    [meter.ratPower]    表计额定功率
	 * @apiParam {String}     [meter.meterRatio]  变比
	 * @apiParam {object[]}   [meter.gun]         枪
	 * @apiParam {String}     meter.gun.qrCode   二维码
	 * @apiParam {String}     [meter.gumPoint]   充电枪名称
	 * @apiParam {String}     [meter.parkNum]  	    对应的车位
	 * @apiParamExample {json} 入参示列
	 * {
     *	"pileName": "xxxxxx",
     *	"subsNo": "22222",
     * 	"selfNo": "113",
     *	"orgId": 1,
     *	"stationId": 11,
     *	"pileType": "3",
     *	"pileStatus": "0",
     *	"innerPileNo": "",
     *	"innerPileName": "",
     *	"pileProtocol": "1",
     *	"conCycle": 15,
     *	"pileAddr": "yyyyy",
     *	"manufacturerId": "1",
     *	"pileGbProtocol": "2",
     *	"pileModelId": 11,
	 *	"numberGun":2,
	 *	"ortMode":1 ,
	 *	"powerMode":1 ,
	 *	"ratePower":22.33 ,
	 *	"serialNumber":"出厂编号修改" ,
	 *	"screenVersion":"版本修改" ,
	 *	"auxiPower":1,
     *	"meter": [
     *   			{
     *       		 "meterName": "A枪出线表",
     *       		 "meterType": "",
     *       		 "innerId": "1",
     *       		 "meterRatio": "",
     *       		 "gun": {
     *           				"qrCode": "sdsdsdw22",
     *          				"gumPoint": "充电枪A",
     *           				"parkNum": ""
     *       		         }
     *   	        },
     *              {
     *               "meterName": "B枪出线表",
     *               "meterType": "",
     *               "innerId": "0",
     *               "meterRatio": "",
     *               "gun": {
     *                       "qrCode": "sdsdsdw2444",
     *                       "gumPoint": "充电枪B",
     *                       "parkNum": ""
     *                      }
     *              },
     *              {
     *               "meterName": "总表",
     *               "meterType": "",
     *               "innerId": "1",
     *               "meterRatio": "",
     *               "gun": {}
     *              }
     *        ]
     * }
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001  参数空异常
	 * @apiError -1102007  重复异常
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/piles",method=RequestMethod.POST)
	public ResultVo insertPile(@RequestBody Map map) throws BizException{
		pileService.insertPile(map);
		return new ResultVo();
	}
	/**
	 * @api {PUT} /api/piles    编辑充电桩
	 * @apiName  modifyPile
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 高辉             编辑充电桩
	 * <br/>pileId
	 * @apiParam {Integer}     pileId  	                       充电桩id
	 * @apiParam {String}     pileName  	           充电桩名称
	 * @apiParam {String}     pileNo	                       充电桩编号
	 * @apiParam {Integer}    orgId	                       运营商id
	 * @apiParam {Integer}    stationId]      所属场站id
	 * @apiParam {String}     pileType        充电桩类型
	 * @apiParam {String}     pileStatus      使用状态
	 * @apiParam {String}     [innerPileNo]     内部编码
	 * @apiParam {String}     [innerPileName]  	内部名称
	 * @apiParam {Integer}    pileProtocol  	通信协议   1 深圳科陆
	 * @apiParam {Integer}    isQRCode  	            屏显二维码
	 * @apiParam {Integer}    conCycle        心跳周期 15、30、60
	 * @apiParam {String}     pileAddr  	            通讯地址  51- 深圳  62 -科陆
	 * @apiParam {Integer}    [prcIdPre]  	            当前电价
	 * @apiParam {String}     [priceDesc]  	           电价描述
	 * @apiParam {String}     [softVersion]     软件版本
	 * @apiParam {String}     [hardVersion]  	硬件版本
	 * @apiParam {String}     manufacturerId  生产厂家
	 * @apiParam {Integer}    [pileModelId]  	设备型号
	 * @apiParam {Integer}    [pileCap]  	            桩额定功率   
	 * @apiParam {String}     [pileGbProtocol]  国际类型  // 1 2
	 * @apiParam {Date}       [productDate]  	生产日期
	 * @apiParam {Date}       [instDate]        安装日期
	 * 
	 * @apiParam {object[]}   meter               表计
	 * @apiParam {String}     meter.meterName     表计名称
	 * @apiParam {String}     [meter.meterType]   表计类型
	 * @apiParam {Decimal}    [meter.ratPower]    表计额定功率
	 * @apiParam {String}     [meter.meterRatio]  变比
	 * @apiParam {object[]}   [meter.gun]         枪
	 * @apiParam {String}     meter.gun.qrCode   二维码
	 * @apiParam {String}     [meter.gumPoint]   充电枪名称
	 * @apiParam {String}     [meter.parkNum]  	    对应的车位
	 * @apiParamExample {json} 入参示列
	 * {
	 *  "pileId":11111,
     *	"pileName": "xxxxxx",
     *	"subsNo": "000000456106",
     * 	"selfNo": "119",
     *	"orgId": 1,
     *	"stationId": 11,
     *	"pileType": "3",
     *	"pileStatus": "0",
     *	"innerPileNo": "",
     *	"innerPileName": "",
     *	"pileProtocol": "1",
     *	"conCycle": 15,
     *	"pileAddr": "yyyyy",
     *	"manufacturerId": "1",
     *	"pileGbProtocol": "2",
     *	"pileModelId": 11,
	 *	"numberGun":2,
	 *	"ortMode":1 ,
	 *	"powerMode":1 ,
	 *	"ratePower":22.33 ,
	 *	"serialNumber":"出厂编号修改" ,
	 *	"screenVersion":"版本修改" ,
	 *	"auxiPower":1,
	 *	"pileNo":"000000456106119",
     *	"meter": [
     *   			{
     *       		 "meterName": "A枪出线表",
     *       		 "meterType": "",
     *       		 "innerId": "1",
     *       		 "meterRatio": "",
     *       		 "gun": {
     *           				"qrCode": "sdsdsdw22",
     *          				"gumPoint": "充电枪A",
     *           				"parkNum": ""
     *       		         }
     *   	        },
     *              {
     *               "meterName": "B枪出线表",
     *               "meterType": "",
     *               "innerId": "0",
     *               "meterRatio": "",
     *               "gun": {
     *                       "qrCode": "sdsdsdw2444",
     *                       "gumPoint": "充电枪B",
     *                       "parkNum": ""
     *                      }
     *              }
     *        ]
     * }
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/piles",method=RequestMethod.PUT)
	public ResultVo modifyPile(@RequestBody Map map) throws BizException{
		pileService.modifyPile(map);
		return new ResultVo();
	}
	/**
	 * @api {GET} /api/piles/_export    导出充电桩列表
	 * @apiName  exportPiles
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 高辉       根据区域,运营商,场站,生产厂家,充电桩名称,充电桩类型,使用状态,通讯协议条件导出充电桩
	 * <br/>
	 * @apiParam {String}   [pileName]        充电桩名称  
	 * @apiParam {String} 	[ortMode]  		  交直模式(type=jzms)
	 * @apiParam {String}   [pileStatus]      使用状态  有效 -0、无效 -1
	 * @apiParam {String}   [pileProtocol]    通讯协议 
	 * @apiParam {String}   [manufacturerId]  生产厂家 
	 * @apiParam {Integer}  [orgId]           运营商
	 * @apiParam {Integer}  [stationId]       所属场站 
	 * @apiParam {String}   [provCode]        省 
	 * @apiParam {String}   [cityCode]        市 
	 * @apiParam {Integer}   pageNum          页码 
	 * @apiParam {Integer}   pageSize 页大小 
	 * @apiParam {String}   [sort]            排序字段
	 * @apiParam {String}   [order]           排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiError -999  系统异常！
	 * @apiError -1102001  参数空异常！
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/piles/_export",method=RequestMethod.GET)
	public void exportPiles(@RequestParam Map map,HttpServletResponse response) throws Exception{
		pileService.exportPiles(map,response);
	}
	/**
	 * @api {GET} /api/toequipments/_export    导出第三方充电桩列表
	 * @apiName  exportEquipments
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 高辉      根据设备名称,设备型号,充电桩类型,充电桩名称,所属场站,运营商...条件查询第三方充电桩列表数据
	 * <br/>
	 * @apiParam {Integer}   userId             登陆用户id
	 * @apiParam {String}   [manufacturername]  设备名称 
	 * @apiParam {String}   [equipmentmodel]    设备型号	
	 * @apiParam {Integer}  [equipmenttype]     充电桩类型	
	 * @apiParam {String}   [equipmentname]     充电桩名称	
	 * @apiParam {String}   [stationId]         所属场站id	
	 * @apiParam {String}   [operatorId]        运营商ID-组织机构代码	
	 * @apiParam {Integer}  pageNum             页码		
	 * @apiParam {Integer}  pageSize            页大小
	 * @apiParam {String}   [sort]              排序字段
	 * @apiParam {String}   [order]             排序(DESC:降序|ASC:升序)	
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/toequipments/_export",method=RequestMethod.GET)
	public void exportEquipments(@RequestParam Map map,HttpServletResponse response) throws Exception{
		pileService.exportEquipments(map,response);
	}
	/**
	 * @api {GET} /api/piles/_exportQrcodes    互联互通二维码导出
	 * @apiName  exportQrcodes
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 高辉       根据区域,运营商,场站,生产厂家,充电桩名称,充电桩类型,使用状态,通讯协议条件导出互联互通二维码导出
	 * <br/>
	 * @apiParam {String}   [pileName]        充电桩名称  
	 * @apiParam {String}   [pileType]        充电桩类型;cdzlx 
	 * @apiParam {String}   [pileStatus]      使用状态  有效 -0、无效 -1
	 * @apiParam {String}   [pileProtocol]    通讯协议 
	 * @apiParam {String}   [manufacturerId]  生产厂家 
	 * @apiParam {Integer}  [orgId]           运营商
	 * @apiParam {Integer}  [stationId]       所属场站 
	 * @apiParam {String}   [provCode]        省 
	 * @apiParam {String}   [cityCode]        市 
	 * @apiParam {Integer}   pageNum          页码 
	 * @apiParam {Integer}   pageSize 页大小 
	 * @apiParam {String}   [sort]            排序字段
	 * @apiParam {String}   [order]           排序(DESC:降序|ASC:升序)
	 * <br/>
	 * @apiError -999  系统异常！
	 * @apiError -1102001  参数空异常！
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/piles/_exportQrcodes",method=RequestMethod.GET)
	public void exportQrcodes(@RequestParam Map map,HttpServletResponse response,HttpServletRequest request) throws Exception{
		pileService.exportQrcodes(map,response, request);
	}
	/**
	 * 
     * @api {POST} /api/piles/_import   充电桩导入
     * @apiName  importPiles
     * @apiGroup PileController
     * @apiVersion 2.0.0
     * @apiDescription 高辉    根据充电桩导入模版导入保存数据
     * <br/>
     * @apiParam {Integer}       userId                    用户id
     * @apiParam {MultipartFile} file                      充电桩模版
     * <br/>
     * @apiSuccess {String}     errorCode                  错误码   0-导入成功  1-导入失败
     * @apiSuccess {String}     errorMsg                   消息说明
     * @apiSuccess {Object}     data                       分页数据封装
     * @apiSuccess {String[]}   data.list 		                         分页数据封装集合                
     * @apiSuccess {String}		data.list.impStatus			 数据状态
     * @apiSuccess {String}     data.list.pileName 		         充电桩名称
     * @apiSuccess {String}     data.list.subsNo	 	         充电桩自编号
     * @apiSuccess {String}     data.list.stationName      所属场站名称
     * @apiSuccess {String}     data.list.pileAddr 		         通讯地址
     * @apiSuccess {String}     data.list.ortMode		         交直模式
     * @apiSuccess {String}     data.list.numberGun		         枪数
     * @apiSuccess {String}     data.list.powerMode		         功率模式
     * @apiSuccess {String}     data.list.protocol 		         通信协议
     * @apiSuccess {String}     data.list.pileAddr 		         通讯地址
     * @apiSuccess {String}     data.list.pileConCycle 	        心跳周期
	 * @apiSuccess {String}     data.list.manufacturer     生产厂家名称
	 * @apiSuccess {String}     data.list.serialNumber     出厂编号
	 * @apiSuccess {String}     data.list.pileModel        设备型号
	 * @apiSuccess {String}     data.list.productionDate   生产日期
     * @apiSuccess {String}     data.list.installDate	        安装日期
     * @apiSuccess {String}     data.list.innerPileNo 	        内部编号
     * @apiSuccess {String}     data.list.innerPileName    内部名称
     * @apiSuccess {String}     data.list.ratePower		        额定功率      
     * @apiSuccess {String}     data.list.gbProtocol       国际类型
     * @apiSuccess {String}     data.list.isCode 	                    屏显二维码
     * @apiSuccess {String}     data.list.description 	        检验失败描述
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/piles/_import",method=RequestMethod.POST)
	public ResultVo importPiles(HttpServletRequest request,@RequestParam("file") MultipartFile file,@RequestParam Map map) throws Exception{
		ResultVo resultVo = new ResultVo();
		MultipartRequest multipartRequest=(MultipartRequest) request;
		MultipartFile excelFile=multipartRequest.getFile("file");
		List<Map> list = pileService.importPiles(excelFile,map);
		resultVo.setData(list);
		if(null==list || list.isEmpty()){
			resultVo.setErrorCode(0);
		}else{
			resultVo.setErrorCode(1);
			resultVo.setErrorMsg("导入失败!");
		}
		return resultVo;
	}
	
	/**
     * @api {POST} /api/dicts/pile    充电桩名称或编号业务字典
     * @apiName  getPileNoAndNameDict
     * @apiGroup PileController
     * @apiVersion 2.0.0
     * @apiDescription 高辉     设备名称或编号业务字典
     * <br/>
     * @apiParam {Integer}       userId                    用户id
     * @apiParam {Integer}       [pileId]                  充电桩id
     * @apiParam {Integer}       [orgId]                   所属企业id
     * @apiParam {Integer}       [stationId]               所属场站id
     * <br/>
     * @apiSuccess {String}     errorCode                  错误码  
     * @apiSuccess {String}     errorMsg                   消息说明
     * @apiSuccess {Object}     data                       数据封装
     * @apiSuccess {String[]}   data.list 		                                 数据集合
     * @apiSuccess {String}     data.list.pileId	 	         充电桩Id
     * @apiSuccess {String}     data.list.pileName 		         充电桩名称
     * @apiSuccess {String}     data.list.pileNo	 	         充电桩编号
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     * @apiError -1102001 用户id为空!
	 */
	@RequestMapping(value = "/dicts/pile", method = RequestMethod.GET)
	public ResultVo  getPileNoAndNameDict(@RequestParam Map map) throws BizException{
		ResultVo resultVo = new ResultVo();
		List<Map> list = pileService.getPileNoAndNameDict(map);
		resultVo.setData(list);
		return resultVo;
	}
	
	/**
	 * 
	 * 
	 * @api {get} /api/piles/_gunNoInnerIds    根据枪数和交直模式查询 出线表信息
	 * @apiName getGunNoInnerIds
	 * @apiGroup PileController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据枪数和交直模式查询 出线表信息
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Integer}    numberGun    枪数
	 * @apiParam {Integer}    ortMode     交直模式
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object} data.meter 数据对象
	 * @apiSuccess {Object} data.meter.meter01 数据对象
	 * @apiSuccess {Object} data.meter.meter01.innerId 桩内表编号
	 * @apiSuccess {Object} data.meter.meter01.meterName 表计名称
	 * @apiSuccess {Object} data.meter.meter01.key key
	 * @apiSuccess {Object} data.meter.meter01.gun 
	 * @apiSuccess {Object} data.meter.meter01.gun.gumPoint 枪口名称
	 * 
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value = "piles/_gunNoInnerIds", method = RequestMethod.GET)
	public ResultVo getGunNoInnerIds(@RequestParam Map map){
		ResultVo resVo = new ResultVo();
		Map<String, Map> gunNoInnerIdMap = pileService.getGunNoInnerIds(map);
		resVo.setData(gunNoInnerIdMap);
		return resVo;
	}
	
}

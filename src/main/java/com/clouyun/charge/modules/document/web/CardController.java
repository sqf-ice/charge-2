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

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.CardService;
import com.github.pagehelper.PageInfo;

/**
 * 描述: CardController  卡片管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: sim.y
 * 版本: 2.0
 * 创建日期: 2017年6月16日
 */
@RestController
@RequestMapping("/api/cards")
public class CardController  extends BusinessController{
	
	
	@Autowired
	CardService cardService;
	/**
	 * 
	 * @api {get} /api/cards   查询卡片列表信息
	 * @apiName getCardAll
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 查询卡片列表信息
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Date}    [startTime]     开卡时间 yyyy-MM-dd
	 * @apiParam {Date}    [endTime]     	  开卡时间 yyyy-MM-dd
	 * @apiParam {String}    [cardId]      卡号
	 * @apiParam {String}    [consName]    会员名称
	 * @apiParam {String}    [groupName]   集团名称
	 * @apiParam {String}    [consPhone]   电话号码
	 * @apiParam {String}    [licensePlate] 车牌号
	 * @apiParam {Integer}   pageNum  页码
	 * @apiParam {Integer}	 pageSize 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {Object[]} data.list 分页数据对象数组
	 * @apiSuccess {Date} data.list.actvTime 开卡时间
	 * @apiSuccess {String} data.list.cardId 卡号
	 * @apiSuccess {String} data.list.consName 会员名称
	 * @apiSuccess {String} data.list.consTypeCode 会员类型 hylb:01.个人会员  02.集团会员
	 * @apiSuccess {String} data.list.groupName 集团名称
	 * @apiSuccess {String} data.list.consPhone 电话号码
	 * @apiSuccess {String} data.list.licensePlate 车牌号
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001   {0}不能为空!
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResultVo getCardAll(@RequestParam Map map) throws Exception{
		ResultVo vo = new ResultVo();
		PageInfo pageList = cardService.getCardAll(map);
		vo.setData(pageList);
		return vo;
	}
	
	/**
	 * 
	 * @api {get} /api/cards/{cardId}   查询卡片信息
	 * @apiName getCardById
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据卡号查询卡片信息
	 * <br/>
	 * @apiParam {String}    cardId     卡号
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * @apiSuccess {String} data.cardId  卡号 
	 * @apiSuccess {String} data.cardType  卡类型  kpfflx  01:预付费   02:后付费
	 * @apiSuccess {Integer} data.orgId   运营商Id 
	 * @apiSuccess {Date} data.actvTime  开卡日期
	 * @apiSuccess {String} data.consPhone  电话号码
	 * @apiSuccess {String} data.consName  会员名称 
	 * @apiSuccess {String} data.consTypeCode  会员类型 hylb:01.个人会员  02.集团会员
	 * @apiSuccess {String} data.groupName  集团名称 
	 * @apiSuccess {String} data.licensePlate  车牌号 
	 * @apiSuccess {String} data.brand  车品牌
	 * @apiSuccess {String} data.model  车型
	 * @apiSuccess {Integer} data.vehicleId  车辆Id 
	 * @apiSuccess {Integer} data.consId  会员Id 
	 * @apiSuccess {Integer} data.cardId  卡号 
	 * @apiSuccess {Integer} data.groupId  集团Id
	 * @apiSuccess {Integer} data.isCheck  是否一卡一车绑定 0:否  1:是  
	 * 
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="{cardId}",method = RequestMethod.GET)
	public ResultVo getCardById(@PathVariable("cardId") String cardId,@RequestParam Map map) throws Exception{
		ResultVo vo = new ResultVo();
		Map cardMap = cardService.getCardById(cardId,map);
		vo.setData(cardMap);
		return vo;
	}
	
	/**
	 * 
	 * @api {post} /api/cards    新增卡片信息 - 开卡
	 * @apiName saveCardInfo
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B  新增卡片信息 - 开卡
	 * <br/>
	 * @apiParam {Integer}    userId    用户Id
	 * @apiParam {String}    cardId     卡号
	 * @apiParam {Integer}    orgId    运营商Id
	 * @apiParam {Date}    actvTime    开卡日期 yyyy-MM-dd
	 * @apiParam {String} [cardType]  卡类型  kpfflx  01:预付费   02:后付费
	 * @apiParam {String}    consPhone    电话号码
	 * @apiParam {String}    consName    会员名称
	 * @apiParam {Integer}   consTypeCode    会员类型 hylb:01.个人会员  02.集团会员
	 * @apiParam {String}    [groupName]    集团名称 若会员类型为集团会员则集团名称为必填
	 * @apiParam {Integer}   [vehicleId]    车辆Id
	 * @apiParam {String}    licensePlate  车牌号
	 * @apiParam {String}	 [brand]			车品牌
	 * @apiParam {String}	 [model]			车型
	 * @apiParam {Integer}	 [consId]		会员Id
	 * @apiParam {Integer}	 [groupId]		集团Id
	 * @apiParam {Integer}	 isCheck		是否绑定一卡一车
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001  {0}不能为空!
	 * @apiError -1102006   {0}格式异常!
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResultVo saveCardInfo(@RequestBody Map map) throws Exception{
		ResultVo vo = new ResultVo();
		cardService.saveCardInfo(map);
		return vo;
	}
	
	/**
	 * 
	 * @api {post} /api/cards/_recharge   卡充值
	 * @apiName rechargeCard
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 卡充值
	 * <br/>
	 * @apiParam {String}    cardId    卡号
	 * @apiParam {Double}	 amount	   充值金额
	 * @apiParam {String}	 verifyType 付款方式 kczfs: 01.微信 02.支付宝 03.现金  04.其他
	 * @apiParam {Integer}	 stationId 收款场站
	 * @apiParam {Integer}	 userId    用户Id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_recharge",method=RequestMethod.POST)
	public ResultVo rechargeCard(@RequestBody Map map) throws Exception{
		ResultVo vo = new ResultVo();
		cardService.rechargeCard(map);
		return vo;
	}
	
	/**
	 * 
	 * 
	 * @api {post} /api/cards/_unlock    解锁
	 * @apiName unlockCard
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 解锁
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Integer}    orgId     运营商Id
	 * @apiParam {String}    cardId     卡号
	 * @apiParam {Double}    amount     调济金额
	 * 
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001 {0}不能为空!
	 */
	@RequestMapping(value="_unlock",method = RequestMethod.POST)
	public ResultVo unlockCard(@RequestBody Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		cardService.unlockCard(map);
		return resVo;
	}
	
	
	/**
	 * 
	 * @api {get} /api/cards/_export    卡片信息导出
	 * @apiName exprotCard
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 卡片信息导出
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Date}    [startTime]     开卡时间 yyyy-MM-dd
	 * @apiParam {Date}    [endTime]     	  开卡时间 yyyy-MM-dd
	 * @apiParam {String}    [cardId]      卡号
	 * @apiParam {String}    [consName]    会员名称
	 * @apiParam {String}    [groupName]   集团名称
	 * @apiParam {String}    [consPhone]   电话号码
	 * @apiParam {String}    [licensePlate] 车牌号
	 * @apiParam {Integer}   pageNum  页码
	 * @apiParam {Integer}	 pageSize 页大小
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(value="_export",method = RequestMethod.GET)
	public void exprotCard(@RequestParam Map map,HttpServletResponse response) throws Exception{
		cardService.exportCard(map, response);
	}
	
	/**
	 * 
	 * 
	 * @api {get} /api/cards/_pass    获取运营商秘钥
	 * @apiName getPassMap
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 方法描述
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * @apiSuccess {Integer} data.total     总记录数
	 * @apiSuccess {String} data.key  运营商Id+秘钥类型 列: 24_1
	 * @apiSuccess {String} data.value 秘钥
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1102001 用户不能为空!
	 */
	@RequestMapping(value="_pass",method=RequestMethod.GET)
	public ResultVo getPassMap(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		Map passMap = cardService.getPassMap(map);
		resVo.setData(passMap);
		return resVo;
	}
	
	/**
	 * 
	 * @api {put} /api/cards    编辑卡
	 * @apiName updateCardInfo
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B  编辑卡
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Data}    actvTime     开卡时间 (yyyy-MM-dd)
	 * @apiParam {Integer}    vehicleId    车辆Id
	 * @apiParam {Integer}    isCheck     是否绑定 0:未绑定  1:绑定
	 * @apiParam {String}    [cardType]  卡类型  kpfflx  01:预付费   02:后付费
	 * @apiParam {String}    cardId     卡号
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public ResultVo updateCardInfo(@RequestBody Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		cardService.updateCardInfo(map);
		return resVo;
	}
	
	
	/**
	 * 
	 * @api {get} /api/cards/consInfo    根据会员电话和运营商关联会员信息
	 * @apiName queryConsInfo
	 * @apiGroup CardController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 根据会员电话和运营商关联会员信息
	 * <br/>
	 * @apiParam {Integer}    userId     用户Id
	 * @apiParam {Integer}    consPhone     联系电话
	 * @apiParam {Integer}    appFrom     运营商Id
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
	@RequestMapping(value="consInfo",method=RequestMethod.GET)
	public ResultVo queryConsInfo(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		Map consMap = cardService.queryConsInfo(map);
		resVo.setData(consMap);
		return resVo;
	}
	
	/**
	 * 
	 * @api {get} /api/cards/_exist   判断卡号是否存在
	 * @apiName getCardIdIsExist
	 * @apiGroup CardController.java
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅B 方法描述
	 * <br/>
	 * @apiParam {Integer}	 userId 	用户Id
	 * @apiParam {Integer}    cardId    卡号
	 * <br/>
	 * @apiSuccess {String} errorCode   错误码
	 * @apiSuccess {String} errorMsg    消息说明
	 * @apiSuccess {Object} data        分页数据封装
	 * <br/>
	 * @apiError -999  系统异常
	 * @apiError -888  请求方式异常
	 * @apiError -1103002 卡号重复!
	 */
	@RequestMapping(value="_exist",method = RequestMethod.GET)
	public ResultVo getCardIdIsExist(@RequestParam Map map) throws Exception{
		ResultVo resVo = new ResultVo();
		DataVo vo = new DataVo(map); 
		if(vo.isBlank("userId")){
			throw new BizException(1102001, "用户");
		}
		if(vo.isBlank("cardId")){
			throw new BizException(1102001, "卡号");
		}
		
		cardService.getCardIdIsExist(vo.getString("cardId"));
		return resVo;
	}
}

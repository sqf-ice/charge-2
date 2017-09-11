package com.clouyun.charge.modules.document.web;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.document.service.QRCodeManagementService;

/**
 * 描述: 二维码管理控制器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年2月21日 下午2:09:50
 */
@RestController
@RequestMapping("/api/qrCodes")
public class QRCodeManagementController extends BusinessController {
	
	public static final Logger logger = LoggerFactory.getLogger(QRCodeManagementController.class);
	
	@Autowired
	QRCodeManagementService codeManagementService;
	
	/**
	 * 根据桩ID获取充电桩信息
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/qrcode", method = RequestMethod.POST)
	public ResultVo getPile(@RequestBody DataVo data) throws Exception {
		ResultVo vo = new ResultVo();
		vo.setData(codeManagementService.selectByPrimaryKey(data));
		return vo;
	}
	
	/**
	 * 根据查询条件获取充电桩信息
	 * @param data 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResultVo getAllPile(@RequestBody DataVo data) throws Exception {
		return codeManagementService.selectAll(data);
	}
	
	/**
	 * 根据查询条件获取充电桩信息
	 * @param data 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chgGuns", method = RequestMethod.POST)
	public ResultVo getChgGunByPileId(@RequestBody DataVo data) throws Exception {
		ResultVo vo = new ResultVo();
		vo.setData(codeManagementService.selectChgGunByPileId(data));
		return vo;
	}
	
	/**
	 * 根据字典表ID和字典Num获取字典明细
	 */
	@RequestMapping(value = "/dictItem", method = RequestMethod.POST)
	public ResultVo getPubDictItemByKey(@RequestBody DataVo data) throws Exception {
		return codeManagementService.selectPubDictItemByKey(data);
	}
	
	/**
	 * 验证二维码唯一性
	 */
	@RequestMapping(value = "/qrCodeCount", method = RequestMethod.POST)
	public ResultVo getQRCodeCount(@RequestBody DataVo data) throws Exception {
		return codeManagementService.QRCodeCount(data);
	}
	
	/**
	 * 修改二维码
	 */
	@RequestMapping(value = "/putChgGuns", method = RequestMethod.POST)
	public ResultVo updateAgumPointQRCode(@RequestBody DataVo data) throws Exception {
		ResultVo vo = new ResultVo();
		int count = codeManagementService.updateGumPointQRCode(data);
		vo.setData(count);
		return vo;
	}
	
	/**
	 * 修改下发状态
	 */
	@RequestMapping(value = "/uPileCodeState", method = RequestMethod.POST)
	public ResultVo updatePileCodeState(@RequestBody DataVo data) throws Exception {
		return codeManagementService.updatePileCodeState(data);
	}
	
	/**
	 * 修改互联互通下发状态
	 */
	@RequestMapping(value = "/uHLHT", method = RequestMethod.POST)
	public ResultVo updateHLHTCodeState(@RequestBody DataVo data) throws Exception {
		return codeManagementService.updateHLHTCodeState(data);
	}
	
	/**
	 * 新增二维码
	 */
	@RequestMapping(value = "/iQRCode", method = RequestMethod.POST)
	public ResultVo insertQRCode(@RequestBody DataVo data) throws Exception {
		return codeManagementService.insertQRCode(data);
	}
	
	/**
	 * 通过充电桩编号获取充电桩信息
	 */
	@RequestMapping(value = "/piles/pileNo", method = RequestMethod.POST)
	public ResultVo selectPileByPileNo(@RequestBody DataVo data) throws Exception {
		return codeManagementService.selectPileByPileNo(data);
	}
	
	/**
	 * 通过桩ID获取表计信息
	 */
	@RequestMapping(value = "/chgMeters", method = RequestMethod.POST)
	public ResultVo selectChgmeterByPileId(@RequestBody DataVo data) throws Exception {
		return codeManagementService.selectChgmeterByPileId(data);
	}
	
	/**
	 * 插入枪表表计表
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/iGunMeters", method = RequestMethod.POST)
	public ResultVo inserGunMeters(@RequestBody Map data) throws Exception {
		ResultVo vo = new ResultVo();
		vo.setData(codeManagementService.inserGunMeters(data));
		return vo;
	}
	
	/**
	 * 更新枪表
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uGumPointQRCode", method = RequestMethod.POST)
	public ResultVo uGumPointQRCode(@RequestBody Map data) throws Exception {
		ResultVo vo = new ResultVo();
		int count = codeManagementService.updateGumPointQRCode(data);
		vo.setData(count);
		return vo;
	}
	
}
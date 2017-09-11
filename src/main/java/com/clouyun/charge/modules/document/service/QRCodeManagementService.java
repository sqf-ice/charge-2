package com.clouyun.charge.modules.document.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.modules.document.mapper.QRCodeManagementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年2月27日 上午9:26:05
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QRCodeManagementService {
	
	public static final Logger logger = LoggerFactory.getLogger(QRCodeManagementService.class);
	
	@Autowired
	QRCodeManagementMapper codeManagementMapper;
	
	public Map selectByPrimaryKey(DataVo vo)  {
		Map list = codeManagementMapper.selectByPrimaryKey(vo);
		return list;
	}
	/**
	 * 插入表计表
	 * @param vo
	 * @return
	 */
	public int inserGunMeters(Map vo)  {
		List<Map> list = (List<Map>) vo.get("saveList");
		int inserChgMeter = codeManagementMapper.inserChgMeter(list);
		List saveChgGunList = new ArrayList();
		List<Integer> pileIds  = new ArrayList<>();
		for (Map map : list) {
			//总表不插枪
			if(null != map.get("gumPoint")){
				saveChgGunList.add(map);
			}
			pileIds.add(Integer.parseInt(map.get("pileId").toString()));
		}
		int inserChgGun = codeManagementMapper.inserChgGun(saveChgGunList);
		//新增时设置下发状态为未下发
		vo.put("qrCodeState", 0);
		vo.put("hlhtQRCodeState", 0);
		vo.put("pileIds", pileIds);
		codeManagementMapper.updatePileCodeState(vo);
		codeManagementMapper.updateHLHTCodeState(vo);
		return inserChgGun;
	}
	
	public ResultVo selectAll(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		String partten = "[A-Za-z0-9\u4E00-\u9FA5]*";
		//如果场站id无值,按照场站名称模糊查询
		if(null == dataVo.get("stationId") && null != dataVo.get("stationName") && !"".equals(dataVo.get("stationName"))){
			if(!Pattern.matches(partten, dataVo.get("stationName").toString())){
				dataVo.put("stationName", "-999");
			}
		}
		
		//如果运营商id无值,按照运营商名称模糊查询
		if(null == dataVo.get("orgId") && null != dataVo.get("orgName") && !"".equals(dataVo.get("orgName"))){
			if(!Pattern.matches(partten, dataVo.get("orgName").toString())){
				dataVo.put("orgName", "-999");
			}
		}
		List<Map> all = codeManagementMapper.selectAll(dataVo);

		if(all.size() > 0){
			//将桩Id封装查询枪信息
			Set<Integer> set = new HashSet<>();
			for (Map map : all) {
				set.add(Integer.parseInt(map.get("pileId").toString()));
			}
			
			Map map = new HashMap();
			map.put("pileId", set);
			List<Map> allGun = codeManagementMapper.selectChgGunByPileId(map);
			
			//将枪信息封装入Map返回
			for (Map pileMap : all) {
				for (Map gunMap : allGun) {
					if(pileMap.get("pileId").equals(gunMap.get("pileId"))){
						if("充电枪01".equals(gunMap.get("gumPoint"))){
							pileMap.put("aGumPointQRCode",gunMap.get("qrCode").toString());
							pileMap.put("aGumPoint",gunMap.get("gumPoint").toString());
							pileMap.put("aGunId",gunMap.get("gunId").toString());
							pileMap.put("aInnerId",gunMap.get("innerId").toString());
							pileMap.put("aConnectorId",gunMap.get("connectorId").toString());
						}
						if("充电枪02".equals(gunMap.get("gumPoint"))){
							pileMap.put("bGumPointQRCode",gunMap.get("qrCode").toString());
							pileMap.put("bGumPoint",gunMap.get("gumPoint").toString());
							pileMap.put("bGunId",gunMap.get("gunId").toString());
							pileMap.put("bInnerId",gunMap.get("innerId").toString());
							pileMap.put("bConnectorId",gunMap.get("connectorId").toString());
						}
					}
				}
			}
		}
		resultVo.setData(all);
		return resultVo;
	}
	public Integer selectCount(DataVo dataVo){
		return codeManagementMapper.count(dataVo);
	}
	
	public List<Map> selectChgGunByPileId(DataVo dataVo){
		List<Map> chgGuns = codeManagementMapper.selectChgGunByPileId(dataVo);
		return chgGuns;
	}
	
	public ResultVo selectPubDictItemByKey(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		resultVo.setData(codeManagementMapper.selectPubDictItemByKey(dataVo));
		return resultVo;
	}
	
	public ResultVo QRCodeCount(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		resultVo.setTotal(codeManagementMapper.QRCodeCount(dataVo));
		return resultVo;
	}
	
	public int updateGumPointQRCode(Map dataVo){
		List<Map> list = (List<Map>) dataVo.get("updateList");
		int updateChgGun;
		if(dataVo.get("pileId") != null){
			updateChgGun = codeManagementMapper.updateGumPointQRCode(list);
		}else{
			int updateChgMeter = codeManagementMapper.updateChgMeter(list);
			List saveChgGunList = new ArrayList();
			List<Integer> pileIds  = new ArrayList<>();
			for (Map map : list) {
				//总表不插枪
				if(null != map.get("gumPoint")){
					saveChgGunList.add(map);
				}
				pileIds.add(Integer.parseInt(map.get("pileId").toString()));
			}
			updateChgGun = codeManagementMapper.updateGumPointQRCode(saveChgGunList);
			//新增时设置下发状态为未下发
			dataVo.put("qrCodeState", 0);
			dataVo.put("hlhtQRCodeState", 0);
			dataVo.put("pileIds", pileIds);
			codeManagementMapper.updatePileCodeState(dataVo);
			codeManagementMapper.updateHLHTCodeState(dataVo);
		}
		return updateChgGun;
	}
	
	public ResultVo updatePileCodeState(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		resultVo.setTotal(codeManagementMapper.updatePileCodeState(dataVo));
		return resultVo;
	}
	
	public ResultVo updateHLHTCodeState(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		resultVo.setTotal(codeManagementMapper.updateHLHTCodeState(dataVo));
		return resultVo;
	}
	
	public ResultVo insertQRCode(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		resultVo.setTotal(codeManagementMapper.insertQRCode(dataVo));
		return resultVo;
	}
	
	public ResultVo selectPileByPileNo(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		resultVo.setData(codeManagementMapper.selectPileByPileNo(dataVo));
		return resultVo;
	}
	
	public ResultVo selectChgmeterByPileId(DataVo dataVo){
		ResultVo resultVo = new ResultVo();
		resultVo.setData(codeManagementMapper.selectChgmeterByPileId(dataVo));
		return resultVo;
	}
}

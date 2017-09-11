package com.clouyun.charge.modules.member.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.modules.member.mapper.CAcctMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 描述: 集团service
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月15日 上午10:31:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CAcctService{

	public static final Logger logger = LoggerFactory.getLogger(CAcctService.class);

	@Autowired
	CAcctMapper acctMapper;
	
	public void insertCAcct(DataVo dataVo) throws BizException{
        checkData(dataVo);
		acctMapper.insertCAcct(dataVo);
	} 
	
	public void updateCAcct(DataVo vo) throws BizException{
		checkData(vo);
		
		if(vo.isBlank("acctId")){
			throw new BizException(1201000,"账户id");
		}
		//上次状态不为注销才记录流水
		DataVo dataVo = acctMapper.queryCAcctStatus(vo);

        if (dataVo != null && !"03".equals(dataVo.getString("acctStateCode"))){
			//注销操作
			if ("03".equals(vo.getString("acctStateCode"))){
				String arr = "集团";
				Integer id = 0;
				if (vo.isNotBlank("consId")){
					arr = "会员";
					id = vo.getInt("consId");
				}else{
					id = vo.getInt("groupId");
				}
				String acctAmount = "0";
				String acctAmount1 = vo.getString("acctAmount");
				if(vo.isNotBlank("acctAmount")){
					acctAmount = acctAmount1;
				}
				vo.put("seqFlag","1");//交易变动方向-->支出
				vo.put("preAmount",acctAmount);//变动前金额
				vo.put("curAmount",0);//变动后金额
				vo.put("chgAmount",acctAmount);//变更金额
				vo.put("chgType","04");//注销
				vo.put("seqDesc",arr + "id:"+id+",注销金额:"+acctAmount+",操作人Id:"+vo.getString("userId"));
				vo.put("acctAmount",0);
				acctMapper.insertCAcctSeq(vo);
			}
		}
		acctMapper.updateCAcct(vo);

		vo = null;
		dataVo = null;
	}
	
	public void batchInserCAcct(List<Map> list) throws BizException{
		
		if(list != null && list.size() > 0){
		    DataVo vo = null;
			for (Map map : list) {
			    vo = new DataVo(map);
				checkData(vo);
			}
		}
		acctMapper.batchInserCAcct(list);
	} 
	
	public void batchUpdateCAcct(List<Map> list) throws BizException{
		if(list != null && list.size() > 0){
		    DataVo vo = null;
			for (Map map : list) {
                vo = new DataVo(map);
                checkData(vo);
				if(vo.isBlank("acctId")){
					throw new BizException(1201000,"账户id");
				}
			}
		}
		acctMapper.batchUpdateCAcct(list);
	}
	/**
	 * 检查参数
	 */
	private void checkData(DataVo vo) throws BizException {
		if (vo.isBlank("acctAmount")){
			vo.put("acctAmount",0);
		}
		if (vo.isBlank("acctStateCode")){
			vo.put("acctStateCode","01");
		}
		if(vo.isBlank("acctAmount")){
			throw new BizException(1201000,"账户金额");
		}
		if(vo.isBlank("acctStateCode")){
			throw new BizException(1201000,"账户状态");
		}
		if(vo.isNotBlank("acctAmount") && !ValidateUtils.Number(vo.getString("acctAmount"))){
			throw  new BizException(1000014,"账户金额");
		}
	}
	
	
}

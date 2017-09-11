package com.clouyun.charge.common.utils;

import com.clouyun.boot.common.domain.DataVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 拼接互联互通导出connectorId
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年3月16日 下午3:36:36
 */
public class ConnectorIdUtil {
	
	/**
	 * 规则:企业编号9位+场站编号3位+桩编号3位+枪2位(取InnerId)
	 * @param map map封装的参数pileNo,stationNo,orgCode,orgNo,gumPoint,innerId
	 * @return
	 */
	public static Map<String,String> getConnector(List<Map> map){
			
			StringBuffer aConnId = new StringBuffer(128);
			StringBuffer bConnId = new StringBuffer(128);
			
			Map<String,String> returnMap = new HashMap<>();
			
			for (Map omap : map) {
				String connPileNo = "";
				String connStationNo = "";
				String connOrgCode = "";
				String connOrgNo = "";
				String aConnGumPoint = "";
				String aConnInnerId = "";
				String bConnGumPoint = "";
				String bConnInnerId = "";
				//根据导入的充电桩编号,获取到桩编号,场站编号,企业编号
				if(null != omap.get("pileNo") && "" != omap.get("pileNo")){
					connPileNo = omap.get("pileNo").toString();
				}
				
				if(null != omap.get("stationNo") && "" != omap.get("stationNo")){
					connStationNo = omap.get("stationNo").toString();
				}
				
				if(null != omap.get("orgCode") && "" != omap.get("orgCode")){
					connOrgCode = omap.get("orgCode").toString();//组织机构代码
				}
				
				if(null != omap.get("orgNo") && "" != omap.get("orgNo")){
					connOrgNo = omap.get("orgNo").toString();//企业编码
				}
				
				if(null != omap.get("aGumPoint") && "" != omap.get("aGumPoint")){
					aConnGumPoint = omap.get("aGumPoint").toString();//充电枪A或B
				}
				
				if(null != omap.get("aInnerId") && "" != omap.get("aInnerId")){
					try {
						Double inner = Double.parseDouble(omap.get("aInnerId").toString());//枪码
						aConnInnerId = inner.intValue() + "";//枪码
					} catch (Exception e) {
						aConnInnerId = omap.get("aInnerId").toString();
					}
				}
				if(null != omap.get("bGumPoint") && "" != omap.get("bGumPoint")){
					bConnGumPoint = omap.get("bGumPoint").toString();//充电枪A或B
				}
				
				if(null != omap.get("bInnerId") && "" != omap.get("bInnerId")){
					try {
						Double inner = Double.parseDouble(omap.get("bInnerId").toString());//枪码
						bConnInnerId = inner.intValue() + "";//枪码
					} catch (Exception e) {
						bConnInnerId = omap.get("bInnerId").toString();
					}
				}
				
				//企业编码
				String newOrgNo="";
				//企业编码不足9位，前面补零
				if(connOrgNo.length() < 9 && null != connOrgNo){
					StringBuffer sb= new StringBuffer("");
					int zeroSize = 9 - connOrgNo.length();
					for(int j = 0;j < zeroSize;j++){
						sb.append("0");
					}
					sb.append(connOrgNo);
					newOrgNo=sb.toString();
				}else{
					newOrgNo=connOrgNo;
				}
		
				//场站编码=场站编号-企业id
				String stationNo = "";
				String newStationNo = "";
				try {
					if("" != connStationNo && null != connStationNo && connOrgCode != null){
						stationNo = connStationNo.substring(connOrgNo.length());
					}
				} catch (Exception e1) {
					stationNo = "";
				}
				
				//场站编码不足3位，前面补零
				if(stationNo.length() < 3){
					StringBuffer sb = new StringBuffer("");
					int zeroSize = 3 - stationNo.length();
					for(int j = 0;j < zeroSize;j++){
						sb.append("0");
					}
					sb.append(stationNo);
					newStationNo=sb.toString();
				}else{
					newStationNo=stationNo;
				}
					
				//桩编码
				String pileNo="";
				
				try {
					pileNo = connPileNo.substring(connStationNo.length());
				} catch (Exception e) {
					pileNo="000";
				}
				String newpileNo="";
				//桩编码不足3位，前面补零
				if(pileNo.length() < 3){
					StringBuffer sb = new StringBuffer("");
					int zeroSize = 3 - pileNo.length();
					for(int j = 0;j < zeroSize;j++){
						sb.append("0");
					}
					sb.append(pileNo);
					newpileNo=sb.toString();
				}else{
					newpileNo=pileNo;
				}
		
				//枪
				if(("充电枪01").equals(aConnGumPoint)){
					String newGunA="";
					//场站编码不足2位，前面补零
					if(aConnInnerId.length() < 2){
						StringBuffer sb=new StringBuffer("");
						int zeroSize = 2 - aConnInnerId.length();
						for(int j = 0;j < zeroSize;j++){
							sb.append("0");
						}
						sb.append(aConnInnerId);
						newGunA=sb.toString();
					}
					returnMap.put("充电枪01",aConnId.append(newOrgNo).append(newStationNo).append(newpileNo).append(newGunA).toString());
				}
				if(("充电枪02").equals(bConnGumPoint)){
					//枪B的编码
					String newGunB="";
					//场站编码不足2位，前面补零
					if(bConnInnerId.length() < 2){
						StringBuffer sb = new StringBuffer("");
						int zeroSize=2 - bConnInnerId.length();
						for(int j = 0;j < zeroSize;j++){
							sb.append("0");
						}
						sb.append(bConnInnerId);
						newGunB=sb.toString();
					}
					returnMap.put("充电枪02",bConnId.append(newOrgNo).append(newStationNo).append(newpileNo).append(newGunB).toString());
				}
			}
		return returnMap;
	}


	/**
	 * 规则:企业编号9位+场站编号3位+桩编号3位+枪2位(取InnerId)
	 * @param map map封装的参数pileNo,stationNo,orgCode,orgNo,gumPoint,innerId
	 * @return
	 */
	public static Map<String,String> newGetConnector(List<Map> map){

		StringBuffer connId = new StringBuffer(128);

		Map<String,String> returnMap = new HashMap<>();

		for (Map omap : map) {
			DataVo vo = new DataVo(omap);
			String connPileNo = "";
			String connStationNo = "";
			String connOrgNo = "";
			String connGumPoint = "";
			String connInnerId = "";
			//根据导入的充电桩编号,获取到桩编号,场站编号,企业编号
			if(vo.isNotBlank("pileNo")){
				connPileNo = vo.getString("pileNo");
			}

			if(vo.isNotBlank("stationNo")){
				connStationNo = vo.getString("stationNo");
			}

			if(vo.isNotBlank("orgNo")){
				connOrgNo = vo.getString("orgNo");//企业编码
			}

			if(vo.isNotBlank("gumPoint")){
				connGumPoint = vo.getString("gumPoint");//充电枪名称
			}

			if(vo.isNotBlank("innerId")){
				connInnerId = vo.getString("innerId");
			}

			//企业编码
			String newOrgNo="";
			//企业编码不足9位，前面补零
			if(connOrgNo.length() < 9 && null != connOrgNo){
				StringBuffer sb= new StringBuffer("");
				int zeroSize = 9 - connOrgNo.length();
				for(int j = 0;j < zeroSize;j++){
					sb.append("0");
				}
				sb.append(connOrgNo);
				newOrgNo=sb.toString();
			}else{
				newOrgNo=connOrgNo;
			}

			//场站编码=场站编号-企业id
			String stationNo = "";
			String newStationNo = "";
			try {
				if("" != connStationNo && null != connStationNo && connOrgNo != null){
					stationNo = connStationNo.substring(connOrgNo.length());
				}
			} catch (Exception e1) {
				stationNo = "";
			}

			//场站编码不足3位，前面补零
			if(stationNo.length() < 3){
				StringBuffer sb = new StringBuffer("");
				int zeroSize = 3 - stationNo.length();
				for(int j = 0;j < zeroSize;j++){
					sb.append("0");
				}
				sb.append(stationNo);
				newStationNo=sb.toString();
			}else{
				newStationNo=stationNo;
			}

			//桩编码
			String pileNo="";

			try {
				pileNo = connPileNo.substring(connStationNo.length());
			} catch (Exception e) {
				pileNo="000";
			}
			String newpileNo="";
			//桩编码不足3位，前面补零
			if(pileNo.length() < 3){
				StringBuffer sb = new StringBuffer("");
				int zeroSize = 3 - pileNo.length();
				for(int j = 0;j < zeroSize;j++){
					sb.append("0");
				}
				sb.append(pileNo);
				newpileNo=sb.toString();
			}else{
				newpileNo=pileNo;
			}

			//枪
			String newGun="";
			//场站编码不足2位，前面补零
			if(connInnerId.length() < 2){
				StringBuffer sb=new StringBuffer("");
				int zeroSize = 2 - connInnerId.length();
				for(int j = 0;j < zeroSize;j++){
					sb.append("0");
				}
				sb.append(connInnerId);
				newGun=sb.toString();
			}else{
				newGun = connInnerId;
			}
			returnMap.put(connGumPoint,connId.append(newOrgNo).append(newStationNo).append(newpileNo).append(newGun).toString());
		}
		return returnMap;
	}

}

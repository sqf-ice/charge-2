package com.clouyun.charge.modules.monitor.service;

import com.clou.common.utils.ObjectUtils;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.cdzcache.imp.CDZStatusGet;
import com.clouyun.cdzcache.obj.CDQDlObj;
import com.clouyun.cdzcache.obj.CDZDlObj;
import com.clouyun.cdzcache.util.Utility;
import com.clouyun.charge.modules.charge.mapper.ChargeInMapper;
import com.clouyun.charge.modules.monitor.mapper.PileMonitorMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.vehicle.mapper.VehicleMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class PileMonitorQueryResultImp {
	
	@Autowired
	private VehicleMapper vehicleMapper;
	@Autowired
	private ChargeInMapper chargeInMapper;
	@Autowired
	private PileMonitorMapper pileMonitorMapper;
	@Autowired
    private DictService dictService;
	@Autowired
	private CDZStatusGet csg;
	
	
	private Logger log = Logger.getLogger(PileMonitorQueryResultImp.class);
	public static HashMap<Byte,String> pileStatusMap = new HashMap<Byte,String>();
	public static HashMap<Byte,String> gunStatusMap = new HashMap<Byte,String>();
	
	static{
		pileStatusMap.put((byte)0, "充电");
		pileStatusMap.put((byte)1, "空闲");
		pileStatusMap.put((byte)2, "异常");
		pileStatusMap.put((byte)3, "停用");
		pileStatusMap.put((byte)5, "放电");
		pileStatusMap.put((byte)32, "离线");
		
		gunStatusMap.put((byte)0, "充电");
		gunStatusMap.put((byte)1, "空闲");
		gunStatusMap.put((byte)2, "异常");
		gunStatusMap.put((byte)3, "停用");
		gunStatusMap.put((byte)5, "放电");
		gunStatusMap.put((byte)32, "未监控到");
	}
	/**
	 * 替换“充电”，加单击事件
	 * @param trmAddr
	 * @param portNo
	 * @param status
	 * @return
	 */
	private String getCDQStatus(String trmAddr,int portNo,byte status){
		String str = gunStatusMap.get(status);
		return str;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean checkStationStatus(Iterator it,Object statStatus,byte status){
		if(!ObjectUtils.isNull(statStatus)){
			String queryStatus = statStatus.toString();
			if("1".equals(queryStatus)&&(status!=1)){
				it.remove();
				return true;
			}
			if("2".equals(queryStatus)&&(status!=0)){
				it.remove();
				return true;
			}
			if("3".equals(queryStatus)&&(status!=2)){
				it.remove();
				return true;
			}
			if("4".equals(queryStatus)&&(status!=3)){
				it.remove();
				return true;
			}
			if("5".equals(queryStatus)&&(status!=32)){
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	public static final byte pileTotal=0,acPileTotal=1,dcPileTotal=2,chargingPile=3;
	public static final byte gunStatus=0,gunUse=1,dlStr=2,cdscStr=3,ydjkStr=4,wd=5,bms=6,cph=7,cpp=8,czbh=9;
	public static final byte cdl=0,hdl=1,dcdl=2,dhdl=3;
	public static final byte ratef=0,ratep=1,rateg=2;
	public static final String uiStr = "电压：<b style='color:#1E90FF;'>0</b>V，电流：<b style='color:#1E90FF;'>0</b>A";
	public static final String zeroStr = "<b style='color:#1E90FF;'>0</b>℃";
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void fillterData(Collection arg0, Map arg1) {
		CDZStatusGet ss = csg;
		File file = new File("config/" + "JedisConfig_dataCache.xml");
		log.info("=========> File JedisConfig_dataCache.xml Loaded In:" + file.getAbsolutePath());
		Iterator it = arg0.iterator();
		
		Double cdl=0D,hdl=0D,dcdl=0D,dhdl=0D;         //dlv[cdl,hdl,dcdl,dhdl]
		Double ratef=0D,ratep=0D,rateg=0D,fhz=0D;     //rv[ratef,ratep,rateg]
		
		Integer pileTotal = 0;    //pns[pileTotal,acPileTotal,dcPileTotal,chargingPile]
		Integer  acPileTotal = 0;
		Integer  dcPileTotal = 0;
		Integer  chargingPile = 0;
		while (it.hasNext()) {
			Map<Object, Object> map = (Map<Object, Object>)it.next();
			String trmAddr = (String) map.get("pileAddr");
			Integer trmId = (Integer)map.get("pileId");
			//Integer staId = Integer.valueOf(arg1.get("nodeId").toString());//(Integer)map.get(ChgPileField.STATION_ID);
			byte status = ss.getCDZStatus(trmAddr);
			if(checkStationStatus(it, arg1.get("stationStatus"), status))//旧平台不知道干啥的，先留着
				continue;
			if(!pileStatusMap.containsKey(status))
				status = (byte)1;
			map.put("status", pileStatusMap.get(status)==null? "空闲" :pileStatusMap.get(status));
			String pileType = (String) map.get("pileType");
			
			int gunNum = 1;
			Double lv = 0D;
			try {
				CDZDlObj zo = ss.getCDZLoad(trmAddr, null);
				if(zo==null){
					log.warn("根据终端地址没取到终端状态数据"+trmAddr);
					continue;
				}
				
				Integer[] pns = new Integer[]{0,0,0,0};
				String[] strs = new String[]{"","","","","","","","","",""};
				Double[] dlv = new Double[]{0D,0D,0D,0D};
				Double[] rv = new Double[]{0D,0D,0D};
				String begTime = ss.getCDZLjBegTime(trmAddr);
				if(begTime==null||"".equals(begTime.trim())){
					begTime = Utility.longTimeStr(System.currentTimeMillis());
				}
				lv = zo.getZfh();
				fhz+=lv;
				DecimalFormat df = new DecimalFormat("###00.00");
				DecimalFormat df1 = new DecimalFormat("##000.00");
				switch(pileType){
				case"1"://单交流充电桩
					organize1(ss, trmAddr, trmId, zo, df, df1,pns,strs, dlv,rv);
					break;
				case"2"://单直流充电桩
					organize2(ss, trmAddr,trmId, zo, df, df1,pns,strs, dlv,rv);
					break;
				case"3"://双交流充电桩
					gunNum = 2;
					organize3(ss, trmAddr,trmId, zo, df, df1,pns,strs, dlv,rv);
					break;
				case"4"://双直流充电桩
					gunNum = 2;
					Integer modelId = (Integer)map.get("pileModelId");
					organize4(ss, trmAddr,trmId, zo, df, df1,pns,strs, dlv,rv, modelId);
					break;
				case"5"://交直流充电桩
					gunNum = 2;
					organize5(ss, trmAddr,trmId, zo, df, df1,pns,strs, dlv,rv);
					break;
				}
				
				pileTotal += pns[PileMonitorQueryResultImp.pileTotal];
				acPileTotal += pns[PileMonitorQueryResultImp.acPileTotal];
				dcPileTotal += pns[PileMonitorQueryResultImp.dcPileTotal];
				chargingPile += pns[PileMonitorQueryResultImp.chargingPile];
				
				String gunStatus = strs[PileMonitorQueryResultImp.gunStatus];//枪充电状态          strs[gunStatus,gunUse,dlStr,cdscStr,ydjkStr]
				String gunUse = strs[PileMonitorQueryResultImp.gunUse];      //枪使用时间,使用率
				String dlStr = strs[PileMonitorQueryResultImp.dlStr];        //充电量
				String cdscStr = strs[PileMonitorQueryResultImp.cdscStr];    //充电时长
				String ydjkStr = strs[PileMonitorQueryResultImp.ydjkStr];    //用电监控
				String wd = strs[PileMonitorQueryResultImp.wd];          	 //温度
				String bmsStr = strs[PileMonitorQueryResultImp.bms];	     //BMS信息
				String cphStr = strs[PileMonitorQueryResultImp.cph];		 //车牌号
				String cppStr = strs[PileMonitorQueryResultImp.cpp];		 //车品牌
				String czbhStr = strs[PileMonitorQueryResultImp.czbh];	 	 //车自编号
				
				cdl += dlv[PileMonitorQueryResultImp.cdl];
				hdl += dlv[PileMonitorQueryResultImp.hdl];
				dcdl += dlv[PileMonitorQueryResultImp.dcdl];
				dhdl += dlv[PileMonitorQueryResultImp.dhdl];
				
				ratef += rv[PileMonitorQueryResultImp.ratef];
				ratep += rv[PileMonitorQueryResultImp.ratep];
				rateg += rv[PileMonitorQueryResultImp.rateg];
				
				
				map.put("gunTypeCode", gunNum);//桩枪数
				if(status!=(byte)32)
					map.put("load", "负荷："+lv);//负荷
				else{
					long lt = ss.getTrmLastLoginTime(trmId);
					map.put("load", lt>0?("掉线时间："+Utility.longTimeStr(lt)):"近期没上过线");
				}
				map.put("gunStatus", gunStatus);//枪充电状态 
				map.put("gunDl", dlStr);//累计电量
				map.put("wd", wd);//用电监控(温度)
				map.put("ydjkStr", ydjkStr);//用电监控(电压、电流)
				map.put("bmsStr", bmsStr);//BMS
				map.put("cphStr", cphStr);//车牌号
				map.put("cppStr", cppStr);//车品牌
				map.put("czbhStr", czbhStr);//车自编号
				map.put("ydjkStr", ydjkStr);//用电监控(电压、电流)
				map.put("cdscStr", cdscStr);//AB枪充电时长
				map.put("gunUse", gunUse);////AB枪累计时间
				map.put("pileTypeCope",map.get("pileType"));
				//map.put("pileType", DictReader.getItemNameByItemNum(7, map.get("pileType").toString()));//桩类型：例单交流充电桩
				map.put("begTime", begTime);//统计开始时间
			} catch (Exception e) {
				log.error("取桩状态数据报错,"+e.getMessage(), e);
				e.printStackTrace();
			}
		}
		
		DecimalFormat df1 = new DecimalFormat("##0.00");
//		arg1.put("ljcdl", df1.format(cdl));
//		arg1.put("ljhdl", df1.format(hdl));
//		arg1.put("dljcdl", df1.format(dcdl));
//		arg1.put("dljhdl", df1.format(dhdl));
//		arg1.put("fhz", df1.format(fhz));
		
//		arg1.put("ratef", df1.format(ratef));
//		arg1.put("ratep", df1.format(ratep));
//		arg1.put("rateg", df1.format(rateg));
		
//		arg1.put("gunTotal", pileTotal);
		arg1.put("acGunTotal", acPileTotal);
		arg1.put("dcGunTotal", dcPileTotal);
		
		arg1.put("chargingGun", chargingPile);
	}
	
	private void organize1(CDZStatusGet ss,String trmAddr,Integer trmId,CDZDlObj zo,DecimalFormat df,DecimalFormat df1,
			Integer[] pns,String[] strs,Double[] dlv,Double[] rv){
		pns[pileTotal] = pns[pileTotal]+1;
		pns[acPileTotal] = pns[acPileTotal]+1;
		byte s = ss.getCDQStatus(trmAddr, 1);
		if(s==0){
			pns[chargingPile] = pns[chargingPile]+1;
		}
		strs[gunStatus] = "A枪：" + getCDQStatus(trmAddr, 1, s);
		//非直流充电桩没有总表信息
		strs[ydjkStr] = uiStr;
		strs[wd] = zeroStr;
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 1);
			strs[gunStatus] = strs[gunStatus]+subTime(vs,trmAddr,1);
			strs[bms] += "A枪："+subTime2(vs)+"<br>";//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,1);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += "A枪：" + arr[0];
				strs[cpp] += "A枪：" + arr[1];
				strs[czbh] += "A枪：" + arr[2];
			}
		}
		CDQDlObj qo = zo.getCddl(1);
		if(qo!=null){
			strs[dlStr] = strs[dlStr]+("A枪当日："+dlStr(df, qo));
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				dlv[dhdl] += dv;
			}
		}
		qo = zo.getLjdl(1);
		if(qo!=null){
			strs[dlStr] += "A枪累计："+dlStr(df1, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[cdl] += dv;
				dlv[hdl] += dv;
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("A枪"+cdsjStr(zo,1));
		
		Double v = zo.getRateAllDl(1, 20, 30);
		if(v!=null)
			rv[ratef] +=v;
		v = zo.getRateAllDl(1, 36, 42);
		if(v!=null)
			rv[ratef] +=v;
		
		v = zo.getRateAllDl(1, 14, 20);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(1, 30, 36);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(1, 42, 46);
		if(v!=null)
			rv[ratep] +=v;
		
		v = zo.getRateAllDl(1, 0, 14);
		if(v!=null)
			rv[rateg] +=v;
		v = zo.getRateAllDl(1, 46, 48);
		if(v!=null)
			rv[rateg] +=v;
	}
	
	/**
	 * 获取会员中车辆信息
	 * @param trmId
	 * @param i
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String[] findCarInfo(Integer trmId,String trmAddr, int i) {
		String str = csg.getCDQConsCarInfo(trmAddr, i);
		String arr[] = new String[3];
		if(str==null){
			String[] ss = csg.getCDInfo(trmAddr, i);
			Integer consId = null;
			if(ss!=null && ss[2]!=null && ss[3].toLowerCase().startsWith("f")){
				Integer billPayId = Integer.valueOf(ss[3].toLowerCase().replace("f", ""));
				Map dataMap=new HashMap();
				dataMap.put("nodeId", billPayId);
				Map billPay=chargeInMapper.getBillPay(dataMap);
				consId = billPay!=null?Integer.parseInt(billPay.get("consId").toString()):null;
			}else if(ss!=null && ss[3]!=null && !"".equals(ss[2].trim())){
				String cardNo = ss[3].trim();
				Map card=pileMonitorMapper.consIdByCardId(cardNo);
				if(card!=null && card.get("consId")!=null){
					consId = Integer.parseInt(card.get("consId").toString());
				}
				
				if(ss[4]!=null){
					String vin = ss[4].trim();
					Map<String,Object> map =new HashMap<String,Object>();
					map.put("vin", vin);
					Map info=null;
					if(vehicleMapper.getVehicles(map)!=null&&!vehicleMapper.getVehicles(map).isEmpty()){
						info=vehicleMapper.getVehicles(map).get(0);
					}
					 
					if(info!=null){
						arr[0] = info.get("licensePlate")!=null?info.get("licensePlate").toString():"";
						arr[1] = info.get("brand")!=null?info.get("brand").toString():"";
						arr[2] = info.get("onNumber")!=null?info.get("onNumber").toString():"";
						csg.setCDQConsCarInfo(trmAddr, i, consId+";"+arr[0]+";"+arr[1]+";"+arr[2]);			
						
						return arr;
					}
				}
			}
			
			if(consId!=null){
				List<Map> vehicleList=pileMonitorMapper.vehiclesByConsId(consId);
				Map info = vehicleList!=null?vehicleList.get(0):null;
				if(null != info){
					arr[0] = info.get("licensePlate")!=null?info.get("licensePlate").toString():"";
					arr[1] = info.get("brand")!=null?info.get("brand").toString():"";
					arr[2] = info.get("onNumber")!=null?info.get("onNumber").toString():"";
					csg.setCDQConsCarInfo(trmAddr, i, consId+";"+arr[0]+";"+arr[1]+";"+arr[2]);
				}
			}
		}else{
			String[] ss = str.split(";");
			arr[0] = ss.length>=2?ss[1]:"";
			arr[1] = ss.length>=3?ss[2]:"";
			arr[2] = ss.length>=4?ss[3]:"";
		}
		return arr;
	}
	
	private void organize2(CDZStatusGet ss,String trmAddr,Integer trmId,CDZDlObj zo,DecimalFormat df,DecimalFormat df1,
			Integer[] pns,String[] strs,Double[] dlv,Double[] rv){
		pns[pileTotal] +=1;
		pns[dcPileTotal]+=1;
		byte s = ss.getCDQStatus(trmAddr, 3);
		if(s==0){
			pns[chargingPile]+=1;
		}
		strs[gunStatus] = "A枪："+getCDQStatus(trmAddr, 3, s) ;
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 3);
			Double[] zbvs = null;
			try {
				zbvs = ss.getZBValue(trmAddr);
			} catch (Exception e) {}//总表
			strs[gunStatus] += subTime(vs,trmAddr,3);
			strs[ydjkStr] += subTime1(zbvs);//用电监控
			Double tv = vs[17]!=null?Double.valueOf(vs[17]):0D;
			if(tv<=0)
				tv = vs[18]!=null?Double.valueOf(vs[18]):0D;
			strs[wd] = "A枪:"+dlStr(tv)+"℃";//电机温度
			strs[bms] += "A枪："+subTime2(vs)+"<br>";//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,3);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += "A枪："+arr[0];
				strs[cpp] += "A枪："+arr[1];
				strs[czbh] += "A枪："+arr[2];
			}
		}else{
			strs[ydjkStr] = uiStr;
			strs[wd] = zeroStr;
		}
		
		CDQDlObj qo = zo.getCddl(3);
		if(qo==null)
			qo = zo.getCddl(1);
		if(qo!=null){
			strs[dlStr] += "A枪当日："+dlStr(df, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				dlv[dhdl] += dv;
			}
		}
		qo = zo.getLjdl(3);
		if(qo==null)
			qo = zo.getLjdl(1);
		if(qo!=null){
			strs[dlStr] += "A枪累计："+dlStr(df1, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[cdl] += dv;
				dlv[hdl] += dv;
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("A枪"+cdsjStr(zo,3));
		
		Double v = zo.getRateAllDl(3, 20, 30);   //10-15
		if(v!=null)
			rv[ratef] +=v;
		v = zo.getRateAllDl(3, 36, 42);  //18-21
		if(v!=null)
			rv[ratef] +=v;
		
		v = zo.getRateAllDl(3, 14, 20);  //7-10
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(3, 30, 36); //15-18
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(3, 42, 46); //21-23
		if(v!=null)
			rv[ratep] +=v;
		
		v = zo.getRateAllDl(3, 0, 14); //0-7
		if(v!=null)
			rv[rateg] +=v;
		v = zo.getRateAllDl(3, 46, 48); //23-0
		if(v!=null)
			rv[rateg] +=v;
	}
	
	private void organize3(CDZStatusGet ss,String trmAddr,Integer trmId, CDZDlObj zo,DecimalFormat df,DecimalFormat df1,
			Integer[] pns,String[] strs,Double[] dlv,Double[] rv){
		pns[pileTotal] +=2;
		pns[acPileTotal] += 2;
		byte s = ss.getCDQStatus(trmAddr, 1);
		if(s==0){
			pns[chargingPile] +=1;
		}
		strs[gunStatus] = "A枪："+getCDQStatus(trmAddr, 1, s);
		//非直流充电桩没有总表信息
		strs[ydjkStr] = uiStr;
		strs[wd] = zeroStr;
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 1);
			strs[gunStatus] += subTime(vs,trmAddr,1);
			strs[bms] += "A枪："+subTime2(vs)+"<br>";//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,1);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += "A枪："+arr[0];
				strs[cpp] += "A枪："+arr[1];
				strs[czbh] += "A枪："+arr[2];
			}
		}
		s = ss.getCDQStatus(trmAddr, 0);
		if(s==0){
			pns[chargingPile]+=1;
		}
		strs[gunStatus] += "<br>B枪："+getCDQStatus(trmAddr, 0, s);
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 0);
			strs[gunStatus]+=subTime(vs,trmAddr,0);
			strs[bms] += "B枪："+subTime2(vs);//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,0);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += " B枪："+arr[0];
				strs[cpp] += " B枪："+arr[1];
				strs[czbh] += " B枪："+arr[2];
			}
		}
		
		CDQDlObj qo = zo.getCddl(1);
		if(qo!=null){
			strs[dlStr] += "A枪当日："+dlStr(df, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				dlv[dhdl] += dv;
			}
		}
		qo = zo.getLjdl(1);
		if(qo!=null){
			strs[dlStr] += "A枪累计："+dlStr(df1, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[cdl] += dv;
				dlv[hdl] += dv;
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("A枪"+cdsjStr(zo,1));
		
		qo = zo.getCddl(0);
		strs[dlStr] += "<br>";
		if(qo!=null){
			strs[dlStr] += "B枪当日："+dlStr(df, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				dlv[dhdl] += dv;
			}
		}
		qo = zo.getLjdl(0);
		if(qo!=null){
			strs[dlStr] += "B枪累计："+dlStr(df1, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[cdl] += dv;
				dlv[hdl] += dv;
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("<br>B枪"+cdsjStr(zo,0));
		
		Double v = zo.getRateAllDl(0, 20, 30);
		if(v!=null)
			rv[ratef] +=v;
		v = zo.getRateAllDl(0, 36, 42);
		if(v!=null)
			rv[ratef] +=v;
		
		v = zo.getRateAllDl(0, 14, 20);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(0, 30, 36);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(0, 42, 46);
		if(v!=null)
			rv[ratep] +=v;
		
		v = zo.getRateAllDl(0, 0, 14);
		if(v!=null)
			rv[rateg] +=v;
		v = zo.getRateAllDl(0, 46, 48);
		if(v!=null)
			rv[rateg] +=v;
		
		v = zo.getRateAllDl(1, 20, 30);
		if(v!=null)
			rv[ratef] +=v;
		v = zo.getRateAllDl(1, 36, 42);
		if(v!=null)
			rv[ratef] +=v;
		
		v = zo.getRateAllDl(1, 14, 20);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(1, 30, 36);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(1, 42, 46);
		if(v!=null)
			rv[ratep] +=v;
		
		v = zo.getRateAllDl(1, 0, 14);
		if(v!=null)
			rv[rateg] +=v;
		v = zo.getRateAllDl(1, 46, 48);
		if(v!=null)
			rv[rateg] +=v;
	}
	
	private void organize4(CDZStatusGet ss,String trmAddr,Integer trmId, CDZDlObj zo,DecimalFormat df,DecimalFormat df1,
			Integer[] pns,String[] strs,Double[] dlv,Double[] rv,
			Integer modelId) throws BizException{
		pns[pileTotal] +=2;
		pns[dcPileTotal]+=2;
		
		byte s = ss.getCDQStatus(trmAddr, 3);
		if(s==0){
			pns[chargingPile]+=1;
		}
		Double[] zbvs = null;
		if(ss.getCDZStatus(trmAddr)!=32){
			try {
				zbvs = ss.getZBValue(trmAddr);
			} catch (Exception e) {}//总表
		}
		strs[ydjkStr] += subTime1(zbvs);//用电监控
		
		strs[gunStatus] = "A枪："+getCDQStatus(trmAddr, 3, s);
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 3);
			strs[gunStatus]+=subTime(vs,trmAddr,3);
			Double tv = vs[17]!=null?Double.valueOf(vs[17]):0D;
			if(tv<=0)
				tv = vs[18]!=null?Double.valueOf(vs[18]):0D;
			strs[wd] = "A枪:"+dlStr(tv)+"℃";//电机温度
			strs[bms] += "A枪："+subTime2(vs)+"<br>";//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,3);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += "A枪："+arr[0];
				strs[cpp] += "A枪："+arr[1];
				strs[czbh] += "A枪："+arr[2];
			}
		}
		
		s = ss.getCDQStatus(trmAddr, 2);
		if(s==0){
			pns[chargingPile]+=1;
		}
		strs[gunStatus] += "<br>B枪："+getCDQStatus(trmAddr, 2, s);
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 2);
			strs[gunStatus]+=subTime(vs,trmAddr,2);
			Double tv = vs[17]!=null?Double.valueOf(vs[17]):0D;
			if(tv<=0)
				tv = vs[18]!=null?Double.valueOf(vs[18]):0D;
			if("".equals(strs[wd].trim())){
				strs[wd] += "B枪:"+dlStr(tv)+"℃";
			}else{
				strs[wd] += "； B枪:"+dlStr(tv)+"℃";
			}
			strs[bms] += "B枪："+subTime2(vs);//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,2);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += " B枪："+arr[0];
				strs[cpp] += " B枪："+arr[1];
				strs[czbh] += " B枪："+arr[2];
			}
		}
		
		
		Double rate = getZbRate(trmId, modelId,false);
		Double zdcdl=0D,zcdl=0D;
		boolean hasDay = false;
		CDQDlObj qo = zo.getCddl(1);
		if(qo!=null){
			hasDay = true;
		}
		boolean hasZb = false;
		qo = zo.getLjdl(1);
		if(qo!=null){
			if(qo.getBmEnd()>qo.getBmBeg())
				hasZb = true;
		}
		
		qo = zo.getCddl(3);
		if(qo!=null){
			strs[dlStr] += "A枪当日："+dlStr(df, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				zdcdl += dv;
			}
			dv = qo.getBmEnd()-qo.getBmBeg();
			if(!hasZb && dv>0){
				dlv[dhdl] += (qo.getBmEnd()-qo.getBmBeg());
			}
		}
		qo = zo.getLjdl(3);
		if(qo!=null){
			strs[dlStr] += "A枪累计："+dlStr(df1, qo);
			Double bv = qo.getBmEnd()-qo.getBmBeg();
			if(bv>0){
				dlv[cdl] += (bv);
				zcdl += (bv);
			}
			if(!hasZb && bv>0){
				dlv[hdl] += bv;
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("A枪"+cdsjStr(zo,3));
		
		qo = zo.getCddl(2);
		strs[dlStr] += "<br>";
		if(qo!=null){
			strs[dlStr] += "B枪当日："+dlStr(df, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				zdcdl += dv;
			}
			if(!hasZb && dv>0){
				dlv[dhdl] += dv;
			}
		}
		qo = zo.getLjdl(2);
		if(qo!=null){
			strs[dlStr] += "B枪累计："+dlStr(df1, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[cdl] += dv;
				zcdl += dv;
			}
			if(!hasZb && dv>0){
				dlv[hdl] += dv;
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("<br>B枪"+cdsjStr(zo,2));
		
		strs[dlStr] += "<br>";
		Double zdhdl=0D,zhdl=0D;
		qo = zo.getCddl(1);
		boolean uz = true;
		if(qo!=null){
			Double dvd = ((qo.getBmEnd()-qo.getBmBeg())*rate);
			if(zdcdl>dvd){
				rate = getZbRate(trmId, modelId,true);
				dvd = ((qo.getBmEnd()-qo.getBmBeg())*rate);
			}
			if(zdcdl>dvd){
				uz = false;
			}
			//log.error(trmAddr+"倍率:"+rate+",总表表码计算日累计:"+dvd+",枪表码日累计:"+zdcdl+",是否用总表码计算:"+uz);
		}
		qo = zo.getLjdl(1);
		if(qo!=null){
			Double dv = ((qo.getBmEnd()-qo.getBmBeg())*rate);
			if(zcdl>dv){
				uz = false;
			}
			//log.error(trmAddr+"倍率:"+rate+",总表表码计算总累计:"+dv+",枪表码总累计:"+zcdl+",是否用总表码计算:"+uz);
		}
		
		qo = zo.getCddl(1);
		if(qo!=null){
			strs[dlStr] += "总表当日:<b>"+df.format(uz?(Double.valueOf(qo.getDl())*rate):zdcdl)+"</b>kWh;   ";
			Double dv = ((qo.getBmEnd()-qo.getBmBeg())*rate);
			if(!uz || zdcdl>dv){
				dv = zdcdl;
			}
			if(dv>0){
				dlv[dhdl] += dv;
				zdhdl += dv;
			}
		}
		qo = zo.getLjdl(1);
		if(qo!=null){
			strs[dlStr] += "总表累计:<b>"+df1.format(uz?(Double.valueOf(qo.getDl())*rate):zcdl)+"</b>kWh;   ";
			Double dv = ((qo.getBmEnd()-qo.getBmBeg())*rate);
			if(!uz || zcdl>dv){
				dv = zcdl;
			}
			if(dv>0){
				dlv[hdl] += dv;
				zhdl += dv;
			}
		}
		
		
		DecimalFormat df2 = new DecimalFormat("##0");
		strs[dlStr] += "<br>";
		if(zdhdl>0D && zdcdl>0D){
			Double sv = (zdhdl-zdcdl)/zdhdl*100;
			if(sv>=50){
				strs[dlStr] += "当日损耗:<b style='color:red;'>"+(df2.format(sv))+"%</b>;";
			}else
				strs[dlStr] += "当日损耗:<b>"+(df2.format(sv))+"%</b>;";
		}/*else if(hasDay && zdhdl==0 && zhdl>0){   //这里就暂时不显示损耗部分
			strs[dlStr] += "当日损耗:<b>"+(df2.format(0))+"%</b>;";
		}*/
		
		String as="";
		if(hasDay)
			as = "&nbsp;&nbsp;&nbsp;&nbsp;";
		if(zhdl>0D && zcdl>0D){
			Double sv = (zhdl-zcdl)/zhdl*100;
			if((uz&&sv<2) || sv>=50)
				trmZbRates.remove(trmId);
			if(sv>=50){
				strs[dlStr] += (as+"总损耗:<b style='color:red;'>"+(df2.format(sv))+"%</b>");
			}else{
				strs[dlStr] += (as+"总损耗:<b>"+(df2.format(sv))+"%</b>");
			}
		}/*else if(hasDay && zhdl==0  && zhdl>0){    //这里就暂时不显示损耗部分
			strs[dlStr] += (as+"总损耗:<b>"+(df2.format(0))+"%</b>");
		}*/
		
		Double v = zo.getRateAllDl(2, 20, 30);
		if(v!=null)
			rv[ratef] +=v;
		v = zo.getRateAllDl(2, 36, 42);
		if(v!=null)
			rv[ratef] +=v;
		
		v = zo.getRateAllDl(2, 14, 20);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(2, 30, 36);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(2, 42, 46);
		if(v!=null)
			rv[ratep] +=v;
		
		v = zo.getRateAllDl(2, 0, 14);
		if(v!=null)
			rv[rateg] +=v;
		v = zo.getRateAllDl(2, 46, 48);
		if(v!=null)
			rv[rateg] +=v;
		
		v = zo.getRateAllDl(3, 20, 30);
		if(v!=null)
			rv[ratef] +=v;
		v = zo.getRateAllDl(3, 36, 42);
		if(v!=null)
			rv[ratef] +=v;
		
		v = zo.getRateAllDl(3, 14, 20);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(3, 30, 36);
		if(v!=null)
			rv[ratep] +=v;
		v = zo.getRateAllDl(3, 42, 46);
		if(v!=null)
			rv[ratep] +=v;
		
		v = zo.getRateAllDl(3, 0, 14);
		if(v!=null)
			rv[rateg] +=v;
		v = zo.getRateAllDl(3, 46, 48);
		if(v!=null)
			rv[rateg] +=v;
	}
	
	public static void main(String[] args){
		long lt = System.currentTimeMillis();
		lt = lt/(60*60*1000)*(60*60*1000);
		lt = lt -120*60*1000;
		while(lt<System.currentTimeMillis()){
			System.out.println(Utility.longTimeStr(lt)+"=="+(lt/(1000*60) % 60 ==0));
			lt+= 20*60*1000;
		}
		
		String str = "300/5";
		String[] ss = str.split("/");
		Double rv = 1D;
		if(ss!=null && ss.length==2)
			rv += rv*(Double.valueOf(ss[0])/Double.valueOf(ss[1]));
		System.out.println(rv);
		
	}
	
	private static Map<Integer, Double> trmZbRates = new HashMap<Integer, Double>();
	private long lastClear = System.currentTimeMillis();
	private Double getZbRate(Integer trmId,Integer modelId,boolean requery) throws BizException{
		Double rate = 30D;
		if(modelId!=null && modelId.intValue()==2) 
			rate = 60D;
		else if(modelId!=null && modelId.intValue()==5)
			rate = 80D;
		
		if(requery || System.currentTimeMillis()-lastClear>60*60*1000L){
			trmZbRates.clear();
			lastClear = System.currentTimeMillis();
		}
		
		Double rv = trmZbRates.get(trmId);
		if(rv==null){
			rv = 1D;
			Map chgMeterMap=pileMonitorMapper.chgMeterBypileId(trmId);
			if(chgMeterMap!=null&&!chgMeterMap.isEmpty()){
				if(chgMeterMap.get("ct")!=null || chgMeterMap.get("pt")!=null)
					rate = 1D;
				if(chgMeterMap.get("ct")!=null && !"00".equals(chgMeterMap.get("ct").toString())){
					String str="";
					List<ComboxVo> list=dictService.getDictByType("ct");
					for(ComboxVo comboxVo :list){
						if(chgMeterMap.get("ct").equals(comboxVo.getId())){
							str=comboxVo.getText();
							break;
						}
					}

					String[] ss = str.split("/");
					if(ss!=null && ss.length==2){
						rv = rv*(Double.valueOf(ss[0])/Double.valueOf(ss[1]));
					}
				}
				
				if(chgMeterMap.get("pt")!=null && !"00".equals(chgMeterMap.get("pt").toString())){
					String str="";
					List<ComboxVo> list=dictService.getDictByType("pt");
					for(ComboxVo comboxVo :list){
						if(chgMeterMap.get("pt").equals(comboxVo.getId())){
							str=comboxVo.getText();
							break;
						}
					}
					String[] ss = str.split("/");
					if(ss!=null && ss.length==2){
						rv = rv*(Double.valueOf(ss[0])/Double.valueOf(ss[1]));
					}
				}
			}
			trmZbRates.put(trmId, rv);
		}
			
	    if(rv>=1D){
			rate = rv;
		}
		
		return rate;
	}
	
	private void organize5(CDZStatusGet ss,String trmAddr,Integer trmId, CDZDlObj zo,DecimalFormat df,DecimalFormat df1,
			Integer[] pns,String[] strs,Double[] dlv,Double[] rv){
		pns[pileTotal] +=2;
		pns[dcPileTotal]+=1;
		pns[acPileTotal]+=1;//交直流算交流
		byte s = ss.getCDQStatus(trmAddr, 3);
		if(s==0){
			pns[chargingPile]+=1;
		}
		strs[gunStatus] = "A枪："+getCDQStatus(trmAddr, 3, s);
		//非直流充电桩没有总表信息 
		strs[ydjkStr] = uiStr;
		strs[wd] = zeroStr;
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 3);
			strs[gunStatus]+=subTime(vs,trmAddr,3);
			strs[bms] += "A枪："+subTime2(vs)+"<br>";//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,3);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += "A枪："+arr[0];
				strs[cpp] += "A枪："+arr[1];
				strs[czbh] += "A枪："+arr[2];
			}
		}
		s = ss.getCDQStatus(trmAddr, 1);
		if(s==0){
			pns[chargingPile]+=1;
		}
		strs[gunStatus] += "<br>B枪："+getCDQStatus(trmAddr, 1, s);
		if(s==(byte)0){
			String[] vs = ss.getCDQCurData(trmAddr, 1);
			strs[gunStatus]+=subTime(vs,trmAddr,1);
			strs[bms] += "B枪："+subTime2(vs);//BMS
			//正在充电
			String arr[] = findCarInfo(trmId,trmAddr,1);
			if(!ObjectUtils.isNull(arr[0])){
				strs[cph] += " B枪："+arr[0];
				strs[cpp] += " B枪："+arr[1];
				strs[czbh] += " B枪："+arr[2];
			}
		}
		
		CDQDlObj qo = zo.getCddl(3);
		if(qo!=null){
			strs[dlStr] += "A枪当日："+dlStr(df, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				dlv[dhdl] += dv;
			}
		}
		qo = zo.getLjdl(3);
		if(qo!=null){
			strs[dlStr] += "A枪累计："+dlStr(df1, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[cdl] += dv;
				dlv[hdl] += dv; 
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("A枪"+cdsjStr(zo,3));
		
		qo = zo.getCddl(1);
		strs[dlStr] += "<br>";
		if(qo!=null){
			strs[dlStr] += "B枪当日："+dlStr(df, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[dcdl] += dv;
				dlv[dhdl] += dv;
			}
		}
		qo = zo.getLjdl(1);
		if(qo!=null){
			strs[dlStr] += "B枪累计："+dlStr(df1, qo);
			Double dv = qo.getBmEnd()-qo.getBmBeg();
			if(dv>0){
				dlv[cdl] += dv;
				dlv[hdl] += dv;
			}
		}
		// 获取A枪当天充电时长和累计时长
		strs[cdscStr] += ("<br>B枪"+cdsjStr(zo,1));
	}
	
	/**
	 * 格式户时间返回带HTML字符串
	 * @param
	 * @return
	 */
	private static String cdsjStr(CDZDlObj zo,int port) {
		int[] vs = zo.getPortDayUseRate(port);
		String ts = "";
		if(vs[0]<90){
			ts = vs[0]+"分";
		}/*else if(vs[0]>=1440+720){
			int day = vs[0]/1440;
			int rmin = vs[0]%1440;
			ts = (day+"天"+(rmin/60)+"时"+(rmin%60)+"分");
		}*/else{
			ts = (vs[0]/60)+"时"+(vs[0]%60)+"分";
		}
		String str = "当日:<b style='color:#1E90FF;'>"+ ts+"</b>";
		vs = zo.getPortAllUseRate(port);
		ts = "";
		if(vs[0]<90){
			ts = vs[0]+"分";
		}/*else if(vs[0]>=1440+720){
			int day = vs[0]/1440;
			int rmin = vs[0]%1440;
			ts = (day+"天"+(rmin/60)+"时"+(rmin%60)+"分");
		}*/else{
			ts = (vs[0]/60)+"时"+(vs[0]%60)+"分";
		}
		str += " 累计:<b style='color:#1E90FF;'>"+ ts+ "</b>;使用率:<b style='color:#1E90FF;'>" + vs[2]+ "</b>%";
		
		return str;
	}
	
	public static String dlStr(DecimalFormat df,CDQDlObj qo){
		Double dv = qo.getBmEnd()-qo.getBmBeg();
		if(dv<0){
			dv = 0D;
		}
		return "<b style='color:#1E90FF;'>"+df.format(dv)+"</b>kWh;        ";
	}
	public static String dlStr(Object str){
		return "<b style='color:#1E90FF;'>"+str.toString()+"</b>";
	}
	
	public String subTime(String[] vs,String trmAddr,int portNo){
		if(vs==null || vs.length<15 || vs[0]==null)return null;
		String[] ss = vs[0].split("-| |:");
		if(ss.length==6){
			int cutSec = Integer.valueOf(vs[2]);
			DecimalFormat df = new DecimalFormat("#00");
			String soc = (vs[18]!=null&&!"".equals(vs[18]))?(",SOC="+df.format(Double.valueOf(vs[18])))+"%":"";
			Double ua = Double.valueOf(vs[8]!=null?vs[8]:"0");
			Double ub = Double.valueOf(vs[9]!=null?vs[9]:"0");
			Double uc = Double.valueOf(vs[10]!=null?vs[10]:"0");
			String ustr = (ua>0D?("<br>A相电压="+ua+","):"")+(ub>0D?("<br>B相电压="+ub+","):"")+(uc>0D?("<br>C相电压="+uc+","):"");
			if(soc.length()>1){
				ustr = ustr.replace("A相", "电机");
			}
			
			Double ia = Double.valueOf(vs[11]!=null?vs[11]:"0");
			Double ib = Double.valueOf(vs[12]!=null?vs[12]:"0");
			Double ic = Double.valueOf(vs[13]!=null?vs[13]:"0");
			String istr = (ua>0D?("<br>A相电流="+ia+","):"")+(ub>0D?("<br>B相电流="+ib+","):"")+(uc>0D?("<br>C相电流="+ic+","):"");
			if(soc.length()>1){
				istr = istr.replace("A相", "电机");
			}
			String timeStr = (cutSec/60)+"分";
			if(cutSec<10*60){
				timeStr += ((cutSec%60)+"秒");
			}
			if(cutSec>99*60){
				timeStr = (cutSec/3600)+"小时"+((cutSec/60)%60)+"分";
			}
			DecimalFormat df1 = new DecimalFormat("0.00");
			if(vs[14]==null)
				vs[14] = "0";
			
			String[] idss = csg.getCDQBillNo(trmAddr, portNo);
			String billNo = (idss!=null&&idss.length==2)?idss[0]:null;
			if(billNo==null)
				billNo = "No Card No";
			return "<a title='"+billNo+"' ><span style='color:#1E90FF;font-weight:bold;'>,"+df1.format(Double.valueOf(vs[14]))+"kWh,"+timeStr+soc+"</span></a>"+ustr+istr;
		}
		if(vs[0]!=null&&!"null".equals(vs[0]))
			return vs[0];
		return "";
	}
	
	/**
	 * 拼装用电监控数据
	 * @param vs
	 * @return
	 */
	public String subTime1(Double[] vs){
		if(vs==null || vs.length!=11)
			return uiStr;
		
		Double ua = vs[5] != null ? vs[5] : 0;
		Double ub = vs[6] != null ? vs[6] : 0;
		Double uc = vs[7] != null ? vs[7] : 0;
		String str = "电压：" + dlStr(ua > 0D ? ua : ub > 0D ? ub : uc > 0D ? uc : "0") + "V，";
		
		Double ia = vs[8] != null ? vs[8] : 0;
		Double ib = vs[9] != null ? vs[9] : 0;
		Double ic = vs[10] != null ? vs[10] : 0;
		str += "电流：" + dlStr(ia > 0D ? ia : ib > 0D ? ib : ic > 0D ? ic : "0") + "A";
		
		return str;
	}
	/**
	 * 拼装BMS信息
	 * @param vs
	 * @return
	 */
	public String subTime2(String[] vs) {
		if (vs == null || vs.length < 5|| vs[0]==null)
			return null;
		String[] ss = vs[0].split("-| |:");
		if (ss.length == 6) {
			DecimalFormat df = new DecimalFormat("#00");
			Double ua = Double.valueOf(vs[8] != null ? vs[8] : "0");
			if(vs[19]!=null)
				ua = Double.valueOf(vs[19]);
			Double ub = Double.valueOf(vs[9] != null ? vs[9] : "0");
			Double uc = Double.valueOf(vs[10] != null ? vs[10] : "0");
			String str = "电压：" + dlStr(ua > 0D ? ua : ub > 0D ? ub : uc > 0D ? uc : "0") + "V，";
			Double ia = Double.valueOf(vs[11] != null ? vs[11] : "0");
			if(vs[20]!=null)
				ia = Double.valueOf(vs[20]);
			Double ib = Double.valueOf(vs[12] != null ? vs[12] : "0");
			Double ic = Double.valueOf(vs[13] != null ? vs[13] : "0");
			str += "电流：" + dlStr(ia > 0D ? ia : ib > 0D ? ib : ic > 0D ? ic : "0") + "A，";
			str += "SOC：" + dlStr(ObjectUtils.isNull(vs[18]) ? "0" : df.format(Double.valueOf(vs[18]))) + "%";
			return str;
		}
		return "";
	}
}


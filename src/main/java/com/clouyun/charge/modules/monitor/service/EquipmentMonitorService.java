package com.clouyun.charge.modules.monitor.service;

import com.clou.common.utils.CalendarUtils;
import com.clou.common.utils.ObjectUtils;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.cdz.CdzOper;
import com.clouyun.cdzcache.imp.CDZStatusGet;
import com.clouyun.cdzcache.obj.CDQDlObj;
import com.clouyun.cdzcache.obj.CDZDlObj;
import com.clouyun.cdzcache.util.Utility;
import com.clouyun.charge.common.utils.PileTypeUtils;
import com.clouyun.charge.modules.monitor.mapper.EquipmentMonitorMapper;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 描述: 设备监控
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年8月8日 上午10:01:18
 */
@Service
public class EquipmentMonitorService {
	
	public static final Logger logger = LoggerFactory.getLogger(EquipmentMonitorService.class);

	@Autowired
	private EquipmentMonitorMapper equipmentMonitorMapper;
	@Autowired
	private CDZStatusGet cdzStatusGet;
	@Autowired
	private CdzOper cdzOper;

	private static HashMap<Byte,String> pileStatusMap = new HashMap<>();
	private static HashMap<Byte,String> gunStatusMap = new HashMap<>();

	static{
		pileStatusMap.put((byte)0, "充电");
		pileStatusMap.put((byte)1, "空闲");
		pileStatusMap.put((byte)2, "异常");
		pileStatusMap.put((byte)3, "停用");
		pileStatusMap.put((byte)5, "放电");
		pileStatusMap.put((byte)14, "等待连接");
		pileStatusMap.put((byte)15, "绝缘检测");
		pileStatusMap.put((byte)16, "充电结束");
		pileStatusMap.put((byte)17, "轮冲等待");
		pileStatusMap.put((byte)18, "本地正在操作充电");
		pileStatusMap.put((byte)19, "预约状态");
		pileStatusMap.put((byte)20, "等待充电结束");
		pileStatusMap.put((byte)32, "离线");

		gunStatusMap.put((byte)0, "充电");
		gunStatusMap.put((byte)1, "空闲");
		gunStatusMap.put((byte)2, "异常");
		gunStatusMap.put((byte)3, "停用");
		gunStatusMap.put((byte)5, "放电");
		gunStatusMap.put((byte)11, "插枪未充电");
		gunStatusMap.put((byte)14, "等待连接");
		gunStatusMap.put((byte)15, "绝缘检测");
		gunStatusMap.put((byte)16, "充电结束");
		gunStatusMap.put((byte)17, "轮冲等待");
		gunStatusMap.put((byte)18, "本地正在操作充电");
		gunStatusMap.put((byte)19, "预约状态");
		gunStatusMap.put((byte)20, "等待充电结束");
		gunStatusMap.put((byte)32, "未监控到");
	}

	public List<Map> stopChargeInfo(Integer pileId){
		//根据桩id获取桩信息
		List<DataVo> infoList = equipmentMonitorMapper.queryPileInfoById(pileId);

		List list = new ArrayList();
		if (infoList != null && infoList.size() > 0){
			DataVo dataVo = infoList.get(0);
			int ortMode = dataVo.getInt("ortMode");
			int numberGun = dataVo.getInt("numberGun");
			String pileAddr = dataVo.getString("pileAddr");
			Map<String, Integer> innerIdMap = PileTypeUtils.getNumberGun(numberGun, ortMode);

			for (String key : innerIdMap.keySet()) {
				Map resultMap = new LinkedHashMap();
				if ("Z".equals(key)){
					continue;//总表跳过
				}
				Integer innerId = innerIdMap.get(key);

				byte cdqStatus = cdzStatusGet.getCDQStatus(pileAddr, innerId);
				String gunStatus = getCDQStatus(cdqStatus);
				resultMap.put("status",gunStatus);
				resultMap.put("statusNum",cdqStatus);
				String gunName = "充电枪" + key;
				for (DataVo vo : infoList) {
					if(gunName.equals(vo.getString("gumPoint"))){
						resultMap.put("gumPoint",vo.getString("gumPoint"));
						resultMap.put("gunId",vo.getInt("gunId"));
					}
				}
				list.add(resultMap);
			}
		}



		return list;
	}


	public Map mapToStationInfo(Map map) throws BizException {
		DataVo params = new DataVo(map);

		if (params.isBlank("userId")){
			throw new BizException(1000006);
		}

		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(map);
		}

		Map result = queryResult(params);

//		PageInfo page = new PageInfo(result);
		return result;
	}

	private Map queryResult(DataVo params){
		String gunName = "枪";
		DecimalFormat df = new DecimalFormat("###0.00");
		//Map totalMap = new LinkedHashMap();
		//根据场站id获取到充电桩
		List<DataVo> pilesByStationId = equipmentMonitorMapper.getPilesByStationId(params);
		//场站用电量
		DataVo stationChgPower = getStationChgPower(params);
		//排除待结收入
		params.put("payState","1");
		//获取场站下的当月订单收入
		Map<Integer, Map<Integer, Map>> monthSumAmountMap = getMonthSumAmount(params);
		//获取场站下的当日订单上收入
		Map<Integer, Map<Integer, Map>> dailySumAmountMap = getDailySumAmount(params);
		//获取所有总表的综合倍率
		Map<Integer, Double> trmZbRates = queryZbRate();
		//获取表码差
		Map<Integer,Map<Integer,BigDecimal>> meterCodeMap = getMeterCode(params);

		Iterator<DataVo> it = pilesByStationId.iterator();

		Integer chargingGun = 0;//充电中枪数
        BigDecimal daliyCharge = BigDecimal.ZERO;//日充电量
//		BigDecimal dailyElec = BigDecimal.ZERO;//日用电量
        BigDecimal sumCharge = BigDecimal.ZERO;//总充电量
		BigDecimal sumElec = BigDecimal.ZERO;//总用电量
		Integer gunTotal = 0;///枪数
		Integer dcGunTotal = 0;//快枪
		Integer acGunTotal = 0;//慢枪

		List<Map> list = new ArrayList<>();
		Map<String,Map> pileAddrMap = new LinkedHashMap<>();
		Map<String,Byte> sortMap = new LinkedHashMap<>();

		while (it.hasNext()){
			Map chargeTimeMap = new LinkedHashMap();
			Map totalChargeMap = new LinkedHashMap();
			Map<String,Map> resultMap = new LinkedHashMap();
            Map gunMap = new LinkedHashMap();
            Map bmsMap = new LinkedHashMap();
            Map carMap = new LinkedHashMap();
            Map powerMonitor = new LinkedHashMap();
            Map consMap = new LinkedHashMap();
            Map voltageCurrent = new LinkedHashMap();
            BigDecimal pileCharge = BigDecimal.ZERO;//桩的充电量
            BigDecimal pileElec = BigDecimal.ZERO;//桩的用电量


			Map map2 = it.next();
			DataVo next = new DataVo(map2);
			String pileAddr = next.getString("pileAddr");
			Integer pileId = next.getInt("pileId");
			int ortMode = next.getInt("ortMode");
			int numberGun = next.getInt("numberGun");
			int modelId = next.getInt("pileModelId");

			Map<String, Integer> innerIdMap = PileTypeUtils.getNumberGun(numberGun, ortMode);
			Map<Integer, BigDecimal> meterInnerMap = meterCodeMap.get(pileId);
			//根据通讯地址获取桩的状态
			byte pileStatus = cdzStatusGet.getCDZStatus(pileAddr);
			//旧平台有一个检查场站状态的业务,但是旧业务没有具体使用到,暂时跳过

			if(!pileStatusMap.containsKey(pileStatus)){
				pileStatus = (byte)1;
			}
			next.put("status", pileStatusMap.get(pileStatus));//状态
			next.put("statusNumber",pileStatus);
			sortMap.put(pileAddr,pileStatus);//排序用

			Double lv = 0D;//桩负荷

			//总枪,快慢枪
			gunTotal += numberGun;
			if(ortMode == 1){
				//交流-->慢
				acGunTotal += numberGun;
			}else if (ortMode == 2){
				dcGunTotal += numberGun;
			}else if (ortMode == 3){
				if (numberGun % 2 != 0){
					int i = (numberGun - 1) / 2;
					acGunTotal += (i + 1);//慢枪多一个
					dcGunTotal += i;
				}else{
					//偶数
					int i = numberGun / 2;
					//交直一体的快慢各半
					acGunTotal += i;
					dcGunTotal += i;
				}
			}

			try {
				CDZDlObj cdzLoad = cdzStatusGet.getCDZLoad(pileAddr, null);
				if (cdzLoad == null){
					logger.warn("根据终端地址没取到终端状态数据:"+pileAddr);
					continue;
				}

				String begTime = cdzStatusGet.getCDZLjBegTime(pileAddr);
				if (begTime == null || "".equals(begTime.trim())){
					begTime = Utility.longTimeStr(System.currentTimeMillis());
				}

				next.put("begTime",begTime);//统计开始时间

				lv = cdzLoad.getZfh();
				next.put("load",lv);//负荷

				if(pileStatus == 32){
					long lt = cdzStatusGet.getTrmLastLoginTime(pileId);
					next.put("load", lt > 0 ? ("掉线时间:"+Utility.longTimeStr(lt)):"近期没上过线");
				}

				Map bmsInfoMap = new LinkedHashMap();
				Map cardInfoMap = new LinkedHashMap();
				Double zdcdl=0D,zcdl=0D;

//				boolean hasZb = false;
//				boolean hasDay = false;
//
//				//包含总表
//				if(innerIdMap.containsKey("Z")){
//					//总表的综合倍率
//					Integer zbInnerId = PileTypeUtils.getInnerId(numberGun, ortMode, "Z");
//					CDQDlObj qo = cdzLoad.getCddl(zbInnerId);
//					if(qo != null){
//						hasDay = true;
//					}
//
//					qo = cdzLoad.getLjdl(zbInnerId);
//					if(qo!=null){
//						if(qo.getBmEnd()>qo.getBmBeg()){
//							hasZb = true;
//						}
//					}
//				}

				//key-->innerId
				Map<Integer, Map> monthSumAmountInnerMap = monthSumAmountMap.get(pileId);
				Map<Integer, Map> dailySumAmountInnerMap = dailySumAmountMap.get(pileId);

				for (String key : innerIdMap.keySet()) {


					Double motorTemp = 0D;

					//总表信息额外处理
					if("Z".equals(key)){
						continue;
					}
					//累加枪的用电量
					BigDecimal gunMeterCode = BigDecimal.ZERO;
					if (meterInnerMap != null){
						BigDecimal bigDecimal = meterInnerMap.get(innerIdMap.get(key));
						//大于
						if (bigDecimal.compareTo(BigDecimal.ZERO) >= 0){
							gunMeterCode = bigDecimal;
						}else{
                            logger.error("通讯地址:"+pileAddr+",枪口编号"+innerIdMap.get(key)+",表码差:"+bigDecimal);
                        }
					}
					pileElec = pileElec.add(gunMeterCode);


					Map gunInfoMap = new LinkedHashMap();

					Integer innerId = innerIdMap.get(key);

					byte cdqStatus = cdzStatusGet.getCDQStatus(pileAddr, innerId);
					String gunStatus = getCDQStatus(cdqStatus);
					if (cdqStatus == 0){
						chargingGun += 1;//充电中枪数
						String[] cdqCurData = cdzStatusGet.getCDQCurData(pileAddr, innerId);
						gunInfoMap = subTime(cdqCurData,pileAddr,innerId,gunStatus);
						//直流模式才有温度监控
						if(ortMode == 2){
							motorTemp = cdqCurData[17]!=null?Double.valueOf(cdqCurData[17]):0D;
							if(motorTemp<=0){//电机温度
								motorTemp = cdqCurData[18]!=null?Double.valueOf(cdqCurData[18]):0D;
							}
						}

						bmsInfoMap = subTime2(cdqCurData,ortMode);//bms

						bmsMap.put(key + gunName,bmsInfoMap);
						cardInfoMap = findCarInfo(pileId, pileAddr, innerId);//车辆信息
						carMap.put(key + gunName,cardInfoMap);
					}
					if(gunInfoMap.size() <= 0){
						gunInfoMap.put("gunStatus",gunStatus);
					}
					gunMap.put(key + gunName,gunInfoMap);

					Map sumChargeMap = new LinkedHashMap();
					CDQDlObj qo = cdzLoad.getCddl(innerId);
					Map timeMap = new LinkedHashMap();
					Object dChgPower = "0";
					Object mChgPower = "0";
                    BigDecimal conversionRate = BigDecimal.ZERO;//使用率
					int timeDiff = 0;
					if(dailySumAmountInnerMap != null){
						Map dailyMap = dailySumAmountInnerMap.get(innerIdMap.get(key));
						if(dailyMap != null){
							dChgPower = dailyMap.get("chgPower");
							if (dChgPower != null){
                                daliyCharge = daliyCharge.add(new BigDecimal(dChgPower.toString()));
							}else{
								dChgPower = 0;
							}
							if(dailyMap.get("timeDiff") != null){
								timeDiff = Integer.parseInt(dailyMap.get("timeDiff").toString());
							}
						}
					}
					timeMap.put("daily", DateUtils.convertTime(timeDiff));

					timeDiff = 0;//时间差,秒数
					if (monthSumAmountInnerMap != null){
						Map monthMap = monthSumAmountInnerMap.get(innerIdMap.get(key));
						if(monthMap != null){
							mChgPower = monthMap.get("chgPower");
							if (mChgPower != null){
								sumCharge = sumCharge.add(new BigDecimal(mChgPower.toString()));
							}else{
								mChgPower = 0;
							}
							if(monthMap.get("timeDiff") != null){
								timeDiff = Integer.parseInt(monthMap.get("timeDiff").toString());
							}

							Calendar cal = Calendar.getInstance();
                            int day = cal.get(Calendar.DAY_OF_MONTH);    //获取当前天数
                            if (day != 0){
                                conversionRate = new BigDecimal(timeDiff).divide(new BigDecimal(day * 3600),2, BigDecimal.ROUND_HALF_UP);
                            }
                        }
					}
					timeMap.put("total",DateUtils.convertTime(timeDiff));

					timeMap.put("conversionRate",conversionRate + "%");
					sumChargeMap.put("daily",dChgPower);
					sumChargeMap.put("total",mChgPower);

					pileCharge = pileCharge.add(new BigDecimal(mChgPower.toString()));//桩的累计充电量

					//充电时长,key是枪号,map对应值
					chargeTimeMap.put(key + gunName,timeMap);
					//累计电量
					totalChargeMap.put(key + gunName,sumChargeMap);


					//电机温度
                    Map powerMonitorInfo = new LinkedHashMap();
					powerMonitorInfo.put("motorTemp",motorTemp);
					powerMonitor.put(key + gunName,powerMonitorInfo);

                    Map consInfoMap = new LinkedHashMap();
					// 获得充电枪当前充电的订单编号或卡号(后台启动的用f开头)和VIN编码(如果有),没充电返回null
					String[] cdqCardId = cdzStatusGet.getCDQBillNo(pileAddr, innerId);
					String consPhone = "";
					if(cdqCardId != null){
						String cardId = cdqCardId[0];

						String s = cardId.substring(0, 1).toLowerCase();
						String billPayId = cardId.substring(1);
						DataVo dataVo = null;
						if ("f".equals(s)){//f打头的是app充电的,查询bill_pay
							if (billPayId != null && !"".equals(billPayId)){
								dataVo = equipmentMonitorMapper.queryBillPayById(Integer.parseInt(billPayId));
							}
						}else{
							dataVo = equipmentMonitorMapper.queryConsByCardId(cardId);
						}
						if (dataVo != null && dataVo.size() > 0){
							consPhone = dataVo.getString("consPhone");
						}
						consInfoMap.put("consPhone",consPhone);
					}
					//充电才展示
					if (cdqStatus == 0){
						consMap.put(key + gunName,consInfoMap);
					}
				}

                /**
                 * 用电监控信息
                 */
                Double[] zbvs = null;
                voltageCurrent = subTime1(null);//给默认值
                if(ortMode == 2){//直流才有用电监控
                    if(pileStatus != 32){
                        try {
                            zbvs = cdzStatusGet.getZBValue(pileAddr);
                            voltageCurrent = subTime1(zbvs);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

				//包含总表
				if(innerIdMap.containsKey("Z")){
					//总表的综合倍率
					Double rate = getZbRate(pileId, modelId, trmZbRates);
					Integer zbInnerId = PileTypeUtils.getInnerId(numberGun, ortMode, "Z");
					BigDecimal zbMeterCode = BigDecimal.ZERO;
					if (meterInnerMap != null){
						BigDecimal bigDecimal = meterInnerMap.get(zbInnerId);
						//大于
						if (bigDecimal.compareTo(BigDecimal.ZERO) >= 0){
							zbMeterCode = bigDecimal;
						}else{
						    logger.error("通讯地址:"+pileAddr+",枪口编号"+zbInnerId+",表码差:"+bigDecimal);
                        }
					}
					pileElec = pileElec.add(zbMeterCode);
					Double zdhdl=0D,zhdl=0D;

					CDQDlObj qo = cdzLoad.getCddl(zbInnerId);
					boolean uz = true;
					if(qo!=null){
						Double dvd = (qo.getBmEnd() - qo.getBmBeg()) * rate;
						if(zdcdl > dvd){
							rate = getZbRate(pileId, modelId,trmZbRates);
							dvd = (qo.getBmEnd() - qo.getBmBeg()) * rate;
						}
						if(zdcdl > dvd){
							uz = false;
						}
						logger.error(pileAddr+"倍率:"+rate+",总表表码计算日累计:"+dvd+",枪表码日累计:"+zdcdl+",是否用总表码计算:"+uz);
					}

					qo = cdzLoad.getLjdl(zbInnerId);
					if(qo!=null){
						Double dv = (qo.getBmEnd() - qo.getBmBeg()) * rate;
						if(zcdl > dv){
							uz = false;
						}
						logger.error(pileAddr+"倍率:"+rate+",总表表码计算总累计:"+dv+",枪表码总累计:"+zcdl+",是否用总表码计算:"+uz);
					}

//					qo = cdzLoad.getCddl(zbInnerId);
//					String zDaily = "0";
//					if(qo!=null){
//						zDaily = df.format(uz ? (Double.valueOf(qo.getDl()) * rate) : zdcdl);
//						Double dv = (qo.getBmEnd() - qo.getBmBeg()) * rate;
//						if(!uz || zdcdl>dv){
//							dv = zdcdl;
//						}
//						if(dv>0){
//							//dailyElec += dv;
//							zdhdl += dv;
//						}
//					}

					qo = cdzLoad.getLjdl(zbInnerId);
					String zTotal = "0";
					if(qo!=null){
						zTotal = df.format(uz ? (Double.valueOf(qo.getDl()) * rate) : zcdl);
						Double dv = ((qo.getBmEnd()-qo.getBmBeg())*rate);
						if(!uz || zcdl>dv){
							dv = zcdl;
						}
						if(dv>0){
							//sumElec += dv;
							zhdl += dv;
						}
					}

//					String dailyLoss = "0";
					DecimalFormat df2 = new DecimalFormat("##0");
//					if(zdhdl > 0D && zdcdl > 0D){
//						Double sv = (zdhdl - zdcdl) / zdhdl * 100;
//						if(sv >= 50){
//							//大于50的时候需要标红
//							dailyLoss= df2.format(sv);
//						}else{
//							dailyLoss = df2.format(sv);
//						}
//					}

					String totalLoss = "0";
					if(zhdl>0D && zcdl>0D){
						Double sv = (zhdl - zcdl) / zhdl * 100;
						if((uz && sv < 2) || sv >= 50)
							trmZbRates.remove(pileId);
						if(sv>=50){
							//大于50的时候需要标红
							totalLoss = df2.format(sv);
						}else{
							totalLoss = df2.format(sv);
						}
					}

					Map map = new LinkedHashMap();
//					map.put("daily",zDaily);
					map.put("total",zbMeterCode);
//					lossMap.put("daily",dailyLoss);
					totalChargeMap.put("总表",map);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("取桩状态数据报错,"+e.getMessage(), e);
			}
			Map lossMap = new LinkedHashMap();
			BigDecimal loss = BigDecimal.ZERO;
			BigDecimal subtract = pileElec.subtract(pileCharge);
			if (subtract.compareTo(BigDecimal.ZERO) > 0){
				loss = subtract;
			}
			lossMap.put("total",loss);
			totalChargeMap.put("损耗",lossMap);

			//sumElec = sumElec.add(pileElec);//总用电量
			//list.add(resultMap);
			resultMap.put("title",next);
			resultMap.put("gunPointStatus",gunMap);
			resultMap.put("totalCharge",totalChargeMap);
			resultMap.put("chargeTimeMap",chargeTimeMap);
            resultMap.put("voltageCurrent",voltageCurrent);
            resultMap.put("powerMonitor",powerMonitor);
			resultMap.put("bmsMap",bmsMap);
			resultMap.put("carMap",carMap);
			resultMap.put("consMap",consMap);
			pileAddrMap.put(pileAddr,resultMap);
		}

		// 这里将map.entrySet()转换成list
		Set<Map.Entry<String,Byte>> entries = sortMap.entrySet();
		List<Map.Entry<String,Byte>> plMapList = new ArrayList<Map.Entry<String,Byte>>(
				entries);
		// 然后通过比较器来实现排序
		Collections.sort(plMapList, new Comparator<Map.Entry<String,Byte>>() {
			// 升序排序
			public int compare(Map.Entry<String,Byte> o1,
							   Map.Entry<String,Byte> o2) {
				if (o1.getValue() > o2.getValue()) {
					return 1;
				} else if ( o1.getValue() ==  o2.getValue()) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		List sortList = new ArrayList();
		for (int i = 0; i < plMapList.size(); i++) {
			Map.Entry<String, Byte> entry = plMapList.get(i);
			Map map = pileAddrMap.get(entry.getKey());
			if(map != null && map.size() > 0){
                sortList.add(map);
            }
		}

		Map totalMap = new LinkedHashMap();
		Map resultInfoMap = new LinkedHashMap();
		//排除待结收入
		params.put("payState","1");
		//场站总收入,日收入
		getSumAndDailyIncome(params, totalMap);

		if (stationChgPower != null && stationChgPower.size() > 0 && stationChgPower.isNotBlank("pileChg")){
			sumElec = new BigDecimal(stationChgPower.getString("pileChg"));
		}

		DecimalFormat df3 = new DecimalFormat("###0.00");
		totalMap.put("totalCharge",df3.format(sumCharge));//总充电量
		totalMap.put("totalElec",df3.format(sumElec));//总用电量
		totalMap.put("daliyCharge",df3.format(daliyCharge));//日充电量
//		totalMap.put("dailyElec",df3.format(dailyElec));//日用电量
		totalMap.put("gunTotal",gunTotal);//总枪数
		totalMap.put("dcGunTotal",dcGunTotal);//快枪d
		totalMap.put("acGunTotal",acGunTotal);//慢枪
		totalMap.put("chargingGun",chargingGun);//充电枪数


		if(pileAddrMap != null && pileAddrMap.size() > 0){
			List<DataVo> dataVos = equipmentMonitorMapper.queryPileWarmRecord(pileAddrMap.keySet().toArray());

			if(dataVos != null && dataVos.size() > 0){
				for (DataVo vo: dataVos) {
					String trmAddr = vo.getString("trmAddr");
					Map map = pileAddrMap.get(trmAddr);
					if(map != null && map.size() > 0){
						Map title = (Map)map.get("title");
						if(title != null && title.size() > 0){
							String pileStatus = title.get("status").toString();
							if("异常".equals(pileStatus)){
								String alrDesc = pileStatus + "    " + vo.getString("alrDesc");
								title.put("status",alrDesc);
							}
						}
					}
				}
			}
		}

		resultInfoMap.put("totalInfo",totalMap);
		resultInfoMap.put("result",sortList);

		return resultInfoMap;
	}

	private DataVo getStationChgPower(DataVo params) {
		getMonthCalendar(params);
		DataVo stationChgPower = equipmentMonitorMapper.queryStationChgPower(params);
		return stationChgPower;
	}

	private Map<Integer,Map<Integer, BigDecimal>> getMeterCode(DataVo params) {
		Map<Integer,Map<Integer, BigDecimal>> meterCodeMap = new HashMap<>();
		params.put("calType",4);//查询当月数据
		List<DataVo> meterCodeList = equipmentMonitorMapper.queryMeterCode(params);

		if (meterCodeList != null && meterCodeList.size() > 0){
			for (DataVo vo : meterCodeList) {
				BigDecimal cur = BigDecimal.ZERO;//结束表码
				BigDecimal pre = BigDecimal.ZERO;//开始表码
				if (vo.isNotBlank("zxygzCur")){
					cur = new BigDecimal(vo.getString("zxygzCur"));
				}
				if (vo.isNotBlank("zxygzPre")){
					pre = new BigDecimal(vo.getString("zxygzPre"));
				}

				BigDecimal meterCode = cur.subtract(pre);
				int pileId = vo.getInt("pileId");
				int innerId = vo.getInt("innerId");
				if (meterCodeMap.get(pileId) != null){
					Map<Integer, BigDecimal> innerMap = meterCodeMap.get(pileId);
					innerMap.put(innerId,meterCode);
				}else{
					Map innerMap = new HashMap();
					innerMap.put(innerId,meterCode);
					meterCodeMap.put(pileId,innerMap);
				}
			}
		}
		meterCodeList = null;
		return meterCodeMap;
	}

	/**
	 * 获取当日数据
	 * @param params
	 * @return
	 */
	private Map<Integer,Map<Integer,Map>> getDailySumAmount(DataVo params) {
		getDailyCalendar(params);
		List<DataVo> billPayGroupByPileDaily = equipmentMonitorMapper.queryBillPayGroupByPile(params);
		Map<Integer,Map<Integer,Map>> billPayGroupByPileMapDaily = new HashMap<>();
		putSumAmountVal(billPayGroupByPileDaily, billPayGroupByPileMapDaily);
		billPayGroupByPileDaily = null;
		return billPayGroupByPileMapDaily;
	}

	/**
	 * put值
	 * @param billPayGroupByPileDaily
	 * @param billPayGroupByPileMapDaily
	 */
	private void putSumAmountVal(List<DataVo> billPayGroupByPileDaily, Map<Integer, Map<Integer, Map>> billPayGroupByPileMapDaily) {
		if (billPayGroupByPileDaily != null && billPayGroupByPileDaily.size() > 0){
			for (DataVo vo : billPayGroupByPileDaily) {
				int innerId = vo.getInt("innerId");
				int pileId = vo.getInt("pileId");

				if (billPayGroupByPileMapDaily.get(pileId) != null){
					Map<Integer, Map> map = billPayGroupByPileMapDaily.get(pileId);
					map.put(innerId,vo);
				}else{
					Map<Integer,Map> bpMap = new HashMap<>();
					bpMap.put(innerId,vo);
					billPayGroupByPileMapDaily.put(pileId,bpMap);
				}
			}
		}
	}

	/**
	 * 获取当月收入
	 * @param params
	 * @return
	 */
	private Map<Integer,Map<Integer,Map>> getMonthSumAmount(DataVo params) {
		getMonthCalendar(params);
		List<DataVo> billPayGroupByPileMonth = equipmentMonitorMapper.queryBillPayGroupByPile(params);
		Map<Integer,Map<Integer,Map>> billPayGroupByPileMapMonth = new HashMap<>();
		putSumAmountVal(billPayGroupByPileMonth, billPayGroupByPileMapMonth);
		billPayGroupByPileMonth = null;
		return billPayGroupByPileMapMonth;
	}

	/**
	 * 充电时长计算转换
	 * @return   now  当日
	 * @return   sum  累计
	 * @return   conversionRate  使用率
	 */
	private Map cdsjStr(CDZDlObj zo,int port) {
		Map map = new LinkedHashMap();
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
//		String str = "当日:<b style='color:#1E90FF;'>"+ ts+"</b>";
		map.put("daily",ts);
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
//		str += " 累计:<b style='color:#1E90FF;'>"+ ts+ "</b>;使用率:<b style='color:#1E90FF;'>" + vs[2]+ "</b>%";
		map.put("total",ts);
		map.put("conversionRate",vs[2] + "%");
		return map;
	}


	private String dlStr(DecimalFormat df,CDQDlObj qo){
		Double dv = qo.getBmEnd() - qo.getBmBeg();
		if(dv<0){
			dv = 0D;
		}
		return df.format(dv);
	}

	private Double getZbRate(Integer trmId,Integer modelId,Map<Integer,Double> trmZbRates){
		Double rate = 30D;
		if(modelId!=null && modelId.intValue()==2)
			rate = 60D;
		else if(modelId!=null && modelId.intValue()==5)
			rate = 80D;

		Double rv = trmZbRates.get(trmId);
		if(rv!=null && rv>=1D){
			rate = rv;
		}

		return rate;
	}

	//查询所有总表的倍率
	@SuppressWarnings("rawtypes")
	private Map<Integer, Double> queryZbRate(){
		Map<Integer, Double> trmZbRates = new LinkedHashMap<>();

		Double rate = 1D;
		Map map = new LinkedHashMap();
		map.put("isZb",1);
		List<DataVo> meterInfo = equipmentMonitorMapper.queryMeterInfo(map);

		trmZbRates.clear();
		for(DataVo o : meterInfo){

			if(o.isNotBlank("meterRatio")){
				rate = o.getDouble("meterRatio");
			}
			trmZbRates.put(o.getInt("pileId"), rate);
		}
		return trmZbRates;
	}

	/**
	 * 获取会员中车辆信息
	 * @param trmId
	 * @param innerId
	 * @return
	 */
	private Map findCarInfo(Integer trmId,String trmAddr, int innerId) {
		Map resultMap = new LinkedHashMap();
//		String str = cdzStatusGet.getCDQConsCarInfo(trmAddr, innerId);
		String onNumber = "";
		String brand = "";
		String licensePlate = "";
//		if(str==null){
			String[] ss = cdzStatusGet.getCDInfo(trmAddr, innerId);
			//log.error("查询车辆的String:"+Arrays.toString(ss));
			Integer consId = null;
			if(ss!=null && ss[2]!=null && ss[3].toLowerCase().startsWith("f")){
				Integer billPayId = Integer.valueOf(ss[3].toLowerCase().replace("f", ""));
				DataVo billPayMap = equipmentMonitorMapper.queryBillPayById(billPayId);

				consId = billPayMap != null ? billPayMap.getInt("consId") : null;

			}else if(ss!=null && ss[3]!=null && !"".equals(ss[2].trim())){
				if(ss[4]!=null){
					String vin = ss[4].trim();
					if(vin != null && !"".equals(vin)){
						DataVo vinMap = equipmentMonitorMapper.queryVehicleByVin(vin);
						if(vinMap!=null){
							DataVo consIdMap = equipmentMonitorMapper.queryCConsByCarId(vinMap.getInt("vehicleId"));
							if(consIdMap != null)
								consId = consIdMap.getInt("consId");
						}
					}
				}

				if(consId==null){
					String cardNo = ss[3].trim();
					if (cardNo != null && !"".equals(cardNo)){
						DataVo cardMap = equipmentMonitorMapper.queryCCardById(cardNo);

						if(cardMap != null && cardMap.get("consId") != null){
							consId = cardMap.getInt("consId");
						}
					}
				}
			}
			if(consId != null){
				DataVo vehicleMap = equipmentMonitorMapper.queryVehicleByConsId(consId);
				if(vehicleMap != null && vehicleMap.size() > 0){
					licensePlate = vehicleMap.getString("licensePlate");
					brand = vehicleMap.getString("brandName");
					onNumber = vehicleMap.getString("onNumber");
					cdzStatusGet.setCDQConsCarInfo(trmAddr, innerId, consId+";"+licensePlate+";"+brand+";"+onNumber);
				}
			}
//		}else{
//			String[] ss = str.split(";");
//			licensePlate = ss.length>=2?ss[1]:"";
//			brand = ss.length>=3?ss[2]:"";
//			onNumber = ss.length>=4?ss[3]:"";
//		}

		resultMap.put("licensePlate",licensePlate);
		resultMap.put("brand",brand);
		resultMap.put("onNumber",onNumber);

		return resultMap;
	}



	/**
	 * 拼装BMS信息
	 * @param vs
	 * @return
	 */
	public Map subTime2(String[] vs,Integer ortMode) {
		Map resultMap = new LinkedHashMap();
		Double bmsVm = 0D;//电压
		Double bmsEc = 0D;//电流
		String bmsSoc = "0%";//soc
		if (vs == null || vs.length < 5|| vs[0]==null)
			return resultMap;
		String[] ss = vs[0].split("-| |:");
		if (ss.length == 6) {
			DecimalFormat df = new DecimalFormat("#00");
			Double ua = Double.valueOf(vs[8] != null ? vs[8] : "0");
			if(vs[19]!=null)
				ua = Double.valueOf(vs[19]);
			Double ub = Double.valueOf(vs[9] != null ? vs[9] : "0");
			Double uc = Double.valueOf(vs[10] != null ? vs[10] : "0");
			bmsVm = ua > 0D ? ua : ub > 0D ? ub : uc > 0D ? uc : 0;
			Double ia = Double.valueOf(vs[11] != null ? vs[11] : "0");
			if(vs[20]!=null)
				ia = Double.valueOf(vs[20]);
			Double ib = Double.valueOf(vs[12] != null ? vs[12] : "0");
			Double ic = Double.valueOf(vs[13] != null ? vs[13] : "0");
			bmsEc = ia > 0D ? ia : ib > 0D ? ib : ic > 0D ? ic : 0;
			bmsSoc = (ObjectUtils.isNull(vs[18]) ? "0" : df.format(Double.valueOf(vs[18]))) + "%";
			resultMap.put("bmsUV",bmsVm + "V");
			resultMap.put("bmsIA",bmsEc + "A");
			resultMap.put("bmsSoc",bmsSoc);
			//交流没有soc,给前端标识
			if (ortMode != 1){
				resultMap.put("isSoc",1);
			}else{
				resultMap.put("isSoc",0);
			}
		}
		return resultMap;
	}

	public Map subTime(String[] vs,String trmAddr,int portNo,String gunStatus){
		Map map = new LinkedHashMap();

		if(vs==null || vs.length<15 || vs[0]==null){
			map.put("gunStatus",gunStatus);
			return map;
		}
		String[] ss = vs[0].split("-| |:");
		if(ss.length==6){
			int cutSec = Integer.valueOf(vs[2]);
			DecimalFormat df = new DecimalFormat("#00");
			String soc = (vs[18]!=null&&!"".equals(vs[18]))?(",SOC="+df.format(Double.valueOf(vs[18])))+"%":"";
			Double ua = Double.valueOf(vs[8]!=null?vs[8]:"0");
			Double ub = Double.valueOf(vs[9]!=null?vs[9]:"0");
			Double uc = Double.valueOf(vs[10]!=null?vs[10]:"0");
			String ustr = (ua>0D?("A相电压="+ua+"V,"):"")+(ub>0D?("B相电压="+ub+"V,"):"")+(uc>0D?("C相电压="+uc+"V,"):"");
			if(soc.length()>1){
				ustr = ustr.replace("A相", "电机");
			}

			Double ia = Double.valueOf(vs[11]!=null?vs[11]:"0");
			Double ib = Double.valueOf(vs[12]!=null?vs[12]:"0");
			Double ic = Double.valueOf(vs[13]!=null?vs[13]:"0");
			String istr = (ua>0D?("A相电流="+ia+"A,"):"")+(ub>0D?("B相电流="+ib+"A,"):"")+(uc>0D?("C相电流="+ic+"A,"):"");
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

			String[] idss = cdzStatusGet.getCDQBillNo(trmAddr, portNo);
			String billNo = (idss!=null&&idss.length==2)?idss[0]:null;
			if(billNo==null){
				billNo = "No Card No";
			}
//			return billNo+","+df1.format(Double.valueOf(vs[14]))+"kWh,"+timeStr+soc+ustr+istr;
//			gunStatus += (billNo+","+df1.format(Double.valueOf(vs[14]))+"kWh,"+timeStr+soc);
			gunStatus += (df1.format(Double.valueOf(vs[14]))+"kWh,"+timeStr+soc);
			map.put("UV",ustr);
			map.put("IA",istr);
		}

		map.put("gunStatus",gunStatus);
//		if(vs[0]!=null&&!"null".equals(vs[0]))
//			return vs[0];
		return map;
	}

	/**
	 * 拼装用电监控数据
	 * @param vs
	 * @return
	 */
	public Map subTime1(Double[] vs){
		Map resultMap = new LinkedHashMap();
		Double pmc = 0D;//用电监控电流
		Double pmv = 0D;//用电监控电压
		if(vs==null || vs.length != 11) {
			resultMap.put("IA",pmc + "A");
			resultMap.put("UV",pmv + "V");
			return resultMap;
		}

		Double ua = vs[5] != null ? vs[5] : 0;
		Double ub = vs[6] != null ? vs[6] : 0;
		Double uc = vs[7] != null ? vs[7] : 0;
		pmv = ua > 0D ? ua : ub > 0D ? ub : uc > 0D ? uc : 0;//电压

		Double ia = vs[8] != null ? vs[8] : 0;
		Double ib = vs[9] != null ? vs[9] : 0;
		Double ic = vs[10] != null ? vs[10] : 0;
		pmc = ia > 0D ? ia : ib > 0D ? ib : ic > 0D ? ic : 0;//电流

		resultMap.put("IA",pmc + "A");
		resultMap.put("UV",pmv + "V");
		return resultMap;
	}

	/**
	 * 替换“充电”，加单击事件
	 * @param status
	 * @return
	 */
	private String getCDQStatus(byte status){
		String str = gunStatusMap.get(status);
		return str;
	}

	private void getSumAndDailyIncome(DataVo params,Map totalMap) {
		BigDecimal sumIncome = BigDecimal.ZERO;
		BigDecimal dailyIncome = BigDecimal.ZERO;

		//月数据
		getMonthCalendar(params);
//		Calendar cal;
//		String startTime;
//		String endTime;
		BigDecimal sumIncomeArr = equipmentMonitorMapper.queryStationIncome(params);
		//当日数据
		getDailyCalendar(params);
		BigDecimal dailyIncomeArr = equipmentMonitorMapper.queryStationIncome(params);

		if (sumIncomeArr != null){
			sumIncome = sumIncomeArr;
		}
		if (dailyIncomeArr != null){
			dailyIncome = dailyIncomeArr;
		}

		totalMap.put("totalIncome",sumIncome);
		totalMap.put("dailyIncome",dailyIncome);
	}

	private void getDailyCalendar(DataVo params) {
		Calendar cal;
		String startTime;
		String endTime;
		cal = Calendar.getInstance();
		startTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMdd);
		cal.add(Calendar.DAY_OF_MONTH,1);
		endTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMdd);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
	}

	private void getMonthCalendar(DataVo params) {
		Calendar cal = Calendar.getInstance();
		params.put("month", CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMM));
		cal.set(Calendar.DAY_OF_MONTH,1);
		String startTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMdd);
		cal.add(Calendar.MONTH,1);
		String endTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMdd);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
	}

	/**
	 * CdzOper 服务的 String stopCdzPort(int trmId,String trmAddr,int portNo) 方法
	 * @param gunId  需要调用停电服务的gunId
	 */
	public String stopCharge(Integer gunId){
		Map map = new HashMap();
		map.put("gunId",gunId);
		List<DataVo> list = equipmentMonitorMapper.queryChgGunInfoById(map);
		if (list != null && list.size() > 0){
			DataVo dataVo = list.get(0);
			int innerId = dataVo.getInt("innerId");
			int pileId = dataVo.getInt("pileId");
			String pileAddr = dataVo.getString("pileAddr");

			String message = cdzOper.stopCdzPort(pileId, pileAddr, innerId);
			list = null;//置空
			return message;
		}
		//只有根据gunId没有查出信息的时候才会返回这个信息
		return "停止充电异常,请联系管理员";
	}

	/**
	 * 批量停止充电服务,目前接口属于循环调用
	 * @param gunIds 批量停止的gunId
	 * @return
	 */
	public List<Map> batchStopCharge(List gunIds){
		Map map = new HashMap();
		map.put("list",gunIds);
		List<DataVo> list = equipmentMonitorMapper.queryChgGunInfoById(map);

		List<Map> resultList = new ArrayList<>();
		if (list != null && list.size() > 0){
			for (DataVo dataVo : list) {
				map = new HashMap();
				int innerId = dataVo.getInt("innerId");
				int pileId = dataVo.getInt("pileId");
				String pileAddr = dataVo.getString("pileAddr");
				//停止充电
				String message = cdzOper.stopCdzPort(pileId, pileAddr, innerId);
				map.put("gumPoint",dataVo.getString("gumPoint"));
				map.put("message",message);
				resultList.add(map);
			}
		}
		list = null;//置空
		return resultList;
	}

}

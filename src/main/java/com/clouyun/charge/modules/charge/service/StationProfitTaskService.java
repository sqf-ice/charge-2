package com.clouyun.charge.modules.charge.service;

import com.clou.common.utils.CalendarUtils;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.modules.charge.mapper.StationProfitTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


/**
 * 描述: 场站利润
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年5月22日 下午5:15:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StationProfitTaskService extends BusinessService{
	
	@Autowired
	StationProfitTaskMapper profitMapper;
	
	private final Integer INIT_ZERO = 0;
	private final Integer BATCH_MAX_SIZE = 100;

	@SuppressWarnings("unused")
	public void run(String month) {
		//返回结果集
		try {
			if (null == month || "".equals(month)){
				throw new BizException(2000001);
			}

			saveLog("场站利润定时任务:"+month, OperateType.add.OperateId,"补跑场站利润定时任务开始:"+CalendarUtils.formatCalendar(Calendar.getInstance(),CalendarUtils.yyyyMMddHHmmss),null);

			System.out.println("--------------------------");
	        System.out.println("date=" + CalendarUtils.formatCalendar(Calendar.getInstance(), CalendarUtils.yyyyMMddHHmmss));
			System.out.println("--------------------------");
			List<Map> resultList = new ArrayList();
			//key-->stationId value-->结果集
			Map<Integer,Map> resultMap = new HashMap();
			Map queryMap = new HashMap();
			//每月5号之前跑上月数据
			Calendar cal = CalendarUtils.convertStrToCalendar(month,CalendarUtils.yyyyMM);

			if(cal == null){
				throw new BizException(2000002);
			}
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);

			String startTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);

			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 0);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			String endTime = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMMddHHmmss);
			//结算月份
			String settlementMonth = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMM);

			if(profitMapper.queryStationProfitCount(settlementMonth) > 0){
				profitMapper.deleteStationProfit(settlementMonth);
			}

			queryMap.put("startTime", startTime);
			queryMap.put("endTime", endTime);

			//订单
			List<Map> billPayList = profitMapper.queryBillPay(queryMap);

			//采集数据
			//List<Map> pileCollDataList = profitMapper.queryPileCollData(queryMap);

			//场站设备总用电量
			List<Map> stationPileChg = profitMapper.queryStationPileChg(queryMap);
			//集团场站月结
			List<Map> groupMonthlyCache = profitMapper.queryGroupMonthlyCache(queryMap);

			//上上个月场站利润
			cal.add(Calendar.MONTH, -1);
			String lastLastMonth = CalendarUtils.formatCalendar(cal, CalendarUtils.yyyyMM);
			List<Map> lastMonthProfit = profitMapper.queryLastMonthProfit(lastLastMonth);


			List<Map> allStation = profitMapper.queryStation();
			List<Map> allPile = profitMapper.queryPile();
			//合约有效期大于1号的
			List<Map> allContract = profitMapper.queryContract(queryMap);

			//设备收入合约
			queryMap.put("contractType", 0);
			queryMap.put("contractIncomeType", 0);

			List<Map> inPileTotalIncomeList = profitMapper.queryDailyIncome(queryMap);

			//设备运维合约
			queryMap.put("contractIncomeType", 1);
			List<Map> inPilePmIncomeList = profitMapper.queryDailyIncome(queryMap);

			queryMap.remove("contractIncomeType");
			queryMap.put("contractType", 2);
			queryMap.put("contractCostType", 0);
			//分成合约支出
			List<Map> diviExpendList = profitMapper.queryDailyIncome(queryMap);


			//利润缓存数据
			List<Map> stationProfitCache = profitMapper.queryStationProfitCache(settlementMonth);

			//合约缓存数据,临时使用
			List<DataVo> contractCacheList = profitMapper.queryContractCache(settlementMonth);
			//业务电量
//			queryMap.put("testMemer", 0);
//			queryMap.put("usingRoperty", 1);
//			List<Map> businessChgPowerList = profitMapper.queryBusinessChgPower(queryMap);

			BigDecimal chgPower = BigDecimal.ZERO;
			//充电订单收入(非合约)
			BigDecimal orderIncome = BigDecimal.ZERO;

			//一个场站有多种合约,同类型只能有一种
			Map<Integer,List<Map>> contractMap = new HashMap<>();

			//key是合约id
//			Map<Integer,Map> conMap = new HashMap();
			if(allContract != null && allContract.size() > 0){
				for (Map map3 : allContract) {
					if(map3.get("stationId") != null){
						Integer stationId = Integer.parseInt(map3.get("stationId").toString());
						if(contractMap.get(stationId) != null){
							List<Map> list = contractMap.get(stationId);
							list.add(map3);
							contractMap.put(stationId, list);
						}else{
							List<Map> list = new ArrayList();
							list.add(map3);
							contractMap.put(stationId, list);
						}
					}
				}
			}

			Map<Integer,BigDecimal> groupMonthlyMap = new HashMap();
			if(groupMonthlyCache != null && groupMonthlyCache.size() > 0){
				for (Map map : groupMonthlyCache) {
					if(map.get("stationId") != null){
						Integer stationId = Integer.parseInt(map.get("stationId").toString());
						if(groupMonthlyMap.get(stationId) != null){
							BigDecimal charge = groupMonthlyMap.get(stationId);
							BigDecimal newCharge = charge.add(new BigDecimal(map.get("monthlyCharge").toString()));
							groupMonthlyMap.put(stationId, newCharge);
						}else{
							groupMonthlyMap.put(stationId, new BigDecimal(map.get("monthlyCharge").toString()));
						}
					}
				}
			}

			Map<Integer,Map> stationMap = new HashMap<>();
			if (allStation != null && allStation.size() > 0){
				for (Map map:allStation) {
					if (map.get("stationId") != null){
						int stationId = Integer.parseInt(map.get("stationId").toString());
						stationMap.put(stationId, map);
					}
				}
			}

//			Map<Integer,BigDecimal> businessChgPowerMap = new HashMap<>();
//			if(businessChgPowerList != null && businessChgPowerList.size() > 0){
//				for (Map map : businessChgPowerList) {
//					if(map.get("stationId") != null){
//						Integer stationId = Integer.parseInt(map.get("stationId").toString());
//						BigDecimal power = BigDecimal.ZERO;
//						if(map.get("power") != null){
//							power = new BigDecimal(map.get("power").toString());
//						}
//						businessChgPowerMap.put(stationId, power);
//					}
//				}
//			}


			Iterator<Map> iterator = billPayList.iterator();
			while (iterator.hasNext()) {
				Calendar passCal = null;
				Calendar billCal = null;
				Map next = iterator.next();
				if(next.get("passDate") != null){
					passCal = CalendarUtils.convertStrToCalendar(next.get("passDate").toString(), CalendarUtils.yyyyMMddHHmmss);
				}
				if(next.get("endTime") != null){
					billCal = CalendarUtils.convertStrToCalendar(next.get("endTime").toString(), CalendarUtils.yyyyMMddHHmmss);
				}
				int compareTo = 0;
				if(passCal != null && billCal != null){
					String formatCalendar = CalendarUtils.formatCalendar(billCal, CalendarUtils.yyyyMMddHHmmss);
					String formatCalendar1 = CalendarUtils.formatCalendar(passCal, CalendarUtils.yyyyMMddHHmmss);
					compareTo = billCal.compareTo(passCal);
				}
				if(compareTo == 1){
					iterator.remove();
				}
			}

			if(billPayList != null && billPayList.size() > 0){
				for (Map map : billPayList) {
					if(map.get("stationId") != null){
						Integer stationId = Integer.parseInt(map.get("stationId").toString());
						Map initMap = init(settlementMonth);
						resultMap.put(stationId, initMap);
					}
				}
			}

			Map<Integer,BigDecimal> stationPileChgMap = new HashMap<>();
			if(stationPileChg != null && stationPileChg.size() > 0){
				for (Map map : stationPileChg) {
					if(map.get("stationId") != null){
						Integer stationId = Integer.parseInt(map.get("stationId").toString());
						BigDecimal pileTotalPower = BigDecimal.ZERO;
						if(map.get("pileChg") != null){
							pileTotalPower = new BigDecimal(map.get("pileChg").toString());
						}
						stationPileChgMap.put(stationId, pileTotalPower);
					}
				}
			}

			//订单
			if(billPayList != null && billPayList.size() > 0){
				for (Map map : billPayList) {
					if(map.get("stationId") != null){
						Integer stationId = Integer.parseInt(map.get("stationId").toString());
						Map map2 = resultMap.get(stationId);

						if(map2 != null && map2.size() > 0){
							if(map.get("amount") != null){
								orderIncome = new BigDecimal(map.get("amount").toString());

								if(map2 != null && map2.size() > 0){
									if(map2.get("orderIncome") != null){
										BigDecimal oldOrderIncome = new BigDecimal(map2.get("orderIncome").toString()) ;
										map2.put("orderIncome", oldOrderIncome.add(orderIncome));
									}
								}
							}

							if(map.get("chgPower") != null){
								chgPower = new BigDecimal(map.get("chgPower").toString());
								if(map2.get("stationTestCharge") != null && map.get("testMember") != null){
									BigDecimal oldStationTestCharge = new BigDecimal(map2.get("stationTestCharge").toString());
									oldStationTestCharge = chgPower.add(oldStationTestCharge);
									if("1".equals(map.get("testMember").toString())){
										map2.put("stationTestCharge", oldStationTestCharge);
										map2.put("testMember", "1");
									}
								}
								if(map2.get("stationUseCharge") != null && map.get("usingRoperty") != null && map.get("belongsType") != null){
									BigDecimal oldStationUseCharge = new BigDecimal(map2.get("stationUseCharge").toString());
									oldStationUseCharge = chgPower.add(oldStationUseCharge);
									//自用企业电量
									if("1".equals(map.get("usingRoperty").toString()) && "2".equals(map.get("belongsType").toString())){
										map2.put("stationUseCharge", oldStationUseCharge);
										map2.put("usingRoperty", "1");
									}
								}
								if(map2.get("stationTotalCharge") != null){
									BigDecimal oldStationTotalCharge = new BigDecimal(map2.get("stationTotalCharge").toString());
									oldStationTotalCharge = chgPower.add(oldStationTotalCharge);
									//充电总充电量
									map2.put("stationTotalCharge", oldStationTotalCharge);
								}

								//场站相关
								map2.put("stationTargetCharge", map.get("targetCharge"));//目标充电量
								map2.put("stationTargetIncome", map.get("targetIncome"));//目标收入
								map2.put("networkFee", map.get("networkFee"));//网络费
								map2.put("parkingFee", map.get("parkingFee"));//停车费
								map2.put("propertyFee", map.get("propertyFee"));//物管费
								map2.put("artificialFee", map.get("artificialFee"));//人工费

								map2.put("costAmortization", map.get("costAmortization"));//费用摊销
								map2.put("orgId", map.get("orgId"));
								map2.put("orgName", map.get("orgName"));
								map2.put("stationId", map.get("stationId"));
								map2.put("stationName", map.get("stationName"));

								map2.put("stationType", map.get("stationType"));
								map2.put("stationServiceType", map.get("serviceType"));
								map2.put("stationCoopType", map.get("stationCoopType"));

								resultMap.put(stationId, map2);
							}

							List<Map> list = contractMap.get(stationId);

							if(list != null && list.size() > 0){
								for (Map map3 : list) {
									if(map3.get("contractType") != null){
										//收入合约
										if(map3.get("contractIncomeType") != null && "0".equals(map3.get("contractType").toString())){
											//设备租赁合约
											if("2".equals(map3.get("contractIncomeType").toString())){
												if(map3.get("contractAmount") != null){
													map2.put("inFacRentalFee", map3.get("contractAmount"));
												}
											}

											//车位租赁费
											if("3".equals(map3.get("contractIncomeType").toString())){
												if(map3.get("contractAmount") != null){
													map2.put("inParkRentalFee", map3.get("contractAmount"));
												}
											}
											//土地租赁费
											if("4".equals(map3.get("contractIncomeType").toString())){
												if(map3.get("contractAmount") != null){
													map2.put("inLandRentalFee", map3.get("contractAmount"));
												}
											}
										}
										// 支出合约
										if(map3.get("contractCostType") != null && "2".equals(map3.get("contractType").toString())){
											//土地租赁
											if("1".equals(map3.get("contractCostType").toString())){
												if(map3.get("contractAmount") != null){
													map2.put("exLandRentalFee", map3.get("contractAmount"));
												}
											}
											//土地租赁分成费
											if("2".equals(map3.get("contractCostType").toString())){
												if(map3.get("contractAmount") != null){
													map2.put("exLandRentalExpend", map3.get("contractAmount"));
												}
											}
											//房屋租赁费
											if("3".equals(map3.get("contractCostType").toString())){
												if(map3.get("contractAmount") != null){
													map2.put("exHouseRentalFee", map3.get("contractAmount"));
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			if(stationProfitCache != null && stationProfitCache.size() > 0){
				for (Map map : stationProfitCache) {
					if(map.get("stationId") != null){
						Integer stationId = Integer.parseInt(map.get("stationId").toString());
						Map map2 = resultMap.get(stationId);
						Object otherIncome = INIT_ZERO;
						Object repairFee = INIT_ZERO;
						Object waterFee = INIT_ZERO;
						Object eleFee = INIT_ZERO;
						Object stationTotalPower = INIT_ZERO;
						Object lowConsumable = INIT_ZERO;
						Object otherExpenses = INIT_ZERO;

						if(map.get("otherIncome") != null){
							otherIncome = map.get("otherIncome");
						}
						if(map.get("repairFee") != null){
							repairFee = map.get("repairFee");
						}
						if(map.get("waterFee") != null){
							waterFee = map.get("waterFee");
						}
						if(map.get("eleFee") != null){
							eleFee = map.get("eleFee");
						}
						if(map.get("stationTotalPower") != null){
							stationTotalPower = map.get("stationTotalPower");
						}
						if(map.get("lowConsumable") != null){
							lowConsumable = map.get("lowConsumable");
						}
						if(map.get("otherExpenses") != null){
							otherExpenses = map.get("otherExpenses");
						}

						if(map2 != null && map2.size() > 0){
							map2.put("otherIncome", otherIncome);//其他收入
							map2.put("repairFee", repairFee);//维修费
							map2.put("waterFee", waterFee);//水费
							map2.put("eleFee", eleFee);//电费
							map2.put("stationTotalPower", stationTotalPower);//场站总用电量
							map2.put("lowConsumable", lowConsumable);//低值易耗品
							map2.put("otherExpenses", otherExpenses);//其他支出
						}else{
							Map init = init(settlementMonth);
							init.put("otherIncome", otherIncome);//其他收入
							init.put("repairFee", repairFee);//维修费
							init.put("waterFee", waterFee);//水费
							init.put("eleFee", eleFee);//电费
							init.put("stationTotalPower", stationTotalPower);//场站总用电量
							init.put("lowConsumable", lowConsumable);//低值易耗品
							init.put("otherExpenses", otherExpenses);//其他支出
							init.put("stationId", stationId);

							Map stationInfo = stationMap.get(stationId);
							init.put("stationName", stationInfo.get("stationName"));
							init.put("orgId", stationInfo.get("orgId"));
							init.put("orgName", stationInfo.get("orgName"));

							resultMap.put(stationId, init);
						}
					}
				}
			}


			//合约
			Map<Integer,Map<String,BigDecimal>> inPileTotalIncomeMap = new HashMap();
			Map<Integer,Map<String,BigDecimal>> inPilePmIncomeMap = new HashMap();
			Map<Integer,Map<String,BigDecimal>> diviExpendMap = new HashMap();


			contractMap(inPileTotalIncomeList, inPileTotalIncomeMap);
			contractMap(inPilePmIncomeList, inPilePmIncomeMap);
			contractMap(diviExpendList, diviExpendMap);
			//合约历史数据
			Map<Integer, DataVo> contractCacheMap = new HashMap<>();
			for (DataVo vo : contractCacheList) {
				if (vo.isNotBlank("stationId")){
					int stationId = vo.getInt("stationId");
					contractCacheMap.put(stationId, vo);
				}
			}

			//采集表
//			Map<Integer,Map> pileDataMap = new HashMap(); 
//			if(pileCollDataList != null && pileCollDataList.size() > 0){
//				for (Map map3 : pileCollDataList) {
//					if(map3.get("pileId") != null){
//						Integer pileId = Integer.parseInt(map3.get("pileId").toString());
//						
//						BigDecimal min = BigDecimal.ZERO;
//						BigDecimal max = BigDecimal.ZERO;
//						
//						if(map3.get("min") != null){
//							min = new BigDecimal(map3.get("min").toString());
//						}
//						if(map3.get("max") != null){
//							max = new BigDecimal(map3.get("max").toString());
//						}
//						//表码差
//						BigDecimal pileTotalPower = max.subtract(min);
//
//						if(map3.get("ortMode") != null && map3.get("numberGun") != null){
//							Integer ortMode = Integer.parseInt(map3.get("ortMode").toString());
//							Integer numberGun = Integer.parseInt(map3.get("numberGun").toString());
//
//							//直流模式计算总表
//							if("2".equals(ortMode)){
//								Integer innerId = PileTypeUtils.getInnerId(numberGun, ortMode, "Z");
//								
//								BigDecimal ratioPower = BigDecimal.ZERO;
//								BigDecimal meterRatio = BigDecimal.ONE;
//								//表计
//								Map<Integer, Map> map = meterInfoMap.get(pileId);
//								if(map != null && map.size() > 0){
//									Map map2 = map.get(innerId);
//									if(map2.get("meterRatio") != null){
//										meterRatio = new BigDecimal(map2.get("meterRatio").toString());
//									}
//									ratioPower = pileTotalPower.multiply(meterRatio).setScale(2, BigDecimal.ROUND_HALF_UP);
//								}
//								
//								map3.put("totalPower", ratioPower);
//							}else{
//								//交流或者交直模式是枪表之和
//								//设备总用电量
//								BigDecimal totalPower = BigDecimal.ZERO;
//								if(pileDataMap.get(pileId) != null){
//									Map oldMap = (Map) map3.get(pileId);
//									BigDecimal oldPileTotalPower = new BigDecimal(oldMap.get("totalPower").toString());
//									totalPower = oldPileTotalPower.add(pileTotalPower);
//								}else{
//									totalPower = pileTotalPower;
//								}
//								map3.put("totalPower", totalPower);
//							}
//						}
//						pileDataMap.put(pileId, map3);
//					}
//				}
//			}
			//设备总用电量,办公用电,设备总损耗
			if(allPile != null && allPile.size() > 0){
				for (Map map3 : allPile) {
					if(map3.get("pileId") != null){
//						Integer pileId = Integer.parseInt(map3.get("pileId").toString());
//						Map dataMap = pileDataMap.get(pileId);
						//if(dataMap != null && dataMap.size() > 0){

							if(map3.get("stationId") != null){
								Integer stationId = Integer.parseInt(map3.get("stationId").toString());
								Map map = resultMap.get(stationId);
								if(map != null && map.size() > 0){
									//场站总用电量
									BigDecimal stationTotalPower = BigDecimal.ZERO;
									
									if(map.get("stationTotalPower") != null){
										stationTotalPower = new BigDecimal(map.get("stationTotalPower").toString());
									}
									
									//充电总电量
									BigDecimal stationTotalCharge = new BigDecimal(map.get("stationTotalCharge").toString());
									//设备总用电量
									BigDecimal pileTotalPower = BigDecimal.ZERO;

									if(stationPileChgMap.get(stationId) != null){
										pileTotalPower = stationPileChgMap.get(stationId);
									}

									//等于0
									if(stationTotalPower.compareTo(BigDecimal.ZERO) == 0){
										stationTotalPower = pileTotalPower;
										map.put("stationTotalPower", stationTotalPower);
									}

									map.put("pileTotalPower", pileTotalPower);
									//办公用电
									map.put("stationOfficeCharge", stationTotalPower.subtract(pileTotalPower));
									//设备总损耗
									map.put("pileTotalLoss", pileTotalPower.subtract(stationTotalCharge));
								}
							}
						}
					}
//				}
			}
			
			//统计合计
			for (Integer key : resultMap.keySet()) {
				Map map2 = resultMap.get(key);
				DataVo cacheDataVo = contractCacheMap.get(key);

				//合约记录有数据的场站按照历史记录算,没有按照正常合约算
				if (cacheDataVo != null && cacheDataVo.size() > 0){
					//合约历史数据,兼容
					putContractHistoryResultVal(cacheDataVo, map2,key,groupMonthlyMap);
				}else{
					//正常合约计算数据
					putContractResultVal(groupMonthlyMap, inPileTotalIncomeMap, inPilePmIncomeMap, diviExpendMap, key, map2);
				}
				putStationGoalChargeVal(map2);
			}

			for (Integer key:contractCacheMap.keySet()) {
				if (345 == key){
					System.out.println("--");
				}
				if (resultMap.get(key) == null){
					DataVo cacheDataVo = contractCacheMap.get(key);
					Map init = init(settlementMonth);
					putContractHistoryResultVal(cacheDataVo, init,key,groupMonthlyMap);
					putStationGoalChargeVal(init);
					Map stationInfo = stationMap.get(key);
					init.put("stationName", stationInfo.get("stationName"));
					init.put("orgId", stationInfo.get("orgId"));
					init.put("orgName", stationInfo.get("orgName"));
					init.put("stationId", key);
					resultMap.put(key, init);
				}
			}
			
			if(lastMonthProfit != null && lastMonthProfit.size() > 0){
				for (Map map : lastMonthProfit) {
					if(map.get("stationId") != null){
						Integer stationId = Integer.parseInt(map.get("stationId").toString());
						Map map2 = resultMap.get(stationId);
						if(map.get("stationTargetCharge") == null){
							map.put("stationTargetCharge", 0);
						}
						if(map.get("stationTotalCharge") == null){
							map.put("stationTotalCharge", 0);
						}
						if(map.get("stationTargetIncome") == null){
							map.put("stationTargetIncome", 0);
						}
						if(map.get("fixedIncomeSubtotal") == null){
							map.put("fixedIncomeSubtotal", 0);
						}
						if(map.get("unfixedIncomeSubtotal") == null){
							map.put("unfixedIncomeSubtotal", 0);
						}
						if(map.get("stationIncomeSubtotal") == null){
							map.put("stationIncomeSubtotal", 0);
						}
						if(map.get("fixedExpendSubtotal") == null){
							map.put("fixedExpendSubtotal", 0);
						}
						if(map.get("unfixedExpendSubtotal") == null){
							map.put("unfixedExpendSubtotal", 0);
						}
						if(map.get("costTotal") == null){
							map.put("costTotal", 0);
						}
						if(map.get("stationGrossProfit") == null){
							map.put("stationGrossProfit", 0);
						}
						BigDecimal lastStationTargetCharge = new BigDecimal(map.get("stationTargetCharge").toString());
						BigDecimal lastStationTotalCharge = new BigDecimal(map.get("stationTotalCharge").toString());
						BigDecimal lastStationTargetIncome = new BigDecimal(map.get("stationTargetIncome").toString());
						BigDecimal lastFixedIncomeSubtotal = new BigDecimal(map.get("fixedIncomeSubtotal").toString());
						BigDecimal lastUnfixedIncomeSubtotal = new BigDecimal(map.get("unfixedIncomeSubtotal").toString());
						BigDecimal lastStationIncomeSubtotal = new BigDecimal(map.get("stationIncomeSubtotal").toString());
						BigDecimal lastFixedExpendSubtotal = new BigDecimal(map.get("fixedExpendSubtotal").toString());
						BigDecimal lastUnfixedExpendSubtotal = new BigDecimal(map.get("unfixedExpendSubtotal").toString());
						BigDecimal lastCostTotal = new BigDecimal(map.get("costTotal").toString());
						BigDecimal lastStationGrossProfit = new BigDecimal(map.get("stationGrossProfit").toString());
						
						if(map2 != null){
							if(map2.get("stationTargetCharge") == null){
								map2.put("stationTargetCharge", 0);
							}
							if(map2.get("stationTotalCharge") == null){
								map2.put("stationTotalCharge", 0);
							}
							if(map2.get("stationTargetIncome") == null){
								map2.put("stationTargetIncome", 0);
							}
							if(map2.get("fixedIncomeSubtotal") == null){
								map2.put("fixedIncomeSubtotal", 0);
							}
							if(map2.get("unfixedIncomeSubtotal") == null){
								map2.put("unfixedIncomeSubtotal", 0);
							}
							if(map2.get("stationIncomeSubtotal") == null){
								map2.put("stationIncomeSubtotal", 0);
							}
							if(map2.get("fixedExpendSubtotal") == null){
								map2.put("fixedExpendSubtotal", 0);
							}
							if(map2.get("unfixedExpendSubtotal") == null){
								map2.put("unfixedExpendSubtotal", 0);
							}
							if(map2.get("costTotal") == null){
								map2.put("costTotal", 0);
							}
							if(map2.get("stationGrossProfit") == null){
								map2.put("stationGrossProfit", 0);
							}
							BigDecimal stationTargetCharge = new BigDecimal(map2.get("stationTargetCharge").toString());
							BigDecimal stationTotalCharge = new BigDecimal(map2.get("stationTotalCharge").toString());
							BigDecimal stationTargetIncome = new BigDecimal(map2.get("stationTargetIncome").toString());
							BigDecimal fixedIncomeSubtotal = new BigDecimal(map2.get("fixedIncomeSubtotal").toString());
							BigDecimal unfixedIncomeSubtotal = new BigDecimal(map2.get("unfixedIncomeSubtotal").toString());
							BigDecimal stationIncomeSubtotal = new BigDecimal(map2.get("stationIncomeSubtotal").toString());
							BigDecimal fixedExpendSubtotal = new BigDecimal(map2.get("fixedExpendSubtotal").toString());
							BigDecimal unfixedExpendSubtotal = new BigDecimal(map2.get("unfixedExpendSubtotal").toString());
							BigDecimal costTotal = new BigDecimal(map2.get("costTotal").toString());
							BigDecimal stationGrossProfit = new BigDecimal(map2.get("stationGrossProfit").toString());
							
							
							map2.put("stationTargetChargeCompare", stationTargetCharge.compareTo(lastStationTargetCharge));
							map2.put("stationTotalChargeCompare", stationTotalCharge.compareTo(lastStationTotalCharge));
							map2.put("stationTargetIncomeCompare", stationTargetIncome.compareTo(lastStationTargetIncome));
							map2.put("fixedIncomeSubtotalCompare", fixedIncomeSubtotal.compareTo(lastFixedIncomeSubtotal));
							map2.put("unfixedIncomeSubtotalCompare", unfixedIncomeSubtotal.compareTo(lastUnfixedIncomeSubtotal));
							map2.put("stationIncomeSubtotalCompare", stationIncomeSubtotal.compareTo(lastStationIncomeSubtotal));
							map2.put("fixedExpendSubtotalCompare", fixedExpendSubtotal.compareTo(lastFixedExpendSubtotal));
							map2.put("unfixedExpendSubtotalCompare", unfixedExpendSubtotal.compareTo(lastUnfixedExpendSubtotal));
							map2.put("costTotalCompare", costTotal.compareTo(lastCostTotal));
							map2.put("stationGrossProfitCompare", stationGrossProfit.compareTo(lastStationGrossProfit));
						}
					}
				}
			}
			
			//最后把订单没有的场站设置默认值
			if(allStation != null && allStation.size() > 0){
				for (Map map3 : allStation) {
					if(map3.get("stationId") != null){
						Integer stationId = Integer.parseInt(map3.get("stationId").toString());
						if(resultMap.get(stationId) == null){
				 			Map map2 = new HashMap();
							map2.put("stationTargetChargeCompare", INIT_ZERO);
							map2.put("stationTotalChargeCompare", INIT_ZERO);
							map2.put("stationTargetIncomeCompare", INIT_ZERO);
							map2.put("fixedIncomeSubtotalCompare", INIT_ZERO);
							map2.put("unfixedIncomeSubtotalCompare", INIT_ZERO);
							map2.put("stationIncomeSubtotalCompare", INIT_ZERO);
							map2.put("fixedExpendSubtotalCompare", INIT_ZERO);
							map2.put("unfixedExpendSubtotalCompare", INIT_ZERO);
							map2.put("costTotalCompare", INIT_ZERO);
							map2.put("stationGrossProfitCompare", INIT_ZERO);
							map2.put("stationGoalCharge", INIT_ZERO);
							map2.put("stationTestCharge", INIT_ZERO);
							map2.put("stationUseCharge", INIT_ZERO);
							map2.put("stationTotalCharge", INIT_ZERO);
							map2.put("stationTargetCharge", INIT_ZERO);
							map2.put("stationTargetIncome", INIT_ZERO);
							map2.put("inPileTotalIncome", INIT_ZERO);
							map2.put("settlementMonth",settlementMonth);
							map2.put("orgId", map3.get("orgId"));
							map2.put("orgName", map3.get("orgName"));
							map2.put("stationId", map3.get("stationId"));
							map2.put("stationName", map3.get("stationName"));
							map2.put("stationType", map3.get("stationType"));
							map2.put("stationServiceType", map3.get("serviceType"));
							map2.put("stationCoopType", map3.get("stationCoopType"));
							
							Double stationTargetCharge = 0D;
							if(map3.get("targetCharge") != null){
								stationTargetCharge = Double.parseDouble(map3.get("targetCharge").toString());
							}
							Double stationTargetIncome = 0D;
							if(map3.get("targetIncome") != null){
								stationTargetIncome = Double.parseDouble(map3.get("targetIncome").toString());
							}
							Double networkFee = 0D;
							if(map3.get("networkFee") != null){
								networkFee = Double.parseDouble(map3.get("networkFee").toString());
							}
							Double parkingFee = 0D;
							if(map3.get("parkingFee") != null){
								parkingFee = Double.parseDouble(map3.get("parkingFee").toString());
							}
							Double propertyFee = 0D;
							if(map3.get("propertyFee") != null){
								propertyFee = Double.parseDouble(map3.get("propertyFee").toString());
							}
							Double artificialFee = 0D;
							if(map3.get("artificialFee") != null){
								artificialFee = Double.parseDouble(map3.get("artificialFee").toString());
							}
							Double costAmortization = 0D;
							if(map3.get("costAmortization") != null){
								costAmortization = Double.parseDouble(map3.get("costAmortization").toString());
							}
							
							map2.put("stationTargetCharge", stationTargetCharge);//目标充电量
							map2.put("stationTargetIncome", stationTargetIncome);//目标收入
							map2.put("otherIncome", INIT_ZERO);//其他收入
							map2.put("repairFee", INIT_ZERO);//维修费
							map2.put("waterFee", INIT_ZERO);//水费
							map2.put("networkFee", networkFee);//网络费
							map2.put("parkingFee", parkingFee);//停车费
							map2.put("propertyFee", propertyFee);//物管费
							map2.put("artificialFee", artificialFee);//人工费
							
							map2.put("costAmortization", costAmortization);//费用摊销
							map2.put("lowConsumable", INIT_ZERO);//低值易耗品
							map2.put("otherExpenses", INIT_ZERO);//其他支出
							map2.put("pileTotalPower", INIT_ZERO);
							//办公用电
							map2.put("stationOfficeCharge", INIT_ZERO);
							//设备总损耗
							map2.put("pileTotalLoss", INIT_ZERO);
							map2.put("exHouseRentalFee", INIT_ZERO);
							map2.put("exLandRentalExpend", INIT_ZERO);
							map2.put("exLandRentalFee", INIT_ZERO);
							map2.put("inLandRentalFee", INIT_ZERO);
							map2.put("inFacRentalFee", INIT_ZERO);
							map2.put("inParkRentalFee", INIT_ZERO);
							map2.put("fixedIncomeSubtotal", INIT_ZERO);
							//固定资产折旧默认为INIT_ZERO
							map2.put("depreFixedAsset", INIT_ZERO);
							map2.put("fixedIncomeSubtotal", INIT_ZERO);
							map2.put("unfixedIncomeSubtotal", INIT_ZERO);
							map2.put("stationIncomeSubtotal", INIT_ZERO);
							map2.put("fixedExpendSubtotal", INIT_ZERO);
							map2.put("unfixedExpendSubtotal", INIT_ZERO);
							map2.put("costTotal", INIT_ZERO);
							map2.put("stationGrossProfit", INIT_ZERO);
							map2.put("orderIncome", INIT_ZERO);
							map2.put("inPilePmIncome", INIT_ZERO);
							map2.put("diviExpend", INIT_ZERO);
							map2.put("stationTotalPower", INIT_ZERO);
							map2.put("eleFee", INIT_ZERO);
							map2.put("groupMonthly", INIT_ZERO);
							
							resultMap.put(stationId, map2);
						}
					}
				}
			}
			
			
			if(resultMap.values().size() > 0){
				resultList.addAll(resultMap.values());
				if (resultList.size() > 0) {
					for (int i = 0; i < resultList.size() / BATCH_MAX_SIZE + 1; i++) {
						if ((i + 1) * BATCH_MAX_SIZE < resultList.size()) {
							profitMapper.batchInser(resultList.subList(i * BATCH_MAX_SIZE, (i + 1) * BATCH_MAX_SIZE));
						} else {
							profitMapper.batchInser(resultList.subList(i * BATCH_MAX_SIZE, resultList.size()));
							break;
						}
					}
				}

//				batch(resultList, new BatchIn() {
//					@Override
//					public void batch(List list) {
//						profitMapper.batchInser(list);
//					}
//				});
			}

			saveLog("场站利润定时任务:"+month, OperateType.add.OperateId,"补跑场站利润定时任务结束:"+CalendarUtils.formatCalendar(Calendar.getInstance(),CalendarUtils.yyyyMMddHHmmss),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void putStationGoalChargeVal(Map map2) {
		//业务充电量
		BigDecimal stationGoalCharge = BigDecimal.ZERO;
		if(map2.get("stationUseCharge") != null && map2.get("stationTestCharge") != null && map2.get("stationTotalPower") != null){
            BigDecimal stationUseCharge = new BigDecimal(map2.get("stationUseCharge").toString());
            BigDecimal stationTestCharge = new BigDecimal(map2.get("stationTestCharge").toString());
            BigDecimal stationTotalCharge = new BigDecimal(map2.get("stationTotalCharge").toString());

            if(map2.get("testMember") != null && map2.get("usingRoperty") != null){
                stationGoalCharge = stationTotalCharge.subtract(stationTestCharge);
            }else{
                stationGoalCharge = stationTotalCharge.subtract(stationUseCharge).subtract(stationTestCharge);
            }
        }
//				if(businessChgPowerMap.get(key) != null){
//					stationGoalCharge = businessChgPowerMap.get(key);
//				}

		map2.put("stationGoalCharge", stationGoalCharge);
	}

	private void putContractHistoryResultVal(DataVo contractCachaMap, Map map2,Integer key,Map<Integer, BigDecimal> groupMonthlyMap) {
		//订单收入(非合约,场站订单充电收入)
		double orderIncome = contractCachaMap.getDouble("orderIncome");
		//集团月结(月结合约对应场站收入)
		double groupMonthly = contractCachaMap.getDouble("groupMonthly");
		//'设备总收入(分成合约)',
		double inPileTotalIncome = contractCachaMap.getDouble("inPileTotalIncome");
		//'设备运维收入(分成合约)'
		double inPilePmIncome = contractCachaMap.getDouble("inPilePmIncome");
		//'充电设施租赁费(固定收入合约)'
		double inFacRentalFee = contractCachaMap.getDouble("inFacRentalFee");
		//车位租赁费(固定收入合约)
		double inParkRentalFee = contractCachaMap.getDouble("inParkRentalFee");
		//土地租赁费(固定收入合约)
		double inLandRentalFee = contractCachaMap.getDouble("inLandRentalFee");
		//分成支出(分成合约)
		double diviExpend = contractCachaMap.getDouble("diviExpend");
		//土地租赁费(固定支出合约)
		double exLandRentalFee = contractCachaMap.getDouble("exLandRentalFee");
		//土地租赁分成费(固定支出合约)
		double exLandRentalExpend = contractCachaMap.getDouble("exLandRentalExpend");
		//房租租赁费(固定支出合约)
		double exHouseRentalFee = contractCachaMap.getDouble("exHouseRentalFee");

//		BigDecimal groupMonthly = BigDecimal.ZERO;
//		//集团月结场站收入
//		if(groupMonthlyMap.get(key) != null){
//			groupMonthly = groupMonthlyMap.get(key);
//		}
		map2.put("groupMonthly", groupMonthly);
		map2.put("orderIncome", orderIncome);
		map2.put("inPilePmIncome", inPilePmIncome);
		map2.put("inPileTotalIncome", inPileTotalIncome);
		map2.put("diviExpend", diviExpend);
		map2.put("inParkRentalFee", inParkRentalFee);
		map2.put("inLandRentalFee", inLandRentalFee);
		map2.put("inFacRentalFee", inFacRentalFee);
		map2.put("exLandRentalFee", exLandRentalFee);
		map2.put("exLandRentalExpend", exLandRentalExpend);
		map2.put("exHouseRentalFee", exHouseRentalFee);

		BigDecimal orderIncomes = BigDecimal.ZERO;//订单收入(非合约,场站订单充电收入)
		if(map2.get("orderIncome") != null){
			orderIncomes = new BigDecimal(map2.get("orderIncome").toString());
		}

		//固定收入小计
		if(compareToZero(inPileTotalIncome) && compareToZero(inPilePmIncome)
				&& compareToZero(inFacRentalFee) && compareToZero(inParkRentalFee)
				&& compareToZero(inLandRentalFee) && compareToZero(groupMonthly)){
			map2.put("fixedIncomeSubtotal", orderIncomes);
		}else{
			double fixedIncomeSubtotal = inPileTotalIncome + inPilePmIncome + inFacRentalFee + inParkRentalFee + inLandRentalFee + groupMonthly;
			map2.put("fixedIncomeSubtotal", fixedIncomeSubtotal);
		}

		BigDecimal otherIncome = BigDecimal.ZERO;

		if(map2.get("otherIncome") != null){
			otherIncome = new BigDecimal(map2.get("otherIncome").toString());
		}
		//非固定收入小计
		map2.put("unfixedIncomeSubtotal", otherIncome);
		//场站收入合计
		BigDecimal stationIncomeSubtotal = otherIncome.add(new BigDecimal(map2.get("fixedIncomeSubtotal").toString()));
		map2.put("stationIncomeSubtotal", stationIncomeSubtotal);


		BigDecimal waterFee = BigDecimal.ZERO;//水费
		BigDecimal networkFee = BigDecimal.ZERO;//网费
		BigDecimal propertyFee = BigDecimal.ZERO;//物管费
		BigDecimal artificialFee = BigDecimal.ZERO;//人工支出
		BigDecimal depreFixedAsset = BigDecimal.ZERO;//固定资产折旧
		BigDecimal costAmortization = BigDecimal.ZERO;//费用摊销
		BigDecimal parkingFee = BigDecimal.ZERO;//停车费
		BigDecimal eleFee = BigDecimal.ZERO;//电费

		if(map2.get("eleFee") != null){
			eleFee = new BigDecimal(map2.get("eleFee").toString());
		}
		if(map2.get("waterFee") != null){
			waterFee = new BigDecimal(map2.get("waterFee").toString());
		}
		if(map2.get("networkFee") != null){
			networkFee = new BigDecimal(map2.get("networkFee").toString());
		}
		if(map2.get("propertyFee") != null){
			propertyFee = new BigDecimal(map2.get("propertyFee").toString());
		}
		if(map2.get("artificialFee") != null){
			artificialFee = new BigDecimal(map2.get("artificialFee").toString());
		}
		if(map2.get("depreFixedAsset") != null){
			depreFixedAsset = new BigDecimal(map2.get("depreFixedAsset").toString());
		}
		if(map2.get("costAmortization") != null){
			costAmortization = new BigDecimal(map2.get("costAmortization").toString());
		}
		if(map2.get("parkingFee") != null){
			parkingFee = new BigDecimal(map2.get("parkingFee").toString());
		}
		//固定支出小计
		BigDecimal fixedExpendSubtotal = eleFee.add(waterFee).add(networkFee)
				.add(new BigDecimal(diviExpend)).add(new BigDecimal(exLandRentalFee))
				.add(new BigDecimal(exHouseRentalFee)).add(new BigDecimal(exLandRentalExpend))
				.add(propertyFee).add(artificialFee).add(depreFixedAsset)
				.add(costAmortization).add(parkingFee);
		map2.put("fixedExpendSubtotal", fixedExpendSubtotal);

		BigDecimal repairFee = BigDecimal.ZERO;//维修费
		BigDecimal lowConsumable = BigDecimal.ZERO;//低值易耗品
		BigDecimal otherExpenses = BigDecimal.ZERO;//其他支出

		if(map2.get("repairFee") != null){
			repairFee = new BigDecimal(map2.get("repairFee").toString());
		}
		if(map2.get("lowConsumable") != null){
			lowConsumable = new BigDecimal(map2.get("lowConsumable").toString());
		}
		if(map2.get("otherExpenses") != null){
			otherExpenses = new BigDecimal(map2.get("otherExpenses").toString());
		}
		//非固定支出小计
		BigDecimal unfixedExpendSubtotal = repairFee.add(lowConsumable).add(otherExpenses);
		map2.put("unfixedExpendSubtotal", unfixedExpendSubtotal);
		//成本合计
		BigDecimal costTotal= fixedExpendSubtotal.add(unfixedExpendSubtotal);
		map2.put("costTotal", costTotal);
		//场站毛利润
		BigDecimal stationGrossProfit = stationIncomeSubtotal.subtract(costTotal);
		map2.put("stationGrossProfit", stationGrossProfit);
	}

	private boolean compareToZero(double inPileTotalIncome) {
		return BigDecimal.ZERO.compareTo(new BigDecimal(inPileTotalIncome)) == 0;
	}

	/**
	 * 计算正常合约数据
	 */
	private void putContractResultVal(Map<Integer, BigDecimal> groupMonthlyMap, Map<Integer, Map<String, BigDecimal>> inPileTotalIncomeMap, Map<Integer, Map<String, BigDecimal>> inPilePmIncomeMap, Map<Integer, Map<String, BigDecimal>> diviExpendMap, Integer key, Map map2) {
		BigDecimal inPileTotalIncomes = BigDecimal.ZERO;//设备总收入(分成合约)
		BigDecimal inPilePmIncomes = BigDecimal.ZERO;//设备运维收入(分成合约)
		BigDecimal diviExpends = BigDecimal.ZERO;//支出
		//计算合约
		Map<String, BigDecimal> totalIncomemap = inPileTotalIncomeMap.get(key);
		Map<String, BigDecimal> pmIncomemap = inPilePmIncomeMap.get(key);
		Map<String, BigDecimal> diviExpendmap = diviExpendMap.get(key);
		if(totalIncomemap != null && totalIncomemap.size() > 0){
            //服务费
//					BigDecimal dailyIncomeTotalServiceFee = totalIncomemap.get("dailyIncomeTotalServiceFee");
//					//分成比例
//					BigDecimal percentage = totalIncomemap.get("percentage");
//
//					//设备收入合约：分成总服务费X分成比例）
//					inPileTotalIncomes = dailyIncomeTotalServiceFee.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);

            inPileTotalIncomes = totalIncomemap.get("totalIncome");

        }
		if(pmIncomemap != null && pmIncomemap.size() > 0){
            //服务费
//					BigDecimal dailyIncomeTotalServiceFee = pmIncomemap.get("dailyIncomeTotalServiceFee");
//					//分成比例
//					BigDecimal percentage = pmIncomemap.get("percentage");
//
//					//设备运维合约：分成总服务费x分成比例
//					inPilePmIncomes = dailyIncomeTotalServiceFee.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);
            inPilePmIncomes = pmIncomemap.get("totalIncome");

        }
		if(diviExpendmap != null && diviExpendmap.size() > 0){
            //服务费
//					BigDecimal dailyIncomeTotalServiceFee = diviExpendmap.get("dailyIncomeTotalServiceFee");
//					//分成比例
//					BigDecimal percentage = diviExpendmap.get("percentage");
//
//					//支出：分成总服务费x分成比例
//					diviExpends = dailyIncomeTotalServiceFee.multiply(percentage).setScale(2, BigDecimal.ROUND_HALF_UP);
            diviExpends = diviExpendmap.get("totalIncome");

        }

		map2.put("inPilePmIncome", inPilePmIncomes);
		map2.put("inPileTotalIncome", inPileTotalIncomes);
		map2.put("diviExpend", diviExpends);
		BigDecimal orderIncomes = BigDecimal.ZERO;//订单收入(非合约,场站订单充电收入)
		BigDecimal inFacRentalFee = BigDecimal.ZERO;//充电设施租赁费(固定收入合约)
		BigDecimal inParkRentalFee = BigDecimal.ZERO;//车位租赁费(固定收入合约)
		BigDecimal inLandRentalFee = BigDecimal.ZERO;//土地租赁费(固定收入合约)
		BigDecimal groupMonthly = BigDecimal.ZERO;//集团月结

		//集团月结场站收入
		if(groupMonthlyMap.get(key) != null){
            groupMonthly = groupMonthlyMap.get(key);
            map2.put("groupMonthly", groupMonthlyMap.get(key));
        }

		if(map2.get("orderIncome") != null){
            orderIncomes = new BigDecimal(map2.get("orderIncome").toString());
        }
		if(map2.get("inFacRentalFee") != null){
            inFacRentalFee = new BigDecimal(map2.get("inFacRentalFee").toString());
        }
		if(map2.get("inParkRentalFee") != null){
            inParkRentalFee = new BigDecimal(map2.get("inParkRentalFee").toString());
        }
		if(map2.get("inLandRentalFee") != null){
            inLandRentalFee = new BigDecimal(map2.get("inLandRentalFee").toString());
        }

		//固定收入小计
		if(BigDecimal.ZERO.equals(inPileTotalIncomes) && BigDecimal.ZERO.equals(inPilePmIncomes)
                && BigDecimal.ZERO.equals(inFacRentalFee) && BigDecimal.ZERO.equals(inParkRentalFee)
                && BigDecimal.ZERO.equals(inLandRentalFee) && BigDecimal.ZERO.equals(groupMonthly)){
            map2.put("fixedIncomeSubtotal", orderIncomes);
        }else{
            BigDecimal fixedIncomeSubtotal = inPileTotalIncomes.add(inPilePmIncomes).add(inFacRentalFee).add(inParkRentalFee).add(inLandRentalFee).add(groupMonthly);
            map2.put("fixedIncomeSubtotal", fixedIncomeSubtotal);
        }

		BigDecimal otherIncome = BigDecimal.ZERO;

		if(map2.get("otherIncome") != null){
            otherIncome = new BigDecimal(map2.get("otherIncome").toString());
        }
		//非固定收入小计
		map2.put("unfixedIncomeSubtotal", otherIncome);
		//场站收入合计
		BigDecimal stationIncomeSubtotal = otherIncome.add(new BigDecimal(map2.get("fixedIncomeSubtotal").toString()));
		map2.put("stationIncomeSubtotal", stationIncomeSubtotal);


		BigDecimal waterFee = BigDecimal.ZERO;//水费
		BigDecimal networkFee = BigDecimal.ZERO;//网费
		BigDecimal diviExpend = BigDecimal.ZERO;//分成支出(分成合约)
		BigDecimal exLandRentalFee = BigDecimal.ZERO;//土地租赁费(固定支出合约)
		BigDecimal exLandRentalExpend = BigDecimal.ZERO;//土地租赁分成费(固定支出合约)
		BigDecimal exHouseRentalFee = BigDecimal.ZERO;//房租租赁费(固定支出合约)
		BigDecimal propertyFee = BigDecimal.ZERO;//物管费
		BigDecimal artificialFee = BigDecimal.ZERO;//人工支出
		BigDecimal depreFixedAsset = BigDecimal.ZERO;//固定资产折旧
		BigDecimal costAmortization = BigDecimal.ZERO;//费用摊销
		BigDecimal parkingFee = BigDecimal.ZERO;//停车费
		BigDecimal eleFee = BigDecimal.ZERO;//电费

		if(map2.get("eleFee") != null){
            eleFee = new BigDecimal(map2.get("eleFee").toString());
        }
		if(map2.get("waterFee") != null){
            waterFee = new BigDecimal(map2.get("waterFee").toString());
        }
		if(map2.get("networkFee") != null){
            networkFee = new BigDecimal(map2.get("networkFee").toString());
        }
		if(map2.get("diviExpend") != null){
            diviExpend = new BigDecimal(map2.get("diviExpend").toString());
        }
		if(map2.get("exLandRentalFee") != null){
            exLandRentalFee = new BigDecimal(map2.get("exLandRentalFee").toString());
        }
		if(map2.get("exLandRentalExpend") != null){
            exLandRentalExpend = new BigDecimal(map2.get("exLandRentalExpend").toString());
        }
		if(map2.get("exHouseRentalFee") != null){
            exHouseRentalFee = new BigDecimal(map2.get("exHouseRentalFee").toString());
        }
		if(map2.get("propertyFee") != null){
            propertyFee = new BigDecimal(map2.get("propertyFee").toString());
        }
		if(map2.get("artificialFee") != null){
            artificialFee = new BigDecimal(map2.get("artificialFee").toString());
        }
		if(map2.get("depreFixedAsset") != null){
            depreFixedAsset = new BigDecimal(map2.get("depreFixedAsset").toString());
        }
		if(map2.get("costAmortization") != null){
            costAmortization = new BigDecimal(map2.get("costAmortization").toString());
        }
		if(map2.get("parkingFee") != null){
            parkingFee = new BigDecimal(map2.get("parkingFee").toString());
        }
		//固定支出小计
		BigDecimal fixedExpendSubtotal = eleFee.add(waterFee).add(networkFee)
                    .add(diviExpend).add(exLandRentalFee).add(exHouseRentalFee).add(exLandRentalExpend)
                    .add(propertyFee).add(artificialFee).add(depreFixedAsset)
                    .add(costAmortization).add(parkingFee);
		map2.put("fixedExpendSubtotal", fixedExpendSubtotal);

		BigDecimal repairFee = BigDecimal.ZERO;//维修费
		BigDecimal lowConsumable = BigDecimal.ZERO;//低值易耗品
		BigDecimal otherExpenses = BigDecimal.ZERO;//其他支出

		if(map2.get("repairFee") != null){
            repairFee = new BigDecimal(map2.get("repairFee").toString());
        }
		if(map2.get("lowConsumable") != null){
            lowConsumable = new BigDecimal(map2.get("lowConsumable").toString());
        }
		if(map2.get("otherExpenses") != null){
            otherExpenses = new BigDecimal(map2.get("otherExpenses").toString());
        }
		//非固定支出小计
		BigDecimal unfixedExpendSubtotal = repairFee.add(lowConsumable).add(otherExpenses);
		map2.put("unfixedExpendSubtotal", unfixedExpendSubtotal);
		//成本合计
		BigDecimal costTotal= fixedExpendSubtotal.add(unfixedExpendSubtotal);
		map2.put("costTotal", costTotal);
		//场站毛利润
		BigDecimal stationGrossProfit = stationIncomeSubtotal.subtract(costTotal);
		map2.put("stationGrossProfit", stationGrossProfit);
	}

	private void contractMap(List<Map> inPileTotalIncomeList,
			Map<Integer, Map<String, BigDecimal>> inPileTotalIncomeMap) {
		if(inPileTotalIncomeList != null && inPileTotalIncomeList.size() > 0){
			for (Map map : inPileTotalIncomeList) {
				if(map.get("stationId") != null){
					Integer stationId = Integer.parseInt(map.get("stationId").toString());
					//服务费
					//BigDecimal dailyIncomeTotalServiceFee = BigDecimal.ZERO;
					//分成比例
					//BigDecimal percentage = BigDecimal.ZERO;
					Map<String,BigDecimal> map2 = new HashMap();
					BigDecimal totalIncome = BigDecimal.ZERO;
					if(map.get("totalIncome") != null){
						totalIncome = new BigDecimal(map.get("totalIncome").toString());
					}
					map2.put("totalIncome", totalIncome);
					inPileTotalIncomeMap.put(stationId, map2);
//					if(inPileTotalIncomeMap.get(stationId) != null && inPileTotalIncomeMap.get(stationId).size() > 0){
//						Map map2 = inPileTotalIncomeMap.get(stationId);
//						BigDecimal oldTotalServiceFee = (BigDecimal) map2.get("dailyIncomeTotalServiceFee");
//						if(map.get("dailyIncomeTotalServiceFee") != null){
//							dailyIncomeTotalServiceFee = new BigDecimal(map.get("dailyIncomeTotalServiceFee").toString());
//						}
//						
//						map2.put("dailyIncomeTotalServiceFee", dailyIncomeTotalServiceFee.add(oldTotalServiceFee));
//						
//					}else{
//						Map<String,BigDecimal> map2 = new HashMap();
//						if(map.get("dailyIncomeTotalServiceFee") != null){
//							dailyIncomeTotalServiceFee = new BigDecimal(map.get("dailyIncomeTotalServiceFee").toString());
//						}
//						if(map.get("percentage") != null){
//							percentage = new BigDecimal(map.get("percentage").toString());
//						}
//						map2.put("dailyIncomeTotalServiceFee", dailyIncomeTotalServiceFee);
//						map2.put("percentage", percentage);
//						inPileTotalIncomeMap.put(stationId, map2);
//					}
				}
			}
		}
	}

	private Map init(String settlementMonth) {
		Map initMap = new HashMap();
		initMap.put("settlementMonth",settlementMonth);
		initMap.put("stationTotalPower", INIT_ZERO);
		initMap.put("stationTargetCharge", INIT_ZERO);
		initMap.put("stationTargetChargeCompare", INIT_ZERO);
		initMap.put("stationTotalCharge", INIT_ZERO);
		initMap.put("stationTotalChargeCompare", INIT_ZERO);
		initMap.put("stationGoalCharge", INIT_ZERO);
		initMap.put("stationTestCharge", INIT_ZERO);
		initMap.put("stationUseCharge", INIT_ZERO);
		initMap.put("stationOfficeCharge", INIT_ZERO);
		initMap.put("pileTotalLoss", INIT_ZERO);
		initMap.put("pileTotalPower", INIT_ZERO);
		initMap.put("stationTargetIncome", INIT_ZERO);
		initMap.put("stationTargetIncomeCompare", INIT_ZERO);
		initMap.put("orderIncome", INIT_ZERO);
		initMap.put("inPileTotalIncome", INIT_ZERO);
		initMap.put("inPilePmIncome", INIT_ZERO);
		initMap.put("inFacRentalFee", INIT_ZERO);
		initMap.put("inParkRentalFee", INIT_ZERO);
		initMap.put("inLandRentalFee", INIT_ZERO);
		initMap.put("fixedIncomeSubtotal", INIT_ZERO);
		initMap.put("fixedIncomeSubtotalCompare", INIT_ZERO);
		initMap.put("otherIncome", INIT_ZERO);
		initMap.put("unfixedIncomeSubtotal", INIT_ZERO);
		initMap.put("unfixedIncomeSubtotalCompare", INIT_ZERO);
		initMap.put("stationIncomeSubtotal", INIT_ZERO);
		initMap.put("stationIncomeSubtotalCompare", INIT_ZERO);
		initMap.put("eleFee", INIT_ZERO);
		initMap.put("waterFee", INIT_ZERO);
		initMap.put("networkFee", INIT_ZERO);
		initMap.put("diviExpend", INIT_ZERO);
		initMap.put("exLandRentalFee", INIT_ZERO);
		initMap.put("exLandRentalExpend", INIT_ZERO);
		initMap.put("exHouseRentalFee", INIT_ZERO);
		initMap.put("propertyFee", INIT_ZERO);//物管费
		initMap.put("artificialFee", INIT_ZERO);//人工费
		initMap.put("depreFixedAsset", INIT_ZERO);
		initMap.put("costAmortization", INIT_ZERO);//费用摊销
		initMap.put("parkingFee", INIT_ZERO);//停车费
		initMap.put("fixedExpendSubtotal", INIT_ZERO);
		initMap.put("fixedExpendSubtotalCompare", INIT_ZERO);
		initMap.put("repairFee", INIT_ZERO);//维修费
		initMap.put("lowConsumable", INIT_ZERO);//低值易耗品
		initMap.put("otherExpenses", INIT_ZERO);//其他支出
		initMap.put("unfixedExpendSubtotal", INIT_ZERO);
		initMap.put("unfixedExpendSubtotalCompare", INIT_ZERO);
		initMap.put("costTotal", INIT_ZERO);
		initMap.put("costTotalCompare", INIT_ZERO);
		initMap.put("stationGrossProfit", INIT_ZERO);
		initMap.put("stationGrossProfitCompare", INIT_ZERO);
		initMap.put("groupMonthly", INIT_ZERO);
		return initMap;
	}

}

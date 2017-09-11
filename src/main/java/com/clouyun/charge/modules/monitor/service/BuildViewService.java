package com.clouyun.charge.modules.monitor.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.modules.spring.web.BaseService;
import com.clouyun.charge.common.constant.MonitorConstants;
import com.clouyun.charge.common.constant.MonitorConstants.OrtMode;
import com.clouyun.charge.common.constant.MonitorConstants.UserRoperty;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.modules.monitor.mapper.BuildViewMapper;
import com.clouyun.charge.modules.system.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 
 * 描述: 建设总览
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui 版本: 2.0.0
 * 创建日期:2017年6月21日
 */
@Service
@SuppressWarnings("rawtypes")
public class BuildViewService extends BaseService {

	private static final Integer CDW_ORGID = 24;// 车电网orgId，条件限制
	private static final Integer ZDLY_ORGID = 47;// 中电绿源
	private static final Integer DST_ORGID = 58;// 地上铁
	@Autowired
	private BuildViewMapper buildViewMapper;
	@Autowired
	private UserService userService;

	/**
	 * 场站接入指标
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月21日
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getStatAcceInd(Map map) throws BizException {
		Map<String, Object> result = new HashMap();
		Map<String, Integer> selfMap = new HashMap();
		Map<String, Integer> joinMap = new HashMap();
		Map<String, Integer> aloneMap = new HashMap();
		int acceIndSum = 1000;
		int acceSum = 0;// 接入场站总数
		int selfSum = 0;// 自营的场站数
		int joinSum = 0;// 加盟的场站
		int aloneSum = 0;// 独立运营
		int selfFastSum = 0;//自营的快充场站数
		int selfSlowSum = 0;//自营的慢充场站数
		int joinFastSum = 0;
		int joinSlowSum = 0;
		int aloneFastSum = 0;
		int aloneSlowSum = 0;
		// 获取有效的场站列表
		List<DataVo> staList = buildViewMapper.getStations();
		if (CollectionUtils.isNotEmpty(staList)) {
			List<DataVo> pileList = buildViewMapper.getPilesCount();
			Map<String, Map<String, Double>> speed = new HashMap();
			if (CollectionUtils.isNotEmpty(pileList)) {
				getPileModes(pileList,speed);//获取桩的交流和直流数
			}
			List<DataVo> joinOrg = buildViewMapper.getJoinOrg();//查询合作的运营商
			for (DataVo staVo : staList) {
				acceSum ++;
				String stationId = staVo.getString("stationId");
				Map<String, Double> model = speed.get(stationId);
				int orgId = staVo.getInt("orgId");
				boolean flag = getIsModelStation(model);//是否是快充场站
				if (CDW_ORGID == orgId) {// orgId=24的是属于车电网的场站
					if (flag) {
						selfFastSum++;
					} else {
						selfSlowSum++;
					}
					selfSum++;
				} else if (isContainOrgId(joinOrg, orgId)) {//是否包含此企业
					if (flag) {
						joinFastSum++;
					} else {
						joinSlowSum++;
					}
					joinSum++;
				} else {
					if (flag) {
						aloneFastSum++;
					} else {
						aloneSlowSum++;
					}
					aloneSum++;
				}
			}
		}
		selfMap.put("selfFastSum", selfFastSum);//自营快充
		selfMap.put("selfSlowSum", selfSlowSum);//自营慢充
		joinMap.put("joinFastSum", joinFastSum);//加盟快充
		joinMap.put("joinSlowSum", joinSlowSum);//加盟慢充
		aloneMap.put("aloneFastSum", aloneFastSum);//独立运营快充
		aloneMap.put("aloneSlowSum", aloneSlowSum);//独立运营慢充
		int toSum = buildViewMapper.getToStationCount();// 2、获取第三方场站 (互联互通场站数)
		acceSum += toSum;
		selfMap.put("selfSum", selfSum);
		joinMap.put("joinSum", joinSum);
		aloneMap.put("aloneSum", aloneSum);
		result.put("acceSum", acceSum);
		result.put("self", selfMap);
		result.put("join", joinMap);
		result.put("alone", aloneMap);
		result.put("toSum", toSum);
		result.put("acceIndSum", acceIndSum);
		selfMap = null;
		joinMap = null;
		aloneMap = null;
		return result;
	}

	/**
	 * 获取桩的交流和直流数
	 * @return
	 */
    private void getPileModes(List<DataVo> pileList,Map<String, Map<String, Double>> speed){
		for (DataVo pileVo : pileList) {
			String stationId = pileVo.getString("stationId");
			String ortMode = pileVo.getString("ortMode");
			Double sum = pileVo.getDouble("sum");
			if (!speed.containsKey(stationId)) {
				Map<String, Double> temp = new HashMap();
				temp.put("ac", 0D);// 交流
				temp.put("dc", 0D);// 直流
				if (OrtMode.AC.getCode().equals(ortMode)) {//交流桩
					temp.put("ac", sum);// 交流
				} else {
					temp.put("dc", sum);// 直流 ---交直模式的统计为直流
				}
				speed.put(stationId, temp);
			} else {
				Map<String, Double> temp = speed.get(stationId);
				if (OrtMode.AC.getCode().equals(ortMode)) {
					temp.put("ac", sum);
				} else {
					temp.put("dc", sum);
				}
			}
		}
	}

	/**
	 * 判断场站是否是快充场站
	 * @param model
	 * @return
	 */
	private boolean getIsModelStation(Map<String, Double> model){
		boolean flag = true;// 快
		//Map<String, Double> model = speed.get(stationId);
		//int orgId = staVo.getInt("orgId");
		if (null != model) {// 判断场站是快充场站还是慢充场站
			if (model.get("dc") == 0) {
				flag = false;
			} else {
				if (model.get("dc") / (model.get("dc") + model.get("ac")) <= 0.5) {//
					flag = false;
				}
			}
		} else {// 场站没有枪的情况视为慢充站
			flag = false;
		}
    	return  flag;
	}

	/**
	 * 桩接入指标
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月21日
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPileAcceInd(Map map) throws BizException {
		
		Map<String, Object> result = new HashMap();
		Map<String, Integer> selfMap = new HashMap();
		Map<String, Integer> joinMap = new HashMap();
		Map<String, Integer> aloneMap = new HashMap();

		int acceSum = 0;// 接入桩总数
		int acceIndSum = 10000;
		int selfSum = 0;// 自营
		int selfDcSum = 0;
		int selfFastSum = 0;
		int selfSlowSum = 0;

		int joinSum = 0;// 加盟
		int joinDcSum = 0;
		int joinFastSum = 0;
		int joinSlowSum = 0;

		int aloneSum = 0;// 独立运营
		int aloneDcSum = 0;//直流
		int aloneFastSum = 0;
		int aloneSlowSum = 0;

		List<DataVo> pileList = buildViewMapper.getPiles();
		if (CollectionUtils.isNotEmpty(pileList)) {
			List<DataVo> joinOrg = buildViewMapper.getJoinOrg();//查询合作的运营商
			for (DataVo pileVo : pileList) {
				acceSum ++;
				String ortMode = pileVo.getString("ortMode");
				Double ratePower = pileVo.getDouble("ratePower");
				int orgId = pileVo.getInt("orgId");
				if (pileVo.getInt("orgId") == CDW_ORGID) {
					selfSum++;
					if (OrtMode.AC.getCode().equals(ortMode)) {// 交
						if (ratePower >= 40) {
							selfFastSum++;
						} else {
							selfSlowSum++;
						}
					} else {// 直
						selfDcSum++;
					}
				} else if (isContainOrgId(joinOrg, orgId)) {
					joinSum++;
					if (OrtMode.AC.getCode().equals(ortMode)) {// 交
						if (ratePower >= 40) {
							joinFastSum++;
						} else {
							joinSlowSum++;
						}
					} else {// 直
						joinDcSum++;
					}
				} else {
					aloneSum++;
					if (OrtMode.AC.getCode().equals(ortMode)) {// 交
						if (ratePower >= 40) {
							aloneFastSum++;
						} else {
							aloneSlowSum++;
						}
					} else {// 直
						aloneDcSum++;
					}
				}
			}
		}
		// 第三方充电桩
		int toSum = buildViewMapper.getToPiles();
		acceSum += toSum;
		selfMap.put("selfFastSum", selfFastSum);//自营快充
		selfMap.put("selfSlowSum", selfSlowSum);//自营慢充
		selfMap.put("selfDcSum", selfDcSum);//自营直流
		joinMap.put("joinFastSum", joinFastSum);
		joinMap.put("joinSlowSum", joinSlowSum);
		joinMap.put("joinDcSum", joinDcSum);
		aloneMap.put("aloneFastSum", aloneFastSum);
		aloneMap.put("aloneSlowSum", aloneSlowSum);
		aloneMap.put("aloneDcSum", aloneDcSum);
		selfMap.put("selfSum", selfSum);
		joinMap.put("joinSum", joinSum);
		aloneMap.put("aloneSum", aloneSum);
		result.put("acceSum", acceSum);
		result.put("self", selfMap);
		result.put("join", joinMap);
		result.put("alone", aloneMap);
		result.put("toSum", toSum);
		result.put("acceIndSum", acceIndSum);
		selfMap = null;
		joinMap = null;
		aloneMap = null;
		return result;
	}

	/**
	 * 场站分布指标 按省
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月21日
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDistInd(Map map) throws BizException {
		DataVo params = new DataVo(map);
		if (params.isNotBlank("cityQty")) {
			map.put("cityQty", params.getInt("cityQty"));
		}
		if (params.isNotBlank("provQty")) {
			map.put("provQty", params.getInt("provQty"));
		}
		Map<String, Object> result = new HashMap();
		Map<String, Integer> provPileMap = new HashMap();
		List<DataVo> provStatList = buildViewMapper.getStationByProv(map);
		if (CollectionUtils.isNotEmpty(provStatList)) {
			List<Map<String, Object>> tempList = new ArrayList();
			getProvMap(provPileMap,provStatList,tempList);
			result.put("prov", tempList);
			result.put("provTotal", tempList.size());
		}
		Map<String, Integer> cityPileMap = new HashMap();
		List<DataVo> cityStatList = buildViewMapper.getStationByCity(map);
		if (CollectionUtils.isNotEmpty(cityStatList)) {
			List<Map<String, Object>> tempList = new ArrayList();
			getCityMap(cityPileMap,cityStatList,tempList);
			result.put("city", tempList);
			result.put("cityTotal", tempList.size());
		}
		return result;
	}

	/**
	 * 获取省下的场站和桩信息
	 * @param provPileMap
	 * @param provStatList
	 * @param tempList
	 */
	private void  getProvMap(Map<String, Integer> provPileMap,List<DataVo> provStatList,List<Map<String, Object>> tempList){
		List<DataVo> pileList = buildViewMapper.getPileByProv();//查询省下的桩信息
		if(CollectionUtils.isNotEmpty(pileList)){
			for (DataVo pileVo : pileList) {
				String provCode = pileVo.getString("provCode");
				int sum = pileVo.getInt("sum");
				provPileMap.put(provCode,sum);
			}
		}
		for (DataVo statVo : provStatList) {
			Map<String, Object> tempMap = new HashMap();
			tempMap.put("stationSum", statVo.getInt("sum"));
			String provCode = statVo.getString("provCode");
			String areaName = statVo.getString("areaName");
			tempMap.put("pileSum", 0);
			if (null != provPileMap.get(provCode)) {
				tempMap.put("pileSum", provPileMap.get(provCode));
			}
			tempMap.put("provName",areaName);
			tempList.add(tempMap);
		}
	}

	/**
	 * 获取城市下的场站和桩信息
	 * @param cityPileMap
	 * @param cityStatList
	 * @param tempList
	 */
	private void  getCityMap(Map<String, Integer> cityPileMap,List<DataVo> cityStatList,List<Map<String, Object>> tempList){
		Map<String, String> areaMap = new HashMap();
		List<DataVo> areaList = buildViewMapper.getAreaProv();
		if (CollectionUtils.isNotEmpty(areaList)) {
			for (DataVo areaVo : areaList) {
				String areaNo = areaVo.getString("areaNo");
				String areaName = areaVo.getString("areaName");
				if (!areaMap.containsKey(areaNo)) {
					areaMap.put(areaNo,areaName);
				}
			}
		}
		List<DataVo> pileList = buildViewMapper.getPileByCity();
		if (CollectionUtils.isNotEmpty(pileList)){
			for (DataVo pileVo : pileList) {
				String cityCode = pileVo.getString("cityCode");
				int sum = pileVo.getInt("sum");
				cityPileMap.put(cityCode,sum);
			}
		}
		for (DataVo statVo : cityStatList) {
			Map<String, Object> tempMap = new HashMap();
			tempMap.put("stationSum", statVo.getInt("sum"));
			String cityCode = statVo.getString("cityCode");
			String areaName = statVo.getString("areaName");
			String pAreaNo = statVo.getString("pAreaNo");
			tempMap.put("pileSum", 0);
			if (null != cityPileMap.get(cityCode)) {
				tempMap.put("pileSum", cityPileMap.get(cityCode));
			}
			if ("市辖区".endsWith(areaName)) {
				tempMap.put("cityName",areaMap.get(pAreaNo) + "市");
			} else {
				tempMap.put("cityName", areaName);
			}
			tempList.add(tempMap);
		}
	}

	/**
	 * 建设速度
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月21日
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, Object>> getBuildSpeed(Map map) throws BizException {
		// 获取当前时间
		DataVo parmas = new DataVo(map);
		int scale = 0;
		if (parmas.isNotBlank("scale")) {
			if (parmas.getInt("scale") > 0) {
				throw new BizException(1801001);
			}
			scale = parmas.getInt("scale");
		}
		List<Map<String, Object>> result = new ArrayList();
		Calendar cal = Calendar.getInstance();// 0
		cal.add(Calendar.MONTH, scale);
		String endTime = CalendarUtils.formatCalendar(cal,"yyyy-MM-dd");
		map.put("endTime", endTime);
		cal.add(Calendar.MONTH, -11);
		String startTime = CalendarUtils.formatCalendar(cal,"yyyy-MM-dd");
		map.put("startTime", startTime.substring(0, 7) + "-01");
		List<DataVo> statList = buildViewMapper.getStationByTime(map);// 场站
		List<DataVo> toStatList = buildViewMapper.getToStationByTime(map);// 互联互通（第三方场站)--根据同步时间进行统计
		List<DataVo> pileList = buildViewMapper.getPileByTime(map);
		List<DataVo> toPileList = buildViewMapper.getToPileByTime(map);// 第三方充电桩
		Map<String, Integer> statMap = convertToMap(statList);// 月份时间-场站数量
		statList = null;
		Map<String, Integer> toStatMap = convertToMap(toStatList);// 月份时间-场站数量
		toStatList = null;
		Map<String, Map<String, Integer>> pileMap = new HashMap();
		Map<String, Integer> toPileMap = convertToMap(toPileList);
		toPileList = null;
		//获取自营,加盟,自运营桩数
		getPileInfo(pileList, pileMap, toPileMap);
		pileList = null;
		for (int j = -11; j <= 0; j++) {
			Calendar tempCal = Calendar.getInstance();// 0
			Map<String, Object> speed = new HashMap();
			Map<String, Object> temp = new HashMap();
			temp.put("stationSum", 0);
			tempCal.add(Calendar.MONTH, scale + j);
			String time = CalendarUtils.formatCalendar(tempCal,"yyyy-MM");
			speed.put("time", time.substring(0, 7));
			if (null != statMap.get(time)) {
				if (null != toStatMap.get(time)) {
					temp.put("stationSum",statMap.get(time) + toStatMap.get(time));
				} else {
					temp.put("stationSum", statMap.get(time));
				}
			} else {
				if (null != toStatMap.get(time)) {
					temp.put("stationSum", toStatMap.get(time));
				}
			}
			//返回桩树结果集
			putPileNumResult(pileMap, temp, time);
			speed.put("speed", temp);
			result.add(speed);
		}
		statMap = null;
		toStatMap = null;
		toPileMap = null;
		pileMap = null;
		parmas = null;
		return result;
	}

	/**
	 * 建设总览下的建设速度  日期:2017年9月05日
	 * 		新增:1.预算金额
	 * 			2.投资金额
	 * 			3.移除场站数
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, Object>> queryBuildSpeed(Map map) throws BizException {
		// 获取当前时间
		DataVo parmas = new DataVo(map);
		int scale = 0;
		if (parmas.isNotBlank("scale")) {
			if (parmas.getInt("scale") > 0) {
				throw new BizException(1801001);
			}
			scale = parmas.getInt("scale");
		}
		List<Map<String, Object>> result = new ArrayList();
		Calendar cal = Calendar.getInstance();// 0
		cal.add(Calendar.MONTH, scale);
		String endTime = CalendarUtils.formatCalendar(cal,"yyyy-MM-dd");
		map.put("endTime", endTime);
		cal.add(Calendar.MONTH, -11);
		String startTime = CalendarUtils.formatCalendar(cal,"yyyy-MM-dd");
		map.put("startTime", startTime.substring(0, 7) + "-01");
		List<DataVo> statList = buildViewMapper.getStationByTime(map);// 场站
		List<DataVo> toStatList = buildViewMapper.getToStationByTime(map);// 互联互通（第三方场站)--根据同步时间进行统计
		List<DataVo> pileList = buildViewMapper.getPileByTime(map);
		List<DataVo> toPileList = buildViewMapper.getToPileByTime(map);// 第三方充电桩
		Map<String, DataVo> statMap = convertMapToMap(statList);// 月份时间-场站数量
		statList = null;
		//Map<String, Integer> toStatMap = convertToMap(toStatList);// 月份时间-场站数量
		toStatList = null;
		Map<String, Map<String, Integer>> pileMap = new HashMap();
		Map<String, Integer> toPileMap = convertToMap(toPileList);
		toPileList = null;
		//获取自营,加盟,自运营桩数
		getPileInfo(pileList, pileMap, toPileMap);
		pileList = null;
		for (int j = -11; j <= 0; j++) {
			Calendar tempCal = Calendar.getInstance();// 0
			Map<String, Object> speed = new HashMap();
			Map<String, Object> temp = new HashMap();
			tempCal.add(Calendar.MONTH, scale + j);
			String time = CalendarUtils.formatCalendar(tempCal,"yyyy-MM");
			speed.put("time", time.substring(0, 7));
			if (null != statMap.get(time)) {
				DataVo dataVo = statMap.get(time);
				BigDecimal investAmount = BigDecimal.ZERO;
				BigDecimal budgetAmount = BigDecimal.ZERO;
				if (dataVo.isNotBlank("investAmount")){
					investAmount = new BigDecimal(dataVo.getString("investAmount"));
				}
				if (dataVo.isNotBlank("investAmount")){
					budgetAmount = new BigDecimal(dataVo.getString("budgetAmount"));
				}
				//返回的是万元
				temp.put("investAmount", investAmount.divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP));//投资金额
				temp.put("budgetAmount", budgetAmount.divide(new BigDecimal(10000),2,BigDecimal.ROUND_HALF_UP));//预算金额
			} else {
				temp.put("investAmount", "0.00");
				temp.put("budgetAmount", "0.00");
			}
			putPileNumResult(pileMap, temp, time);
			speed.put("speed", temp);
			result.add(speed);
		}
		statMap = null;
		toPileMap = null;
		pileMap = null;
		parmas = null;
		return result;
	}

	//返回自营,加盟,自运营,第三方桩
	private void putPileNumResult(Map<String, Map<String, Integer>> pileMap, Map<String, Object> temp, String time) {
		if (null != pileMap.get(time)) {
            Map<String, Integer> tempMap = pileMap.get(time);
            temp.put("self",   tempMap.get("self"));
            temp.put("join",   tempMap.get("join"));
            temp.put("alone",  tempMap.get("alone"));
            temp.put("toPile", tempMap.get("toPile"));
        } else {
            temp.put("self", 0);
            temp.put("join", 0);
            temp.put("alone", 0);
            temp.put("toPile", 0);
        }
	}

	//获取自营,加盟,自运营桩数
	private void getPileInfo(List<DataVo> pileList, Map<String, Map<String, Integer>> pileMap, Map<String, Integer> toPileMap) {
		if (CollectionUtils.isNotEmpty(pileList)) {
			List<DataVo> joinOrg = buildViewMapper.getJoinOrg();
			for (DataVo pileVo : pileList) {
				Map<String, Integer> temp = new HashMap();
				String time = pileVo.getString("time");
				int orgId = pileVo.getInt("orgId");
				if (!pileMap.containsKey(time)) {
					temp.put("self", 0);
					temp.put("join", 0);
					temp.put("alone", 0);
					if (CDW_ORGID == orgId) {
						temp.put("self", 1);
					} else if (isContainOrgId(joinOrg, orgId)) {
						temp.put("join", 1);
					} else {
						temp.put("alone", 1);
					}
					pileMap.put(time, temp);
				} else {
					Map<String, Integer> tempMap = pileMap.get(time);
					if (orgId == CDW_ORGID) {
						int self = tempMap.get("self") + 1;
						tempMap.put("self", self);
					} else if (isContainOrgId(joinOrg, orgId)) {
						int join = tempMap.get("join") + 1;
						tempMap.put("join", join);
					} else {
						int alone = tempMap.get("alone") + 1;
						tempMap.put("alone", alone);
					}
				}
				Map<String, Integer> tempPileMap = pileMap.get(time);
				if (null != toPileMap) {
					tempPileMap.put("toPile", toPileMap.get(time) == null ? 0: toPileMap.get(time));
				} else {
					tempPileMap.put("toPile", 0);
				}
			}
			joinOrg = null;
		}
	}


	/**
	 * 场站建设速度
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月21日
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, Object>> getStationBuildSpeed(Map map)throws BizException {
		DataVo params = new DataVo(map);
		if (params.isBlank("userId")) {
			throw new BizException(1803000, "用户id");
		}
		Set<Integer> stationIds = userService.getUserRoleDataById(
				params.getInt("userId"), RoleDataEnum.STATION.dataType);
		if (CollectionUtils.isNotEmpty(stationIds)) {
			map.put("stationIds", stationIds);
		}
		List<Map<String, Object>> result = new ArrayList();
		Calendar cal = Calendar.getInstance();// 0
		String endTime = CalendarUtils.formatCalendar(cal, "yyyy-MM-dd");
		map.put("endTime", endTime);
		cal.add(Calendar.MONTH, -4);
		String startTime = CalendarUtils.formatCalendar(cal, "yyyy-MM-dd");
		map.put("startTime", startTime.substring(0, 7) + "-01");
		List<DataVo> statList = buildViewMapper.getStationByTime(map);// 场站
		List<DataVo> toStatList = buildViewMapper.getToStationByTime(map);// 互联互通（第三方场站)--根据同步时间进行统计
		Map<String, Integer> statMap = convertToMap(statList);
		Map<String, Integer> toStatMap = convertToMap(toStatList);
		Map<String, Object> station = new HashMap();
		List<Map<String, Object>> tempList = new ArrayList();
		int sum = 0;
		for (int j = -4; j <= 0; j++) {
			Calendar tempCal = Calendar.getInstance();
			tempCal.add(Calendar.MONTH, j);
			Map<String, Object> temp = new HashMap();
			String time = CalendarUtils.formatCalendar(tempCal, "yyyy-MM");
			temp.put("time", time.substring(0, 7));
			if (null != statMap.get(time)) {
				if (null != toStatMap.get(time)) {
					sum += statMap.get(time) + toStatMap.get(time);
					temp.put("stationSum",statMap.get(time) + toStatMap.get(time));
				} else {
					sum += statMap.get(time);
					temp.put("stationSum", statMap.get(time));
				}
			} else {
				if (null != toStatMap.get(time)) {
					sum += toStatMap.get(time);
					temp.put("stationSum", toStatMap.get(time));
				} else {
					temp.put("stationSum", 0);
				}
			}
			tempList.add(temp);
		}
		station.put("sum", sum);
		station.put("speed", tempList);
		result.add(station);
		statList = null;
		toStatList = null;
		station = null;
		tempList = null;
		params = null;
		return result;
	}

	/**
	 * 营运车辆指标
	 * 版本:2.0.0
	 * 作者:gaohui
	 * 日期:2017年6月21日
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOperatingVehicleInd(Map map)throws BizException {
		int dstVehSum = 0;// 地上铁接入车辆数
		int zdlyVehSum = 0;// 中电绿源接入车辆数
		int vehicleSum = 0;// 车辆接入总数 = 地上铁接入车辆数 + 中电绿源接入车辆数
		Double dstMileSum = 0D;// 地上铁接入车辆月行驶里程
		Double zdlyMileSum = 0D;// 中电绿源接入车辆月行驶里程
		int dstTarget = 0; // 地上铁补贴目标完成率
		int zdlyTarget = 0; // 中电绿源补贴目标完成率
		int acceSum = 10000;// 车辆接入指标总数
		Map<String, Object> result = new HashMap();
		DataVo params = new DataVo(map);
		String month = "";
		if (params.isNotBlank("conDate")) {
			month = params.getString("conDate");
		} else {
			Calendar calendar = Calendar.getInstance();// 默认查询当月的数据
			month = CalendarUtils.formatCalendar(calendar, "yyyy-MM");
		}
		map.put("month", month);
		List<DataVo> vehicleList = buildViewMapper.getVehiclesByOrgIds();// 获取中电绿源和地上跌的车辆
		if (null != vehicleList && vehicleList.size() > 0) {
			for (DataVo vehicleVo : vehicleList) {
				int sum = vehicleVo.getInt("sum");
				int orgId = vehicleVo.getInt("orgId");
				vehicleSum += sum;
				if (ZDLY_ORGID == orgId) {// 中电绿源
					zdlyVehSum = sum;
				} else if (DST_ORGID == orgId) {// 地上铁
					dstVehSum = sum;
				}
			}
		}
		vehicleList = null;
		List<DataVo> mileageList = buildViewMapper.getMileSum(map);//
		if (null != mileageList && mileageList.size() > 0) {
			for (DataVo mileageVo : mileageList) {
				Double mileageSum = mileageVo.getDouble("mileageSum");
				int orgId = mileageVo.getInt("orgId");
				if (ZDLY_ORGID == orgId) {// 中电绿源
					zdlyMileSum = mileageSum;
				} else if (DST_ORGID == orgId) {// 地上铁
					dstMileSum = mileageSum;
				}
			}
		}
		mileageList = null;// 置空
		if (dstVehSum == 0) {
			dstTarget = 0;
		} else {
			dstTarget = (int) ((dstMileSum * 12 / (30000 * dstVehSum)) * 100);// 地上铁补贴完成率
		}
		if (zdlyVehSum == 0) {
			zdlyTarget = 0;
		} else {
			zdlyTarget = (int) ((zdlyMileSum * 12 / (30000 * zdlyVehSum)) * 100);// 中电绿源补贴完成率
		}
		result.put("acceSum", acceSum);// 车辆接入指标总数
		result.put("vehicleSum", vehicleSum);// 地上铁车辆总数 ＋中电绿源车辆总数
		result.put("dsdVehSum", dstVehSum);// 地上铁车辆总数
		result.put("zdlyVehSum", zdlyVehSum);// 中电绿源车辆总数
		result.put("dsdMileSum", dstMileSum);// 地上铁行驶里程数
		result.put("zdlyMileSum", zdlyMileSum);// 中电绿源行驶里程数
		result.put("dsdTarget", dstTarget);// 地上铁补贴完成率
		result.put("zdlyTarget", zdlyTarget);// 中电绿源补贴完成率
		return result;
	}

	/**
	 * 场站接入指标 版本:2.0.0 作者:gaohui 日期:2017年6月21日
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getStatInd(Map map) throws BizException {
		Map<String, Object> result = new HashMap();
		int acceSum = 0;// 接入场站总数
		int selfSum = 0;// 自营的场站数
		int joinSum = 0;// 加盟的场站
		int aloneSum = 0;// 独立运营
		List<DataVo> staList = buildViewMapper.getStations();
		if (null != staList && staList.size() > 0) {
			acceSum += staList.size();
			List<DataVo> joinOrg = buildViewMapper.getJoinOrg();
			for (Map sta : staList) {
				DataVo staVo = new DataVo(sta);
				int orgId = staVo.getInt("orgId");
				if (CDW_ORGID == orgId) {// orgId=24的是属于车电网的场站
					selfSum++;
				} else if (isContainOrgId(joinOrg, orgId)) {
					joinSum++;
				} else {
					aloneSum++;
				}
			}
			joinOrg = null;
		}
		staList = null;
		int toSum = buildViewMapper.getToStationCount();
		acceSum += toSum;
		result.put("acceSum", acceSum);
		result.put("selfSum", selfSum);
		result.put("joinSum", joinSum);
		result.put("aloneSum", aloneSum);
		result.put("toSum", toSum);
		return result;
	}

	/**
	 * 版本:2.0.0 作者:gaohui 日期:2017年7月6日
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getServiceVehicleInd(Map map)throws BizException {
		int month = 0;
		int busSum = 0; // 公交总数
		int comSum = 0; // 通勤总数
		int phySum = 0; // 物流总数
		int socSum = 0; // 社会车辆
		Double powerSum = 0.00; // 本月总充电量
		int serviceSum = 0;// 本月服务次数
		Map<String, Object> result = new HashMap();//
		Calendar cal = Calendar.getInstance();// 0
		cal.add(Calendar.MONTH, month);
		String endTime = CalendarUtils.formatCalendar(cal, "yyyy-MM-dd");
		map.put("endTime", endTime + " 23:59:59");
		map.put("startTime", endTime.substring(0, 7) + "-01 00:00:00");
		DataVo bileSums = buildViewMapper.getBillPayCountAndSum(map);
		DataVo toBillSums = buildViewMapper.getToOrderAndSum(map);
		if (null != bileSums) {
			powerSum += bileSums.getDouble("powerSum");
			serviceSum += bileSums.getInt("serviceSum");
		}
		if (null != toBillSums) {
			powerSum += toBillSums.getDouble("powerSum");
			serviceSum += toBillSums.getInt("serviceSum");
		}
		List<DataVo> vehicles = buildViewMapper.getBillPayVehicles(map);
		if (CollectionUtils.isNotEmpty(vehicles)) {
			for (DataVo vehicleVo : vehicles) {
				String using = vehicleVo.getString("usingRoperty");
				String belongsType = vehicleVo.getString("belongsType");
				if (belongsType.equals(MonitorConstants.BelongsType.SOL.getCode())) {
					socSum++;
				}
				if (UserRoperty.BUS.getCode().equals(using)) {// 公交
					busSum++;
				} else if (UserRoperty.PHY.getCode().equals(using)) {// 物流
					phySum++;
				} else if (UserRoperty.COM.getCode().equals(using)) {// 通勤
					comSum++;
				}
			}

		}
		vehicles = null;
		result.put("busSum", busSum);//公交
		result.put("comSum", comSum);//通勤
		result.put("phySum", phySum);//物流
		result.put("socSum", socSum);//社会
		result.put("powerSum", Math.rint(powerSum / 1000));// ---单位MKW
		result.put("serviceSum", serviceSum);//服务次数
		bileSums = null;
		toBillSums = null;
		return result;
	}

	private Map convertToMap(List<DataVo> list) {
		Map<String, Integer> statTemp = new HashMap();
		if (null != list && list.size() > 0) {
			for (DataVo statVo : list) {
				String time = statVo.getString("time");
				int sum = statVo.getInt("sum");
				if (!statTemp.containsKey(time)) {
					statTemp.put(time,sum);
				}
			}
		}
		return statTemp;
	}

	private Map<String,DataVo> convertMapToMap(List<DataVo> list) {
		Map<String, DataVo> statTemp = new HashMap();
		if (null != list && list.size() > 0) {
			for (DataVo statVo : list) {
				String time = statVo.getString("time");
				if (!statTemp.containsKey(time)) {
					statTemp.put(time,statVo);
				}
			}
		}
		return statTemp;
	}

	/**
	 * 是否包含此企业
	 * @param list
	 * @param orgId
	 * @return
	 */
	private boolean isContainOrgId(List<DataVo> list, Integer orgId) {
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(list)) {
			for (DataVo vo : list) {
				if (orgId == vo.getInt("orgId")) {
					flag = true;
					continue;
				}
			}
		}
		return flag;
	}
}

package com.clouyun.charge.modules.charge.service;

import java.text.DecimalFormat;
import java.util.*;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.cdzcache.CacheServiceCdz;
import com.clouyun.cdzcache.util.Utility;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.system.service.DictService;
import com.github.pagehelper.PageInfo;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.mapper.HistoryDataMapper;
import javax.servlet.http.HttpServletResponse;

@Service
public class HistoryDataService {
	
	@Autowired
	HistoryDataMapper historyDataMapper;
	@Autowired
	DictService dictService;
	@Autowired
	private CacheServiceCdz csc;
    
	  /**
	   * 历史数据查询
	   */
	  public PageInfo selectHistoryData(DataVo dv) throws BizException {
		  ChargeManageUtil.stationIdsCondition(dv,RoleDataEnum.STATION.dataType);
		  ChargeManageUtil.setPageInfo(dv);
		  List<DataVo> list = historyDataMapper.selectHistoryData(dv);
		  List<ComboxVo> boxList1  =dictService.getDictByType("cllx");
		  for ( DataVo vo: list){
			  ChargeManageUtil.setDataVoPut("vehicleType",boxList1,vo);//车辆类型
		  }
	      return 	new PageInfo(list);
	  }

	/**
	 * 根据会员查询会员信息
	 */
	public DataVo findPayId(Map dv){
		return historyDataMapper.findPayId(dv);
	}
	/**
	 * 查询ChgDataCur信息
	 */
	public List<DataVo> findChgDataCur(Map dv){
		return historyDataMapper.selectChgDataCur(dv);
	}
	/**
	 * 查询ChgPile信息
	 */
	public DataVo findChgPile(Map dv){
		return historyDataMapper.findChgPile(dv);
	}
	/**
	 * 查询findChgRc信息
	 */
	public List<DataVo> findChgRc(Map dv){
		return historyDataMapper.findChgRc(dv);
	}
	/**
	 * 查询用电实时数据（充电检测数据，bms检测数据，收入监控）
	 */
    public Map<String,Object> historyAchieveData(Map data) {
				String payId = data.get("payId").toString();
		Map payMap = new HashMap();
		payMap.put("payId",payId);
		DataVo billPay=findPayId(data);//得到会员信息
		List<DataVo> dataList=findChgDataCur(data);//得到ChgDataCur
		if(dataList.size()==0){
			return null;
		}
		Map pileMap = new HashMap();
		pileMap.put("pileId",billPay.getInt("pileId"));
		DataVo pile=findChgPile(pileMap);//得到ChgPile
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		if(null == pile || pile.isBlank("pileType")){
			ChargeManageUtil.printMessage("订单关联的充电桩不存在。");
		}
		List<List<Double>> lists = new ArrayList<List<Double>>();
		List<List<String>> listTime = new ArrayList<List<String>>();
		for (int i = 0; i < 11; i++) {
			lists.add(new ArrayList<Double>());
		}
		for(int i=0;i<6;i++){
			listTime.add(new ArrayList<String>());
		}
		Map<Integer, List<DataVo>> map = new HashMap<Integer, List<DataVo>>();// 根据objType来存放相同类型的值
		if (dataList!=null&&dataList.size()>0) {
			for (DataVo chgDataCur : dataList) {
				addDataCurToMap(Integer.valueOf(chgDataCur.getString("objType")),map, chgDataCur);
			}
		}
		int type = Integer.parseInt(pile.getString("ortMode"));//枪类型换字典
		List soc = new ArrayList();
		switch (type) {
			case 1:// 交流充电桩 //交流桩不考虑总表
				organize1(intervalList(map.get(3)),lists, listTime);
				break;
			case 2:// 单直流充电桩
				List<DataVo> l = map.get(2);
				if(l!=null && l.size()>0){
					for (DataVo vo : l){
						soc.add(vo.getDouble("soc"));
					}
				}
				organize2(map.get(3), intervalList(l),lists, listTime);
				break;
			case 3:// 交直流充电桩
				organize1(intervalList(map.get(3)),lists, listTime);
				break;
			default:
				break;
		}
		Double charge[] = {billPay.getDouble("amount"),billPay.getDouble("chgPower")};
		dataMap.put("charge", charge);
		dataMap.put("u", lists.get(u).toArray());
		dataMap.put("i", lists.get(i).toArray());
		dataMap.put("p", lists.get(p).toArray());
		dataMap.put("zu", lists.get(zu).toArray());
		dataMap.put("zi", lists.get(zi).toArray());
		dataMap.put("zp", lists.get(zp).toArray());
		dataMap.put("dl", lists.get(dl).toArray());
		dataMap.put("sr", lists.get(sr).toArray());
		dataMap.put("wd", lists.get(wd).toArray());
		dataMap.put("bmsU", lists.get(bmsU).toArray());
		dataMap.put("bmsI", lists.get(bmsI).toArray());
		dataMap.put("ZUipTime", listTime.get(ZUipTime).toArray());
		dataMap.put("UipTime", listTime.get(UipTime).toArray());
		dataMap.put("WdTime", listTime.get(WdTime).toArray());
		dataMap.put("BmsTime", listTime.get(BmsTime).toArray());
		dataMap.put("SrTime", listTime.get(SrTime).toArray());
		Collections.sort(soc);
		dataMap.put("soc", soc);
		try {
			getChaSourse(dataMap,payMap);
		}catch (Exception e){
			return null;
		}
		return  dataMap;
    }

	/**
	 * 根据objType类型添加map
	 * @param key map键值分类
	 * @param map 存储map
	 * @param dataCur 对象
	 * @return
	 */
	private static void addDataCurToMap(Integer key,Map<Integer, List<DataVo>> map,
										DataVo dataCur) {
		if (map.containsKey(key)) {
			List<DataVo> value = map.get(key);
			value.add(dataCur);
			map.put(key, value);
		} else {
			List<DataVo> value = new ArrayList<DataVo>();
			value.add(dataCur);
			map.put(key, value);
		}
	}
	/**
	 * 交流桩添加数据
	 * @param list 交流桩采集表数据
	 * @param lists 总集合
	 */
	private void    organize1(List<DataVo> list, List<List<Double>> lists, List<List<String>> listTime) {
		if(list!=null&&list.size()>0){
			for (DataVo chgDataCur : list) {
				getUipToList(lists.get(u), lists.get(i), lists.get(p),
						chgDataCur, listTime.get(UipTime));
				getDlJeToList(lists.get(dl), lists.get(sr), chgDataCur,
						listTime.get(SrTime));
			}
		}
	}

	public static final byte i = 0, p = 1, u = 2, dl = 3, sr = 4, bmsU = 5,
			bmsI = 6, wd = 7, zi = 8, zp = 9, zu = 10, ZUipTime = 0,
			UipTime = 1, BmsTime = 2, WdTime = 3, SrTime = 4;

	/**
	 * 充电过程数据对象，获取电压、电流、功率(kw)添加到对应List
	 * @param listU 电压集合
	 * @param listI 电流集合
	 * @param listP 功率集合
	 * @param chgDataCur
	 */
	private void getUipToList(List<Double> listU, List<Double> listI,
							  List<Double> listP, DataVo chgDataCur, List<String> listTime) {
		// 不存在为空情况，没值为0.00
		Double u = chgDataCur.get("ua") != null ? chgDataCur.getDouble("ua")
				: ( chgDataCur.get("ub") != null ? chgDataCur.getDouble("ub")
				: (chgDataCur.get("uc") != null ? chgDataCur.getDouble("uc"):null));
		Double i = chgDataCur.get("ia") != null ? chgDataCur.getDouble("ia")
				: (chgDataCur.get("ib") != null ? chgDataCur.getDouble("ib")
				: (chgDataCur.get("ic") != null ? chgDataCur.getDouble("ic"):null));
		if (u !=null && i != null) {
			listU.add(u);
			listI.add(i);
			listP.add(ChargeManageUtil.div(ChargeManageUtil.mul(u, i), 1000, 2));
			listTime.add(chgDataCur.getString("dataTime"));
		}
	}
	/**
	 * 充电过程数据对象，获取电量、金额添加到对应List
	 * @param listDl 电量集合
	 * @param listJe 金额集合
	 * @param chgDataCur
	 */
	private void getDlJeToList(List<Double> listDl, List<Double> listJe,DataVo chgDataCur, List<String> listTime) {
		Double dl = chgDataCur.getDouble("chgDl");
		Double je = chgDataCur.getDouble("chgJe");
		if (dl!=null&&je!=null) {
			if(je>0){ //金额大于0添加
				listDl.add(dl);
				listJe.add(je);
				listTime.add(chgDataCur.getString("dataTime"));
			}
		}
	}
	/**
	 * 数据量大时过滤数据   需求待确认，
	 * @param list
	 * @return
	 */
	private List<DataVo> intervalList(List<DataVo> list) {
		List<DataVo> ll = new ArrayList<DataVo>();
		if (list != null && list.size() > 0) {
			int size = list.size();
			if (size > 200 && size < 500) {
				for (int i = 0; i < size - 2; i += 2) {
					ll.add(list.get(i));
				}
			} else if (size >= 500 && size < 1000) {
				for (int i = 0; i < size - 5; i += 5) {
					ll.add(list.get(i));
				}
			} else if (size >= 1000) {
				for (int i = 0; i < size - 10; i += 10) {
					ll.add(list.get(i));
				}
			} else {
				ll.addAll(list);
			}
		}
		return ll;
	}
	/**
	 * 直流桩添加数据
	 * @param listB 直流桩表计数据集合
	 * @param listQ 直流枪数据集合
	 * @param lists 总集合
	 */
	private void organize2(List<DataVo> listB, List<DataVo> listQ,
						   List<List<Double>> lists, List<List<String>> listTime) {

		if (listB != null && listB.size() > 0) {
			Map<Integer, List<DataVo>> map = new HashMap<Integer, List<DataVo>>();
			// 重新生成过滤枪表和总表 目的是过滤过多数据量
			for (DataVo chgDataCur : listB) {
				addDataCurToMap(chgDataCur.getInt("innerId"), map, chgDataCur);
			}
			// key值为1是总表数据
			List<DataVo> listZb = intervalList(map.get(1));
			for (DataVo chgDataCur : listZb) {
				getUipToList(lists.get(zu), lists.get(zi), lists.get(zp),
						chgDataCur, listTime.get(ZUipTime));
			}
		}
		if (listQ != null && listQ.size() > 0) {
			for (DataVo chgDataCur : listQ) {
				getUipToList(lists.get(u), lists.get(i), lists.get(p),
						chgDataCur, listTime.get(UipTime));
				getDlJeToList(lists.get(dl), lists.get(sr), chgDataCur,
						listTime.get(SrTime));
				getBmsTolist(lists.get(bmsU), lists.get(bmsI), lists.get(wd),
						chgDataCur, listTime.get(BmsTime), listTime.get(WdTime));
			}
		}
	}
	/**
	 * 充电过程数据对象，获取BMS电压、BMS电流、电机温度添加到对应List
	 * @param listSu BMS电压集合
	 * @param listSi BMS电流集合
	 * @param listWd 电机温度集合
	 * @param chgDataCur
	 */
	private void getBmsTolist(List<Double> listSu, List<Double> listSi,
							  List<Double> listWd, DataVo chgDataCur, List<String> listTime,
							  List<String> listTime1) {
		Double sU = chgDataCur.getDouble("ua");
		Double sI = chgDataCur.getDouble("ia");
		Double wd = chgDataCur.getDouble("temper2");
		if (sU != null && sI != null) {
			listSu.add(sU);
			listSi.add(sI);
			listTime.add(chgDataCur.getString("dataTime"));
		}
		if (wd != null)
			listWd.add(wd);
		listTime1.add(chgDataCur.getString("dataTime"));
	}
	/**
	 * 获取充电过程
	 * @param dataMap
	 * @throws JSONException
	 */
	public void getChaSourse(Map<String, Object> dataMap, Map data) throws JSONException {
		List<DataVo> list  = findChgRc(data);
		if (list != null && list.size() > 0) {
			DataVo dataVo = list.get(0);
			String begin =dataVo.getString("begTime");
			String end = dataVo.getString("endTime1");
			String timeEnd1 = dataVo.getString("timeMin1");
			dataMap.put("chargeTime", chargeTime(Long.parseLong(timeEnd1)));
			dataMap.put("begin", begin);
			dataMap.put("end", end);
			String ratedl =dataVo.get("ratedl") == null ? "" : dataVo.getString("ratedl");
			if (ratedl.length() > 0){
				Calendar c = CalendarUtils.getCalendar(begin);
				int begPos = c.get(Calendar.HOUR_OF_DAY)*2 + c.get(Calendar.MINUTE)/30;
				if(begPos<0 || begPos>47)
					begPos = 0;
				dataMap.put("sourse", sourseList(ratedl,begPos));
			}else{
				List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
				Map<String, Object> map = new HashMap<String, Object>();
				mapList.add(map);
				Double dl = (dataVo.get("dl") == null) ? 0D : Double.valueOf(dataVo.getString("dl"));
				map.put("time", "All");
				map.put("power", ChargeManageUtil.div(dl,1000)+" kWh");
				dataMap.put("sourse", mapList);
			}
		}
	}
	private String chargeTime(long cutSec) {
		if(cutSec<0){
			throw new IllegalArgumentException("The cutSec must be a positive integer or zero");
		}
		String timeStr = (cutSec/60)+"分";
		if(cutSec<10*60){
			timeStr += ((cutSec%60)+"秒");
		}
		if(cutSec>99*60){
			timeStr = (cutSec/3600)+"小时"+((cutSec/60)%60)+"分";
		}
		return timeStr;
	}
	/**
	 * 返回订单充电过程充电量列表
	 * @param ratedl
	 * @return
	 */
	private List<Map<String, Object>> sourseList(String ratedl,int begPos) {
		String arr [] = ratedl.split(",");
		SortedMap<Integer, String> smap = new TreeMap<Integer, String>();
		for (String string : arr) {
			String s [] = string.split("=");
			Integer pos = Integer.parseInt(s[0]);
			if(pos<begPos)
				pos+=48;
			smap.put(pos, string);
		}

		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		Double allv = 0D;
		DecimalFormat df = new DecimalFormat("0.00");
		for(Integer pos:smap.keySet()){
			String string = smap.get(pos);

			map = new HashMap<String, Object>();
			String s [] = string.split("=");
			map.put("time", timeInterval(Integer.parseInt(s[0])));
			double v = ChargeManageUtil.div(Double.parseDouble(s[1]),100);
			allv+=v;
			map.put("power", df.format(v)+" kWh");
			mapList.add(map);
		}
		map = new HashMap<String, Object>();
		mapList.add(map);
		map.put("time", "All");
		map.put("power", df.format(allv) +" kWh");

		return mapList;
	}
	/**
	 * 根据key值返回时间段字符串
	 * @param mark key值
	 * @return
	 */
	private static String timeInterval(int mark) {
		if (mark > 47)
			throw new IllegalArgumentException("数据时标不能大于47.");
		DecimalFormat format = new DecimalFormat("#00");
		if (mark == 0) {
			return "23:30 - 00:00";
		}
		int end = mark * 30;
		int begin = end - 30;
		StringBuilder builder = new StringBuilder();
		return builder.append(format.format(begin / 60) + ":")
				.append(format.format(begin % 60) + " - ")
				.append(format.format(end / 60) + ":")
				.append(format.format(end % 60)).toString();
	}

	/**
	 * 历史数据导出
	 * @param data
	 * @param response
	 */
	public void exportHistoryData(DataVo data, HttpServletResponse response)throws Exception {
		List dvList=selectHistoryData(data).getList();
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("订单时间");
		headList.add("订单编号");
		headList.add("开始充电时间");
		headList.add("结束充电时间");
		headList.add("充电电量(kW·h)");
		headList.add("运营商");
		headList.add("场站名称");
		headList.add("设备名称");
		headList.add("会员名称");
		headList.add("会员号码");
		headList.add("车牌号");
		headList.add("路线");
		valList.add("createTime");
		valList.add("billPayNo");
		valList.add("startTime");
		valList.add("endTime");
		valList.add("chgPower");
		valList.add("orgName");
		valList.add("stationName");
		valList.add("pileName");
		valList.add("consName");
		valList.add("consPhone");
		valList.add("licensePlate");
		valList.add("line");
		ExportUtils.exportExcel(dvList,response,headList,valList,"历史数据表");
	}

    public DataVo interConnectivityOrder(DataVo vo) {
		List<DataVo> pileList =	historyDataMapper.getToPile(vo);
		int pileType1 = 0;//快
		int pileType2 = 0;//慢
		int chargePile=0;
		Set<String> equipmentIds = new HashSet<>();
		for(DataVo piles:  pileList){
			equipmentIds.add(piles.getString("equipmentId"));
		}
		vo.put("equipmentIds",equipmentIds);
		List<DataVo> gunList =	historyDataMapper.getgunList(vo);
		List<DataVo> equipmentList = new ArrayList<>();
		for (DataVo pile1 : pileList){
			DataVo dataVo = new DataVo();
			int i=0;
			List<DataVo> connectorList = new ArrayList<>();
			DataVo equipment = new DataVo();
			String equipmentId1	=pile1.getString("equipmentId");
			equipment.put("equipmentId",equipmentId1);
			equipment.put("equipmentName",pile1.getString("equipmentName"));
			Integer equipmentType =	pile1.getInt("equipmentType");
			equipment.put("equipmentType",equipmentType);
			equipment.put("chargeState",1);//桩状态
			if(equipmentType==1){
				pileType1++;
			}else if(equipmentType==2){
				pileType2++;
			}
			equipment.put("date",pile1.getString("syncTime"));
			for (DataVo gun: gunList){
				String equipmentId2	=gun.getString("equipmentId");
				if(equipmentId1.equalsIgnoreCase(equipmentId2)){
					i++;
					DataVo connector = new DataVo();
				String connectorId =	gun.getString("connectorId");
				String connectorName =	gun.getString("connectorName");
				Integer connectorType =	gun.getInt("connectorType");
					connector.put("connectorId",connectorId);
					connector.put("connectorName",connectorName);
					connector.put("connectorType",connectorType);
				    byte state	=getEquipmentStatus(gun);
					connector.put("chargeState",state);//枪状态

					if(equipment.getInt("chargeState")!=0){
						equipment.put("chargeState",state);
					}
					if(state==0){
						connector.put("chargeTime","");//充电时间
						connector.put("current",gun.getString("current"));//电压
						connector.put("voltageUpperLimits",gun.getString("voltageUpperLimits"));//电流
					}
					connectorList.add(connector);
					continue;
				}
				if(equipment.getInt("chargeState")==0){
					equipment.put("chargeState",0);//桩状态
					chargePile++;
				}
			}

			equipment.put("size",i);
			equipment.put("connectors",connectorList);
			dataVo.put("equipments",equipment);
			equipmentList.add(dataVo);
		}
		Set gunSet  = new HashSet();
		for(DataVo list :gunList){
			gunSet.add(list.getString("connectorId"));
		}
		vo.put("gunSet",gunSet);
		DataVo count = new DataVo();
		DataVo orderCount =	historyDataMapper.getOrderList(vo);
		vo.put("dateTime",new Date());
		DataVo orderCountDate =	historyDataMapper.getOrderList(vo);
		count.put("totalMoneyCount",ChargeManageUtil.df.format(orderCount.getDouble("totalMoney")));
		count.put("totalPowerCount",ChargeManageUtil.df.format(orderCount.getDouble("totalPower")));
		count.put("totalSize",orderCount.getDouble("size"));
		count.put("totalMoneyDate",ChargeManageUtil.df.format(orderCountDate.getDouble("totalMoney")));
		count.put("totalPoweDate",ChargeManageUtil.df.format(orderCountDate.getDouble("totalPower")));
		count.put("totalDateSize",orderCountDate.getDouble("size"));
		count.put("equipmentTypeCount",pileType1+pileType2);
		count.put("equipmentType1",pileType1);
		count.put("equipmentType2",pileType2);
		count.put("chargeSize",chargePile);//充电中桩
		DataVo returnMap = new DataVo();
		returnMap.put("count",count);
		returnMap.put("list",equipmentList);
		List<DataVo> stationList =	historyDataMapper.getToStation(vo);
		returnMap.put("station",stationList);
		return returnMap;
    }

	//获取桩状态
	public byte getEquipmentStatus(DataVo vo)
	{
		byte status = 0;
		Set<String> keys = new HashSet();
			keys.add(vo.get("operatorId") + "_" + vo.get("connectorId"));
		try
		{
			Map<String, byte[]> map = csc.get(keys);
			if (map != null) {
				for (String key : map.keySet())
				{
					int qs = Utility.hexToInt(((byte[])map.get(key))[0]);
					if ((qs >= 2) && (qs <= 4))
					{
						status = 2;
						break;
					}
					status = 1;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
}

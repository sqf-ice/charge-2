package com.clouyun.charge.modules.monitor.service;

import com.clou.common.utils.ObjectUtils;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.cdzcache.imp.CDProcData;
import com.clouyun.cdzcache.imp.CDZStatusGet;
import com.clouyun.cdzcache.obj.LoginRec;
import com.clouyun.cdzcache.obj.QPData;
import com.clouyun.cdzcache.obj.ZPData;
import com.clouyun.charge.modules.monitor.mapper.DataMonitortMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author fft
 *
 */
@Service
public class DataMonitorService {
	@Autowired
	private DataMonitortMapper dataMonitortMapper;
	@Autowired
	private CDProcData cpd;
	@Autowired
	private CDZStatusGet csg;
	
	private static final Logger logger = Logger.getLogger(DataMonitorService.class);
	
	public static final byte aU = 0, // A枪电压
			aI = 1, 	// A枪电流
			aP = 2, 	// A枪功率
			bU = 3, 	// B枪电压
			bI = 4, 	// B枪电流
			bP = 5, 	// B枪功率
			zU = 6, 	// 总表电压
			zI = 7, 	// 总表电流
			zP = 8, 	// 总表
			aDl = 9,	// A枪电量
			aSr = 10,	// A枪收入
			bDl = 11,	// B枪电量
			bSr = 12,	// B枪收入
			aWd = 13,	// A枪温度
			bWd = 14,	// B枪温度
			aBmsU = 15,	// A枪BMS电压
			aBmsI = 16,	// A枪BMS电流
			bBmsU = 17,	// B枪BMS电压
			bBmsI = 18,	// B枪BMS电流
			AUipTime = 0,	// A枪时间轴
			BUipTime = 1,	// B枪时间轴
			ZUipTime = 2,	// 总表时间轴
			ADlTime = 3,	// A枪电量时间轴
			BDlTime = 4,	// B枪电量时间轴
			ABmsTime = 5,	// A枪BMS时间轴
			BBmsTime = 6,	// B枪BMS时间轴
			AWdTime = 7,	// A枪温度时间轴
			BWdTime = 8;	// B枪温度时间轴
	
	public Map getDataMonitor(Map dataMap){
		int pileType=Integer.parseInt(dataMap.get("pileType").toString());
		int pileId=Integer.parseInt(dataMap.get("pileId").toString());
		String pileAddr=dataMap.get("pileAddr").toString();

		Map<String, Object> map = new HashMap<String, Object>();
		Double socA = 0D;//取最后一个值
		Double socB = 0D;//取最后一个值
		int arr[] = new int[2];
		List<List<Double>> lists = new ArrayList<List<Double>>();
		List<List<String>> listTime = new ArrayList<List<String>>();
		for(int i=0;i<19;i++){
			lists.add(new ArrayList<Double>());
		}
		for(int i=0;i<9;i++){
			listTime.add(new ArrayList<String>());
		}
		List<Map<String, String>> maps = new ArrayList<Map<String,String>>();
		List<Map<String, String>> maps1 = new ArrayList<Map<String,String>>();
		
			ZPData data = cpd.getCdzProcData((byte) pileType,pileId, pileAddr);
			LinkedHashMap<Long, LoginRec> xt = data.getLoginMap();
			maps = xtMapToList(xt,true);
			maps1 = xtMapToList(xt,false);
			// 如果当前桩未处于充电状态，则只返回心跳信息
			arr = data.getCurDayOnlineStat();
			byte b = csg.getCDZStatus(pileAddr);
			if (b != 0) {
				map.put("xt", maps);
				map.put("xt1", maps1);
				map.put("arr", arr);
				map.put("isCharge", false);
				
				return map;	
			}
			
			switch (pileType) {
			//方法待优化
			case 1:// 单交流充电桩
				// A
				SortedMap<Long, QPData> djaMap = data.getQPDate(1);
				if (mapIsNotNull(djaMap)) {
					organize1(djaMap, lists, listTime);
				}
				break;
			case 2:// 单直流
				// 总表
				SortedMap<Long, QPData> dzzbMap = data.getQPDate(1);
				if (mapIsNotNull(dzzbMap)) {
					organize5(dzzbMap, lists, listTime);
				}
				// A
				SortedMap<Long, QPData> dzaMap = data.getQPDate(3);
				if (mapIsNotNull(dzaMap)) {
					socA = isEmpty(dzaMap.get(dzaMap.lastKey()).getSoc(), 0D);
					organize2(dzaMap, lists, listTime);
				}
				break;
			case 3:// 双交流
				// A
				SortedMap<Long, QPData> sjaMap = data.getQPDate(1);
				if (mapIsNotNull(sjaMap)) {
					organize1(sjaMap, lists, listTime);
				}
				// B
				SortedMap<Long, QPData> sjbMap = data.getQPDate(0);
				if (mapIsNotNull(sjbMap)) {
					organize3(sjbMap, lists, listTime);
				}
				break;
			case 4:// 双直流
				// 总表
				SortedMap<Long, QPData> szzbMap = data.getQPDate(1);
				if (mapIsNotNull(szzbMap)) {
					organize5(szzbMap, lists, listTime);
				}
				// A
				SortedMap<Long, QPData> szaMap = data.getQPDate(3);
				if (mapIsNotNull(szaMap)) {
					socA = isEmpty(szaMap.get(szaMap.lastKey()).getSoc(), 0D);
					organize2(szaMap, lists, listTime);
				}
				// B
				SortedMap<Long, QPData> szbMap = data.getQPDate(2);
				if (mapIsNotNull(szbMap)) {
					socB = isEmpty(szbMap.get(szbMap.lastKey()).getSoc(), 0D);
					organize4(szbMap, lists, listTime);
				}
				break;
			case 5:// 交直流充电桩
				// A
				SortedMap<Long, QPData> jzaMap = data.getQPDate(3);
				if (mapIsNotNull(jzaMap)) {
					socA = isEmpty(jzaMap.get(jzaMap.lastKey()).getSoc(), 0D);
					organize2(jzaMap, lists, listTime);
				}
				// B
				SortedMap<Long, QPData> jzbMap = data.getQPDate(1);
				if (mapIsNotNull(jzbMap)) {
					socB = isEmpty(jzbMap.get(jzbMap.lastKey()).getSoc(), 0D);
					organize4(jzbMap, lists, listTime);
				}
				break;
			default:
				break;
			}
		
		Double charge[] = getChargeInfo(pileId);
		map.put("isCharge", true);
		map.put("xt", maps);
		map.put("xt1", maps1);
		map.put("charge", charge);
		map.put("aU", lists.get(aU).toArray());
		map.put("aI", lists.get(aI).toArray());
		map.put("aP", lists.get(aP).toArray());
		map.put("AUipTime", listTime.get(AUipTime).toArray());
		map.put("bU", lists.get(bU).toArray());
		map.put("bI", lists.get(bI).toArray());
		map.put("bP", lists.get(bP).toArray());
		map.put("BUipTime", listTime.get(BUipTime).toArray());
		map.put("zU", lists.get(zU).toArray());
		map.put("zI", lists.get(zI).toArray());
		map.put("zP", lists.get(zP).toArray());
		map.put("ZUipTime", listTime.get(ZUipTime).toArray());
		map.put("aDl", lists.get(aDl).toArray());
		map.put("aSr", lists.get(aSr).toArray());
		map.put("ADlTime", listTime.get(ADlTime).toArray());
		map.put("aWd", lists.get(aWd).toArray());
		map.put("AWdTime", listTime.get(AWdTime).toArray());
		map.put("bDl", lists.get(bDl).toArray());
		map.put("bSr", lists.get(bSr).toArray());
		map.put("BDlTime", listTime.get(BDlTime).toArray());
		map.put("bWd", lists.get(bWd).toArray());
		map.put("BWdTime", listTime.get(BWdTime).toArray());
		map.put("aBmsU", lists.get(aBmsU).toArray());
		map.put("aBmsI", lists.get(aBmsI).toArray());
		map.put("ABmsTime", listTime.get(ABmsTime).toArray());
		map.put("bBmsU", lists.get(bBmsU).toArray());
		map.put("bBmsI", lists.get(bBmsI).toArray());
		map.put("BBmsTime", listTime.get(BBmsTime).toArray());
		map.put("arr", arr);
		map.put("socA", socA);
		map.put("socB", socB);
	
		return map;
		
	}
	
	private List<Map<String, String>> xtMapToList(LinkedHashMap<Long, LoginRec> xt, boolean valid) {
		if (!ObjectUtils.isNull(xt)) {
			List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
			for (Map.Entry<Long, LoginRec> entry : xt.entrySet()) {
				if (!ObjectUtils.isNull(entry.getKey())) {
					LoginRec lr = entry.getValue();
					if(valid && !lr.isValid())
						continue;
					Map<String, String> map = new HashMap<String, String>();
					map.put("sj", longToDate(entry.getKey()));
					map.put("ms", lr.toString());
					mapList.add(map);
				}
			}
			return mapList;
		}
		return new ArrayList<Map<String, String>>();
	}
	
	/**
	 * 统计单个桩的订单表 今日收入、总收入、总充电量
	 * @return
	 */
	private Double[] getChargeInfo(int pileId) {
		Double arr[] = new Double[]{0D,0D,0D};
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pileId", pileId);		
		Map dataMap=dataMonitortMapper.getpileInfoByPileId(map);//总收入和总电量		
		String endTime=DateUtils.getDate();//获取时间字符串    yyyy-MM-dd
		map.put("endTime", endTime);
		Map dataMap1=dataMonitortMapper.getpileInfoByPileId(map);//当日收入
		if (dataMap!=null&&!dataMap.isEmpty()) {
			arr[1] = dataMap.get("amount")==null? 0 : Double.parseDouble(dataMap.get("amount").toString());
			arr[2] = dataMap.get("chgPower")==null? 0 : Double.parseDouble(dataMap.get("chgPower").toString());
		}
		if (dataMap1!=null&&!dataMap1.isEmpty()){
			arr[0] = dataMap1.get("amount")==null? 0 : Double.parseDouble(dataMap1.get("amount").toString());
		}
			
		return arr;
	}
	
	/**
	 * 判断map是否为空
	 * @param map
	 * @return
	 */
	private static boolean mapIsNotNull(SortedMap<Long, QPData>map){
		return map!=null && !map.isEmpty();
	}
	
	/**
	 * 交流A枪
	 * 所有相关联数据统计一条完整相关的数据
	 * @param map
	 */
	private void organize1(SortedMap<Long, QPData> map, List<List<Double>> lists, List<List<String>> listTime) {
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			Double u = entry.getValue().getU();
			Double a = entry.getValue().getI();
			Double p = entry.getValue().getP();
			Double dL = entry.getValue().getDl();
			Double je = entry.getValue().getJe();
			String time = longToDate(entry.getKey());//数据时标
			if (!ObjectUtils.isNull(u) && !ObjectUtils.isNull(a)
					&& !ObjectUtils.isNull(p)) {
				lists.get(aU).add(u);
				lists.get(aI).add(a);
				lists.get(aP).add(p);
				listTime.get(AUipTime).add(time);
			}
			if (!ObjectUtils.isNull(dL) && !ObjectUtils.isNull(je)) {
				lists.get(aDl).add(dL);
				lists.get(aSr).add(je);
				listTime.get(ADlTime).add(time);
			}
		}
	}
	/**
	 * 直流A枪
	 * 所有相关联数据统计一条完整相关的数据
	 * @param map
	 */
	private void organize2(SortedMap<Long, QPData> map, List<List<Double>> lists, List<List<String>> listTime) {
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			Double u = entry.getValue().getU();
			Double a = entry.getValue().getI();
			Double p = entry.getValue().getP();
			Double ti = entry.getValue().getTi();
			Double tu = entry.getValue().getTu();
			Double dL = entry.getValue().getDl();
			Double je = entry.getValue().getJe();
			Double w = entry.getValue().getTmp();
			String time = longToDate(entry.getKey());//数据时标
			if (!ObjectUtils.isNull(u) && !ObjectUtils.isNull(a)
					&& !ObjectUtils.isNull(p)) {
				lists.get(aU).add(u);
				lists.get(aI).add(a);
				lists.get(aP).add(p);
				listTime.get(AUipTime).add(time);
			}
			if (!ObjectUtils.isNull(dL) && !ObjectUtils.isNull(je)) {
				lists.get(aDl).add(dL);
				lists.get(aSr).add(je);
				listTime.get(ADlTime).add(time);
			}
			if (!ObjectUtils.isNull(w)){
				lists.get(aWd).add(w);
				listTime.get(AWdTime).add(time);
			}
			if (!ObjectUtils.isNull(ti) && !ObjectUtils.isNull(tu)) {
				lists.get(aBmsI).add(ti);
				lists.get(aBmsU).add(tu);
				listTime.get(ABmsTime).add(time);
			}
		}
	}
	/**
	 * 交流B枪
	 * 所有相关联数据统计一条完整相关的数据
	 * @param map
	 */
	private void organize3(SortedMap<Long, QPData> map ,List<List<Double>>lists, List<List<String>> listTime){
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			Double u = entry.getValue().getU();
			Double a = entry.getValue().getI();
			Double p = entry.getValue().getP();
			Double dL = entry.getValue().getDl();
			Double je = entry.getValue().getJe();
			Double w = entry.getValue().getTmp();
			String time = longToDate(entry.getKey());//数据时标
			if (!ObjectUtils.isNull(u)&&!ObjectUtils.isNull(a)&&!ObjectUtils.isNull(p)){
				lists.get(bU).add(u);
				lists.get(bI).add(a);
				lists.get(bP).add(p);
				listTime.get(BUipTime).add(time);
			}
			if (!ObjectUtils.isNull(dL)&&!ObjectUtils.isNull(je)){
				lists.get(bDl).add(dL);
				lists.get(bSr).add(je);
				listTime.get(BDlTime).add(time);
				
			}
			if (!ObjectUtils.isNull(w)){
				lists.get(bWd).add(w);
				listTime.get(BWdTime).add(time);
			}
		}
	}
	/**
	 * 直流B枪
	 * 所有相关联数据统计一条完整相关的数据
	 * @param map
	 */
	private void organize4(SortedMap<Long, QPData> map, List<List<Double>> lists, List<List<String>> listTime) {
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			Double u = entry.getValue().getU();
			Double a = entry.getValue().getI();
			Double p = entry.getValue().getP();
			Double ti = entry.getValue().getTi();
			Double tu = entry.getValue().getTu();
			Double dL = entry.getValue().getDl();
			Double je = entry.getValue().getJe();
			Double w = entry.getValue().getTmp();
			String time = longToDate(entry.getKey());//数据时标
			if (!ObjectUtils.isNull(u) && !ObjectUtils.isNull(a)
					&& !ObjectUtils.isNull(p)) {
				lists.get(bU).add(u);
				lists.get(bI).add(a);
				lists.get(bP).add(p);
				listTime.get(BUipTime).add(time);
			}
			if (!ObjectUtils.isNull(dL) && !ObjectUtils.isNull(je)) {
				lists.get(bDl).add(dL);
				lists.get(bSr).add(je);
				listTime.get(BDlTime).add(time);
			}
			if (!ObjectUtils.isNull(w)){
				lists.get(bWd).add(w);
				listTime.get(BWdTime).add(time);
			}
			if (!ObjectUtils.isNull(ti) && !ObjectUtils.isNull(tu)) {
				lists.get(bBmsI).add(ti);
				lists.get(bBmsU).add(tu);
				listTime.get(BBmsTime).add(time);
			}
		}
	}
	/**
	 * 总表
	 * @param map
	 */
	private void organize5(SortedMap<Long, QPData> map ,List<List<Double>>lists, List<List<String>> listTime){
		for (Map.Entry<Long, QPData> entry : map.entrySet()) {
			Double u = entry.getValue().getU();
			Double a = entry.getValue().getI();
			Double p = entry.getValue().getP();
			String time = longToDate(entry.getKey());//数据时标
			if (!ObjectUtils.isNull(u)&&!ObjectUtils.isNull(a)&&!ObjectUtils.isNull(p)){
				lists.get(zU).add(u);
				lists.get(zI).add(a);
				lists.get(zP).add(p);
				listTime.get(ZUipTime).add(time);
			}
		}
	}
	
	/**
	 * long类型时间转换成字符串时间
	 * @param l
	 * @return
	 */
	private static String longToDate(Long l) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(l);
		return format.format(date);
	}
	
	private static Double isEmpty(Object d1, Double d2) {
		if (!ObjectUtils.isNull(d1))
			return Double.valueOf(d1.toString());
		return d2;
	}
	

}

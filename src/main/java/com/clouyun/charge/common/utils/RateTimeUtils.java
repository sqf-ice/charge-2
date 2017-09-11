package com.clouyun.charge.common.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.clou.common.utils.CalendarUtils;

public class RateTimeUtils {
	
	
	public static void main(String[] args) {
		Calendar c1 = CalendarUtils.convertStrToCalendar("2017-05-12 11:07:44", CalendarUtils.yyyyMMddHHmmss);
		Calendar c2 = CalendarUtils.convertStrToCalendar("2017-05-12 11:11:49", CalendarUtils.yyyyMMddHHmmss);
		
		Map<String, Long> rateTimeLen = rateTimeLen(c1.getTimeInMillis(), c2.getTimeInMillis());
		System.out.println(rateTimeLen);
	}
	/**
	 * 根据充电开始时间结束时间获取峰平谷时长(单位秒)
	 * @param begTime    开始时间 
	 * @param endTime    结束时间
	 * @param rt         费率时段模板
	 * @return
	 */
	public static Map<String,Long> rateTimeLen(long begTime,long endTime){
		Map<String,List<Byte>> rt = getRateTimeTemplate();
		Map<String,Long> map = new HashMap<String,Long>();
		long day0 = begTime;
		TimeZone tz = Calendar.getInstance().getTimeZone();
		tz.getRawOffset();
		day0 = day0+tz.getRawOffset();   
		long t = day0/1000;
		t = t/60;
		t = t/1440;          
		day0 =  t*(1440L*60*1000)-tz.getRawOffset();
		day0 = day0/1000;
		
		begTime = begTime/1000;
		endTime = endTime/1000;
		
		byte pos = (byte)((begTime-day0)/(60*30));
		pos = (byte)(pos%48);
		for(String key:rt.keySet()){
			List<Byte> set = rt.get(key);
			if(set.contains(pos)){
				long bt = day0+(pos+1)*60*30;
				if(bt>endTime)
					bt = endTime;
				Long lv = map.get(key);
				if(lv==null)lv = 0L;
				System.out.println();
				lv += (bt-begTime);
				map.put(key, lv);
				begTime = bt;
				break;
			}
		}
		if(endTime>begTime){
			int epos = (byte)((endTime-day0)/(60*30));
			pos = (byte)(epos%48);
			for(String key:rt.keySet()){
				List<Byte> set = rt.get(key);
				if(set.contains(pos)){
					long et = day0+epos*60*30;
					Long lv = map.get(key);
					if(lv==null)lv = 0L;
					lv += (endTime-et);
					map.put(key, lv);
					endTime = et;
					break;
				}
			}
		}
		while(begTime<endTime){
			pos = (byte)((begTime-day0)/(60*30));
			pos = (byte)(pos%48);
			for(String key:rt.keySet()){
				List<Byte> set = rt.get(key);
				if(set.contains(pos)){
					Long lv = map.get(key);
					if(lv==null)lv = 0L;
					lv += 30*60;
					map.put(key, lv);
					break;
				}
			}
			begTime+=30*60;
		}
		return map;
	}

	/**
	 * 组织费率时段模板
	 * @return
	 */
	public static Map<String,List<Byte>> getRateTimeTemplate(){
		Map<String,List<Byte>> rt = new HashMap<String,List<Byte>>();
		rt.put("峰", Arrays.asList( (byte)18,(byte)19,(byte)20,(byte)21,(byte)22, //9:0~11:30
				(byte)28,(byte)29,(byte)30,(byte)31,(byte)32,  //14:00~16:30
				(byte)38,(byte)39,(byte)40,(byte)41 ) );    //19:0~21
		
		rt.put("平", Arrays.asList( (byte)14,(byte)15,(byte)16,(byte)17,  //7:0~9:0
				(byte)23,(byte)24,(byte)25,(byte)26,(byte)27,  //11:30~14:00
				(byte)33,(byte)34,(byte)35,(byte)36,(byte)37,  //16:30~19:00
				(byte)42,(byte)43,(byte)44,(byte)45 ) );   //21:0~23:00
		
		rt.put("谷", Arrays.asList( (byte)46,(byte)47,   //23:00~0:00
				(byte)0,(byte)1,(byte)2,(byte)3,(byte)4,(byte)5,(byte)6,
				(byte)7,(byte)8,(byte)9,(byte)10,(byte)11,(byte)12,(byte)13) );  //0:0~7:00
		
		return rt;
	}
}

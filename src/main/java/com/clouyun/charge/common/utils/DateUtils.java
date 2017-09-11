package com.clouyun.charge.common.utils;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/30.
 */
public class DateUtils {

    /**
     * 得到月
     * @param date
     * @return
     */
    public  static  int getMonthFormat(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return  calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 得到年
     * @param date
     * @return
     */
    public  static  String getYear(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return sdf.format(calendar.getTime());
    }
    /**
     * 得到月
     * @param date
     * @return
     */
    public  static  String getMonth(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return sdf.format(calendar.getTime());
    }
    /**
     * 得到月
     * @param str
     * @return
     */
    public  static  Date getMonth(String str) throws  Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(str));
        return  calendar.getTime();
    }
    /**
     * 得到日
     * @param date
     * @return
     */
    public  static  String getDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return sdf.format(calendar.getTime());
    }
    /**
     * 得到前一天
     * @param date
     * @return
     */
    public  static  Date getUpDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);//昨天
        return calendar.getTime();
    }
    /**
     * 得到上个月
     * @param date
     * @return
     */
    public  static  Date getUpMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);//上个月
        return calendar.getTime();
    }
    /**
     * 得到上个月
     * @param date
     * @return
     */
    public  static  Date getUpYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);//去年
        return calendar.getTime();
    }

    /**
     * 得到月份最后一天
     * @param date
     * @return
     * @throws BizException
     */
    public static String getLastDayOfMonth(Date date) throws Exception {
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        // 设置时间,当前时间不用设置
         calendar.setTime(date);
       // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }



    /**
     * 得到月天数
     * @param date
     * @return
     * @throws BizException
     */
    public static Integer getMonthDate(Date date) throws BizException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dateOfMonth = c.getActualMaximum(Calendar.DATE);
        return  dateOfMonth;
    }

    /**
     * 月时间格式获取时间
     * @param str
     * @return
     * @throws Exception
     */
    public static Date getDate(String str) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(str));
        return  calendar.getTime();
    }

    /**
     * 得到年时间格式
     * @param str
     * @return
     */
    public  static  Date getYear(String str) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(str));
        return  calendar.getTime();
    }
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
    /**
     * 获取月第一天日期
     * @param month 年份
     * @return Date
     */
    public static String  getMonthFirst(int year,int month){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //设置日历中月份的第1天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth ;
    }
    /**
     * 得到天数
     * @param startTime
     * @param endTime
     * @return
     * @throws BizException
     */
    public static Integer getDays(String startTime, String endTime) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(endTime));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(startTime));
        long time2 = cal.getTimeInMillis();
        long days=(time1-time2)/(1000*3600*24);
        return  Integer.parseInt(String.valueOf(days));
    }
    /**
     * 得到月周一
     * @param date
     * @return
     * @throws BizException
     */
    public static String getWeek(Date date) throws BizException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int i = 1;
        while(cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            cal.set(Calendar.DAY_OF_MONTH, i++);
        }
        String dtStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return  dtStr;
    }
    /**
     * 得到下一个月
     * @param date
     * @return
     */
    public  static  Date getDownMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);//下一个月
        return calendar.getTime();
    }
}

package com.clouyun.charge.modules.charge;


import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.modules.spring.web.servlet.ServletActionContext;
import com.clouyun.charge.common.utils.DateUtils;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class ChargeManageUtil {

    @Autowired
    private UserService userService;
    @Autowired
	private DictService dictService;
    private  static  ChargeManageUtil chargeManageUtil;
	@PostConstruct
	public void init() {
		chargeManageUtil =this;
		chargeManageUtil.userService =this.userService;
		chargeManageUtil.dictService = this.dictService;
	}
    private static final Map<Integer, LinkedHashMap<String, String>> dicts = new HashMap<Integer, LinkedHashMap<String, String>>();
	public static final Integer PAGE_NUM=1;
	public static  final  DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
	//每页显示50条数据
	public static final Integer PAGE_SIZE=30000;
	
	/**
	 * 标准日期格式
	 */
	public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

	/**
	 *
	 * @param vo
	 * @param type 类型0运营商，1.场站
	 * @param key 需要添加的key
	 */
     public static void getUserRoleData(DataVo vo,Integer type,String key) throws BizException {
		 Set<Integer>  orgIds=chargeManageUtil.userService.getUserRoleDataById(vo.getInt("userId"),type );
			 if(orgIds!=null&&orgIds.size()>0) {
				 vo.put(key, orgIds);
			 }
	 }


	/**
	 * 查询字典
	 * @param boxList
	 * @param code
	 */
	public static    void   setDataVoPut(String code, List<ComboxVo> boxList,DataVo vo){
		if(vo.isNotBlank(code)) {
			if(boxList!=null&&boxList.size()>0){
				for (ComboxVo box : boxList) {
					if (box.getId().equals(vo.getString(code))) {
						vo.put(code, box.getText());
						return;
					}
				}
			}
		}
	}


	/**
	 * 获取登录用户拥有场站或者企业
	 * @param vo
	 * @param type
	 * @return
	 */

	public static Set<Integer> getSubsByUserId(DataVo vo,Integer type) throws BizException {
			return chargeManageUtil.userService.getUserRoleDataById(vo.getInt("userId"),type );
	}
	/**
	 * 判断字符是否为空(return true)
	 * @param str
	 * @return
	 */
	public static boolean isNull(Object str){
		return (str == null || "".equals(str) || "null".equalsIgnoreCase(str.toString()) || "undefined".equalsIgnoreCase(str.toString()));
	}
	/**
	 * 依据登陆用户获取下级机构
	 * @param containsSelf 是否存入自己所属机构
	 *
	 * 支持多企业
	 *
	 * @return
	 */
	public static Map<Integer, DataVo> getAllChildOrg(boolean containsSelf){
		return null;
	}
	/**
	 * 加上登录用户拥有的企业条件
	 * @return
	 */
	public static void orgIdsCondition(DataVo dv,Integer type) throws BizException {
		Set<Integer> orgIds = ChargeManageUtil.getSubsByUserId(dv,type);
		if(orgIds != null && orgIds.size()>0){
			dv.put("appFrom",orgIds);
		}
	}


	/**
	 * 根据prcId查询企业信息
	 * @return
	 */
	public static DataVo queryOrgByprcId(Map dv){
		return null;
	}

	/**
	 * 字符串按指定格式转换成日期格式Calendar yyyy-MM-dd HH:mm:ss/yyyy-MM-dd
	 *
	 * @param sTime
	 * @return
	 */
	public static Calendar convertStrToCalendar(String sTime, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		java.util.Date date = new java.util.Date();
		try {
			date = sdf.parse(sTime);
		} catch (java.text.ParseException ex) {
			return null;
		}
		Calendar cRt = Calendar.getInstance();
		cRt.setTime(date);
		return cRt;
	}
	/**
	 * 依据itemType，itemNum获取ItemName
	 * @param itemType
	 * @param itemNum
	 * @return
	 */
	public static String getItemNameByItemNum(Integer itemType, String itemNum) {
		return dicts.get(itemType).get(itemNum);
	}
	/**
	 * 字符串格式yyyy-MM-dd HH:mm:ss转换成日期格式Calendar
	 *
	 * @param sTime
	 * @return
	 */
	public static Calendar getCalendar(String sTime) {
		return convertStrToCalendar(sTime, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 按指定格式格式化日期，并以String显示
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatCalendar(Calendar date, String pattern) {
		if(date==null)return "";
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date.getTime());
	}

	/**
	 * 
	 * 加上stationId条件进行判断
	 * @return
	 */
	public static void stationIdCondition(Map dv){
		 if (!ChargeManageUtil.isNull(dv.get("stationId"))) {
	    		dv.put("stationId", Integer.parseInt(dv.get("stationId").toString()));
			}else{
				if(!ChargeManageUtil.isNull(dv.get("stationName"))){
					String partten = "[A-Za-z0-9\u4E00-\u9FA5]*";
					if(Pattern.matches(partten, dv.get("stationName").toString())){
						dv.put("stationName", dv.get("stationName"));
					}else{
						dv.put("stationId", "-1");
					} 
				}
			}
	}
	
	/**
	 * 加上groupId条件进行判断
	 * 
	 * @return
	 */
	public static void groupIdCondition(Map dv){
		if (!ChargeManageUtil.isNull(dv.get("groupId"))) {
    		dv.put("groupId", Integer.parseInt(dv.get("groupId").toString()));
		}else{
			if(!ChargeManageUtil.isNull(dv.get("groupName"))){
				String partten = "[A-Za-z0-9\u4E00-\u9FA5]*";
				if(Pattern.matches(partten, dv.get("groupName").toString())){
					dv.put("groupName", dv.get("groupName"));
				}else{
					dv.put("groupId", "-1");
				} 
			}
		}
	}
	
	/**
	 * 加上pileId条件进行判断
	 * @return
	 */
	public static void pileIdCondition(Map dv){
		if (!ChargeManageUtil.isNull(dv.get("pileId"))) {
    		dv.put("pileId", Integer.parseInt(dv.get("pileId").toString()));
		}else{
			if(!ChargeManageUtil.isNull(dv.get("pileName"))){
				String partten = "[A-Za-z0-9\u4E00-\u9FA5]*";
				if(Pattern.matches(partten, dv.get("pileName").toString())){
					dv.put("pileName", dv.get("pileName"));
				}else{
					dv.put("pileId", "-1");
				} 
			}
		}
	}
	
	/**
	 * 加上登录用户拥有的场站条件
	 * @return
	 */
	public static void stationIdsCondition(DataVo dv,Integer type) throws BizException {
		Set<Integer> stationIds= ChargeManageUtil.getSubsByUserId(dv,type);
		if(null != stationIds && stationIds.size()>0){
			dv.put("stationIds",stationIds);
		}
	}

	

	/**
	 * 专为table 过滤日期用
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatCalendar(Object date, String pattern) {
		if(date==null)return "";
		if(date instanceof Calendar){
			return formatCalendar((Calendar)date, pattern);
		}else{
			return "";
		}
	}
    public static void printMessage(String outData) {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.reset();
		ServletOutputStream out;
		try {
			out = response.getOutputStream();
			out.print(outData);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
	/**
	 *
	 * 除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
	 *
	 * @param v1  被除数
	 * @param v2 除数
	 * @param scale 精确到小数点以后几位
	 * @return
	 */
	public static double div(double v1, double v2, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException("精确位数必须大于0！");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 乘法运算
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 *
	 * 除法运算，当发生除不尽的情况时，精确到小数点以后2位，以后的数字四舍五入
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @return
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, 2);
	}

	/**
	 * 判断登陆用户是否为超级用户
	 * @return
	 */
	public static boolean isSuperMan() {
		return (boolean) ServletActionContext.getRequest().getSession().getAttribute("superRole");
	}
   public  static  void setPageInfo(DataVo vo){
	   if(vo.isBlank("pageNum") ){
		   vo.put("pageNum",PAGE_NUM);
	   }
	   if(vo.isBlank("pageSize") ){
		   vo.put("pageSize",PAGE_SIZE);
	   }
	   PageHelper.startPage(vo);
   }

	/**
	 * 用户充电时长
	 * @param dv
	 */
   public static void  getUseTime(DataVo dv){
	   SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   long differMillis = 0;
		   if (dv.isNotBlank("startTime")&&dv.isNotBlank("endTime")) {
			   try {
				   String endTime = dv.getString("endTime");
				   String startTime = dv.getString("startTime");
				   Date endTimeDate =sdf.parse(endTime);
				   Date startTimeDate =sdf.parse(startTime);
				   Calendar calendarStartTime = Calendar.getInstance();
				   calendarStartTime.setTime(startTimeDate);
				   Calendar calendarEndTime = Calendar.getInstance();
				   calendarEndTime.setTime(endTimeDate);
				   differMillis = calendarEndTime.getTimeInMillis()-calendarStartTime.getTimeInMillis();
			   }catch (Exception e){
				   differMillis=0;
			   }
		   }
		   int minute = (int)differMillis/(1000*60);
		   int userSecond = (int)(differMillis/1000)%60;
		   int useHour = minute/60;
		   int userMinute = minute%60;
		   dv.put("useTime", useHour+"小时"+userMinute+"分"+userSecond+"秒");


   }
	public  static  void isMapNull(String[] Lists, DataVo map) throws BizException {
		for (int i = 0; i < Lists.length; i++) {
			String code = Lists[i];
			if(map.isBlank(code)){
				throw   new BizException(1102001,code);
			}
		}
	}
	public  static  void isStationNull(String[] Lists, DataVo map) throws BizException {
		for (int i = 0; i < Lists.length; i++) {
			String code = Lists[i];
			if(map.isBlank(code)){
				throw   new BizException(1102001,code);
			}
		}
	}
	public static void isNumeric(String str,String lable) throws BizException {
		Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
		Matcher isNum = pattern.matcher(str);
		if(!isNum.matches() ){
			throw   new BizException(1102006,lable);
		}
	}
	public static void isDate(String str,String lable) throws BizException {
		Pattern pattern = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}");
		Matcher isNum = pattern.matcher(str);
		if(!isNum.matches() ){
			throw   new BizException(1102006,lable);
		}
	}
   public  static  void  setDateCompare(DataVo dayVo,int type) throws Exception {
	   Date date =null;//上一个月
	   Date upDate = null;//上上个月值
	   String year ="";
	   String upYear ="";
	   int month =0;
	   int upMonth =0;
	   if(dayVo.isBlank("month")){
		   if(type==1){
			   date =  	DateUtils.getUpDay(new Date());//昨天
			   upDate =DateUtils.getUpDay(date);//上一月
		   }else  if(type==2){
			   date = DateUtils.getUpDay(new Date());//上个月
			   upDate = DateUtils.getUpMonth(date);//上上个月
			   year = DateUtils.getYear(date);
			   month =DateUtils.getMonthFormat(date);
			   upMonth =DateUtils.getMonthFormat(upDate);
		   }else if(type==3){
			   date =  DateUtils.getUpDay(new Date());//今年
			   upDate = DateUtils.getUpYear(date);//去年
			   year = DateUtils.getYear(date);
			   upYear = DateUtils.getYear(upDate);
		   }
	   }else {
		   Date monthStr = DateUtils .getDate(dayVo.getString("month"));
		   if(type==1){
			   date =  	DateUtils.getUpDay(monthStr);//昨天
			   upDate = DateUtils.getUpDay(date);//前天
		   }else  if(type==2){
			   date = monthStr;//本月
			   upDate = DateUtils.getUpMonth(date);//上个月
			   year = DateUtils.getYear(date);
			   month =DateUtils.getMonthFormat(date);
			   upMonth =DateUtils.getMonthFormat(upDate);
		   }else if(type==3){
			   date =  DateUtils.getUpDay(monthStr);//今年
			   upDate = DateUtils.getUpYear(date);//去年
			   year = DateUtils.getYear(date);
			   upYear = DateUtils.getYear(upDate);
		   }
	   }
	   if(type==1){
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   dayVo.put("date",sdf.format(date));
		   dayVo.put("upDate",sdf.format(upDate));
	   }else  if(type==2){
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   dayVo.put("date",sdf.format(date));
		   dayVo.put("upDate",sdf.format(upDate));
		   dayVo.put("month", DateUtils.getMonthFirst(Integer.parseInt(year),month));
		   dayVo.put("upMonth",DateUtils.getMonthFirst(Integer.parseInt(year),upMonth));
	   }else if(type==3){
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   dayVo.put("date",sdf.format(date));
		   dayVo.put("upDate",sdf.format(upDate));
		   dayVo.put("upYear", sdf.format(DateUtils.getYearFirst(Integer.parseInt(upYear))));
		   dayVo.put("year",sdf.format(DateUtils.getYearFirst(Integer.parseInt(year))));
	   }
   }



	public  static  void isMapNullPoint(String[] Lists, DataVo map,Map tsMap) throws BizException {
		for (int i = 0; i < Lists.length; i++) {
			String code = Lists[i];
			if(map.isBlank(code)){
				throw   new BizException(1102001,tsMap.get(code));
			}
		}
	}

	public static void setDateCompare(DataVo dayVo) throws Exception {
		Date date =null;//上一个月
		if(dayVo.isBlank("month")){
			date = DateUtils.getUpMonth(new Date());//上个月
		}else {
			date = DateUtils.getDate(dayVo.getString("month")) ;
		}
          Date upDate = DateUtils.getUpMonth(date);
		Date dwomDate = DateUtils.getDownMonth(date);
		String dateFormat = DateUtils.getMonth(date); //月时间格式
		String upDateFormat = DateUtils.getMonth(upDate);//上月时间格式
		String firstForm  = DateUtils.getWeek(upDate);//得到第一个周一格式
		String midFormat  = DateUtils.getWeek(date);//得到中间周一格式
		String lastFormat  = DateUtils.getWeek(dwomDate);//得到最后周一格式
		Integer day  = DateUtils.getMonthDate(date);//得到月天数
		Integer upDay  = DateUtils.getMonthDate(upDate);//得到上月月天数
		Integer weekDay  = DateUtils.getDays(midFormat,lastFormat);//得到周天数
		Integer upWeekDay  = DateUtils.getDays(firstForm,midFormat);//得到上月周天数
		Integer week  =  weekDay/7;//得到周数
		Integer upWeek  = upWeekDay/7;//得到周数
		dayVo.put("dateFormat",dateFormat);//月时间格式
		dayVo.put("upDateFormat",upDateFormat);//上月月时间格式
		dayVo.put("firstForm",firstForm);//上月第一个星期一
		dayVo.put("midFormat",midFormat);//本月周一日期
		dayVo.put("lastFormat",lastFormat);//下月周一日期
		dayVo.put("day",day);//月天数
		dayVo.put("upDay",upDay);//上月天数
		dayVo.put("weekDay",weekDay);//本月周天数
		dayVo.put("upWeekDay",upWeekDay);//上月周天数
		dayVo.put("week",week);//周数
		dayVo.put("upWeek",upWeek);//周数
		dayVo.put("lastMonthFormat",DateUtils.getLastDayOfMonth(date));//周数
	}

	public static void percentage(DataVo returnMap,Double percentage) {
		DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
		if(percentage>=0.995&&percentage<1){
			returnMap.put("percentage","99%");
		}else if(percentage>0&&percentage<=0.01) {
			returnMap.put("percentage","1%");
		}else {
			returnMap.put("percentage",df1.format(percentage));
		}
	}
	public static String percentage(double percentage) {
		DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
		if(percentage>=0.995&&percentage<1){
			return "99%";
		}else if(percentage>0&&percentage<=0.01) {
			return "1%";
		}else {
			return df1.format(percentage);
		}
	}
	/**
	 * 字典表取值
	 * @param str
	 * @return
	 * @throws BizException
	 */
	public  static  Map<String,List<ComboxVo>>  getDictMap(String[] str) throws BizException {
		Map<String,List<ComboxVo>> map  = new HashMap();
		for (String dic: str){
			map.put(dic,chargeManageUtil.dictService.getDictByType(dic));
		}
		return map;
	}

	/**
	 * 判断是否含有特殊字符
	 * @param str
	 * @return true为包含，false为不包含
	 */
   public  static  boolean isSpecialChar(String str){
	   String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
	   Pattern p = Pattern.compile(regEx);
	   Matcher m = p.matcher(str);
	   return m.find();
   }

}

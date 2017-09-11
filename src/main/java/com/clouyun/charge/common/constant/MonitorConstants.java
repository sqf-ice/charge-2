package com.clouyun.charge.common.constant;

import java.text.DecimalFormat;
public class MonitorConstants {
	
	public static final DecimalFormat DF = new DecimalFormat("0.00");// 格式化小数，不足的补0
	public static final String S = "S";//任务级别
	public static final String A = "A";
	public static final String B = "B";
	public static final String C = "C";
	public static final String D = "D";
	public static final Integer TASKAGAIN = 1;//再次派单标识
	public static final Integer PARENTID = 15;//告警项父id 
	
	
	/**
	 * 告警、作业状态
	 * 描述: 
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 1.0
	 * 创建日期: 2017年4月7日
	 */
	public enum WTStatus {
		US("1", "US"),//未处理
		DS("2", "DS"),//已派单
		CS("3", "CS"),//待确认
		RS("4", "RS"),//退回
		ES("5", "ES"),//已处理
		ADS("6","ADS");//再次派单	
		private String code;
		private String status;
		WTStatus(String status, String code) {
			this.code = code;
			this.status = status;
		}
		public String getCode() {
			return code;
		}
		public String getStatus() {
			return status;
		}
    }
	/**
	 * 
	 * 描述: 任务类型
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年4月21日
	 */
	public enum TaskType {
		WARN(0, "WARN"),//告警任务
		TASK(1, "TASK");//作业任务
		private String  code;
		private Integer type;
		TaskType(Integer type, String code) {
			this.code = code;
			this.type = type;
		}
		public String getCode() {
			return code;
		}
		public Integer getType() {
			return type;
		}
    }
	/**
	 * 
	 * 描述: 任务频率
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年4月21日
	 */
	public enum TaskFreq{
		SINGLE("0","SINGLE"),//单次任务
		DAY("1","DAY"),//日
		WEEK("2","WEEK"),//周
		MONTH("3","MONTH");//月
		private String  code;
		private String  type;
		TaskFreq(String type, String code) {
			this.code = code;
			this.type = type;
		}
		public String getCode() {
			return code;
		}
		public String getType() {
			return type;
		}
	}
	/**
	 * 
	 * 描述: 任务用户处理类型
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年4月21日
	 */
	public enum UserType {
		FIELD("1","FIELD"),//外勤用户
		USER("0", "USER");//平台用户
		private String code;
		private String type;
		UserType(String type, String code) {
			this.code = code;
			this.type = type;
		}
		public String getCode() {
			return code;
		}
		public String getType() {
			return type;
		}
    }
	/**
	 * 
	 * 描述: 资源
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年4月28日
	 */
	public enum OptRes{
		TEXT("01"),//文本
		DATA("02"),//数据
		PHOT("03"),//照片
		PHOG("04"),//照片(GPS)
		POSI("05"),//位置
		MATE("06");//物料信息
		private String code;
		OptRes(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
    }
	/**
	 * 
	 * 描述: 任务是否超时标识
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年4月28日
	 */
	public enum Time{
		ONTIME("0"),//准时
		OVERTIME("1");//超时
		private String code;
		Time(String code){
			this.code = code;
		}
		public String getCode() {
			return code;
		}
    }
	/**
	 * 描述:交直一体 
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年7月6日
	 */
	public enum OrtMode{
		AC("1", "交流"),
		DC("2", "直流"),
		ADC("3","交直一体");
		private String code;
		private String type;
		OrtMode(String code,String type){
			this.code = code;
			this.type = type;
		}
		public String getCode(){
			return code;
		}
		public String getType(){
			return type;
		}
    }
	/**
	 * 描述: 车辆使用属性
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年7月6日
	 */
	public enum UserRoperty{
		BUS("3", "公交"),
		PHY("4", "物流"),
		COM("8","通勤");//其它类型用着再添加
		private String code;
		private String type;
		UserRoperty(String code,String type){
			this.code = code;
			this.type = type;
		}
		public String getCode(){
			return code;
		}
		public String getType(){
			return type;
		}
    }
	/**
	 * 车辆所属类型
	 * 描述: 
	 * 版权: Copyright (c) 2017
	 * 公司: 科陆电子
	 * 作者: gaohui
	 * 版本: 2.0.0
	 * 创建日期: 2017年8月17日
	 */
	public enum BelongsType{
		SOL("1", "社会车辆"),
		COM("2", "企业车辆");
		private String code;
		private String type;
		BelongsType(String code,String type){
			this.code = code;
			this.type = type;
		}
		public String getCode(){
			return code;
		}
		public String getType(){
			return type;
		}
	}
}

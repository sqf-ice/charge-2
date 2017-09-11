package com.clouyun.charge.modules.charge.vo;

public class AbnormalBill{
	
	/**异常订单类型,1:APP桩已经停止充电,2:刷卡异常订单,3:APP正在充电中的 */
	private int type=3;
	
	/**订单ID */
	private int billPayId;
	/**订单编号 */
	private String billPayNo;
	/**充电电量 */
	private Double chgPower;
	/**充电金额 */
	private Double amount;
	/**充电时长  */
	private Integer useTime;
	/**支付状态  1待结算 */
	private String payState;
	/**支付方式：5刷卡支付 */
	private String payType;
	/**刷卡订单描述信息 */
	private String billDesc;
	private byte orderStatus;
	
	/**会员名称 */
	private String consName;
	/**会员手机号码 */
	private String consPhone;
	/**场站名称 */
	private String stationName;
	/**运营商名称 */
	private String orgName;
	
	/**桩编号 */
	private String pileNo;
	/**桩名称 */
	private String pileName;
	/**桩通讯地址 */
	private String pileAddr;
	/**枪编号 */
	private Byte gunNo;
	
	/**订单创建时间 */
	private String createTime;
	/**订单开始时间 */
	private String startTime;
	/**订单结束时间 */
	private String endTime;
	
	
	
	
	@Override
	public String toString() {
		return "AbnormalBill [type=" + type + ", billPayId=" + billPayId + ", billPayNo=" + billPayNo + ", chgPower="
				+ chgPower + ", amount=" + amount + ", useTime=" + useTime + ", payState=" + payState
				+ ", payType=" + payType + ", billDesc=" + billDesc + ", consName=" + consName + ", stationName="
				+ stationName + ", orgName=" + orgName + ", pileNo=" + pileNo + ", pileName=" + pileName + ", pileAddr="
				+ pileAddr + ", gunNo=" + gunNo + ", createTime=" + createTime + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getBillPayId() {
		return billPayId;
	}
	public void setBillPayId(int billPayId) {
		this.billPayId = billPayId;
	}
	public String getBillPayNo() {
		return billPayNo;
	}
	public void setBillPayNo(String billPayNo) {
		this.billPayNo = billPayNo;
	}
	public Double getChgPower() {
		return chgPower;
	}
	public void setChgPower(Double chgPower) {
		this.chgPower = chgPower;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getUseTime() {
		return useTime;
	}
	public void setUseTime(Integer useTime) {
		this.useTime = useTime;
	}
	public String getConsName() {
		return consName;
	}
	public void setConsName(String consName) {
		this.consName = consName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getPileNo() {
		return pileNo;
	}
	public void setPileNo(String pileNo) {
		this.pileNo = pileNo;
	}
	public String getPileName() {
		return pileName;
	}
	public void setPileName(String pileName) {
		this.pileName = pileName;
	}
	public String getPileAddr() {
		return pileAddr;
	}
	public void setPileAddr(String pileAddr) {
		this.pileAddr = pileAddr;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getPayState() {
		return payState;
	}
	public void setPayState(String payState) {
		this.payState = payState;
	}
	public String getPayType() {
		return payType;
	}
	public byte getPayTypeValue(){
		if(payType!=null && payType.matches("\\d"))
			return Byte.valueOf(payType);
		return 0;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBillDesc() {
		return billDesc;
	}
	public void setBillDesc(String billDesc) {
		this.billDesc = billDesc;
	}
	public Byte getGunNo() {
		return gunNo;
	}
	public void setGunNo(Byte gunNo) {
		this.gunNo = gunNo;
	}
	public byte getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(byte orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getConsPhone() {
		return consPhone;
	}
	public void setConsPhone(String consPhone) {
		this.consPhone = consPhone;
	}
	
	

}

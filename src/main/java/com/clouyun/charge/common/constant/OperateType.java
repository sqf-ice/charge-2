package com.clouyun.charge.common.constant;


public enum OperateType {
	longin( "登陆", "longin"),
	longinOut( "退出登陆", "longinOut"),
	del( "删除", "delete"),
	diss( "无效", "diss"),
	update( "修改", "update"),
	copy( "复制", "copy"),
	edit( "编辑", "update"),
	add( "新建", "add"),
	scan( "浏览", "scan"),
	freeze( "冻结", "freeze"),
	commopr( "通信", "commopr"),
	dividApproval("分成合约审批","dividApproval"),
	monthlyApproval("月结合约审批","monthlyApproval"),
	batchImport("批量导入", "batchImport"),
	set("设置", "set"),
	createTemplate("创建模板", "createTemplate"),
	chooseVerFile("选择版本文件", "chooseVerFile"),
	updateVersion("更新版本", "updateVersion"),
	
	
	readCard("读卡操作","readCard"),
	card("开卡操作","card"),
	unlock("解锁操作","unlock"),
	recharge("充值操作","recharge"),
	setSerialPort("设置串口","setSerialPort"),
	
	applyWithdrawals("提现申请","applyWithdrawals"),
	abnormalSettlement("异常结算","abnormalSettlement"),
	
	approval("审批","approval");
	
	public String OperateName;

	public String OperateId;

	OperateType(String OperateName, String OperateId) {
		this.OperateName = OperateName;
		this.OperateId = OperateId;
	}
	
	public static void main(String[] args){
		System.out.print(OperateType.valueOf("monthlyApproval").OperateName);
	}
}

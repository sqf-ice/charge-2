package com.clouyun.charge.modules.charge.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.BigDecimalUtils;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.mapper.ChargeInMapper;
import com.clouyun.charge.modules.document.service.GunService;
import com.clouyun.charge.modules.document.service.StationService;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Service
public class ChargeInService {
	
	@Autowired
	ChargeInMapper  chargeInMapper;
	@Autowired
	GunService gunService;
	@Autowired
	DictService dictService;
	@Autowired
	UserService userService;
	@Autowired
	StationService stationService;
	 /**
     * 充电收入查询
     */
    public PageInfo selectCdsr(DataVo vo)throws  Exception {
		List<DataVo> list = selectCdsrList(vo);//充电收入集合
		return new PageInfo(list);
    }

	/**
	 * 充电收入集合
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<DataVo> selectCdsrList(DataVo vo)throws  Exception {
		ChargeManageUtil.getUserRoleData(vo,RoleDataEnum.STATION.dataType,"stationIds");//得到用户场站id
		ChargeManageUtil.setPageInfo(vo);//开启分页
		List<DataVo> dvList = chargeInMapper.selectCdsr(vo);//场站充电信息
		if(dvList!=null&&dvList.size()>0){
			String[] str = {"hylb","zfzt","shoukfs"};//会员类型字典(hylb) ,支付状态字典(zfzt),支付方式字典(shoukfs),设备类型字典(cdzlx)
			Map<String,List<ComboxVo>> dicMap =   ChargeManageUtil.getDictMap(str);//获取字典表的值
			Set gunIdSet  = new HashSet();//枪id集合
			for (DataVo dv :dvList ){
				dv.put("gumPoint","");//设置枪口编号为空
				gunIdSet.add(dv.getString("pileId"));//得到枪集合
				ChargeManageUtil.getUseTime(dv);//得到充电时长
				ChargeManageUtil.setDataVoPut("consTypeCode",dicMap.get("hylb"),dv);;//会员类型
				ChargeManageUtil.setDataVoPut("payState",dicMap.get("zfzt"),dv);//支付状态
				ChargeManageUtil.setDataVoPut("payType",dicMap.get("shoukfs"),dv);//支付方式
//				ChargeManageUtil.setDataVoPut("pileType",dicMap.get("cdzlx"),dv);//设备类型
				//修改为交流，直流，交直一体
				setPileType(dv);
			}
			DataVo setMap  = new DataVo();
			setMap.put("gunList",gunIdSet);
			List<DataVo> gunList = chargeInMapper.gunList(setMap);//枪信息
			if(gunList!=null&&gunList.size()>0){
				for (DataVo list :dvList ){
					for(DataVo listVo : gunList){
						//枪id和innerId配置得到枪口编号
						if(list.getString("pileId").equalsIgnoreCase(listVo.getString("pileId"))&&list.getString("innerId").equalsIgnoreCase(listVo.getString("innerId"))){
							list.put("gumPoint",listVo.getString("gumPoint"));
							continue;
						}
					}
				}
			}

		}

    	return dvList;
	}
	/**
	 * 充电收入合计
	 */
	public   DataVo  chargeCount(DataVo vo) throws BizException {
		DataVo  count = new DataVo();
		vo.remove("pageNum");
		vo.remove("pageSize");

		// 此处应根据登录用户ID获取到能查看的企业，未实现
		ChargeManageUtil.getUserRoleData(vo,RoleDataEnum.STATION.dataType,"stationIds");//得到用户场站id

		DataVo dvListCount = chargeInMapper.selectCdsrCount(vo);//合计
		if(dvListCount==null){
			dvListCount = new DataVo();
		}
		count.put("chgPower",ChargeManageUtil.df.format(dvListCount.getDouble("chgPower")));
		count.put("amount",ChargeManageUtil.df.format( dvListCount.getDouble("amount")));
		count.put("elceFee",ChargeManageUtil.df.format(dvListCount.getDouble("elceFee")));
		count.put("parkFee",ChargeManageUtil.df.format(dvListCount.getDouble("parkFee")));
		count.put("servFee",ChargeManageUtil.df.format(dvListCount.getDouble("servFee")));
		count.put("discountFee",ChargeManageUtil.df.format(dvListCount.getDouble("discountFee")));
       return  count;
	}

	/**
	 * 充电收入导出
	 * @param map
	 * @param response
	 */
	public void exportCdsr(DataVo map, HttpServletResponse response) throws Exception{
		List dvList=  selectCdsrList(map);//充电收入集合
		DataVo count=  chargeCount(map);//充电合计集合
		List<String>	headList =  new ArrayList<>();
		List<String> valList = new ArrayList<>();
			headList.add("运营商");
			headList.add("订单编号");
			headList.add("消费金额(元)");
			headList.add("充电电量(kW·h)");
			headList.add("集团名称");
			headList.add("会员名称");
			headList.add("会员类型");
			headList.add("充值卡号");
			headList.add("电费(元)");
			headList.add("服务费(元/kW·h)");
			headList.add("停车费(元)");
			headList.add("优惠折扣(元)");
			headList.add("订单状态");
			headList.add("支付方式");
			headList.add("支付时间");
			headList.add("场站名称");
			headList.add("设备类型");
			headList.add("设备编号");
			headList.add("设备名称");
			headList.add("手机号");
			headList.add("订单时间");
			headList.add("开始充电时间");
			headList.add("结束充电时间");
			headList.add("充电时长");
			headList.add("备注");
			headList.add("枪口编号");
			valList.add("orgName");//运营商
			valList.add("billPayNo");//订单编号
			valList.add("amount");//消费金额
			valList.add("chgPower");//充电电量
			valList.add("groupName");//集团名称
			valList.add("consName");//会员名称
			valList.add("consTypeCode");//会员类型
			valList.add("rechargeCard");//充值卡号
			valList.add("elceFee");//电费
			valList.add("servFee");//服务费
			valList.add("parkFee");//停车费
			valList.add("discountFee");//优惠折扣
			valList.add("payState");//订单状态
			valList.add("payType");//支付方式
			valList.add("finishTime");//支付时间
			valList.add("stationName");//场站名称
			valList.add("pileType");//设备类型
			valList.add("pileNo");//设备编号
			valList.add("pileName");//设备名称
			valList.add("consPhone");//手机号
			valList.add("createTime");//订单时间
			valList.add("startTime");//开始充电时间
			valList.add("endTime");//结束充电时间
			valList.add("useTime");//充电时长
			valList.add("outBillNo");//备注
			valList.add("gumPoint");//备注
		    count.put("orgName","合计");
		    dvList.add(count);
		ExportUtils.exportExcel(dvList,response,headList,valList,"充电收入表");
	}

	/**
	 * 分成收入查询
	 */
	public PageInfo selectFcsr(DataVo vo) throws BizException {
		List<DataVo> lists = selectFcsrList(vo);//得到分成收入List
		return new PageInfo(lists);
	}

	/**
	 * 分成收入list
	 * @param vo
	 * @return
	 * @throws BizException
	 */
	public  List<DataVo> selectFcsrList(DataVo vo) throws BizException {
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType); //得到场站Id
		ChargeManageUtil.setPageInfo(vo);//开启分页
		List<DataVo>	lists= chargeInMapper.selectFcsr(vo);//查询分成list
		if(lists!=null&&lists.size()>0){
			List<ComboxVo> boxList1  =dictService.getDictByType("hylx");//会员类型表
			Set<Integer> contractIds = new HashSet<Integer>();
			for(DataVo list :lists){
				contractIds.add(list.getInt("contractId"));
			}
			DataVo contractVo = new DataVo();
			contractVo.put("contractIds", contractIds);
			List<DataVo> cConCompanyList = chargeInMapper.getcConCompanyList(contractVo);
			Map<Integer,List<DataVo>> cConCompanyMap = new HashMap();
			List<DataVo> cConCompanys = null;
			Integer contractId;
			for(DataVo cConCompany : cConCompanyList){
				contractId = cConCompany.getInt("contractId");
				cConCompanys = cConCompanyMap.get(contractId);
				if(cConCompanys == null){
					cConCompanys = new ArrayList<DataVo>();
				}
				cConCompanys.add(cConCompany);
				cConCompanyMap.put(contractId, cConCompanys);
			}
			for(DataVo list :lists){
				ChargeManageUtil.setDataVoPut("contractType",boxList1,list);;//合约类型
				contractId= list.getInt("contractId");
				List<DataVo> cConCompany =	cConCompanyMap.get(contractId);
				Double czfcPercentage = 100d;
				Double ditcsf = Double.parseDouble(list.get("ditcsf").toString());
				StringBuffer sb = new StringBuffer();
				String br = "<br/>";
				if(cConCompany != null && !cConCompany.isEmpty()){
					for(DataVo ic : cConCompany){
						sb.append(ic.getString("cConCompanyName")+" & ");
						sb.append(ChargeManageUtil.df.format( ic.getDouble("percentage"))+"% & ");
						Double fcje = 0.00;
						if(ic.isNotBlank("percentage")){
							fcje=(ic.getDouble("percentage")*ditcsf)/100;
						}
						sb.append(ChargeManageUtil.df.format( fcje)+br);
						czfcPercentage = getSubtraction(czfcPercentage,ic.getDouble("percentage"));
					}
				}
				sb.append(list.getString("orgName")+" & "+ ChargeManageUtil.df.format( czfcPercentage) +"% & "+ChargeManageUtil.df.format((czfcPercentage*ditcsf)/100));
				list.put("ditip",ChargeManageUtil.df.format((czfcPercentage*ditcsf)/100));
				list.put("fcje",sb.toString());
				list.put("percentage",czfcPercentage);
			}
		}
		return lists;
	}

	/**
	 * 分成收入合计
	 * @param vo
	 * @return
	 */
  public  DataVo selectFcsrCount(DataVo vo) throws BizException {
	  Double	diep=0.0;
	  Double	dicp=0.0;
	  Double	dicl=0.0;
	  Double	conEff=0.0;
	  Double	diti=0.0;
	  Double	ditsf=0.0;
	  Double	diepcs=0.0;
	  Double	ditip=0.0;

	  ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType); //得到场站Id

	  List<DataVo>	listsCount= chargeInMapper.selectFcsrCount(vo);
	  if(listsCount!=null&&listsCount.size()>0){
		  for(DataVo list :listsCount){
			  diep+=list.getDouble("diep");//设备用电量
			  dicp+=list.getDouble("dicp");//设备充电量
			  dicl+=list.getDouble("dicl");//设备损耗
			  conEff+=list.getDouble("conEff");//充电效率
			  diti+=list.getDouble("diti");//充电收入
			  ditsf+=list.getDouble("ditsf");//充电侧分成服务费
			  diepcs+=list.getDouble("diepcs");//用电侧分成服务费
			  ditip+=list.getDouble("ditcsf");//分成收入
		  }
	  }
	  DataVo count = new DataVo();
	  count.put("diep",ChargeManageUtil.df.format(diep));
	  count.put("dicp",ChargeManageUtil.df.format(dicp));
	  count.put("dicl",ChargeManageUtil.df.format(dicl));
	  count.put("conEff",ChargeManageUtil.df.format(conEff));
	  count.put("diti",ChargeManageUtil.df.format(diti));
	  count.put("ditsf",ChargeManageUtil.df.format(ditsf));
	  count.put("diepcs",ChargeManageUtil.df.format(diepcs));
	  count.put("ditip",ChargeManageUtil.df.format(ditip));
	  return count;

  }

	/**
	 *  分成收入导出
	 * @param map
	 * @param response
	 */
	public void exportFcsr(DataVo map, HttpServletResponse response) throws Exception{
		List<DataVo> dvList=selectFcsrList(map);
		DataVo count = selectFcsrCount(map);
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("结算日期");
		headList.add("运营商");
		headList.add("合约名称");
		headList.add("合约场站");
		headList.add("合约类型");
		headList.add("设备用电量");
		headList.add("设备充电量");
		headList.add("设备损耗");
		headList.add("充电效率");
		headList.add("充电收入");
		headList.add("充电侧分成服务费");
		headList.add("用电侧分成服务费");
		headList.add("分成收入");
		headList.add("合约企业&分成比例&分成金额 ");
		valList.add("did");
		valList.add("orgName");
		valList.add("contractName");
		valList.add("stationName");
		valList.add("contractType");
		valList.add("diep");
		valList.add("dicp");
		valList.add("dicl");
		valList.add("conEff");
		valList.add("diti");
		valList.add("ditsf");
		valList.add("diepcs");
		valList.add("ditip");
		valList.add("fcje");
		count.put("did","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList,response,headList,valList,"分成收入表");
	}

	/**
	 * 分成收入详情查询
	 * @throws BizException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageInfo detailIncome(Map data) throws BizException {
		PageInfo pageInfo = null;
		DataVo vo = new DataVo(data);
		if(vo.isBlank("nodeId")){
			throw new BizException(1700000,"合约id");
		}
		if(vo.isBlank("stationId")){
			throw new BizException(1700000,"场站id");
		}
		if(vo.isBlank("did")){
			throw new BizException(1700000,"日期");
		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		Map nodeMap  = new HashMap();
		nodeMap.put("contractId",vo.getInt("nodeId"));
		List<DataVo> InConTractCompany = chargeInMapper.selectDetailIncome(nodeMap);
		nodeMap.put("nodeId",vo.getString("nodeId"));
		nodeMap.put("did", vo.getString("did"));//少传的日期
		nodeMap.put("stationId", vo.getInt("stationId"));
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> dailyIncomeList = chargeInMapper.selectDailyIncome(nodeMap);//---正常
//		vo.remove("pageNum");
//		vo.remove("pageSize");
		if(null!=dailyIncomeList && dailyIncomeList.size()>0){
			for(DataVo dailyVo : dailyIncomeList) {
				Double ditcsf = dailyVo.getDouble("ditcsf");
//				Map map = new HashMap();
				Double czfcPercentage = 100d;
//			    map.put("contractId", dailyVo.getString("contractid"));
				String fcjeList ="";
				if(null!=InConTractCompany && InConTractCompany.size()>0){
					for (DataVo ic : InConTractCompany) {
						String str = "";
						str += ic.getString("cConCompanyName") + ic.getString("percentage").toString() + "% & ";
						Double fcje = 0.00;
						if (ic.isNotBlank("percentage")) {
							Double percentage = ic.getDouble("percentage");
							fcje = Double.parseDouble(numberFormat.format((percentage * ditcsf) / 100));
							str += fcje;
							fcjeList+=str;
							//fcjeList.add(str);
							czfcPercentage = getSubtraction(czfcPercentage, ic.getDouble("percentage"));
						}
						String czfcStr = dailyVo.getString("orgname") + " & " + czfcPercentage + "% & " + numberFormat.format((czfcPercentage * ditcsf) / 100);
						fcjeList+=czfcStr;
						//fcjeList.add(czfcStr);
					}
				}
//			    hm.put(di.getString("pileid"), fcjeList);
//			    hmList.put("hm",hm);
				dailyVo.put("hm",fcjeList);
			}

			pageInfo  = new PageInfo(dailyIncomeList);
		}
		//dailyIncomeList.add(hmList);
		//pageInfo.setList(dailyIncomeList);
		return   pageInfo;
	}

	/**
	 * 发票管理查询
	 */
	public PageInfo selectFpgl(DataVo vo) throws BizException {
		List<DataVo>  lists= selectFpglList(vo);//得到发票list
		return new PageInfo(lists);
	}

	/**
	 *得到发票处理list
	 * @param vo
	 * @throws BizException
	 */
	public 	List<DataVo> selectFpglList(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo,RoleDataEnum.ORG.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> lists =	chargeInMapper.selectFpgl(vo);
		if(lists!=null&&lists.size()>0){
			List<ComboxVo> boxList  =dictService.getDictByType("isInvoice");//发票处理字典
			for (DataVo list : lists ){
				list.put("canAmount",getCanInvoiceAmount(list.getString("consId")));
				ChargeManageUtil.setDataVoPut("isInvoice",boxList,list);
			}
		}
		return lists;
	}

	/**
	 * 发票处理合计
	 * @param vo
	 * @return
	 */
	public double selectFpglCount(DataVo vo) throws BizException {
		vo.remove("pageNum");
		vo.remove("pageSize");

		ChargeManageUtil.orgIdsCondition(vo,RoleDataEnum.ORG.dataType);

		String amount = chargeInMapper.selectFpglCount(vo);
		double applyMoney=0.00;
		if(amount!=null&&amount!=""){
			try {
				applyMoney= Double.parseDouble(amount);
			}catch (Exception e){
				applyMoney=0.00;
			}
		}
		return   applyMoney;//得到发票处理合计;
	}
	/**
	 * 发票管理导出
	 * @param map
	 * @param response
	 */

	public void exportFpgl(DataVo map, HttpServletResponse response) throws Exception{
		List<DataVo> dvList=selectFpglList(map);
		double applyMoney= selectFpglCount(map);
		DataVo count = new DataVo();
		count.add("applyMoney",applyMoney);
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
			headList.add("会员名称");
			headList.add("开票金额(元)");
			headList.add("购方名称");
			headList.add("可开票金额(元)");
			headList.add("申请时间");
			headList.add("处理状态");
			headList.add("收件地址");
			headList.add("手机号");
			headList.add("收件人姓名");
			valList.add("consName");
			valList.add("applyMoney");
			valList.add("buyName");
			valList.add("canAmount");
			valList.add("applyTime");
			valList.add("isInvoice");
			valList.add("recipientAddr");
			valList.add("recipientPhone");
			valList.add("recipientName");
		count.put("consName","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList,response,headList,valList," 发票管理表");
	}

	/**
	 * 发票信息详情
	 * @param invoiceId
	 * @return
	 */
	public DataVo detailFpgl(Integer invoiceId) {
		return chargeInMapper.detailFpgl(invoiceId);
	}

	/**
	 * 场站收入查询
	 */
	public PageInfo selectCzsr(DataVo vo) throws Exception {
		 List<DataVo> dvList = selectCzsrList(vo);
		 PageInfo pageInfo  = new PageInfo(dvList);
		 fillterData(dvList);//收入计算
	     DataVo count =selectCzsrCount(vo);
         DataVo returnMap = new DataVo();
		 returnMap.put("count",count);
		 returnMap.put("czsr",dvList);
         List<DataVo>  returnList = new ArrayList<>();
		 returnList.add(returnMap);
		 pageInfo.setList(returnList);
		 return pageInfo;
	}

	/**
	 * 场站收入List
	 * @param vo
	 * @return
	 */
	public List<DataVo> selectCzsrList(DataVo vo) throws BizException {
//		ChargeManageUtil.setPageInfo(vo);
//		ChargeManageUtil.getUserRoleData(vo,RoleDataEnum.ORG.dataType,"orgIds");
//		List<DataVo> dvList = null;
//		if(vo.isBlank("orgId")){
//			dvList=chargeInMapper.selectCzsrOrg(vo);//根据orgId分组结果
//		}else {
//			ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
//			dvList=chargeInMapper.selectCzsrStation(vo);//根据场站Id分组
//		}
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
		List<DataVo> 	dvList=chargeInMapper.selectCzsrStation(vo);//根据场站Id分组
		return dvList;
	}
	/**
	 * 场站收入合计
	 * @param vo
	 * @return
	 */
   	public DataVo selectCzsrCount(DataVo vo) throws BizException {
   		vo.remove("pageNum");
   		vo.remove("pageSize");

		List<DataVo> dvListCount=chargeInMapper.selectCzsrCount(vo);//收入合计
		if(dvListCount!=null&&dvListCount.size()>0){
			fillterData(dvListCount);
		}
		return  dvListCount.get(0);
	}
	/**
	 * 场站收入导出
	 * @param map
	 * @param response
	 */
	public void exportCzsr(DataVo map, HttpServletResponse response)throws Exception {
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		List dvlist=selectCzsrList(map);
		DataVo count = selectCzsrCount(map);
		headList.add("运营商");
		headList.add("场站编号");
		headList.add("场站名");
		headList.add("日期");
		headList.add("总收入(元)");
		headList.add("电费总收入(元)");
		headList.add("非费率总收入(元)");
		headList.add("尖价总收入(元)");
		headList.add("峰价总收入(元)");
		headList.add("平价总收入(元)");
		headList.add("谷价总收入(元)");
		headList.add("服务费收入(元/kW·h)");
		headList.add("停车费收入(元)");
		headList.add("充电总量(kW·h)");
		headList.add("非费率电量(kW·h)");
		headList.add("尖价总量(kW·h)");
		headList.add("峰价总量(kW·h)");
		headList.add("平价总量(kW·h)");
		headList.add("谷价总量(kW·h)");
		valList.add("orgName");//运营是名称
		valList.add("stationNo");//场站编号
		valList.add("stationName");//场站名
		valList.add("endTime");//场站名
		valList.add("amount");//总收入(元)
		valList.add("elceFee");//电费总收入(元)
		valList.add("noElceFee");//非费率总收入(元)
		valList.add("elceFee1");//尖价总收入(元)
		valList.add("elceFee2");//峰价总收入(元)
		valList.add("elceFee3");//平价总收入(元)
		valList.add("elceFee4");//谷价总收入(元)
		valList.add("servFee");//服务费收入(元/kW·h)
		valList.add("parkFee");//停车费收入(元)
		valList.add("chgPower");//充电总量(kW·h)
		valList.add("noChgPower");//非费率电量(kW·h)
		valList.add("powerZxyg1");
		valList.add("powerZxyg2");
		valList.add("powerZxyg3");
		valList.add("powerZxyg4");
		count.put("orgName","合计");
		dvlist.add(count);
		ExportUtils.exportExcel(dvlist,response,headList,valList,"场站收入表");
	}
    public PageInfo detaiStationDate(DataVo vo) throws BizException {
        List<DataVo> dvList = detaiStationDateList(vo);
        PageInfo pageInfo  = new PageInfo(dvList);
        fillterData(dvList);//收入计算
        DataVo count =selectCzsrCount(vo);
        DataVo returnMap = new DataVo();
        returnMap.put("count",count);
        returnMap.put("czsr",dvList);
        List<DataVo>  returnList = new ArrayList<>();
        returnList.add(returnMap);
        pageInfo.setList(returnList);
        return pageInfo;
    }
    /**
     * 场站收入导出
     * @param map
     * @param response
     */
    public void exportCzsrDate(DataVo map, HttpServletResponse response)throws Exception {
        List<String>	headList =  new ArrayList<>();
        List<String>	valList =  new ArrayList<>();
        List dvlist=detaiStationDateList(map);
		fillterData(dvlist);
        DataVo count = selectCzsrCount(map);
        headList.add("运营商");
        headList.add("场站编号");
        headList.add("场站名");
        headList.add("日期");
        headList.add("总收入(元)");
        headList.add("电费总收入(元)");
        headList.add("非费率总收入(元)");
        headList.add("尖价总收入(元)");
        headList.add("峰价总收入(元)");
        headList.add("平价总收入(元)");
        headList.add("谷价总收入(元)");
        headList.add("服务费收入(元/kW·h)");
        headList.add("停车费收入(元)");
        headList.add("充电总量(kW·h)");
        headList.add("非费率电量(kW·h)");
        headList.add("尖价总量(kW·h)");
        headList.add("峰价总量(kW·h)");
        headList.add("平价总量(kW·h)");
        headList.add("谷价总量(kW·h)");
        valList.add("orgName");//运营是名称
        valList.add("stationNo");//场站编号
        valList.add("stationName");//场站名
        valList.add("endTime");//场站名
        valList.add("amount");//总收入(元)
        valList.add("elceFee");//电费总收入(元)
        valList.add("noElceFee");//非费率总收入(元)
        valList.add("elceFee1");//尖价总收入(元)
        valList.add("elceFee2");//峰价总收入(元)
        valList.add("elceFee3");//平价总收入(元)
        valList.add("elceFee4");//谷价总收入(元)
        valList.add("servFee");//服务费收入(元/kW·h)
        valList.add("parkFee");//停车费收入(元)
        valList.add("chgPower");//充电总量(kW·h)
        valList.add("noChgPower");//非费率电量(kW·h)
        valList.add("powerZxyg1");
        valList.add("powerZxyg2");
        valList.add("powerZxyg3");
        valList.add("powerZxyg4");
        count.put("orgName","合计");
        dvlist.add(count);
        ExportUtils.exportExcel(dvlist,response,headList,valList,"场站收入详情表");
    }
    /**
     * 场站收入详情列表
     * @param vo
     * @return
     */
    public  List<DataVo> detaiStationDateList(DataVo vo) {
        ChargeManageUtil.setPageInfo(vo);//开启分页
        List<DataVo> list = chargeInMapper.detaiStationDate(vo); //得到场站详情集合
        return list;
    }

	/**
	 * 场站收入汇总详情
	 */
	public PageInfo detaiStation(DataVo vo) {
	    	ChargeManageUtil.setPageInfo(vo);//开启分页
			List<DataVo> list = chargeInMapper.detaiStation(vo);//得到场站汇总集合
		if(list!=null&&list.size()>0){
			for(DataVo bp : list){
				if( bp.isNotBlank("startTime")){
					bp.put("checkState",bp.getString("startTime"));
				}
				if( bp.isNotBlank("endTime")){
					bp.put("isStop",bp.getString("endTime"));
				}

				//格式化
				bp.put("prcZxyg1",ChargeManageUtil.df.format(bp.getDouble("prcZxygz1")*bp.getDouble("powerZxyg1")));
				bp.put("prcZxyg2",ChargeManageUtil.df.format(bp.getDouble("prcZxygz2")*bp.getDouble("powerZxyg2")));
				bp.put("prcZxyg3",ChargeManageUtil.df.format(bp.getDouble("prcZxygz3")*bp.getDouble("powerZxyg3")));
				bp.put("prcZxyg4",ChargeManageUtil.df.format(bp.getDouble("prcZxygz4")*bp.getDouble("powerZxyg4")));
			}
		}
			return  new PageInfo(list);
	}




	/**
	 * 场站收入汇总导出
	 * @param vo
	 * @param response
	 */
	public void detailStationExport(DataVo vo, HttpServletResponse response)throws Exception {
		//获取费率时段
		Map<String, List<Byte>> rateTimeTemplate = getRateTimeTemplate();
		List<DataVo> dvList=detaiStation(vo).getList();
		for (DataVo map : dvList) {
  //得到卡号
		if(map.getInt("payType")==5){//为5卡号
				map.put("hyCardOrPhone",map.getString("rechargeCard"));
			}else {
				map.put("hyCardOrPhone",map.getString("consPhone"));
			}
			Calendar startTime = Calendar.getInstance();

			//尖峰平谷 充电量
			startTime.setTime(map.getDate("startTime"));
			Calendar endTime = Calendar.getInstance();
			endTime.setTime(map.getDate("endTime"));
			long startMillis = 0;
			if(startTime != null){
				startMillis = startTime.getTimeInMillis();
			}
			long endMillis = 0;
			if(endTime != null){
				endMillis = endTime.getTimeInMillis();
			}
			Map<String, Long> rateTimeLen = rateTimeLen(startMillis, endMillis, rateTimeTemplate);
			//尖价时长为0
			map.put("powerZxyg1Time", 0);
			if(rateTimeLen.get("平") != null){
				Double val = rateTimeLen.get("平").doubleValue();
				Double resultVal  =  val / 60;
				BigDecimal b = new BigDecimal(resultVal);
				map.put("powerZxyg3Time", b.setScale(1, BigDecimal.ROUND_HALF_UP));
			}else{
				map.put("powerZxyg3Time", 0);
			}
			if(rateTimeLen.get("峰") != null){
				Double val = rateTimeLen.get("峰").doubleValue();
				Double resultVal  =  val / 60;
				BigDecimal b = new BigDecimal(resultVal);
				map.put("powerZxyg2Time",  b.setScale(1, BigDecimal.ROUND_HALF_UP));
			}else{
				map.put("powerZxyg2Time", 0);
			}
			if(rateTimeLen.get("谷") != null){
				Double val = rateTimeLen.get("谷").doubleValue();
				Double resultVal  =  val / 60;
				BigDecimal b = new BigDecimal(resultVal);
				map.put("powerZxyg4Time",  b.setScale(1, BigDecimal.ROUND_HALF_UP));
			}else{
				map.put("powerZxyg4Time", 0);
			}

		}
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("订单号");
		headList.add("充电场站名称");
		headList.add("设备名称");
		headList.add("订单日期");
		headList.add("充电开始时间");
		headList.add("充电结束时间 ");
		headList.add("会员卡号/电话");
		headList.add("会员名称");
		headList.add("集团名");
		headList.add("车牌号");
		headList.add("线路");
		headList.add("初始SOC");
		headList.add("结束SOC");
		headList.add("充电量(kW·h)");
		headList.add("尖价电量(kW·h)");
		headList.add("尖价充电时长(分钟)");
		headList.add("峰价电量(kW·h)");
		headList.add("峰价充电时长(分钟)");
		headList.add("平价电量(kW·h)");
		headList.add("平价充电时长(分钟)");
		headList.add("谷价电量(kW·h)");
		headList.add("谷价充电时长(分钟)");
		headList.add("消费金额(元)");
		headList.add("尖价电费(元)");
		headList.add("峰价电费(元)");
		headList.add("平价电费(元)");
		headList.add("谷价电费(元)");
		headList.add("服务费(元)");
		valList.add("billPayNo");
		valList.add("stationName");
		valList.add("pileName");
		valList.add("createTime");
		valList.add("startTime");
		valList.add("endTime");
		valList.add("hyCardOrPhone");
		valList.add("consName");
		valList.add("groupName");
		valList.add("licensePlate");
		valList.add("line");
		valList.add("socBeg");
		valList.add("socEnd");
		valList.add("chgPower");
		valList.add("powerZxyg1");
		valList.add("powerZxyg1Time");
		valList.add("powerZxyg2");
		valList.add("powerZxyg2Time");
		valList.add("powerZxyg3");
		valList.add("powerZxyg3Time");
		valList.add("powerZxyg4");
		valList.add("powerZxyg4Time");
		valList.add("amount");
		valList.add("prcZxyg1");
		valList.add("prcZxyg2");
		valList.add("prcZxyg3");
		valList.add("prcZxyg4");
		valList.add("servFee");
		ExportUtils.exportExcel(dvList,response,headList,valList,"场站汇总表");
	}
	/**
	 * 待结收入查询
	 */
	public PageInfo selectDjsr(DataVo vo) throws Exception {

		List<DataVo> dvList = selectDjsrList(vo);//待结收入集合
		PageInfo  pageInfo = new PageInfo(dvList);//分页值
		List<DataVo> returnList = new ArrayList<>();
		DataVo returnMap = new DataVo();
		returnMap.add("djsr",dvList);
		returnMap.add("count",selectDjsrCount(vo));//待结收入合计
		returnList.add(returnMap);
		pageInfo.setList(returnList);
		return pageInfo;
	}

	/**
	 * 待结收入查询
	 * @param vo
	 * @return
	 */
	public   List<DataVo> selectDjsrList(DataVo vo) throws BizException {
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);//得到场站
		ChargeManageUtil.setPageInfo(vo);//开启分页
		List<DataVo> dvList = chargeInMapper.selectDjsr(vo);//待结list
		if(dvList!=null&&dvList.size()>0){
			String[] str = {"hylb","zfzt","shoukfs"};//会员类型字典(hylb) ,支付状态字典(zfzt),支付方式字典(shoukfs),设备类型字典(cdzlx)
			Map<String,List<ComboxVo>> dicMap =   ChargeManageUtil.getDictMap(str);//获取字典表的值
			for (DataVo dv :dvList ){
				ChargeManageUtil.getUseTime(dv);//得到充电时长
				ChargeManageUtil.setDataVoPut("consTypeCode",dicMap.get("hylb"),dv);;//会员类型
				ChargeManageUtil.setDataVoPut("payState",dicMap.get("zfzt"),dv);//支付状态
				ChargeManageUtil.setDataVoPut("payType",dicMap.get("shoukfs"),dv);//支付方式
//				ChargeManageUtil.setDataVoPut("pileType",dicMap.get("cdzlx"),dv);//设备类型
				setPileType(dv);
			}
		}
		vo.remove("pageNum");
		vo.remove("pageSize");
		return   dvList;
	}

	/**
	 * 待结收入合计
	 * @param vo
	 * @return
	 */
	public   DataVo selectDjsrCount (DataVo vo){
		DataVo dvCount = chargeInMapper.selectDjsrCount(vo);
		DataVo count = new DataVo();
		Double totalPowerCount = 0.0;
		Double totalMoneyCount = 0.0;
		if(dvCount!=null&&dvCount.size()>0){
			totalPowerCount=dvCount.getDouble("amount");
			totalMoneyCount= dvCount.getDouble("chgPower");
		}
		count.put("totalPowerCount",ChargeManageUtil.df.format(totalPowerCount));
		count.put("totalMoneyCount",ChargeManageUtil.df.format(totalMoneyCount));
		return count;
	}

	/**
	 * 待结收入导出
	 * @param map
	 * @param response
	 */
	public void exportDjsr(DataVo map, HttpServletResponse response)throws Exception {
		List<DataVo> dvList= selectDjsrList(map);
		DataVo count = selectDjsrCount(map);
		count.put("chgPower",count.getDouble("totalPowerCount"));
		count.put("amount",count.getDouble("totalMoneyCount"));
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
			headList.add("运营商");
			headList.add("订单编号");
			headList.add("消费金额(元)");
			headList.add("充电电量(kW·h)");
			headList.add("集团名称");
			headList.add("会员名称");
			headList.add("会员类型");
			headList.add("充值卡号");
			headList.add("电费(元)");
			headList.add("服务费(元/kW·h)");
			headList.add("停车费(元)");
			headList.add("订单状态");
			headList.add("支付方式");
			headList.add("支付时间");
			headList.add("场站名称");
			headList.add("设备类型");
			headList.add("设备编号");
			headList.add("设备名称");
			headList.add("手机号");
			headList.add("订单时间");
			headList.add("开始充电时间");
			headList.add("结束充电时间");
			headList.add("充电时长");
			headList.add("备注");
			valList.add("orgName");
			valList.add("billPayNo");
			valList.add("amount");
			valList.add("chgPower");
			valList.add("groupName");
			valList.add("consName");
			valList.add("consTypeCode");
			valList.add("rechargeCard");
			valList.add("elceFee");
			valList.add("servFee");
			valList.add("parkFee");
			valList.add("payState");
			valList.add("payType");
			valList.add("finishTime");
			valList.add("stationName");
			valList.add("pileType");
			valList.add("pileNo");
			valList.add("pileName");
			valList.add("consPhone");
			valList.add("createTime");
			valList.add("startTime");
			valList.add("endTime");
			valList.add("useTime");
			valList.add("outBillNo");
		count.put("orgName","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList,response,headList,valList,"待结收入表");
	}
	/**
	 * 第三方收入查询
	 * @param vo
	 * @return
	 */
	public PageInfo selectCdsrCoop(DataVo vo) throws BizException {
		List<DataVo>	dvList = selectCdsrCoopList(vo);//得到待结收入list
		return new PageInfo(dvList);
	}

	/**
	 * 第三方收入list
	 * @param vo
	 * @return
	 */
	public List<DataVo>  selectCdsrCoopList(DataVo vo) throws BizException {
		List<String> operatorIds =stationService.getOperatorIds(vo.getInt("userId"));
		if(operatorIds!=null&&operatorIds.size()>0){
			vo.put("operatorIds",operatorIds);
		}
		if(vo.getString("operatorId").equalsIgnoreCase("MA5DA0053")){
			vo.put("cdw",1);
		}
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> dvList =  chargeInMapper.selectCdsrCoop(vo);
		vo.remove("pageNum");
		vo.remove("pageSize");
		if(dvList!=null&&dvList.size()>0){
			List<ComboxVo> boxList  =dictService.getDictByType("shoukfs");//支付方式字典
			List<ComboxVo> boxList2  =dictService.getDictByType("zfzt");//支付状态字典
			DataVo stationVo = new DataVo();
			DataVo pubOrgVo = new DataVo();
			Set connectorIds = new HashSet();
			Set stationIds = new HashSet();
			for(DataVo dataVo :dvList){
				ChargeManageUtil.getUseTime(dataVo);//用的时长
				if(dataVo.isBlank("consId")){
					connectorIds.add(dataVo.getString("connectorId"));
				}else {
					stationIds.add(dataVo.getString("consId"));
				}
				ChargeManageUtil.setDataVoPut("payType",boxList,dataVo);
				ChargeManageUtil.setDataVoPut("billStatus",boxList2,dataVo);
			}
			stationVo.put("connectorId",connectorIds);
			stationVo.put("consIds",stationIds);
			List<Map> staionMap  =  chargeInMapper.stationMap(stationVo);  //得到场站
			List<Map> pubOrgMap  =  chargeInMapper.pubOrgMap(pubOrgVo);  //得到企业

			for(DataVo map :dvList) {
				if (map.isBlank("consId")) {//如果用户id不为空
					for (Map sta : staionMap) {
						if (sta.get("connectorId").toString().equalsIgnoreCase(map.get("connectorId").toString())) {
							map.put("equipmentOperatorName", sta.get("orgName"));
							map.put("stationName", sta.get("stationName"));
							map.put("equipmentId", map.get("connectorId").toString().substring(0, map.get("connectorId").toString().length() - 2));
							continue;
						}
					}

				} else {//为空
					for (Map pub : pubOrgMap) {
						if (pub.get("consId").toString().equalsIgnoreCase(map.get("consId").toString())) {
							map.put("equipmentOperatorName", map.get("operatorName"));
							map.put("operatorName", pub.get("orgName"));
							map.put("equipmentId", map.get("connectorId").toString().substring(0, map.get("connectorId").toString().length() - 2));
							continue;
						}
					}
				}
			}
		}
		return dvList;
	}

	/**
	 * 第三方充电合计
	 * @param vo
	 * @return
	 */
	public  DataVo selectCdsrCoopCount(DataVo vo) throws BizException {
		vo.remove("pageNum");
		vo.remove("pageSize");
		DataVo  count = new DataVo();
		double totalPowerCount=0.0;
		double totalMoneyCount=0.0;

        List<String> operatorIds =stationService.getOperatorIds(vo.getInt("userId"));
        if(operatorIds!=null&&operatorIds.size()>0){
            vo.put("operatorIds",operatorIds);
        }

		DataVo dvListCount =  chargeInMapper.selectCdsrCoopCount(vo);
		if(dvListCount!=null&&dvListCount.size()>0){
			totalPowerCount = dvListCount.getDouble("totalPower");
			totalMoneyCount = dvListCount.getDouble("totalMoney");
		}
		count.put("totalPower",ChargeManageUtil.df.format(totalPowerCount));
		count.put("totalMoney",ChargeManageUtil.df.format(totalMoneyCount));
		return count;
	}
	/**
	 * 第三方充电导出
	 * @param data
	 * @param response
	 */
	public void exprotCdsrCoop(DataVo data , HttpServletResponse response)throws Exception {
		List dvList = selectCdsrCoopList(data);
		DataVo  count =           selectCdsrCoopCount(data);
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("客户运营商");
		headList.add("设备运营商");
		headList.add("接口编号");
		headList.add("设备编号");
		headList.add("场站名称");
		headList.add("订单流水号");
		headList.add("消费金额(元)");
		headList.add("充电电量(kW·h)");
		headList.add("支付状态");
		headList.add("支付方式");
		headList.add("开始充电时间");
		headList.add("结束充电时间");
		headList.add("充电时长");
		headList.add("会员名称");
		valList.add("operatorName");
		valList.add("equipmentOperatorName");
		valList.add("connectorId");
		valList.add("equipmentId");
		valList.add("stationName");
		valList.add("startChargeSeq");
		valList.add("totalMoney");
		valList.add("totalPower");
		valList.add("billStatus");
		valList.add("payType");
		valList.add("startTime");
		valList.add("endTime");
		valList.add("useTime");
		valList.add("consName");
		count.put("operatorName","合计");
		dvList.add(count);
		ExportUtils.exportExcel(dvList,response,headList,valList,"第三方充电表");
	}
	/**
	 *  更新发票信息
	 */
	public Integer updateFpgl(Map dv){
		return chargeInMapper.updateFpgl(dv);
	}

    public  void fillterData(List<DataVo> dvList){
		Double noElceFee = 0.00;
		Double noChgPower = 0.00;
		DecimalFormat fnum = new DecimalFormat("##0.00");
         for (DataVo map:dvList){
			 noElceFee = new BigDecimal(map.getString("elceFee")).subtract(new BigDecimal( map.getString("elceFee1")))
					 .subtract(new BigDecimal(map.getString("elceFee2"))).subtract(new BigDecimal(map.getString("elceFee3")))
					 .subtract(new BigDecimal(map.getString("elceFee4"))).doubleValue();
			 noChgPower = new BigDecimal(map.getString("chgPower")).subtract(new BigDecimal(map.getString("powerZxyg1")))
					 .subtract(new BigDecimal(map.getString("powerZxyg2"))).subtract(new BigDecimal(map.getString("powerZxyg3")))
					 .subtract(new BigDecimal(map.getString("powerZxyg4"))).doubleValue();
			 map.put("noElceFee",fnum.format(noElceFee));
			 map.put("noChgPower",fnum.format(noChgPower));
			 map.put("amount",fnum.format(map.getDouble("amount")));
			 map.put("elceFee",fnum.format(map.getDouble("elceFee")));
			 map.put("elceFee1",fnum.format(map.getDouble("elceFee1")));
			 map.put("elceFee2",fnum.format(map.getDouble("elceFee2")));
			 map.put("elceFee3",fnum.format(map.getDouble("elceFee3")));
			 map.put("elceFee4",fnum.format(map.getDouble("elceFee4")));
			 map.put("servFee",fnum.format(map.getDouble("servFee")));
			 map.put("parkFee",fnum.format(map.getDouble("parkFee")));
			 map.put("chgPower",fnum.format(map.getDouble("chgPower")));
			 map.put("powerZxyg1",fnum.format(map.getDouble("powerZxyg1")));
			 map.put("powerZxyg2",fnum.format(map.getDouble("powerZxyg2")));
			 map.put("powerZxyg3",fnum.format(map.getDouble("powerZxyg3")));
			 map.put("powerZxyg4",fnum.format(map.getDouble("powerZxyg4")));

		 }
	}




	/**
	 * 获取两个数值相减后的值
	 * @param dData
	 * @param xData
	 * @return
	 */
	private Double  getSubtraction(Double dData,Double xData){
		if (ChargeManageUtil.isNull(dData)) {
			dData = 0.0D;
		}
		if (ChargeManageUtil.isNull(xData)) {
			xData = 0.0D;
		}
		return new BigDecimal(String.valueOf(dData)).subtract(new BigDecimal(String.valueOf(xData))).doubleValue();
	}

    

    /**
     * 查询订单信息
     */
    public Map printBillPay(Map chargeMap) throws BizException{
    	DataVo vo = new DataVo(chargeMap);
    	int billPayId = vo.getInt("billPayId");
    	if(billPayId == 0){
    		throw new BizException(1102001,"订单Id");
    	}
    	vo.put("billPayId", billPayId);
    	Map map = chargeInMapper.printBillPay(vo);
    	map = getConvertBillPayInfo(map);
    	return map;
    }
    /**
     * 查询订单信息   返回多个
     */
    public List printBillPays(Map chargeMap) throws BizException{
    	DataVo vo = new DataVo(chargeMap);
    	if(vo.isBlank("billPayIds")){
    		throw new BizException(1102001,"订单Id");
    	}
    	String billPayIdStr = vo.getString("billPayIds");
    	String[] billPayIdStrs = billPayIdStr.split(",");
    	List<Integer> billPayIds = new ArrayList<Integer>();
    	for (int i = 0; i < billPayIdStrs.length; i++) {
    		billPayIds.add(Integer.valueOf(billPayIdStrs[i].trim()));
		}
    	if(billPayIds.size() > 0){
    		vo.put("billPayIds", billPayIds);
    	}else{
    		throw new BizException(1102001,"订单Id");
    	}
    	List list = chargeInMapper.printBillPays(vo);
    	
    	if(list != null && list.size()>0){
    		for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				map = getConvertBillPayInfo(map);
			}
    	}else{
    		throw new BizException(1700009);
    	}
    	return list;
    }
    
    /**
     * 数据业务处理
     */
    private Map getConvertBillPayInfo(Map map) throws BizException{
    	//充电时长
    	Date startTime = DateUtils.parseDate(map.get("startTime").toString());
    	Date endTime = DateUtils.parseDate(map.get("endTime").toString());
    	String chargeDateStr = DateUtils.getDistanceTime(startTime, endTime);
    	map.put("chargeDateStr", chargeDateStr);
    	//付款方式
    	String payType ="";
    	DataVo mapDataVo  = new DataVo(map);
    	if(mapDataVo.isNotBlank("payType")){
    		payType = dictService.getDictLabel("shoukfs", map.get("payType").toString());
    	}
    	map.put("payType", payType);
    	//充电卡号
    	Integer consId =  (map.get("consId")!=null && !"".equals(map.get("consId").toString())) ? Integer.valueOf(map.get("consId").toString()):null;
    	if(consId!=null){
    		String billDesc = map.get("billDesc")!=null?map.get("billDesc").toString():"";
    		if(billDesc.length()>0){
    			String[] billDescs = billDesc.split("_");
    			map.put("billDesc", billDescs[0]);
    		}
    	}
    	//计算充电金额
    	//峰
    	if(map.get("prcZxygz2")==null || "".equals(map.get("prcZxygz2").toString())){
    		map.put("prcZxygz2", 0);
    	}
    	Double prcZxygz2 = map.get("prcZxygz2")!=null?Double.valueOf(map.get("prcZxygz2").toString()):0;
    	if(map.get("powerZxyg2")==null || "".equals(map.get("powerZxyg2").toString())){
    		map.put("powerZxyg2", 0);
    	}
    	Double powerZxyg2 = map.get("powerZxyg2")!=null?Double.valueOf(map.get("powerZxyg2").toString()):0;
    	Double amountZxyg2 = 0d;
    	if(prcZxygz2 > 0 && powerZxyg2 > 0){
    		amountZxyg2 = BigDecimalUtils.roundFormat(BigDecimalUtils.mul(prcZxygz2, powerZxyg2));
    	}
    	map.put("amountZxyg2", amountZxyg2);
    	//平
    	if(map.get("prcZxygz3")==null || "".equals(map.get("prcZxygz3").toString())){
    		map.put("prcZxygz3", 0);
    	}
    	Double prcZxygz3 = Double.valueOf(map.get("prcZxygz3").toString());
    	if(map.get("powerZxyg3")==null || "".equals(map.get("powerZxyg3").toString())){
    		map.put("powerZxyg3", 0);
    	}
    	Double powerZxyg3 =Double.valueOf(map.get("powerZxyg3").toString());
    	Double amountZxyg3 = 0d;
    	if(prcZxygz3 > 0 && powerZxyg3 > 0){
    		amountZxyg3 = BigDecimalUtils.roundFormat(BigDecimalUtils.mul(prcZxygz3, powerZxyg3));
    	}
    	map.put("amountZxyg3", amountZxyg3);
    	//谷
    	if(map.get("prcZxygz4")==null || "".equals(map.get("prcZxygz4").toString())){
    		map.put("prcZxygz4", 0);
    	}
    	Double prcZxygz4 = map.get("prcZxygz4")!=null?Double.valueOf(map.get("prcZxygz4").toString()):0;
    	if(map.get("powerZxyg4")==null || "".equals(map.get("powerZxyg4").toString())){
    		map.put("powerZxyg4", 0);
    	}
    	Double powerZxyg4 = map.get("powerZxyg4")!=null?Double.valueOf(map.get("powerZxyg4").toString()):0;
    	Double amountZxyg4 = 0d;
    	if(prcZxygz4 > 0 && powerZxyg4 > 0){
    		amountZxyg4 = BigDecimalUtils.roundFormat(BigDecimalUtils.mul(prcZxygz4, powerZxyg4));
    	}
    	map.put("amountZxyg4", amountZxyg4);
    	Double prcZxygz1 = map.get("prcZxygz1")!=null?Double.valueOf(map.get("prcZxygz1").toString()):0;
    	if(map.get("powerZxyg1")==null || "".equals(map.get("powerZxyg1").toString())){
    		map.put("powerZxyg1", 0);
    	}
    	//尖-单
    	if(map.get("prcZxygz1")==null || "".equals(map.get("prcZxygz1").toString())){
    		map.put("prcZxygz1", 0);
    	}
    	if(amountZxyg2 <= 0 && amountZxyg3 <= 0 && amountZxyg4 <= 0){
	    	Double powerZxyg1 = map.get("chgPower")!=null?Double.valueOf(map.get("chgPower").toString()):0;
	    	Double amountZxyg1 = 0d;
	    	if(prcZxygz1 > 0 && powerZxyg1 > 0){
	    		map.put("powerZxyg1", powerZxyg1);
	    		amountZxyg1 = BigDecimalUtils.roundFormat(BigDecimalUtils.mul(prcZxygz1, powerZxyg1));
	    	}
	    	map.put("amountZxyg1", amountZxyg1);
	    	//总计
	    	Double powerTotal = BigDecimalUtils.add(BigDecimalUtils.add(powerZxyg1, powerZxyg4), BigDecimalUtils.add(powerZxyg2, powerZxyg3));
	    	map.put("powerTotal", powerTotal);
	    	Double amountTotal = BigDecimalUtils.add(BigDecimalUtils.add(amountZxyg1, amountZxyg4), BigDecimalUtils.add(amountZxyg2, amountZxyg3));
	    	map.put("amountTotal", amountTotal);
    	}else{
    		map.put("amountZxyg1", 0);
    		//总计
    		Double powerTotal = BigDecimalUtils.add(powerZxyg4, BigDecimalUtils.add(powerZxyg2, powerZxyg3));
    		map.put("powerTotal", powerTotal);
    		Double amountTotal = BigDecimalUtils.add(amountZxyg4, BigDecimalUtils.add(amountZxyg2, amountZxyg3));
    		map.put("amountTotal", amountTotal);
    	}
    	/*//设备类型
    	Integer pileType = map.get("pileType")!=null ? Integer.valueOf(map.get("pileType").toString()):null;
    	String pileTypeStr = "";
    	if(pileType != null){
    		switch (pileType) {
			case 1:
				pileTypeStr = "单交流充电桩";
				break;
			case 2:
				pileTypeStr = "单直流充电桩";
				break;
			case 3:
				pileTypeStr = "双交流充电桩";
				break;
			case 4:
				pileTypeStr = "双直流充电桩";
				break;
			case 5:
				pileTypeStr = "交直流充电桩";
				break;
			}
    	}
    	map.put("pileType", pileTypeStr);*/
    	//交直模式
    	String ortMode = map.get("ortMode")!=null ? map.get("ortMode").toString():"";
		map.put("ortMode", dictService.getDictLabel("jzms", ortMode));
		//充电枪 枪口
    	Map gunMap = new HashMap();
    	gunMap.put("pileId", Integer.valueOf(map.get("pileId").toString()));
    	gunMap.put("innerId", Integer.valueOf(map.get("innerId").toString()));
    	String gunPoint = gunService.getGunPoint(gunMap);
    	map.put("gunPoint", gunPoint);
    	return map;
    }
    
    
    
	/**
	 * 查询订单详情
	 * @param data
	 * @return
	 */
    public DataVo detailChargein(Map data) throws ParseException, BizException {
	   DataVo dv   =  chargeInMapper.getBillDetail(data);//得到订单详情
		String  billDesc = dv.getString("billDesc");//订单描述
		String[] str =billDesc.split("_");
		dv.put("cardId",str[0]);//充值卡号
		dv.put("preAmount","");//卡充电前余额
		dv.put("curAmount","");//卡充电后余额
	   if(dv.getInt("payType")==5){  //支付类型5为卡支付 得到卡充值前后金额
		 try {
			 data.put("cardNo2",str[0]);//卡编号
			 data.put("pileSeqId",str[1]);//桩编号
			 DataVo dvCard =   chargeInMapper.getBillDetailCard(data);
			 dv.put("preAmount",dvCard.getString("preAmount"));
			 dv.put("curAmount",dvCard.getString("curAmount"));
		 } catch (Exception e ){
		 	e.printStackTrace();
		 }
	   }
		int priceType = dv.getInt("prcTypeCode");//得到电费模板类型
	   if(priceType==0){ //没有值
		   //4个电价都相等为单一电价
		   if(dv.getDouble("prcZxygz1")==dv.getDouble("prcZxygz2")&&dv.getDouble("prcZxygz1")==dv.getDouble("prcZxygz3")&&dv.getDouble("prcZxygz1")==dv.getDouble("prcZxygz4")){
			   dv.put("singlePrice",dv.getDouble("prcZxygz1"));
			   dv.put("priceType",1);
		   }
	   }else {
          //(1.单电价 2.多费率 3.单电价固定费率 4.单电价比例费率 5. 多费率固定 6.多费率比例)
	   	  if(priceType==1||priceType==3||priceType==4){
			  dv.put("priceType",1);//电费类型
		  }else {
			  dv.put("priceType",2);//电费类型
		  }


	   }
	   //字典取值
	   String[] dic = {"hylb","zfzt","shoukfs"};
		Map<String,List<ComboxVo>> map  = ChargeManageUtil.getDictMap(dic);
		ChargeManageUtil.setDataVoPut("consTypeCode",map.get("hylb"),dv);;//会员类型
		ChargeManageUtil.setDataVoPut("payState",map.get("zfzt"),dv);//支付状态字典
		ChargeManageUtil.setDataVoPut("payType",map.get("shoukfs"),dv);//支付方式
//		ChargeManageUtil.setDataVoPut("pileType",map.get("cdzlx"),dv);//设备类型
		setPileType(dv);
		return  dv;
    }
	/**
	 * 得到可开票金额(元)
	 * @param consId
	 * @return
	 */
	public Double getCanInvoiceAmount(String consId) {
		if (!ChargeManageUtil.isNull(consId)) {
			Map map = new HashMap();
			if (!ChargeManageUtil.isNull(consId)) {
				map.put("consId", consId);
			}
			map.put("payState", 3);
			DataVo billPay = chargeInMapper.getFpBillPay(map);
			Double sumAmount = 0D;
			if (billPay != null) {
				sumAmount = billPay.getDouble("amount");
			}
			Map map2 = new HashMap();
			if (!ChargeManageUtil.isNull(consId)) {
				map2.put("consId", consId);
			}
			map2.put("isInvoice", 1);
			DataVo cinvoice = chargeInMapper.getCinvoice(map2);
			Double haveAmount = 0.00;
			if (!ChargeManageUtil.isNull(cinvoice)) {
				haveAmount = cinvoice.getDouble("applyMoney");
			}
			Double canAmount = new BigDecimal(String.valueOf(sumAmount)).subtract(new BigDecimal(String.valueOf(haveAmount))).doubleValue();

			return canAmount;
		}
		return 0.00;
	}
	public PageInfo divideConName(DataVo vo) throws BizException {
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<Data> list = chargeInMapper.divideConName(vo);
		return   new PageInfo(list);
	}

	public PageInfo divideStationName(DataVo vo)  throws BizException{
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<Data> list = chargeInMapper.divideStationName(vo);
		return   new PageInfo(list);
	}
	
	/**
	 * 获取订单数据  按时间将每日数据写入c_chargetime表和c_income_index表
	 */
	@Transactional(rollbackFor = Exception.class)
	public void getBillPayByHistory(Map map){
		DataVo vo = new DataVo(map);
		vo.put("payState", 1);
		List<DataVo> list = chargeInMapper.getBillPayByHistory(vo);
		Map<String, Map> chargetimeListMap = new HashMap<String, Map>();
		Map<String, Map> incomeIndexListMap = new HashMap<String, Map>();
		Map chargeTimeMap =null;
		Map incomeIndexMap =null;
		Integer orgId = null;
		Integer stationId = null;
		Double amount = 0d;
		Double chgPower = 0d;
		String startTime="";
		String endTime = "";
		Integer chgTime=0;
		Integer year = null;
		Integer month = null;
		Integer day = null;
		Integer serCount = 0;
		for (DataVo billPayVo : list) {
			String time = billPayVo.getString("endTime");
			Calendar calendar = CalendarUtils.convertStrToCalendar(time, CalendarUtils.yyyyMMdd);
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH)+1;
			day = calendar.get(Calendar.DAY_OF_MONTH);
			time = CalendarUtils.formatCalendar(calendar, CalendarUtils.yyyyMMdd);
			//获取 运营商Id,开始充电时间,结束充电时间,场站Id,收入,充电量
			orgId = billPayVo.getInt("orgId");
			stationId = billPayVo.getInt("stationId");
			amount = billPayVo.getDouble("amount");
			chgPower = billPayVo.getDouble("chgPower");
			startTime = billPayVo.getString("startTime");
			endTime = billPayVo.getString("endTime");
			chgTime = getDateDiff(startTime, endTime);
			//充电时长
			chargeTimeMap = chargetimeListMap.get(time+"_"+orgId);
			if(chargeTimeMap == null){
				chargeTimeMap = new HashMap();
				chargeTimeMap.put("chargeTime", chgTime);
				chargeTimeMap.put("year", year);
				chargeTimeMap.put("month", month);
				chargeTimeMap.put("day", day);
				chargeTimeMap.put("createDate", time);
				chargeTimeMap.put("orgId", orgId);
			}else{
				chgTime = chgTime + (Integer) chargeTimeMap.get("chargeTime");
				chargeTimeMap.put("chargeTime", chgTime);
			}
			chargetimeListMap.put(time+"_"+orgId, chargeTimeMap);
			
			//收入指标
			if(stationId != null){
				incomeIndexMap = incomeIndexListMap.get(time+"_"+stationId);
				if(incomeIndexMap  == null){
					serCount = 0;
					incomeIndexMap = new HashMap();
					incomeIndexMap.put("orgId", orgId);
					incomeIndexMap.put("stationId", stationId);
					incomeIndexMap.put("year", year);
					incomeIndexMap.put("month", month);
					incomeIndexMap.put("day", day);
					incomeIndexMap.put("createDate", time);
					incomeIndexMap.put("amount", amount);
					incomeIndexMap.put("chgPower", chgPower);
					incomeIndexMap.put("serCount", serCount);
				}else{
					Double amountDouble = (Double) incomeIndexMap.get("amount");
					Double chgPowerDouble = (Double) incomeIndexMap.get("chgPower");
					serCount = (Integer) incomeIndexMap.get("serCount")+1;
					incomeIndexMap.put("amount", new BigDecimal(incomeIndexMap.get("amount").toString()).add(new BigDecimal(amount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					incomeIndexMap.put("chgPower", new BigDecimal(incomeIndexMap.get("chgPower").toString()).add(new BigDecimal(chgPower)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					incomeIndexMap.put("serCount", serCount);
				}
				incomeIndexListMap.put(time+"_"+stationId, incomeIndexMap);
			}
		}
		//写表 转换为List<Map>
		vo = new DataVo();
		List<Map> chargetimes = new ArrayList<Map>();
		if(chargetimeListMap!=null && chargetimeListMap.size()>0){
			chargetimes.addAll(chargetimeListMap.values());
			vo.put("chargetimes", chargetimes);
			//新增
			chargeInMapper.saveChargeTime(vo);
		}
		vo = new DataVo();
		List<Map> incomeIndexs = new ArrayList<Map>();
		if(incomeIndexListMap!=null && incomeIndexListMap.size()>0){
			incomeIndexs.addAll(incomeIndexListMap.values());
			vo.put("incomeIndexs", incomeIndexs);
			//新增
			chargeInMapper.saveIncomeIndex(vo);
		}
	}
	
	/**
	 * 计算两个日期差   
	 */
	private Integer getDateDiff(String start,String end){
		if(start == null || start=="" || end == null || end ==""){
			return 0;
		}
		Calendar startCalendar = CalendarUtils.convertStrToCalendar(start, CalendarUtils.yyyyMMddHHmmss);
		Calendar endCalendar = CalendarUtils.convertStrToCalendar(end, CalendarUtils.yyyyMMddHHmmss);
		if(startCalendar.getTimeInMillis()>endCalendar.getTimeInMillis()){
			return (int) ((startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis())/(1000*60));
		}else if(startCalendar.getTimeInMillis()<endCalendar.getTimeInMillis()){
			return (int) ((endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis())/(1000*60));
		}
		return 0;
	}

	private void setPileType(DataVo dv) throws BizException {
		int pileType = dv.getInt("ortMode");
		if(pileType==1){
			dv.put("pileType", "交流");
		}else if(pileType==2){
			dv.put("pileType", "直流");
		}else if(pileType==3){
			dv.put("pileType", "交直一体");
		}
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

	/**
	 * 根据充电开始时间结束时间获取峰平谷时长(单位秒)
	 * @param begTime    开始时间
	 * @param endTime    结束时间
	 * @param rt         费率时段模板
	 * @return
	 */
	public static Map<String,Long> rateTimeLen(long begTime,long endTime,Map<String,List<Byte>> rt){
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
}

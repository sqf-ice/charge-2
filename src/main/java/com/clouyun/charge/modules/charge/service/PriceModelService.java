package com.clouyun.charge.modules.charge.service;

import java.util.*;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.OperateType;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.system.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.mapper.PriceModelMapper;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;

@Service
public class PriceModelService extends  BusinessService{
	
	@Autowired
	PriceModelMapper  priceModelMapper;
	@Autowired
	DictService dictService;

    
	  /**
	   * 模板列表查询
	   */
	  public PageInfo selectPriceModel(DataVo dv) throws BizException {
		  ChargeManageUtil.orgIdsCondition(dv, RoleDataEnum.ORG.dataType);
		  ChargeManageUtil.setPageInfo(dv);
	      return new PageInfo(priceModelMapper.selectPriceModel(dv));

	  }
    

	  /**
	   * 模板列表查询
	   */
	  public List<DataVo> queryPriceModel(Map dv){
	    	return priceModelMapper.queryPriceModel(dv);
	  }

	  /**
	   * 电价任务查询
	   */
	  public PageInfo selectDjrw(DataVo vo) throws BizException {
		  ChargeManageUtil.orgIdsCondition(vo,RoleDataEnum.ORG.dataType);
		  ChargeManageUtil.setPageInfo(vo);
		  List<DataVo> dvList = priceModelMapper.selectDjrw(vo);
		 List<ComboxVo> boxList  =dictService.getDictByType("sfzx");
		  for (DataVo dataVo : dvList){
			  ChargeManageUtil.setDataVoPut("taskExecFlag",boxList,dataVo);
		  }
	      return new PageInfo(dvList);

	  }

	/**
	 *  删除模板任务
	 */

	public Integer delPriceModel(DataVo dv) throws BizException {
		DataVo vo =  priceModelMapper.getTaskPrice(dv);
		if(vo!=null&&vo.size()>0){
			throw   new BizException(1700007);
		}
		priceModelMapper.delPriceModel(dv);
		saveLog("新增电价任务", String.valueOf(OperateType.add),"新增电价任务",dv.getInt("userId"));
		return 0;
	}
	  
	  
	  
	  /**
	    *  删除电价任务
	  */
	     
	  public Integer  delPriceTask(Map dv) throws BizException {
		  DataVo vo =  priceModelMapper.getPriceTask(dv);
		  if(vo.getInt("taskExecFlag")==2){
			  saveLog("删除电价任务", String.valueOf(OperateType.del),"删除电价任务",vo.getInt("userId"));
			  priceModelMapper.delPriceTask(dv);
			    return 0;
		  }else {
			  throw   new BizException(1700008);
		  }
	  }
	
	  
	/**
 	 * 费率时间段查询
 	 * 
 	 */
	 	
	 public	List<DataVo> queryModelTime(Map dv){
		 return priceModelMapper.queryModelTime(dv);
	 }
	 
	 
	 /**
	  * 查询电价任务
	  */
	 public	 DataVo queryDjrw(Map dv){
		 DataVo priceTask=null;
		 DataVo priceModel=null;
		 List<DataVo> dvlist =priceModelMapper.queryDjrw(dv);
		 if(dvlist !=null && dvlist.size()>0){
			 priceTask=dvlist.get(0);
		 }
		 //根据prcId字段获取电价模板参数
		 Map map=new HashMap();
		 map.put("prcId", priceTask.get("prcId"));
		 List<DataVo> list=queryPriceModel(map);
		 if(list !=null && list.size()>0){
			 priceModel=list.get(0);
		 }
		 //模板名称
		 String prcName=null;
		 //电价
		 String priceDes=null;
		 //服务费
		 String prcServiceDes=null;
		 if(priceModel != null){
			 if(("1").equals(priceModel.get("prcTypeCode").toString())){
				 priceDes = "当前电价类型为单一电价，价格为："+priceModel.get("prcZxygz1");
			 }else{
				 priceDes = "当前电价类型为费率电价，分别为尖值电价："+priceModel.get("prcZxygz1")+
						 "; 峰值电价："+priceModel.get("prcZxygz2")+
						 "; 平值电价："+priceModel.get("prcZxygz3")+
						 "; 谷值电价："+priceModel.get("prcZxygz4");
			 }
			 if(!ChargeManageUtil.isNull(priceModel.get("prcService"))){
				 prcServiceDes = "当前服务费价格为："+priceModel.get("prcService");
			 }
			 prcName=(String) priceModel.get("prcName");
		 }

		 String pileNames="";
		 String noPileNames="";
		 map.clear();
		 map.put("taskId", priceTask.get("taskId"));
		 List<DataVo> psList=queryPptr(map);
		 for(Map m:psList){
			 map.clear();
			 map.put("pileId",m.get("pileId"));
			 List<DataVo> cpList=queryChgpile(map);
			 DataVo cp=null;
			 if(cpList != null && cpList.size()>0){
				 cp=cpList.get(0);
			 }
			 if(cp!=null){
				 if(("1").equals(m.get("pileSuccessFlag"))){
					 pileNames +=cp.get("pileName")+",";
				 }else{
					 noPileNames += cp.get("pileName")+",";
				 }
			 }

		 }
		 if(!ChargeManageUtil.isNull(noPileNames)){
			 noPileNames = noPileNames.substring(0, noPileNames.length()-1);
		 }
		 if(!ChargeManageUtil.isNull(pileNames)){
			 pileNames = pileNames.substring(0, pileNames.length()-1);
		 }
		 priceTask.put("execDate", priceTask.get("taskExecTime"));
		 priceTask.put("priceDes", priceDes);
		 priceTask.put("prcServiceDes",prcServiceDes);
		 priceTask.put("pileNames", pileNames);
		 priceTask.put("noPileNames", noPileNames);
		 priceTask.put("prcName",prcName);
		 return priceTask;
	 }
	 
	 
	/**
 	 * 查询电价任务与充电桩关联关系表
 	 */
	 public List<DataVo> queryPptr(Map dv){
		 return priceModelMapper.queryPptr(dv);
	 }
	 	
	 	
 	/**
 	 * 查询充电桩信息
 	 */
	 public List<DataVo> queryChgpile(Map dv){
		 return priceModelMapper.queryChgpile(dv);
	 }
	 
	 /**
 	 * 新增电价任务
 	 */
	 public	 Integer  insertPriceTask(Map dv){
		 return priceModelMapper.insertPriceTask(dv);
	 }
	 	
	 	
 	/**
 	 * 向电价任务与充电桩关联关系表插入记录
 	 */
	 public	 Integer insertRela(Map dv){
		 return priceModelMapper.insertRela(dv);
	 }

	/**
	 * 电价描述
	 * @param data
	 * @return
	 */
    public DataVo describetPriceModel(Map data) {
    	List<DataVo> list = priceModelMapper.describetPriceModel(data);
		DataVo map = new DataVo();

		String	pilePriceDesc = "0";
		DataVo priceTask = null;
		DataVo pubPrice = null;
		String execDate="";
		String priceDes="";
		String prcServiceDes="";
		if(list.size()>0) {
			if(list.get(0).isNotBlank("taskId")){
			Integer taskId = list.get(0).getInt("taskId");
			Map map1 = new HashMap();
			map1.put("taskId", taskId);
			priceTask = priceModelMapper.getPriceTask(map1);

			execDate = priceTask.getString("taskExecTime");
			//根据prcId获取电价模板中的信息
			Map map2 = new HashMap();
			map2.put("prcId", priceTask.getString("prcId"));
			pubPrice = priceModelMapper.getPubPrice(map2);

			if (pubPrice.getInt("prcTypeCode") == 1) {

				priceDes = "当前电价类型为单一电价，价格为：" + pubPrice.getString("prcZxygz1") + "元/kwh";

			} else {

				priceDes = "当前电价类型为费率电价，分别为尖值电价：" + pubPrice.getString("prcZxygz1") +
						"元/kwh; 峰值电价：" + pubPrice.getString("prcZxygz2") +
						"元/kwh; 平值电价：" + pubPrice.getString("prcZxygz3") +
						"元/kwh; 谷值电价：" + pubPrice.getString("prcZxygz4") + "元/kwh";
			}

			if (pubPrice.isNotBlank("prcService")) {
				prcServiceDes = "当前服务费价格为：" + pubPrice.getString("prcService") + "元/kwh";
			}
			pilePriceDesc = "1";
			map.put("pilePriceDesc", pilePriceDesc);
			if (priceTask != null) {
				map.put("taskName", priceTask.getString("taskName"));
			} else {
				map.put("taskName", "");
			}
			map.put("priceDes", priceDes);
			map.put("prcServiceDes", prcServiceDes);
			map.put("execDate", execDate);
		}
		}
		return map;
     }

	/**
	 * 电价模板导出
	 * @param data
	 * @param response
	 */
	public void exportPriceModel(DataVo data, HttpServletResponse response) throws Exception{
		List dvList=selectPriceModel(data).getList();
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("创建时间");
		headList.add("模板名称");
		headList.add("运营商");
		headList.add("创建人");
		valList.add("createTime");
		valList.add("prcName");
		valList.add("orgName");
		valList.add("userName");
		ExportUtils.exportExcel(dvList,response,headList,valList,"电价模板表");
	}

	/**
	 * 电价任务导出
	 * @param data
	 * @param response
	 */
	public void exportDjrw(DataVo data, HttpServletResponse response) throws Exception{
		List dvList=selectDjrw(data).getList();
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("任务名称");
		headList.add("模板编号");
		headList.add("模板名称");
		headList.add("执行时间");
		headList.add("是否执行");
		valList.add("taskName");
		valList.add("prcId");
		valList.add("taskName");
		valList.add("taskExecTime");
		valList.add("taskExecFlag");
		ExportUtils.exportExcel(dvList,response,headList,valList,"电价任务表");
	}

	/**
	 * 电价模板详情
	 * @param data
	 */
	public DataVo detailPriceModel(Map data) {
		DataVo pubPrice=null;
		List<DataVo> dvlist=queryPriceModel(data);
		if(dvlist !=null && dvlist.size()>0){
			pubPrice=dvlist.get(0);
		}
		if(!("").equals(pubPrice.get("agreement"))){
			if(("51").equals(pubPrice.get("agreement"))){
				pubPrice.put("agreementName", "深圳电动汽车充电协议");
			}else if(("62").equals(pubPrice.get("agreement"))){
				pubPrice.put("agreementName", "科陆电动汽车充电协议");
			}
		}
		//根据orgId查询企业信息
		Map map=new HashMap();
		map.put("orgId", pubPrice.get("orgId"));
		DataVo pubOrg=ChargeManageUtil.queryOrgByprcId(map);
		if(pubOrg != null){
			pubPrice.put("orgName",pubOrg.get("orgName"));
		}

		//查询费率时间段
		Map datavo=new HashMap();
		datavo.put("priceId",pubPrice.get("prcId"));
		List<DataVo> modelTimeList=queryModelTime(datavo);
		if(modelTimeList != null && modelTimeList.size()>0 ){
			pubPrice.put("modelTimeList", modelTimeList);
		}
		return pubPrice;
	}

    public PageInfo priceTask(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo,RoleDataEnum.ORG.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> dvList = priceModelMapper.priceTask(vo);
		return new PageInfo(dvList);
    }

	/**
	 * 添加模板
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public int addPriceModel(DataVo vo) throws BizException {
		int prcTypeCode = vo.getInt("prcTypeCode");
		if(prcTypeCode==1||prcTypeCode==3||prcTypeCode==4){
			vo.put("prcZxygz2",vo.getString("prcZxygz1"));
			vo.put("prcZxygz3",vo.getString("prcZxygz1"));
			vo.put("prcZxygz4",vo.getString("prcZxygz1"));
			priceModelMapper.addPriceModel(vo);
		}else {
			if(prcTypeCode!=2){
				if(vo.isBlank("modelTime")){
					throw   new BizException(1102001,"modelTime");
				}
			}
			priceModelMapper.addPriceModel(vo);
			if(prcTypeCode!=2){
				priceModelMapper.addModelTime(vo);
			}
		}
		saveLog("电价新增", String.valueOf(OperateType.add),"电价新增",vo.getInt("userId"));
		return 1;
	}

	public PageInfo priceModel(DataVo vo) throws BizException {
		ChargeManageUtil.orgIdsCondition(vo,RoleDataEnum.ORG.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> dvList = priceModelMapper.priceModel(vo);
		return new PageInfo(dvList);
	}

    public List<DataVo> stationPile(DataVo  vo) throws BizException {
		ChargeManageUtil.stationIdsCondition(vo,RoleDataEnum.STATION.dataType);
		List<DataVo> stationList = priceModelMapper.stationList(vo);
		List<DataVo> pileList = priceModelMapper.pileList(vo);
		Set setPileIds = new HashSet();
		for(DataVo pile :pileList){
			setPileIds.add(pile.getInt("pileId"));
		}
		vo.put("pileIds",setPileIds);
		List<DataVo> pileTaskrela = priceModelMapper.pileTaskrela(vo);
		for(DataVo pile :pileList){
			for(DataVo task: pileTaskrela){
				if(pile.getString("pileId").equalsIgnoreCase(task.getString("pileId"))){
					pile.put("state",1);
				}else {
					pile.put("state",2);
				}
			}
		}
		List<DataVo> stations = new ArrayList<>();
		for(DataVo station :stationList){
			DataVo stationVo = new DataVo();
			List<DataVo> piles = new ArrayList<>();
			int stationId1 = station.getInt("stationId");
			stationVo.put("stationName",station.getString("stationName"));
			for(DataVo pile :pileList){
				DataVo pileIds = new DataVo();
				int stationId2 = pile.getInt("stationId");
				if(stationId1==stationId2){
					pileIds.put("sate",pile.getInt("state"));
					pileIds.put("pileId",pile.getInt("pileId"));
					pileIds.put("pileName",pile.getString("pileName"));
					piles.add(pileIds);
				}
			}
			stationVo.put("piles",piles);
			stations.add(stationVo);
		}

		return  stations;
    }
	@Transactional(rollbackFor=Exception.class)
	public void savePriceTask(DataVo data) {
		Map m=new HashMap();
		m.put("orgId", data.get("orgId"));
		m.put("prcId", data.get("prcId"));
		m.put("taskExecFlag", "2");
		m.put("taskExecTime", data.get("execDate"));
		m.put("taskName", data.get("taskName"));
		m.put("userId", data.get("userId"));
		//插入电价任务表
		insertPriceTask(m);
		String[] pileIdArray = ((String)data.get("pileIds")).split(",");
		for(int i=0;i<pileIdArray.length;i++){
			Map map=new HashMap();
			map.put("taskId", m.get("taskId"));
			map.put("pileId",Integer.parseInt(pileIdArray[i]));
			map.put("pileSuccessFlag", "2");
			//插入电价任务与充电桩关联关系表
			insertRela(map);
		}
		saveLog("新增电价任务", String.valueOf(OperateType.add),"新增电价任务",data.getInt("userId"));
	}
}

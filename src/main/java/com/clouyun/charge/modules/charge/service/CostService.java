package com.clouyun.charge.modules.charge.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.charge.mapper.CostMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CostService {
	
	@Autowired
	CostMapper  costMapper;
	
	
  

    
	  /**
	   * 充电成本查询
	   */
	  public PageInfo selectCost(DataVo vo) throws BizException {
		  ChargeManageUtil.stationIdsCondition(vo, RoleDataEnum.STATION.dataType);
		  ChargeManageUtil.setPageInfo(vo);
	      return new PageInfo(costMapper.selectCost(vo));
	  }
	/**
	 * 收入报表查询
	 */
    public PageInfo selectIncome(DataVo vo) throws BizException {
		ChargeManageUtil.stationIdsCondition( vo, RoleDataEnum.STATION.dataType);
		ChargeManageUtil.setPageInfo(vo);
		List<DataVo> list =  costMapper.selectIncome(vo);
		return new PageInfo(list);
    }
	/**
	 * 充电成本导出
	 * @param data
	 * @return
	 */
    public PageInfo detailIncome(DataVo data) {
		ChargeManageUtil.setPageInfo(data);
    	return new PageInfo(costMapper.detailIncome(data));
    }
	public void exportCost(DataVo data, HttpServletResponse response)throws Exception {
		List dvList=selectCost(data).getList();
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("开始日期");
		headList.add("结束日期");
		headList.add("场站编号");
		headList.add("场站名称");
		headList.add("用电量(kW·h)");
		headList.add("充电设施用电量(kW·h)");
		headList.add("充电量(kW·h)");
		headList.add("其他设施用电量(kW·h)");
		headList.add("变压器损耗电量(kW·h)");
		headList.add("充电设施损耗量(kW·h)");
		headList.add("报表生成时间");
		valList.add("prStartDate");
		valList.add("prEndDate");
		valList.add("stationNo");
		valList.add("stationName");
		valList.add("prTotalPower");
		valList.add("prPowerChg");
		valList.add("prChg");
		valList.add("prPowerEqip");
		valList.add("prTransLoss");
		valList.add("prChgLoss");
		valList.add("createDate");
		ExportUtils.exportExcel(dvList,response,headList,valList,"充电成本表");
	}

	/**
	 * 收入报表导出
	 * @param data
	 * @param response
	 */
	public void exportIncome(DataVo data, HttpServletResponse response)throws Exception {
		List dvList=selectIncome(data).getList();
		List<String>	headList =  new ArrayList<>();
		List<String>	valList =  new ArrayList<>();
		headList.add("开始日期");
		headList.add("结束日期");
		headList.add("场站编号");
		headList.add("场站名称");
		headList.add("总用电量(kW·h)");
		headList.add("尖时总用电量(kW·h)");
		headList.add("峰时总用电量(kW·h)");
		headList.add("平时总用电量(kW·h)");
		headList.add("谷时总用电量(kW·h)");
		headList.add("用电成本(kW·h)");
		headList.add("总冲电量(kW·h)");
		headList.add("尖时总充电量(kW·h)");
		headList.add("峰时总充电量(kW·h)");
		headList.add("平时总充电量(kW·h)");
		headList.add("谷时总充电量(kW·h)");
		headList.add("充电收入(元)");
		headList.add("销售毛利(元)");
		headList.add("报表生成时间");
		valList.add("irStartDate");
		valList.add("irEndDate");
		valList.add("stationNo");
		valList.add("stationName");
		valList.add("irTotalPower");
		valList.add("irPower1");
		valList.add("irPower2");
		valList.add("irPower3");
		valList.add("irPower4");
		valList.add("irPowerCost");
		valList.add("irPowerChg");
		valList.add("irChg1");
		valList.add("irChg2");
		valList.add("irChg3");
		valList.add("irChg4");
		valList.add("irIncome");
		valList.add("irProfit");
		valList.add("createDate");
		ExportUtils.exportExcel(dvList,response,headList,valList,"收入报表");
	}
}

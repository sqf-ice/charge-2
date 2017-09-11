package com.clouyun.charge.modules.document.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.ValidateUtils;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.document.mapper.ItDeviceMapper;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.system.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述: IT设备管理
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年6月25日 下午2:18:41
 */
@Service
@Transactional(rollbackFor = BizException.class)
public class ItDeviceService {

	public static final Logger logger = LoggerFactory.getLogger(ItDeviceService.class);

	@Autowired
	private ItDeviceMapper itDeviceMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private DictService dictService;

	/*
	 * 查询IT设备管理列表
	 */
	public PageInfo selectAll(Map map) throws BizException {
		DataVo params = new DataVo(map);
		getPermission(params);

		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(params);
		}

		List<Map> all = itDeviceMapper.queryItDevices(params);
		PageInfo page = new PageInfo(all);
		return page;
	}

	private void getPermission(DataVo params) throws BizException {
		// 此处应根据登录用户ID获取到能查看的企业，未实现
		if (params.isBlank("userId")){
            throw new BizException(1000006);
        }
		// 此处应该根据登录用户ID获取到他能看到的企业创建的角色,admin查看所有
		Set<Integer> userRoleDataById = userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.STATION.dataType);
		if(userRoleDataById != null && userRoleDataById.size() > 0){
            params.put("stationIds", userRoleDataById);
        }
	}

	public int insert(Map map) throws BizException{
		DataVo vo = new DataVo(map);
		vo.put("deviceStatus",0);

		//检查参数
		Map map1 = check(vo);

		int insertCount = itDeviceMapper.insert(map1);
		return insertCount;
	}

	public int update(Map map)throws BizException{
		DataVo vo = new DataVo(map);
		if(vo.isBlank("ratedPower")){
			vo.put("ratedPower",0);
		}
		if(vo.isBlank("deviceStatus")){
			vo.put("deviceStatus",0);
		}
		Map map1 = check(vo);

		int count = itDeviceMapper.update(map1);
		return count;
	}
	public Map check(DataVo vo) throws  BizException{

		if(vo.isBlank("orgId")){
			throw new BizException(1200000,"运营商");
		}
		if(vo.isBlank("stationId")){
			throw new BizException(1200000,"所属场站");
		}
		if(vo.isBlank("deviceType")){
			throw new BizException(1200000,"设备类型");
		}
		if(vo.isBlank("deviceNo")){
			throw new BizException(1200000,"设备编号");
		}
		if(vo.isBlank("equipmentType")){
			throw new BizException(1200000,"设备型号");
		}
		if(vo.isBlank("deviceName")){
			throw new BizException(1200000,"设备名称");
		}
		if(vo.isNotBlank("ratedPower")){
			if(!ValidateUtils.Number(vo.getString("ratedPower"))){
				throw new BizException(1000014, "额定功率");
			}
			Double ratedPower = Double.parseDouble(vo.getString("ratedPower"));
			if(ratedPower > 9999.99 || ratedPower < -9999.99){
				throw new BizException(1102017);
			}
		}
		DataVo queryVo = new DataVo();
		queryVo.put("column","device_no");
		queryVo.put("arr",vo.getString("deviceNo"));
		queryVo.put("stationId",vo.getInt("stationId"));
		if (vo.isNotBlank("deviceId")){
			queryVo.put("deviceId",vo.getInt("deviceId"));
		}

		if(itDeviceMapper.validateArr(queryVo) > 0){
			throw new BizException(1102007,"设备编号");
		}

		queryVo.put("column","device_name");
		queryVo.put("arr",vo.getString("deviceName"));
		if(itDeviceMapper.validateArr(queryVo) > 0){
			throw new BizException(1102007,"设备名称");
		}

		List<String> list = new ArrayList<>();
		list.add("ratedPower");
		list.add("manufactureTime");
		list.add("guaranteeTime");
		list.add("installTime");
		list.add("deviceStatus");

		Map map1 = CommonUtils.convertDefaultVal(vo,list);
		return map1;

	}

	public Map queryItDeviceByKey(Integer deviceId){
		Map map = itDeviceMapper.queryItDeviceByKey(deviceId);
		return map;
	}

	public Integer deleteDevice(List list) throws BizException {

		if(list == null || list.size() <= 0){
			throw new BizException(1102016);
		}

		Integer count = itDeviceMapper.dissDevice(list);
		return count;
	}

	public void exportDevice(Map map,HttpServletResponse response) throws Exception {
		DataVo params = new DataVo(map);
		getPermission(params);
		// 如果有分页参数，则进行分页，否则查全部的
		if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
			PageHelper.startPage(params);
		}

		List<Map> list = itDeviceMapper.queryItDevices(params);

		List<String> headList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		//转义字典
		escapeResult(list);

		headList.add("运营商");
		headList.add("所属场站");
		headList.add("设备名称");
		headList.add("设备编号");
		headList.add("设备型号");
		headList.add("设备类型");
		headList.add("参数");
		headList.add("额定功率");
		headList.add("生产厂家");
		headList.add("生产日期");
		headList.add("保质日期");
		headList.add("安装日期");
		headList.add("资产归属");
		headList.add("设备状态");
		headList.add("备注");

		valList.add("orgName");
		valList.add("stationName");
		valList.add("deviceName");
		valList.add("deviceNo");
		valList.add("equipmentType");
		valList.add("deviceType");
		valList.add("parameter");
		valList.add("ratedPower");
		valList.add("manufacturer");
		valList.add("manufactureTime");
		valList.add("guaranteeTime");
		valList.add("installTime");
		valList.add("assetOwnership");
		valList.add("deviceStatus");
		valList.add("remarks");
		ExportUtils.exportExcel(list, response,headList,valList,"IT设备信息");
	}

	private void escapeResult(List<Map> list) throws BizException {
		DataVo vo ;
		if(list != null && list.size() > 0){
			for (Map result : list) {
				vo = new DataVo(result);
				result.put("deviceStatus",dictService.getDictLabel("zt", vo.getString("deviceStatus")));
				result.put("deviceType", dictService.getDictLabel("sblx", vo.getString("deviceType")));
			}
		}
	}

	private void convertDefaultVal(DataVo vo){
		if(vo.isBlank("manufactureTime")){
			vo.put("manufactureTime",null);
		}
		if(vo.isBlank("ratedPower")){
			vo.put("ratedPower",0);
		}
		if(vo.isBlank("guaranteeTime")){
			vo.put("guaranteeTime",null);
		}
		if(vo.isBlank("installTime")){
			vo.put("installTime",null);
		}
	}
}


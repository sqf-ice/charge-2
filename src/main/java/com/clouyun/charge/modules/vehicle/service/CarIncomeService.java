package com.clouyun.charge.modules.vehicle.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.DateUtils;
import com.clouyun.cdzcache.imp.CdzCacheDataImp;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.system.service.DictService;
import com.clouyun.charge.modules.vehicle.mapper.CarIncomeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import java.util.*;
/**
 * 描述: 车辆收入控制
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yikai
 * 版本: 1.0
 * 创建日期: 2017年05月11日
 */
@Service
public class CarIncomeService {
    @Autowired
    CarIncomeMapper carIncomeMapper;
    @Autowired
    DictService dictService;
    @Autowired
    CdzCacheDataImp cdi;
    /**
     *  车辆总数与各类型车辆数统计
     */
    public List<DataVo> carTypeCount(DataVo vo) throws BizException {
      Set<Integer> orgIds = ChargeManageUtil.getSubsByUserId(vo, RoleDataEnum.ORG.dataType);
      if(orgIds!=null&&orgIds.size()>0) {
          vo.put("orgIds", orgIds);
      }
     return    carIncomeMapper.carTypeCount(vo);
    }

    /**
     * 车辆收入24小时图表
     * @return
     */
    public Collection<DataVo> carHourChart(DataVo vo) throws BizException {
        Set<Integer> orgIds = ChargeManageUtil.getSubsByUserId(vo,RoleDataEnum.ORG.dataType);
        if(orgIds!=null&&orgIds.size()>0) {
            vo.put("orgIds", orgIds);
        };
        Map<String,DataVo> map = new LinkedHashMap<>();
        DataVo dataVo = null;
        for(int i=0;i<24;i++){
            dataVo = new DataVo();
            dataVo.put("amount",0);
            dataVo.put("chaPower",0);
            if(i< 10) {
                dataVo.put("hourTime", "0" + i);
                map.put("0" + i, dataVo);
            }else{
                dataVo.put("hourTime", i +"");
                map.put(i + "", dataVo);
            }
        }
        List<DataVo> lists = carIncomeMapper.carHourChart(vo);
        for(DataVo v : lists){
            String hour = v.getString("hourTime");
            map.put(hour,v);
        }
        return map.values();

    }

    /**
     * 当月单车充电排行取前十
     * @return
     */
    public PageInfo carChgPowerTop10(DataVo  vo) throws BizException {
        Set<Integer> orgIds = ChargeManageUtil.getSubsByUserId(vo,RoleDataEnum.ORG.dataType);
        if(orgIds!=null&&orgIds.size()>0) {
            vo.put("orgIds", orgIds);
        }
        if(vo.isBlank("pageNum") ){
            vo.put("pageNum",1);
        }
        if(vo.isBlank("pageSize") ){
            vo.put("pageSize",8);
        }
        PageHelper.startPage(vo);
       List<DataVo> list = carIncomeMapper.carChgPowerTop10(vo);
        return new PageInfo(list);
    }

    /**
     * 车辆在各站的充电数[充电中]（统计前20）
     * @return
     */
    public PageInfo carChgPowerTop20(DataVo orgIndVo) throws BizException {
        Map<String,Set<Byte>> cdqMap = cdi.getCDZChargePort();//充电的枪编号集合
        if(cdqMap!=null&&cdqMap.size()>0){
            DataVo dataVo = new DataVo();
            dataVo.put("pileAddr",cdqMap.keySet());
            Set<Integer> orgIds = ChargeManageUtil.getSubsByUserId(orgIndVo,RoleDataEnum.ORG.dataType);
            if(orgIds!=null&&orgIds.size()>0) {
                dataVo.put("orgIds",orgIds);
            }
           /* if(orgIndVo.isBlank("pageNum") ){
                orgIndVo.put("pageNum",1);
            }
            if(orgIndVo.isBlank("pageSize") ){
                orgIndVo.put("pageSize",8);
            }
            PageHelper.startPage(orgIndVo);*/
            
            List<DataVo> dvList  =  carIncomeMapper.carChgPowerTop20(dataVo);
            PageInfo  pageInfo = new PageInfo(dvList);
            List<DataVo> mapList = new ArrayList<DataVo>();
            
            
            List<String> carLicensePlates = new ArrayList<String>(); 
            List<String> carLicensePlateFs = new ArrayList<String>(); 
            List<String> vins = new ArrayList<String>(); 
            
            String type ="";  // F, noF,vin
            for (DataVo data : dvList){
                DataVo map  = new DataVo();
                List<DataVo> voList = new ArrayList<DataVo>();
                String  pillAddrList = data.getString("pileAddrList");
                String[] pillAddrs = pillAddrList.split(",");
                if(pillAddrs!=null&&pillAddrs.length>1){
                    for (String pillAddr:pillAddrs){
                        DataVo vo = new DataVo();
                        String licensePlate="";
                        Map<Byte,String[]>  pillBayList  =  cdi.getCDZChargeInfo(pillAddr,cdqMap.get(pillAddr));
                        if(pillBayList!=null&&pillBayList.size()>0){
                            Object[] pillBay = pillBayList.values().toArray();
                            String[] pill = (String[]) pillBay[0];
                            String vin = pill[0];
                            vin =vin.trim();
                            if(vin==null||vin.isEmpty()){  //判断vim是否存在
                                String carLicensePlate=pill[1];
                                if(carLicensePlate!=null||!carLicensePlate.isEmpty()) {
                                    if (pill[1].startsWith("f")) {//判断是否用f开头
                                        carLicensePlate = carLicensePlate.replace("f","");
                                        carLicensePlateFs.add(carLicensePlate);
                                        type = "F";
                                        // licensePlate=   carIncomeMapper.selectCarPlateF(carLicensePlate);//查询卡号用f开头的车牌
                                    } else {
                                    	carLicensePlates.add(carLicensePlate);
                                       // licensePlate=    carIncomeMapper.selectCarPlate(carLicensePlate);//查询卡号不用f开头的车牌
                                    	type = "noF";
                                    }
                                    licensePlate = carLicensePlate;
                                }
                            }else {
                                if(!ChargeManageUtil.isSpecialChar(vin)){
                                    licensePlate = vin;
                                    type = "vin";
                                    vins.add(vin);
                                    // licensePlate=    carIncomeMapper.selectCarvVim(vin);//查询卡号不用f开头的车牌
                                }
                            }
                            vo.put("licensePlate",licensePlate);
                            vo.put("type", type);
                            Long chgTime=0L;
                            try {
                                 chgTime = Long.parseLong(pill[6]);
                            }catch (Exception e){
                                chgTime=0L;
                                e.printStackTrace();
                            }
                            vo.put("chgTime", DateUtils.sec2Time(chgTime*1000));
                            vo.put("chgPower",pill[3]);

                        }
                        if(vo.size()>0){
                            voList.add(vo);
                        }else {
                            data.put("size", data.getInt("size")-1);
                        }

                    }
                }
              
                
                map.put("stationId",data.getString("stationId"));
                map.put("stationName",data.getString("stationName"));
                map.put("size",data.getString("size"));
                map.put("vehicle",voList);
                mapList.add(map);
            }
            
            
            Map<String, String> carLicensePlateMap = new HashMap<String, String>();
            Map<String, String> carLicensePlateFMap = new HashMap<String, String>();
            Map<String, String> vinMap = new HashMap<String, String>();
            
            DataVo dVo = new DataVo();
            if(!carLicensePlates.isEmpty()){
            	dVo.put("carLicensePlates", carLicensePlates);
            	List<DataVo> carPlateList = carIncomeMapper.queryCarPlate(dVo);
            	for (DataVo carVo : carPlateList) {
            		carLicensePlateMap.put(carVo.getString("cardId"), carVo.getString("licensePlate"));
				}
            }
            dVo.clear();
            if(!carLicensePlateFs.isEmpty()){
            	dVo.put("carLicensePlateFs", carLicensePlateFs);
            	List<DataVo> carPlateFList = carIncomeMapper.queryCarPlateF(dVo);
            	for (DataVo carVo : carPlateFList) {
            		carLicensePlateFMap.put(carVo.getString("billPayId"), carVo.getString("licensePlate"));
				}
            }
            dVo.clear();
            if(!vins.isEmpty()){
            	dVo.put("vins", vins);
            	List<DataVo> vinList = carIncomeMapper.queryVins(dVo);
            	for (DataVo vinVo : vinList) {
            		vinMap.put(vinVo.getString("vin"), vinVo.getString("licensePlate"));
				}
            }
            String licensePlate;
            Iterator<DataVo> mapListIterator = mapList.iterator();
            while (mapListIterator.hasNext()) {
            	Map map = mapListIterator.next();
            	List<DataVo> voList = (List<DataVo>) map.get("vehicle");
            	if(voList != null && !voList.isEmpty()){
        			Iterator<DataVo> iterator = voList.iterator();
        			while(iterator.hasNext()){
        				DataVo carVo = iterator.next();
        				licensePlate = carVo.getString("licensePlate");
        				type = carVo.getString("type");
        				if("F".equals(type)){
        					licensePlate = carLicensePlateFMap.get(licensePlate);
        				}else if("noF".equals(type)){
        					licensePlate = carLicensePlateMap.get(licensePlate);
        				}else if("vin".equals(type)){
        					licensePlate = vinMap.get(licensePlate);
        				}
        				if(licensePlate != null && !"".equals(licensePlate)){
        					carVo.put("licensePlate", licensePlate);
        					carVo.remove("type");
        				}else{
        					carVo.put("licensePlate", "");
        					//iterator.remove();
        				}
        			}
        		}
            	if(voList.isEmpty()){
            		//mapListIterator.remove();
            	}else{
            		map.put("size", voList.size());
            	}
			}
            
            pageInfo.setList(mapList);
            return pageInfo;
        }else {
            return null;
        }


    }

    /**
     * 查询车辆收入列表
     * @return
     */
    public PageInfo carIncomeList(DataVo map) throws BizException {
        List<DataVo> car = null;
        if(map.isBlank("cdStationId")){ // 充电场站为空
            car = getCarListType1(map);//得到车集合
        }else { //场站不为空
            car=getCarListType2(map);
        }
           fillDateCar(car,map);//格式化
            return new PageInfo(car);
    }



    private void fillDateCar(List<DataVo> carList, DataVo map) {
        for(DataVo car: carList ){
//            充电次数、充电量、消费金额为空值时补充上0
            if(car.getString("size")==null||car.getString("size")==""){
                car.put("size",0.00);
            }
            if(car.getString("chgPower")==null||car.getString("chgPower")==""){
                car.put("chgPower",0.00);
            }
            if(car.getString("amount")==null||car.getString("amount")==""){
                car.put("amount",0.00);
            }
            if(car.getString("startTime")==null||car.getString("startTime")==""){
                car.put("startTime",map.getString("startDate"));
            }
            if(car.getString("endTime")==null||car.getString("endTime")==""){
                car.put("endTime",map.getString("endDate"));
            }
        }
    }

    /**
     * 查询条件不加入充电场站
     */
   public  List<DataVo>  getCarListType1(DataVo map) throws BizException {
       Set<Integer> stations = ChargeManageUtil.getSubsByUserId(map,RoleDataEnum.ORG.dataType);
       if(stations!=null&&stations.size()>0){
           map.put("orgIds",stations);
       }
       ChargeManageUtil.setPageInfo(map);
       List<DataVo> carList   = carIncomeMapper.carList(map); //得到车集合
       map.remove("pageNum");
       map.remove("pageSize");
       if(carList!=null&&carList.size()>0){
           Set<Integer> carIds = new HashSet<>();
           for (DataVo vo :carList) {
               carIds.add(vo.getInt("vehicleId")); //车id集合
           }
           map.put("carIds",carIds);
           List<DataVo> lists   = carIncomeMapper.carIncomeList(map);//查询订单表中的车
           for (DataVo vo1:carList){
               vo1.put("startTime","");
               vo1.put("endTime","");
               vo1.put("cdStationName","");
               vo1.put("size",0);
               vo1.put("chgPower",0);
               vo1.put("amount",0);
               vo1.put("carId",vo1.getInt("vehicleId"));
               for (DataVo vo2:lists){
                   if(vo1.getInt("vehicleId")==vo2.getInt("carId")){
                       vo1.put("startTime",vo2.getString("startTime"));
                       vo1.put("endTime",vo2.getString("endTime"));
                       vo1.put("cdStationName",vo2.getString("cdStationName"));
                       vo1.put("size",vo2.getInt("size"));
                       vo1.put("chgPower",vo2.getDouble("chgPower"));
                       vo1.put("amount",vo2.getDouble("amount"));
                   }
               }
           }
       }
      return carList;
   }
    /**
     * 查询条件加入充电场站 （有充电场站要根据订单表排序）
     */
    public  List<DataVo>  getCarListType2(DataVo map) throws BizException {
        Set<Integer> stations = ChargeManageUtil.getSubsByUserId(map,RoleDataEnum.ORG.dataType);
        if(stations!=null&&stations.size()>0){
            map.put("orgIds",stations);
        }
        List<DataVo> lists = carIncomeMapper.carListType2(map);
        return lists;
    }

    /**
     * 查询条件没有充电场站 合计
     */

    public  DataVo  getCarListTypeCount(DataVo map) throws BizException {
        map.remove("pageNum");
        map.remove("pageSize");
        Set<Integer> orgIds = ChargeManageUtil.getSubsByUserId(map,RoleDataEnum.ORG.dataType);
        if(orgIds != null && orgIds.size() > 0){
            map.put("orgIds",orgIds);
        }

        DataVo vo  =carIncomeMapper.carListTypeCount(map);
        return vo;
    }




    /**
     * 车辆收入详情
     * @param map
     * @return
     */
    public PageInfo carIncomeDetail(DataVo map) {
        ChargeManageUtil.setPageInfo(map);
        List<DataVo> lists   = carIncomeMapper.carIncomeDetail(map);
        for (DataVo vo: lists){
            ChargeManageUtil.getUseTime(vo);
        }
        return  new PageInfo(lists);
    }

    /**
     * 车辆收入列表导出
     * @param map
     * @param response
     */
    public void carIncomeListExport(DataVo map, HttpServletResponse response) throws Exception{
        List<DataVo> lists   = null;
        if(map.isBlank("cdStationId")){ // 充电场站为空
            lists = getCarListType1(map);//得到车集合
        }else {//场站不为空
            lists=getCarListType2(map);
        }
        if(lists!=null){
            fillDateCar(lists,map);//格式化
        }
        DataVo count = getCarListTypeCount(map);//合计
        List<String>	headList =  new ArrayList<>();
        List<String>	valList =  new ArrayList<>();
        headList.add("开始时间");
        headList.add("结束时间");
        headList.add("企业名");
        headList.add("所属场站");
        headList.add("车牌号");
        headList.add("自编号");
        headList.add("车品牌");
        headList.add("线路");
        headList.add("充电场站");
        headList.add("充电次数");
        headList.add("充电量(kwh)");
        headList.add("消费金额(元)");
        valList.add("startTime");
        valList.add("endTime");
        valList.add("orgName");
        valList.add("stationName");
        valList.add("licensePlate");
        valList.add("onNumber");
        valList.add("brand");
        valList.add("line");
        valList.add("cdStationName");
        valList.add("size");
        valList.add("chgPower");
        valList.add("amount");
        count.put("startTime","合计");
        lists.add(count);
        ExportUtils.exportExcel(lists,response,headList,valList,"车辆收入表");
    }

    /**
     * 车辆详情导出
     * @param data
     */
    public void carIncomeDetailExport(DataVo data,HttpServletResponse response) throws  Exception {
        List<DataVo> dvList=carIncomeDetail(data).getList();
        List<ComboxVo> boxList1  =dictService.getDictByType("cllx");
        for(DataVo vo :dvList){
           ChargeManageUtil.setDataVoPut("vehicleType",boxList1,vo);
        }
        List<String>	headList =  new ArrayList<>();
        List<String>	valList =  new ArrayList<>();
        headList.add("开始时间");
        headList.add("结束时间");
        headList.add("订单编号");//
        headList.add("企业名");
        headList.add("所属场站");
        headList.add("车牌号");
        headList.add("车辆类型");
        headList.add("自编号");
        headList.add("线路");
//        headList.add("单价(元/kwh)");
        headList.add("充电时长");
        headList.add("充电量(kwh)");
        headList.add("消费金额(元)");
        headList.add("充电场站");
        headList.add("充电桩编号");
        valList.add("startTime");
        valList.add("endTime");
        valList.add("billPayNo");
        valList.add("orgName");
        valList.add("stationName");
        valList.add("licensePlate");
        valList.add("vehicleType");
        valList.add("onNumber");
        valList.add("line");
//        valList.add("price");
        valList.add("useTime");
        valList.add("chgPower");
        valList.add("amount");
        valList.add("cdStationName");
        valList.add("pileNo");
        ExportUtils.exportExcel(dvList,response,headList,valList,"车辆收入详情表");
    }

    /**
     * 车辆收入详情统计汇总列表
     * @param data
     * @return
     */
    public PageInfo carsCountDetailList(DataVo data) throws BizException {
        Set<Integer> orgIds = ChargeManageUtil.getSubsByUserId(data,RoleDataEnum.ORG.dataType);
        if(orgIds!=null&&orgIds.size()>0) {
            data.put("orgIds", orgIds);
        }
        ChargeManageUtil.setPageInfo(data);
        List<DataVo> lists   = carIncomeMapper.carsCountDetailList(data);
        for (DataVo vo :lists){
            ChargeManageUtil.getUseTime(vo);
        }
        return new PageInfo(lists);
    }

    /**
     *  车辆收入详情统计汇总列表导出
     * @param data
     * @param response
     */
    public void carsCountDetailListExprot(DataVo data, HttpServletResponse response) throws Exception{
        List<DataVo> dvList=carsCountDetailList(data).getList();
        List<ComboxVo> boxList1  =dictService.getDictByType("cllx");
        for(DataVo vo :dvList){
            ChargeManageUtil.setDataVoPut("vehicleType",boxList1,vo);
        }
        List<String>	headList =  new ArrayList<>();
        List<String>	valList =  new ArrayList<>();
        headList.add("开始时间");
        headList.add("结束时间");
        headList.add("订单编号");
        headList.add("企业名");
        headList.add("所属场站");
        headList.add("车牌号");
        headList.add("车辆类型");
        headList.add("自编号");
        headList.add("线路");
//        headList.add("单价(元/kwh)");
        headList.add("充电时长");
        headList.add("充电量(kwh)");
        headList.add("消费金额(元)");
        headList.add("充电场站");
        headList.add("充电桩编号");
        valList.add("startTime");
        valList.add("endTime");
        valList.add("billPayNo");
        valList.add("orgName");
        valList.add("stationName");
        valList.add("licensePlate");
        valList.add("vehicleType");
        valList.add("onNumber");
        valList.add("line");
//        valList.add("price");
        valList.add("useTime");
        valList.add("chgPower");
        valList.add("amount");
        valList.add("cdStationName");
        valList.add("pileNo");
        ExportUtils.exportExcel(dvList,response,headList,valList,"车辆汇总详情表");

    }

    public PageInfo carsStation(DataVo data) {
        ChargeManageUtil.setPageInfo(data);
        List<DataVo> voList = carIncomeMapper.carsStation(data);
        return new PageInfo(voList);
    }

}

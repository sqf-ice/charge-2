package com.clouyun.charge.modules.charge.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ChargeInMapper {

     
     
     
     /**
      * 充电收入查询
      */
     List<DataVo> selectCdsr(Map dv);

     /**
      * 待结收入查询
      */
     List<DataVo> selectDjsr(Map dv);


     /**
      * 分成收入查询
      */
     List<DataVo> selectFcsr(Map dv);
     
     /**
      * 发票管理 查询
      */
     List<DataVo> selectFpgl(Map dv);

     /**
      * 场站收入查询
      */
     List<DataVo> selectCzsrCount(Map dv);

     /**
      *  查询daily集合
      */
     List<DataVo> selectDailyIncome(Map dv);
     /**
      *  查询分成收入详情
      */
     List<DataVo> selectDetailIncome(Map dv);
     /**
      *  查询场站收入详情
      */
    List<DataVo> detaiStation(Map dv);

    /**
     * 更新发票信息
     */
    Integer updateFpgl(Map dv);
    
    /**
     * 订单信息小票打印
     */
    Map printBillPay(DataVo vo);
    
    
    /**
     * 订单信息小票打印(多数据)
     */
    List printBillPays(DataVo vo);
    
     /**
      * 得到baillPay对象
      */
    DataVo getBillPay(Map map);
     /**
      * 得到chgRecordList对象
      */
     List<DataVo> getChgRecordList(Map map);
     /**
      * 得到PubDictItem对象
      */
     List<DataVo> getPubDictItem(Map map);

     /**
      * 查询运营商
      * @return
      */
     List<DataVo> getPubOrg(Map map);
     /**
      * 根据pileId获取充电站和充电桩信息
      * @return
      */
     List<DataVo> getChgPileList(Map map);

     /**
      * 得到Ccons对象
      * @return
      */
     DataVo getCcons(Map map);

     /**
      * 得到GroupList对象
      * @return
      */
     List<DataVo> getGroupList(Map map);
     /**
      * 得到VehicleInfo对象
      * 得到VehicleInfo对象
      * @return
      */
     List<DataVo> getVehicleInfo(Map map);

    /**
     * 第三方场站
     * @param data
     * @return
     */
    List<DataVo> selectCdsrCoop(Map data);
    /**
     * 得到发票管理BillPay
     * @param data
     * @return
     */
    DataVo    getFpBillPay(Map data);
    /**
     * 得到发票管理cinvoice
     * @param data
     * @return
     */
    DataVo    getCinvoice(Map data);

    /**
     * 发票管理详情
     * @param invoiceId
     * @return
     */
    DataVo detailFpgl(Integer invoiceId);

    /**
     *获取组织机构
     * @param vo
     * @return
     */
    Set<String> getOperatorIds(DataVo vo);

    /**
     * 分成收入合约企业&分成比例&分成金额
     * @param contractId
     * @return
     */
    List<DataVo> getInConTractCompany(String contractId);

    List<Data> divideConName(DataVo vo);

    List<Data> divideStationName(DataVo vo);

    DataVo getBillDetail(Map data);
    
    /**
     * 获取订单信息 (定时器数据,补定时器未启动之前数据)
     */
    List getBillPayByHistory(DataVo vo);
    /**
     * 新增充电时长表
     */
    void saveChargeTime(DataVo vo);
    /**
     * 新增收入指标表
     */
    void saveIncomeIndex(DataVo vo);

    /**
     * 查找枪编号
     * @param vo
     * @return
     */
    List<DataVo> gunList(DataVo vo);

    /**
     * 得到第三方场站
     * @param stationVo
     * @return
     */
    List<Map> stationMap(DataVo stationVo);

    /**
     * 第三方场站企业map
     * @param stationVo
     * @return
     */
    List<Map> pubOrgMap(DataVo stationVo);

    Set<String> getCodes(DataVo dataMap);

    List<DataVo> consIdList(DataVo setMap);

    DataVo selectCdsrCount(DataVo vo);

   DataVo getBillDetailCard(Map data);

    DataVo selectCdsrCoopCount(DataVo vo);

    DataVo selectDjsrCount(DataVo vo);

    List<DataVo> selectFcsrCount(DataVo vo);

    String selectFpglCount(DataVo vo);

    List<DataVo> getcConCompany(int contractId);

    List<DataVo> selectCzsrOrg(DataVo vo);

    List<DataVo> selectCzsrStation(DataVo vo);

    List<DataVo> detaiStationDate(DataVo vo);
    
    /**
     * 根据合约Id查询合约及对应的分成比例
     */
    List<DataVo> getcConCompanyList(DataVo vo);
}


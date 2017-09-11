package com.clouyun.charge.modules.charge.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import com.clouyun.boot.common.domain.DataVo;

@Mapper
public interface GatheringMapper {

     
     
     
     
     /**
      * 获取集团名称
      */
     List<DataVo> getGroup(Map dv);
     
     /**
      * 查询充值记录
      */
     List<DataVo> selectRecharge(Map dv);

     /**
      * 查询月结记录
      */
     List<DataVo> selectYuejie(Map dv);

     
     /**
      * 卡充值记录查询
      */
     List<DataVo> selectKcz(Map dv);

     /**
      * 查询集团表
      */
     List<DataVo> selectCgroup(Map dv);

     /**
      * 查询会员账户信息表
      */
     List<DataVo> selectCacct(Map dv);
     
     /**
      * 更新账户资金表
      */
     Integer updateCacct(Map dv);
     /**
      * 得到月结新增的缴费用户
      * @param vo
      * @return
      */
    List<DataVo> getGroupName(DataVo vo);

     /**
      * 新增月结中获取结款时间
      * @return
      */
     List<DataVo> getGroupBillMonth();

     /**
      * 新增月结中获取合约名称
      * @param vo
      * @return
      */
     List<DataVo> getConTractName(DataVo vo);

     /**
      * 得到月结新增的收款金额
      * @param vo
      * @return
      */
     DataVo   getYuejieMoney(DataVo vo);

    /**
     * 得到Cacct
     * @param dataMap
     * @return
     */
    DataVo findCacct(DataVo dataMap);

    /**
     * 得到cgb
     * @param map
     * @return
     */
    DataVo selectCGrupBill(Map map);

    /**
     * 得到getStationRela
     * @param tractId
     * @return
     */
    Set<Integer> getStationRela(String tractId);

    /**
     * 得到月结的bill
     * @param dataVo
     * @return
     */
    List<DataVo> getyujieBill(DataVo dataVo);

    /**
     *
     * @param cas
     */
    void intoCacctSeq(DataVo cas);

    void updateBillPay(DataVo billPayMap);

    void updateBgb(String vo);

    DataVo getBillPay(int billPayId);

    List<DataVo> ChgRecord(DataVo vo);

    DataVo getPile(int pileId);

    DataVo getPileNew(int pileId);

    void updateBill(DataVo billPay);

    DataVo getChgRecord(DataVo vo);

    DataVo selectRechargeCount(DataVo vo);

    DataVo selectYuejieCount(DataVo vo);

    DataVo selectKczCount(DataVo vo);

    Integer getYuejiList(DataVo yjDatavo);
}


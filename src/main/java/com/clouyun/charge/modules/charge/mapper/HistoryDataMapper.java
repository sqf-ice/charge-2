package com.clouyun.charge.modules.charge.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.clouyun.boot.common.domain.DataVo;

@Mapper
public interface HistoryDataMapper {

     
     
     
     
     /**
      * 历史数据查询
      */
     List<DataVo> selectHistoryData(Map dv);
     /**
      * 根据会员查询会员信息
      */
     DataVo findPayId(Map dv);
     /**
      * 查询ChgDataCur信息
      */
     List<DataVo> selectChgDataCur(Map dv);
     /**
      * 查询ChgPile信息
      */
     DataVo findChgPile(Map dv);
     /**
      * 查询ChgRc信息
      */
     List<DataVo> findChgRc(Map dv);

    List<DataVo> getToPile(DataVo vo);

     List<DataVo> getgunList(DataVo vo);

     DataVo getOrderList(DataVo vo);

    List<DataVo> getToStation(DataVo vo);
}


package com.clouyun.charge.modules.charge.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface CostMapper {

     
     
     
     /**
      * 充电成本查询
      */
     List<DataVo> selectCost(Map dv);

     /**
      * 收入报表查询
      */
     List<DataVo> selectIncome(Map dv);
     /**
      * 收入报表详情
      * @param data
      * @return
      */
    List<DataVo> detailIncome(Map data);
}


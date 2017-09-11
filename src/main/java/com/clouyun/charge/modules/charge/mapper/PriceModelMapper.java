package com.clouyun.charge.modules.charge.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.clouyun.boot.common.domain.DataVo;

@Mapper
public interface PriceModelMapper {

     /**
      * 模板列表查询
      */
     List<DataVo> selectPriceModel(Map dv);
     
     /**
      * 模板查询
      */
     List<DataVo> queryPriceModel(Map dv);
     /**
      * 电价任务查询
      */
     List<DataVo> selectDjrw(Map dv);


     /**
      *  删除电价模板
      */
     
     Integer delPriceModel(Map dv);
     
     
     /**
      *  删除电价任务
      */
     
     Integer delPriceTask(Map dv);
     
     
     
     
 	
 	/**
 	 * 费率时间段查询
 	 * 
 	 */
 	
 	List<DataVo> queryModelTime(Map dv);
 	
 	
 	/**
 	 * 查询电价任务
 	 */
 	List<DataVo> queryDjrw(Map dv);
 	
 	
 	
 	/**
 	 * 查询电价任务与充电桩关联关系表
 	 */
 	List<DataVo> queryPptr(Map dv);
 	
 	
 	/**
 	 * 查询充电桩信息
 	 */
 	List<DataVo> queryChgpile(Map dv);
 	
 	
 	/**
 	 * 新增电价任务
 	 */
 	 Integer  insertPriceTask(Map dv);
 	
 	
 	/**
 	 * 向电价任务与充电桩关联关系表插入记录
 	 */
 	 Integer insertRela(Map dv);

	/**
	 * 电价描述
	 * @param data
	 * @return
	 */
    List<DataVo> describetPriceModel(Map data);

	/**
	 * 查询task任务
	 * @param data
	 * @return
	 */
	DataVo getPriceTask(Map data);

	/**
	 * 查询pubPrice表
	 * @param data
	 * @return
	 */
	DataVo getPubPrice(Map data);

    List<DataVo> priceTask(DataVo vo);

	/**
	 * 添加模板
	 * @param vo
	 */
	void addPriceModel(DataVo vo);

	/**
	 * 时间费率
	 * @param vo
	 */
	void addModelTime(DataVo vo);

    List<DataVo> priceModel(DataVo vo);
	List<DataVo> pileList(DataVo vo);

	List<DataVo> stationList(DataVo vo);

	DataVo getTaskPrice(DataVo dv);

    List<DataVo> pileTaskrela(DataVo vo);
}


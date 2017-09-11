package com.clouyun.charge.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;

@Mapper
public interface ChgStationMapper {
	
	//新增场站信息
    public int insertChgStation(DataVo dv);
    
    //新增场站的充电桩类型信息
    public int insertChgPileType(DataVo dv);
	
    //新增模板 
    public int insertPubPrice(DataVo dv);
    
    //新增费率对应的时间段 
    public int insertModelTime(DataVo dv);
    
    //根据stationId查询充电桩类型信息
    List<DataVo> selectChgPileType(DataVo dv);
    
}
package com.clouyun.charge.modules.document.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年6月13日 下午2:18:49
 */
@Mapper
public interface ItDeviceMapper {

    List<Map> queryItDevices(Map map);

    int insert(Map map);

    Map queryItDeviceByKey(Integer deviceId);

    Integer dissDevice(List list);

    int update(Map map);

    int validateArr(Map map);
}
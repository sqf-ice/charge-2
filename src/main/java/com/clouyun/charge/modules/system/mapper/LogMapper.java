package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 描述: 日志表操作(sys_log)
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月11日
 */
@Mapper
public interface LogMapper {

    /**
     * 分页查询日志列表
     * @param data
     * @return
     */
    List<DataVo> getLogsByPage(Map data);

    /**
     * 根据日志表ID查询
     * @param logId
     * @return
     */
    DataVo getLogById(Integer logId);

    /**
     * 新增日志
     *
     * @param data
     * @return
     */
    int insertLog(DataVo data);

}

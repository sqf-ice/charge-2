package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述: 资讯表操作(c_info)
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月11日
 */
@Mapper
public interface InfomationMapper {

    /**
     * 分页查询资讯列表
     * @param data
     * @return
     */
    List<DataVo> getInfomationsByPage(Map data);

    /**
     * 根据资讯表ID查询
     * @param infoId
     * @return
     */
    Map getInfomationById(@Param("infoId") Integer infoId);

    /**
     * 新增资讯
     *
     * @param data
     * @return
     */
    int insertInfomation(Map data);

    /**
     * 更新资讯
     *
     * @param data
     * @return
     */
    int updateInfomation(Map data);

    /**
     * 删除资讯
     * @param infoIds
     * @return
     */
    int delInfomationByIds(@Param("infoIds") List infoIds);
}

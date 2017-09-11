package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述: 字典表操作(c_info)
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月12日
 */
@Mapper
public interface DictMapper {
    /**
     * 查询一级字典列表
     * @param data
     * @return
     */
    List getLevel1Dicts(Map data);

    /**
     * 根据pid查询二级字典列表
     * @param data
     * @return
     */
    List getLevel2Dicts(Map data);


    /**
     * 根据Ids删除字典
     * @param dictIds
     * @return
     */
    int delDictsByIds(@Param("dictIds")List<Integer> dictIds);


    /**
     * 新增字典
     * @param data
     * @return
     */
    int insertDict(Map data);

    /**
     * 根据ID获取字典详情
     * @param id
     * @return
     */
    Map getDictById(@Param("id")Integer id);

    /**
     * 根据Ids获取列表
     * @param dictIds
     * @return
     */
    List getDictsByIds(@Param("dictIds")List dictIds);

    /**
     * 根据ID更新字典
     * @param data
     * @return
     */
    int updateDict(Map data);

    /**
     * 根据父类型type找到二级字典
     * @param type
     * @return
     */
    List getDictByType(@Param("type")String type);

    /**
     * 查询所有字典返回前端
     * @return
     */
    List<DataVo> getAllDicts();
}
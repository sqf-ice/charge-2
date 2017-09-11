package com.clouyun.charge.modules.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年05月13日
 */
@Mapper
public interface CoopOperMapper {

    /**
     * 根据企业ID获取合作运营企业ID集合
     * @param orgIds
     * @return
     */
    List<Integer> getCoopOperOrgIds(@Param("orgIds") List<Integer> orgIds);

    /**
     * 根据合作运营企业ID反向关联获取企业ID集合也能根据企业ID获取合作运营企业ID集合
     * @param orgIds
     * @return
     */
    List<Integer> getReverseCoopOperOrgIds(@Param("orgIds") List<Integer> orgIds);

    /**
     * 根据企业ID获取合作运营企业的场站ID集合
     * @param orgIds
     * @return
     */
    List<Integer> getCoopOperStationIds(@Param("orgIds") List<Integer> orgIds);

    /**
     * 根据被合作运营企业ID获取合作企业ID集合
     * @param coopOrgId
     * @return
     */
    List<Integer> getOrgIdsByCoopOrgId(@Param("coopOrgId") Integer coopOrgId);

    /**
     * 根据被合作运营企业ID获取合作企业名称集合
     * @param coopOrgId
     * @return
     */
    List<String> getOrgNamesByCoopOrgId(@Param("coopOrgId") Integer coopOrgId);

    /**
     * 根据被合作运营企业ID和合作运营企业ID集合删除合作运营关联
     * @param coopOrgId
     * @param orgIds
     * @return
     */
    int deleteCoopOperByCoopId(@Param("coopOrgId") Integer coopOrgId, @Param("orgIds") List<Integer> orgIds);

    /**
     * 根据被合作运营企业ID和合作运营企业ID集合新增合作运营关联
     * @param coopOrgId
     * @param orgIds
     * @return
     */
    int insertCoopOperByCoopId(@Param("coopOrgId") Integer coopOrgId, @Param("orgIds") List orgIds);
}

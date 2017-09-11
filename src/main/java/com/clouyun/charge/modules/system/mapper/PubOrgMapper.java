package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface PubOrgMapper {

	//新增企业-基本信息
    int insertPubOrg(DataVo dv);

    //根据orgId查询企业信息 
  	DataVo selectPubOrgByOrgId(DataVo dv);
  	
    //根据orgId修改企业信息 
  	int updatePubOrgByOrgId(DataVo dv);
  	
	//根据地区编码查询
     List<DataVo> selectPubOrgByDistCode(DataVo dv);
     
     // 企业审核查询 
     List<DataVo> selectPubOrgAudit(DataVo dv);
     
     //企业审核统计
     Integer countAudit(DataVo dv);
     
     // 企业未审核,审核通过，审核不通过的详细信息查询
     List<DataVo> detailAudit(DataVo dv);
     
    //查询企业场站信息
     List<DataVo> detailAuditChg(DataVo dv);
     
     //获取企业审核的审核人列表，查询企业表 
     List<DataVo> queryAuditMember(DataVo dv);

    // 企业管理分页查询
    List<DataVo> getOrgsByPage(Map data);

    // 根据企业ID获取详细信息
    DataVo getOrgById(@Param("orgId")Integer orgId);
    
    /**
     * 查询运营商业务字典
     */
    List getOrg(DataVo vo);

    /**
     * 平台新增企业
     * @param data
     * @return
     */
    Integer insertOrg(Map data);

    //查询未审核企业
    List<DataVo> getAuditOrgsByPage(Map data);

    /**
     * 企业审核根据企业ID查询未审核数据
     * @param orgId
     * @return
     */
    DataVo getAuditOrgById(@Param("orgId") Integer orgId);

    /**
     * 企业审核根据企业ID获取临时场站信息
     * @param orgId
     * @return
     */
    List<DataVo> getStationTempByOrgId(@Param("orgId") Integer orgId);

    /**
     * 获取审核人下拉框数据
     * @return
     */
    List<ComboxVo> getAuditMember();

    /**
     * 获取企业名称下拉框
     * @param orgIds
     * @return
     */
    List<ComboxVo> getOrgNameById(@Param("orgIds") Set<Integer> orgIds);

    /**
     * 根据orgName获取企业信息
     * @param orgName
     * @return
     */
    List<DataVo> getOrgByName(@Param("orgName") String orgName);

    /**
     * 根据验证字段和验证字段值查询总数
     * @param orgId
     * @param chkFileName
     * @param chkValue
     * @return
     */
    Integer getOrgCountByName(@Param("orgId")Integer orgId, @Param("chkFileName")String chkFileName,@Param("chkValue")String chkValue);

    /**
     * 根据orgIds查询orgCode
     */
    List<DataVo> getOrgCodeByIds(@Param("orgIds") Set<Integer> orgIds);
}


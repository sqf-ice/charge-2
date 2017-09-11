package com.clouyun.charge.modules.document.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.clouyun.boot.common.domain.DataVo;


/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年2月20日 下午5:18:49
 */
@Mapper
public interface QrCodeMapper {
	//单一主键查询
    Map selectByPrimaryKey(Integer pileId);
    
    List<Map> selectByListKey(List list);
    
    List<Map> selectAll(Map map);
    
    Integer count(Map map);
    
    int inserChgGun(List list);
    
    int inserChgMeter(List list);

    /**
     * 根据充电桩Id查询充电枪的二维码
     * @param map
     * @return
     */
    List<Map> selectChgGunByPileId(Collection list);
    
    /**
     * 根据充电桩Id查询表计信息
     * @param map
     * @return
     */
    List<Map> selectChgmeterByPileId(Map map);
    
    /**
     * 用编号查询字典表
     */
    List<Map> selectPubDictItemByKey(Map map);
    
    /**
     * 验证二维码的唯一性
     */
    Integer QRCodeCount(Map map);
    
    /**
     * 修改二维码
     */
    int updateGumPointQRCode(List list);
    
    /**
     * 更改二维码下发状态
     * @param map
     * @return
     */
    int updatePileCodeState(Map map);
    
    /**
     * 更改互联互通二维码下发状态
     * @param map
     * @return
     */
    int updateHLHTCodeState(Map map);
    
    /**
     * 新增二维码
     */
    int insertQRCode(Map map);
    
    /**
     * 根据充电桩编号获取充电桩信息
     */
    List<Map> selectPileByPileNo(Map map);
    
    /**
     * 修改表计
     */
    int updateChgMeter(List list);
    
}
package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.ui.ComboxVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 区域字典操作表(pub_area)
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年05月02日
 */
@Mapper
public interface AreaMapper {
    /**
     * 根据类型获取区域字典
     * @param type
     * @return
     */
    List<ComboxVo> getAreaByType(@Param("type")String type);

    /**
     * 根据父类编码获取区域字典
     * @param pno
     * @return
     */
    List<ComboxVo> getAreaByParentNo(@Param("pno")String pno);
}

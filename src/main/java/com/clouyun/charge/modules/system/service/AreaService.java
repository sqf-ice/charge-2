package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.services.CacheService;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.RedisKeyEnum;
import com.clouyun.charge.modules.system.mapper.AreaMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.clouyun.charge.common.constant.RedisKeyEnum.AREA_01_KEY;

/**
 * 描述: 区域字典服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年05月02日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AreaService extends BusinessService {

    private static final Logger logger = LoggerFactory.getLogger(AreaService.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AreaMapper areaMapper;

    // //所有下拉字典key
    //private static final String AREA_COMBOX_KEY = "pub_combox_area_20170526";
    //
    //// 一级区域字典key
    //private static final String AREA_01_KEY = "area_type_1";
    //
    //// 所有区域缓存key，key值为区域编码，value值为区域名称
    //private static final String AREA_NO_KEY = "pub_no_area_20170526";
    //
    //// 所有区域缓存key，key值为区域名称，value值为区域编码
    //private static final String AREA_NAME_KEY = "pub_name_area_20170526";

    /**
     * 根据pno获取区域字典，如果没有父节点编码则获取一级区域字典
     *
     * @param pno 父节点编码
     * @return
     * @description 缓存异常失败不影响数据返回
     */
    public List<ComboxVo> getAreasByNo(String pno) throws BizException {
        Map<String, List<ComboxVo>> map = cacheService.get(RedisKeyEnum.AREA_COMBOX_KEY.value);
        List<ComboxVo> list;
        if (map == null || map.isEmpty()) {
            map = Maps.newHashMap();
            // 如果为空，则查询一级区域地址字典
            if (StringUtils.isBlank(pno)) {
                list = areaMapper.getAreaByType("01");
                map.put(AREA_01_KEY.value, list);
            } else {
                list = areaMapper.getAreaByParentNo(pno);
                map.put(pno, list);
            }
            try {
                cacheService.set(RedisKeyEnum.AREA_COMBOX_KEY.value, map);
            } catch (Exception e) {
                logger.error("区域字典写入缓存失败，异常：{}", e);
            }
        } else {
            if (StringUtils.isBlank(pno)) {
                list = map.get(AREA_01_KEY);
                if (CollectionUtils.isEmpty(list)) {
                    list = areaMapper.getAreaByType("01");
                    map.put(RedisKeyEnum.AREA_01_KEY.value, list);
                    try {
                        cacheService.set(RedisKeyEnum.AREA_COMBOX_KEY.value, map);
                    } catch (Exception e) {
                        logger.error("区域字典写入缓存失败，异常：{}", e);
                    }
                }
            } else {
                list = map.get(pno);
                if (CollectionUtils.isEmpty(list)) {
                    list = areaMapper.getAreaByParentNo(pno);
                    map.put(pno, list);
                    try {
                        cacheService.set(RedisKeyEnum.AREA_COMBOX_KEY.value, map);
                    } catch (Exception e) {
                        logger.error("区域字典写入缓存失败，异常：{}", e);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 根据区域编码返回区域名称
     *
     * @param areaNo 区域编码
     * @return
     * @throws BizException
     */
    public String getAreaNameByNo(String areaNo) throws BizException {
        if (StringUtils.isBlank(areaNo))
            return "";
        Map<String, String> map = cacheService.get(RedisKeyEnum.AREA_NO_KEY.value);
        if (map == null || map.isEmpty()) {
            List<ComboxVo> list = areaMapper.getAreaByType(null);
            map = Maps.newHashMap();
            for (ComboxVo comboxVo : list) {
                map.put(comboxVo.getId(), comboxVo.getText());
            }
            try {
                cacheService.set(RedisKeyEnum.AREA_NO_KEY.value, map);
            } catch (Exception e) {
                logger.error("区域字典写入缓存失败，异常：{}", e);
            }
        }
        return map.get(areaNo);
    }

    /**
     * 根据区域名称返回区域编码
     *
     * @param areaName 区域名称
     * @return areaNo
     * @throws BizException
     */
    public String getAreaNoByName(String areaName) throws BizException {
        if (StringUtils.isBlank(areaName))
            return "";
        Map<String, String> map = cacheService.get(RedisKeyEnum.AREA_NAME_KEY.value);
        if (map == null || map.isEmpty()) {
            List<ComboxVo> list = areaMapper.getAreaByType(null);
            map = Maps.newHashMap();
            for (ComboxVo comboxVo : list) {
                map.put(comboxVo.getText(), comboxVo.getId());
            }
            try {
                cacheService.set(RedisKeyEnum.AREA_NAME_KEY.value, map);
            } catch (Exception e) {
                logger.error("区域字典写入缓存失败，异常：{}", e);
            }
        }
        return map.get(areaName);
    }
}

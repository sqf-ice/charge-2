package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ui.ComboxVo;
import com.clouyun.boot.common.domain.ui.Tree;
import com.clouyun.boot.common.domain.ui.TreeNode;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.boot.services.CacheService;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.RedisKeyEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.modules.system.mapper.DictMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * 描述: 字典管理服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月12日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictService extends BusinessService {

    private static Logger logger = LoggerFactory.getLogger(DictService.class);

    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private CacheService cacheService;


    //// 缓存库字典key值前缀
    //private static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 查找一级字典返回树
     *
     * @param data
     * @return
     * @throws BizException
     */
    public List findLeve1Dicts(Map data) throws BizException {
        List<TreeNode> dicts = dictMapper.getLevel1Dicts(data);
        for (TreeNode node : dicts) {
            // 借用字段，0为用户不可编辑
            if (node.getLevel() == 0) {
                node.setPid(-2);
            } else {
                node.setPid(-1);
            }
        }
        // 创建可编辑字典父节点
        TreeNode node = new TreeNode();
        node.setPid(0);
        node.setName("可编辑类");
        node.setId(-1);
        // 创建不可编辑字典父节点
        TreeNode node1 = new TreeNode();
        node1.setPid(0);
        node1.setName("不可编辑类");
        node1.setId(-2);
        // 新建对象添加的集合中
        dicts.add(node);
        dicts.add(node1);
        Tree tree = new Tree();
        //// 转树形结构
        return tree.list(dicts);
    }

    /**
     * 查找二级字典列表
     *
     * @param data
     * @return
     * @throws BizException
     */
    public PageInfo findLeve2Dicts(Map data) throws BizException {
        DataVo params = new DataVo(data);
        CommonUtils.idIsEmpty(params.getInt("pid"), "二级字典父节点ID");
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(data);
        }
        List dicts = dictMapper.getLevel2Dicts(data);
        PageInfo page = new PageInfo(dicts);
        return page;
    }

    /**
     * 查找字典详细信息
     *
     * @param dictId
     * @return
     */
    public Map findDictById(Integer dictId) throws BizException {
        return dictMapper.getDictById(dictId);
    }

    /**
     * 保存字典
     *
     * @param data
     * @return
     * @throws BizException
     */
    public int insertDict(Map data) throws BizException {
        DataVo vo = new DataVo(data);
        // parentId为空或为0默认为一级字典
        Integer parentId = vo.getInt("parentId");

        // 现有一级字典不允许编辑的情况下，parentId不可能为0
        CommonUtils.idIsEmpty(parentId, "二级字典父节点ID");
        CommonUtils.valIsEmpty(vo.getString("label"), "规则名称");
        CommonUtils.valIsEmpty(vo.getString("value"), "规则编码");
        CommonUtils.idIsEmpty(vo.getInt("updateBy"), "更新者");
        CommonUtils.valIsEmpty(vo.getString("sort"), "排序字段");
        // 字典标识，一级字典库中保证唯一，现在只能修改二级字典，暂不考虑
        String type = vo.getString("type");
        CommonUtils.valIsEmpty(type, "字典类型");
        int i = dictMapper.insertDict(data);

        // 父级菜单内存库中key
        String key = RedisKeyEnum.SYS_DICT_KEY.value + type;
        // 如果内存中存在，则删除
        if (cacheService.exists(key)) {
            // 删除内存库中对应缓存
            cacheService.remove(key);
        }
        return i;
    }

    /**
     * 更新字典
     *
     * @param data
     * @return
     * @throws BizException
     */
    public int updateDict(Map data) throws BizException {
        DataVo vo = new DataVo(data);
        // parentId为空或为0默认为一级字典
        Integer parentId = vo.getInt("parentId");
        CommonUtils.idIsEmpty(vo.getId(), "更新数据主键ID");
        CommonUtils.idIsEmpty(parentId, "二级字典父节点ID");
        CommonUtils.valIsEmpty(vo.getString("label"), "规则名称");
        CommonUtils.valIsEmpty(vo.getString("value"), "规则编码");
        CommonUtils.idIsEmpty(vo.getInt("updateBy"), "更新者");
        CommonUtils.valIsEmpty(vo.getString("sort"), "排序字段");
        // 字典标识，一级字典库中保证唯一，现在只能修改二级字典，暂不考虑
        String type = vo.getString("type");
        CommonUtils.valIsEmpty(type, "字典类型");
        int i = dictMapper.updateDict(data);

        // 父级菜单内存库中key
        String key = RedisKeyEnum.SYS_DICT_KEY.value + type;
        // 如果内存中存在，则删除
        if (cacheService.exists(key)) {
            // 删除内存库中对应缓存
            cacheService.remove(key);
        }
        return i;
    }


    /**
     * 删除Ids删除多个字典
     *
     * @param dictIds
     * @return
     * @throws BizException
     */
    public int delDictsByIds(List dictIds) throws BizException {
        if (CollectionUtils.isEmpty(dictIds))
            throw new BizException(1001001);
        List<Map> list = dictMapper.getDictsByIds(dictIds);
        List<Integer> delList = Lists.newArrayList();
        for (Map map : list) {
            DataVo vo = new DataVo(map);
            if (vo.getInt("parentId") == 0)
                throw new BizException(1001000);
            delList.add(vo.getId());
            // 父级菜单内存库中key
            String key = RedisKeyEnum.SYS_DICT_KEY.value + vo.getString("type");
            // 如果内存中存在，则删除
            if (cacheService.exists(key)) {
                // 删除内存库中对应缓存
                cacheService.remove(key);
            }
        }

        return dictMapper.delDictsByIds(delList);
    }

    /**
     * 根据父类型获取二级字典组成下拉框
     *
     * @param type
     * @return
     * @throws BizException
     */
    public List<ComboxVo> getDictByType(String type) throws BizException {
        // 先从内存库获取，不判断key是否存在，直接获取，没有直接返回null
        List<ComboxVo> list = cacheService.get(RedisKeyEnum.SYS_DICT_KEY.value + type);

        // 如果内存库没有则查数据库
        if (CollectionUtils.isEmpty(list)) {
            if (StringUtils.isNotBlank(type)) {
                list = dictMapper.getDictByType(type);
                // 如果数据库存在，则放入内存库中
                this.setDictCache(type, list);
            }
        }
        return list;
    }


    /**
     * 根据字典类型和字典value值返回字典文本
     * @param type 字典类型
     * @param value 字典value值
     * @return
     * @throws BizException
     */
    public String getDictLabel(String type, String value) throws BizException {
        if(StringUtils.isBlank(type) || StringUtils.isBlank(value))
            return "";
        // 先从内存库获取，不判断key是否存在，直接获取，没有直接返回null
        List<ComboxVo> list = cacheService.get(RedisKeyEnum.SYS_DICT_KEY.value + type);
        String label = "";
        // 如果内存库没有则查数据库
        if (CollectionUtils.isEmpty(list)) {
            list = dictMapper.getDictByType(type);
            // 如果数据库存在，则放入内存库中，否则直接返回空
            this.setDictCache(type, list);
        }
        for (ComboxVo comboxVo : list) {
            if (value.equals(comboxVo.getId())) {
                label = comboxVo.getText();
                break;
            }
        }
        return label;
    }

    /**
     * 根据字典类型和字典文本值返回字典value值
     * @param type 字典类型
     * @param label 字典label值
     * @return
     * @throws BizException
     */
    public String getDictValue(String type, String label) throws BizException {
        if(StringUtils.isBlank(type) || StringUtils.isBlank(label))
            return "";
        // 先从内存库获取，不判断key是否存在，直接获取，没有直接返回null
        List<ComboxVo> list = cacheService.get(RedisKeyEnum.SYS_DICT_KEY.value + type);
        String value = "";
        // 如果内存库没有则查数据库
        if (CollectionUtils.isEmpty(list)) {
            list = dictMapper.getDictByType(type);
            // 如果数据库存在，则放入内存库中，否则直接返回空
            this.setDictCache(type, list);
        }
        for (ComboxVo comboxVo : list) {
            if (label.equals(comboxVo.getText())) {
                value = comboxVo.getId();
                break;
            }
        }
        return value;
    }

    /**
     * 根据字典类型讲字典集合缓存到redis中
     * @param type 字典类型
     * @param list 字典数据集合
     */
    private void setDictCache(String type, List<ComboxVo> list) {
        if (StringUtils.isNotBlank(type) && CollectionUtils.isNotEmpty(list)) {
            // 存内存库失败，不影响字典返回
            try {
                cacheService.set(RedisKeyEnum.SYS_DICT_KEY.value + type, list);
            } catch (Exception e) {
                logger.error("字典缓存失败,key值：" + RedisKeyEnum.SYS_DICT_KEY.value + type, e);
            }
        }
    }

    public Map findAllDicts() throws BizException {
        List<DataVo> list = dictMapper.getAllDicts();
        SortedMap<String,List<DataVo>> map = Maps.newTreeMap();
        for (DataVo vo : list) {
            String key = vo.getString("type");
            vo.remove("type");
            if (map.containsKey(key)) {
                map.get(key).add(vo);
            } else {
                List<DataVo> list1 = Lists.newArrayList();
                list1.add(vo);
                map.put(key, list1);
            }
        }
        return map;
    }

    /**
     * 删除缓存库字典值
     * @param keys
     * @throws BizException
     */
    public void delCacheDict(List<String> keys) throws BizException {
        if (CollectionUtils.isEmpty(keys))
            throw new BizException(1001001);
        //CollectionUtils.filter(keys, new Predicate() {
        //    @Override
        //    public boolean evaluate(Object o) {
        //        return o.toString().startsWith(RedisKeyEnum.SYS_DICT_KEY.value);
        //    }
        //});
        for (String key : keys) {
            cacheService.remove(RedisKeyEnum.SYS_DICT_KEY.value + key);
        }
    }
}


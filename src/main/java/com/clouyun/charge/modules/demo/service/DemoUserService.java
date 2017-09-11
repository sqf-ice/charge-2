package com.clouyun.charge.modules.demo.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.charge.ChargeManageUtil;
import com.clouyun.charge.modules.demo.mapper.DemoUserMapper;
import com.clouyun.charge.modules.demo.vo.DemoUser;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 用户业务类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: 00063587
 * 版本: 2.0.0
 * 创建日期:2017年09月04日
 */
@Service
public class DemoUserService {

    @Autowired
    DemoUserMapper demoUserMapper;

    //根据用户ID获取用户对象
    public DataVo findUserById(Integer id){
        return demoUserMapper.findUserById(id);
    }

    //根据用户名获取用户对象
    public DataVo findUserByUserName(String userName){
        return demoUserMapper.findUserByUserName(userName);
    }

    //增加用户
    public void addUser(DataVo vo){
        DataVo user = demoUserMapper.findUserByUserName(vo.get("userName").toString());
        if (user!=null)
            //TODO  待处理
            System.out.println("用户名已存在");
        else {
            if(vo.get("userStatus")==null)
                vo.put("userStatus","01");
            demoUserMapper.addUser(vo);
        }
    }

    //根据用户ID删除用户
    public void delUserById(Integer id){
        demoUserMapper.delUserById(id);
    }

    //根据用户IDs批量删除用户
    public void delUserByIds(List ids){
        demoUserMapper.delUserByIds(ids);
    }

    //根据ID更新用户数据
    public  void updateUser(DataVo vo){
        demoUserMapper.updateUser(vo);
    }

    //条件查询,分页
    public PageInfo findByCondition(DataVo vo) {
        ChargeManageUtil.setPageInfo(vo);
        List<DataVo> lists = demoUserMapper.findByCondition(vo);//查询用户list
        return new PageInfo(lists);
    }

    //统计数量
    public Integer pageCount(DataVo vo){
        return demoUserMapper.pageCount(vo);
    }

}
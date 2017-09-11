package com.clouyun.charge.modules.demo.mapper;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.charge.modules.demo.vo.DemoUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by 00063587 on 2017/9/4.
 */
@Mapper
public interface DemoUserMapper {

    //根据用户ID获取用户对象
    DataVo findUserById(Integer id);

    //根据用户名获取用户对象
    DataVo findUserByUserName(String userName);

    //增加用户
    void addUser(DataVo vo);

    //根据用户ID删除用户
    void delUserById(Integer id);

    //根据用户IDs批量删除用户
    void delUserByIds(List ids);

    //根据ID更新用户数据
    void updateUser(DataVo vo);

    //条件查询，分页
    List<DataVo> findByCondition(DataVo vo);

    //user数量
    Integer pageCount(DataVo vo);


}

package com.clouyun.charge.modules.system.mapper;

import com.clouyun.boot.common.domain.DataVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年02月21日
 */
@Mapper
public interface LockMapper {

    /**
     * 新增登录锁
     *
     * @return
     */
    public int addLock(DataVo data);

    public DataVo findLockByUserId(int userId);

    public int updateLock(DataVo data);
}

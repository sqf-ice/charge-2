package com.clouyun.charge.common;

import com.clouyun.boot.modules.spring.web.BaseService;
import com.clouyun.charge.modules.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月11日
 */
public class BusinessService extends BaseService {
    @Autowired
    protected LogService logService;

    /**
     * 保存日志
     *
     * @param title    日志标题
     * @param type     操作类型
     * @param desc     日志描述
     * @param objectId 操作对象ID
     */
    protected void saveLog(String title, String type,String desc, Integer objectId) {
        logService.insertLog(title, type, desc, objectId);
    }
}

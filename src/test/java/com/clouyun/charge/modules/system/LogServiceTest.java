package com.clouyun.charge.modules.system;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.system.service.LogService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月11日
 */
public class LogServiceTest extends ApplicationTests {
    @Autowired
    LogService logService;
    @Override
    public void initialize() {

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
    @Test
    public void insertlog() throws BizException {
        //DataVo map = new DataVo();
        //map.put("type","2");
        //map.put("title","测试业务日志保存");
        //map.put("createBy",1);
        //logService.insertLog("","",1,"");
    }
}

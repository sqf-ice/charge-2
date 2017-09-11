package com.clouyun.charge.modules.system;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.system.service.InfomationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月12日
 */
public class InfomationServiceTest extends ApplicationTests {
    @Autowired
    private InfomationService infomationService;
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

    /**
     * 测试修改资讯
     * @throws BizException
     */
    //@Test
    //public void updateInfomation() throws BizException {
    //    Map map = Maps.newHashMap();
    //    map.put("infoId",9);
    //    map.put("infoHead","测试修改资讯标题");
    //    System.out.println(infomationService.updateInfomation(map));
    //}
    //
    ///**
    // * 测试修改资讯
    // * @throws BizException
    // */
    //@Test
    //public void insertInfomation() throws BizException {
    //    Map map = Maps.newHashMap();
    //    map.put("infoHead","测试新增资讯标题");
    //    infomationService.insertInfomation(map);
    //}
}

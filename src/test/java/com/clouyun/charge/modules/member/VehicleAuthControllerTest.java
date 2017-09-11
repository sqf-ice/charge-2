package com.clouyun.charge.modules.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.member.web.VehicleAuthController;


/**
 * 
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 2.0.0
 * 创建日期: 2017年6月19日
 */
public class VehicleAuthControllerTest extends ApplicationTests{

    @Autowired
    private VehicleAuthController vehicleAuthController;//你要测试的Controller

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleAuthController).build();
    }
    /**
     * 测试更新
     * @throws Exception
     */
    @Test
    public void authVehicle() throws Exception {
        String dictJson = FileUtils.read("/modules/member/authVehicle.json");
        System.out.println("读取到的Json文件:" + dictJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(put("/api/vehicleauths")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(dictJson));
        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
        //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    @Override
    public void initialize() {

    }
}

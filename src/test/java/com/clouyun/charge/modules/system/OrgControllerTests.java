package com.clouyun.charge.modules.system;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.system.web.OrgController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 描述: 企业控制器测试类
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月19日
 */
public class OrgControllerTests extends ApplicationTests{

    @Autowired
    private OrgController orgController;//你要测试的Controller

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orgController).build();
    }


    /**
     * 测试保存企业
     * @throws Exception
     */
    @Test
    public void saveOrg() throws Exception {
        String orgJson = FileUtils.read("/modules/system/saveOrg.json");
        System.out.println("读取到的Json文件:" + orgJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/org")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(orgJson));
        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
        //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    /**
     * 测试角色
     * @throws Exception
     */
    @Test
    public void updateRole() throws Exception {
        String dictJson = FileUtils.read("/modules/system/updateRole.json");
        System.out.println("读取到的Json文件:" + dictJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(put("/api/roles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(dictJson));
        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
        //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }

    /**
     * 测试删除字典
     * @throws Exception
     */
    @Test
    public void delDict() throws Exception {
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(delete("/api/dicts/1005"));
        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
        //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }

    @Test
    public void auditOrg() throws Exception {
        String dictJson = FileUtils.read("/modules/system/auditOrg.json");
        System.out.println("读取到的Json文件:" + dictJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(put("/api/org/audit")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(dictJson));
        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
        //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    /**
     * 测试删除字典
     * @throws Exception
     */
    @Test
    public void saveUser() throws Exception {
        String dictJson = FileUtils.read("/modules/system/saveOrg.json");
        System.out.println("读取到的Json文件:" + dictJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/org/register")
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

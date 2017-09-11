package com.clouyun.charge.modules.member;

import com.clouyun.boot.common.utils.FileUtils;
import com.clouyun.charge.ApplicationTests;
import com.clouyun.charge.modules.member.web.MemberController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: caowei
 * 版本: 1.0
 * 创建日期: 2017年4月19日 下午1:56:54
 */
public class MembersControllerTests extends ApplicationTests{

    @Autowired
    private MemberController memberController;//你要测试的Controller

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }


    /**
     * 查询所有
     */
    @Test
    public void queryall() throws Exception {
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(get("/api/members?userId=1"))
                //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
                .andExpect(status().isOk());
                //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }
    
    /**
     * 查询流水
     */
    @Test
    public void querykey() throws Exception {
    	//调用接口，传入添加的用户参数
    	ResultActions ra = mockMvc.perform(get("/api/members/flowWaters?consId=10000316"))
    			//判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
    			.andExpect(status().isOk());
    	//此处使用jsonPath解析返回值，判断具体的内容
    	MvcResult mr = ra.andReturn();
    	String result = mr.getResponse().getContentAsString();
    	System.out.println("状态返回值：" + mr.getResponse().getStatus());
    	System.out.println("测试数据返回值:" + result.toString());
    }
    /**
     * 测试保存
     * @throws Exception
     */
    @Test
    public void save() throws Exception {
        String dictJson = FileUtils.read("/modules/member/saveMember.json");
        System.out.println("读取到的Json文件:" + dictJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(post("/api/members?userId=1")
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
     * 测试更新
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        String dictJson = FileUtils.read("/modules/member/updateMember.json");
        System.out.println("读取到的Json文件:" + dictJson);
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(put("/api/members?userId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(dictJson));
        //判断返回值，是否达到预期，测试示例中的返回值的结构如下{"errorCode":0,"errorMsg":"OK"}
        //此处使用jsonPath解析返回值，判断具体的内容
        MvcResult mr = ra.andReturn();
        String result = mr.getResponse().getContentAsString();
        System.out.println("状态返回值：" + mr.getResponse().getStatus());
        System.out.println("测试数据返回值:" + result.toString());
    }

    @Test
    public void del() throws Exception {
        //调用接口，传入添加的用户参数
        ResultActions ra = mockMvc.perform(delete("/api/members/88?userId=1"));
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

package com.clouyun.charge.modules.demo.web;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.modules.demo.service.DemoUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 描述: 用户Controller
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: 00063587
 * 版本: 1.0.0
 * 创建日期:2017年09月04日
 */
@RestController
@RequestMapping("/api/demoUser")
public class DemoUserController {

    @Autowired
    DemoUserService demoUserService;

    /**
     * @api {post} /api/demoUser/login   登录
     * @apiName login
     * @apiGroup DemoUserController
     * @apiVersion 1.0.0
     * @apiDescription 用户登录
     * <br/>
     * @apiParam {String}          userName                   用户名
     * @apiParam {String}          userPwd                    密码
     * <br/>
     * @apiSuccess {String}       errorCode                   错误码
     * @apiSuccess {String}       errorMsg                    消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultVo login(@RequestParam("userName") String userName, @RequestParam("userPwd") String userPwd) throws Exception {
        ResultVo resultVo = new ResultVo();
        DataVo vo = demoUserService.findUserByUserName(userName);
        if (vo!=null&&vo.get("userPwd").toString().equals(userPwd))
            return resultVo;
        else {
            resultVo.setErrorCode(1);
            resultVo.setErrorMsg("用户名或者密码错误");
        }
        return resultVo;
    }

    /**
     * @api {post} /api/demoUser/addUser   增加/注册
     * @apiName addUser
     * @apiGroup DemoUserController
     * @apiVersion 1.0.0
     * @apiDescription  添加用户/用户注册
     * <br/>
     * @apiParam {String}          userName                   用户名
     * @apiParam {String}          userPwd                    密码
     * @apiParam {String}          [userGender]               性别
     * @apiParam {String}          [userPhone]                手机号码
     * @apiParam {String}          [userEmail]                邮箱
     * @apiParam {String}          [userAddr]                 住址
     * <br/>
     * @apiSuccess {String}       errorCode                   错误码
     * @apiSuccess {String}       errorMsg                    消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ResultVo addUser(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        DataVo vo = new DataVo(data);
        demoUserService.addUser(vo);
        return resultVo;
    }

    /**
     * @api {delete} /api/demoUser/delUserById/{userId}   删除
     * @apiName deleteUser
     * @apiGroup DemoUserController
     * @apiVersion 1.0.0
     * @apiDescription  根据id删除用户信息
     * <br/>
     * @apiParam {Integer}        userId                      用户ID
     * <br/>
     * @apiSuccess {String}       errorCode                   错误码
     * @apiSuccess {String}       errorMsg                    消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     */
    @RequestMapping(value = "/delUserById/{userId}", method = {RequestMethod.DELETE})
    public ResultVo deleteUser(@PathVariable("userId") Integer userId) throws Exception {
        ResultVo resultVo = new ResultVo();
        demoUserService.delUserById(userId);
        return resultVo;
    }

    /**
     * @api {delete} /api/demoUser/delUsersByIds/{userIds}   批量删除
     * @apiName deleteUsers
     * @apiGroup DemoUserController
     * @apiVersion 1.0.0
     * @apiDescription  根据id批量删除用户信息
     * <br/>
     * @apiParam {String}        userIds                      用户IDs
     * <br/>
     * @apiSuccess {String}       errorCode                   错误码
     * @apiSuccess {String}       errorMsg                    消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     */
    @RequestMapping(value = "/delUsersByIds/{userIds}", method = {RequestMethod.DELETE})
    public ResultVo deleteUsers(@PathVariable("userIds") List userIds) throws Exception {
        ResultVo resultVo = new ResultVo();
        demoUserService.delUserByIds(userIds);
        return resultVo;
    }

    /**
     * @api {put} /api/demoUser/updateUser   更新、修改数据
     * @apiName updateUser
     * @apiGroup DemoUserController
     * @apiVersion 1.0.0
     * @apiDescription  修改用户数据
     * <br/>
     * @apiParam {Integer}         userId                     用户id
     * @apiParam {String}          [userName]                 用户名
     * @apiParam {String}          [userPwd]                  密码
     * @apiParam {String}          [userGender]               性别
     * @apiParam {String}          [userPhone]                手机号码
     * @apiParam {String}          [userEmail]                邮箱
     * @apiParam {String}          [userAddr]                 住址
     * <br/>
     * @apiSuccess {String}       errorCode                   错误码
     * @apiSuccess {String}       errorMsg                    消息说明
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
    public ResultVo updateUser(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        DataVo vo = new DataVo(data);
        demoUserService.updateUser(vo);
        return resultVo;
    }

    /**
     * @api {get} /api/demoUser/userlist   分页查询用户列表
     * @apiName queryUsers
     * @apiGroup DemoUserController
     * @apiVersion 1.0.0
     * @apiDescription  查询用户列表
     * <br/>
     * @apiParam {String}          [userName]                 用户名
     * @apiParam {String}          [userPwd]                  密码
     * @apiParam {String}          [userGender]               性别
     * @apiParam {String}          [userPhone]                手机号码
     * @apiParam {String}          [userEmail]                邮箱
     * @apiParam {String}          [userAddr]                 住址
     * @apiParam {String}          [userStatus]               用户状态
     * @apiParam {Integer}         pageNum                    页码
     * @apiParam {Integer}         pageSize                   页大小
     * <br/>
     * @apiSuccess {String}       errorCode                   错误码
     * @apiSuccess {String}       errorMsg                    消息说明
     * @apiSuccess {Object}       data                        分页数据封装
     * @apiSuccess {Integer}      data.total                  总记录数
     * @apiSuccess {Object[]}     data.list                   分页数据对象数组
     * @apiSuccess {Int}          data.list.userId            用户id
     * @apiSuccess {String}       data.list.userName          用户名
     * @apiSuccess {String}       data.list.userPwd           用户密码
     * @apiSuccess {String}       data.list.userGender        性别
     * @apiSuccess {String}       data.list.userPhone         手机号码
     * @apiSuccess {String}       data.list.userEmail         邮箱
     * @apiSuccess {String}       data.list.userAddr          住址
     * @apiSuccess {String}       data.list.userStatus        用户状态
     * <br/>
     * @apiError -999 系统异常!
     * @apiError -888 请求方式异常!
     */
    @RequestMapping(value = "/userlist", method = RequestMethod.GET)
    public ResultVo queryUsers(@RequestParam Map data) throws Exception {
        ResultVo resultVo = new ResultVo();
        DataVo vo  =  new DataVo(data);
        PageInfo pageInfo = demoUserService.findByCondition(vo);
        resultVo.setData(pageInfo);
        return resultVo;
    }

}
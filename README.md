
**mvn任务**
- `$ gulp` to build an optimized version of your application in folder dist
- `$ gulp serve` to start BrowserSync server on your source files with live reload
- `$ gulp serve:dist` to start BrowserSync server on your optimized application without live reload
- `$ gulp test` to run your unit tests with Karma
- `$ gulp test:auto` to run your unit tests with Karma in watch mode
- `$ gulp protractor` to launch your e2e tests with Protractor
- `$ gulp protractor:dist` to launch your e2e tests with Protractor on the dist files

**目录结构**
```
ROOT
 |-src
    |-main 业务代码位置
       |-common 公用模块
       |-modules 业务模块
           |-charge 充电管理
               |-cost 充电成本
               |-history 历史数据
               |-income 充电收入
               |-recharge 充值管理
               |-setting 电价设置
           |-document 档案管理
                |-card  卡片管理      
                |-contract 合约管理
                |-device 设备管理
                |-stations 场站管理
                |-version 版本管理
           |-electricity 用电分析
                |-collectMonitor
                |-electroMonitor
                |-lossAnalysis
                |-lossModel
           |-finance 财务管理
                |-cfoApproval 总监审批
                |-finaApproval 财务审批 
           |-member  会员管理
                |-group 集团信息      
                |-integral 积分管理
                |-member 会员信息
                |-promotion 促销管理
           |-monitor  实时监控
                |-alarm 告警管理     
                |-deviceMonitor 设备监控
                |-overview 运营总览
                |-vehicleMonitor  车辆监控              
           |-system 系统管理
                |-company 企业管理
                |-config 配置管理
                |-file 文件下载
                |-info 资讯管理
                |-log 日志管理
                |-role 角色管理
                |-user 用户管理
           |-vehicle  车辆管理
                |-carDriver  驾驶员           
                |-vehicleInfo 车辆信息
                |-vehicleRevenue 车辆收入
           |-withdraw  提现管理
                |-finaDetails 财务明细
                |-withDetails 提现明细         
    |-test 测试代码位置
```

**APIs**

这里需要补充APIs


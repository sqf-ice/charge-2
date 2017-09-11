package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.constant.RoleDataEnum;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.common.utils.ExportUtils;
import com.clouyun.charge.common.utils.OSSUploadFileUtils;
import com.clouyun.charge.modules.system.mapper.InfomationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述: 资讯管理服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月11日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InfomationService extends BusinessService {

    @Autowired
    private InfomationMapper infomationMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserService userService;

    /**
     * 查找资讯列表
     *
     * @param data
     * @return
     * @throws BizException
     */
    public PageInfo findInfomations(Map data) throws BizException {
        DataVo params = new DataVo(data);
        CommonUtils.idIsEmpty(params.getInt("userId"), "登录用户");
        data.put("orgIds", userService.getUserRoleDataById(params.getInt("userId"), RoleDataEnum.ORG.dataType));
        // 如果有分页参数，则进行分页，否则查全部的
        if (params.isNotBlank("pageNum") && params.isNotBlank("pageSize")) {
            PageHelper.startPage(data);
        }
        List<DataVo> infomations = infomationMapper.getInfomationsByPage(data);
        PageInfo page = new PageInfo(infomations);
        return page;
    }

    /**
     * 查找资讯详细信息
     *
     * @param infoId
     * @return
     */
    public Map findInfomationById(Integer infoId) throws BizException {
        Map info = infomationMapper.getInfomationById(infoId);
        return info;
    }

    /**
     * 保存资讯
     *
     * @param data
     * @return
     * @throws BizException
     */
    public int insertInfomation(Map data, MultipartFile file) throws BizException {
        DataVo params = new DataVo(data);
        CommonUtils.valIsEmpty(params.getString("infoHead"), "资讯标题");
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String imageFileName = "images/" + new Date().getTime() + fileName.substring(fileName.lastIndexOf("."));
            OSSUploadFileUtils.toUploadFile(file, imageFileName);
            data.put("infoPic", imageFileName);
        }
        // 如果资讯内容不为空生产html文件
        if (params.isNotBlank("infoBody")) {
            String htmlName = "information/" + CommonUtils.randomNum() + ".html";
            writeHtml(params, htmlName);
            data.put("infoUrl", htmlName);
        }
        return infomationMapper.insertInfomation(data);

    }

    /**
     * 更新资讯
     *
     * @param data
     * @return
     * @throws BizException
     */
    public int updateInfomation(Map data, MultipartFile file) throws BizException {
        DataVo params = new DataVo(data);
        CommonUtils.idIsEmpty(params.getInt("infoId"), "更新数据主键ID");
        CommonUtils.valIsEmpty(params.getString("infoHead"), "资讯标题");
        if (file != null && !file.isEmpty()) {
            String imageFileName;
            // 旧图片地址
            String infoPic = params.getString("infoPic");
            // 如果不存在则新命名，否则直接覆盖旧文件
            if (StringUtils.isBlank(infoPic)) {
                String fileName = file.getOriginalFilename();
                imageFileName = "images/" + CommonUtils.randomNum() + fileName.substring(fileName.lastIndexOf("."));
                data.put("infoPic", imageFileName);
            } else {
                imageFileName = infoPic;
                // 移除该字段，不再重复更新
                data.remove("infoPic");
            }
            OSSUploadFileUtils.toUploadFile(file, imageFileName);
        }
        // 如果资讯内容不为空生产html文件
        if (params.isNotBlank("infoBody")) {
            String infoUrl = params.getString("infoUrl");
            String htmlName;
            // 如果已经存在html地址则覆盖
            if (StringUtils.isBlank(infoUrl)) {
                htmlName = "information/" + CommonUtils.randomNum() + ".html";
                data.put("infoUrl", htmlName);
            } else {
                htmlName = infoUrl;
                // 避免相同字段重复更新
                data.remove("infoUrl");
            }
            writeHtml(params, htmlName);
        }
        return infomationMapper.updateInfomation(data);
    }

    /**
     * 将资讯内容写成html文件
     *
     * @param info
     * @param htmlName
     */
    private void writeHtml(DataVo info, String htmlName) throws BizException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
        sb.append("<meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1\">");
        sb.append("<title>" + info.getString("infoHead") + "</title>");
        sb.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">");
        sb.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div style=\"width:100%;margin:0 auto;\">");
        sb.append("<center><h1>" + info.getString("infoHead") + "</h1><center>");
        sb.append(info.getString("infoBody"));
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        OSSUploadFileUtils.toUploadFile(sb.toString(), htmlName);

    }

    /**
     * 删除资讯
     *
     * @param infoIds
     * @return
     * @throws BizException
     */
    public int delInfomationByIds(List infoIds) throws BizException {
        return infomationMapper.delInfomationByIds(infoIds);
    }

    public void exportInfo(Map data, HttpServletResponse response) throws Exception {
        List<DataVo> list = infomationMapper.getInfomationsByPage(data);
        for (DataVo vo : list) {
            vo.set("infoType", dictService.getDictLabel("zxlx", vo.getString("infoType")));
            vo.set("infoStatus", dictService.getDictLabel("zxzt", vo.getString("infoStatus")));
        }
        //结果集
        List<String> headList = Lists.newArrayList();
        List<String> valList = Lists.newArrayList();
        headList.add("资讯标题");
        headList.add("资讯类型");
        headList.add("创建时间");
        headList.add("活动开始时间");
        headList.add("活动结束时间");
        headList.add("资讯状态");
        headList.add("点赞次数");
        headList.add("分享次数");

        valList.add("infoHead");
        valList.add("infoType");
        valList.add("infoPublishTime");
        valList.add("infoStartTime");
        valList.add("infoEndTime");
        valList.add("infoStatus");
        valList.add("infoBeingLiked");
        valList.add("infoBeingShared");
        ExportUtils.exportExcel(list, response, headList, valList, "资讯管理");
    }
}

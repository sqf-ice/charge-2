package com.clouyun.charge.common.helper;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.boot.common.utils.StringUtils;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年07月19日
 */
public class FileHelper {
    // 定义允许上传的文件扩展名
    private static final Map<String, String> extMap = Maps.newHashMap();

    static {
        // 其中images,flashs,medias,files,对应文件夹名称,对应dirName
        // key文件夹名称
        // value该文件夹内可以上传文件的后缀名
        extMap.put("images", "gif,jpg,jpeg,png,bmp");
        extMap.put("flashs", "swf,flv");
        extMap.put("medias", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("files", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
        extMap.put("sensitive", "txt");
    }

    public static void verifyExt(String folder, String suffix) throws BizException {
        String var = extMap.get(folder);
        if (StringUtils.isBlank(var))
            throw new BizException(1002000);
        if (!Arrays.asList(var.split(",")).contains(suffix))
            throw new BizException(1006000);

    }
}

package com.clouyun.charge;

import com.clouyun.boot.common.domain.ResultVo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 描述: 上传文件
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年05月04日
 */
@RestController
public class UploadFile {
    private static final String FILE_PATH = "D:\\temp";

    @RequestMapping(value = "/upload")
    public ResultVo upload(@RequestParam MultipartFile file) throws Exception {
        String fileAbsolutePath = FILE_PATH + "\\20170504\\" + file.getOriginalFilename();
        File lFile = new File(fileAbsolutePath);
        lFile.getParentFile().mkdirs();
        try {
            OutputStream os = new FileOutputStream(lFile);
            IOUtils.copy(file.getInputStream(), os);
            os.close();
        } catch (IOException e) {

        }
        return new ResultVo();
    }
}

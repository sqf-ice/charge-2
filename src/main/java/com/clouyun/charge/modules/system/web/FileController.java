package com.clouyun.charge.modules.system.web;

import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.charge.common.BusinessController;
import com.clouyun.charge.modules.system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: OSS文件控制器
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年07月19日
 */
@RestController
@RequestMapping("/api/files")
public class FileController extends BusinessController {
	
	@Autowired
	private FileService fileService;

	/**
	 * @api {GET} /api/files 文件下载列表
	 * @apiName queryFiles
	 * @apiGroup FileController
	 * @apiVersion 2.0.0
	 * @apiDescription 杨帅A 获取文件列表
	 * <br/>
	 * @apiSuccess {Object} data 返回数据对象
	 * @apiSuccess {String} data.key 所属类别(key值是动态的，可直接在前端展示)
	 * @apiSuccess {Object[]} data.key 当前类别下包含的文件集合
	 * @apiSuccess {String} data.key.fileName 文件名称
	 * @apiSuccess {String} data.key.type 所属类别
	 * @apiSuccess {String} data.key.url 下载路径
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResultVo queryFiles() throws Exception {
		ResultVo resultVo = new ResultVo();
		resultVo.setData(fileService.findFiles());
		return resultVo;
	}
}

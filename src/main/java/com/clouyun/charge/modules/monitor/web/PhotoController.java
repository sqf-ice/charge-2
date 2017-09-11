package com.clouyun.charge.modules.monitor.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.domain.ResultVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.service.PhotoService;

/**
 * 
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月5日
 */
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
	
	@Autowired
	PhotoService photoService;
	
	
	@RequestMapping(value = "/listByTaskId", method = RequestMethod.POST)
	public ResultVo queryPhotolistByTaskId(@RequestBody DataVo vo) throws BizException{
		ResultVo resultVo = new ResultVo();
		List<Map> list = photoService.queryPhotolistByTaskId(vo);
		resultVo.setData(list);
		return resultVo;
	}

}

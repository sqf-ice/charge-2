package com.clouyun.charge.modules.monitor.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.modules.monitor.mapper.PhotoMapper;

/**
 * 
 * 描述: 
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: gaohui
 * 版本: 1.0
 * 创建日期: 2017年4月5日
 */
@Service
@SuppressWarnings("rawtypes")
public class PhotoService {
	
	@Autowired
	private PhotoMapper photoMapper;
	/**
	 * 根据taskId获取图片信息
	 * @param map
	 * @return
	 * @throws BizException
	 * 2017年4月7日
	 * gaohui
	 */
	public List<Map> queryPhotolistByTaskId(Map map)throws BizException{
		
		return photoMapper.findPhotoListByTaskId(map);
	}

}

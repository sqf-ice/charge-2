package com.clouyun.charge.modules.system.service;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.exception.BizException;
import com.clouyun.charge.common.BusinessService;
import com.clouyun.charge.common.utils.CommonUtils;
import com.clouyun.charge.modules.system.mapper.FileMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.SortedMap;

/**
 * 描述: 用户管理服务层
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年02月21日
 */
@Service
public class FileService extends BusinessService {
	
	@Autowired
	private FileMapper fileMapper;
	
	public SortedMap<String, List<DataVo>> findFiles() throws BizException {
		List<DataVo> list = fileMapper.getFiles();
		SortedMap<String, List<DataVo>> map = Maps.newTreeMap();
		for (DataVo vo : list) {
			String key = vo.getString("type");
			vo.set("url", CommonUtils.PATH + vo.getString("url"));
			if (map.containsKey(key)) {
				map.get(key).add(vo);
			} else {
				List<DataVo> list1 = Lists.newArrayList();
				list1.add(vo);
				map.put(key, list1);
			}
		}
		return map;
	}
}
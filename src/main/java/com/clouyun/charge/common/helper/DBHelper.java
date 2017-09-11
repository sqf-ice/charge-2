package com.clouyun.charge.common.helper;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 2.0
 * 创建日期: 2017年07月26日
 */
public class DBHelper {
	
	// 最大批量插入数量
	private static final int BATCH_IN_MAX_SIZE = 100;
	
	// 最大批量更新数量
	private static final int BATCH_UP_MAX_SIZE = 100;
	
	/**
	 * 批量添加
	 * @param list
	 * @param batchIn
	 * @return
	 */
	public Integer batchInsert(List list, BatchIn batchIn) throws Exception {
		int count = 0;
		if (CollectionUtils.isNotEmpty(list)) {
			List list1;
			for (int i = 0; i < list.size() / BATCH_IN_MAX_SIZE + 1; i++) {
				if ((i + 1) * BATCH_IN_MAX_SIZE < list.size()) {
					list1 = list.subList(i * BATCH_IN_MAX_SIZE, (i + 1) * BATCH_IN_MAX_SIZE);
					batchIn.batchInsert(list1);
					count += list1.size();
				} else {
					list1 = list.subList(i * BATCH_IN_MAX_SIZE, list.size());
					batchIn.batchInsert(list1);
					count += list1.size();
					break;
				}
			}
		}
		return count;
	}
	
	/**
	 * 批量添加
	 * @param set
	 * @param batchIn
	 * @return
	 * @throws Exception
	 */
	public Integer batchInsert(Set set, BatchIn batchIn) throws Exception {
		return this.batchInsert(Lists.newArrayList(set), batchIn);
	}
	
	/**
	 * 批量更新
	 * @param set
	 * @param batchIn
	 * @return
	 * @throws Exception
	 */
	public Integer batchUpdate(Set set, BatchIn batchIn) throws Exception {
		return this.batchUpdate(Lists.newArrayList(set), batchIn);
	}
	
	/**
	 * 批量更新
	 * @param list
	 * @param batchInterface
	 * @return
	 */
	public Integer batchUpdate(List list, BatchIn batchInterface) {
		int count = 0;
		if (list != null && list.size() > 0) {
			List list1;
			for (int i = 0; i < list.size() / BATCH_UP_MAX_SIZE + 1; i++) {
				if ((i + 1) * BATCH_UP_MAX_SIZE < list.size()) {
					list1 = list.subList(i * BATCH_UP_MAX_SIZE, (i + 1) * BATCH_UP_MAX_SIZE);
					batchInterface.batchUpdate(list1);
					count += list1.size();
				} else {
					list1 = list.subList(i * BATCH_UP_MAX_SIZE, list.size());
					batchInterface.batchUpdate(list1);
					count += list1.size();
					break;
				}
			}
		}
		return count;
	}
}

package com.clouyun.charge.common.helper;

import java.util.List;

public interface BatchIn {
	
	/**
	 * 批量添加
	 * @param list
	 * @return
	 */
	Integer batchInsert(List list);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	Integer batchUpdate(List list);
}

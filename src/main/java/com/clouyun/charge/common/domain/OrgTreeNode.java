package com.clouyun.charge.common.domain;

import java.util.List;

import com.clouyun.boot.common.domain.ui.TreeNode;

/**
 * 描述: TODO
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: libiao <libiao@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年03月02日
 */
public class OrgTreeNode  extends TreeNode{
    private String orgNo;
	/**
	 * @return the orgNo
	 */
	public String getOrgNo() {
		return orgNo;
	}

	/**
	 * @param orgNo the orgNo to set
	 */
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
}

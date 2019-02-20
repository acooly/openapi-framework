/*
* acooly.cn Inc.
* Copyright (c) 2019 All Rights Reserved.
* create by zhike
* date:2019-02-20
*/
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 问题分类表 Entity
 *
 * @author zhike
 * Date: 2019-02-20 17:09:59
 */
@Entity
@Table(name = "api_qa_classify")
@Getter
@Setter
public class ApiQaClassify extends AbstractEntity {

	/** 分类名称 */
	@Size(max=255)
    private String name;

	/** 父类ID */
    private Long parentId;

	/** 路径 */
	@Size(max=255)
    private String path;

	/** 排序辅助值 */
	@NotNull
    private Long sortTime = System.currentTimeMillis();

	/** 备注 */
	@Size(max=255)
    private String comments;

	/**
	 * 所有的子
	 */
	@Transient
	private List<ApiQaClassify> children = new LinkedList<ApiQaClassify>();

	public void addChild(ApiQaClassify node) {
		this.children.add(node);
	}

	public static class NodeComparator implements Comparator<ApiQaClassify> {
		@Override
		public int compare(ApiQaClassify node1, ApiQaClassify node2) {
			long orderTime1 = node1.getSortTime();
			long orderTime2 = node2.getSortTime();
			return orderTime1 > orderTime2 ? -1 : (orderTime1 == orderTime2 ? 0 : 1);
		}
	}

}

/*
 * acooly.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * zhangpu 2015年8月6日 上午3:32:51 创建
 */
package com.acooly.openapi.framework.common.utils.ketama;

/**
 * 节点
 *
 * @author zhangpu
 */
public class KetamaNode {
	/**
	 * 节点名称
	 */
	private String name;
	/**
	 * 节点描述
	 */
	private String data;

	/**
	 * 构造函数
	 */
	public KetamaNode(String name) {
		this.name = name;
	}

	public KetamaNode(String name, String descn) {
		this.name = name;
		this.data = descn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override public String toString() {
		return "KetamaNode{" +
			   "name='" + name + '\'' +
			   ", data='" + data + '\'' +
			   '}';
	}
}

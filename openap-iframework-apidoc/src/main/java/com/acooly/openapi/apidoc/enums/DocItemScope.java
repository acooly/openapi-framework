/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.enums;

/**
 * Created by liubin on 2015-07-27.
 */
public enum DocItemScope {

	SYSTEM("系统"),
	SERVICE("服务");

	String msg;

	DocItemScope(String msg){
		this.msg = msg;
	}
}

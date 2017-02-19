/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 03:31 创建
 *
 */
package com.yiji.framework.openapi.core.listener;

/**
 * 执行顺序
 * @author qzhanbo@yiji.com
 */
public interface Ordered {
	
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
	
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 数字越小越先被执行
     * @return
     */
	int getOrder();
	
}

/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-27 18:11 创建
 *
 */
package com.yiji.framework.openapi.core.auth.permission;

/**
 * @author qzhanbo@yiji.com
 */
public interface Permission {
    /**
     * 判断资源是否有权限.
     * @param resource
     * @return
     */
	boolean implies(String resource);
}

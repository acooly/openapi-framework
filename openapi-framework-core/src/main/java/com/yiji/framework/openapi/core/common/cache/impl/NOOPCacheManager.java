/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-08-12 16:31 创建
 *
 */
package com.yiji.framework.openapi.core.common.cache.impl;

import com.yiji.framework.openapi.core.common.cache.CacheManager;

/**
 * @author qzhanbo@yiji.com
 */
public class NOOPCacheManager implements CacheManager {
	@Override
	public void add(String key, Object value) {

	}

	@Override
	public void add(String key, Object value, int holdSecond) {

	}

	@Override
	public Object get(String key) {
		return null;
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void cleanup(String key) {

	}

}

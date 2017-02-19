/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-27 02:19 创建
 *
 */
package com.yiji.framework.openapi.core.service.base;

import java.util.Map;

/**
 * @author qzhanbo@yiji.com
 */
public interface ServiceValidator {
	void validate(Map<String, String> requestData);
}

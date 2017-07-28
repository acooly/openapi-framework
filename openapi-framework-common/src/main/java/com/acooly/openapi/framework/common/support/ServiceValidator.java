/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 02:19 创建
 *
 */
package com.acooly.openapi.framework.common.support;

import java.util.Map;

/**
 * @author qiubo@qq.com
 */
public interface ServiceValidator {
	void validate(Map<String, String> requestData);
}

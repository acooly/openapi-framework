/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.core.marshall;

import com.yiji.framework.openapi.common.enums.ApiProtocol;

/**
 * 组装接口
 * 
 * @author zhangpu
 */
public interface ApiMarshall<T, S> {

	T marshall(S source);

	ApiProtocol getProtocol();

}

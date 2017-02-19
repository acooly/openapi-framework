/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.core.marshall;

import com.yiji.framework.openapi.common.message.ApiRequest;

/**
 * 请求报文组装接口
 * 
 * @author zhangpu
 * 
 * @param <T>
 */
public interface ApiRequestMarshall<T extends ApiRequest, S> extends ApiMarshall<T, S> {

	@Override
	T marshall(S source);

}

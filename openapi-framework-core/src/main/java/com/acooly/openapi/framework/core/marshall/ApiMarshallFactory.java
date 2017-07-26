/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall;

import com.acooly.openapi.framework.common.enums.ApiProtocol;

/**
 * Marshall 工厂接口
 * 
 * @author zhangpu
 */
public interface ApiMarshallFactory {

	ApiRequestMarshall getRequestMarshall(ApiProtocol protocols);

	ApiResponseMarshall getResponseMarshall(ApiProtocol protocols);

	ApiRedirectMarshall getRedirectMarshall(ApiProtocol protocols);

	ApiNotifyMarshall getNotifyMarshall(ApiProtocol protocols);

}

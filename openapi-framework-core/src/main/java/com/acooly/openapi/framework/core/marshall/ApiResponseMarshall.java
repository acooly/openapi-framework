/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall;

import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * 同步响应组装
 *
 * @param <T>
 * @param <S>
 * @author zhangpu
 */
public interface ApiResponseMarshall<T, S extends ApiResponse> extends ApiMarshall<T, S> {}

/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.core.marshall;


import com.yiji.framework.openapi.common.message.ApiResponse;

/**
 * 同步响应组装
 *
 * @param <T>
 * @param <S>
 * @author zhangpu
 */
public interface ApiResponseMarshall<T, S extends ApiResponse> extends ApiMarshall<T, S> {

}

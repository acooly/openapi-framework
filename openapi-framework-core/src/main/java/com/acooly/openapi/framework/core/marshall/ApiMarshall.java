/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall;

import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.enums.ApiProtocol;

/**
 * 组装接口
 *
 * @author zhangpu
 */
public interface ApiMarshall<T, S> {

    /**
     * 解析
     *
     * @param source
     * @return
     */
    T marshall(S source);

    /**
     * 标记协议
     *
     * @return
     */
    default ApiProtocol getProtocol() {
        return ApiProtocol.JSON;
    }

    /**
     * 报文类型
     *
     * @return
     */
    ApiMessageType getApiMessageType();


}

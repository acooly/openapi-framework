/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:26
 */
package com.acooly.openapi.framework.core;

import com.acooly.openapi.framework.common.enums.ApiProtocol;

/**
 * @author zhangpu
 * @date 2019-09-06 11:26
 */
public interface ProtocolAble {

    /**
     * 标记协议
     *
     * @return
     */
    ApiProtocol getApiProtocol();
}

/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-08 11:30
 */
package com.acooly.openapi.framework.core.marshall;

import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.enums.ApiProtocol;

/**
 * 报文组装实现工厂
 *
 * @author zhangpu
 * @date 2019-09-08 11:30
 */
public interface ApiMarshallFactory {

    ApiMarshall getApiMarshall(ApiProtocol apiProtocol, ApiMessageType apiMessageType);


    /**
     * 获取请求解析实现
     *
     * @param apiProtocol
     * @return
     */
    default ApiRequestMarshall getRequestMarshall(ApiProtocol apiProtocol) {
        return (ApiRequestMarshall) getApiMarshall(apiProtocol, ApiMessageType.Request);
    }

    /**
     * 获取响应解析实现
     *
     * @param apiProtocol
     * @return
     */
    default ApiResponseMarshall getResponseMarshall(ApiProtocol apiProtocol) {
        return (ApiResponseMarshall) getApiMarshall(apiProtocol, ApiMessageType.Response);
    }

    /**
     * 获取通知解析实现
     *
     * @param apiProtocol
     * @return
     */
    default ApiNotifyMarshall getNotifyMarshall(ApiProtocol apiProtocol) {
        return (ApiNotifyMarshall) getApiMarshall(apiProtocol, ApiMessageType.Notify);
    }

    /**
     * 获取跳转解析实现
     *
     * @param apiProtocol
     * @return
     */
    default ApiRedirectMarshall getRedirectMarshall(ApiProtocol apiProtocol) {
        return (ApiRedirectMarshall) getApiMarshall(apiProtocol, ApiMessageType.Redirect);
    }

}

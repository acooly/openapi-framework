/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.service.base;

import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.util.GenericsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Api服务基类
 *
 * <p>提供转换工具和帮助支持
 *
 * @author zhangpu
 * @date 2014年5月16日
 */
public abstract class AbstractAsyncApiService<O extends ApiRequest, R extends ApiResponse, N extends ApiNotify>
        extends AbstractApiService<O, R> {

    protected static Logger logger = LoggerFactory.getLogger(AbstractAsyncApiService.class);

    private Class<N> notifyClazz;

    @Override
    public N getApiNotifyBean() {
        if (notifyClazz == null) {
            notifyClazz = GenericsUtils.getSuperClassGenricType(getClass(), 2);
        }
        try {
            if (notifyClazz.equals(Object.class)) {
                return (N) new ApiNotify();
            } else {
                return BeanUtils.instantiateClass(notifyClazz);
            }
        } catch (Exception e) {
            throw new RuntimeException("实例化Notify对象失败:" + notifyClazz.toString());
        }
    }

}

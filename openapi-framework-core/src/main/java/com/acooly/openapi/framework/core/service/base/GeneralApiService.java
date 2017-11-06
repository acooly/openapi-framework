/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.service.base;

import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.listener.ApiListener;
import com.acooly.openapi.framework.core.listener.OpenApiEventMulticaster;
import com.acooly.openapi.framework.core.listener.event.ServiceEvent;
import com.acooly.openapi.framework.core.listener.multicaster.ServiceEventMulticaster;
import com.acooly.openapi.framework.core.util.GenericsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * 服务层框架:泛型和拦截封装
 *
 * @param <O>
 * @param <R>
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 * @date 2014年8月3日
 */
public abstract class GeneralApiService<O extends ApiRequest, R extends ApiResponse>
    implements ApiService<O, R> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private OpenApiEventMulticaster serviceEventMulticaster = new ServiceEventMulticaster();

  private Class<O> requestClazz;
  private Class<R> responseClazz;

  @Override
  public O getRequestBean() {
    if (requestClazz == null) {
      requestClazz = GenericsUtils.getSuperClassGenricType(getClass(), 0);
    }
    try {
      if (requestClazz.equals(Object.class)) {
        return (O) new ApiRequest();
      } else {
        return BeanUtils.instantiate(requestClazz);
      }
    } catch (Exception e) {
      throw new RuntimeException("实例化Request对象失败:" + requestClazz.toString());
    }
  }

  @Override
  public R getResponseBean() {
    if (responseClazz == null) {
      responseClazz = GenericsUtils.getSuperClassGenricType(getClass(), 1);
    }
    try {
      if (responseClazz.equals(Object.class)) {
        return (R) new ApiResponse();
      } else {
        return BeanUtils.instantiate(responseClazz);
      }
    } catch (Exception e) {
      throw new RuntimeException("实例化Response对象失败:" + responseClazz.toString());
    }
  }

  /**
   * 添加服务监听器
   *
   * @param listener
   */
  protected void addListener(ApiListener<? extends ServiceEvent> listener) {
    serviceEventMulticaster.addListener(listener);
  }

  /**
   * 删除服务监听器
   *
   * @param listener
   */
  protected void removeListener(ApiListener<? extends ServiceEvent> listener) {
    serviceEventMulticaster.removeListener(listener);
  }

  /** 删除所有服务监听器 */
  protected void removeAllListeners() {
    serviceEventMulticaster.removeAllListeners();
  }

  @Override
  public void publishEvent(ServiceEvent event) {
    serviceEventMulticaster.publishEvent(event);
  }

  @Override
  public boolean canPublish() {
    return serviceEventMulticaster.canPublish();
  }
}

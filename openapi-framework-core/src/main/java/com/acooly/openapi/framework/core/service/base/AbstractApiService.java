/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.service.base;

import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.OpenApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.service.OrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Api Service 抽象模板实现
 *
 * <p>service处理模板，统一错误处理
 *
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 */
public abstract class AbstractApiService<O extends ApiRequest, R extends ApiResponse>
    extends GeneralApiService<O, R> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractApiService.class);

  /** 订单持久化服务 */
  @Resource private OrderInfoService orderInfoService;

  /**
   * 默认异步处理方法.用于构造异步响应对象.此方法会把外部请求对象中所有的参数填充到ApiNotify
   *
   * @param orderInfo 请求订单信息
   * @param data 外部调用时传入的对象
   * @return
   */
  @Override
  public final ApiNotify handleNotify(OrderDto orderInfo, Object data) {
    ApiNotifyOrder apiNotifyOrder = (ApiNotifyOrder) data;
    ApiNotify apiNotify = getApiNotifyBean();
    BeanCopier.copy(orderInfo, apiNotify, "notifyUrl", "returnUrl");
    ObjectAccessor<ApiNotify> objectAccessor = ObjectAccessor.of(apiNotify);
    for (Map.Entry<String, String> entry : apiNotifyOrder.getParameters().entrySet()) {
      objectAccessor.setPropertyValue(entry.getKey(), entry.getValue());
    }
    // 对数据库的orderNo特别处理
    customizeApiNotify(orderInfo, apiNotifyOrder, apiNotify);
    return apiNotify;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public final void service(ApiContext apiContext) {
    try {
      if (apiContext.getOpenApiService().busiType() != ApiBusiType.Query
          && OpenApiConstants.saveOrder) {
        saveOrder(apiContext);
      }
      doService((O)apiContext.getRequest(),(R) apiContext.getResponse());
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * 服务处理方法
   *
   * <p>如果抛出非ApiServiceException异常,对外响应内部错误
   *
   * <p>如果抛出ApiServiceException异常,会根据异常的信息返回给用户
   *
   * <p>
   *
   * @param request
   * @param response
   */
  protected abstract void doService(O request, R response);

  /**
   * 异步服务返回对象
   *
   * @param orderInfo 原始订单信息
   * @param apiNotifyOrder 外部调用过来准备推送的数据
   * @param apiNotify 准备发给cs的推送内容对象
   */
  protected void customizeApiNotify(
          OrderDto orderInfo, ApiNotifyOrder apiNotifyOrder, ApiNotify apiNotify) {}

  /**
   * 异步通知entity默认使用基类，API业务服务可以根据需求定义ApiNotify子类，然后在ApiService中覆写该方法返回子类的类型，
   * handleNotify抽象实现会自动填充完成通知对象组装
   *
   * @return
   */
  @Override
  public ApiNotify getApiNotifyBean() {
    return new ApiNotify();
  }

  protected String getGid() {
    return ApiContextHolder.getApiContext().getGid();
  }

  public final void saveOrder(ApiContext apiContext) {
    try {
      ApiRequest request = apiContext.getRequest();
      OrderDto orderInfo = new OrderDto();
      orderInfo.setRequestNo(request.getRequestNo());
      orderInfo.setGid(apiContext.getGid());
      if (apiContext.getRequest() instanceof ApiAsyncRequest) {
        orderInfo.setNotifyUrl(((ApiAsyncRequest) request).getNotifyUrl());
        orderInfo.setReturnUrl(((ApiAsyncRequest) request).getReturnUrl());
      }
      orderInfo.setPartnerId(request.getPartnerId());
      orderInfo.setService(request.getService());
      orderInfo.setVersion(request.getVersion());
      orderInfo.setSignType(apiContext.getSignType().name());
      orderInfo.setContext(request.getContext());
      orderInfoService.insert(orderInfo);
    } catch (Exception e) {
      logger.warn("订单写入失败，忽略错误，继续执行服务:" + e.getMessage());
    }
  }
}

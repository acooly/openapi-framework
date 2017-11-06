/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.core.notify.impl;

import com.acooly.core.utils.Ids;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.executer.ApiContextHolder;
import com.acooly.openapi.framework.core.marshall.ApiMarshallFactory;
import com.acooly.openapi.framework.core.notify.ApiNotifyHandler;
import com.acooly.openapi.framework.core.notify.ApiNotifySender;
import com.acooly.openapi.framework.core.notify.domain.NotifySendMessage;
import com.acooly.openapi.framework.core.service.base.ApiService;
import com.acooly.openapi.framework.core.service.factory.ApiServiceFactory;
import com.acooly.openapi.framework.domain.OrderInfo;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.service.OrderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 框架异步通知处理 默认实现
 *
 * @author zhangpu
 */
@Component
public class DefaultApiNotifyHandler implements ApiNotifyHandler {

  private static final Logger logger = LoggerFactory.getLogger(DefaultApiNotifyHandler.class);

  @Autowired protected OrderInfoService orderInfoService;
  @Autowired protected ApiServiceFactory apiServiceFactory;
  @Resource protected ApiMarshallFactory apiMarshallFactory;
  @Resource protected ApiNotifySender apiNotifySender;
  @Autowired protected AuthInfoRealm authInfoRealm;

  @SuppressWarnings("rawtypes")
  @Override
  public void notify(ApiNotifyOrder apiNotifyOrder) {
    MDC.put(ApiConstants.GID, apiNotifyOrder.getGid());
    try {
      // 获取订单信息
      apiNotifyOrder.check();
      OrderInfo orderInfo = getOrderInfo(apiNotifyOrder.getGid(), apiNotifyOrder.getPartnerId());
      if (orderInfo == null) {
        throw new ApiServiceException(ApiServiceResultCode.NOTIFY_ERROR, "GID对应的原始请求订单不存在");
      }
      if (StringUtils.isBlank(orderInfo.getNotifyUrl())) {
        throw new ApiServiceException(ApiServiceResultCode.NOTIFY_ERROR, "notifyUrl为空，不发送通知。");
      }

      ApiContextHolder.init();
      ApiContextHolder.getApiContext().setGid(apiNotifyOrder.getGid());

      // 查找对应服务并调用异步业务处理
      ApiService apiService =
          apiServiceFactory.getApiService(orderInfo.getService(), orderInfo.getVersion());
      ApiNotify apiNotify = apiService.handleNotify(orderInfo, apiNotifyOrder);
      ApiContextHolder.getApiContext().setApiService(apiService);
      ApiContextHolder.getApiContext().setResponse(apiNotify);
      // 组装报文
      Map<String, String> notifyMap =
          (Map<String, String>)
              apiMarshallFactory.getNotifyMarshall(orderInfo.getProtocol()).marshall(apiNotify);
      // 删除框架的签名，交给CS系统发送时签名
      String callerNotifyUrl = apiNotifyOrder.getParameter(ApiConstants.NOTIFY_URL);
      String notifyUrl =
          StringUtils.isNotBlank(callerNotifyUrl) ? callerNotifyUrl : orderInfo.getNotifyUrl();

      NotifySendMessage notifySendMessage = new NotifySendMessage();
      notifySendMessage.setGid(apiNotifyOrder.getGid());
      notifySendMessage.setPartnerId(apiNotify.getPartnerId());
      notifySendMessage.setService(apiNotify.getService());
      notifySendMessage.setVersion(apiNotify.getVersion());
      notifySendMessage.setUrl(notifyUrl);
      notifySendMessage.setMerchOrderNo(apiNotify.getMerchOrderNo());
      notifySendMessage.setRequestNo(apiNotify.getRequestNo());
      notifySendMessage.setParameters(notifyMap);
      apiNotifySender.send(notifySendMessage);
    } catch (ApiServiceException ase) {
      logger.warn("异步通知 失败:", ase);
      throw ase;
    } catch (Exception e) {
      logger.warn("异步通知 失败:", e);
      throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "处理失败,请查openApi日志堆栈");
    } finally {
      MDC.clear();
    }
  }

  @Override
  public void send(ApiNotifyOrder apiNotifyOrder) {
    MDC.put(ApiConstants.GID, apiNotifyOrder.getGid());
    try {
      // 获取订单信息
      apiNotifyOrder.check();
      OrderInfo orderInfo = new OrderInfo();
      orderInfo.setGid(apiNotifyOrder.getGid());
      orderInfo.setPartnerId(apiNotifyOrder.getPartnerId());
      orderInfo.setNotifyUrl(apiNotifyOrder.getParameter(ApiConstants.NOTIFY_URL));
      orderInfo.setProtocol(ApiProtocol.JSON);
      orderInfo.setRequestNo(Ids.oid());
      orderInfo.setOid(Ids.oid());
      orderInfo.setService(apiNotifyOrder.getParameter(ApiConstants.SERVICE));
      orderInfo.setVersion(apiNotifyOrder.getParameter(ApiConstants.VERSION));
      orderInfo.setSignType(SignType.MD5.code());
      if (StringUtils.isNotBlank(apiNotifyOrder.getParameter(ApiConstants.MERCH_ORDER_NO))) {
        orderInfo.setOrderNo(apiNotifyOrder.getParameter(ApiConstants.MERCH_ORDER_NO));
      }
      if (StringUtils.isBlank(orderInfo.getNotifyUrl())) {
        throw new ApiServiceException(ApiServiceResultCode.NOTIFY_ERROR, "notifyUrl为空，不发送通知。");
      }

      if (StringUtils.isBlank(orderInfo.getService())) {
        throw new ApiServiceException(ApiServiceResultCode.NOTIFY_ERROR, "service为空，不发送通知。");
      }

      if (StringUtils.isBlank(orderInfo.getVersion())) {
        throw new ApiServiceException(ApiServiceResultCode.NOTIFY_ERROR, "version为空，不发送通知。");
      }
      ApiContextHolder.init();
      ApiContextHolder.getApiContext().setGid(apiNotifyOrder.getGid());

      // 查找对应服务并调用异步业务处理
      ApiService apiService =
          apiServiceFactory.getApiService(orderInfo.getService(), orderInfo.getVersion());
      ApiNotify apiNotify = apiService.handleNotify(orderInfo, apiNotifyOrder);
      ApiContextHolder.getApiContext().setApiService(apiService);
      ApiContextHolder.getApiContext().setResponse(apiNotify);
      // 组装报文
      Map<String, String> notifyMap =
          (Map<String, String>)
              apiMarshallFactory.getNotifyMarshall(orderInfo.getProtocol()).marshall(apiNotify);
      // 交给CS系统发送
      NotifySendMessage notifySendMessage = new NotifySendMessage();
      notifySendMessage.setGid(apiNotifyOrder.getGid());
      notifySendMessage.setPartnerId(apiNotify.getPartnerId());
      notifySendMessage.setService(apiNotify.getService());
      notifySendMessage.setVersion(apiNotify.getVersion());
      notifySendMessage.setUrl(orderInfo.getNotifyUrl());
      notifySendMessage.setMerchOrderNo(apiNotify.getMerchOrderNo());
      notifySendMessage.setRequestNo(apiNotify.getRequestNo());
      notifySendMessage.setParameters(notifyMap);
      apiNotifySender.send(notifySendMessage);
    } catch (ApiServiceException ase) {
      logger.warn("异步通知 失败:", ase);
      throw ase;
    } catch (Exception e) {
      logger.warn("异步通知 失败:", e);
      throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "处理失败,请查openApi日志堆栈");
    } finally {
      MDC.clear();
    }
  }

  /**
   * 通过GID获取请求订单信息
   *
   * @param gid
   * @return
   */
  private OrderInfo getOrderInfo(String gid, String partnerId) {
    OrderInfo orderInfo = orderInfoService.findByGid(gid, partnerId);
    if (orderInfo == null) {
      throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "请求的原始订单不存在");
    }
    return orderInfo;
  }
}

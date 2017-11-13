/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.framework.core.notify.api;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.notify.ApiNotifyHandler;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.order.ApiQueryOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import com.acooly.openapi.framework.service.ApiPartnerService;
import com.acooly.openapi.framework.service.OrderInfoService;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * OpenApi 通知远程服务实现 （facade接口实现）
 *
 * @author zhangpu
 */
@Service
public class OpenApiRemoteServiceImpl implements OpenApiRemoteService {

  private static Logger logger = LoggerFactory.getLogger(OpenApiRemoteServiceImpl.class);

  @Resource private ApiNotifyHandler apiNotifyHandler;
  @Resource private OrderInfoService orderInfoService;
  @Resource private ApiAuthentication apiAuthentication;

  @Resource private ApiPartnerService apiPartnerService;

  public static void main(String[] args) {

    Map<String, String> signedMap = Maps.newLinkedHashMap();
    signedMap.put("requestNo", "11111111");
    signedMap.put("partnerId", "12341234234");

    Map<String, Object> parameters =
        Maps.transformEntries(
            signedMap,
            new EntryTransformer<String, String, Object>() {
              @Override
              public Object transformEntry(String key, String value) {
                return value;
              }
            });

    System.out.println(parameters);
  }

  /** 异步通知处理 */
  @Override
  public ResultBase asyncNotify(ApiNotifyOrder apiNotifyOrder) {
    ResultBase result = new ResultBase();
    try {
      apiNotifyOrder.check();
      apiNotifyHandler.notify(apiNotifyOrder);
    } catch (ApiServiceException e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getDetail());
    } catch (Exception e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getMessage());
    }
    return result;
  }

  @Override
  public ApiNotifyResult syncReturn(ApiNotifyOrder apiNotifyOrder) {
    logger.info("服务跳回 入参:{}", apiNotifyOrder);
    ApiNotifyResult result = new ApiNotifyResult();
    try {
      apiNotifyOrder.check();
      OrderDto orderInfo = getOrderInfo(apiNotifyOrder.getGid(), apiNotifyOrder.getPartnerId());
      Map<String, String> signedMap = getSignMap(orderInfo, apiNotifyOrder);
      apiAuthentication.signature(signedMap);
      Map<String, Object> parameters =
          Maps.transformEntries(
              signedMap,
              new EntryTransformer<String, String, Object>() {
                @Override
                public Object transformEntry(String key, String value) {
                  return value;
                }
              });
      result.setParameters(parameters);
      result.setSign(signedMap.get(ApiConstants.SIGN));
      result.setNotifyUrl(signedMap.get(ApiConstants.NOTIFY_URL));
      result.setReturnUrl(signedMap.get(ApiConstants.RETURN_URL));
      result.setStatus(ResultStatus.success);
    } catch (ApiServiceException e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getDetail());
    } catch (Exception e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getMessage());
    }
    logger.info("服务跳回 出参:{}", result);
    return result;
  }

  @Override
  public ResultBase sendMessage(ApiNotifyOrder apiNotifyOrder) {
    ResultBase result = new ResultBase();
    try {
      apiNotifyOrder.check();
      apiNotifyHandler.send(apiNotifyOrder);
    } catch (ApiServiceException e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getDetail());
    } catch (Exception e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getMessage());
    }
    return result;
  }

  @Override
  public ResultBase getPartnerSercretKey(ApiQueryOrder apiQueryOrder) {
    ResultBase result = new ResultBase();
    try {
      apiQueryOrder.check();
      String sercretKey = apiPartnerService.getPartnerSercretKey(apiQueryOrder.getPartnerId());
      result.setParameter("sercretKey", sercretKey);
    } catch (ApiServiceException e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getDetail());
    } catch (Exception e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getMessage());
    }
    return result;
  }

  protected Map<String, String> getSignMap(OrderDto orderInfo, ApiNotifyOrder apiNotifyOrder) {
    Map<String, String> signedMap = Maps.newLinkedHashMap();
    // 设置基础和默认参数
    signedMap.put(ApiConstants.REQUEST_NO, orderInfo.getRequestNo());
    signedMap.put(ApiConstants.MERCH_ORDER_NO, orderInfo.getOrderNo());
    signedMap.put(ApiConstants.PARTNER_ID, orderInfo.getPartnerId());
    signedMap.put(ApiConstants.SERVICE, orderInfo.getService());
    signedMap.put(ApiConstants.VERSION, orderInfo.getVersion());
    signedMap.put(ApiConstants.PROTOCOL, orderInfo.getProtocol().code());
    String signType = orderInfo.getSignType();
    signedMap.put(
        ApiConstants.SIGN_TYPE, Strings.isBlank(signType) ? SignTypeEnum.MD5.toString() : signType);
    signedMap.put(ApiConstants.CONTEXT, orderInfo.getContext());
    signedMap.put(ApiConstants.RESULT_CODE, ApiServiceResultCode.SUCCESS.code());
    signedMap.put(ApiConstants.RESULT_MESSAGE, ApiServiceResultCode.SUCCESS.message());
    signedMap.put(ApiConstants.SUCCESS, "true");
    // 使用下次请求的参数覆盖
    signedMap.putAll(apiNotifyOrder.getParameters());
    String notifyUrl = getNotifyUrl(apiNotifyOrder, orderInfo);
    String returnUrl = getReturnUrl(apiNotifyOrder, orderInfo);
    signedMap.put(ApiConstants.RETURN_URL, returnUrl);
    signedMap.put(ApiConstants.NOTIFY_URL, notifyUrl);
    return signedMap;
  }

  private String getNotifyUrl(ApiNotifyOrder apiNotifyOrder, OrderDto orderInfo) {
    String callerNotifyUrl = apiNotifyOrder.getParameter(ApiConstants.NOTIFY_URL);
    if (StringUtils.isNotBlank(callerNotifyUrl)) {
      return callerNotifyUrl;
    } else {
      return orderInfo.getNotifyUrl();
    }
  }

  private String getReturnUrl(ApiNotifyOrder apiNotifyOrder, OrderDto orderInfo) {
    String callerReturnUrl = apiNotifyOrder.getParameter(ApiConstants.RETURN_URL);
    if (StringUtils.isNotBlank(callerReturnUrl)) {
      return callerReturnUrl;
    } else {
      return orderInfo.getReturnUrl();
    }
  }

  /**
   * 通过GID获取请求订单信息
   *
   * @param gid
   * @return
   */
  private OrderDto getOrderInfo(String gid, String partnerId) {
    OrderDto orderInfo = orderInfoService.findByGid(gid, partnerId);
    if (orderInfo == null) {
      throw new ApiServiceException(ApiServiceResultCode.REQUEST_GID_NOT_EXSIT, "gid对应的订单不存在");
    }
    return orderInfo;
  }
}

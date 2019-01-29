/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.framework.notify.facade;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.order.ApiQueryOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import com.acooly.openapi.framework.notify.service.ApiNotifyHandler;
import com.acooly.openapi.framework.service.service.AuthInfoRealmService;
import com.acooly.openapi.framework.service.service.OrderInfoService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
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
@Service("openApiRemoteService")
public class OpenApiRemoteServiceImpl implements OpenApiRemoteService {

    private static Logger logger = LoggerFactory.getLogger(OpenApiRemoteServiceImpl.class);

    @Resource
    private ApiNotifyHandler apiNotifyHandler;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private ApiAuthentication apiAuthentication;

    @Resource
    private AuthInfoRealmService authInfoRealmService;

    /**
     * 异步通知处理
     */
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
            String accessKey = orderInfo.getAccessKey();
            String signType = Strings.isBlankDefault(orderInfo.getSignType(), SignType.MD5.code());
            Map<String, String> signedMap = getSignMap(orderInfo, apiNotifyOrder);
            String body = JSON.toJSONString(signedMap);
            String sign = apiAuthentication.signature(body, accessKey, signType);
            Map<String, Object> parameters = Maps.transformEntries(signedMap, (key, value) -> value);
            result.setSign(sign);
            result.setSignType(signType);
            result.setAccessKey(accessKey);
            result.setBody(body);
            result.setParameters(parameters);
            String notifyUrl = getNotifyUrl(apiNotifyOrder, orderInfo);
            String returnUrl = getReturnUrl(apiNotifyOrder, orderInfo);
            result.setNotifyUrl(notifyUrl);
            result.setReturnUrl(returnUrl);
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
            String sercretKey = authInfoRealmService.getSercretKey(apiQueryOrder.getAccesskey());
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
        signedMap.put(ApiConstants.SERVICE, orderInfo.getService());
        signedMap.put(ApiConstants.VERSION, orderInfo.getVersion());
        signedMap.put(ApiConstants.PROTOCOL, orderInfo.getProtocol().code());
        signedMap.put(ApiConstants.CONTEXT, orderInfo.getContext());
        signedMap.put(ApiConstants.RESULT_CODE, ApiServiceResultCode.SUCCESS.code());
        signedMap.put(ApiConstants.RESULT_MESSAGE, ApiServiceResultCode.SUCCESS.message());
        signedMap.put(ApiConstants.SUCCESS, "true");
        // 使用下次请求的参数覆盖
        signedMap.putAll(apiNotifyOrder.getParameters());
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

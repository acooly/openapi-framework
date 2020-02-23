/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-10-10 17:28
 */
package com.acooly.openapi.framework.facade;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Asserts;
import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.order.ApiVerifyOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * OpenApi工具类
 * <p>
 * 专用于：下层服务处理跳转，异步通知等OpenApi相关的操作。
 *
 * @author zhangpu
 * @date 2019-10-10 17:28
 */
@Slf4j
public class OpenApis {


    /**
     * 跳转解析
     * <p>
     * 用于下层服务接受跳转请求时解析请求。主要包括：
     * 1、解析请求，也可调用：OpenApiTools.redirectParseRequest
     * 2、验证签名，调用openApiRemoteService远程服务，如果不通过抛出异常。
     * 3、解析报文，解析为可用的跳转报文对象。也可以调用：OpenApiTools.redirectParseMessage
     *
     * @param request              Http请求对象
     * @param clazz                OpenApi的ApiRedirect报文对象
     * @param openApiRemoteService OpenApi远程服务
     * @return
     */
    public static <T> ApiRedirectContext<T> redirectParse(HttpServletRequest request, Class<T> clazz, OpenApiRemoteService openApiRemoteService) {
        ApiMessageContext context = redirectParseRequest(request);
        String gid = context.getGid();
        MDC.put(ApiConstants.GID, gid);
        log.info("跳转请求 参数：{}", context.getParameters());
        ResultBase resultBase = openApiRemoteService.verify(new ApiVerifyOrder(context));
        log.info("跳转请求 验签：{}", resultBase.success());
        if (!resultBase.success()) {
            throw new BusinessException(resultBase.getStatus());
        }
        ApiRedirectContext<T> apiRedirectContext = new ApiRedirectContext<>(context);
        T apiRedirect = redirectParseMessage(context.getBody(), clazz);
        apiRedirectContext.setApiRedirect(apiRedirect);
        return apiRedirectContext;
    }

    /**
     * 跳转接口跳回客户界面（returnUrl）
     *
     * @param response
     * @param apiNotifyOrder
     * @param openApiRemoteService
     */
    public static void redirectSendBack(HttpServletResponse response, ApiNotifyOrder apiNotifyOrder, OpenApiRemoteService openApiRemoteService) {
        // 调用OpenApi解析返回报文
        Asserts.notEmpty(apiNotifyOrder.getGid(), "GID不能为空");
        Asserts.notEmpty(apiNotifyOrder.getPartnerId(), "parentId不能为空");
        ApiNotifyResult apiNotifyResult = openApiRemoteService.syncReturn(apiNotifyOrder);
        String returnUrl = apiNotifyResult.getCompleteReturnUrl();
        log.info("跳转请求 跳回：ApiNotifyResult: {}, Url: {}", apiNotifyResult, returnUrl);
        Servlets.redirect(response, returnUrl);
    }


    public static void notify(ApiNotifyOrder apiNotifyOrder, OpenApiRemoteService openApiRemoteService) {
        Asserts.notEmpty(apiNotifyOrder.getGid(), "GID不能为空");
        Asserts.notEmpty(apiNotifyOrder.getPartnerId(), "parentId不能为空");
        ResultBase resultBase = openApiRemoteService.asyncNotify(apiNotifyOrder);
        log.info("通知请求 {}", resultBase);
    }


    /**
     * 跳转请求解析
     * 主要包括：请求头，请求参数和请求体，并对参数的获取做了协议兼容和适配（getValue）
     * 可用于下层服务接受跳转请求时，解析请求参数的第一步。
     *
     * @param request
     * @return
     */
    public static ApiMessageContext redirectParseRequest(HttpServletRequest request) {
        return ApiUtils.getApiRequestContext(request);
    }

    /**
     * 跳转报文解析
     * <p>
     * 使用OpenApi内部能力，实现：报文组织为报文对象。
     * 主要用于：下层接受跳转服务的第三步：解析报文获取结构化数据
     *
     * @param message 报文
     * @param clazz   报文对象
     * @param <T>
     * @return
     */
    public static <T> T redirectParseMessage(String message, Class<T> clazz) {
        return JsonMarshallor.INSTANCE.parse(message, clazz);
    }


    /**
     * 跳转解析专用Context对象
     *
     * @param <T> OpenApi的ApiRedirect报文对象
     */
    @Getter
    @Setter
    public static class ApiRedirectContext<T> extends ApiMessageContext {

        public ApiRedirectContext(ApiMessageContext context) {
            super(context);
        }

        private T apiRedirect;
    }

}

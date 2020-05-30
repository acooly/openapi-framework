/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.core.utils.validate.Validators;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterEnum;
import com.acooly.openapi.framework.core.marshall.ApiMarshallFactory;
import com.acooly.openapi.framework.core.marshall.ApiRequestMarshall;
import com.acooly.openapi.framework.service.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 组织请求报文为报文对象
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class RequestMarshallOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected ApiMarshallFactory apiMarshallFactory;

    @Resource
    private OrderInfoService orderInfoService;

    @Override
    public void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        ApiRequestMarshall apiRequestMarshall = apiMarshallFactory.getRequestMarshall(context.getApiProtocol());
        ApiRequest apiRequest = (ApiRequest) apiRequestMarshall.marshall(context);
        context.setRequest(apiRequest);
        doValidateAfterMarshall(context);
    }

    protected void doValidateAfterMarshall(ApiContext context) {
        doValidateServiceParam(context);
        doValidateParameter(context.getRequest());
        doValidateIdempotence(context.getRequest());
    }

    /**
     * 公共Api参数合法性检查
     */
    protected void doValidateParameter(ApiRequest apiRequest) {
        try {
            Validators.assertJSR303(apiRequest);
            apiRequest.check();
        } catch (IllegalArgumentException iae) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, iae.getMessage());
        } catch (ApiServiceException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, "参数合法性检查未通过:" + e.getMessage());
        }
    }

    /**
     * 幂等性校验
     *
     * @param apiRequest
     */
    protected void doValidateIdempotence(ApiRequest apiRequest) {
        orderInfoService.checkUnique(apiRequest.getPartnerId(), apiRequest.getRequestNo());
    }

    /**
     * 服务参数校验
     *
     * @param apiContext
     */
    protected void doValidateServiceParam(ApiContext apiContext) {
        apiContext.getOpenApiService().responseType().accept(apiContext);
    }

    @Override
    protected OpenApiFilterEnum openApiFilter() {
        return OpenApiFilterEnum.RequestMarshall;
    }
}

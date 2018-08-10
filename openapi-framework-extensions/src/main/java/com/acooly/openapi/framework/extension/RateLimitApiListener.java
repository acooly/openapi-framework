package com.acooly.openapi.framework.extension;

import com.acooly.module.cache.limit.RateChecker;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiListener;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.event.dto.BeforeServiceExecuteEvent;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.listener.AbstractListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qiuboboy@qq.com
 * @date 2018-08-10 15:21
 */
@OpenApiListener(global = true, asyn = false)
@Slf4j
public class RateLimitApiListener extends AbstractListener<BeforeServiceExecuteEvent> {
    public static final String PREFIX = "openapi-rate-";
    @Autowired
    private OpenAPIProperties openAPIProperties;
    @Autowired
    private RateChecker rateChecker;


    @Override
    public void onOpenApiEvent(BeforeServiceExecuteEvent event) {
        if (openAPIProperties.getRates().isEmpty()) {
            return;
        }
        ApiContext apiContext = ApiContextHolder.getApiContext();
        String partnerId = apiContext.getPartnerId();
        String method = apiContext.getServiceName();
        openAPIProperties.getRates().forEach(rate -> {
            if (rate.acceptPartnerId(partnerId) && rate.acceptMethod(method)) {
                String partnerIdStr = partnerId;
                String methodStr = method;
                if (rate.allPartnerId()) {
                    partnerIdStr = ApiConstants.WILDCARD_TOKEN;
                }
                if (rate.allMethod()) {
                    methodStr = ApiConstants.WILDCARD_TOKEN;
                }
                if (!rateChecker.check(PREFIX + partnerIdStr + "." + methodStr, rate.getInterval(), rate.getMaxRequests())) {
                    throw new ApiServiceException(ApiServiceResultCode.TOO_MANY_REQUEST);
                }
            }
        });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

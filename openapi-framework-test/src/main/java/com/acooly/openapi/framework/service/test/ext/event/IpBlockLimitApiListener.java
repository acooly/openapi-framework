/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-10-08 15:21
 */
package com.acooly.openapi.framework.service.test.ext.event;

import com.acooly.component.data.ip.service.IpSearchService;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.system.IPUtil;
import com.acooly.openapi.framework.common.annotation.OpenApiListener;
import com.acooly.openapi.framework.common.event.dto.BeforeServiceExecuteEvent;
import com.acooly.openapi.framework.core.listener.AbstractListener;
import com.acooly.openapi.framework.core.listener.ApiListener;
import com.acooly.openapi.framework.core.listener.Ordered;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * IP段限制检测监听器
 * <p>
 * 限制中国大陆用户不可访问
 *
 * @author zhangpu
 * @date 2021-10-08 15:21
 */
@Slf4j
@OpenApiListener(global = true, asyn = false)
public class IpBlockLimitApiListener extends AbstractListener<BeforeServiceExecuteEvent> {

    @Autowired
    private IpSearchService ipSearchService;

    @Override
    public void onOpenApiEvent(BeforeServiceExecuteEvent event) {
        String requestIp = event.getApiContext().getRequestIp();
        if (Strings.isNotBlank(requestIp) && IPUtil.isPublicIpv4(requestIp)) {
            if (ipSearchService.isChinaIp(requestIp)) {
                throw new BusinessException(CommonErrorCodes.UNAUTHORIZED_ERROR, "限制中国大陆IP访问");
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

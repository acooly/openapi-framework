/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-02-12 23:36
 */
package com.acooly.openapi.framework.core.auth.event;

import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.module.event.EventHandler;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.auth.realm.impl.CacheableAuthInfoRealm;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.event.ApiUpdateEvent;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-02-12 23:36
 */
@Slf4j
@EventHandler
public class ApiUpdateEventHandler {

    @Autowired
    private AuthInfoRealm authInfoRealm;

    @Autowired
    private ApiAuthService apiAuthService;

    @Handler
    public void handleApiAuthUpdateEvent(ApiUpdateEvent apiAuthUpdateEvent) {
        if (!(authInfoRealm instanceof CacheableAuthInfoRealm)) {
            return;
        }
        String accessKey = null;
        if (apiAuthUpdateEvent.getApiPartner() != null) {
            List<ApiAuth> apiAuths = apiAuthService.findByPartnerId(apiAuthUpdateEvent.getApiPartner().getPartnerId());
            if (Collections3.isEmpty(apiAuths)) {
                return;
            }

            for (ApiAuth apiAuth : apiAuths) {
                clearCache(apiAuth.getAccessKey());
            }

        } else {
            if (apiAuthUpdateEvent.getApiAuth() != null) {
                accessKey = apiAuthUpdateEvent.getApiAuth().getAccessKey();
            } else if (apiAuthUpdateEvent.getApiAuthAcl() != null) {
                accessKey = apiAuthUpdateEvent.getApiAuthAcl().getAccessKey();
            }
            if (Strings.isNotBlank(accessKey)) {
                clearCache(accessKey);
            }
        }
    }

    protected void clearCache(String accessKey) {
        ((CacheableAuthInfoRealm) authInfoRealm).removeCache(accessKey);
    }

}

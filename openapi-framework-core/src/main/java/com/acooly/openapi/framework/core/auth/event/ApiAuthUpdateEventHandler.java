/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-02-12 23:36
 */
package com.acooly.openapi.framework.core.auth.event;

import com.acooly.core.utils.Strings;
import com.acooly.module.event.EventHandler;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.auth.realm.impl.CacheableAuthInfoRealm;
import com.acooly.openapi.framework.service.event.ApiAuthUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangpu
 * @date 2019-02-12 23:36
 */
@Slf4j
@EventHandler
public class ApiAuthUpdateEventHandler {

    @Autowired
    private AuthInfoRealm authInfoRealm;

    @Handler
    public void handleApiAuthUpdateEvent(ApiAuthUpdateEvent apiAuthUpdateEvent) {
        if (authInfoRealm instanceof CacheableAuthInfoRealm) {
            String accessKey = null;
            if (apiAuthUpdateEvent.getApiAuth() != null) {
                accessKey = apiAuthUpdateEvent.getApiAuth().getAccessKey();
            } else if (apiAuthUpdateEvent.getApiAuthAcl() != null) {
                accessKey = apiAuthUpdateEvent.getApiAuthAcl().getAccessKey();
            }
            if (Strings.isBlank(accessKey)) {
                return;
            }
            ((CacheableAuthInfoRealm) authInfoRealm).removeCache(accessKey);
        }
    }

}

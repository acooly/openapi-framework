package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.service.service.AuthInfoRealmService;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author qiubo@yiji.com
 */
public class DefaultAuthInfoRealm extends AnonymousSupportAuthInfoRealm {

    @Resource
    private AuthInfoRealmService authInfoRealmService;

    @Override
    public String doGetSecretKey(String accessKey) {
        return authInfoRealmService.getSercretKey(accessKey);
    }

    @Override
    public Set<String> doGetAuthorizedServices(String accessKey) {
        return authInfoRealmService.getAuthorizationInfo(accessKey);
    }

    @Override
    public Set<String> getAuthIpWhitelist(String accessKey) {
        return authInfoRealmService.getIpWhitelist(accessKey);
    }

    @Override
    public String loadTenantId(String accessKey) {
        return authInfoRealmService.getTenantId(accessKey);
    }
}

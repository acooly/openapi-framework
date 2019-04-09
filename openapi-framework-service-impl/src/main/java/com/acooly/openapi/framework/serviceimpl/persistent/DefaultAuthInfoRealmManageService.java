package com.acooly.openapi.framework.serviceimpl.persistent;

import com.acooly.openapi.framework.service.AuthInfoRealmManageService;
import com.acooly.openapi.framework.serviceimpl.manage.entity.ApiAuth;
import com.acooly.openapi.framework.serviceimpl.manage.service.ApiAuthService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:35
 */
public class DefaultAuthInfoRealmManageService implements AuthInfoRealmManageService {

    @Autowired
    ApiAuthService apiAuthService;

    @Override
    public void createAuthenticationInfo(String accessKey, String secretKey) {
        ApiAuth apiAuth = new ApiAuth();
        apiAuth.setSecretKey(secretKey);
        apiAuth.setAccessKey(accessKey);
        apiAuthService.save(apiAuth);
    }

    @Override
    public void createAuthorizationInfo(String accessKey, String authorizationInfo) {
        ApiAuth apiAuth = apiAuthService.findByAccesskey(accessKey);
        apiAuth.setPermissions(authorizationInfo);
        apiAuthService.update(apiAuth);
    }

    @Override
    public void updateAuthenticationInfo(String accessKey, String sercretKey) {
        ApiAuth apiAuth = apiAuthService.findByAccesskey(accessKey);
        apiAuth.setSecretKey(sercretKey);
        apiAuthService.update(apiAuth);
    }

    @Override
    public String getSercretKey(String accessKey) {
        ApiAuth apiAuth = apiAuthService.findByAccesskey(accessKey);
        if(apiAuth==null){
            return null;
        }
        return apiAuth.getSecretKey();
    }

    @Override
    public Set<String> getAuthorizationInfo(String accessKey) {
        return Sets.newHashSet(apiAuthService.findByAccesskey(accessKey).getPermissions());
    }
}

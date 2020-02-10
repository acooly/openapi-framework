package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import com.acooly.openapi.framework.service.service.AuthInfoRealmManageService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:35
 */
@Service
public class DefaultAuthInfoRealmManageService implements AuthInfoRealmManageService {

    @Autowired
    ApiAuthService apiAuthService;

    @Override
    public void createAuthenticationInfo(String parentAccessKey, String accessKey, String secretKey) {
        ApiAuth parentAuth = apiAuthService.findByAccesskey(parentAccessKey);
        ApiAuth apiAuth = new ApiAuth();
        BeanCopier.copy(parentAuth, apiAuth);
        apiAuth.setId(null);
        apiAuth.setParentId(parentAuth.getId());
        apiAuth.setAuthNo(Ids.oid());
        apiAuth.setAccessKey(accessKey);
        apiAuth.setSecretKey(secretKey);
        apiAuth.setPermissions(null);
        apiAuthService.save(apiAuth);
    }

    @Override
    public void createAuthenticationInfo(String accessKey, String secretKey) {
        ApiAuth apiAuth = new ApiAuth();
        apiAuth.setSecretKey(secretKey);
        apiAuth.setAccessKey(accessKey);
        apiAuthService.save(apiAuth);
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
        if (apiAuth == null) {
            return null;
        }
        return apiAuth.getSecretKey();
    }

    @Override
    public Set<String> getAuthorizationInfo(String accessKey) {
        return Sets.newHashSet(apiAuthService.findByAccesskey(accessKey).getPermissions());
    }
}

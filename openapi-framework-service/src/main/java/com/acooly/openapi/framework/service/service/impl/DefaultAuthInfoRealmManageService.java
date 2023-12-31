package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
import com.acooly.openapi.framework.service.domain.ApiPartner;
import com.acooly.openapi.framework.service.service.ApiAuthAclService;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import com.acooly.openapi.framework.service.service.ApiPartnerService;
import com.acooly.openapi.framework.service.service.AuthInfoRealmManageService;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:35
 */
@Slf4j
@Service
public class DefaultAuthInfoRealmManageService implements AuthInfoRealmManageService {

    @Autowired
    ApiPartnerService apiPartnerService;
    @Autowired
    ApiAuthService apiAuthService;
    @Autowired
    ApiAuthAclService apiAuthAclService;

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
        ApiAuth apiAuth = loadAndCheckApiAuth(accessKey);
        loadAndCheckPartner(apiAuth.getPartnerId());
        return apiAuth.getSecretKey();
    }

    @Override
    public Set<String> getAuthorizationInfo(String accessKey) {
        Set<String> permissions = Sets.newHashSet();

        // 解析添加特殊权限
        String perms = apiAuthService.findByAccesskey(accessKey).getPermissions();
        if (Strings.isNotBlank(perms)) {
            for (String perm : Strings.split(perms, ",")) {
                permissions.add(perm);
            }
        }

        // 获取ACL
        List<ApiAuthAcl> acls = apiAuthAclService.queryAcls(accessKey);
        if (Collections3.isNotEmpty(acls)) {
            for (ApiAuthAcl acl : acls) {
                permissions.add(accessKey + ":" + acl.getName());
            }
        }

        return permissions;
    }

    @Override
    public Set<String> getIpWhitelist(String accessKey) {
        ApiAuth apiAuth = apiAuthService.findByAccesskey(accessKey);
        if (apiAuth != null && apiAuth.getWhitelistCheck() == WhetherStatus.yes && Strings.isNotBlank(apiAuth.getWhitelist())) {
            return Sets.newHashSet(Splitter.on(",").omitEmptyStrings().trimResults().split(apiAuth.getWhitelist()));
        }
        return null;
    }

    @Override
    public String getTenantId(String accessKey) {
        ApiAuth apiAuth = loadAndCheckApiAuth(accessKey);
        ApiPartner apiPartner = loadAndCheckPartner(apiAuth.getPartnerId());
        return apiPartner.getTenantId();
    }

    private ApiAuth loadAndCheckApiAuth(String accessKey) {
        return apiAuthService.findAndCheckByAccesskey(accessKey);
    }

    private ApiPartner loadAndCheckPartner(String partnerId) {
        ApiPartner apiPartner = apiPartnerService.getPartner(partnerId);
        if (apiPartner == null) {
            log.warn("加载Partner 失败 partnerId:{}, 合作接入方(ApiPartner)不存在。", partnerId);
            throw new ApiServiceException(ApiServiceResultCode.ACCESS_KEY_NOT_EXIST, "合作接入方(ApiPartner)不存在");
        }
        return apiPartner;
    }
}

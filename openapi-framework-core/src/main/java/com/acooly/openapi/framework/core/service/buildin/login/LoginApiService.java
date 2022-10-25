package com.acooly.openapi.framework.core.service.buildin.login;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
import com.acooly.openapi.framework.common.utils.AccessKeys;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.auth.realm.impl.CacheableAuthInfoRealm;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.domain.LoginDto;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import com.acooly.openapi.framework.service.service.AppApiLoginService;
import com.acooly.openapi.framework.service.service.AuthInfoRealmManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * 用户登录
 *
 * @author zhangpu
 * @note <p>用户登录服务需要目标项目根据需求实现ApiLoginService接口
 */
@Slf4j
@ApiDocType(code = ApiConstants.BUILDIN_APIDOC_CODE, name = ApiConstants.BUILDIN_APIDOC_NAME)
@ApiDocNote("用户登录服务需要目标项目根据需求实现ApiLoginService接口,否则框架自动使用DefaultAppApiLoginService，全部通过认证。")
@OpenApiService(name = ApiConstants.LOGIN_SERVICE_NAME, desc = "用户登录", responseType = ResponseType.SYN, owner = "acooly")
public class LoginApiService extends BaseApiService<LoginRequest, LoginResponse> implements InitializingBean {
    @Autowired
    private OpenAPIProperties openAPIProperties;

    @Autowired(required = false)
    private AppApiLoginService appApiLoginService;

    @Autowired(required = false)
    private AuthInfoRealmManageService authInfoRealmManageService;

    @Autowired
    private ApiAuthService apiAuthService;

    @Resource
    private CacheableAuthInfoRealm cacheableAuthInfoRealm;

    @Override
    protected void doService(LoginRequest request, LoginResponse response) {
        try {
            String requestAccessKey = ApiContextHolder.getApiContext().getAccessKey();
            // OpenApi的login不提供App客户端请求参数的存储和处理，交给集成工程的login接口实现
            LoginDto dto = appApiLoginService.login(request, ApiContextHolder.getApiContext());
            response.setCustomerId(dto.getCustomerId());
            String accessKey = requestAccessKey + "#" + request.getUsername();
            String secretKey = null;
            ApiAuth apiAuth = apiAuthService.findByAccesskey(accessKey);
            if(apiAuth != null){
                secretKey = apiAuth.getSecretKey();
            }
            if (Strings.isBlank(secretKey)) {
                secretKey = AccessKeys.newSecretKey();
                authInfoRealmManageService.createAuthenticationInfo(requestAccessKey, accessKey, secretKey);
                // 这里不用设置动态accessKey的权限，权限与其父accessKey一致。
            } else {
                if (openAPIProperties.getLogin().isSecretKeyDynamic()) {
                    secretKey = AccessKeys.newSecretKey();
                    authInfoRealmManageService.updateAuthenticationInfo(accessKey, secretKey);
                    cacheableAuthInfoRealm.removeCache(accessKey);
                }
            }
            response.getExt().putAll(dto.getExt());
            response.setCustomerId(dto.getCustomerId());
            response.setAccessKey(accessKey);
            response.setSecretKey(secretKey);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.warn("ApiService Login failure.", e);
            throw new ApiServiceException(ApiServiceResultCode.UNAUTH_PASSWORD_ERROR);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (!openAPIProperties.getLogin().isEnable()) {
            return;
        }
        if (appApiLoginService.getClass() == DefaultAppApiLoginService.class) {
            log.warn("*****************************************************************************************************************************");
            log.warn("应用系统没有提供AppApiLoginService bean实现，默认启用匿名实现，即登录时不验证用户名密码，请业务开发者考虑app是否需要登录时验证密码！");
            log.warn("*****************************************************************************************************************************");
        } else {
            log.info("app登录验证实现类:{}", appApiLoginService.getClass());
        }
    }
}

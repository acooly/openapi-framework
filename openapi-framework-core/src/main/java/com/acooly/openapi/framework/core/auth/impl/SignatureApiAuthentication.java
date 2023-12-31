package com.acooly.openapi.framework.core.auth.impl;

import com.acooly.core.common.boot.Env;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.acooly.openapi.framework.core.security.sign.Signer;
import com.acooly.openapi.framework.core.security.sign.SignerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static com.acooly.openapi.framework.common.ApiConstants.TEST_ACCESS_KEY;

/**
 * 签名认证实现
 *
 * @author zhangpu
 */
@Component
public class SignatureApiAuthentication implements ApiAuthentication {
    private static final Logger logger = LoggerFactory.getLogger(SignatureApiAuthentication.class);
    @Resource
    protected SignerFactory<ApiContext> signerFactory;
    @Resource
    protected AuthInfoRealm authInfoRealm;
    @Resource
    protected OpenAPIProperties openAPIProperties;

    /**
     * 认证
     */
    @Override
    public void authenticate(ApiContext apiContext) {
        try {
            String requestSign = apiContext.getSign();
            String accessKey = apiContext.getAccessKey();
            if (TEST_ACCESS_KEY.equals(accessKey)) {
                if (Env.isOnline()) {
                    throw new ApiServiceException(ApiServiceResultCode.TEST_NOT_SUPPORT_IN_PRODUCTION);
                }
            }
            verify(apiContext.getRequestBody(), accessKey, apiContext.getSignType(), requestSign);
            apiContext.setAuthenticated(true);

            if (!Strings.equals(openAPIProperties.getAnonymous().getAccessKey(), accessKey)) {
                // 读取当前对应的租户信息
                String tenantId = authInfoRealm.getTenantId(accessKey);
                apiContext.setTenantId(tenantId);
                MDC.put(ApiConstants.TENANT_ID, tenantId);
            }
        } catch (ApiServiceException asae) {
            throw asae;
        } catch (Exception e) {
            logger.warn("认证异常", e);
            throw new ApiServiceAuthenticationException();
        }
    }


    @Override
    public void verify(String body, String accessKey, SignTypeEnum signType, String verifySign) {
        Signer<ApiContext> signer = signerFactory.getSigner(signType);
        signer.verify(verifySign, (String) authInfoRealm.getAuthenticationInfo(accessKey), body);
    }

    /**
     * 签名
     */
    @Override
    public String signature(Map<String, String> response, String accessKey, String signType) {
        return signature(ApiUtils.getWaitForSignString(response), accessKey, signType);
    }

    @Override
    public String signature(String body, String accessKey, String signType) {
        try {
            String secretKey = (String) authInfoRealm.getAuthenticationInfo(accessKey);
            String sign = signerFactory.getSigner(SignTypeEnum.valueOf(signType)).sign(body, secretKey);
            return sign;
        } catch (ApiServiceException asae) {
            throw asae;
        } catch (Exception e) {
            logger.warn("签名异常", e);
            throw new ApiServiceAuthenticationException("签名错误");
        }
    }

}

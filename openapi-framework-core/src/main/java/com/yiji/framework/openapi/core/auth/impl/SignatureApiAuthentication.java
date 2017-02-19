package com.yiji.framework.openapi.core.auth.impl;

import com.yiji.framework.openapi.common.ApiConstants;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.common.utils.Strings;
import com.yiji.framework.openapi.core.auth.ApiAuthentication;
import com.yiji.framework.openapi.core.auth.realm.AuthInfoRealm;
import com.yiji.framework.openapi.core.security.sign.SignTypeEnum;
import com.yiji.framework.openapi.core.security.sign.Signer;
import com.yiji.framework.openapi.core.security.sign.SignerFactory;
import com.yiji.framework.openapi.core.exception.impl.ApiServiceAuthenticationException;
import com.yiji.framework.openapi.core.util.ApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 签名认证实现
 *
 * @author zhangpu
 */
@Component
public class SignatureApiAuthentication implements ApiAuthentication {

    private static final Logger logger = LoggerFactory.getLogger(SignatureApiAuthentication.class);

    @Resource
    protected SignerFactory<Map<String, String>> signerFactory;
    @Resource
    protected AuthInfoRealm authInfoRealm;

    /**
     * 认证
     *
     * @param requestData
     */
    @Override
    public void authenticate(Map<String, String> requestData) {
        try {
            String signType = Strings.isBlankDefault(ApiUtils.getParameter(requestData, ApiConstants.SIGN_TYPE), SignTypeEnum.MD5.name());
            String requestSign = ApiUtils.getParameter(requestData, ApiConstants.SIGN);
            String partnerId = ApiUtils.getParameter(requestData, ApiConstants.PARTNER_ID);
            Signer<Map<String, String>> signer = signerFactory.getSigner(signType);
            signer.verify(requestSign, (String) authInfoRealm.getAuthenticationInfo(partnerId),
                    requestData);
        } catch (ApiServiceException asae) {
            throw asae;
        } catch (Exception e) {
            logger.warn("签名认证异常", e);
            throw new ApiServiceAuthenticationException();
        }
    }

    /**
     * 签名
     *
     * @param response
     */
    @Override
    public void signature(Map<String, String> response) {
        String signType = response.get(ApiConstants.SIGN_TYPE);
        String partnerId = response.get(ApiConstants.PARTNER_ID);
        signature(response, partnerId, signType);
    }

    @Override
    public void signature(Map<String, String> responseData, String partnerId, String signType) {
        try {
            String sign = signerFactory.getSigner(signType).sign(responseData,
                    authInfoRealm.getAuthenticationInfo(partnerId));
            responseData.put(ApiConstants.SIGN, sign);
        } catch (ApiServiceException asae) {
            throw asae;
        } catch (Exception e) {
            logger.warn("签名异常", e);
            throw new ApiServiceAuthenticationException("签名错误");
        }
    }

    public void setSignerFactory(SignerFactory<Map<String, String>> signerFactory) {
        this.signerFactory = signerFactory;
    }

    public void setAuthInfoRealm(AuthInfoRealm authInfoRealm) {
        this.authInfoRealm = authInfoRealm;
    }

}

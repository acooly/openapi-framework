package com.acooly.openapi.framework.core.service.support.auth;

import com.acooly.openapi.framework.common.annotation.OpenApiNote;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author qiuboboy@qq.com
 * @date 2018-08-20 15:49
 */
@OpenApiNote("当app通过内置web控件发起请求时，需要验证请求的合法性,此服务用于验证外部请求签名合法性。" +
        "<br/>app端签名过程：" +
        "<li>1. 获取签名字符串 body+expireDate(格式为：yyyy-MM-dd HH:mm:ss)+secretKey</li>" +
        "<li>2. 求md5值</li>")
@OpenApiService(
        name = "auth",
        desc = "认证服务",
        responseType = ResponseType.SYN,
        owner = "公共"
)
public class AuthenticationApiService extends BaseApiService<AuthRequest, ApiResponse> {
    @Autowired
    private ApiAuthentication apiAuthentication;

    @Override
    protected void doService(AuthRequest request, ApiResponse response) {
        Date date = request.getExpiredBody().getExpireDate();
        Date now = new Date();
        if (Math.abs(now.getTime() - date.getTime()) > AuthRequest.EXPIRE) {
            throw new ApiServiceException("AUTH_EXPIRE", "认证已经过期");
        }
        String signature = apiAuthentication.signature(request.getExpiredBody().getSignBody(), request.getAccessKey(), ApiContextHolder.getApiContext().getSignType().name());
        if (!request.getSign().equals(signature)) {
            throw new ApiServiceException("AUTH_FAILURE", "认证服务认证失败");
        }
    }
}

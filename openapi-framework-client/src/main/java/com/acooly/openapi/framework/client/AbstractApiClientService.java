package com.acooly.openapi.framework.client;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象客户端服务
 *
 * @author
 */
@Slf4j
public abstract class AbstractApiClientService {

    @Autowired
    protected OpenApiClient openApiClient;

    protected <T extends ApiResponse> T request(ApiRequest request, Class<T> clazz) {

        if (Strings.isBlank(request.getRequestNo())) {
            request.setRequestNo(Ids.getDid());
        }
        request.setPartnerId(openApiClient.getAccessKey());
        parseServiceName(request);

        T response = openApiClient.send(request, clazz);
        if (!response.isSuccess()) {
            log.warn("OpenApiClient [失败] partner/service/requestNo: {}/{}/{} , 错误码:{}/{}/{}", request.getPartnerId(),
                    request.getService(), request.getRequestNo(), response.getCode(), response.getMessage(),
                    response.getDetail());
            throw new BusinessException(response.getMessage(), response.getCode());
        }
        return response;
    }

    private void parseServiceName(ApiRequest request) {
        if (Strings.isNotBlank(request.getService())) {
            return;
        }
        try {
            String requestClassName = request.getClass().getSimpleName();
            if (Strings.containsIgnoreCase(requestClassName, "ApiRequest")) {
                request.setService(Strings.uncapitalize(Strings.substringBefore(requestClassName, "ApiRequest")));
            } else if (Strings.containsIgnoreCase(requestClassName, "Request")) {
                request.setService(Strings.uncapitalize(Strings.substringBefore(requestClassName, "Request")));
            }

        } catch (Exception e) {
            log.warn("默认规则解析服务名失败。 request:{}", request.getClass().getName());
        }
    }

}

package com.acooly.openapi.mock;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.Servlets;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.service.service.ApiMetaServiceService;
import com.acooly.openapi.mock.dao.ApiMockDao;
import com.acooly.openapi.mock.entity.ApiMock;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2018-08-14 16:58
 */
@Controller
@RequestMapping(value = "gateway.mock")
public class MockController {
    @Autowired
    private ApiMetaServiceService apiMetaServiceService;
    @Autowired
    private ApiMockDao apiMockDao;
    @Resource
    protected ApiAuthentication apiAuthentication;

    @RequestMapping("")
    public void testFtl(HttpServletRequest request, HttpServletResponse response) {
        ApiContext apiContext = new ApiContext();
        ApiContextHolder.setApiContext(apiContext);
        apiContext.setHttpRequest(request);
        apiContext.setHttpResponse(response);
        apiContext.init();
        List<ApiMock> apiMocks = apiMockDao.findByServiceNameAndVersion(apiContext.getServiceName(), apiContext.getServiceVersion());
        String responseBody;
        if (apiMocks.isEmpty()) {
            responseBody = notFound();
        } else {
            Map requestMap = (Map) JSON.parse(apiContext.getRequestBody());
            int maxMatchCount = 0;
            int maxMathIdx = 0;
            for (int i = 0; i < apiMocks.size(); i++) {
                ApiMock apiMock = apiMocks.get(i);
                Map expect = (Map) JSON.parse(apiMock.getExpect());
                int matchCount = matchCount(requestMap, expect);
                if (matchCount > maxMatchCount) {
                    maxMatchCount = matchCount;
                    maxMathIdx = i;
                }
            }
            if (maxMatchCount == 0) {
                responseBody = notFound();
            } else {
                ApiMock apiMock = apiMocks.get(maxMathIdx);
                responseBody = apiMock.getResponse();
            }
        }
        apiContext.setResponseBody(responseBody);
        if (!Strings.isNullOrEmpty(apiContext.getAccessKey())) {
            String sign =
                    apiAuthentication.signature(
                            responseBody, apiContext.getAccessKey(), apiContext.getSignType().name());
            if (!Strings.isNullOrEmpty(sign)) {
                apiContext.getHttpResponse().setHeader(ApiConstants.SIGN_TYPE, apiContext.getSignType().name());
                apiContext.getHttpResponse().setHeader(ApiConstants.SIGN, sign);
            }
            apiContext.setResponseSign(sign);
        }
        Servlets.writeResponse(response, responseBody);
        return;
    }

    private String notFound() {
        String responseBody;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(false);
        apiResponse.setResult(ApiServiceResultCode.MOCK_NOT_FOUND);
        responseBody = JSON.toJSONString(apiResponse);
        return responseBody;
    }

    private int matchCount(Map actual, Map expect) {
        int count = 0;
        for (Object o : expect.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object av = actual.get(entry.getKey());
            if (av != null && av.equals(entry.getValue())) {
                count++;
            }
        }
        return count;
    }

}

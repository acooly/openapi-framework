/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-10-19 15:01
 */
package com.acooly.openapi.test.facade;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Ids;
import com.acooly.module.test.AppTestBase;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiSignOrder;
import com.acooly.openapi.framework.facade.order.ApiVerifyOrder;
import com.acooly.openapi.framework.facade.result.ApiSignResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * facade测试
 *
 * @author zhangpu
 * @date 2019-10-19 15:01
 */
@Slf4j
@EnableAutoConfiguration
public class OpenApiRemoteServiceTest extends AppTestBase {

    static {
        Apps.setProfileIfNotExists("sdev");
    }

    @Autowired
    private OpenApiRemoteService openApiRemoteService;

    @Test
    public void testSignAndVerify() {

        String waitForSign = "adsfasdfasdfasdf";
        log.info("OpenApi Remote body: {}", waitForSign);
        ApiSignOrder apiSignOrder = new ApiSignOrder(ApiConstants.TEST_ACCESS_KEY, waitForSign);
        apiSignOrder.setGid(Ids.gid());
        apiSignOrder.setPartnerId(ApiConstants.TEST_ACCESS_KEY);
        ApiSignResult apiSignResult = openApiRemoteService.sign(apiSignOrder);
        if (!apiSignResult.success()) {
            log.warn("OpenApi Remote Sign failure: {}", apiSignResult.getStatus(), apiSignResult.getDetail());
            return;
        }
        log.info("OpenApi Remote Sign: {}", apiSignResult.getSign());

        ApiVerifyOrder apiVerifyOrder = new ApiVerifyOrder(ApiConstants.TEST_ACCESS_KEY, apiSignResult.getSign(), waitForSign);
        apiVerifyOrder.setGid(Ids.gid());
        apiVerifyOrder.setPartnerId(ApiConstants.TEST_ACCESS_KEY);
        ResultBase apiVerifyResult = openApiRemoteService.verify(apiVerifyOrder);
        if (!apiVerifyResult.success()) {
            log.warn("OpenApi Remote verify failure: {}", apiVerifyResult.getStatus(), apiVerifyResult.getDetail());
            return;
        }
        log.info("OpenApi Remote verify: ok");
    }

}

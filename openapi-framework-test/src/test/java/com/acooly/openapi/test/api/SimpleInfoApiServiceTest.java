/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-04-14 14:46
 */
package com.acooly.openapi.test.api;

import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2019-04-14 14:46
 */
@Slf4j
public class SimpleInfoApiServiceTest extends AbstractApiServieTests {


    String specialStr = "😁🀀εǚ☏©\uD83D\uDC3E ";


    @Test
    public void testSpecialStr() {
        ApiRequest request = new ApiRequest();
        request.setProtocol(ApiProtocol.HTTP_FORM_JSON);
        request.setService("demoSimpleInfo");
        request.setContext(specialStr);
        request(request, ApiResponse.class);
    }


    public static void main(String[] args) {
        SimpleInfoApiServiceTest test = new SimpleInfoApiServiceTest();
        ApiRequest request = new ApiRequest();
        request.setContext("😁🀀εǚ☏©");
    }

}

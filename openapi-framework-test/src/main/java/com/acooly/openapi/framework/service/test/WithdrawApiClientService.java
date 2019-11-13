package com.acooly.openapi.framework.service.test;

import com.acooly.openapi.framework.client.AbstractApiClientService;
import com.acooly.openapi.framework.service.test.request.WithdrawApiRequest;
import com.acooly.openapi.framework.service.test.response.WithdrawApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhangpu
 * @date 2019-02-11 01:17
 */
@Slf4j
@Component
public class WithdrawApiClientService extends AbstractApiClientService {

    public WithdrawApiResponse withdraw(WithdrawApiRequest request) {
        return request(request, WithdrawApiResponse.class);
    }
}

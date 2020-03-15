/**
 * openapi-client-demo
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-03-05 11:35
 */
package com.acooly.openapi.test.openapitools.message;

import com.acooly.openapi.framework.common.message.ApiRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2020-03-05 11:35
 */
@Getter
@Setter
public class LoginApiRequest extends ApiRequest {

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 128)
    private String password;

    private String customerIp;

    private String channel;

    private String deviceType;

    @Size(max = 64)
    private String deviceModel;

    @Size(max = 64)
    private String deviceId;

}

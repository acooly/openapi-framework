/**
 * openapi-client-demo
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-03-05 11:37
 */
package com.acooly.openapi.test.openapitools.message;

import com.acooly.openapi.framework.common.message.ApiResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2020-03-05 11:37
 */
@Getter
@Setter
public class LoginApiResponse extends ApiResponse {

    @NotBlank
    @Size(min = 8, max = 16)
    private String accessKey;

    @NotBlank
    @Size(min = 40, max = 40)
    private String secretKey;

}

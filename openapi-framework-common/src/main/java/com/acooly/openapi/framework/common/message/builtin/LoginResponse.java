package com.acooly.openapi.framework.common.message.builtin;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * 登录 响应报文
 *
 * @author zhangpu
 */
@Setter
@Getter
public class LoginResponse extends ApiResponse {

    @NotBlank
    @Size(min = 8, max = 16)
    @OpenApiField(desc = "访问码", constraint = "动态访问码，可以是客户的用户名,作为登录所有接口的签名accessKey",
            demo = "acooly", ordinal = 1)
    private String accessKey;

    @NotBlank
    @Size(min = 40, max = 40)
    @OpenApiField(desc = "安全码", constraint = "登录后所有接口的签名动态秘钥",
            demo = "asdfadfasd234asdfarqwerq", ordinal = 2)
    private String secretKey;

    @Size(min = 40, max = 40)
    @OpenApiField(desc = "合作方ID", constraint = "登录后所有接口的parentId参数", demo = "asdfadfasd234asdfarqwerq", ordinal = 3)
    private String parentId;


    @NotBlank
    @OpenApiField(desc = "客户id", constraint = "客户id", demo = "112", ordinal = 4)
    private String customerId;

    @NotBlank
    @OpenApiField(desc = "扩展字段", constraint = "响应扩展字段,Json格式", demo = "{\"xxxx\":\"yyyy\"}", ordinal = 5)
    private String extJson;
}

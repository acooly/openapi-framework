/**
 * create by zhangpu date:2015年5月12日
 */
package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author zhangpu
 */
@Getter
@Setter
public class AppRequest extends ApiRequest {

    /**
     * 设备唯一标识
     */
    @NotEmpty
    @Size(min = 8, max = 128)
    @OpenApiField(desc = "设备标识", constraint = "APP客户端设备唯一标识",
            demo = "9774d56d682e549c", ordinal = ApiConstants.ORDINAL_MAX + 1)
    private String deviceId;

    @NotEmpty
    @Size(min = 1, max = 10)
    @OpenApiField(desc = "APP版本号", constraint = "APP程序版本号",
            demo = "111", ordinal = ApiConstants.ORDINAL_MAX + 2)
    private String appVersion;
}

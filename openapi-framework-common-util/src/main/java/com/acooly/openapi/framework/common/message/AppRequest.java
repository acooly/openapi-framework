/**
 * create by zhangpu date:2015年5月12日
 */
package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * App客户端请求基类
 *
 * @author zhangpu@acooly.cn
 * @date 2015年5月12日
 */
public class AppRequest extends ApiRequest {

    /**
     * 设备唯一标识
     */
    @NotBlank
    @Size(min = 8, max = 128)
    @OpenApiField(desc = "设备标识", constraint = "APP客户端设备唯一标识",
            demo = "9774d56d682e549c", ordinal = ApiConstants.ORDINAL_MAX + 1)
    private String deviceId;

    @NotBlank
    @Size(min = 1, max = 10)
    @OpenApiField(desc = "APP版本号", constraint = "APP程序版本号",
            demo = "111", ordinal = ApiConstants.ORDINAL_MAX + 2)
    private String appVersion;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}

/**
 * create by zhangpu date:2015年5月12日
 */
package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;

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
    @Size(min = 8, max = 128)
    @OpenApiField(desc = "设备标识", constraint = "APP客户端设备唯一标识,如果是Web，则可以是浏览器唯一标志，例如：浏览器指纹",
            demo = "9774d56d682e549c", ordinal = ApiConstants.ORDINAL_MAX + 1)
    private String deviceId;

    /**
     * 客户端版本号
     */
    @Size(min = 1, max = 10)
    @OpenApiField(desc = "APP版本号", constraint = "APP程序版本号", demo = "1.2.0", ordinal = ApiConstants.ORDINAL_MAX + 2)
    private String appVersion;

    /**
     * 用户唯一标志
     * 用户名(userNo) or 会员编码(memberNo) or 客户编码(customerId)
     */
    @Size(min = 6, max = 32)
    @OpenApiField(desc = "用户标志", constraint = "用户唯一标志", demo = "12345678901234567890", ordinal = ApiConstants.ORDINAL_MAX + 3)
    private String customerId;

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

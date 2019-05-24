/**
 * create by zhangpu date:2015年5月6日
 */
package com.acooly.openapi.framework.common.message.builtin;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.enums.DeviceType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 登录 请求报文
 *
 * @author zhangpu
 */
@Getter
@Setter
public class LoginRequest extends ApiRequest {

    @NotEmpty
    @Size(max = 50)
    @OpenApiField(desc = "用户名", constraint = "登录的ID", demo = "zhangpu", ordinal = 1)
    private String username;

    @NotEmpty
    @Size(max = 128)
    @OpenApiField(desc = "密码", security = true, demo = "xxdawER2e$#rwe", ordinal = 2)
    private String password;

    @OpenApiField(desc = "客户IP", constraint = "请求客户端IP", demo = "218.231.22.11", ordinal = 3)
    private String customerIp;

    @OpenApiField(desc = "登录渠道", constraint = "WECHAT:微信, H5:H5, WEB:网站, ANDROID:安卓, IOS:苹果, other:其他（设计为字符串，你可以根据业务情况自定义）",
            demo = "WEB", ordinal = 4)
    private String channel;

    @OpenApiField(desc = "设备类型", constraint = "设备类型", ordinal = 5)
    private DeviceType deviceType;

    @Size(max = 64)
    @OpenApiField(desc = "设备型号", demo = "HUAWEI-MATE10", ordinal = 6)
    private String deviceModel;

    @Size(max = 64)
    @OpenApiField(desc = "设备标识", demo = "adfas234sdfsdfa", ordinal = 7)
    private String deviceId;

}

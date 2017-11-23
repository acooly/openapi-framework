/** create by zhangpu date:2015年5月6日 */
package com.acooly.openapi.framework.common.login;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
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
@OpenApiMessage(service = ApiConstants.LOGIN_SERVICE_NAME, type = ApiMessageType.Request)
public class LoginRequest extends ApiRequest {

  @NotEmpty
  @Size(max = 50)
  @OpenApiField(desc = "用户名", constraint = "登录的ID")
  private String username;

  @NotEmpty
  @Size(max = 128)
  @OpenApiField(desc = "密码", security = true)
  private String password;

  @OpenApiField(desc = "客户IP")
  private String customerIp;

  @OpenApiField(desc = "登录渠道", constraint = "注意：与注册渠道一致")
  private String channel;

  @OpenApiField(desc = "设备类型")
  private String deviceType;

  @Size(max = 64)
  @OpenApiField(desc = "设备型号")
  private String deviceModel;

  @Size(max = 64)
  @OpenApiField(desc = "设备标识")
  private String deviceId;

  @Size(max = 512)
  @OpenApiField(desc = "请求扩展字段,Json格式")
  private String extJson;
}

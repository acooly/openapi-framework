/** create by zhangpu date:2015年5月12日 */
package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/** @author zhangpu */
@Getter
@Setter
public class AppRequest extends ApiRequest {

  /** 设备唯一标识 */
  @NotEmpty
  @Size(min = 8, max = 128)
  @OpenApiField(desc = "设备标识", constraint = "APP客户端设备唯一标识")
  private String deviceId;

  @NotEmpty
  @Size(min = 1, max = 10)
  @OpenApiField(desc = "APP版本号", constraint = "APP程序版本号")
  private String appVersion;
}

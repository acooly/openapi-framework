package com.acooly.openapi.framework.common.login;

import com.acooly.core.common.enums.EntityStatus;
import lombok.Data;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-22 18:04
 */
@Data
public class AppCustomerDto {
  private String userName;

  private String accessKey;

  private String secretKey;

  private String deviceType;

  private String deviceModel;

  private String deviceId;

  private EntityStatus status;
}

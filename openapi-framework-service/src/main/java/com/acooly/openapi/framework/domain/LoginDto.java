package com.acooly.openapi.framework.domain;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto extends InfoBase {
  private String customerId;
  private String extJson;
}

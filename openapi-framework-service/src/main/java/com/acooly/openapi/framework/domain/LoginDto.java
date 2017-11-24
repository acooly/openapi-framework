package com.acooly.openapi.framework.domain;

import com.acooly.core.common.facade.InfoBase;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LoginDto extends InfoBase {
  private String customerId;
  private Map<String, String> ext = Maps.newHashMap();
  /** 增加扩展参数 */
  public void ext(String key, String value) {
    this.ext.put(key, value);
  }

  /** 获取扩展参数 */
  public String ext(String key) {
    return this.ext.get(key);
  }
}

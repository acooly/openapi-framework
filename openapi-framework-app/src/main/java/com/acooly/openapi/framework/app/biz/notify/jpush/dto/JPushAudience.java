/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify.jpush.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangpu
 * @date 2015年11月4日
 */
@Getter
@Setter
public class JPushAudience {

  private List<String> tag;

  @JsonProperty("tag_and")
  private List<String> tagAnd;

  private List<String> alias;

  @JsonProperty("registration_id")
  private List<String> registrationId;

  public void appendAlias(String a) {
    if (alias == null) {
      alias = Lists.newArrayList();
    }
    alias.add(a);
  }

}

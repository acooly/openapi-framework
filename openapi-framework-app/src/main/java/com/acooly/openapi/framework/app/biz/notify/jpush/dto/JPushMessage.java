/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify.jpush.dto;

import com.acooly.core.utils.mapper.JsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2015年11月4日
 */
@Getter
@Setter
public class JPushMessage {

  @NotBlank
  @JsonProperty("msg_content")
  private String msgContent;

  @JsonProperty("title")
  private String title;

  @JsonProperty("content_type")
  private String contentType;

  @JsonProperty("extras")
  private Map<String, Object> extras;

  @Override
  public String toString() {
    return JsonMapper.nonEmptyMapper().toJson(this);
  }
}

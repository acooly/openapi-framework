/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify.jpush.support;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushAudience;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author zhangpu
 * @date 2015年11月4日
 */
public class JPushAudienceSerializer extends JsonSerializer<JPushAudience> {

  @Override
  public void serialize(JPushAudience value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    if (Collections3.isEmpty(value.getTag())
        && Collections3.isEmpty(value.getTagAnd())
        && Collections3.isEmpty(value.getAlias())
        && Collections3.isEmpty(value.getRegistrationId())) {
      jgen.writeString("all");
    } else {
      jgen.writeObject(value);
    }
  }
}

/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify.jpush;

import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushAudience;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushMessage;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushNotification;
import com.acooly.openapi.framework.app.biz.notify.jpush.dto.JPushOptions;
import com.acooly.openapi.framework.app.biz.notify.jpush.support.JPushAudienceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * JPush 请求对象
 *
 * @author zhangpu
 * @date 2015年11月4日
 */
public class JPushOrder {

  private String platform = "all";

  @JsonSerialize(using = JPushAudienceSerializer.class)
  private JPushAudience audience = new JPushAudience();

  private JPushMessage message = new JPushMessage();

  private JPushOptions options = new JPushOptions();

  private JPushNotification notification = new JPushNotification();

  public static void main(String[] args) {
    JPushOrder context = new JPushOrder();
    JPushAudience ja = new JPushAudience();
    ja.appendAlias("zhangpu");
    context.setAudience(ja);
    System.out.println(context.toString());
  }

  public void appendAlias(String alias) {
    getAudience().appendAlias(alias);
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public JPushAudience getAudience() {
    return audience;
  }

  public void setAudience(JPushAudience audience) {
    this.audience = audience;
  }

  public JPushMessage getMessage() {
    return message;
  }

  public void setMessage(JPushMessage message) {
    this.message = message;
  }

  public JPushOptions getOptions() {
    return options;
  }

  public void setOptions(JPushOptions options) {
    this.options = options;
  }

  public JPushNotification getNotification() {
    return notification;
  }

  public void setNotification(JPushNotification notification) {
    this.notification = notification;
  }

  @Override
  public String toString() {
    return JsonMapper.nonEmptyMapper().toJson(this);
  }
}

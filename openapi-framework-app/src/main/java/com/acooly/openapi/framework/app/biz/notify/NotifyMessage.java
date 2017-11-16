/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.notify;

import com.acooly.openapi.framework.app.biz.enums.AppMessageContentType;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

/**
 * 推送消息
 *
 * <ul>
 *   <b>注意：如果是JPush实现</b>
 *   <li>默认会同时发送JPush:Message(title -> title,msgContent -> content, extras ->
 *       context)和JPush:Notification( alert -> content和extras -> context, android:title -> title)
 *   <li>如果在properties扩展参数中设置了JPush_Notification参数，则只会发送JPush：Message, notification的消息一扩展设置为准
 * </ul>
 *
 * @author zhangpu
 * @date 2015年11月4日
 */
public class NotifyMessage {

  @NotBlank
  /** 消息标题 */
  private String title;
  /** 内容类型 */
  private AppMessageContentType contentType = AppMessageContentType.normal;

  @NotBlank
  /** 消息内容 */
  private String content;
  /** 业务参数 */
  private Map<String, Object> context;
  /** 扩展参数 */
  private Map<NotifyProperty, Object> properties;
  /** 是否生产环境,注意，如果直接在properties里面设置了JPush_Options参数，则覆盖该参数 */
  private boolean online = false;

  public NotifyMessage() {}

  public NotifyMessage(String title, String content) {
    super();
    this.title = title;
    this.content = content;
  }

  public NotifyMessage(String title, String content, Map<String, Object> context) {
    super();
    this.title = title;
    this.content = content;
    this.context = context;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Map<String, Object> getContext() {
    return context;
  }

  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  public Map<NotifyProperty, Object> getProperties() {
    return properties;
  }

  public void setProperties(Map<NotifyProperty, Object> properties) {
    this.properties = properties;
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public AppMessageContentType getContentType() {
    return contentType;
  }

  public void setContentType(AppMessageContentType contentType) {
    this.contentType = contentType;
  }
}

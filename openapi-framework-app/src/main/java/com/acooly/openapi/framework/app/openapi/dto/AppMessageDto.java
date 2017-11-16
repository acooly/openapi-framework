/** create by zhangpu date:2016年2月13日 */
package com.acooly.openapi.framework.app.openapi.dto;

import com.acooly.openapi.framework.app.biz.enums.AppMessageType;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhangpu
 * @date 2016年2月13日
 */
public class AppMessageDto {

  /** 主键ID */
  @NotNull
  @OpenApiField(desc = "唯一标识")
  private Long id;
  /** 标题 */
  @NotEmpty
  @OpenApiField(desc = "标题")
  private String title;
  /** 发送时间 */
  @NotNull
  @OpenApiField(desc = "发送时间")
  private Date sendTime;
  /** 群发类型 */
  @NotNull
  @OpenApiField(desc = "群发类型")
  private AppMessageType type;
  /** 发送内容 */
  @NotEmpty
  @OpenApiField(desc = "内容")
  private String content;
  /** 会话内容 */
  @OpenApiField(desc = "会话")
  private String context;
  /** 发送人 */
  @OpenApiField(desc = "发送人")
  private String sender;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }

  public AppMessageType getType() {
    return type;
  }

  public void setType(AppMessageType type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }
}

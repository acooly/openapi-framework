package com.acooly.openapi.framework.app.biz.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.framework.app.biz.enums.AppMessageContentType;
import com.acooly.openapi.framework.app.biz.enums.AppMessageStatus;
import com.acooly.openapi.framework.app.biz.enums.AppMessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * 系统消息 Entity
 *
 * @author Acooly Code Generator Date: 2015-11-04 13:30:36
 */
@Entity
@Table(name = "app_message")
@Getter
@Setter
public class AppMessage extends AbstractEntity {
  /** 标题 */
  private String title;
  /** 发送时间 */
  private Date sendTime;
  /** 内容类型 */
  @Enumerated(EnumType.STRING)
  private AppMessageContentType contentType;
  /** 群发类型 */
  @Enumerated(EnumType.STRING)
  private AppMessageType type;
  /** 定时发送时间 */
  private Date schedulerTime;
  /** 发送内容 */
  private String content;

  /** 会话内容 */
  private String context;

  /** 发送人 */
  private String sender;
  /** 类型为group时有效。多个用户使用逗号分隔 */
  private String receivers;
  /** 状态. */
  @Enumerated(EnumType.STRING)
  private AppMessageStatus status;
  /** 备注 */
  private String comments;
}

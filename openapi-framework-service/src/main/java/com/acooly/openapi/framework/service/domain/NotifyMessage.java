/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.service.domain;

import com.acooly.core.common.facade.LinkedHashMapParameterize;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.openapi.framework.common.enums.MessageType;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/** @author zhangpu */
@Getter
@Setter
public class NotifyMessage extends LinkedHashMapParameterize<String, String>
    implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = -4139923883446595513L;

  @Id private Long id;
  @NotBlank private String gid;
  @NotBlank private String partnerId;
  @NotNull private MessageType messageType = MessageType.HTTP;
  @NotBlank @HttpUrl private String url;
  private String content;
  private String service;
  private String version;

  private Date createTime;
  private Date updateTime;

  private int sendCount = 0;
  private Date nextSendTime;
  /** 通知响应信息 */
  private String respInfo;

  private String requestNo;

  private String merchOrderNo;

  /** 任务状态 */
  @NotNull private TaskStatus status = TaskStatus.Waitting;
  /** 任务执行状态（读取时修改为处理中，完成后修改对应完结状态，设计为代替数据库事务或外部锁） */
  @NotNull private TaskExecuteStatus executeStatus = TaskExecuteStatus.Unprocessed;

  @Override
  public String toString() {
    return ToString.toString(this);
  }
}

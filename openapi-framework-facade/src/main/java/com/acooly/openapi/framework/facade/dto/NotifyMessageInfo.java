/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-11-11 13:46 创建
 */
package com.acooly.openapi.framework.facade.dto;

import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import javax.validation.constraints.NotBlank;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author acooly
 */
public class NotifyMessageInfo implements Serializable {

    private static final long serialVersionUID = -4139923883446595513L;

    @Id
    private Long id;
    @NotBlank
    private String gid;
    @NotBlank
    private String partnerId;
    @NotNull
    private String messageTypeCode;
    private String messageTypeMessage;
    @NotBlank
    @HttpUrl
    private String url;
    private String content;
    private String service;
    private String version;

    private Date createTime;
    private Date updateTime;

    private int sendCount = 0;
    private Date nextSendTime;
    /**
     * 通知响应信息
     */
    private String respInfo;

    private String requestNo;

    private String merchOrderNo;

    /**
     * 任务状态
     */
    @NotNull
    private TaskStatus status = TaskStatus.Waitting;
    /**
     * 任务执行状态（读取时修改为处理中，完成后修改对应完结状态，设计为代替数据库事务或外部锁）
     */
    @NotNull
    private TaskExecuteStatus executeStatus = TaskExecuteStatus.Unprocessed;
}

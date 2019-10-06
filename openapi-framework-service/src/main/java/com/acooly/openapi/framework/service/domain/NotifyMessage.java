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
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.MessageType;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangpu
 */
@Getter
@Setter
public class NotifyMessage extends LinkedHashMapParameterize<String, String>
        implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4139923883446595513L;

    @Id
    private Long id;
    @NotBlank
    private String gid;
    private String requestNo;
    @NotBlank
    private String partnerId;
    @NotNull
    private MessageType messageType = MessageType.HTTP;
    @NotBlank
    @HttpUrl
    private String url;
    @NotBlank
    private String signType;

    private String sign;

    /**
     * 发送的内容
     */
    @NotBlank
    private String content;
    /**
     * 协议（用于选择通知的具体实现）
     */
    private ApiProtocol protocol;

    private String service;
    private String version;

    private Date createTime;
    private Date updateTime;

    private int sendCount = 0;
    private Date nextSendTime;

    /**
     * 被通知方：响应信息
     */
    private String respInfo;


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

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}

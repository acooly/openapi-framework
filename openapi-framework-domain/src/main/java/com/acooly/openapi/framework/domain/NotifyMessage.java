/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.domain;

import com.acooly.core.common.facade.LinkedHashMapParameterize;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.google.common.base.Objects;
import com.acooly.openapi.framework.common.enums.MessageType;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangpu
 */
public class NotifyMessage extends LinkedHashMapParameterize<String, String> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4139923883446595513L;

    @Id
    private Long id;
    @NotBlank
    private String gid;
    @NotBlank
    private String partnerId;
    @NotNull
    private MessageType messageType = MessageType.HTTP;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Date getNextSendTime() {
        return nextSendTime;
    }

    public void setNextSendTime(Date nextSendTime) {
        this.nextSendTime = nextSendTime;
    }

    public TaskExecuteStatus getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(TaskExecuteStatus executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getRespInfo() {
        return respInfo;
    }

    public void setRespInfo(String respInfo) {
        this.respInfo = respInfo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getMerchOrderNo() {
        return merchOrderNo;
    }

    public void setMerchOrderNo(String merchOrderNo) {
        this.merchOrderNo = merchOrderNo;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("gid", gid)
                .add("requestNo", requestNo)
                .add("merchOrderNo", merchOrderNo)
                .add("partnerId", partnerId)
                .add("service", service)
                .add("createTime", createTime)
                .add("sendCount", sendCount)
                .add("status", status)
                .add("executeStatus", executeStatus)
                .add("respInfo", respInfo)
                .toString();
    }
}

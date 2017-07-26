/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-28
 *
 */
package com.acooly.openapi.framework.domain;


import com.acooly.core.common.domain.AbstractEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * api_partner_service Entity
 *
 * @author acooly
 *         Date: 2016-07-28 15:33:42
 */
@Entity
@Table(name = "api_partner_service")
public class ApiPartnerService extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 接入方编码
     */
    private String partnerId;
    /**
     * 接入方名称
     */
    private String parnerName;
    /**
     * 接入方主键
     */
    private Long apipartnerid;
    /**
     * 服务主键
     */
    private Long apiserviceid;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 服务中文名
     */
    private String serviceTitle;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String comments;

    public String getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getParnerName() {
        return this.parnerName;
    }

    public void setParnerName(String parnerName) {
        this.parnerName = parnerName;
    }

    public Long getApipartnerid() {
        return apipartnerid;
    }

    public void setApipartnerid(Long apipartnerid) {
        this.apipartnerid = apipartnerid;
    }

    public Long getApiserviceid() {
        return apiserviceid;
    }

    public void setApiserviceid(Long apiserviceid) {
        this.apiserviceid = apiserviceid;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return this.serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceTitle() {
        return this.serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}

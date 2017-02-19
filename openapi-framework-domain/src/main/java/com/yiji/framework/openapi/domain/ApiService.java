/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.domain;


import com.acooly.core.common.domain.AbstractEntity;
import javax.persistence.*;
import java.util.Date;

/**
 * 服务分类 Entity
 *
 * @author acooly
 *         Date: 2016-07-16 01:57:14
 */
@Entity
@Table(name = "api_service")
public class ApiService extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 服务编码
     */
    private String code;
    /**
     * 服务名
     */
    private String name;
    /**
     * 服务版本
     */
    private String version;
    /**
     * 中文描述
     */
    private String title;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "type_id")
    private ApiServiceType apiServiceType;


    public ApiServiceType getApiServiceType() {
        return apiServiceType;
    }

    public void setApiServiceType(ApiServiceType apiServiceType) {
        this.apiServiceType = apiServiceType;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

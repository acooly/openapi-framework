/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.yiji.framework.openapi.common.enums.SecretType;
import com.yiji.framework.openapi.common.enums.SignType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * 合作方管理 Entity
 *
 * @author acooly
 *         Date: 2016-07-16 02:05:01
 */
@Entity
@Table(name = "api_partner")
public class ApiPartner extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 合作方编码
     */
    private String partnerId;
    /**
     * 合作方名称
     */
    private String partnerName;
    /**
     * 安全方案
     */
    @Enumerated(EnumType.STRING)
    private SecretType secretType;
    /**
     * 签名类型
     */
    @Enumerated(EnumType.STRING)
    private SignType signType;
    /**
     * 秘钥
     */
    private String secretKey;
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

    public String getPartnerName() {
        return this.partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }


    public SecretType getSecretType() {
        return this.secretType;
    }

    public void setSecretType(SecretType secretType) {
        this.secretType = secretType;
    }

    public SignType getSignType() {
        return this.signType;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
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

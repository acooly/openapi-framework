/*
 * acooly.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * faZheng 2016年10月11日 下午15:37:19 创建
 */

package com.acooly.openapi.framework.demo.message.dto;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.demo.message.enums.TransactorType;
import com.acooly.openapi.framework.demo.message.enums.UserAuthEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 经营人信息
 *
 * @author faZheng
 */
public class TransactorInfo {

    /**
     * 所属企业用户ID
     */
    @NotBlank
    @OpenApiField(desc = "所属企业用户ID", constraint = "所属企业用户ID")
    @Size(max = 32)
    private String userId;

    /**
     * 经营人ID *
     */
    @NotBlank
    @Size(max = 32)
    @OpenApiField(desc = "经营人ID", constraint = "经营人ID")
    private String transactorId;

    /**
     * 经营人类型
     */
    @NotNull
    @OpenApiField(desc = "经营人类型", constraint = "经营人类型")
    private TransactorType transactorType;

    /**
     * 真实姓名
     */
    @NotBlank
    @Size(max = 64)
    @OpenApiField(desc = "真实姓名", constraint = "真实姓名, 如：张三")
    private String realName;

    /**
     * 身份证号码
     */
    @NotBlank
    @Size(max = 18)
    @OpenApiField(desc = "身份证号码", constraint = "身份证号码")
    private String certNo;

    /**
     * 手机号
     */
    @Size(max = 18)
    @OpenApiField(desc = "手机号码", constraint = "手机号码")
    private String mobileNo;

    /**
     * 联系邮箱
     */
    @Size(max = 128)
    @OpenApiField(desc = "联系邮箱", constraint = "联系邮箱, 格式如：xx@xx.com")
    private String email;

    /**
     * 常住地址
     */
    @Size(max = 256)
    @OpenApiField(desc = "常住地址", constraint = "常住地址")
    private String address;

    /**
     * 实名认证状态
     */
    @OpenApiField(desc = "实名认证状态", constraint = "实名认证状态")
    private UserAuthEnum realNameAuth;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransactorId() {
        return this.transactorId;
    }

    public void setTransactorId(String transactorId) {
        this.transactorId = transactorId;
    }

    public TransactorType getTransactorType() {
        return this.transactorType;
    }

    public void setTransactorType(TransactorType transactorType) {
        this.transactorType = transactorType;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCertNo() {
        return this.certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserAuthEnum getRealNameAuth() {
        return this.realNameAuth;
    }

    public void setRealNameAuth(UserAuthEnum realNameAuth) {
        this.realNameAuth = realNameAuth;
    }
}

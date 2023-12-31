/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.enums.MessageTypeEnum;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 服务报文 Entity
 *
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
@Getter
@Setter
@Entity
@Table(name = "api_doc_message")
public class ApiDocMessage extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 消息编号
     * serviceNo+messageType
     */
    @NotBlank
    @Size(max = 64)
    private String messageNo;

    /**
     * 服务ID
     */
    @NotNull
    private String serviceNo;

    /**
     * 消息类型
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private MessageTypeEnum messageType = MessageTypeEnum.Request;

    /**
     * 自动说明
     */
    @Size(max = 255)
    private String note;

    /**
     * 人工说明
     */
    @Size(max = 255)
    private String manualNote;

    /**
     * 排序值
     */
    private Long sortTime = System.currentTimeMillis();

    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;


    /**
     * 签名
     */
    @Size(max = 128)
    private String signature;

    @Transient
    private List<ApiDocItem> apiDocItems = Lists.newArrayList();


    @Override
    public int hashCode() {
        if (Strings.isNoneBlank(getMessageNo())) {
            return getMessageNo().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AbstractEntity)) {
            return false;
        } else {
            ApiDocMessage that = (ApiDocMessage) o;
            return Strings.equals(that.getMessageNo(), this.getMessageNo());
        }
    }

}

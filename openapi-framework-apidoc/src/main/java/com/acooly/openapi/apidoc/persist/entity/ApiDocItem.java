/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.enums.ApiDataTypeEnum;
import com.acooly.openapi.apidoc.enums.ApiEncryptstatusEnum;
import com.acooly.openapi.apidoc.enums.FieldStatus;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Signed;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.List;

/**
 * 报文字段 Entity
 *
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
@Getter
@Setter
@Entity
@Table(name = "api_doc_item")
public class ApiDocItem extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 父ID
     */
    private Long parentId;


    /**
     * 报文编码
     */
    @Signed
    @NotEmpty
    private String messageNo;

    /**
     * 唯一编号
     */
    @Signed
    @Size(max = 255)
    private String parentNo;

    /**
     * 唯一编号
     * md5(message_no+parent_no+name)
     */
    @Signed
    @Size(max = 255)
    private String itemNo;

    /**
     * 字段名称
     */
    @Signed
    @NotEmpty
    @Size(max = 128)
    private String name;

    /**
     * 字段标题
     */
    @Signed
    @NotEmpty
    @Size(max = 128)
    private String title;

    @Signed
    private Integer min;
    @Signed
    private Integer max;

    /**
     * 数据类型
     */
    @Signed
    @NotNull
    @Size(max = 16)
    private ApiDataTypeEnum dataType = ApiDataTypeEnum.S;

    /**
     * 字段描述
     */
    @Signed
    @Size(max = 512)
    private String descn;

    /**
     * 字段示例
     */
    @Signed
    @Size(max = 512)
    private String demo;

    /**
     * 可选状态
     */
    @Signed
    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldStatus status = FieldStatus.O;

    /**
     * 是否加密
     */
    @Signed
    @Enumerated(EnumType.STRING)
    private ApiEncryptstatusEnum encryptstatus = ApiEncryptstatusEnum.no;


    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;

    /**
     * 排序辅助值
     */
    private Long sortTime = System.currentTimeMillis();

    /**
     * 签名
     */
    @Size(max = 128)
    private String signatrue;

    @Transient
    private List<ApiDocItem> children = Lists.newArrayList();

    public ApiDocItem() {
    }

    public ApiDocItem(String name, String title, String descn, Integer min, Integer max, ApiDataTypeEnum dataType, String demo, FieldStatus status, ApiEncryptstatusEnum encryptstatus) {
        this.name = name;
        this.title = title;
        this.dataType = dataType;
        this.descn = descn;
        this.min = min;
        this.max = max;
        this.demo = demo;
        this.status = status;
        this.encryptstatus = encryptstatus;
    }

    public void addChild(ApiDocItem apiDocItem) {
        children.add(apiDocItem);
    }


    @Override
    public int hashCode() {
        if (Strings.isNoneBlank(getItemNo())) {
            return getItemNo().hashCode();
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
            ApiDocItem that = (ApiDocItem) o;
            return Strings.equals(that.getItemNo(), this.getItemNo());
        }
    }


    public static class ApiDocItemComparator implements Comparator<ApiDocItem> {
        @Override
        public int compare(ApiDocItem node1, ApiDocItem node2) {
            long orderTime1 = node1.getSortTime();
            long orderTime2 = node2.getSortTime();
            return orderTime1 > orderTime2 ? 1 : (orderTime1 == orderTime2 ? 0 : -1);
        }
    }


}

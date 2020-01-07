/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.domain.Sortable;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.arithmetic.tree.TreeNode;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.enums.DocStatusEnum;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 服务方案 Entity
 *
 * @author acooly
 * Date: 2017-12-05 12:34:38
 */
@Getter
@Setter
@Entity
@Table(name = "api_doc_scheme")
public class ApiDocScheme extends AbstractEntity implements TreeNode<ApiDocScheme>, Sortable {

    public static final Long TOP_PARENT_ID = 0L;
    public static final String TOP_PARENT_PATH = "/";
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 方案编码
     */
    @Size(max = 64)
    private String schemeNo;

    /**
     * 父级编码
     */
    private String parentSchemeNo;

    /**
     * 标题
     */
    @NotBlank
    @Size(max = 64)
    private String title;

    /**
     * 作者
     */
    @Size(max = 64)
    private String author = ApiDocProperties.DEF_SCHEME_AUTHOR;

    /**
     * 说明
     */
    @Size(max = 128)
    private String note;

    /**
     * 方案类型
     */
    @Enumerated(EnumType.STRING)
    private SchemeTypeEnum schemeType;

    /**
     * 排序值
     */
    private Long sortTime = System.currentTimeMillis();

    /**
     * 父级id
     */
    private Long parentId = TOP_PARENT_ID;

    /**
     * 分类，考虑扩展性，不使用枚举限制
     */
    @Size(max = 45)
    private String category = ApiDocProperties.DEFAULT_CATEGORY;

    /**
     * 层级路径
     */
    @Size(max = 255)
    private String path = TOP_PARENT_PATH;

    /**
     * 子节点数量
     */
    private Integer subCount = 0;

    /**
     * 文章状态
     */
    private DocStatusEnum status = DocStatusEnum.draft;

    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;

    /**
     * 所有的子
     */
    @Transient
    private List<ApiDocScheme> children = new LinkedList<ApiDocScheme>();

    /**
     * 文章状态
     */
    @Transient
    private String statusText;

    @Transient
    private List<ApiDocSchemeService> apiDocSchemeServices = Lists.newArrayList();

    @Transient
    private List<ApiDocService> services = Lists.newArrayList();

    @Transient
    private ApiDocSchemeDesc apiDocschemeDesc;


    public ApiDocScheme() {

    }

    public void append(ApiDocService apiDocService) {
        this.services.add(apiDocService);
    }

    public void append(ApiDocSchemeService apiDocSchemeService) {
        this.apiDocSchemeServices.add(apiDocSchemeService);
    }

    public ApiDocScheme(String schemeNo, String title, String parentSchemeNo) {
        this.schemeNo = schemeNo;
        this.title = title;
        if (Strings.isNotBlank(parentSchemeNo)) {
            this.parentSchemeNo = parentSchemeNo;
        }
        this.setStatus(DocStatusEnum.onShelf);
        this.setSchemeType(SchemeTypeEnum.auto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ApiDocScheme that = (ApiDocScheme) o;

        return schemeNo.equals(that.schemeNo);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + schemeNo.hashCode();
        return result;
    }

    @Override
    public void addChild(ApiDocScheme node) {
        this.children.add(node);
    }

    public static class NodeComparator implements Comparator<ApiDocScheme> {
        @Override
        public int compare(ApiDocScheme node1, ApiDocScheme node2) {
            long orderTime1 = node1.getSortTime();
            long orderTime2 = node2.getSortTime();
            return orderTime1 > orderTime2 ? -1 : (orderTime1 == orderTime2 ? 0 : 1);
        }
    }

    public String getStatusText() {
        if (status != null) {
            statusText = status.getMessage();
        }
        return statusText;
    }

}

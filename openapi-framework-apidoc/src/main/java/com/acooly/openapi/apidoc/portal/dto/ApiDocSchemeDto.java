/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-02 07:38 创建
 */
package com.acooly.openapi.apidoc.portal.dto;

import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * scheme 实体 DTO
 *
 * @author zhangpu 2017-08-02 07:38
 */
@Data
public class ApiDocSchemeDto implements Serializable {

    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 主题
     */
    private String category;

    /**
     * 方案类型
     */
    private SchemeTypeEnum schemeTypeEnum;

    /**
     * 唯一编码
     */
    private String schemeNo;

    /**
     * 名称
     */
    private String title;

    /**
     * 层级路径
     */
    private String path;

    /**
     * 子节点数量
     */
    private Integer subCount;

    /**
     * 链接地址
     */
    private String link;

    /**
     * 备注
     */
    private String comments;

    /**
     * 所有的子
     */
    private List<ApiDocSchemeDto> children = new LinkedList<ApiDocSchemeDto>();

    /**
     * 详细内容
     */
    private String content;

    /**
     * 关联的服务列表
     */
    private List<ApiDocServiceDto> serviceList = Lists.newArrayList();

    public ApiDocSchemeDto() {
    }

    public ApiDocSchemeDto(Long id, String title, String schemeNo) {
        this.id = id;
        this.title = title;
        this.schemeNo = schemeNo;
    }
}

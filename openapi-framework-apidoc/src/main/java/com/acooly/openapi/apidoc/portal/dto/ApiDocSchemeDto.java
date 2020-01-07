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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "ApiDocSchemeDto", description = "方案文档目录")
public class ApiDocSchemeDto implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    private Long parentId;

    /**
     * 分类
     */
    @ApiModelProperty(value = "文档分类{api:api文档,product:产品文档}")
    private String category;

    /**
     * 方案类型
     */
    @ApiModelProperty(value = "方案类型")
    private SchemeTypeEnum schemeTypeEnum;

    /**
     * 唯一编码
     */
    @ApiModelProperty(value = "唯一编码")
    private String schemeNo;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String title;

    /**
     * 层级路径
     */
    @ApiModelProperty(value = "层级路径")
    private String path;

    /**
     * 子节点数量
     */
    @ApiModelProperty(value = "子节点数量")
    private Integer subCount;

//    /**
//     * 链接地址
//     */
//    @ApiModelProperty(value = "链接地址")
//    private String link;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comments;

    /**
     * 所有的子
     */
    @ApiModelProperty(value = "子节点")
    private List<ApiDocSchemeDto> children = new LinkedList<ApiDocSchemeDto>();

    /**
     * 详细内容
     */
    @ApiModelProperty(value = "内容文本-markdown格式")
    private String content;

    /**
     * 关联的服务列表
     */
    @ApiModelProperty(value = "关联的服务列表")
    private List<ApiDocServiceDto> services = Lists.newArrayList();

    public ApiDocSchemeDto() {
    }

    public ApiDocSchemeDto(Long id, String title, String schemeNo) {
        this.id = id;
        this.title = title;
        this.schemeNo = schemeNo;
    }
}

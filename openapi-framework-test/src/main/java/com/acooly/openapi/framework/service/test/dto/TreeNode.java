/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:55 创建
 */
package com.acooly.openapi.framework.service.test.dto;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Data;

import java.util.List;

/**
 * @author zhangpu 2019-01-07 17:55
 */
@Data
public class TreeNode {

    @OpenApiField(desc = "ID", constraint = "ID")
    private Long id;
    @OpenApiField(desc = "父ID", constraint = "父ID")
    private Long parentId;
    @OpenApiField(desc = "标题", constraint = "标题")
    private String title;
    @OpenApiField(desc = "子节点", constraint = "子节点")
    private List<TreeNode> children;

}

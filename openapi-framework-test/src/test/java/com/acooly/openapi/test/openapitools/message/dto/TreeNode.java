/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:55 创建
 */
package com.acooly.openapi.test.openapitools.message.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangpu 2019-01-07 17:55
 */
@Getter
@Setter
public class TreeNode {
    private Long id;
    private Long parentId;
    private String title;
    /**
     * 树形结构：循环子列表
     */
    private List<TreeNode> children;

    /**
     * 子对象
     */
    private GoodsInfo goodsInfo;

    /**
     * 子列表
     */
    private List<GoodsInfo> goodsInfos;
}

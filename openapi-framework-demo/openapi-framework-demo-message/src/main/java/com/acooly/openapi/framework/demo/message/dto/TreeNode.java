/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:55 创建
 */
package com.acooly.openapi.framework.demo.message.dto;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangpu 2019-01-07 17:55
 */
@Data
public class TreeNode {

    @OpenApiField(desc = "ID", constraint = "ID", ordinal = 1)
    private Long id;
    @OpenApiField(desc = "父ID", constraint = "父ID", ordinal = 2)
    private Long parentId;
    @OpenApiField(desc = "标题", constraint = "标题", ordinal = 3)
    private String title;
    @OpenApiField(desc = "子节点", constraint = "子节点", ordinal = 4)
    private List<TreeNode> children = new ArrayList<>();
    @OpenApiField(desc = "单个商品", ordinal = 5)
    private GoodsInfo goodsInfo;
    @OpenApiField(desc = "商品信息", ordinal = 6)
    private List<GoodsInfo> goodsInfos;

    public void addChild(TreeNode treeNode) {
        this.children.add(treeNode);
    }

    public TreeNode() {
    }

    public TreeNode(Long id, Long parentId, String title) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
    }

    public TreeNode(Long id, Long parentId, String title, GoodsInfo goodsInfo, List<GoodsInfo> goodsInfos) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.goodsInfo = goodsInfo;
        this.goodsInfos = goodsInfos;
    }
}

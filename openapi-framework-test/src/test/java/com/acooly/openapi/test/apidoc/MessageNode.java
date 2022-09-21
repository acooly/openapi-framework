/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:55 创建
 */
package com.acooly.openapi.test.apidoc;

import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.demo.message.dto.GoodsInfo;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangpu 2019-01-07 17:55
 */
@Data
public class MessageNode {

    @OpenApiField(desc = "ID", constraint = "ID", demo = "1", ordinal = 1)
    private Long id;
    @OpenApiField(desc = "父ID", constraint = "父ID", demo = "0", ordinal = 2)
    private Long parentId;
    @OpenApiField(desc = "标题", constraint = "标题", demo = "这是标题名称", ordinal = 3)
    private String title;

    @OpenApiField(desc = "价格", constraint = "单价(元)", demo = "1300.38", ordinal = 4)
    private Money price;

    @OpenApiField(desc = "生日", constraint = "生日", demo = "1982-09-17", ordinal = 5)
    private Date birthday;

    @OpenApiField(desc = "生肖", constraint = "生肖属相", demo = "Dog", ordinal = 6)
    private AnimalSign animalSign;

    @OpenApiField(desc = "列表", constraint = "列表数据", ordinal = 7)
    private List<String> list;

    @OpenApiField(desc = "子节点", constraint = "子节点", ordinal = 4)
    private List<MessageNode> children = new ArrayList<>();
    @OpenApiField(desc = "单个商品", ordinal = 15)
    private GoodsInfo goodsInfo;
    @OpenApiField(desc = "商品信息", ordinal = 16)
    private List<GoodsInfo> goodsInfos;

    public MessageNode() {
    }

    public MessageNode(Long id, Long parentId, String title) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
    }

    public MessageNode(Long id, Long parentId, String title, GoodsInfo goodsInfo, List<GoodsInfo> goodsInfos) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.goodsInfo = goodsInfo;
        this.goodsInfos = goodsInfos;
    }

    public void addChild(MessageNode treeNode) {
        this.children.add(treeNode);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, false);
    }
}

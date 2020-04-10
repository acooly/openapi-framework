/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:59 创建
 */
package com.acooly.openapi.framework.demo.service.api;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.demo.message.dto.GoodsInfo;
import com.acooly.openapi.framework.demo.message.dto.TreeNode;
import com.acooly.openapi.framework.demo.message.request.DemoTreeNodeListApiRequest;
import com.acooly.openapi.framework.demo.message.response.DemoTreeNodeListApiResponse;
import com.acooly.openapi.framework.demo.service.DemoApiUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author zhangpu 2019-01-07 17:59
 */
@ApiDocType(code = DemoApiUtils.API_DEMO_DOC_TYPE_CODE, name = DemoApiUtils.API_DEMO_DOC_TYPE_NAME)
@ApiDocNote("树形递归结构报文测试")
@OpenApiService(
        name = "demoTreeNodeList",
        desc = "测试：树形结构查询",
        responseType = ResponseType.SYN,
        owner = "acooly",
        busiType = ApiBusiType.Trade
)
public class DemoTreeNodeListApiService extends BaseApiService<DemoTreeNodeListApiRequest, DemoTreeNodeListApiResponse> {
    @Override
    protected void doService(DemoTreeNodeListApiRequest request, DemoTreeNodeListApiResponse response) {

        List<GoodsInfo> goodsInfos = DemoApiUtils.buildGoodsList(2, null);
        GoodsInfo goodsInfo = Collections3.getFirst(goodsInfos);
        List<TreeNode> treeNodes = Lists.newArrayList();
        treeNodes.add(new TreeNode(1L, 0L, "node1", goodsInfo, goodsInfos));
        treeNodes.add(new TreeNode(2L, 0L, "node2", goodsInfo, goodsInfos));
        treeNodes.add(new TreeNode(3L, 1L, "node3", goodsInfo, goodsInfos));
        treeNodes.add(new TreeNode(4L, 1L, "node4", goodsInfo, goodsInfos));
        treeNodes.add(new TreeNode(5L, 2L, "node5", goodsInfo, goodsInfos));
        treeNodes = tree(treeNodes);
        response.setRows(treeNodes);
        response.setTotalPages(1);
        response.setTotalRows(treeNodes.size());
    }

    protected List<TreeNode> tree(List<TreeNode> treeNodes) {
        Map<Long, TreeNode> data = Maps.newHashMap();
        List<TreeNode> tree = Lists.newArrayList();
        for (TreeNode t : treeNodes) {
            data.put(t.getId(), t);
            if (0 == t.getParentId().longValue()) {
                tree.add(t);
            }
        }
        for (TreeNode t : treeNodes) {
            if (data.get(t.getParentId()) != null) {
                data.get(t.getParentId()).addChild(t);
            }
        }
        return tree;
    }
}

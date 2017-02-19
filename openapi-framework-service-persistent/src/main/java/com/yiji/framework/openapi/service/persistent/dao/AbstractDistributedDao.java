/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.service.persistent.dao;

import com.acooly.core.utils.arithmetic.ketama.KetamaNode;
import com.acooly.core.utils.arithmetic.ketama.KetamaNodeLocator;
import com.google.common.base.Strings;

/**
 * 抽象orderInfoDao
 * <p/>
 * 使用ketama-hash动态定位当前访问操作的表名称
 * <p/>
 * Created by zhangpu on 2015/9/4.
 */
public abstract class AbstractDistributedDao {
    protected static final int NODES = 100;
    protected static final int NODE_COPIES = 160;
    protected static final String DEFAULT_ORIGINAL_TABLE = "api_order_info";
    protected static KetamaNodeLocator ketamaNodeLocator = new KetamaNodeLocator("", NODES, NODE_COPIES, "_");

    protected String getTable(String partnerId) {
        if (Strings.isNullOrEmpty(partnerId)) {
            return DEFAULT_ORIGINAL_TABLE;
        }
        KetamaNode node = ketamaNodeLocator.getPrimary(partnerId);
        return DEFAULT_ORIGINAL_TABLE + node.getName();
    }

    protected String getTable(String partnerId, String originalName) {
        if (Strings.isNullOrEmpty(partnerId)) {
            return originalName;
        }
        KetamaNode node = ketamaNodeLocator.getPrimary(partnerId);
        return originalName + node.getName();
    }

}

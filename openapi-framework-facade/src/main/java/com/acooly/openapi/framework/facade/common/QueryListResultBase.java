/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-11-10 01:33 创建
 */
package com.acooly.openapi.framework.facade.common;


import com.acooly.core.common.facade.ResultBase;

import java.util.List;

/**
 * @author acooly
 */
public class QueryListResultBase<T> extends ResultBase {

    /**
     * 列表对象
     */
    private List<T> entiries;

    public List<T> getEntiries() {
        return entiries;
    }

    public void setEntiries(List<T> entiries) {
        this.entiries = entiries;
    }
}

/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.annotation.OpenApiField;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页响应报文
 *
 * @author zhangpu
 */
public abstract class PageApiResponse<T> extends ApiResponse {

    @NotNull
    @OpenApiField(desc = "总行数", demo = "200", ordinal = 1)
    private long totalRows = 0;

    @NotNull
    @OpenApiField(desc = "总页数", constraint = "总行数/页大小", demo = "10", ordinal = 2)
    private long totalPages = 0;

    @OpenApiField(desc = "页数据", constraint = "当totalRows大于0时，rows不为空", ordinal = 3)
    private List<T> rows = new ArrayList();

    public void append(T t) {
        rows.add(t);
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
